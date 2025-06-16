package com.unidunav.profesor.service;

import com.unidunav.predmet.dto.PredmetDTO;
import com.unidunav.predmet.model.Predmet;
import com.unidunav.profesor.dto.ProfesorDTO;
import com.unidunav.profesor.model.Profesor;
import com.unidunav.profesor.repository.ProfesorRepository;
import com.unidunav.profesorPredmet.model.ProfesorPredmet;
import com.unidunav.profesorPredmet.repository.ProfesorPredmetRepository;
import com.unidunav.user.model.User;
import com.unidunav.user.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProfesorServiceImpl implements ProfesorService {

    @Autowired
    private ProfesorRepository repository;
    
    @Autowired
    private ProfesorPredmetRepository profesorPredmetRepository;
    
    @Autowired
    private UserRepository userRepository;


    public ProfesorDTO toDTO(Profesor profesor) {
        ProfesorDTO dto = new ProfesorDTO();
        dto.setId(profesor.getId());
        
        if (profesor.getUser() != null) {
            dto.setIme(profesor.getUser().getIme());
            dto.setPrezime(profesor.getUser().getPrezime());
        }

//        dto.setIme(profesor.getIme());
//        dto.setPrezime(profesor.getPrezime());
        dto.setBiografija(profesor.getBiografija());
     // U toDTO
        dto.setSlikaPath(profesor.getSlikaPath());
        return dto;
    }

    public Profesor toEntity(ProfesorDTO dto) {
        Profesor profesor = new Profesor();
        profesor.setId(dto.getId());
//        profesor.setIme(dto.getIme());
//        profesor.setPrezime(dto.getPrezime());
        profesor.setBiografija(dto.getBiografija());
     // U toEntity
        profesor.setSlikaPath(dto.getSlikaPath());
        return profesor;
    }

    @Override
    public ProfesorDTO create(ProfesorDTO dto) {
        return toDTO(repository.save(toEntity(dto)));
    }

    @Override
    public List<ProfesorDTO> findAll() {
        return repository.findByUserDeletedFalse()
                         .stream()
                         .map(this::toDTO)
                         .collect(Collectors.toList());
    }

//    @Override
//    public List<ProfesorDTO> findAll() {
//        return repository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
//    }

    @Override
    public ProfesorDTO findById(Long id) {
        return repository.findById(id).map(this::toDTO).orElse(null);
    }

    @Override
    public ProfesorDTO update(Long id, ProfesorDTO dto) {
        return repository.findById(id).map(existing -> {
//            existing.setIme(dto.getIme());
//            existing.setPrezime(dto.getPrezime());
            existing.setBiografija(dto.getBiografija());
            return toDTO(repository.save(existing));
        }).orElse(null);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }
    
    @Override
    public String uploadSlika(Long profesorId, MultipartFile slika) throws IOException {
        Profesor profesor = repository.findById(profesorId)
                .orElseThrow(() -> new RuntimeException("Profesor nije pronađen"));

        String folder = "uploads/profesori/";
        File dir = new File(folder);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        String originalFilename = slika.getOriginalFilename();
        String filename = "profesor_" + profesorId + "_" + System.currentTimeMillis() + "_" + originalFilename;
        Path filePath = Paths.get(folder + filename);

        Files.write(filePath, slika.getBytes());

        profesor.setSlikaPath(filePath.toString());
        repository.save(profesor);

        return filePath.toString();
    }
    
    @Override
    public List<PredmetDTO> findPredmetiByProfesorId(Long profesorId) {
        List<ProfesorPredmet> veze = profesorPredmetRepository.findByProfesorId(profesorId);
        return veze.stream()
            .map(veza -> {
                Predmet predmet = veza.getPredmet();
                return new PredmetDTO(
                    predmet.getId(),
                    predmet.getNaziv(),
                    predmet.getEcts(),
                    predmet.getInformacijeOPredmetu(),
                    predmet.getGodinaStudija().getId()
                );
            })
            .collect(Collectors.toList());
    }
    
    @Override
    public ProfesorDTO getProfilByUserId(Long userId) {
        Profesor profesor = repository.findByUserId(userId).orElseThrow(() -> new RuntimeException("Profesor nije pronađen"));
        return toDTO(profesor);
    }

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Override
    public ProfesorDTO izmeniProfil(Long userId, String ime, String prezime,
                                    String staraLozinka, String novaLozinka,
                                    String biografija, MultipartFile slika) {
        Profesor profesor = repository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Profesor nije pronađen"));

        User user = profesor.getUser();
        user.setIme(ime);
        user.setPrezime(prezime);

        if (staraLozinka != null && novaLozinka != null && !novaLozinka.isEmpty()) {
            if (!passwordEncoder.matches(staraLozinka, user.getPassword())) {
                throw new RuntimeException("Stara lozinka nije tačna.");
            }
            user.setPassword(passwordEncoder.encode(novaLozinka));
        }
        userRepository.save(user);

        profesor.setBiografija(biografija);

        if (slika != null && !slika.isEmpty()) {
            try {
                String putanja = uploadSlika(profesor.getId(), slika);
                profesor.setSlikaPath(putanja);
            } catch (IOException e) {
                throw new RuntimeException("Greška pri snimanju slike: " + e.getMessage(), e);
            }
        }

        repository.save(profesor);
        return toDTO(profesor);
    }

}
