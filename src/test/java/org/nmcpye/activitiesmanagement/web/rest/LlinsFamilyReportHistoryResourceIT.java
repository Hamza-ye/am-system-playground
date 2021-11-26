package org.nmcpye.activitiesmanagement.web.rest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.nmcpye.activitiesmanagement.IntegrationTest;
import org.nmcpye.activitiesmanagement.domain.LlinsFamilyReport;
import org.nmcpye.activitiesmanagement.domain.LlinsFamilyReportHistory;
import org.nmcpye.activitiesmanagement.domain.WorkingDay;
import org.nmcpye.activitiesmanagement.domain.enumeration.FamilyType;
import org.nmcpye.activitiesmanagement.repository.LlinsFamilyReportHistoryRepository;
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
 * Integration tests for the {@link LlinsFamilyReportHistoryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class LlinsFamilyReportHistoryResourceIT {

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
    private LlinsFamilyReportHistoryRepository llinsFamilyReportHistoryRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLlinsFamilyReportHistoryMockMvc;

    private LlinsFamilyReportHistory llinsFamilyReportHistory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LlinsFamilyReportHistory createEntity(EntityManager em) {
        LlinsFamilyReportHistory llinsFamilyReportHistory = new LlinsFamilyReportHistory()
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
        llinsFamilyReportHistory.setDayReached(workingDay);
        // Add required entity
        LlinsFamilyReport llinsFamilyReport;
        if (TestUtil.findAll(em, LlinsFamilyReport.class).isEmpty()) {
            llinsFamilyReport = LlinsFamilyReportResourceIT.createEntity(em);
            em.persist(llinsFamilyReport);
            em.flush();
        } else {
            llinsFamilyReport = TestUtil.findAll(em, LlinsFamilyReport.class).get(0);
        }
        llinsFamilyReportHistory.setLlinsFamilyReport(llinsFamilyReport);
        return llinsFamilyReportHistory;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LlinsFamilyReportHistory createUpdatedEntity(EntityManager em) {
        LlinsFamilyReportHistory llinsFamilyReportHistory = new LlinsFamilyReportHistory()
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
        llinsFamilyReportHistory.setDayReached(workingDay);
        // Add required entity
        LlinsFamilyReport llinsFamilyReport;
        if (TestUtil.findAll(em, LlinsFamilyReport.class).isEmpty()) {
            llinsFamilyReport = LlinsFamilyReportResourceIT.createUpdatedEntity(em);
            em.persist(llinsFamilyReport);
            em.flush();
        } else {
            llinsFamilyReport = TestUtil.findAll(em, LlinsFamilyReport.class).get(0);
        }
        llinsFamilyReportHistory.setLlinsFamilyReport(llinsFamilyReport);
        return llinsFamilyReportHistory;
    }

    @BeforeEach
    public void initTest() {
        llinsFamilyReportHistory = createEntity(em);
    }

    @Test
    @Transactional
    void createLlinsFamilyReportHistory() throws Exception {
        int databaseSizeBeforeCreate = llinsFamilyReportHistoryRepository.findAll().size();
        // Create the LlinsFamilyReportHistory
        restLlinsFamilyReportHistoryMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(llinsFamilyReportHistory))
            )
            .andExpect(status().isCreated());

        // Validate the LlinsFamilyReportHistory in the database
        List<LlinsFamilyReportHistory> llinsFamilyReportHistoryList = llinsFamilyReportHistoryRepository.findAll();
        assertThat(llinsFamilyReportHistoryList).hasSize(databaseSizeBeforeCreate + 1);
        LlinsFamilyReportHistory testLlinsFamilyReportHistory = llinsFamilyReportHistoryList.get(llinsFamilyReportHistoryList.size() - 1);
        assertThat(testLlinsFamilyReportHistory.getUid()).isEqualTo(DEFAULT_UID);
        assertThat(testLlinsFamilyReportHistory.getCreated()).isEqualTo(DEFAULT_CREATED);
        assertThat(testLlinsFamilyReportHistory.getLastUpdated()).isEqualTo(DEFAULT_LAST_UPDATED);
        assertThat(testLlinsFamilyReportHistory.getDocumentNo()).isEqualTo(DEFAULT_DOCUMENT_NO);
        assertThat(testLlinsFamilyReportHistory.getMaleIndividuals()).isEqualTo(DEFAULT_MALE_INDIVIDUALS);
        assertThat(testLlinsFamilyReportHistory.getFemaleIndividuals()).isEqualTo(DEFAULT_FEMALE_INDIVIDUALS);
        assertThat(testLlinsFamilyReportHistory.getLessThan5Males()).isEqualTo(DEFAULT_LESS_THAN_5_MALES);
        assertThat(testLlinsFamilyReportHistory.getLessThan5Females()).isEqualTo(DEFAULT_LESS_THAN_5_FEMALES);
        assertThat(testLlinsFamilyReportHistory.getPregnantWomen()).isEqualTo(DEFAULT_PREGNANT_WOMEN);
        assertThat(testLlinsFamilyReportHistory.getQuantityReceived()).isEqualTo(DEFAULT_QUANTITY_RECEIVED);
        assertThat(testLlinsFamilyReportHistory.getFamilyType()).isEqualTo(DEFAULT_FAMILY_TYPE);
    }

    @Test
    @Transactional
    void createLlinsFamilyReportHistoryWithExistingId() throws Exception {
        // Create the LlinsFamilyReportHistory with an existing ID
        llinsFamilyReportHistory.setId(1L);

        int databaseSizeBeforeCreate = llinsFamilyReportHistoryRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLlinsFamilyReportHistoryMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(llinsFamilyReportHistory))
            )
            .andExpect(status().isBadRequest());

        // Validate the LlinsFamilyReportHistory in the database
        List<LlinsFamilyReportHistory> llinsFamilyReportHistoryList = llinsFamilyReportHistoryRepository.findAll();
        assertThat(llinsFamilyReportHistoryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkUidIsRequired() throws Exception {
        int databaseSizeBeforeTest = llinsFamilyReportHistoryRepository.findAll().size();
        // set the field null
        llinsFamilyReportHistory.setUid(null);

        // Create the LlinsFamilyReportHistory, which fails.

        restLlinsFamilyReportHistoryMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(llinsFamilyReportHistory))
            )
            .andExpect(status().isBadRequest());

        List<LlinsFamilyReportHistory> llinsFamilyReportHistoryList = llinsFamilyReportHistoryRepository.findAll();
        assertThat(llinsFamilyReportHistoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFamilyTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = llinsFamilyReportHistoryRepository.findAll().size();
        // set the field null
        llinsFamilyReportHistory.setFamilyType(null);

        // Create the LlinsFamilyReportHistory, which fails.

        restLlinsFamilyReportHistoryMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(llinsFamilyReportHistory))
            )
            .andExpect(status().isBadRequest());

        List<LlinsFamilyReportHistory> llinsFamilyReportHistoryList = llinsFamilyReportHistoryRepository.findAll();
        assertThat(llinsFamilyReportHistoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllLlinsFamilyReportHistories() throws Exception {
        // Initialize the database
        llinsFamilyReportHistoryRepository.saveAndFlush(llinsFamilyReportHistory);

        // Get all the llinsFamilyReportHistoryList
        restLlinsFamilyReportHistoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(llinsFamilyReportHistory.getId().intValue())))
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
    void getLlinsFamilyReportHistory() throws Exception {
        // Initialize the database
        llinsFamilyReportHistoryRepository.saveAndFlush(llinsFamilyReportHistory);

        // Get the llinsFamilyReportHistory
        restLlinsFamilyReportHistoryMockMvc
            .perform(get(ENTITY_API_URL_ID, llinsFamilyReportHistory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(llinsFamilyReportHistory.getId().intValue()))
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
    void getNonExistingLlinsFamilyReportHistory() throws Exception {
        // Get the llinsFamilyReportHistory
        restLlinsFamilyReportHistoryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewLlinsFamilyReportHistory() throws Exception {
        // Initialize the database
        llinsFamilyReportHistoryRepository.saveAndFlush(llinsFamilyReportHistory);

        int databaseSizeBeforeUpdate = llinsFamilyReportHistoryRepository.findAll().size();

        // Update the llinsFamilyReportHistory
        LlinsFamilyReportHistory updatedLlinsFamilyReportHistory = llinsFamilyReportHistoryRepository
            .findById(llinsFamilyReportHistory.getId())
            .get();
        // Disconnect from session so that the updates on updatedLlinsFamilyReportHistory are not directly saved in db
        em.detach(updatedLlinsFamilyReportHistory);
        updatedLlinsFamilyReportHistory
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

        restLlinsFamilyReportHistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedLlinsFamilyReportHistory.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedLlinsFamilyReportHistory))
            )
            .andExpect(status().isOk());

        // Validate the LlinsFamilyReportHistory in the database
        List<LlinsFamilyReportHistory> llinsFamilyReportHistoryList = llinsFamilyReportHistoryRepository.findAll();
        assertThat(llinsFamilyReportHistoryList).hasSize(databaseSizeBeforeUpdate);
        LlinsFamilyReportHistory testLlinsFamilyReportHistory = llinsFamilyReportHistoryList.get(llinsFamilyReportHistoryList.size() - 1);
        assertThat(testLlinsFamilyReportHistory.getUid()).isEqualTo(UPDATED_UID);
        assertThat(testLlinsFamilyReportHistory.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testLlinsFamilyReportHistory.getLastUpdated()).isEqualTo(UPDATED_LAST_UPDATED);
        assertThat(testLlinsFamilyReportHistory.getDocumentNo()).isEqualTo(UPDATED_DOCUMENT_NO);
        assertThat(testLlinsFamilyReportHistory.getMaleIndividuals()).isEqualTo(UPDATED_MALE_INDIVIDUALS);
        assertThat(testLlinsFamilyReportHistory.getFemaleIndividuals()).isEqualTo(UPDATED_FEMALE_INDIVIDUALS);
        assertThat(testLlinsFamilyReportHistory.getLessThan5Males()).isEqualTo(UPDATED_LESS_THAN_5_MALES);
        assertThat(testLlinsFamilyReportHistory.getLessThan5Females()).isEqualTo(UPDATED_LESS_THAN_5_FEMALES);
        assertThat(testLlinsFamilyReportHistory.getPregnantWomen()).isEqualTo(UPDATED_PREGNANT_WOMEN);
        assertThat(testLlinsFamilyReportHistory.getQuantityReceived()).isEqualTo(UPDATED_QUANTITY_RECEIVED);
        assertThat(testLlinsFamilyReportHistory.getFamilyType()).isEqualTo(UPDATED_FAMILY_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingLlinsFamilyReportHistory() throws Exception {
        int databaseSizeBeforeUpdate = llinsFamilyReportHistoryRepository.findAll().size();
        llinsFamilyReportHistory.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLlinsFamilyReportHistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, llinsFamilyReportHistory.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(llinsFamilyReportHistory))
            )
            .andExpect(status().isBadRequest());

        // Validate the LlinsFamilyReportHistory in the database
        List<LlinsFamilyReportHistory> llinsFamilyReportHistoryList = llinsFamilyReportHistoryRepository.findAll();
        assertThat(llinsFamilyReportHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLlinsFamilyReportHistory() throws Exception {
        int databaseSizeBeforeUpdate = llinsFamilyReportHistoryRepository.findAll().size();
        llinsFamilyReportHistory.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLlinsFamilyReportHistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(llinsFamilyReportHistory))
            )
            .andExpect(status().isBadRequest());

        // Validate the LlinsFamilyReportHistory in the database
        List<LlinsFamilyReportHistory> llinsFamilyReportHistoryList = llinsFamilyReportHistoryRepository.findAll();
        assertThat(llinsFamilyReportHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLlinsFamilyReportHistory() throws Exception {
        int databaseSizeBeforeUpdate = llinsFamilyReportHistoryRepository.findAll().size();
        llinsFamilyReportHistory.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLlinsFamilyReportHistoryMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(llinsFamilyReportHistory))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LlinsFamilyReportHistory in the database
        List<LlinsFamilyReportHistory> llinsFamilyReportHistoryList = llinsFamilyReportHistoryRepository.findAll();
        assertThat(llinsFamilyReportHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLlinsFamilyReportHistoryWithPatch() throws Exception {
        // Initialize the database
        llinsFamilyReportHistoryRepository.saveAndFlush(llinsFamilyReportHistory);

        int databaseSizeBeforeUpdate = llinsFamilyReportHistoryRepository.findAll().size();

        // Update the llinsFamilyReportHistory using partial update
        LlinsFamilyReportHistory partialUpdatedLlinsFamilyReportHistory = new LlinsFamilyReportHistory();
        partialUpdatedLlinsFamilyReportHistory.setId(llinsFamilyReportHistory.getId());

        partialUpdatedLlinsFamilyReportHistory
            .uid(UPDATED_UID)
            .created(UPDATED_CREATED)
            .documentNo(UPDATED_DOCUMENT_NO)
            .maleIndividuals(UPDATED_MALE_INDIVIDUALS)
            .femaleIndividuals(UPDATED_FEMALE_INDIVIDUALS)
            .pregnantWomen(UPDATED_PREGNANT_WOMEN);

        restLlinsFamilyReportHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLlinsFamilyReportHistory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLlinsFamilyReportHistory))
            )
            .andExpect(status().isOk());

        // Validate the LlinsFamilyReportHistory in the database
        List<LlinsFamilyReportHistory> llinsFamilyReportHistoryList = llinsFamilyReportHistoryRepository.findAll();
        assertThat(llinsFamilyReportHistoryList).hasSize(databaseSizeBeforeUpdate);
        LlinsFamilyReportHistory testLlinsFamilyReportHistory = llinsFamilyReportHistoryList.get(llinsFamilyReportHistoryList.size() - 1);
        assertThat(testLlinsFamilyReportHistory.getUid()).isEqualTo(UPDATED_UID);
        assertThat(testLlinsFamilyReportHistory.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testLlinsFamilyReportHistory.getLastUpdated()).isEqualTo(DEFAULT_LAST_UPDATED);
        assertThat(testLlinsFamilyReportHistory.getDocumentNo()).isEqualTo(UPDATED_DOCUMENT_NO);
        assertThat(testLlinsFamilyReportHistory.getMaleIndividuals()).isEqualTo(UPDATED_MALE_INDIVIDUALS);
        assertThat(testLlinsFamilyReportHistory.getFemaleIndividuals()).isEqualTo(UPDATED_FEMALE_INDIVIDUALS);
        assertThat(testLlinsFamilyReportHistory.getLessThan5Males()).isEqualTo(DEFAULT_LESS_THAN_5_MALES);
        assertThat(testLlinsFamilyReportHistory.getLessThan5Females()).isEqualTo(DEFAULT_LESS_THAN_5_FEMALES);
        assertThat(testLlinsFamilyReportHistory.getPregnantWomen()).isEqualTo(UPDATED_PREGNANT_WOMEN);
        assertThat(testLlinsFamilyReportHistory.getQuantityReceived()).isEqualTo(DEFAULT_QUANTITY_RECEIVED);
        assertThat(testLlinsFamilyReportHistory.getFamilyType()).isEqualTo(DEFAULT_FAMILY_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateLlinsFamilyReportHistoryWithPatch() throws Exception {
        // Initialize the database
        llinsFamilyReportHistoryRepository.saveAndFlush(llinsFamilyReportHistory);

        int databaseSizeBeforeUpdate = llinsFamilyReportHistoryRepository.findAll().size();

        // Update the llinsFamilyReportHistory using partial update
        LlinsFamilyReportHistory partialUpdatedLlinsFamilyReportHistory = new LlinsFamilyReportHistory();
        partialUpdatedLlinsFamilyReportHistory.setId(llinsFamilyReportHistory.getId());

        partialUpdatedLlinsFamilyReportHistory
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

        restLlinsFamilyReportHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLlinsFamilyReportHistory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLlinsFamilyReportHistory))
            )
            .andExpect(status().isOk());

        // Validate the LlinsFamilyReportHistory in the database
        List<LlinsFamilyReportHistory> llinsFamilyReportHistoryList = llinsFamilyReportHistoryRepository.findAll();
        assertThat(llinsFamilyReportHistoryList).hasSize(databaseSizeBeforeUpdate);
        LlinsFamilyReportHistory testLlinsFamilyReportHistory = llinsFamilyReportHistoryList.get(llinsFamilyReportHistoryList.size() - 1);
        assertThat(testLlinsFamilyReportHistory.getUid()).isEqualTo(UPDATED_UID);
        assertThat(testLlinsFamilyReportHistory.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testLlinsFamilyReportHistory.getLastUpdated()).isEqualTo(UPDATED_LAST_UPDATED);
        assertThat(testLlinsFamilyReportHistory.getDocumentNo()).isEqualTo(UPDATED_DOCUMENT_NO);
        assertThat(testLlinsFamilyReportHistory.getMaleIndividuals()).isEqualTo(UPDATED_MALE_INDIVIDUALS);
        assertThat(testLlinsFamilyReportHistory.getFemaleIndividuals()).isEqualTo(UPDATED_FEMALE_INDIVIDUALS);
        assertThat(testLlinsFamilyReportHistory.getLessThan5Males()).isEqualTo(UPDATED_LESS_THAN_5_MALES);
        assertThat(testLlinsFamilyReportHistory.getLessThan5Females()).isEqualTo(UPDATED_LESS_THAN_5_FEMALES);
        assertThat(testLlinsFamilyReportHistory.getPregnantWomen()).isEqualTo(UPDATED_PREGNANT_WOMEN);
        assertThat(testLlinsFamilyReportHistory.getQuantityReceived()).isEqualTo(UPDATED_QUANTITY_RECEIVED);
        assertThat(testLlinsFamilyReportHistory.getFamilyType()).isEqualTo(UPDATED_FAMILY_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingLlinsFamilyReportHistory() throws Exception {
        int databaseSizeBeforeUpdate = llinsFamilyReportHistoryRepository.findAll().size();
        llinsFamilyReportHistory.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLlinsFamilyReportHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, llinsFamilyReportHistory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(llinsFamilyReportHistory))
            )
            .andExpect(status().isBadRequest());

        // Validate the LlinsFamilyReportHistory in the database
        List<LlinsFamilyReportHistory> llinsFamilyReportHistoryList = llinsFamilyReportHistoryRepository.findAll();
        assertThat(llinsFamilyReportHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLlinsFamilyReportHistory() throws Exception {
        int databaseSizeBeforeUpdate = llinsFamilyReportHistoryRepository.findAll().size();
        llinsFamilyReportHistory.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLlinsFamilyReportHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(llinsFamilyReportHistory))
            )
            .andExpect(status().isBadRequest());

        // Validate the LlinsFamilyReportHistory in the database
        List<LlinsFamilyReportHistory> llinsFamilyReportHistoryList = llinsFamilyReportHistoryRepository.findAll();
        assertThat(llinsFamilyReportHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLlinsFamilyReportHistory() throws Exception {
        int databaseSizeBeforeUpdate = llinsFamilyReportHistoryRepository.findAll().size();
        llinsFamilyReportHistory.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLlinsFamilyReportHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(llinsFamilyReportHistory))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LlinsFamilyReportHistory in the database
        List<LlinsFamilyReportHistory> llinsFamilyReportHistoryList = llinsFamilyReportHistoryRepository.findAll();
        assertThat(llinsFamilyReportHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLlinsFamilyReportHistory() throws Exception {
        // Initialize the database
        llinsFamilyReportHistoryRepository.saveAndFlush(llinsFamilyReportHistory);

        int databaseSizeBeforeDelete = llinsFamilyReportHistoryRepository.findAll().size();

        // Delete the llinsFamilyReportHistory
        restLlinsFamilyReportHistoryMockMvc
            .perform(delete(ENTITY_API_URL_ID, llinsFamilyReportHistory.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<LlinsFamilyReportHistory> llinsFamilyReportHistoryList = llinsFamilyReportHistoryRepository.findAll();
        assertThat(llinsFamilyReportHistoryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
