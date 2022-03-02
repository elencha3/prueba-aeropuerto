package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Pilot;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Pilot entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PilotRepository extends JpaRepository<Pilot, Long> {}
