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
import org.nmcpye.activitiesmanagement.domain.PersonAuthorityGroup;
import org.nmcpye.activitiesmanagement.repository.PersonAuthorityGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link PersonAuthorityGroupResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PersonAuthorityGroupResourceIT {

    private static final String DEFAULT_UID = "AAAAAAAAAA";
    private static final String UPDATED_UID = "BBBBBBBBBB";

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_LAST_UPDATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_UPDATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/person-authority-groups";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PersonAuthorityGroupRepository personAuthorityGroupRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPersonAuthorityGroupMockMvc;

    private PersonAuthorityGroup personAuthorityGroup;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PersonAuthorityGroup createEntity(EntityManager em) {
        PersonAuthorityGroup personAuthorityGroup = new PersonAuthorityGroup()
            .uid(DEFAULT_UID)
            .code(DEFAULT_CODE)
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .created(DEFAULT_CREATED)
            .lastUpdated(DEFAULT_LAST_UPDATED);
        return personAuthorityGroup;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PersonAuthorityGroup createUpdatedEntity(EntityManager em) {
        PersonAuthorityGroup personAuthorityGroup = new PersonAuthorityGroup()
            .uid(UPDATED_UID)
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .created(UPDATED_CREATED)
            .lastUpdated(UPDATED_LAST_UPDATED);
        return personAuthorityGroup;
    }

    @BeforeEach
    public void initTest() {
        personAuthorityGroup = createEntity(em);
    }

    @Test
    @Transactional
    void createPersonAuthorityGroup() throws Exception {
        int databaseSizeBeforeCreate = personAuthorityGroupRepository.findAll().size();
        // Create the PersonAuthorityGroup
        restPersonAuthorityGroupMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(personAuthorityGroup))
            )
            .andExpect(status().isCreated());

        // Validate the PersonAuthorityGroup in the database
        List<PersonAuthorityGroup> personAuthorityGroupList = personAuthorityGroupRepository.findAll();
        assertThat(personAuthorityGroupList).hasSize(databaseSizeBeforeCreate + 1);
        PersonAuthorityGroup testPersonAuthorityGroup = personAuthorityGroupList.get(personAuthorityGroupList.size() - 1);
        assertThat(testPersonAuthorityGroup.getUid()).isEqualTo(DEFAULT_UID);
        assertThat(testPersonAuthorityGroup.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testPersonAuthorityGroup.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPersonAuthorityGroup.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testPersonAuthorityGroup.getCreated()).isEqualTo(DEFAULT_CREATED);
        assertThat(testPersonAuthorityGroup.getLastUpdated()).isEqualTo(DEFAULT_LAST_UPDATED);
    }

    @Test
    @Transactional
    void createPersonAuthorityGroupWithExistingId() throws Exception {
        // Create the PersonAuthorityGroup with an existing ID
        personAuthorityGroup.setId(1L);

        int databaseSizeBeforeCreate = personAuthorityGroupRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPersonAuthorityGroupMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(personAuthorityGroup))
            )
            .andExpect(status().isBadRequest());

        // Validate the PersonAuthorityGroup in the database
        List<PersonAuthorityGroup> personAuthorityGroupList = personAuthorityGroupRepository.findAll();
        assertThat(personAuthorityGroupList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkUidIsRequired() throws Exception {
        int databaseSizeBeforeTest = personAuthorityGroupRepository.findAll().size();
        // set the field null
        personAuthorityGroup.setUid(null);

        // Create the PersonAuthorityGroup, which fails.

        restPersonAuthorityGroupMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(personAuthorityGroup))
            )
            .andExpect(status().isBadRequest());

        List<PersonAuthorityGroup> personAuthorityGroupList = personAuthorityGroupRepository.findAll();
        assertThat(personAuthorityGroupList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPersonAuthorityGroups() throws Exception {
        // Initialize the database
        personAuthorityGroupRepository.saveAndFlush(personAuthorityGroup);

        // Get all the personAuthorityGroupList
        restPersonAuthorityGroupMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(personAuthorityGroup.getId().intValue())))
            .andExpect(jsonPath("$.[*].uid").value(hasItem(DEFAULT_UID)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].created").value(hasItem(DEFAULT_CREATED.toString())))
            .andExpect(jsonPath("$.[*].lastUpdated").value(hasItem(DEFAULT_LAST_UPDATED.toString())));
    }

    @Test
    @Transactional
    void getPersonAuthorityGroup() throws Exception {
        // Initialize the database
        personAuthorityGroupRepository.saveAndFlush(personAuthorityGroup);

        // Get the personAuthorityGroup
        restPersonAuthorityGroupMockMvc
            .perform(get(ENTITY_API_URL_ID, personAuthorityGroup.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(personAuthorityGroup.getId().intValue()))
            .andExpect(jsonPath("$.uid").value(DEFAULT_UID))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.created").value(DEFAULT_CREATED.toString()))
            .andExpect(jsonPath("$.lastUpdated").value(DEFAULT_LAST_UPDATED.toString()));
    }

    @Test
    @Transactional
    void getNonExistingPersonAuthorityGroup() throws Exception {
        // Get the personAuthorityGroup
        restPersonAuthorityGroupMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPersonAuthorityGroup() throws Exception {
        // Initialize the database
        personAuthorityGroupRepository.saveAndFlush(personAuthorityGroup);

        int databaseSizeBeforeUpdate = personAuthorityGroupRepository.findAll().size();

        // Update the personAuthorityGroup
        PersonAuthorityGroup updatedPersonAuthorityGroup = personAuthorityGroupRepository.findById(personAuthorityGroup.getId()).get();
        // Disconnect from session so that the updates on updatedPersonAuthorityGroup are not directly saved in db
        em.detach(updatedPersonAuthorityGroup);
        updatedPersonAuthorityGroup
            .uid(UPDATED_UID)
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .created(UPDATED_CREATED)
            .lastUpdated(UPDATED_LAST_UPDATED);

        restPersonAuthorityGroupMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPersonAuthorityGroup.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPersonAuthorityGroup))
            )
            .andExpect(status().isOk());

        // Validate the PersonAuthorityGroup in the database
        List<PersonAuthorityGroup> personAuthorityGroupList = personAuthorityGroupRepository.findAll();
        assertThat(personAuthorityGroupList).hasSize(databaseSizeBeforeUpdate);
        PersonAuthorityGroup testPersonAuthorityGroup = personAuthorityGroupList.get(personAuthorityGroupList.size() - 1);
        assertThat(testPersonAuthorityGroup.getUid()).isEqualTo(UPDATED_UID);
        assertThat(testPersonAuthorityGroup.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testPersonAuthorityGroup.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPersonAuthorityGroup.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testPersonAuthorityGroup.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testPersonAuthorityGroup.getLastUpdated()).isEqualTo(UPDATED_LAST_UPDATED);
    }

    @Test
    @Transactional
    void putNonExistingPersonAuthorityGroup() throws Exception {
        int databaseSizeBeforeUpdate = personAuthorityGroupRepository.findAll().size();
        personAuthorityGroup.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPersonAuthorityGroupMockMvc
            .perform(
                put(ENTITY_API_URL_ID, personAuthorityGroup.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(personAuthorityGroup))
            )
            .andExpect(status().isBadRequest());

        // Validate the PersonAuthorityGroup in the database
        List<PersonAuthorityGroup> personAuthorityGroupList = personAuthorityGroupRepository.findAll();
        assertThat(personAuthorityGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPersonAuthorityGroup() throws Exception {
        int databaseSizeBeforeUpdate = personAuthorityGroupRepository.findAll().size();
        personAuthorityGroup.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonAuthorityGroupMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(personAuthorityGroup))
            )
            .andExpect(status().isBadRequest());

        // Validate the PersonAuthorityGroup in the database
        List<PersonAuthorityGroup> personAuthorityGroupList = personAuthorityGroupRepository.findAll();
        assertThat(personAuthorityGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPersonAuthorityGroup() throws Exception {
        int databaseSizeBeforeUpdate = personAuthorityGroupRepository.findAll().size();
        personAuthorityGroup.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonAuthorityGroupMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(personAuthorityGroup))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PersonAuthorityGroup in the database
        List<PersonAuthorityGroup> personAuthorityGroupList = personAuthorityGroupRepository.findAll();
        assertThat(personAuthorityGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePersonAuthorityGroupWithPatch() throws Exception {
        // Initialize the database
        personAuthorityGroupRepository.saveAndFlush(personAuthorityGroup);

        int databaseSizeBeforeUpdate = personAuthorityGroupRepository.findAll().size();

        // Update the personAuthorityGroup using partial update
        PersonAuthorityGroup partialUpdatedPersonAuthorityGroup = new PersonAuthorityGroup();
        partialUpdatedPersonAuthorityGroup.setId(personAuthorityGroup.getId());

        partialUpdatedPersonAuthorityGroup.uid(UPDATED_UID).code(UPDATED_CODE).created(UPDATED_CREATED);

        restPersonAuthorityGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPersonAuthorityGroup.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPersonAuthorityGroup))
            )
            .andExpect(status().isOk());

        // Validate the PersonAuthorityGroup in the database
        List<PersonAuthorityGroup> personAuthorityGroupList = personAuthorityGroupRepository.findAll();
        assertThat(personAuthorityGroupList).hasSize(databaseSizeBeforeUpdate);
        PersonAuthorityGroup testPersonAuthorityGroup = personAuthorityGroupList.get(personAuthorityGroupList.size() - 1);
        assertThat(testPersonAuthorityGroup.getUid()).isEqualTo(UPDATED_UID);
        assertThat(testPersonAuthorityGroup.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testPersonAuthorityGroup.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPersonAuthorityGroup.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testPersonAuthorityGroup.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testPersonAuthorityGroup.getLastUpdated()).isEqualTo(DEFAULT_LAST_UPDATED);
    }

    @Test
    @Transactional
    void fullUpdatePersonAuthorityGroupWithPatch() throws Exception {
        // Initialize the database
        personAuthorityGroupRepository.saveAndFlush(personAuthorityGroup);

        int databaseSizeBeforeUpdate = personAuthorityGroupRepository.findAll().size();

        // Update the personAuthorityGroup using partial update
        PersonAuthorityGroup partialUpdatedPersonAuthorityGroup = new PersonAuthorityGroup();
        partialUpdatedPersonAuthorityGroup.setId(personAuthorityGroup.getId());

        partialUpdatedPersonAuthorityGroup
            .uid(UPDATED_UID)
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .created(UPDATED_CREATED)
            .lastUpdated(UPDATED_LAST_UPDATED);

        restPersonAuthorityGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPersonAuthorityGroup.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPersonAuthorityGroup))
            )
            .andExpect(status().isOk());

        // Validate the PersonAuthorityGroup in the database
        List<PersonAuthorityGroup> personAuthorityGroupList = personAuthorityGroupRepository.findAll();
        assertThat(personAuthorityGroupList).hasSize(databaseSizeBeforeUpdate);
        PersonAuthorityGroup testPersonAuthorityGroup = personAuthorityGroupList.get(personAuthorityGroupList.size() - 1);
        assertThat(testPersonAuthorityGroup.getUid()).isEqualTo(UPDATED_UID);
        assertThat(testPersonAuthorityGroup.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testPersonAuthorityGroup.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPersonAuthorityGroup.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testPersonAuthorityGroup.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testPersonAuthorityGroup.getLastUpdated()).isEqualTo(UPDATED_LAST_UPDATED);
    }

    @Test
    @Transactional
    void patchNonExistingPersonAuthorityGroup() throws Exception {
        int databaseSizeBeforeUpdate = personAuthorityGroupRepository.findAll().size();
        personAuthorityGroup.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPersonAuthorityGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, personAuthorityGroup.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(personAuthorityGroup))
            )
            .andExpect(status().isBadRequest());

        // Validate the PersonAuthorityGroup in the database
        List<PersonAuthorityGroup> personAuthorityGroupList = personAuthorityGroupRepository.findAll();
        assertThat(personAuthorityGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPersonAuthorityGroup() throws Exception {
        int databaseSizeBeforeUpdate = personAuthorityGroupRepository.findAll().size();
        personAuthorityGroup.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonAuthorityGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(personAuthorityGroup))
            )
            .andExpect(status().isBadRequest());

        // Validate the PersonAuthorityGroup in the database
        List<PersonAuthorityGroup> personAuthorityGroupList = personAuthorityGroupRepository.findAll();
        assertThat(personAuthorityGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPersonAuthorityGroup() throws Exception {
        int databaseSizeBeforeUpdate = personAuthorityGroupRepository.findAll().size();
        personAuthorityGroup.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonAuthorityGroupMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(personAuthorityGroup))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PersonAuthorityGroup in the database
        List<PersonAuthorityGroup> personAuthorityGroupList = personAuthorityGroupRepository.findAll();
        assertThat(personAuthorityGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePersonAuthorityGroup() throws Exception {
        // Initialize the database
        personAuthorityGroupRepository.saveAndFlush(personAuthorityGroup);

        int databaseSizeBeforeDelete = personAuthorityGroupRepository.findAll().size();

        // Delete the personAuthorityGroup
        restPersonAuthorityGroupMockMvc
            .perform(delete(ENTITY_API_URL_ID, personAuthorityGroup.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PersonAuthorityGroup> personAuthorityGroupList = personAuthorityGroupRepository.findAll();
        assertThat(personAuthorityGroupList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
