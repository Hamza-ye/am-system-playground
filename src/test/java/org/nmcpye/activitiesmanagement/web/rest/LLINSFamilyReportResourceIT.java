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
import org.nmcpye.activitiesmanagement.domain.LLINSFamilyReport;
import org.nmcpye.activitiesmanagement.domain.Team;
import org.nmcpye.activitiesmanagement.domain.WorkingDay;
import org.nmcpye.activitiesmanagement.domain.enumeration.FamilyType;
import org.nmcpye.activitiesmanagement.repository.LLINSFamilyReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link LLINSFamilyReportResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class LLINSFamilyReportResourceIT {

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
    private LLINSFamilyReportRepository lLINSFamilyReportRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLLINSFamilyReportMockMvc;

    private LLINSFamilyReport lLINSFamilyReport;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LLINSFamilyReport createEntity(EntityManager em) {
        LLINSFamilyReport lLINSFamilyReport = new LLINSFamilyReport()
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
        lLINSFamilyReport.setDayReached(workingDay);
        // Add required entity
        Team team;
        if (TestUtil.findAll(em, Team.class).isEmpty()) {
            team = TeamResourceIT.createEntity(em);
            em.persist(team);
            em.flush();
        } else {
            team = TestUtil.findAll(em, Team.class).get(0);
        }
        lLINSFamilyReport.setExecutingTeam(team);
        return lLINSFamilyReport;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LLINSFamilyReport createUpdatedEntity(EntityManager em) {
        LLINSFamilyReport lLINSFamilyReport = new LLINSFamilyReport()
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
        lLINSFamilyReport.setDayReached(workingDay);
        // Add required entity
        Team team;
        if (TestUtil.findAll(em, Team.class).isEmpty()) {
            team = TeamResourceIT.createUpdatedEntity(em);
            em.persist(team);
            em.flush();
        } else {
            team = TestUtil.findAll(em, Team.class).get(0);
        }
        lLINSFamilyReport.setExecutingTeam(team);
        return lLINSFamilyReport;
    }

    @BeforeEach
    public void initTest() {
        lLINSFamilyReport = createEntity(em);
    }

    @Test
    @Transactional
    void createLLINSFamilyReport() throws Exception {
        int databaseSizeBeforeCreate = lLINSFamilyReportRepository.findAll().size();
        // Create the LLINSFamilyReport
        restLLINSFamilyReportMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(lLINSFamilyReport))
            )
            .andExpect(status().isCreated());

        // Validate the LLINSFamilyReport in the database
        List<LLINSFamilyReport> lLINSFamilyReportList = lLINSFamilyReportRepository.findAll();
        assertThat(lLINSFamilyReportList).hasSize(databaseSizeBeforeCreate + 1);
        LLINSFamilyReport testLLINSFamilyReport = lLINSFamilyReportList.get(lLINSFamilyReportList.size() - 1);
        assertThat(testLLINSFamilyReport.getUid()).isEqualTo(DEFAULT_UID);
        assertThat(testLLINSFamilyReport.getCreated()).isEqualTo(DEFAULT_CREATED);
        assertThat(testLLINSFamilyReport.getLastUpdated()).isEqualTo(DEFAULT_LAST_UPDATED);
        assertThat(testLLINSFamilyReport.getCheckNo()).isEqualTo(DEFAULT_CHECK_NO);
        assertThat(testLLINSFamilyReport.getMaleIndividuals()).isEqualTo(DEFAULT_MALE_INDIVIDUALS);
        assertThat(testLLINSFamilyReport.getFemaleIndividuals()).isEqualTo(DEFAULT_FEMALE_INDIVIDUALS);
        assertThat(testLLINSFamilyReport.getLessThan5Males()).isEqualTo(DEFAULT_LESS_THAN_5_MALES);
        assertThat(testLLINSFamilyReport.getLessThan5Females()).isEqualTo(DEFAULT_LESS_THAN_5_FEMALES);
        assertThat(testLLINSFamilyReport.getPregnantWomen()).isEqualTo(DEFAULT_PREGNANT_WOMEN);
        assertThat(testLLINSFamilyReport.getQuantityReceived()).isEqualTo(DEFAULT_QUANTITY_RECEIVED);
        assertThat(testLLINSFamilyReport.getFamilyType()).isEqualTo(DEFAULT_FAMILY_TYPE);
        assertThat(testLLINSFamilyReport.getComment()).isEqualTo(DEFAULT_COMMENT);
    }

    @Test
    @Transactional
    void createLLINSFamilyReportWithExistingId() throws Exception {
        // Create the LLINSFamilyReport with an existing ID
        lLINSFamilyReport.setId(1L);

        int databaseSizeBeforeCreate = lLINSFamilyReportRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLLINSFamilyReportMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(lLINSFamilyReport))
            )
            .andExpect(status().isBadRequest());

        // Validate the LLINSFamilyReport in the database
        List<LLINSFamilyReport> lLINSFamilyReportList = lLINSFamilyReportRepository.findAll();
        assertThat(lLINSFamilyReportList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkUidIsRequired() throws Exception {
        int databaseSizeBeforeTest = lLINSFamilyReportRepository.findAll().size();
        // set the field null
        lLINSFamilyReport.setUid(null);

        // Create the LLINSFamilyReport, which fails.

        restLLINSFamilyReportMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(lLINSFamilyReport))
            )
            .andExpect(status().isBadRequest());

        List<LLINSFamilyReport> lLINSFamilyReportList = lLINSFamilyReportRepository.findAll();
        assertThat(lLINSFamilyReportList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMaleIndividualsIsRequired() throws Exception {
        int databaseSizeBeforeTest = lLINSFamilyReportRepository.findAll().size();
        // set the field null
        lLINSFamilyReport.setMaleIndividuals(null);

        // Create the LLINSFamilyReport, which fails.

        restLLINSFamilyReportMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(lLINSFamilyReport))
            )
            .andExpect(status().isBadRequest());

        List<LLINSFamilyReport> lLINSFamilyReportList = lLINSFamilyReportRepository.findAll();
        assertThat(lLINSFamilyReportList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFemaleIndividualsIsRequired() throws Exception {
        int databaseSizeBeforeTest = lLINSFamilyReportRepository.findAll().size();
        // set the field null
        lLINSFamilyReport.setFemaleIndividuals(null);

        // Create the LLINSFamilyReport, which fails.

        restLLINSFamilyReportMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(lLINSFamilyReport))
            )
            .andExpect(status().isBadRequest());

        List<LLINSFamilyReport> lLINSFamilyReportList = lLINSFamilyReportRepository.findAll();
        assertThat(lLINSFamilyReportList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLessThan5MalesIsRequired() throws Exception {
        int databaseSizeBeforeTest = lLINSFamilyReportRepository.findAll().size();
        // set the field null
        lLINSFamilyReport.setLessThan5Males(null);

        // Create the LLINSFamilyReport, which fails.

        restLLINSFamilyReportMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(lLINSFamilyReport))
            )
            .andExpect(status().isBadRequest());

        List<LLINSFamilyReport> lLINSFamilyReportList = lLINSFamilyReportRepository.findAll();
        assertThat(lLINSFamilyReportList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLessThan5FemalesIsRequired() throws Exception {
        int databaseSizeBeforeTest = lLINSFamilyReportRepository.findAll().size();
        // set the field null
        lLINSFamilyReport.setLessThan5Females(null);

        // Create the LLINSFamilyReport, which fails.

        restLLINSFamilyReportMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(lLINSFamilyReport))
            )
            .andExpect(status().isBadRequest());

        List<LLINSFamilyReport> lLINSFamilyReportList = lLINSFamilyReportRepository.findAll();
        assertThat(lLINSFamilyReportList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPregnantWomenIsRequired() throws Exception {
        int databaseSizeBeforeTest = lLINSFamilyReportRepository.findAll().size();
        // set the field null
        lLINSFamilyReport.setPregnantWomen(null);

        // Create the LLINSFamilyReport, which fails.

        restLLINSFamilyReportMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(lLINSFamilyReport))
            )
            .andExpect(status().isBadRequest());

        List<LLINSFamilyReport> lLINSFamilyReportList = lLINSFamilyReportRepository.findAll();
        assertThat(lLINSFamilyReportList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkQuantityReceivedIsRequired() throws Exception {
        int databaseSizeBeforeTest = lLINSFamilyReportRepository.findAll().size();
        // set the field null
        lLINSFamilyReport.setQuantityReceived(null);

        // Create the LLINSFamilyReport, which fails.

        restLLINSFamilyReportMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(lLINSFamilyReport))
            )
            .andExpect(status().isBadRequest());

        List<LLINSFamilyReport> lLINSFamilyReportList = lLINSFamilyReportRepository.findAll();
        assertThat(lLINSFamilyReportList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFamilyTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = lLINSFamilyReportRepository.findAll().size();
        // set the field null
        lLINSFamilyReport.setFamilyType(null);

        // Create the LLINSFamilyReport, which fails.

        restLLINSFamilyReportMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(lLINSFamilyReport))
            )
            .andExpect(status().isBadRequest());

        List<LLINSFamilyReport> lLINSFamilyReportList = lLINSFamilyReportRepository.findAll();
        assertThat(lLINSFamilyReportList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllLLINSFamilyReports() throws Exception {
        // Initialize the database
        lLINSFamilyReportRepository.saveAndFlush(lLINSFamilyReport);

        // Get all the lLINSFamilyReportList
        restLLINSFamilyReportMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(lLINSFamilyReport.getId().intValue())))
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
    void getLLINSFamilyReport() throws Exception {
        // Initialize the database
        lLINSFamilyReportRepository.saveAndFlush(lLINSFamilyReport);

        // Get the lLINSFamilyReport
        restLLINSFamilyReportMockMvc
            .perform(get(ENTITY_API_URL_ID, lLINSFamilyReport.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(lLINSFamilyReport.getId().intValue()))
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
    void getNonExistingLLINSFamilyReport() throws Exception {
        // Get the lLINSFamilyReport
        restLLINSFamilyReportMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewLLINSFamilyReport() throws Exception {
        // Initialize the database
        lLINSFamilyReportRepository.saveAndFlush(lLINSFamilyReport);

        int databaseSizeBeforeUpdate = lLINSFamilyReportRepository.findAll().size();

        // Update the lLINSFamilyReport
        LLINSFamilyReport updatedLLINSFamilyReport = lLINSFamilyReportRepository.findById(lLINSFamilyReport.getId()).get();
        // Disconnect from session so that the updates on updatedLLINSFamilyReport are not directly saved in db
        em.detach(updatedLLINSFamilyReport);
        updatedLLINSFamilyReport
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

        restLLINSFamilyReportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedLLINSFamilyReport.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedLLINSFamilyReport))
            )
            .andExpect(status().isOk());

        // Validate the LLINSFamilyReport in the database
        List<LLINSFamilyReport> lLINSFamilyReportList = lLINSFamilyReportRepository.findAll();
        assertThat(lLINSFamilyReportList).hasSize(databaseSizeBeforeUpdate);
        LLINSFamilyReport testLLINSFamilyReport = lLINSFamilyReportList.get(lLINSFamilyReportList.size() - 1);
        assertThat(testLLINSFamilyReport.getUid()).isEqualTo(UPDATED_UID);
        assertThat(testLLINSFamilyReport.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testLLINSFamilyReport.getLastUpdated()).isEqualTo(UPDATED_LAST_UPDATED);
        assertThat(testLLINSFamilyReport.getCheckNo()).isEqualTo(UPDATED_CHECK_NO);
        assertThat(testLLINSFamilyReport.getMaleIndividuals()).isEqualTo(UPDATED_MALE_INDIVIDUALS);
        assertThat(testLLINSFamilyReport.getFemaleIndividuals()).isEqualTo(UPDATED_FEMALE_INDIVIDUALS);
        assertThat(testLLINSFamilyReport.getLessThan5Males()).isEqualTo(UPDATED_LESS_THAN_5_MALES);
        assertThat(testLLINSFamilyReport.getLessThan5Females()).isEqualTo(UPDATED_LESS_THAN_5_FEMALES);
        assertThat(testLLINSFamilyReport.getPregnantWomen()).isEqualTo(UPDATED_PREGNANT_WOMEN);
        assertThat(testLLINSFamilyReport.getQuantityReceived()).isEqualTo(UPDATED_QUANTITY_RECEIVED);
        assertThat(testLLINSFamilyReport.getFamilyType()).isEqualTo(UPDATED_FAMILY_TYPE);
        assertThat(testLLINSFamilyReport.getComment()).isEqualTo(UPDATED_COMMENT);
    }

    @Test
    @Transactional
    void putNonExistingLLINSFamilyReport() throws Exception {
        int databaseSizeBeforeUpdate = lLINSFamilyReportRepository.findAll().size();
        lLINSFamilyReport.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLLINSFamilyReportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, lLINSFamilyReport.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(lLINSFamilyReport))
            )
            .andExpect(status().isBadRequest());

        // Validate the LLINSFamilyReport in the database
        List<LLINSFamilyReport> lLINSFamilyReportList = lLINSFamilyReportRepository.findAll();
        assertThat(lLINSFamilyReportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLLINSFamilyReport() throws Exception {
        int databaseSizeBeforeUpdate = lLINSFamilyReportRepository.findAll().size();
        lLINSFamilyReport.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLLINSFamilyReportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(lLINSFamilyReport))
            )
            .andExpect(status().isBadRequest());

        // Validate the LLINSFamilyReport in the database
        List<LLINSFamilyReport> lLINSFamilyReportList = lLINSFamilyReportRepository.findAll();
        assertThat(lLINSFamilyReportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLLINSFamilyReport() throws Exception {
        int databaseSizeBeforeUpdate = lLINSFamilyReportRepository.findAll().size();
        lLINSFamilyReport.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLLINSFamilyReportMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(lLINSFamilyReport))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LLINSFamilyReport in the database
        List<LLINSFamilyReport> lLINSFamilyReportList = lLINSFamilyReportRepository.findAll();
        assertThat(lLINSFamilyReportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLLINSFamilyReportWithPatch() throws Exception {
        // Initialize the database
        lLINSFamilyReportRepository.saveAndFlush(lLINSFamilyReport);

        int databaseSizeBeforeUpdate = lLINSFamilyReportRepository.findAll().size();

        // Update the lLINSFamilyReport using partial update
        LLINSFamilyReport partialUpdatedLLINSFamilyReport = new LLINSFamilyReport();
        partialUpdatedLLINSFamilyReport.setId(lLINSFamilyReport.getId());

        partialUpdatedLLINSFamilyReport
            .uid(UPDATED_UID)
            .lastUpdated(UPDATED_LAST_UPDATED)
            .checkNo(UPDATED_CHECK_NO)
            .maleIndividuals(UPDATED_MALE_INDIVIDUALS)
            .femaleIndividuals(UPDATED_FEMALE_INDIVIDUALS)
            .pregnantWomen(UPDATED_PREGNANT_WOMEN)
            .quantityReceived(UPDATED_QUANTITY_RECEIVED)
            .comment(UPDATED_COMMENT);

        restLLINSFamilyReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLLINSFamilyReport.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLLINSFamilyReport))
            )
            .andExpect(status().isOk());

        // Validate the LLINSFamilyReport in the database
        List<LLINSFamilyReport> lLINSFamilyReportList = lLINSFamilyReportRepository.findAll();
        assertThat(lLINSFamilyReportList).hasSize(databaseSizeBeforeUpdate);
        LLINSFamilyReport testLLINSFamilyReport = lLINSFamilyReportList.get(lLINSFamilyReportList.size() - 1);
        assertThat(testLLINSFamilyReport.getUid()).isEqualTo(UPDATED_UID);
        assertThat(testLLINSFamilyReport.getCreated()).isEqualTo(DEFAULT_CREATED);
        assertThat(testLLINSFamilyReport.getLastUpdated()).isEqualTo(UPDATED_LAST_UPDATED);
        assertThat(testLLINSFamilyReport.getCheckNo()).isEqualTo(UPDATED_CHECK_NO);
        assertThat(testLLINSFamilyReport.getMaleIndividuals()).isEqualTo(UPDATED_MALE_INDIVIDUALS);
        assertThat(testLLINSFamilyReport.getFemaleIndividuals()).isEqualTo(UPDATED_FEMALE_INDIVIDUALS);
        assertThat(testLLINSFamilyReport.getLessThan5Males()).isEqualTo(DEFAULT_LESS_THAN_5_MALES);
        assertThat(testLLINSFamilyReport.getLessThan5Females()).isEqualTo(DEFAULT_LESS_THAN_5_FEMALES);
        assertThat(testLLINSFamilyReport.getPregnantWomen()).isEqualTo(UPDATED_PREGNANT_WOMEN);
        assertThat(testLLINSFamilyReport.getQuantityReceived()).isEqualTo(UPDATED_QUANTITY_RECEIVED);
        assertThat(testLLINSFamilyReport.getFamilyType()).isEqualTo(DEFAULT_FAMILY_TYPE);
        assertThat(testLLINSFamilyReport.getComment()).isEqualTo(UPDATED_COMMENT);
    }

    @Test
    @Transactional
    void fullUpdateLLINSFamilyReportWithPatch() throws Exception {
        // Initialize the database
        lLINSFamilyReportRepository.saveAndFlush(lLINSFamilyReport);

        int databaseSizeBeforeUpdate = lLINSFamilyReportRepository.findAll().size();

        // Update the lLINSFamilyReport using partial update
        LLINSFamilyReport partialUpdatedLLINSFamilyReport = new LLINSFamilyReport();
        partialUpdatedLLINSFamilyReport.setId(lLINSFamilyReport.getId());

        partialUpdatedLLINSFamilyReport
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

        restLLINSFamilyReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLLINSFamilyReport.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLLINSFamilyReport))
            )
            .andExpect(status().isOk());

        // Validate the LLINSFamilyReport in the database
        List<LLINSFamilyReport> lLINSFamilyReportList = lLINSFamilyReportRepository.findAll();
        assertThat(lLINSFamilyReportList).hasSize(databaseSizeBeforeUpdate);
        LLINSFamilyReport testLLINSFamilyReport = lLINSFamilyReportList.get(lLINSFamilyReportList.size() - 1);
        assertThat(testLLINSFamilyReport.getUid()).isEqualTo(UPDATED_UID);
        assertThat(testLLINSFamilyReport.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testLLINSFamilyReport.getLastUpdated()).isEqualTo(UPDATED_LAST_UPDATED);
        assertThat(testLLINSFamilyReport.getCheckNo()).isEqualTo(UPDATED_CHECK_NO);
        assertThat(testLLINSFamilyReport.getMaleIndividuals()).isEqualTo(UPDATED_MALE_INDIVIDUALS);
        assertThat(testLLINSFamilyReport.getFemaleIndividuals()).isEqualTo(UPDATED_FEMALE_INDIVIDUALS);
        assertThat(testLLINSFamilyReport.getLessThan5Males()).isEqualTo(UPDATED_LESS_THAN_5_MALES);
        assertThat(testLLINSFamilyReport.getLessThan5Females()).isEqualTo(UPDATED_LESS_THAN_5_FEMALES);
        assertThat(testLLINSFamilyReport.getPregnantWomen()).isEqualTo(UPDATED_PREGNANT_WOMEN);
        assertThat(testLLINSFamilyReport.getQuantityReceived()).isEqualTo(UPDATED_QUANTITY_RECEIVED);
        assertThat(testLLINSFamilyReport.getFamilyType()).isEqualTo(UPDATED_FAMILY_TYPE);
        assertThat(testLLINSFamilyReport.getComment()).isEqualTo(UPDATED_COMMENT);
    }

    @Test
    @Transactional
    void patchNonExistingLLINSFamilyReport() throws Exception {
        int databaseSizeBeforeUpdate = lLINSFamilyReportRepository.findAll().size();
        lLINSFamilyReport.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLLINSFamilyReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, lLINSFamilyReport.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(lLINSFamilyReport))
            )
            .andExpect(status().isBadRequest());

        // Validate the LLINSFamilyReport in the database
        List<LLINSFamilyReport> lLINSFamilyReportList = lLINSFamilyReportRepository.findAll();
        assertThat(lLINSFamilyReportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLLINSFamilyReport() throws Exception {
        int databaseSizeBeforeUpdate = lLINSFamilyReportRepository.findAll().size();
        lLINSFamilyReport.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLLINSFamilyReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(lLINSFamilyReport))
            )
            .andExpect(status().isBadRequest());

        // Validate the LLINSFamilyReport in the database
        List<LLINSFamilyReport> lLINSFamilyReportList = lLINSFamilyReportRepository.findAll();
        assertThat(lLINSFamilyReportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLLINSFamilyReport() throws Exception {
        int databaseSizeBeforeUpdate = lLINSFamilyReportRepository.findAll().size();
        lLINSFamilyReport.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLLINSFamilyReportMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(lLINSFamilyReport))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LLINSFamilyReport in the database
        List<LLINSFamilyReport> lLINSFamilyReportList = lLINSFamilyReportRepository.findAll();
        assertThat(lLINSFamilyReportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLLINSFamilyReport() throws Exception {
        // Initialize the database
        lLINSFamilyReportRepository.saveAndFlush(lLINSFamilyReport);

        int databaseSizeBeforeDelete = lLINSFamilyReportRepository.findAll().size();

        // Delete the lLINSFamilyReport
        restLLINSFamilyReportMockMvc
            .perform(delete(ENTITY_API_URL_ID, lLINSFamilyReport.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<LLINSFamilyReport> lLINSFamilyReportList = lLINSFamilyReportRepository.findAll();
        assertThat(lLINSFamilyReportList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
