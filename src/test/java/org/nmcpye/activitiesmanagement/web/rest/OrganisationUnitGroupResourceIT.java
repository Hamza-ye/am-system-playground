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
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.nmcpye.activitiesmanagement.IntegrationTest;
import org.nmcpye.activitiesmanagement.domain.OrganisationUnitGroup;
import org.nmcpye.activitiesmanagement.repository.OrganisationUnitGroupRepository;
import org.nmcpye.activitiesmanagement.service.OrganisationUnitGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link OrganisationUnitGroupResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class OrganisationUnitGroupResourceIT {

    private static final String DEFAULT_UID = "AAAAAAAAAA";
    private static final String UPDATED_UID = "BBBBBBBBBB";

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_SHORT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_SHORT_NAME = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_LAST_UPDATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_UPDATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_SYMBOL = "AAAAAAAAAA";
    private static final String UPDATED_SYMBOL = "BBBBBBBBBB";

    private static final String DEFAULT_COLOR = "AAAAAAAAAA";
    private static final String UPDATED_COLOR = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/organisation-unit-groups";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private OrganisationUnitGroupRepository organisationUnitGroupRepository;

    @Mock
    private OrganisationUnitGroupRepository organisationUnitGroupRepositoryMock;

    @Mock
    private OrganisationUnitGroupService organisationUnitGroupServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOrganisationUnitGroupMockMvc;

    private OrganisationUnitGroup organisationUnitGroup;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrganisationUnitGroup createEntity(EntityManager em) {
        OrganisationUnitGroup organisationUnitGroup = new OrganisationUnitGroup()
            .uid(DEFAULT_UID)
            .code(DEFAULT_CODE)
            .name(DEFAULT_NAME)
            .shortName(DEFAULT_SHORT_NAME)
            .created(DEFAULT_CREATED)
            .lastUpdated(DEFAULT_LAST_UPDATED)
            .symbol(DEFAULT_SYMBOL)
            .color(DEFAULT_COLOR);
        return organisationUnitGroup;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrganisationUnitGroup createUpdatedEntity(EntityManager em) {
        OrganisationUnitGroup organisationUnitGroup = new OrganisationUnitGroup()
            .uid(UPDATED_UID)
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .shortName(UPDATED_SHORT_NAME)
            .created(UPDATED_CREATED)
            .lastUpdated(UPDATED_LAST_UPDATED)
            .symbol(UPDATED_SYMBOL)
            .color(UPDATED_COLOR);
        return organisationUnitGroup;
    }

    @BeforeEach
    public void initTest() {
        organisationUnitGroup = createEntity(em);
    }

    @Test
    @Transactional
    void createOrganisationUnitGroup() throws Exception {
        int databaseSizeBeforeCreate = organisationUnitGroupRepository.findAll().size();
        // Create the OrganisationUnitGroup
        restOrganisationUnitGroupMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(organisationUnitGroup))
            )
            .andExpect(status().isCreated());

        // Validate the OrganisationUnitGroup in the database
        List<OrganisationUnitGroup> organisationUnitGroupList = organisationUnitGroupRepository.findAll();
        assertThat(organisationUnitGroupList).hasSize(databaseSizeBeforeCreate + 1);
        OrganisationUnitGroup testOrganisationUnitGroup = organisationUnitGroupList.get(organisationUnitGroupList.size() - 1);
        assertThat(testOrganisationUnitGroup.getUid()).isEqualTo(DEFAULT_UID);
        assertThat(testOrganisationUnitGroup.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testOrganisationUnitGroup.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testOrganisationUnitGroup.getShortName()).isEqualTo(DEFAULT_SHORT_NAME);
        assertThat(testOrganisationUnitGroup.getCreated()).isEqualTo(DEFAULT_CREATED);
        assertThat(testOrganisationUnitGroup.getLastUpdated()).isEqualTo(DEFAULT_LAST_UPDATED);
        assertThat(testOrganisationUnitGroup.getSymbol()).isEqualTo(DEFAULT_SYMBOL);
        assertThat(testOrganisationUnitGroup.getColor()).isEqualTo(DEFAULT_COLOR);
    }

    @Test
    @Transactional
    void createOrganisationUnitGroupWithExistingId() throws Exception {
        // Create the OrganisationUnitGroup with an existing ID
        organisationUnitGroup.setId(1L);

        int databaseSizeBeforeCreate = organisationUnitGroupRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrganisationUnitGroupMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(organisationUnitGroup))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrganisationUnitGroup in the database
        List<OrganisationUnitGroup> organisationUnitGroupList = organisationUnitGroupRepository.findAll();
        assertThat(organisationUnitGroupList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkUidIsRequired() throws Exception {
        int databaseSizeBeforeTest = organisationUnitGroupRepository.findAll().size();
        // set the field null
        organisationUnitGroup.setUid(null);

        // Create the OrganisationUnitGroup, which fails.

        restOrganisationUnitGroupMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(organisationUnitGroup))
            )
            .andExpect(status().isBadRequest());

        List<OrganisationUnitGroup> organisationUnitGroupList = organisationUnitGroupRepository.findAll();
        assertThat(organisationUnitGroupList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllOrganisationUnitGroups() throws Exception {
        // Initialize the database
        organisationUnitGroupRepository.saveAndFlush(organisationUnitGroup);

        // Get all the organisationUnitGroupList
        restOrganisationUnitGroupMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(organisationUnitGroup.getId().intValue())))
            .andExpect(jsonPath("$.[*].uid").value(hasItem(DEFAULT_UID)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].shortName").value(hasItem(DEFAULT_SHORT_NAME)))
            .andExpect(jsonPath("$.[*].created").value(hasItem(DEFAULT_CREATED.toString())))
            .andExpect(jsonPath("$.[*].lastUpdated").value(hasItem(DEFAULT_LAST_UPDATED.toString())))
            .andExpect(jsonPath("$.[*].symbol").value(hasItem(DEFAULT_SYMBOL)))
            .andExpect(jsonPath("$.[*].color").value(hasItem(DEFAULT_COLOR)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllOrganisationUnitGroupsWithEagerRelationshipsIsEnabled() throws Exception {
        when(organisationUnitGroupServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restOrganisationUnitGroupMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(organisationUnitGroupServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllOrganisationUnitGroupsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(organisationUnitGroupServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restOrganisationUnitGroupMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(organisationUnitGroupServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getOrganisationUnitGroup() throws Exception {
        // Initialize the database
        organisationUnitGroupRepository.saveAndFlush(organisationUnitGroup);

        // Get the organisationUnitGroup
        restOrganisationUnitGroupMockMvc
            .perform(get(ENTITY_API_URL_ID, organisationUnitGroup.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(organisationUnitGroup.getId().intValue()))
            .andExpect(jsonPath("$.uid").value(DEFAULT_UID))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.shortName").value(DEFAULT_SHORT_NAME))
            .andExpect(jsonPath("$.created").value(DEFAULT_CREATED.toString()))
            .andExpect(jsonPath("$.lastUpdated").value(DEFAULT_LAST_UPDATED.toString()))
            .andExpect(jsonPath("$.symbol").value(DEFAULT_SYMBOL))
            .andExpect(jsonPath("$.color").value(DEFAULT_COLOR));
    }

    @Test
    @Transactional
    void getNonExistingOrganisationUnitGroup() throws Exception {
        // Get the organisationUnitGroup
        restOrganisationUnitGroupMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewOrganisationUnitGroup() throws Exception {
        // Initialize the database
        organisationUnitGroupRepository.saveAndFlush(organisationUnitGroup);

        int databaseSizeBeforeUpdate = organisationUnitGroupRepository.findAll().size();

        // Update the organisationUnitGroup
        OrganisationUnitGroup updatedOrganisationUnitGroup = organisationUnitGroupRepository.findById(organisationUnitGroup.getId()).get();
        // Disconnect from session so that the updates on updatedOrganisationUnitGroup are not directly saved in db
        em.detach(updatedOrganisationUnitGroup);
        updatedOrganisationUnitGroup
            .uid(UPDATED_UID)
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .shortName(UPDATED_SHORT_NAME)
            .created(UPDATED_CREATED)
            .lastUpdated(UPDATED_LAST_UPDATED)
            .symbol(UPDATED_SYMBOL)
            .color(UPDATED_COLOR);

        restOrganisationUnitGroupMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedOrganisationUnitGroup.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedOrganisationUnitGroup))
            )
            .andExpect(status().isOk());

        // Validate the OrganisationUnitGroup in the database
        List<OrganisationUnitGroup> organisationUnitGroupList = organisationUnitGroupRepository.findAll();
        assertThat(organisationUnitGroupList).hasSize(databaseSizeBeforeUpdate);
        OrganisationUnitGroup testOrganisationUnitGroup = organisationUnitGroupList.get(organisationUnitGroupList.size() - 1);
        assertThat(testOrganisationUnitGroup.getUid()).isEqualTo(UPDATED_UID);
        assertThat(testOrganisationUnitGroup.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testOrganisationUnitGroup.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testOrganisationUnitGroup.getShortName()).isEqualTo(UPDATED_SHORT_NAME);
        assertThat(testOrganisationUnitGroup.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testOrganisationUnitGroup.getLastUpdated()).isEqualTo(UPDATED_LAST_UPDATED);
        assertThat(testOrganisationUnitGroup.getSymbol()).isEqualTo(UPDATED_SYMBOL);
        assertThat(testOrganisationUnitGroup.getColor()).isEqualTo(UPDATED_COLOR);
    }

    @Test
    @Transactional
    void putNonExistingOrganisationUnitGroup() throws Exception {
        int databaseSizeBeforeUpdate = organisationUnitGroupRepository.findAll().size();
        organisationUnitGroup.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrganisationUnitGroupMockMvc
            .perform(
                put(ENTITY_API_URL_ID, organisationUnitGroup.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(organisationUnitGroup))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrganisationUnitGroup in the database
        List<OrganisationUnitGroup> organisationUnitGroupList = organisationUnitGroupRepository.findAll();
        assertThat(organisationUnitGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOrganisationUnitGroup() throws Exception {
        int databaseSizeBeforeUpdate = organisationUnitGroupRepository.findAll().size();
        organisationUnitGroup.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrganisationUnitGroupMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(organisationUnitGroup))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrganisationUnitGroup in the database
        List<OrganisationUnitGroup> organisationUnitGroupList = organisationUnitGroupRepository.findAll();
        assertThat(organisationUnitGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOrganisationUnitGroup() throws Exception {
        int databaseSizeBeforeUpdate = organisationUnitGroupRepository.findAll().size();
        organisationUnitGroup.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrganisationUnitGroupMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(organisationUnitGroup))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrganisationUnitGroup in the database
        List<OrganisationUnitGroup> organisationUnitGroupList = organisationUnitGroupRepository.findAll();
        assertThat(organisationUnitGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOrganisationUnitGroupWithPatch() throws Exception {
        // Initialize the database
        organisationUnitGroupRepository.saveAndFlush(organisationUnitGroup);

        int databaseSizeBeforeUpdate = organisationUnitGroupRepository.findAll().size();

        // Update the organisationUnitGroup using partial update
        OrganisationUnitGroup partialUpdatedOrganisationUnitGroup = new OrganisationUnitGroup();
        partialUpdatedOrganisationUnitGroup.setId(organisationUnitGroup.getId());

        partialUpdatedOrganisationUnitGroup.code(UPDATED_CODE).name(UPDATED_NAME).created(UPDATED_CREATED);

        restOrganisationUnitGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrganisationUnitGroup.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrganisationUnitGroup))
            )
            .andExpect(status().isOk());

        // Validate the OrganisationUnitGroup in the database
        List<OrganisationUnitGroup> organisationUnitGroupList = organisationUnitGroupRepository.findAll();
        assertThat(organisationUnitGroupList).hasSize(databaseSizeBeforeUpdate);
        OrganisationUnitGroup testOrganisationUnitGroup = organisationUnitGroupList.get(organisationUnitGroupList.size() - 1);
        assertThat(testOrganisationUnitGroup.getUid()).isEqualTo(DEFAULT_UID);
        assertThat(testOrganisationUnitGroup.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testOrganisationUnitGroup.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testOrganisationUnitGroup.getShortName()).isEqualTo(DEFAULT_SHORT_NAME);
        assertThat(testOrganisationUnitGroup.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testOrganisationUnitGroup.getLastUpdated()).isEqualTo(DEFAULT_LAST_UPDATED);
        assertThat(testOrganisationUnitGroup.getSymbol()).isEqualTo(DEFAULT_SYMBOL);
        assertThat(testOrganisationUnitGroup.getColor()).isEqualTo(DEFAULT_COLOR);
    }

    @Test
    @Transactional
    void fullUpdateOrganisationUnitGroupWithPatch() throws Exception {
        // Initialize the database
        organisationUnitGroupRepository.saveAndFlush(organisationUnitGroup);

        int databaseSizeBeforeUpdate = organisationUnitGroupRepository.findAll().size();

        // Update the organisationUnitGroup using partial update
        OrganisationUnitGroup partialUpdatedOrganisationUnitGroup = new OrganisationUnitGroup();
        partialUpdatedOrganisationUnitGroup.setId(organisationUnitGroup.getId());

        partialUpdatedOrganisationUnitGroup
            .uid(UPDATED_UID)
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .shortName(UPDATED_SHORT_NAME)
            .created(UPDATED_CREATED)
            .lastUpdated(UPDATED_LAST_UPDATED)
            .symbol(UPDATED_SYMBOL)
            .color(UPDATED_COLOR);

        restOrganisationUnitGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrganisationUnitGroup.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrganisationUnitGroup))
            )
            .andExpect(status().isOk());

        // Validate the OrganisationUnitGroup in the database
        List<OrganisationUnitGroup> organisationUnitGroupList = organisationUnitGroupRepository.findAll();
        assertThat(organisationUnitGroupList).hasSize(databaseSizeBeforeUpdate);
        OrganisationUnitGroup testOrganisationUnitGroup = organisationUnitGroupList.get(organisationUnitGroupList.size() - 1);
        assertThat(testOrganisationUnitGroup.getUid()).isEqualTo(UPDATED_UID);
        assertThat(testOrganisationUnitGroup.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testOrganisationUnitGroup.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testOrganisationUnitGroup.getShortName()).isEqualTo(UPDATED_SHORT_NAME);
        assertThat(testOrganisationUnitGroup.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testOrganisationUnitGroup.getLastUpdated()).isEqualTo(UPDATED_LAST_UPDATED);
        assertThat(testOrganisationUnitGroup.getSymbol()).isEqualTo(UPDATED_SYMBOL);
        assertThat(testOrganisationUnitGroup.getColor()).isEqualTo(UPDATED_COLOR);
    }

    @Test
    @Transactional
    void patchNonExistingOrganisationUnitGroup() throws Exception {
        int databaseSizeBeforeUpdate = organisationUnitGroupRepository.findAll().size();
        organisationUnitGroup.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrganisationUnitGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, organisationUnitGroup.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(organisationUnitGroup))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrganisationUnitGroup in the database
        List<OrganisationUnitGroup> organisationUnitGroupList = organisationUnitGroupRepository.findAll();
        assertThat(organisationUnitGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOrganisationUnitGroup() throws Exception {
        int databaseSizeBeforeUpdate = organisationUnitGroupRepository.findAll().size();
        organisationUnitGroup.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrganisationUnitGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(organisationUnitGroup))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrganisationUnitGroup in the database
        List<OrganisationUnitGroup> organisationUnitGroupList = organisationUnitGroupRepository.findAll();
        assertThat(organisationUnitGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOrganisationUnitGroup() throws Exception {
        int databaseSizeBeforeUpdate = organisationUnitGroupRepository.findAll().size();
        organisationUnitGroup.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrganisationUnitGroupMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(organisationUnitGroup))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrganisationUnitGroup in the database
        List<OrganisationUnitGroup> organisationUnitGroupList = organisationUnitGroupRepository.findAll();
        assertThat(organisationUnitGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOrganisationUnitGroup() throws Exception {
        // Initialize the database
        organisationUnitGroupRepository.saveAndFlush(organisationUnitGroup);

        int databaseSizeBeforeDelete = organisationUnitGroupRepository.findAll().size();

        // Delete the organisationUnitGroup
        restOrganisationUnitGroupMockMvc
            .perform(delete(ENTITY_API_URL_ID, organisationUnitGroup.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<OrganisationUnitGroup> organisationUnitGroupList = organisationUnitGroupRepository.findAll();
        assertThat(organisationUnitGroupList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
