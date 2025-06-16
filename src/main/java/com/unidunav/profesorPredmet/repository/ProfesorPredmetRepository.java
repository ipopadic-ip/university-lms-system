package com.unidunav.profesorPredmet.repository;

import com.unidunav.profesorPredmet.model.ProfesorPredmet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProfesorPredmetRepository extends JpaRepository<ProfesorPredmet, Long> {
    List<ProfesorPredmet> findByProfesorId(Long profesorId);
    List<ProfesorPredmet> findByPredmetId(Long predmetId);
    
    List<ProfesorPredmet> findByDeletedFalse();

    List<ProfesorPredmet> findByProfesorIdAndDeletedFalse(Long profesorId);
    List<ProfesorPredmet> findByPredmetIdAndDeletedFalse(Long predmetId);
    
}