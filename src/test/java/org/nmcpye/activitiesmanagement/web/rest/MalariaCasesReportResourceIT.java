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
import org.nmcpye.activitiesmanagement.domain.MalariaCasesReport;
import org.nmcpye.activitiesmanagement.domain.Period;
import org.nmcpye.activitiesmanagement.repository.MalariaCasesReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link MalariaCasesReportResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MalariaCasesReportResourceIT {

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

    private static final Integer DEFAULT_RDT_PF = 1;
    private static final Integer UPDATED_RDT_PF = 2;

    private static final Integer DEFAULT_RDT_PV = 1;
    private static final Integer UPDATED_RDT_PV = 2;

    private static final Integer DEFAULT_RDT_POTHER = 1;
    private static final Integer UPDATED_RDT_POTHER = 2;

    private static final Integer DEFAULT_MICRO_TESTED = 1;
    private static final Integer UPDATED_MICRO_TESTED = 2;

    private static final Integer DEFAULT_MICRO_POSITIVE = 1;
    private static final Integer UPDATED_MICRO_POSITIVE = 2;

    private static final Integer DEFAULT_MICRO_PF = 1;
    private static final Integer UPDATED_MICRO_PF = 2;

    private static final Integer DEFAULT_MICRO_PV = 1;
    private static final Integer UPDATED_MICRO_PV = 2;

    private static final Integer DEFAULT_MICRO_MIX = 1;
    private static final Integer UPDATED_MICRO_MIX = 2;

    private static final Integer DEFAULT_MICRO_POTHER = 1;
    private static final Integer UPDATED_MICRO_POTHER = 2;

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

    private static final Integer DEFAULT_TOTAL_FREQUENTS = 1;
    private static final Integer UPDATED_TOTAL_FREQUENTS = 2;

    private static final String DEFAULT_COMMENT = "AAAAAAAAAA";
    private static final String UPDATED_COMMENT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/malaria-cases-reports";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MalariaCasesReportRepository malariaCasesReportRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMalariaCasesReportMockMvc;

    private MalariaCasesReport malariaCasesReport;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MalariaCasesReport createEntity(EntityManager em) {
        MalariaCasesReport malariaCasesReport = new MalariaCasesReport()
            .uid(DEFAULT_UID)
            .created(DEFAULT_CREATED)
            .lastUpdated(DEFAULT_LAST_UPDATED)
            .rdtTested(DEFAULT_RDT_TESTED)
            .rdtPositive(DEFAULT_RDT_POSITIVE)
            .rdtPf(DEFAULT_RDT_PF)
            .rdtPv(DEFAULT_RDT_PV)
            .rdtPother(DEFAULT_RDT_POTHER)
            .microTested(DEFAULT_MICRO_TESTED)
            .microPositive(DEFAULT_MICRO_POSITIVE)
            .microPf(DEFAULT_MICRO_PF)
            .microPv(DEFAULT_MICRO_PV)
            .microMix(DEFAULT_MICRO_MIX)
            .microPother(DEFAULT_MICRO_POTHER)
            .probableCases(DEFAULT_PROBABLE_CASES)
            .inpatientCases(DEFAULT_INPATIENT_CASES)
            .deathCases(DEFAULT_DEATH_CASES)
            .treated(DEFAULT_TREATED)
            .suspectedCases(DEFAULT_SUSPECTED_CASES)
            .totalFrequents(DEFAULT_TOTAL_FREQUENTS)
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
        malariaCasesReport.setReportClass(casesReportClass);
        // Add required entity
        Period period;
        if (TestUtil.findAll(em, Period.class).isEmpty()) {
            period = PeriodResourceIT.createEntity(em);
            em.persist(period);
            em.flush();
        } else {
            period = TestUtil.findAll(em, Period.class).get(0);
        }
        malariaCasesReport.setPeriod(period);
        return malariaCasesReport;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MalariaCasesReport createUpdatedEntity(EntityManager em) {
        MalariaCasesReport malariaCasesReport = new MalariaCasesReport()
            .uid(UPDATED_UID)
            .created(UPDATED_CREATED)
            .lastUpdated(UPDATED_LAST_UPDATED)
            .rdtTested(UPDATED_RDT_TESTED)
            .rdtPositive(UPDATED_RDT_POSITIVE)
            .rdtPf(UPDATED_RDT_PF)
            .rdtPv(UPDATED_RDT_PV)
            .rdtPother(UPDATED_RDT_POTHER)
            .microTested(UPDATED_MICRO_TESTED)
            .microPositive(UPDATED_MICRO_POSITIVE)
            .microPf(UPDATED_MICRO_PF)
            .microPv(UPDATED_MICRO_PV)
            .microMix(UPDATED_MICRO_MIX)
            .microPother(UPDATED_MICRO_POTHER)
            .probableCases(UPDATED_PROBABLE_CASES)
            .inpatientCases(UPDATED_INPATIENT_CASES)
            .deathCases(UPDATED_DEATH_CASES)
            .treated(UPDATED_TREATED)
            .suspectedCases(UPDATED_SUSPECTED_CASES)
            .totalFrequents(UPDATED_TOTAL_FREQUENTS)
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
        malariaCasesReport.setReportClass(casesReportClass);
        // Add required entity
        Period period;
        if (TestUtil.findAll(em, Period.class).isEmpty()) {
            period = PeriodResourceIT.createUpdatedEntity(em);
            em.persist(period);
            em.flush();
        } else {
            period = TestUtil.findAll(em, Period.class).get(0);
        }
        malariaCasesReport.setPeriod(period);
        return malariaCasesReport;
    }

    @BeforeEach
    public void initTest() {
        malariaCasesReport = createEntity(em);
    }

    @Test
    @Transactional
    void createMalariaCasesReport() throws Exception {
        int databaseSizeBeforeCreate = malariaCasesReportRepository.findAll().size();
        // Create the MalariaCasesReport
        restMalariaCasesReportMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(malariaCasesReport))
            )
            .andExpect(status().isCreated());

        // Validate the MalariaCasesReport in the database
        List<MalariaCasesReport> malariaCasesReportList = malariaCasesReportRepository.findAll();
        assertThat(malariaCasesReportList).hasSize(databaseSizeBeforeCreate + 1);
        MalariaCasesReport testMalariaCasesReport = malariaCasesReportList.get(malariaCasesReportList.size() - 1);
        assertThat(testMalariaCasesReport.getUid()).isEqualTo(DEFAULT_UID);
        assertThat(testMalariaCasesReport.getCreated()).isEqualTo(DEFAULT_CREATED);
        assertThat(testMalariaCasesReport.getLastUpdated()).isEqualTo(DEFAULT_LAST_UPDATED);
        assertThat(testMalariaCasesReport.getRdtTested()).isEqualTo(DEFAULT_RDT_TESTED);
        assertThat(testMalariaCasesReport.getRdtPositive()).isEqualTo(DEFAULT_RDT_POSITIVE);
        assertThat(testMalariaCasesReport.getRdtPf()).isEqualTo(DEFAULT_RDT_PF);
        assertThat(testMalariaCasesReport.getRdtPv()).isEqualTo(DEFAULT_RDT_PV);
        assertThat(testMalariaCasesReport.getRdtPother()).isEqualTo(DEFAULT_RDT_POTHER);
        assertThat(testMalariaCasesReport.getMicroTested()).isEqualTo(DEFAULT_MICRO_TESTED);
        assertThat(testMalariaCasesReport.getMicroPositive()).isEqualTo(DEFAULT_MICRO_POSITIVE);
        assertThat(testMalariaCasesReport.getMicroPf()).isEqualTo(DEFAULT_MICRO_PF);
        assertThat(testMalariaCasesReport.getMicroPv()).isEqualTo(DEFAULT_MICRO_PV);
        assertThat(testMalariaCasesReport.getMicroMix()).isEqualTo(DEFAULT_MICRO_MIX);
        assertThat(testMalariaCasesReport.getMicroPother()).isEqualTo(DEFAULT_MICRO_POTHER);
        assertThat(testMalariaCasesReport.getProbableCases()).isEqualTo(DEFAULT_PROBABLE_CASES);
        assertThat(testMalariaCasesReport.getInpatientCases()).isEqualTo(DEFAULT_INPATIENT_CASES);
        assertThat(testMalariaCasesReport.getDeathCases()).isEqualTo(DEFAULT_DEATH_CASES);
        assertThat(testMalariaCasesReport.getTreated()).isEqualTo(DEFAULT_TREATED);
        assertThat(testMalariaCasesReport.getSuspectedCases()).isEqualTo(DEFAULT_SUSPECTED_CASES);
        assertThat(testMalariaCasesReport.getTotalFrequents()).isEqualTo(DEFAULT_TOTAL_FREQUENTS);
        assertThat(testMalariaCasesReport.getComment()).isEqualTo(DEFAULT_COMMENT);
    }

    @Test
    @Transactional
    void createMalariaCasesReportWithExistingId() throws Exception {
        // Create the MalariaCasesReport with an existing ID
        malariaCasesReport.setId(1L);

        int databaseSizeBeforeCreate = malariaCasesReportRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMalariaCasesReportMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(malariaCasesReport))
            )
            .andExpect(status().isBadRequest());

        // Validate the MalariaCasesReport in the database
        List<MalariaCasesReport> malariaCasesReportList = malariaCasesReportRepository.findAll();
        assertThat(malariaCasesReportList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkUidIsRequired() throws Exception {
        int databaseSizeBeforeTest = malariaCasesReportRepository.findAll().size();
        // set the field null
        malariaCasesReport.setUid(null);

        // Create the MalariaCasesReport, which fails.

        restMalariaCasesReportMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(malariaCasesReport))
            )
            .andExpect(status().isBadRequest());

        List<MalariaCasesReport> malariaCasesReportList = malariaCasesReportRepository.findAll();
        assertThat(malariaCasesReportList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMalariaCasesReports() throws Exception {
        // Initialize the database
        malariaCasesReportRepository.saveAndFlush(malariaCasesReport);

        // Get all the malariaCasesReportList
        restMalariaCasesReportMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(malariaCasesReport.getId().intValue())))
            .andExpect(jsonPath("$.[*].uid").value(hasItem(DEFAULT_UID)))
            .andExpect(jsonPath("$.[*].created").value(hasItem(DEFAULT_CREATED.toString())))
            .andExpect(jsonPath("$.[*].lastUpdated").value(hasItem(DEFAULT_LAST_UPDATED.toString())))
            .andExpect(jsonPath("$.[*].rdtTested").value(hasItem(DEFAULT_RDT_TESTED)))
            .andExpect(jsonPath("$.[*].rdtPositive").value(hasItem(DEFAULT_RDT_POSITIVE)))
            .andExpect(jsonPath("$.[*].rdtPf").value(hasItem(DEFAULT_RDT_PF)))
            .andExpect(jsonPath("$.[*].rdtPv").value(hasItem(DEFAULT_RDT_PV)))
            .andExpect(jsonPath("$.[*].rdtPother").value(hasItem(DEFAULT_RDT_POTHER)))
            .andExpect(jsonPath("$.[*].microTested").value(hasItem(DEFAULT_MICRO_TESTED)))
            .andExpect(jsonPath("$.[*].microPositive").value(hasItem(DEFAULT_MICRO_POSITIVE)))
            .andExpect(jsonPath("$.[*].microPf").value(hasItem(DEFAULT_MICRO_PF)))
            .andExpect(jsonPath("$.[*].microPv").value(hasItem(DEFAULT_MICRO_PV)))
            .andExpect(jsonPath("$.[*].microMix").value(hasItem(DEFAULT_MICRO_MIX)))
            .andExpect(jsonPath("$.[*].microPother").value(hasItem(DEFAULT_MICRO_POTHER)))
            .andExpect(jsonPath("$.[*].probableCases").value(hasItem(DEFAULT_PROBABLE_CASES)))
            .andExpect(jsonPath("$.[*].inpatientCases").value(hasItem(DEFAULT_INPATIENT_CASES)))
            .andExpect(jsonPath("$.[*].deathCases").value(hasItem(DEFAULT_DEATH_CASES)))
            .andExpect(jsonPath("$.[*].treated").value(hasItem(DEFAULT_TREATED)))
            .andExpect(jsonPath("$.[*].suspectedCases").value(hasItem(DEFAULT_SUSPECTED_CASES)))
            .andExpect(jsonPath("$.[*].totalFrequents").value(hasItem(DEFAULT_TOTAL_FREQUENTS)))
            .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT)));
    }

    @Test
    @Transactional
    void getMalariaCasesReport() throws Exception {
        // Initialize the database
        malariaCasesReportRepository.saveAndFlush(malariaCasesReport);

        // Get the malariaCasesReport
        restMalariaCasesReportMockMvc
            .perform(get(ENTITY_API_URL_ID, malariaCasesReport.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(malariaCasesReport.getId().intValue()))
            .andExpect(jsonPath("$.uid").value(DEFAULT_UID))
            .andExpect(jsonPath("$.created").value(DEFAULT_CREATED.toString()))
            .andExpect(jsonPath("$.lastUpdated").value(DEFAULT_LAST_UPDATED.toString()))
            .andExpect(jsonPath("$.rdtTested").value(DEFAULT_RDT_TESTED))
            .andExpect(jsonPath("$.rdtPositive").value(DEFAULT_RDT_POSITIVE))
            .andExpect(jsonPath("$.rdtPf").value(DEFAULT_RDT_PF))
            .andExpect(jsonPath("$.rdtPv").value(DEFAULT_RDT_PV))
            .andExpect(jsonPath("$.rdtPother").value(DEFAULT_RDT_POTHER))
            .andExpect(jsonPath("$.microTested").value(DEFAULT_MICRO_TESTED))
            .andExpect(jsonPath("$.microPositive").value(DEFAULT_MICRO_POSITIVE))
            .andExpect(jsonPath("$.microPf").value(DEFAULT_MICRO_PF))
            .andExpect(jsonPath("$.microPv").value(DEFAULT_MICRO_PV))
            .andExpect(jsonPath("$.microMix").value(DEFAULT_MICRO_MIX))
            .andExpect(jsonPath("$.microPother").value(DEFAULT_MICRO_POTHER))
            .andExpect(jsonPath("$.probableCases").value(DEFAULT_PROBABLE_CASES))
            .andExpect(jsonPath("$.inpatientCases").value(DEFAULT_INPATIENT_CASES))
            .andExpect(jsonPath("$.deathCases").value(DEFAULT_DEATH_CASES))
            .andExpect(jsonPath("$.treated").value(DEFAULT_TREATED))
            .andExpect(jsonPath("$.suspectedCases").value(DEFAULT_SUSPECTED_CASES))
            .andExpect(jsonPath("$.totalFrequents").value(DEFAULT_TOTAL_FREQUENTS))
            .andExpect(jsonPath("$.comment").value(DEFAULT_COMMENT));
    }

    @Test
    @Transactional
    void getNonExistingMalariaCasesReport() throws Exception {
        // Get the malariaCasesReport
        restMalariaCasesReportMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewMalariaCasesReport() throws Exception {
        // Initialize the database
        malariaCasesReportRepository.saveAndFlush(malariaCasesReport);

        int databaseSizeBeforeUpdate = malariaCasesReportRepository.findAll().size();

        // Update the malariaCasesReport
        MalariaCasesReport updatedMalariaCasesReport = malariaCasesReportRepository.findById(malariaCasesReport.getId()).get();
        // Disconnect from session so that the updates on updatedMalariaCasesReport are not directly saved in db
        em.detach(updatedMalariaCasesReport);
        updatedMalariaCasesReport
            .uid(UPDATED_UID)
            .created(UPDATED_CREATED)
            .lastUpdated(UPDATED_LAST_UPDATED)
            .rdtTested(UPDATED_RDT_TESTED)
            .rdtPositive(UPDATED_RDT_POSITIVE)
            .rdtPf(UPDATED_RDT_PF)
            .rdtPv(UPDATED_RDT_PV)
            .rdtPother(UPDATED_RDT_POTHER)
            .microTested(UPDATED_MICRO_TESTED)
            .microPositive(UPDATED_MICRO_POSITIVE)
            .microPf(UPDATED_MICRO_PF)
            .microPv(UPDATED_MICRO_PV)
            .microMix(UPDATED_MICRO_MIX)
            .microPother(UPDATED_MICRO_POTHER)
            .probableCases(UPDATED_PROBABLE_CASES)
            .inpatientCases(UPDATED_INPATIENT_CASES)
            .deathCases(UPDATED_DEATH_CASES)
            .treated(UPDATED_TREATED)
            .suspectedCases(UPDATED_SUSPECTED_CASES)
            .totalFrequents(UPDATED_TOTAL_FREQUENTS)
            .comment(UPDATED_COMMENT);

        restMalariaCasesReportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedMalariaCasesReport.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedMalariaCasesReport))
            )
            .andExpect(status().isOk());

        // Validate the MalariaCasesReport in the database
        List<MalariaCasesReport> malariaCasesReportList = malariaCasesReportRepository.findAll();
        assertThat(malariaCasesReportList).hasSize(databaseSizeBeforeUpdate);
        MalariaCasesReport testMalariaCasesReport = malariaCasesReportList.get(malariaCasesReportList.size() - 1);
        assertThat(testMalariaCasesReport.getUid()).isEqualTo(UPDATED_UID);
        assertThat(testMalariaCasesReport.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testMalariaCasesReport.getLastUpdated()).isEqualTo(UPDATED_LAST_UPDATED);
        assertThat(testMalariaCasesReport.getRdtTested()).isEqualTo(UPDATED_RDT_TESTED);
        assertThat(testMalariaCasesReport.getRdtPositive()).isEqualTo(UPDATED_RDT_POSITIVE);
        assertThat(testMalariaCasesReport.getRdtPf()).isEqualTo(UPDATED_RDT_PF);
        assertThat(testMalariaCasesReport.getRdtPv()).isEqualTo(UPDATED_RDT_PV);
        assertThat(testMalariaCasesReport.getRdtPother()).isEqualTo(UPDATED_RDT_POTHER);
        assertThat(testMalariaCasesReport.getMicroTested()).isEqualTo(UPDATED_MICRO_TESTED);
        assertThat(testMalariaCasesReport.getMicroPositive()).isEqualTo(UPDATED_MICRO_POSITIVE);
        assertThat(testMalariaCasesReport.getMicroPf()).isEqualTo(UPDATED_MICRO_PF);
        assertThat(testMalariaCasesReport.getMicroPv()).isEqualTo(UPDATED_MICRO_PV);
        assertThat(testMalariaCasesReport.getMicroMix()).isEqualTo(UPDATED_MICRO_MIX);
        assertThat(testMalariaCasesReport.getMicroPother()).isEqualTo(UPDATED_MICRO_POTHER);
        assertThat(testMalariaCasesReport.getProbableCases()).isEqualTo(UPDATED_PROBABLE_CASES);
        assertThat(testMalariaCasesReport.getInpatientCases()).isEqualTo(UPDATED_INPATIENT_CASES);
        assertThat(testMalariaCasesReport.getDeathCases()).isEqualTo(UPDATED_DEATH_CASES);
        assertThat(testMalariaCasesReport.getTreated()).isEqualTo(UPDATED_TREATED);
        assertThat(testMalariaCasesReport.getSuspectedCases()).isEqualTo(UPDATED_SUSPECTED_CASES);
        assertThat(testMalariaCasesReport.getTotalFrequents()).isEqualTo(UPDATED_TOTAL_FREQUENTS);
        assertThat(testMalariaCasesReport.getComment()).isEqualTo(UPDATED_COMMENT);
    }

    @Test
    @Transactional
    void putNonExistingMalariaCasesReport() throws Exception {
        int databaseSizeBeforeUpdate = malariaCasesReportRepository.findAll().size();
        malariaCasesReport.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMalariaCasesReportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, malariaCasesReport.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(malariaCasesReport))
            )
            .andExpect(status().isBadRequest());

        // Validate the MalariaCasesReport in the database
        List<MalariaCasesReport> malariaCasesReportList = malariaCasesReportRepository.findAll();
        assertThat(malariaCasesReportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMalariaCasesReport() throws Exception {
        int databaseSizeBeforeUpdate = malariaCasesReportRepository.findAll().size();
        malariaCasesReport.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMalariaCasesReportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(malariaCasesReport))
            )
            .andExpect(status().isBadRequest());

        // Validate the MalariaCasesReport in the database
        List<MalariaCasesReport> malariaCasesReportList = malariaCasesReportRepository.findAll();
        assertThat(malariaCasesReportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMalariaCasesReport() throws Exception {
        int databaseSizeBeforeUpdate = malariaCasesReportRepository.findAll().size();
        malariaCasesReport.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMalariaCasesReportMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(malariaCasesReport))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the MalariaCasesReport in the database
        List<MalariaCasesReport> malariaCasesReportList = malariaCasesReportRepository.findAll();
        assertThat(malariaCasesReportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMalariaCasesReportWithPatch() throws Exception {
        // Initialize the database
        malariaCasesReportRepository.saveAndFlush(malariaCasesReport);

        int databaseSizeBeforeUpdate = malariaCasesReportRepository.findAll().size();

        // Update the malariaCasesReport using partial update
        MalariaCasesReport partialUpdatedMalariaCasesReport = new MalariaCasesReport();
        partialUpdatedMalariaCasesReport.setId(malariaCasesReport.getId());

        partialUpdatedMalariaCasesReport
            .rdtPositive(UPDATED_RDT_POSITIVE)
            .rdtPf(UPDATED_RDT_PF)
            .microPf(UPDATED_MICRO_PF)
            .microMix(UPDATED_MICRO_MIX)
            .microPother(UPDATED_MICRO_POTHER)
            .probableCases(UPDATED_PROBABLE_CASES)
            .deathCases(UPDATED_DEATH_CASES)
            .treated(UPDATED_TREATED)
            .comment(UPDATED_COMMENT);

        restMalariaCasesReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMalariaCasesReport.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMalariaCasesReport))
            )
            .andExpect(status().isOk());

        // Validate the MalariaCasesReport in the database
        List<MalariaCasesReport> malariaCasesReportList = malariaCasesReportRepository.findAll();
        assertThat(malariaCasesReportList).hasSize(databaseSizeBeforeUpdate);
        MalariaCasesReport testMalariaCasesReport = malariaCasesReportList.get(malariaCasesReportList.size() - 1);
        assertThat(testMalariaCasesReport.getUid()).isEqualTo(DEFAULT_UID);
        assertThat(testMalariaCasesReport.getCreated()).isEqualTo(DEFAULT_CREATED);
        assertThat(testMalariaCasesReport.getLastUpdated()).isEqualTo(DEFAULT_LAST_UPDATED);
        assertThat(testMalariaCasesReport.getRdtTested()).isEqualTo(DEFAULT_RDT_TESTED);
        assertThat(testMalariaCasesReport.getRdtPositive()).isEqualTo(UPDATED_RDT_POSITIVE);
        assertThat(testMalariaCasesReport.getRdtPf()).isEqualTo(UPDATED_RDT_PF);
        assertThat(testMalariaCasesReport.getRdtPv()).isEqualTo(DEFAULT_RDT_PV);
        assertThat(testMalariaCasesReport.getRdtPother()).isEqualTo(DEFAULT_RDT_POTHER);
        assertThat(testMalariaCasesReport.getMicroTested()).isEqualTo(DEFAULT_MICRO_TESTED);
        assertThat(testMalariaCasesReport.getMicroPositive()).isEqualTo(DEFAULT_MICRO_POSITIVE);
        assertThat(testMalariaCasesReport.getMicroPf()).isEqualTo(UPDATED_MICRO_PF);
        assertThat(testMalariaCasesReport.getMicroPv()).isEqualTo(DEFAULT_MICRO_PV);
        assertThat(testMalariaCasesReport.getMicroMix()).isEqualTo(UPDATED_MICRO_MIX);
        assertThat(testMalariaCasesReport.getMicroPother()).isEqualTo(UPDATED_MICRO_POTHER);
        assertThat(testMalariaCasesReport.getProbableCases()).isEqualTo(UPDATED_PROBABLE_CASES);
        assertThat(testMalariaCasesReport.getInpatientCases()).isEqualTo(DEFAULT_INPATIENT_CASES);
        assertThat(testMalariaCasesReport.getDeathCases()).isEqualTo(UPDATED_DEATH_CASES);
        assertThat(testMalariaCasesReport.getTreated()).isEqualTo(UPDATED_TREATED);
        assertThat(testMalariaCasesReport.getSuspectedCases()).isEqualTo(DEFAULT_SUSPECTED_CASES);
        assertThat(testMalariaCasesReport.getTotalFrequents()).isEqualTo(DEFAULT_TOTAL_FREQUENTS);
        assertThat(testMalariaCasesReport.getComment()).isEqualTo(UPDATED_COMMENT);
    }

    @Test
    @Transactional
    void fullUpdateMalariaCasesReportWithPatch() throws Exception {
        // Initialize the database
        malariaCasesReportRepository.saveAndFlush(malariaCasesReport);

        int databaseSizeBeforeUpdate = malariaCasesReportRepository.findAll().size();

        // Update the malariaCasesReport using partial update
        MalariaCasesReport partialUpdatedMalariaCasesReport = new MalariaCasesReport();
        partialUpdatedMalariaCasesReport.setId(malariaCasesReport.getId());

        partialUpdatedMalariaCasesReport
            .uid(UPDATED_UID)
            .created(UPDATED_CREATED)
            .lastUpdated(UPDATED_LAST_UPDATED)
            .rdtTested(UPDATED_RDT_TESTED)
            .rdtPositive(UPDATED_RDT_POSITIVE)
            .rdtPf(UPDATED_RDT_PF)
            .rdtPv(UPDATED_RDT_PV)
            .rdtPother(UPDATED_RDT_POTHER)
            .microTested(UPDATED_MICRO_TESTED)
            .microPositive(UPDATED_MICRO_POSITIVE)
            .microPf(UPDATED_MICRO_PF)
            .microPv(UPDATED_MICRO_PV)
            .microMix(UPDATED_MICRO_MIX)
            .microPother(UPDATED_MICRO_POTHER)
            .probableCases(UPDATED_PROBABLE_CASES)
            .inpatientCases(UPDATED_INPATIENT_CASES)
            .deathCases(UPDATED_DEATH_CASES)
            .treated(UPDATED_TREATED)
            .suspectedCases(UPDATED_SUSPECTED_CASES)
            .totalFrequents(UPDATED_TOTAL_FREQUENTS)
            .comment(UPDATED_COMMENT);

        restMalariaCasesReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMalariaCasesReport.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMalariaCasesReport))
            )
            .andExpect(status().isOk());

        // Validate the MalariaCasesReport in the database
        List<MalariaCasesReport> malariaCasesReportList = malariaCasesReportRepository.findAll();
        assertThat(malariaCasesReportList).hasSize(databaseSizeBeforeUpdate);
        MalariaCasesReport testMalariaCasesReport = malariaCasesReportList.get(malariaCasesReportList.size() - 1);
        assertThat(testMalariaCasesReport.getUid()).isEqualTo(UPDATED_UID);
        assertThat(testMalariaCasesReport.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testMalariaCasesReport.getLastUpdated()).isEqualTo(UPDATED_LAST_UPDATED);
        assertThat(testMalariaCasesReport.getRdtTested()).isEqualTo(UPDATED_RDT_TESTED);
        assertThat(testMalariaCasesReport.getRdtPositive()).isEqualTo(UPDATED_RDT_POSITIVE);
        assertThat(testMalariaCasesReport.getRdtPf()).isEqualTo(UPDATED_RDT_PF);
        assertThat(testMalariaCasesReport.getRdtPv()).isEqualTo(UPDATED_RDT_PV);
        assertThat(testMalariaCasesReport.getRdtPother()).isEqualTo(UPDATED_RDT_POTHER);
        assertThat(testMalariaCasesReport.getMicroTested()).isEqualTo(UPDATED_MICRO_TESTED);
        assertThat(testMalariaCasesReport.getMicroPositive()).isEqualTo(UPDATED_MICRO_POSITIVE);
        assertThat(testMalariaCasesReport.getMicroPf()).isEqualTo(UPDATED_MICRO_PF);
        assertThat(testMalariaCasesReport.getMicroPv()).isEqualTo(UPDATED_MICRO_PV);
        assertThat(testMalariaCasesReport.getMicroMix()).isEqualTo(UPDATED_MICRO_MIX);
        assertThat(testMalariaCasesReport.getMicroPother()).isEqualTo(UPDATED_MICRO_POTHER);
        assertThat(testMalariaCasesReport.getProbableCases()).isEqualTo(UPDATED_PROBABLE_CASES);
        assertThat(testMalariaCasesReport.getInpatientCases()).isEqualTo(UPDATED_INPATIENT_CASES);
        assertThat(testMalariaCasesReport.getDeathCases()).isEqualTo(UPDATED_DEATH_CASES);
        assertThat(testMalariaCasesReport.getTreated()).isEqualTo(UPDATED_TREATED);
        assertThat(testMalariaCasesReport.getSuspectedCases()).isEqualTo(UPDATED_SUSPECTED_CASES);
        assertThat(testMalariaCasesReport.getTotalFrequents()).isEqualTo(UPDATED_TOTAL_FREQUENTS);
        assertThat(testMalariaCasesReport.getComment()).isEqualTo(UPDATED_COMMENT);
    }

    @Test
    @Transactional
    void patchNonExistingMalariaCasesReport() throws Exception {
        int databaseSizeBeforeUpdate = malariaCasesReportRepository.findAll().size();
        malariaCasesReport.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMalariaCasesReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, malariaCasesReport.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(malariaCasesReport))
            )
            .andExpect(status().isBadRequest());

        // Validate the MalariaCasesReport in the database
        List<MalariaCasesReport> malariaCasesReportList = malariaCasesReportRepository.findAll();
        assertThat(malariaCasesReportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMalariaCasesReport() throws Exception {
        int databaseSizeBeforeUpdate = malariaCasesReportRepository.findAll().size();
        malariaCasesReport.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMalariaCasesReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(malariaCasesReport))
            )
            .andExpect(status().isBadRequest());

        // Validate the MalariaCasesReport in the database
        List<MalariaCasesReport> malariaCasesReportList = malariaCasesReportRepository.findAll();
        assertThat(malariaCasesReportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMalariaCasesReport() throws Exception {
        int databaseSizeBeforeUpdate = malariaCasesReportRepository.findAll().size();
        malariaCasesReport.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMalariaCasesReportMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(malariaCasesReport))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the MalariaCasesReport in the database
        List<MalariaCasesReport> malariaCasesReportList = malariaCasesReportRepository.findAll();
        assertThat(malariaCasesReportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMalariaCasesReport() throws Exception {
        // Initialize the database
        malariaCasesReportRepository.saveAndFlush(malariaCasesReport);

        int databaseSizeBeforeDelete = malariaCasesReportRepository.findAll().size();

        // Delete the malariaCasesReport
        restMalariaCasesReportMockMvc
            .perform(delete(ENTITY_API_URL_ID, malariaCasesReport.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<MalariaCasesReport> malariaCasesReportList = malariaCasesReportRepository.findAll();
        assertThat(malariaCasesReportList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
