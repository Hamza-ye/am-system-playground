package org.nmcpye.activitiesmanagement.web.rest;

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

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
    private ChvRepository chvRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restChvMockMvc;

    private Chv chv;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Chv createEntity(EntityManager em) {
        Chv chv = new Chv()
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
        chv.setDistrict(organisationUnit);
        return chv;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Chv createUpdatedEntity(EntityManager em) {
        Chv chv = new Chv()
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
        chv.setDistrict(organisationUnit);
        return chv;
    }

    @BeforeEach
    public void initTest() {
        chv = createEntity(em);
    }

    @Test
    @Transactional
    void createChv() throws Exception {
        int databaseSizeBeforeCreate = chvRepository.findAll().size();
        // Create the Chv
        restChvMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(chv)))
            .andExpect(status().isCreated());

        // Validate the Chv in the database
        List<Chv> chvList = chvRepository.findAll();
        assertThat(chvList).hasSize(databaseSizeBeforeCreate + 1);
        Chv testChv = chvList.get(chvList.size() - 1);
        assertThat(testChv.getUid()).isEqualTo(DEFAULT_UID);
        assertThat(testChv.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testChv.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testChv.getCreated()).isEqualTo(DEFAULT_CREATED);
        assertThat(testChv.getLastUpdated()).isEqualTo(DEFAULT_LAST_UPDATED);
        assertThat(testChv.getMobile()).isEqualTo(DEFAULT_MOBILE);
    }

    @Test
    @Transactional
    void createChvWithExistingId() throws Exception {
        // Create the Chv with an existing ID
        chv.setId(1L);

        int databaseSizeBeforeCreate = chvRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restChvMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(chv)))
            .andExpect(status().isBadRequest());

        // Validate the Chv in the database
        List<Chv> chvList = chvRepository.findAll();
        assertThat(chvList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkUidIsRequired() throws Exception {
        int databaseSizeBeforeTest = chvRepository.findAll().size();
        // set the field null
        chv.setUid(null);

        // Create the Chv, which fails.

        restChvMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(chv)))
            .andExpect(status().isBadRequest());

        List<Chv> chvList = chvRepository.findAll();
        assertThat(chvList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllChvs() throws Exception {
        // Initialize the database
        chvRepository.saveAndFlush(chv);

        // Get all the chvList
        restChvMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(chv.getId().intValue())))
            .andExpect(jsonPath("$.[*].uid").value(hasItem(DEFAULT_UID)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].created").value(hasItem(DEFAULT_CREATED.toString())))
            .andExpect(jsonPath("$.[*].lastUpdated").value(hasItem(DEFAULT_LAST_UPDATED.toString())))
            .andExpect(jsonPath("$.[*].mobile").value(hasItem(DEFAULT_MOBILE)));
    }

    @Test
    @Transactional
    void getChv() throws Exception {
        // Initialize the database
        chvRepository.saveAndFlush(chv);

        // Get the chv
        restChvMockMvc
            .perform(get(ENTITY_API_URL_ID, chv.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(chv.getId().intValue()))
            .andExpect(jsonPath("$.uid").value(DEFAULT_UID))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.created").value(DEFAULT_CREATED.toString()))
            .andExpect(jsonPath("$.lastUpdated").value(DEFAULT_LAST_UPDATED.toString()))
            .andExpect(jsonPath("$.mobile").value(DEFAULT_MOBILE));
    }

    @Test
    @Transactional
    void getNonExistingChv() throws Exception {
        // Get the chv
        restChvMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewChv() throws Exception {
        // Initialize the database
        chvRepository.saveAndFlush(chv);

        int databaseSizeBeforeUpdate = chvRepository.findAll().size();

        // Update the chv
        Chv updatedChv = chvRepository.findById(chv.getId()).get();
        // Disconnect from session so that the updates on updatedChv are not directly saved in db
        em.detach(updatedChv);
        updatedChv
            .uid(UPDATED_UID)
            .code(UPDATED_CODE)
            .description(UPDATED_DESCRIPTION)
            .created(UPDATED_CREATED)
            .lastUpdated(UPDATED_LAST_UPDATED)
            .mobile(UPDATED_MOBILE);

        restChvMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedChv.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedChv))
            )
            .andExpect(status().isOk());

        // Validate the Chv in the database
        List<Chv> chvList = chvRepository.findAll();
        assertThat(chvList).hasSize(databaseSizeBeforeUpdate);
        Chv testChv = chvList.get(chvList.size() - 1);
        assertThat(testChv.getUid()).isEqualTo(UPDATED_UID);
        assertThat(testChv.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testChv.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testChv.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testChv.getLastUpdated()).isEqualTo(UPDATED_LAST_UPDATED);
        assertThat(testChv.getMobile()).isEqualTo(UPDATED_MOBILE);
    }

    @Test
    @Transactional
    void putNonExistingChv() throws Exception {
        int databaseSizeBeforeUpdate = chvRepository.findAll().size();
        chv.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restChvMockMvc
            .perform(
                put(ENTITY_API_URL_ID, chv.getId()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(chv))
            )
            .andExpect(status().isBadRequest());

        // Validate the Chv in the database
        List<Chv> chvList = chvRepository.findAll();
        assertThat(chvList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchChv() throws Exception {
        int databaseSizeBeforeUpdate = chvRepository.findAll().size();
        chv.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChvMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(chv))
            )
            .andExpect(status().isBadRequest());

        // Validate the Chv in the database
        List<Chv> chvList = chvRepository.findAll();
        assertThat(chvList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamChv() throws Exception {
        int databaseSizeBeforeUpdate = chvRepository.findAll().size();
        chv.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChvMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(chv)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Chv in the database
        List<Chv> chvList = chvRepository.findAll();
        assertThat(chvList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateChvWithPatch() throws Exception {
        // Initialize the database
        chvRepository.saveAndFlush(chv);

        int databaseSizeBeforeUpdate = chvRepository.findAll().size();

        // Update the chv using partial update
        Chv partialUpdatedChv = new Chv();
        partialUpdatedChv.setId(chv.getId());

        partialUpdatedChv.uid(UPDATED_UID);

        restChvMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedChv.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedChv))
            )
            .andExpect(status().isOk());

        // Validate the Chv in the database
        List<Chv> chvList = chvRepository.findAll();
        assertThat(chvList).hasSize(databaseSizeBeforeUpdate);
        Chv testChv = chvList.get(chvList.size() - 1);
        assertThat(testChv.getUid()).isEqualTo(UPDATED_UID);
        assertThat(testChv.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testChv.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testChv.getCreated()).isEqualTo(DEFAULT_CREATED);
        assertThat(testChv.getLastUpdated()).isEqualTo(DEFAULT_LAST_UPDATED);
        assertThat(testChv.getMobile()).isEqualTo(DEFAULT_MOBILE);
    }

    @Test
    @Transactional
    void fullUpdateChvWithPatch() throws Exception {
        // Initialize the database
        chvRepository.saveAndFlush(chv);

        int databaseSizeBeforeUpdate = chvRepository.findAll().size();

        // Update the chv using partial update
        Chv partialUpdatedChv = new Chv();
        partialUpdatedChv.setId(chv.getId());

        partialUpdatedChv
            .uid(UPDATED_UID)
            .code(UPDATED_CODE)
            .description(UPDATED_DESCRIPTION)
            .created(UPDATED_CREATED)
            .lastUpdated(UPDATED_LAST_UPDATED)
            .mobile(UPDATED_MOBILE);

        restChvMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedChv.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedChv))
            )
            .andExpect(status().isOk());

        // Validate the Chv in the database
        List<Chv> chvList = chvRepository.findAll();
        assertThat(chvList).hasSize(databaseSizeBeforeUpdate);
        Chv testChv = chvList.get(chvList.size() - 1);
        assertThat(testChv.getUid()).isEqualTo(UPDATED_UID);
        assertThat(testChv.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testChv.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testChv.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testChv.getLastUpdated()).isEqualTo(UPDATED_LAST_UPDATED);
        assertThat(testChv.getMobile()).isEqualTo(UPDATED_MOBILE);
    }

    @Test
    @Transactional
    void patchNonExistingChv() throws Exception {
        int databaseSizeBeforeUpdate = chvRepository.findAll().size();
        chv.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restChvMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, chv.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(chv))
            )
            .andExpect(status().isBadRequest());

        // Validate the Chv in the database
        List<Chv> chvList = chvRepository.findAll();
        assertThat(chvList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchChv() throws Exception {
        int databaseSizeBeforeUpdate = chvRepository.findAll().size();
        chv.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChvMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(chv))
            )
            .andExpect(status().isBadRequest());

        // Validate the Chv in the database
        List<Chv> chvList = chvRepository.findAll();
        assertThat(chvList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamChv() throws Exception {
        int databaseSizeBeforeUpdate = chvRepository.findAll().size();
        chv.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChvMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(chv)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Chv in the database
        List<Chv> chvList = chvRepository.findAll();
        assertThat(chvList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteChv() throws Exception {
        // Initialize the database
        chvRepository.saveAndFlush(chv);

        int databaseSizeBeforeDelete = chvRepository.findAll().size();

        // Delete the chv
        restChvMockMvc.perform(delete(ENTITY_API_URL_ID, chv.getId()).accept(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Chv> chvList = chvRepository.findAll();
        assertThat(chvList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
