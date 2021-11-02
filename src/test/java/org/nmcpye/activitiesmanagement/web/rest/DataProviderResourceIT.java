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
import org.nmcpye.activitiesmanagement.domain.DataProvider;
import org.nmcpye.activitiesmanagement.repository.DataProviderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link DataProviderResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DataProviderResourceIT {

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

    private static final String ENTITY_API_URL = "/api/data-providers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DataProviderRepository dataProviderRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDataProviderMockMvc;

    private DataProvider dataProvider;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DataProvider createEntity(EntityManager em) {
        DataProvider dataProvider = new DataProvider()
            .uid(DEFAULT_UID)
            .code(DEFAULT_CODE)
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .created(DEFAULT_CREATED)
            .lastUpdated(DEFAULT_LAST_UPDATED)
            .mobile(DEFAULT_MOBILE);
        return dataProvider;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DataProvider createUpdatedEntity(EntityManager em) {
        DataProvider dataProvider = new DataProvider()
            .uid(UPDATED_UID)
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .created(UPDATED_CREATED)
            .lastUpdated(UPDATED_LAST_UPDATED)
            .mobile(UPDATED_MOBILE);
        return dataProvider;
    }

    @BeforeEach
    public void initTest() {
        dataProvider = createEntity(em);
    }

    @Test
    @Transactional
    void createDataProvider() throws Exception {
        int databaseSizeBeforeCreate = dataProviderRepository.findAll().size();
        // Create the DataProvider
        restDataProviderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dataProvider)))
            .andExpect(status().isCreated());

        // Validate the DataProvider in the database
        List<DataProvider> dataProviderList = dataProviderRepository.findAll();
        assertThat(dataProviderList).hasSize(databaseSizeBeforeCreate + 1);
        DataProvider testDataProvider = dataProviderList.get(dataProviderList.size() - 1);
        assertThat(testDataProvider.getUid()).isEqualTo(DEFAULT_UID);
        assertThat(testDataProvider.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testDataProvider.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testDataProvider.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testDataProvider.getCreated()).isEqualTo(DEFAULT_CREATED);
        assertThat(testDataProvider.getLastUpdated()).isEqualTo(DEFAULT_LAST_UPDATED);
        assertThat(testDataProvider.getMobile()).isEqualTo(DEFAULT_MOBILE);
    }

    @Test
    @Transactional
    void createDataProviderWithExistingId() throws Exception {
        // Create the DataProvider with an existing ID
        dataProvider.setId(1L);

        int databaseSizeBeforeCreate = dataProviderRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDataProviderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dataProvider)))
            .andExpect(status().isBadRequest());

        // Validate the DataProvider in the database
        List<DataProvider> dataProviderList = dataProviderRepository.findAll();
        assertThat(dataProviderList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkUidIsRequired() throws Exception {
        int databaseSizeBeforeTest = dataProviderRepository.findAll().size();
        // set the field null
        dataProvider.setUid(null);

        // Create the DataProvider, which fails.

        restDataProviderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dataProvider)))
            .andExpect(status().isBadRequest());

        List<DataProvider> dataProviderList = dataProviderRepository.findAll();
        assertThat(dataProviderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDataProviders() throws Exception {
        // Initialize the database
        dataProviderRepository.saveAndFlush(dataProvider);

        // Get all the dataProviderList
        restDataProviderMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dataProvider.getId().intValue())))
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
    void getDataProvider() throws Exception {
        // Initialize the database
        dataProviderRepository.saveAndFlush(dataProvider);

        // Get the dataProvider
        restDataProviderMockMvc
            .perform(get(ENTITY_API_URL_ID, dataProvider.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(dataProvider.getId().intValue()))
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
    void getNonExistingDataProvider() throws Exception {
        // Get the dataProvider
        restDataProviderMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDataProvider() throws Exception {
        // Initialize the database
        dataProviderRepository.saveAndFlush(dataProvider);

        int databaseSizeBeforeUpdate = dataProviderRepository.findAll().size();

        // Update the dataProvider
        DataProvider updatedDataProvider = dataProviderRepository.findById(dataProvider.getId()).get();
        // Disconnect from session so that the updates on updatedDataProvider are not directly saved in db
        em.detach(updatedDataProvider);
        updatedDataProvider
            .uid(UPDATED_UID)
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .created(UPDATED_CREATED)
            .lastUpdated(UPDATED_LAST_UPDATED)
            .mobile(UPDATED_MOBILE);

        restDataProviderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDataProvider.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedDataProvider))
            )
            .andExpect(status().isOk());

        // Validate the DataProvider in the database
        List<DataProvider> dataProviderList = dataProviderRepository.findAll();
        assertThat(dataProviderList).hasSize(databaseSizeBeforeUpdate);
        DataProvider testDataProvider = dataProviderList.get(dataProviderList.size() - 1);
        assertThat(testDataProvider.getUid()).isEqualTo(UPDATED_UID);
        assertThat(testDataProvider.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testDataProvider.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDataProvider.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testDataProvider.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testDataProvider.getLastUpdated()).isEqualTo(UPDATED_LAST_UPDATED);
        assertThat(testDataProvider.getMobile()).isEqualTo(UPDATED_MOBILE);
    }

    @Test
    @Transactional
    void putNonExistingDataProvider() throws Exception {
        int databaseSizeBeforeUpdate = dataProviderRepository.findAll().size();
        dataProvider.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDataProviderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, dataProvider.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dataProvider))
            )
            .andExpect(status().isBadRequest());

        // Validate the DataProvider in the database
        List<DataProvider> dataProviderList = dataProviderRepository.findAll();
        assertThat(dataProviderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDataProvider() throws Exception {
        int databaseSizeBeforeUpdate = dataProviderRepository.findAll().size();
        dataProvider.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDataProviderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dataProvider))
            )
            .andExpect(status().isBadRequest());

        // Validate the DataProvider in the database
        List<DataProvider> dataProviderList = dataProviderRepository.findAll();
        assertThat(dataProviderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDataProvider() throws Exception {
        int databaseSizeBeforeUpdate = dataProviderRepository.findAll().size();
        dataProvider.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDataProviderMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dataProvider)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DataProvider in the database
        List<DataProvider> dataProviderList = dataProviderRepository.findAll();
        assertThat(dataProviderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDataProviderWithPatch() throws Exception {
        // Initialize the database
        dataProviderRepository.saveAndFlush(dataProvider);

        int databaseSizeBeforeUpdate = dataProviderRepository.findAll().size();

        // Update the dataProvider using partial update
        DataProvider partialUpdatedDataProvider = new DataProvider();
        partialUpdatedDataProvider.setId(dataProvider.getId());

        partialUpdatedDataProvider.code(UPDATED_CODE).name(UPDATED_NAME).created(UPDATED_CREATED).lastUpdated(UPDATED_LAST_UPDATED);

        restDataProviderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDataProvider.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDataProvider))
            )
            .andExpect(status().isOk());

        // Validate the DataProvider in the database
        List<DataProvider> dataProviderList = dataProviderRepository.findAll();
        assertThat(dataProviderList).hasSize(databaseSizeBeforeUpdate);
        DataProvider testDataProvider = dataProviderList.get(dataProviderList.size() - 1);
        assertThat(testDataProvider.getUid()).isEqualTo(DEFAULT_UID);
        assertThat(testDataProvider.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testDataProvider.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDataProvider.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testDataProvider.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testDataProvider.getLastUpdated()).isEqualTo(UPDATED_LAST_UPDATED);
        assertThat(testDataProvider.getMobile()).isEqualTo(DEFAULT_MOBILE);
    }

    @Test
    @Transactional
    void fullUpdateDataProviderWithPatch() throws Exception {
        // Initialize the database
        dataProviderRepository.saveAndFlush(dataProvider);

        int databaseSizeBeforeUpdate = dataProviderRepository.findAll().size();

        // Update the dataProvider using partial update
        DataProvider partialUpdatedDataProvider = new DataProvider();
        partialUpdatedDataProvider.setId(dataProvider.getId());

        partialUpdatedDataProvider
            .uid(UPDATED_UID)
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .created(UPDATED_CREATED)
            .lastUpdated(UPDATED_LAST_UPDATED)
            .mobile(UPDATED_MOBILE);

        restDataProviderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDataProvider.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDataProvider))
            )
            .andExpect(status().isOk());

        // Validate the DataProvider in the database
        List<DataProvider> dataProviderList = dataProviderRepository.findAll();
        assertThat(dataProviderList).hasSize(databaseSizeBeforeUpdate);
        DataProvider testDataProvider = dataProviderList.get(dataProviderList.size() - 1);
        assertThat(testDataProvider.getUid()).isEqualTo(UPDATED_UID);
        assertThat(testDataProvider.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testDataProvider.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDataProvider.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testDataProvider.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testDataProvider.getLastUpdated()).isEqualTo(UPDATED_LAST_UPDATED);
        assertThat(testDataProvider.getMobile()).isEqualTo(UPDATED_MOBILE);
    }

    @Test
    @Transactional
    void patchNonExistingDataProvider() throws Exception {
        int databaseSizeBeforeUpdate = dataProviderRepository.findAll().size();
        dataProvider.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDataProviderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, dataProvider.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(dataProvider))
            )
            .andExpect(status().isBadRequest());

        // Validate the DataProvider in the database
        List<DataProvider> dataProviderList = dataProviderRepository.findAll();
        assertThat(dataProviderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDataProvider() throws Exception {
        int databaseSizeBeforeUpdate = dataProviderRepository.findAll().size();
        dataProvider.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDataProviderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(dataProvider))
            )
            .andExpect(status().isBadRequest());

        // Validate the DataProvider in the database
        List<DataProvider> dataProviderList = dataProviderRepository.findAll();
        assertThat(dataProviderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDataProvider() throws Exception {
        int databaseSizeBeforeUpdate = dataProviderRepository.findAll().size();
        dataProvider.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDataProviderMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(dataProvider))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DataProvider in the database
        List<DataProvider> dataProviderList = dataProviderRepository.findAll();
        assertThat(dataProviderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDataProvider() throws Exception {
        // Initialize the database
        dataProviderRepository.saveAndFlush(dataProvider);

        int databaseSizeBeforeDelete = dataProviderRepository.findAll().size();

        // Delete the dataProvider
        restDataProviderMockMvc
            .perform(delete(ENTITY_API_URL_ID, dataProvider.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DataProvider> dataProviderList = dataProviderRepository.findAll();
        assertThat(dataProviderList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
