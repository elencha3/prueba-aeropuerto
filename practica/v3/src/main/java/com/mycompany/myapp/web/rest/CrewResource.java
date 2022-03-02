package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Crew;
import com.mycompany.myapp.repository.CrewRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Crew}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class CrewResource {

    private final Logger log = LoggerFactory.getLogger(CrewResource.class);

    private static final String ENTITY_NAME = "crew";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CrewRepository crewRepository;

    public CrewResource(CrewRepository crewRepository) {
        this.crewRepository = crewRepository;
    }

    /**
     * {@code POST  /crews} : Create a new crew.
     *
     * @param crew the crew to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new crew, or with status {@code 400 (Bad Request)} if the crew has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/crews")
    public ResponseEntity<Crew> createCrew(@Valid @RequestBody Crew crew) throws URISyntaxException {
        log.debug("REST request to save Crew : {}", crew);
        if (crew.getId() != null) {
            throw new BadRequestAlertException("A new crew cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Crew result = crewRepository.save(crew);
        return ResponseEntity
            .created(new URI("/api/crews/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /crews/:id} : Updates an existing crew.
     *
     * @param id the id of the crew to save.
     * @param crew the crew to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated crew,
     * or with status {@code 400 (Bad Request)} if the crew is not valid,
     * or with status {@code 500 (Internal Server Error)} if the crew couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/crews/{id}")
    public ResponseEntity<Crew> updateCrew(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody Crew crew)
        throws URISyntaxException {
        log.debug("REST request to update Crew : {}, {}", id, crew);
        if (crew.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, crew.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!crewRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Crew result = crewRepository.save(crew);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, crew.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /crews/:id} : Partial updates given fields of an existing crew, field will ignore if it is null
     *
     * @param id the id of the crew to save.
     * @param crew the crew to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated crew,
     * or with status {@code 400 (Bad Request)} if the crew is not valid,
     * or with status {@code 404 (Not Found)} if the crew is not found,
     * or with status {@code 500 (Internal Server Error)} if the crew couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/crews/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Crew> partialUpdateCrew(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Crew crew
    ) throws URISyntaxException {
        log.debug("REST request to partial update Crew partially : {}, {}", id, crew);
        if (crew.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, crew.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!crewRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Crew> result = crewRepository
            .findById(crew.getId())
            .map(existingCrew -> {
                if (crew.getName() != null) {
                    existingCrew.setName(crew.getName());
                }
                if (crew.getDni() != null) {
                    existingCrew.setDni(crew.getDni());
                }
                if (crew.getAdress() != null) {
                    existingCrew.setAdress(crew.getAdress());
                }
                if (crew.getEmail() != null) {
                    existingCrew.setEmail(crew.getEmail());
                }
                if (crew.getPicture() != null) {
                    existingCrew.setPicture(crew.getPicture());
                }
                if (crew.getPictureContentType() != null) {
                    existingCrew.setPictureContentType(crew.getPictureContentType());
                }

                return existingCrew;
            })
            .map(crewRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, crew.getId().toString())
        );
    }

    /**
     * {@code GET  /crews} : get all the crews.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of crews in body.
     */
    @GetMapping("/crews")
    public ResponseEntity<List<Crew>> getAllCrews(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Crews");
        Page<Crew> page = crewRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /crews/:id} : get the "id" crew.
     *
     * @param id the id of the crew to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the crew, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/crews/{id}")
    public ResponseEntity<Crew> getCrew(@PathVariable Long id) {
        log.debug("REST request to get Crew : {}", id);
        Optional<Crew> crew = crewRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(crew);
    }

    /**
     * {@code DELETE  /crews/:id} : delete the "id" crew.
     *
     * @param id the id of the crew to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/crews/{id}")
    public ResponseEntity<Void> deleteCrew(@PathVariable Long id) {
        log.debug("REST request to delete Crew : {}", id);
        crewRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
