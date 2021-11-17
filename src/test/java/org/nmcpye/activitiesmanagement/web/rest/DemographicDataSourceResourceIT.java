package org.nmcpye.activitiesmanagement.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.nmcpye.activitiesmanagement.IntegrationTest;
import org.nmcpye.activitiesmanagement.domain.demographicdata.DemographicDataSource;
import org.nmcpye.activitiesmanagement.repository.DemographicDataSourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link DemographicDataSourceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DemographicDataSourceResourceIT {

    private static final String DEFAULT_UID = "AAAAAAAAAA";
    private static final String UPDATED_UID = "BBBBBBBBBB";

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Date DEFAULT_CREATED = Date.from(Instant.ofEpochMilli(0L));
    private static final Date UPDATED_CREATED = Date.from(Instant.now().truncatedTo(ChronoUnit.MILLIS));

    private static final Date DEFAULT_LAST_UPDATED = Date.from(Instant.ofEpochMilli(0L));
    private static final Date UPDATED_LAST_UPDATED = Date.from(Instant.now().truncatedTo(ChronoUnit.MILLIS));

    private static final String ENTITY_API_URL = "/api/demographic-data-sources";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DemographicDataSourceRepository demographicDataSourceRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDemographicDataSourceMockMvc;

    private DemographicDataSource demographicDataSource;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DemographicDataSource createEntity(EntityManager em) {
        DemographicDataSource demographicDataSource = new DemographicDataSource()
            .uid(DEFAULT_UID)
            .code(DEFAULT_CODE)
            .name(DEFAULT_NAME)
            .created(DEFAULT_CREATED)
            .lastUpdated(DEFAULT_LAST_UPDATED);
        return demographicDataSource;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DemographicDataSource createUpdatedEntity(EntityManager em) {
        DemographicDataSource demographicDataSource = new DemographicDataSource()
            .uid(UPDATED_UID)
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .created(UPDATED_CREATED)
            .lastUpdated(UPDATED_LAST_UPDATED);
        return demographicDataSource;
    }

    @BeforeEach
    public void initTest() {
        demographicDataSource = createEntity(em);
    }

    @Test
    @Transactional
    void createDemographicDataSource() throws Exception {
        int databaseSizeBeforeCreate = demographicDataSourceRepository.findAll().size();
        // Create the DemographicDataSource
        restDemographicDataSourceMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(demographicDataSource))
            )
            .andExpect(status().isCreated());

        // Validate the DemographicDataSource in the database
        List<DemographicDataSource> demographicDataSourceList = demographicDataSourceRepository.findAll();
        assertThat(demographicDataSourceList).hasSize(databaseSizeBeforeCreate + 1);
        DemographicDataSource testDemographicDataSource = demographicDataSourceList.get(demographicDataSourceList.size() - 1);
        assertThat(testDemographicDataSource.getUid()).isEqualTo(DEFAULT_UID);
        assertThat(testDemographicDataSource.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testDemographicDataSource.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testDemographicDataSource.getCreated()).isEqualTo(DEFAULT_CREATED);
        assertThat(testDemographicDataSource.getLastUpdated()).isEqualTo(DEFAULT_LAST_UPDATED);
    }

    @Test
    @Transactional
    void createDemographicDataSourceWithExistingId() throws Exception {
        // Create the DemographicDataSource with an existing ID
        demographicDataSource.setId(1L);

        int databaseSizeBeforeCreate = demographicDataSourceRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDemographicDataSourceMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(demographicDataSource))
            )
            .andExpect(status().isBadRequest());

        // Validate the DemographicDataSource in the database
        List<DemographicDataSource> demographicDataSourceList = demographicDataSourceRepository.findAll();
        assertThat(demographicDataSourceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkUidIsRequired() throws Exception {
        int databaseSizeBeforeTest = demographicDataSourceRepository.findAll().size();
        // set the field null
        demographicDataSource.setUid(null);

        // Create the DemographicDataSource, which fails.

        restDemographicDataSourceMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(demographicDataSource))
            )
            .andExpect(status().isBadRequest());

        List<DemographicDataSource> demographicDataSourceList = demographicDataSourceRepository.findAll();
        assertThat(demographicDataSourceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDemographicDataSources() throws Exception {
        // Initialize the database
        demographicDataSourceRepository.saveAndFlush(demographicDataSource);

        // Get all the demographicDataSourceList
        restDemographicDataSourceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(demographicDataSource.getId().intValue())))
            .andExpect(jsonPath("$.[*].uid").value(hasItem(DEFAULT_UID)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].created").value(hasItem(DEFAULT_CREATED.toString())))
            .andExpect(jsonPath("$.[*].lastUpdated").value(hasItem(DEFAULT_LAST_UPDATED.toString())));
    }

    @Test
    @Transactional
    void getDemographicDataSource() throws Exception {
        // Initialize the database
        demographicDataSourceRepository.saveAndFlush(demographicDataSource);

        // Get the demographicDataSource
        restDemographicDataSourceMockMvc
            .perform(get(ENTITY_API_URL_ID, demographicDataSource.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(demographicDataSource.getId().intValue()))
            .andExpect(jsonPath("$.uid").value(DEFAULT_UID))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.created").value(DEFAULT_CREATED.toString()))
            .andExpect(jsonPath("$.lastUpdated").value(DEFAULT_LAST_UPDATED.toString()));
    }

    @Test
    @Transactional
    void getNonExistingDemographicDataSource() throws Exception {
        // Get the demographicDataSource
        restDemographicDataSourceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDemographicDataSource() throws Exception {
        // Initialize the database
        demographicDataSourceRepository.saveAndFlush(demographicDataSource);

        int databaseSizeBeforeUpdate = demographicDataSourceRepository.findAll().size();

        // Update the demographicDataSource
        DemographicDataSource updatedDemographicDataSource = demographicDataSourceRepository.findById(demographicDataSource.getId()).get();
        // Disconnect from session so that the updates on updatedDemographicDataSource are not directly saved in db
        em.detach(updatedDemographicDataSource);
        updatedDemographicDataSource
            .uid(UPDATED_UID)
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .created(UPDATED_CREATED)
            .lastUpdated(UPDATED_LAST_UPDATED);

        restDemographicDataSourceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDemographicDataSource.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedDemographicDataSource))
            )
            .andExpect(status().isOk());

        // Validate the DemographicDataSource in the database
        List<DemographicDataSource> demographicDataSourceList = demographicDataSourceRepository.findAll();
        assertThat(demographicDataSourceList).hasSize(databaseSizeBeforeUpdate);
        DemographicDataSource testDemographicDataSource = demographicDataSourceList.get(demographicDataSourceList.size() - 1);
        assertThat(testDemographicDataSource.getUid()).isEqualTo(UPDATED_UID);
        assertThat(testDemographicDataSource.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testDemographicDataSource.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDemographicDataSource.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testDemographicDataSource.getLastUpdated()).isEqualTo(UPDATED_LAST_UPDATED);
    }

    @Test
    @Transactional
    void putNonExistingDemographicDataSource() throws Exception {
        int databaseSizeBeforeUpdate = demographicDataSourceRepository.findAll().size();
        demographicDataSource.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDemographicDataSourceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, demographicDataSource.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(demographicDataSource))
            )
            .andExpect(status().isBadRequest());

        // Validate the DemographicDataSource in the database
        List<DemographicDataSource> demographicDataSourceList = demographicDataSourceRepository.findAll();
        assertThat(demographicDataSourceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDemographicDataSource() throws Exception {
        int databaseSizeBeforeUpdate = demographicDataSourceRepository.findAll().size();
        demographicDataSource.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDemographicDataSourceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(demographicDataSource))
            )
            .andExpect(status().isBadRequest());

        // Validate the DemographicDataSource in the database
        List<DemographicDataSource> demographicDataSourceList = demographicDataSourceRepository.findAll();
        assertThat(demographicDataSourceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDemographicDataSource() throws Exception {
        int databaseSizeBeforeUpdate = demographicDataSourceRepository.findAll().size();
        demographicDataSource.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDemographicDataSourceMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(demographicDataSource))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DemographicDataSource in the database
        List<DemographicDataSource> demographicDataSourceList = demographicDataSourceRepository.findAll();
        assertThat(demographicDataSourceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDemographicDataSourceWithPatch() throws Exception {
        // Initialize the database
        demographicDataSourceRepository.saveAndFlush(demographicDataSource);

        int databaseSizeBeforeUpdate = demographicDataSourceRepository.findAll().size();

        // Update the demographicDataSource using partial update
        DemographicDataSource partialUpdatedDemographicDataSource = new DemographicDataSource();
        partialUpdatedDemographicDataSource.setId(demographicDataSource.getId());

        partialUpdatedDemographicDataSource.name(UPDATED_NAME);

        restDemographicDataSourceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDemographicDataSource.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDemographicDataSource))
            )
            .andExpect(status().isOk());

        // Validate the DemographicDataSource in the database
        List<DemographicDataSource> demographicDataSourceList = demographicDataSourceRepository.findAll();
        assertThat(demographicDataSourceList).hasSize(databaseSizeBeforeUpdate);
        DemographicDataSource testDemographicDataSource = demographicDataSourceList.get(demographicDataSourceList.size() - 1);
        assertThat(testDemographicDataSource.getUid()).isEqualTo(DEFAULT_UID);
        assertThat(testDemographicDataSource.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testDemographicDataSource.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDemographicDataSource.getCreated()).isEqualTo(DEFAULT_CREATED);
        assertThat(testDemographicDataSource.getLastUpdated()).isEqualTo(DEFAULT_LAST_UPDATED);
    }

    @Test
    @Transactional
    void fullUpdateDemographicDataSourceWithPatch() throws Exception {
        // Initialize the database
        demographicDataSourceRepository.saveAndFlush(demographicDataSource);

        int databaseSizeBeforeUpdate = demographicDataSourceRepository.findAll().size();

        // Update the demographicDataSource using partial update
        DemographicDataSource partialUpdatedDemographicDataSource = new DemographicDataSource();
        partialUpdatedDemographicDataSource.setId(demographicDataSource.getId());

        partialUpdatedDemographicDataSource
            .uid(UPDATED_UID)
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .created(UPDATED_CREATED)
            .lastUpdated(UPDATED_LAST_UPDATED);

        restDemographicDataSourceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDemographicDataSource.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDemographicDataSource))
            )
            .andExpect(status().isOk());

        // Validate the DemographicDataSource in the database
        List<DemographicDataSource> demographicDataSourceList = demographicDataSourceRepository.findAll();
        assertThat(demographicDataSourceList).hasSize(databaseSizeBeforeUpdate);
        DemographicDataSource testDemographicDataSource = demographicDataSourceList.get(demographicDataSourceList.size() - 1);
        assertThat(testDemographicDataSource.getUid()).isEqualTo(UPDATED_UID);
        assertThat(testDemographicDataSource.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testDemographicDataSource.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDemographicDataSource.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testDemographicDataSource.getLastUpdated()).isEqualTo(UPDATED_LAST_UPDATED);
    }

    @Test
    @Transactional
    void patchNonExistingDemographicDataSource() throws Exception {
        int databaseSizeBeforeUpdate = demographicDataSourceRepository.findAll().size();
        demographicDataSource.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDemographicDataSourceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, demographicDataSource.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(demographicDataSource))
            )
            .andExpect(status().isBadRequest());

        // Validate the DemographicDataSource in the database
        List<DemographicDataSource> demographicDataSourceList = demographicDataSourceRepository.findAll();
        assertThat(demographicDataSourceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDemographicDataSource() throws Exception {
        int databaseSizeBeforeUpdate = demographicDataSourceRepository.findAll().size();
        demographicDataSource.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDemographicDataSourceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(demographicDataSource))
            )
            .andExpect(status().isBadRequest());

        // Validate the DemographicDataSource in the database
        List<DemographicDataSource> demographicDataSourceList = demographicDataSourceRepository.findAll();
        assertThat(demographicDataSourceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDemographicDataSource() throws Exception {
        int databaseSizeBeforeUpdate = demographicDataSourceRepository.findAll().size();
        demographicDataSource.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDemographicDataSourceMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(demographicDataSource))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DemographicDataSource in the database
        List<DemographicDataSource> demographicDataSourceList = demographicDataSourceRepository.findAll();
        assertThat(demographicDataSourceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDemographicDataSource() throws Exception {
        // Initialize the database
        demographicDataSourceRepository.saveAndFlush(demographicDataSource);

        int databaseSizeBeforeDelete = demographicDataSourceRepository.findAll().size();

        // Delete the demographicDataSource
        restDemographicDataSourceMockMvc
            .perform(delete(ENTITY_API_URL_ID, demographicDataSource.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DemographicDataSource> demographicDataSourceList = demographicDataSourceRepository.findAll();
        assertThat(demographicDataSourceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
