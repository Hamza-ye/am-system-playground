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
import org.nmcpye.activitiesmanagement.domain.OrganisationUnitGroupSet;
import org.nmcpye.activitiesmanagement.repository.OrganisationUnitGroupSetRepository;
import org.nmcpye.activitiesmanagement.service.OrganisationUnitGroupSetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link OrganisationUnitGroupSetResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class OrganisationUnitGroupSetResourceIT {

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

    private static final Boolean DEFAULT_COMPULSORY = false;
    private static final Boolean UPDATED_COMPULSORY = true;

    private static final Boolean DEFAULT_INCLUDE_SUBHIERARCHY_IN_ANALYTICS = false;
    private static final Boolean UPDATED_INCLUDE_SUBHIERARCHY_IN_ANALYTICS = true;

    private static final String ENTITY_API_URL = "/api/organisation-unit-group-sets";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private OrganisationUnitGroupSetRepository organisationUnitGroupSetRepository;

    @Mock
    private OrganisationUnitGroupSetRepository organisationUnitGroupSetRepositoryMock;

    @Mock
    private OrganisationUnitGroupSetService organisationUnitGroupSetServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOrganisationUnitGroupSetMockMvc;

    private OrganisationUnitGroupSet organisationUnitGroupSet;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrganisationUnitGroupSet createEntity(EntityManager em) {
        OrganisationUnitGroupSet organisationUnitGroupSet = new OrganisationUnitGroupSet()
            .uid(DEFAULT_UID)
            .code(DEFAULT_CODE)
            .name(DEFAULT_NAME)
            .created(DEFAULT_CREATED)
            .lastUpdated(DEFAULT_LAST_UPDATED)
            .compulsory(DEFAULT_COMPULSORY)
            .includeSubhierarchyInAnalytics(DEFAULT_INCLUDE_SUBHIERARCHY_IN_ANALYTICS);
        return organisationUnitGroupSet;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrganisationUnitGroupSet createUpdatedEntity(EntityManager em) {
        OrganisationUnitGroupSet organisationUnitGroupSet = new OrganisationUnitGroupSet()
            .uid(UPDATED_UID)
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .created(UPDATED_CREATED)
            .lastUpdated(UPDATED_LAST_UPDATED)
            .compulsory(UPDATED_COMPULSORY)
            .includeSubhierarchyInAnalytics(UPDATED_INCLUDE_SUBHIERARCHY_IN_ANALYTICS);
        return organisationUnitGroupSet;
    }

    @BeforeEach
    public void initTest() {
        organisationUnitGroupSet = createEntity(em);
    }

    @Test
    @Transactional
    void createOrganisationUnitGroupSet() throws Exception {
        int databaseSizeBeforeCreate = organisationUnitGroupSetRepository.findAll().size();
        // Create the OrganisationUnitGroupSet
        restOrganisationUnitGroupSetMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(organisationUnitGroupSet))
            )
            .andExpect(status().isCreated());

        // Validate the OrganisationUnitGroupSet in the database
        List<OrganisationUnitGroupSet> organisationUnitGroupSetList = organisationUnitGroupSetRepository.findAll();
        assertThat(organisationUnitGroupSetList).hasSize(databaseSizeBeforeCreate + 1);
        OrganisationUnitGroupSet testOrganisationUnitGroupSet = organisationUnitGroupSetList.get(organisationUnitGroupSetList.size() - 1);
        assertThat(testOrganisationUnitGroupSet.getUid()).isEqualTo(DEFAULT_UID);
        assertThat(testOrganisationUnitGroupSet.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testOrganisationUnitGroupSet.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testOrganisationUnitGroupSet.getCreated()).isEqualTo(DEFAULT_CREATED);
        assertThat(testOrganisationUnitGroupSet.getLastUpdated()).isEqualTo(DEFAULT_LAST_UPDATED);
        assertThat(testOrganisationUnitGroupSet.getCompulsory()).isEqualTo(DEFAULT_COMPULSORY);
        assertThat(testOrganisationUnitGroupSet.getIncludeSubhierarchyInAnalytics()).isEqualTo(DEFAULT_INCLUDE_SUBHIERARCHY_IN_ANALYTICS);
    }

    @Test
    @Transactional
    void createOrganisationUnitGroupSetWithExistingId() throws Exception {
        // Create the OrganisationUnitGroupSet with an existing ID
        organisationUnitGroupSet.setId(1L);

        int databaseSizeBeforeCreate = organisationUnitGroupSetRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrganisationUnitGroupSetMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(organisationUnitGroupSet))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrganisationUnitGroupSet in the database
        List<OrganisationUnitGroupSet> organisationUnitGroupSetList = organisationUnitGroupSetRepository.findAll();
        assertThat(organisationUnitGroupSetList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkUidIsRequired() throws Exception {
        int databaseSizeBeforeTest = organisationUnitGroupSetRepository.findAll().size();
        // set the field null
        organisationUnitGroupSet.setUid(null);

        // Create the OrganisationUnitGroupSet, which fails.

        restOrganisationUnitGroupSetMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(organisationUnitGroupSet))
            )
            .andExpect(status().isBadRequest());

        List<OrganisationUnitGroupSet> organisationUnitGroupSetList = organisationUnitGroupSetRepository.findAll();
        assertThat(organisationUnitGroupSetList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllOrganisationUnitGroupSets() throws Exception {
        // Initialize the database
        organisationUnitGroupSetRepository.saveAndFlush(organisationUnitGroupSet);

        // Get all the organisationUnitGroupSetList
        restOrganisationUnitGroupSetMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(organisationUnitGroupSet.getId().intValue())))
            .andExpect(jsonPath("$.[*].uid").value(hasItem(DEFAULT_UID)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].created").value(hasItem(DEFAULT_CREATED.toString())))
            .andExpect(jsonPath("$.[*].lastUpdated").value(hasItem(DEFAULT_LAST_UPDATED.toString())))
            .andExpect(jsonPath("$.[*].compulsory").value(hasItem(DEFAULT_COMPULSORY.booleanValue())))
            .andExpect(
                jsonPath("$.[*].includeSubhierarchyInAnalytics").value(hasItem(DEFAULT_INCLUDE_SUBHIERARCHY_IN_ANALYTICS.booleanValue()))
            );
    }

    @SuppressWarnings({ "unchecked" })
    void getAllOrganisationUnitGroupSetsWithEagerRelationshipsIsEnabled() throws Exception {
        when(organisationUnitGroupSetServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restOrganisationUnitGroupSetMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(organisationUnitGroupSetServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllOrganisationUnitGroupSetsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(organisationUnitGroupSetServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restOrganisationUnitGroupSetMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(organisationUnitGroupSetServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getOrganisationUnitGroupSet() throws Exception {
        // Initialize the database
        organisationUnitGroupSetRepository.saveAndFlush(organisationUnitGroupSet);

        // Get the organisationUnitGroupSet
        restOrganisationUnitGroupSetMockMvc
            .perform(get(ENTITY_API_URL_ID, organisationUnitGroupSet.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(organisationUnitGroupSet.getId().intValue()))
            .andExpect(jsonPath("$.uid").value(DEFAULT_UID))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.created").value(DEFAULT_CREATED.toString()))
            .andExpect(jsonPath("$.lastUpdated").value(DEFAULT_LAST_UPDATED.toString()))
            .andExpect(jsonPath("$.compulsory").value(DEFAULT_COMPULSORY.booleanValue()))
            .andExpect(jsonPath("$.includeSubhierarchyInAnalytics").value(DEFAULT_INCLUDE_SUBHIERARCHY_IN_ANALYTICS.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingOrganisationUnitGroupSet() throws Exception {
        // Get the organisationUnitGroupSet
        restOrganisationUnitGroupSetMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewOrganisationUnitGroupSet() throws Exception {
        // Initialize the database
        organisationUnitGroupSetRepository.saveAndFlush(organisationUnitGroupSet);

        int databaseSizeBeforeUpdate = organisationUnitGroupSetRepository.findAll().size();

        // Update the organisationUnitGroupSet
        OrganisationUnitGroupSet updatedOrganisationUnitGroupSet = organisationUnitGroupSetRepository
            .findById(organisationUnitGroupSet.getId())
            .get();
        // Disconnect from session so that the updates on updatedOrganisationUnitGroupSet are not directly saved in db
        em.detach(updatedOrganisationUnitGroupSet);
        updatedOrganisationUnitGroupSet
            .uid(UPDATED_UID)
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .created(UPDATED_CREATED)
            .lastUpdated(UPDATED_LAST_UPDATED)
            .compulsory(UPDATED_COMPULSORY)
            .includeSubhierarchyInAnalytics(UPDATED_INCLUDE_SUBHIERARCHY_IN_ANALYTICS);

        restOrganisationUnitGroupSetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedOrganisationUnitGroupSet.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedOrganisationUnitGroupSet))
            )
            .andExpect(status().isOk());

        // Validate the OrganisationUnitGroupSet in the database
        List<OrganisationUnitGroupSet> organisationUnitGroupSetList = organisationUnitGroupSetRepository.findAll();
        assertThat(organisationUnitGroupSetList).hasSize(databaseSizeBeforeUpdate);
        OrganisationUnitGroupSet testOrganisationUnitGroupSet = organisationUnitGroupSetList.get(organisationUnitGroupSetList.size() - 1);
        assertThat(testOrganisationUnitGroupSet.getUid()).isEqualTo(UPDATED_UID);
        assertThat(testOrganisationUnitGroupSet.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testOrganisationUnitGroupSet.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testOrganisationUnitGroupSet.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testOrganisationUnitGroupSet.getLastUpdated()).isEqualTo(UPDATED_LAST_UPDATED);
        assertThat(testOrganisationUnitGroupSet.getCompulsory()).isEqualTo(UPDATED_COMPULSORY);
        assertThat(testOrganisationUnitGroupSet.getIncludeSubhierarchyInAnalytics()).isEqualTo(UPDATED_INCLUDE_SUBHIERARCHY_IN_ANALYTICS);
    }

    @Test
    @Transactional
    void putNonExistingOrganisationUnitGroupSet() throws Exception {
        int databaseSizeBeforeUpdate = organisationUnitGroupSetRepository.findAll().size();
        organisationUnitGroupSet.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrganisationUnitGroupSetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, organisationUnitGroupSet.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(organisationUnitGroupSet))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrganisationUnitGroupSet in the database
        List<OrganisationUnitGroupSet> organisationUnitGroupSetList = organisationUnitGroupSetRepository.findAll();
        assertThat(organisationUnitGroupSetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOrganisationUnitGroupSet() throws Exception {
        int databaseSizeBeforeUpdate = organisationUnitGroupSetRepository.findAll().size();
        organisationUnitGroupSet.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrganisationUnitGroupSetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(organisationUnitGroupSet))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrganisationUnitGroupSet in the database
        List<OrganisationUnitGroupSet> organisationUnitGroupSetList = organisationUnitGroupSetRepository.findAll();
        assertThat(organisationUnitGroupSetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOrganisationUnitGroupSet() throws Exception {
        int databaseSizeBeforeUpdate = organisationUnitGroupSetRepository.findAll().size();
        organisationUnitGroupSet.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrganisationUnitGroupSetMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(organisationUnitGroupSet))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrganisationUnitGroupSet in the database
        List<OrganisationUnitGroupSet> organisationUnitGroupSetList = organisationUnitGroupSetRepository.findAll();
        assertThat(organisationUnitGroupSetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOrganisationUnitGroupSetWithPatch() throws Exception {
        // Initialize the database
        organisationUnitGroupSetRepository.saveAndFlush(organisationUnitGroupSet);

        int databaseSizeBeforeUpdate = organisationUnitGroupSetRepository.findAll().size();

        // Update the organisationUnitGroupSet using partial update
        OrganisationUnitGroupSet partialUpdatedOrganisationUnitGroupSet = new OrganisationUnitGroupSet();
        partialUpdatedOrganisationUnitGroupSet.setId(organisationUnitGroupSet.getId());

        partialUpdatedOrganisationUnitGroupSet
            .uid(UPDATED_UID)
            .name(UPDATED_NAME)
            .created(UPDATED_CREATED)
            .lastUpdated(UPDATED_LAST_UPDATED)
            .compulsory(UPDATED_COMPULSORY)
            .includeSubhierarchyInAnalytics(UPDATED_INCLUDE_SUBHIERARCHY_IN_ANALYTICS);

        restOrganisationUnitGroupSetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrganisationUnitGroupSet.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrganisationUnitGroupSet))
            )
            .andExpect(status().isOk());

        // Validate the OrganisationUnitGroupSet in the database
        List<OrganisationUnitGroupSet> organisationUnitGroupSetList = organisationUnitGroupSetRepository.findAll();
        assertThat(organisationUnitGroupSetList).hasSize(databaseSizeBeforeUpdate);
        OrganisationUnitGroupSet testOrganisationUnitGroupSet = organisationUnitGroupSetList.get(organisationUnitGroupSetList.size() - 1);
        assertThat(testOrganisationUnitGroupSet.getUid()).isEqualTo(UPDATED_UID);
        assertThat(testOrganisationUnitGroupSet.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testOrganisationUnitGroupSet.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testOrganisationUnitGroupSet.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testOrganisationUnitGroupSet.getLastUpdated()).isEqualTo(UPDATED_LAST_UPDATED);
        assertThat(testOrganisationUnitGroupSet.getCompulsory()).isEqualTo(UPDATED_COMPULSORY);
        assertThat(testOrganisationUnitGroupSet.getIncludeSubhierarchyInAnalytics()).isEqualTo(UPDATED_INCLUDE_SUBHIERARCHY_IN_ANALYTICS);
    }

    @Test
    @Transactional
    void fullUpdateOrganisationUnitGroupSetWithPatch() throws Exception {
        // Initialize the database
        organisationUnitGroupSetRepository.saveAndFlush(organisationUnitGroupSet);

        int databaseSizeBeforeUpdate = organisationUnitGroupSetRepository.findAll().size();

        // Update the organisationUnitGroupSet using partial update
        OrganisationUnitGroupSet partialUpdatedOrganisationUnitGroupSet = new OrganisationUnitGroupSet();
        partialUpdatedOrganisationUnitGroupSet.setId(organisationUnitGroupSet.getId());

        partialUpdatedOrganisationUnitGroupSet
            .uid(UPDATED_UID)
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .created(UPDATED_CREATED)
            .lastUpdated(UPDATED_LAST_UPDATED)
            .compulsory(UPDATED_COMPULSORY)
            .includeSubhierarchyInAnalytics(UPDATED_INCLUDE_SUBHIERARCHY_IN_ANALYTICS);

        restOrganisationUnitGroupSetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrganisationUnitGroupSet.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrganisationUnitGroupSet))
            )
            .andExpect(status().isOk());

        // Validate the OrganisationUnitGroupSet in the database
        List<OrganisationUnitGroupSet> organisationUnitGroupSetList = organisationUnitGroupSetRepository.findAll();
        assertThat(organisationUnitGroupSetList).hasSize(databaseSizeBeforeUpdate);
        OrganisationUnitGroupSet testOrganisationUnitGroupSet = organisationUnitGroupSetList.get(organisationUnitGroupSetList.size() - 1);
        assertThat(testOrganisationUnitGroupSet.getUid()).isEqualTo(UPDATED_UID);
        assertThat(testOrganisationUnitGroupSet.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testOrganisationUnitGroupSet.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testOrganisationUnitGroupSet.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testOrganisationUnitGroupSet.getLastUpdated()).isEqualTo(UPDATED_LAST_UPDATED);
        assertThat(testOrganisationUnitGroupSet.getCompulsory()).isEqualTo(UPDATED_COMPULSORY);
        assertThat(testOrganisationUnitGroupSet.getIncludeSubhierarchyInAnalytics()).isEqualTo(UPDATED_INCLUDE_SUBHIERARCHY_IN_ANALYTICS);
    }

    @Test
    @Transactional
    void patchNonExistingOrganisationUnitGroupSet() throws Exception {
        int databaseSizeBeforeUpdate = organisationUnitGroupSetRepository.findAll().size();
        organisationUnitGroupSet.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrganisationUnitGroupSetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, organisationUnitGroupSet.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(organisationUnitGroupSet))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrganisationUnitGroupSet in the database
        List<OrganisationUnitGroupSet> organisationUnitGroupSetList = organisationUnitGroupSetRepository.findAll();
        assertThat(organisationUnitGroupSetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOrganisationUnitGroupSet() throws Exception {
        int databaseSizeBeforeUpdate = organisationUnitGroupSetRepository.findAll().size();
        organisationUnitGroupSet.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrganisationUnitGroupSetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(organisationUnitGroupSet))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrganisationUnitGroupSet in the database
        List<OrganisationUnitGroupSet> organisationUnitGroupSetList = organisationUnitGroupSetRepository.findAll();
        assertThat(organisationUnitGroupSetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOrganisationUnitGroupSet() throws Exception {
        int databaseSizeBeforeUpdate = organisationUnitGroupSetRepository.findAll().size();
        organisationUnitGroupSet.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrganisationUnitGroupSetMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(organisationUnitGroupSet))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrganisationUnitGroupSet in the database
        List<OrganisationUnitGroupSet> organisationUnitGroupSetList = organisationUnitGroupSetRepository.findAll();
        assertThat(organisationUnitGroupSetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOrganisationUnitGroupSet() throws Exception {
        // Initialize the database
        organisationUnitGroupSetRepository.saveAndFlush(organisationUnitGroupSet);

        int databaseSizeBeforeDelete = organisationUnitGroupSetRepository.findAll().size();

        // Delete the organisationUnitGroupSet
        restOrganisationUnitGroupSetMockMvc
            .perform(delete(ENTITY_API_URL_ID, organisationUnitGroupSet.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<OrganisationUnitGroupSet> organisationUnitGroupSetList = organisationUnitGroupSetRepository.findAll();
        assertThat(organisationUnitGroupSetList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
