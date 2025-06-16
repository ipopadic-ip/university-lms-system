package com.unidunav.predmet.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.unidunav.predmet.model.Predmet;

@Repository
public interface PredmetRepository extends JpaRepository<Predmet, Long> {
	List<Predmet> findByGodinaStudijaId(Long godinaStudijaId);
	
    List<Predmet> findByDeletedFalse(); // za aktivne predmete
    
    List<Predmet> findByGodinaStudijaIdAndDeletedFalse(Long godinaStudijaId);

    List<Predmet> findByProfesoriProfesorId(Long profesorId);
}
