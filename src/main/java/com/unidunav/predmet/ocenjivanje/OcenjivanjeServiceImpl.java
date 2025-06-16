package com.unidunav.predmet.ocenjivanje;

import com.unidunav.predmet.model.PohadjanjePredmeta;
import com.unidunav.predmet.model.EvaluacijaZnanja;
import com.unidunav.predmet.model.TipEvaluacije;
import com.unidunav.predmet.repository.PohadjanjePredmetaRepository;
import com.unidunav.predmet.repository.EvaluacijaZnanjaRepository;
import com.unidunav.predmet.repository.TipEvaluacijeRepository;
import com.unidunav.student.model.Student;
import com.unidunav.student.repository.StudentRepository;
import com.unidunav.user.model.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;

@Service
public class OcenjivanjeServiceImpl implements OcenjivanjeService {

    @Autowired
    private PohadjanjePredmetaRepository pohadjanjeRepo;

    @Autowired
    private EvaluacijaZnanjaRepository evaluacijaRepo;

    @Autowired
    private StudentRepository studentRepo;

    @Autowired
    private TipEvaluacijeRepository tipRepo;

    @Override
    public List<StudentEvaluacijaDTO> getEvaluacijeZaPredmet(Long predmetId) {
        List<PohadjanjePredmeta> pohadjanja = pohadjanjeRepo.findByPredmetId(predmetId);

        return pohadjanja.stream()
                .flatMap(pohadjanje -> {
                    List<EvaluacijaZnanja> sveEvaluacije = evaluacijaRepo.findByPohadjanjeId(pohadjanje.getId());

                    // Računanje ukupnog broja bodova
                    int ukupnoBodova = sveEvaluacije.stream()
                        .filter(ev -> ev.getBrojBodova() != null)
                        .mapToInt(EvaluacijaZnanja::getBrojBodova)
                        .sum();

                    return sveEvaluacije.stream().map(e -> {
                        Student student = pohadjanje.getStudent();
                        User user = student.getUser();
                        TipEvaluacije tip = e.getTipEvaluacije();

                        StudentEvaluacijaDTO dto = new StudentEvaluacijaDTO(
                            student.getId(),
                            user.getIme(),
                            user.getPrezime(),
                            student.getBrojIndeksa(),
                            e.getId(),
                            tip != null ? tip.getTip() : "Nepoznat",
                            e.getVremePocetka(),
                            e.getBrojBodova(),
                            pohadjanje.getOcena()
                        );

                        // Postavi ukupne bodove
                        dto.setUkupnoBodova(ukupnoBodova);

                        return dto;
                    });
                })
                .collect(Collectors.toList());
    }


    @Override
    public void azurirajBodove(Long evaluacijaId, int brojBodova) {
        EvaluacijaZnanja evaluacija = evaluacijaRepo.findById(evaluacijaId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Evaluacija nije pronađena"));

        if (evaluacija.getVremePocetka().plusDays(15).isBefore(LocalDateTime.now())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Istekao rok za unos bodova");
        }

        evaluacija.setBrojBodova(brojBodova);
        evaluacijaRepo.save(evaluacija);

        // Ažuriraj ukupnu ocenu
        PohadjanjePredmeta pohadjanje = evaluacija.getPohadjanje();
        List<EvaluacijaZnanja> sveEvaluacije = evaluacijaRepo.findByPohadjanjeId(pohadjanje.getId());

        int ukupanBrojBodova = sveEvaluacije.stream()
            .filter(ev -> ev.getBrojBodova() != null)
            .mapToInt(EvaluacijaZnanja::getBrojBodova)
            .sum();

        if (ukupanBrojBodova < 51) {
            pohadjanje.setOcena(null); // Ne upisuj 5, nego null
        } else {
            int novaOcena = izracunajOcenu(ukupanBrojBodova);
            pohadjanje.setOcena(novaOcena);
        }

        pohadjanjeRepo.save(pohadjanje);
    }

    private int izracunajOcenu(int bodovi) {
        if (bodovi < 51) return 5;
        else if (bodovi < 61) return 6;
        else if (bodovi < 71) return 7;
        else if (bodovi < 81) return 8;
        else if (bodovi < 91) return 9;
        else return 10;
    }
}
