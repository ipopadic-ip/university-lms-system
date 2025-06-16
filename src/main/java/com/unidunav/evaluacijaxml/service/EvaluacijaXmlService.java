package com.unidunav.evaluacijaxml.service;

import com.unidunav.evaluacijaxml.dto.EvaluacijaXmlDTO;
import com.unidunav.evaluacijaxml.dto.EvaluacijaXmlList;
import com.unidunav.predmet.model.EvaluacijaZnanja;
import com.unidunav.predmet.model.PohadjanjePredmeta;
import com.unidunav.predmet.repository.EvaluacijaZnanjaRepository;
import com.unidunav.predmet.repository.PohadjanjePredmetaRepository;
import jakarta.xml.bind.*;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;

@Service
public class EvaluacijaXmlService {

    private final PohadjanjePredmetaRepository pohadjanjeRepo;
    private final EvaluacijaZnanjaRepository evaluacijaRepo;

    public EvaluacijaXmlService(PohadjanjePredmetaRepository pohadjanjeRepo, EvaluacijaZnanjaRepository evaluacijaRepo) {
        this.pohadjanjeRepo = pohadjanjeRepo;
        this.evaluacijaRepo = evaluacijaRepo;
    }

    public EvaluacijaXmlList parseXml(InputStream is) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(EvaluacijaXmlList.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        return (EvaluacijaXmlList) unmarshaller.unmarshal(is);
    }

    public void sacuvajEvaluacije(EvaluacijaXmlList lista) {
        for (EvaluacijaXmlDTO dto : lista.getEvaluacije()) {
            PohadjanjePredmeta p = pohadjanjeRepo.findById(dto.getPohadjanjeId()).orElse(null);
            EvaluacijaZnanja e = evaluacijaRepo.findById(dto.getEvaluacijaId()).orElse(null);

            if (p != null && e != null) {
                e.setBrojBodova(dto.getBrojBodova());
                evaluacijaRepo.save(e);

                // Nakon izmene bodova, izračunaj novu ukupnu ocenu ako je to tvoja logika
                izracunajOcenuIPostavi(p);
            }
        }
    }

    private void izracunajOcenuIPostavi(PohadjanjePredmeta pohadjanje) {
        List<EvaluacijaZnanja> evaluacije = evaluacijaRepo.findByPohadjanje(pohadjanje);
        int ukupnoBodova = evaluacije.stream().mapToInt(EvaluacijaZnanja::getBrojBodova).sum();

        int novaOcena;
        if (ukupnoBodova >= 91) novaOcena = 10;
        else if (ukupnoBodova >= 81) novaOcena = 9;
        else if (ukupnoBodova >= 71) novaOcena = 8;
        else if (ukupnoBodova >= 61) novaOcena = 7;
        else if (ukupnoBodova >= 51) novaOcena = 6;
        else novaOcena = 5;

        pohadjanje.setOcena(novaOcena);
        pohadjanjeRepo.save(pohadjanje);
    }
}
