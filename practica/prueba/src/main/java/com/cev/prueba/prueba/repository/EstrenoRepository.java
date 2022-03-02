package com.cev.prueba.prueba.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cev.prueba.prueba.domain.Estreno;

@Repository
public interface EstrenoRepository extends JpaRepository<Estreno, Long> {

}
