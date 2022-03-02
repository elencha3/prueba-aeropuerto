package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Airport;
import com.mycompany.myapp.repository.AirportRepository;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Airport}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class AirportResource {

    private final Logger log = LoggerFactory.getLogger(AirportResource.class);

    private static final String ENTITY_NAME = "airport";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AirportRepository airportRepository;

    public AirportResource(AirportRepository airportRepository) {
        this.airportRepository = airportRepository;
    }

    /**
     * {@code POST  /airports} : Create a new airport.
     *
     * @param airport the airport to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new airport, or with status {@code 400 (Bad Request)} if the airport has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/airports")
    public ResponseEntity<Airport> createAirport(@Valid @RequestBody Airport airport) throws URISyntaxException {
        log.debug("REST request to save Airport : {}", airport);
        if (airport.getId() != null) {
            throw new BadRequestAlertException("A new airport cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Airport result = airportRepository.save(airport);
        return ResponseEntity
            .created(new URI("/api/airports/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /airports/:id} : Updates an existing airport.
     *
     * @param id the id of the airport to save.
     * @param airport the airport to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated airport,
     * or with status {@code 400 (Bad Request)} if the airport is not valid,
     * or with status {@code 500 (Internal Server Error)} if the airport couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/airports/{id}")
    public ResponseEntity<Airport> updateAirport(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Airport airport
    ) throws URISyntaxException {
        log.debug("REST request to update Airport : {}, {}", id, airport);
        if (airport.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, airport.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!airportRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Airport result = airportRepository.save(airport);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, airport.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /airports/:id} : Partial updates given fields of an existing airport, field will ignore if it is null
     *
     * @param id the id of the airport to save.
     * @param airport the airport to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated airport,
     * or with status {@code 400 (Bad Request)} if the airport is not valid,
     * or with status {@code 404 (Not Found)} if the airport is not found,
     * or with status {@code 500 (Internal Server Error)} if the airport couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/airports/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Airport> partialUpdateAirport(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Airport airport
    ) throws URISyntaxException {
        log.debug("REST request to partial update Airport partially : {}, {}", id, airport);
        if (airport.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, airport.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!airportRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Airport> result = airportRepository
            .findById(airport.getId())
            .map(existingAirport -> {
                if (airport.getName() != null) {
                    existingAirport.setName(airport.getName());
                }

                return existingAirport;
            })
            .map(airportRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, airport.getId().toString())
        );
    }

    /**
     * {@code GET  /airports} : get all the airports.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of airports in body.
     */
    @GetMapping("/airports")
    public ResponseEntity<List<Airport>> getAllAirports(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Airports");
        Page<Airport> page = airportRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /airports/:id} : get the "id" airport.
     *
     * @param id the id of the airport to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the airport, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/airports/{id}")
    public ResponseEntity<Airport> getAirport(@PathVariable Long id) {
        log.debug("REST request to get Airport : {}", id);
        Optional<Airport> airport = airportRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(airport);
    }

    /**
     * {@code DELETE  /airports/:id} : delete the "id" airport.
     *
     * @param id the id of the airport to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/airports/{id}")
    public ResponseEntity<Void> deleteAirport(@PathVariable Long id) {
        log.debug("REST request to delete Airport : {}", id);
        airportRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
