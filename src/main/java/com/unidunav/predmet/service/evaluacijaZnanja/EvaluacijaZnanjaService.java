package com.unidunav.predmet.service.evaluacijaZnanja;

import java.util.List;
import com.unidunav.predmet.dto.EvaluacijaZnanjaCreateDTO;

import com.unidunav.predmet.dto.EvaluacijaZnanjaDTO;
import com.unidunav.predmet.dto.PromenaTipaEvaluacijeDTO;
import com.unidunav.student.dto.StudentPredmetDTO;

public interface EvaluacijaZnanjaService {
    EvaluacijaZnanjaDTO create(EvaluacijaZnanjaDTO dto);
    List<EvaluacijaZnanjaDTO> findAll();
    EvaluacijaZnanjaDTO findById(Long id);
    EvaluacijaZnanjaDTO update(Long id, EvaluacijaZnanjaDTO dto);
    void delete(Long id);
    void kreirajEvaluacijeZaPredmet(EvaluacijaZnanjaCreateDTO dto);
    List<EvaluacijaZnanjaDTO> getEvaluacijeForStudent(Long studentId);
    List<EvaluacijaZnanjaDTO> getEvaluacijeZaPredmetIPredavaca(Long predmetId, Long profesorId);
    void izmeniTipEvaluacije(Long evaluacijaId, Long noviTipId);
    List<EvaluacijaZnanjaDTO> getEvaluacijeZaPredmet(Long predmetId);
    void upisiBodoveIEvaluiraj(Long evaluacijaId, Integer brojBodova);
    List<StudentPredmetDTO> getStudentiSaEvaluacijamaZaPredmet(Long predmetId);
    void promeniTipEvaluacije(PromenaTipaEvaluacijeDTO dto);
}
