package com.unidunav.predmet.service;

import java.util.List;

import com.unidunav.predmet.dto.PredmetDTO;
import com.unidunav.predmet.model.Predmet;

public interface PredmetService {

    PredmetDTO create(PredmetDTO dto);
    List<PredmetDTO> findAll();
    PredmetDTO findById(Long id);
    PredmetDTO update(Long id, PredmetDTO dto);
    void delete(Long id);
	List<PredmetDTO> findAllAktivni();
}

