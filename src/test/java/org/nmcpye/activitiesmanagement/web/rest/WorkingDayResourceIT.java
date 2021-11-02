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
import org.nmcpye.activitiesmanagement.domain.WorkingDay;
import org.nmcpye.activitiesmanagement.repository.WorkingDayRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link WorkingDayResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class WorkingDayResourceIT {

    private static final Integer DEFAULT_DAY_NO = 0;
    private static final Integer UPDATED_DAY_NO = 1;

    private static final String DEFAULT_DAY_LABEL = "AAAAAAAAAA";
    private static final String UPDATED_DAY_LABEL = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/working-days";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private WorkingDayRepository workingDayRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restWorkingDayMockMvc;

    private WorkingDay workingDay;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WorkingDay createEntity(EntityManager em) {
        WorkingDay workingDay = new WorkingDay().dayNo(DEFAULT_DAY_NO).dayLabel(DEFAULT_DAY_LABEL);
        return workingDay;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WorkingDay createUpdatedEntity(EntityManager em) {
        WorkingDay workingDay = new WorkingDay().dayNo(UPDATED_DAY_NO).dayLabel(UPDATED_DAY_LABEL);
        return workingDay;
    }

    @BeforeEach
    public void initTest() {
        workingDay = createEntity(em);
    }

    @Test
    @Transactional
    void createWorkingDay() throws Exception {
        int databaseSizeBeforeCreate = workingDayRepository.findAll().size();
        // Create the WorkingDay
        restWorkingDayMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(workingDay)))
            .andExpect(status().isCreated());

        // Validate the WorkingDay in the database
        List<WorkingDay> workingDayList = workingDayRepository.findAll();
        assertThat(workingDayList).hasSize(databaseSizeBeforeCreate + 1);
        WorkingDay testWorkingDay = workingDayList.get(workingDayList.size() - 1);
        assertThat(testWorkingDay.getDayNo()).isEqualTo(DEFAULT_DAY_NO);
        assertThat(testWorkingDay.getDayLabel()).isEqualTo(DEFAULT_DAY_LABEL);
    }

    @Test
    @Transactional
    void createWorkingDayWithExistingId() throws Exception {
        // Create the WorkingDay with an existing ID
        workingDay.setId(1L);

        int databaseSizeBeforeCreate = workingDayRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restWorkingDayMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(workingDay)))
            .andExpect(status().isBadRequest());

        // Validate the WorkingDay in the database
        List<WorkingDay> workingDayList = workingDayRepository.findAll();
        assertThat(workingDayList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDayNoIsRequired() throws Exception {
        int databaseSizeBeforeTest = workingDayRepository.findAll().size();
        // set the field null
        workingDay.setDayNo(null);

        // Create the WorkingDay, which fails.

        restWorkingDayMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(workingDay)))
            .andExpect(status().isBadRequest());

        List<WorkingDay> workingDayList = workingDayRepository.findAll();
        assertThat(workingDayList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDayLabelIsRequired() throws Exception {
        int databaseSizeBeforeTest = workingDayRepository.findAll().size();
        // set the field null
        workingDay.setDayLabel(null);

        // Create the WorkingDay, which fails.

        restWorkingDayMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(workingDay)))
            .andExpect(status().isBadRequest());

        List<WorkingDay> workingDayList = workingDayRepository.findAll();
        assertThat(workingDayList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllWorkingDays() throws Exception {
        // Initialize the database
        workingDayRepository.saveAndFlush(workingDay);

        // Get all the workingDayList
        restWorkingDayMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(workingDay.getId().intValue())))
            .andExpect(jsonPath("$.[*].dayNo").value(hasItem(DEFAULT_DAY_NO)))
            .andExpect(jsonPath("$.[*].dayLabel").value(hasItem(DEFAULT_DAY_LABEL)));
    }

    @Test
    @Transactional
    void getWorkingDay() throws Exception {
        // Initialize the database
        workingDayRepository.saveAndFlush(workingDay);

        // Get the workingDay
        restWorkingDayMockMvc
            .perform(get(ENTITY_API_URL_ID, workingDay.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(workingDay.getId().intValue()))
            .andExpect(jsonPath("$.dayNo").value(DEFAULT_DAY_NO))
            .andExpect(jsonPath("$.dayLabel").value(DEFAULT_DAY_LABEL));
    }

    @Test
    @Transactional
    void getNonExistingWorkingDay() throws Exception {
        // Get the workingDay
        restWorkingDayMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewWorkingDay() throws Exception {
        // Initialize the database
        workingDayRepository.saveAndFlush(workingDay);

        int databaseSizeBeforeUpdate = workingDayRepository.findAll().size();

        // Update the workingDay
        WorkingDay updatedWorkingDay = workingDayRepository.findById(workingDay.getId()).get();
        // Disconnect from session so that the updates on updatedWorkingDay are not directly saved in db
        em.detach(updatedWorkingDay);
        updatedWorkingDay.dayNo(UPDATED_DAY_NO).dayLabel(UPDATED_DAY_LABEL);

        restWorkingDayMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedWorkingDay.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedWorkingDay))
            )
            .andExpect(status().isOk());

        // Validate the WorkingDay in the database
        List<WorkingDay> workingDayList = workingDayRepository.findAll();
        assertThat(workingDayList).hasSize(databaseSizeBeforeUpdate);
        WorkingDay testWorkingDay = workingDayList.get(workingDayList.size() - 1);
        assertThat(testWorkingDay.getDayNo()).isEqualTo(UPDATED_DAY_NO);
        assertThat(testWorkingDay.getDayLabel()).isEqualTo(UPDATED_DAY_LABEL);
    }

    @Test
    @Transactional
    void putNonExistingWorkingDay() throws Exception {
        int databaseSizeBeforeUpdate = workingDayRepository.findAll().size();
        workingDay.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWorkingDayMockMvc
            .perform(
                put(ENTITY_API_URL_ID, workingDay.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workingDay))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkingDay in the database
        List<WorkingDay> workingDayList = workingDayRepository.findAll();
        assertThat(workingDayList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchWorkingDay() throws Exception {
        int databaseSizeBeforeUpdate = workingDayRepository.findAll().size();
        workingDay.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkingDayMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workingDay))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkingDay in the database
        List<WorkingDay> workingDayList = workingDayRepository.findAll();
        assertThat(workingDayList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamWorkingDay() throws Exception {
        int databaseSizeBeforeUpdate = workingDayRepository.findAll().size();
        workingDay.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkingDayMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(workingDay)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the WorkingDay in the database
        List<WorkingDay> workingDayList = workingDayRepository.findAll();
        assertThat(workingDayList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateWorkingDayWithPatch() throws Exception {
        // Initialize the database
        workingDayRepository.saveAndFlush(workingDay);

        int databaseSizeBeforeUpdate = workingDayRepository.findAll().size();

        // Update the workingDay using partial update
        WorkingDay partialUpdatedWorkingDay = new WorkingDay();
        partialUpdatedWorkingDay.setId(workingDay.getId());

        partialUpdatedWorkingDay.dayNo(UPDATED_DAY_NO);

        restWorkingDayMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWorkingDay.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWorkingDay))
            )
            .andExpect(status().isOk());

        // Validate the WorkingDay in the database
        List<WorkingDay> workingDayList = workingDayRepository.findAll();
        assertThat(workingDayList).hasSize(databaseSizeBeforeUpdate);
        WorkingDay testWorkingDay = workingDayList.get(workingDayList.size() - 1);
        assertThat(testWorkingDay.getDayNo()).isEqualTo(UPDATED_DAY_NO);
        assertThat(testWorkingDay.getDayLabel()).isEqualTo(DEFAULT_DAY_LABEL);
    }

    @Test
    @Transactional
    void fullUpdateWorkingDayWithPatch() throws Exception {
        // Initialize the database
        workingDayRepository.saveAndFlush(workingDay);

        int databaseSizeBeforeUpdate = workingDayRepository.findAll().size();

        // Update the workingDay using partial update
        WorkingDay partialUpdatedWorkingDay = new WorkingDay();
        partialUpdatedWorkingDay.setId(workingDay.getId());

        partialUpdatedWorkingDay.dayNo(UPDATED_DAY_NO).dayLabel(UPDATED_DAY_LABEL);

        restWorkingDayMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWorkingDay.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWorkingDay))
            )
            .andExpect(status().isOk());

        // Validate the WorkingDay in the database
        List<WorkingDay> workingDayList = workingDayRepository.findAll();
        assertThat(workingDayList).hasSize(databaseSizeBeforeUpdate);
        WorkingDay testWorkingDay = workingDayList.get(workingDayList.size() - 1);
        assertThat(testWorkingDay.getDayNo()).isEqualTo(UPDATED_DAY_NO);
        assertThat(testWorkingDay.getDayLabel()).isEqualTo(UPDATED_DAY_LABEL);
    }

    @Test
    @Transactional
    void patchNonExistingWorkingDay() throws Exception {
        int databaseSizeBeforeUpdate = workingDayRepository.findAll().size();
        workingDay.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWorkingDayMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, workingDay.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(workingDay))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkingDay in the database
        List<WorkingDay> workingDayList = workingDayRepository.findAll();
        assertThat(workingDayList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchWorkingDay() throws Exception {
        int databaseSizeBeforeUpdate = workingDayRepository.findAll().size();
        workingDay.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkingDayMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(workingDay))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkingDay in the database
        List<WorkingDay> workingDayList = workingDayRepository.findAll();
        assertThat(workingDayList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamWorkingDay() throws Exception {
        int databaseSizeBeforeUpdate = workingDayRepository.findAll().size();
        workingDay.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkingDayMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(workingDay))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the WorkingDay in the database
        List<WorkingDay> workingDayList = workingDayRepository.findAll();
        assertThat(workingDayList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteWorkingDay() throws Exception {
        // Initialize the database
        workingDayRepository.saveAndFlush(workingDay);

        int databaseSizeBeforeDelete = workingDayRepository.findAll().size();

        // Delete the workingDay
        restWorkingDayMockMvc
            .perform(delete(ENTITY_API_URL_ID, workingDay.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<WorkingDay> workingDayList = workingDayRepository.findAll();
        assertThat(workingDayList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
