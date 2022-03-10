package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Plane;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data SQL repository for the Plane entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PlaneRepository extends JpaRepository<Plane, Long> {
    Optional<Plane> findFirstByOrderByAgeDesc();
}
