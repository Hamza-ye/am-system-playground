package org.nmcpye.activitiesmanagement.web.rest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.nmcpye.activitiesmanagement.IntegrationTest;
import org.nmcpye.activitiesmanagement.domain.LlinsVillageTarget;
import org.nmcpye.activitiesmanagement.domain.Team;
import org.nmcpye.activitiesmanagement.domain.WorkingDay;
import org.nmcpye.activitiesmanagement.domain.organisationunit.OrganisationUnit;
import org.nmcpye.activitiesmanagement.repository.LlinsVillageTargetRepository;
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
 * Integration tests for the {@link LlinsVillageTargetResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class LlinsVillageTargetResourceIT {

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
    private LlinsVillageTargetRepository llinsVillageTargetRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLlinsVillageTargetMockMvc;

    private LlinsVillageTarget llinsVillageTarget;

    /**
     * Create an entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LlinsVillageTarget createEntity(EntityManager em) {
        LlinsVillageTarget llinsVillageTarget = new LlinsVillageTarget()
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
        llinsVillageTarget.setOrganisationUnit(organisationUnit);
        // Add required entity
        WorkingDay workingDay;
        if (TestUtil.findAll(em, WorkingDay.class).isEmpty()) {
            workingDay = WorkingDayResourceIT.createEntity(em);
            em.persist(workingDay);
            em.flush();
        } else {
            workingDay = TestUtil.findAll(em, WorkingDay.class).get(0);
        }
        llinsVillageTarget.setDayPlanned(workingDay);
        // Add required entity
        Team team;
        if (TestUtil.findAll(em, Team.class).isEmpty()) {
            team = TeamResourceIT.createEntity(em);
            em.persist(team);
            em.flush();
        } else {
            team = TestUtil.findAll(em, Team.class).get(0);
        }
        llinsVillageTarget.setTeamAssigned(team);
        return llinsVillageTarget;
    }

    /**
     * Create an updated entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LlinsVillageTarget createUpdatedEntity(EntityManager em) {
        LlinsVillageTarget llinsVillageTarget = new LlinsVillageTarget()
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
        llinsVillageTarget.setOrganisationUnit(organisationUnit);
        // Add required entity
        WorkingDay workingDay;
        if (TestUtil.findAll(em, WorkingDay.class).isEmpty()) {
            workingDay = WorkingDayResourceIT.createUpdatedEntity(em);
            em.persist(workingDay);
            em.flush();
        } else {
            workingDay = TestUtil.findAll(em, WorkingDay.class).get(0);
        }
        llinsVillageTarget.setDayPlanned(workingDay);
        // Add required entity
        Team team;
        if (TestUtil.findAll(em, Team.class).isEmpty()) {
            team = TeamResourceIT.createUpdatedEntity(em);
            em.persist(team);
            em.flush();
        } else {
            team = TestUtil.findAll(em, Team.class).get(0);
        }
        llinsVillageTarget.setTeamAssigned(team);
        return llinsVillageTarget;
    }

    @BeforeEach
    public void initTest() {
        llinsVillageTarget = createEntity(em);
    }

    @Test
    @Transactional
    void createLlinsVillageTarget() throws Exception {
        int databaseSizeBeforeCreate = llinsVillageTargetRepository.findAll().size();
        // Create the LlinsVillageTarget
        restLlinsVillageTargetMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(llinsVillageTarget))
            )
            .andExpect(status().isCreated());

        // Validate the LlinsVillageTarget in the database
        List<LlinsVillageTarget> llinsVillageTargetList = llinsVillageTargetRepository.findAll();
        assertThat(llinsVillageTargetList).hasSize(databaseSizeBeforeCreate + 1);
        LlinsVillageTarget testLlinsVillageTarget = llinsVillageTargetList.get(llinsVillageTargetList.size() - 1);
        assertThat(testLlinsVillageTarget.getUid()).isEqualTo(DEFAULT_UID);
        assertThat(testLlinsVillageTarget.getCreated()).isEqualTo(DEFAULT_CREATED);
        assertThat(testLlinsVillageTarget.getLastUpdated()).isEqualTo(DEFAULT_LAST_UPDATED);
        assertThat(testLlinsVillageTarget.getResidentsIndividuals()).isEqualTo(DEFAULT_RESIDENTS_INDIVIDUALS);
        assertThat(testLlinsVillageTarget.getIdpsIndividuals()).isEqualTo(DEFAULT_IDPS_INDIVIDUALS);
        assertThat(testLlinsVillageTarget.getResidentsFamilies()).isEqualTo(DEFAULT_RESIDENTS_FAMILIES);
        assertThat(testLlinsVillageTarget.getIdpsFamilies()).isEqualTo(DEFAULT_IDPS_FAMILIES);
        assertThat(testLlinsVillageTarget.getNoOfDaysNeeded()).isEqualTo(DEFAULT_NO_OF_DAYS_NEEDED);
        assertThat(testLlinsVillageTarget.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
    }

    @Test
    @Transactional
    void createLlinsVillageTargetWithExistingId() throws Exception {
        // Create the LlinsVillageTarget with an existing ID
        llinsVillageTarget.setId(1L);

        int databaseSizeBeforeCreate = llinsVillageTargetRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLlinsVillageTargetMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(llinsVillageTarget))
            )
            .andExpect(status().isBadRequest());

        // Validate the LlinsVillageTarget in the database
        List<LlinsVillageTarget> llinsVillageTargetList = llinsVillageTargetRepository.findAll();
        assertThat(llinsVillageTargetList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkUidIsRequired() throws Exception {
        int databaseSizeBeforeTest = llinsVillageTargetRepository.findAll().size();
        // set the field null
        llinsVillageTarget.setUid(null);

        // Create the LlinsVillageTarget, which fails.

        restLlinsVillageTargetMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(llinsVillageTarget))
            )
            .andExpect(status().isBadRequest());

        List<LlinsVillageTarget> llinsVillageTargetList = llinsVillageTargetRepository.findAll();
        assertThat(llinsVillageTargetList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkResidentsIndividualsIsRequired() throws Exception {
        int databaseSizeBeforeTest = llinsVillageTargetRepository.findAll().size();
        // set the field null
        llinsVillageTarget.setResidentsIndividuals(null);

        // Create the LlinsVillageTarget, which fails.

        restLlinsVillageTargetMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(llinsVillageTarget))
            )
            .andExpect(status().isBadRequest());

        List<LlinsVillageTarget> llinsVillageTargetList = llinsVillageTargetRepository.findAll();
        assertThat(llinsVillageTargetList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIdpsIndividualsIsRequired() throws Exception {
        int databaseSizeBeforeTest = llinsVillageTargetRepository.findAll().size();
        // set the field null
        llinsVillageTarget.setIdpsIndividuals(null);

        // Create the LlinsVillageTarget, which fails.

        restLlinsVillageTargetMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(llinsVillageTarget))
            )
            .andExpect(status().isBadRequest());

        List<LlinsVillageTarget> llinsVillageTargetList = llinsVillageTargetRepository.findAll();
        assertThat(llinsVillageTargetList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkResidentsFamiliesIsRequired() throws Exception {
        int databaseSizeBeforeTest = llinsVillageTargetRepository.findAll().size();
        // set the field null
        llinsVillageTarget.setResidentsFamilies(null);

        // Create the LlinsVillageTarget, which fails.

        restLlinsVillageTargetMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(llinsVillageTarget))
            )
            .andExpect(status().isBadRequest());

        List<LlinsVillageTarget> llinsVillageTargetList = llinsVillageTargetRepository.findAll();
        assertThat(llinsVillageTargetList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIdpsFamiliesIsRequired() throws Exception {
        int databaseSizeBeforeTest = llinsVillageTargetRepository.findAll().size();
        // set the field null
        llinsVillageTarget.setIdpsFamilies(null);

        // Create the LlinsVillageTarget, which fails.

        restLlinsVillageTargetMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(llinsVillageTarget))
            )
            .andExpect(status().isBadRequest());

        List<LlinsVillageTarget> llinsVillageTargetList = llinsVillageTargetRepository.findAll();
        assertThat(llinsVillageTargetList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkQuantityIsRequired() throws Exception {
        int databaseSizeBeforeTest = llinsVillageTargetRepository.findAll().size();
        // set the field null
        llinsVillageTarget.setQuantity(null);

        // Create the LlinsVillageTarget, which fails.

        restLlinsVillageTargetMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(llinsVillageTarget))
            )
            .andExpect(status().isBadRequest());

        List<LlinsVillageTarget> llinsVillageTargetList = llinsVillageTargetRepository.findAll();
        assertThat(llinsVillageTargetList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllLlinsVillageTargets() throws Exception {
        // Initialize the database
        llinsVillageTargetRepository.saveAndFlush(llinsVillageTarget);

        // Get all the llinsVillageTargetList
        restLlinsVillageTargetMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(llinsVillageTarget.getId().intValue())))
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
    void getLlinsVillageTarget() throws Exception {
        // Initialize the database
        llinsVillageTargetRepository.saveAndFlush(llinsVillageTarget);

        // Get the llinsVillageTarget
        restLlinsVillageTargetMockMvc
            .perform(get(ENTITY_API_URL_ID, llinsVillageTarget.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(llinsVillageTarget.getId().intValue()))
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
    void getNonExistingLlinsVillageTarget() throws Exception {
        // Get the llinsVillageTarget
        restLlinsVillageTargetMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewLlinsVillageTarget() throws Exception {
        // Initialize the database
        llinsVillageTargetRepository.saveAndFlush(llinsVillageTarget);

        int databaseSizeBeforeUpdate = llinsVillageTargetRepository.findAll().size();

        // Update the llinsVillageTarget
        LlinsVillageTarget updatedLlinsVillageTarget = llinsVillageTargetRepository.findById(llinsVillageTarget.getId()).get();
        // Disconnect from session so that the updates on updatedLlinsVillageTarget are not directly saved in db
        em.detach(updatedLlinsVillageTarget);
        updatedLlinsVillageTarget
            .uid(UPDATED_UID)
            .created(UPDATED_CREATED)
            .lastUpdated(UPDATED_LAST_UPDATED)
            .residentsIndividuals(UPDATED_RESIDENTS_INDIVIDUALS)
            .idpsIndividuals(UPDATED_IDPS_INDIVIDUALS)
            .residentsFamilies(UPDATED_RESIDENTS_FAMILIES)
            .idpsFamilies(UPDATED_IDPS_FAMILIES)
            .noOfDaysNeeded(UPDATED_NO_OF_DAYS_NEEDED)
            .quantity(UPDATED_QUANTITY);

        restLlinsVillageTargetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedLlinsVillageTarget.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedLlinsVillageTarget))
            )
            .andExpect(status().isOk());

        // Validate the LlinsVillageTarget in the database
        List<LlinsVillageTarget> llinsVillageTargetList = llinsVillageTargetRepository.findAll();
        assertThat(llinsVillageTargetList).hasSize(databaseSizeBeforeUpdate);
        LlinsVillageTarget testLlinsVillageTarget = llinsVillageTargetList.get(llinsVillageTargetList.size() - 1);
        assertThat(testLlinsVillageTarget.getUid()).isEqualTo(UPDATED_UID);
        assertThat(testLlinsVillageTarget.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testLlinsVillageTarget.getLastUpdated()).isEqualTo(UPDATED_LAST_UPDATED);
        assertThat(testLlinsVillageTarget.getResidentsIndividuals()).isEqualTo(UPDATED_RESIDENTS_INDIVIDUALS);
        assertThat(testLlinsVillageTarget.getIdpsIndividuals()).isEqualTo(UPDATED_IDPS_INDIVIDUALS);
        assertThat(testLlinsVillageTarget.getResidentsFamilies()).isEqualTo(UPDATED_RESIDENTS_FAMILIES);
        assertThat(testLlinsVillageTarget.getIdpsFamilies()).isEqualTo(UPDATED_IDPS_FAMILIES);
        assertThat(testLlinsVillageTarget.getNoOfDaysNeeded()).isEqualTo(UPDATED_NO_OF_DAYS_NEEDED);
        assertThat(testLlinsVillageTarget.getQuantity()).isEqualTo(UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void putNonExistingLlinsVillageTarget() throws Exception {
        int databaseSizeBeforeUpdate = llinsVillageTargetRepository.findAll().size();
        llinsVillageTarget.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLlinsVillageTargetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, llinsVillageTarget.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(llinsVillageTarget))
            )
            .andExpect(status().isBadRequest());

        // Validate the LlinsVillageTarget in the database
        List<LlinsVillageTarget> llinsVillageTargetList = llinsVillageTargetRepository.findAll();
        assertThat(llinsVillageTargetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLlinsVillageTarget() throws Exception {
        int databaseSizeBeforeUpdate = llinsVillageTargetRepository.findAll().size();
        llinsVillageTarget.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLlinsVillageTargetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(llinsVillageTarget))
            )
            .andExpect(status().isBadRequest());

        // Validate the LlinsVillageTarget in the database
        List<LlinsVillageTarget> llinsVillageTargetList = llinsVillageTargetRepository.findAll();
        assertThat(llinsVillageTargetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLlinsVillageTarget() throws Exception {
        int databaseSizeBeforeUpdate = llinsVillageTargetRepository.findAll().size();
        llinsVillageTarget.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLlinsVillageTargetMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(llinsVillageTarget))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LlinsVillageTarget in the database
        List<LlinsVillageTarget> llinsVillageTargetList = llinsVillageTargetRepository.findAll();
        assertThat(llinsVillageTargetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLlinsVillageTargetWithPatch() throws Exception {
        // Initialize the database
        llinsVillageTargetRepository.saveAndFlush(llinsVillageTarget);

        int databaseSizeBeforeUpdate = llinsVillageTargetRepository.findAll().size();

        // Update the llinsVillageTarget using partial update
        LlinsVillageTarget partialUpdatedLlinsVillageTarget = new LlinsVillageTarget();
        partialUpdatedLlinsVillageTarget.setId(llinsVillageTarget.getId());

        partialUpdatedLlinsVillageTarget
            .uid(UPDATED_UID)
            .lastUpdated(UPDATED_LAST_UPDATED)
            .residentsFamilies(UPDATED_RESIDENTS_FAMILIES)
            .noOfDaysNeeded(UPDATED_NO_OF_DAYS_NEEDED)
            .quantity(UPDATED_QUANTITY);

        restLlinsVillageTargetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLlinsVillageTarget.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLlinsVillageTarget))
            )
            .andExpect(status().isOk());

        // Validate the LlinsVillageTarget in the database
        List<LlinsVillageTarget> llinsVillageTargetList = llinsVillageTargetRepository.findAll();
        assertThat(llinsVillageTargetList).hasSize(databaseSizeBeforeUpdate);
        LlinsVillageTarget testLlinsVillageTarget = llinsVillageTargetList.get(llinsVillageTargetList.size() - 1);
        assertThat(testLlinsVillageTarget.getUid()).isEqualTo(UPDATED_UID);
        assertThat(testLlinsVillageTarget.getCreated()).isEqualTo(DEFAULT_CREATED);
        assertThat(testLlinsVillageTarget.getLastUpdated()).isEqualTo(UPDATED_LAST_UPDATED);
        assertThat(testLlinsVillageTarget.getResidentsIndividuals()).isEqualTo(DEFAULT_RESIDENTS_INDIVIDUALS);
        assertThat(testLlinsVillageTarget.getIdpsIndividuals()).isEqualTo(DEFAULT_IDPS_INDIVIDUALS);
        assertThat(testLlinsVillageTarget.getResidentsFamilies()).isEqualTo(UPDATED_RESIDENTS_FAMILIES);
        assertThat(testLlinsVillageTarget.getIdpsFamilies()).isEqualTo(DEFAULT_IDPS_FAMILIES);
        assertThat(testLlinsVillageTarget.getNoOfDaysNeeded()).isEqualTo(UPDATED_NO_OF_DAYS_NEEDED);
        assertThat(testLlinsVillageTarget.getQuantity()).isEqualTo(UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void fullUpdateLlinsVillageTargetWithPatch() throws Exception {
        // Initialize the database
        llinsVillageTargetRepository.saveAndFlush(llinsVillageTarget);

        int databaseSizeBeforeUpdate = llinsVillageTargetRepository.findAll().size();

        // Update the llinsVillageTarget using partial update
        LlinsVillageTarget partialUpdatedLlinsVillageTarget = new LlinsVillageTarget();
        partialUpdatedLlinsVillageTarget.setId(llinsVillageTarget.getId());

        partialUpdatedLlinsVillageTarget
            .uid(UPDATED_UID)
            .created(UPDATED_CREATED)
            .lastUpdated(UPDATED_LAST_UPDATED)
            .residentsIndividuals(UPDATED_RESIDENTS_INDIVIDUALS)
            .idpsIndividuals(UPDATED_IDPS_INDIVIDUALS)
            .residentsFamilies(UPDATED_RESIDENTS_FAMILIES)
            .idpsFamilies(UPDATED_IDPS_FAMILIES)
            .noOfDaysNeeded(UPDATED_NO_OF_DAYS_NEEDED)
            .quantity(UPDATED_QUANTITY);

        restLlinsVillageTargetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLlinsVillageTarget.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLlinsVillageTarget))
            )
            .andExpect(status().isOk());

        // Validate the LlinsVillageTarget in the database
        List<LlinsVillageTarget> llinsVillageTargetList = llinsVillageTargetRepository.findAll();
        assertThat(llinsVillageTargetList).hasSize(databaseSizeBeforeUpdate);
        LlinsVillageTarget testLlinsVillageTarget = llinsVillageTargetList.get(llinsVillageTargetList.size() - 1);
        assertThat(testLlinsVillageTarget.getUid()).isEqualTo(UPDATED_UID);
        assertThat(testLlinsVillageTarget.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testLlinsVillageTarget.getLastUpdated()).isEqualTo(UPDATED_LAST_UPDATED);
        assertThat(testLlinsVillageTarget.getResidentsIndividuals()).isEqualTo(UPDATED_RESIDENTS_INDIVIDUALS);
        assertThat(testLlinsVillageTarget.getIdpsIndividuals()).isEqualTo(UPDATED_IDPS_INDIVIDUALS);
        assertThat(testLlinsVillageTarget.getResidentsFamilies()).isEqualTo(UPDATED_RESIDENTS_FAMILIES);
        assertThat(testLlinsVillageTarget.getIdpsFamilies()).isEqualTo(UPDATED_IDPS_FAMILIES);
        assertThat(testLlinsVillageTarget.getNoOfDaysNeeded()).isEqualTo(UPDATED_NO_OF_DAYS_NEEDED);
        assertThat(testLlinsVillageTarget.getQuantity()).isEqualTo(UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void patchNonExistingLlinsVillageTarget() throws Exception {
        int databaseSizeBeforeUpdate = llinsVillageTargetRepository.findAll().size();
        llinsVillageTarget.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLlinsVillageTargetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, llinsVillageTarget.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(llinsVillageTarget))
            )
            .andExpect(status().isBadRequest());

        // Validate the LlinsVillageTarget in the database
        List<LlinsVillageTarget> llinsVillageTargetList = llinsVillageTargetRepository.findAll();
        assertThat(llinsVillageTargetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLlinsVillageTarget() throws Exception {
        int databaseSizeBeforeUpdate = llinsVillageTargetRepository.findAll().size();
        llinsVillageTarget.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLlinsVillageTargetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(llinsVillageTarget))
            )
            .andExpect(status().isBadRequest());

        // Validate the LlinsVillageTarget in the database
        List<LlinsVillageTarget> llinsVillageTargetList = llinsVillageTargetRepository.findAll();
        assertThat(llinsVillageTargetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLlinsVillageTarget() throws Exception {
        int databaseSizeBeforeUpdate = llinsVillageTargetRepository.findAll().size();
        llinsVillageTarget.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLlinsVillageTargetMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(llinsVillageTarget))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LlinsVillageTarget in the database
        List<LlinsVillageTarget> llinsVillageTargetList = llinsVillageTargetRepository.findAll();
        assertThat(llinsVillageTargetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLlinsVillageTarget() throws Exception {
        // Initialize the database
        llinsVillageTargetRepository.saveAndFlush(llinsVillageTarget);

        int databaseSizeBeforeDelete = llinsVillageTargetRepository.findAll().size();

        // Delete the llinsVillageTarget
        restLlinsVillageTargetMockMvc
            .perform(delete(ENTITY_API_URL_ID, llinsVillageTarget.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<LlinsVillageTarget> llinsVillageTargetList = llinsVillageTargetRepository.findAll();
        assertThat(llinsVillageTargetList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
