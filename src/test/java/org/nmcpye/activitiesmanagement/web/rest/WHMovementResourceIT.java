package org.nmcpye.activitiesmanagement.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.nmcpye.activitiesmanagement.IntegrationTest;
import org.nmcpye.activitiesmanagement.domain.WHMovement;
import org.nmcpye.activitiesmanagement.domain.Warehouse;
import org.nmcpye.activitiesmanagement.domain.WorkingDay;
import org.nmcpye.activitiesmanagement.domain.enumeration.MovementType;
import org.nmcpye.activitiesmanagement.repository.WHMovementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link WHMovementResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class WHMovementResourceIT {

    private static final MovementType DEFAULT_MOVEMENT_TYPE = MovementType.IN;
    private static final MovementType UPDATED_MOVEMENT_TYPE = MovementType.OUT;

    private static final Integer DEFAULT_QUANTITY = 0;
    private static final Integer UPDATED_QUANTITY = 1;

    private static final String DEFAULT_RECONCILIATION_SOURCE = "AAAAAAAAAA";
    private static final String UPDATED_RECONCILIATION_SOURCE = "BBBBBBBBBB";

    private static final String DEFAULT_RECONCILIATION_DESTINATION = "AAAAAAAAAA";
    private static final String UPDATED_RECONCILIATION_DESTINATION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_CONFIRMED_BY_OTHER_SIDE = false;
    private static final Boolean UPDATED_CONFIRMED_BY_OTHER_SIDE = true;

    private static final String DEFAULT_COMMENT = "AAAAAAAAAA";
    private static final String UPDATED_COMMENT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/wh-movements";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private WHMovementRepository wHMovementRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restWHMovementMockMvc;

    private WHMovement wHMovement;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WHMovement createEntity(EntityManager em) {
        WHMovement wHMovement = new WHMovement()
            .movementType(DEFAULT_MOVEMENT_TYPE)
            .quantity(DEFAULT_QUANTITY)
            .reconciliationSource(DEFAULT_RECONCILIATION_SOURCE)
            .reconciliationDestination(DEFAULT_RECONCILIATION_DESTINATION)
            .confirmedByOtherSide(DEFAULT_CONFIRMED_BY_OTHER_SIDE)
            .comment(DEFAULT_COMMENT);
        // Add required entity
        WorkingDay workingDay;
        if (TestUtil.findAll(em, WorkingDay.class).isEmpty()) {
            workingDay = WorkingDayResourceIT.createEntity(em);
            em.persist(workingDay);
            em.flush();
        } else {
            workingDay = TestUtil.findAll(em, WorkingDay.class).get(0);
        }
        wHMovement.setDay(workingDay);
        // Add required entity
        Warehouse warehouse;
        if (TestUtil.findAll(em, Warehouse.class).isEmpty()) {
            warehouse = WarehouseResourceIT.createEntity(em);
            em.persist(warehouse);
            em.flush();
        } else {
            warehouse = TestUtil.findAll(em, Warehouse.class).get(0);
        }
        wHMovement.setInitiatedWH(warehouse);
        return wHMovement;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WHMovement createUpdatedEntity(EntityManager em) {
        WHMovement wHMovement = new WHMovement()
            .movementType(UPDATED_MOVEMENT_TYPE)
            .quantity(UPDATED_QUANTITY)
            .reconciliationSource(UPDATED_RECONCILIATION_SOURCE)
            .reconciliationDestination(UPDATED_RECONCILIATION_DESTINATION)
            .confirmedByOtherSide(UPDATED_CONFIRMED_BY_OTHER_SIDE)
            .comment(UPDATED_COMMENT);
        // Add required entity
        WorkingDay workingDay;
        if (TestUtil.findAll(em, WorkingDay.class).isEmpty()) {
            workingDay = WorkingDayResourceIT.createUpdatedEntity(em);
            em.persist(workingDay);
            em.flush();
        } else {
            workingDay = TestUtil.findAll(em, WorkingDay.class).get(0);
        }
        wHMovement.setDay(workingDay);
        // Add required entity
        Warehouse warehouse;
        if (TestUtil.findAll(em, Warehouse.class).isEmpty()) {
            warehouse = WarehouseResourceIT.createUpdatedEntity(em);
            em.persist(warehouse);
            em.flush();
        } else {
            warehouse = TestUtil.findAll(em, Warehouse.class).get(0);
        }
        wHMovement.setInitiatedWH(warehouse);
        return wHMovement;
    }

    @BeforeEach
    public void initTest() {
        wHMovement = createEntity(em);
    }

    @Test
    @Transactional
    void createWHMovement() throws Exception {
        int databaseSizeBeforeCreate = wHMovementRepository.findAll().size();
        // Create the WHMovement
        restWHMovementMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(wHMovement)))
            .andExpect(status().isCreated());

        // Validate the WHMovement in the database
        List<WHMovement> wHMovementList = wHMovementRepository.findAll();
        assertThat(wHMovementList).hasSize(databaseSizeBeforeCreate + 1);
        WHMovement testWHMovement = wHMovementList.get(wHMovementList.size() - 1);
        assertThat(testWHMovement.getMovementType()).isEqualTo(DEFAULT_MOVEMENT_TYPE);
        assertThat(testWHMovement.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
        assertThat(testWHMovement.getReconciliationSource()).isEqualTo(DEFAULT_RECONCILIATION_SOURCE);
        assertThat(testWHMovement.getReconciliationDestination()).isEqualTo(DEFAULT_RECONCILIATION_DESTINATION);
        assertThat(testWHMovement.getConfirmedByOtherSide()).isEqualTo(DEFAULT_CONFIRMED_BY_OTHER_SIDE);
        assertThat(testWHMovement.getComment()).isEqualTo(DEFAULT_COMMENT);
    }

    @Test
    @Transactional
    void createWHMovementWithExistingId() throws Exception {
        // Create the WHMovement with an existing ID
        wHMovement.setId(1L);

        int databaseSizeBeforeCreate = wHMovementRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restWHMovementMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(wHMovement)))
            .andExpect(status().isBadRequest());

        // Validate the WHMovement in the database
        List<WHMovement> wHMovementList = wHMovementRepository.findAll();
        assertThat(wHMovementList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkMovementTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = wHMovementRepository.findAll().size();
        // set the field null
        wHMovement.setMovementType(null);

        // Create the WHMovement, which fails.

        restWHMovementMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(wHMovement)))
            .andExpect(status().isBadRequest());

        List<WHMovement> wHMovementList = wHMovementRepository.findAll();
        assertThat(wHMovementList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkQuantityIsRequired() throws Exception {
        int databaseSizeBeforeTest = wHMovementRepository.findAll().size();
        // set the field null
        wHMovement.setQuantity(null);

        // Create the WHMovement, which fails.

        restWHMovementMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(wHMovement)))
            .andExpect(status().isBadRequest());

        List<WHMovement> wHMovementList = wHMovementRepository.findAll();
        assertThat(wHMovementList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllWHMovements() throws Exception {
        // Initialize the database
        wHMovementRepository.saveAndFlush(wHMovement);

        // Get all the wHMovementList
        restWHMovementMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(wHMovement.getId().intValue())))
            .andExpect(jsonPath("$.[*].movementType").value(hasItem(DEFAULT_MOVEMENT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.[*].reconciliationSource").value(hasItem(DEFAULT_RECONCILIATION_SOURCE)))
            .andExpect(jsonPath("$.[*].reconciliationDestination").value(hasItem(DEFAULT_RECONCILIATION_DESTINATION)))
            .andExpect(jsonPath("$.[*].confirmedByOtherSide").value(hasItem(DEFAULT_CONFIRMED_BY_OTHER_SIDE.booleanValue())))
            .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT)));
    }

    @Test
    @Transactional
    void getWHMovement() throws Exception {
        // Initialize the database
        wHMovementRepository.saveAndFlush(wHMovement);

        // Get the wHMovement
        restWHMovementMockMvc
            .perform(get(ENTITY_API_URL_ID, wHMovement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(wHMovement.getId().intValue()))
            .andExpect(jsonPath("$.movementType").value(DEFAULT_MOVEMENT_TYPE.toString()))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY))
            .andExpect(jsonPath("$.reconciliationSource").value(DEFAULT_RECONCILIATION_SOURCE))
            .andExpect(jsonPath("$.reconciliationDestination").value(DEFAULT_RECONCILIATION_DESTINATION))
            .andExpect(jsonPath("$.confirmedByOtherSide").value(DEFAULT_CONFIRMED_BY_OTHER_SIDE.booleanValue()))
            .andExpect(jsonPath("$.comment").value(DEFAULT_COMMENT));
    }

    @Test
    @Transactional
    void getNonExistingWHMovement() throws Exception {
        // Get the wHMovement
        restWHMovementMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewWHMovement() throws Exception {
        // Initialize the database
        wHMovementRepository.saveAndFlush(wHMovement);

        int databaseSizeBeforeUpdate = wHMovementRepository.findAll().size();

        // Update the wHMovement
        WHMovement updatedWHMovement = wHMovementRepository.findById(wHMovement.getId()).get();
        // Disconnect from session so that the updates on updatedWHMovement are not directly saved in db
        em.detach(updatedWHMovement);
        updatedWHMovement
            .movementType(UPDATED_MOVEMENT_TYPE)
            .quantity(UPDATED_QUANTITY)
            .reconciliationSource(UPDATED_RECONCILIATION_SOURCE)
            .reconciliationDestination(UPDATED_RECONCILIATION_DESTINATION)
            .confirmedByOtherSide(UPDATED_CONFIRMED_BY_OTHER_SIDE)
            .comment(UPDATED_COMMENT);

        restWHMovementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedWHMovement.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedWHMovement))
            )
            .andExpect(status().isOk());

        // Validate the WHMovement in the database
        List<WHMovement> wHMovementList = wHMovementRepository.findAll();
        assertThat(wHMovementList).hasSize(databaseSizeBeforeUpdate);
        WHMovement testWHMovement = wHMovementList.get(wHMovementList.size() - 1);
        assertThat(testWHMovement.getMovementType()).isEqualTo(UPDATED_MOVEMENT_TYPE);
        assertThat(testWHMovement.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testWHMovement.getReconciliationSource()).isEqualTo(UPDATED_RECONCILIATION_SOURCE);
        assertThat(testWHMovement.getReconciliationDestination()).isEqualTo(UPDATED_RECONCILIATION_DESTINATION);
        assertThat(testWHMovement.getConfirmedByOtherSide()).isEqualTo(UPDATED_CONFIRMED_BY_OTHER_SIDE);
        assertThat(testWHMovement.getComment()).isEqualTo(UPDATED_COMMENT);
    }

    @Test
    @Transactional
    void putNonExistingWHMovement() throws Exception {
        int databaseSizeBeforeUpdate = wHMovementRepository.findAll().size();
        wHMovement.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWHMovementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, wHMovement.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(wHMovement))
            )
            .andExpect(status().isBadRequest());

        // Validate the WHMovement in the database
        List<WHMovement> wHMovementList = wHMovementRepository.findAll();
        assertThat(wHMovementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchWHMovement() throws Exception {
        int databaseSizeBeforeUpdate = wHMovementRepository.findAll().size();
        wHMovement.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWHMovementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(wHMovement))
            )
            .andExpect(status().isBadRequest());

        // Validate the WHMovement in the database
        List<WHMovement> wHMovementList = wHMovementRepository.findAll();
        assertThat(wHMovementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamWHMovement() throws Exception {
        int databaseSizeBeforeUpdate = wHMovementRepository.findAll().size();
        wHMovement.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWHMovementMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(wHMovement)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the WHMovement in the database
        List<WHMovement> wHMovementList = wHMovementRepository.findAll();
        assertThat(wHMovementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateWHMovementWithPatch() throws Exception {
        // Initialize the database
        wHMovementRepository.saveAndFlush(wHMovement);

        int databaseSizeBeforeUpdate = wHMovementRepository.findAll().size();

        // Update the wHMovement using partial update
        WHMovement partialUpdatedWHMovement = new WHMovement();
        partialUpdatedWHMovement.setId(wHMovement.getId());

        partialUpdatedWHMovement.quantity(UPDATED_QUANTITY).reconciliationSource(UPDATED_RECONCILIATION_SOURCE).comment(UPDATED_COMMENT);

        restWHMovementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWHMovement.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWHMovement))
            )
            .andExpect(status().isOk());

        // Validate the WHMovement in the database
        List<WHMovement> wHMovementList = wHMovementRepository.findAll();
        assertThat(wHMovementList).hasSize(databaseSizeBeforeUpdate);
        WHMovement testWHMovement = wHMovementList.get(wHMovementList.size() - 1);
        assertThat(testWHMovement.getMovementType()).isEqualTo(DEFAULT_MOVEMENT_TYPE);
        assertThat(testWHMovement.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testWHMovement.getReconciliationSource()).isEqualTo(UPDATED_RECONCILIATION_SOURCE);
        assertThat(testWHMovement.getReconciliationDestination()).isEqualTo(DEFAULT_RECONCILIATION_DESTINATION);
        assertThat(testWHMovement.getConfirmedByOtherSide()).isEqualTo(DEFAULT_CONFIRMED_BY_OTHER_SIDE);
        assertThat(testWHMovement.getComment()).isEqualTo(UPDATED_COMMENT);
    }

    @Test
    @Transactional
    void fullUpdateWHMovementWithPatch() throws Exception {
        // Initialize the database
        wHMovementRepository.saveAndFlush(wHMovement);

        int databaseSizeBeforeUpdate = wHMovementRepository.findAll().size();

        // Update the wHMovement using partial update
        WHMovement partialUpdatedWHMovement = new WHMovement();
        partialUpdatedWHMovement.setId(wHMovement.getId());

        partialUpdatedWHMovement
            .movementType(UPDATED_MOVEMENT_TYPE)
            .quantity(UPDATED_QUANTITY)
            .reconciliationSource(UPDATED_RECONCILIATION_SOURCE)
            .reconciliationDestination(UPDATED_RECONCILIATION_DESTINATION)
            .confirmedByOtherSide(UPDATED_CONFIRMED_BY_OTHER_SIDE)
            .comment(UPDATED_COMMENT);

        restWHMovementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWHMovement.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWHMovement))
            )
            .andExpect(status().isOk());

        // Validate the WHMovement in the database
        List<WHMovement> wHMovementList = wHMovementRepository.findAll();
        assertThat(wHMovementList).hasSize(databaseSizeBeforeUpdate);
        WHMovement testWHMovement = wHMovementList.get(wHMovementList.size() - 1);
        assertThat(testWHMovement.getMovementType()).isEqualTo(UPDATED_MOVEMENT_TYPE);
        assertThat(testWHMovement.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testWHMovement.getReconciliationSource()).isEqualTo(UPDATED_RECONCILIATION_SOURCE);
        assertThat(testWHMovement.getReconciliationDestination()).isEqualTo(UPDATED_RECONCILIATION_DESTINATION);
        assertThat(testWHMovement.getConfirmedByOtherSide()).isEqualTo(UPDATED_CONFIRMED_BY_OTHER_SIDE);
        assertThat(testWHMovement.getComment()).isEqualTo(UPDATED_COMMENT);
    }

    @Test
    @Transactional
    void patchNonExistingWHMovement() throws Exception {
        int databaseSizeBeforeUpdate = wHMovementRepository.findAll().size();
        wHMovement.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWHMovementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, wHMovement.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(wHMovement))
            )
            .andExpect(status().isBadRequest());

        // Validate the WHMovement in the database
        List<WHMovement> wHMovementList = wHMovementRepository.findAll();
        assertThat(wHMovementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchWHMovement() throws Exception {
        int databaseSizeBeforeUpdate = wHMovementRepository.findAll().size();
        wHMovement.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWHMovementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(wHMovement))
            )
            .andExpect(status().isBadRequest());

        // Validate the WHMovement in the database
        List<WHMovement> wHMovementList = wHMovementRepository.findAll();
        assertThat(wHMovementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamWHMovement() throws Exception {
        int databaseSizeBeforeUpdate = wHMovementRepository.findAll().size();
        wHMovement.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWHMovementMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(wHMovement))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the WHMovement in the database
        List<WHMovement> wHMovementList = wHMovementRepository.findAll();
        assertThat(wHMovementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteWHMovement() throws Exception {
        // Initialize the database
        wHMovementRepository.saveAndFlush(wHMovement);

        int databaseSizeBeforeDelete = wHMovementRepository.findAll().size();

        // Delete the wHMovement
        restWHMovementMockMvc
            .perform(delete(ENTITY_API_URL_ID, wHMovement.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<WHMovement> wHMovementList = wHMovementRepository.findAll();
        assertThat(wHMovementList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
