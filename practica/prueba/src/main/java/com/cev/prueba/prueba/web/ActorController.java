package com.cev.prueba.prueba.web;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cev.prueba.prueba.domain.Actor;
import com.cev.prueba.prueba.repository.ActorRepository;

@RestController
public class ActorController {

	ActorRepository actorRepository;
	
	
	public ActorController(ActorRepository actorRepository) {
		this.actorRepository = actorRepository;
	}

	@GetMapping(path="/actores")
	List<Actor> getAll(){
		return actorRepository.findAll();
	}
	
	@PostMapping (path="/actores")
	Actor altaActor(@RequestBody Actor actor) {
		return actorRepository.save(actor);
	}
	
	
}
