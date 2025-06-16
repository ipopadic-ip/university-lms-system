package com.unidunav.predmet.service.evaluacijaZnanja;

import com.unidunav.predmet.dto.EvaluacijaZnanjaCreateDTO;
import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.unidunav.predmet.dto.EvaluacijaZnanjaDTO;
import com.unidunav.predmet.dto.PromenaTipaEvaluacijeDTO;
import com.unidunav.predmet.model.EvaluacijaZnanja;
import com.unidunav.predmet.model.PohadjanjePredmeta;
import com.unidunav.predmet.model.TipEvaluacije;
import com.unidunav.predmet.repository.EvaluacijaZnanjaRepository;
import com.unidunav.predmet.repository.PohadjanjePredmetaRepository;
import com.unidunav.predmet.repository.TipEvaluacijeRepository;
import com.unidunav.profesorPredmet.repository.ProfesorPredmetRepository;
import com.unidunav.student.dto.StudentPredmetDTO;
import com.unidunav.student.model.Student;

import java.util.List;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Service
public class EvaluacijaZnanjaServiceImpl implements EvaluacijaZnanjaService {

    @Autowired private EvaluacijaZnanjaRepository repository;
    @Autowired private TipEvaluacijeRepository tipRepo;
    @Autowired private PohadjanjePredmetaRepository pohRepo;

    private EvaluacijaZnanjaDTO toDTO(EvaluacijaZnanja ez) {
        EvaluacijaZnanjaDTO dto = new EvaluacijaZnanjaDTO();
        dto.setId(ez.getId());
        dto.setVremePocetka(ez.getVremePocetka());
        dto.setBrojBodova(ez.getBrojBodova() != null ? ez.getBrojBodova() : 0);
//        dto.setBrojBodova(ez.getBrojBodova());
        dto.setTipEvaluacijeId(ez.getTipEvaluacije().getId());
        dto.setPohadjanjeId(ez.getPohadjanje().getId());
        if (ez.getTipEvaluacije() != null) {
            dto.setTipEvaluacijeNaziv(ez.getTipEvaluacije().getTip());
        }

        if (ez.getPohadjanje() != null && ez.getPohadjanje().getPredmet() != null) {
            dto.setPredmetNaziv(ez.getPohadjanje().getPredmet().getNaziv());
        }
        return dto;
    }

    private EvaluacijaZnanja toEntity(EvaluacijaZnanjaDTO dto) {
        TipEvaluacije tip = tipRepo.findById(dto.getTipEvaluacijeId()).orElseThrow();
        PohadjanjePredmeta poh = pohRepo.findById(dto.getPohadjanjeId()).orElseThrow();

        EvaluacijaZnanja ez = new EvaluacijaZnanja();
        ez.setId(dto.getId());
        ez.setVremePocetka(dto.getVremePocetka());
        ez.setBrojBodova(dto.getBrojBodova());
        ez.setTipEvaluacije(tip);
        ez.setPohadjanje(poh);
        return ez;
    }

    @Override
    public EvaluacijaZnanjaDTO create(EvaluacijaZnanjaDTO dto) {
        return toDTO(repository.save(toEntity(dto)));
    }

