package org.nmcpye.activitiesmanagement.web.rest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.nmcpye.activitiesmanagement.IntegrationTest;
import org.nmcpye.activitiesmanagement.domain.ChvMalariaCaseReport;
import org.nmcpye.activitiesmanagement.domain.chv.Chv;
import org.nmcpye.activitiesmanagement.domain.dataset.CasesReportClass;
import org.nmcpye.activitiesmanagement.domain.enumeration.Gender;
import org.nmcpye.activitiesmanagement.domain.enumeration.MalariaTestResult;
import org.nmcpye.activitiesmanagement.repository.ChvMalariaCaseReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link ChvMalariaCaseReportResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ChvMalariaCaseReportResourceIT {

    private static final String DEFAULT_UID = "AAAAAAAAAA";
    private static final String UPDATED_UID = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_LAST_UPDATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_UPDATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_INDIVIDUAL_NAME = "AAAAAAAAAA";
    private static final String UPDATED_INDIVIDUAL_NAME = "BBBBBBBBBB";

    private static final Gender DEFAULT_GENDER = Gender.MALE;
    private static final Gender UPDATED_GENDER = Gender.FEMALE;

    private static final Boolean DEFAULT_IS_PREGNANT = false;
    private static final Boolean UPDATED_IS_PREGNANT = true;

    private static final MalariaTestResult DEFAULT_MALARIA_TEST_RESULT = MalariaTestResult.POSITIVE;
    private static final MalariaTestResult UPDATED_MALARIA_TEST_RESULT = MalariaTestResult.NEGATIVE;

    private static final Integer DEFAULT_DRUGS_GIVEN = 1;
    private static final Integer UPDATED_DRUGS_GIVEN = 2;

    private static final Integer DEFAULT_SUPPS_GIVEN = 1;
    private static final Integer UPDATED_SUPPS_GIVEN = 2;

    private static final Boolean DEFAULT_REFERRAL = false;
    private static final Boolean UPDATED_REFERRAL = true;

    private static final String DEFAULT_BAR_IMAGE_URL = "AAAAAAAAAA";
    private static final String UPDATED_BAR_IMAGE_URL = "BBBBBBBBBB";

    private static final String DEFAULT_COMMENT = "AAAAAAAAAA";
    private static final String UPDATED_COMMENT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/chv-malaria-case-reports";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ChvMalariaCaseReportRepository chvMalariaCaseReportRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restChvMalariaCaseReportMockMvc;

    private ChvMalariaCaseReport chvMalariaCaseReport;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ChvMalariaCaseReport createEntity(EntityManager em) {
        ChvMalariaCaseReport chvMalariaCaseReport = new ChvMalariaCaseReport()
            .uid(DEFAULT_UID)
            .created(DEFAULT_CREATED)
            .lastUpdated(DEFAULT_LAST_UPDATED)
            .date(DEFAULT_DATE)
            .individualName(DEFAULT_INDIVIDUAL_NAME)
            .gender(DEFAULT_GENDER)
            .isPregnant(DEFAULT_IS_PREGNANT)
            .malariaTestResult(DEFAULT_MALARIA_TEST_RESULT)
            .drugsGiven(DEFAULT_DRUGS_GIVEN)
            .suppsGiven(DEFAULT_SUPPS_GIVEN)
            .referral(DEFAULT_REFERRAL)
            .barImageUrl(DEFAULT_BAR_IMAGE_URL)
            .comment(DEFAULT_COMMENT);
        // Add required entity
        Chv chv;
        if (TestUtil.findAll(em, Chv.class).isEmpty()) {
            chv = ChvResourceIT.createEntity(em);
            em.persist(chv);
            em.flush();
        } else {
            chv = TestUtil.findAll(em, Chv.class).get(0);
        }
        chvMalariaCaseReport.setChv(chv);
        // Add required entity
        CasesReportClass casesReportClass;
        if (TestUtil.findAll(em, CasesReportClass.class).isEmpty()) {
            casesReportClass = CasesReportClassResourceIT.createEntity(em);
            em.persist(casesReportClass);
            em.flush();
        } else {
            casesReportClass = TestUtil.findAll(em, CasesReportClass.class).get(0);
        }
        chvMalariaCaseReport.setReportClass(casesReportClass);
        return chvMalariaCaseReport;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ChvMalariaCaseReport createUpdatedEntity(EntityManager em) {
        ChvMalariaCaseReport chvMalariaCaseReport = new ChvMalariaCaseReport()
            .uid(UPDATED_UID)
            .created(UPDATED_CREATED)
            .lastUpdated(UPDATED_LAST_UPDATED)
            .date(UPDATED_DATE)
            .individualName(UPDATED_INDIVIDUAL_NAME)
            .gender(UPDATED_GENDER)
            .isPregnant(UPDATED_IS_PREGNANT)
            .malariaTestResult(UPDATED_MALARIA_TEST_RESULT)
            .drugsGiven(UPDATED_DRUGS_GIVEN)
            .suppsGiven(UPDATED_SUPPS_GIVEN)
            .referral(UPDATED_REFERRAL)
            .barImageUrl(UPDATED_BAR_IMAGE_URL)
            .comment(UPDATED_COMMENT);
        // Add required entity
        Chv chv;
        if (TestUtil.findAll(em, Chv.class).isEmpty()) {
            chv = ChvResourceIT.createUpdatedEntity(em);
            em.persist(chv);
            em.flush();
        } else {
            chv = TestUtil.findAll(em, Chv.class).get(0);
        }
        chvMalariaCaseReport.setChv(chv);
        // Add required entity
        CasesReportClass casesReportClass;
        if (TestUtil.findAll(em, CasesReportClass.class).isEmpty()) {
            casesReportClass = CasesReportClassResourceIT.createUpdatedEntity(em);
            em.persist(casesReportClass);
            em.flush();
        } else {
            casesReportClass = TestUtil.findAll(em, CasesReportClass.class).get(0);
        }
        chvMalariaCaseReport.setReportClass(casesReportClass);
        return chvMalariaCaseReport;
    }

    @BeforeEach
    public void initTest() {
        chvMalariaCaseReport = createEntity(em);
    }

    @Test
    @Transactional
    void createChvMalariaCaseReport() throws Exception {
        int databaseSizeBeforeCreate = chvMalariaCaseReportRepository.findAll().size();
        // Create the ChvMalariaCaseReport
        restChvMalariaCaseReportMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(chvMalariaCaseReport))
            )
            .andExpect(status().isCreated());

        // Validate the ChvMalariaCaseReport in the database
        List<ChvMalariaCaseReport> chvMalariaCaseReportList = chvMalariaCaseReportRepository.findAll();
        assertThat(chvMalariaCaseReportList).hasSize(databaseSizeBeforeCreate + 1);
        ChvMalariaCaseReport testChvMalariaCaseReport = chvMalariaCaseReportList.get(chvMalariaCaseReportList.size() - 1);
        assertThat(testChvMalariaCaseReport.getUid()).isEqualTo(DEFAULT_UID);
        assertThat(testChvMalariaCaseReport.getCreated()).isEqualTo(DEFAULT_CREATED);
        assertThat(testChvMalariaCaseReport.getLastUpdated()).isEqualTo(DEFAULT_LAST_UPDATED);
        assertThat(testChvMalariaCaseReport.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testChvMalariaCaseReport.getIndividualName()).isEqualTo(DEFAULT_INDIVIDUAL_NAME);
        assertThat(testChvMalariaCaseReport.getGender()).isEqualTo(DEFAULT_GENDER);
        assertThat(testChvMalariaCaseReport.getIsPregnant()).isEqualTo(DEFAULT_IS_PREGNANT);
        assertThat(testChvMalariaCaseReport.getMalariaTestResult()).isEqualTo(DEFAULT_MALARIA_TEST_RESULT);
        assertThat(testChvMalariaCaseReport.getDrugsGiven()).isEqualTo(DEFAULT_DRUGS_GIVEN);
        assertThat(testChvMalariaCaseReport.getSuppsGiven()).isEqualTo(DEFAULT_SUPPS_GIVEN);
        assertThat(testChvMalariaCaseReport.getReferral()).isEqualTo(DEFAULT_REFERRAL);
        assertThat(testChvMalariaCaseReport.getBarImageUrl()).isEqualTo(DEFAULT_BAR_IMAGE_URL);
        assertThat(testChvMalariaCaseReport.getComment()).isEqualTo(DEFAULT_COMMENT);
    }

    @Test
    @Transactional
    void createChvMalariaCaseReportWithExistingId() throws Exception {
        // Create the ChvMalariaCaseReport with an existing ID
        chvMalariaCaseReport.setId(1L);

        int databaseSizeBeforeCreate = chvMalariaCaseReportRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restChvMalariaCaseReportMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(chvMalariaCaseReport))
            )
            .andExpect(status().isBadRequest());

        // Validate the ChvMalariaCaseReport in the database
        List<ChvMalariaCaseReport> chvMalariaCaseReportList = chvMalariaCaseReportRepository.findAll();
        assertThat(chvMalariaCaseReportList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkUidIsRequired() throws Exception {
        int databaseSizeBeforeTest = chvMalariaCaseReportRepository.findAll().size();
        // set the field null
        chvMalariaCaseReport.setUid(null);

        // Create the ChvMalariaCaseReport, which fails.

        restChvMalariaCaseReportMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(chvMalariaCaseReport))
            )
            .andExpect(status().isBadRequest());

        List<ChvMalariaCaseReport> chvMalariaCaseReportList = chvMalariaCaseReportRepository.findAll();
        assertThat(chvMalariaCaseReportList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllChvMalariaCaseReports() throws Exception {
        // Initialize the database
        chvMalariaCaseReportRepository.saveAndFlush(chvMalariaCaseReport);

        // Get all the chvMalariaCaseReportList
        restChvMalariaCaseReportMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(chvMalariaCaseReport.getId().intValue())))
            .andExpect(jsonPath("$.[*].uid").value(hasItem(DEFAULT_UID)))
            .andExpect(jsonPath("$.[*].created").value(hasItem(DEFAULT_CREATED.toString())))
            .andExpect(jsonPath("$.[*].lastUpdated").value(hasItem(DEFAULT_LAST_UPDATED.toString())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].individualName").value(hasItem(DEFAULT_INDIVIDUAL_NAME)))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER.toString())))
            .andExpect(jsonPath("$.[*].isPregnant").value(hasItem(DEFAULT_IS_PREGNANT.booleanValue())))
            .andExpect(jsonPath("$.[*].malariaTestResult").value(hasItem(DEFAULT_MALARIA_TEST_RESULT.toString())))
            .andExpect(jsonPath("$.[*].drugsGiven").value(hasItem(DEFAULT_DRUGS_GIVEN)))
            .andExpect(jsonPath("$.[*].suppsGiven").value(hasItem(DEFAULT_SUPPS_GIVEN)))
            .andExpect(jsonPath("$.[*].referral").value(hasItem(DEFAULT_REFERRAL.booleanValue())))
            .andExpect(jsonPath("$.[*].barImageUrl").value(hasItem(DEFAULT_BAR_IMAGE_URL)))
            .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT)));
    }

    @Test
    @Transactional
    void getChvMalariaCaseReport() throws Exception {
        // Initialize the database
        chvMalariaCaseReportRepository.saveAndFlush(chvMalariaCaseReport);

        // Get the chvMalariaCaseReport
        restChvMalariaCaseReportMockMvc
            .perform(get(ENTITY_API_URL_ID, chvMalariaCaseReport.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(chvMalariaCaseReport.getId().intValue()))
            .andExpect(jsonPath("$.uid").value(DEFAULT_UID))
            .andExpect(jsonPath("$.created").value(DEFAULT_CREATED.toString()))
            .andExpect(jsonPath("$.lastUpdated").value(DEFAULT_LAST_UPDATED.toString()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.individualName").value(DEFAULT_INDIVIDUAL_NAME))
            .andExpect(jsonPath("$.gender").value(DEFAULT_GENDER.toString()))
            .andExpect(jsonPath("$.isPregnant").value(DEFAULT_IS_PREGNANT.booleanValue()))
            .andExpect(jsonPath("$.malariaTestResult").value(DEFAULT_MALARIA_TEST_RESULT.toString()))
            .andExpect(jsonPath("$.drugsGiven").value(DEFAULT_DRUGS_GIVEN))
            .andExpect(jsonPath("$.suppsGiven").value(DEFAULT_SUPPS_GIVEN))
            .andExpect(jsonPath("$.referral").value(DEFAULT_REFERRAL.booleanValue()))
            .andExpect(jsonPath("$.barImageUrl").value(DEFAULT_BAR_IMAGE_URL))
            .andExpect(jsonPath("$.comment").value(DEFAULT_COMMENT));
    }

    @Test
    @Transactional
    void getNonExistingChvMalariaCaseReport() throws Exception {
        // Get the chvMalariaCaseReport
        restChvMalariaCaseReportMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewChvMalariaCaseReport() throws Exception {
        // Initialize the database
        chvMalariaCaseReportRepository.saveAndFlush(chvMalariaCaseReport);

        int databaseSizeBeforeUpdate = chvMalariaCaseReportRepository.findAll().size();

        // Update the chvMalariaCaseReport
        ChvMalariaCaseReport updatedChvMalariaCaseReport = chvMalariaCaseReportRepository.findById(chvMalariaCaseReport.getId()).get();
        // Disconnect from session so that the updates on updatedChvMalariaCaseReport are not directly saved in db
        em.detach(updatedChvMalariaCaseReport);
        updatedChvMalariaCaseReport
            .uid(UPDATED_UID)
            .created(UPDATED_CREATED)
            .lastUpdated(UPDATED_LAST_UPDATED)
            .date(UPDATED_DATE)
            .individualName(UPDATED_INDIVIDUAL_NAME)
            .gender(UPDATED_GENDER)
            .isPregnant(UPDATED_IS_PREGNANT)
            .malariaTestResult(UPDATED_MALARIA_TEST_RESULT)
            .drugsGiven(UPDATED_DRUGS_GIVEN)
            .suppsGiven(UPDATED_SUPPS_GIVEN)
            .referral(UPDATED_REFERRAL)
            .barImageUrl(UPDATED_BAR_IMAGE_URL)
            .comment(UPDATED_COMMENT);

        restChvMalariaCaseReportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedChvMalariaCaseReport.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedChvMalariaCaseReport))
            )
            .andExpect(status().isOk());

        // Validate the ChvMalariaCaseReport in the database
        List<ChvMalariaCaseReport> chvMalariaCaseReportList = chvMalariaCaseReportRepository.findAll();
        assertThat(chvMalariaCaseReportList).hasSize(databaseSizeBeforeUpdate);
        ChvMalariaCaseReport testChvMalariaCaseReport = chvMalariaCaseReportList.get(chvMalariaCaseReportList.size() - 1);
        assertThat(testChvMalariaCaseReport.getUid()).isEqualTo(UPDATED_UID);
        assertThat(testChvMalariaCaseReport.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testChvMalariaCaseReport.getLastUpdated()).isEqualTo(UPDATED_LAST_UPDATED);
        assertThat(testChvMalariaCaseReport.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testChvMalariaCaseReport.getIndividualName()).isEqualTo(UPDATED_INDIVIDUAL_NAME);
        assertThat(testChvMalariaCaseReport.getGender()).isEqualTo(UPDATED_GENDER);
        assertThat(testChvMalariaCaseReport.getIsPregnant()).isEqualTo(UPDATED_IS_PREGNANT);
        assertThat(testChvMalariaCaseReport.getMalariaTestResult()).isEqualTo(UPDATED_MALARIA_TEST_RESULT);
        assertThat(testChvMalariaCaseReport.getDrugsGiven()).isEqualTo(UPDATED_DRUGS_GIVEN);
        assertThat(testChvMalariaCaseReport.getSuppsGiven()).isEqualTo(UPDATED_SUPPS_GIVEN);
        assertThat(testChvMalariaCaseReport.getReferral()).isEqualTo(UPDATED_REFERRAL);
        assertThat(testChvMalariaCaseReport.getBarImageUrl()).isEqualTo(UPDATED_BAR_IMAGE_URL);
        assertThat(testChvMalariaCaseReport.getComment()).isEqualTo(UPDATED_COMMENT);
    }

    @Test
    @Transactional
    void putNonExistingChvMalariaCaseReport() throws Exception {
        int databaseSizeBeforeUpdate = chvMalariaCaseReportRepository.findAll().size();
        chvMalariaCaseReport.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restChvMalariaCaseReportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, chvMalariaCaseReport.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(chvMalariaCaseReport))
            )
            .andExpect(status().isBadRequest());

        // Validate the ChvMalariaCaseReport in the database
        List<ChvMalariaCaseReport> chvMalariaCaseReportList = chvMalariaCaseReportRepository.findAll();
        assertThat(chvMalariaCaseReportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchChvMalariaCaseReport() throws Exception {
        int databaseSizeBeforeUpdate = chvMalariaCaseReportRepository.findAll().size();
        chvMalariaCaseReport.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChvMalariaCaseReportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(chvMalariaCaseReport))
            )
            .andExpect(status().isBadRequest());

        // Validate the ChvMalariaCaseReport in the database
        List<ChvMalariaCaseReport> chvMalariaCaseReportList = chvMalariaCaseReportRepository.findAll();
        assertThat(chvMalariaCaseReportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamChvMalariaCaseReport() throws Exception {
        int databaseSizeBeforeUpdate = chvMalariaCaseReportRepository.findAll().size();
        chvMalariaCaseReport.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChvMalariaCaseReportMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(chvMalariaCaseReport))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ChvMalariaCaseReport in the database
        List<ChvMalariaCaseReport> chvMalariaCaseReportList = chvMalariaCaseReportRepository.findAll();
        assertThat(chvMalariaCaseReportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateChvMalariaCaseReportWithPatch() throws Exception {
        // Initialize the database
        chvMalariaCaseReportRepository.saveAndFlush(chvMalariaCaseReport);

        int databaseSizeBeforeUpdate = chvMalariaCaseReportRepository.findAll().size();

        // Update the chvMalariaCaseReport using partial update
        ChvMalariaCaseReport partialUpdatedChvMalariaCaseReport = new ChvMalariaCaseReport();
        partialUpdatedChvMalariaCaseReport.setId(chvMalariaCaseReport.getId());

        partialUpdatedChvMalariaCaseReport
            .uid(UPDATED_UID)
            .created(UPDATED_CREATED)
            .date(UPDATED_DATE)
            .individualName(UPDATED_INDIVIDUAL_NAME)
            .isPregnant(UPDATED_IS_PREGNANT)
            .suppsGiven(UPDATED_SUPPS_GIVEN)
            .barImageUrl(UPDATED_BAR_IMAGE_URL);

        restChvMalariaCaseReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedChvMalariaCaseReport.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedChvMalariaCaseReport))
            )
            .andExpect(status().isOk());

        // Validate the ChvMalariaCaseReport in the database
        List<ChvMalariaCaseReport> chvMalariaCaseReportList = chvMalariaCaseReportRepository.findAll();
        assertThat(chvMalariaCaseReportList).hasSize(databaseSizeBeforeUpdate);
        ChvMalariaCaseReport testChvMalariaCaseReport = chvMalariaCaseReportList.get(chvMalariaCaseReportList.size() - 1);
        assertThat(testChvMalariaCaseReport.getUid()).isEqualTo(UPDATED_UID);
        assertThat(testChvMalariaCaseReport.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testChvMalariaCaseReport.getLastUpdated()).isEqualTo(DEFAULT_LAST_UPDATED);
        assertThat(testChvMalariaCaseReport.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testChvMalariaCaseReport.getIndividualName()).isEqualTo(UPDATED_INDIVIDUAL_NAME);
        assertThat(testChvMalariaCaseReport.getGender()).isEqualTo(DEFAULT_GENDER);
        assertThat(testChvMalariaCaseReport.getIsPregnant()).isEqualTo(UPDATED_IS_PREGNANT);
        assertThat(testChvMalariaCaseReport.getMalariaTestResult()).isEqualTo(DEFAULT_MALARIA_TEST_RESULT);
        assertThat(testChvMalariaCaseReport.getDrugsGiven()).isEqualTo(DEFAULT_DRUGS_GIVEN);
        assertThat(testChvMalariaCaseReport.getSuppsGiven()).isEqualTo(UPDATED_SUPPS_GIVEN);
        assertThat(testChvMalariaCaseReport.getReferral()).isEqualTo(DEFAULT_REFERRAL);
        assertThat(testChvMalariaCaseReport.getBarImageUrl()).isEqualTo(UPDATED_BAR_IMAGE_URL);
        assertThat(testChvMalariaCaseReport.getComment()).isEqualTo(DEFAULT_COMMENT);
    }

    @Test
    @Transactional
    void fullUpdateChvMalariaCaseReportWithPatch() throws Exception {
        // Initialize the database
        chvMalariaCaseReportRepository.saveAndFlush(chvMalariaCaseReport);

        int databaseSizeBeforeUpdate = chvMalariaCaseReportRepository.findAll().size();

        // Update the chvMalariaCaseReport using partial update
        ChvMalariaCaseReport partialUpdatedChvMalariaCaseReport = new ChvMalariaCaseReport();
        partialUpdatedChvMalariaCaseReport.setId(chvMalariaCaseReport.getId());

        partialUpdatedChvMalariaCaseReport
            .uid(UPDATED_UID)
            .created(UPDATED_CREATED)
            .lastUpdated(UPDATED_LAST_UPDATED)
            .date(UPDATED_DATE)
            .individualName(UPDATED_INDIVIDUAL_NAME)
            .gender(UPDATED_GENDER)
            .isPregnant(UPDATED_IS_PREGNANT)
            .malariaTestResult(UPDATED_MALARIA_TEST_RESULT)
            .drugsGiven(UPDATED_DRUGS_GIVEN)
            .suppsGiven(UPDATED_SUPPS_GIVEN)
            .referral(UPDATED_REFERRAL)
            .barImageUrl(UPDATED_BAR_IMAGE_URL)
            .comment(UPDATED_COMMENT);

        restChvMalariaCaseReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedChvMalariaCaseReport.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedChvMalariaCaseReport))
            )
            .andExpect(status().isOk());

        // Validate the ChvMalariaCaseReport in the database
        List<ChvMalariaCaseReport> chvMalariaCaseReportList = chvMalariaCaseReportRepository.findAll();
        assertThat(chvMalariaCaseReportList).hasSize(databaseSizeBeforeUpdate);
        ChvMalariaCaseReport testChvMalariaCaseReport = chvMalariaCaseReportList.get(chvMalariaCaseReportList.size() - 1);
        assertThat(testChvMalariaCaseReport.getUid()).isEqualTo(UPDATED_UID);
        assertThat(testChvMalariaCaseReport.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testChvMalariaCaseReport.getLastUpdated()).isEqualTo(UPDATED_LAST_UPDATED);
        assertThat(testChvMalariaCaseReport.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testChvMalariaCaseReport.getIndividualName()).isEqualTo(UPDATED_INDIVIDUAL_NAME);
        assertThat(testChvMalariaCaseReport.getGender()).isEqualTo(UPDATED_GENDER);
        assertThat(testChvMalariaCaseReport.getIsPregnant()).isEqualTo(UPDATED_IS_PREGNANT);
        assertThat(testChvMalariaCaseReport.getMalariaTestResult()).isEqualTo(UPDATED_MALARIA_TEST_RESULT);
        assertThat(testChvMalariaCaseReport.getDrugsGiven()).isEqualTo(UPDATED_DRUGS_GIVEN);
        assertThat(testChvMalariaCaseReport.getSuppsGiven()).isEqualTo(UPDATED_SUPPS_GIVEN);
        assertThat(testChvMalariaCaseReport.getReferral()).isEqualTo(UPDATED_REFERRAL);
        assertThat(testChvMalariaCaseReport.getBarImageUrl()).isEqualTo(UPDATED_BAR_IMAGE_URL);
        assertThat(testChvMalariaCaseReport.getComment()).isEqualTo(UPDATED_COMMENT);
    }

    @Test
    @Transactional
    void patchNonExistingChvMalariaCaseReport() throws Exception {
        int databaseSizeBeforeUpdate = chvMalariaCaseReportRepository.findAll().size();
        chvMalariaCaseReport.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restChvMalariaCaseReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, chvMalariaCaseReport.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(chvMalariaCaseReport))
            )
            .andExpect(status().isBadRequest());

        // Validate the ChvMalariaCaseReport in the database
        List<ChvMalariaCaseReport> chvMalariaCaseReportList = chvMalariaCaseReportRepository.findAll();
        assertThat(chvMalariaCaseReportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchChvMalariaCaseReport() throws Exception {
        int databaseSizeBeforeUpdate = chvMalariaCaseReportRepository.findAll().size();
        chvMalariaCaseReport.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChvMalariaCaseReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(chvMalariaCaseReport))
            )
            .andExpect(status().isBadRequest());

        // Validate the ChvMalariaCaseReport in the database
        List<ChvMalariaCaseReport> chvMalariaCaseReportList = chvMalariaCaseReportRepository.findAll();
        assertThat(chvMalariaCaseReportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamChvMalariaCaseReport() throws Exception {
        int databaseSizeBeforeUpdate = chvMalariaCaseReportRepository.findAll().size();
        chvMalariaCaseReport.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChvMalariaCaseReportMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(chvMalariaCaseReport))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ChvMalariaCaseReport in the database
        List<ChvMalariaCaseReport> chvMalariaCaseReportList = chvMalariaCaseReportRepository.findAll();
        assertThat(chvMalariaCaseReportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteChvMalariaCaseReport() throws Exception {
        // Initialize the database
        chvMalariaCaseReportRepository.saveAndFlush(chvMalariaCaseReport);

        int databaseSizeBeforeDelete = chvMalariaCaseReportRepository.findAll().size();

        // Delete the chvMalariaCaseReport
        restChvMalariaCaseReportMockMvc
            .perform(delete(ENTITY_API_URL_ID, chvMalariaCaseReport.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ChvMalariaCaseReport> chvMalariaCaseReportList = chvMalariaCaseReportRepository.findAll();
        assertThat(chvMalariaCaseReportList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
