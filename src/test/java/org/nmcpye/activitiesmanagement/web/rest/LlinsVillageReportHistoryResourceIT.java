package org.nmcpye.activitiesmanagement.web.rest;

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
    private LlinsVillageReportHistoryRepository llinsVillageReportHistoryRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLlinsVillageReportHistoryMockMvc;

    private LlinsVillageReportHistory llinsVillageReportHistory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LlinsVillageReportHistory createEntity(EntityManager em) {
        LlinsVillageReportHistory llinsVillageReportHistory = new LlinsVillageReportHistory()
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
        llinsVillageReportHistory.setDayReached(workingDay);
        // Add required entity
        LlinsVillageReport llinsVillageReport;
        if (TestUtil.findAll(em, LlinsVillageReport.class).isEmpty()) {
            llinsVillageReport = LlinsVillageReportResourceIT.createEntity(em);
            em.persist(llinsVillageReport);
            em.flush();
        } else {
            llinsVillageReport = TestUtil.findAll(em, LlinsVillageReport.class).get(0);
        }
        llinsVillageReportHistory.setLlinsVillageReport(llinsVillageReport);
        return llinsVillageReportHistory;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LlinsVillageReportHistory createUpdatedEntity(EntityManager em) {
        LlinsVillageReportHistory llinsVillageReportHistory = new LlinsVillageReportHistory()
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
        llinsVillageReportHistory.setDayReached(workingDay);
        // Add required entity
        LlinsVillageReport llinsVillageReport;
        if (TestUtil.findAll(em, LlinsVillageReport.class).isEmpty()) {
            llinsVillageReport = LlinsVillageReportResourceIT.createUpdatedEntity(em);
            em.persist(llinsVillageReport);
            em.flush();
        } else {
            llinsVillageReport = TestUtil.findAll(em, LlinsVillageReport.class).get(0);
        }
        llinsVillageReportHistory.setLlinsVillageReport(llinsVillageReport);
        return llinsVillageReportHistory;
    }

    @BeforeEach
    public void initTest() {
        llinsVillageReportHistory = createEntity(em);
    }

    @Test
    @Transactional
    void createLlinsVillageReportHistory() throws Exception {
        int databaseSizeBeforeCreate = llinsVillageReportHistoryRepository.findAll().size();
        // Create the LlinsVillageReportHistory
        restLlinsVillageReportHistoryMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(llinsVillageReportHistory))
            )
            .andExpect(status().isCreated());

        // Validate the LlinsVillageReportHistory in the database
        List<LlinsVillageReportHistory> llinsVillageReportHistoryList = llinsVillageReportHistoryRepository.findAll();
        assertThat(llinsVillageReportHistoryList).hasSize(databaseSizeBeforeCreate + 1);
        LlinsVillageReportHistory testLlinsVillageReportHistory = llinsVillageReportHistoryList.get(
            llinsVillageReportHistoryList.size() - 1
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
    void createLlinsVillageReportHistoryWithExistingId() throws Exception {
        // Create the LlinsVillageReportHistory with an existing ID
        llinsVillageReportHistory.setId(1L);

        int databaseSizeBeforeCreate = llinsVillageReportHistoryRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLlinsVillageReportHistoryMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(llinsVillageReportHistory))
            )
            .andExpect(status().isBadRequest());

        // Validate the LlinsVillageReportHistory in the database
        List<LlinsVillageReportHistory> llinsVillageReportHistoryList = llinsVillageReportHistoryRepository.findAll();
        assertThat(llinsVillageReportHistoryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkUidIsRequired() throws Exception {
        int databaseSizeBeforeTest = llinsVillageReportHistoryRepository.findAll().size();
        // set the field null
        llinsVillageReportHistory.setUid(null);

        // Create the LlinsVillageReportHistory, which fails.

        restLlinsVillageReportHistoryMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(llinsVillageReportHistory))
            )
            .andExpect(status().isBadRequest());

        List<LlinsVillageReportHistory> llinsVillageReportHistoryList = llinsVillageReportHistoryRepository.findAll();
        assertThat(llinsVillageReportHistoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkHousesIsRequired() throws Exception {
        int databaseSizeBeforeTest = llinsVillageReportHistoryRepository.findAll().size();
        // set the field null
        llinsVillageReportHistory.setHouses(null);

        // Create the LlinsVillageReportHistory, which fails.

        restLlinsVillageReportHistoryMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(llinsVillageReportHistory))
            )
            .andExpect(status().isBadRequest());

        List<LlinsVillageReportHistory> llinsVillageReportHistoryList = llinsVillageReportHistoryRepository.findAll();
        assertThat(llinsVillageReportHistoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkResidentHouseholdIsRequired() throws Exception {
        int databaseSizeBeforeTest = llinsVillageReportHistoryRepository.findAll().size();
        // set the field null
        llinsVillageReportHistory.setResidentHousehold(null);

        // Create the LlinsVillageReportHistory, which fails.

        restLlinsVillageReportHistoryMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(llinsVillageReportHistory))
            )
            .andExpect(status().isBadRequest());

        List<LlinsVillageReportHistory> llinsVillageReportHistoryList = llinsVillageReportHistoryRepository.findAll();
        assertThat(llinsVillageReportHistoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIdpsHouseholdIsRequired() throws Exception {
        int databaseSizeBeforeTest = llinsVillageReportHistoryRepository.findAll().size();
        // set the field null
        llinsVillageReportHistory.setIdpsHousehold(null);

        // Create the LlinsVillageReportHistory, which fails.

        restLlinsVillageReportHistoryMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(llinsVillageReportHistory))
            )
            .andExpect(status().isBadRequest());

        List<LlinsVillageReportHistory> llinsVillageReportHistoryList = llinsVillageReportHistoryRepository.findAll();
        assertThat(llinsVillageReportHistoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkQuantityReceivedIsRequired() throws Exception {
        int databaseSizeBeforeTest = llinsVillageReportHistoryRepository.findAll().size();
        // set the field null
        llinsVillageReportHistory.setQuantityReceived(null);

        // Create the LlinsVillageReportHistory, which fails.

        restLlinsVillageReportHistoryMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(llinsVillageReportHistory))
            )
            .andExpect(status().isBadRequest());

        List<LlinsVillageReportHistory> llinsVillageReportHistoryList = llinsVillageReportHistoryRepository.findAll();
        assertThat(llinsVillageReportHistoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllLlinsVillageReportHistories() throws Exception {
        // Initialize the database
        llinsVillageReportHistoryRepository.saveAndFlush(llinsVillageReportHistory);

        // Get all the llinsVillageReportHistoryList
        restLlinsVillageReportHistoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(llinsVillageReportHistory.getId().intValue())))
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
    void getLlinsVillageReportHistory() throws Exception {
        // Initialize the database
        llinsVillageReportHistoryRepository.saveAndFlush(llinsVillageReportHistory);

        // Get the llinsVillageReportHistory
        restLlinsVillageReportHistoryMockMvc
            .perform(get(ENTITY_API_URL_ID, llinsVillageReportHistory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(llinsVillageReportHistory.getId().intValue()))
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
    void getNonExistingLlinsVillageReportHistory() throws Exception {
        // Get the llinsVillageReportHistory
        restLlinsVillageReportHistoryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewLlinsVillageReportHistory() throws Exception {
        // Initialize the database
        llinsVillageReportHistoryRepository.saveAndFlush(llinsVillageReportHistory);

        int databaseSizeBeforeUpdate = llinsVillageReportHistoryRepository.findAll().size();

        // Update the llinsVillageReportHistory
        LlinsVillageReportHistory updatedLlinsVillageReportHistory = llinsVillageReportHistoryRepository
            .findById(llinsVillageReportHistory.getId())
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

        restLlinsVillageReportHistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedLlinsVillageReportHistory.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedLlinsVillageReportHistory))
            )
            .andExpect(status().isOk());

        // Validate the LlinsVillageReportHistory in the database
        List<LlinsVillageReportHistory> llinsVillageReportHistoryList = llinsVillageReportHistoryRepository.findAll();
        assertThat(llinsVillageReportHistoryList).hasSize(databaseSizeBeforeUpdate);
        LlinsVillageReportHistory testLlinsVillageReportHistory = llinsVillageReportHistoryList.get(
            llinsVillageReportHistoryList.size() - 1
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
    void putNonExistingLlinsVillageReportHistory() throws Exception {
        int databaseSizeBeforeUpdate = llinsVillageReportHistoryRepository.findAll().size();
        llinsVillageReportHistory.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLlinsVillageReportHistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, llinsVillageReportHistory.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(llinsVillageReportHistory))
            )
            .andExpect(status().isBadRequest());

        // Validate the LlinsVillageReportHistory in the database
        List<LlinsVillageReportHistory> llinsVillageReportHistoryList = llinsVillageReportHistoryRepository.findAll();
        assertThat(llinsVillageReportHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLlinsVillageReportHistory() throws Exception {
        int databaseSizeBeforeUpdate = llinsVillageReportHistoryRepository.findAll().size();
        llinsVillageReportHistory.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLlinsVillageReportHistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(llinsVillageReportHistory))
            )
            .andExpect(status().isBadRequest());

        // Validate the LlinsVillageReportHistory in the database
        List<LlinsVillageReportHistory> llinsVillageReportHistoryList = llinsVillageReportHistoryRepository.findAll();
        assertThat(llinsVillageReportHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLlinsVillageReportHistory() throws Exception {
        int databaseSizeBeforeUpdate = llinsVillageReportHistoryRepository.findAll().size();
        llinsVillageReportHistory.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLlinsVillageReportHistoryMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(llinsVillageReportHistory))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LlinsVillageReportHistory in the database
        List<LlinsVillageReportHistory> llinsVillageReportHistoryList = llinsVillageReportHistoryRepository.findAll();
        assertThat(llinsVillageReportHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLlinsVillageReportHistoryWithPatch() throws Exception {
        // Initialize the database
        llinsVillageReportHistoryRepository.saveAndFlush(llinsVillageReportHistory);

        int databaseSizeBeforeUpdate = llinsVillageReportHistoryRepository.findAll().size();

        // Update the llinsVillageReportHistory using partial update
        LlinsVillageReportHistory partialUpdatedLlinsVillageReportHistory = new LlinsVillageReportHistory();
        partialUpdatedLlinsVillageReportHistory.setId(llinsVillageReportHistory.getId());

        partialUpdatedLlinsVillageReportHistory
            .created(UPDATED_CREATED)
            .lastUpdated(UPDATED_LAST_UPDATED)
            .idpsHousehold(UPDATED_IDPS_HOUSEHOLD);

        restLlinsVillageReportHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLlinsVillageReportHistory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLlinsVillageReportHistory))
            )
            .andExpect(status().isOk());

        // Validate the LlinsVillageReportHistory in the database
        List<LlinsVillageReportHistory> llinsVillageReportHistoryList = llinsVillageReportHistoryRepository.findAll();
        assertThat(llinsVillageReportHistoryList).hasSize(databaseSizeBeforeUpdate);
        LlinsVillageReportHistory testLlinsVillageReportHistory = llinsVillageReportHistoryList.get(
            llinsVillageReportHistoryList.size() - 1
        );
        assertThat(testLlinsVillageReportHistory.getUid()).isEqualTo(DEFAULT_UID);
        assertThat(testLlinsVillageReportHistory.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testLlinsVillageReportHistory.getLastUpdated()).isEqualTo(UPDATED_LAST_UPDATED);
        assertThat(testLlinsVillageReportHistory.getHouses()).isEqualTo(DEFAULT_HOUSES);
        assertThat(testLlinsVillageReportHistory.getResidentHousehold()).isEqualTo(DEFAULT_RESIDENT_HOUSEHOLD);
        assertThat(testLlinsVillageReportHistory.getIdpsHousehold()).isEqualTo(UPDATED_IDPS_HOUSEHOLD);
        assertThat(testLlinsVillageReportHistory.getMaleIndividuals()).isEqualTo(DEFAULT_MALE_INDIVIDUALS);
        assertThat(testLlinsVillageReportHistory.getFemaleIndividuals()).isEqualTo(DEFAULT_FEMALE_INDIVIDUALS);
        assertThat(testLlinsVillageReportHistory.getLessThan5Males()).isEqualTo(DEFAULT_LESS_THAN_5_MALES);
        assertThat(testLlinsVillageReportHistory.getLessThan5Females()).isEqualTo(DEFAULT_LESS_THAN_5_FEMALES);
        assertThat(testLlinsVillageReportHistory.getPregnantWomen()).isEqualTo(DEFAULT_PREGNANT_WOMEN);
        assertThat(testLlinsVillageReportHistory.getQuantityReceived()).isEqualTo(DEFAULT_QUANTITY_RECEIVED);
    }

    @Test
    @Transactional
    void fullUpdateLlinsVillageReportHistoryWithPatch() throws Exception {
        // Initialize the database
        llinsVillageReportHistoryRepository.saveAndFlush(llinsVillageReportHistory);

        int databaseSizeBeforeUpdate = llinsVillageReportHistoryRepository.findAll().size();

        // Update the llinsVillageReportHistory using partial update
        LlinsVillageReportHistory partialUpdatedLlinsVillageReportHistory = new LlinsVillageReportHistory();
        partialUpdatedLlinsVillageReportHistory.setId(llinsVillageReportHistory.getId());

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

        restLlinsVillageReportHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLlinsVillageReportHistory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLlinsVillageReportHistory))
            )
            .andExpect(status().isOk());

        // Validate the LlinsVillageReportHistory in the database
        List<LlinsVillageReportHistory> llinsVillageReportHistoryList = llinsVillageReportHistoryRepository.findAll();
        assertThat(llinsVillageReportHistoryList).hasSize(databaseSizeBeforeUpdate);
        LlinsVillageReportHistory testLlinsVillageReportHistory = llinsVillageReportHistoryList.get(
            llinsVillageReportHistoryList.size() - 1
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
    void patchNonExistingLlinsVillageReportHistory() throws Exception {
        int databaseSizeBeforeUpdate = llinsVillageReportHistoryRepository.findAll().size();
        llinsVillageReportHistory.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLlinsVillageReportHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, llinsVillageReportHistory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(llinsVillageReportHistory))
            )
            .andExpect(status().isBadRequest());

        // Validate the LlinsVillageReportHistory in the database
        List<LlinsVillageReportHistory> llinsVillageReportHistoryList = llinsVillageReportHistoryRepository.findAll();
        assertThat(llinsVillageReportHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLlinsVillageReportHistory() throws Exception {
        int databaseSizeBeforeUpdate = llinsVillageReportHistoryRepository.findAll().size();
        llinsVillageReportHistory.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLlinsVillageReportHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(llinsVillageReportHistory))
            )
            .andExpect(status().isBadRequest());

        // Validate the LlinsVillageReportHistory in the database
        List<LlinsVillageReportHistory> llinsVillageReportHistoryList = llinsVillageReportHistoryRepository.findAll();
        assertThat(llinsVillageReportHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLlinsVillageReportHistory() throws Exception {
        int databaseSizeBeforeUpdate = llinsVillageReportHistoryRepository.findAll().size();
        llinsVillageReportHistory.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLlinsVillageReportHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(llinsVillageReportHistory))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LlinsVillageReportHistory in the database
        List<LlinsVillageReportHistory> llinsVillageReportHistoryList = llinsVillageReportHistoryRepository.findAll();
        assertThat(llinsVillageReportHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLlinsVillageReportHistory() throws Exception {
        // Initialize the database
        llinsVillageReportHistoryRepository.saveAndFlush(llinsVillageReportHistory);

        int databaseSizeBeforeDelete = llinsVillageReportHistoryRepository.findAll().size();

        // Delete the llinsVillageReportHistory
        restLlinsVillageReportHistoryMockMvc
            .perform(delete(ENTITY_API_URL_ID, llinsVillageReportHistory.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<LlinsVillageReportHistory> llinsVillageReportHistoryList = llinsVillageReportHistoryRepository.findAll();
        assertThat(llinsVillageReportHistoryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
