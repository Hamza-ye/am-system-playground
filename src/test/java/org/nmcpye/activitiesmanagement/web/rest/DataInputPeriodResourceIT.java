package org.nmcpye.activitiesmanagement.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.nmcpye.activitiesmanagement.IntegrationTest;
import org.nmcpye.activitiesmanagement.domain.DataInputPeriod;
import org.nmcpye.activitiesmanagement.repository.DataInputPeriodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link DataInputPeriodResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DataInputPeriodResourceIT {

    private static final LocalDate DEFAULT_OPENING_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_OPENING_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_CLOSING_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CLOSING_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/data-input-periods";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DataInputPeriodRepository dataInputPeriodRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDataInputPeriodMockMvc;

    private DataInputPeriod dataInputPeriod;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DataInputPeriod createEntity(EntityManager em) {
        DataInputPeriod dataInputPeriod = new DataInputPeriod().openingDate(DEFAULT_OPENING_DATE).closingDate(DEFAULT_CLOSING_DATE);
        return dataInputPeriod;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DataInputPeriod createUpdatedEntity(EntityManager em) {
        DataInputPeriod dataInputPeriod = new DataInputPeriod().openingDate(UPDATED_OPENING_DATE).closingDate(UPDATED_CLOSING_DATE);
        return dataInputPeriod;
    }

    @BeforeEach
    public void initTest() {
        dataInputPeriod = createEntity(em);
    }

    @Test
    @Transactional
    void createDataInputPeriod() throws Exception {
        int databaseSizeBeforeCreate = dataInputPeriodRepository.findAll().size();
        // Create the DataInputPeriod
        restDataInputPeriodMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dataInputPeriod))
            )
            .andExpect(status().isCreated());

        // Validate the DataInputPeriod in the database
        List<DataInputPeriod> dataInputPeriodList = dataInputPeriodRepository.findAll();
        assertThat(dataInputPeriodList).hasSize(databaseSizeBeforeCreate + 1);
        DataInputPeriod testDataInputPeriod = dataInputPeriodList.get(dataInputPeriodList.size() - 1);
        assertThat(testDataInputPeriod.getOpeningDate()).isEqualTo(DEFAULT_OPENING_DATE);
        assertThat(testDataInputPeriod.getClosingDate()).isEqualTo(DEFAULT_CLOSING_DATE);
    }

    @Test
    @Transactional
    void createDataInputPeriodWithExistingId() throws Exception {
        // Create the DataInputPeriod with an existing ID
        dataInputPeriod.setId(1L);

        int databaseSizeBeforeCreate = dataInputPeriodRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDataInputPeriodMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dataInputPeriod))
            )
            .andExpect(status().isBadRequest());

        // Validate the DataInputPeriod in the database
        List<DataInputPeriod> dataInputPeriodList = dataInputPeriodRepository.findAll();
        assertThat(dataInputPeriodList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllDataInputPeriods() throws Exception {
        // Initialize the database
        dataInputPeriodRepository.saveAndFlush(dataInputPeriod);

        // Get all the dataInputPeriodList
        restDataInputPeriodMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dataInputPeriod.getId().intValue())))
            .andExpect(jsonPath("$.[*].openingDate").value(hasItem(DEFAULT_OPENING_DATE.toString())))
            .andExpect(jsonPath("$.[*].closingDate").value(hasItem(DEFAULT_CLOSING_DATE.toString())));
    }

    @Test
    @Transactional
    void getDataInputPeriod() throws Exception {
        // Initialize the database
        dataInputPeriodRepository.saveAndFlush(dataInputPeriod);

        // Get the dataInputPeriod
        restDataInputPeriodMockMvc
            .perform(get(ENTITY_API_URL_ID, dataInputPeriod.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(dataInputPeriod.getId().intValue()))
            .andExpect(jsonPath("$.openingDate").value(DEFAULT_OPENING_DATE.toString()))
            .andExpect(jsonPath("$.closingDate").value(DEFAULT_CLOSING_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingDataInputPeriod() throws Exception {
        // Get the dataInputPeriod
        restDataInputPeriodMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDataInputPeriod() throws Exception {
        // Initialize the database
        dataInputPeriodRepository.saveAndFlush(dataInputPeriod);

        int databaseSizeBeforeUpdate = dataInputPeriodRepository.findAll().size();

        // Update the dataInputPeriod
        DataInputPeriod updatedDataInputPeriod = dataInputPeriodRepository.findById(dataInputPeriod.getId()).get();
        // Disconnect from session so that the updates on updatedDataInputPeriod are not directly saved in db
        em.detach(updatedDataInputPeriod);
        updatedDataInputPeriod.openingDate(UPDATED_OPENING_DATE).closingDate(UPDATED_CLOSING_DATE);

        restDataInputPeriodMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDataInputPeriod.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedDataInputPeriod))
            )
            .andExpect(status().isOk());

        // Validate the DataInputPeriod in the database
        List<DataInputPeriod> dataInputPeriodList = dataInputPeriodRepository.findAll();
        assertThat(dataInputPeriodList).hasSize(databaseSizeBeforeUpdate);
        DataInputPeriod testDataInputPeriod = dataInputPeriodList.get(dataInputPeriodList.size() - 1);
        assertThat(testDataInputPeriod.getOpeningDate()).isEqualTo(UPDATED_OPENING_DATE);
        assertThat(testDataInputPeriod.getClosingDate()).isEqualTo(UPDATED_CLOSING_DATE);
    }

    @Test
    @Transactional
    void putNonExistingDataInputPeriod() throws Exception {
        int databaseSizeBeforeUpdate = dataInputPeriodRepository.findAll().size();
        dataInputPeriod.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDataInputPeriodMockMvc
            .perform(
                put(ENTITY_API_URL_ID, dataInputPeriod.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dataInputPeriod))
            )
            .andExpect(status().isBadRequest());

        // Validate the DataInputPeriod in the database
        List<DataInputPeriod> dataInputPeriodList = dataInputPeriodRepository.findAll();
        assertThat(dataInputPeriodList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDataInputPeriod() throws Exception {
        int databaseSizeBeforeUpdate = dataInputPeriodRepository.findAll().size();
        dataInputPeriod.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDataInputPeriodMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dataInputPeriod))
            )
            .andExpect(status().isBadRequest());

        // Validate the DataInputPeriod in the database
        List<DataInputPeriod> dataInputPeriodList = dataInputPeriodRepository.findAll();
        assertThat(dataInputPeriodList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDataInputPeriod() throws Exception {
        int databaseSizeBeforeUpdate = dataInputPeriodRepository.findAll().size();
        dataInputPeriod.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDataInputPeriodMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dataInputPeriod))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DataInputPeriod in the database
        List<DataInputPeriod> dataInputPeriodList = dataInputPeriodRepository.findAll();
        assertThat(dataInputPeriodList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDataInputPeriodWithPatch() throws Exception {
        // Initialize the database
        dataInputPeriodRepository.saveAndFlush(dataInputPeriod);

        int databaseSizeBeforeUpdate = dataInputPeriodRepository.findAll().size();

        // Update the dataInputPeriod using partial update
        DataInputPeriod partialUpdatedDataInputPeriod = new DataInputPeriod();
        partialUpdatedDataInputPeriod.setId(dataInputPeriod.getId());

        partialUpdatedDataInputPeriod.openingDate(UPDATED_OPENING_DATE).closingDate(UPDATED_CLOSING_DATE);

        restDataInputPeriodMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDataInputPeriod.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDataInputPeriod))
            )
            .andExpect(status().isOk());

        // Validate the DataInputPeriod in the database
        List<DataInputPeriod> dataInputPeriodList = dataInputPeriodRepository.findAll();
        assertThat(dataInputPeriodList).hasSize(databaseSizeBeforeUpdate);
        DataInputPeriod testDataInputPeriod = dataInputPeriodList.get(dataInputPeriodList.size() - 1);
        assertThat(testDataInputPeriod.getOpeningDate()).isEqualTo(UPDATED_OPENING_DATE);
        assertThat(testDataInputPeriod.getClosingDate()).isEqualTo(UPDATED_CLOSING_DATE);
    }

    @Test
    @Transactional
    void fullUpdateDataInputPeriodWithPatch() throws Exception {
        // Initialize the database
        dataInputPeriodRepository.saveAndFlush(dataInputPeriod);

        int databaseSizeBeforeUpdate = dataInputPeriodRepository.findAll().size();

        // Update the dataInputPeriod using partial update
        DataInputPeriod partialUpdatedDataInputPeriod = new DataInputPeriod();
        partialUpdatedDataInputPeriod.setId(dataInputPeriod.getId());

        partialUpdatedDataInputPeriod.openingDate(UPDATED_OPENING_DATE).closingDate(UPDATED_CLOSING_DATE);

        restDataInputPeriodMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDataInputPeriod.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDataInputPeriod))
            )
            .andExpect(status().isOk());

        // Validate the DataInputPeriod in the database
        List<DataInputPeriod> dataInputPeriodList = dataInputPeriodRepository.findAll();
        assertThat(dataInputPeriodList).hasSize(databaseSizeBeforeUpdate);
        DataInputPeriod testDataInputPeriod = dataInputPeriodList.get(dataInputPeriodList.size() - 1);
        assertThat(testDataInputPeriod.getOpeningDate()).isEqualTo(UPDATED_OPENING_DATE);
        assertThat(testDataInputPeriod.getClosingDate()).isEqualTo(UPDATED_CLOSING_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingDataInputPeriod() throws Exception {
        int databaseSizeBeforeUpdate = dataInputPeriodRepository.findAll().size();
        dataInputPeriod.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDataInputPeriodMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, dataInputPeriod.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(dataInputPeriod))
            )
            .andExpect(status().isBadRequest());

        // Validate the DataInputPeriod in the database
        List<DataInputPeriod> dataInputPeriodList = dataInputPeriodRepository.findAll();
        assertThat(dataInputPeriodList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDataInputPeriod() throws Exception {
        int databaseSizeBeforeUpdate = dataInputPeriodRepository.findAll().size();
        dataInputPeriod.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDataInputPeriodMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(dataInputPeriod))
            )
            .andExpect(status().isBadRequest());

        // Validate the DataInputPeriod in the database
        List<DataInputPeriod> dataInputPeriodList = dataInputPeriodRepository.findAll();
        assertThat(dataInputPeriodList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDataInputPeriod() throws Exception {
        int databaseSizeBeforeUpdate = dataInputPeriodRepository.findAll().size();
        dataInputPeriod.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDataInputPeriodMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(dataInputPeriod))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DataInputPeriod in the database
        List<DataInputPeriod> dataInputPeriodList = dataInputPeriodRepository.findAll();
        assertThat(dataInputPeriodList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDataInputPeriod() throws Exception {
        // Initialize the database
        dataInputPeriodRepository.saveAndFlush(dataInputPeriod);

        int databaseSizeBeforeDelete = dataInputPeriodRepository.findAll().size();

        // Delete the dataInputPeriod
        restDataInputPeriodMockMvc
            .perform(delete(ENTITY_API_URL_ID, dataInputPeriod.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DataInputPeriod> dataInputPeriodList = dataInputPeriodRepository.findAll();
        assertThat(dataInputPeriodList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
