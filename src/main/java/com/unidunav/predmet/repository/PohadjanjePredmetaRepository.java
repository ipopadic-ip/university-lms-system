package com.unidunav.predmet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.unidunav.predmet.model.PohadjanjePredmeta;
import org.springframework.stereotype.Repository;

@Repository
public interface PohadjanjePredmetaRepository extends JpaRepository<PohadjanjePredmeta, Long> {
	@Query("SELECT pp FROM PohadjanjePredmeta pp JOIN FETCH pp.predmet WHERE pp.student.id = :studentId")
	List<PohadjanjePredmeta> findByStudentId(@Param("studentId") Long studentId);
	
	@Query("SELECT p FROM PohadjanjePredmeta p WHERE p.predmet.id = :predmetId")
	List<PohadjanjePredmeta> findByPredmetId(Long predmetId);
	
	@Query("SELECT pp FROM PohadjanjePredmeta pp " +
		       "WHERE pp.predmet.id IN (" +
		       "SELECT p.predmet.id FROM ProfesorPredmet p WHERE p.profesor.id = :profesorId)")
		List<PohadjanjePredmeta> findByProfesorId(@Param("profesorId") Long profesorId);
}
