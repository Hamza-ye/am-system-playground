package org.nmcpye.activitiesmanagement.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.nmcpye.activitiesmanagement.IntegrationTest;
import org.nmcpye.activitiesmanagement.domain.chv.Chv;
import org.nmcpye.activitiesmanagement.domain.organisationunit.OrganisationUnit;
import org.nmcpye.activitiesmanagement.repository.ChvRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ChvResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ChvResourceIT {

    private static final String DEFAULT_UID = "AAAAAAAAAA";
    private static final String UPDATED_UID = "BBBBBBBBBB";

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Date DEFAULT_CREATED = Date.from(Instant.ofEpochMilli(0L));
    private static final Date UPDATED_CREATED = Date.from(Instant.now().truncatedTo(ChronoUnit.MILLIS));

    private static final Date DEFAULT_LAST_UPDATED = Date.from(Instant.ofEpochMilli(0L));
    private static final Date UPDATED_LAST_UPDATED = Date.from(Instant.now().truncatedTo(ChronoUnit.MILLIS));

    private static final String DEFAULT_MOBILE = "AAAAAAAAAA";
    private static final String UPDATED_MOBILE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/chvs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ChvRepository cHVRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCHVMockMvc;

    private Chv cHV;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Chv createEntity(EntityManager em) {
        Chv cHV = new Chv()
            .uid(DEFAULT_UID)
            .code(DEFAULT_CODE)
            .description(DEFAULT_DESCRIPTION)
            .created(DEFAULT_CREATED)
            .lastUpdated(DEFAULT_LAST_UPDATED)
            .mobile(DEFAULT_MOBILE);
        // Add required entity
        OrganisationUnit organisationUnit;
        if (TestUtil.findAll(em, OrganisationUnit.class).isEmpty()) {
            organisationUnit = OrganisationUnitResourceIT.createEntity(em);
            em.persist(organisationUnit);
            em.flush();
        } else {
            organisationUnit = TestUtil.findAll(em, OrganisationUnit.class).get(0);
        }
        cHV.setDistrict(organisationUnit);
        return cHV;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Chv createUpdatedEntity(EntityManager em) {
        Chv cHV = new Chv()
            .uid(UPDATED_UID)
            .code(UPDATED_CODE)
            .description(UPDATED_DESCRIPTION)
            .created(UPDATED_CREATED)
            .lastUpdated(UPDATED_LAST_UPDATED)
            .mobile(UPDATED_MOBILE);
        // Add required entity
        OrganisationUnit organisationUnit;
        if (TestUtil.findAll(em, OrganisationUnit.class).isEmpty()) {
            organisationUnit = OrganisationUnitResourceIT.createUpdatedEntity(em);
            em.persist(organisationUnit);
            em.flush();
        } else {
            organisationUnit = TestUtil.findAll(em, OrganisationUnit.class).get(0);
        }
        cHV.setDistrict(organisationUnit);
        return cHV;
    }

    @BeforeEach
    public void initTest() {
        cHV = createEntity(em);
    }

    @Test
    @Transactional
    void createCHV() throws Exception {
        int databaseSizeBeforeCreate = cHVRepository.findAll().size();
        // Create the Chv
        restCHVMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cHV)))
            .andExpect(status().isCreated());

        // Validate the Chv in the database
        List<Chv> cHVList = cHVRepository.findAll();
        assertThat(cHVList).hasSize(databaseSizeBeforeCreate + 1);
        Chv testChv = cHVList.get(cHVList.size() - 1);
        assertThat(testChv.getUid()).isEqualTo(DEFAULT_UID);
        assertThat(testChv.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testChv.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testChv.getCreated()).isEqualTo(DEFAULT_CREATED);
        assertThat(testChv.getLastUpdated()).isEqualTo(DEFAULT_LAST_UPDATED);
        assertThat(testChv.getMobile()).isEqualTo(DEFAULT_MOBILE);
    }

    @Test
    @Transactional
    void createCHVWithExistingId() throws Exception {
        // Create the Chv with an existing ID
        cHV.setId(1L);

        int databaseSizeBeforeCreate = cHVRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCHVMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cHV)))
            .andExpect(status().isBadRequest());

        // Validate the Chv in the database
        List<Chv> cHVList = cHVRepository.findAll();
        assertThat(cHVList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkUidIsRequired() throws Exception {
        int databaseSizeBeforeTest = cHVRepository.findAll().size();
        // set the field null
        cHV.setUid(null);

        // Create the Chv, which fails.

        restCHVMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cHV)))
            .andExpect(status().isBadRequest());

        List<Chv> cHVList = cHVRepository.findAll();
        assertThat(cHVList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCHVS() throws Exception {
        // Initialize the database
        cHVRepository.saveAndFlush(cHV);

        // Get all the cHVList
        restCHVMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cHV.getId().intValue())))
            .andExpect(jsonPath("$.[*].uid").value(hasItem(DEFAULT_UID)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].created").value(hasItem(DEFAULT_CREATED.toString())))
            .andExpect(jsonPath("$.[*].lastUpdated").value(hasItem(DEFAULT_LAST_UPDATED.toString())))
            .andExpect(jsonPath("$.[*].mobile").value(hasItem(DEFAULT_MOBILE)));
    }

    @Test
    @Transactional
    void getCHV() throws Exception {
        // Initialize the database
        cHVRepository.saveAndFlush(cHV);

        // Get the cHV
        restCHVMockMvc
            .perform(get(ENTITY_API_URL_ID, cHV.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cHV.getId().intValue()))
            .andExpect(jsonPath("$.uid").value(DEFAULT_UID))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.created").value(DEFAULT_CREATED.toString()))
            .andExpect(jsonPath("$.lastUpdated").value(DEFAULT_LAST_UPDATED.toString()))
            .andExpect(jsonPath("$.mobile").value(DEFAULT_MOBILE));
    }

    @Test
    @Transactional
    void getNonExistingCHV() throws Exception {
        // Get the cHV
        restCHVMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCHV() throws Exception {
        // Initialize the database
        cHVRepository.saveAndFlush(cHV);

        int databaseSizeBeforeUpdate = cHVRepository.findAll().size();

        // Update the cHV
        Chv updatedChv = cHVRepository.findById(cHV.getId()).get();
        // Disconnect from session so that the updates on updatedChv are not directly saved in db
        em.detach(updatedChv);
        updatedChv
            .uid(UPDATED_UID)
            .code(UPDATED_CODE)
            .description(UPDATED_DESCRIPTION)
            .created(UPDATED_CREATED)
            .lastUpdated(UPDATED_LAST_UPDATED)
            .mobile(UPDATED_MOBILE);

        restCHVMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedChv.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedChv))
            )
            .andExpect(status().isOk());

        // Validate the Chv in the database
        List<Chv> cHVList = cHVRepository.findAll();
        assertThat(cHVList).hasSize(databaseSizeBeforeUpdate);
        Chv testChv = cHVList.get(cHVList.size() - 1);
        assertThat(testChv.getUid()).isEqualTo(UPDATED_UID);
        assertThat(testChv.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testChv.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testChv.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testChv.getLastUpdated()).isEqualTo(UPDATED_LAST_UPDATED);
        assertThat(testChv.getMobile()).isEqualTo(UPDATED_MOBILE);
    }

    @Test
    @Transactional
    void putNonExistingCHV() throws Exception {
        int databaseSizeBeforeUpdate = cHVRepository.findAll().size();
        cHV.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCHVMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cHV.getId()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cHV))
            )
            .andExpect(status().isBadRequest());

        // Validate the Chv in the database
        List<Chv> cHVList = cHVRepository.findAll();
        assertThat(cHVList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCHV() throws Exception {
        int databaseSizeBeforeUpdate = cHVRepository.findAll().size();
        cHV.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCHVMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cHV))
            )
            .andExpect(status().isBadRequest());

        // Validate the Chv in the database
        List<Chv> cHVList = cHVRepository.findAll();
        assertThat(cHVList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCHV() throws Exception {
        int databaseSizeBeforeUpdate = cHVRepository.findAll().size();
        cHV.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCHVMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cHV)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Chv in the database
        List<Chv> cHVList = cHVRepository.findAll();
        assertThat(cHVList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCHVWithPatch() throws Exception {
        // Initialize the database
        cHVRepository.saveAndFlush(cHV);

        int databaseSizeBeforeUpdate = cHVRepository.findAll().size();

        // Update the cHV using partial update
        Chv partialUpdatedChv = new Chv();
        partialUpdatedChv.setId(cHV.getId());

        restCHVMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedChv.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedChv))
            )
            .andExpect(status().isOk());

        // Validate the Chv in the database
        List<Chv> cHVList = cHVRepository.findAll();
        assertThat(cHVList).hasSize(databaseSizeBeforeUpdate);
        Chv testChv = cHVList.get(cHVList.size() - 1);
        assertThat(testChv.getUid()).isEqualTo(DEFAULT_UID);
        assertThat(testChv.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testChv.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testChv.getCreated()).isEqualTo(DEFAULT_CREATED);
        assertThat(testChv.getLastUpdated()).isEqualTo(DEFAULT_LAST_UPDATED);
        assertThat(testChv.getMobile()).isEqualTo(DEFAULT_MOBILE);
    }

    @Test
    @Transactional
    void fullUpdateCHVWithPatch() throws Exception {
        // Initialize the database
        cHVRepository.saveAndFlush(cHV);

        int databaseSizeBeforeUpdate = cHVRepository.findAll().size();

        // Update the cHV using partial update
        Chv partialUpdatedChv = new Chv();
        partialUpdatedChv.setId(cHV.getId());

        partialUpdatedChv
            .uid(UPDATED_UID)
            .code(UPDATED_CODE)
            .description(UPDATED_DESCRIPTION)
            .created(UPDATED_CREATED)
            .lastUpdated(UPDATED_LAST_UPDATED)
            .mobile(UPDATED_MOBILE);

        restCHVMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedChv.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedChv))
            )
            .andExpect(status().isOk());

        // Validate the Chv in the database
        List<Chv> cHVList = cHVRepository.findAll();
        assertThat(cHVList).hasSize(databaseSizeBeforeUpdate);
        Chv testChv = cHVList.get(cHVList.size() - 1);
        assertThat(testChv.getUid()).isEqualTo(UPDATED_UID);
        assertThat(testChv.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testChv.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testChv.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testChv.getLastUpdated()).isEqualTo(UPDATED_LAST_UPDATED);
        assertThat(testChv.getMobile()).isEqualTo(UPDATED_MOBILE);
    }

    @Test
    @Transactional
    void patchNonExistingCHV() throws Exception {
        int databaseSizeBeforeUpdate = cHVRepository.findAll().size();
        cHV.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCHVMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, cHV.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cHV))
            )
            .andExpect(status().isBadRequest());

        // Validate the Chv in the database
        List<Chv> cHVList = cHVRepository.findAll();
        assertThat(cHVList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCHV() throws Exception {
        int databaseSizeBeforeUpdate = cHVRepository.findAll().size();
        cHV.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCHVMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cHV))
            )
            .andExpect(status().isBadRequest());

        // Validate the Chv in the database
        List<Chv> cHVList = cHVRepository.findAll();
        assertThat(cHVList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCHV() throws Exception {
        int databaseSizeBeforeUpdate = cHVRepository.findAll().size();
        cHV.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCHVMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(cHV)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Chv in the database
        List<Chv> cHVList = cHVRepository.findAll();
        assertThat(cHVList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCHV() throws Exception {
        // Initialize the database
        cHVRepository.saveAndFlush(cHV);

        int databaseSizeBeforeDelete = cHVRepository.findAll().size();

        // Delete the cHV
        restCHVMockMvc.perform(delete(ENTITY_API_URL_ID, cHV.getId()).accept(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Chv> cHVList = cHVRepository.findAll();
        assertThat(cHVList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
