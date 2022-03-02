package com.cev.prueba.prueba.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cev.prueba.prueba.domain.Pelicula;

@Repository
public interface PeliculaRepository extends JpaRepository<Pelicula, Long> {

	List<Pelicula> findByTituloContaining(String titulo);

	 List<Pelicula> findByTituloContainingIgnoreCaseAndPuntuacionGreaterThanEqualAndPuntuacionLessThanEqual(String titulo,
			Integer puntuacionMin, Integer puntuacionMax);

	List<Pelicula> findByPuntuacionGreaterThanEqualAndPuntuacionLessThanEqual(Integer puntuacionMin,
			Integer puntuacionMax);

	List<Pelicula> findByTituloContainingIgnoreCaseAndPuntuacionGreaterThanEqual(String titulo, Integer puntuacionMin);

	List<Pelicula> findByPuntuacionGreaterThanEqual(Integer puntuacionMin);

	List<Pelicula> findByTituloContainingIgnoreCase(String titulo);

	List<Pelicula> findByPuntuacionLessThanEqual(Integer puntuacionMax);

	List<Pelicula> findByTituloContainingIgnoreCaseAndPuntuacionLessThanEqual(String titulo, Integer puntuacionMax);
	
	

}


