package org.nmcpye.activitiesmanagement.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.nmcpye.activitiesmanagement.IntegrationTest;
import org.nmcpye.activitiesmanagement.domain.StatusOfCoverage;
import org.nmcpye.activitiesmanagement.repository.StatusOfCoverageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link StatusOfCoverageResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class StatusOfCoverageResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/status-of-coverages";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private StatusOfCoverageRepository statusOfCoverageRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restStatusOfCoverageMockMvc;

    private StatusOfCoverage statusOfCoverage;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StatusOfCoverage createEntity(EntityManager em) {
        StatusOfCoverage statusOfCoverage = new StatusOfCoverage().code(DEFAULT_CODE).status(DEFAULT_STATUS);
        return statusOfCoverage;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StatusOfCoverage createUpdatedEntity(EntityManager em) {
        StatusOfCoverage statusOfCoverage = new StatusOfCoverage().code(UPDATED_CODE).status(UPDATED_STATUS);
        return statusOfCoverage;
    }

    @BeforeEach
    public void initTest() {
        statusOfCoverage = createEntity(em);
    }

    @Test
    @Transactional
    void createStatusOfCoverage() throws Exception {
        int databaseSizeBeforeCreate = statusOfCoverageRepository.findAll().size();
        // Create the StatusOfCoverage
        restStatusOfCoverageMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(statusOfCoverage))
            )
            .andExpect(status().isCreated());

        // Validate the StatusOfCoverage in the database
        List<StatusOfCoverage> statusOfCoverageList = statusOfCoverageRepository.findAll();
        assertThat(statusOfCoverageList).hasSize(databaseSizeBeforeCreate + 1);
        StatusOfCoverage testStatusOfCoverage = statusOfCoverageList.get(statusOfCoverageList.size() - 1);
        assertThat(testStatusOfCoverage.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testStatusOfCoverage.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void createStatusOfCoverageWithExistingId() throws Exception {
        // Create the StatusOfCoverage with an existing ID
        statusOfCoverage.setId(1L);

        int databaseSizeBeforeCreate = statusOfCoverageRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restStatusOfCoverageMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(statusOfCoverage))
            )
            .andExpect(status().isBadRequest());

        // Validate the StatusOfCoverage in the database
        List<StatusOfCoverage> statusOfCoverageList = statusOfCoverageRepository.findAll();
        assertThat(statusOfCoverageList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = statusOfCoverageRepository.findAll().size();
        // set the field null
        statusOfCoverage.setCode(null);

        // Create the StatusOfCoverage, which fails.

        restStatusOfCoverageMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(statusOfCoverage))
            )
            .andExpect(status().isBadRequest());

        List<StatusOfCoverage> statusOfCoverageList = statusOfCoverageRepository.findAll();
        assertThat(statusOfCoverageList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = statusOfCoverageRepository.findAll().size();
        // set the field null
        statusOfCoverage.setStatus(null);

        // Create the StatusOfCoverage, which fails.

        restStatusOfCoverageMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(statusOfCoverage))
            )
            .andExpect(status().isBadRequest());

        List<StatusOfCoverage> statusOfCoverageList = statusOfCoverageRepository.findAll();
        assertThat(statusOfCoverageList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllStatusOfCoverages() throws Exception {
        // Initialize the database
        statusOfCoverageRepository.saveAndFlush(statusOfCoverage);

        // Get all the statusOfCoverageList
        restStatusOfCoverageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(statusOfCoverage.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)));
    }

    @Test
    @Transactional
    void getStatusOfCoverage() throws Exception {
        // Initialize the database
        statusOfCoverageRepository.saveAndFlush(statusOfCoverage);

        // Get the statusOfCoverage
        restStatusOfCoverageMockMvc
            .perform(get(ENTITY_API_URL_ID, statusOfCoverage.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(statusOfCoverage.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS));
    }

    @Test
    @Transactional
    void getNonExistingStatusOfCoverage() throws Exception {
        // Get the statusOfCoverage
        restStatusOfCoverageMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewStatusOfCoverage() throws Exception {
        // Initialize the database
        statusOfCoverageRepository.saveAndFlush(statusOfCoverage);

        int databaseSizeBeforeUpdate = statusOfCoverageRepository.findAll().size();

        // Update the statusOfCoverage
        StatusOfCoverage updatedStatusOfCoverage = statusOfCoverageRepository.findById(statusOfCoverage.getId()).get();
        // Disconnect from session so that the updates on updatedStatusOfCoverage are not directly saved in db
        em.detach(updatedStatusOfCoverage);
        updatedStatusOfCoverage.code(UPDATED_CODE).status(UPDATED_STATUS);

        restStatusOfCoverageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedStatusOfCoverage.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedStatusOfCoverage))
            )
            .andExpect(status().isOk());

        // Validate the StatusOfCoverage in the database
        List<StatusOfCoverage> statusOfCoverageList = statusOfCoverageRepository.findAll();
        assertThat(statusOfCoverageList).hasSize(databaseSizeBeforeUpdate);
        StatusOfCoverage testStatusOfCoverage = statusOfCoverageList.get(statusOfCoverageList.size() - 1);
        assertThat(testStatusOfCoverage.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testStatusOfCoverage.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void putNonExistingStatusOfCoverage() throws Exception {
        int databaseSizeBeforeUpdate = statusOfCoverageRepository.findAll().size();
        statusOfCoverage.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStatusOfCoverageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, statusOfCoverage.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(statusOfCoverage))
            )
            .andExpect(status().isBadRequest());

        // Validate the StatusOfCoverage in the database
        List<StatusOfCoverage> statusOfCoverageList = statusOfCoverageRepository.findAll();
        assertThat(statusOfCoverageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchStatusOfCoverage() throws Exception {
        int databaseSizeBeforeUpdate = statusOfCoverageRepository.findAll().size();
        statusOfCoverage.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStatusOfCoverageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(statusOfCoverage))
            )
            .andExpect(status().isBadRequest());

        // Validate the StatusOfCoverage in the database
        List<StatusOfCoverage> statusOfCoverageList = statusOfCoverageRepository.findAll();
        assertThat(statusOfCoverageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamStatusOfCoverage() throws Exception {
        int databaseSizeBeforeUpdate = statusOfCoverageRepository.findAll().size();
        statusOfCoverage.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStatusOfCoverageMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(statusOfCoverage))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the StatusOfCoverage in the database
        List<StatusOfCoverage> statusOfCoverageList = statusOfCoverageRepository.findAll();
        assertThat(statusOfCoverageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateStatusOfCoverageWithPatch() throws Exception {
        // Initialize the database
        statusOfCoverageRepository.saveAndFlush(statusOfCoverage);

        int databaseSizeBeforeUpdate = statusOfCoverageRepository.findAll().size();

        // Update the statusOfCoverage using partial update
        StatusOfCoverage partialUpdatedStatusOfCoverage = new StatusOfCoverage();
        partialUpdatedStatusOfCoverage.setId(statusOfCoverage.getId());

        partialUpdatedStatusOfCoverage.code(UPDATED_CODE);

        restStatusOfCoverageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStatusOfCoverage.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedStatusOfCoverage))
            )
            .andExpect(status().isOk());

        // Validate the StatusOfCoverage in the database
        List<StatusOfCoverage> statusOfCoverageList = statusOfCoverageRepository.findAll();
        assertThat(statusOfCoverageList).hasSize(databaseSizeBeforeUpdate);
        StatusOfCoverage testStatusOfCoverage = statusOfCoverageList.get(statusOfCoverageList.size() - 1);
        assertThat(testStatusOfCoverage.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testStatusOfCoverage.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void fullUpdateStatusOfCoverageWithPatch() throws Exception {
        // Initialize the database
        statusOfCoverageRepository.saveAndFlush(statusOfCoverage);

        int databaseSizeBeforeUpdate = statusOfCoverageRepository.findAll().size();

        // Update the statusOfCoverage using partial update
        StatusOfCoverage partialUpdatedStatusOfCoverage = new StatusOfCoverage();
        partialUpdatedStatusOfCoverage.setId(statusOfCoverage.getId());

        partialUpdatedStatusOfCoverage.code(UPDATED_CODE).status(UPDATED_STATUS);

        restStatusOfCoverageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStatusOfCoverage.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedStatusOfCoverage))
            )
            .andExpect(status().isOk());

        // Validate the StatusOfCoverage in the database
        List<StatusOfCoverage> statusOfCoverageList = statusOfCoverageRepository.findAll();
        assertThat(statusOfCoverageList).hasSize(databaseSizeBeforeUpdate);
        StatusOfCoverage testStatusOfCoverage = statusOfCoverageList.get(statusOfCoverageList.size() - 1);
        assertThat(testStatusOfCoverage.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testStatusOfCoverage.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void patchNonExistingStatusOfCoverage() throws Exception {
        int databaseSizeBeforeUpdate = statusOfCoverageRepository.findAll().size();
        statusOfCoverage.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStatusOfCoverageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, statusOfCoverage.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(statusOfCoverage))
            )
            .andExpect(status().isBadRequest());

        // Validate the StatusOfCoverage in the database
        List<StatusOfCoverage> statusOfCoverageList = statusOfCoverageRepository.findAll();
        assertThat(statusOfCoverageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchStatusOfCoverage() throws Exception {
        int databaseSizeBeforeUpdate = statusOfCoverageRepository.findAll().size();
        statusOfCoverage.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStatusOfCoverageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(statusOfCoverage))
            )
            .andExpect(status().isBadRequest());

        // Validate the StatusOfCoverage in the database
        List<StatusOfCoverage> statusOfCoverageList = statusOfCoverageRepository.findAll();
        assertThat(statusOfCoverageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamStatusOfCoverage() throws Exception {
        int databaseSizeBeforeUpdate = statusOfCoverageRepository.findAll().size();
        statusOfCoverage.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStatusOfCoverageMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(statusOfCoverage))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the StatusOfCoverage in the database
        List<StatusOfCoverage> statusOfCoverageList = statusOfCoverageRepository.findAll();
        assertThat(statusOfCoverageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteStatusOfCoverage() throws Exception {
        // Initialize the database
        statusOfCoverageRepository.saveAndFlush(statusOfCoverage);

        int databaseSizeBeforeDelete = statusOfCoverageRepository.findAll().size();

        // Delete the statusOfCoverage
        restStatusOfCoverageMockMvc
            .perform(delete(ENTITY_API_URL_ID, statusOfCoverage.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<StatusOfCoverage> statusOfCoverageList = statusOfCoverageRepository.findAll();
        assertThat(statusOfCoverageList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
