package com.unidunav.obavestenje.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.unidunav.obavestenje.dto.OpsteObavestenjeDTO;
import com.unidunav.obavestenje.model.OpsteObavestenje;
import com.unidunav.obavestenje.repository.OpsteObavestenjeRepository;
import com.unidunav.user.model.User;

@Service
public class OpsteObavestenjeService {

    @Autowired
    private OpsteObavestenjeRepository repository;

    public List<OpsteObavestenje> findAll() {
        return repository.findByAktivanTrue();
    }


    public OpsteObavestenje save(OpsteObavestenje obavestenje) {
        return repository.save(obavestenje);
    }

    public void delete(Long id) {
        repository.findById(id).ifPresent(o -> {
            o.setAktivan(false);
            repository.save(o);
        });
    }
    public OpsteObavestenje findById(Long id) {
        return repository.findById(id).orElse(null);
    }
    public OpsteObavestenje create(OpsteObavestenjeDTO dto, User autor) {
        OpsteObavestenje o = new OpsteObavestenje();
        o.setNaslov(dto.getNaslov());
        o.setTekst(dto.getTekst());
        o.setDatum(LocalDate.now());
        o.setAutor(autor);
        return repository.save(o);
    }
    
}