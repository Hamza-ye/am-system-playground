package org.nmcpye.activitiesmanagement.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
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
import org.nmcpye.activitiesmanagement.domain.dataset.DataSet;
import org.nmcpye.activitiesmanagement.repository.DataSetRepository;
import org.nmcpye.activitiesmanagement.service.DataSetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link DataSetResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class DataSetResourceIT {

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

    private static final Date DEFAULT_CREATED = Date.from(Instant.ofEpochMilli(0L));
    private static final Date UPDATED_CREATED = Date.from(Instant.now().truncatedTo(ChronoUnit.MILLIS));

    private static final Date DEFAULT_LAST_UPDATED = Date.from(Instant.ofEpochMilli(0L));
    private static final Date UPDATED_LAST_UPDATED = Date.from(Instant.now().truncatedTo(ChronoUnit.MILLIS));

    private static final Integer DEFAULT_EXPIRY_DAYS = 1;
    private static final Integer UPDATED_EXPIRY_DAYS = 2;

    private static final Integer DEFAULT_TIMELY_DAYS = 1;
    private static final Integer UPDATED_TIMELY_DAYS = 2;

    private static final String ENTITY_API_URL = "/api/data-sets";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DataSetRepository dataSetRepository;

    @Mock
    private DataSetRepository dataSetRepositoryMock;

    @Mock
    private DataSetService dataSetServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDataSetMockMvc;

    private DataSet dataSet;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DataSet createEntity(EntityManager em) {
        DataSet dataSet = new DataSet()
            .uid(DEFAULT_UID)
            .code(DEFAULT_CODE)
            .name(DEFAULT_NAME)
            .shortName(DEFAULT_SHORT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .created(DEFAULT_CREATED)
            .lastUpdated(DEFAULT_LAST_UPDATED)
            .expiryDays(DEFAULT_EXPIRY_DAYS)
            .timelyDays(DEFAULT_TIMELY_DAYS);
        return dataSet;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DataSet createUpdatedEntity(EntityManager em) {
        DataSet dataSet = new DataSet()
            .uid(UPDATED_UID)
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .shortName(UPDATED_SHORT_NAME)
            .description(UPDATED_DESCRIPTION)
            .created(UPDATED_CREATED)
            .lastUpdated(UPDATED_LAST_UPDATED)
            .expiryDays(UPDATED_EXPIRY_DAYS)
            .timelyDays(UPDATED_TIMELY_DAYS);
        return dataSet;
    }

    @BeforeEach
    public void initTest() {
        dataSet = createEntity(em);
    }

    @Test
    @Transactional
    void createDataSet() throws Exception {
        int databaseSizeBeforeCreate = dataSetRepository.findAll().size();
        // Create the DataSet
        restDataSetMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dataSet)))
            .andExpect(status().isCreated());

        // Validate the DataSet in the database
        List<DataSet> dataSetList = dataSetRepository.findAll();
        assertThat(dataSetList).hasSize(databaseSizeBeforeCreate + 1);
        DataSet testDataSet = dataSetList.get(dataSetList.size() - 1);
        assertThat(testDataSet.getUid()).isEqualTo(DEFAULT_UID);
        assertThat(testDataSet.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testDataSet.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testDataSet.getShortName()).isEqualTo(DEFAULT_SHORT_NAME);
        assertThat(testDataSet.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testDataSet.getCreated()).isEqualTo(DEFAULT_CREATED);
        assertThat(testDataSet.getLastUpdated()).isEqualTo(DEFAULT_LAST_UPDATED);
        assertThat(testDataSet.getExpiryDays()).isEqualTo(DEFAULT_EXPIRY_DAYS);
        assertThat(testDataSet.getTimelyDays()).isEqualTo(DEFAULT_TIMELY_DAYS);
    }

    @Test
    @Transactional
    void createDataSetWithExistingId() throws Exception {
        // Create the DataSet with an existing ID
        dataSet.setId(1L);

        int databaseSizeBeforeCreate = dataSetRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDataSetMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dataSet)))
            .andExpect(status().isBadRequest());

        // Validate the DataSet in the database
        List<DataSet> dataSetList = dataSetRepository.findAll();
        assertThat(dataSetList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkUidIsRequired() throws Exception {
        int databaseSizeBeforeTest = dataSetRepository.findAll().size();
        // set the field null
        dataSet.setUid(null);

        // Create the DataSet, which fails.

        restDataSetMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dataSet)))
            .andExpect(status().isBadRequest());

        List<DataSet> dataSetList = dataSetRepository.findAll();
        assertThat(dataSetList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDataSets() throws Exception {
        // Initialize the database
        dataSetRepository.saveAndFlush(dataSet);

        // Get all the dataSetList
        restDataSetMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dataSet.getId().intValue())))
            .andExpect(jsonPath("$.[*].uid").value(hasItem(DEFAULT_UID)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].shortName").value(hasItem(DEFAULT_SHORT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].created").value(hasItem(DEFAULT_CREATED.toString())))
            .andExpect(jsonPath("$.[*].lastUpdated").value(hasItem(DEFAULT_LAST_UPDATED.toString())))
            .andExpect(jsonPath("$.[*].expiryDays").value(hasItem(DEFAULT_EXPIRY_DAYS)))
            .andExpect(jsonPath("$.[*].timelyDays").value(hasItem(DEFAULT_TIMELY_DAYS)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllDataSetsWithEagerRelationshipsIsEnabled() throws Exception {
        when(dataSetServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restDataSetMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(dataSetServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllDataSetsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(dataSetServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restDataSetMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(dataSetServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getDataSet() throws Exception {
        // Initialize the database
        dataSetRepository.saveAndFlush(dataSet);

        // Get the dataSet
        restDataSetMockMvc
            .perform(get(ENTITY_API_URL_ID, dataSet.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(dataSet.getId().intValue()))
            .andExpect(jsonPath("$.uid").value(DEFAULT_UID))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.shortName").value(DEFAULT_SHORT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.created").value(DEFAULT_CREATED.toString()))
            .andExpect(jsonPath("$.lastUpdated").value(DEFAULT_LAST_UPDATED.toString()))
            .andExpect(jsonPath("$.expiryDays").value(DEFAULT_EXPIRY_DAYS))
            .andExpect(jsonPath("$.timelyDays").value(DEFAULT_TIMELY_DAYS));
    }

    @Test
    @Transactional
    void getNonExistingDataSet() throws Exception {
        // Get the dataSet
        restDataSetMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDataSet() throws Exception {
        // Initialize the database
        dataSetRepository.saveAndFlush(dataSet);

        int databaseSizeBeforeUpdate = dataSetRepository.findAll().size();

        // Update the dataSet
        DataSet updatedDataSet = dataSetRepository.findById(dataSet.getId()).get();
        // Disconnect from session so that the updates on updatedDataSet are not directly saved in db
        em.detach(updatedDataSet);
        updatedDataSet
            .uid(UPDATED_UID)
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .shortName(UPDATED_SHORT_NAME)
            .description(UPDATED_DESCRIPTION)
            .created(UPDATED_CREATED)
            .lastUpdated(UPDATED_LAST_UPDATED)
            .expiryDays(UPDATED_EXPIRY_DAYS)
            .timelyDays(UPDATED_TIMELY_DAYS);

        restDataSetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDataSet.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedDataSet))
            )
            .andExpect(status().isOk());

        // Validate the DataSet in the database
        List<DataSet> dataSetList = dataSetRepository.findAll();
        assertThat(dataSetList).hasSize(databaseSizeBeforeUpdate);
        DataSet testDataSet = dataSetList.get(dataSetList.size() - 1);
        assertThat(testDataSet.getUid()).isEqualTo(UPDATED_UID);
        assertThat(testDataSet.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testDataSet.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDataSet.getShortName()).isEqualTo(UPDATED_SHORT_NAME);
        assertThat(testDataSet.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testDataSet.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testDataSet.getLastUpdated()).isEqualTo(UPDATED_LAST_UPDATED);
        assertThat(testDataSet.getExpiryDays()).isEqualTo(UPDATED_EXPIRY_DAYS);
        assertThat(testDataSet.getTimelyDays()).isEqualTo(UPDATED_TIMELY_DAYS);
    }

    @Test
    @Transactional
    void putNonExistingDataSet() throws Exception {
        int databaseSizeBeforeUpdate = dataSetRepository.findAll().size();
        dataSet.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDataSetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, dataSet.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dataSet))
            )
            .andExpect(status().isBadRequest());

        // Validate the DataSet in the database
        List<DataSet> dataSetList = dataSetRepository.findAll();
        assertThat(dataSetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDataSet() throws Exception {
        int databaseSizeBeforeUpdate = dataSetRepository.findAll().size();
        dataSet.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDataSetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dataSet))
            )
            .andExpect(status().isBadRequest());

        // Validate the DataSet in the database
        List<DataSet> dataSetList = dataSetRepository.findAll();
        assertThat(dataSetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDataSet() throws Exception {
        int databaseSizeBeforeUpdate = dataSetRepository.findAll().size();
        dataSet.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDataSetMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dataSet)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DataSet in the database
        List<DataSet> dataSetList = dataSetRepository.findAll();
        assertThat(dataSetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDataSetWithPatch() throws Exception {
        // Initialize the database
        dataSetRepository.saveAndFlush(dataSet);

        int databaseSizeBeforeUpdate = dataSetRepository.findAll().size();

        // Update the dataSet using partial update
        DataSet partialUpdatedDataSet = new DataSet();
        partialUpdatedDataSet.setId(dataSet.getId());

        partialUpdatedDataSet
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .shortName(UPDATED_SHORT_NAME)
            .description(UPDATED_DESCRIPTION)
            .lastUpdated(UPDATED_LAST_UPDATED)
            .timelyDays(UPDATED_TIMELY_DAYS);

        restDataSetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDataSet.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDataSet))
            )
            .andExpect(status().isOk());

        // Validate the DataSet in the database
        List<DataSet> dataSetList = dataSetRepository.findAll();
        assertThat(dataSetList).hasSize(databaseSizeBeforeUpdate);
        DataSet testDataSet = dataSetList.get(dataSetList.size() - 1);
        assertThat(testDataSet.getUid()).isEqualTo(DEFAULT_UID);
        assertThat(testDataSet.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testDataSet.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDataSet.getShortName()).isEqualTo(UPDATED_SHORT_NAME);
        assertThat(testDataSet.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testDataSet.getCreated()).isEqualTo(DEFAULT_CREATED);
        assertThat(testDataSet.getLastUpdated()).isEqualTo(UPDATED_LAST_UPDATED);
        assertThat(testDataSet.getExpiryDays()).isEqualTo(DEFAULT_EXPIRY_DAYS);
        assertThat(testDataSet.getTimelyDays()).isEqualTo(UPDATED_TIMELY_DAYS);
    }

    @Test
    @Transactional
    void fullUpdateDataSetWithPatch() throws Exception {
        // Initialize the database
        dataSetRepository.saveAndFlush(dataSet);

        int databaseSizeBeforeUpdate = dataSetRepository.findAll().size();

        // Update the dataSet using partial update
        DataSet partialUpdatedDataSet = new DataSet();
        partialUpdatedDataSet.setId(dataSet.getId());

        partialUpdatedDataSet
            .uid(UPDATED_UID)
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .shortName(UPDATED_SHORT_NAME)
            .description(UPDATED_DESCRIPTION)
            .created(UPDATED_CREATED)
            .lastUpdated(UPDATED_LAST_UPDATED)
            .expiryDays(UPDATED_EXPIRY_DAYS)
            .timelyDays(UPDATED_TIMELY_DAYS);

        restDataSetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDataSet.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDataSet))
            )
            .andExpect(status().isOk());

        // Validate the DataSet in the database
        List<DataSet> dataSetList = dataSetRepository.findAll();
        assertThat(dataSetList).hasSize(databaseSizeBeforeUpdate);
        DataSet testDataSet = dataSetList.get(dataSetList.size() - 1);
        assertThat(testDataSet.getUid()).isEqualTo(UPDATED_UID);
        assertThat(testDataSet.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testDataSet.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDataSet.getShortName()).isEqualTo(UPDATED_SHORT_NAME);
        assertThat(testDataSet.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testDataSet.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testDataSet.getLastUpdated()).isEqualTo(UPDATED_LAST_UPDATED);
        assertThat(testDataSet.getExpiryDays()).isEqualTo(UPDATED_EXPIRY_DAYS);
        assertThat(testDataSet.getTimelyDays()).isEqualTo(UPDATED_TIMELY_DAYS);
    }

    @Test
    @Transactional
    void patchNonExistingDataSet() throws Exception {
        int databaseSizeBeforeUpdate = dataSetRepository.findAll().size();
        dataSet.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDataSetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, dataSet.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(dataSet))
            )
            .andExpect(status().isBadRequest());

        // Validate the DataSet in the database
        List<DataSet> dataSetList = dataSetRepository.findAll();
        assertThat(dataSetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDataSet() throws Exception {
        int databaseSizeBeforeUpdate = dataSetRepository.findAll().size();
        dataSet.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDataSetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(dataSet))
            )
            .andExpect(status().isBadRequest());

        // Validate the DataSet in the database
        List<DataSet> dataSetList = dataSetRepository.findAll();
        assertThat(dataSetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDataSet() throws Exception {
        int databaseSizeBeforeUpdate = dataSetRepository.findAll().size();
        dataSet.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDataSetMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(dataSet)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DataSet in the database
        List<DataSet> dataSetList = dataSetRepository.findAll();
        assertThat(dataSetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDataSet() throws Exception {
        // Initialize the database
        dataSetRepository.saveAndFlush(dataSet);

        int databaseSizeBeforeDelete = dataSetRepository.findAll().size();

        // Delete the dataSet
        restDataSetMockMvc
            .perform(delete(ENTITY_API_URL_ID, dataSet.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DataSet> dataSetList = dataSetRepository.findAll();
        assertThat(dataSetList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
