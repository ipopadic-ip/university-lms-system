package com.unidunav.profesorPredmet.service;

import com.unidunav.profesorPredmet.dto.TerminNastaveDTO;
import com.unidunav.predmet.repository.PredmetRepository;
import com.unidunav.profesorPredmet.model.Ishod;
import com.unidunav.profesorPredmet.repository.IshodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import com.unidunav.profesor.repository.ProfesorRepository;
import com.unidunav.profesorPredmet.dto.TerminNastaveResponseDTO;
import com.unidunav.profesorPredmet.model.ProfesorPredmet;
import com.unidunav.profesorPredmet.model.TerminNastave;
import com.unidunav.profesorPredmet.repository.ProfesorPredmetRepository;
import com.unidunav.profesorPredmet.repository.TerminNastaveRepository;
import com.unidunav.user.model.User;
import com.unidunav.user.repository.UserRepository;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TerminNastaveService {

    private final TerminNastaveRepository terminRepo;
    private final ProfesorPredmetRepository profesorPredmetRepo;
    private final UserRepository userRepo;

    public TerminNastaveService(TerminNastaveRepository terminRepo, ProfesorPredmetRepository profesorPredmetRepo, UserRepository userRepo) {
        this.terminRepo = terminRepo;
        this.profesorPredmetRepo = profesorPredmetRepo;
        this.userRepo = userRepo;
    }
    
    public List<TerminNastaveDTO> findAllByProfesor(Long profesorId) {
        return terminRepo.findByProfesorPredmetProfesorId(profesorId)
            .stream()
            .map(this::mapToDTO)
            .collect(Collectors.toList());
    }

    public TerminNastaveDTO create(TerminNastaveDTO dto) {
    	 TerminNastave termin = new TerminNastave();
         termin.setTerminPocetka(dto.getTerminPocetka());
         termin.setTerminZavrsetka(dto.getTerminZavrsetka());
         termin.setIshod(dto.getIshod());
         ProfesorPredmet profPred = profesorPredmetRepo.findById(dto.getProfesorPredmetId())
                 .orElseThrow(() -> new RuntimeException("Veza profesor-predmet nije pronađena"));
             termin.setProfesorPredmet(profPred);
             termin = terminRepo.save(termin);
             return mapToDTO(termin);
    }
    
    public TerminNastaveDTO azuriraj(Long id, TerminNastaveDTO dto) {
        TerminNastave termin = terminRepo.findById(id)
            .orElseThrow(() -> new RuntimeException("Termin nije pronađen"));
        termin.setTerminPocetka(dto.getTerminPocetka());
        termin.setTerminZavrsetka(dto.getTerminZavrsetka());
        termin.setIshod(dto.getIshod());

        ProfesorPredmet profPred = profesorPredmetRepo.findById(dto.getProfesorPredmetId())
            .orElseThrow(() -> new RuntimeException("Veza profesor-predmet nije pronađena"));
        termin.setProfesorPredmet(profPred);

        termin = terminRepo.save(termin);
        return mapToDTO(termin);
    }
    
    public void obrisi(Long id) {
    	 if (!terminRepo.existsById(id)) {
             throw new RuntimeException("Termin ne postoji");
         }
         terminRepo.deleteById(id);
    }
    
    private TerminNastaveDTO mapToDTO(TerminNastave t) {
        TerminNastaveDTO dto = new TerminNastaveDTO();
        dto.setId(t.getId());
        dto.setTerminPocetka(t.getTerminPocetka());
        dto.setTerminZavrsetka(t.getTerminZavrsetka());
        dto.setProfesorPredmetId(t.getProfesorPredmet().getId());
        dto.setIshod(t.getIshod());
        return dto;
    }
    
    private TerminNastaveResponseDTO toResponseDTO(TerminNastave termin) {
        TerminNastaveResponseDTO dto = new TerminNastaveResponseDTO();
        dto.setId(termin.getId());
        dto.setTerminPocetka(termin.getTerminPocetka());
        dto.setTerminZavrsetka(termin.getTerminZavrsetka());

        if (termin.getProfesorPredmet() != null && termin.getProfesorPredmet().getPredmet() != null) {
            dto.setNazivPredmeta(termin.getProfesorPredmet().getPredmet().getNaziv());
        } else {
            dto.setNazivPredmeta("Nepoznat predmet");
        }

        dto.setIshodTema(termin.getIshod());;

        return dto;
    }
    
    public List<TerminNastaveResponseDTO> sviZaProfesorPredmet(Long id) {
        return terminRepo.findByProfesorPredmetId(id).stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }
    
    public void azurirajIshod(Long id, String noviIshod) {
        TerminNastave termin = terminRepo.findById(id)
            .orElseThrow(() -> new RuntimeException("Termin nije pronađen"));
        termin.setIshod(noviIshod);
        terminRepo.save(termin);
    }

//    public TerminNastaveResponseDTO kreiraj(TerminNastaveDTO dto) {
//        String email = SecurityContextHolder.getContext().getAuthentication().getName();
//        User autor = userRepo.findByEmail(email).orElseThrow(() -> new RuntimeException("Korisnik nije pronađen"));
//
//        ProfesorPredmet pp = profesorPredmetRepo.findById(dto.getProfesorPredmetId())
//                .orElseThrow(() -> new RuntimeException("ProfesorPredmet nije pronađen"));
//
//        TerminNastave termin = new TerminNastave();
//        termin.setTerminPocetka(dto.getTerminPocetka());
//        termin.setTerminZavrsetka(dto.getTerminZavrsetka());
//        termin.setProfesorPredmet(pp);
//        termin.setAutor(autor);
//
//        TerminNastave sacuvan = repo.save(termin);
//
//        return mapToDTO(sacuvan);
//    }
    
//    public List<TerminNastaveResponseDTO> sviZaProfesorPredmet(Long id) {
//        return terminRepo.findByProfesorPredmetId(id).stream()
//                .map(this::mapToDTO)
//                .collect(Collectors.toList());
//    }

//    public TerminNastaveResponseDTO azuriraj(Long id, TerminNastaveDTO dto) {
//        TerminNastave termin = repo.findById(id).orElseThrow(() -> new RuntimeException("Termin nije pronađen"));
//        termin.setTerminPocetka(dto.getTerminPocetka());
//        termin.setTerminZavrsetka(dto.getTerminZavrsetka());
//        return mapToDTO(repo.save(termin));
//    }

//    public void obrisi(Long id) {
//        repo.deleteById(id);
//    }

//    private TerminNastaveResponseDTO mapToDTO(TerminNastave termin) {
//        TerminNastaveResponseDTO dto = new TerminNastaveResponseDTO();
//        dto.setId(termin.getId());
//        dto.setTerminPocetka(termin.getTerminPocetka());
//        dto.setTerminZavrsetka(termin.getTerminZavrsetka());
//        dto.setProfesorPredmetId(termin.getProfesorPredmet().getId());
//        dto.setAutorIme(termin.getAutor().getIme() + " " + termin.getAutor().getPrezime());
//        return dto;
//    }
}
