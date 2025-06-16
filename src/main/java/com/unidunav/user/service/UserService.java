package com.unidunav.user.service;

import com.unidunav.profesor.model.Profesor;
import com.unidunav.profesor.repository.ProfesorRepository;
import com.unidunav.student.model.Student;
import com.unidunav.student.repository.StudentRepository;
import com.unidunav.user.dto.CreateUserDTO;
import com.unidunav.user.dto.UserDTO;
import com.unidunav.user.dto.request.UserUpdateMeDTO;
import com.unidunav.user.model.Role;
import com.unidunav.user.model.User;
import com.unidunav.user.repository.RoleRepository;
import com.unidunav.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private ProfesorRepository profesorRepository;
    
    @Autowired
    private RoleRepository roleRepository;




    public boolean deleteUser(Long id, User currentUser) throws AccessDeniedException {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Korisnik nije pronađen."));

        boolean isAdmin = currentUser.getRoles().stream().anyMatch(r -> r.getNaziv().equalsIgnoreCase("ADMIN"));
        boolean isSluzbenik = currentUser.getRoles().stream().anyMatch(r -> r.getNaziv().equalsIgnoreCase("SLUZBENIK"));

        if (isAdmin || (isSluzbenik && user.getRoles().stream().allMatch(r -> r.getNaziv().equalsIgnoreCase("STUDENT")))) {
            user.setDeleted(true);
            userRepository.save(user);
            return true;
        }

        throw new AccessDeniedException("Nemate prava da obrišete ovog korisnika.");
    }


    public boolean updateUser(Long id, UserDTO updatedData, User currentUser) throws AccessDeniedException {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Korisnik nije pronađen."));

        boolean isAdmin = currentUser.getRoles().stream().anyMatch(r -> r.getNaziv().equalsIgnoreCase("ADMIN"));
        boolean isSluzbenik = currentUser.getRoles().stream().anyMatch(r -> r.getNaziv().equalsIgnoreCase("SLUZBENIK"));
        boolean isOwner = currentUser.getId().equals(user.getId());

        if (isAdmin || isOwner || (isSluzbenik && user.getRoles().stream().allMatch(r -> r.getNaziv().equalsIgnoreCase("STUDENT")))) {
            user.setIme(updatedData.getIme());
            user.setPrezime(updatedData.getPrezime());
            user.setAdresa(updatedData.getAdresa());
            user.setJmbg(updatedData.getJmbg());
            
            if (updatedData.getRoles() != null && isAdmin) {
                Set<Role> noveRole = updatedData.getRoles().stream()
                    .map(role -> roleRepository.findById(role.getId())
                        .orElseThrow(() -> new RuntimeException("Rola nije pronađena: " + role.getId())))
                    .collect(Collectors.toSet());

                user.setRoles(noveRole);
                
             // 📌 Sinhronizacija entiteta na osnovu novih rola
                boolean imaRoluStudent = noveRole.stream().anyMatch(r -> r.getNaziv().equalsIgnoreCase("STUDENT"));
                boolean imaRoluProfesor = noveRole.stream().anyMatch(r -> r.getNaziv().equalsIgnoreCase("PROFESOR"));
                boolean imaRoluSluzbenik = noveRole.stream().anyMatch(r -> r.getNaziv().equalsIgnoreCase("SLUZBENIK"));

                // STUDENT
                if (imaRoluStudent && !studentRepository.existsById(user.getId())) {
                    Student s = new Student();
                    s.setUser(user);
                    int godinaUpisa = LocalDate.now().getYear();
                    s.setGodinaUpisa(godinaUpisa);
                    s.setBrojIndeksa("BR-" + godinaUpisa + "-" + user.getId());
//                    s.setBrojIndeksa("BR-" + user.getId()); 
//                    s.setGodinaUpisa(LocalDate.now().getYear());
                    studentRepository.save(s);
                } 
//                else if (!imaRoluStudent && studentRepository.existsById(user.getId())) {
//                    studentRepository.deleteById(user.getId());
//                }

                // PROFESOR
                if (imaRoluProfesor && !profesorRepository.existsById(user.getId())) {
                    Profesor p = new Profesor();
                    p.setUser(user);
                    profesorRepository.save(p);
                } 
//                else if (!imaRoluProfesor && profesorRepository.existsById(user.getId())) {
//                    profesorRepository.deleteById(user.getId());
//                }
                
            }

            
            userRepository.save(user);
            return true;
        }

        throw new AccessDeniedException("Nemate prava da izmenite ovog korisnika.");
    }
    
    public boolean restoreUser(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()) return false;

        User user = optionalUser.get();
        if (!user.isDeleted()) return false; // već aktivan

        user.setDeleted(false);
        userRepository.save(user);
        return true;
    }



    public List<UserDTO> getAll() {
        return userRepository.findAllActive()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    
    public Optional<UserDTO> getById(Long id) {
        return userRepository.findById(id)
                .filter(user -> !user.isDeleted())
                .map(this::mapToDTO);
    }


    
    public void createUserWithRole(CreateUserDTO request) throws AccessDeniedException {
        // 1. Dohvati trenutno ulogovanog korisnika i proveri da li je SLUŽBENIK
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        boolean isSluzbenik = currentUser.getRoles().stream()
                .anyMatch(r -> r.getNaziv().equalsIgnoreCase("SLUZBENIK"));

        // Ako je službenik, proveri da li pokušava da doda nešto što NIJE student
        if (isSluzbenik) {
            boolean sadrziNeStudentRole = request.getRoles().stream()
                    .anyMatch(role -> !role.getNaziv().equalsIgnoreCase("STUDENT"));

            if (sadrziNeStudentRole) {
                throw new AccessDeniedException("Službenik može da dodaje samo studente.");
            }
        }

        // 2. Pronađi sve Role entitete iz baze
        Set<Role> persistedRoles = request.getRoles().stream()
                .map(role -> roleRepository.findByNaziv(role.getNaziv().toUpperCase())
                        .orElseThrow(() -> new RuntimeException("Nepostojeća rola: " + role.getNaziv())))
                .collect(Collectors.toSet());

        // 3. Napravi User entitet
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setIme(request.getIme());
        user.setPrezime(request.getPrezime());
        user.setAdresa(request.getAdresa());
        user.setJmbg(request.getJmbg());
        user.setRoles(persistedRoles);
        user = userRepository.save(user);

        // 4. Dodaj u odgovarajuću tabelu po ulozi
        
        Set<String> specijalneRole = Set.of("STUDENT", "PROFESOR");

        for (Role role : persistedRoles) {
            String roleName = role.getNaziv().toUpperCase();

            if (specijalneRole.contains(roleName)) {
                switch (roleName) {
                    case "STUDENT" -> {
                        Student s = new Student();
                        s.setUser(user);
                        int godinaUpisa = LocalDate.now().getYear();
                        s.setGodinaUpisa(godinaUpisa);
                        s.setBrojIndeksa("BR-" + godinaUpisa + "-" + user.getId());
                        studentRepository.save(s);
                    }
                    case "PROFESOR" -> {
                        Profesor p = new Profesor();
                        p.setUser(user);
                        profesorRepository.save(p);
                    }
                }
            } else {
                System.out.println("Rola '" + roleName + "' nema dodatnu tabelu, samo je dodeljena korisniku.");
            }
        }

        
//        for (Role role : persistedRoles) {
//            switch (role.getNaziv().toUpperCase()) {
//                case "STUDENT" -> {
//                    Student s = new Student();
//                    s.setUser(user);
//                    int godinaUpisa = LocalDate.now().getYear();
//                    s.setGodinaUpisa(godinaUpisa);
//                    s.setBrojIndeksa("BR-" + godinaUpisa + "-" + user.getId());
//                    studentRepository.save(s);
//                }
//                case "PROFESOR" -> {
//                    Profesor p = new Profesor();
//                    p.setUser(user);
//                    profesorRepository.save(p);
//                }
//                case "ADMIN", "SLUZBENIK" -> {
//                }
//                default -> throw new IllegalArgumentException("Nepoznata rola: " + role.getNaziv());
//            }
//        }
        
        
    }

    
    public UserDTO createUser(CreateUserDTO dto) {
        User user = new User();
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRoles(dto.getRoles());
        
        User saved = userRepository.save(user);
        return mapToDTO(saved);
    }
    
    
    
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public User save(User user) {
        return userRepository.save(user);
    }




//    public List<UserDTO> getAll() {
//        return userRepository.findAll()
//                .stream()
//                .map(this::mapToDTO)
//                .collect(Collectors.toList());
//    }
//    public List<UserDTO> getAllUsers() {
//        return userRepository.findAll().stream()
//            .map(this::mapToDTO)
//            .collect(Collectors.toList());
//    }
    
    private UserDTO mapToDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setIme(user.getIme());
        dto.setPrezime(user.getPrezime());
        dto.setAdresa(user.getAdresa());
        dto.setJmbg(user.getJmbg());
        dto.setRoles(user.getRoles());
        dto.setDeleted(user.isDeleted());
        return dto;
    }
    
    public void updateMe(UserUpdateMeDTO dto, User currentUser) {
        User user = userRepository.findById(currentUser.getId())
                .orElseThrow(() -> new RuntimeException("Korisnik nije pronađen."));

        user.setIme(dto.getIme());
        user.setPrezime(dto.getPrezime());

        userRepository.save(user);
    }


    
    public List<UserDTO> getAllAdmin() {
        return userRepository.findAllOrderedByStatusAndName()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    
    public Optional<UserDTO> findByEmail(String email) {
        return userRepository.findByEmail(email)
                             .map(this::mapToDTO);
    }
    
    public boolean changePassword(Long userId, String oldPassword, String newPassword) {
        User user = userRepository.findById(userId).orElse(null);

        if (user == null) return false;

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            return false; // stara lozinka nije tačna
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        return true;
    }

}
