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
import org.nmcpye.activitiesmanagement.domain.LlinsVillageReportHistory;
import org.nmcpye.activitiesmanagement.domain.WorkingDay;
import org.nmcpye.activitiesmanagement.repository.LlinsVillageReportHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link LlinsVillageReportHistoryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class LlinsVillageReportHistoryResourceIT {

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

    private static final String ENTITY_API_URL = "/api/llins-village-report-histories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private LlinsVillageReportHistoryRepository lLINSVillageReportHistoryRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLLINSVillageReportHistoryMockMvc;

    private LlinsVillageReportHistory lLINSVillageReportHistory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LlinsVillageReportHistory createEntity(EntityManager em) {
        LlinsVillageReportHistory lLINSVillageReportHistory = new LlinsVillageReportHistory()
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
            .quantityReceived(DEFAULT_QUANTITY_RECEIVED);
        // Add required entity
        WorkingDay workingDay;
        if (TestUtil.findAll(em, WorkingDay.class).isEmpty()) {
            workingDay = WorkingDayResourceIT.createEntity(em);
            em.persist(workingDay);
            em.flush();
        } else {
            workingDay = TestUtil.findAll(em, WorkingDay.class).get(0);
        }
        lLINSVillageReportHistory.setDayReached(workingDay);
        // Add required entity
        LlinsVillageReport lLINSVillageReport;
        if (TestUtil.findAll(em, LlinsVillageReport.class).isEmpty()) {
            lLINSVillageReport = LlinsVillageReportResourceIT.createEntity(em);
            em.persist(lLINSVillageReport);
            em.flush();
        } else {
            lLINSVillageReport = TestUtil.findAll(em, LlinsVillageReport.class).get(0);
        }
        lLINSVillageReportHistory.setLlinsVillageReport(lLINSVillageReport);
        return lLINSVillageReportHistory;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LlinsVillageReportHistory createUpdatedEntity(EntityManager em) {
        LlinsVillageReportHistory lLINSVillageReportHistory = new LlinsVillageReportHistory()
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
            .quantityReceived(UPDATED_QUANTITY_RECEIVED);
        // Add required entity
        WorkingDay workingDay;
        if (TestUtil.findAll(em, WorkingDay.class).isEmpty()) {
            workingDay = WorkingDayResourceIT.createUpdatedEntity(em);
            em.persist(workingDay);
            em.flush();
        } else {
            workingDay = TestUtil.findAll(em, WorkingDay.class).get(0);
        }
        lLINSVillageReportHistory.setDayReached(workingDay);
        // Add required entity
        LlinsVillageReport lLINSVillageReport;
        if (TestUtil.findAll(em, LlinsVillageReport.class).isEmpty()) {
            lLINSVillageReport = LlinsVillageReportResourceIT.createUpdatedEntity(em);
            em.persist(lLINSVillageReport);
            em.flush();
        } else {
            lLINSVillageReport = TestUtil.findAll(em, LlinsVillageReport.class).get(0);
        }
        lLINSVillageReportHistory.setLlinsVillageReport(lLINSVillageReport);
        return lLINSVillageReportHistory;
    }

    @BeforeEach
    public void initTest() {
        lLINSVillageReportHistory = createEntity(em);
    }

    @Test
    @Transactional
    void createLLINSVillageReportHistory() throws Exception {
        int databaseSizeBeforeCreate = lLINSVillageReportHistoryRepository.findAll().size();
        // Create the LlinsVillageReportHistory
        restLLINSVillageReportHistoryMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(lLINSVillageReportHistory))
            )
            .andExpect(status().isCreated());

        // Validate the LlinsVillageReportHistory in the database
        List<LlinsVillageReportHistory> lLINSVillageReportHistoryList = lLINSVillageReportHistoryRepository.findAll();
        assertThat(lLINSVillageReportHistoryList).hasSize(databaseSizeBeforeCreate + 1);
        LlinsVillageReportHistory testLlinsVillageReportHistory = lLINSVillageReportHistoryList.get(
            lLINSVillageReportHistoryList.size() - 1
        );
        assertThat(testLlinsVillageReportHistory.getUid()).isEqualTo(DEFAULT_UID);
        assertThat(testLlinsVillageReportHistory.getCreated()).isEqualTo(DEFAULT_CREATED);
        assertThat(testLlinsVillageReportHistory.getLastUpdated()).isEqualTo(DEFAULT_LAST_UPDATED);
        assertThat(testLlinsVillageReportHistory.getHouses()).isEqualTo(DEFAULT_HOUSES);
        assertThat(testLlinsVillageReportHistory.getResidentHousehold()).isEqualTo(DEFAULT_RESIDENT_HOUSEHOLD);
        assertThat(testLlinsVillageReportHistory.getIdpsHousehold()).isEqualTo(DEFAULT_IDPS_HOUSEHOLD);
        assertThat(testLlinsVillageReportHistory.getMaleIndividuals()).isEqualTo(DEFAULT_MALE_INDIVIDUALS);
        assertThat(testLlinsVillageReportHistory.getFemaleIndividuals()).isEqualTo(DEFAULT_FEMALE_INDIVIDUALS);
        assertThat(testLlinsVillageReportHistory.getLessThan5Males()).isEqualTo(DEFAULT_LESS_THAN_5_MALES);
        assertThat(testLlinsVillageReportHistory.getLessThan5Females()).isEqualTo(DEFAULT_LESS_THAN_5_FEMALES);
        assertThat(testLlinsVillageReportHistory.getPregnantWomen()).isEqualTo(DEFAULT_PREGNANT_WOMEN);
        assertThat(testLlinsVillageReportHistory.getQuantityReceived()).isEqualTo(DEFAULT_QUANTITY_RECEIVED);
    }

    @Test
    @Transactional
    void createLLINSVillageReportHistoryWithExistingId() throws Exception {
        // Create the LlinsVillageReportHistory with an existing ID
        lLINSVillageReportHistory.setId(1L);

        int databaseSizeBeforeCreate = lLINSVillageReportHistoryRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLLINSVillageReportHistoryMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(lLINSVillageReportHistory))
            )
            .andExpect(status().isBadRequest());

        // Validate the LlinsVillageReportHistory in the database
        List<LlinsVillageReportHistory> lLINSVillageReportHistoryList = lLINSVillageReportHistoryRepository.findAll();
        assertThat(lLINSVillageReportHistoryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkUidIsRequired() throws Exception {
        int databaseSizeBeforeTest = lLINSVillageReportHistoryRepository.findAll().size();
        // set the field null
        lLINSVillageReportHistory.setUid(null);

        // Create the LlinsVillageReportHistory, which fails.

        restLLINSVillageReportHistoryMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(lLINSVillageReportHistory))
            )
            .andExpect(status().isBadRequest());

        List<LlinsVillageReportHistory> lLINSVillageReportHistoryList = lLINSVillageReportHistoryRepository.findAll();
        assertThat(lLINSVillageReportHistoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkHousesIsRequired() throws Exception {
        int databaseSizeBeforeTest = lLINSVillageReportHistoryRepository.findAll().size();
        // set the field null
        lLINSVillageReportHistory.setHouses(null);

        // Create the LlinsVillageReportHistory, which fails.

        restLLINSVillageReportHistoryMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(lLINSVillageReportHistory))
            )
            .andExpect(status().isBadRequest());

        List<LlinsVillageReportHistory> lLINSVillageReportHistoryList = lLINSVillageReportHistoryRepository.findAll();
        assertThat(lLINSVillageReportHistoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkResidentHouseholdIsRequired() throws Exception {
        int databaseSizeBeforeTest = lLINSVillageReportHistoryRepository.findAll().size();
        // set the field null
        lLINSVillageReportHistory.setResidentHousehold(null);

        // Create the LlinsVillageReportHistory, which fails.

        restLLINSVillageReportHistoryMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(lLINSVillageReportHistory))
            )
            .andExpect(status().isBadRequest());

        List<LlinsVillageReportHistory> lLINSVillageReportHistoryList = lLINSVillageReportHistoryRepository.findAll();
        assertThat(lLINSVillageReportHistoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIdpsHouseholdIsRequired() throws Exception {
        int databaseSizeBeforeTest = lLINSVillageReportHistoryRepository.findAll().size();
        // set the field null
        lLINSVillageReportHistory.setIdpsHousehold(null);

        // Create the LlinsVillageReportHistory, which fails.

        restLLINSVillageReportHistoryMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(lLINSVillageReportHistory))
            )
            .andExpect(status().isBadRequest());

        List<LlinsVillageReportHistory> lLINSVillageReportHistoryList = lLINSVillageReportHistoryRepository.findAll();
        assertThat(lLINSVillageReportHistoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkQuantityReceivedIsRequired() throws Exception {
        int databaseSizeBeforeTest = lLINSVillageReportHistoryRepository.findAll().size();
        // set the field null
        lLINSVillageReportHistory.setQuantityReceived(null);

        // Create the LlinsVillageReportHistory, which fails.

        restLLINSVillageReportHistoryMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(lLINSVillageReportHistory))
            )
            .andExpect(status().isBadRequest());

        List<LlinsVillageReportHistory> lLINSVillageReportHistoryList = lLINSVillageReportHistoryRepository.findAll();
        assertThat(lLINSVillageReportHistoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllLLINSVillageReportHistories() throws Exception {
        // Initialize the database
        lLINSVillageReportHistoryRepository.saveAndFlush(lLINSVillageReportHistory);

        // Get all the lLINSVillageReportHistoryList
        restLLINSVillageReportHistoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(lLINSVillageReportHistory.getId().intValue())))
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
            .andExpect(jsonPath("$.[*].quantityReceived").value(hasItem(DEFAULT_QUANTITY_RECEIVED)));
    }

    @Test
    @Transactional
    void getLLINSVillageReportHistory() throws Exception {
        // Initialize the database
        lLINSVillageReportHistoryRepository.saveAndFlush(lLINSVillageReportHistory);

        // Get the lLINSVillageReportHistory
        restLLINSVillageReportHistoryMockMvc
            .perform(get(ENTITY_API_URL_ID, lLINSVillageReportHistory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(lLINSVillageReportHistory.getId().intValue()))
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
            .andExpect(jsonPath("$.quantityReceived").value(DEFAULT_QUANTITY_RECEIVED));
    }

    @Test
    @Transactional
    void getNonExistingLLINSVillageReportHistory() throws Exception {
        // Get the lLINSVillageReportHistory
        restLLINSVillageReportHistoryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewLLINSVillageReportHistory() throws Exception {
        // Initialize the database
        lLINSVillageReportHistoryRepository.saveAndFlush(lLINSVillageReportHistory);

        int databaseSizeBeforeUpdate = lLINSVillageReportHistoryRepository.findAll().size();

        // Update the lLINSVillageReportHistory
        LlinsVillageReportHistory updatedLlinsVillageReportHistory = lLINSVillageReportHistoryRepository
            .findById(lLINSVillageReportHistory.getId())
            .get();
        // Disconnect from session so that the updates on updatedLlinsVillageReportHistory are not directly saved in db
        em.detach(updatedLlinsVillageReportHistory);
        updatedLlinsVillageReportHistory
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
            .quantityReceived(UPDATED_QUANTITY_RECEIVED);

        restLLINSVillageReportHistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedLlinsVillageReportHistory.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedLlinsVillageReportHistory))
            )
            .andExpect(status().isOk());

        // Validate the LlinsVillageReportHistory in the database
        List<LlinsVillageReportHistory> lLINSVillageReportHistoryList = lLINSVillageReportHistoryRepository.findAll();
        assertThat(lLINSVillageReportHistoryList).hasSize(databaseSizeBeforeUpdate);
        LlinsVillageReportHistory testLlinsVillageReportHistory = lLINSVillageReportHistoryList.get(
            lLINSVillageReportHistoryList.size() - 1
        );
        assertThat(testLlinsVillageReportHistory.getUid()).isEqualTo(UPDATED_UID);
        assertThat(testLlinsVillageReportHistory.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testLlinsVillageReportHistory.getLastUpdated()).isEqualTo(UPDATED_LAST_UPDATED);
        assertThat(testLlinsVillageReportHistory.getHouses()).isEqualTo(UPDATED_HOUSES);
        assertThat(testLlinsVillageReportHistory.getResidentHousehold()).isEqualTo(UPDATED_RESIDENT_HOUSEHOLD);
        assertThat(testLlinsVillageReportHistory.getIdpsHousehold()).isEqualTo(UPDATED_IDPS_HOUSEHOLD);
        assertThat(testLlinsVillageReportHistory.getMaleIndividuals()).isEqualTo(UPDATED_MALE_INDIVIDUALS);
        assertThat(testLlinsVillageReportHistory.getFemaleIndividuals()).isEqualTo(UPDATED_FEMALE_INDIVIDUALS);
        assertThat(testLlinsVillageReportHistory.getLessThan5Males()).isEqualTo(UPDATED_LESS_THAN_5_MALES);
        assertThat(testLlinsVillageReportHistory.getLessThan5Females()).isEqualTo(UPDATED_LESS_THAN_5_FEMALES);
        assertThat(testLlinsVillageReportHistory.getPregnantWomen()).isEqualTo(UPDATED_PREGNANT_WOMEN);
        assertThat(testLlinsVillageReportHistory.getQuantityReceived()).isEqualTo(UPDATED_QUANTITY_RECEIVED);
    }

    @Test
    @Transactional
    void putNonExistingLLINSVillageReportHistory() throws Exception {
        int databaseSizeBeforeUpdate = lLINSVillageReportHistoryRepository.findAll().size();
        lLINSVillageReportHistory.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLLINSVillageReportHistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, lLINSVillageReportHistory.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(lLINSVillageReportHistory))
            )
            .andExpect(status().isBadRequest());

        // Validate the LlinsVillageReportHistory in the database
        List<LlinsVillageReportHistory> lLINSVillageReportHistoryList = lLINSVillageReportHistoryRepository.findAll();
        assertThat(lLINSVillageReportHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLLINSVillageReportHistory() throws Exception {
        int databaseSizeBeforeUpdate = lLINSVillageReportHistoryRepository.findAll().size();
        lLINSVillageReportHistory.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLLINSVillageReportHistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(lLINSVillageReportHistory))
            )
            .andExpect(status().isBadRequest());

        // Validate the LlinsVillageReportHistory in the database
        List<LlinsVillageReportHistory> lLINSVillageReportHistoryList = lLINSVillageReportHistoryRepository.findAll();
        assertThat(lLINSVillageReportHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLLINSVillageReportHistory() throws Exception {
        int databaseSizeBeforeUpdate = lLINSVillageReportHistoryRepository.findAll().size();
        lLINSVillageReportHistory.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLLINSVillageReportHistoryMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(lLINSVillageReportHistory))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LlinsVillageReportHistory in the database
        List<LlinsVillageReportHistory> lLINSVillageReportHistoryList = lLINSVillageReportHistoryRepository.findAll();
        assertThat(lLINSVillageReportHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLLINSVillageReportHistoryWithPatch() throws Exception {
        // Initialize the database
        lLINSVillageReportHistoryRepository.saveAndFlush(lLINSVillageReportHistory);

        int databaseSizeBeforeUpdate = lLINSVillageReportHistoryRepository.findAll().size();

        // Update the lLINSVillageReportHistory using partial update
        LlinsVillageReportHistory partialUpdatedLlinsVillageReportHistory = new LlinsVillageReportHistory();
        partialUpdatedLlinsVillageReportHistory.setId(lLINSVillageReportHistory.getId());

        partialUpdatedLlinsVillageReportHistory
            .uid(UPDATED_UID)
            .created(UPDATED_CREATED)
            .houses(UPDATED_HOUSES)
            .femaleIndividuals(UPDATED_FEMALE_INDIVIDUALS)
            .lessThan5Males(UPDATED_LESS_THAN_5_MALES)
            .lessThan5Females(UPDATED_LESS_THAN_5_FEMALES)
            .quantityReceived(UPDATED_QUANTITY_RECEIVED);

        restLLINSVillageReportHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLlinsVillageReportHistory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLlinsVillageReportHistory))
            )
            .andExpect(status().isOk());

        // Validate the LlinsVillageReportHistory in the database
        List<LlinsVillageReportHistory> lLINSVillageReportHistoryList = lLINSVillageReportHistoryRepository.findAll();
        assertThat(lLINSVillageReportHistoryList).hasSize(databaseSizeBeforeUpdate);
        LlinsVillageReportHistory testLlinsVillageReportHistory = lLINSVillageReportHistoryList.get(
            lLINSVillageReportHistoryList.size() - 1
        );
        assertThat(testLlinsVillageReportHistory.getUid()).isEqualTo(UPDATED_UID);
        assertThat(testLlinsVillageReportHistory.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testLlinsVillageReportHistory.getLastUpdated()).isEqualTo(DEFAULT_LAST_UPDATED);
        assertThat(testLlinsVillageReportHistory.getHouses()).isEqualTo(UPDATED_HOUSES);
        assertThat(testLlinsVillageReportHistory.getResidentHousehold()).isEqualTo(DEFAULT_RESIDENT_HOUSEHOLD);
        assertThat(testLlinsVillageReportHistory.getIdpsHousehold()).isEqualTo(DEFAULT_IDPS_HOUSEHOLD);
        assertThat(testLlinsVillageReportHistory.getMaleIndividuals()).isEqualTo(DEFAULT_MALE_INDIVIDUALS);
        assertThat(testLlinsVillageReportHistory.getFemaleIndividuals()).isEqualTo(UPDATED_FEMALE_INDIVIDUALS);
        assertThat(testLlinsVillageReportHistory.getLessThan5Males()).isEqualTo(UPDATED_LESS_THAN_5_MALES);
        assertThat(testLlinsVillageReportHistory.getLessThan5Females()).isEqualTo(UPDATED_LESS_THAN_5_FEMALES);
        assertThat(testLlinsVillageReportHistory.getPregnantWomen()).isEqualTo(DEFAULT_PREGNANT_WOMEN);
        assertThat(testLlinsVillageReportHistory.getQuantityReceived()).isEqualTo(UPDATED_QUANTITY_RECEIVED);
    }

    @Test
    @Transactional
    void fullUpdateLLINSVillageReportHistoryWithPatch() throws Exception {
        // Initialize the database
        lLINSVillageReportHistoryRepository.saveAndFlush(lLINSVillageReportHistory);

        int databaseSizeBeforeUpdate = lLINSVillageReportHistoryRepository.findAll().size();

        // Update the lLINSVillageReportHistory using partial update
        LlinsVillageReportHistory partialUpdatedLlinsVillageReportHistory = new LlinsVillageReportHistory();
        partialUpdatedLlinsVillageReportHistory.setId(lLINSVillageReportHistory.getId());

        partialUpdatedLlinsVillageReportHistory
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
            .quantityReceived(UPDATED_QUANTITY_RECEIVED);

        restLLINSVillageReportHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLlinsVillageReportHistory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLlinsVillageReportHistory))
            )
            .andExpect(status().isOk());

        // Validate the LlinsVillageReportHistory in the database
        List<LlinsVillageReportHistory> lLINSVillageReportHistoryList = lLINSVillageReportHistoryRepository.findAll();
        assertThat(lLINSVillageReportHistoryList).hasSize(databaseSizeBeforeUpdate);
        LlinsVillageReportHistory testLlinsVillageReportHistory = lLINSVillageReportHistoryList.get(
            lLINSVillageReportHistoryList.size() - 1
        );
        assertThat(testLlinsVillageReportHistory.getUid()).isEqualTo(UPDATED_UID);
        assertThat(testLlinsVillageReportHistory.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testLlinsVillageReportHistory.getLastUpdated()).isEqualTo(UPDATED_LAST_UPDATED);
        assertThat(testLlinsVillageReportHistory.getHouses()).isEqualTo(UPDATED_HOUSES);
        assertThat(testLlinsVillageReportHistory.getResidentHousehold()).isEqualTo(UPDATED_RESIDENT_HOUSEHOLD);
        assertThat(testLlinsVillageReportHistory.getIdpsHousehold()).isEqualTo(UPDATED_IDPS_HOUSEHOLD);
        assertThat(testLlinsVillageReportHistory.getMaleIndividuals()).isEqualTo(UPDATED_MALE_INDIVIDUALS);
        assertThat(testLlinsVillageReportHistory.getFemaleIndividuals()).isEqualTo(UPDATED_FEMALE_INDIVIDUALS);
        assertThat(testLlinsVillageReportHistory.getLessThan5Males()).isEqualTo(UPDATED_LESS_THAN_5_MALES);
        assertThat(testLlinsVillageReportHistory.getLessThan5Females()).isEqualTo(UPDATED_LESS_THAN_5_FEMALES);
        assertThat(testLlinsVillageReportHistory.getPregnantWomen()).isEqualTo(UPDATED_PREGNANT_WOMEN);
        assertThat(testLlinsVillageReportHistory.getQuantityReceived()).isEqualTo(UPDATED_QUANTITY_RECEIVED);
    }

    @Test
    @Transactional
    void patchNonExistingLLINSVillageReportHistory() throws Exception {
        int databaseSizeBeforeUpdate = lLINSVillageReportHistoryRepository.findAll().size();
        lLINSVillageReportHistory.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLLINSVillageReportHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, lLINSVillageReportHistory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(lLINSVillageReportHistory))
            )
            .andExpect(status().isBadRequest());

        // Validate the LlinsVillageReportHistory in the database
        List<LlinsVillageReportHistory> lLINSVillageReportHistoryList = lLINSVillageReportHistoryRepository.findAll();
        assertThat(lLINSVillageReportHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLLINSVillageReportHistory() throws Exception {
        int databaseSizeBeforeUpdate = lLINSVillageReportHistoryRepository.findAll().size();
        lLINSVillageReportHistory.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLLINSVillageReportHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(lLINSVillageReportHistory))
            )
            .andExpect(status().isBadRequest());

        // Validate the LlinsVillageReportHistory in the database
        List<LlinsVillageReportHistory> lLINSVillageReportHistoryList = lLINSVillageReportHistoryRepository.findAll();
        assertThat(lLINSVillageReportHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLLINSVillageReportHistory() throws Exception {
        int databaseSizeBeforeUpdate = lLINSVillageReportHistoryRepository.findAll().size();
        lLINSVillageReportHistory.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLLINSVillageReportHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(lLINSVillageReportHistory))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LlinsVillageReportHistory in the database
        List<LlinsVillageReportHistory> lLINSVillageReportHistoryList = lLINSVillageReportHistoryRepository.findAll();
        assertThat(lLINSVillageReportHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLLINSVillageReportHistory() throws Exception {
        // Initialize the database
        lLINSVillageReportHistoryRepository.saveAndFlush(lLINSVillageReportHistory);

        int databaseSizeBeforeDelete = lLINSVillageReportHistoryRepository.findAll().size();

        // Delete the lLINSVillageReportHistory
        restLLINSVillageReportHistoryMockMvc
            .perform(delete(ENTITY_API_URL_ID, lLINSVillageReportHistory.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<LlinsVillageReportHistory> lLINSVillageReportHistoryList = lLINSVillageReportHistoryRepository.findAll();
        assertThat(lLINSVillageReportHistoryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
