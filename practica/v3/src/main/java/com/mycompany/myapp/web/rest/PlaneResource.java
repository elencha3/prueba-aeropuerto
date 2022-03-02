package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Plane;
import com.mycompany.myapp.repository.PlaneRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Plane}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class PlaneResource {

    private final Logger log = LoggerFactory.getLogger(PlaneResource.class);

    private static final String ENTITY_NAME = "plane";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PlaneRepository planeRepository;

    public PlaneResource(PlaneRepository planeRepository) {
        this.planeRepository = planeRepository;
    }

    /**
     * {@code POST  /planes} : Create a new plane.
     *
     * @param plane the plane to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new plane, or with status {@code 400 (Bad Request)} if the plane has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/planes")
    public ResponseEntity<Plane> createPlane(@Valid @RequestBody Plane plane) throws URISyntaxException {
        log.debug("REST request to save Plane : {}", plane);
        if (plane.getId() != null) {
            throw new BadRequestAlertException("A new plane cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Plane result = planeRepository.save(plane);
        return ResponseEntity
            .created(new URI("/api/planes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /planes/:id} : Updates an existing plane.
     *
     * @param id the id of the plane to save.
     * @param plane the plane to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated plane,
     * or with status {@code 400 (Bad Request)} if the plane is not valid,
     * or with status {@code 500 (Internal Server Error)} if the plane couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/planes/{id}")
    public ResponseEntity<Plane> updatePlane(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody Plane plane)
        throws URISyntaxException {
        log.debug("REST request to update Plane : {}, {}", id, plane);
        if (plane.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, plane.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!planeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Plane result = planeRepository.save(plane);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, plane.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /planes/:id} : Partial updates given fields of an existing plane, field will ignore if it is null
     *
     * @param id the id of the plane to save.
     * @param plane the plane to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated plane,
     * or with status {@code 400 (Bad Request)} if the plane is not valid,
     * or with status {@code 404 (Not Found)} if the plane is not found,
     * or with status {@code 500 (Internal Server Error)} if the plane couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/planes/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Plane> partialUpdatePlane(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Plane plane
    ) throws URISyntaxException {
        log.debug("REST request to partial update Plane partially : {}, {}", id, plane);
        if (plane.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, plane.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!planeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Plane> result = planeRepository
            .findById(plane.getId())
            .map(existingPlane -> {
                if (plane.getPlate() != null) {
                    existingPlane.setPlate(plane.getPlate());
                }
                if (plane.getType() != null) {
                    existingPlane.setType(plane.getType());
                }
                if (plane.getAge() != null) {
                    existingPlane.setAge(plane.getAge());
                }
                if (plane.getSerialNumber() != null) {
                    existingPlane.setSerialNumber(plane.getSerialNumber());
                }

                return existingPlane;
            })
            .map(planeRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, plane.getId().toString())
        );
    }

    /**
     * {@code GET  /planes} : get all the planes.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of planes in body.
     */
    @GetMapping("/planes")
    public ResponseEntity<List<Plane>> getAllPlanes(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Planes");
        Page<Plane> page = planeRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /planes/:id} : get the "id" plane.
     *
     * @param id the id of the plane to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the plane, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/planes/{id}")
    public ResponseEntity<Plane> getPlane(@PathVariable Long id) {
        log.debug("REST request to get Plane : {}", id);
        Optional<Plane> plane = planeRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(plane);
    }

    /**
     * {@code DELETE  /planes/:id} : delete the "id" plane.
     *
     * @param id the id of the plane to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/planes/{id}")
    public ResponseEntity<Void> deletePlane(@PathVariable Long id) {
        log.debug("REST request to delete Plane : {}", id);
        planeRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
