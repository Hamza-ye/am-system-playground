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
import org.nmcpye.activitiesmanagement.domain.Period;
import org.nmcpye.activitiesmanagement.domain.PeriodType;
import org.nmcpye.activitiesmanagement.repository.PeriodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link PeriodResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PeriodResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/periods";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PeriodRepository periodRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPeriodMockMvc;

    private Period period;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Period createEntity(EntityManager em) {
        Period period = new Period().name(DEFAULT_NAME).startDate(DEFAULT_START_DATE).endDate(DEFAULT_END_DATE);
        // Add required entity
        PeriodType periodType;
        if (TestUtil.findAll(em, PeriodType.class).isEmpty()) {
            periodType = PeriodTypeResourceIT.createEntity(em);
            em.persist(periodType);
            em.flush();
        } else {
            periodType = TestUtil.findAll(em, PeriodType.class).get(0);
        }
        period.setPeriodType(periodType);
        return period;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Period createUpdatedEntity(EntityManager em) {
        Period period = new Period().name(UPDATED_NAME).startDate(UPDATED_START_DATE).endDate(UPDATED_END_DATE);
        // Add required entity
        PeriodType periodType;
        if (TestUtil.findAll(em, PeriodType.class).isEmpty()) {
            periodType = PeriodTypeResourceIT.createUpdatedEntity(em);
            em.persist(periodType);
            em.flush();
        } else {
            periodType = TestUtil.findAll(em, PeriodType.class).get(0);
        }
        period.setPeriodType(periodType);
        return period;
    }

    @BeforeEach
    public void initTest() {
        period = createEntity(em);
    }

    @Test
    @Transactional
    void createPeriod() throws Exception {
        int databaseSizeBeforeCreate = periodRepository.findAll().size();
        // Create the Period
        restPeriodMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(period)))
            .andExpect(status().isCreated());

        // Validate the Period in the database
        List<Period> periodList = periodRepository.findAll();
        assertThat(periodList).hasSize(databaseSizeBeforeCreate + 1);
        Period testPeriod = periodList.get(periodList.size() - 1);
        assertThat(testPeriod.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPeriod.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testPeriod.getEndDate()).isEqualTo(DEFAULT_END_DATE);
    }

    @Test
    @Transactional
    void createPeriodWithExistingId() throws Exception {
        // Create the Period with an existing ID
        period.setId(1L);

        int databaseSizeBeforeCreate = periodRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPeriodMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(period)))
            .andExpect(status().isBadRequest());

        // Validate the Period in the database
        List<Period> periodList = periodRepository.findAll();
        assertThat(periodList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkStartDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = periodRepository.findAll().size();
        // set the field null
        period.setStartDate(null);

        // Create the Period, which fails.

        restPeriodMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(period)))
            .andExpect(status().isBadRequest());

        List<Period> periodList = periodRepository.findAll();
        assertThat(periodList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEndDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = periodRepository.findAll().size();
        // set the field null
        period.setEndDate(null);

        // Create the Period, which fails.

        restPeriodMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(period)))
            .andExpect(status().isBadRequest());

        List<Period> periodList = periodRepository.findAll();
        assertThat(periodList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPeriods() throws Exception {
        // Initialize the database
        periodRepository.saveAndFlush(period);

        // Get all the periodList
        restPeriodMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(period.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())));
    }

    @Test
    @Transactional
    void getPeriod() throws Exception {
        // Initialize the database
        periodRepository.saveAndFlush(period);

        // Get the period
        restPeriodMockMvc
            .perform(get(ENTITY_API_URL_ID, period.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(period.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingPeriod() throws Exception {
        // Get the period
        restPeriodMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPeriod() throws Exception {
        // Initialize the database
        periodRepository.saveAndFlush(period);

        int databaseSizeBeforeUpdate = periodRepository.findAll().size();

        // Update the period
        Period updatedPeriod = periodRepository.findById(period.getId()).get();
        // Disconnect from session so that the updates on updatedPeriod are not directly saved in db
        em.detach(updatedPeriod);
        updatedPeriod.name(UPDATED_NAME).startDate(UPDATED_START_DATE).endDate(UPDATED_END_DATE);

        restPeriodMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPeriod.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPeriod))
            )
            .andExpect(status().isOk());

        // Validate the Period in the database
        List<Period> periodList = periodRepository.findAll();
        assertThat(periodList).hasSize(databaseSizeBeforeUpdate);
        Period testPeriod = periodList.get(periodList.size() - 1);
        assertThat(testPeriod.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPeriod.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testPeriod.getEndDate()).isEqualTo(UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void putNonExistingPeriod() throws Exception {
        int databaseSizeBeforeUpdate = periodRepository.findAll().size();
        period.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPeriodMockMvc
            .perform(
                put(ENTITY_API_URL_ID, period.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(period))
            )
            .andExpect(status().isBadRequest());

        // Validate the Period in the database
        List<Period> periodList = periodRepository.findAll();
        assertThat(periodList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPeriod() throws Exception {
        int databaseSizeBeforeUpdate = periodRepository.findAll().size();
        period.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPeriodMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(period))
            )
            .andExpect(status().isBadRequest());

        // Validate the Period in the database
        List<Period> periodList = periodRepository.findAll();
        assertThat(periodList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPeriod() throws Exception {
        int databaseSizeBeforeUpdate = periodRepository.findAll().size();
        period.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPeriodMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(period)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Period in the database
        List<Period> periodList = periodRepository.findAll();
        assertThat(periodList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePeriodWithPatch() throws Exception {
        // Initialize the database
        periodRepository.saveAndFlush(period);

        int databaseSizeBeforeUpdate = periodRepository.findAll().size();

        // Update the period using partial update
        Period partialUpdatedPeriod = new Period();
        partialUpdatedPeriod.setId(period.getId());

        restPeriodMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPeriod.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPeriod))
            )
            .andExpect(status().isOk());

        // Validate the Period in the database
        List<Period> periodList = periodRepository.findAll();
        assertThat(periodList).hasSize(databaseSizeBeforeUpdate);
        Period testPeriod = periodList.get(periodList.size() - 1);
        assertThat(testPeriod.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPeriod.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testPeriod.getEndDate()).isEqualTo(DEFAULT_END_DATE);
    }

    @Test
    @Transactional
    void fullUpdatePeriodWithPatch() throws Exception {
        // Initialize the database
        periodRepository.saveAndFlush(period);

        int databaseSizeBeforeUpdate = periodRepository.findAll().size();

        // Update the period using partial update
        Period partialUpdatedPeriod = new Period();
        partialUpdatedPeriod.setId(period.getId());

        partialUpdatedPeriod.name(UPDATED_NAME).startDate(UPDATED_START_DATE).endDate(UPDATED_END_DATE);

        restPeriodMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPeriod.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPeriod))
            )
            .andExpect(status().isOk());

        // Validate the Period in the database
        List<Period> periodList = periodRepository.findAll();
        assertThat(periodList).hasSize(databaseSizeBeforeUpdate);
        Period testPeriod = periodList.get(periodList.size() - 1);
        assertThat(testPeriod.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPeriod.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testPeriod.getEndDate()).isEqualTo(UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingPeriod() throws Exception {
        int databaseSizeBeforeUpdate = periodRepository.findAll().size();
        period.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPeriodMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, period.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(period))
            )
            .andExpect(status().isBadRequest());

        // Validate the Period in the database
        List<Period> periodList = periodRepository.findAll();
        assertThat(periodList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPeriod() throws Exception {
        int databaseSizeBeforeUpdate = periodRepository.findAll().size();
        period.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPeriodMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(period))
            )
            .andExpect(status().isBadRequest());

        // Validate the Period in the database
        List<Period> periodList = periodRepository.findAll();
        assertThat(periodList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPeriod() throws Exception {
        int databaseSizeBeforeUpdate = periodRepository.findAll().size();
        period.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPeriodMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(period)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Period in the database
        List<Period> periodList = periodRepository.findAll();
        assertThat(periodList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePeriod() throws Exception {
        // Initialize the database
        periodRepository.saveAndFlush(period);

        int databaseSizeBeforeDelete = periodRepository.findAll().size();

        // Delete the period
        restPeriodMockMvc
            .perform(delete(ENTITY_API_URL_ID, period.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Period> periodList = periodRepository.findAll();
        assertThat(periodList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
