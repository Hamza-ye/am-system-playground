package org.nmcpye.activitiesmanagement.web.rest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.nmcpye.activitiesmanagement.IntegrationTest;
import org.nmcpye.activitiesmanagement.domain.Family;
import org.nmcpye.activitiesmanagement.domain.LlinsFamilyTarget;
import org.nmcpye.activitiesmanagement.domain.Team;
import org.nmcpye.activitiesmanagement.domain.WorkingDay;
import org.nmcpye.activitiesmanagement.domain.enumeration.FamilyType;
import org.nmcpye.activitiesmanagement.domain.enumeration.StatusOfFamilyTarget;
import org.nmcpye.activitiesmanagement.repository.LlinsFamilyTargetRepository;
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
 * Integration tests for the {@link LlinsFamilyTargetResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class LlinsFamilyTargetResourceIT {

    private static final String DEFAULT_UID = "AAAAAAAAAA";
    private static final String UPDATED_UID = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_LAST_UPDATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_UPDATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Integer DEFAULT_RESIDENTS_INDIVIDUALS_PLANNED = 0;
    private static final Integer UPDATED_RESIDENTS_INDIVIDUALS_PLANNED = 1;

    private static final Integer DEFAULT_IDPS_INDIVIDUALS_PLANNED = 0;
    private static final Integer UPDATED_IDPS_INDIVIDUALS_PLANNED = 1;

    private static final Integer DEFAULT_QUANTITY_PLANNED = 0;
    private static final Integer UPDATED_QUANTITY_PLANNED = 1;

    private static final FamilyType DEFAULT_FAMILY_TYPE = FamilyType.RESIDENT;
    private static final FamilyType UPDATED_FAMILY_TYPE = FamilyType.IDPS;

    private static final StatusOfFamilyTarget DEFAULT_STATUS_OF_FAMILY_TARGET = StatusOfFamilyTarget.REACHED;
    private static final StatusOfFamilyTarget UPDATED_STATUS_OF_FAMILY_TARGET = StatusOfFamilyTarget.NOT_REACHED_YET;

    private static final String ENTITY_API_URL = "/api/llins-family-targets";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private LlinsFamilyTargetRepository llinsFamilyTargetRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLlinsFamilyTargetMockMvc;

    private LlinsFamilyTarget llinsFamilyTarget;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LlinsFamilyTarget createEntity(EntityManager em) {
        LlinsFamilyTarget llinsFamilyTarget = new LlinsFamilyTarget()
            .uid(DEFAULT_UID)
            .created(DEFAULT_CREATED)
            .lastUpdated(DEFAULT_LAST_UPDATED)
            .residentsIndividualsPlanned(DEFAULT_RESIDENTS_INDIVIDUALS_PLANNED)
            .idpsIndividualsPlanned(DEFAULT_IDPS_INDIVIDUALS_PLANNED)
            .quantityPlanned(DEFAULT_QUANTITY_PLANNED)
            .familyType(DEFAULT_FAMILY_TYPE)
            .statusOfFamilyTarget(DEFAULT_STATUS_OF_FAMILY_TARGET);
        // Add required entity
        WorkingDay workingDay;
        if (TestUtil.findAll(em, WorkingDay.class).isEmpty()) {
            workingDay = WorkingDayResourceIT.createEntity(em);
            em.persist(workingDay);
            em.flush();
        } else {
            workingDay = TestUtil.findAll(em, WorkingDay.class).get(0);
        }
        llinsFamilyTarget.setDayPlanned(workingDay);
        // Add required entity
        Family family;
        if (TestUtil.findAll(em, Family.class).isEmpty()) {
            family = FamilyResourceIT.createEntity(em);
            em.persist(family);
            em.flush();
        } else {
            family = TestUtil.findAll(em, Family.class).get(0);
        }
        llinsFamilyTarget.setFamily(family);
        // Add required entity
        Team team;
        if (TestUtil.findAll(em, Team.class).isEmpty()) {
            team = TeamResourceIT.createEntity(em);
            em.persist(team);
            em.flush();
        } else {
            team = TestUtil.findAll(em, Team.class).get(0);
        }
        llinsFamilyTarget.setTeamAssigned(team);
        return llinsFamilyTarget;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LlinsFamilyTarget createUpdatedEntity(EntityManager em) {
        LlinsFamilyTarget llinsFamilyTarget = new LlinsFamilyTarget()
            .uid(UPDATED_UID)
            .created(UPDATED_CREATED)
            .lastUpdated(UPDATED_LAST_UPDATED)
            .residentsIndividualsPlanned(UPDATED_RESIDENTS_INDIVIDUALS_PLANNED)
            .idpsIndividualsPlanned(UPDATED_IDPS_INDIVIDUALS_PLANNED)
            .quantityPlanned(UPDATED_QUANTITY_PLANNED)
            .familyType(UPDATED_FAMILY_TYPE)
            .statusOfFamilyTarget(UPDATED_STATUS_OF_FAMILY_TARGET);
        // Add required entity
        WorkingDay workingDay;
        if (TestUtil.findAll(em, WorkingDay.class).isEmpty()) {
            workingDay = WorkingDayResourceIT.createUpdatedEntity(em);
            em.persist(workingDay);
            em.flush();
        } else {
            workingDay = TestUtil.findAll(em, WorkingDay.class).get(0);
        }
        llinsFamilyTarget.setDayPlanned(workingDay);
        // Add required entity
        Family family;
        if (TestUtil.findAll(em, Family.class).isEmpty()) {
            family = FamilyResourceIT.createUpdatedEntity(em);
            em.persist(family);
            em.flush();
        } else {
            family = TestUtil.findAll(em, Family.class).get(0);
        }
        llinsFamilyTarget.setFamily(family);
        // Add required entity
        Team team;
        if (TestUtil.findAll(em, Team.class).isEmpty()) {
            team = TeamResourceIT.createUpdatedEntity(em);
            em.persist(team);
            em.flush();
        } else {
            team = TestUtil.findAll(em, Team.class).get(0);
        }
        llinsFamilyTarget.setTeamAssigned(team);
        return llinsFamilyTarget;
    }

    @BeforeEach
    public void initTest() {
        llinsFamilyTarget = createEntity(em);
    }

    @Test
    @Transactional
    void createLlinsFamilyTarget() throws Exception {
        int databaseSizeBeforeCreate = llinsFamilyTargetRepository.findAll().size();
        // Create the LlinsFamilyTarget
        restLlinsFamilyTargetMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(llinsFamilyTarget))
            )
            .andExpect(status().isCreated());

        // Validate the LlinsFamilyTarget in the database
        List<LlinsFamilyTarget> llinsFamilyTargetList = llinsFamilyTargetRepository.findAll();
        assertThat(llinsFamilyTargetList).hasSize(databaseSizeBeforeCreate + 1);
        LlinsFamilyTarget testLlinsFamilyTarget = llinsFamilyTargetList.get(llinsFamilyTargetList.size() - 1);
        assertThat(testLlinsFamilyTarget.getUid()).isEqualTo(DEFAULT_UID);
        assertThat(testLlinsFamilyTarget.getCreated()).isEqualTo(DEFAULT_CREATED);
        assertThat(testLlinsFamilyTarget.getLastUpdated()).isEqualTo(DEFAULT_LAST_UPDATED);
        assertThat(testLlinsFamilyTarget.getResidentsIndividualsPlanned()).isEqualTo(DEFAULT_RESIDENTS_INDIVIDUALS_PLANNED);
        assertThat(testLlinsFamilyTarget.getIdpsIndividualsPlanned()).isEqualTo(DEFAULT_IDPS_INDIVIDUALS_PLANNED);
        assertThat(testLlinsFamilyTarget.getQuantityPlanned()).isEqualTo(DEFAULT_QUANTITY_PLANNED);
        assertThat(testLlinsFamilyTarget.getFamilyType()).isEqualTo(DEFAULT_FAMILY_TYPE);
        assertThat(testLlinsFamilyTarget.getStatusOfFamilyTarget()).isEqualTo(DEFAULT_STATUS_OF_FAMILY_TARGET);
    }

    @Test
    @Transactional
    void createLlinsFamilyTargetWithExistingId() throws Exception {
        // Create the LlinsFamilyTarget with an existing ID
        llinsFamilyTarget.setId(1L);

        int databaseSizeBeforeCreate = llinsFamilyTargetRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLlinsFamilyTargetMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(llinsFamilyTarget))
            )
            .andExpect(status().isBadRequest());

        // Validate the LlinsFamilyTarget in the database
        List<LlinsFamilyTarget> llinsFamilyTargetList = llinsFamilyTargetRepository.findAll();
        assertThat(llinsFamilyTargetList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkUidIsRequired() throws Exception {
        int databaseSizeBeforeTest = llinsFamilyTargetRepository.findAll().size();
        // set the field null
        llinsFamilyTarget.setUid(null);

        // Create the LlinsFamilyTarget, which fails.

        restLlinsFamilyTargetMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(llinsFamilyTarget))
            )
            .andExpect(status().isBadRequest());

        List<LlinsFamilyTarget> llinsFamilyTargetList = llinsFamilyTargetRepository.findAll();
        assertThat(llinsFamilyTargetList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkResidentsIndividualsPlannedIsRequired() throws Exception {
        int databaseSizeBeforeTest = llinsFamilyTargetRepository.findAll().size();
        // set the field null
        llinsFamilyTarget.setResidentsIndividualsPlanned(null);

        // Create the LlinsFamilyTarget, which fails.

        restLlinsFamilyTargetMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(llinsFamilyTarget))
            )
            .andExpect(status().isBadRequest());

        List<LlinsFamilyTarget> llinsFamilyTargetList = llinsFamilyTargetRepository.findAll();
        assertThat(llinsFamilyTargetList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIdpsIndividualsPlannedIsRequired() throws Exception {
        int databaseSizeBeforeTest = llinsFamilyTargetRepository.findAll().size();
        // set the field null
        llinsFamilyTarget.setIdpsIndividualsPlanned(null);

        // Create the LlinsFamilyTarget, which fails.

        restLlinsFamilyTargetMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(llinsFamilyTarget))
            )
            .andExpect(status().isBadRequest());

        List<LlinsFamilyTarget> llinsFamilyTargetList = llinsFamilyTargetRepository.findAll();
        assertThat(llinsFamilyTargetList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkQuantityPlannedIsRequired() throws Exception {
        int databaseSizeBeforeTest = llinsFamilyTargetRepository.findAll().size();
        // set the field null
        llinsFamilyTarget.setQuantityPlanned(null);

        // Create the LlinsFamilyTarget, which fails.

        restLlinsFamilyTargetMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(llinsFamilyTarget))
            )
            .andExpect(status().isBadRequest());

        List<LlinsFamilyTarget> llinsFamilyTargetList = llinsFamilyTargetRepository.findAll();
        assertThat(llinsFamilyTargetList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFamilyTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = llinsFamilyTargetRepository.findAll().size();
        // set the field null
        llinsFamilyTarget.setFamilyType(null);

        // Create the LlinsFamilyTarget, which fails.

        restLlinsFamilyTargetMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(llinsFamilyTarget))
            )
            .andExpect(status().isBadRequest());

        List<LlinsFamilyTarget> llinsFamilyTargetList = llinsFamilyTargetRepository.findAll();
        assertThat(llinsFamilyTargetList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusOfFamilyTargetIsRequired() throws Exception {
        int databaseSizeBeforeTest = llinsFamilyTargetRepository.findAll().size();
        // set the field null
        llinsFamilyTarget.setStatusOfFamilyTarget(null);

        // Create the LlinsFamilyTarget, which fails.

        restLlinsFamilyTargetMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(llinsFamilyTarget))
            )
            .andExpect(status().isBadRequest());

        List<LlinsFamilyTarget> llinsFamilyTargetList = llinsFamilyTargetRepository.findAll();
        assertThat(llinsFamilyTargetList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllLlinsFamilyTargets() throws Exception {
        // Initialize the database
        llinsFamilyTargetRepository.saveAndFlush(llinsFamilyTarget);

        // Get all the llinsFamilyTargetList
        restLlinsFamilyTargetMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(llinsFamilyTarget.getId().intValue())))
            .andExpect(jsonPath("$.[*].uid").value(hasItem(DEFAULT_UID)))
            .andExpect(jsonPath("$.[*].created").value(hasItem(DEFAULT_CREATED.toString())))
            .andExpect(jsonPath("$.[*].lastUpdated").value(hasItem(DEFAULT_LAST_UPDATED.toString())))
            .andExpect(jsonPath("$.[*].residentsIndividualsPlanned").value(hasItem(DEFAULT_RESIDENTS_INDIVIDUALS_PLANNED)))
            .andExpect(jsonPath("$.[*].idpsIndividualsPlanned").value(hasItem(DEFAULT_IDPS_INDIVIDUALS_PLANNED)))
            .andExpect(jsonPath("$.[*].quantityPlanned").value(hasItem(DEFAULT_QUANTITY_PLANNED)))
            .andExpect(jsonPath("$.[*].familyType").value(hasItem(DEFAULT_FAMILY_TYPE.toString())))
            .andExpect(jsonPath("$.[*].statusOfFamilyTarget").value(hasItem(DEFAULT_STATUS_OF_FAMILY_TARGET.toString())));
    }

    @Test
    @Transactional
    void getLlinsFamilyTarget() throws Exception {
        // Initialize the database
        llinsFamilyTargetRepository.saveAndFlush(llinsFamilyTarget);

        // Get the llinsFamilyTarget
        restLlinsFamilyTargetMockMvc
            .perform(get(ENTITY_API_URL_ID, llinsFamilyTarget.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(llinsFamilyTarget.getId().intValue()))
            .andExpect(jsonPath("$.uid").value(DEFAULT_UID))
            .andExpect(jsonPath("$.created").value(DEFAULT_CREATED.toString()))
            .andExpect(jsonPath("$.lastUpdated").value(DEFAULT_LAST_UPDATED.toString()))
            .andExpect(jsonPath("$.residentsIndividualsPlanned").value(DEFAULT_RESIDENTS_INDIVIDUALS_PLANNED))
            .andExpect(jsonPath("$.idpsIndividualsPlanned").value(DEFAULT_IDPS_INDIVIDUALS_PLANNED))
            .andExpect(jsonPath("$.quantityPlanned").value(DEFAULT_QUANTITY_PLANNED))
            .andExpect(jsonPath("$.familyType").value(DEFAULT_FAMILY_TYPE.toString()))
            .andExpect(jsonPath("$.statusOfFamilyTarget").value(DEFAULT_STATUS_OF_FAMILY_TARGET.toString()));
    }

    @Test
    @Transactional
    void getNonExistingLlinsFamilyTarget() throws Exception {
        // Get the llinsFamilyTarget
        restLlinsFamilyTargetMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewLlinsFamilyTarget() throws Exception {
        // Initialize the database
        llinsFamilyTargetRepository.saveAndFlush(llinsFamilyTarget);

        int databaseSizeBeforeUpdate = llinsFamilyTargetRepository.findAll().size();

        // Update the llinsFamilyTarget
        LlinsFamilyTarget updatedLlinsFamilyTarget = llinsFamilyTargetRepository.findById(llinsFamilyTarget.getId()).get();
        // Disconnect from session so that the updates on updatedLlinsFamilyTarget are not directly saved in db
        em.detach(updatedLlinsFamilyTarget);
        updatedLlinsFamilyTarget
            .uid(UPDATED_UID)
            .created(UPDATED_CREATED)
            .lastUpdated(UPDATED_LAST_UPDATED)
            .residentsIndividualsPlanned(UPDATED_RESIDENTS_INDIVIDUALS_PLANNED)
            .idpsIndividualsPlanned(UPDATED_IDPS_INDIVIDUALS_PLANNED)
            .quantityPlanned(UPDATED_QUANTITY_PLANNED)
            .familyType(UPDATED_FAMILY_TYPE)
            .statusOfFamilyTarget(UPDATED_STATUS_OF_FAMILY_TARGET);

        restLlinsFamilyTargetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedLlinsFamilyTarget.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedLlinsFamilyTarget))
            )
            .andExpect(status().isOk());

        // Validate the LlinsFamilyTarget in the database
        List<LlinsFamilyTarget> llinsFamilyTargetList = llinsFamilyTargetRepository.findAll();
        assertThat(llinsFamilyTargetList).hasSize(databaseSizeBeforeUpdate);
        LlinsFamilyTarget testLlinsFamilyTarget = llinsFamilyTargetList.get(llinsFamilyTargetList.size() - 1);
        assertThat(testLlinsFamilyTarget.getUid()).isEqualTo(UPDATED_UID);
        assertThat(testLlinsFamilyTarget.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testLlinsFamilyTarget.getLastUpdated()).isEqualTo(UPDATED_LAST_UPDATED);
        assertThat(testLlinsFamilyTarget.getResidentsIndividualsPlanned()).isEqualTo(UPDATED_RESIDENTS_INDIVIDUALS_PLANNED);
        assertThat(testLlinsFamilyTarget.getIdpsIndividualsPlanned()).isEqualTo(UPDATED_IDPS_INDIVIDUALS_PLANNED);
        assertThat(testLlinsFamilyTarget.getQuantityPlanned()).isEqualTo(UPDATED_QUANTITY_PLANNED);
        assertThat(testLlinsFamilyTarget.getFamilyType()).isEqualTo(UPDATED_FAMILY_TYPE);
        assertThat(testLlinsFamilyTarget.getStatusOfFamilyTarget()).isEqualTo(UPDATED_STATUS_OF_FAMILY_TARGET);
    }

    @Test
    @Transactional
    void putNonExistingLlinsFamilyTarget() throws Exception {
        int databaseSizeBeforeUpdate = llinsFamilyTargetRepository.findAll().size();
        llinsFamilyTarget.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLlinsFamilyTargetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, llinsFamilyTarget.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(llinsFamilyTarget))
            )
            .andExpect(status().isBadRequest());

        // Validate the LlinsFamilyTarget in the database
        List<LlinsFamilyTarget> llinsFamilyTargetList = llinsFamilyTargetRepository.findAll();
        assertThat(llinsFamilyTargetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLlinsFamilyTarget() throws Exception {
        int databaseSizeBeforeUpdate = llinsFamilyTargetRepository.findAll().size();
        llinsFamilyTarget.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLlinsFamilyTargetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(llinsFamilyTarget))
            )
            .andExpect(status().isBadRequest());

        // Validate the LlinsFamilyTarget in the database
        List<LlinsFamilyTarget> llinsFamilyTargetList = llinsFamilyTargetRepository.findAll();
        assertThat(llinsFamilyTargetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLlinsFamilyTarget() throws Exception {
        int databaseSizeBeforeUpdate = llinsFamilyTargetRepository.findAll().size();
        llinsFamilyTarget.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLlinsFamilyTargetMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(llinsFamilyTarget))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LlinsFamilyTarget in the database
        List<LlinsFamilyTarget> llinsFamilyTargetList = llinsFamilyTargetRepository.findAll();
        assertThat(llinsFamilyTargetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLlinsFamilyTargetWithPatch() throws Exception {
        // Initialize the database
        llinsFamilyTargetRepository.saveAndFlush(llinsFamilyTarget);

        int databaseSizeBeforeUpdate = llinsFamilyTargetRepository.findAll().size();

        // Update the llinsFamilyTarget using partial update
        LlinsFamilyTarget partialUpdatedLlinsFamilyTarget = new LlinsFamilyTarget();
        partialUpdatedLlinsFamilyTarget.setId(llinsFamilyTarget.getId());

        restLlinsFamilyTargetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLlinsFamilyTarget.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLlinsFamilyTarget))
            )
            .andExpect(status().isOk());

        // Validate the LlinsFamilyTarget in the database
        List<LlinsFamilyTarget> llinsFamilyTargetList = llinsFamilyTargetRepository.findAll();
        assertThat(llinsFamilyTargetList).hasSize(databaseSizeBeforeUpdate);
        LlinsFamilyTarget testLlinsFamilyTarget = llinsFamilyTargetList.get(llinsFamilyTargetList.size() - 1);
        assertThat(testLlinsFamilyTarget.getUid()).isEqualTo(DEFAULT_UID);
        assertThat(testLlinsFamilyTarget.getCreated()).isEqualTo(DEFAULT_CREATED);
        assertThat(testLlinsFamilyTarget.getLastUpdated()).isEqualTo(DEFAULT_LAST_UPDATED);
        assertThat(testLlinsFamilyTarget.getResidentsIndividualsPlanned()).isEqualTo(DEFAULT_RESIDENTS_INDIVIDUALS_PLANNED);
        assertThat(testLlinsFamilyTarget.getIdpsIndividualsPlanned()).isEqualTo(DEFAULT_IDPS_INDIVIDUALS_PLANNED);
        assertThat(testLlinsFamilyTarget.getQuantityPlanned()).isEqualTo(DEFAULT_QUANTITY_PLANNED);
        assertThat(testLlinsFamilyTarget.getFamilyType()).isEqualTo(DEFAULT_FAMILY_TYPE);
        assertThat(testLlinsFamilyTarget.getStatusOfFamilyTarget()).isEqualTo(DEFAULT_STATUS_OF_FAMILY_TARGET);
    }

    @Test
    @Transactional
    void fullUpdateLlinsFamilyTargetWithPatch() throws Exception {
        // Initialize the database
        llinsFamilyTargetRepository.saveAndFlush(llinsFamilyTarget);

        int databaseSizeBeforeUpdate = llinsFamilyTargetRepository.findAll().size();

        // Update the llinsFamilyTarget using partial update
        LlinsFamilyTarget partialUpdatedLlinsFamilyTarget = new LlinsFamilyTarget();
        partialUpdatedLlinsFamilyTarget.setId(llinsFamilyTarget.getId());

        partialUpdatedLlinsFamilyTarget
            .uid(UPDATED_UID)
            .created(UPDATED_CREATED)
            .lastUpdated(UPDATED_LAST_UPDATED)
            .residentsIndividualsPlanned(UPDATED_RESIDENTS_INDIVIDUALS_PLANNED)
            .idpsIndividualsPlanned(UPDATED_IDPS_INDIVIDUALS_PLANNED)
            .quantityPlanned(UPDATED_QUANTITY_PLANNED)
            .familyType(UPDATED_FAMILY_TYPE)
            .statusOfFamilyTarget(UPDATED_STATUS_OF_FAMILY_TARGET);

        restLlinsFamilyTargetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLlinsFamilyTarget.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLlinsFamilyTarget))
            )
            .andExpect(status().isOk());

        // Validate the LlinsFamilyTarget in the database
        List<LlinsFamilyTarget> llinsFamilyTargetList = llinsFamilyTargetRepository.findAll();
        assertThat(llinsFamilyTargetList).hasSize(databaseSizeBeforeUpdate);
        LlinsFamilyTarget testLlinsFamilyTarget = llinsFamilyTargetList.get(llinsFamilyTargetList.size() - 1);
        assertThat(testLlinsFamilyTarget.getUid()).isEqualTo(UPDATED_UID);
        assertThat(testLlinsFamilyTarget.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testLlinsFamilyTarget.getLastUpdated()).isEqualTo(UPDATED_LAST_UPDATED);
        assertThat(testLlinsFamilyTarget.getResidentsIndividualsPlanned()).isEqualTo(UPDATED_RESIDENTS_INDIVIDUALS_PLANNED);
        assertThat(testLlinsFamilyTarget.getIdpsIndividualsPlanned()).isEqualTo(UPDATED_IDPS_INDIVIDUALS_PLANNED);
        assertThat(testLlinsFamilyTarget.getQuantityPlanned()).isEqualTo(UPDATED_QUANTITY_PLANNED);
        assertThat(testLlinsFamilyTarget.getFamilyType()).isEqualTo(UPDATED_FAMILY_TYPE);
        assertThat(testLlinsFamilyTarget.getStatusOfFamilyTarget()).isEqualTo(UPDATED_STATUS_OF_FAMILY_TARGET);
    }

    @Test
    @Transactional
    void patchNonExistingLlinsFamilyTarget() throws Exception {
        int databaseSizeBeforeUpdate = llinsFamilyTargetRepository.findAll().size();
        llinsFamilyTarget.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLlinsFamilyTargetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, llinsFamilyTarget.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(llinsFamilyTarget))
            )
            .andExpect(status().isBadRequest());

        // Validate the LlinsFamilyTarget in the database
        List<LlinsFamilyTarget> llinsFamilyTargetList = llinsFamilyTargetRepository.findAll();
        assertThat(llinsFamilyTargetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLlinsFamilyTarget() throws Exception {
        int databaseSizeBeforeUpdate = llinsFamilyTargetRepository.findAll().size();
        llinsFamilyTarget.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLlinsFamilyTargetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(llinsFamilyTarget))
            )
            .andExpect(status().isBadRequest());

        // Validate the LlinsFamilyTarget in the database
        List<LlinsFamilyTarget> llinsFamilyTargetList = llinsFamilyTargetRepository.findAll();
        assertThat(llinsFamilyTargetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLlinsFamilyTarget() throws Exception {
        int databaseSizeBeforeUpdate = llinsFamilyTargetRepository.findAll().size();
        llinsFamilyTarget.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLlinsFamilyTargetMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(llinsFamilyTarget))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LlinsFamilyTarget in the database
        List<LlinsFamilyTarget> llinsFamilyTargetList = llinsFamilyTargetRepository.findAll();
        assertThat(llinsFamilyTargetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLlinsFamilyTarget() throws Exception {
        // Initialize the database
        llinsFamilyTargetRepository.saveAndFlush(llinsFamilyTarget);

        int databaseSizeBeforeDelete = llinsFamilyTargetRepository.findAll().size();

        // Delete the llinsFamilyTarget
        restLlinsFamilyTargetMockMvc
            .perform(delete(ENTITY_API_URL_ID, llinsFamilyTarget.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<LlinsFamilyTarget> llinsFamilyTargetList = llinsFamilyTargetRepository.findAll();
        assertThat(llinsFamilyTargetList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
