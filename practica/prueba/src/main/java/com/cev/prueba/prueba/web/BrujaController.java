package com.cev.prueba.prueba.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BrujaController {

	@GetMapping("/bruja")
	String dimeNavegador(@RequestHeader(name = "user-agent") String userAgent) {
		
		return "Estás navegando en : " + userAgent + " nice";
	}
	
}
