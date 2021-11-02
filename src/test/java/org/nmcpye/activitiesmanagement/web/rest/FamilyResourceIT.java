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
import org.nmcpye.activitiesmanagement.domain.OrganisationUnit;
import org.nmcpye.activitiesmanagement.repository.FamilyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link FamilyResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FamilyResourceIT {

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

    private static final Integer DEFAULT_FAMILY_NO = 1;
    private static final Integer UPDATED_FAMILY_NO = 2;

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/families";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FamilyRepository familyRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFamilyMockMvc;

    private Family family;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Family createEntity(EntityManager em) {
        Family family = new Family()
            .uid(DEFAULT_UID)
            .code(DEFAULT_CODE)
            .name(DEFAULT_NAME)
            .created(DEFAULT_CREATED)
            .lastUpdated(DEFAULT_LAST_UPDATED)
            .familyNo(DEFAULT_FAMILY_NO)
            .address(DEFAULT_ADDRESS);
        // Add required entity
        OrganisationUnit organisationUnit;
        if (TestUtil.findAll(em, OrganisationUnit.class).isEmpty()) {
            organisationUnit = OrganisationUnitResourceIT.createEntity(em);
            em.persist(organisationUnit);
            em.flush();
        } else {
            organisationUnit = TestUtil.findAll(em, OrganisationUnit.class).get(0);
        }
        family.setOrganisationUnit(organisationUnit);
        return family;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Family createUpdatedEntity(EntityManager em) {
        Family family = new Family()
            .uid(UPDATED_UID)
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .created(UPDATED_CREATED)
            .lastUpdated(UPDATED_LAST_UPDATED)
            .familyNo(UPDATED_FAMILY_NO)
            .address(UPDATED_ADDRESS);
        // Add required entity
        OrganisationUnit organisationUnit;
        if (TestUtil.findAll(em, OrganisationUnit.class).isEmpty()) {
            organisationUnit = OrganisationUnitResourceIT.createUpdatedEntity(em);
            em.persist(organisationUnit);
            em.flush();
        } else {
            organisationUnit = TestUtil.findAll(em, OrganisationUnit.class).get(0);
        }
        family.setOrganisationUnit(organisationUnit);
        return family;
    }

    @BeforeEach
    public void initTest() {
        family = createEntity(em);
    }

    @Test
    @Transactional
    void createFamily() throws Exception {
        int databaseSizeBeforeCreate = familyRepository.findAll().size();
        // Create the Family
        restFamilyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(family)))
            .andExpect(status().isCreated());

        // Validate the Family in the database
        List<Family> familyList = familyRepository.findAll();
        assertThat(familyList).hasSize(databaseSizeBeforeCreate + 1);
        Family testFamily = familyList.get(familyList.size() - 1);
        assertThat(testFamily.getUid()).isEqualTo(DEFAULT_UID);
        assertThat(testFamily.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testFamily.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testFamily.getCreated()).isEqualTo(DEFAULT_CREATED);
        assertThat(testFamily.getLastUpdated()).isEqualTo(DEFAULT_LAST_UPDATED);
        assertThat(testFamily.getFamilyNo()).isEqualTo(DEFAULT_FAMILY_NO);
        assertThat(testFamily.getAddress()).isEqualTo(DEFAULT_ADDRESS);
    }

    @Test
    @Transactional
    void createFamilyWithExistingId() throws Exception {
        // Create the Family with an existing ID
        family.setId(1L);

        int databaseSizeBeforeCreate = familyRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFamilyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(family)))
            .andExpect(status().isBadRequest());

        // Validate the Family in the database
        List<Family> familyList = familyRepository.findAll();
        assertThat(familyList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkUidIsRequired() throws Exception {
        int databaseSizeBeforeTest = familyRepository.findAll().size();
        // set the field null
        family.setUid(null);

        // Create the Family, which fails.

        restFamilyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(family)))
            .andExpect(status().isBadRequest());

        List<Family> familyList = familyRepository.findAll();
        assertThat(familyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFamilyNoIsRequired() throws Exception {
        int databaseSizeBeforeTest = familyRepository.findAll().size();
        // set the field null
        family.setFamilyNo(null);

        // Create the Family, which fails.

        restFamilyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(family)))
            .andExpect(status().isBadRequest());

        List<Family> familyList = familyRepository.findAll();
        assertThat(familyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllFamilies() throws Exception {
        // Initialize the database
        familyRepository.saveAndFlush(family);

        // Get all the familyList
        restFamilyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(family.getId().intValue())))
            .andExpect(jsonPath("$.[*].uid").value(hasItem(DEFAULT_UID)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].created").value(hasItem(DEFAULT_CREATED.toString())))
            .andExpect(jsonPath("$.[*].lastUpdated").value(hasItem(DEFAULT_LAST_UPDATED.toString())))
            .andExpect(jsonPath("$.[*].familyNo").value(hasItem(DEFAULT_FAMILY_NO)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)));
    }

    @Test
    @Transactional
    void getFamily() throws Exception {
        // Initialize the database
        familyRepository.saveAndFlush(family);

        // Get the family
        restFamilyMockMvc
            .perform(get(ENTITY_API_URL_ID, family.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(family.getId().intValue()))
            .andExpect(jsonPath("$.uid").value(DEFAULT_UID))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.created").value(DEFAULT_CREATED.toString()))
            .andExpect(jsonPath("$.lastUpdated").value(DEFAULT_LAST_UPDATED.toString()))
            .andExpect(jsonPath("$.familyNo").value(DEFAULT_FAMILY_NO))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS));
    }

    @Test
    @Transactional
    void getNonExistingFamily() throws Exception {
        // Get the family
        restFamilyMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewFamily() throws Exception {
        // Initialize the database
        familyRepository.saveAndFlush(family);

        int databaseSizeBeforeUpdate = familyRepository.findAll().size();

        // Update the family
        Family updatedFamily = familyRepository.findById(family.getId()).get();
        // Disconnect from session so that the updates on updatedFamily are not directly saved in db
        em.detach(updatedFamily);
        updatedFamily
            .uid(UPDATED_UID)
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .created(UPDATED_CREATED)
            .lastUpdated(UPDATED_LAST_UPDATED)
            .familyNo(UPDATED_FAMILY_NO)
            .address(UPDATED_ADDRESS);

        restFamilyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedFamily.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedFamily))
            )
            .andExpect(status().isOk());

        // Validate the Family in the database
        List<Family> familyList = familyRepository.findAll();
        assertThat(familyList).hasSize(databaseSizeBeforeUpdate);
        Family testFamily = familyList.get(familyList.size() - 1);
        assertThat(testFamily.getUid()).isEqualTo(UPDATED_UID);
        assertThat(testFamily.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testFamily.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testFamily.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testFamily.getLastUpdated()).isEqualTo(UPDATED_LAST_UPDATED);
        assertThat(testFamily.getFamilyNo()).isEqualTo(UPDATED_FAMILY_NO);
        assertThat(testFamily.getAddress()).isEqualTo(UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void putNonExistingFamily() throws Exception {
        int databaseSizeBeforeUpdate = familyRepository.findAll().size();
        family.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFamilyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, family.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(family))
            )
            .andExpect(status().isBadRequest());

        // Validate the Family in the database
        List<Family> familyList = familyRepository.findAll();
        assertThat(familyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFamily() throws Exception {
        int databaseSizeBeforeUpdate = familyRepository.findAll().size();
        family.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFamilyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(family))
            )
            .andExpect(status().isBadRequest());

        // Validate the Family in the database
        List<Family> familyList = familyRepository.findAll();
        assertThat(familyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFamily() throws Exception {
        int databaseSizeBeforeUpdate = familyRepository.findAll().size();
        family.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFamilyMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(family)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Family in the database
        List<Family> familyList = familyRepository.findAll();
        assertThat(familyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFamilyWithPatch() throws Exception {
        // Initialize the database
        familyRepository.saveAndFlush(family);

        int databaseSizeBeforeUpdate = familyRepository.findAll().size();

        // Update the family using partial update
        Family partialUpdatedFamily = new Family();
        partialUpdatedFamily.setId(family.getId());

        partialUpdatedFamily.code(UPDATED_CODE).familyNo(UPDATED_FAMILY_NO).address(UPDATED_ADDRESS);

        restFamilyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFamily.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFamily))
            )
            .andExpect(status().isOk());

        // Validate the Family in the database
        List<Family> familyList = familyRepository.findAll();
        assertThat(familyList).hasSize(databaseSizeBeforeUpdate);
        Family testFamily = familyList.get(familyList.size() - 1);
        assertThat(testFamily.getUid()).isEqualTo(DEFAULT_UID);
        assertThat(testFamily.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testFamily.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testFamily.getCreated()).isEqualTo(DEFAULT_CREATED);
        assertThat(testFamily.getLastUpdated()).isEqualTo(DEFAULT_LAST_UPDATED);
        assertThat(testFamily.getFamilyNo()).isEqualTo(UPDATED_FAMILY_NO);
        assertThat(testFamily.getAddress()).isEqualTo(UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void fullUpdateFamilyWithPatch() throws Exception {
        // Initialize the database
        familyRepository.saveAndFlush(family);

        int databaseSizeBeforeUpdate = familyRepository.findAll().size();

        // Update the family using partial update
        Family partialUpdatedFamily = new Family();
        partialUpdatedFamily.setId(family.getId());

        partialUpdatedFamily
            .uid(UPDATED_UID)
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .created(UPDATED_CREATED)
            .lastUpdated(UPDATED_LAST_UPDATED)
            .familyNo(UPDATED_FAMILY_NO)
            .address(UPDATED_ADDRESS);

        restFamilyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFamily.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFamily))
            )
            .andExpect(status().isOk());

        // Validate the Family in the database
        List<Family> familyList = familyRepository.findAll();
        assertThat(familyList).hasSize(databaseSizeBeforeUpdate);
        Family testFamily = familyList.get(familyList.size() - 1);
        assertThat(testFamily.getUid()).isEqualTo(UPDATED_UID);
        assertThat(testFamily.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testFamily.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testFamily.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testFamily.getLastUpdated()).isEqualTo(UPDATED_LAST_UPDATED);
        assertThat(testFamily.getFamilyNo()).isEqualTo(UPDATED_FAMILY_NO);
        assertThat(testFamily.getAddress()).isEqualTo(UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void patchNonExistingFamily() throws Exception {
        int databaseSizeBeforeUpdate = familyRepository.findAll().size();
        family.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFamilyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, family.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(family))
            )
            .andExpect(status().isBadRequest());

        // Validate the Family in the database
        List<Family> familyList = familyRepository.findAll();
        assertThat(familyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFamily() throws Exception {
        int databaseSizeBeforeUpdate = familyRepository.findAll().size();
        family.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFamilyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(family))
            )
            .andExpect(status().isBadRequest());

        // Validate the Family in the database
        List<Family> familyList = familyRepository.findAll();
        assertThat(familyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFamily() throws Exception {
        int databaseSizeBeforeUpdate = familyRepository.findAll().size();
        family.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFamilyMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(family)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Family in the database
        List<Family> familyList = familyRepository.findAll();
        assertThat(familyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFamily() throws Exception {
        // Initialize the database
        familyRepository.saveAndFlush(family);

        int databaseSizeBeforeDelete = familyRepository.findAll().size();

        // Delete the family
        restFamilyMockMvc
            .perform(delete(ENTITY_API_URL_ID, family.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Family> familyList = familyRepository.findAll();
        assertThat(familyList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
