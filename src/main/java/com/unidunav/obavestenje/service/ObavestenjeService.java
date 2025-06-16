package com.unidunav.obavestenje.service;

import com.unidunav.obavestenje.dto.ObavestenjeDTO;
import org.springframework.beans.factory.annotation.Autowired;

import com.unidunav.obavestenje.dto.ObavestenjeResponseDTO;
import com.unidunav.obavestenje.model.Obavestenje;
import com.unidunav.obavestenje.repository.ObavestenjeRepository;
import com.unidunav.predmet.model.PohadjanjePredmeta;
import com.unidunav.predmet.model.Predmet;
import com.unidunav.predmet.repository.PohadjanjePredmetaRepository;
import com.unidunav.predmet.repository.PredmetRepository;
import com.unidunav.user.model.User;
import com.unidunav.user.repository.UserRepository;
import java.util.stream.Collectors;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ObavestenjeService {

    private final ObavestenjeRepository obavestenjeRepo;
    private final PredmetRepository predmetRepo;
    private final UserRepository userRepo;

    public ObavestenjeService(ObavestenjeRepository obavestenjeRepo, PredmetRepository predmetRepo, UserRepository userRepo) {
        this.obavestenjeRepo = obavestenjeRepo;
        this.predmetRepo = predmetRepo;
        this.userRepo = userRepo;
    }
    
    public ObavestenjeResponseDTO kreirajObavestenje(ObavestenjeDTO dto) {
        // Dobavljanje ulogovanog korisnika
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User autor = userRepo.findByEmail(username)  // ili findByJmbg(username)
            .orElseThrow(() -> new RuntimeException("Ulogovani korisnik nije pronađen"));

        // Pronalaženje predmeta
        Predmet predmet = predmetRepo.findById(dto.getPredmetId())
            .orElseThrow(() -> new RuntimeException("Predmet nije pronađen"));

     // Ako datum nije prosleđen – automatski ga postavljamo
        LocalDate datumZaUpis = dto.getDatum() != null ? dto.getDatum() : LocalDate.now();
        
        // Kreiranje i čuvanje obaveštenja
        Obavestenje obavestenje = new Obavestenje(dto.getTekst(), datumZaUpis, predmet, autor);
        Obavestenje sacuvano = obavestenjeRepo.save(obavestenje);

        // Mapiranje u DTO
        ObavestenjeResponseDTO response = new ObavestenjeResponseDTO();
        response.setId(sacuvano.getId());
        response.setTekst(sacuvano.getTekst());
        response.setDatum(sacuvano.getDatum());
        response.setPredmetId(predmet.getId());
        response.setPredmetNaziv(predmet.getNaziv());
        response.setAutorId(autor.getId());
        response.setAutorIme(autor.getIme() + " " + autor.getPrezime());

        return response;
    }
    
    public ObavestenjeResponseDTO izmeniObavestenje(Long id, ObavestenjeDTO dto) {
        Obavestenje obavestenje = obavestenjeRepo.findById(id)
            .orElseThrow(() -> new RuntimeException("Obaveštenje nije pronađeno"));

        // Provera autora (ulogovanog korisnika)
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User ulogovani = userRepo.findByEmail(username)
            .orElseThrow(() -> new RuntimeException("Ulogovani korisnik nije pronađen"));

        if (!obavestenje.getAutor().getId().equals(ulogovani.getId())) {
            throw new RuntimeException("Nemate dozvolu da izmenite ovo obaveštenje");
        }

        obavestenje.setTekst(dto.getTekst());
        obavestenje.setDatum(LocalDate.now());

        // Po potrebi možeš dozvoliti i promenu predmeta:
        if (!obavestenje.getPredmet().getId().equals(dto.getPredmetId())) {
            Predmet noviPredmet = predmetRepo.findById(dto.getPredmetId())
                .orElseThrow(() -> new RuntimeException("Predmet nije pronađen"));
            obavestenje.setPredmet(noviPredmet);
        }

        Obavestenje sacuvano = obavestenjeRepo.save(obavestenje);

        return new ObavestenjeResponseDTO(
            sacuvano.getId(),
            sacuvano.getTekst(),
            sacuvano.getDatum(),
            sacuvano.getPredmet().getId(),
            sacuvano.getPredmet().getNaziv(),
            sacuvano.getAutor().getId(),
            sacuvano.getAutor().getIme() + " " + sacuvano.getAutor().getPrezime()
        );
    }

    public void obrisiObavestenje(Long id) {
        Obavestenje obavestenje = obavestenjeRepo.findById(id)
            .orElseThrow(() -> new RuntimeException("Obaveštenje nije pronađeno"));

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User ulogovani = userRepo.findByEmail(username)
            .orElseThrow(() -> new RuntimeException("Ulogovani korisnik nije pronađen"));

        if (!obavestenje.getAutor().getId().equals(ulogovani.getId())) {
            throw new RuntimeException("Nemate dozvolu da obrišete ovo obaveštenje");
        }

        obavestenjeRepo.delete(obavestenje);
    }
    
    public List<ObavestenjeResponseDTO> findObavestenjaZaProfesora() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User profesor = userRepo.findByEmail(username)
            .orElseThrow(() -> new RuntimeException("Ulogovani profesor nije pronađen"));

        return obavestenjeRepo.findByAutorId(profesor.getId()).stream()
            .map(obavestenje -> new ObavestenjeResponseDTO(
                obavestenje.getId(),
                obavestenje.getTekst(),
                obavestenje.getDatum(),
                obavestenje.getPredmet().getId(),
                obavestenje.getPredmet().getNaziv(),
                obavestenje.getAutor().getId(),
                obavestenje.getAutor().getIme() + " " + obavestenje.getAutor().getPrezime()
            ))
            .collect(Collectors.toList());
    }


    @Autowired
    private PohadjanjePredmetaRepository pohadjanjePredmetaRepository;

    @Autowired
    private ObavestenjeRepository obavestenjeRepository;

    public List<ObavestenjeResponseDTO> findObavestenjaZaStudenta(Long studentId) {
        List<PohadjanjePredmeta> pohadjanja = pohadjanjePredmetaRepository.findByStudentId(studentId);

        return pohadjanja.stream()
                .filter(PohadjanjePredmeta::isAktivan)
                .flatMap(p -> p.getPredmet().getObavestenja().stream())
                .map(obavestenje -> new ObavestenjeResponseDTO(
                        obavestenje.getId(),
                        obavestenje.getTekst(),
                        obavestenje.getDatum(),
                        obavestenje.getPredmet().getId(),
                        obavestenje.getPredmet().getNaziv(),
                        obavestenje.getAutor().getId(),
                        obavestenje.getAutor().getIme() + " " + obavestenje.getAutor().getPrezime()
                ))
                .collect(Collectors.toList());
    }
    
    public List<ObavestenjeResponseDTO> svaObavestenjaDTO() {
        return obavestenjeRepo.findAll().stream()
            .map(obavestenje -> new ObavestenjeResponseDTO(
                obavestenje.getId(),
                obavestenje.getTekst(),
                obavestenje.getDatum(),
                obavestenje.getPredmet().getId(),
                obavestenje.getPredmet().getNaziv(),
                obavestenje.getAutor().getId(),
                obavestenje.getAutor().getIme() + " " + obavestenje.getAutor().getPrezime()
            )).collect(Collectors.toList());
    }

    public List<Obavestenje> svaObavestenja() {
        return obavestenjeRepo.findAll();
    }

    public Obavestenje nadjiPoId(Long id) {
        return obavestenjeRepo.findById(id).orElseThrow(() -> new RuntimeException("Obaveštenje nije pronađeno"));
    }
}

