package org.nmcpye.activitiesmanagement.web.rest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.nmcpye.activitiesmanagement.IntegrationTest;
import org.nmcpye.activitiesmanagement.domain.LlinsFamilyReport;
import org.nmcpye.activitiesmanagement.domain.Team;
import org.nmcpye.activitiesmanagement.domain.WorkingDay;
import org.nmcpye.activitiesmanagement.domain.enumeration.FamilyType;
import org.nmcpye.activitiesmanagement.repository.LlinsFamilyReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link LlinsFamilyReportResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class LlinsFamilyReportResourceIT {

    private static final String DEFAULT_UID = "AAAAAAAAAA";
    private static final String UPDATED_UID = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_LAST_UPDATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_UPDATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Integer DEFAULT_CHECK_NO = 1;
    private static final Integer UPDATED_CHECK_NO = 2;

    private static final Integer DEFAULT_MALE_INDIVIDUALS = 0;
    private static final Integer UPDATED_MALE_INDIVIDUALS = 1;

    private static final Integer DEFAULT_FEMALE_INDIVIDUALS = 0;
    private static final Integer UPDATED_FEMALE_INDIVIDUALS = 1;

    private static final Integer DEFAULT_LESS_THAN_5_MALES = 0;
    private static final Integer UPDATED_LESS_THAN_5_MALES = 1;

    private static final Integer DEFAULT_LESS_THAN_5_FEMALES = 0;
    private static final Integer UPDATED_LESS_THAN_5_FEMALES = 1;

    private static final Integer DEFAULT_PREGNANT_WOMEN = 0;
    private static final Integer UPDATED_PREGNANT_WOMEN = 1;

    private static final Integer DEFAULT_QUANTITY_RECEIVED = 0;
    private static final Integer UPDATED_QUANTITY_RECEIVED = 1;

    private static final FamilyType DEFAULT_FAMILY_TYPE = FamilyType.RESIDENT;
    private static final FamilyType UPDATED_FAMILY_TYPE = FamilyType.IDPS;

    private static final String DEFAULT_COMMENT = "AAAAAAAAAA";
    private static final String UPDATED_COMMENT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/llins-family-reports";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private LlinsFamilyReportRepository llinsFamilyReportRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLlinsFamilyReportMockMvc;

    private LlinsFamilyReport llinsFamilyReport;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LlinsFamilyReport createEntity(EntityManager em) {
        LlinsFamilyReport llinsFamilyReport = new LlinsFamilyReport()
            .uid(DEFAULT_UID)
            .created(DEFAULT_CREATED)
            .lastUpdated(DEFAULT_LAST_UPDATED)
            .checkNo(DEFAULT_CHECK_NO)
            .maleIndividuals(DEFAULT_MALE_INDIVIDUALS)
            .femaleIndividuals(DEFAULT_FEMALE_INDIVIDUALS)
            .lessThan5Males(DEFAULT_LESS_THAN_5_MALES)
            .lessThan5Females(DEFAULT_LESS_THAN_5_FEMALES)
            .pregnantWomen(DEFAULT_PREGNANT_WOMEN)
            .quantityReceived(DEFAULT_QUANTITY_RECEIVED)
            .familyType(DEFAULT_FAMILY_TYPE)
            .comment(DEFAULT_COMMENT);
        // Add required entity
        WorkingDay workingDay;
        if (TestUtil.findAll(em, WorkingDay.class).isEmpty()) {
            workingDay = WorkingDayResourceIT.createEntity(em);
            em.persist(workingDay);
            em.flush();
        } else {
            workingDay = TestUtil.findAll(em, WorkingDay.class).get(0);
        }
        llinsFamilyReport.setDayReached(workingDay);
        // Add required entity
        Team team;
        if (TestUtil.findAll(em, Team.class).isEmpty()) {
            team = TeamResourceIT.createEntity(em);
            em.persist(team);
            em.flush();
        } else {
            team = TestUtil.findAll(em, Team.class).get(0);
        }
        llinsFamilyReport.setExecutingTeam(team);
        return llinsFamilyReport;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LlinsFamilyReport createUpdatedEntity(EntityManager em) {
        LlinsFamilyReport llinsFamilyReport = new LlinsFamilyReport()
            .uid(UPDATED_UID)
            .created(UPDATED_CREATED)
            .lastUpdated(UPDATED_LAST_UPDATED)
            .checkNo(UPDATED_CHECK_NO)
            .maleIndividuals(UPDATED_MALE_INDIVIDUALS)
            .femaleIndividuals(UPDATED_FEMALE_INDIVIDUALS)
            .lessThan5Males(UPDATED_LESS_THAN_5_MALES)
            .lessThan5Females(UPDATED_LESS_THAN_5_FEMALES)
            .pregnantWomen(UPDATED_PREGNANT_WOMEN)
            .quantityReceived(UPDATED_QUANTITY_RECEIVED)
            .familyType(UPDATED_FAMILY_TYPE)
            .comment(UPDATED_COMMENT);
        // Add required entity
        WorkingDay workingDay;
        if (TestUtil.findAll(em, WorkingDay.class).isEmpty()) {
            workingDay = WorkingDayResourceIT.createUpdatedEntity(em);
            em.persist(workingDay);
            em.flush();
        } else {
            workingDay = TestUtil.findAll(em, WorkingDay.class).get(0);
        }
        llinsFamilyReport.setDayReached(workingDay);
        // Add required entity
        Team team;
        if (TestUtil.findAll(em, Team.class).isEmpty()) {
            team = TeamResourceIT.createUpdatedEntity(em);
            em.persist(team);
            em.flush();
        } else {
            team = TestUtil.findAll(em, Team.class).get(0);
        }
        llinsFamilyReport.setExecutingTeam(team);
        return llinsFamilyReport;
    }

    @BeforeEach
    public void initTest() {
        llinsFamilyReport = createEntity(em);
    }

    @Test
    @Transactional
    void createLlinsFamilyReport() throws Exception {
        int databaseSizeBeforeCreate = llinsFamilyReportRepository.findAll().size();
        // Create the LlinsFamilyReport
        restLlinsFamilyReportMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(llinsFamilyReport))
            )
            .andExpect(status().isCreated());

        // Validate the LlinsFamilyReport in the database
        List<LlinsFamilyReport> llinsFamilyReportList = llinsFamilyReportRepository.findAll();
        assertThat(llinsFamilyReportList).hasSize(databaseSizeBeforeCreate + 1);
        LlinsFamilyReport testLlinsFamilyReport = llinsFamilyReportList.get(llinsFamilyReportList.size() - 1);
        assertThat(testLlinsFamilyReport.getUid()).isEqualTo(DEFAULT_UID);
        assertThat(testLlinsFamilyReport.getCreated()).isEqualTo(DEFAULT_CREATED);
        assertThat(testLlinsFamilyReport.getLastUpdated()).isEqualTo(DEFAULT_LAST_UPDATED);
        assertThat(testLlinsFamilyReport.getCheckNo()).isEqualTo(DEFAULT_CHECK_NO);
        assertThat(testLlinsFamilyReport.getMaleIndividuals()).isEqualTo(DEFAULT_MALE_INDIVIDUALS);
        assertThat(testLlinsFamilyReport.getFemaleIndividuals()).isEqualTo(DEFAULT_FEMALE_INDIVIDUALS);
        assertThat(testLlinsFamilyReport.getLessThan5Males()).isEqualTo(DEFAULT_LESS_THAN_5_MALES);
        assertThat(testLlinsFamilyReport.getLessThan5Females()).isEqualTo(DEFAULT_LESS_THAN_5_FEMALES);
        assertThat(testLlinsFamilyReport.getPregnantWomen()).isEqualTo(DEFAULT_PREGNANT_WOMEN);
        assertThat(testLlinsFamilyReport.getQuantityReceived()).isEqualTo(DEFAULT_QUANTITY_RECEIVED);
        assertThat(testLlinsFamilyReport.getFamilyType()).isEqualTo(DEFAULT_FAMILY_TYPE);
        assertThat(testLlinsFamilyReport.getComment()).isEqualTo(DEFAULT_COMMENT);
    }

    @Test
    @Transactional
    void createLlinsFamilyReportWithExistingId() throws Exception {
        // Create the LlinsFamilyReport with an existing ID
        llinsFamilyReport.setId(1L);

        int databaseSizeBeforeCreate = llinsFamilyReportRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLlinsFamilyReportMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(llinsFamilyReport))
            )
            .andExpect(status().isBadRequest());

        // Validate the LlinsFamilyReport in the database
        List<LlinsFamilyReport> llinsFamilyReportList = llinsFamilyReportRepository.findAll();
        assertThat(llinsFamilyReportList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkUidIsRequired() throws Exception {
        int databaseSizeBeforeTest = llinsFamilyReportRepository.findAll().size();
        // set the field null
        llinsFamilyReport.setUid(null);

        // Create the LlinsFamilyReport, which fails.

        restLlinsFamilyReportMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(llinsFamilyReport))
            )
            .andExpect(status().isBadRequest());

        List<LlinsFamilyReport> llinsFamilyReportList = llinsFamilyReportRepository.findAll();
        assertThat(llinsFamilyReportList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMaleIndividualsIsRequired() throws Exception {
        int databaseSizeBeforeTest = llinsFamilyReportRepository.findAll().size();
        // set the field null
        llinsFamilyReport.setMaleIndividuals(null);

        // Create the LlinsFamilyReport, which fails.

        restLlinsFamilyReportMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(llinsFamilyReport))
            )
            .andExpect(status().isBadRequest());

        List<LlinsFamilyReport> llinsFamilyReportList = llinsFamilyReportRepository.findAll();
        assertThat(llinsFamilyReportList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFemaleIndividualsIsRequired() throws Exception {
        int databaseSizeBeforeTest = llinsFamilyReportRepository.findAll().size();
        // set the field null
        llinsFamilyReport.setFemaleIndividuals(null);

        // Create the LlinsFamilyReport, which fails.

        restLlinsFamilyReportMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(llinsFamilyReport))
            )
            .andExpect(status().isBadRequest());

        List<LlinsFamilyReport> llinsFamilyReportList = llinsFamilyReportRepository.findAll();
        assertThat(llinsFamilyReportList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLessThan5MalesIsRequired() throws Exception {
        int databaseSizeBeforeTest = llinsFamilyReportRepository.findAll().size();
        // set the field null
        llinsFamilyReport.setLessThan5Males(null);

        // Create the LlinsFamilyReport, which fails.

        restLlinsFamilyReportMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(llinsFamilyReport))
            )
            .andExpect(status().isBadRequest());

        List<LlinsFamilyReport> llinsFamilyReportList = llinsFamilyReportRepository.findAll();
        assertThat(llinsFamilyReportList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLessThan5FemalesIsRequired() throws Exception {
        int databaseSizeBeforeTest = llinsFamilyReportRepository.findAll().size();
        // set the field null
        llinsFamilyReport.setLessThan5Females(null);

        // Create the LlinsFamilyReport, which fails.

        restLlinsFamilyReportMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(llinsFamilyReport))
            )
            .andExpect(status().isBadRequest());

        List<LlinsFamilyReport> llinsFamilyReportList = llinsFamilyReportRepository.findAll();
        assertThat(llinsFamilyReportList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPregnantWomenIsRequired() throws Exception {
        int databaseSizeBeforeTest = llinsFamilyReportRepository.findAll().size();
        // set the field null
        llinsFamilyReport.setPregnantWomen(null);

        // Create the LlinsFamilyReport, which fails.

        restLlinsFamilyReportMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(llinsFamilyReport))
            )
            .andExpect(status().isBadRequest());

        List<LlinsFamilyReport> llinsFamilyReportList = llinsFamilyReportRepository.findAll();
        assertThat(llinsFamilyReportList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkQuantityReceivedIsRequired() throws Exception {
        int databaseSizeBeforeTest = llinsFamilyReportRepository.findAll().size();
        // set the field null
        llinsFamilyReport.setQuantityReceived(null);

        // Create the LlinsFamilyReport, which fails.

        restLlinsFamilyReportMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(llinsFamilyReport))
            )
            .andExpect(status().isBadRequest());

        List<LlinsFamilyReport> llinsFamilyReportList = llinsFamilyReportRepository.findAll();
        assertThat(llinsFamilyReportList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFamilyTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = llinsFamilyReportRepository.findAll().size();
        // set the field null
        llinsFamilyReport.setFamilyType(null);

        // Create the LlinsFamilyReport, which fails.

        restLlinsFamilyReportMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(llinsFamilyReport))
            )
            .andExpect(status().isBadRequest());

        List<LlinsFamilyReport> llinsFamilyReportList = llinsFamilyReportRepository.findAll();
        assertThat(llinsFamilyReportList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllLlinsFamilyReports() throws Exception {
        // Initialize the database
        llinsFamilyReportRepository.saveAndFlush(llinsFamilyReport);

        // Get all the llinsFamilyReportList
        restLlinsFamilyReportMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(llinsFamilyReport.getId().intValue())))
            .andExpect(jsonPath("$.[*].uid").value(hasItem(DEFAULT_UID)))
            .andExpect(jsonPath("$.[*].created").value(hasItem(DEFAULT_CREATED.toString())))
            .andExpect(jsonPath("$.[*].lastUpdated").value(hasItem(DEFAULT_LAST_UPDATED.toString())))
            .andExpect(jsonPath("$.[*].checkNo").value(hasItem(DEFAULT_CHECK_NO)))
            .andExpect(jsonPath("$.[*].maleIndividuals").value(hasItem(DEFAULT_MALE_INDIVIDUALS)))
            .andExpect(jsonPath("$.[*].femaleIndividuals").value(hasItem(DEFAULT_FEMALE_INDIVIDUALS)))
            .andExpect(jsonPath("$.[*].lessThan5Males").value(hasItem(DEFAULT_LESS_THAN_5_MALES)))
            .andExpect(jsonPath("$.[*].lessThan5Females").value(hasItem(DEFAULT_LESS_THAN_5_FEMALES)))
            .andExpect(jsonPath("$.[*].pregnantWomen").value(hasItem(DEFAULT_PREGNANT_WOMEN)))
            .andExpect(jsonPath("$.[*].quantityReceived").value(hasItem(DEFAULT_QUANTITY_RECEIVED)))
            .andExpect(jsonPath("$.[*].familyType").value(hasItem(DEFAULT_FAMILY_TYPE.toString())))
            .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT)));
    }

    @Test
    @Transactional
    void getLlinsFamilyReport() throws Exception {
        // Initialize the database
        llinsFamilyReportRepository.saveAndFlush(llinsFamilyReport);

        // Get the llinsFamilyReport
        restLlinsFamilyReportMockMvc
            .perform(get(ENTITY_API_URL_ID, llinsFamilyReport.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(llinsFamilyReport.getId().intValue()))
            .andExpect(jsonPath("$.uid").value(DEFAULT_UID))
            .andExpect(jsonPath("$.created").value(DEFAULT_CREATED.toString()))
            .andExpect(jsonPath("$.lastUpdated").value(DEFAULT_LAST_UPDATED.toString()))
            .andExpect(jsonPath("$.checkNo").value(DEFAULT_CHECK_NO))
            .andExpect(jsonPath("$.maleIndividuals").value(DEFAULT_MALE_INDIVIDUALS))
            .andExpect(jsonPath("$.femaleIndividuals").value(DEFAULT_FEMALE_INDIVIDUALS))
            .andExpect(jsonPath("$.lessThan5Males").value(DEFAULT_LESS_THAN_5_MALES))
            .andExpect(jsonPath("$.lessThan5Females").value(DEFAULT_LESS_THAN_5_FEMALES))
            .andExpect(jsonPath("$.pregnantWomen").value(DEFAULT_PREGNANT_WOMEN))
            .andExpect(jsonPath("$.quantityReceived").value(DEFAULT_QUANTITY_RECEIVED))
            .andExpect(jsonPath("$.familyType").value(DEFAULT_FAMILY_TYPE.toString()))
            .andExpect(jsonPath("$.comment").value(DEFAULT_COMMENT));
    }

    @Test
    @Transactional
    void getNonExistingLlinsFamilyReport() throws Exception {
        // Get the llinsFamilyReport
        restLlinsFamilyReportMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewLlinsFamilyReport() throws Exception {
        // Initialize the database
        llinsFamilyReportRepository.saveAndFlush(llinsFamilyReport);

        int databaseSizeBeforeUpdate = llinsFamilyReportRepository.findAll().size();

        // Update the llinsFamilyReport
        LlinsFamilyReport updatedLlinsFamilyReport = llinsFamilyReportRepository.findById(llinsFamilyReport.getId()).get();
        // Disconnect from session so that the updates on updatedLlinsFamilyReport are not directly saved in db
        em.detach(updatedLlinsFamilyReport);
        updatedLlinsFamilyReport
            .uid(UPDATED_UID)
            .created(UPDATED_CREATED)
            .lastUpdated(UPDATED_LAST_UPDATED)
            .checkNo(UPDATED_CHECK_NO)
            .maleIndividuals(UPDATED_MALE_INDIVIDUALS)
            .femaleIndividuals(UPDATED_FEMALE_INDIVIDUALS)
            .lessThan5Males(UPDATED_LESS_THAN_5_MALES)
            .lessThan5Females(UPDATED_LESS_THAN_5_FEMALES)
            .pregnantWomen(UPDATED_PREGNANT_WOMEN)
            .quantityReceived(UPDATED_QUANTITY_RECEIVED)
            .familyType(UPDATED_FAMILY_TYPE)
            .comment(UPDATED_COMMENT);

        restLlinsFamilyReportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedLlinsFamilyReport.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedLlinsFamilyReport))
            )
            .andExpect(status().isOk());

        // Validate the LlinsFamilyReport in the database
        List<LlinsFamilyReport> llinsFamilyReportList = llinsFamilyReportRepository.findAll();
        assertThat(llinsFamilyReportList).hasSize(databaseSizeBeforeUpdate);
        LlinsFamilyReport testLlinsFamilyReport = llinsFamilyReportList.get(llinsFamilyReportList.size() - 1);
        assertThat(testLlinsFamilyReport.getUid()).isEqualTo(UPDATED_UID);
        assertThat(testLlinsFamilyReport.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testLlinsFamilyReport.getLastUpdated()).isEqualTo(UPDATED_LAST_UPDATED);
        assertThat(testLlinsFamilyReport.getCheckNo()).isEqualTo(UPDATED_CHECK_NO);
        assertThat(testLlinsFamilyReport.getMaleIndividuals()).isEqualTo(UPDATED_MALE_INDIVIDUALS);
        assertThat(testLlinsFamilyReport.getFemaleIndividuals()).isEqualTo(UPDATED_FEMALE_INDIVIDUALS);
        assertThat(testLlinsFamilyReport.getLessThan5Males()).isEqualTo(UPDATED_LESS_THAN_5_MALES);
        assertThat(testLlinsFamilyReport.getLessThan5Females()).isEqualTo(UPDATED_LESS_THAN_5_FEMALES);
        assertThat(testLlinsFamilyReport.getPregnantWomen()).isEqualTo(UPDATED_PREGNANT_WOMEN);
        assertThat(testLlinsFamilyReport.getQuantityReceived()).isEqualTo(UPDATED_QUANTITY_RECEIVED);
        assertThat(testLlinsFamilyReport.getFamilyType()).isEqualTo(UPDATED_FAMILY_TYPE);
        assertThat(testLlinsFamilyReport.getComment()).isEqualTo(UPDATED_COMMENT);
    }

    @Test
    @Transactional
    void putNonExistingLlinsFamilyReport() throws Exception {
        int databaseSizeBeforeUpdate = llinsFamilyReportRepository.findAll().size();
        llinsFamilyReport.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLlinsFamilyReportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, llinsFamilyReport.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(llinsFamilyReport))
            )
            .andExpect(status().isBadRequest());

        // Validate the LlinsFamilyReport in the database
        List<LlinsFamilyReport> llinsFamilyReportList = llinsFamilyReportRepository.findAll();
        assertThat(llinsFamilyReportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLlinsFamilyReport() throws Exception {
        int databaseSizeBeforeUpdate = llinsFamilyReportRepository.findAll().size();
        llinsFamilyReport.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLlinsFamilyReportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(llinsFamilyReport))
            )
            .andExpect(status().isBadRequest());

        // Validate the LlinsFamilyReport in the database
        List<LlinsFamilyReport> llinsFamilyReportList = llinsFamilyReportRepository.findAll();
        assertThat(llinsFamilyReportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLlinsFamilyReport() throws Exception {
        int databaseSizeBeforeUpdate = llinsFamilyReportRepository.findAll().size();
        llinsFamilyReport.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLlinsFamilyReportMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(llinsFamilyReport))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LlinsFamilyReport in the database
        List<LlinsFamilyReport> llinsFamilyReportList = llinsFamilyReportRepository.findAll();
        assertThat(llinsFamilyReportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLlinsFamilyReportWithPatch() throws Exception {
        // Initialize the database
        llinsFamilyReportRepository.saveAndFlush(llinsFamilyReport);

        int databaseSizeBeforeUpdate = llinsFamilyReportRepository.findAll().size();

        // Update the llinsFamilyReport using partial update
        LlinsFamilyReport partialUpdatedLlinsFamilyReport = new LlinsFamilyReport();
        partialUpdatedLlinsFamilyReport.setId(llinsFamilyReport.getId());

        partialUpdatedLlinsFamilyReport
            .uid(UPDATED_UID)
            .checkNo(UPDATED_CHECK_NO)
            .maleIndividuals(UPDATED_MALE_INDIVIDUALS)
            .quantityReceived(UPDATED_QUANTITY_RECEIVED);

        restLlinsFamilyReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLlinsFamilyReport.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLlinsFamilyReport))
            )
            .andExpect(status().isOk());

        // Validate the LlinsFamilyReport in the database
        List<LlinsFamilyReport> llinsFamilyReportList = llinsFamilyReportRepository.findAll();
        assertThat(llinsFamilyReportList).hasSize(databaseSizeBeforeUpdate);
        LlinsFamilyReport testLlinsFamilyReport = llinsFamilyReportList.get(llinsFamilyReportList.size() - 1);
        assertThat(testLlinsFamilyReport.getUid()).isEqualTo(UPDATED_UID);
        assertThat(testLlinsFamilyReport.getCreated()).isEqualTo(DEFAULT_CREATED);
        assertThat(testLlinsFamilyReport.getLastUpdated()).isEqualTo(DEFAULT_LAST_UPDATED);
        assertThat(testLlinsFamilyReport.getCheckNo()).isEqualTo(UPDATED_CHECK_NO);
        assertThat(testLlinsFamilyReport.getMaleIndividuals()).isEqualTo(UPDATED_MALE_INDIVIDUALS);
        assertThat(testLlinsFamilyReport.getFemaleIndividuals()).isEqualTo(DEFAULT_FEMALE_INDIVIDUALS);
        assertThat(testLlinsFamilyReport.getLessThan5Males()).isEqualTo(DEFAULT_LESS_THAN_5_MALES);
        assertThat(testLlinsFamilyReport.getLessThan5Females()).isEqualTo(DEFAULT_LESS_THAN_5_FEMALES);
        assertThat(testLlinsFamilyReport.getPregnantWomen()).isEqualTo(DEFAULT_PREGNANT_WOMEN);
        assertThat(testLlinsFamilyReport.getQuantityReceived()).isEqualTo(UPDATED_QUANTITY_RECEIVED);
        assertThat(testLlinsFamilyReport.getFamilyType()).isEqualTo(DEFAULT_FAMILY_TYPE);
        assertThat(testLlinsFamilyReport.getComment()).isEqualTo(DEFAULT_COMMENT);
    }

    @Test
    @Transactional
    void fullUpdateLlinsFamilyReportWithPatch() throws Exception {
        // Initialize the database
        llinsFamilyReportRepository.saveAndFlush(llinsFamilyReport);

        int databaseSizeBeforeUpdate = llinsFamilyReportRepository.findAll().size();

        // Update the llinsFamilyReport using partial update
        LlinsFamilyReport partialUpdatedLlinsFamilyReport = new LlinsFamilyReport();
        partialUpdatedLlinsFamilyReport.setId(llinsFamilyReport.getId());

        partialUpdatedLlinsFamilyReport
            .uid(UPDATED_UID)
            .created(UPDATED_CREATED)
            .lastUpdated(UPDATED_LAST_UPDATED)
            .checkNo(UPDATED_CHECK_NO)
            .maleIndividuals(UPDATED_MALE_INDIVIDUALS)
            .femaleIndividuals(UPDATED_FEMALE_INDIVIDUALS)
            .lessThan5Males(UPDATED_LESS_THAN_5_MALES)
            .lessThan5Females(UPDATED_LESS_THAN_5_FEMALES)
            .pregnantWomen(UPDATED_PREGNANT_WOMEN)
            .quantityReceived(UPDATED_QUANTITY_RECEIVED)
            .familyType(UPDATED_FAMILY_TYPE)
            .comment(UPDATED_COMMENT);

        restLlinsFamilyReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLlinsFamilyReport.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLlinsFamilyReport))
            )
            .andExpect(status().isOk());

        // Validate the LlinsFamilyReport in the database
        List<LlinsFamilyReport> llinsFamilyReportList = llinsFamilyReportRepository.findAll();
        assertThat(llinsFamilyReportList).hasSize(databaseSizeBeforeUpdate);
        LlinsFamilyReport testLlinsFamilyReport = llinsFamilyReportList.get(llinsFamilyReportList.size() - 1);
        assertThat(testLlinsFamilyReport.getUid()).isEqualTo(UPDATED_UID);
        assertThat(testLlinsFamilyReport.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testLlinsFamilyReport.getLastUpdated()).isEqualTo(UPDATED_LAST_UPDATED);
        assertThat(testLlinsFamilyReport.getCheckNo()).isEqualTo(UPDATED_CHECK_NO);
        assertThat(testLlinsFamilyReport.getMaleIndividuals()).isEqualTo(UPDATED_MALE_INDIVIDUALS);
        assertThat(testLlinsFamilyReport.getFemaleIndividuals()).isEqualTo(UPDATED_FEMALE_INDIVIDUALS);
        assertThat(testLlinsFamilyReport.getLessThan5Males()).isEqualTo(UPDATED_LESS_THAN_5_MALES);
        assertThat(testLlinsFamilyReport.getLessThan5Females()).isEqualTo(UPDATED_LESS_THAN_5_FEMALES);
        assertThat(testLlinsFamilyReport.getPregnantWomen()).isEqualTo(UPDATED_PREGNANT_WOMEN);
        assertThat(testLlinsFamilyReport.getQuantityReceived()).isEqualTo(UPDATED_QUANTITY_RECEIVED);
        assertThat(testLlinsFamilyReport.getFamilyType()).isEqualTo(UPDATED_FAMILY_TYPE);
        assertThat(testLlinsFamilyReport.getComment()).isEqualTo(UPDATED_COMMENT);
    }

    @Test
    @Transactional
    void patchNonExistingLlinsFamilyReport() throws Exception {
        int databaseSizeBeforeUpdate = llinsFamilyReportRepository.findAll().size();
        llinsFamilyReport.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLlinsFamilyReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, llinsFamilyReport.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(llinsFamilyReport))
            )
            .andExpect(status().isBadRequest());

        // Validate the LlinsFamilyReport in the database
        List<LlinsFamilyReport> llinsFamilyReportList = llinsFamilyReportRepository.findAll();
        assertThat(llinsFamilyReportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLlinsFamilyReport() throws Exception {
        int databaseSizeBeforeUpdate = llinsFamilyReportRepository.findAll().size();
        llinsFamilyReport.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLlinsFamilyReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(llinsFamilyReport))
            )
            .andExpect(status().isBadRequest());

        // Validate the LlinsFamilyReport in the database
        List<LlinsFamilyReport> llinsFamilyReportList = llinsFamilyReportRepository.findAll();
        assertThat(llinsFamilyReportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLlinsFamilyReport() throws Exception {
        int databaseSizeBeforeUpdate = llinsFamilyReportRepository.findAll().size();
        llinsFamilyReport.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLlinsFamilyReportMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(llinsFamilyReport))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LlinsFamilyReport in the database
        List<LlinsFamilyReport> llinsFamilyReportList = llinsFamilyReportRepository.findAll();
        assertThat(llinsFamilyReportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLlinsFamilyReport() throws Exception {
        // Initialize the database
        llinsFamilyReportRepository.saveAndFlush(llinsFamilyReport);

        int databaseSizeBeforeDelete = llinsFamilyReportRepository.findAll().size();

        // Delete the llinsFamilyReport
        restLlinsFamilyReportMockMvc
            .perform(delete(ENTITY_API_URL_ID, llinsFamilyReport.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<LlinsFamilyReport> llinsFamilyReportList = llinsFamilyReportRepository.findAll();
        assertThat(llinsFamilyReportList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
