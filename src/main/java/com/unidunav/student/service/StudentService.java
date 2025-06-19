package com.unidunav.student.service;

import com.unidunav.obavestenje.dto.ObavestenjeStudentuDTO;
import com.unidunav.predmet.dto.PredmetDTO;
import com.unidunav.predmet.dto.StudentIstorijaStudiranjaResponseDTOProfesor;
import com.unidunav.student.dto.StudentDTO;
import com.unidunav.student.dto.StudentPredmetDTO;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public interface StudentService {

    StudentDTO create(StudentDTO dto);
    List<StudentDTO> findAll();
    StudentDTO findById(Long id);
    StudentDTO update(Long id, StudentDTO dto);
    void delete(Long id);
    
    String uploadSlika(Long studentId, MultipartFile slika) throws IOException;
    List<PredmetDTO> getPredmetiKojeStudentSlusa(Long studentId);
    List<ObavestenjeStudentuDTO> getObavestenjaZaStudenta(Long studentId);
    List<StudentDTO> findByBrojIndeksa(String indeks);
    List<StudentDTO> findByBrojIndeksa2(String indeks);
    
    List<StudentDTO> findStudentiZaProfesora(Long profesorId);
    StudentIstorijaStudiranjaResponseDTOProfesor getStudentIstorijaStudiranjaProfesor(Long studentId);
    List<StudentPredmetDTO> findStudentiZaPredmet(Long predmetId);
}
