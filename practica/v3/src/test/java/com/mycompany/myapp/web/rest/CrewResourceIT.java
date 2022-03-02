package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Crew;
import com.mycompany.myapp.repository.CrewRepository;
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
 * Integration tests for the {@link CrewResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CrewResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DNI = "57369247Q";
    private static final String UPDATED_DNI = "65475338H";

    private static final String DEFAULT_ADRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADRESS = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "bCZ@S}eh.3CK";
    private static final String UPDATED_EMAIL = "V*,@#*.vT<)h";

    private static final byte[] DEFAULT_PICTURE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_PICTURE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_PICTURE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_PICTURE_CONTENT_TYPE = "image/png";

    private static final String ENTITY_API_URL = "/api/crews";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CrewRepository crewRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCrewMockMvc;

    private Crew crew;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Crew createEntity(EntityManager em) {
        Crew crew = new Crew()
            .name(DEFAULT_NAME)
            .dni(DEFAULT_DNI)
            .adress(DEFAULT_ADRESS)
            .email(DEFAULT_EMAIL)
            .picture(DEFAULT_PICTURE)
            .pictureContentType(DEFAULT_PICTURE_CONTENT_TYPE);
        return crew;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Crew createUpdatedEntity(EntityManager em) {
        Crew crew = new Crew()
            .name(UPDATED_NAME)
            .dni(UPDATED_DNI)
            .adress(UPDATED_ADRESS)
            .email(UPDATED_EMAIL)
            .picture(UPDATED_PICTURE)
            .pictureContentType(UPDATED_PICTURE_CONTENT_TYPE);
        return crew;
    }

    @BeforeEach
    public void initTest() {
        crew = createEntity(em);
    }

    @Test
    @Transactional
    void createCrew() throws Exception {
        int databaseSizeBeforeCreate = crewRepository.findAll().size();
        // Create the Crew
        restCrewMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(crew)))
            .andExpect(status().isCreated());

        // Validate the Crew in the database
        List<Crew> crewList = crewRepository.findAll();
        assertThat(crewList).hasSize(databaseSizeBeforeCreate + 1);
        Crew testCrew = crewList.get(crewList.size() - 1);
        assertThat(testCrew.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCrew.getDni()).isEqualTo(DEFAULT_DNI);
        assertThat(testCrew.getAdress()).isEqualTo(DEFAULT_ADRESS);
        assertThat(testCrew.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testCrew.getPicture()).isEqualTo(DEFAULT_PICTURE);
        assertThat(testCrew.getPictureContentType()).isEqualTo(DEFAULT_PICTURE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void createCrewWithExistingId() throws Exception {
        // Create the Crew with an existing ID
        crew.setId(1L);

        int databaseSizeBeforeCreate = crewRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCrewMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(crew)))
            .andExpect(status().isBadRequest());

        // Validate the Crew in the database
        List<Crew> crewList = crewRepository.findAll();
        assertThat(crewList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = crewRepository.findAll().size();
        // set the field null
        crew.setName(null);

        // Create the Crew, which fails.

        restCrewMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(crew)))
            .andExpect(status().isBadRequest());

        List<Crew> crewList = crewRepository.findAll();
        assertThat(crewList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDniIsRequired() throws Exception {
        int databaseSizeBeforeTest = crewRepository.findAll().size();
        // set the field null
        crew.setDni(null);

        // Create the Crew, which fails.

        restCrewMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(crew)))
            .andExpect(status().isBadRequest());

        List<Crew> crewList = crewRepository.findAll();
        assertThat(crewList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAdressIsRequired() throws Exception {
        int databaseSizeBeforeTest = crewRepository.findAll().size();
        // set the field null
        crew.setAdress(null);

        // Create the Crew, which fails.

        restCrewMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(crew)))
            .andExpect(status().isBadRequest());

        List<Crew> crewList = crewRepository.findAll();
        assertThat(crewList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = crewRepository.findAll().size();
        // set the field null
        crew.setEmail(null);

        // Create the Crew, which fails.

        restCrewMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(crew)))
            .andExpect(status().isBadRequest());

        List<Crew> crewList = crewRepository.findAll();
        assertThat(crewList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCrews() throws Exception {
        // Initialize the database
        crewRepository.saveAndFlush(crew);

        // Get all the crewList
        restCrewMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(crew.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].dni").value(hasItem(DEFAULT_DNI)))
            .andExpect(jsonPath("$.[*].adress").value(hasItem(DEFAULT_ADRESS)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].pictureContentType").value(hasItem(DEFAULT_PICTURE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].picture").value(hasItem(Base64Utils.encodeToString(DEFAULT_PICTURE))));
    }

    @Test
    @Transactional
    void getCrew() throws Exception {
        // Initialize the database
        crewRepository.saveAndFlush(crew);

        // Get the crew
        restCrewMockMvc
            .perform(get(ENTITY_API_URL_ID, crew.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(crew.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.dni").value(DEFAULT_DNI))
            .andExpect(jsonPath("$.adress").value(DEFAULT_ADRESS))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.pictureContentType").value(DEFAULT_PICTURE_CONTENT_TYPE))
            .andExpect(jsonPath("$.picture").value(Base64Utils.encodeToString(DEFAULT_PICTURE)));
    }

    @Test
    @Transactional
    void getNonExistingCrew() throws Exception {
        // Get the crew
        restCrewMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCrew() throws Exception {
        // Initialize the database
        crewRepository.saveAndFlush(crew);

        int databaseSizeBeforeUpdate = crewRepository.findAll().size();

        // Update the crew
        Crew updatedCrew = crewRepository.findById(crew.getId()).get();
        // Disconnect from session so that the updates on updatedCrew are not directly saved in db
        em.detach(updatedCrew);
        updatedCrew
            .name(UPDATED_NAME)
            .dni(UPDATED_DNI)
            .adress(UPDATED_ADRESS)
            .email(UPDATED_EMAIL)
            .picture(UPDATED_PICTURE)
            .pictureContentType(UPDATED_PICTURE_CONTENT_TYPE);

        restCrewMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCrew.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCrew))
            )
            .andExpect(status().isOk());

        // Validate the Crew in the database
        List<Crew> crewList = crewRepository.findAll();
        assertThat(crewList).hasSize(databaseSizeBeforeUpdate);
        Crew testCrew = crewList.get(crewList.size() - 1);
        assertThat(testCrew.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCrew.getDni()).isEqualTo(UPDATED_DNI);
        assertThat(testCrew.getAdress()).isEqualTo(UPDATED_ADRESS);
        assertThat(testCrew.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testCrew.getPicture()).isEqualTo(UPDATED_PICTURE);
        assertThat(testCrew.getPictureContentType()).isEqualTo(UPDATED_PICTURE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingCrew() throws Exception {
        int databaseSizeBeforeUpdate = crewRepository.findAll().size();
        crew.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCrewMockMvc
            .perform(
                put(ENTITY_API_URL_ID, crew.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(crew))
            )
            .andExpect(status().isBadRequest());

        // Validate the Crew in the database
        List<Crew> crewList = crewRepository.findAll();
        assertThat(crewList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCrew() throws Exception {
        int databaseSizeBeforeUpdate = crewRepository.findAll().size();
        crew.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCrewMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(crew))
            )
            .andExpect(status().isBadRequest());

        // Validate the Crew in the database
        List<Crew> crewList = crewRepository.findAll();
        assertThat(crewList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCrew() throws Exception {
        int databaseSizeBeforeUpdate = crewRepository.findAll().size();
        crew.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCrewMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(crew)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Crew in the database
        List<Crew> crewList = crewRepository.findAll();
        assertThat(crewList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCrewWithPatch() throws Exception {
        // Initialize the database
        crewRepository.saveAndFlush(crew);

        int databaseSizeBeforeUpdate = crewRepository.findAll().size();

        // Update the crew using partial update
        Crew partialUpdatedCrew = new Crew();
        partialUpdatedCrew.setId(crew.getId());

        partialUpdatedCrew
            .adress(UPDATED_ADRESS)
            .email(UPDATED_EMAIL)
            .picture(UPDATED_PICTURE)
            .pictureContentType(UPDATED_PICTURE_CONTENT_TYPE);

        restCrewMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCrew.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCrew))
            )
            .andExpect(status().isOk());

        // Validate the Crew in the database
        List<Crew> crewList = crewRepository.findAll();
        assertThat(crewList).hasSize(databaseSizeBeforeUpdate);
        Crew testCrew = crewList.get(crewList.size() - 1);
        assertThat(testCrew.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCrew.getDni()).isEqualTo(DEFAULT_DNI);
        assertThat(testCrew.getAdress()).isEqualTo(UPDATED_ADRESS);
        assertThat(testCrew.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testCrew.getPicture()).isEqualTo(UPDATED_PICTURE);
        assertThat(testCrew.getPictureContentType()).isEqualTo(UPDATED_PICTURE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateCrewWithPatch() throws Exception {
        // Initialize the database
        crewRepository.saveAndFlush(crew);

        int databaseSizeBeforeUpdate = crewRepository.findAll().size();

        // Update the crew using partial update
        Crew partialUpdatedCrew = new Crew();
        partialUpdatedCrew.setId(crew.getId());

        partialUpdatedCrew
            .name(UPDATED_NAME)
            .dni(UPDATED_DNI)
            .adress(UPDATED_ADRESS)
            .email(UPDATED_EMAIL)
            .picture(UPDATED_PICTURE)
            .pictureContentType(UPDATED_PICTURE_CONTENT_TYPE);

        restCrewMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCrew.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCrew))
            )
            .andExpect(status().isOk());

        // Validate the Crew in the database
        List<Crew> crewList = crewRepository.findAll();
        assertThat(crewList).hasSize(databaseSizeBeforeUpdate);
        Crew testCrew = crewList.get(crewList.size() - 1);
        assertThat(testCrew.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCrew.getDni()).isEqualTo(UPDATED_DNI);
        assertThat(testCrew.getAdress()).isEqualTo(UPDATED_ADRESS);
        assertThat(testCrew.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testCrew.getPicture()).isEqualTo(UPDATED_PICTURE);
        assertThat(testCrew.getPictureContentType()).isEqualTo(UPDATED_PICTURE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingCrew() throws Exception {
        int databaseSizeBeforeUpdate = crewRepository.findAll().size();
        crew.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCrewMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, crew.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(crew))
            )
            .andExpect(status().isBadRequest());

        // Validate the Crew in the database
        List<Crew> crewList = crewRepository.findAll();
        assertThat(crewList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCrew() throws Exception {
        int databaseSizeBeforeUpdate = crewRepository.findAll().size();
        crew.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCrewMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(crew))
            )
            .andExpect(status().isBadRequest());

        // Validate the Crew in the database
        List<Crew> crewList = crewRepository.findAll();
        assertThat(crewList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCrew() throws Exception {
        int databaseSizeBeforeUpdate = crewRepository.findAll().size();
        crew.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCrewMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(crew)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Crew in the database
        List<Crew> crewList = crewRepository.findAll();
        assertThat(crewList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCrew() throws Exception {
        // Initialize the database
        crewRepository.saveAndFlush(crew);

        int databaseSizeBeforeDelete = crewRepository.findAll().size();

        // Delete the crew
        restCrewMockMvc
            .perform(delete(ENTITY_API_URL_ID, crew.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Crew> crewList = crewRepository.findAll();
        assertThat(crewList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
