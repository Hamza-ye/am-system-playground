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
import org.nmcpye.activitiesmanagement.domain.PeriodType;
import org.nmcpye.activitiesmanagement.repository.PeriodTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link PeriodTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PeriodTypeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/period-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PeriodTypeRepository periodTypeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPeriodTypeMockMvc;

    private PeriodType periodType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PeriodType createEntity(EntityManager em) {
        PeriodType periodType = new PeriodType().name(DEFAULT_NAME);
        return periodType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PeriodType createUpdatedEntity(EntityManager em) {
        PeriodType periodType = new PeriodType().name(UPDATED_NAME);
        return periodType;
    }

    @BeforeEach
    public void initTest() {
        periodType = createEntity(em);
    }

    @Test
    @Transactional
    void createPeriodType() throws Exception {
        int databaseSizeBeforeCreate = periodTypeRepository.findAll().size();
        // Create the PeriodType
        restPeriodTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(periodType)))
            .andExpect(status().isCreated());

        // Validate the PeriodType in the database
        List<PeriodType> periodTypeList = periodTypeRepository.findAll();
        assertThat(periodTypeList).hasSize(databaseSizeBeforeCreate + 1);
        PeriodType testPeriodType = periodTypeList.get(periodTypeList.size() - 1);
        assertThat(testPeriodType.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createPeriodTypeWithExistingId() throws Exception {
        // Create the PeriodType with an existing ID
        periodType.setId(1L);

        int databaseSizeBeforeCreate = periodTypeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPeriodTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(periodType)))
            .andExpect(status().isBadRequest());

        // Validate the PeriodType in the database
        List<PeriodType> periodTypeList = periodTypeRepository.findAll();
        assertThat(periodTypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPeriodTypes() throws Exception {
        // Initialize the database
        periodTypeRepository.saveAndFlush(periodType);

        // Get all the periodTypeList
        restPeriodTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(periodType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getPeriodType() throws Exception {
        // Initialize the database
        periodTypeRepository.saveAndFlush(periodType);

        // Get the periodType
        restPeriodTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, periodType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(periodType.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingPeriodType() throws Exception {
        // Get the periodType
        restPeriodTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPeriodType() throws Exception {
        // Initialize the database
        periodTypeRepository.saveAndFlush(periodType);

        int databaseSizeBeforeUpdate = periodTypeRepository.findAll().size();

        // Update the periodType
        PeriodType updatedPeriodType = periodTypeRepository.findById(periodType.getId()).get();
        // Disconnect from session so that the updates on updatedPeriodType are not directly saved in db
        em.detach(updatedPeriodType);
        updatedPeriodType.name(UPDATED_NAME);

        restPeriodTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPeriodType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPeriodType))
            )
            .andExpect(status().isOk());

        // Validate the PeriodType in the database
        List<PeriodType> periodTypeList = periodTypeRepository.findAll();
        assertThat(periodTypeList).hasSize(databaseSizeBeforeUpdate);
        PeriodType testPeriodType = periodTypeList.get(periodTypeList.size() - 1);
        assertThat(testPeriodType.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingPeriodType() throws Exception {
        int databaseSizeBeforeUpdate = periodTypeRepository.findAll().size();
        periodType.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPeriodTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, periodType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(periodType))
            )
            .andExpect(status().isBadRequest());

        // Validate the PeriodType in the database
        List<PeriodType> periodTypeList = periodTypeRepository.findAll();
        assertThat(periodTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPeriodType() throws Exception {
        int databaseSizeBeforeUpdate = periodTypeRepository.findAll().size();
        periodType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPeriodTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(periodType))
            )
            .andExpect(status().isBadRequest());

        // Validate the PeriodType in the database
        List<PeriodType> periodTypeList = periodTypeRepository.findAll();
        assertThat(periodTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPeriodType() throws Exception {
        int databaseSizeBeforeUpdate = periodTypeRepository.findAll().size();
        periodType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPeriodTypeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(periodType)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PeriodType in the database
        List<PeriodType> periodTypeList = periodTypeRepository.findAll();
        assertThat(periodTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePeriodTypeWithPatch() throws Exception {
        // Initialize the database
        periodTypeRepository.saveAndFlush(periodType);

        int databaseSizeBeforeUpdate = periodTypeRepository.findAll().size();

        // Update the periodType using partial update
        PeriodType partialUpdatedPeriodType = new PeriodType();
        partialUpdatedPeriodType.setId(periodType.getId());

        partialUpdatedPeriodType.name(UPDATED_NAME);

        restPeriodTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPeriodType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPeriodType))
            )
            .andExpect(status().isOk());

        // Validate the PeriodType in the database
        List<PeriodType> periodTypeList = periodTypeRepository.findAll();
        assertThat(periodTypeList).hasSize(databaseSizeBeforeUpdate);
        PeriodType testPeriodType = periodTypeList.get(periodTypeList.size() - 1);
        assertThat(testPeriodType.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void fullUpdatePeriodTypeWithPatch() throws Exception {
        // Initialize the database
        periodTypeRepository.saveAndFlush(periodType);

        int databaseSizeBeforeUpdate = periodTypeRepository.findAll().size();

        // Update the periodType using partial update
        PeriodType partialUpdatedPeriodType = new PeriodType();
        partialUpdatedPeriodType.setId(periodType.getId());

        partialUpdatedPeriodType.name(UPDATED_NAME);

        restPeriodTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPeriodType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPeriodType))
            )
            .andExpect(status().isOk());

        // Validate the PeriodType in the database
        List<PeriodType> periodTypeList = periodTypeRepository.findAll();
        assertThat(periodTypeList).hasSize(databaseSizeBeforeUpdate);
        PeriodType testPeriodType = periodTypeList.get(periodTypeList.size() - 1);
        assertThat(testPeriodType.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingPeriodType() throws Exception {
        int databaseSizeBeforeUpdate = periodTypeRepository.findAll().size();
        periodType.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPeriodTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, periodType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(periodType))
            )
            .andExpect(status().isBadRequest());

        // Validate the PeriodType in the database
        List<PeriodType> periodTypeList = periodTypeRepository.findAll();
        assertThat(periodTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPeriodType() throws Exception {
        int databaseSizeBeforeUpdate = periodTypeRepository.findAll().size();
        periodType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPeriodTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(periodType))
            )
            .andExpect(status().isBadRequest());

        // Validate the PeriodType in the database
        List<PeriodType> periodTypeList = periodTypeRepository.findAll();
        assertThat(periodTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPeriodType() throws Exception {
        int databaseSizeBeforeUpdate = periodTypeRepository.findAll().size();
        periodType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPeriodTypeMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(periodType))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PeriodType in the database
        List<PeriodType> periodTypeList = periodTypeRepository.findAll();
        assertThat(periodTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePeriodType() throws Exception {
        // Initialize the database
        periodTypeRepository.saveAndFlush(periodType);

        int databaseSizeBeforeDelete = periodTypeRepository.findAll().size();

        // Delete the periodType
        restPeriodTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, periodType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PeriodType> periodTypeList = periodTypeRepository.findAll();
        assertThat(periodTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
