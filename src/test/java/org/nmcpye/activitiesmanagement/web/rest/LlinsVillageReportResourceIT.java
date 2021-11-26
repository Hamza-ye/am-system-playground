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
import org.nmcpye.activitiesmanagement.domain.LlinsVillageReport;
import org.nmcpye.activitiesmanagement.domain.Team;
import org.nmcpye.activitiesmanagement.domain.WorkingDay;
import org.nmcpye.activitiesmanagement.repository.LlinsVillageReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link LlinsVillageReportResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class LlinsVillageReportResourceIT {

    private static final String DEFAULT_UID = "AAAAAAAAAA";
    private static final String UPDATED_UID = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_LAST_UPDATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_UPDATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Integer DEFAULT_HOUSES = 0;
    private static final Integer UPDATED_HOUSES = 1;

    private static final Integer DEFAULT_RESIDENT_HOUSEHOLD = 0;
    private static final Integer UPDATED_RESIDENT_HOUSEHOLD = 1;

    private static final Integer DEFAULT_IDPS_HOUSEHOLD = 0;
    private static final Integer UPDATED_IDPS_HOUSEHOLD = 1;

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

    private static final String DEFAULT_COMMENT = "AAAAAAAAAA";
    private static final String UPDATED_COMMENT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/llins-village-reports";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private LlinsVillageReportRepository lLINSVillageReportRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLLINSVillageReportMockMvc;

    private LlinsVillageReport lLINSVillageReport;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LlinsVillageReport createEntity(EntityManager em) {
        LlinsVillageReport lLINSVillageReport = new LlinsVillageReport()
            .uid(DEFAULT_UID)
            .created(DEFAULT_CREATED)
            .lastUpdated(DEFAULT_LAST_UPDATED)
            .houses(DEFAULT_HOUSES)
            .residentHousehold(DEFAULT_RESIDENT_HOUSEHOLD)
            .idpsHousehold(DEFAULT_IDPS_HOUSEHOLD)
            .maleIndividuals(DEFAULT_MALE_INDIVIDUALS)
            .femaleIndividuals(DEFAULT_FEMALE_INDIVIDUALS)
            .lessThan5Males(DEFAULT_LESS_THAN_5_MALES)
            .lessThan5Females(DEFAULT_LESS_THAN_5_FEMALES)
            .pregnantWomen(DEFAULT_PREGNANT_WOMEN)
            .quantityReceived(DEFAULT_QUANTITY_RECEIVED)
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
        lLINSVillageReport.setDayReached(workingDay);
        // Add required entity
        Team team;
        if (TestUtil.findAll(em, Team.class).isEmpty()) {
            team = TeamResourceIT.createEntity(em);
            em.persist(team);
            em.flush();
        } else {
            team = TestUtil.findAll(em, Team.class).get(0);
        }
        lLINSVillageReport.setExecutingTeam(team);
        return lLINSVillageReport;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LlinsVillageReport createUpdatedEntity(EntityManager em) {
        LlinsVillageReport lLINSVillageReport = new LlinsVillageReport()
            .uid(UPDATED_UID)
            .created(UPDATED_CREATED)
            .lastUpdated(UPDATED_LAST_UPDATED)
            .houses(UPDATED_HOUSES)
            .residentHousehold(UPDATED_RESIDENT_HOUSEHOLD)
            .idpsHousehold(UPDATED_IDPS_HOUSEHOLD)
            .maleIndividuals(UPDATED_MALE_INDIVIDUALS)
            .femaleIndividuals(UPDATED_FEMALE_INDIVIDUALS)
            .lessThan5Males(UPDATED_LESS_THAN_5_MALES)
            .lessThan5Females(UPDATED_LESS_THAN_5_FEMALES)
            .pregnantWomen(UPDATED_PREGNANT_WOMEN)
            .quantityReceived(UPDATED_QUANTITY_RECEIVED)
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
        lLINSVillageReport.setDayReached(workingDay);
        // Add required entity
        Team team;
        if (TestUtil.findAll(em, Team.class).isEmpty()) {
            team = TeamResourceIT.createUpdatedEntity(em);
            em.persist(team);
            em.flush();
        } else {
            team = TestUtil.findAll(em, Team.class).get(0);
        }
        lLINSVillageReport.setExecutingTeam(team);
        return lLINSVillageReport;
    }

    @BeforeEach
    public void initTest() {
        lLINSVillageReport = createEntity(em);
    }

    @Test
    @Transactional
    void createLLINSVillageReport() throws Exception {
        int databaseSizeBeforeCreate = lLINSVillageReportRepository.findAll().size();
        // Create the LlinsVillageReport
        restLLINSVillageReportMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(lLINSVillageReport))
            )
            .andExpect(status().isCreated());

        // Validate the LlinsVillageReport in the database
        List<LlinsVillageReport> lLINSVillageReportList = lLINSVillageReportRepository.findAll();
        assertThat(lLINSVillageReportList).hasSize(databaseSizeBeforeCreate + 1);
        LlinsVillageReport testLlinsVillageReport = lLINSVillageReportList.get(lLINSVillageReportList.size() - 1);
        assertThat(testLlinsVillageReport.getUid()).isEqualTo(DEFAULT_UID);
        assertThat(testLlinsVillageReport.getCreated()).isEqualTo(DEFAULT_CREATED);
        assertThat(testLlinsVillageReport.getLastUpdated()).isEqualTo(DEFAULT_LAST_UPDATED);
        assertThat(testLlinsVillageReport.getHouses()).isEqualTo(DEFAULT_HOUSES);
        assertThat(testLlinsVillageReport.getResidentHousehold()).isEqualTo(DEFAULT_RESIDENT_HOUSEHOLD);
        assertThat(testLlinsVillageReport.getIdpsHousehold()).isEqualTo(DEFAULT_IDPS_HOUSEHOLD);
        assertThat(testLlinsVillageReport.getMaleIndividuals()).isEqualTo(DEFAULT_MALE_INDIVIDUALS);
        assertThat(testLlinsVillageReport.getFemaleIndividuals()).isEqualTo(DEFAULT_FEMALE_INDIVIDUALS);
        assertThat(testLlinsVillageReport.getLessThan5Males()).isEqualTo(DEFAULT_LESS_THAN_5_MALES);
        assertThat(testLlinsVillageReport.getLessThan5Females()).isEqualTo(DEFAULT_LESS_THAN_5_FEMALES);
        assertThat(testLlinsVillageReport.getPregnantWomen()).isEqualTo(DEFAULT_PREGNANT_WOMEN);
        assertThat(testLlinsVillageReport.getQuantityReceived()).isEqualTo(DEFAULT_QUANTITY_RECEIVED);
        assertThat(testLlinsVillageReport.getComment()).isEqualTo(DEFAULT_COMMENT);
    }

    @Test
    @Transactional
    void createLLINSVillageReportWithExistingId() throws Exception {
        // Create the LlinsVillageReport with an existing ID
        lLINSVillageReport.setId(1L);

        int databaseSizeBeforeCreate = lLINSVillageReportRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLLINSVillageReportMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(lLINSVillageReport))
            )
            .andExpect(status().isBadRequest());

        // Validate the LlinsVillageReport in the database
        List<LlinsVillageReport> lLINSVillageReportList = lLINSVillageReportRepository.findAll();
        assertThat(lLINSVillageReportList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkUidIsRequired() throws Exception {
        int databaseSizeBeforeTest = lLINSVillageReportRepository.findAll().size();
        // set the field null
        lLINSVillageReport.setUid(null);

        // Create the LlinsVillageReport, which fails.

        restLLINSVillageReportMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(lLINSVillageReport))
            )
            .andExpect(status().isBadRequest());

        List<LlinsVillageReport> lLINSVillageReportList = lLINSVillageReportRepository.findAll();
        assertThat(lLINSVillageReportList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkHousesIsRequired() throws Exception {
        int databaseSizeBeforeTest = lLINSVillageReportRepository.findAll().size();
        // set the field null
        lLINSVillageReport.setHouses(null);

        // Create the LlinsVillageReport, which fails.

        restLLINSVillageReportMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(lLINSVillageReport))
            )
            .andExpect(status().isBadRequest());

        List<LlinsVillageReport> lLINSVillageReportList = lLINSVillageReportRepository.findAll();
        assertThat(lLINSVillageReportList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkResidentHouseholdIsRequired() throws Exception {
        int databaseSizeBeforeTest = lLINSVillageReportRepository.findAll().size();
        // set the field null
        lLINSVillageReport.setResidentHousehold(null);

        // Create the LlinsVillageReport, which fails.

        restLLINSVillageReportMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(lLINSVillageReport))
            )
            .andExpect(status().isBadRequest());

        List<LlinsVillageReport> lLINSVillageReportList = lLINSVillageReportRepository.findAll();
        assertThat(lLINSVillageReportList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIdpsHouseholdIsRequired() throws Exception {
        int databaseSizeBeforeTest = lLINSVillageReportRepository.findAll().size();
        // set the field null
        lLINSVillageReport.setIdpsHousehold(null);

        // Create the LlinsVillageReport, which fails.

        restLLINSVillageReportMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(lLINSVillageReport))
            )
            .andExpect(status().isBadRequest());

        List<LlinsVillageReport> lLINSVillageReportList = lLINSVillageReportRepository.findAll();
        assertThat(lLINSVillageReportList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkQuantityReceivedIsRequired() throws Exception {
        int databaseSizeBeforeTest = lLINSVillageReportRepository.findAll().size();
        // set the field null
        lLINSVillageReport.setQuantityReceived(null);

        // Create the LlinsVillageReport, which fails.

        restLLINSVillageReportMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(lLINSVillageReport))
            )
            .andExpect(status().isBadRequest());

        List<LlinsVillageReport> lLINSVillageReportList = lLINSVillageReportRepository.findAll();
        assertThat(lLINSVillageReportList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllLLINSVillageReports() throws Exception {
        // Initialize the database
        lLINSVillageReportRepository.saveAndFlush(lLINSVillageReport);

        // Get all the lLINSVillageReportList
        restLLINSVillageReportMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(lLINSVillageReport.getId().intValue())))
            .andExpect(jsonPath("$.[*].uid").value(hasItem(DEFAULT_UID)))
            .andExpect(jsonPath("$.[*].created").value(hasItem(DEFAULT_CREATED.toString())))
            .andExpect(jsonPath("$.[*].lastUpdated").value(hasItem(DEFAULT_LAST_UPDATED.toString())))
            .andExpect(jsonPath("$.[*].houses").value(hasItem(DEFAULT_HOUSES)))
            .andExpect(jsonPath("$.[*].residentHousehold").value(hasItem(DEFAULT_RESIDENT_HOUSEHOLD)))
            .andExpect(jsonPath("$.[*].idpsHousehold").value(hasItem(DEFAULT_IDPS_HOUSEHOLD)))
            .andExpect(jsonPath("$.[*].maleIndividuals").value(hasItem(DEFAULT_MALE_INDIVIDUALS)))
            .andExpect(jsonPath("$.[*].femaleIndividuals").value(hasItem(DEFAULT_FEMALE_INDIVIDUALS)))
            .andExpect(jsonPath("$.[*].lessThan5Males").value(hasItem(DEFAULT_LESS_THAN_5_MALES)))
            .andExpect(jsonPath("$.[*].lessThan5Females").value(hasItem(DEFAULT_LESS_THAN_5_FEMALES)))
            .andExpect(jsonPath("$.[*].pregnantWomen").value(hasItem(DEFAULT_PREGNANT_WOMEN)))
            .andExpect(jsonPath("$.[*].quantityReceived").value(hasItem(DEFAULT_QUANTITY_RECEIVED)))
            .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT)));
    }

    @Test
    @Transactional
    void getLLINSVillageReport() throws Exception {
        // Initialize the database
        lLINSVillageReportRepository.saveAndFlush(lLINSVillageReport);

        // Get the lLINSVillageReport
        restLLINSVillageReportMockMvc
            .perform(get(ENTITY_API_URL_ID, lLINSVillageReport.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(lLINSVillageReport.getId().intValue()))
            .andExpect(jsonPath("$.uid").value(DEFAULT_UID))
            .andExpect(jsonPath("$.created").value(DEFAULT_CREATED.toString()))
            .andExpect(jsonPath("$.lastUpdated").value(DEFAULT_LAST_UPDATED.toString()))
            .andExpect(jsonPath("$.houses").value(DEFAULT_HOUSES))
            .andExpect(jsonPath("$.residentHousehold").value(DEFAULT_RESIDENT_HOUSEHOLD))
            .andExpect(jsonPath("$.idpsHousehold").value(DEFAULT_IDPS_HOUSEHOLD))
            .andExpect(jsonPath("$.maleIndividuals").value(DEFAULT_MALE_INDIVIDUALS))
            .andExpect(jsonPath("$.femaleIndividuals").value(DEFAULT_FEMALE_INDIVIDUALS))
            .andExpect(jsonPath("$.lessThan5Males").value(DEFAULT_LESS_THAN_5_MALES))
            .andExpect(jsonPath("$.lessThan5Females").value(DEFAULT_LESS_THAN_5_FEMALES))
            .andExpect(jsonPath("$.pregnantWomen").value(DEFAULT_PREGNANT_WOMEN))
            .andExpect(jsonPath("$.quantityReceived").value(DEFAULT_QUANTITY_RECEIVED))
            .andExpect(jsonPath("$.comment").value(DEFAULT_COMMENT));
    }

    @Test
    @Transactional
    void getNonExistingLLINSVillageReport() throws Exception {
        // Get the lLINSVillageReport
        restLLINSVillageReportMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewLLINSVillageReport() throws Exception {
        // Initialize the database
        lLINSVillageReportRepository.saveAndFlush(lLINSVillageReport);

        int databaseSizeBeforeUpdate = lLINSVillageReportRepository.findAll().size();

        // Update the lLINSVillageReport
        LlinsVillageReport updatedLlinsVillageReport = lLINSVillageReportRepository.findById(lLINSVillageReport.getId()).get();
        // Disconnect from session so that the updates on updatedLlinsVillageReport are not directly saved in db
        em.detach(updatedLlinsVillageReport);
        updatedLlinsVillageReport
            .uid(UPDATED_UID)
            .created(UPDATED_CREATED)
            .lastUpdated(UPDATED_LAST_UPDATED)
            .houses(UPDATED_HOUSES)
            .residentHousehold(UPDATED_RESIDENT_HOUSEHOLD)
            .idpsHousehold(UPDATED_IDPS_HOUSEHOLD)
            .maleIndividuals(UPDATED_MALE_INDIVIDUALS)
            .femaleIndividuals(UPDATED_FEMALE_INDIVIDUALS)
            .lessThan5Males(UPDATED_LESS_THAN_5_MALES)
            .lessThan5Females(UPDATED_LESS_THAN_5_FEMALES)
            .pregnantWomen(UPDATED_PREGNANT_WOMEN)
            .quantityReceived(UPDATED_QUANTITY_RECEIVED)
            .comment(UPDATED_COMMENT);

        restLLINSVillageReportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedLlinsVillageReport.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedLlinsVillageReport))
            )
            .andExpect(status().isOk());

        // Validate the LlinsVillageReport in the database
        List<LlinsVillageReport> lLINSVillageReportList = lLINSVillageReportRepository.findAll();
        assertThat(lLINSVillageReportList).hasSize(databaseSizeBeforeUpdate);
        LlinsVillageReport testLlinsVillageReport = lLINSVillageReportList.get(lLINSVillageReportList.size() - 1);
        assertThat(testLlinsVillageReport.getUid()).isEqualTo(UPDATED_UID);
        assertThat(testLlinsVillageReport.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testLlinsVillageReport.getLastUpdated()).isEqualTo(UPDATED_LAST_UPDATED);
        assertThat(testLlinsVillageReport.getHouses()).isEqualTo(UPDATED_HOUSES);
        assertThat(testLlinsVillageReport.getResidentHousehold()).isEqualTo(UPDATED_RESIDENT_HOUSEHOLD);
        assertThat(testLlinsVillageReport.getIdpsHousehold()).isEqualTo(UPDATED_IDPS_HOUSEHOLD);
        assertThat(testLlinsVillageReport.getMaleIndividuals()).isEqualTo(UPDATED_MALE_INDIVIDUALS);
        assertThat(testLlinsVillageReport.getFemaleIndividuals()).isEqualTo(UPDATED_FEMALE_INDIVIDUALS);
        assertThat(testLlinsVillageReport.getLessThan5Males()).isEqualTo(UPDATED_LESS_THAN_5_MALES);
        assertThat(testLlinsVillageReport.getLessThan5Females()).isEqualTo(UPDATED_LESS_THAN_5_FEMALES);
        assertThat(testLlinsVillageReport.getPregnantWomen()).isEqualTo(UPDATED_PREGNANT_WOMEN);
        assertThat(testLlinsVillageReport.getQuantityReceived()).isEqualTo(UPDATED_QUANTITY_RECEIVED);
        assertThat(testLlinsVillageReport.getComment()).isEqualTo(UPDATED_COMMENT);
    }

    @Test
    @Transactional
    void putNonExistingLLINSVillageReport() throws Exception {
        int databaseSizeBeforeUpdate = lLINSVillageReportRepository.findAll().size();
        lLINSVillageReport.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLLINSVillageReportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, lLINSVillageReport.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(lLINSVillageReport))
            )
            .andExpect(status().isBadRequest());

        // Validate the LlinsVillageReport in the database
        List<LlinsVillageReport> lLINSVillageReportList = lLINSVillageReportRepository.findAll();
        assertThat(lLINSVillageReportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLLINSVillageReport() throws Exception {
        int databaseSizeBeforeUpdate = lLINSVillageReportRepository.findAll().size();
        lLINSVillageReport.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLLINSVillageReportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(lLINSVillageReport))
            )
            .andExpect(status().isBadRequest());

        // Validate the LlinsVillageReport in the database
        List<LlinsVillageReport> lLINSVillageReportList = lLINSVillageReportRepository.findAll();
        assertThat(lLINSVillageReportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLLINSVillageReport() throws Exception {
        int databaseSizeBeforeUpdate = lLINSVillageReportRepository.findAll().size();
        lLINSVillageReport.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLLINSVillageReportMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(lLINSVillageReport))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LlinsVillageReport in the database
        List<LlinsVillageReport> lLINSVillageReportList = lLINSVillageReportRepository.findAll();
        assertThat(lLINSVillageReportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLLINSVillageReportWithPatch() throws Exception {
        // Initialize the database
        lLINSVillageReportRepository.saveAndFlush(lLINSVillageReport);

        int databaseSizeBeforeUpdate = lLINSVillageReportRepository.findAll().size();

        // Update the lLINSVillageReport using partial update
        LlinsVillageReport partialUpdatedLlinsVillageReport = new LlinsVillageReport();
        partialUpdatedLlinsVillageReport.setId(lLINSVillageReport.getId());

        partialUpdatedLlinsVillageReport
            .uid(UPDATED_UID)
            .created(UPDATED_CREATED)
            .houses(UPDATED_HOUSES)
            .femaleIndividuals(UPDATED_FEMALE_INDIVIDUALS)
            .lessThan5Males(UPDATED_LESS_THAN_5_MALES)
            .lessThan5Females(UPDATED_LESS_THAN_5_FEMALES)
            .quantityReceived(UPDATED_QUANTITY_RECEIVED);

        restLLINSVillageReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLlinsVillageReport.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLlinsVillageReport))
            )
            .andExpect(status().isOk());

        // Validate the LlinsVillageReport in the database
        List<LlinsVillageReport> lLINSVillageReportList = lLINSVillageReportRepository.findAll();
        assertThat(lLINSVillageReportList).hasSize(databaseSizeBeforeUpdate);
        LlinsVillageReport testLlinsVillageReport = lLINSVillageReportList.get(lLINSVillageReportList.size() - 1);
        assertThat(testLlinsVillageReport.getUid()).isEqualTo(UPDATED_UID);
        assertThat(testLlinsVillageReport.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testLlinsVillageReport.getLastUpdated()).isEqualTo(DEFAULT_LAST_UPDATED);
        assertThat(testLlinsVillageReport.getHouses()).isEqualTo(UPDATED_HOUSES);
        assertThat(testLlinsVillageReport.getResidentHousehold()).isEqualTo(DEFAULT_RESIDENT_HOUSEHOLD);
        assertThat(testLlinsVillageReport.getIdpsHousehold()).isEqualTo(DEFAULT_IDPS_HOUSEHOLD);
        assertThat(testLlinsVillageReport.getMaleIndividuals()).isEqualTo(DEFAULT_MALE_INDIVIDUALS);
        assertThat(testLlinsVillageReport.getFemaleIndividuals()).isEqualTo(UPDATED_FEMALE_INDIVIDUALS);
        assertThat(testLlinsVillageReport.getLessThan5Males()).isEqualTo(UPDATED_LESS_THAN_5_MALES);
        assertThat(testLlinsVillageReport.getLessThan5Females()).isEqualTo(UPDATED_LESS_THAN_5_FEMALES);
        assertThat(testLlinsVillageReport.getPregnantWomen()).isEqualTo(DEFAULT_PREGNANT_WOMEN);
        assertThat(testLlinsVillageReport.getQuantityReceived()).isEqualTo(UPDATED_QUANTITY_RECEIVED);
        assertThat(testLlinsVillageReport.getComment()).isEqualTo(DEFAULT_COMMENT);
    }

    @Test
    @Transactional
    void fullUpdateLLINSVillageReportWithPatch() throws Exception {
        // Initialize the database
        lLINSVillageReportRepository.saveAndFlush(lLINSVillageReport);

        int databaseSizeBeforeUpdate = lLINSVillageReportRepository.findAll().size();

        // Update the lLINSVillageReport using partial update
        LlinsVillageReport partialUpdatedLlinsVillageReport = new LlinsVillageReport();
        partialUpdatedLlinsVillageReport.setId(lLINSVillageReport.getId());

        partialUpdatedLlinsVillageReport
            .uid(UPDATED_UID)
            .created(UPDATED_CREATED)
            .lastUpdated(UPDATED_LAST_UPDATED)
            .houses(UPDATED_HOUSES)
            .residentHousehold(UPDATED_RESIDENT_HOUSEHOLD)
            .idpsHousehold(UPDATED_IDPS_HOUSEHOLD)
            .maleIndividuals(UPDATED_MALE_INDIVIDUALS)
            .femaleIndividuals(UPDATED_FEMALE_INDIVIDUALS)
            .lessThan5Males(UPDATED_LESS_THAN_5_MALES)
            .lessThan5Females(UPDATED_LESS_THAN_5_FEMALES)
            .pregnantWomen(UPDATED_PREGNANT_WOMEN)
            .quantityReceived(UPDATED_QUANTITY_RECEIVED)
            .comment(UPDATED_COMMENT);

        restLLINSVillageReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLlinsVillageReport.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLlinsVillageReport))
            )
            .andExpect(status().isOk());

        // Validate the LlinsVillageReport in the database
        List<LlinsVillageReport> lLINSVillageReportList = lLINSVillageReportRepository.findAll();
        assertThat(lLINSVillageReportList).hasSize(databaseSizeBeforeUpdate);
        LlinsVillageReport testLlinsVillageReport = lLINSVillageReportList.get(lLINSVillageReportList.size() - 1);
        assertThat(testLlinsVillageReport.getUid()).isEqualTo(UPDATED_UID);
        assertThat(testLlinsVillageReport.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testLlinsVillageReport.getLastUpdated()).isEqualTo(UPDATED_LAST_UPDATED);
        assertThat(testLlinsVillageReport.getHouses()).isEqualTo(UPDATED_HOUSES);
        assertThat(testLlinsVillageReport.getResidentHousehold()).isEqualTo(UPDATED_RESIDENT_HOUSEHOLD);
        assertThat(testLlinsVillageReport.getIdpsHousehold()).isEqualTo(UPDATED_IDPS_HOUSEHOLD);
        assertThat(testLlinsVillageReport.getMaleIndividuals()).isEqualTo(UPDATED_MALE_INDIVIDUALS);
        assertThat(testLlinsVillageReport.getFemaleIndividuals()).isEqualTo(UPDATED_FEMALE_INDIVIDUALS);
        assertThat(testLlinsVillageReport.getLessThan5Males()).isEqualTo(UPDATED_LESS_THAN_5_MALES);
        assertThat(testLlinsVillageReport.getLessThan5Females()).isEqualTo(UPDATED_LESS_THAN_5_FEMALES);
        assertThat(testLlinsVillageReport.getPregnantWomen()).isEqualTo(UPDATED_PREGNANT_WOMEN);
        assertThat(testLlinsVillageReport.getQuantityReceived()).isEqualTo(UPDATED_QUANTITY_RECEIVED);
        assertThat(testLlinsVillageReport.getComment()).isEqualTo(UPDATED_COMMENT);
    }

    @Test
    @Transactional
    void patchNonExistingLLINSVillageReport() throws Exception {
        int databaseSizeBeforeUpdate = lLINSVillageReportRepository.findAll().size();
        lLINSVillageReport.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLLINSVillageReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, lLINSVillageReport.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(lLINSVillageReport))
            )
            .andExpect(status().isBadRequest());

        // Validate the LlinsVillageReport in the database
        List<LlinsVillageReport> lLINSVillageReportList = lLINSVillageReportRepository.findAll();
        assertThat(lLINSVillageReportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLLINSVillageReport() throws Exception {
        int databaseSizeBeforeUpdate = lLINSVillageReportRepository.findAll().size();
        lLINSVillageReport.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLLINSVillageReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(lLINSVillageReport))
            )
            .andExpect(status().isBadRequest());

        // Validate the LlinsVillageReport in the database
        List<LlinsVillageReport> lLINSVillageReportList = lLINSVillageReportRepository.findAll();
        assertThat(lLINSVillageReportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLLINSVillageReport() throws Exception {
        int databaseSizeBeforeUpdate = lLINSVillageReportRepository.findAll().size();
        lLINSVillageReport.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLLINSVillageReportMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(lLINSVillageReport))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LlinsVillageReport in the database
        List<LlinsVillageReport> lLINSVillageReportList = lLINSVillageReportRepository.findAll();
        assertThat(lLINSVillageReportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLLINSVillageReport() throws Exception {
        // Initialize the database
        lLINSVillageReportRepository.saveAndFlush(lLINSVillageReport);

        int databaseSizeBeforeDelete = lLINSVillageReportRepository.findAll().size();

        // Delete the lLINSVillageReport
        restLLINSVillageReportMockMvc
            .perform(delete(ENTITY_API_URL_ID, lLINSVillageReport.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<LlinsVillageReport> lLINSVillageReportList = lLINSVillageReportRepository.findAll();
        assertThat(lLINSVillageReportList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
