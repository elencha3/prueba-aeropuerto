package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Flight;
import com.mycompany.myapp.repository.FlightRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class FlightService {

    @Autowired
    FlightRepository flightRepository;

    public Page<Flight> getPilotFlights(String dni, Pageable pageable) {
        return flightRepository.findByPilot_Dni(dni, pageable);
    }

    public Long countByCrews_Dni(String dni) {
       return flightRepository.countByCrews_Dni(dni);
    }

}
