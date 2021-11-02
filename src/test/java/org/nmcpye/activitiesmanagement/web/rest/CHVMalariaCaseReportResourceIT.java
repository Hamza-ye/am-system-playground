package org.nmcpye.activitiesmanagement.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.nmcpye.activitiesmanagement.IntegrationTest;
import org.nmcpye.activitiesmanagement.domain.CHV;
import org.nmcpye.activitiesmanagement.domain.CHVMalariaCaseReport;
import org.nmcpye.activitiesmanagement.domain.CasesReportClass;
import org.nmcpye.activitiesmanagement.domain.enumeration.Gender;
import org.nmcpye.activitiesmanagement.domain.enumeration.MalariaTestResult;
import org.nmcpye.activitiesmanagement.repository.CHVMalariaCaseReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link CHVMalariaCaseReportResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CHVMalariaCaseReportResourceIT {

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
    private CHVMalariaCaseReportRepository cHVMalariaCaseReportRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCHVMalariaCaseReportMockMvc;

    private CHVMalariaCaseReport cHVMalariaCaseReport;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CHVMalariaCaseReport createEntity(EntityManager em) {
        CHVMalariaCaseReport cHVMalariaCaseReport = new CHVMalariaCaseReport()
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
        CHV cHV;
        if (TestUtil.findAll(em, CHV.class).isEmpty()) {
            cHV = CHVResourceIT.createEntity(em);
            em.persist(cHV);
            em.flush();
        } else {
            cHV = TestUtil.findAll(em, CHV.class).get(0);
        }
        cHVMalariaCaseReport.setChv(cHV);
        // Add required entity
        CasesReportClass casesReportClass;
        if (TestUtil.findAll(em, CasesReportClass.class).isEmpty()) {
            casesReportClass = CasesReportClassResourceIT.createEntity(em);
            em.persist(casesReportClass);
            em.flush();
        } else {
            casesReportClass = TestUtil.findAll(em, CasesReportClass.class).get(0);
        }
        cHVMalariaCaseReport.setReportClass(casesReportClass);
        return cHVMalariaCaseReport;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CHVMalariaCaseReport createUpdatedEntity(EntityManager em) {
        CHVMalariaCaseReport cHVMalariaCaseReport = new CHVMalariaCaseReport()
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
        CHV cHV;
        if (TestUtil.findAll(em, CHV.class).isEmpty()) {
            cHV = CHVResourceIT.createUpdatedEntity(em);
            em.persist(cHV);
            em.flush();
        } else {
            cHV = TestUtil.findAll(em, CHV.class).get(0);
        }
        cHVMalariaCaseReport.setChv(cHV);
        // Add required entity
        CasesReportClass casesReportClass;
        if (TestUtil.findAll(em, CasesReportClass.class).isEmpty()) {
            casesReportClass = CasesReportClassResourceIT.createUpdatedEntity(em);
            em.persist(casesReportClass);
            em.flush();
        } else {
            casesReportClass = TestUtil.findAll(em, CasesReportClass.class).get(0);
        }
        cHVMalariaCaseReport.setReportClass(casesReportClass);
        return cHVMalariaCaseReport;
    }

    @BeforeEach
    public void initTest() {
        cHVMalariaCaseReport = createEntity(em);
    }

    @Test
    @Transactional
    void createCHVMalariaCaseReport() throws Exception {
        int databaseSizeBeforeCreate = cHVMalariaCaseReportRepository.findAll().size();
        // Create the CHVMalariaCaseReport
        restCHVMalariaCaseReportMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cHVMalariaCaseReport))
            )
            .andExpect(status().isCreated());

        // Validate the CHVMalariaCaseReport in the database
        List<CHVMalariaCaseReport> cHVMalariaCaseReportList = cHVMalariaCaseReportRepository.findAll();
        assertThat(cHVMalariaCaseReportList).hasSize(databaseSizeBeforeCreate + 1);
        CHVMalariaCaseReport testCHVMalariaCaseReport = cHVMalariaCaseReportList.get(cHVMalariaCaseReportList.size() - 1);
        assertThat(testCHVMalariaCaseReport.getUid()).isEqualTo(DEFAULT_UID);
        assertThat(testCHVMalariaCaseReport.getCreated()).isEqualTo(DEFAULT_CREATED);
        assertThat(testCHVMalariaCaseReport.getLastUpdated()).isEqualTo(DEFAULT_LAST_UPDATED);
        assertThat(testCHVMalariaCaseReport.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testCHVMalariaCaseReport.getIndividualName()).isEqualTo(DEFAULT_INDIVIDUAL_NAME);
        assertThat(testCHVMalariaCaseReport.getGender()).isEqualTo(DEFAULT_GENDER);
        assertThat(testCHVMalariaCaseReport.getIsPregnant()).isEqualTo(DEFAULT_IS_PREGNANT);
        assertThat(testCHVMalariaCaseReport.getMalariaTestResult()).isEqualTo(DEFAULT_MALARIA_TEST_RESULT);
        assertThat(testCHVMalariaCaseReport.getDrugsGiven()).isEqualTo(DEFAULT_DRUGS_GIVEN);
        assertThat(testCHVMalariaCaseReport.getSuppsGiven()).isEqualTo(DEFAULT_SUPPS_GIVEN);
        assertThat(testCHVMalariaCaseReport.getReferral()).isEqualTo(DEFAULT_REFERRAL);
        assertThat(testCHVMalariaCaseReport.getBarImageUrl()).isEqualTo(DEFAULT_BAR_IMAGE_URL);
        assertThat(testCHVMalariaCaseReport.getComment()).isEqualTo(DEFAULT_COMMENT);
    }

    @Test
    @Transactional
    void createCHVMalariaCaseReportWithExistingId() throws Exception {
        // Create the CHVMalariaCaseReport with an existing ID
        cHVMalariaCaseReport.setId(1L);

        int databaseSizeBeforeCreate = cHVMalariaCaseReportRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCHVMalariaCaseReportMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cHVMalariaCaseReport))
            )
            .andExpect(status().isBadRequest());

        // Validate the CHVMalariaCaseReport in the database
        List<CHVMalariaCaseReport> cHVMalariaCaseReportList = cHVMalariaCaseReportRepository.findAll();
        assertThat(cHVMalariaCaseReportList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkUidIsRequired() throws Exception {
        int databaseSizeBeforeTest = cHVMalariaCaseReportRepository.findAll().size();
        // set the field null
        cHVMalariaCaseReport.setUid(null);

        // Create the CHVMalariaCaseReport, which fails.

        restCHVMalariaCaseReportMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cHVMalariaCaseReport))
            )
            .andExpect(status().isBadRequest());

        List<CHVMalariaCaseReport> cHVMalariaCaseReportList = cHVMalariaCaseReportRepository.findAll();
        assertThat(cHVMalariaCaseReportList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCHVMalariaCaseReports() throws Exception {
        // Initialize the database
        cHVMalariaCaseReportRepository.saveAndFlush(cHVMalariaCaseReport);

        // Get all the cHVMalariaCaseReportList
        restCHVMalariaCaseReportMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cHVMalariaCaseReport.getId().intValue())))
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
    void getCHVMalariaCaseReport() throws Exception {
        // Initialize the database
        cHVMalariaCaseReportRepository.saveAndFlush(cHVMalariaCaseReport);

        // Get the cHVMalariaCaseReport
        restCHVMalariaCaseReportMockMvc
            .perform(get(ENTITY_API_URL_ID, cHVMalariaCaseReport.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cHVMalariaCaseReport.getId().intValue()))
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
    void getNonExistingCHVMalariaCaseReport() throws Exception {
        // Get the cHVMalariaCaseReport
        restCHVMalariaCaseReportMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCHVMalariaCaseReport() throws Exception {
        // Initialize the database
        cHVMalariaCaseReportRepository.saveAndFlush(cHVMalariaCaseReport);

        int databaseSizeBeforeUpdate = cHVMalariaCaseReportRepository.findAll().size();

        // Update the cHVMalariaCaseReport
        CHVMalariaCaseReport updatedCHVMalariaCaseReport = cHVMalariaCaseReportRepository.findById(cHVMalariaCaseReport.getId()).get();
        // Disconnect from session so that the updates on updatedCHVMalariaCaseReport are not directly saved in db
        em.detach(updatedCHVMalariaCaseReport);
        updatedCHVMalariaCaseReport
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

        restCHVMalariaCaseReportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCHVMalariaCaseReport.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCHVMalariaCaseReport))
            )
            .andExpect(status().isOk());

        // Validate the CHVMalariaCaseReport in the database
        List<CHVMalariaCaseReport> cHVMalariaCaseReportList = cHVMalariaCaseReportRepository.findAll();
        assertThat(cHVMalariaCaseReportList).hasSize(databaseSizeBeforeUpdate);
        CHVMalariaCaseReport testCHVMalariaCaseReport = cHVMalariaCaseReportList.get(cHVMalariaCaseReportList.size() - 1);
        assertThat(testCHVMalariaCaseReport.getUid()).isEqualTo(UPDATED_UID);
        assertThat(testCHVMalariaCaseReport.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testCHVMalariaCaseReport.getLastUpdated()).isEqualTo(UPDATED_LAST_UPDATED);
        assertThat(testCHVMalariaCaseReport.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testCHVMalariaCaseReport.getIndividualName()).isEqualTo(UPDATED_INDIVIDUAL_NAME);
        assertThat(testCHVMalariaCaseReport.getGender()).isEqualTo(UPDATED_GENDER);
        assertThat(testCHVMalariaCaseReport.getIsPregnant()).isEqualTo(UPDATED_IS_PREGNANT);
        assertThat(testCHVMalariaCaseReport.getMalariaTestResult()).isEqualTo(UPDATED_MALARIA_TEST_RESULT);
        assertThat(testCHVMalariaCaseReport.getDrugsGiven()).isEqualTo(UPDATED_DRUGS_GIVEN);
        assertThat(testCHVMalariaCaseReport.getSuppsGiven()).isEqualTo(UPDATED_SUPPS_GIVEN);
        assertThat(testCHVMalariaCaseReport.getReferral()).isEqualTo(UPDATED_REFERRAL);
        assertThat(testCHVMalariaCaseReport.getBarImageUrl()).isEqualTo(UPDATED_BAR_IMAGE_URL);
        assertThat(testCHVMalariaCaseReport.getComment()).isEqualTo(UPDATED_COMMENT);
    }

    @Test
    @Transactional
    void putNonExistingCHVMalariaCaseReport() throws Exception {
        int databaseSizeBeforeUpdate = cHVMalariaCaseReportRepository.findAll().size();
        cHVMalariaCaseReport.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCHVMalariaCaseReportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cHVMalariaCaseReport.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cHVMalariaCaseReport))
            )
            .andExpect(status().isBadRequest());

        // Validate the CHVMalariaCaseReport in the database
        List<CHVMalariaCaseReport> cHVMalariaCaseReportList = cHVMalariaCaseReportRepository.findAll();
        assertThat(cHVMalariaCaseReportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCHVMalariaCaseReport() throws Exception {
        int databaseSizeBeforeUpdate = cHVMalariaCaseReportRepository.findAll().size();
        cHVMalariaCaseReport.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCHVMalariaCaseReportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cHVMalariaCaseReport))
            )
            .andExpect(status().isBadRequest());

        // Validate the CHVMalariaCaseReport in the database
        List<CHVMalariaCaseReport> cHVMalariaCaseReportList = cHVMalariaCaseReportRepository.findAll();
        assertThat(cHVMalariaCaseReportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCHVMalariaCaseReport() throws Exception {
        int databaseSizeBeforeUpdate = cHVMalariaCaseReportRepository.findAll().size();
        cHVMalariaCaseReport.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCHVMalariaCaseReportMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cHVMalariaCaseReport))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CHVMalariaCaseReport in the database
        List<CHVMalariaCaseReport> cHVMalariaCaseReportList = cHVMalariaCaseReportRepository.findAll();
        assertThat(cHVMalariaCaseReportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCHVMalariaCaseReportWithPatch() throws Exception {
        // Initialize the database
        cHVMalariaCaseReportRepository.saveAndFlush(cHVMalariaCaseReport);

        int databaseSizeBeforeUpdate = cHVMalariaCaseReportRepository.findAll().size();

        // Update the cHVMalariaCaseReport using partial update
        CHVMalariaCaseReport partialUpdatedCHVMalariaCaseReport = new CHVMalariaCaseReport();
        partialUpdatedCHVMalariaCaseReport.setId(cHVMalariaCaseReport.getId());

        partialUpdatedCHVMalariaCaseReport
            .created(UPDATED_CREATED)
            .date(UPDATED_DATE)
            .individualName(UPDATED_INDIVIDUAL_NAME)
            .gender(UPDATED_GENDER)
            .referral(UPDATED_REFERRAL)
            .comment(UPDATED_COMMENT);

        restCHVMalariaCaseReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCHVMalariaCaseReport.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCHVMalariaCaseReport))
            )
            .andExpect(status().isOk());

        // Validate the CHVMalariaCaseReport in the database
        List<CHVMalariaCaseReport> cHVMalariaCaseReportList = cHVMalariaCaseReportRepository.findAll();
        assertThat(cHVMalariaCaseReportList).hasSize(databaseSizeBeforeUpdate);
        CHVMalariaCaseReport testCHVMalariaCaseReport = cHVMalariaCaseReportList.get(cHVMalariaCaseReportList.size() - 1);
        assertThat(testCHVMalariaCaseReport.getUid()).isEqualTo(DEFAULT_UID);
        assertThat(testCHVMalariaCaseReport.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testCHVMalariaCaseReport.getLastUpdated()).isEqualTo(DEFAULT_LAST_UPDATED);
        assertThat(testCHVMalariaCaseReport.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testCHVMalariaCaseReport.getIndividualName()).isEqualTo(UPDATED_INDIVIDUAL_NAME);
        assertThat(testCHVMalariaCaseReport.getGender()).isEqualTo(UPDATED_GENDER);
        assertThat(testCHVMalariaCaseReport.getIsPregnant()).isEqualTo(DEFAULT_IS_PREGNANT);
        assertThat(testCHVMalariaCaseReport.getMalariaTestResult()).isEqualTo(DEFAULT_MALARIA_TEST_RESULT);
        assertThat(testCHVMalariaCaseReport.getDrugsGiven()).isEqualTo(DEFAULT_DRUGS_GIVEN);
        assertThat(testCHVMalariaCaseReport.getSuppsGiven()).isEqualTo(DEFAULT_SUPPS_GIVEN);
        assertThat(testCHVMalariaCaseReport.getReferral()).isEqualTo(UPDATED_REFERRAL);
        assertThat(testCHVMalariaCaseReport.getBarImageUrl()).isEqualTo(DEFAULT_BAR_IMAGE_URL);
        assertThat(testCHVMalariaCaseReport.getComment()).isEqualTo(UPDATED_COMMENT);
    }

    @Test
    @Transactional
    void fullUpdateCHVMalariaCaseReportWithPatch() throws Exception {
        // Initialize the database
        cHVMalariaCaseReportRepository.saveAndFlush(cHVMalariaCaseReport);

        int databaseSizeBeforeUpdate = cHVMalariaCaseReportRepository.findAll().size();

        // Update the cHVMalariaCaseReport using partial update
        CHVMalariaCaseReport partialUpdatedCHVMalariaCaseReport = new CHVMalariaCaseReport();
        partialUpdatedCHVMalariaCaseReport.setId(cHVMalariaCaseReport.getId());

        partialUpdatedCHVMalariaCaseReport
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

        restCHVMalariaCaseReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCHVMalariaCaseReport.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCHVMalariaCaseReport))
            )
            .andExpect(status().isOk());

        // Validate the CHVMalariaCaseReport in the database
        List<CHVMalariaCaseReport> cHVMalariaCaseReportList = cHVMalariaCaseReportRepository.findAll();
        assertThat(cHVMalariaCaseReportList).hasSize(databaseSizeBeforeUpdate);
        CHVMalariaCaseReport testCHVMalariaCaseReport = cHVMalariaCaseReportList.get(cHVMalariaCaseReportList.size() - 1);
        assertThat(testCHVMalariaCaseReport.getUid()).isEqualTo(UPDATED_UID);
        assertThat(testCHVMalariaCaseReport.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testCHVMalariaCaseReport.getLastUpdated()).isEqualTo(UPDATED_LAST_UPDATED);
        assertThat(testCHVMalariaCaseReport.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testCHVMalariaCaseReport.getIndividualName()).isEqualTo(UPDATED_INDIVIDUAL_NAME);
        assertThat(testCHVMalariaCaseReport.getGender()).isEqualTo(UPDATED_GENDER);
        assertThat(testCHVMalariaCaseReport.getIsPregnant()).isEqualTo(UPDATED_IS_PREGNANT);
        assertThat(testCHVMalariaCaseReport.getMalariaTestResult()).isEqualTo(UPDATED_MALARIA_TEST_RESULT);
        assertThat(testCHVMalariaCaseReport.getDrugsGiven()).isEqualTo(UPDATED_DRUGS_GIVEN);
        assertThat(testCHVMalariaCaseReport.getSuppsGiven()).isEqualTo(UPDATED_SUPPS_GIVEN);
        assertThat(testCHVMalariaCaseReport.getReferral()).isEqualTo(UPDATED_REFERRAL);
        assertThat(testCHVMalariaCaseReport.getBarImageUrl()).isEqualTo(UPDATED_BAR_IMAGE_URL);
        assertThat(testCHVMalariaCaseReport.getComment()).isEqualTo(UPDATED_COMMENT);
    }

    @Test
    @Transactional
    void patchNonExistingCHVMalariaCaseReport() throws Exception {
        int databaseSizeBeforeUpdate = cHVMalariaCaseReportRepository.findAll().size();
        cHVMalariaCaseReport.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCHVMalariaCaseReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, cHVMalariaCaseReport.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cHVMalariaCaseReport))
            )
            .andExpect(status().isBadRequest());

        // Validate the CHVMalariaCaseReport in the database
        List<CHVMalariaCaseReport> cHVMalariaCaseReportList = cHVMalariaCaseReportRepository.findAll();
        assertThat(cHVMalariaCaseReportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCHVMalariaCaseReport() throws Exception {
        int databaseSizeBeforeUpdate = cHVMalariaCaseReportRepository.findAll().size();
        cHVMalariaCaseReport.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCHVMalariaCaseReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cHVMalariaCaseReport))
            )
            .andExpect(status().isBadRequest());

        // Validate the CHVMalariaCaseReport in the database
        List<CHVMalariaCaseReport> cHVMalariaCaseReportList = cHVMalariaCaseReportRepository.findAll();
        assertThat(cHVMalariaCaseReportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCHVMalariaCaseReport() throws Exception {
        int databaseSizeBeforeUpdate = cHVMalariaCaseReportRepository.findAll().size();
        cHVMalariaCaseReport.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCHVMalariaCaseReportMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cHVMalariaCaseReport))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CHVMalariaCaseReport in the database
        List<CHVMalariaCaseReport> cHVMalariaCaseReportList = cHVMalariaCaseReportRepository.findAll();
        assertThat(cHVMalariaCaseReportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCHVMalariaCaseReport() throws Exception {
        // Initialize the database
        cHVMalariaCaseReportRepository.saveAndFlush(cHVMalariaCaseReport);

        int databaseSizeBeforeDelete = cHVMalariaCaseReportRepository.findAll().size();

        // Delete the cHVMalariaCaseReport
        restCHVMalariaCaseReportMockMvc
            .perform(delete(ENTITY_API_URL_ID, cHVMalariaCaseReport.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CHVMalariaCaseReport> cHVMalariaCaseReportList = cHVMalariaCaseReportRepository.findAll();
        assertThat(cHVMalariaCaseReportList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
