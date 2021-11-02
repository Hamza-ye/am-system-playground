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
import org.nmcpye.activitiesmanagement.domain.Fingerprint;
import org.nmcpye.activitiesmanagement.repository.FingerprintRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link FingerprintResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FingerprintResourceIT {

    private static final String DEFAULT_UID = "AAAAAAAAAA";
    private static final String UPDATED_UID = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_LAST_UPDATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_UPDATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_FINGERPRINT_URL = "AAAAAAAAAA";
    private static final String UPDATED_FINGERPRINT_URL = "BBBBBBBBBB";

    private static final String DEFAULT_FINGERPRINT_OWNER = "AAAAAAAAAA";
    private static final String UPDATED_FINGERPRINT_OWNER = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/fingerprints";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FingerprintRepository fingerprintRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFingerprintMockMvc;

    private Fingerprint fingerprint;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Fingerprint createEntity(EntityManager em) {
        Fingerprint fingerprint = new Fingerprint()
            .uid(DEFAULT_UID)
            .description(DEFAULT_DESCRIPTION)
            .created(DEFAULT_CREATED)
            .lastUpdated(DEFAULT_LAST_UPDATED)
            .fingerprintUrl(DEFAULT_FINGERPRINT_URL)
            .fingerprintOwner(DEFAULT_FINGERPRINT_OWNER);
        return fingerprint;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Fingerprint createUpdatedEntity(EntityManager em) {
        Fingerprint fingerprint = new Fingerprint()
            .uid(UPDATED_UID)
            .description(UPDATED_DESCRIPTION)
            .created(UPDATED_CREATED)
            .lastUpdated(UPDATED_LAST_UPDATED)
            .fingerprintUrl(UPDATED_FINGERPRINT_URL)
            .fingerprintOwner(UPDATED_FINGERPRINT_OWNER);
        return fingerprint;
    }

    @BeforeEach
    public void initTest() {
        fingerprint = createEntity(em);
    }

    @Test
    @Transactional
    void createFingerprint() throws Exception {
        int databaseSizeBeforeCreate = fingerprintRepository.findAll().size();
        // Create the Fingerprint
        restFingerprintMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fingerprint)))
            .andExpect(status().isCreated());

        // Validate the Fingerprint in the database
        List<Fingerprint> fingerprintList = fingerprintRepository.findAll();
        assertThat(fingerprintList).hasSize(databaseSizeBeforeCreate + 1);
        Fingerprint testFingerprint = fingerprintList.get(fingerprintList.size() - 1);
        assertThat(testFingerprint.getUid()).isEqualTo(DEFAULT_UID);
        assertThat(testFingerprint.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testFingerprint.getCreated()).isEqualTo(DEFAULT_CREATED);
        assertThat(testFingerprint.getLastUpdated()).isEqualTo(DEFAULT_LAST_UPDATED);
        assertThat(testFingerprint.getFingerprintUrl()).isEqualTo(DEFAULT_FINGERPRINT_URL);
        assertThat(testFingerprint.getFingerprintOwner()).isEqualTo(DEFAULT_FINGERPRINT_OWNER);
    }

    @Test
    @Transactional
    void createFingerprintWithExistingId() throws Exception {
        // Create the Fingerprint with an existing ID
        fingerprint.setId(1L);

        int databaseSizeBeforeCreate = fingerprintRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFingerprintMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fingerprint)))
            .andExpect(status().isBadRequest());

        // Validate the Fingerprint in the database
        List<Fingerprint> fingerprintList = fingerprintRepository.findAll();
        assertThat(fingerprintList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkUidIsRequired() throws Exception {
        int databaseSizeBeforeTest = fingerprintRepository.findAll().size();
        // set the field null
        fingerprint.setUid(null);

        // Create the Fingerprint, which fails.

        restFingerprintMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fingerprint)))
            .andExpect(status().isBadRequest());

        List<Fingerprint> fingerprintList = fingerprintRepository.findAll();
        assertThat(fingerprintList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllFingerprints() throws Exception {
        // Initialize the database
        fingerprintRepository.saveAndFlush(fingerprint);

        // Get all the fingerprintList
        restFingerprintMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fingerprint.getId().intValue())))
            .andExpect(jsonPath("$.[*].uid").value(hasItem(DEFAULT_UID)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].created").value(hasItem(DEFAULT_CREATED.toString())))
            .andExpect(jsonPath("$.[*].lastUpdated").value(hasItem(DEFAULT_LAST_UPDATED.toString())))
            .andExpect(jsonPath("$.[*].fingerprintUrl").value(hasItem(DEFAULT_FINGERPRINT_URL)))
            .andExpect(jsonPath("$.[*].fingerprintOwner").value(hasItem(DEFAULT_FINGERPRINT_OWNER)));
    }

    @Test
    @Transactional
    void getFingerprint() throws Exception {
        // Initialize the database
        fingerprintRepository.saveAndFlush(fingerprint);

        // Get the fingerprint
        restFingerprintMockMvc
            .perform(get(ENTITY_API_URL_ID, fingerprint.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(fingerprint.getId().intValue()))
            .andExpect(jsonPath("$.uid").value(DEFAULT_UID))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.created").value(DEFAULT_CREATED.toString()))
            .andExpect(jsonPath("$.lastUpdated").value(DEFAULT_LAST_UPDATED.toString()))
            .andExpect(jsonPath("$.fingerprintUrl").value(DEFAULT_FINGERPRINT_URL))
            .andExpect(jsonPath("$.fingerprintOwner").value(DEFAULT_FINGERPRINT_OWNER));
    }

    @Test
    @Transactional
    void getNonExistingFingerprint() throws Exception {
        // Get the fingerprint
        restFingerprintMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewFingerprint() throws Exception {
        // Initialize the database
        fingerprintRepository.saveAndFlush(fingerprint);

        int databaseSizeBeforeUpdate = fingerprintRepository.findAll().size();

        // Update the fingerprint
        Fingerprint updatedFingerprint = fingerprintRepository.findById(fingerprint.getId()).get();
        // Disconnect from session so that the updates on updatedFingerprint are not directly saved in db
        em.detach(updatedFingerprint);
        updatedFingerprint
            .uid(UPDATED_UID)
            .description(UPDATED_DESCRIPTION)
            .created(UPDATED_CREATED)
            .lastUpdated(UPDATED_LAST_UPDATED)
            .fingerprintUrl(UPDATED_FINGERPRINT_URL)
            .fingerprintOwner(UPDATED_FINGERPRINT_OWNER);

        restFingerprintMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedFingerprint.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedFingerprint))
            )
            .andExpect(status().isOk());

        // Validate the Fingerprint in the database
        List<Fingerprint> fingerprintList = fingerprintRepository.findAll();
        assertThat(fingerprintList).hasSize(databaseSizeBeforeUpdate);
        Fingerprint testFingerprint = fingerprintList.get(fingerprintList.size() - 1);
        assertThat(testFingerprint.getUid()).isEqualTo(UPDATED_UID);
        assertThat(testFingerprint.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testFingerprint.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testFingerprint.getLastUpdated()).isEqualTo(UPDATED_LAST_UPDATED);
        assertThat(testFingerprint.getFingerprintUrl()).isEqualTo(UPDATED_FINGERPRINT_URL);
        assertThat(testFingerprint.getFingerprintOwner()).isEqualTo(UPDATED_FINGERPRINT_OWNER);
    }

    @Test
    @Transactional
    void putNonExistingFingerprint() throws Exception {
        int databaseSizeBeforeUpdate = fingerprintRepository.findAll().size();
        fingerprint.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFingerprintMockMvc
            .perform(
                put(ENTITY_API_URL_ID, fingerprint.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fingerprint))
            )
            .andExpect(status().isBadRequest());

        // Validate the Fingerprint in the database
        List<Fingerprint> fingerprintList = fingerprintRepository.findAll();
        assertThat(fingerprintList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFingerprint() throws Exception {
        int databaseSizeBeforeUpdate = fingerprintRepository.findAll().size();
        fingerprint.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFingerprintMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fingerprint))
            )
            .andExpect(status().isBadRequest());

        // Validate the Fingerprint in the database
        List<Fingerprint> fingerprintList = fingerprintRepository.findAll();
        assertThat(fingerprintList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFingerprint() throws Exception {
        int databaseSizeBeforeUpdate = fingerprintRepository.findAll().size();
        fingerprint.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFingerprintMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fingerprint)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Fingerprint in the database
        List<Fingerprint> fingerprintList = fingerprintRepository.findAll();
        assertThat(fingerprintList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFingerprintWithPatch() throws Exception {
        // Initialize the database
        fingerprintRepository.saveAndFlush(fingerprint);

        int databaseSizeBeforeUpdate = fingerprintRepository.findAll().size();

        // Update the fingerprint using partial update
        Fingerprint partialUpdatedFingerprint = new Fingerprint();
        partialUpdatedFingerprint.setId(fingerprint.getId());

        partialUpdatedFingerprint.description(UPDATED_DESCRIPTION).created(UPDATED_CREATED).lastUpdated(UPDATED_LAST_UPDATED);

        restFingerprintMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFingerprint.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFingerprint))
            )
            .andExpect(status().isOk());

        // Validate the Fingerprint in the database
        List<Fingerprint> fingerprintList = fingerprintRepository.findAll();
        assertThat(fingerprintList).hasSize(databaseSizeBeforeUpdate);
        Fingerprint testFingerprint = fingerprintList.get(fingerprintList.size() - 1);
        assertThat(testFingerprint.getUid()).isEqualTo(DEFAULT_UID);
        assertThat(testFingerprint.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testFingerprint.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testFingerprint.getLastUpdated()).isEqualTo(UPDATED_LAST_UPDATED);
        assertThat(testFingerprint.getFingerprintUrl()).isEqualTo(DEFAULT_FINGERPRINT_URL);
        assertThat(testFingerprint.getFingerprintOwner()).isEqualTo(DEFAULT_FINGERPRINT_OWNER);
    }

    @Test
    @Transactional
    void fullUpdateFingerprintWithPatch() throws Exception {
        // Initialize the database
        fingerprintRepository.saveAndFlush(fingerprint);

        int databaseSizeBeforeUpdate = fingerprintRepository.findAll().size();

        // Update the fingerprint using partial update
        Fingerprint partialUpdatedFingerprint = new Fingerprint();
        partialUpdatedFingerprint.setId(fingerprint.getId());

        partialUpdatedFingerprint
            .uid(UPDATED_UID)
            .description(UPDATED_DESCRIPTION)
            .created(UPDATED_CREATED)
            .lastUpdated(UPDATED_LAST_UPDATED)
            .fingerprintUrl(UPDATED_FINGERPRINT_URL)
            .fingerprintOwner(UPDATED_FINGERPRINT_OWNER);

        restFingerprintMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFingerprint.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFingerprint))
            )
            .andExpect(status().isOk());

        // Validate the Fingerprint in the database
        List<Fingerprint> fingerprintList = fingerprintRepository.findAll();
        assertThat(fingerprintList).hasSize(databaseSizeBeforeUpdate);
        Fingerprint testFingerprint = fingerprintList.get(fingerprintList.size() - 1);
        assertThat(testFingerprint.getUid()).isEqualTo(UPDATED_UID);
        assertThat(testFingerprint.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testFingerprint.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testFingerprint.getLastUpdated()).isEqualTo(UPDATED_LAST_UPDATED);
        assertThat(testFingerprint.getFingerprintUrl()).isEqualTo(UPDATED_FINGERPRINT_URL);
        assertThat(testFingerprint.getFingerprintOwner()).isEqualTo(UPDATED_FINGERPRINT_OWNER);
    }

    @Test
    @Transactional
    void patchNonExistingFingerprint() throws Exception {
        int databaseSizeBeforeUpdate = fingerprintRepository.findAll().size();
        fingerprint.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFingerprintMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, fingerprint.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(fingerprint))
            )
            .andExpect(status().isBadRequest());

        // Validate the Fingerprint in the database
        List<Fingerprint> fingerprintList = fingerprintRepository.findAll();
        assertThat(fingerprintList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFingerprint() throws Exception {
        int databaseSizeBeforeUpdate = fingerprintRepository.findAll().size();
        fingerprint.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFingerprintMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(fingerprint))
            )
            .andExpect(status().isBadRequest());

        // Validate the Fingerprint in the database
        List<Fingerprint> fingerprintList = fingerprintRepository.findAll();
        assertThat(fingerprintList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFingerprint() throws Exception {
        int databaseSizeBeforeUpdate = fingerprintRepository.findAll().size();
        fingerprint.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFingerprintMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(fingerprint))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Fingerprint in the database
        List<Fingerprint> fingerprintList = fingerprintRepository.findAll();
        assertThat(fingerprintList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFingerprint() throws Exception {
        // Initialize the database
        fingerprintRepository.saveAndFlush(fingerprint);

        int databaseSizeBeforeDelete = fingerprintRepository.findAll().size();

        // Delete the fingerprint
        restFingerprintMockMvc
            .perform(delete(ENTITY_API_URL_ID, fingerprint.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Fingerprint> fingerprintList = fingerprintRepository.findAll();
        assertThat(fingerprintList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
