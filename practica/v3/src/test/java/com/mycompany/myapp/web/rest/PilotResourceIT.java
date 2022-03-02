package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Pilot;
import com.mycompany.myapp.repository.PilotRepository;
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
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link PilotResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PilotResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DNI = "93503793P";
    private static final String UPDATED_DNI = "55212549B";

    private static final String DEFAULT_ADRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADRESS = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "W`@T/%,.QR!.WQ";
    private static final String UPDATED_EMAIL = "S8q<@pV:.v~;]c!";

    private static final Long DEFAULT_HORAS_VUELO = 1L;
    private static final Long UPDATED_HORAS_VUELO = 2L;

    private static final byte[] DEFAULT_PICTURE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_PICTURE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_PICTURE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_PICTURE_CONTENT_TYPE = "image/png";

    private static final String ENTITY_API_URL = "/api/pilots";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PilotRepository pilotRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPilotMockMvc;

    private Pilot pilot;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Pilot createEntity(EntityManager em) {
        Pilot pilot = new Pilot()
            .name(DEFAULT_NAME)
            .dni(DEFAULT_DNI)
            .adress(DEFAULT_ADRESS)
            .email(DEFAULT_EMAIL)
            .horasVuelo(DEFAULT_HORAS_VUELO)
            .picture(DEFAULT_PICTURE)
            .pictureContentType(DEFAULT_PICTURE_CONTENT_TYPE);
        return pilot;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Pilot createUpdatedEntity(EntityManager em) {
        Pilot pilot = new Pilot()
            .name(UPDATED_NAME)
            .dni(UPDATED_DNI)
            .adress(UPDATED_ADRESS)
            .email(UPDATED_EMAIL)
            .horasVuelo(UPDATED_HORAS_VUELO)
            .picture(UPDATED_PICTURE)
            .pictureContentType(UPDATED_PICTURE_CONTENT_TYPE);
        return pilot;
    }

    @BeforeEach
    public void initTest() {
        pilot = createEntity(em);
    }

    @Test
    @Transactional
    void createPilot() throws Exception {
        int databaseSizeBeforeCreate = pilotRepository.findAll().size();
        // Create the Pilot
        restPilotMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pilot)))
            .andExpect(status().isCreated());

        // Validate the Pilot in the database
        List<Pilot> pilotList = pilotRepository.findAll();
        assertThat(pilotList).hasSize(databaseSizeBeforeCreate + 1);
        Pilot testPilot = pilotList.get(pilotList.size() - 1);
        assertThat(testPilot.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPilot.getDni()).isEqualTo(DEFAULT_DNI);
        assertThat(testPilot.getAdress()).isEqualTo(DEFAULT_ADRESS);
        assertThat(testPilot.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testPilot.getHorasVuelo()).isEqualTo(DEFAULT_HORAS_VUELO);
        assertThat(testPilot.getPicture()).isEqualTo(DEFAULT_PICTURE);
        assertThat(testPilot.getPictureContentType()).isEqualTo(DEFAULT_PICTURE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void createPilotWithExistingId() throws Exception {
        // Create the Pilot with an existing ID
        pilot.setId(1L);

        int databaseSizeBeforeCreate = pilotRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPilotMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pilot)))
            .andExpect(status().isBadRequest());

        // Validate the Pilot in the database
        List<Pilot> pilotList = pilotRepository.findAll();
        assertThat(pilotList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = pilotRepository.findAll().size();
        // set the field null
        pilot.setName(null);

        // Create the Pilot, which fails.

        restPilotMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pilot)))
            .andExpect(status().isBadRequest());

        List<Pilot> pilotList = pilotRepository.findAll();
        assertThat(pilotList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDniIsRequired() throws Exception {
        int databaseSizeBeforeTest = pilotRepository.findAll().size();
        // set the field null
        pilot.setDni(null);

        // Create the Pilot, which fails.

        restPilotMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pilot)))
            .andExpect(status().isBadRequest());

        List<Pilot> pilotList = pilotRepository.findAll();
        assertThat(pilotList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAdressIsRequired() throws Exception {
        int databaseSizeBeforeTest = pilotRepository.findAll().size();
        // set the field null
        pilot.setAdress(null);

        // Create the Pilot, which fails.

        restPilotMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pilot)))
            .andExpect(status().isBadRequest());

        List<Pilot> pilotList = pilotRepository.findAll();
        assertThat(pilotList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = pilotRepository.findAll().size();
        // set the field null
        pilot.setEmail(null);

        // Create the Pilot, which fails.

        restPilotMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pilot)))
            .andExpect(status().isBadRequest());

        List<Pilot> pilotList = pilotRepository.findAll();
        assertThat(pilotList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkHorasVueloIsRequired() throws Exception {
        int databaseSizeBeforeTest = pilotRepository.findAll().size();
        // set the field null
        pilot.setHorasVuelo(null);

        // Create the Pilot, which fails.

        restPilotMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pilot)))
            .andExpect(status().isBadRequest());

        List<Pilot> pilotList = pilotRepository.findAll();
        assertThat(pilotList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPilots() throws Exception {
        // Initialize the database
        pilotRepository.saveAndFlush(pilot);

        // Get all the pilotList
        restPilotMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pilot.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].dni").value(hasItem(DEFAULT_DNI)))
            .andExpect(jsonPath("$.[*].adress").value(hasItem(DEFAULT_ADRESS)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].horasVuelo").value(hasItem(DEFAULT_HORAS_VUELO.intValue())))
            .andExpect(jsonPath("$.[*].pictureContentType").value(hasItem(DEFAULT_PICTURE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].picture").value(hasItem(Base64Utils.encodeToString(DEFAULT_PICTURE))));
    }

    @Test
    @Transactional
    void getPilot() throws Exception {
        // Initialize the database
        pilotRepository.saveAndFlush(pilot);

        // Get the pilot
        restPilotMockMvc
            .perform(get(ENTITY_API_URL_ID, pilot.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(pilot.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.dni").value(DEFAULT_DNI))
            .andExpect(jsonPath("$.adress").value(DEFAULT_ADRESS))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.horasVuelo").value(DEFAULT_HORAS_VUELO.intValue()))
            .andExpect(jsonPath("$.pictureContentType").value(DEFAULT_PICTURE_CONTENT_TYPE))
            .andExpect(jsonPath("$.picture").value(Base64Utils.encodeToString(DEFAULT_PICTURE)));
    }

    @Test
    @Transactional
    void getNonExistingPilot() throws Exception {
        // Get the pilot
        restPilotMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPilot() throws Exception {
        // Initialize the database
        pilotRepository.saveAndFlush(pilot);

        int databaseSizeBeforeUpdate = pilotRepository.findAll().size();

        // Update the pilot
        Pilot updatedPilot = pilotRepository.findById(pilot.getId()).get();
        // Disconnect from session so that the updates on updatedPilot are not directly saved in db
        em.detach(updatedPilot);
        updatedPilot
            .name(UPDATED_NAME)
            .dni(UPDATED_DNI)
            .adress(UPDATED_ADRESS)
            .email(UPDATED_EMAIL)
            .horasVuelo(UPDATED_HORAS_VUELO)
            .picture(UPDATED_PICTURE)
            .pictureContentType(UPDATED_PICTURE_CONTENT_TYPE);

        restPilotMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPilot.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPilot))
            )
            .andExpect(status().isOk());

        // Validate the Pilot in the database
        List<Pilot> pilotList = pilotRepository.findAll();
        assertThat(pilotList).hasSize(databaseSizeBeforeUpdate);
        Pilot testPilot = pilotList.get(pilotList.size() - 1);
        assertThat(testPilot.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPilot.getDni()).isEqualTo(UPDATED_DNI);
        assertThat(testPilot.getAdress()).isEqualTo(UPDATED_ADRESS);
        assertThat(testPilot.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testPilot.getHorasVuelo()).isEqualTo(UPDATED_HORAS_VUELO);
        assertThat(testPilot.getPicture()).isEqualTo(UPDATED_PICTURE);
        assertThat(testPilot.getPictureContentType()).isEqualTo(UPDATED_PICTURE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingPilot() throws Exception {
        int databaseSizeBeforeUpdate = pilotRepository.findAll().size();
        pilot.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPilotMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pilot.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pilot))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pilot in the database
        List<Pilot> pilotList = pilotRepository.findAll();
        assertThat(pilotList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPilot() throws Exception {
        int databaseSizeBeforeUpdate = pilotRepository.findAll().size();
        pilot.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPilotMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pilot))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pilot in the database
        List<Pilot> pilotList = pilotRepository.findAll();
        assertThat(pilotList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPilot() throws Exception {
        int databaseSizeBeforeUpdate = pilotRepository.findAll().size();
        pilot.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPilotMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pilot)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Pilot in the database
        List<Pilot> pilotList = pilotRepository.findAll();
        assertThat(pilotList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePilotWithPatch() throws Exception {
        // Initialize the database
        pilotRepository.saveAndFlush(pilot);

        int databaseSizeBeforeUpdate = pilotRepository.findAll().size();

        // Update the pilot using partial update
        Pilot partialUpdatedPilot = new Pilot();
        partialUpdatedPilot.setId(pilot.getId());

        partialUpdatedPilot
            .dni(UPDATED_DNI)
            .adress(UPDATED_ADRESS)
            .picture(UPDATED_PICTURE)
            .pictureContentType(UPDATED_PICTURE_CONTENT_TYPE);

        restPilotMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPilot.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPilot))
            )
            .andExpect(status().isOk());

        // Validate the Pilot in the database
        List<Pilot> pilotList = pilotRepository.findAll();
        assertThat(pilotList).hasSize(databaseSizeBeforeUpdate);
        Pilot testPilot = pilotList.get(pilotList.size() - 1);
        assertThat(testPilot.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPilot.getDni()).isEqualTo(UPDATED_DNI);
        assertThat(testPilot.getAdress()).isEqualTo(UPDATED_ADRESS);
        assertThat(testPilot.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testPilot.getHorasVuelo()).isEqualTo(DEFAULT_HORAS_VUELO);
        assertThat(testPilot.getPicture()).isEqualTo(UPDATED_PICTURE);
        assertThat(testPilot.getPictureContentType()).isEqualTo(UPDATED_PICTURE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void fullUpdatePilotWithPatch() throws Exception {
        // Initialize the database
        pilotRepository.saveAndFlush(pilot);

        int databaseSizeBeforeUpdate = pilotRepository.findAll().size();

        // Update the pilot using partial update
        Pilot partialUpdatedPilot = new Pilot();
        partialUpdatedPilot.setId(pilot.getId());

        partialUpdatedPilot
            .name(UPDATED_NAME)
            .dni(UPDATED_DNI)
            .adress(UPDATED_ADRESS)
            .email(UPDATED_EMAIL)
            .horasVuelo(UPDATED_HORAS_VUELO)
            .picture(UPDATED_PICTURE)
            .pictureContentType(UPDATED_PICTURE_CONTENT_TYPE);

        restPilotMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPilot.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPilot))
            )
            .andExpect(status().isOk());

        // Validate the Pilot in the database
        List<Pilot> pilotList = pilotRepository.findAll();
        assertThat(pilotList).hasSize(databaseSizeBeforeUpdate);
        Pilot testPilot = pilotList.get(pilotList.size() - 1);
        assertThat(testPilot.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPilot.getDni()).isEqualTo(UPDATED_DNI);
        assertThat(testPilot.getAdress()).isEqualTo(UPDATED_ADRESS);
        assertThat(testPilot.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testPilot.getHorasVuelo()).isEqualTo(UPDATED_HORAS_VUELO);
        assertThat(testPilot.getPicture()).isEqualTo(UPDATED_PICTURE);
        assertThat(testPilot.getPictureContentType()).isEqualTo(UPDATED_PICTURE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingPilot() throws Exception {
        int databaseSizeBeforeUpdate = pilotRepository.findAll().size();
        pilot.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPilotMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, pilot.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pilot))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pilot in the database
        List<Pilot> pilotList = pilotRepository.findAll();
        assertThat(pilotList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPilot() throws Exception {
        int databaseSizeBeforeUpdate = pilotRepository.findAll().size();
        pilot.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPilotMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pilot))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pilot in the database
        List<Pilot> pilotList = pilotRepository.findAll();
        assertThat(pilotList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPilot() throws Exception {
        int databaseSizeBeforeUpdate = pilotRepository.findAll().size();
        pilot.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPilotMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(pilot)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Pilot in the database
        List<Pilot> pilotList = pilotRepository.findAll();
        assertThat(pilotList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePilot() throws Exception {
        // Initialize the database
        pilotRepository.saveAndFlush(pilot);

        int databaseSizeBeforeDelete = pilotRepository.findAll().size();

        // Delete the pilot
        restPilotMockMvc
            .perform(delete(ENTITY_API_URL_ID, pilot.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Pilot> pilotList = pilotRepository.findAll();
        assertThat(pilotList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
