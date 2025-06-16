package com.unidunav.profesor.repository;

import com.unidunav.profesor.model.Profesor;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProfesorRepository extends JpaRepository<Profesor, Long> {
	List<Profesor> findByUserDeletedFalse();
	
	@Query("SELECT p FROM Profesor p WHERE p.user.email = :email")
	Optional<Profesor> findByUser_Email(String email);
	
	Optional<Profesor> findByUserId(Long userId);

}
