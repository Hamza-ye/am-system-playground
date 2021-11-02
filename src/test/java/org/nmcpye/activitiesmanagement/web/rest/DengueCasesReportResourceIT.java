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
import org.nmcpye.activitiesmanagement.domain.CasesReportClass;
import org.nmcpye.activitiesmanagement.domain.DengueCasesReport;
import org.nmcpye.activitiesmanagement.domain.Period;
import org.nmcpye.activitiesmanagement.repository.DengueCasesReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link DengueCasesReportResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DengueCasesReportResourceIT {

    private static final String DEFAULT_UID = "AAAAAAAAAA";
    private static final String UPDATED_UID = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_LAST_UPDATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_UPDATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Integer DEFAULT_RDT_TESTED = 1;
    private static final Integer UPDATED_RDT_TESTED = 2;

    private static final Integer DEFAULT_RDT_POSITIVE = 1;
    private static final Integer UPDATED_RDT_POSITIVE = 2;

    private static final Integer DEFAULT_PROBABLE_CASES = 1;
    private static final Integer UPDATED_PROBABLE_CASES = 2;

    private static final Integer DEFAULT_INPATIENT_CASES = 1;
    private static final Integer UPDATED_INPATIENT_CASES = 2;

    private static final Integer DEFAULT_DEATH_CASES = 1;
    private static final Integer UPDATED_DEATH_CASES = 2;

    private static final Integer DEFAULT_TREATED = 1;
    private static final Integer UPDATED_TREATED = 2;

    private static final Integer DEFAULT_SUSPECTED_CASES = 1;
    private static final Integer UPDATED_SUSPECTED_CASES = 2;

    private static final String DEFAULT_COMMENT = "AAAAAAAAAA";
    private static final String UPDATED_COMMENT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/dengue-cases-reports";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DengueCasesReportRepository dengueCasesReportRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDengueCasesReportMockMvc;

    private DengueCasesReport dengueCasesReport;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DengueCasesReport createEntity(EntityManager em) {
        DengueCasesReport dengueCasesReport = new DengueCasesReport()
            .uid(DEFAULT_UID)
            .created(DEFAULT_CREATED)
            .lastUpdated(DEFAULT_LAST_UPDATED)
            .rdtTested(DEFAULT_RDT_TESTED)
            .rdtPositive(DEFAULT_RDT_POSITIVE)
            .probableCases(DEFAULT_PROBABLE_CASES)
            .inpatientCases(DEFAULT_INPATIENT_CASES)
            .deathCases(DEFAULT_DEATH_CASES)
            .treated(DEFAULT_TREATED)
            .suspectedCases(DEFAULT_SUSPECTED_CASES)
            .comment(DEFAULT_COMMENT);
        // Add required entity
        CasesReportClass casesReportClass;
        if (TestUtil.findAll(em, CasesReportClass.class).isEmpty()) {
            casesReportClass = CasesReportClassResourceIT.createEntity(em);
            em.persist(casesReportClass);
            em.flush();
        } else {
            casesReportClass = TestUtil.findAll(em, CasesReportClass.class).get(0);
        }
        dengueCasesReport.setReportClass(casesReportClass);
        // Add required entity
        Period period;
        if (TestUtil.findAll(em, Period.class).isEmpty()) {
            period = PeriodResourceIT.createEntity(em);
            em.persist(period);
            em.flush();
        } else {
            period = TestUtil.findAll(em, Period.class).get(0);
        }
        dengueCasesReport.setPeriod(period);
        return dengueCasesReport;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DengueCasesReport createUpdatedEntity(EntityManager em) {
        DengueCasesReport dengueCasesReport = new DengueCasesReport()
            .uid(UPDATED_UID)
            .created(UPDATED_CREATED)
            .lastUpdated(UPDATED_LAST_UPDATED)
            .rdtTested(UPDATED_RDT_TESTED)
            .rdtPositive(UPDATED_RDT_POSITIVE)
            .probableCases(UPDATED_PROBABLE_CASES)
            .inpatientCases(UPDATED_INPATIENT_CASES)
            .deathCases(UPDATED_DEATH_CASES)
            .treated(UPDATED_TREATED)
            .suspectedCases(UPDATED_SUSPECTED_CASES)
            .comment(UPDATED_COMMENT);
        // Add required entity
        CasesReportClass casesReportClass;
        if (TestUtil.findAll(em, CasesReportClass.class).isEmpty()) {
            casesReportClass = CasesReportClassResourceIT.createUpdatedEntity(em);
            em.persist(casesReportClass);
            em.flush();
        } else {
            casesReportClass = TestUtil.findAll(em, CasesReportClass.class).get(0);
        }
        dengueCasesReport.setReportClass(casesReportClass);
        // Add required entity
        Period period;
        if (TestUtil.findAll(em, Period.class).isEmpty()) {
            period = PeriodResourceIT.createUpdatedEntity(em);
            em.persist(period);
            em.flush();
        } else {
            period = TestUtil.findAll(em, Period.class).get(0);
        }
        dengueCasesReport.setPeriod(period);
        return dengueCasesReport;
    }

    @BeforeEach
    public void initTest() {
        dengueCasesReport = createEntity(em);
    }

    @Test
    @Transactional
    void createDengueCasesReport() throws Exception {
        int databaseSizeBeforeCreate = dengueCasesReportRepository.findAll().size();
        // Create the DengueCasesReport
        restDengueCasesReportMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dengueCasesReport))
            )
            .andExpect(status().isCreated());

        // Validate the DengueCasesReport in the database
        List<DengueCasesReport> dengueCasesReportList = dengueCasesReportRepository.findAll();
        assertThat(dengueCasesReportList).hasSize(databaseSizeBeforeCreate + 1);
        DengueCasesReport testDengueCasesReport = dengueCasesReportList.get(dengueCasesReportList.size() - 1);
        assertThat(testDengueCasesReport.getUid()).isEqualTo(DEFAULT_UID);
        assertThat(testDengueCasesReport.getCreated()).isEqualTo(DEFAULT_CREATED);
        assertThat(testDengueCasesReport.getLastUpdated()).isEqualTo(DEFAULT_LAST_UPDATED);
        assertThat(testDengueCasesReport.getRdtTested()).isEqualTo(DEFAULT_RDT_TESTED);
        assertThat(testDengueCasesReport.getRdtPositive()).isEqualTo(DEFAULT_RDT_POSITIVE);
        assertThat(testDengueCasesReport.getProbableCases()).isEqualTo(DEFAULT_PROBABLE_CASES);
        assertThat(testDengueCasesReport.getInpatientCases()).isEqualTo(DEFAULT_INPATIENT_CASES);
        assertThat(testDengueCasesReport.getDeathCases()).isEqualTo(DEFAULT_DEATH_CASES);
        assertThat(testDengueCasesReport.getTreated()).isEqualTo(DEFAULT_TREATED);
        assertThat(testDengueCasesReport.getSuspectedCases()).isEqualTo(DEFAULT_SUSPECTED_CASES);
        assertThat(testDengueCasesReport.getComment()).isEqualTo(DEFAULT_COMMENT);
    }

    @Test
    @Transactional
    void createDengueCasesReportWithExistingId() throws Exception {
        // Create the DengueCasesReport with an existing ID
        dengueCasesReport.setId(1L);

        int databaseSizeBeforeCreate = dengueCasesReportRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDengueCasesReportMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dengueCasesReport))
            )
            .andExpect(status().isBadRequest());

        // Validate the DengueCasesReport in the database
        List<DengueCasesReport> dengueCasesReportList = dengueCasesReportRepository.findAll();
        assertThat(dengueCasesReportList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkUidIsRequired() throws Exception {
        int databaseSizeBeforeTest = dengueCasesReportRepository.findAll().size();
        // set the field null
        dengueCasesReport.setUid(null);

        // Create the DengueCasesReport, which fails.

        restDengueCasesReportMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dengueCasesReport))
            )
            .andExpect(status().isBadRequest());

        List<DengueCasesReport> dengueCasesReportList = dengueCasesReportRepository.findAll();
        assertThat(dengueCasesReportList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDengueCasesReports() throws Exception {
        // Initialize the database
        dengueCasesReportRepository.saveAndFlush(dengueCasesReport);

        // Get all the dengueCasesReportList
        restDengueCasesReportMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dengueCasesReport.getId().intValue())))
            .andExpect(jsonPath("$.[*].uid").value(hasItem(DEFAULT_UID)))
            .andExpect(jsonPath("$.[*].created").value(hasItem(DEFAULT_CREATED.toString())))
            .andExpect(jsonPath("$.[*].lastUpdated").value(hasItem(DEFAULT_LAST_UPDATED.toString())))
            .andExpect(jsonPath("$.[*].rdtTested").value(hasItem(DEFAULT_RDT_TESTED)))
            .andExpect(jsonPath("$.[*].rdtPositive").value(hasItem(DEFAULT_RDT_POSITIVE)))
            .andExpect(jsonPath("$.[*].probableCases").value(hasItem(DEFAULT_PROBABLE_CASES)))
            .andExpect(jsonPath("$.[*].inpatientCases").value(hasItem(DEFAULT_INPATIENT_CASES)))
            .andExpect(jsonPath("$.[*].deathCases").value(hasItem(DEFAULT_DEATH_CASES)))
            .andExpect(jsonPath("$.[*].treated").value(hasItem(DEFAULT_TREATED)))
            .andExpect(jsonPath("$.[*].suspectedCases").value(hasItem(DEFAULT_SUSPECTED_CASES)))
            .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT)));
    }

    @Test
    @Transactional
    void getDengueCasesReport() throws Exception {
        // Initialize the database
        dengueCasesReportRepository.saveAndFlush(dengueCasesReport);

        // Get the dengueCasesReport
        restDengueCasesReportMockMvc
            .perform(get(ENTITY_API_URL_ID, dengueCasesReport.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(dengueCasesReport.getId().intValue()))
            .andExpect(jsonPath("$.uid").value(DEFAULT_UID))
            .andExpect(jsonPath("$.created").value(DEFAULT_CREATED.toString()))
            .andExpect(jsonPath("$.lastUpdated").value(DEFAULT_LAST_UPDATED.toString()))
            .andExpect(jsonPath("$.rdtTested").value(DEFAULT_RDT_TESTED))
            .andExpect(jsonPath("$.rdtPositive").value(DEFAULT_RDT_POSITIVE))
            .andExpect(jsonPath("$.probableCases").value(DEFAULT_PROBABLE_CASES))
            .andExpect(jsonPath("$.inpatientCases").value(DEFAULT_INPATIENT_CASES))
            .andExpect(jsonPath("$.deathCases").value(DEFAULT_DEATH_CASES))
            .andExpect(jsonPath("$.treated").value(DEFAULT_TREATED))
            .andExpect(jsonPath("$.suspectedCases").value(DEFAULT_SUSPECTED_CASES))
            .andExpect(jsonPath("$.comment").value(DEFAULT_COMMENT));
    }

    @Test
    @Transactional
    void getNonExistingDengueCasesReport() throws Exception {
        // Get the dengueCasesReport
        restDengueCasesReportMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDengueCasesReport() throws Exception {
        // Initialize the database
        dengueCasesReportRepository.saveAndFlush(dengueCasesReport);

        int databaseSizeBeforeUpdate = dengueCasesReportRepository.findAll().size();

        // Update the dengueCasesReport
        DengueCasesReport updatedDengueCasesReport = dengueCasesReportRepository.findById(dengueCasesReport.getId()).get();
        // Disconnect from session so that the updates on updatedDengueCasesReport are not directly saved in db
        em.detach(updatedDengueCasesReport);
        updatedDengueCasesReport
            .uid(UPDATED_UID)
            .created(UPDATED_CREATED)
            .lastUpdated(UPDATED_LAST_UPDATED)
            .rdtTested(UPDATED_RDT_TESTED)
            .rdtPositive(UPDATED_RDT_POSITIVE)
            .probableCases(UPDATED_PROBABLE_CASES)
            .inpatientCases(UPDATED_INPATIENT_CASES)
            .deathCases(UPDATED_DEATH_CASES)
            .treated(UPDATED_TREATED)
            .suspectedCases(UPDATED_SUSPECTED_CASES)
            .comment(UPDATED_COMMENT);

        restDengueCasesReportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDengueCasesReport.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedDengueCasesReport))
            )
            .andExpect(status().isOk());

        // Validate the DengueCasesReport in the database
        List<DengueCasesReport> dengueCasesReportList = dengueCasesReportRepository.findAll();
        assertThat(dengueCasesReportList).hasSize(databaseSizeBeforeUpdate);
        DengueCasesReport testDengueCasesReport = dengueCasesReportList.get(dengueCasesReportList.size() - 1);
        assertThat(testDengueCasesReport.getUid()).isEqualTo(UPDATED_UID);
        assertThat(testDengueCasesReport.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testDengueCasesReport.getLastUpdated()).isEqualTo(UPDATED_LAST_UPDATED);
        assertThat(testDengueCasesReport.getRdtTested()).isEqualTo(UPDATED_RDT_TESTED);
        assertThat(testDengueCasesReport.getRdtPositive()).isEqualTo(UPDATED_RDT_POSITIVE);
        assertThat(testDengueCasesReport.getProbableCases()).isEqualTo(UPDATED_PROBABLE_CASES);
        assertThat(testDengueCasesReport.getInpatientCases()).isEqualTo(UPDATED_INPATIENT_CASES);
        assertThat(testDengueCasesReport.getDeathCases()).isEqualTo(UPDATED_DEATH_CASES);
        assertThat(testDengueCasesReport.getTreated()).isEqualTo(UPDATED_TREATED);
        assertThat(testDengueCasesReport.getSuspectedCases()).isEqualTo(UPDATED_SUSPECTED_CASES);
        assertThat(testDengueCasesReport.getComment()).isEqualTo(UPDATED_COMMENT);
    }

    @Test
    @Transactional
    void putNonExistingDengueCasesReport() throws Exception {
        int databaseSizeBeforeUpdate = dengueCasesReportRepository.findAll().size();
        dengueCasesReport.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDengueCasesReportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, dengueCasesReport.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dengueCasesReport))
            )
            .andExpect(status().isBadRequest());

        // Validate the DengueCasesReport in the database
        List<DengueCasesReport> dengueCasesReportList = dengueCasesReportRepository.findAll();
        assertThat(dengueCasesReportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDengueCasesReport() throws Exception {
        int databaseSizeBeforeUpdate = dengueCasesReportRepository.findAll().size();
        dengueCasesReport.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDengueCasesReportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dengueCasesReport))
            )
            .andExpect(status().isBadRequest());

        // Validate the DengueCasesReport in the database
        List<DengueCasesReport> dengueCasesReportList = dengueCasesReportRepository.findAll();
        assertThat(dengueCasesReportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDengueCasesReport() throws Exception {
        int databaseSizeBeforeUpdate = dengueCasesReportRepository.findAll().size();
        dengueCasesReport.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDengueCasesReportMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dengueCasesReport))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DengueCasesReport in the database
        List<DengueCasesReport> dengueCasesReportList = dengueCasesReportRepository.findAll();
        assertThat(dengueCasesReportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDengueCasesReportWithPatch() throws Exception {
        // Initialize the database
        dengueCasesReportRepository.saveAndFlush(dengueCasesReport);

        int databaseSizeBeforeUpdate = dengueCasesReportRepository.findAll().size();

        // Update the dengueCasesReport using partial update
        DengueCasesReport partialUpdatedDengueCasesReport = new DengueCasesReport();
        partialUpdatedDengueCasesReport.setId(dengueCasesReport.getId());

        partialUpdatedDengueCasesReport
            .lastUpdated(UPDATED_LAST_UPDATED)
            .rdtTested(UPDATED_RDT_TESTED)
            .rdtPositive(UPDATED_RDT_POSITIVE)
            .deathCases(UPDATED_DEATH_CASES);

        restDengueCasesReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDengueCasesReport.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDengueCasesReport))
            )
            .andExpect(status().isOk());

        // Validate the DengueCasesReport in the database
        List<DengueCasesReport> dengueCasesReportList = dengueCasesReportRepository.findAll();
        assertThat(dengueCasesReportList).hasSize(databaseSizeBeforeUpdate);
        DengueCasesReport testDengueCasesReport = dengueCasesReportList.get(dengueCasesReportList.size() - 1);
        assertThat(testDengueCasesReport.getUid()).isEqualTo(DEFAULT_UID);
        assertThat(testDengueCasesReport.getCreated()).isEqualTo(DEFAULT_CREATED);
        assertThat(testDengueCasesReport.getLastUpdated()).isEqualTo(UPDATED_LAST_UPDATED);
        assertThat(testDengueCasesReport.getRdtTested()).isEqualTo(UPDATED_RDT_TESTED);
        assertThat(testDengueCasesReport.getRdtPositive()).isEqualTo(UPDATED_RDT_POSITIVE);
        assertThat(testDengueCasesReport.getProbableCases()).isEqualTo(DEFAULT_PROBABLE_CASES);
        assertThat(testDengueCasesReport.getInpatientCases()).isEqualTo(DEFAULT_INPATIENT_CASES);
        assertThat(testDengueCasesReport.getDeathCases()).isEqualTo(UPDATED_DEATH_CASES);
        assertThat(testDengueCasesReport.getTreated()).isEqualTo(DEFAULT_TREATED);
        assertThat(testDengueCasesReport.getSuspectedCases()).isEqualTo(DEFAULT_SUSPECTED_CASES);
        assertThat(testDengueCasesReport.getComment()).isEqualTo(DEFAULT_COMMENT);
    }

    @Test
    @Transactional
    void fullUpdateDengueCasesReportWithPatch() throws Exception {
        // Initialize the database
        dengueCasesReportRepository.saveAndFlush(dengueCasesReport);

        int databaseSizeBeforeUpdate = dengueCasesReportRepository.findAll().size();

        // Update the dengueCasesReport using partial update
        DengueCasesReport partialUpdatedDengueCasesReport = new DengueCasesReport();
        partialUpdatedDengueCasesReport.setId(dengueCasesReport.getId());

        partialUpdatedDengueCasesReport
            .uid(UPDATED_UID)
            .created(UPDATED_CREATED)
            .lastUpdated(UPDATED_LAST_UPDATED)
            .rdtTested(UPDATED_RDT_TESTED)
            .rdtPositive(UPDATED_RDT_POSITIVE)
            .probableCases(UPDATED_PROBABLE_CASES)
            .inpatientCases(UPDATED_INPATIENT_CASES)
            .deathCases(UPDATED_DEATH_CASES)
            .treated(UPDATED_TREATED)
            .suspectedCases(UPDATED_SUSPECTED_CASES)
            .comment(UPDATED_COMMENT);

        restDengueCasesReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDengueCasesReport.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDengueCasesReport))
            )
            .andExpect(status().isOk());

        // Validate the DengueCasesReport in the database
        List<DengueCasesReport> dengueCasesReportList = dengueCasesReportRepository.findAll();
        assertThat(dengueCasesReportList).hasSize(databaseSizeBeforeUpdate);
        DengueCasesReport testDengueCasesReport = dengueCasesReportList.get(dengueCasesReportList.size() - 1);
        assertThat(testDengueCasesReport.getUid()).isEqualTo(UPDATED_UID);
        assertThat(testDengueCasesReport.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testDengueCasesReport.getLastUpdated()).isEqualTo(UPDATED_LAST_UPDATED);
        assertThat(testDengueCasesReport.getRdtTested()).isEqualTo(UPDATED_RDT_TESTED);
        assertThat(testDengueCasesReport.getRdtPositive()).isEqualTo(UPDATED_RDT_POSITIVE);
        assertThat(testDengueCasesReport.getProbableCases()).isEqualTo(UPDATED_PROBABLE_CASES);
        assertThat(testDengueCasesReport.getInpatientCases()).isEqualTo(UPDATED_INPATIENT_CASES);
        assertThat(testDengueCasesReport.getDeathCases()).isEqualTo(UPDATED_DEATH_CASES);
        assertThat(testDengueCasesReport.getTreated()).isEqualTo(UPDATED_TREATED);
        assertThat(testDengueCasesReport.getSuspectedCases()).isEqualTo(UPDATED_SUSPECTED_CASES);
        assertThat(testDengueCasesReport.getComment()).isEqualTo(UPDATED_COMMENT);
    }

    @Test
    @Transactional
    void patchNonExistingDengueCasesReport() throws Exception {
        int databaseSizeBeforeUpdate = dengueCasesReportRepository.findAll().size();
        dengueCasesReport.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDengueCasesReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, dengueCasesReport.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(dengueCasesReport))
            )
            .andExpect(status().isBadRequest());

        // Validate the DengueCasesReport in the database
        List<DengueCasesReport> dengueCasesReportList = dengueCasesReportRepository.findAll();
        assertThat(dengueCasesReportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDengueCasesReport() throws Exception {
        int databaseSizeBeforeUpdate = dengueCasesReportRepository.findAll().size();
        dengueCasesReport.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDengueCasesReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(dengueCasesReport))
            )
            .andExpect(status().isBadRequest());

        // Validate the DengueCasesReport in the database
        List<DengueCasesReport> dengueCasesReportList = dengueCasesReportRepository.findAll();
        assertThat(dengueCasesReportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDengueCasesReport() throws Exception {
        int databaseSizeBeforeUpdate = dengueCasesReportRepository.findAll().size();
        dengueCasesReport.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDengueCasesReportMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(dengueCasesReport))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DengueCasesReport in the database
        List<DengueCasesReport> dengueCasesReportList = dengueCasesReportRepository.findAll();
        assertThat(dengueCasesReportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDengueCasesReport() throws Exception {
        // Initialize the database
        dengueCasesReportRepository.saveAndFlush(dengueCasesReport);

        int databaseSizeBeforeDelete = dengueCasesReportRepository.findAll().size();

        // Delete the dengueCasesReport
        restDengueCasesReportMockMvc
            .perform(delete(ENTITY_API_URL_ID, dengueCasesReport.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DengueCasesReport> dengueCasesReportList = dengueCasesReportRepository.findAll();
        assertThat(dengueCasesReportList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
