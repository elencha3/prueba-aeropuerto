package com.cev.prueba.prueba.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cev.prueba.prueba.domain.Review;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

}