    @Override
    public List<EvaluacijaZnanjaDTO> findAll() {
        return repository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public EvaluacijaZnanjaDTO findById(Long id) {
        return repository.findById(id).map(this::toDTO).orElse(null);
    }

    @Override
    public EvaluacijaZnanjaDTO update(Long id, EvaluacijaZnanjaDTO dto) {
        return repository.findById(id).map(existing -> {
            existing.setVremePocetka(dto.getVremePocetka());
            existing.setBrojBodova(dto.getBrojBodova());
            existing.setTipEvaluacije(tipRepo.findById(dto.getTipEvaluacijeId()).orElseThrow());
            existing.setPohadjanje(pohRepo.findById(dto.getPohadjanjeId()).orElseThrow());
            return toDTO(repository.save(existing));
        }).orElse(null);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }
    
    @Override
    public List<EvaluacijaZnanjaDTO> getEvaluacijeForStudent(Long studentId) {
        List<PohadjanjePredmeta> pohadjanja = pohRepo.findByStudentId(studentId);
        List<EvaluacijaZnanja> sveEvaluacije = repository.findAll();

        return sveEvaluacije.stream()
        		.filter(e -> pohadjanja.stream().anyMatch(p -> e.getPohadjanje().getId().equals(p.getId())))
            .map(this::toDTO)
            .collect(Collectors.toList());
    }
    
    @Override
    @Transactional
    public void kreirajEvaluacijeZaPredmet(EvaluacijaZnanjaCreateDTO dto) {
        List<PohadjanjePredmeta> pohadjanja = pohRepo.findByPredmetId(dto.getPredmetId());

        TipEvaluacije tip = tipRepo.findById(dto.getTipEvaluacijeId())
        	    .orElseThrow(() -> new RuntimeException("Nepostojeći tip evaluacije"));

        for (PohadjanjePredmeta poh : pohadjanja) {
            EvaluacijaZnanja evaluacija = new EvaluacijaZnanja();
            evaluacija.setVremePocetka(dto.getVremePocetka());
            evaluacija.setTipEvaluacije(tip);
            evaluacija.setPohadjanje(poh);
            evaluacija.setBrojBodova(null); // još se ne zna
            repository.save(evaluacija);
        }
    }
    
    @Autowired private ProfesorPredmetRepository profesorPredmetRepo;

    
    @Override
    public List<EvaluacijaZnanjaDTO> getEvaluacijeZaPredmetIPredavaca(Long predmetId, Long profesorId) {
        List<Long> predavaciIds = profesorPredmetRepo.findByPredmetIdAndDeletedFalse(predmetId)
            .stream()
            .map(pp -> pp.getProfesor().getId())
            .toList();

        if (!predavaciIds.contains(profesorId)) {
            return List.of(); // ili baci izuzetak
        }

        return repository.findAll().stream()
            .filter(e -> e.getPohadjanje() != null &&
                         e.getPohadjanje().getPredmet().getId().equals(predmetId))
            .map(this::toDTO)
            .collect(Collectors.toList());
    }


    @Override
    @Transactional
    public void izmeniTipEvaluacije(Long evaluacijaId, Long noviTipId) {
        System.out.println("POKRENUTO: izmeniTipEvaluacije");
        System.out.println("Evaluacija ID: " + evaluacijaId);
        System.out.println("Novi tip ID: " + noviTipId);

        EvaluacijaZnanja ez = repository.findById(evaluacijaId)
            .orElseThrow(() -> {
                System.out.println("GRESKA: Evaluacija nije pronađena");
                return new RuntimeException("Evaluacija nije pronađena");
            });

        TipEvaluacije noviTip = tipRepo.findById(noviTipId)
            .orElseThrow(() -> {
                System.out.println("GRESKA: Tip evaluacije nije pronađen");
                return new RuntimeException("Tip evaluacije nije pronađen");
            });

        System.out.println("Stari tip: " + ez.getTipEvaluacije().getId());
        System.out.println("Novi tip: " + noviTip.getId());

        ez.setTipEvaluacije(noviTip);
        repository.save(ez);

        System.out.println("Uspešno sačuvano.");
    }

    
    @Override
    public List<EvaluacijaZnanjaDTO> getEvaluacijeZaPredmet(Long predmetId) {
        return repository.findAll().stream()
            .filter(e -> e.getPohadjanje() != null &&
                         e.getPohadjanje().getPredmet().getId().equals(predmetId))
            .map(this::toDTO)
            .collect(Collectors.toList());
    }
    
    @Override
    @Transactional
    public void upisiBodoveIEvaluiraj(Long evaluacijaId, Integer brojBodova) {
        EvaluacijaZnanja evaluacija = repository.findById(evaluacijaId)
            .orElseThrow(() -> new RuntimeException("Evaluacija nije pronađena"));

        PohadjanjePredmeta pohadjanje = evaluacija.getPohadjanje();

        // Upis bodova u evaluaciju
        evaluacija.setBrojBodova(brojBodova);
        repository.save(evaluacija);

        // Saberi sve bodove tog studenta za isti predmet
        Long predmetId = pohadjanje.getPredmet().getId();
        Long studentId = pohadjanje.getStudent().getId();

        List<EvaluacijaZnanja> sveEvaluacije = repository.findByPohadjanje_Predmet_IdAndPohadjanje_Student_Id(predmetId, studentId);

        int ukupnoBodova = sveEvaluacije.stream()
            .filter(e -> e.getBrojBodova() != null)
            .mapToInt(EvaluacijaZnanja::getBrojBodova)
            .sum();

        // Odredi ocenu
        int ocena;
        if (ukupnoBodova < 51) ocena = 5;
        else if (ukupnoBodova <= 60) ocena = 6;
        else if (ukupnoBodova <= 70) ocena = 7;
        else if (ukupnoBodova <= 80) ocena = 8;
        else if (ukupnoBodova <= 90) ocena = 9;
        else ocena = 10;

        // Upisi ocenu u pohadjanje_predmeta
        pohadjanje.setOcena(ocena);
        pohRepo.save(pohadjanje);
    }

    @Override
    public List<StudentPredmetDTO> getStudentiSaEvaluacijamaZaPredmet(Long predmetId) {
        List<PohadjanjePredmeta> pohadjanja = pohRepo.findByPredmetId(predmetId);

        Map<Student, List<EvaluacijaZnanja>> mapirano = new HashMap<>();

        for (PohadjanjePredmeta p : pohadjanja) {
            List<EvaluacijaZnanja> evaluacije = repository.findByPohadjanje(p);
            if (!evaluacije.isEmpty()) {
                mapirano.computeIfAbsent(p.getStudent(), k -> new ArrayList<>()).addAll(evaluacije);
            }
        }

        return mapirano.entrySet().stream().map(entry -> {
            Student student = entry.getKey();
            List<EvaluacijaZnanja> evaluacije = entry.getValue();

            StudentPredmetDTO dto = new StudentPredmetDTO();
            dto.setId(student.getId());
            dto.setIme(student.getUser().getIme());
            dto.setPrezime(student.getUser().getPrezime());
            dto.setBrojIndeksa(student.getBrojIndeksa());
            dto.setGodinaUpisa(student.getGodinaUpisa());
            dto.setProsecnaOcena(student.getProsecnaOcena());
            dto.setUkupnoEcts(student.getUkupnoEcts());
            dto.setSlikaPath(student.getSlikaPath());
            dto.setZavrsniRad(student.getZavrsniRad());

            // Setovanje predmeta – svi su za isti predmet pa možeš uzeti iz prvog pohadjanja
            dto.setPredmet(pohadjanja.stream()
                .filter(p -> p.getStudent().equals(student))
                .map(p -> p.getPredmet().getNaziv())
                .findFirst().orElse("Nepoznat"));

            List<EvaluacijaZnanjaDTO> evaluacijaDTO = evaluacije.stream().map(ev -> {
                EvaluacijaZnanjaDTO evDTO = new EvaluacijaZnanjaDTO();
                evDTO.setId(ev.getId());
                evDTO.setVremePocetka(ev.getVremePocetka());
                evDTO.setBrojBodova(ev.getBrojBodova());
                evDTO.setTipEvaluacijeId(ev.getTipEvaluacije().getId());
                evDTO.setTipEvaluacijeNaziv(ev.getTipEvaluacije().getTip());
                evDTO.setPohadjanjeId(ev.getPohadjanje().getId());
                evDTO.setPredmetNaziv(ev.getPohadjanje().getPredmet().getNaziv());
                return evDTO;
            }).toList();

            dto.setEvaluacije(evaluacijaDTO);

            return dto;
        }).toList();
    }

    @Override
    @Transactional
    public void promeniTipEvaluacije(PromenaTipaEvaluacijeDTO dto) {
        // Dohvati sve evaluacije za isti predmet i isto vreme
        List<EvaluacijaZnanja> evaluacije = repository
            .findByVremePocetkaAndPredmetId(dto.getVremePocetka(), dto.getPredmetId());

        // Dohvati novi tip evaluacije
        TipEvaluacije noviTip = tipRepo.findById(dto.getNoviTipId())
            .orElseThrow(() -> new RuntimeException("Tip evaluacije nije pronađen"));

        // Ažuriraj sve evaluacije sa novim tipom
        for (EvaluacijaZnanja e : evaluacije) {
            e.setTipEvaluacije(noviTip);
        }

        repository.saveAll(evaluacije);
    }



}
