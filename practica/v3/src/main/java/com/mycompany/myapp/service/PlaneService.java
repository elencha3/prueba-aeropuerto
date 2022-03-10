package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Plane;
import com.mycompany.myapp.repository.PlaneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PlaneService {
    @Autowired
     PlaneRepository planeRepository;

    public Optional<Plane> getOldestPlane() {
        return planeRepository.findFirstByOrderByAgeDesc();
    }
}
