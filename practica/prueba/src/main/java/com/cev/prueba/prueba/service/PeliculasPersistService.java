package com.cev.prueba.prueba.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.cev.prueba.prueba.domain.Pelicula;
import com.cev.prueba.prueba.repository.PeliculaRepository;

@Service
public class PeliculasPersistService {

	PeliculaRepository peliculasRepository;

	public PeliculasPersistService(PeliculaRepository peliculaRepository) {	
		this.peliculasRepository = peliculaRepository;
	}
	
	
	public Pelicula getPelicula(Long id) {
		return peliculasRepository.getById(id);
	}
	
	public Long add(Pelicula pelicula) {
		Pelicula peliculaGuardada= peliculasRepository.save(pelicula);
		return peliculaGuardada.getId();
	}
	
	public List<Pelicula> getPelicula(){
		return peliculasRepository.findAll();
	}
	
	public void guarda(Long id, Pelicula pelicula) {
		
		Pelicula peliculaGuardada = peliculasRepository.getById(id);
		peliculaGuardada.setDirector(pelicula.getDirector());
		peliculaGuardada.setFechaEstreno(pelicula.getFechaEstreno());
		peliculaGuardada.setPuntuacion(pelicula.getPuntuacion());
		peliculaGuardada.setSinopsis(pelicula.getSinopsis());
		peliculaGuardada.setTitulo(pelicula.getTitulo());
		peliculasRepository.save(peliculaGuardada);
	}
	
	
	public void borra(Long id) {
		peliculasRepository.delete(peliculasRepository.getById(id));
		
	}
	
	public Optional<Pelicula> findById(Long id){
		return peliculasRepository.findById(id);
	}
	
	public List<Pelicula> findByTituloContaining(String titulo){
		return peliculasRepository.findByTituloContaining(titulo);
	}

	
}
