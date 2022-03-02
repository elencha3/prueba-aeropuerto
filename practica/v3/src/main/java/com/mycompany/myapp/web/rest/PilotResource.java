package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Pilot;
import com.mycompany.myapp.repository.PilotRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Pilot}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class PilotResource {

    private final Logger log = LoggerFactory.getLogger(PilotResource.class);

    private static final String ENTITY_NAME = "pilot";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PilotRepository pilotRepository;

    public PilotResource(PilotRepository pilotRepository) {
        this.pilotRepository = pilotRepository;
    }

    /**
     * {@code POST  /pilots} : Create a new pilot.
     *
     * @param pilot the pilot to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new pilot, or with status {@code 400 (Bad Request)} if the pilot has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/pilots")
    public ResponseEntity<Pilot> createPilot(@Valid @RequestBody Pilot pilot) throws URISyntaxException {
        log.debug("REST request to save Pilot : {}", pilot);
        if (pilot.getId() != null) {
            throw new BadRequestAlertException("A new pilot cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Pilot result = pilotRepository.save(pilot);
        return ResponseEntity
            .created(new URI("/api/pilots/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /pilots/:id} : Updates an existing pilot.
     *
     * @param id the id of the pilot to save.
     * @param pilot the pilot to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pilot,
     * or with status {@code 400 (Bad Request)} if the pilot is not valid,
     * or with status {@code 500 (Internal Server Error)} if the pilot couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/pilots/{id}")
    public ResponseEntity<Pilot> updatePilot(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody Pilot pilot)
        throws URISyntaxException {
        log.debug("REST request to update Pilot : {}, {}", id, pilot);
        if (pilot.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pilot.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pilotRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Pilot result = pilotRepository.save(pilot);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, pilot.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /pilots/:id} : Partial updates given fields of an existing pilot, field will ignore if it is null
     *
     * @param id the id of the pilot to save.
     * @param pilot the pilot to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pilot,
     * or with status {@code 400 (Bad Request)} if the pilot is not valid,
     * or with status {@code 404 (Not Found)} if the pilot is not found,
     * or with status {@code 500 (Internal Server Error)} if the pilot couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/pilots/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Pilot> partialUpdatePilot(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Pilot pilot
    ) throws URISyntaxException {
        log.debug("REST request to partial update Pilot partially : {}, {}", id, pilot);
        if (pilot.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pilot.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pilotRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Pilot> result = pilotRepository
            .findById(pilot.getId())
            .map(existingPilot -> {
                if (pilot.getName() != null) {
                    existingPilot.setName(pilot.getName());
                }
                if (pilot.getDni() != null) {
                    existingPilot.setDni(pilot.getDni());
                }
                if (pilot.getAdress() != null) {
                    existingPilot.setAdress(pilot.getAdress());
                }
                if (pilot.getEmail() != null) {
                    existingPilot.setEmail(pilot.getEmail());
                }
                if (pilot.getHorasVuelo() != null) {
                    existingPilot.setHorasVuelo(pilot.getHorasVuelo());
                }
                if (pilot.getPicture() != null) {
                    existingPilot.setPicture(pilot.getPicture());
                }
                if (pilot.getPictureContentType() != null) {
                    existingPilot.setPictureContentType(pilot.getPictureContentType());
                }

                return existingPilot;
            })
            .map(pilotRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, pilot.getId().toString())
        );
    }

    /**
     * {@code GET  /pilots} : get all the pilots.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of pilots in body.
     */
    @GetMapping("/pilots")
    public ResponseEntity<List<Pilot>> getAllPilots(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Pilots");
        Page<Pilot> page = pilotRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /pilots/:id} : get the "id" pilot.
     *
     * @param id the id of the pilot to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the pilot, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/pilots/{id}")
    public ResponseEntity<Pilot> getPilot(@PathVariable Long id) {
        log.debug("REST request to get Pilot : {}", id);
        Optional<Pilot> pilot = pilotRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(pilot);
    }

    /**
     * {@code DELETE  /pilots/:id} : delete the "id" pilot.
     *
     * @param id the id of the pilot to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/pilots/{id}")
    public ResponseEntity<Void> deletePilot(@PathVariable Long id) {
        log.debug("REST request to delete Pilot : {}", id);
        pilotRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
