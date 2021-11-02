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
import org.nmcpye.activitiesmanagement.domain.LLINSFamilyReportHistory;
import org.nmcpye.activitiesmanagement.domain.WorkingDay;
import org.nmcpye.activitiesmanagement.domain.enumeration.FamilyType;
import org.nmcpye.activitiesmanagement.repository.LLINSFamilyReportHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link LLINSFamilyReportHistoryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class LLINSFamilyReportHistoryResourceIT {

    private static final String DEFAULT_UID = "AAAAAAAAAA";
    private static final String UPDATED_UID = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_LAST_UPDATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_UPDATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Integer DEFAULT_DOCUMENT_NO = 1;
    private static final Integer UPDATED_DOCUMENT_NO = 2;

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

    private static final String ENTITY_API_URL = "/api/llins-family-report-histories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private LLINSFamilyReportHistoryRepository lLINSFamilyReportHistoryRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLLINSFamilyReportHistoryMockMvc;

    private LLINSFamilyReportHistory lLINSFamilyReportHistory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LLINSFamilyReportHistory createEntity(EntityManager em) {
        LLINSFamilyReportHistory lLINSFamilyReportHistory = new LLINSFamilyReportHistory()
            .uid(DEFAULT_UID)
            .created(DEFAULT_CREATED)
            .lastUpdated(DEFAULT_LAST_UPDATED)
            .documentNo(DEFAULT_DOCUMENT_NO)
            .maleIndividuals(DEFAULT_MALE_INDIVIDUALS)
            .femaleIndividuals(DEFAULT_FEMALE_INDIVIDUALS)
            .lessThan5Males(DEFAULT_LESS_THAN_5_MALES)
            .lessThan5Females(DEFAULT_LESS_THAN_5_FEMALES)
            .pregnantWomen(DEFAULT_PREGNANT_WOMEN)
            .quantityReceived(DEFAULT_QUANTITY_RECEIVED)
            .familyType(DEFAULT_FAMILY_TYPE);
        // Add required entity
        WorkingDay workingDay;
        if (TestUtil.findAll(em, WorkingDay.class).isEmpty()) {
            workingDay = WorkingDayResourceIT.createEntity(em);
            em.persist(workingDay);
            em.flush();
        } else {
            workingDay = TestUtil.findAll(em, WorkingDay.class).get(0);
        }
        lLINSFamilyReportHistory.setDayReached(workingDay);
        // Add required entity
        LLINSFamilyReport lLINSFamilyReport;
        if (TestUtil.findAll(em, LLINSFamilyReport.class).isEmpty()) {
            lLINSFamilyReport = LLINSFamilyReportResourceIT.createEntity(em);
            em.persist(lLINSFamilyReport);
            em.flush();
        } else {
            lLINSFamilyReport = TestUtil.findAll(em, LLINSFamilyReport.class).get(0);
        }
        lLINSFamilyReportHistory.setLlinsFamilyReport(lLINSFamilyReport);
        return lLINSFamilyReportHistory;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LLINSFamilyReportHistory createUpdatedEntity(EntityManager em) {
        LLINSFamilyReportHistory lLINSFamilyReportHistory = new LLINSFamilyReportHistory()
            .uid(UPDATED_UID)
            .created(UPDATED_CREATED)
            .lastUpdated(UPDATED_LAST_UPDATED)
            .documentNo(UPDATED_DOCUMENT_NO)
            .maleIndividuals(UPDATED_MALE_INDIVIDUALS)
            .femaleIndividuals(UPDATED_FEMALE_INDIVIDUALS)
            .lessThan5Males(UPDATED_LESS_THAN_5_MALES)
            .lessThan5Females(UPDATED_LESS_THAN_5_FEMALES)
            .pregnantWomen(UPDATED_PREGNANT_WOMEN)
            .quantityReceived(UPDATED_QUANTITY_RECEIVED)
            .familyType(UPDATED_FAMILY_TYPE);
        // Add required entity
        WorkingDay workingDay;
        if (TestUtil.findAll(em, WorkingDay.class).isEmpty()) {
            workingDay = WorkingDayResourceIT.createUpdatedEntity(em);
            em.persist(workingDay);
            em.flush();
        } else {
            workingDay = TestUtil.findAll(em, WorkingDay.class).get(0);
        }
        lLINSFamilyReportHistory.setDayReached(workingDay);
        // Add required entity
        LLINSFamilyReport lLINSFamilyReport;
        if (TestUtil.findAll(em, LLINSFamilyReport.class).isEmpty()) {
            lLINSFamilyReport = LLINSFamilyReportResourceIT.createUpdatedEntity(em);
            em.persist(lLINSFamilyReport);
            em.flush();
        } else {
            lLINSFamilyReport = TestUtil.findAll(em, LLINSFamilyReport.class).get(0);
        }
        lLINSFamilyReportHistory.setLlinsFamilyReport(lLINSFamilyReport);
        return lLINSFamilyReportHistory;
    }

    @BeforeEach
    public void initTest() {
        lLINSFamilyReportHistory = createEntity(em);
    }

    @Test
    @Transactional
    void createLLINSFamilyReportHistory() throws Exception {
        int databaseSizeBeforeCreate = lLINSFamilyReportHistoryRepository.findAll().size();
        // Create the LLINSFamilyReportHistory
        restLLINSFamilyReportHistoryMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(lLINSFamilyReportHistory))
            )
            .andExpect(status().isCreated());

        // Validate the LLINSFamilyReportHistory in the database
        List<LLINSFamilyReportHistory> lLINSFamilyReportHistoryList = lLINSFamilyReportHistoryRepository.findAll();
        assertThat(lLINSFamilyReportHistoryList).hasSize(databaseSizeBeforeCreate + 1);
        LLINSFamilyReportHistory testLLINSFamilyReportHistory = lLINSFamilyReportHistoryList.get(lLINSFamilyReportHistoryList.size() - 1);
        assertThat(testLLINSFamilyReportHistory.getUid()).isEqualTo(DEFAULT_UID);
        assertThat(testLLINSFamilyReportHistory.getCreated()).isEqualTo(DEFAULT_CREATED);
        assertThat(testLLINSFamilyReportHistory.getLastUpdated()).isEqualTo(DEFAULT_LAST_UPDATED);
        assertThat(testLLINSFamilyReportHistory.getDocumentNo()).isEqualTo(DEFAULT_DOCUMENT_NO);
        assertThat(testLLINSFamilyReportHistory.getMaleIndividuals()).isEqualTo(DEFAULT_MALE_INDIVIDUALS);
        assertThat(testLLINSFamilyReportHistory.getFemaleIndividuals()).isEqualTo(DEFAULT_FEMALE_INDIVIDUALS);
        assertThat(testLLINSFamilyReportHistory.getLessThan5Males()).isEqualTo(DEFAULT_LESS_THAN_5_MALES);
        assertThat(testLLINSFamilyReportHistory.getLessThan5Females()).isEqualTo(DEFAULT_LESS_THAN_5_FEMALES);
        assertThat(testLLINSFamilyReportHistory.getPregnantWomen()).isEqualTo(DEFAULT_PREGNANT_WOMEN);
        assertThat(testLLINSFamilyReportHistory.getQuantityReceived()).isEqualTo(DEFAULT_QUANTITY_RECEIVED);
        assertThat(testLLINSFamilyReportHistory.getFamilyType()).isEqualTo(DEFAULT_FAMILY_TYPE);
    }

    @Test
    @Transactional
    void createLLINSFamilyReportHistoryWithExistingId() throws Exception {
        // Create the LLINSFamilyReportHistory with an existing ID
        lLINSFamilyReportHistory.setId(1L);

        int databaseSizeBeforeCreate = lLINSFamilyReportHistoryRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLLINSFamilyReportHistoryMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(lLINSFamilyReportHistory))
            )
            .andExpect(status().isBadRequest());

        // Validate the LLINSFamilyReportHistory in the database
        List<LLINSFamilyReportHistory> lLINSFamilyReportHistoryList = lLINSFamilyReportHistoryRepository.findAll();
        assertThat(lLINSFamilyReportHistoryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkUidIsRequired() throws Exception {
        int databaseSizeBeforeTest = lLINSFamilyReportHistoryRepository.findAll().size();
        // set the field null
        lLINSFamilyReportHistory.setUid(null);

        // Create the LLINSFamilyReportHistory, which fails.

        restLLINSFamilyReportHistoryMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(lLINSFamilyReportHistory))
            )
            .andExpect(status().isBadRequest());

        List<LLINSFamilyReportHistory> lLINSFamilyReportHistoryList = lLINSFamilyReportHistoryRepository.findAll();
        assertThat(lLINSFamilyReportHistoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFamilyTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = lLINSFamilyReportHistoryRepository.findAll().size();
        // set the field null
        lLINSFamilyReportHistory.setFamilyType(null);

        // Create the LLINSFamilyReportHistory, which fails.

        restLLINSFamilyReportHistoryMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(lLINSFamilyReportHistory))
            )
            .andExpect(status().isBadRequest());

        List<LLINSFamilyReportHistory> lLINSFamilyReportHistoryList = lLINSFamilyReportHistoryRepository.findAll();
        assertThat(lLINSFamilyReportHistoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllLLINSFamilyReportHistories() throws Exception {
        // Initialize the database
        lLINSFamilyReportHistoryRepository.saveAndFlush(lLINSFamilyReportHistory);

        // Get all the lLINSFamilyReportHistoryList
        restLLINSFamilyReportHistoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(lLINSFamilyReportHistory.getId().intValue())))
            .andExpect(jsonPath("$.[*].uid").value(hasItem(DEFAULT_UID)))
            .andExpect(jsonPath("$.[*].created").value(hasItem(DEFAULT_CREATED.toString())))
            .andExpect(jsonPath("$.[*].lastUpdated").value(hasItem(DEFAULT_LAST_UPDATED.toString())))
            .andExpect(jsonPath("$.[*].documentNo").value(hasItem(DEFAULT_DOCUMENT_NO)))
            .andExpect(jsonPath("$.[*].maleIndividuals").value(hasItem(DEFAULT_MALE_INDIVIDUALS)))
            .andExpect(jsonPath("$.[*].femaleIndividuals").value(hasItem(DEFAULT_FEMALE_INDIVIDUALS)))
            .andExpect(jsonPath("$.[*].lessThan5Males").value(hasItem(DEFAULT_LESS_THAN_5_MALES)))
            .andExpect(jsonPath("$.[*].lessThan5Females").value(hasItem(DEFAULT_LESS_THAN_5_FEMALES)))
            .andExpect(jsonPath("$.[*].pregnantWomen").value(hasItem(DEFAULT_PREGNANT_WOMEN)))
            .andExpect(jsonPath("$.[*].quantityReceived").value(hasItem(DEFAULT_QUANTITY_RECEIVED)))
            .andExpect(jsonPath("$.[*].familyType").value(hasItem(DEFAULT_FAMILY_TYPE.toString())));
    }

    @Test
    @Transactional
    void getLLINSFamilyReportHistory() throws Exception {
        // Initialize the database
        lLINSFamilyReportHistoryRepository.saveAndFlush(lLINSFamilyReportHistory);

        // Get the lLINSFamilyReportHistory
        restLLINSFamilyReportHistoryMockMvc
            .perform(get(ENTITY_API_URL_ID, lLINSFamilyReportHistory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(lLINSFamilyReportHistory.getId().intValue()))
            .andExpect(jsonPath("$.uid").value(DEFAULT_UID))
            .andExpect(jsonPath("$.created").value(DEFAULT_CREATED.toString()))
            .andExpect(jsonPath("$.lastUpdated").value(DEFAULT_LAST_UPDATED.toString()))
            .andExpect(jsonPath("$.documentNo").value(DEFAULT_DOCUMENT_NO))
            .andExpect(jsonPath("$.maleIndividuals").value(DEFAULT_MALE_INDIVIDUALS))
            .andExpect(jsonPath("$.femaleIndividuals").value(DEFAULT_FEMALE_INDIVIDUALS))
            .andExpect(jsonPath("$.lessThan5Males").value(DEFAULT_LESS_THAN_5_MALES))
            .andExpect(jsonPath("$.lessThan5Females").value(DEFAULT_LESS_THAN_5_FEMALES))
            .andExpect(jsonPath("$.pregnantWomen").value(DEFAULT_PREGNANT_WOMEN))
            .andExpect(jsonPath("$.quantityReceived").value(DEFAULT_QUANTITY_RECEIVED))
            .andExpect(jsonPath("$.familyType").value(DEFAULT_FAMILY_TYPE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingLLINSFamilyReportHistory() throws Exception {
        // Get the lLINSFamilyReportHistory
        restLLINSFamilyReportHistoryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewLLINSFamilyReportHistory() throws Exception {
        // Initialize the database
        lLINSFamilyReportHistoryRepository.saveAndFlush(lLINSFamilyReportHistory);

        int databaseSizeBeforeUpdate = lLINSFamilyReportHistoryRepository.findAll().size();

        // Update the lLINSFamilyReportHistory
        LLINSFamilyReportHistory updatedLLINSFamilyReportHistory = lLINSFamilyReportHistoryRepository
            .findById(lLINSFamilyReportHistory.getId())
            .get();
        // Disconnect from session so that the updates on updatedLLINSFamilyReportHistory are not directly saved in db
        em.detach(updatedLLINSFamilyReportHistory);
        updatedLLINSFamilyReportHistory
            .uid(UPDATED_UID)
            .created(UPDATED_CREATED)
            .lastUpdated(UPDATED_LAST_UPDATED)
            .documentNo(UPDATED_DOCUMENT_NO)
            .maleIndividuals(UPDATED_MALE_INDIVIDUALS)
            .femaleIndividuals(UPDATED_FEMALE_INDIVIDUALS)
            .lessThan5Males(UPDATED_LESS_THAN_5_MALES)
            .lessThan5Females(UPDATED_LESS_THAN_5_FEMALES)
            .pregnantWomen(UPDATED_PREGNANT_WOMEN)
            .quantityReceived(UPDATED_QUANTITY_RECEIVED)
            .familyType(UPDATED_FAMILY_TYPE);

        restLLINSFamilyReportHistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedLLINSFamilyReportHistory.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedLLINSFamilyReportHistory))
            )
            .andExpect(status().isOk());

        // Validate the LLINSFamilyReportHistory in the database
        List<LLINSFamilyReportHistory> lLINSFamilyReportHistoryList = lLINSFamilyReportHistoryRepository.findAll();
        assertThat(lLINSFamilyReportHistoryList).hasSize(databaseSizeBeforeUpdate);
        LLINSFamilyReportHistory testLLINSFamilyReportHistory = lLINSFamilyReportHistoryList.get(lLINSFamilyReportHistoryList.size() - 1);
        assertThat(testLLINSFamilyReportHistory.getUid()).isEqualTo(UPDATED_UID);
        assertThat(testLLINSFamilyReportHistory.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testLLINSFamilyReportHistory.getLastUpdated()).isEqualTo(UPDATED_LAST_UPDATED);
        assertThat(testLLINSFamilyReportHistory.getDocumentNo()).isEqualTo(UPDATED_DOCUMENT_NO);
        assertThat(testLLINSFamilyReportHistory.getMaleIndividuals()).isEqualTo(UPDATED_MALE_INDIVIDUALS);
        assertThat(testLLINSFamilyReportHistory.getFemaleIndividuals()).isEqualTo(UPDATED_FEMALE_INDIVIDUALS);
        assertThat(testLLINSFamilyReportHistory.getLessThan5Males()).isEqualTo(UPDATED_LESS_THAN_5_MALES);
        assertThat(testLLINSFamilyReportHistory.getLessThan5Females()).isEqualTo(UPDATED_LESS_THAN_5_FEMALES);
        assertThat(testLLINSFamilyReportHistory.getPregnantWomen()).isEqualTo(UPDATED_PREGNANT_WOMEN);
        assertThat(testLLINSFamilyReportHistory.getQuantityReceived()).isEqualTo(UPDATED_QUANTITY_RECEIVED);
        assertThat(testLLINSFamilyReportHistory.getFamilyType()).isEqualTo(UPDATED_FAMILY_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingLLINSFamilyReportHistory() throws Exception {
        int databaseSizeBeforeUpdate = lLINSFamilyReportHistoryRepository.findAll().size();
        lLINSFamilyReportHistory.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLLINSFamilyReportHistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, lLINSFamilyReportHistory.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(lLINSFamilyReportHistory))
            )
            .andExpect(status().isBadRequest());

        // Validate the LLINSFamilyReportHistory in the database
        List<LLINSFamilyReportHistory> lLINSFamilyReportHistoryList = lLINSFamilyReportHistoryRepository.findAll();
        assertThat(lLINSFamilyReportHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLLINSFamilyReportHistory() throws Exception {
        int databaseSizeBeforeUpdate = lLINSFamilyReportHistoryRepository.findAll().size();
        lLINSFamilyReportHistory.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLLINSFamilyReportHistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(lLINSFamilyReportHistory))
            )
            .andExpect(status().isBadRequest());

        // Validate the LLINSFamilyReportHistory in the database
        List<LLINSFamilyReportHistory> lLINSFamilyReportHistoryList = lLINSFamilyReportHistoryRepository.findAll();
        assertThat(lLINSFamilyReportHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLLINSFamilyReportHistory() throws Exception {
        int databaseSizeBeforeUpdate = lLINSFamilyReportHistoryRepository.findAll().size();
        lLINSFamilyReportHistory.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLLINSFamilyReportHistoryMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(lLINSFamilyReportHistory))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LLINSFamilyReportHistory in the database
        List<LLINSFamilyReportHistory> lLINSFamilyReportHistoryList = lLINSFamilyReportHistoryRepository.findAll();
        assertThat(lLINSFamilyReportHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLLINSFamilyReportHistoryWithPatch() throws Exception {
        // Initialize the database
        lLINSFamilyReportHistoryRepository.saveAndFlush(lLINSFamilyReportHistory);

        int databaseSizeBeforeUpdate = lLINSFamilyReportHistoryRepository.findAll().size();

        // Update the lLINSFamilyReportHistory using partial update
        LLINSFamilyReportHistory partialUpdatedLLINSFamilyReportHistory = new LLINSFamilyReportHistory();
        partialUpdatedLLINSFamilyReportHistory.setId(lLINSFamilyReportHistory.getId());

        partialUpdatedLLINSFamilyReportHistory
            .uid(UPDATED_UID)
            .lastUpdated(UPDATED_LAST_UPDATED)
            .maleIndividuals(UPDATED_MALE_INDIVIDUALS)
            .femaleIndividuals(UPDATED_FEMALE_INDIVIDUALS)
            .lessThan5Males(UPDATED_LESS_THAN_5_MALES)
            .lessThan5Females(UPDATED_LESS_THAN_5_FEMALES)
            .familyType(UPDATED_FAMILY_TYPE);

        restLLINSFamilyReportHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLLINSFamilyReportHistory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLLINSFamilyReportHistory))
            )
            .andExpect(status().isOk());

        // Validate the LLINSFamilyReportHistory in the database
        List<LLINSFamilyReportHistory> lLINSFamilyReportHistoryList = lLINSFamilyReportHistoryRepository.findAll();
        assertThat(lLINSFamilyReportHistoryList).hasSize(databaseSizeBeforeUpdate);
        LLINSFamilyReportHistory testLLINSFamilyReportHistory = lLINSFamilyReportHistoryList.get(lLINSFamilyReportHistoryList.size() - 1);
        assertThat(testLLINSFamilyReportHistory.getUid()).isEqualTo(UPDATED_UID);
        assertThat(testLLINSFamilyReportHistory.getCreated()).isEqualTo(DEFAULT_CREATED);
        assertThat(testLLINSFamilyReportHistory.getLastUpdated()).isEqualTo(UPDATED_LAST_UPDATED);
        assertThat(testLLINSFamilyReportHistory.getDocumentNo()).isEqualTo(DEFAULT_DOCUMENT_NO);
        assertThat(testLLINSFamilyReportHistory.getMaleIndividuals()).isEqualTo(UPDATED_MALE_INDIVIDUALS);
        assertThat(testLLINSFamilyReportHistory.getFemaleIndividuals()).isEqualTo(UPDATED_FEMALE_INDIVIDUALS);
        assertThat(testLLINSFamilyReportHistory.getLessThan5Males()).isEqualTo(UPDATED_LESS_THAN_5_MALES);
        assertThat(testLLINSFamilyReportHistory.getLessThan5Females()).isEqualTo(UPDATED_LESS_THAN_5_FEMALES);
        assertThat(testLLINSFamilyReportHistory.getPregnantWomen()).isEqualTo(DEFAULT_PREGNANT_WOMEN);
        assertThat(testLLINSFamilyReportHistory.getQuantityReceived()).isEqualTo(DEFAULT_QUANTITY_RECEIVED);
        assertThat(testLLINSFamilyReportHistory.getFamilyType()).isEqualTo(UPDATED_FAMILY_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateLLINSFamilyReportHistoryWithPatch() throws Exception {
        // Initialize the database
        lLINSFamilyReportHistoryRepository.saveAndFlush(lLINSFamilyReportHistory);

        int databaseSizeBeforeUpdate = lLINSFamilyReportHistoryRepository.findAll().size();

        // Update the lLINSFamilyReportHistory using partial update
        LLINSFamilyReportHistory partialUpdatedLLINSFamilyReportHistory = new LLINSFamilyReportHistory();
        partialUpdatedLLINSFamilyReportHistory.setId(lLINSFamilyReportHistory.getId());

        partialUpdatedLLINSFamilyReportHistory
            .uid(UPDATED_UID)
            .created(UPDATED_CREATED)
            .lastUpdated(UPDATED_LAST_UPDATED)
            .documentNo(UPDATED_DOCUMENT_NO)
            .maleIndividuals(UPDATED_MALE_INDIVIDUALS)
            .femaleIndividuals(UPDATED_FEMALE_INDIVIDUALS)
            .lessThan5Males(UPDATED_LESS_THAN_5_MALES)
            .lessThan5Females(UPDATED_LESS_THAN_5_FEMALES)
            .pregnantWomen(UPDATED_PREGNANT_WOMEN)
            .quantityReceived(UPDATED_QUANTITY_RECEIVED)
            .familyType(UPDATED_FAMILY_TYPE);

        restLLINSFamilyReportHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLLINSFamilyReportHistory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLLINSFamilyReportHistory))
            )
            .andExpect(status().isOk());

        // Validate the LLINSFamilyReportHistory in the database
        List<LLINSFamilyReportHistory> lLINSFamilyReportHistoryList = lLINSFamilyReportHistoryRepository.findAll();
        assertThat(lLINSFamilyReportHistoryList).hasSize(databaseSizeBeforeUpdate);
        LLINSFamilyReportHistory testLLINSFamilyReportHistory = lLINSFamilyReportHistoryList.get(lLINSFamilyReportHistoryList.size() - 1);
        assertThat(testLLINSFamilyReportHistory.getUid()).isEqualTo(UPDATED_UID);
        assertThat(testLLINSFamilyReportHistory.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testLLINSFamilyReportHistory.getLastUpdated()).isEqualTo(UPDATED_LAST_UPDATED);
        assertThat(testLLINSFamilyReportHistory.getDocumentNo()).isEqualTo(UPDATED_DOCUMENT_NO);
        assertThat(testLLINSFamilyReportHistory.getMaleIndividuals()).isEqualTo(UPDATED_MALE_INDIVIDUALS);
        assertThat(testLLINSFamilyReportHistory.getFemaleIndividuals()).isEqualTo(UPDATED_FEMALE_INDIVIDUALS);
        assertThat(testLLINSFamilyReportHistory.getLessThan5Males()).isEqualTo(UPDATED_LESS_THAN_5_MALES);
        assertThat(testLLINSFamilyReportHistory.getLessThan5Females()).isEqualTo(UPDATED_LESS_THAN_5_FEMALES);
        assertThat(testLLINSFamilyReportHistory.getPregnantWomen()).isEqualTo(UPDATED_PREGNANT_WOMEN);
        assertThat(testLLINSFamilyReportHistory.getQuantityReceived()).isEqualTo(UPDATED_QUANTITY_RECEIVED);
        assertThat(testLLINSFamilyReportHistory.getFamilyType()).isEqualTo(UPDATED_FAMILY_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingLLINSFamilyReportHistory() throws Exception {
        int databaseSizeBeforeUpdate = lLINSFamilyReportHistoryRepository.findAll().size();
        lLINSFamilyReportHistory.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLLINSFamilyReportHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, lLINSFamilyReportHistory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(lLINSFamilyReportHistory))
            )
            .andExpect(status().isBadRequest());

        // Validate the LLINSFamilyReportHistory in the database
        List<LLINSFamilyReportHistory> lLINSFamilyReportHistoryList = lLINSFamilyReportHistoryRepository.findAll();
        assertThat(lLINSFamilyReportHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLLINSFamilyReportHistory() throws Exception {
        int databaseSizeBeforeUpdate = lLINSFamilyReportHistoryRepository.findAll().size();
        lLINSFamilyReportHistory.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLLINSFamilyReportHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(lLINSFamilyReportHistory))
            )
            .andExpect(status().isBadRequest());

        // Validate the LLINSFamilyReportHistory in the database
        List<LLINSFamilyReportHistory> lLINSFamilyReportHistoryList = lLINSFamilyReportHistoryRepository.findAll();
        assertThat(lLINSFamilyReportHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLLINSFamilyReportHistory() throws Exception {
        int databaseSizeBeforeUpdate = lLINSFamilyReportHistoryRepository.findAll().size();
        lLINSFamilyReportHistory.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLLINSFamilyReportHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(lLINSFamilyReportHistory))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LLINSFamilyReportHistory in the database
        List<LLINSFamilyReportHistory> lLINSFamilyReportHistoryList = lLINSFamilyReportHistoryRepository.findAll();
        assertThat(lLINSFamilyReportHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLLINSFamilyReportHistory() throws Exception {
        // Initialize the database
        lLINSFamilyReportHistoryRepository.saveAndFlush(lLINSFamilyReportHistory);

        int databaseSizeBeforeDelete = lLINSFamilyReportHistoryRepository.findAll().size();

        // Delete the lLINSFamilyReportHistory
        restLLINSFamilyReportHistoryMockMvc
            .perform(delete(ENTITY_API_URL_ID, lLINSFamilyReportHistory.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<LLINSFamilyReportHistory> lLINSFamilyReportHistoryList = lLINSFamilyReportHistoryRepository.findAll();
        assertThat(lLINSFamilyReportHistoryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
