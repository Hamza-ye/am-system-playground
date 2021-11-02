package org.nmcpye.activitiesmanagement.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.nmcpye.activitiesmanagement.web.rest.TestUtil.sameNumber;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.nmcpye.activitiesmanagement.IntegrationTest;
import org.nmcpye.activitiesmanagement.domain.Activity;
import org.nmcpye.activitiesmanagement.domain.Warehouse;
import org.nmcpye.activitiesmanagement.repository.WarehouseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link WarehouseResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class WarehouseResourceIT {

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

    private static final Integer DEFAULT_WH_NO = 1;
    private static final Integer UPDATED_WH_NO = 2;

    private static final BigDecimal DEFAULT_INITIAL_BALANCE_PLAN = new BigDecimal(0);
    private static final BigDecimal UPDATED_INITIAL_BALANCE_PLAN = new BigDecimal(1);

    private static final BigDecimal DEFAULT_INITIAL_BALANCE_ACTUAL = new BigDecimal(0);
    private static final BigDecimal UPDATED_INITIAL_BALANCE_ACTUAL = new BigDecimal(1);

    private static final String ENTITY_API_URL = "/api/warehouses";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private WarehouseRepository warehouseRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restWarehouseMockMvc;

    private Warehouse warehouse;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Warehouse createEntity(EntityManager em) {
        Warehouse warehouse = new Warehouse()
            .uid(DEFAULT_UID)
            .code(DEFAULT_CODE)
            .name(DEFAULT_NAME)
            .created(DEFAULT_CREATED)
            .lastUpdated(DEFAULT_LAST_UPDATED)
            .whNo(DEFAULT_WH_NO)
            .initialBalancePlan(DEFAULT_INITIAL_BALANCE_PLAN)
            .initialBalanceActual(DEFAULT_INITIAL_BALANCE_ACTUAL);
        // Add required entity
        Activity activity;
        if (TestUtil.findAll(em, Activity.class).isEmpty()) {
            activity = ActivityResourceIT.createEntity(em);
            em.persist(activity);
            em.flush();
        } else {
            activity = TestUtil.findAll(em, Activity.class).get(0);
        }
        warehouse.setActivity(activity);
        return warehouse;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Warehouse createUpdatedEntity(EntityManager em) {
        Warehouse warehouse = new Warehouse()
            .uid(UPDATED_UID)
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .created(UPDATED_CREATED)
            .lastUpdated(UPDATED_LAST_UPDATED)
            .whNo(UPDATED_WH_NO)
            .initialBalancePlan(UPDATED_INITIAL_BALANCE_PLAN)
            .initialBalanceActual(UPDATED_INITIAL_BALANCE_ACTUAL);
        // Add required entity
        Activity activity;
        if (TestUtil.findAll(em, Activity.class).isEmpty()) {
            activity = ActivityResourceIT.createUpdatedEntity(em);
            em.persist(activity);
            em.flush();
        } else {
            activity = TestUtil.findAll(em, Activity.class).get(0);
        }
        warehouse.setActivity(activity);
        return warehouse;
    }

    @BeforeEach
    public void initTest() {
        warehouse = createEntity(em);
    }

    @Test
    @Transactional
    void createWarehouse() throws Exception {
        int databaseSizeBeforeCreate = warehouseRepository.findAll().size();
        // Create the Warehouse
        restWarehouseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(warehouse)))
            .andExpect(status().isCreated());

        // Validate the Warehouse in the database
        List<Warehouse> warehouseList = warehouseRepository.findAll();
        assertThat(warehouseList).hasSize(databaseSizeBeforeCreate + 1);
        Warehouse testWarehouse = warehouseList.get(warehouseList.size() - 1);
        assertThat(testWarehouse.getUid()).isEqualTo(DEFAULT_UID);
        assertThat(testWarehouse.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testWarehouse.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testWarehouse.getCreated()).isEqualTo(DEFAULT_CREATED);
        assertThat(testWarehouse.getLastUpdated()).isEqualTo(DEFAULT_LAST_UPDATED);
        assertThat(testWarehouse.getWhNo()).isEqualTo(DEFAULT_WH_NO);
        assertThat(testWarehouse.getInitialBalancePlan()).isEqualByComparingTo(DEFAULT_INITIAL_BALANCE_PLAN);
        assertThat(testWarehouse.getInitialBalanceActual()).isEqualByComparingTo(DEFAULT_INITIAL_BALANCE_ACTUAL);
    }

    @Test
    @Transactional
    void createWarehouseWithExistingId() throws Exception {
        // Create the Warehouse with an existing ID
        warehouse.setId(1L);

        int databaseSizeBeforeCreate = warehouseRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restWarehouseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(warehouse)))
            .andExpect(status().isBadRequest());

        // Validate the Warehouse in the database
        List<Warehouse> warehouseList = warehouseRepository.findAll();
        assertThat(warehouseList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkUidIsRequired() throws Exception {
        int databaseSizeBeforeTest = warehouseRepository.findAll().size();
        // set the field null
        warehouse.setUid(null);

        // Create the Warehouse, which fails.

        restWarehouseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(warehouse)))
            .andExpect(status().isBadRequest());

        List<Warehouse> warehouseList = warehouseRepository.findAll();
        assertThat(warehouseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkWhNoIsRequired() throws Exception {
        int databaseSizeBeforeTest = warehouseRepository.findAll().size();
        // set the field null
        warehouse.setWhNo(null);

        // Create the Warehouse, which fails.

        restWarehouseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(warehouse)))
            .andExpect(status().isBadRequest());

        List<Warehouse> warehouseList = warehouseRepository.findAll();
        assertThat(warehouseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkInitialBalancePlanIsRequired() throws Exception {
        int databaseSizeBeforeTest = warehouseRepository.findAll().size();
        // set the field null
        warehouse.setInitialBalancePlan(null);

        // Create the Warehouse, which fails.

        restWarehouseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(warehouse)))
            .andExpect(status().isBadRequest());

        List<Warehouse> warehouseList = warehouseRepository.findAll();
        assertThat(warehouseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkInitialBalanceActualIsRequired() throws Exception {
        int databaseSizeBeforeTest = warehouseRepository.findAll().size();
        // set the field null
        warehouse.setInitialBalanceActual(null);

        // Create the Warehouse, which fails.

        restWarehouseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(warehouse)))
            .andExpect(status().isBadRequest());

        List<Warehouse> warehouseList = warehouseRepository.findAll();
        assertThat(warehouseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllWarehouses() throws Exception {
        // Initialize the database
        warehouseRepository.saveAndFlush(warehouse);

        // Get all the warehouseList
        restWarehouseMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(warehouse.getId().intValue())))
            .andExpect(jsonPath("$.[*].uid").value(hasItem(DEFAULT_UID)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].created").value(hasItem(DEFAULT_CREATED.toString())))
            .andExpect(jsonPath("$.[*].lastUpdated").value(hasItem(DEFAULT_LAST_UPDATED.toString())))
            .andExpect(jsonPath("$.[*].whNo").value(hasItem(DEFAULT_WH_NO)))
            .andExpect(jsonPath("$.[*].initialBalancePlan").value(hasItem(sameNumber(DEFAULT_INITIAL_BALANCE_PLAN))))
            .andExpect(jsonPath("$.[*].initialBalanceActual").value(hasItem(sameNumber(DEFAULT_INITIAL_BALANCE_ACTUAL))));
    }

    @Test
    @Transactional
    void getWarehouse() throws Exception {
        // Initialize the database
        warehouseRepository.saveAndFlush(warehouse);

        // Get the warehouse
        restWarehouseMockMvc
            .perform(get(ENTITY_API_URL_ID, warehouse.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(warehouse.getId().intValue()))
            .andExpect(jsonPath("$.uid").value(DEFAULT_UID))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.created").value(DEFAULT_CREATED.toString()))
            .andExpect(jsonPath("$.lastUpdated").value(DEFAULT_LAST_UPDATED.toString()))
            .andExpect(jsonPath("$.whNo").value(DEFAULT_WH_NO))
            .andExpect(jsonPath("$.initialBalancePlan").value(sameNumber(DEFAULT_INITIAL_BALANCE_PLAN)))
            .andExpect(jsonPath("$.initialBalanceActual").value(sameNumber(DEFAULT_INITIAL_BALANCE_ACTUAL)));
    }

    @Test
    @Transactional
    void getNonExistingWarehouse() throws Exception {
        // Get the warehouse
        restWarehouseMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewWarehouse() throws Exception {
        // Initialize the database
        warehouseRepository.saveAndFlush(warehouse);

        int databaseSizeBeforeUpdate = warehouseRepository.findAll().size();

        // Update the warehouse
        Warehouse updatedWarehouse = warehouseRepository.findById(warehouse.getId()).get();
        // Disconnect from session so that the updates on updatedWarehouse are not directly saved in db
        em.detach(updatedWarehouse);
        updatedWarehouse
            .uid(UPDATED_UID)
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .created(UPDATED_CREATED)
            .lastUpdated(UPDATED_LAST_UPDATED)
            .whNo(UPDATED_WH_NO)
            .initialBalancePlan(UPDATED_INITIAL_BALANCE_PLAN)
            .initialBalanceActual(UPDATED_INITIAL_BALANCE_ACTUAL);

        restWarehouseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedWarehouse.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedWarehouse))
            )
            .andExpect(status().isOk());

        // Validate the Warehouse in the database
        List<Warehouse> warehouseList = warehouseRepository.findAll();
        assertThat(warehouseList).hasSize(databaseSizeBeforeUpdate);
        Warehouse testWarehouse = warehouseList.get(warehouseList.size() - 1);
        assertThat(testWarehouse.getUid()).isEqualTo(UPDATED_UID);
        assertThat(testWarehouse.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testWarehouse.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testWarehouse.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testWarehouse.getLastUpdated()).isEqualTo(UPDATED_LAST_UPDATED);
        assertThat(testWarehouse.getWhNo()).isEqualTo(UPDATED_WH_NO);
        assertThat(testWarehouse.getInitialBalancePlan()).isEqualTo(UPDATED_INITIAL_BALANCE_PLAN);
        assertThat(testWarehouse.getInitialBalanceActual()).isEqualTo(UPDATED_INITIAL_BALANCE_ACTUAL);
    }

    @Test
    @Transactional
    void putNonExistingWarehouse() throws Exception {
        int databaseSizeBeforeUpdate = warehouseRepository.findAll().size();
        warehouse.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWarehouseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, warehouse.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(warehouse))
            )
            .andExpect(status().isBadRequest());

        // Validate the Warehouse in the database
        List<Warehouse> warehouseList = warehouseRepository.findAll();
        assertThat(warehouseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchWarehouse() throws Exception {
        int databaseSizeBeforeUpdate = warehouseRepository.findAll().size();
        warehouse.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWarehouseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(warehouse))
            )
            .andExpect(status().isBadRequest());

        // Validate the Warehouse in the database
        List<Warehouse> warehouseList = warehouseRepository.findAll();
        assertThat(warehouseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamWarehouse() throws Exception {
        int databaseSizeBeforeUpdate = warehouseRepository.findAll().size();
        warehouse.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWarehouseMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(warehouse)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Warehouse in the database
        List<Warehouse> warehouseList = warehouseRepository.findAll();
        assertThat(warehouseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateWarehouseWithPatch() throws Exception {
        // Initialize the database
        warehouseRepository.saveAndFlush(warehouse);

        int databaseSizeBeforeUpdate = warehouseRepository.findAll().size();

        // Update the warehouse using partial update
        Warehouse partialUpdatedWarehouse = new Warehouse();
        partialUpdatedWarehouse.setId(warehouse.getId());

        partialUpdatedWarehouse.code(UPDATED_CODE);

        restWarehouseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWarehouse.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWarehouse))
            )
            .andExpect(status().isOk());

        // Validate the Warehouse in the database
        List<Warehouse> warehouseList = warehouseRepository.findAll();
        assertThat(warehouseList).hasSize(databaseSizeBeforeUpdate);
        Warehouse testWarehouse = warehouseList.get(warehouseList.size() - 1);
        assertThat(testWarehouse.getUid()).isEqualTo(DEFAULT_UID);
        assertThat(testWarehouse.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testWarehouse.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testWarehouse.getCreated()).isEqualTo(DEFAULT_CREATED);
        assertThat(testWarehouse.getLastUpdated()).isEqualTo(DEFAULT_LAST_UPDATED);
        assertThat(testWarehouse.getWhNo()).isEqualTo(DEFAULT_WH_NO);
        assertThat(testWarehouse.getInitialBalancePlan()).isEqualByComparingTo(DEFAULT_INITIAL_BALANCE_PLAN);
        assertThat(testWarehouse.getInitialBalanceActual()).isEqualByComparingTo(DEFAULT_INITIAL_BALANCE_ACTUAL);
    }

    @Test
    @Transactional
    void fullUpdateWarehouseWithPatch() throws Exception {
        // Initialize the database
        warehouseRepository.saveAndFlush(warehouse);

        int databaseSizeBeforeUpdate = warehouseRepository.findAll().size();

        // Update the warehouse using partial update
        Warehouse partialUpdatedWarehouse = new Warehouse();
        partialUpdatedWarehouse.setId(warehouse.getId());

        partialUpdatedWarehouse
            .uid(UPDATED_UID)
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .created(UPDATED_CREATED)
            .lastUpdated(UPDATED_LAST_UPDATED)
            .whNo(UPDATED_WH_NO)
            .initialBalancePlan(UPDATED_INITIAL_BALANCE_PLAN)
            .initialBalanceActual(UPDATED_INITIAL_BALANCE_ACTUAL);

        restWarehouseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWarehouse.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWarehouse))
            )
            .andExpect(status().isOk());

        // Validate the Warehouse in the database
        List<Warehouse> warehouseList = warehouseRepository.findAll();
        assertThat(warehouseList).hasSize(databaseSizeBeforeUpdate);
        Warehouse testWarehouse = warehouseList.get(warehouseList.size() - 1);
        assertThat(testWarehouse.getUid()).isEqualTo(UPDATED_UID);
        assertThat(testWarehouse.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testWarehouse.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testWarehouse.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testWarehouse.getLastUpdated()).isEqualTo(UPDATED_LAST_UPDATED);
        assertThat(testWarehouse.getWhNo()).isEqualTo(UPDATED_WH_NO);
        assertThat(testWarehouse.getInitialBalancePlan()).isEqualByComparingTo(UPDATED_INITIAL_BALANCE_PLAN);
        assertThat(testWarehouse.getInitialBalanceActual()).isEqualByComparingTo(UPDATED_INITIAL_BALANCE_ACTUAL);
    }

    @Test
    @Transactional
    void patchNonExistingWarehouse() throws Exception {
        int databaseSizeBeforeUpdate = warehouseRepository.findAll().size();
        warehouse.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWarehouseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, warehouse.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(warehouse))
            )
            .andExpect(status().isBadRequest());

        // Validate the Warehouse in the database
        List<Warehouse> warehouseList = warehouseRepository.findAll();
        assertThat(warehouseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchWarehouse() throws Exception {
        int databaseSizeBeforeUpdate = warehouseRepository.findAll().size();
        warehouse.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWarehouseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(warehouse))
            )
            .andExpect(status().isBadRequest());

        // Validate the Warehouse in the database
        List<Warehouse> warehouseList = warehouseRepository.findAll();
        assertThat(warehouseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamWarehouse() throws Exception {
        int databaseSizeBeforeUpdate = warehouseRepository.findAll().size();
        warehouse.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWarehouseMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(warehouse))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Warehouse in the database
        List<Warehouse> warehouseList = warehouseRepository.findAll();
        assertThat(warehouseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteWarehouse() throws Exception {
        // Initialize the database
        warehouseRepository.saveAndFlush(warehouse);

        int databaseSizeBeforeDelete = warehouseRepository.findAll().size();

        // Delete the warehouse
        restWarehouseMockMvc
            .perform(delete(ENTITY_API_URL_ID, warehouse.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Warehouse> warehouseList = warehouseRepository.findAll();
        assertThat(warehouseList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
