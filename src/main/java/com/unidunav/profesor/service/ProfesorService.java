package com.unidunav.profesor.service;

import com.unidunav.predmet.dto.PredmetDTO;
import com.unidunav.profesor.dto.ProfesorDTO;

import java.io.IOException;
import com.unidunav.profesor.model.Profesor;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public interface ProfesorService {

    ProfesorDTO create(ProfesorDTO dto);
    List<ProfesorDTO> findAll();
    ProfesorDTO findById(Long id);
    ProfesorDTO update(Long id, ProfesorDTO dto);
    void delete(Long id);
    
    ProfesorDTO toDTO(Profesor entity);
    Profesor toEntity(ProfesorDTO dto);
    
    String uploadSlika(Long profesorId, MultipartFile slika) throws IOException;
    
    List<PredmetDTO> findPredmetiByProfesorId(Long profesorId);
    
    ProfesorDTO getProfilByUserId(Long userId);
    ProfesorDTO izmeniProfil(Long userId, String ime, String prezime, String staraLozinka, String novaLozinka, String biografija, MultipartFile slika);

    
}
