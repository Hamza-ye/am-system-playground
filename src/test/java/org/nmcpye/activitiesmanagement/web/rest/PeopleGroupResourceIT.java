package org.nmcpye.activitiesmanagement.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.nmcpye.activitiesmanagement.IntegrationTest;
import org.nmcpye.activitiesmanagement.domain.PeopleGroup;
import org.nmcpye.activitiesmanagement.repository.PeopleGroupRepository;
import org.nmcpye.activitiesmanagement.service.PeopleGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link PeopleGroupResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class PeopleGroupResourceIT {

    private static final String DEFAULT_UID = "AAAAAAAAAA";
    private static final String UPDATED_UID = "BBBBBBBBBB";

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_LAST_UPDATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_UPDATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final UUID DEFAULT_UUID = UUID.randomUUID();
    private static final UUID UPDATED_UUID = UUID.randomUUID();

    private static final String ENTITY_API_URL = "/api/people-groups";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PeopleGroupRepository peopleGroupRepository;

    @Mock
    private PeopleGroupRepository peopleGroupRepositoryMock;

    @Mock
    private PeopleGroupService peopleGroupServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPeopleGroupMockMvc;

    private PeopleGroup peopleGroup;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PeopleGroup createEntity(EntityManager em) {
        PeopleGroup peopleGroup = new PeopleGroup()
            .uid(DEFAULT_UID)
            .code(DEFAULT_CODE)
            .name(DEFAULT_NAME)
            .created(DEFAULT_CREATED)
            .lastUpdated(DEFAULT_LAST_UPDATED)
            .uuid(DEFAULT_UUID);
        return peopleGroup;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PeopleGroup createUpdatedEntity(EntityManager em) {
        PeopleGroup peopleGroup = new PeopleGroup()
            .uid(UPDATED_UID)
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .created(UPDATED_CREATED)
            .lastUpdated(UPDATED_LAST_UPDATED)
            .uuid(UPDATED_UUID);
        return peopleGroup;
    }

    @BeforeEach
    public void initTest() {
        peopleGroup = createEntity(em);
    }

    @Test
    @Transactional
    void createPeopleGroup() throws Exception {
        int databaseSizeBeforeCreate = peopleGroupRepository.findAll().size();
        // Create the PeopleGroup
        restPeopleGroupMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(peopleGroup)))
            .andExpect(status().isCreated());

        // Validate the PeopleGroup in the database
        List<PeopleGroup> peopleGroupList = peopleGroupRepository.findAll();
        assertThat(peopleGroupList).hasSize(databaseSizeBeforeCreate + 1);
        PeopleGroup testPeopleGroup = peopleGroupList.get(peopleGroupList.size() - 1);
        assertThat(testPeopleGroup.getUid()).isEqualTo(DEFAULT_UID);
        assertThat(testPeopleGroup.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testPeopleGroup.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPeopleGroup.getCreated()).isEqualTo(DEFAULT_CREATED);
        assertThat(testPeopleGroup.getLastUpdated()).isEqualTo(DEFAULT_LAST_UPDATED);
        assertThat(testPeopleGroup.getUuid()).isEqualTo(DEFAULT_UUID);
    }

    @Test
    @Transactional
    void createPeopleGroupWithExistingId() throws Exception {
        // Create the PeopleGroup with an existing ID
        peopleGroup.setId(1L);

        int databaseSizeBeforeCreate = peopleGroupRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPeopleGroupMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(peopleGroup)))
            .andExpect(status().isBadRequest());

        // Validate the PeopleGroup in the database
        List<PeopleGroup> peopleGroupList = peopleGroupRepository.findAll();
        assertThat(peopleGroupList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkUidIsRequired() throws Exception {
        int databaseSizeBeforeTest = peopleGroupRepository.findAll().size();
        // set the field null
        peopleGroup.setUid(null);

        // Create the PeopleGroup, which fails.

        restPeopleGroupMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(peopleGroup)))
            .andExpect(status().isBadRequest());

        List<PeopleGroup> peopleGroupList = peopleGroupRepository.findAll();
        assertThat(peopleGroupList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPeopleGroups() throws Exception {
        // Initialize the database
        peopleGroupRepository.saveAndFlush(peopleGroup);

        // Get all the peopleGroupList
        restPeopleGroupMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(peopleGroup.getId().intValue())))
            .andExpect(jsonPath("$.[*].uid").value(hasItem(DEFAULT_UID)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].created").value(hasItem(DEFAULT_CREATED.toString())))
            .andExpect(jsonPath("$.[*].lastUpdated").value(hasItem(DEFAULT_LAST_UPDATED.toString())))
            .andExpect(jsonPath("$.[*].uuid").value(hasItem(DEFAULT_UUID.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPeopleGroupsWithEagerRelationshipsIsEnabled() throws Exception {
        when(peopleGroupServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPeopleGroupMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(peopleGroupServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPeopleGroupsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(peopleGroupServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPeopleGroupMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(peopleGroupServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getPeopleGroup() throws Exception {
        // Initialize the database
        peopleGroupRepository.saveAndFlush(peopleGroup);

        // Get the peopleGroup
        restPeopleGroupMockMvc
            .perform(get(ENTITY_API_URL_ID, peopleGroup.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(peopleGroup.getId().intValue()))
            .andExpect(jsonPath("$.uid").value(DEFAULT_UID))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.created").value(DEFAULT_CREATED.toString()))
            .andExpect(jsonPath("$.lastUpdated").value(DEFAULT_LAST_UPDATED.toString()))
            .andExpect(jsonPath("$.uuid").value(DEFAULT_UUID.toString()));
    }

    @Test
    @Transactional
    void getNonExistingPeopleGroup() throws Exception {
        // Get the peopleGroup
        restPeopleGroupMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPeopleGroup() throws Exception {
        // Initialize the database
        peopleGroupRepository.saveAndFlush(peopleGroup);

        int databaseSizeBeforeUpdate = peopleGroupRepository.findAll().size();

        // Update the peopleGroup
        PeopleGroup updatedPeopleGroup = peopleGroupRepository.findById(peopleGroup.getId()).get();
        // Disconnect from session so that the updates on updatedPeopleGroup are not directly saved in db
        em.detach(updatedPeopleGroup);
        updatedPeopleGroup
            .uid(UPDATED_UID)
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .created(UPDATED_CREATED)
            .lastUpdated(UPDATED_LAST_UPDATED)
            .uuid(UPDATED_UUID);

        restPeopleGroupMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPeopleGroup.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPeopleGroup))
            )
            .andExpect(status().isOk());

        // Validate the PeopleGroup in the database
        List<PeopleGroup> peopleGroupList = peopleGroupRepository.findAll();
        assertThat(peopleGroupList).hasSize(databaseSizeBeforeUpdate);
        PeopleGroup testPeopleGroup = peopleGroupList.get(peopleGroupList.size() - 1);
        assertThat(testPeopleGroup.getUid()).isEqualTo(UPDATED_UID);
        assertThat(testPeopleGroup.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testPeopleGroup.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPeopleGroup.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testPeopleGroup.getLastUpdated()).isEqualTo(UPDATED_LAST_UPDATED);
        assertThat(testPeopleGroup.getUuid()).isEqualTo(UPDATED_UUID);
    }

    @Test
    @Transactional
    void putNonExistingPeopleGroup() throws Exception {
        int databaseSizeBeforeUpdate = peopleGroupRepository.findAll().size();
        peopleGroup.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPeopleGroupMockMvc
            .perform(
                put(ENTITY_API_URL_ID, peopleGroup.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(peopleGroup))
            )
            .andExpect(status().isBadRequest());

        // Validate the PeopleGroup in the database
        List<PeopleGroup> peopleGroupList = peopleGroupRepository.findAll();
        assertThat(peopleGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPeopleGroup() throws Exception {
        int databaseSizeBeforeUpdate = peopleGroupRepository.findAll().size();
        peopleGroup.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPeopleGroupMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(peopleGroup))
            )
            .andExpect(status().isBadRequest());

        // Validate the PeopleGroup in the database
        List<PeopleGroup> peopleGroupList = peopleGroupRepository.findAll();
        assertThat(peopleGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPeopleGroup() throws Exception {
        int databaseSizeBeforeUpdate = peopleGroupRepository.findAll().size();
        peopleGroup.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPeopleGroupMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(peopleGroup)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PeopleGroup in the database
        List<PeopleGroup> peopleGroupList = peopleGroupRepository.findAll();
        assertThat(peopleGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePeopleGroupWithPatch() throws Exception {
        // Initialize the database
        peopleGroupRepository.saveAndFlush(peopleGroup);

        int databaseSizeBeforeUpdate = peopleGroupRepository.findAll().size();

        // Update the peopleGroup using partial update
        PeopleGroup partialUpdatedPeopleGroup = new PeopleGroup();
        partialUpdatedPeopleGroup.setId(peopleGroup.getId());

        partialUpdatedPeopleGroup.code(UPDATED_CODE).name(UPDATED_NAME).uuid(UPDATED_UUID);

        restPeopleGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPeopleGroup.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPeopleGroup))
            )
            .andExpect(status().isOk());

        // Validate the PeopleGroup in the database
        List<PeopleGroup> peopleGroupList = peopleGroupRepository.findAll();
        assertThat(peopleGroupList).hasSize(databaseSizeBeforeUpdate);
        PeopleGroup testPeopleGroup = peopleGroupList.get(peopleGroupList.size() - 1);
        assertThat(testPeopleGroup.getUid()).isEqualTo(DEFAULT_UID);
        assertThat(testPeopleGroup.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testPeopleGroup.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPeopleGroup.getCreated()).isEqualTo(DEFAULT_CREATED);
        assertThat(testPeopleGroup.getLastUpdated()).isEqualTo(DEFAULT_LAST_UPDATED);
        assertThat(testPeopleGroup.getUuid()).isEqualTo(UPDATED_UUID);
    }

    @Test
    @Transactional
    void fullUpdatePeopleGroupWithPatch() throws Exception {
        // Initialize the database
        peopleGroupRepository.saveAndFlush(peopleGroup);

        int databaseSizeBeforeUpdate = peopleGroupRepository.findAll().size();

        // Update the peopleGroup using partial update
        PeopleGroup partialUpdatedPeopleGroup = new PeopleGroup();
        partialUpdatedPeopleGroup.setId(peopleGroup.getId());

        partialUpdatedPeopleGroup
            .uid(UPDATED_UID)
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .created(UPDATED_CREATED)
            .lastUpdated(UPDATED_LAST_UPDATED)
            .uuid(UPDATED_UUID);

        restPeopleGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPeopleGroup.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPeopleGroup))
            )
            .andExpect(status().isOk());

        // Validate the PeopleGroup in the database
        List<PeopleGroup> peopleGroupList = peopleGroupRepository.findAll();
        assertThat(peopleGroupList).hasSize(databaseSizeBeforeUpdate);
        PeopleGroup testPeopleGroup = peopleGroupList.get(peopleGroupList.size() - 1);
        assertThat(testPeopleGroup.getUid()).isEqualTo(UPDATED_UID);
        assertThat(testPeopleGroup.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testPeopleGroup.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPeopleGroup.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testPeopleGroup.getLastUpdated()).isEqualTo(UPDATED_LAST_UPDATED);
        assertThat(testPeopleGroup.getUuid()).isEqualTo(UPDATED_UUID);
    }

    @Test
    @Transactional
    void patchNonExistingPeopleGroup() throws Exception {
        int databaseSizeBeforeUpdate = peopleGroupRepository.findAll().size();
        peopleGroup.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPeopleGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, peopleGroup.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(peopleGroup))
            )
            .andExpect(status().isBadRequest());

        // Validate the PeopleGroup in the database
        List<PeopleGroup> peopleGroupList = peopleGroupRepository.findAll();
        assertThat(peopleGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPeopleGroup() throws Exception {
        int databaseSizeBeforeUpdate = peopleGroupRepository.findAll().size();
        peopleGroup.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPeopleGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(peopleGroup))
            )
            .andExpect(status().isBadRequest());

        // Validate the PeopleGroup in the database
        List<PeopleGroup> peopleGroupList = peopleGroupRepository.findAll();
        assertThat(peopleGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPeopleGroup() throws Exception {
        int databaseSizeBeforeUpdate = peopleGroupRepository.findAll().size();
        peopleGroup.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPeopleGroupMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(peopleGroup))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PeopleGroup in the database
        List<PeopleGroup> peopleGroupList = peopleGroupRepository.findAll();
        assertThat(peopleGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePeopleGroup() throws Exception {
        // Initialize the database
        peopleGroupRepository.saveAndFlush(peopleGroup);

        int databaseSizeBeforeDelete = peopleGroupRepository.findAll().size();

        // Delete the peopleGroup
        restPeopleGroupMockMvc
            .perform(delete(ENTITY_API_URL_ID, peopleGroup.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PeopleGroup> peopleGroupList = peopleGroupRepository.findAll();
        assertThat(peopleGroupList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
