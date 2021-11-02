package org.nmcpye.activitiesmanagement.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.nmcpye.activitiesmanagement.IntegrationTest;
import org.nmcpye.activitiesmanagement.domain.OrganisationUnitLevel;
import org.nmcpye.activitiesmanagement.repository.OrganisationUnitLevelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link OrganisationUnitLevelResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OrganisationUnitLevelResourceIT {

    private static final String DEFAULT_UID = "AAAAAAAAAA";
    private static final String UPDATED_UID = "BBBBBBBBBB";

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_LAST_UPDATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_UPDATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Integer DEFAULT_LEVEL = 1;
    private static final Integer UPDATED_LEVEL = 2;

    private static final Integer DEFAULT_OFFLINE_LEVELS = 1;
    private static final Integer UPDATED_OFFLINE_LEVELS = 2;

    private static final String ENTITY_API_URL = "/api/organisation-unit-levels";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private OrganisationUnitLevelRepository organisationUnitLevelRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOrganisationUnitLevelMockMvc;

    private OrganisationUnitLevel organisationUnitLevel;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrganisationUnitLevel createEntity(EntityManager em) {
        OrganisationUnitLevel organisationUnitLevel = new OrganisationUnitLevel()
            .uid(DEFAULT_UID)
            .code(DEFAULT_CODE)
            .name(DEFAULT_NAME)
            .created(DEFAULT_CREATED)
            .lastUpdated(DEFAULT_LAST_UPDATED)
            .level(DEFAULT_LEVEL)
            .offlineLevels(DEFAULT_OFFLINE_LEVELS);
        return organisationUnitLevel;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrganisationUnitLevel createUpdatedEntity(EntityManager em) {
        OrganisationUnitLevel organisationUnitLevel = new OrganisationUnitLevel()
            .uid(UPDATED_UID)
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .created(UPDATED_CREATED)
            .lastUpdated(UPDATED_LAST_UPDATED)
            .level(UPDATED_LEVEL)
            .offlineLevels(UPDATED_OFFLINE_LEVELS);
        return organisationUnitLevel;
    }

    @BeforeEach
    public void initTest() {
        organisationUnitLevel = createEntity(em);
    }

    @Test
    @Transactional
    void createOrganisationUnitLevel() throws Exception {
        int databaseSizeBeforeCreate = organisationUnitLevelRepository.findAll().size();
        // Create the OrganisationUnitLevel
        restOrganisationUnitLevelMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(organisationUnitLevel))
            )
            .andExpect(status().isCreated());

        // Validate the OrganisationUnitLevel in the database
        List<OrganisationUnitLevel> organisationUnitLevelList = organisationUnitLevelRepository.findAll();
        assertThat(organisationUnitLevelList).hasSize(databaseSizeBeforeCreate + 1);
        OrganisationUnitLevel testOrganisationUnitLevel = organisationUnitLevelList.get(organisationUnitLevelList.size() - 1);
        assertThat(testOrganisationUnitLevel.getUid()).isEqualTo(DEFAULT_UID);
        assertThat(testOrganisationUnitLevel.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testOrganisationUnitLevel.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testOrganisationUnitLevel.getCreated()).isEqualTo(DEFAULT_CREATED);
        assertThat(testOrganisationUnitLevel.getLastUpdated()).isEqualTo(DEFAULT_LAST_UPDATED);
        assertThat(testOrganisationUnitLevel.getLevel()).isEqualTo(DEFAULT_LEVEL);
        assertThat(testOrganisationUnitLevel.getOfflineLevels()).isEqualTo(DEFAULT_OFFLINE_LEVELS);
    }

    @Test
    @Transactional
    void createOrganisationUnitLevelWithExistingId() throws Exception {
        // Create the OrganisationUnitLevel with an existing ID
        organisationUnitLevel.setId(1L);

        int databaseSizeBeforeCreate = organisationUnitLevelRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrganisationUnitLevelMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(organisationUnitLevel))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrganisationUnitLevel in the database
        List<OrganisationUnitLevel> organisationUnitLevelList = organisationUnitLevelRepository.findAll();
        assertThat(organisationUnitLevelList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkUidIsRequired() throws Exception {
        int databaseSizeBeforeTest = organisationUnitLevelRepository.findAll().size();
        // set the field null
        organisationUnitLevel.setUid(null);

        // Create the OrganisationUnitLevel, which fails.

        restOrganisationUnitLevelMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(organisationUnitLevel))
            )
            .andExpect(status().isBadRequest());

        List<OrganisationUnitLevel> organisationUnitLevelList = organisationUnitLevelRepository.findAll();
        assertThat(organisationUnitLevelList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllOrganisationUnitLevels() throws Exception {
        // Initialize the database
        organisationUnitLevelRepository.saveAndFlush(organisationUnitLevel);

        // Get all the organisationUnitLevelList
        restOrganisationUnitLevelMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(organisationUnitLevel.getId().intValue())))
            .andExpect(jsonPath("$.[*].uid").value(hasItem(DEFAULT_UID)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].created").value(hasItem(DEFAULT_CREATED.toString())))
            .andExpect(jsonPath("$.[*].lastUpdated").value(hasItem(DEFAULT_LAST_UPDATED.toString())))
            .andExpect(jsonPath("$.[*].level").value(hasItem(DEFAULT_LEVEL)))
            .andExpect(jsonPath("$.[*].offlineLevels").value(hasItem(DEFAULT_OFFLINE_LEVELS)));
    }

    @Test
    @Transactional
    void getOrganisationUnitLevel() throws Exception {
        // Initialize the database
        organisationUnitLevelRepository.saveAndFlush(organisationUnitLevel);

        // Get the organisationUnitLevel
        restOrganisationUnitLevelMockMvc
            .perform(get(ENTITY_API_URL_ID, organisationUnitLevel.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(organisationUnitLevel.getId().intValue()))
            .andExpect(jsonPath("$.uid").value(DEFAULT_UID))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.created").value(DEFAULT_CREATED.toString()))
            .andExpect(jsonPath("$.lastUpdated").value(DEFAULT_LAST_UPDATED.toString()))
            .andExpect(jsonPath("$.level").value(DEFAULT_LEVEL))
            .andExpect(jsonPath("$.offlineLevels").value(DEFAULT_OFFLINE_LEVELS));
    }

    @Test
    @Transactional
    void getNonExistingOrganisationUnitLevel() throws Exception {
        // Get the organisationUnitLevel
        restOrganisationUnitLevelMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewOrganisationUnitLevel() throws Exception {
        // Initialize the database
        organisationUnitLevelRepository.saveAndFlush(organisationUnitLevel);

        int databaseSizeBeforeUpdate = organisationUnitLevelRepository.findAll().size();

        // Update the organisationUnitLevel
        OrganisationUnitLevel updatedOrganisationUnitLevel = organisationUnitLevelRepository.findById(organisationUnitLevel.getId()).get();
        // Disconnect from session so that the updates on updatedOrganisationUnitLevel are not directly saved in db
        em.detach(updatedOrganisationUnitLevel);
        updatedOrganisationUnitLevel
            .uid(UPDATED_UID)
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .created(UPDATED_CREATED)
            .lastUpdated(UPDATED_LAST_UPDATED)
            .level(UPDATED_LEVEL)
            .offlineLevels(UPDATED_OFFLINE_LEVELS);

        restOrganisationUnitLevelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedOrganisationUnitLevel.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedOrganisationUnitLevel))
            )
            .andExpect(status().isOk());

        // Validate the OrganisationUnitLevel in the database
        List<OrganisationUnitLevel> organisationUnitLevelList = organisationUnitLevelRepository.findAll();
        assertThat(organisationUnitLevelList).hasSize(databaseSizeBeforeUpdate);
        OrganisationUnitLevel testOrganisationUnitLevel = organisationUnitLevelList.get(organisationUnitLevelList.size() - 1);
        assertThat(testOrganisationUnitLevel.getUid()).isEqualTo(UPDATED_UID);
        assertThat(testOrganisationUnitLevel.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testOrganisationUnitLevel.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testOrganisationUnitLevel.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testOrganisationUnitLevel.getLastUpdated()).isEqualTo(UPDATED_LAST_UPDATED);
        assertThat(testOrganisationUnitLevel.getLevel()).isEqualTo(UPDATED_LEVEL);
        assertThat(testOrganisationUnitLevel.getOfflineLevels()).isEqualTo(UPDATED_OFFLINE_LEVELS);
    }

    @Test
    @Transactional
    void putNonExistingOrganisationUnitLevel() throws Exception {
        int databaseSizeBeforeUpdate = organisationUnitLevelRepository.findAll().size();
        organisationUnitLevel.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrganisationUnitLevelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, organisationUnitLevel.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(organisationUnitLevel))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrganisationUnitLevel in the database
        List<OrganisationUnitLevel> organisationUnitLevelList = organisationUnitLevelRepository.findAll();
        assertThat(organisationUnitLevelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOrganisationUnitLevel() throws Exception {
        int databaseSizeBeforeUpdate = organisationUnitLevelRepository.findAll().size();
        organisationUnitLevel.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrganisationUnitLevelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(organisationUnitLevel))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrganisationUnitLevel in the database
        List<OrganisationUnitLevel> organisationUnitLevelList = organisationUnitLevelRepository.findAll();
        assertThat(organisationUnitLevelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOrganisationUnitLevel() throws Exception {
        int databaseSizeBeforeUpdate = organisationUnitLevelRepository.findAll().size();
        organisationUnitLevel.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrganisationUnitLevelMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(organisationUnitLevel))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrganisationUnitLevel in the database
        List<OrganisationUnitLevel> organisationUnitLevelList = organisationUnitLevelRepository.findAll();
        assertThat(organisationUnitLevelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOrganisationUnitLevelWithPatch() throws Exception {
        // Initialize the database
        organisationUnitLevelRepository.saveAndFlush(organisationUnitLevel);

        int databaseSizeBeforeUpdate = organisationUnitLevelRepository.findAll().size();

        // Update the organisationUnitLevel using partial update
        OrganisationUnitLevel partialUpdatedOrganisationUnitLevel = new OrganisationUnitLevel();
        partialUpdatedOrganisationUnitLevel.setId(organisationUnitLevel.getId());

        partialUpdatedOrganisationUnitLevel
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .created(UPDATED_CREATED)
            .lastUpdated(UPDATED_LAST_UPDATED)
            .level(UPDATED_LEVEL);

        restOrganisationUnitLevelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrganisationUnitLevel.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrganisationUnitLevel))
            )
            .andExpect(status().isOk());

        // Validate the OrganisationUnitLevel in the database
        List<OrganisationUnitLevel> organisationUnitLevelList = organisationUnitLevelRepository.findAll();
        assertThat(organisationUnitLevelList).hasSize(databaseSizeBeforeUpdate);
        OrganisationUnitLevel testOrganisationUnitLevel = organisationUnitLevelList.get(organisationUnitLevelList.size() - 1);
        assertThat(testOrganisationUnitLevel.getUid()).isEqualTo(DEFAULT_UID);
        assertThat(testOrganisationUnitLevel.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testOrganisationUnitLevel.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testOrganisationUnitLevel.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testOrganisationUnitLevel.getLastUpdated()).isEqualTo(UPDATED_LAST_UPDATED);
        assertThat(testOrganisationUnitLevel.getLevel()).isEqualTo(UPDATED_LEVEL);
        assertThat(testOrganisationUnitLevel.getOfflineLevels()).isEqualTo(DEFAULT_OFFLINE_LEVELS);
    }

    @Test
    @Transactional
    void fullUpdateOrganisationUnitLevelWithPatch() throws Exception {
        // Initialize the database
        organisationUnitLevelRepository.saveAndFlush(organisationUnitLevel);

        int databaseSizeBeforeUpdate = organisationUnitLevelRepository.findAll().size();

        // Update the organisationUnitLevel using partial update
        OrganisationUnitLevel partialUpdatedOrganisationUnitLevel = new OrganisationUnitLevel();
        partialUpdatedOrganisationUnitLevel.setId(organisationUnitLevel.getId());

        partialUpdatedOrganisationUnitLevel
            .uid(UPDATED_UID)
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .created(UPDATED_CREATED)
            .lastUpdated(UPDATED_LAST_UPDATED)
            .level(UPDATED_LEVEL)
            .offlineLevels(UPDATED_OFFLINE_LEVELS);

        restOrganisationUnitLevelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrganisationUnitLevel.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrganisationUnitLevel))
            )
            .andExpect(status().isOk());

        // Validate the OrganisationUnitLevel in the database
        List<OrganisationUnitLevel> organisationUnitLevelList = organisationUnitLevelRepository.findAll();
        assertThat(organisationUnitLevelList).hasSize(databaseSizeBeforeUpdate);
        OrganisationUnitLevel testOrganisationUnitLevel = organisationUnitLevelList.get(organisationUnitLevelList.size() - 1);
        assertThat(testOrganisationUnitLevel.getUid()).isEqualTo(UPDATED_UID);
        assertThat(testOrganisationUnitLevel.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testOrganisationUnitLevel.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testOrganisationUnitLevel.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testOrganisationUnitLevel.getLastUpdated()).isEqualTo(UPDATED_LAST_UPDATED);
        assertThat(testOrganisationUnitLevel.getLevel()).isEqualTo(UPDATED_LEVEL);
        assertThat(testOrganisationUnitLevel.getOfflineLevels()).isEqualTo(UPDATED_OFFLINE_LEVELS);
    }

    @Test
    @Transactional
    void patchNonExistingOrganisationUnitLevel() throws Exception {
        int databaseSizeBeforeUpdate = organisationUnitLevelRepository.findAll().size();
        organisationUnitLevel.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrganisationUnitLevelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, organisationUnitLevel.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(organisationUnitLevel))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrganisationUnitLevel in the database
        List<OrganisationUnitLevel> organisationUnitLevelList = organisationUnitLevelRepository.findAll();
        assertThat(organisationUnitLevelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOrganisationUnitLevel() throws Exception {
        int databaseSizeBeforeUpdate = organisationUnitLevelRepository.findAll().size();
        organisationUnitLevel.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrganisationUnitLevelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(organisationUnitLevel))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrganisationUnitLevel in the database
        List<OrganisationUnitLevel> organisationUnitLevelList = organisationUnitLevelRepository.findAll();
        assertThat(organisationUnitLevelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOrganisationUnitLevel() throws Exception {
        int databaseSizeBeforeUpdate = organisationUnitLevelRepository.findAll().size();
        organisationUnitLevel.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrganisationUnitLevelMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(organisationUnitLevel))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrganisationUnitLevel in the database
        List<OrganisationUnitLevel> organisationUnitLevelList = organisationUnitLevelRepository.findAll();
        assertThat(organisationUnitLevelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOrganisationUnitLevel() throws Exception {
        // Initialize the database
        organisationUnitLevelRepository.saveAndFlush(organisationUnitLevel);

        int databaseSizeBeforeDelete = organisationUnitLevelRepository.findAll().size();

        // Delete the organisationUnitLevel
        restOrganisationUnitLevelMockMvc
            .perform(delete(ENTITY_API_URL_ID, organisationUnitLevel.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<OrganisationUnitLevel> organisationUnitLevelList = organisationUnitLevelRepository.findAll();
        assertThat(organisationUnitLevelList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
