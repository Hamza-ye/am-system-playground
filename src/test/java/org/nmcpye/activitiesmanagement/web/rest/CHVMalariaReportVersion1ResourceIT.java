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
import org.nmcpye.activitiesmanagement.domain.CHV;
import org.nmcpye.activitiesmanagement.domain.CHVMalariaReportVersion1;
import org.nmcpye.activitiesmanagement.domain.Period;
import org.nmcpye.activitiesmanagement.repository.CHVMalariaReportVersion1Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link CHVMalariaReportVersion1Resource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CHVMalariaReportVersion1ResourceIT {

    private static final String DEFAULT_UID = "AAAAAAAAAA";
    private static final String UPDATED_UID = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_LAST_UPDATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_UPDATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Integer DEFAULT_TESTED = 1;
    private static final Integer UPDATED_TESTED = 2;

    private static final Integer DEFAULT_POSITIVE = 1;
    private static final Integer UPDATED_POSITIVE = 2;

    private static final Integer DEFAULT_DRUGS_GIVEN = 1;
    private static final Integer UPDATED_DRUGS_GIVEN = 2;

    private static final Integer DEFAULT_SUPPS_GIVEN = 1;
    private static final Integer UPDATED_SUPPS_GIVEN = 2;

    private static final Integer DEFAULT_RDT_BALANCE = 1;
    private static final Integer UPDATED_RDT_BALANCE = 2;

    private static final Integer DEFAULT_RDT_RECEIVED = 1;
    private static final Integer UPDATED_RDT_RECEIVED = 2;

    private static final Integer DEFAULT_RDT_USED = 1;
    private static final Integer UPDATED_RDT_USED = 2;

    private static final Integer DEFAULT_RDT_DAMAGED_LOST = 1;
    private static final Integer UPDATED_RDT_DAMAGED_LOST = 2;

    private static final Integer DEFAULT_DRUGS_BALANCE = 1;
    private static final Integer UPDATED_DRUGS_BALANCE = 2;

    private static final Integer DEFAULT_DRUGS_RECEIVED = 1;
    private static final Integer UPDATED_DRUGS_RECEIVED = 2;

    private static final Integer DEFAULT_DRUGS_USED = 1;
    private static final Integer UPDATED_DRUGS_USED = 2;

    private static final Integer DEFAULT_DRUGS_DAMAGED_LOST = 1;
    private static final Integer UPDATED_DRUGS_DAMAGED_LOST = 2;

    private static final Integer DEFAULT_SUPPS_BALANCE = 1;
    private static final Integer UPDATED_SUPPS_BALANCE = 2;

    private static final Integer DEFAULT_SUPPS_RECEIVED = 1;
    private static final Integer UPDATED_SUPPS_RECEIVED = 2;

    private static final Integer DEFAULT_SUPPS_USED = 1;
    private static final Integer UPDATED_SUPPS_USED = 2;

    private static final Integer DEFAULT_SUPPS_DAMAGED_LOST = 1;
    private static final Integer UPDATED_SUPPS_DAMAGED_LOST = 2;

    private static final String DEFAULT_COMMENT = "AAAAAAAAAA";
    private static final String UPDATED_COMMENT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/chv-malaria-report-version-1-s";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CHVMalariaReportVersion1Repository cHVMalariaReportVersion1Repository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCHVMalariaReportVersion1MockMvc;

    private CHVMalariaReportVersion1 cHVMalariaReportVersion1;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CHVMalariaReportVersion1 createEntity(EntityManager em) {
        CHVMalariaReportVersion1 cHVMalariaReportVersion1 = new CHVMalariaReportVersion1()
            .uid(DEFAULT_UID)
            .created(DEFAULT_CREATED)
            .lastUpdated(DEFAULT_LAST_UPDATED)
            .tested(DEFAULT_TESTED)
            .positive(DEFAULT_POSITIVE)
            .drugsGiven(DEFAULT_DRUGS_GIVEN)
            .suppsGiven(DEFAULT_SUPPS_GIVEN)
            .rdtBalance(DEFAULT_RDT_BALANCE)
            .rdtReceived(DEFAULT_RDT_RECEIVED)
            .rdtUsed(DEFAULT_RDT_USED)
            .rdtDamagedLost(DEFAULT_RDT_DAMAGED_LOST)
            .drugsBalance(DEFAULT_DRUGS_BALANCE)
            .drugsReceived(DEFAULT_DRUGS_RECEIVED)
            .drugsUsed(DEFAULT_DRUGS_USED)
            .drugsDamagedLost(DEFAULT_DRUGS_DAMAGED_LOST)
            .suppsBalance(DEFAULT_SUPPS_BALANCE)
            .suppsReceived(DEFAULT_SUPPS_RECEIVED)
            .suppsUsed(DEFAULT_SUPPS_USED)
            .suppsDamagedLost(DEFAULT_SUPPS_DAMAGED_LOST)
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
        cHVMalariaReportVersion1.setChv(cHV);
        // Add required entity
        Period period;
        if (TestUtil.findAll(em, Period.class).isEmpty()) {
            period = PeriodResourceIT.createEntity(em);
            em.persist(period);
            em.flush();
        } else {
            period = TestUtil.findAll(em, Period.class).get(0);
        }
        cHVMalariaReportVersion1.setPeriod(period);
        return cHVMalariaReportVersion1;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CHVMalariaReportVersion1 createUpdatedEntity(EntityManager em) {
        CHVMalariaReportVersion1 cHVMalariaReportVersion1 = new CHVMalariaReportVersion1()
            .uid(UPDATED_UID)
            .created(UPDATED_CREATED)
            .lastUpdated(UPDATED_LAST_UPDATED)
            .tested(UPDATED_TESTED)
            .positive(UPDATED_POSITIVE)
            .drugsGiven(UPDATED_DRUGS_GIVEN)
            .suppsGiven(UPDATED_SUPPS_GIVEN)
            .rdtBalance(UPDATED_RDT_BALANCE)
            .rdtReceived(UPDATED_RDT_RECEIVED)
            .rdtUsed(UPDATED_RDT_USED)
            .rdtDamagedLost(UPDATED_RDT_DAMAGED_LOST)
            .drugsBalance(UPDATED_DRUGS_BALANCE)
            .drugsReceived(UPDATED_DRUGS_RECEIVED)
            .drugsUsed(UPDATED_DRUGS_USED)
            .drugsDamagedLost(UPDATED_DRUGS_DAMAGED_LOST)
            .suppsBalance(UPDATED_SUPPS_BALANCE)
            .suppsReceived(UPDATED_SUPPS_RECEIVED)
            .suppsUsed(UPDATED_SUPPS_USED)
            .suppsDamagedLost(UPDATED_SUPPS_DAMAGED_LOST)
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
        cHVMalariaReportVersion1.setChv(cHV);
        // Add required entity
        Period period;
        if (TestUtil.findAll(em, Period.class).isEmpty()) {
            period = PeriodResourceIT.createUpdatedEntity(em);
            em.persist(period);
            em.flush();
        } else {
            period = TestUtil.findAll(em, Period.class).get(0);
        }
        cHVMalariaReportVersion1.setPeriod(period);
        return cHVMalariaReportVersion1;
    }

    @BeforeEach
    public void initTest() {
        cHVMalariaReportVersion1 = createEntity(em);
    }

    @Test
    @Transactional
    void createCHVMalariaReportVersion1() throws Exception {
        int databaseSizeBeforeCreate = cHVMalariaReportVersion1Repository.findAll().size();
        // Create the CHVMalariaReportVersion1
        restCHVMalariaReportVersion1MockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cHVMalariaReportVersion1))
            )
            .andExpect(status().isCreated());

        // Validate the CHVMalariaReportVersion1 in the database
        List<CHVMalariaReportVersion1> cHVMalariaReportVersion1List = cHVMalariaReportVersion1Repository.findAll();
        assertThat(cHVMalariaReportVersion1List).hasSize(databaseSizeBeforeCreate + 1);
        CHVMalariaReportVersion1 testCHVMalariaReportVersion1 = cHVMalariaReportVersion1List.get(cHVMalariaReportVersion1List.size() - 1);
        assertThat(testCHVMalariaReportVersion1.getUid()).isEqualTo(DEFAULT_UID);
        assertThat(testCHVMalariaReportVersion1.getCreated()).isEqualTo(DEFAULT_CREATED);
        assertThat(testCHVMalariaReportVersion1.getLastUpdated()).isEqualTo(DEFAULT_LAST_UPDATED);
        assertThat(testCHVMalariaReportVersion1.getTested()).isEqualTo(DEFAULT_TESTED);
        assertThat(testCHVMalariaReportVersion1.getPositive()).isEqualTo(DEFAULT_POSITIVE);
        assertThat(testCHVMalariaReportVersion1.getDrugsGiven()).isEqualTo(DEFAULT_DRUGS_GIVEN);
        assertThat(testCHVMalariaReportVersion1.getSuppsGiven()).isEqualTo(DEFAULT_SUPPS_GIVEN);
        assertThat(testCHVMalariaReportVersion1.getRdtBalance()).isEqualTo(DEFAULT_RDT_BALANCE);
        assertThat(testCHVMalariaReportVersion1.getRdtReceived()).isEqualTo(DEFAULT_RDT_RECEIVED);
        assertThat(testCHVMalariaReportVersion1.getRdtUsed()).isEqualTo(DEFAULT_RDT_USED);
        assertThat(testCHVMalariaReportVersion1.getRdtDamagedLost()).isEqualTo(DEFAULT_RDT_DAMAGED_LOST);
        assertThat(testCHVMalariaReportVersion1.getDrugsBalance()).isEqualTo(DEFAULT_DRUGS_BALANCE);
        assertThat(testCHVMalariaReportVersion1.getDrugsReceived()).isEqualTo(DEFAULT_DRUGS_RECEIVED);
        assertThat(testCHVMalariaReportVersion1.getDrugsUsed()).isEqualTo(DEFAULT_DRUGS_USED);
        assertThat(testCHVMalariaReportVersion1.getDrugsDamagedLost()).isEqualTo(DEFAULT_DRUGS_DAMAGED_LOST);
        assertThat(testCHVMalariaReportVersion1.getSuppsBalance()).isEqualTo(DEFAULT_SUPPS_BALANCE);
        assertThat(testCHVMalariaReportVersion1.getSuppsReceived()).isEqualTo(DEFAULT_SUPPS_RECEIVED);
        assertThat(testCHVMalariaReportVersion1.getSuppsUsed()).isEqualTo(DEFAULT_SUPPS_USED);
        assertThat(testCHVMalariaReportVersion1.getSuppsDamagedLost()).isEqualTo(DEFAULT_SUPPS_DAMAGED_LOST);
        assertThat(testCHVMalariaReportVersion1.getComment()).isEqualTo(DEFAULT_COMMENT);
    }

    @Test
    @Transactional
    void createCHVMalariaReportVersion1WithExistingId() throws Exception {
        // Create the CHVMalariaReportVersion1 with an existing ID
        cHVMalariaReportVersion1.setId(1L);

        int databaseSizeBeforeCreate = cHVMalariaReportVersion1Repository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCHVMalariaReportVersion1MockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cHVMalariaReportVersion1))
            )
            .andExpect(status().isBadRequest());

        // Validate the CHVMalariaReportVersion1 in the database
        List<CHVMalariaReportVersion1> cHVMalariaReportVersion1List = cHVMalariaReportVersion1Repository.findAll();
        assertThat(cHVMalariaReportVersion1List).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkUidIsRequired() throws Exception {
        int databaseSizeBeforeTest = cHVMalariaReportVersion1Repository.findAll().size();
        // set the field null
        cHVMalariaReportVersion1.setUid(null);

        // Create the CHVMalariaReportVersion1, which fails.

        restCHVMalariaReportVersion1MockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cHVMalariaReportVersion1))
            )
            .andExpect(status().isBadRequest());

        List<CHVMalariaReportVersion1> cHVMalariaReportVersion1List = cHVMalariaReportVersion1Repository.findAll();
        assertThat(cHVMalariaReportVersion1List).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCHVMalariaReportVersion1s() throws Exception {
        // Initialize the database
        cHVMalariaReportVersion1Repository.saveAndFlush(cHVMalariaReportVersion1);

        // Get all the cHVMalariaReportVersion1List
        restCHVMalariaReportVersion1MockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cHVMalariaReportVersion1.getId().intValue())))
            .andExpect(jsonPath("$.[*].uid").value(hasItem(DEFAULT_UID)))
            .andExpect(jsonPath("$.[*].created").value(hasItem(DEFAULT_CREATED.toString())))
            .andExpect(jsonPath("$.[*].lastUpdated").value(hasItem(DEFAULT_LAST_UPDATED.toString())))
            .andExpect(jsonPath("$.[*].tested").value(hasItem(DEFAULT_TESTED)))
            .andExpect(jsonPath("$.[*].positive").value(hasItem(DEFAULT_POSITIVE)))
            .andExpect(jsonPath("$.[*].drugsGiven").value(hasItem(DEFAULT_DRUGS_GIVEN)))
            .andExpect(jsonPath("$.[*].suppsGiven").value(hasItem(DEFAULT_SUPPS_GIVEN)))
            .andExpect(jsonPath("$.[*].rdtBalance").value(hasItem(DEFAULT_RDT_BALANCE)))
            .andExpect(jsonPath("$.[*].rdtReceived").value(hasItem(DEFAULT_RDT_RECEIVED)))
            .andExpect(jsonPath("$.[*].rdtUsed").value(hasItem(DEFAULT_RDT_USED)))
            .andExpect(jsonPath("$.[*].rdtDamagedLost").value(hasItem(DEFAULT_RDT_DAMAGED_LOST)))
            .andExpect(jsonPath("$.[*].drugsBalance").value(hasItem(DEFAULT_DRUGS_BALANCE)))
            .andExpect(jsonPath("$.[*].drugsReceived").value(hasItem(DEFAULT_DRUGS_RECEIVED)))
            .andExpect(jsonPath("$.[*].drugsUsed").value(hasItem(DEFAULT_DRUGS_USED)))
            .andExpect(jsonPath("$.[*].drugsDamagedLost").value(hasItem(DEFAULT_DRUGS_DAMAGED_LOST)))
            .andExpect(jsonPath("$.[*].suppsBalance").value(hasItem(DEFAULT_SUPPS_BALANCE)))
            .andExpect(jsonPath("$.[*].suppsReceived").value(hasItem(DEFAULT_SUPPS_RECEIVED)))
            .andExpect(jsonPath("$.[*].suppsUsed").value(hasItem(DEFAULT_SUPPS_USED)))
            .andExpect(jsonPath("$.[*].suppsDamagedLost").value(hasItem(DEFAULT_SUPPS_DAMAGED_LOST)))
            .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT)));
    }

    @Test
    @Transactional
    void getCHVMalariaReportVersion1() throws Exception {
        // Initialize the database
        cHVMalariaReportVersion1Repository.saveAndFlush(cHVMalariaReportVersion1);

        // Get the cHVMalariaReportVersion1
        restCHVMalariaReportVersion1MockMvc
            .perform(get(ENTITY_API_URL_ID, cHVMalariaReportVersion1.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cHVMalariaReportVersion1.getId().intValue()))
            .andExpect(jsonPath("$.uid").value(DEFAULT_UID))
            .andExpect(jsonPath("$.created").value(DEFAULT_CREATED.toString()))
            .andExpect(jsonPath("$.lastUpdated").value(DEFAULT_LAST_UPDATED.toString()))
            .andExpect(jsonPath("$.tested").value(DEFAULT_TESTED))
            .andExpect(jsonPath("$.positive").value(DEFAULT_POSITIVE))
            .andExpect(jsonPath("$.drugsGiven").value(DEFAULT_DRUGS_GIVEN))
            .andExpect(jsonPath("$.suppsGiven").value(DEFAULT_SUPPS_GIVEN))
            .andExpect(jsonPath("$.rdtBalance").value(DEFAULT_RDT_BALANCE))
            .andExpect(jsonPath("$.rdtReceived").value(DEFAULT_RDT_RECEIVED))
            .andExpect(jsonPath("$.rdtUsed").value(DEFAULT_RDT_USED))
            .andExpect(jsonPath("$.rdtDamagedLost").value(DEFAULT_RDT_DAMAGED_LOST))
            .andExpect(jsonPath("$.drugsBalance").value(DEFAULT_DRUGS_BALANCE))
            .andExpect(jsonPath("$.drugsReceived").value(DEFAULT_DRUGS_RECEIVED))
            .andExpect(jsonPath("$.drugsUsed").value(DEFAULT_DRUGS_USED))
            .andExpect(jsonPath("$.drugsDamagedLost").value(DEFAULT_DRUGS_DAMAGED_LOST))
            .andExpect(jsonPath("$.suppsBalance").value(DEFAULT_SUPPS_BALANCE))
            .andExpect(jsonPath("$.suppsReceived").value(DEFAULT_SUPPS_RECEIVED))
            .andExpect(jsonPath("$.suppsUsed").value(DEFAULT_SUPPS_USED))
            .andExpect(jsonPath("$.suppsDamagedLost").value(DEFAULT_SUPPS_DAMAGED_LOST))
            .andExpect(jsonPath("$.comment").value(DEFAULT_COMMENT));
    }

    @Test
    @Transactional
    void getNonExistingCHVMalariaReportVersion1() throws Exception {
        // Get the cHVMalariaReportVersion1
        restCHVMalariaReportVersion1MockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCHVMalariaReportVersion1() throws Exception {
        // Initialize the database
        cHVMalariaReportVersion1Repository.saveAndFlush(cHVMalariaReportVersion1);

        int databaseSizeBeforeUpdate = cHVMalariaReportVersion1Repository.findAll().size();

        // Update the cHVMalariaReportVersion1
        CHVMalariaReportVersion1 updatedCHVMalariaReportVersion1 = cHVMalariaReportVersion1Repository
            .findById(cHVMalariaReportVersion1.getId())
            .get();
        // Disconnect from session so that the updates on updatedCHVMalariaReportVersion1 are not directly saved in db
        em.detach(updatedCHVMalariaReportVersion1);
        updatedCHVMalariaReportVersion1
            .uid(UPDATED_UID)
            .created(UPDATED_CREATED)
            .lastUpdated(UPDATED_LAST_UPDATED)
            .tested(UPDATED_TESTED)
            .positive(UPDATED_POSITIVE)
            .drugsGiven(UPDATED_DRUGS_GIVEN)
            .suppsGiven(UPDATED_SUPPS_GIVEN)
            .rdtBalance(UPDATED_RDT_BALANCE)
            .rdtReceived(UPDATED_RDT_RECEIVED)
            .rdtUsed(UPDATED_RDT_USED)
            .rdtDamagedLost(UPDATED_RDT_DAMAGED_LOST)
            .drugsBalance(UPDATED_DRUGS_BALANCE)
            .drugsReceived(UPDATED_DRUGS_RECEIVED)
            .drugsUsed(UPDATED_DRUGS_USED)
            .drugsDamagedLost(UPDATED_DRUGS_DAMAGED_LOST)
            .suppsBalance(UPDATED_SUPPS_BALANCE)
            .suppsReceived(UPDATED_SUPPS_RECEIVED)
            .suppsUsed(UPDATED_SUPPS_USED)
            .suppsDamagedLost(UPDATED_SUPPS_DAMAGED_LOST)
            .comment(UPDATED_COMMENT);

        restCHVMalariaReportVersion1MockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCHVMalariaReportVersion1.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCHVMalariaReportVersion1))
            )
            .andExpect(status().isOk());

        // Validate the CHVMalariaReportVersion1 in the database
        List<CHVMalariaReportVersion1> cHVMalariaReportVersion1List = cHVMalariaReportVersion1Repository.findAll();
        assertThat(cHVMalariaReportVersion1List).hasSize(databaseSizeBeforeUpdate);
        CHVMalariaReportVersion1 testCHVMalariaReportVersion1 = cHVMalariaReportVersion1List.get(cHVMalariaReportVersion1List.size() - 1);
        assertThat(testCHVMalariaReportVersion1.getUid()).isEqualTo(UPDATED_UID);
        assertThat(testCHVMalariaReportVersion1.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testCHVMalariaReportVersion1.getLastUpdated()).isEqualTo(UPDATED_LAST_UPDATED);
        assertThat(testCHVMalariaReportVersion1.getTested()).isEqualTo(UPDATED_TESTED);
        assertThat(testCHVMalariaReportVersion1.getPositive()).isEqualTo(UPDATED_POSITIVE);
        assertThat(testCHVMalariaReportVersion1.getDrugsGiven()).isEqualTo(UPDATED_DRUGS_GIVEN);
        assertThat(testCHVMalariaReportVersion1.getSuppsGiven()).isEqualTo(UPDATED_SUPPS_GIVEN);
        assertThat(testCHVMalariaReportVersion1.getRdtBalance()).isEqualTo(UPDATED_RDT_BALANCE);
        assertThat(testCHVMalariaReportVersion1.getRdtReceived()).isEqualTo(UPDATED_RDT_RECEIVED);
        assertThat(testCHVMalariaReportVersion1.getRdtUsed()).isEqualTo(UPDATED_RDT_USED);
        assertThat(testCHVMalariaReportVersion1.getRdtDamagedLost()).isEqualTo(UPDATED_RDT_DAMAGED_LOST);
        assertThat(testCHVMalariaReportVersion1.getDrugsBalance()).isEqualTo(UPDATED_DRUGS_BALANCE);
        assertThat(testCHVMalariaReportVersion1.getDrugsReceived()).isEqualTo(UPDATED_DRUGS_RECEIVED);
        assertThat(testCHVMalariaReportVersion1.getDrugsUsed()).isEqualTo(UPDATED_DRUGS_USED);
        assertThat(testCHVMalariaReportVersion1.getDrugsDamagedLost()).isEqualTo(UPDATED_DRUGS_DAMAGED_LOST);
        assertThat(testCHVMalariaReportVersion1.getSuppsBalance()).isEqualTo(UPDATED_SUPPS_BALANCE);
        assertThat(testCHVMalariaReportVersion1.getSuppsReceived()).isEqualTo(UPDATED_SUPPS_RECEIVED);
        assertThat(testCHVMalariaReportVersion1.getSuppsUsed()).isEqualTo(UPDATED_SUPPS_USED);
        assertThat(testCHVMalariaReportVersion1.getSuppsDamagedLost()).isEqualTo(UPDATED_SUPPS_DAMAGED_LOST);
        assertThat(testCHVMalariaReportVersion1.getComment()).isEqualTo(UPDATED_COMMENT);
    }

    @Test
    @Transactional
    void putNonExistingCHVMalariaReportVersion1() throws Exception {
        int databaseSizeBeforeUpdate = cHVMalariaReportVersion1Repository.findAll().size();
        cHVMalariaReportVersion1.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCHVMalariaReportVersion1MockMvc
            .perform(
                put(ENTITY_API_URL_ID, cHVMalariaReportVersion1.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cHVMalariaReportVersion1))
            )
            .andExpect(status().isBadRequest());

        // Validate the CHVMalariaReportVersion1 in the database
        List<CHVMalariaReportVersion1> cHVMalariaReportVersion1List = cHVMalariaReportVersion1Repository.findAll();
        assertThat(cHVMalariaReportVersion1List).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCHVMalariaReportVersion1() throws Exception {
        int databaseSizeBeforeUpdate = cHVMalariaReportVersion1Repository.findAll().size();
        cHVMalariaReportVersion1.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCHVMalariaReportVersion1MockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cHVMalariaReportVersion1))
            )
            .andExpect(status().isBadRequest());

        // Validate the CHVMalariaReportVersion1 in the database
        List<CHVMalariaReportVersion1> cHVMalariaReportVersion1List = cHVMalariaReportVersion1Repository.findAll();
        assertThat(cHVMalariaReportVersion1List).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCHVMalariaReportVersion1() throws Exception {
        int databaseSizeBeforeUpdate = cHVMalariaReportVersion1Repository.findAll().size();
        cHVMalariaReportVersion1.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCHVMalariaReportVersion1MockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cHVMalariaReportVersion1))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CHVMalariaReportVersion1 in the database
        List<CHVMalariaReportVersion1> cHVMalariaReportVersion1List = cHVMalariaReportVersion1Repository.findAll();
        assertThat(cHVMalariaReportVersion1List).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCHVMalariaReportVersion1WithPatch() throws Exception {
        // Initialize the database
        cHVMalariaReportVersion1Repository.saveAndFlush(cHVMalariaReportVersion1);

        int databaseSizeBeforeUpdate = cHVMalariaReportVersion1Repository.findAll().size();

        // Update the cHVMalariaReportVersion1 using partial update
        CHVMalariaReportVersion1 partialUpdatedCHVMalariaReportVersion1 = new CHVMalariaReportVersion1();
        partialUpdatedCHVMalariaReportVersion1.setId(cHVMalariaReportVersion1.getId());

        partialUpdatedCHVMalariaReportVersion1
            .uid(UPDATED_UID)
            .created(UPDATED_CREATED)
            .tested(UPDATED_TESTED)
            .drugsGiven(UPDATED_DRUGS_GIVEN)
            .suppsGiven(UPDATED_SUPPS_GIVEN)
            .rdtBalance(UPDATED_RDT_BALANCE)
            .rdtReceived(UPDATED_RDT_RECEIVED)
            .rdtDamagedLost(UPDATED_RDT_DAMAGED_LOST)
            .drugsReceived(UPDATED_DRUGS_RECEIVED)
            .suppsReceived(UPDATED_SUPPS_RECEIVED)
            .suppsDamagedLost(UPDATED_SUPPS_DAMAGED_LOST);

        restCHVMalariaReportVersion1MockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCHVMalariaReportVersion1.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCHVMalariaReportVersion1))
            )
            .andExpect(status().isOk());

        // Validate the CHVMalariaReportVersion1 in the database
        List<CHVMalariaReportVersion1> cHVMalariaReportVersion1List = cHVMalariaReportVersion1Repository.findAll();
        assertThat(cHVMalariaReportVersion1List).hasSize(databaseSizeBeforeUpdate);
        CHVMalariaReportVersion1 testCHVMalariaReportVersion1 = cHVMalariaReportVersion1List.get(cHVMalariaReportVersion1List.size() - 1);
        assertThat(testCHVMalariaReportVersion1.getUid()).isEqualTo(UPDATED_UID);
        assertThat(testCHVMalariaReportVersion1.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testCHVMalariaReportVersion1.getLastUpdated()).isEqualTo(DEFAULT_LAST_UPDATED);
        assertThat(testCHVMalariaReportVersion1.getTested()).isEqualTo(UPDATED_TESTED);
        assertThat(testCHVMalariaReportVersion1.getPositive()).isEqualTo(DEFAULT_POSITIVE);
        assertThat(testCHVMalariaReportVersion1.getDrugsGiven()).isEqualTo(UPDATED_DRUGS_GIVEN);
        assertThat(testCHVMalariaReportVersion1.getSuppsGiven()).isEqualTo(UPDATED_SUPPS_GIVEN);
        assertThat(testCHVMalariaReportVersion1.getRdtBalance()).isEqualTo(UPDATED_RDT_BALANCE);
        assertThat(testCHVMalariaReportVersion1.getRdtReceived()).isEqualTo(UPDATED_RDT_RECEIVED);
        assertThat(testCHVMalariaReportVersion1.getRdtUsed()).isEqualTo(DEFAULT_RDT_USED);
        assertThat(testCHVMalariaReportVersion1.getRdtDamagedLost()).isEqualTo(UPDATED_RDT_DAMAGED_LOST);
        assertThat(testCHVMalariaReportVersion1.getDrugsBalance()).isEqualTo(DEFAULT_DRUGS_BALANCE);
        assertThat(testCHVMalariaReportVersion1.getDrugsReceived()).isEqualTo(UPDATED_DRUGS_RECEIVED);
        assertThat(testCHVMalariaReportVersion1.getDrugsUsed()).isEqualTo(DEFAULT_DRUGS_USED);
        assertThat(testCHVMalariaReportVersion1.getDrugsDamagedLost()).isEqualTo(DEFAULT_DRUGS_DAMAGED_LOST);
        assertThat(testCHVMalariaReportVersion1.getSuppsBalance()).isEqualTo(DEFAULT_SUPPS_BALANCE);
        assertThat(testCHVMalariaReportVersion1.getSuppsReceived()).isEqualTo(UPDATED_SUPPS_RECEIVED);
        assertThat(testCHVMalariaReportVersion1.getSuppsUsed()).isEqualTo(DEFAULT_SUPPS_USED);
        assertThat(testCHVMalariaReportVersion1.getSuppsDamagedLost()).isEqualTo(UPDATED_SUPPS_DAMAGED_LOST);
        assertThat(testCHVMalariaReportVersion1.getComment()).isEqualTo(DEFAULT_COMMENT);
    }

    @Test
    @Transactional
    void fullUpdateCHVMalariaReportVersion1WithPatch() throws Exception {
        // Initialize the database
        cHVMalariaReportVersion1Repository.saveAndFlush(cHVMalariaReportVersion1);

        int databaseSizeBeforeUpdate = cHVMalariaReportVersion1Repository.findAll().size();

        // Update the cHVMalariaReportVersion1 using partial update
        CHVMalariaReportVersion1 partialUpdatedCHVMalariaReportVersion1 = new CHVMalariaReportVersion1();
        partialUpdatedCHVMalariaReportVersion1.setId(cHVMalariaReportVersion1.getId());

        partialUpdatedCHVMalariaReportVersion1
            .uid(UPDATED_UID)
            .created(UPDATED_CREATED)
            .lastUpdated(UPDATED_LAST_UPDATED)
            .tested(UPDATED_TESTED)
            .positive(UPDATED_POSITIVE)
            .drugsGiven(UPDATED_DRUGS_GIVEN)
            .suppsGiven(UPDATED_SUPPS_GIVEN)
            .rdtBalance(UPDATED_RDT_BALANCE)
            .rdtReceived(UPDATED_RDT_RECEIVED)
            .rdtUsed(UPDATED_RDT_USED)
            .rdtDamagedLost(UPDATED_RDT_DAMAGED_LOST)
            .drugsBalance(UPDATED_DRUGS_BALANCE)
            .drugsReceived(UPDATED_DRUGS_RECEIVED)
            .drugsUsed(UPDATED_DRUGS_USED)
            .drugsDamagedLost(UPDATED_DRUGS_DAMAGED_LOST)
            .suppsBalance(UPDATED_SUPPS_BALANCE)
            .suppsReceived(UPDATED_SUPPS_RECEIVED)
            .suppsUsed(UPDATED_SUPPS_USED)
            .suppsDamagedLost(UPDATED_SUPPS_DAMAGED_LOST)
            .comment(UPDATED_COMMENT);

        restCHVMalariaReportVersion1MockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCHVMalariaReportVersion1.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCHVMalariaReportVersion1))
            )
            .andExpect(status().isOk());

        // Validate the CHVMalariaReportVersion1 in the database
        List<CHVMalariaReportVersion1> cHVMalariaReportVersion1List = cHVMalariaReportVersion1Repository.findAll();
        assertThat(cHVMalariaReportVersion1List).hasSize(databaseSizeBeforeUpdate);
        CHVMalariaReportVersion1 testCHVMalariaReportVersion1 = cHVMalariaReportVersion1List.get(cHVMalariaReportVersion1List.size() - 1);
        assertThat(testCHVMalariaReportVersion1.getUid()).isEqualTo(UPDATED_UID);
        assertThat(testCHVMalariaReportVersion1.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testCHVMalariaReportVersion1.getLastUpdated()).isEqualTo(UPDATED_LAST_UPDATED);
        assertThat(testCHVMalariaReportVersion1.getTested()).isEqualTo(UPDATED_TESTED);
        assertThat(testCHVMalariaReportVersion1.getPositive()).isEqualTo(UPDATED_POSITIVE);
        assertThat(testCHVMalariaReportVersion1.getDrugsGiven()).isEqualTo(UPDATED_DRUGS_GIVEN);
        assertThat(testCHVMalariaReportVersion1.getSuppsGiven()).isEqualTo(UPDATED_SUPPS_GIVEN);
        assertThat(testCHVMalariaReportVersion1.getRdtBalance()).isEqualTo(UPDATED_RDT_BALANCE);
        assertThat(testCHVMalariaReportVersion1.getRdtReceived()).isEqualTo(UPDATED_RDT_RECEIVED);
        assertThat(testCHVMalariaReportVersion1.getRdtUsed()).isEqualTo(UPDATED_RDT_USED);
        assertThat(testCHVMalariaReportVersion1.getRdtDamagedLost()).isEqualTo(UPDATED_RDT_DAMAGED_LOST);
        assertThat(testCHVMalariaReportVersion1.getDrugsBalance()).isEqualTo(UPDATED_DRUGS_BALANCE);
        assertThat(testCHVMalariaReportVersion1.getDrugsReceived()).isEqualTo(UPDATED_DRUGS_RECEIVED);
        assertThat(testCHVMalariaReportVersion1.getDrugsUsed()).isEqualTo(UPDATED_DRUGS_USED);
        assertThat(testCHVMalariaReportVersion1.getDrugsDamagedLost()).isEqualTo(UPDATED_DRUGS_DAMAGED_LOST);
        assertThat(testCHVMalariaReportVersion1.getSuppsBalance()).isEqualTo(UPDATED_SUPPS_BALANCE);
        assertThat(testCHVMalariaReportVersion1.getSuppsReceived()).isEqualTo(UPDATED_SUPPS_RECEIVED);
        assertThat(testCHVMalariaReportVersion1.getSuppsUsed()).isEqualTo(UPDATED_SUPPS_USED);
        assertThat(testCHVMalariaReportVersion1.getSuppsDamagedLost()).isEqualTo(UPDATED_SUPPS_DAMAGED_LOST);
        assertThat(testCHVMalariaReportVersion1.getComment()).isEqualTo(UPDATED_COMMENT);
    }

    @Test
    @Transactional
    void patchNonExistingCHVMalariaReportVersion1() throws Exception {
        int databaseSizeBeforeUpdate = cHVMalariaReportVersion1Repository.findAll().size();
        cHVMalariaReportVersion1.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCHVMalariaReportVersion1MockMvc
            .perform(
                patch(ENTITY_API_URL_ID, cHVMalariaReportVersion1.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cHVMalariaReportVersion1))
            )
            .andExpect(status().isBadRequest());

        // Validate the CHVMalariaReportVersion1 in the database
        List<CHVMalariaReportVersion1> cHVMalariaReportVersion1List = cHVMalariaReportVersion1Repository.findAll();
        assertThat(cHVMalariaReportVersion1List).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCHVMalariaReportVersion1() throws Exception {
        int databaseSizeBeforeUpdate = cHVMalariaReportVersion1Repository.findAll().size();
        cHVMalariaReportVersion1.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCHVMalariaReportVersion1MockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cHVMalariaReportVersion1))
            )
            .andExpect(status().isBadRequest());

        // Validate the CHVMalariaReportVersion1 in the database
        List<CHVMalariaReportVersion1> cHVMalariaReportVersion1List = cHVMalariaReportVersion1Repository.findAll();
        assertThat(cHVMalariaReportVersion1List).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCHVMalariaReportVersion1() throws Exception {
        int databaseSizeBeforeUpdate = cHVMalariaReportVersion1Repository.findAll().size();
        cHVMalariaReportVersion1.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCHVMalariaReportVersion1MockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cHVMalariaReportVersion1))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CHVMalariaReportVersion1 in the database
        List<CHVMalariaReportVersion1> cHVMalariaReportVersion1List = cHVMalariaReportVersion1Repository.findAll();
        assertThat(cHVMalariaReportVersion1List).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCHVMalariaReportVersion1() throws Exception {
        // Initialize the database
        cHVMalariaReportVersion1Repository.saveAndFlush(cHVMalariaReportVersion1);

        int databaseSizeBeforeDelete = cHVMalariaReportVersion1Repository.findAll().size();

        // Delete the cHVMalariaReportVersion1
        restCHVMalariaReportVersion1MockMvc
            .perform(delete(ENTITY_API_URL_ID, cHVMalariaReportVersion1.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CHVMalariaReportVersion1> cHVMalariaReportVersion1List = cHVMalariaReportVersion1Repository.findAll();
        assertThat(cHVMalariaReportVersion1List).hasSize(databaseSizeBeforeDelete - 1);
    }
}
