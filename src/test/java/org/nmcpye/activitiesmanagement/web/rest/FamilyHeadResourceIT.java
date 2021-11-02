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
import org.nmcpye.activitiesmanagement.domain.FamilyHead;
import org.nmcpye.activitiesmanagement.repository.FamilyHeadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link FamilyHeadResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FamilyHeadResourceIT {

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

    private static final String DEFAULT_MOBILE = "AAAAAAAAAA";
    private static final String UPDATED_MOBILE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/family-heads";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FamilyHeadRepository familyHeadRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFamilyHeadMockMvc;

    private FamilyHead familyHead;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FamilyHead createEntity(EntityManager em) {
        FamilyHead familyHead = new FamilyHead()
            .uid(DEFAULT_UID)
            .code(DEFAULT_CODE)
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .created(DEFAULT_CREATED)
            .lastUpdated(DEFAULT_LAST_UPDATED)
            .mobile(DEFAULT_MOBILE);
        // Add required entity
        Family family;
        if (TestUtil.findAll(em, Family.class).isEmpty()) {
            family = FamilyResourceIT.createEntity(em);
            em.persist(family);
            em.flush();
        } else {
            family = TestUtil.findAll(em, Family.class).get(0);
        }
        familyHead.setFamily(family);
        return familyHead;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FamilyHead createUpdatedEntity(EntityManager em) {
        FamilyHead familyHead = new FamilyHead()
            .uid(UPDATED_UID)
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .created(UPDATED_CREATED)
            .lastUpdated(UPDATED_LAST_UPDATED)
            .mobile(UPDATED_MOBILE);
        // Add required entity
        Family family;
        if (TestUtil.findAll(em, Family.class).isEmpty()) {
            family = FamilyResourceIT.createUpdatedEntity(em);
            em.persist(family);
            em.flush();
        } else {
            family = TestUtil.findAll(em, Family.class).get(0);
        }
        familyHead.setFamily(family);
        return familyHead;
    }

    @BeforeEach
    public void initTest() {
        familyHead = createEntity(em);
    }

    @Test
    @Transactional
    void createFamilyHead() throws Exception {
        int databaseSizeBeforeCreate = familyHeadRepository.findAll().size();
        // Create the FamilyHead
        restFamilyHeadMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(familyHead)))
            .andExpect(status().isCreated());

        // Validate the FamilyHead in the database
        List<FamilyHead> familyHeadList = familyHeadRepository.findAll();
        assertThat(familyHeadList).hasSize(databaseSizeBeforeCreate + 1);
        FamilyHead testFamilyHead = familyHeadList.get(familyHeadList.size() - 1);
        assertThat(testFamilyHead.getUid()).isEqualTo(DEFAULT_UID);
        assertThat(testFamilyHead.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testFamilyHead.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testFamilyHead.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testFamilyHead.getCreated()).isEqualTo(DEFAULT_CREATED);
        assertThat(testFamilyHead.getLastUpdated()).isEqualTo(DEFAULT_LAST_UPDATED);
        assertThat(testFamilyHead.getMobile()).isEqualTo(DEFAULT_MOBILE);
    }

    @Test
    @Transactional
    void createFamilyHeadWithExistingId() throws Exception {
        // Create the FamilyHead with an existing ID
        familyHead.setId(1L);

        int databaseSizeBeforeCreate = familyHeadRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFamilyHeadMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(familyHead)))
            .andExpect(status().isBadRequest());

        // Validate the FamilyHead in the database
        List<FamilyHead> familyHeadList = familyHeadRepository.findAll();
        assertThat(familyHeadList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkUidIsRequired() throws Exception {
        int databaseSizeBeforeTest = familyHeadRepository.findAll().size();
        // set the field null
        familyHead.setUid(null);

        // Create the FamilyHead, which fails.

        restFamilyHeadMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(familyHead)))
            .andExpect(status().isBadRequest());

        List<FamilyHead> familyHeadList = familyHeadRepository.findAll();
        assertThat(familyHeadList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllFamilyHeads() throws Exception {
        // Initialize the database
        familyHeadRepository.saveAndFlush(familyHead);

        // Get all the familyHeadList
        restFamilyHeadMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(familyHead.getId().intValue())))
            .andExpect(jsonPath("$.[*].uid").value(hasItem(DEFAULT_UID)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].created").value(hasItem(DEFAULT_CREATED.toString())))
            .andExpect(jsonPath("$.[*].lastUpdated").value(hasItem(DEFAULT_LAST_UPDATED.toString())))
            .andExpect(jsonPath("$.[*].mobile").value(hasItem(DEFAULT_MOBILE)));
    }

    @Test
    @Transactional
    void getFamilyHead() throws Exception {
        // Initialize the database
        familyHeadRepository.saveAndFlush(familyHead);

        // Get the familyHead
        restFamilyHeadMockMvc
            .perform(get(ENTITY_API_URL_ID, familyHead.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(familyHead.getId().intValue()))
            .andExpect(jsonPath("$.uid").value(DEFAULT_UID))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.created").value(DEFAULT_CREATED.toString()))
            .andExpect(jsonPath("$.lastUpdated").value(DEFAULT_LAST_UPDATED.toString()))
            .andExpect(jsonPath("$.mobile").value(DEFAULT_MOBILE));
    }

    @Test
    @Transactional
    void getNonExistingFamilyHead() throws Exception {
        // Get the familyHead
        restFamilyHeadMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewFamilyHead() throws Exception {
        // Initialize the database
        familyHeadRepository.saveAndFlush(familyHead);

        int databaseSizeBeforeUpdate = familyHeadRepository.findAll().size();

        // Update the familyHead
        FamilyHead updatedFamilyHead = familyHeadRepository.findById(familyHead.getId()).get();
        // Disconnect from session so that the updates on updatedFamilyHead are not directly saved in db
        em.detach(updatedFamilyHead);
        updatedFamilyHead
            .uid(UPDATED_UID)
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .created(UPDATED_CREATED)
            .lastUpdated(UPDATED_LAST_UPDATED)
            .mobile(UPDATED_MOBILE);

        restFamilyHeadMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedFamilyHead.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedFamilyHead))
            )
            .andExpect(status().isOk());

        // Validate the FamilyHead in the database
        List<FamilyHead> familyHeadList = familyHeadRepository.findAll();
        assertThat(familyHeadList).hasSize(databaseSizeBeforeUpdate);
        FamilyHead testFamilyHead = familyHeadList.get(familyHeadList.size() - 1);
        assertThat(testFamilyHead.getUid()).isEqualTo(UPDATED_UID);
        assertThat(testFamilyHead.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testFamilyHead.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testFamilyHead.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testFamilyHead.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testFamilyHead.getLastUpdated()).isEqualTo(UPDATED_LAST_UPDATED);
        assertThat(testFamilyHead.getMobile()).isEqualTo(UPDATED_MOBILE);
    }

    @Test
    @Transactional
    void putNonExistingFamilyHead() throws Exception {
        int databaseSizeBeforeUpdate = familyHeadRepository.findAll().size();
        familyHead.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFamilyHeadMockMvc
            .perform(
                put(ENTITY_API_URL_ID, familyHead.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(familyHead))
            )
            .andExpect(status().isBadRequest());

        // Validate the FamilyHead in the database
        List<FamilyHead> familyHeadList = familyHeadRepository.findAll();
        assertThat(familyHeadList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFamilyHead() throws Exception {
        int databaseSizeBeforeUpdate = familyHeadRepository.findAll().size();
        familyHead.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFamilyHeadMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(familyHead))
            )
            .andExpect(status().isBadRequest());

        // Validate the FamilyHead in the database
        List<FamilyHead> familyHeadList = familyHeadRepository.findAll();
        assertThat(familyHeadList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFamilyHead() throws Exception {
        int databaseSizeBeforeUpdate = familyHeadRepository.findAll().size();
        familyHead.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFamilyHeadMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(familyHead)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the FamilyHead in the database
        List<FamilyHead> familyHeadList = familyHeadRepository.findAll();
        assertThat(familyHeadList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFamilyHeadWithPatch() throws Exception {
        // Initialize the database
        familyHeadRepository.saveAndFlush(familyHead);

        int databaseSizeBeforeUpdate = familyHeadRepository.findAll().size();

        // Update the familyHead using partial update
        FamilyHead partialUpdatedFamilyHead = new FamilyHead();
        partialUpdatedFamilyHead.setId(familyHead.getId());

        partialUpdatedFamilyHead
            .code(UPDATED_CODE)
            .description(UPDATED_DESCRIPTION)
            .lastUpdated(UPDATED_LAST_UPDATED)
            .mobile(UPDATED_MOBILE);

        restFamilyHeadMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFamilyHead.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFamilyHead))
            )
            .andExpect(status().isOk());

        // Validate the FamilyHead in the database
        List<FamilyHead> familyHeadList = familyHeadRepository.findAll();
        assertThat(familyHeadList).hasSize(databaseSizeBeforeUpdate);
        FamilyHead testFamilyHead = familyHeadList.get(familyHeadList.size() - 1);
        assertThat(testFamilyHead.getUid()).isEqualTo(DEFAULT_UID);
        assertThat(testFamilyHead.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testFamilyHead.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testFamilyHead.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testFamilyHead.getCreated()).isEqualTo(DEFAULT_CREATED);
        assertThat(testFamilyHead.getLastUpdated()).isEqualTo(UPDATED_LAST_UPDATED);
        assertThat(testFamilyHead.getMobile()).isEqualTo(UPDATED_MOBILE);
    }

    @Test
    @Transactional
    void fullUpdateFamilyHeadWithPatch() throws Exception {
        // Initialize the database
        familyHeadRepository.saveAndFlush(familyHead);

        int databaseSizeBeforeUpdate = familyHeadRepository.findAll().size();

        // Update the familyHead using partial update
        FamilyHead partialUpdatedFamilyHead = new FamilyHead();
        partialUpdatedFamilyHead.setId(familyHead.getId());

        partialUpdatedFamilyHead
            .uid(UPDATED_UID)
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .created(UPDATED_CREATED)
            .lastUpdated(UPDATED_LAST_UPDATED)
            .mobile(UPDATED_MOBILE);

        restFamilyHeadMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFamilyHead.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFamilyHead))
            )
            .andExpect(status().isOk());

        // Validate the FamilyHead in the database
        List<FamilyHead> familyHeadList = familyHeadRepository.findAll();
        assertThat(familyHeadList).hasSize(databaseSizeBeforeUpdate);
        FamilyHead testFamilyHead = familyHeadList.get(familyHeadList.size() - 1);
        assertThat(testFamilyHead.getUid()).isEqualTo(UPDATED_UID);
        assertThat(testFamilyHead.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testFamilyHead.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testFamilyHead.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testFamilyHead.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testFamilyHead.getLastUpdated()).isEqualTo(UPDATED_LAST_UPDATED);
        assertThat(testFamilyHead.getMobile()).isEqualTo(UPDATED_MOBILE);
    }

    @Test
    @Transactional
    void patchNonExistingFamilyHead() throws Exception {
        int databaseSizeBeforeUpdate = familyHeadRepository.findAll().size();
        familyHead.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFamilyHeadMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, familyHead.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(familyHead))
            )
            .andExpect(status().isBadRequest());

        // Validate the FamilyHead in the database
        List<FamilyHead> familyHeadList = familyHeadRepository.findAll();
        assertThat(familyHeadList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFamilyHead() throws Exception {
        int databaseSizeBeforeUpdate = familyHeadRepository.findAll().size();
        familyHead.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFamilyHeadMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(familyHead))
            )
            .andExpect(status().isBadRequest());

        // Validate the FamilyHead in the database
        List<FamilyHead> familyHeadList = familyHeadRepository.findAll();
        assertThat(familyHeadList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFamilyHead() throws Exception {
        int databaseSizeBeforeUpdate = familyHeadRepository.findAll().size();
        familyHead.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFamilyHeadMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(familyHead))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FamilyHead in the database
        List<FamilyHead> familyHeadList = familyHeadRepository.findAll();
        assertThat(familyHeadList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFamilyHead() throws Exception {
        // Initialize the database
        familyHeadRepository.saveAndFlush(familyHead);

        int databaseSizeBeforeDelete = familyHeadRepository.findAll().size();

        // Delete the familyHead
        restFamilyHeadMockMvc
            .perform(delete(ENTITY_API_URL_ID, familyHead.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<FamilyHead> familyHeadList = familyHeadRepository.findAll();
        assertThat(familyHeadList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
