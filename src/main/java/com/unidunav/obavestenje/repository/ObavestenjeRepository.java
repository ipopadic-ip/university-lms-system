package com.unidunav.obavestenje.repository;

import com.unidunav.obavestenje.model.Obavestenje;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ObavestenjeRepository extends JpaRepository<Obavestenje, Long> {
	List<Obavestenje> findByAutorId(Long autorId);
}