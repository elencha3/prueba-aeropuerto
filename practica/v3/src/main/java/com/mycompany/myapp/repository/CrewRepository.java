package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Crew;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Crew entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CrewRepository extends JpaRepository<Crew, Long> {}
