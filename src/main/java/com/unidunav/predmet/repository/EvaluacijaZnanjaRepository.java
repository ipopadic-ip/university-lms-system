package com.unidunav.predmet.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.unidunav.predmet.model.EvaluacijaZnanja;
import com.unidunav.predmet.model.PohadjanjePredmeta;
@Repository
public interface EvaluacijaZnanjaRepository extends JpaRepository<EvaluacijaZnanja, Long> {
	List<EvaluacijaZnanja> findByPohadjanjeId(Long pohadjanjeId);
	
	List<EvaluacijaZnanja> findByPohadjanje_Predmet_IdAndPohadjanje_Student_Id(Long predmetId, Long studentId);
	
//	@Query("SELECT e FROM EvaluacijaZnanja e WHERE e.pohadjanje = :pohadjanje")
	List<EvaluacijaZnanja> findByPohadjanje(@Param("pohadjanje") PohadjanjePredmeta pohadjanje);
	
	@Query("SELECT e FROM EvaluacijaZnanja e WHERE e.vremePocetka = :vreme AND e.pohadjanje.predmet.id = :predmetId")
	List<EvaluacijaZnanja> findByVremePocetkaAndPredmetId(@Param("vreme") LocalDateTime vreme, @Param("predmetId") Long predmetId);


}
