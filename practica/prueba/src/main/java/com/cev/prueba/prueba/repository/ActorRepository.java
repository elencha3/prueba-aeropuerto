package com.cev.prueba.prueba.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cev.prueba.prueba.domain.Actor;

@Repository
public interface ActorRepository extends JpaRepository<Actor, Long> {

}
