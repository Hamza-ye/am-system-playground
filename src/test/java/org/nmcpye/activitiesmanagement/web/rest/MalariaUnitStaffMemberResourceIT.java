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
import org.nmcpye.activitiesmanagement.domain.MalariaUnit;
import org.nmcpye.activitiesmanagement.domain.MalariaUnitStaffMember;
import org.nmcpye.activitiesmanagement.domain.enumeration.MalariaUnitMemberType;
import org.nmcpye.activitiesmanagement.repository.MalariaUnitStaffMemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link MalariaUnitStaffMemberResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MalariaUnitStaffMemberResourceIT {

    private static final String DEFAULT_UID = "AAAAAAAAAA";
    private static final String UPDATED_UID = "BBBBBBBBBB";

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_LAST_UPDATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_UPDATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Integer DEFAULT_MEMBER_NO = 1;
    private static final Integer UPDATED_MEMBER_NO = 2;

    private static final MalariaUnitMemberType DEFAULT_MEMBER_TYPE = MalariaUnitMemberType.ADMIN;
    private static final MalariaUnitMemberType UPDATED_MEMBER_TYPE = MalariaUnitMemberType.LAP;

    private static final String ENTITY_API_URL = "/api/malaria-unit-staff-members";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MalariaUnitStaffMemberRepository malariaUnitStaffMemberRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMalariaUnitStaffMemberMockMvc;

    private MalariaUnitStaffMember malariaUnitStaffMember;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MalariaUnitStaffMember createEntity(EntityManager em) {
        MalariaUnitStaffMember malariaUnitStaffMember = new MalariaUnitStaffMember()
            .uid(DEFAULT_UID)
            .code(DEFAULT_CODE)
            .description(DEFAULT_DESCRIPTION)
            .created(DEFAULT_CREATED)
            .lastUpdated(DEFAULT_LAST_UPDATED)
            .memberNo(DEFAULT_MEMBER_NO)
            .memberType(DEFAULT_MEMBER_TYPE);
        // Add required entity
        MalariaUnit malariaUnit;
        if (TestUtil.findAll(em, MalariaUnit.class).isEmpty()) {
            malariaUnit = MalariaUnitResourceIT.createEntity(em);
            em.persist(malariaUnit);
            em.flush();
        } else {
            malariaUnit = TestUtil.findAll(em, MalariaUnit.class).get(0);
        }
        malariaUnitStaffMember.setMalariaUnit(malariaUnit);
        return malariaUnitStaffMember;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MalariaUnitStaffMember createUpdatedEntity(EntityManager em) {
        MalariaUnitStaffMember malariaUnitStaffMember = new MalariaUnitStaffMember()
            .uid(UPDATED_UID)
            .code(UPDATED_CODE)
            .description(UPDATED_DESCRIPTION)
            .created(UPDATED_CREATED)
            .lastUpdated(UPDATED_LAST_UPDATED)
            .memberNo(UPDATED_MEMBER_NO)
            .memberType(UPDATED_MEMBER_TYPE);
        // Add required entity
        MalariaUnit malariaUnit;
        if (TestUtil.findAll(em, MalariaUnit.class).isEmpty()) {
            malariaUnit = MalariaUnitResourceIT.createUpdatedEntity(em);
            em.persist(malariaUnit);
            em.flush();
        } else {
            malariaUnit = TestUtil.findAll(em, MalariaUnit.class).get(0);
        }
        malariaUnitStaffMember.setMalariaUnit(malariaUnit);
        return malariaUnitStaffMember;
    }

    @BeforeEach
    public void initTest() {
        malariaUnitStaffMember = createEntity(em);
    }

    @Test
    @Transactional
    void createMalariaUnitStaffMember() throws Exception {
        int databaseSizeBeforeCreate = malariaUnitStaffMemberRepository.findAll().size();
        // Create the MalariaUnitStaffMember
        restMalariaUnitStaffMemberMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(malariaUnitStaffMember))
            )
            .andExpect(status().isCreated());

        // Validate the MalariaUnitStaffMember in the database
        List<MalariaUnitStaffMember> malariaUnitStaffMemberList = malariaUnitStaffMemberRepository.findAll();
        assertThat(malariaUnitStaffMemberList).hasSize(databaseSizeBeforeCreate + 1);
        MalariaUnitStaffMember testMalariaUnitStaffMember = malariaUnitStaffMemberList.get(malariaUnitStaffMemberList.size() - 1);
        assertThat(testMalariaUnitStaffMember.getUid()).isEqualTo(DEFAULT_UID);
        assertThat(testMalariaUnitStaffMember.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testMalariaUnitStaffMember.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testMalariaUnitStaffMember.getCreated()).isEqualTo(DEFAULT_CREATED);
        assertThat(testMalariaUnitStaffMember.getLastUpdated()).isEqualTo(DEFAULT_LAST_UPDATED);
        assertThat(testMalariaUnitStaffMember.getMemberNo()).isEqualTo(DEFAULT_MEMBER_NO);
        assertThat(testMalariaUnitStaffMember.getMemberType()).isEqualTo(DEFAULT_MEMBER_TYPE);
    }

    @Test
    @Transactional
    void createMalariaUnitStaffMemberWithExistingId() throws Exception {
        // Create the MalariaUnitStaffMember with an existing ID
        malariaUnitStaffMember.setId(1L);

        int databaseSizeBeforeCreate = malariaUnitStaffMemberRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMalariaUnitStaffMemberMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(malariaUnitStaffMember))
            )
            .andExpect(status().isBadRequest());

        // Validate the MalariaUnitStaffMember in the database
        List<MalariaUnitStaffMember> malariaUnitStaffMemberList = malariaUnitStaffMemberRepository.findAll();
        assertThat(malariaUnitStaffMemberList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkUidIsRequired() throws Exception {
        int databaseSizeBeforeTest = malariaUnitStaffMemberRepository.findAll().size();
        // set the field null
        malariaUnitStaffMember.setUid(null);

        // Create the MalariaUnitStaffMember, which fails.

        restMalariaUnitStaffMemberMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(malariaUnitStaffMember))
            )
            .andExpect(status().isBadRequest());

        List<MalariaUnitStaffMember> malariaUnitStaffMemberList = malariaUnitStaffMemberRepository.findAll();
        assertThat(malariaUnitStaffMemberList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMemberNoIsRequired() throws Exception {
        int databaseSizeBeforeTest = malariaUnitStaffMemberRepository.findAll().size();
        // set the field null
        malariaUnitStaffMember.setMemberNo(null);

        // Create the MalariaUnitStaffMember, which fails.

        restMalariaUnitStaffMemberMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(malariaUnitStaffMember))
            )
            .andExpect(status().isBadRequest());

        List<MalariaUnitStaffMember> malariaUnitStaffMemberList = malariaUnitStaffMemberRepository.findAll();
        assertThat(malariaUnitStaffMemberList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMemberTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = malariaUnitStaffMemberRepository.findAll().size();
        // set the field null
        malariaUnitStaffMember.setMemberType(null);

        // Create the MalariaUnitStaffMember, which fails.

        restMalariaUnitStaffMemberMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(malariaUnitStaffMember))
            )
            .andExpect(status().isBadRequest());

        List<MalariaUnitStaffMember> malariaUnitStaffMemberList = malariaUnitStaffMemberRepository.findAll();
        assertThat(malariaUnitStaffMemberList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMalariaUnitStaffMembers() throws Exception {
        // Initialize the database
        malariaUnitStaffMemberRepository.saveAndFlush(malariaUnitStaffMember);

        // Get all the malariaUnitStaffMemberList
        restMalariaUnitStaffMemberMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(malariaUnitStaffMember.getId().intValue())))
            .andExpect(jsonPath("$.[*].uid").value(hasItem(DEFAULT_UID)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].created").value(hasItem(DEFAULT_CREATED.toString())))
            .andExpect(jsonPath("$.[*].lastUpdated").value(hasItem(DEFAULT_LAST_UPDATED.toString())))
            .andExpect(jsonPath("$.[*].memberNo").value(hasItem(DEFAULT_MEMBER_NO)))
            .andExpect(jsonPath("$.[*].memberType").value(hasItem(DEFAULT_MEMBER_TYPE.toString())));
    }

    @Test
    @Transactional
    void getMalariaUnitStaffMember() throws Exception {
        // Initialize the database
        malariaUnitStaffMemberRepository.saveAndFlush(malariaUnitStaffMember);

        // Get the malariaUnitStaffMember
        restMalariaUnitStaffMemberMockMvc
            .perform(get(ENTITY_API_URL_ID, malariaUnitStaffMember.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(malariaUnitStaffMember.getId().intValue()))
            .andExpect(jsonPath("$.uid").value(DEFAULT_UID))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.created").value(DEFAULT_CREATED.toString()))
            .andExpect(jsonPath("$.lastUpdated").value(DEFAULT_LAST_UPDATED.toString()))
            .andExpect(jsonPath("$.memberNo").value(DEFAULT_MEMBER_NO))
            .andExpect(jsonPath("$.memberType").value(DEFAULT_MEMBER_TYPE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingMalariaUnitStaffMember() throws Exception {
        // Get the malariaUnitStaffMember
        restMalariaUnitStaffMemberMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewMalariaUnitStaffMember() throws Exception {
        // Initialize the database
        malariaUnitStaffMemberRepository.saveAndFlush(malariaUnitStaffMember);

        int databaseSizeBeforeUpdate = malariaUnitStaffMemberRepository.findAll().size();

        // Update the malariaUnitStaffMember
        MalariaUnitStaffMember updatedMalariaUnitStaffMember = malariaUnitStaffMemberRepository
            .findById(malariaUnitStaffMember.getId())
            .get();
        // Disconnect from session so that the updates on updatedMalariaUnitStaffMember are not directly saved in db
        em.detach(updatedMalariaUnitStaffMember);
        updatedMalariaUnitStaffMember
            .uid(UPDATED_UID)
            .code(UPDATED_CODE)
            .description(UPDATED_DESCRIPTION)
            .created(UPDATED_CREATED)
            .lastUpdated(UPDATED_LAST_UPDATED)
            .memberNo(UPDATED_MEMBER_NO)
            .memberType(UPDATED_MEMBER_TYPE);

        restMalariaUnitStaffMemberMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedMalariaUnitStaffMember.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedMalariaUnitStaffMember))
            )
            .andExpect(status().isOk());

        // Validate the MalariaUnitStaffMember in the database
        List<MalariaUnitStaffMember> malariaUnitStaffMemberList = malariaUnitStaffMemberRepository.findAll();
        assertThat(malariaUnitStaffMemberList).hasSize(databaseSizeBeforeUpdate);
        MalariaUnitStaffMember testMalariaUnitStaffMember = malariaUnitStaffMemberList.get(malariaUnitStaffMemberList.size() - 1);
        assertThat(testMalariaUnitStaffMember.getUid()).isEqualTo(UPDATED_UID);
        assertThat(testMalariaUnitStaffMember.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testMalariaUnitStaffMember.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testMalariaUnitStaffMember.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testMalariaUnitStaffMember.getLastUpdated()).isEqualTo(UPDATED_LAST_UPDATED);
        assertThat(testMalariaUnitStaffMember.getMemberNo()).isEqualTo(UPDATED_MEMBER_NO);
        assertThat(testMalariaUnitStaffMember.getMemberType()).isEqualTo(UPDATED_MEMBER_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingMalariaUnitStaffMember() throws Exception {
        int databaseSizeBeforeUpdate = malariaUnitStaffMemberRepository.findAll().size();
        malariaUnitStaffMember.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMalariaUnitStaffMemberMockMvc
            .perform(
                put(ENTITY_API_URL_ID, malariaUnitStaffMember.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(malariaUnitStaffMember))
            )
            .andExpect(status().isBadRequest());

        // Validate the MalariaUnitStaffMember in the database
        List<MalariaUnitStaffMember> malariaUnitStaffMemberList = malariaUnitStaffMemberRepository.findAll();
        assertThat(malariaUnitStaffMemberList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMalariaUnitStaffMember() throws Exception {
        int databaseSizeBeforeUpdate = malariaUnitStaffMemberRepository.findAll().size();
        malariaUnitStaffMember.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMalariaUnitStaffMemberMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(malariaUnitStaffMember))
            )
            .andExpect(status().isBadRequest());

        // Validate the MalariaUnitStaffMember in the database
        List<MalariaUnitStaffMember> malariaUnitStaffMemberList = malariaUnitStaffMemberRepository.findAll();
        assertThat(malariaUnitStaffMemberList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMalariaUnitStaffMember() throws Exception {
        int databaseSizeBeforeUpdate = malariaUnitStaffMemberRepository.findAll().size();
        malariaUnitStaffMember.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMalariaUnitStaffMemberMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(malariaUnitStaffMember))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the MalariaUnitStaffMember in the database
        List<MalariaUnitStaffMember> malariaUnitStaffMemberList = malariaUnitStaffMemberRepository.findAll();
        assertThat(malariaUnitStaffMemberList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMalariaUnitStaffMemberWithPatch() throws Exception {
        // Initialize the database
        malariaUnitStaffMemberRepository.saveAndFlush(malariaUnitStaffMember);

        int databaseSizeBeforeUpdate = malariaUnitStaffMemberRepository.findAll().size();

        // Update the malariaUnitStaffMember using partial update
        MalariaUnitStaffMember partialUpdatedMalariaUnitStaffMember = new MalariaUnitStaffMember();
        partialUpdatedMalariaUnitStaffMember.setId(malariaUnitStaffMember.getId());

        partialUpdatedMalariaUnitStaffMember
            .uid(UPDATED_UID)
            .code(UPDATED_CODE)
            .created(UPDATED_CREATED)
            .lastUpdated(UPDATED_LAST_UPDATED)
            .memberType(UPDATED_MEMBER_TYPE);

        restMalariaUnitStaffMemberMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMalariaUnitStaffMember.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMalariaUnitStaffMember))
            )
            .andExpect(status().isOk());

        // Validate the MalariaUnitStaffMember in the database
        List<MalariaUnitStaffMember> malariaUnitStaffMemberList = malariaUnitStaffMemberRepository.findAll();
        assertThat(malariaUnitStaffMemberList).hasSize(databaseSizeBeforeUpdate);
        MalariaUnitStaffMember testMalariaUnitStaffMember = malariaUnitStaffMemberList.get(malariaUnitStaffMemberList.size() - 1);
        assertThat(testMalariaUnitStaffMember.getUid()).isEqualTo(UPDATED_UID);
        assertThat(testMalariaUnitStaffMember.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testMalariaUnitStaffMember.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testMalariaUnitStaffMember.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testMalariaUnitStaffMember.getLastUpdated()).isEqualTo(UPDATED_LAST_UPDATED);
        assertThat(testMalariaUnitStaffMember.getMemberNo()).isEqualTo(DEFAULT_MEMBER_NO);
        assertThat(testMalariaUnitStaffMember.getMemberType()).isEqualTo(UPDATED_MEMBER_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateMalariaUnitStaffMemberWithPatch() throws Exception {
        // Initialize the database
        malariaUnitStaffMemberRepository.saveAndFlush(malariaUnitStaffMember);

        int databaseSizeBeforeUpdate = malariaUnitStaffMemberRepository.findAll().size();

        // Update the malariaUnitStaffMember using partial update
        MalariaUnitStaffMember partialUpdatedMalariaUnitStaffMember = new MalariaUnitStaffMember();
        partialUpdatedMalariaUnitStaffMember.setId(malariaUnitStaffMember.getId());

        partialUpdatedMalariaUnitStaffMember
            .uid(UPDATED_UID)
            .code(UPDATED_CODE)
            .description(UPDATED_DESCRIPTION)
            .created(UPDATED_CREATED)
            .lastUpdated(UPDATED_LAST_UPDATED)
            .memberNo(UPDATED_MEMBER_NO)
            .memberType(UPDATED_MEMBER_TYPE);

        restMalariaUnitStaffMemberMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMalariaUnitStaffMember.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMalariaUnitStaffMember))
            )
            .andExpect(status().isOk());

        // Validate the MalariaUnitStaffMember in the database
        List<MalariaUnitStaffMember> malariaUnitStaffMemberList = malariaUnitStaffMemberRepository.findAll();
        assertThat(malariaUnitStaffMemberList).hasSize(databaseSizeBeforeUpdate);
        MalariaUnitStaffMember testMalariaUnitStaffMember = malariaUnitStaffMemberList.get(malariaUnitStaffMemberList.size() - 1);
        assertThat(testMalariaUnitStaffMember.getUid()).isEqualTo(UPDATED_UID);
        assertThat(testMalariaUnitStaffMember.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testMalariaUnitStaffMember.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testMalariaUnitStaffMember.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testMalariaUnitStaffMember.getLastUpdated()).isEqualTo(UPDATED_LAST_UPDATED);
        assertThat(testMalariaUnitStaffMember.getMemberNo()).isEqualTo(UPDATED_MEMBER_NO);
        assertThat(testMalariaUnitStaffMember.getMemberType()).isEqualTo(UPDATED_MEMBER_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingMalariaUnitStaffMember() throws Exception {
        int databaseSizeBeforeUpdate = malariaUnitStaffMemberRepository.findAll().size();
        malariaUnitStaffMember.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMalariaUnitStaffMemberMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, malariaUnitStaffMember.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(malariaUnitStaffMember))
            )
            .andExpect(status().isBadRequest());

        // Validate the MalariaUnitStaffMember in the database
        List<MalariaUnitStaffMember> malariaUnitStaffMemberList = malariaUnitStaffMemberRepository.findAll();
        assertThat(malariaUnitStaffMemberList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMalariaUnitStaffMember() throws Exception {
        int databaseSizeBeforeUpdate = malariaUnitStaffMemberRepository.findAll().size();
        malariaUnitStaffMember.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMalariaUnitStaffMemberMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(malariaUnitStaffMember))
            )
            .andExpect(status().isBadRequest());

        // Validate the MalariaUnitStaffMember in the database
        List<MalariaUnitStaffMember> malariaUnitStaffMemberList = malariaUnitStaffMemberRepository.findAll();
        assertThat(malariaUnitStaffMemberList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMalariaUnitStaffMember() throws Exception {
        int databaseSizeBeforeUpdate = malariaUnitStaffMemberRepository.findAll().size();
        malariaUnitStaffMember.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMalariaUnitStaffMemberMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(malariaUnitStaffMember))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the MalariaUnitStaffMember in the database
        List<MalariaUnitStaffMember> malariaUnitStaffMemberList = malariaUnitStaffMemberRepository.findAll();
        assertThat(malariaUnitStaffMemberList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMalariaUnitStaffMember() throws Exception {
        // Initialize the database
        malariaUnitStaffMemberRepository.saveAndFlush(malariaUnitStaffMember);

        int databaseSizeBeforeDelete = malariaUnitStaffMemberRepository.findAll().size();

        // Delete the malariaUnitStaffMember
        restMalariaUnitStaffMemberMockMvc
            .perform(delete(ENTITY_API_URL_ID, malariaUnitStaffMember.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<MalariaUnitStaffMember> malariaUnitStaffMemberList = malariaUnitStaffMemberRepository.findAll();
        assertThat(malariaUnitStaffMemberList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
