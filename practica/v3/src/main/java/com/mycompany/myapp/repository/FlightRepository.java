package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Flight;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Flight entity.
 */
@Repository
public interface FlightRepository extends JpaRepository<Flight, Long> {
    @Query(
        value = "select distinct flight from Flight flight left join fetch flight.crews",
        countQuery = "select count(distinct flight) from Flight flight"
    )
    Page<Flight> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct flight from Flight flight left join fetch flight.crews")
    List<Flight> findAllWithEagerRelationships();

    @Query("select flight from Flight flight left join fetch flight.crews where flight.id =:id")
    Optional<Flight> findOneWithEagerRelationships(@Param("id") Long id);

    Page<Flight> findByPilot_Dni(String dni, Pageable pageable);

    Long countByCrews_Dni(String dni);
}
