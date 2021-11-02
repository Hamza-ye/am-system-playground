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
import org.nmcpye.activitiesmanagement.repository.MalariaUnitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link MalariaUnitResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MalariaUnitResourceIT {

    private static final String DEFAULT_UID = "AAAAAAAAAA";
    private static final String UPDATED_UID = "BBBBBBBBBB";

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_SHORT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_SHORT_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_LAST_UPDATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_UPDATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/malaria-units";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MalariaUnitRepository malariaUnitRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMalariaUnitMockMvc;

    private MalariaUnit malariaUnit;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MalariaUnit createEntity(EntityManager em) {
        MalariaUnit malariaUnit = new MalariaUnit()
            .uid(DEFAULT_UID)
            .code(DEFAULT_CODE)
            .name(DEFAULT_NAME)
            .shortName(DEFAULT_SHORT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .created(DEFAULT_CREATED)
            .lastUpdated(DEFAULT_LAST_UPDATED);
        return malariaUnit;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MalariaUnit createUpdatedEntity(EntityManager em) {
        MalariaUnit malariaUnit = new MalariaUnit()
            .uid(UPDATED_UID)
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .shortName(UPDATED_SHORT_NAME)
            .description(UPDATED_DESCRIPTION)
            .created(UPDATED_CREATED)
            .lastUpdated(UPDATED_LAST_UPDATED);
        return malariaUnit;
    }

    @BeforeEach
    public void initTest() {
        malariaUnit = createEntity(em);
    }

    @Test
    @Transactional
    void createMalariaUnit() throws Exception {
        int databaseSizeBeforeCreate = malariaUnitRepository.findAll().size();
        // Create the MalariaUnit
        restMalariaUnitMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(malariaUnit)))
            .andExpect(status().isCreated());

        // Validate the MalariaUnit in the database
        List<MalariaUnit> malariaUnitList = malariaUnitRepository.findAll();
        assertThat(malariaUnitList).hasSize(databaseSizeBeforeCreate + 1);
        MalariaUnit testMalariaUnit = malariaUnitList.get(malariaUnitList.size() - 1);
        assertThat(testMalariaUnit.getUid()).isEqualTo(DEFAULT_UID);
        assertThat(testMalariaUnit.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testMalariaUnit.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testMalariaUnit.getShortName()).isEqualTo(DEFAULT_SHORT_NAME);
        assertThat(testMalariaUnit.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testMalariaUnit.getCreated()).isEqualTo(DEFAULT_CREATED);
        assertThat(testMalariaUnit.getLastUpdated()).isEqualTo(DEFAULT_LAST_UPDATED);
    }

    @Test
    @Transactional
    void createMalariaUnitWithExistingId() throws Exception {
        // Create the MalariaUnit with an existing ID
        malariaUnit.setId(1L);

        int databaseSizeBeforeCreate = malariaUnitRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMalariaUnitMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(malariaUnit)))
            .andExpect(status().isBadRequest());

        // Validate the MalariaUnit in the database
        List<MalariaUnit> malariaUnitList = malariaUnitRepository.findAll();
        assertThat(malariaUnitList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkUidIsRequired() throws Exception {
        int databaseSizeBeforeTest = malariaUnitRepository.findAll().size();
        // set the field null
        malariaUnit.setUid(null);

        // Create the MalariaUnit, which fails.

        restMalariaUnitMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(malariaUnit)))
            .andExpect(status().isBadRequest());

        List<MalariaUnit> malariaUnitList = malariaUnitRepository.findAll();
        assertThat(malariaUnitList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMalariaUnits() throws Exception {
        // Initialize the database
        malariaUnitRepository.saveAndFlush(malariaUnit);

        // Get all the malariaUnitList
        restMalariaUnitMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(malariaUnit.getId().intValue())))
            .andExpect(jsonPath("$.[*].uid").value(hasItem(DEFAULT_UID)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].shortName").value(hasItem(DEFAULT_SHORT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].created").value(hasItem(DEFAULT_CREATED.toString())))
            .andExpect(jsonPath("$.[*].lastUpdated").value(hasItem(DEFAULT_LAST_UPDATED.toString())));
    }

    @Test
    @Transactional
    void getMalariaUnit() throws Exception {
        // Initialize the database
        malariaUnitRepository.saveAndFlush(malariaUnit);

        // Get the malariaUnit
        restMalariaUnitMockMvc
            .perform(get(ENTITY_API_URL_ID, malariaUnit.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(malariaUnit.getId().intValue()))
            .andExpect(jsonPath("$.uid").value(DEFAULT_UID))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.shortName").value(DEFAULT_SHORT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.created").value(DEFAULT_CREATED.toString()))
            .andExpect(jsonPath("$.lastUpdated").value(DEFAULT_LAST_UPDATED.toString()));
    }

    @Test
    @Transactional
    void getNonExistingMalariaUnit() throws Exception {
        // Get the malariaUnit
        restMalariaUnitMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewMalariaUnit() throws Exception {
        // Initialize the database
        malariaUnitRepository.saveAndFlush(malariaUnit);

        int databaseSizeBeforeUpdate = malariaUnitRepository.findAll().size();

        // Update the malariaUnit
        MalariaUnit updatedMalariaUnit = malariaUnitRepository.findById(malariaUnit.getId()).get();
        // Disconnect from session so that the updates on updatedMalariaUnit are not directly saved in db
        em.detach(updatedMalariaUnit);
        updatedMalariaUnit
            .uid(UPDATED_UID)
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .shortName(UPDATED_SHORT_NAME)
            .description(UPDATED_DESCRIPTION)
            .created(UPDATED_CREATED)
            .lastUpdated(UPDATED_LAST_UPDATED);

        restMalariaUnitMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedMalariaUnit.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedMalariaUnit))
            )
            .andExpect(status().isOk());

        // Validate the MalariaUnit in the database
        List<MalariaUnit> malariaUnitList = malariaUnitRepository.findAll();
        assertThat(malariaUnitList).hasSize(databaseSizeBeforeUpdate);
        MalariaUnit testMalariaUnit = malariaUnitList.get(malariaUnitList.size() - 1);
        assertThat(testMalariaUnit.getUid()).isEqualTo(UPDATED_UID);
        assertThat(testMalariaUnit.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testMalariaUnit.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMalariaUnit.getShortName()).isEqualTo(UPDATED_SHORT_NAME);
        assertThat(testMalariaUnit.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testMalariaUnit.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testMalariaUnit.getLastUpdated()).isEqualTo(UPDATED_LAST_UPDATED);
    }

    @Test
    @Transactional
    void putNonExistingMalariaUnit() throws Exception {
        int databaseSizeBeforeUpdate = malariaUnitRepository.findAll().size();
        malariaUnit.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMalariaUnitMockMvc
            .perform(
                put(ENTITY_API_URL_ID, malariaUnit.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(malariaUnit))
            )
            .andExpect(status().isBadRequest());

        // Validate the MalariaUnit in the database
        List<MalariaUnit> malariaUnitList = malariaUnitRepository.findAll();
        assertThat(malariaUnitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMalariaUnit() throws Exception {
        int databaseSizeBeforeUpdate = malariaUnitRepository.findAll().size();
        malariaUnit.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMalariaUnitMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(malariaUnit))
            )
            .andExpect(status().isBadRequest());

        // Validate the MalariaUnit in the database
        List<MalariaUnit> malariaUnitList = malariaUnitRepository.findAll();
        assertThat(malariaUnitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMalariaUnit() throws Exception {
        int databaseSizeBeforeUpdate = malariaUnitRepository.findAll().size();
        malariaUnit.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMalariaUnitMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(malariaUnit)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MalariaUnit in the database
        List<MalariaUnit> malariaUnitList = malariaUnitRepository.findAll();
        assertThat(malariaUnitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMalariaUnitWithPatch() throws Exception {
        // Initialize the database
        malariaUnitRepository.saveAndFlush(malariaUnit);

        int databaseSizeBeforeUpdate = malariaUnitRepository.findAll().size();

        // Update the malariaUnit using partial update
        MalariaUnit partialUpdatedMalariaUnit = new MalariaUnit();
        partialUpdatedMalariaUnit.setId(malariaUnit.getId());

        partialUpdatedMalariaUnit.uid(UPDATED_UID).code(UPDATED_CODE).shortName(UPDATED_SHORT_NAME).description(UPDATED_DESCRIPTION);

        restMalariaUnitMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMalariaUnit.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMalariaUnit))
            )
            .andExpect(status().isOk());

        // Validate the MalariaUnit in the database
        List<MalariaUnit> malariaUnitList = malariaUnitRepository.findAll();
        assertThat(malariaUnitList).hasSize(databaseSizeBeforeUpdate);
        MalariaUnit testMalariaUnit = malariaUnitList.get(malariaUnitList.size() - 1);
        assertThat(testMalariaUnit.getUid()).isEqualTo(UPDATED_UID);
        assertThat(testMalariaUnit.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testMalariaUnit.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testMalariaUnit.getShortName()).isEqualTo(UPDATED_SHORT_NAME);
        assertThat(testMalariaUnit.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testMalariaUnit.getCreated()).isEqualTo(DEFAULT_CREATED);
        assertThat(testMalariaUnit.getLastUpdated()).isEqualTo(DEFAULT_LAST_UPDATED);
    }

    @Test
    @Transactional
    void fullUpdateMalariaUnitWithPatch() throws Exception {
        // Initialize the database
        malariaUnitRepository.saveAndFlush(malariaUnit);

        int databaseSizeBeforeUpdate = malariaUnitRepository.findAll().size();

        // Update the malariaUnit using partial update
        MalariaUnit partialUpdatedMalariaUnit = new MalariaUnit();
        partialUpdatedMalariaUnit.setId(malariaUnit.getId());

        partialUpdatedMalariaUnit
            .uid(UPDATED_UID)
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .shortName(UPDATED_SHORT_NAME)
            .description(UPDATED_DESCRIPTION)
            .created(UPDATED_CREATED)
            .lastUpdated(UPDATED_LAST_UPDATED);

        restMalariaUnitMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMalariaUnit.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMalariaUnit))
            )
            .andExpect(status().isOk());

        // Validate the MalariaUnit in the database
        List<MalariaUnit> malariaUnitList = malariaUnitRepository.findAll();
        assertThat(malariaUnitList).hasSize(databaseSizeBeforeUpdate);
        MalariaUnit testMalariaUnit = malariaUnitList.get(malariaUnitList.size() - 1);
        assertThat(testMalariaUnit.getUid()).isEqualTo(UPDATED_UID);
        assertThat(testMalariaUnit.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testMalariaUnit.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMalariaUnit.getShortName()).isEqualTo(UPDATED_SHORT_NAME);
        assertThat(testMalariaUnit.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testMalariaUnit.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testMalariaUnit.getLastUpdated()).isEqualTo(UPDATED_LAST_UPDATED);
    }

    @Test
    @Transactional
    void patchNonExistingMalariaUnit() throws Exception {
        int databaseSizeBeforeUpdate = malariaUnitRepository.findAll().size();
        malariaUnit.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMalariaUnitMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, malariaUnit.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(malariaUnit))
            )
            .andExpect(status().isBadRequest());

        // Validate the MalariaUnit in the database
        List<MalariaUnit> malariaUnitList = malariaUnitRepository.findAll();
        assertThat(malariaUnitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMalariaUnit() throws Exception {
        int databaseSizeBeforeUpdate = malariaUnitRepository.findAll().size();
        malariaUnit.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMalariaUnitMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(malariaUnit))
            )
            .andExpect(status().isBadRequest());

        // Validate the MalariaUnit in the database
        List<MalariaUnit> malariaUnitList = malariaUnitRepository.findAll();
        assertThat(malariaUnitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMalariaUnit() throws Exception {
        int databaseSizeBeforeUpdate = malariaUnitRepository.findAll().size();
        malariaUnit.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMalariaUnitMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(malariaUnit))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the MalariaUnit in the database
        List<MalariaUnit> malariaUnitList = malariaUnitRepository.findAll();
        assertThat(malariaUnitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMalariaUnit() throws Exception {
        // Initialize the database
        malariaUnitRepository.saveAndFlush(malariaUnit);

        int databaseSizeBeforeDelete = malariaUnitRepository.findAll().size();

        // Delete the malariaUnit
        restMalariaUnitMockMvc
            .perform(delete(ENTITY_API_URL_ID, malariaUnit.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<MalariaUnit> malariaUnitList = malariaUnitRepository.findAll();
        assertThat(malariaUnitList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
