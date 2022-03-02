package com.cev.prueba.prueba.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cev.prueba.prueba.domain.Pelicula;
import com.cev.prueba.prueba.repository.PeliculaRepository;
import com.cev.prueba.prueba.service.PeliculasPersistService;
import com.cev.prueba.prueba.web.error.CustomError;

@RestController
public class PeliculasController {
	
	@Autowired
	PeliculasPersistService peliculasPersistService; 
	
	@Autowired
	PeliculaRepository peliculasRepository;
	
	
	@GetMapping(path="/peliculas")
	List<Pelicula> getPelicula(@RequestParam (required=false) String titulo, 
			@RequestParam (required=false, name="puntuacion.min") Integer puntuacionMin,
			@RequestParam (required=false, name="puntuacion.max") Integer puntuacionMax){
		
		
		if(puntuacionMin!= null) {
			if(puntuacionMax!=null) {
				if(titulo!=null) { //PuntuacionMin, PuntuacionMax,Titulo
					return peliculasRepository.findByTituloContainingIgnoreCaseAndPuntuacionGreaterThanEqualAndPuntuacionLessThanEqual(titulo, puntuacionMin, puntuacionMax);
				}else { //PuntuacionMin,PuntuacionMax,Titulo = NULL
					return peliculasRepository.findByPuntuacionGreaterThanEqualAndPuntuacionLessThanEqual(puntuacionMin, puntuacionMax);
				}
			}else {
				if(titulo!=null) {//PuntuacionMin, PuntuacionMax=NULL,Titulo
					return peliculasRepository.findByTituloContainingIgnoreCaseAndPuntuacionGreaterThanEqual(titulo, puntuacionMin);
				}else { //PuntuacionMin,PuntuacionMax=NULL,Titulo = NULL
					return peliculasRepository.findByPuntuacionGreaterThanEqual(puntuacionMin);
				}
			}
		} else {
			
			if(puntuacionMax!=null) {
				if(titulo!=null) { //PuntuacionMin=NULL, PuntuacionMax,Titulo
					return peliculasRepository.findByTituloContainingIgnoreCaseAndPuntuacionLessThanEqual(titulo, puntuacionMax);
				}else { //PuntuacionMin=NULL,PuntuacionMax,Titulo = NULL
					return peliculasRepository.findByPuntuacionLessThanEqual(puntuacionMax);
				}
			}else {
				if(titulo!=null) {//PuntuacionMin=NULL, PuntuacionMax=NULL,Titulo
					return peliculasRepository.findByTituloContainingIgnoreCase(titulo);
				}else { //PuntuacionMin=NULL,PuntuacionMax=NULL,Titulo = NULL
					return peliculasRepository.findAll();
				}
			}
		}
				
		
	}
	
	
	@GetMapping(path="/peliculas/{id}")
	Pelicula getPeliculas(@PathVariable Long id){
		
		if(peliculasPersistService.findById(id).isPresent()) { 
			return peliculasPersistService.findById(id).get();
		} else {
			throw new CustomError("No encuentro esa película");
		}
		
	}
	
	@PostMapping(path = "/peliculas")
	Long altaPelicula(@RequestBody Pelicula pelicula) {
		return peliculasPersistService.add(pelicula);
	}
	
	@PutMapping(path = "/peliculas/{id}")
	Pelicula modificaPelicula(@RequestBody Pelicula pelicula,@PathVariable Long id) {
		peliculasPersistService.guarda(id,pelicula);
		return pelicula;
	}
	
	@DeleteMapping(path="peliculas/{id}")
	String borraPelicula (@PathVariable Long id) {
		
		peliculasPersistService.borra(id);
		return("OK");
	}
	
	@GetMapping(path = "/peliculasHeader")
	ResponseEntity<List<Pelicula>> getPeliculasHeader(){
		HttpHeaders headers = new HttpHeaders();
		headers.add("MiHeaderRespuesta", "HeaderRespuesta");
		return ResponseEntity.ok().headers(headers).body(peliculasPersistService.getPelicula());
	}
	
	
}
