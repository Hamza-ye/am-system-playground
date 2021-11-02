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
import org.nmcpye.activitiesmanagement.domain.CasesReportClass;
import org.nmcpye.activitiesmanagement.repository.CasesReportClassRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link CasesReportClassResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CasesReportClassResourceIT {

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

    private static final String ENTITY_API_URL = "/api/cases-report-classes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CasesReportClassRepository casesReportClassRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCasesReportClassMockMvc;

    private CasesReportClass casesReportClass;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CasesReportClass createEntity(EntityManager em) {
        CasesReportClass casesReportClass = new CasesReportClass()
            .uid(DEFAULT_UID)
            .code(DEFAULT_CODE)
            .name(DEFAULT_NAME)
            .shortName(DEFAULT_SHORT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .created(DEFAULT_CREATED)
            .lastUpdated(DEFAULT_LAST_UPDATED);
        return casesReportClass;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CasesReportClass createUpdatedEntity(EntityManager em) {
        CasesReportClass casesReportClass = new CasesReportClass()
            .uid(UPDATED_UID)
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .shortName(UPDATED_SHORT_NAME)
            .description(UPDATED_DESCRIPTION)
            .created(UPDATED_CREATED)
            .lastUpdated(UPDATED_LAST_UPDATED);
        return casesReportClass;
    }

    @BeforeEach
    public void initTest() {
        casesReportClass = createEntity(em);
    }

    @Test
    @Transactional
    void createCasesReportClass() throws Exception {
        int databaseSizeBeforeCreate = casesReportClassRepository.findAll().size();
        // Create the CasesReportClass
        restCasesReportClassMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(casesReportClass))
            )
            .andExpect(status().isCreated());

        // Validate the CasesReportClass in the database
        List<CasesReportClass> casesReportClassList = casesReportClassRepository.findAll();
        assertThat(casesReportClassList).hasSize(databaseSizeBeforeCreate + 1);
        CasesReportClass testCasesReportClass = casesReportClassList.get(casesReportClassList.size() - 1);
        assertThat(testCasesReportClass.getUid()).isEqualTo(DEFAULT_UID);
        assertThat(testCasesReportClass.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testCasesReportClass.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCasesReportClass.getShortName()).isEqualTo(DEFAULT_SHORT_NAME);
        assertThat(testCasesReportClass.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testCasesReportClass.getCreated()).isEqualTo(DEFAULT_CREATED);
        assertThat(testCasesReportClass.getLastUpdated()).isEqualTo(DEFAULT_LAST_UPDATED);
    }

    @Test
    @Transactional
    void createCasesReportClassWithExistingId() throws Exception {
        // Create the CasesReportClass with an existing ID
        casesReportClass.setId(1L);

        int databaseSizeBeforeCreate = casesReportClassRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCasesReportClassMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(casesReportClass))
            )
            .andExpect(status().isBadRequest());

        // Validate the CasesReportClass in the database
        List<CasesReportClass> casesReportClassList = casesReportClassRepository.findAll();
        assertThat(casesReportClassList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkUidIsRequired() throws Exception {
        int databaseSizeBeforeTest = casesReportClassRepository.findAll().size();
        // set the field null
        casesReportClass.setUid(null);

        // Create the CasesReportClass, which fails.

        restCasesReportClassMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(casesReportClass))
            )
            .andExpect(status().isBadRequest());

        List<CasesReportClass> casesReportClassList = casesReportClassRepository.findAll();
        assertThat(casesReportClassList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCasesReportClasses() throws Exception {
        // Initialize the database
        casesReportClassRepository.saveAndFlush(casesReportClass);

        // Get all the casesReportClassList
        restCasesReportClassMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(casesReportClass.getId().intValue())))
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
    void getCasesReportClass() throws Exception {
        // Initialize the database
        casesReportClassRepository.saveAndFlush(casesReportClass);

        // Get the casesReportClass
        restCasesReportClassMockMvc
            .perform(get(ENTITY_API_URL_ID, casesReportClass.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(casesReportClass.getId().intValue()))
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
    void getNonExistingCasesReportClass() throws Exception {
        // Get the casesReportClass
        restCasesReportClassMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCasesReportClass() throws Exception {
        // Initialize the database
        casesReportClassRepository.saveAndFlush(casesReportClass);

        int databaseSizeBeforeUpdate = casesReportClassRepository.findAll().size();

        // Update the casesReportClass
        CasesReportClass updatedCasesReportClass = casesReportClassRepository.findById(casesReportClass.getId()).get();
        // Disconnect from session so that the updates on updatedCasesReportClass are not directly saved in db
        em.detach(updatedCasesReportClass);
        updatedCasesReportClass
            .uid(UPDATED_UID)
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .shortName(UPDATED_SHORT_NAME)
            .description(UPDATED_DESCRIPTION)
            .created(UPDATED_CREATED)
            .lastUpdated(UPDATED_LAST_UPDATED);

        restCasesReportClassMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCasesReportClass.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCasesReportClass))
            )
            .andExpect(status().isOk());

        // Validate the CasesReportClass in the database
        List<CasesReportClass> casesReportClassList = casesReportClassRepository.findAll();
        assertThat(casesReportClassList).hasSize(databaseSizeBeforeUpdate);
        CasesReportClass testCasesReportClass = casesReportClassList.get(casesReportClassList.size() - 1);
        assertThat(testCasesReportClass.getUid()).isEqualTo(UPDATED_UID);
        assertThat(testCasesReportClass.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testCasesReportClass.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCasesReportClass.getShortName()).isEqualTo(UPDATED_SHORT_NAME);
        assertThat(testCasesReportClass.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testCasesReportClass.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testCasesReportClass.getLastUpdated()).isEqualTo(UPDATED_LAST_UPDATED);
    }

    @Test
    @Transactional
    void putNonExistingCasesReportClass() throws Exception {
        int databaseSizeBeforeUpdate = casesReportClassRepository.findAll().size();
        casesReportClass.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCasesReportClassMockMvc
            .perform(
                put(ENTITY_API_URL_ID, casesReportClass.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(casesReportClass))
            )
            .andExpect(status().isBadRequest());

        // Validate the CasesReportClass in the database
        List<CasesReportClass> casesReportClassList = casesReportClassRepository.findAll();
        assertThat(casesReportClassList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCasesReportClass() throws Exception {
        int databaseSizeBeforeUpdate = casesReportClassRepository.findAll().size();
        casesReportClass.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCasesReportClassMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(casesReportClass))
            )
            .andExpect(status().isBadRequest());

        // Validate the CasesReportClass in the database
        List<CasesReportClass> casesReportClassList = casesReportClassRepository.findAll();
        assertThat(casesReportClassList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCasesReportClass() throws Exception {
        int databaseSizeBeforeUpdate = casesReportClassRepository.findAll().size();
        casesReportClass.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCasesReportClassMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(casesReportClass))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CasesReportClass in the database
        List<CasesReportClass> casesReportClassList = casesReportClassRepository.findAll();
        assertThat(casesReportClassList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCasesReportClassWithPatch() throws Exception {
        // Initialize the database
        casesReportClassRepository.saveAndFlush(casesReportClass);

        int databaseSizeBeforeUpdate = casesReportClassRepository.findAll().size();

        // Update the casesReportClass using partial update
        CasesReportClass partialUpdatedCasesReportClass = new CasesReportClass();
        partialUpdatedCasesReportClass.setId(casesReportClass.getId());

        partialUpdatedCasesReportClass
            .uid(UPDATED_UID)
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .created(UPDATED_CREATED)
            .lastUpdated(UPDATED_LAST_UPDATED);

        restCasesReportClassMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCasesReportClass.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCasesReportClass))
            )
            .andExpect(status().isOk());

        // Validate the CasesReportClass in the database
        List<CasesReportClass> casesReportClassList = casesReportClassRepository.findAll();
        assertThat(casesReportClassList).hasSize(databaseSizeBeforeUpdate);
        CasesReportClass testCasesReportClass = casesReportClassList.get(casesReportClassList.size() - 1);
        assertThat(testCasesReportClass.getUid()).isEqualTo(UPDATED_UID);
        assertThat(testCasesReportClass.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testCasesReportClass.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCasesReportClass.getShortName()).isEqualTo(DEFAULT_SHORT_NAME);
        assertThat(testCasesReportClass.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testCasesReportClass.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testCasesReportClass.getLastUpdated()).isEqualTo(UPDATED_LAST_UPDATED);
    }

    @Test
    @Transactional
    void fullUpdateCasesReportClassWithPatch() throws Exception {
        // Initialize the database
        casesReportClassRepository.saveAndFlush(casesReportClass);

        int databaseSizeBeforeUpdate = casesReportClassRepository.findAll().size();

        // Update the casesReportClass using partial update
        CasesReportClass partialUpdatedCasesReportClass = new CasesReportClass();
        partialUpdatedCasesReportClass.setId(casesReportClass.getId());

        partialUpdatedCasesReportClass
            .uid(UPDATED_UID)
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .shortName(UPDATED_SHORT_NAME)
            .description(UPDATED_DESCRIPTION)
            .created(UPDATED_CREATED)
            .lastUpdated(UPDATED_LAST_UPDATED);

        restCasesReportClassMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCasesReportClass.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCasesReportClass))
            )
            .andExpect(status().isOk());

        // Validate the CasesReportClass in the database
        List<CasesReportClass> casesReportClassList = casesReportClassRepository.findAll();
        assertThat(casesReportClassList).hasSize(databaseSizeBeforeUpdate);
        CasesReportClass testCasesReportClass = casesReportClassList.get(casesReportClassList.size() - 1);
        assertThat(testCasesReportClass.getUid()).isEqualTo(UPDATED_UID);
        assertThat(testCasesReportClass.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testCasesReportClass.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCasesReportClass.getShortName()).isEqualTo(UPDATED_SHORT_NAME);
        assertThat(testCasesReportClass.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testCasesReportClass.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testCasesReportClass.getLastUpdated()).isEqualTo(UPDATED_LAST_UPDATED);
    }

    @Test
    @Transactional
    void patchNonExistingCasesReportClass() throws Exception {
        int databaseSizeBeforeUpdate = casesReportClassRepository.findAll().size();
        casesReportClass.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCasesReportClassMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, casesReportClass.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(casesReportClass))
            )
            .andExpect(status().isBadRequest());

        // Validate the CasesReportClass in the database
        List<CasesReportClass> casesReportClassList = casesReportClassRepository.findAll();
        assertThat(casesReportClassList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCasesReportClass() throws Exception {
        int databaseSizeBeforeUpdate = casesReportClassRepository.findAll().size();
        casesReportClass.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCasesReportClassMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(casesReportClass))
            )
            .andExpect(status().isBadRequest());

        // Validate the CasesReportClass in the database
        List<CasesReportClass> casesReportClassList = casesReportClassRepository.findAll();
        assertThat(casesReportClassList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCasesReportClass() throws Exception {
        int databaseSizeBeforeUpdate = casesReportClassRepository.findAll().size();
        casesReportClass.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCasesReportClassMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(casesReportClass))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CasesReportClass in the database
        List<CasesReportClass> casesReportClassList = casesReportClassRepository.findAll();
        assertThat(casesReportClassList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCasesReportClass() throws Exception {
        // Initialize the database
        casesReportClassRepository.saveAndFlush(casesReportClass);

        int databaseSizeBeforeDelete = casesReportClassRepository.findAll().size();

        // Delete the casesReportClass
        restCasesReportClassMockMvc
            .perform(delete(ENTITY_API_URL_ID, casesReportClass.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CasesReportClass> casesReportClassList = casesReportClassRepository.findAll();
        assertThat(casesReportClassList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
