package com.mycompany.myapp.web.rest;


import com.mycompany.myapp.domain.Flight;
import com.mycompany.myapp.domain.Plane;
import com.mycompany.myapp.service.FlightService;
import com.mycompany.myapp.service.PlaneService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.testcontainers.shaded.okhttp3.Response;

import java.util.Optional;

@RestController
@RequestMapping("/api")
public class Controller {

    FlightService flightService;
    PlaneService planeService;

    public Controller(FlightService flightService, PlaneService planeService) {
        this.flightService = flightService;
        this.planeService = planeService;
    }

    @GetMapping("/plane")
    public ResponseEntity<Optional<Plane>> getOldestPlane(){
        return ResponseEntity.ok(planeService.getOldestPlane());
    }

    @GetMapping("/vuelo")
    public ResponseEntity<Page<Flight>> findByPilot_Dni(@RequestParam String dni, @org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        if (validateDNI(dni)) {
            return ResponseEntity.ok(flightService.getPilotFlights(dni, pageable));
        }
        return new ResponseEntity("algo ha fallado", HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/vuelo2")
    public ResponseEntity<Long> countByCrews_Dni(@RequestParam String dni) {
        if(validateDNI(dni)) {
            return ResponseEntity.ok(flightService.countByCrews_Dni(dni));
        }
        return new ResponseEntity("algo ha fallado", HttpStatus.BAD_REQUEST);

    }

    public boolean validateDNI(String dni){
    if (dni.matches(("[0-9]{8}[TRWAGMYFPDXBNJZSQVHLCKE]"))) {
        return true;
     }return false;
    }


}
