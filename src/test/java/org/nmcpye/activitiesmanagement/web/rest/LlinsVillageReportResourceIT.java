package org.nmcpye.activitiesmanagement.web.rest;

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
    private LlinsVillageReportRepository llinsVillageReportRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLlinsVillageReportMockMvc;

    private LlinsVillageReport llinsVillageReport;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LlinsVillageReport createEntity(EntityManager em) {
        LlinsVillageReport llinsVillageReport = new LlinsVillageReport()
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
        llinsVillageReport.setDayReached(workingDay);
        // Add required entity
        Team team;
        if (TestUtil.findAll(em, Team.class).isEmpty()) {
            team = TeamResourceIT.createEntity(em);
            em.persist(team);
            em.flush();
        } else {
            team = TestUtil.findAll(em, Team.class).get(0);
        }
        llinsVillageReport.setExecutingTeam(team);
        return llinsVillageReport;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LlinsVillageReport createUpdatedEntity(EntityManager em) {
        LlinsVillageReport llinsVillageReport = new LlinsVillageReport()
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
        llinsVillageReport.setDayReached(workingDay);
        // Add required entity
        Team team;
        if (TestUtil.findAll(em, Team.class).isEmpty()) {
            team = TeamResourceIT.createUpdatedEntity(em);
            em.persist(team);
            em.flush();
        } else {
            team = TestUtil.findAll(em, Team.class).get(0);
        }
        llinsVillageReport.setExecutingTeam(team);
        return llinsVillageReport;
    }

    @BeforeEach
    public void initTest() {
        llinsVillageReport = createEntity(em);
    }

    @Test
    @Transactional
    void createLlinsVillageReport() throws Exception {
        int databaseSizeBeforeCreate = llinsVillageReportRepository.findAll().size();
        // Create the LlinsVillageReport
        restLlinsVillageReportMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(llinsVillageReport))
            )
            .andExpect(status().isCreated());

        // Validate the LlinsVillageReport in the database
        List<LlinsVillageReport> llinsVillageReportList = llinsVillageReportRepository.findAll();
        assertThat(llinsVillageReportList).hasSize(databaseSizeBeforeCreate + 1);
        LlinsVillageReport testLlinsVillageReport = llinsVillageReportList.get(llinsVillageReportList.size() - 1);
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
    void createLlinsVillageReportWithExistingId() throws Exception {
        // Create the LlinsVillageReport with an existing ID
        llinsVillageReport.setId(1L);

        int databaseSizeBeforeCreate = llinsVillageReportRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLlinsVillageReportMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(llinsVillageReport))
            )
            .andExpect(status().isBadRequest());

        // Validate the LlinsVillageReport in the database
        List<LlinsVillageReport> llinsVillageReportList = llinsVillageReportRepository.findAll();
        assertThat(llinsVillageReportList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkUidIsRequired() throws Exception {
        int databaseSizeBeforeTest = llinsVillageReportRepository.findAll().size();
        // set the field null
        llinsVillageReport.setUid(null);

        // Create the LlinsVillageReport, which fails.

        restLlinsVillageReportMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(llinsVillageReport))
            )
            .andExpect(status().isBadRequest());

        List<LlinsVillageReport> llinsVillageReportList = llinsVillageReportRepository.findAll();
        assertThat(llinsVillageReportList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkHousesIsRequired() throws Exception {
        int databaseSizeBeforeTest = llinsVillageReportRepository.findAll().size();
        // set the field null
        llinsVillageReport.setHouses(null);

        // Create the LlinsVillageReport, which fails.

        restLlinsVillageReportMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(llinsVillageReport))
            )
            .andExpect(status().isBadRequest());

        List<LlinsVillageReport> llinsVillageReportList = llinsVillageReportRepository.findAll();
        assertThat(llinsVillageReportList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkResidentHouseholdIsRequired() throws Exception {
        int databaseSizeBeforeTest = llinsVillageReportRepository.findAll().size();
        // set the field null
        llinsVillageReport.setResidentHousehold(null);

        // Create the LlinsVillageReport, which fails.

        restLlinsVillageReportMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(llinsVillageReport))
            )
            .andExpect(status().isBadRequest());

        List<LlinsVillageReport> llinsVillageReportList = llinsVillageReportRepository.findAll();
        assertThat(llinsVillageReportList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIdpsHouseholdIsRequired() throws Exception {
        int databaseSizeBeforeTest = llinsVillageReportRepository.findAll().size();
        // set the field null
        llinsVillageReport.setIdpsHousehold(null);

        // Create the LlinsVillageReport, which fails.

        restLlinsVillageReportMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(llinsVillageReport))
            )
            .andExpect(status().isBadRequest());

        List<LlinsVillageReport> llinsVillageReportList = llinsVillageReportRepository.findAll();
        assertThat(llinsVillageReportList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkQuantityReceivedIsRequired() throws Exception {
        int databaseSizeBeforeTest = llinsVillageReportRepository.findAll().size();
        // set the field null
        llinsVillageReport.setQuantityReceived(null);

        // Create the LlinsVillageReport, which fails.

        restLlinsVillageReportMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(llinsVillageReport))
            )
            .andExpect(status().isBadRequest());

        List<LlinsVillageReport> llinsVillageReportList = llinsVillageReportRepository.findAll();
        assertThat(llinsVillageReportList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllLlinsVillageReports() throws Exception {
        // Initialize the database
        llinsVillageReportRepository.saveAndFlush(llinsVillageReport);

        // Get all the llinsVillageReportList
        restLlinsVillageReportMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(llinsVillageReport.getId().intValue())))
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
    void getLlinsVillageReport() throws Exception {
        // Initialize the database
        llinsVillageReportRepository.saveAndFlush(llinsVillageReport);

        // Get the llinsVillageReport
        restLlinsVillageReportMockMvc
            .perform(get(ENTITY_API_URL_ID, llinsVillageReport.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(llinsVillageReport.getId().intValue()))
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
    void getNonExistingLlinsVillageReport() throws Exception {
        // Get the llinsVillageReport
        restLlinsVillageReportMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewLlinsVillageReport() throws Exception {
        // Initialize the database
        llinsVillageReportRepository.saveAndFlush(llinsVillageReport);

        int databaseSizeBeforeUpdate = llinsVillageReportRepository.findAll().size();

        // Update the llinsVillageReport
        LlinsVillageReport updatedLlinsVillageReport = llinsVillageReportRepository.findById(llinsVillageReport.getId()).get();
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

        restLlinsVillageReportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedLlinsVillageReport.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedLlinsVillageReport))
            )
            .andExpect(status().isOk());

        // Validate the LlinsVillageReport in the database
        List<LlinsVillageReport> llinsVillageReportList = llinsVillageReportRepository.findAll();
        assertThat(llinsVillageReportList).hasSize(databaseSizeBeforeUpdate);
        LlinsVillageReport testLlinsVillageReport = llinsVillageReportList.get(llinsVillageReportList.size() - 1);
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
    void putNonExistingLlinsVillageReport() throws Exception {
        int databaseSizeBeforeUpdate = llinsVillageReportRepository.findAll().size();
        llinsVillageReport.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLlinsVillageReportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, llinsVillageReport.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(llinsVillageReport))
            )
            .andExpect(status().isBadRequest());

        // Validate the LlinsVillageReport in the database
        List<LlinsVillageReport> llinsVillageReportList = llinsVillageReportRepository.findAll();
        assertThat(llinsVillageReportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLlinsVillageReport() throws Exception {
        int databaseSizeBeforeUpdate = llinsVillageReportRepository.findAll().size();
        llinsVillageReport.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLlinsVillageReportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(llinsVillageReport))
            )
            .andExpect(status().isBadRequest());

        // Validate the LlinsVillageReport in the database
        List<LlinsVillageReport> llinsVillageReportList = llinsVillageReportRepository.findAll();
        assertThat(llinsVillageReportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLlinsVillageReport() throws Exception {
        int databaseSizeBeforeUpdate = llinsVillageReportRepository.findAll().size();
        llinsVillageReport.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLlinsVillageReportMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(llinsVillageReport))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LlinsVillageReport in the database
        List<LlinsVillageReport> llinsVillageReportList = llinsVillageReportRepository.findAll();
        assertThat(llinsVillageReportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLlinsVillageReportWithPatch() throws Exception {
        // Initialize the database
        llinsVillageReportRepository.saveAndFlush(llinsVillageReport);

        int databaseSizeBeforeUpdate = llinsVillageReportRepository.findAll().size();

        // Update the llinsVillageReport using partial update
        LlinsVillageReport partialUpdatedLlinsVillageReport = new LlinsVillageReport();
        partialUpdatedLlinsVillageReport.setId(llinsVillageReport.getId());

        partialUpdatedLlinsVillageReport
            .uid(UPDATED_UID)
            .created(UPDATED_CREATED)
            .houses(UPDATED_HOUSES)
            .residentHousehold(UPDATED_RESIDENT_HOUSEHOLD)
            .idpsHousehold(UPDATED_IDPS_HOUSEHOLD)
            .femaleIndividuals(UPDATED_FEMALE_INDIVIDUALS)
            .lessThan5Males(UPDATED_LESS_THAN_5_MALES)
            .pregnantWomen(UPDATED_PREGNANT_WOMEN);

        restLlinsVillageReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLlinsVillageReport.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLlinsVillageReport))
            )
            .andExpect(status().isOk());

        // Validate the LlinsVillageReport in the database
        List<LlinsVillageReport> llinsVillageReportList = llinsVillageReportRepository.findAll();
        assertThat(llinsVillageReportList).hasSize(databaseSizeBeforeUpdate);
        LlinsVillageReport testLlinsVillageReport = llinsVillageReportList.get(llinsVillageReportList.size() - 1);
        assertThat(testLlinsVillageReport.getUid()).isEqualTo(UPDATED_UID);
        assertThat(testLlinsVillageReport.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testLlinsVillageReport.getLastUpdated()).isEqualTo(DEFAULT_LAST_UPDATED);
        assertThat(testLlinsVillageReport.getHouses()).isEqualTo(UPDATED_HOUSES);
        assertThat(testLlinsVillageReport.getResidentHousehold()).isEqualTo(UPDATED_RESIDENT_HOUSEHOLD);
        assertThat(testLlinsVillageReport.getIdpsHousehold()).isEqualTo(UPDATED_IDPS_HOUSEHOLD);
        assertThat(testLlinsVillageReport.getMaleIndividuals()).isEqualTo(DEFAULT_MALE_INDIVIDUALS);
        assertThat(testLlinsVillageReport.getFemaleIndividuals()).isEqualTo(UPDATED_FEMALE_INDIVIDUALS);
        assertThat(testLlinsVillageReport.getLessThan5Males()).isEqualTo(UPDATED_LESS_THAN_5_MALES);
        assertThat(testLlinsVillageReport.getLessThan5Females()).isEqualTo(DEFAULT_LESS_THAN_5_FEMALES);
        assertThat(testLlinsVillageReport.getPregnantWomen()).isEqualTo(UPDATED_PREGNANT_WOMEN);
        assertThat(testLlinsVillageReport.getQuantityReceived()).isEqualTo(DEFAULT_QUANTITY_RECEIVED);
        assertThat(testLlinsVillageReport.getComment()).isEqualTo(DEFAULT_COMMENT);
    }

    @Test
    @Transactional
    void fullUpdateLlinsVillageReportWithPatch() throws Exception {
        // Initialize the database
        llinsVillageReportRepository.saveAndFlush(llinsVillageReport);

        int databaseSizeBeforeUpdate = llinsVillageReportRepository.findAll().size();

        // Update the llinsVillageReport using partial update
        LlinsVillageReport partialUpdatedLlinsVillageReport = new LlinsVillageReport();
        partialUpdatedLlinsVillageReport.setId(llinsVillageReport.getId());

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

        restLlinsVillageReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLlinsVillageReport.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLlinsVillageReport))
            )
            .andExpect(status().isOk());

        // Validate the LlinsVillageReport in the database
        List<LlinsVillageReport> llinsVillageReportList = llinsVillageReportRepository.findAll();
        assertThat(llinsVillageReportList).hasSize(databaseSizeBeforeUpdate);
        LlinsVillageReport testLlinsVillageReport = llinsVillageReportList.get(llinsVillageReportList.size() - 1);
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
    void patchNonExistingLlinsVillageReport() throws Exception {
        int databaseSizeBeforeUpdate = llinsVillageReportRepository.findAll().size();
        llinsVillageReport.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLlinsVillageReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, llinsVillageReport.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(llinsVillageReport))
            )
            .andExpect(status().isBadRequest());

        // Validate the LlinsVillageReport in the database
        List<LlinsVillageReport> llinsVillageReportList = llinsVillageReportRepository.findAll();
        assertThat(llinsVillageReportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLlinsVillageReport() throws Exception {
        int databaseSizeBeforeUpdate = llinsVillageReportRepository.findAll().size();
        llinsVillageReport.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLlinsVillageReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(llinsVillageReport))
            )
            .andExpect(status().isBadRequest());

        // Validate the LlinsVillageReport in the database
        List<LlinsVillageReport> llinsVillageReportList = llinsVillageReportRepository.findAll();
        assertThat(llinsVillageReportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLlinsVillageReport() throws Exception {
        int databaseSizeBeforeUpdate = llinsVillageReportRepository.findAll().size();
        llinsVillageReport.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLlinsVillageReportMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(llinsVillageReport))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LlinsVillageReport in the database
        List<LlinsVillageReport> llinsVillageReportList = llinsVillageReportRepository.findAll();
        assertThat(llinsVillageReportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLlinsVillageReport() throws Exception {
        // Initialize the database
        llinsVillageReportRepository.saveAndFlush(llinsVillageReport);

        int databaseSizeBeforeDelete = llinsVillageReportRepository.findAll().size();

        // Delete the llinsVillageReport
        restLlinsVillageReportMockMvc
            .perform(delete(ENTITY_API_URL_ID, llinsVillageReport.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<LlinsVillageReport> llinsVillageReportList = llinsVillageReportRepository.findAll();
        assertThat(llinsVillageReportList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
