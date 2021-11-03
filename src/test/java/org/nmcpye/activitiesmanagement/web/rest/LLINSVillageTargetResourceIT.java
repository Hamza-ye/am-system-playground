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
import org.nmcpye.activitiesmanagement.domain.LLINSVillageTarget;
import org.nmcpye.activitiesmanagement.domain.organisationunit.OrganisationUnit;
import org.nmcpye.activitiesmanagement.domain.Team;
import org.nmcpye.activitiesmanagement.domain.WorkingDay;
import org.nmcpye.activitiesmanagement.repository.LLINSVillageTargetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link LLINSVillageTargetResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class LLINSVillageTargetResourceIT {

    private static final String DEFAULT_UID = "AAAAAAAAAA";
    private static final String UPDATED_UID = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_LAST_UPDATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_UPDATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Integer DEFAULT_RESIDENTS_INDIVIDUALS = 0;
    private static final Integer UPDATED_RESIDENTS_INDIVIDUALS = 1;

    private static final Integer DEFAULT_IDPS_INDIVIDUALS = 0;
    private static final Integer UPDATED_IDPS_INDIVIDUALS = 1;

    private static final Integer DEFAULT_RESIDENTS_FAMILIES = 0;
    private static final Integer UPDATED_RESIDENTS_FAMILIES = 1;

    private static final Integer DEFAULT_IDPS_FAMILIES = 0;
    private static final Integer UPDATED_IDPS_FAMILIES = 1;

    private static final Integer DEFAULT_NO_OF_DAYS_NEEDED = 0;
    private static final Integer UPDATED_NO_OF_DAYS_NEEDED = 1;

    private static final Integer DEFAULT_QUANTITY = 0;
    private static final Integer UPDATED_QUANTITY = 1;

    private static final String ENTITY_API_URL = "/api/llins-village-targets";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private LLINSVillageTargetRepository lLINSVillageTargetRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLLINSVillageTargetMockMvc;

    private LLINSVillageTarget lLINSVillageTarget;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LLINSVillageTarget createEntity(EntityManager em) {
        LLINSVillageTarget lLINSVillageTarget = new LLINSVillageTarget()
            .uid(DEFAULT_UID)
            .created(DEFAULT_CREATED)
            .lastUpdated(DEFAULT_LAST_UPDATED)
            .residentsIndividuals(DEFAULT_RESIDENTS_INDIVIDUALS)
            .idpsIndividuals(DEFAULT_IDPS_INDIVIDUALS)
            .residentsFamilies(DEFAULT_RESIDENTS_FAMILIES)
            .idpsFamilies(DEFAULT_IDPS_FAMILIES)
            .noOfDaysNeeded(DEFAULT_NO_OF_DAYS_NEEDED)
            .quantity(DEFAULT_QUANTITY);
        // Add required entity
        OrganisationUnit organisationUnit;
        if (TestUtil.findAll(em, OrganisationUnit.class).isEmpty()) {
            organisationUnit = OrganisationUnitResourceIT.createEntity(em);
            em.persist(organisationUnit);
            em.flush();
        } else {
            organisationUnit = TestUtil.findAll(em, OrganisationUnit.class).get(0);
        }
        lLINSVillageTarget.setOrganisationUnit(organisationUnit);
        // Add required entity
        WorkingDay workingDay;
        if (TestUtil.findAll(em, WorkingDay.class).isEmpty()) {
            workingDay = WorkingDayResourceIT.createEntity(em);
            em.persist(workingDay);
            em.flush();
        } else {
            workingDay = TestUtil.findAll(em, WorkingDay.class).get(0);
        }
        lLINSVillageTarget.setDayPlanned(workingDay);
        // Add required entity
        Team team;
        if (TestUtil.findAll(em, Team.class).isEmpty()) {
            team = TeamResourceIT.createEntity(em);
            em.persist(team);
            em.flush();
        } else {
            team = TestUtil.findAll(em, Team.class).get(0);
        }
        lLINSVillageTarget.setTeamAssigned(team);
        return lLINSVillageTarget;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LLINSVillageTarget createUpdatedEntity(EntityManager em) {
        LLINSVillageTarget lLINSVillageTarget = new LLINSVillageTarget()
            .uid(UPDATED_UID)
            .created(UPDATED_CREATED)
            .lastUpdated(UPDATED_LAST_UPDATED)
            .residentsIndividuals(UPDATED_RESIDENTS_INDIVIDUALS)
            .idpsIndividuals(UPDATED_IDPS_INDIVIDUALS)
            .residentsFamilies(UPDATED_RESIDENTS_FAMILIES)
            .idpsFamilies(UPDATED_IDPS_FAMILIES)
            .noOfDaysNeeded(UPDATED_NO_OF_DAYS_NEEDED)
            .quantity(UPDATED_QUANTITY);
        // Add required entity
        OrganisationUnit organisationUnit;
        if (TestUtil.findAll(em, OrganisationUnit.class).isEmpty()) {
            organisationUnit = OrganisationUnitResourceIT.createUpdatedEntity(em);
            em.persist(organisationUnit);
            em.flush();
        } else {
            organisationUnit = TestUtil.findAll(em, OrganisationUnit.class).get(0);
        }
        lLINSVillageTarget.setOrganisationUnit(organisationUnit);
        // Add required entity
        WorkingDay workingDay;
        if (TestUtil.findAll(em, WorkingDay.class).isEmpty()) {
            workingDay = WorkingDayResourceIT.createUpdatedEntity(em);
            em.persist(workingDay);
            em.flush();
        } else {
            workingDay = TestUtil.findAll(em, WorkingDay.class).get(0);
        }
        lLINSVillageTarget.setDayPlanned(workingDay);
        // Add required entity
        Team team;
        if (TestUtil.findAll(em, Team.class).isEmpty()) {
            team = TeamResourceIT.createUpdatedEntity(em);
            em.persist(team);
            em.flush();
        } else {
            team = TestUtil.findAll(em, Team.class).get(0);
        }
        lLINSVillageTarget.setTeamAssigned(team);
        return lLINSVillageTarget;
    }

    @BeforeEach
    public void initTest() {
        lLINSVillageTarget = createEntity(em);
    }

    @Test
    @Transactional
    void createLLINSVillageTarget() throws Exception {
        int databaseSizeBeforeCreate = lLINSVillageTargetRepository.findAll().size();
        // Create the LLINSVillageTarget
        restLLINSVillageTargetMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(lLINSVillageTarget))
            )
            .andExpect(status().isCreated());

        // Validate the LLINSVillageTarget in the database
        List<LLINSVillageTarget> lLINSVillageTargetList = lLINSVillageTargetRepository.findAll();
        assertThat(lLINSVillageTargetList).hasSize(databaseSizeBeforeCreate + 1);
        LLINSVillageTarget testLLINSVillageTarget = lLINSVillageTargetList.get(lLINSVillageTargetList.size() - 1);
        assertThat(testLLINSVillageTarget.getUid()).isEqualTo(DEFAULT_UID);
        assertThat(testLLINSVillageTarget.getCreated()).isEqualTo(DEFAULT_CREATED);
        assertThat(testLLINSVillageTarget.getLastUpdated()).isEqualTo(DEFAULT_LAST_UPDATED);
        assertThat(testLLINSVillageTarget.getResidentsIndividuals()).isEqualTo(DEFAULT_RESIDENTS_INDIVIDUALS);
        assertThat(testLLINSVillageTarget.getIdpsIndividuals()).isEqualTo(DEFAULT_IDPS_INDIVIDUALS);
        assertThat(testLLINSVillageTarget.getResidentsFamilies()).isEqualTo(DEFAULT_RESIDENTS_FAMILIES);
        assertThat(testLLINSVillageTarget.getIdpsFamilies()).isEqualTo(DEFAULT_IDPS_FAMILIES);
        assertThat(testLLINSVillageTarget.getNoOfDaysNeeded()).isEqualTo(DEFAULT_NO_OF_DAYS_NEEDED);
        assertThat(testLLINSVillageTarget.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
    }

    @Test
    @Transactional
    void createLLINSVillageTargetWithExistingId() throws Exception {
        // Create the LLINSVillageTarget with an existing ID
        lLINSVillageTarget.setId(1L);

        int databaseSizeBeforeCreate = lLINSVillageTargetRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLLINSVillageTargetMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(lLINSVillageTarget))
            )
            .andExpect(status().isBadRequest());

        // Validate the LLINSVillageTarget in the database
        List<LLINSVillageTarget> lLINSVillageTargetList = lLINSVillageTargetRepository.findAll();
        assertThat(lLINSVillageTargetList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkUidIsRequired() throws Exception {
        int databaseSizeBeforeTest = lLINSVillageTargetRepository.findAll().size();
        // set the field null
        lLINSVillageTarget.setUid(null);

        // Create the LLINSVillageTarget, which fails.

        restLLINSVillageTargetMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(lLINSVillageTarget))
            )
            .andExpect(status().isBadRequest());

        List<LLINSVillageTarget> lLINSVillageTargetList = lLINSVillageTargetRepository.findAll();
        assertThat(lLINSVillageTargetList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkResidentsIndividualsIsRequired() throws Exception {
        int databaseSizeBeforeTest = lLINSVillageTargetRepository.findAll().size();
        // set the field null
        lLINSVillageTarget.setResidentsIndividuals(null);

        // Create the LLINSVillageTarget, which fails.

        restLLINSVillageTargetMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(lLINSVillageTarget))
            )
            .andExpect(status().isBadRequest());

        List<LLINSVillageTarget> lLINSVillageTargetList = lLINSVillageTargetRepository.findAll();
        assertThat(lLINSVillageTargetList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIdpsIndividualsIsRequired() throws Exception {
        int databaseSizeBeforeTest = lLINSVillageTargetRepository.findAll().size();
        // set the field null
        lLINSVillageTarget.setIdpsIndividuals(null);

        // Create the LLINSVillageTarget, which fails.

        restLLINSVillageTargetMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(lLINSVillageTarget))
            )
            .andExpect(status().isBadRequest());

        List<LLINSVillageTarget> lLINSVillageTargetList = lLINSVillageTargetRepository.findAll();
        assertThat(lLINSVillageTargetList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkResidentsFamiliesIsRequired() throws Exception {
        int databaseSizeBeforeTest = lLINSVillageTargetRepository.findAll().size();
        // set the field null
        lLINSVillageTarget.setResidentsFamilies(null);

        // Create the LLINSVillageTarget, which fails.

        restLLINSVillageTargetMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(lLINSVillageTarget))
            )
            .andExpect(status().isBadRequest());

        List<LLINSVillageTarget> lLINSVillageTargetList = lLINSVillageTargetRepository.findAll();
        assertThat(lLINSVillageTargetList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIdpsFamiliesIsRequired() throws Exception {
        int databaseSizeBeforeTest = lLINSVillageTargetRepository.findAll().size();
        // set the field null
        lLINSVillageTarget.setIdpsFamilies(null);

        // Create the LLINSVillageTarget, which fails.

        restLLINSVillageTargetMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(lLINSVillageTarget))
            )
            .andExpect(status().isBadRequest());

        List<LLINSVillageTarget> lLINSVillageTargetList = lLINSVillageTargetRepository.findAll();
        assertThat(lLINSVillageTargetList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkQuantityIsRequired() throws Exception {
        int databaseSizeBeforeTest = lLINSVillageTargetRepository.findAll().size();
        // set the field null
        lLINSVillageTarget.setQuantity(null);

        // Create the LLINSVillageTarget, which fails.

        restLLINSVillageTargetMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(lLINSVillageTarget))
            )
            .andExpect(status().isBadRequest());

        List<LLINSVillageTarget> lLINSVillageTargetList = lLINSVillageTargetRepository.findAll();
        assertThat(lLINSVillageTargetList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllLLINSVillageTargets() throws Exception {
        // Initialize the database
        lLINSVillageTargetRepository.saveAndFlush(lLINSVillageTarget);

        // Get all the lLINSVillageTargetList
        restLLINSVillageTargetMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(lLINSVillageTarget.getId().intValue())))
            .andExpect(jsonPath("$.[*].uid").value(hasItem(DEFAULT_UID)))
            .andExpect(jsonPath("$.[*].created").value(hasItem(DEFAULT_CREATED.toString())))
            .andExpect(jsonPath("$.[*].lastUpdated").value(hasItem(DEFAULT_LAST_UPDATED.toString())))
            .andExpect(jsonPath("$.[*].residentsIndividuals").value(hasItem(DEFAULT_RESIDENTS_INDIVIDUALS)))
            .andExpect(jsonPath("$.[*].idpsIndividuals").value(hasItem(DEFAULT_IDPS_INDIVIDUALS)))
            .andExpect(jsonPath("$.[*].residentsFamilies").value(hasItem(DEFAULT_RESIDENTS_FAMILIES)))
            .andExpect(jsonPath("$.[*].idpsFamilies").value(hasItem(DEFAULT_IDPS_FAMILIES)))
            .andExpect(jsonPath("$.[*].noOfDaysNeeded").value(hasItem(DEFAULT_NO_OF_DAYS_NEEDED)))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)));
    }

    @Test
    @Transactional
    void getLLINSVillageTarget() throws Exception {
        // Initialize the database
        lLINSVillageTargetRepository.saveAndFlush(lLINSVillageTarget);

        // Get the lLINSVillageTarget
        restLLINSVillageTargetMockMvc
            .perform(get(ENTITY_API_URL_ID, lLINSVillageTarget.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(lLINSVillageTarget.getId().intValue()))
            .andExpect(jsonPath("$.uid").value(DEFAULT_UID))
            .andExpect(jsonPath("$.created").value(DEFAULT_CREATED.toString()))
            .andExpect(jsonPath("$.lastUpdated").value(DEFAULT_LAST_UPDATED.toString()))
            .andExpect(jsonPath("$.residentsIndividuals").value(DEFAULT_RESIDENTS_INDIVIDUALS))
            .andExpect(jsonPath("$.idpsIndividuals").value(DEFAULT_IDPS_INDIVIDUALS))
            .andExpect(jsonPath("$.residentsFamilies").value(DEFAULT_RESIDENTS_FAMILIES))
            .andExpect(jsonPath("$.idpsFamilies").value(DEFAULT_IDPS_FAMILIES))
            .andExpect(jsonPath("$.noOfDaysNeeded").value(DEFAULT_NO_OF_DAYS_NEEDED))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY));
    }

    @Test
    @Transactional
    void getNonExistingLLINSVillageTarget() throws Exception {
        // Get the lLINSVillageTarget
        restLLINSVillageTargetMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewLLINSVillageTarget() throws Exception {
        // Initialize the database
        lLINSVillageTargetRepository.saveAndFlush(lLINSVillageTarget);

        int databaseSizeBeforeUpdate = lLINSVillageTargetRepository.findAll().size();

        // Update the lLINSVillageTarget
        LLINSVillageTarget updatedLLINSVillageTarget = lLINSVillageTargetRepository.findById(lLINSVillageTarget.getId()).get();
        // Disconnect from session so that the updates on updatedLLINSVillageTarget are not directly saved in db
        em.detach(updatedLLINSVillageTarget);
        updatedLLINSVillageTarget
            .uid(UPDATED_UID)
            .created(UPDATED_CREATED)
            .lastUpdated(UPDATED_LAST_UPDATED)
            .residentsIndividuals(UPDATED_RESIDENTS_INDIVIDUALS)
            .idpsIndividuals(UPDATED_IDPS_INDIVIDUALS)
            .residentsFamilies(UPDATED_RESIDENTS_FAMILIES)
            .idpsFamilies(UPDATED_IDPS_FAMILIES)
            .noOfDaysNeeded(UPDATED_NO_OF_DAYS_NEEDED)
            .quantity(UPDATED_QUANTITY);

        restLLINSVillageTargetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedLLINSVillageTarget.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedLLINSVillageTarget))
            )
            .andExpect(status().isOk());

        // Validate the LLINSVillageTarget in the database
        List<LLINSVillageTarget> lLINSVillageTargetList = lLINSVillageTargetRepository.findAll();
        assertThat(lLINSVillageTargetList).hasSize(databaseSizeBeforeUpdate);
        LLINSVillageTarget testLLINSVillageTarget = lLINSVillageTargetList.get(lLINSVillageTargetList.size() - 1);
        assertThat(testLLINSVillageTarget.getUid()).isEqualTo(UPDATED_UID);
        assertThat(testLLINSVillageTarget.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testLLINSVillageTarget.getLastUpdated()).isEqualTo(UPDATED_LAST_UPDATED);
        assertThat(testLLINSVillageTarget.getResidentsIndividuals()).isEqualTo(UPDATED_RESIDENTS_INDIVIDUALS);
        assertThat(testLLINSVillageTarget.getIdpsIndividuals()).isEqualTo(UPDATED_IDPS_INDIVIDUALS);
        assertThat(testLLINSVillageTarget.getResidentsFamilies()).isEqualTo(UPDATED_RESIDENTS_FAMILIES);
        assertThat(testLLINSVillageTarget.getIdpsFamilies()).isEqualTo(UPDATED_IDPS_FAMILIES);
        assertThat(testLLINSVillageTarget.getNoOfDaysNeeded()).isEqualTo(UPDATED_NO_OF_DAYS_NEEDED);
        assertThat(testLLINSVillageTarget.getQuantity()).isEqualTo(UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void putNonExistingLLINSVillageTarget() throws Exception {
        int databaseSizeBeforeUpdate = lLINSVillageTargetRepository.findAll().size();
        lLINSVillageTarget.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLLINSVillageTargetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, lLINSVillageTarget.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(lLINSVillageTarget))
            )
            .andExpect(status().isBadRequest());

        // Validate the LLINSVillageTarget in the database
        List<LLINSVillageTarget> lLINSVillageTargetList = lLINSVillageTargetRepository.findAll();
        assertThat(lLINSVillageTargetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLLINSVillageTarget() throws Exception {
        int databaseSizeBeforeUpdate = lLINSVillageTargetRepository.findAll().size();
        lLINSVillageTarget.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLLINSVillageTargetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(lLINSVillageTarget))
            )
            .andExpect(status().isBadRequest());

        // Validate the LLINSVillageTarget in the database
        List<LLINSVillageTarget> lLINSVillageTargetList = lLINSVillageTargetRepository.findAll();
        assertThat(lLINSVillageTargetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLLINSVillageTarget() throws Exception {
        int databaseSizeBeforeUpdate = lLINSVillageTargetRepository.findAll().size();
        lLINSVillageTarget.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLLINSVillageTargetMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(lLINSVillageTarget))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LLINSVillageTarget in the database
        List<LLINSVillageTarget> lLINSVillageTargetList = lLINSVillageTargetRepository.findAll();
        assertThat(lLINSVillageTargetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLLINSVillageTargetWithPatch() throws Exception {
        // Initialize the database
        lLINSVillageTargetRepository.saveAndFlush(lLINSVillageTarget);

        int databaseSizeBeforeUpdate = lLINSVillageTargetRepository.findAll().size();

        // Update the lLINSVillageTarget using partial update
        LLINSVillageTarget partialUpdatedLLINSVillageTarget = new LLINSVillageTarget();
        partialUpdatedLLINSVillageTarget.setId(lLINSVillageTarget.getId());

        partialUpdatedLLINSVillageTarget
            .lastUpdated(UPDATED_LAST_UPDATED)
            .residentsIndividuals(UPDATED_RESIDENTS_INDIVIDUALS)
            .residentsFamilies(UPDATED_RESIDENTS_FAMILIES)
            .idpsFamilies(UPDATED_IDPS_FAMILIES);

        restLLINSVillageTargetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLLINSVillageTarget.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLLINSVillageTarget))
            )
            .andExpect(status().isOk());

        // Validate the LLINSVillageTarget in the database
        List<LLINSVillageTarget> lLINSVillageTargetList = lLINSVillageTargetRepository.findAll();
        assertThat(lLINSVillageTargetList).hasSize(databaseSizeBeforeUpdate);
        LLINSVillageTarget testLLINSVillageTarget = lLINSVillageTargetList.get(lLINSVillageTargetList.size() - 1);
        assertThat(testLLINSVillageTarget.getUid()).isEqualTo(DEFAULT_UID);
        assertThat(testLLINSVillageTarget.getCreated()).isEqualTo(DEFAULT_CREATED);
        assertThat(testLLINSVillageTarget.getLastUpdated()).isEqualTo(UPDATED_LAST_UPDATED);
        assertThat(testLLINSVillageTarget.getResidentsIndividuals()).isEqualTo(UPDATED_RESIDENTS_INDIVIDUALS);
        assertThat(testLLINSVillageTarget.getIdpsIndividuals()).isEqualTo(DEFAULT_IDPS_INDIVIDUALS);
        assertThat(testLLINSVillageTarget.getResidentsFamilies()).isEqualTo(UPDATED_RESIDENTS_FAMILIES);
        assertThat(testLLINSVillageTarget.getIdpsFamilies()).isEqualTo(UPDATED_IDPS_FAMILIES);
        assertThat(testLLINSVillageTarget.getNoOfDaysNeeded()).isEqualTo(DEFAULT_NO_OF_DAYS_NEEDED);
        assertThat(testLLINSVillageTarget.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
    }

    @Test
    @Transactional
    void fullUpdateLLINSVillageTargetWithPatch() throws Exception {
        // Initialize the database
        lLINSVillageTargetRepository.saveAndFlush(lLINSVillageTarget);

        int databaseSizeBeforeUpdate = lLINSVillageTargetRepository.findAll().size();

        // Update the lLINSVillageTarget using partial update
        LLINSVillageTarget partialUpdatedLLINSVillageTarget = new LLINSVillageTarget();
        partialUpdatedLLINSVillageTarget.setId(lLINSVillageTarget.getId());

        partialUpdatedLLINSVillageTarget
            .uid(UPDATED_UID)
            .created(UPDATED_CREATED)
            .lastUpdated(UPDATED_LAST_UPDATED)
            .residentsIndividuals(UPDATED_RESIDENTS_INDIVIDUALS)
            .idpsIndividuals(UPDATED_IDPS_INDIVIDUALS)
            .residentsFamilies(UPDATED_RESIDENTS_FAMILIES)
            .idpsFamilies(UPDATED_IDPS_FAMILIES)
            .noOfDaysNeeded(UPDATED_NO_OF_DAYS_NEEDED)
            .quantity(UPDATED_QUANTITY);

        restLLINSVillageTargetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLLINSVillageTarget.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLLINSVillageTarget))
            )
            .andExpect(status().isOk());

        // Validate the LLINSVillageTarget in the database
        List<LLINSVillageTarget> lLINSVillageTargetList = lLINSVillageTargetRepository.findAll();
        assertThat(lLINSVillageTargetList).hasSize(databaseSizeBeforeUpdate);
        LLINSVillageTarget testLLINSVillageTarget = lLINSVillageTargetList.get(lLINSVillageTargetList.size() - 1);
        assertThat(testLLINSVillageTarget.getUid()).isEqualTo(UPDATED_UID);
        assertThat(testLLINSVillageTarget.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testLLINSVillageTarget.getLastUpdated()).isEqualTo(UPDATED_LAST_UPDATED);
        assertThat(testLLINSVillageTarget.getResidentsIndividuals()).isEqualTo(UPDATED_RESIDENTS_INDIVIDUALS);
        assertThat(testLLINSVillageTarget.getIdpsIndividuals()).isEqualTo(UPDATED_IDPS_INDIVIDUALS);
        assertThat(testLLINSVillageTarget.getResidentsFamilies()).isEqualTo(UPDATED_RESIDENTS_FAMILIES);
        assertThat(testLLINSVillageTarget.getIdpsFamilies()).isEqualTo(UPDATED_IDPS_FAMILIES);
        assertThat(testLLINSVillageTarget.getNoOfDaysNeeded()).isEqualTo(UPDATED_NO_OF_DAYS_NEEDED);
        assertThat(testLLINSVillageTarget.getQuantity()).isEqualTo(UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void patchNonExistingLLINSVillageTarget() throws Exception {
        int databaseSizeBeforeUpdate = lLINSVillageTargetRepository.findAll().size();
        lLINSVillageTarget.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLLINSVillageTargetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, lLINSVillageTarget.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(lLINSVillageTarget))
            )
            .andExpect(status().isBadRequest());

        // Validate the LLINSVillageTarget in the database
        List<LLINSVillageTarget> lLINSVillageTargetList = lLINSVillageTargetRepository.findAll();
        assertThat(lLINSVillageTargetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLLINSVillageTarget() throws Exception {
        int databaseSizeBeforeUpdate = lLINSVillageTargetRepository.findAll().size();
        lLINSVillageTarget.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLLINSVillageTargetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(lLINSVillageTarget))
            )
            .andExpect(status().isBadRequest());

        // Validate the LLINSVillageTarget in the database
        List<LLINSVillageTarget> lLINSVillageTargetList = lLINSVillageTargetRepository.findAll();
        assertThat(lLINSVillageTargetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLLINSVillageTarget() throws Exception {
        int databaseSizeBeforeUpdate = lLINSVillageTargetRepository.findAll().size();
        lLINSVillageTarget.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLLINSVillageTargetMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(lLINSVillageTarget))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LLINSVillageTarget in the database
        List<LLINSVillageTarget> lLINSVillageTargetList = lLINSVillageTargetRepository.findAll();
        assertThat(lLINSVillageTargetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLLINSVillageTarget() throws Exception {
        // Initialize the database
        lLINSVillageTargetRepository.saveAndFlush(lLINSVillageTarget);

        int databaseSizeBeforeDelete = lLINSVillageTargetRepository.findAll().size();

        // Delete the lLINSVillageTarget
        restLLINSVillageTargetMockMvc
            .perform(delete(ENTITY_API_URL_ID, lLINSVillageTarget.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<LLINSVillageTarget> lLINSVillageTargetList = lLINSVillageTargetRepository.findAll();
        assertThat(lLINSVillageTargetList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
