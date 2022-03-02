package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Plane;
import com.mycompany.myapp.repository.PlaneRepository;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link PlaneResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PlaneResourceIT {

    private static final String DEFAULT_PLATE = "1\\-7";
    private static final String UPDATED_PLATE = "d\\-Z";

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final Integer DEFAULT_AGE = 0;
    private static final Integer UPDATED_AGE = 1;

    private static final String DEFAULT_SERIAL_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_SERIAL_NUMBER = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/planes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PlaneRepository planeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPlaneMockMvc;

    private Plane plane;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Plane createEntity(EntityManager em) {
        Plane plane = new Plane().plate(DEFAULT_PLATE).type(DEFAULT_TYPE).age(DEFAULT_AGE).serialNumber(DEFAULT_SERIAL_NUMBER);
        return plane;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Plane createUpdatedEntity(EntityManager em) {
        Plane plane = new Plane().plate(UPDATED_PLATE).type(UPDATED_TYPE).age(UPDATED_AGE).serialNumber(UPDATED_SERIAL_NUMBER);
        return plane;
    }

    @BeforeEach
    public void initTest() {
        plane = createEntity(em);
    }

    @Test
    @Transactional
    void createPlane() throws Exception {
        int databaseSizeBeforeCreate = planeRepository.findAll().size();
        // Create the Plane
        restPlaneMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(plane)))
            .andExpect(status().isCreated());

        // Validate the Plane in the database
        List<Plane> planeList = planeRepository.findAll();
        assertThat(planeList).hasSize(databaseSizeBeforeCreate + 1);
        Plane testPlane = planeList.get(planeList.size() - 1);
        assertThat(testPlane.getPlate()).isEqualTo(DEFAULT_PLATE);
        assertThat(testPlane.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testPlane.getAge()).isEqualTo(DEFAULT_AGE);
        assertThat(testPlane.getSerialNumber()).isEqualTo(DEFAULT_SERIAL_NUMBER);
    }

    @Test
    @Transactional
    void createPlaneWithExistingId() throws Exception {
        // Create the Plane with an existing ID
        plane.setId(1L);

        int databaseSizeBeforeCreate = planeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPlaneMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(plane)))
            .andExpect(status().isBadRequest());

        // Validate the Plane in the database
        List<Plane> planeList = planeRepository.findAll();
        assertThat(planeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkPlateIsRequired() throws Exception {
        int databaseSizeBeforeTest = planeRepository.findAll().size();
        // set the field null
        plane.setPlate(null);

        // Create the Plane, which fails.

        restPlaneMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(plane)))
            .andExpect(status().isBadRequest());

        List<Plane> planeList = planeRepository.findAll();
        assertThat(planeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPlanes() throws Exception {
        // Initialize the database
        planeRepository.saveAndFlush(plane);

        // Get all the planeList
        restPlaneMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(plane.getId().intValue())))
            .andExpect(jsonPath("$.[*].plate").value(hasItem(DEFAULT_PLATE)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].age").value(hasItem(DEFAULT_AGE)))
            .andExpect(jsonPath("$.[*].serialNumber").value(hasItem(DEFAULT_SERIAL_NUMBER)));
    }

    @Test
    @Transactional
    void getPlane() throws Exception {
        // Initialize the database
        planeRepository.saveAndFlush(plane);

        // Get the plane
        restPlaneMockMvc
            .perform(get(ENTITY_API_URL_ID, plane.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(plane.getId().intValue()))
            .andExpect(jsonPath("$.plate").value(DEFAULT_PLATE))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE))
            .andExpect(jsonPath("$.age").value(DEFAULT_AGE))
            .andExpect(jsonPath("$.serialNumber").value(DEFAULT_SERIAL_NUMBER));
    }

    @Test
    @Transactional
    void getNonExistingPlane() throws Exception {
        // Get the plane
        restPlaneMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPlane() throws Exception {
        // Initialize the database
        planeRepository.saveAndFlush(plane);

        int databaseSizeBeforeUpdate = planeRepository.findAll().size();

        // Update the plane
        Plane updatedPlane = planeRepository.findById(plane.getId()).get();
        // Disconnect from session so that the updates on updatedPlane are not directly saved in db
        em.detach(updatedPlane);
        updatedPlane.plate(UPDATED_PLATE).type(UPDATED_TYPE).age(UPDATED_AGE).serialNumber(UPDATED_SERIAL_NUMBER);

        restPlaneMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPlane.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPlane))
            )
            .andExpect(status().isOk());

        // Validate the Plane in the database
        List<Plane> planeList = planeRepository.findAll();
        assertThat(planeList).hasSize(databaseSizeBeforeUpdate);
        Plane testPlane = planeList.get(planeList.size() - 1);
        assertThat(testPlane.getPlate()).isEqualTo(UPDATED_PLATE);
        assertThat(testPlane.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testPlane.getAge()).isEqualTo(UPDATED_AGE);
        assertThat(testPlane.getSerialNumber()).isEqualTo(UPDATED_SERIAL_NUMBER);
    }

    @Test
    @Transactional
    void putNonExistingPlane() throws Exception {
        int databaseSizeBeforeUpdate = planeRepository.findAll().size();
        plane.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPlaneMockMvc
            .perform(
                put(ENTITY_API_URL_ID, plane.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(plane))
            )
            .andExpect(status().isBadRequest());

        // Validate the Plane in the database
        List<Plane> planeList = planeRepository.findAll();
        assertThat(planeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPlane() throws Exception {
        int databaseSizeBeforeUpdate = planeRepository.findAll().size();
        plane.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlaneMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(plane))
            )
            .andExpect(status().isBadRequest());

        // Validate the Plane in the database
        List<Plane> planeList = planeRepository.findAll();
        assertThat(planeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPlane() throws Exception {
        int databaseSizeBeforeUpdate = planeRepository.findAll().size();
        plane.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlaneMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(plane)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Plane in the database
        List<Plane> planeList = planeRepository.findAll();
        assertThat(planeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePlaneWithPatch() throws Exception {
        // Initialize the database
        planeRepository.saveAndFlush(plane);

        int databaseSizeBeforeUpdate = planeRepository.findAll().size();

        // Update the plane using partial update
        Plane partialUpdatedPlane = new Plane();
        partialUpdatedPlane.setId(plane.getId());

        partialUpdatedPlane.type(UPDATED_TYPE).age(UPDATED_AGE).serialNumber(UPDATED_SERIAL_NUMBER);

        restPlaneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPlane.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPlane))
            )
            .andExpect(status().isOk());

        // Validate the Plane in the database
        List<Plane> planeList = planeRepository.findAll();
        assertThat(planeList).hasSize(databaseSizeBeforeUpdate);
        Plane testPlane = planeList.get(planeList.size() - 1);
        assertThat(testPlane.getPlate()).isEqualTo(DEFAULT_PLATE);
        assertThat(testPlane.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testPlane.getAge()).isEqualTo(UPDATED_AGE);
        assertThat(testPlane.getSerialNumber()).isEqualTo(UPDATED_SERIAL_NUMBER);
    }

    @Test
    @Transactional
    void fullUpdatePlaneWithPatch() throws Exception {
        // Initialize the database
        planeRepository.saveAndFlush(plane);

        int databaseSizeBeforeUpdate = planeRepository.findAll().size();

        // Update the plane using partial update
        Plane partialUpdatedPlane = new Plane();
        partialUpdatedPlane.setId(plane.getId());

        partialUpdatedPlane.plate(UPDATED_PLATE).type(UPDATED_TYPE).age(UPDATED_AGE).serialNumber(UPDATED_SERIAL_NUMBER);

        restPlaneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPlane.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPlane))
            )
            .andExpect(status().isOk());

        // Validate the Plane in the database
        List<Plane> planeList = planeRepository.findAll();
        assertThat(planeList).hasSize(databaseSizeBeforeUpdate);
        Plane testPlane = planeList.get(planeList.size() - 1);
        assertThat(testPlane.getPlate()).isEqualTo(UPDATED_PLATE);
        assertThat(testPlane.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testPlane.getAge()).isEqualTo(UPDATED_AGE);
        assertThat(testPlane.getSerialNumber()).isEqualTo(UPDATED_SERIAL_NUMBER);
    }

    @Test
    @Transactional
    void patchNonExistingPlane() throws Exception {
        int databaseSizeBeforeUpdate = planeRepository.findAll().size();
        plane.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPlaneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, plane.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(plane))
            )
            .andExpect(status().isBadRequest());

        // Validate the Plane in the database
        List<Plane> planeList = planeRepository.findAll();
        assertThat(planeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPlane() throws Exception {
        int databaseSizeBeforeUpdate = planeRepository.findAll().size();
        plane.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlaneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(plane))
            )
            .andExpect(status().isBadRequest());

        // Validate the Plane in the database
        List<Plane> planeList = planeRepository.findAll();
        assertThat(planeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPlane() throws Exception {
        int databaseSizeBeforeUpdate = planeRepository.findAll().size();
        plane.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlaneMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(plane)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Plane in the database
        List<Plane> planeList = planeRepository.findAll();
        assertThat(planeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePlane() throws Exception {
        // Initialize the database
        planeRepository.saveAndFlush(plane);

        int databaseSizeBeforeDelete = planeRepository.findAll().size();

        // Delete the plane
        restPlaneMockMvc
            .perform(delete(ENTITY_API_URL_ID, plane.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Plane> planeList = planeRepository.findAll();
        assertThat(planeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
