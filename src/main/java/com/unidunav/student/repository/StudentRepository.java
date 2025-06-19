package com.unidunav.student.repository;

import com.unidunav.student.model.Student;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StudentRepository extends JpaRepository<Student, Long> {
//	List<Student> findByBrojIndeksaContainingIgnoreCase(String indeks);
	
	@Query("SELECT s FROM Student s JOIN s.user u WHERE LOWER(s.brojIndeksa) LIKE LOWER(CONCAT('%', :indeks, '%')) AND u.deleted = false")
	List<Student> findByBrojIndeksaContainingIgnoreCaseAndUserNotDeleted(@Param("indeks") String indeks);

	
	@Query("SELECT s FROM Student s JOIN FETCH s.user u WHERE u.deleted = false")
	List<Student> findAllWithUser();
	
	@Query("SELECT s FROM Student s JOIN s.user u WHERE LOWER(s.brojIndeksa) = LOWER(:indeks) AND u.deleted = false")
	List<Student> findByBrojIndeksaAndUserNotDeleted(@Param("indeks") String indeks);

}