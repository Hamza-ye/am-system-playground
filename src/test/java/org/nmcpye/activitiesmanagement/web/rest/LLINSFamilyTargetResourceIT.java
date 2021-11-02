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
import org.nmcpye.activitiesmanagement.domain.Family;
import org.nmcpye.activitiesmanagement.domain.LLINSFamilyTarget;
import org.nmcpye.activitiesmanagement.domain.Team;
import org.nmcpye.activitiesmanagement.domain.WorkingDay;
import org.nmcpye.activitiesmanagement.domain.enumeration.FamilyType;
import org.nmcpye.activitiesmanagement.domain.enumeration.StatusOfFamilyTarget;
import org.nmcpye.activitiesmanagement.repository.LLINSFamilyTargetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link LLINSFamilyTargetResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class LLINSFamilyTargetResourceIT {

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
    private LLINSFamilyTargetRepository lLINSFamilyTargetRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLLINSFamilyTargetMockMvc;

    private LLINSFamilyTarget lLINSFamilyTarget;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LLINSFamilyTarget createEntity(EntityManager em) {
        LLINSFamilyTarget lLINSFamilyTarget = new LLINSFamilyTarget()
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
        lLINSFamilyTarget.setDayPlanned(workingDay);
        // Add required entity
        Family family;
        if (TestUtil.findAll(em, Family.class).isEmpty()) {
            family = FamilyResourceIT.createEntity(em);
            em.persist(family);
            em.flush();
        } else {
            family = TestUtil.findAll(em, Family.class).get(0);
        }
        lLINSFamilyTarget.setFamily(family);
        // Add required entity
        Team team;
        if (TestUtil.findAll(em, Team.class).isEmpty()) {
            team = TeamResourceIT.createEntity(em);
            em.persist(team);
            em.flush();
        } else {
            team = TestUtil.findAll(em, Team.class).get(0);
        }
        lLINSFamilyTarget.setTeamAssigned(team);
        return lLINSFamilyTarget;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LLINSFamilyTarget createUpdatedEntity(EntityManager em) {
        LLINSFamilyTarget lLINSFamilyTarget = new LLINSFamilyTarget()
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
        lLINSFamilyTarget.setDayPlanned(workingDay);
        // Add required entity
        Family family;
        if (TestUtil.findAll(em, Family.class).isEmpty()) {
            family = FamilyResourceIT.createUpdatedEntity(em);
            em.persist(family);
            em.flush();
        } else {
            family = TestUtil.findAll(em, Family.class).get(0);
        }
        lLINSFamilyTarget.setFamily(family);
        // Add required entity
        Team team;
        if (TestUtil.findAll(em, Team.class).isEmpty()) {
            team = TeamResourceIT.createUpdatedEntity(em);
            em.persist(team);
            em.flush();
        } else {
            team = TestUtil.findAll(em, Team.class).get(0);
        }
        lLINSFamilyTarget.setTeamAssigned(team);
        return lLINSFamilyTarget;
    }

    @BeforeEach
    public void initTest() {
        lLINSFamilyTarget = createEntity(em);
    }

    @Test
    @Transactional
    void createLLINSFamilyTarget() throws Exception {
        int databaseSizeBeforeCreate = lLINSFamilyTargetRepository.findAll().size();
        // Create the LLINSFamilyTarget
        restLLINSFamilyTargetMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(lLINSFamilyTarget))
            )
            .andExpect(status().isCreated());

        // Validate the LLINSFamilyTarget in the database
        List<LLINSFamilyTarget> lLINSFamilyTargetList = lLINSFamilyTargetRepository.findAll();
        assertThat(lLINSFamilyTargetList).hasSize(databaseSizeBeforeCreate + 1);
        LLINSFamilyTarget testLLINSFamilyTarget = lLINSFamilyTargetList.get(lLINSFamilyTargetList.size() - 1);
        assertThat(testLLINSFamilyTarget.getUid()).isEqualTo(DEFAULT_UID);
        assertThat(testLLINSFamilyTarget.getCreated()).isEqualTo(DEFAULT_CREATED);
        assertThat(testLLINSFamilyTarget.getLastUpdated()).isEqualTo(DEFAULT_LAST_UPDATED);
        assertThat(testLLINSFamilyTarget.getResidentsIndividualsPlanned()).isEqualTo(DEFAULT_RESIDENTS_INDIVIDUALS_PLANNED);
        assertThat(testLLINSFamilyTarget.getIdpsIndividualsPlanned()).isEqualTo(DEFAULT_IDPS_INDIVIDUALS_PLANNED);
        assertThat(testLLINSFamilyTarget.getQuantityPlanned()).isEqualTo(DEFAULT_QUANTITY_PLANNED);
        assertThat(testLLINSFamilyTarget.getFamilyType()).isEqualTo(DEFAULT_FAMILY_TYPE);
        assertThat(testLLINSFamilyTarget.getStatusOfFamilyTarget()).isEqualTo(DEFAULT_STATUS_OF_FAMILY_TARGET);
    }

    @Test
    @Transactional
    void createLLINSFamilyTargetWithExistingId() throws Exception {
        // Create the LLINSFamilyTarget with an existing ID
        lLINSFamilyTarget.setId(1L);

        int databaseSizeBeforeCreate = lLINSFamilyTargetRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLLINSFamilyTargetMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(lLINSFamilyTarget))
            )
            .andExpect(status().isBadRequest());

        // Validate the LLINSFamilyTarget in the database
        List<LLINSFamilyTarget> lLINSFamilyTargetList = lLINSFamilyTargetRepository.findAll();
        assertThat(lLINSFamilyTargetList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkUidIsRequired() throws Exception {
        int databaseSizeBeforeTest = lLINSFamilyTargetRepository.findAll().size();
        // set the field null
        lLINSFamilyTarget.setUid(null);

        // Create the LLINSFamilyTarget, which fails.

        restLLINSFamilyTargetMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(lLINSFamilyTarget))
            )
            .andExpect(status().isBadRequest());

        List<LLINSFamilyTarget> lLINSFamilyTargetList = lLINSFamilyTargetRepository.findAll();
        assertThat(lLINSFamilyTargetList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkResidentsIndividualsPlannedIsRequired() throws Exception {
        int databaseSizeBeforeTest = lLINSFamilyTargetRepository.findAll().size();
        // set the field null
        lLINSFamilyTarget.setResidentsIndividualsPlanned(null);

        // Create the LLINSFamilyTarget, which fails.

        restLLINSFamilyTargetMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(lLINSFamilyTarget))
            )
            .andExpect(status().isBadRequest());

        List<LLINSFamilyTarget> lLINSFamilyTargetList = lLINSFamilyTargetRepository.findAll();
        assertThat(lLINSFamilyTargetList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIdpsIndividualsPlannedIsRequired() throws Exception {
        int databaseSizeBeforeTest = lLINSFamilyTargetRepository.findAll().size();
        // set the field null
        lLINSFamilyTarget.setIdpsIndividualsPlanned(null);

        // Create the LLINSFamilyTarget, which fails.

        restLLINSFamilyTargetMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(lLINSFamilyTarget))
            )
            .andExpect(status().isBadRequest());

        List<LLINSFamilyTarget> lLINSFamilyTargetList = lLINSFamilyTargetRepository.findAll();
        assertThat(lLINSFamilyTargetList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkQuantityPlannedIsRequired() throws Exception {
        int databaseSizeBeforeTest = lLINSFamilyTargetRepository.findAll().size();
        // set the field null
        lLINSFamilyTarget.setQuantityPlanned(null);

        // Create the LLINSFamilyTarget, which fails.

        restLLINSFamilyTargetMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(lLINSFamilyTarget))
            )
            .andExpect(status().isBadRequest());

        List<LLINSFamilyTarget> lLINSFamilyTargetList = lLINSFamilyTargetRepository.findAll();
        assertThat(lLINSFamilyTargetList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFamilyTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = lLINSFamilyTargetRepository.findAll().size();
        // set the field null
        lLINSFamilyTarget.setFamilyType(null);

        // Create the LLINSFamilyTarget, which fails.

        restLLINSFamilyTargetMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(lLINSFamilyTarget))
            )
            .andExpect(status().isBadRequest());

        List<LLINSFamilyTarget> lLINSFamilyTargetList = lLINSFamilyTargetRepository.findAll();
        assertThat(lLINSFamilyTargetList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusOfFamilyTargetIsRequired() throws Exception {
        int databaseSizeBeforeTest = lLINSFamilyTargetRepository.findAll().size();
        // set the field null
        lLINSFamilyTarget.setStatusOfFamilyTarget(null);

        // Create the LLINSFamilyTarget, which fails.

        restLLINSFamilyTargetMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(lLINSFamilyTarget))
            )
            .andExpect(status().isBadRequest());

        List<LLINSFamilyTarget> lLINSFamilyTargetList = lLINSFamilyTargetRepository.findAll();
        assertThat(lLINSFamilyTargetList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllLLINSFamilyTargets() throws Exception {
        // Initialize the database
        lLINSFamilyTargetRepository.saveAndFlush(lLINSFamilyTarget);

        // Get all the lLINSFamilyTargetList
        restLLINSFamilyTargetMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(lLINSFamilyTarget.getId().intValue())))
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
    void getLLINSFamilyTarget() throws Exception {
        // Initialize the database
        lLINSFamilyTargetRepository.saveAndFlush(lLINSFamilyTarget);

        // Get the lLINSFamilyTarget
        restLLINSFamilyTargetMockMvc
            .perform(get(ENTITY_API_URL_ID, lLINSFamilyTarget.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(lLINSFamilyTarget.getId().intValue()))
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
    void getNonExistingLLINSFamilyTarget() throws Exception {
        // Get the lLINSFamilyTarget
        restLLINSFamilyTargetMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewLLINSFamilyTarget() throws Exception {
        // Initialize the database
        lLINSFamilyTargetRepository.saveAndFlush(lLINSFamilyTarget);

        int databaseSizeBeforeUpdate = lLINSFamilyTargetRepository.findAll().size();

        // Update the lLINSFamilyTarget
        LLINSFamilyTarget updatedLLINSFamilyTarget = lLINSFamilyTargetRepository.findById(lLINSFamilyTarget.getId()).get();
        // Disconnect from session so that the updates on updatedLLINSFamilyTarget are not directly saved in db
        em.detach(updatedLLINSFamilyTarget);
        updatedLLINSFamilyTarget
            .uid(UPDATED_UID)
            .created(UPDATED_CREATED)
            .lastUpdated(UPDATED_LAST_UPDATED)
            .residentsIndividualsPlanned(UPDATED_RESIDENTS_INDIVIDUALS_PLANNED)
            .idpsIndividualsPlanned(UPDATED_IDPS_INDIVIDUALS_PLANNED)
            .quantityPlanned(UPDATED_QUANTITY_PLANNED)
            .familyType(UPDATED_FAMILY_TYPE)
            .statusOfFamilyTarget(UPDATED_STATUS_OF_FAMILY_TARGET);

        restLLINSFamilyTargetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedLLINSFamilyTarget.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedLLINSFamilyTarget))
            )
            .andExpect(status().isOk());

        // Validate the LLINSFamilyTarget in the database
        List<LLINSFamilyTarget> lLINSFamilyTargetList = lLINSFamilyTargetRepository.findAll();
        assertThat(lLINSFamilyTargetList).hasSize(databaseSizeBeforeUpdate);
        LLINSFamilyTarget testLLINSFamilyTarget = lLINSFamilyTargetList.get(lLINSFamilyTargetList.size() - 1);
        assertThat(testLLINSFamilyTarget.getUid()).isEqualTo(UPDATED_UID);
        assertThat(testLLINSFamilyTarget.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testLLINSFamilyTarget.getLastUpdated()).isEqualTo(UPDATED_LAST_UPDATED);
        assertThat(testLLINSFamilyTarget.getResidentsIndividualsPlanned()).isEqualTo(UPDATED_RESIDENTS_INDIVIDUALS_PLANNED);
        assertThat(testLLINSFamilyTarget.getIdpsIndividualsPlanned()).isEqualTo(UPDATED_IDPS_INDIVIDUALS_PLANNED);
        assertThat(testLLINSFamilyTarget.getQuantityPlanned()).isEqualTo(UPDATED_QUANTITY_PLANNED);
        assertThat(testLLINSFamilyTarget.getFamilyType()).isEqualTo(UPDATED_FAMILY_TYPE);
        assertThat(testLLINSFamilyTarget.getStatusOfFamilyTarget()).isEqualTo(UPDATED_STATUS_OF_FAMILY_TARGET);
    }

    @Test
    @Transactional
    void putNonExistingLLINSFamilyTarget() throws Exception {
        int databaseSizeBeforeUpdate = lLINSFamilyTargetRepository.findAll().size();
        lLINSFamilyTarget.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLLINSFamilyTargetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, lLINSFamilyTarget.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(lLINSFamilyTarget))
            )
            .andExpect(status().isBadRequest());

        // Validate the LLINSFamilyTarget in the database
        List<LLINSFamilyTarget> lLINSFamilyTargetList = lLINSFamilyTargetRepository.findAll();
        assertThat(lLINSFamilyTargetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLLINSFamilyTarget() throws Exception {
        int databaseSizeBeforeUpdate = lLINSFamilyTargetRepository.findAll().size();
        lLINSFamilyTarget.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLLINSFamilyTargetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(lLINSFamilyTarget))
            )
            .andExpect(status().isBadRequest());

        // Validate the LLINSFamilyTarget in the database
        List<LLINSFamilyTarget> lLINSFamilyTargetList = lLINSFamilyTargetRepository.findAll();
        assertThat(lLINSFamilyTargetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLLINSFamilyTarget() throws Exception {
        int databaseSizeBeforeUpdate = lLINSFamilyTargetRepository.findAll().size();
        lLINSFamilyTarget.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLLINSFamilyTargetMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(lLINSFamilyTarget))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LLINSFamilyTarget in the database
        List<LLINSFamilyTarget> lLINSFamilyTargetList = lLINSFamilyTargetRepository.findAll();
        assertThat(lLINSFamilyTargetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLLINSFamilyTargetWithPatch() throws Exception {
        // Initialize the database
        lLINSFamilyTargetRepository.saveAndFlush(lLINSFamilyTarget);

        int databaseSizeBeforeUpdate = lLINSFamilyTargetRepository.findAll().size();

        // Update the lLINSFamilyTarget using partial update
        LLINSFamilyTarget partialUpdatedLLINSFamilyTarget = new LLINSFamilyTarget();
        partialUpdatedLLINSFamilyTarget.setId(lLINSFamilyTarget.getId());

        partialUpdatedLLINSFamilyTarget.idpsIndividualsPlanned(UPDATED_IDPS_INDIVIDUALS_PLANNED).familyType(UPDATED_FAMILY_TYPE);

        restLLINSFamilyTargetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLLINSFamilyTarget.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLLINSFamilyTarget))
            )
            .andExpect(status().isOk());

        // Validate the LLINSFamilyTarget in the database
        List<LLINSFamilyTarget> lLINSFamilyTargetList = lLINSFamilyTargetRepository.findAll();
        assertThat(lLINSFamilyTargetList).hasSize(databaseSizeBeforeUpdate);
        LLINSFamilyTarget testLLINSFamilyTarget = lLINSFamilyTargetList.get(lLINSFamilyTargetList.size() - 1);
        assertThat(testLLINSFamilyTarget.getUid()).isEqualTo(DEFAULT_UID);
        assertThat(testLLINSFamilyTarget.getCreated()).isEqualTo(DEFAULT_CREATED);
        assertThat(testLLINSFamilyTarget.getLastUpdated()).isEqualTo(DEFAULT_LAST_UPDATED);
        assertThat(testLLINSFamilyTarget.getResidentsIndividualsPlanned()).isEqualTo(DEFAULT_RESIDENTS_INDIVIDUALS_PLANNED);
        assertThat(testLLINSFamilyTarget.getIdpsIndividualsPlanned()).isEqualTo(UPDATED_IDPS_INDIVIDUALS_PLANNED);
        assertThat(testLLINSFamilyTarget.getQuantityPlanned()).isEqualTo(DEFAULT_QUANTITY_PLANNED);
        assertThat(testLLINSFamilyTarget.getFamilyType()).isEqualTo(UPDATED_FAMILY_TYPE);
        assertThat(testLLINSFamilyTarget.getStatusOfFamilyTarget()).isEqualTo(DEFAULT_STATUS_OF_FAMILY_TARGET);
    }

    @Test
    @Transactional
    void fullUpdateLLINSFamilyTargetWithPatch() throws Exception {
        // Initialize the database
        lLINSFamilyTargetRepository.saveAndFlush(lLINSFamilyTarget);

        int databaseSizeBeforeUpdate = lLINSFamilyTargetRepository.findAll().size();

        // Update the lLINSFamilyTarget using partial update
        LLINSFamilyTarget partialUpdatedLLINSFamilyTarget = new LLINSFamilyTarget();
        partialUpdatedLLINSFamilyTarget.setId(lLINSFamilyTarget.getId());

        partialUpdatedLLINSFamilyTarget
            .uid(UPDATED_UID)
            .created(UPDATED_CREATED)
            .lastUpdated(UPDATED_LAST_UPDATED)
            .residentsIndividualsPlanned(UPDATED_RESIDENTS_INDIVIDUALS_PLANNED)
            .idpsIndividualsPlanned(UPDATED_IDPS_INDIVIDUALS_PLANNED)
            .quantityPlanned(UPDATED_QUANTITY_PLANNED)
            .familyType(UPDATED_FAMILY_TYPE)
            .statusOfFamilyTarget(UPDATED_STATUS_OF_FAMILY_TARGET);

        restLLINSFamilyTargetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLLINSFamilyTarget.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLLINSFamilyTarget))
            )
            .andExpect(status().isOk());

        // Validate the LLINSFamilyTarget in the database
        List<LLINSFamilyTarget> lLINSFamilyTargetList = lLINSFamilyTargetRepository.findAll();
        assertThat(lLINSFamilyTargetList).hasSize(databaseSizeBeforeUpdate);
        LLINSFamilyTarget testLLINSFamilyTarget = lLINSFamilyTargetList.get(lLINSFamilyTargetList.size() - 1);
        assertThat(testLLINSFamilyTarget.getUid()).isEqualTo(UPDATED_UID);
        assertThat(testLLINSFamilyTarget.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testLLINSFamilyTarget.getLastUpdated()).isEqualTo(UPDATED_LAST_UPDATED);
        assertThat(testLLINSFamilyTarget.getResidentsIndividualsPlanned()).isEqualTo(UPDATED_RESIDENTS_INDIVIDUALS_PLANNED);
        assertThat(testLLINSFamilyTarget.getIdpsIndividualsPlanned()).isEqualTo(UPDATED_IDPS_INDIVIDUALS_PLANNED);
        assertThat(testLLINSFamilyTarget.getQuantityPlanned()).isEqualTo(UPDATED_QUANTITY_PLANNED);
        assertThat(testLLINSFamilyTarget.getFamilyType()).isEqualTo(UPDATED_FAMILY_TYPE);
        assertThat(testLLINSFamilyTarget.getStatusOfFamilyTarget()).isEqualTo(UPDATED_STATUS_OF_FAMILY_TARGET);
    }

    @Test
    @Transactional
    void patchNonExistingLLINSFamilyTarget() throws Exception {
        int databaseSizeBeforeUpdate = lLINSFamilyTargetRepository.findAll().size();
        lLINSFamilyTarget.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLLINSFamilyTargetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, lLINSFamilyTarget.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(lLINSFamilyTarget))
            )
            .andExpect(status().isBadRequest());

        // Validate the LLINSFamilyTarget in the database
        List<LLINSFamilyTarget> lLINSFamilyTargetList = lLINSFamilyTargetRepository.findAll();
        assertThat(lLINSFamilyTargetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLLINSFamilyTarget() throws Exception {
        int databaseSizeBeforeUpdate = lLINSFamilyTargetRepository.findAll().size();
        lLINSFamilyTarget.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLLINSFamilyTargetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(lLINSFamilyTarget))
            )
            .andExpect(status().isBadRequest());

        // Validate the LLINSFamilyTarget in the database
        List<LLINSFamilyTarget> lLINSFamilyTargetList = lLINSFamilyTargetRepository.findAll();
        assertThat(lLINSFamilyTargetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLLINSFamilyTarget() throws Exception {
        int databaseSizeBeforeUpdate = lLINSFamilyTargetRepository.findAll().size();
        lLINSFamilyTarget.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLLINSFamilyTargetMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(lLINSFamilyTarget))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LLINSFamilyTarget in the database
        List<LLINSFamilyTarget> lLINSFamilyTargetList = lLINSFamilyTargetRepository.findAll();
        assertThat(lLINSFamilyTargetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLLINSFamilyTarget() throws Exception {
        // Initialize the database
        lLINSFamilyTargetRepository.saveAndFlush(lLINSFamilyTarget);

        int databaseSizeBeforeDelete = lLINSFamilyTargetRepository.findAll().size();

        // Delete the lLINSFamilyTarget
        restLLINSFamilyTargetMockMvc
            .perform(delete(ENTITY_API_URL_ID, lLINSFamilyTarget.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<LLINSFamilyTarget> lLINSFamilyTargetList = lLINSFamilyTargetRepository.findAll();
        assertThat(lLINSFamilyTargetList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
