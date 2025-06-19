package com.unidunav.obavestenje.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.unidunav.obavestenje.model.OpsteObavestenje;

public interface OpsteObavestenjeRepository extends JpaRepository<OpsteObavestenje, Long> {
	List<OpsteObavestenje> findByAktivanTrue();
}
