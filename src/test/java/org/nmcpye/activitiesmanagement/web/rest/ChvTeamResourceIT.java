package org.nmcpye.activitiesmanagement.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
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
import org.nmcpye.activitiesmanagement.domain.ChvTeam;
import org.nmcpye.activitiesmanagement.domain.enumeration.ChvTeamType;
import org.nmcpye.activitiesmanagement.repository.ChvTeamRepository;
import org.nmcpye.activitiesmanagement.service.ChvTeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ChvTeamResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ChvTeamResourceIT {

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

    private static final String DEFAULT_TEAM_NO = "7396113127";
    private static final String UPDATED_TEAM_NO = "05";

    private static final ChvTeamType DEFAULT_TEAM_TYPE = ChvTeamType.SUPERVISOR;
    private static final ChvTeamType UPDATED_TEAM_TYPE = ChvTeamType.EVALUATION;

    private static final String ENTITY_API_URL = "/api/chv-teams";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ChvTeamRepository chvTeamRepository;

    @Mock
    private ChvTeamRepository cHVTeamRepositoryMock;

    @Mock
    private ChvTeamService cHVTeamServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restChvTeamMockMvc;

    private ChvTeam cHVTeam;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ChvTeam createEntity(EntityManager em) {
        ChvTeam chvTeam = new ChvTeam()
            .uid(DEFAULT_UID)
            .code(DEFAULT_CODE)
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .created(DEFAULT_CREATED)
            .lastUpdated(DEFAULT_LAST_UPDATED)
            .teamNo(DEFAULT_TEAM_NO)
            .teamType(DEFAULT_TEAM_TYPE);
        return chvTeam;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ChvTeam createUpdatedEntity(EntityManager em) {
        ChvTeam chvTeam = new ChvTeam()
            .uid(UPDATED_UID)
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .created(UPDATED_CREATED)
            .lastUpdated(UPDATED_LAST_UPDATED)
            .teamNo(UPDATED_TEAM_NO)
            .teamType(UPDATED_TEAM_TYPE);
        return chvTeam;
    }

    @BeforeEach
    public void initTest() {
        cHVTeam = createEntity(em);
    }

    @Test
    @Transactional
    void createChvTeam() throws Exception {
        int databaseSizeBeforeCreate = chvTeamRepository.findAll().size();
        // Create the ChvTeam
        restChvTeamMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cHVTeam)))
            .andExpect(status().isCreated());

        // Validate the ChvTeam in the database
        List<ChvTeam> chvTeam = chvTeamRepository.findAll();
        assertThat(chvTeam).hasSize(databaseSizeBeforeCreate + 1);
        ChvTeam testChvTeam = chvTeam.get(chvTeam.size() - 1);
        assertThat(testChvTeam.getUid()).isEqualTo(DEFAULT_UID);
        assertThat(testChvTeam.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testChvTeam.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testChvTeam.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testChvTeam.getCreated()).isEqualTo(DEFAULT_CREATED);
        assertThat(testChvTeam.getLastUpdated()).isEqualTo(DEFAULT_LAST_UPDATED);
        assertThat(testChvTeam.getTeamNo()).isEqualTo(DEFAULT_TEAM_NO);
        assertThat(testChvTeam.getTeamType()).isEqualTo(DEFAULT_TEAM_TYPE);
    }

    @Test
    @Transactional
    void createChvTeamWithExistingId() throws Exception {
        // Create the ChvTeam with an existing ID
        cHVTeam.setId(1L);

        int databaseSizeBeforeCreate = chvTeamRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restChvTeamMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cHVTeam)))
            .andExpect(status().isBadRequest());

        // Validate the ChvTeam in the database
        List<ChvTeam> cHVTeamList = chvTeamRepository.findAll();
        assertThat(cHVTeamList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkUidIsRequired() throws Exception {
        int databaseSizeBeforeTest = chvTeamRepository.findAll().size();
        // set the field null
        cHVTeam.setUid(null);

        // Create the ChvTeam, which fails.

        restChvTeamMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cHVTeam)))
            .andExpect(status().isBadRequest());

        List<ChvTeam> cHVTeamList = chvTeamRepository.findAll();
        assertThat(cHVTeamList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTeamNoIsRequired() throws Exception {
        int databaseSizeBeforeTest = chvTeamRepository.findAll().size();
        // set the field null
        cHVTeam.setTeamNo(null);

        // Create the ChvTeam, which fails.

        restChvTeamMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cHVTeam)))
            .andExpect(status().isBadRequest());

        List<ChvTeam> cHVTeamList = chvTeamRepository.findAll();
        assertThat(cHVTeamList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTeamTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = chvTeamRepository.findAll().size();
        // set the field null
        cHVTeam.setTeamType(null);

        // Create the ChvTeam, which fails.

        restChvTeamMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cHVTeam)))
            .andExpect(status().isBadRequest());

        List<ChvTeam> cHVTeamList = chvTeamRepository.findAll();
        assertThat(cHVTeamList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllChvTeams() throws Exception {
        // Initialize the database
        chvTeamRepository.saveAndFlush(cHVTeam);

        // Get all the cHVTeamList
        restChvTeamMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cHVTeam.getId().intValue())))
            .andExpect(jsonPath("$.[*].uid").value(hasItem(DEFAULT_UID)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].created").value(hasItem(DEFAULT_CREATED.toString())))
            .andExpect(jsonPath("$.[*].lastUpdated").value(hasItem(DEFAULT_LAST_UPDATED.toString())))
            .andExpect(jsonPath("$.[*].teamNo").value(hasItem(DEFAULT_TEAM_NO)))
            .andExpect(jsonPath("$.[*].teamType").value(hasItem(DEFAULT_TEAM_TYPE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllChvTeamsWithEagerRelationshipsIsEnabled() throws Exception {
        when(cHVTeamServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restChvTeamMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(cHVTeamServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllChvTeamsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(cHVTeamServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restChvTeamMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(cHVTeamServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getChvTeam() throws Exception {
        // Initialize the database
        chvTeamRepository.saveAndFlush(cHVTeam);

        // Get the cHVTeam
        restChvTeamMockMvc
            .perform(get(ENTITY_API_URL_ID, cHVTeam.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cHVTeam.getId().intValue()))
            .andExpect(jsonPath("$.uid").value(DEFAULT_UID))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.created").value(DEFAULT_CREATED.toString()))
            .andExpect(jsonPath("$.lastUpdated").value(DEFAULT_LAST_UPDATED.toString()))
            .andExpect(jsonPath("$.teamNo").value(DEFAULT_TEAM_NO))
            .andExpect(jsonPath("$.teamType").value(DEFAULT_TEAM_TYPE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingChvTeam() throws Exception {
        // Get the cHVTeam
        restChvTeamMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewChvTeam() throws Exception {
        // Initialize the database
        chvTeamRepository.saveAndFlush(cHVTeam);

        int databaseSizeBeforeUpdate = chvTeamRepository.findAll().size();

        // Update the cHVTeam
        ChvTeam updatedChvTeam = chvTeamRepository.findById(cHVTeam.getId()).get();
        // Disconnect from session so that the updates on updatedChvTeam are not directly saved in db
        em.detach(updatedChvTeam);
        updatedChvTeam
            .uid(UPDATED_UID)
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .created(UPDATED_CREATED)
            .lastUpdated(UPDATED_LAST_UPDATED)
            .teamNo(UPDATED_TEAM_NO)
            .teamType(UPDATED_TEAM_TYPE);

        restChvTeamMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedChvTeam.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedChvTeam))
            )
            .andExpect(status().isOk());

        // Validate the ChvTeam in the database
        List<ChvTeam> cHVTeamList = chvTeamRepository.findAll();
        assertThat(cHVTeamList).hasSize(databaseSizeBeforeUpdate);
        ChvTeam testChvTeam = cHVTeamList.get(cHVTeamList.size() - 1);
        assertThat(testChvTeam.getUid()).isEqualTo(UPDATED_UID);
        assertThat(testChvTeam.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testChvTeam.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testChvTeam.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testChvTeam.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testChvTeam.getLastUpdated()).isEqualTo(UPDATED_LAST_UPDATED);
        assertThat(testChvTeam.getTeamNo()).isEqualTo(UPDATED_TEAM_NO);
        assertThat(testChvTeam.getTeamType()).isEqualTo(UPDATED_TEAM_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingCHVTeam() throws Exception {
        int databaseSizeBeforeUpdate = chvTeamRepository.findAll().size();
        cHVTeam.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restChvTeamMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cHVTeam.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cHVTeam))
            )
            .andExpect(status().isBadRequest());

        // Validate the ChvTeam in the database
        List<ChvTeam> cHVTeamList = chvTeamRepository.findAll();
        assertThat(cHVTeamList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCHVTeam() throws Exception {
        int databaseSizeBeforeUpdate = chvTeamRepository.findAll().size();
        cHVTeam.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChvTeamMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cHVTeam))
            )
            .andExpect(status().isBadRequest());

        // Validate the ChvTeam in the database
        List<ChvTeam> cHVTeamList = chvTeamRepository.findAll();
        assertThat(cHVTeamList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCHVTeam() throws Exception {
        int databaseSizeBeforeUpdate = chvTeamRepository.findAll().size();
        cHVTeam.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChvTeamMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cHVTeam)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ChvTeam in the database
        List<ChvTeam> cHVTeamList = chvTeamRepository.findAll();
        assertThat(cHVTeamList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCHVTeamWithPatch() throws Exception {
        // Initialize the database
        chvTeamRepository.saveAndFlush(cHVTeam);

        int databaseSizeBeforeUpdate = chvTeamRepository.findAll().size();

        // Update the cHVTeam using partial update
        ChvTeam partialUpdatedChvTeam = new ChvTeam();
        partialUpdatedChvTeam.setId(cHVTeam.getId());

        partialUpdatedChvTeam
            .code(UPDATED_CODE)
            .description(UPDATED_DESCRIPTION)
            .lastUpdated(UPDATED_LAST_UPDATED)
            .teamType(UPDATED_TEAM_TYPE);

        restChvTeamMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedChvTeam.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedChvTeam))
            )
            .andExpect(status().isOk());

        // Validate the ChvTeam in the database
        List<ChvTeam> cHVTeamList = chvTeamRepository.findAll();
        assertThat(cHVTeamList).hasSize(databaseSizeBeforeUpdate);
        ChvTeam testChvTeam = cHVTeamList.get(cHVTeamList.size() - 1);
        assertThat(testChvTeam.getUid()).isEqualTo(DEFAULT_UID);
        assertThat(testChvTeam.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testChvTeam.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testChvTeam.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testChvTeam.getCreated()).isEqualTo(DEFAULT_CREATED);
        assertThat(testChvTeam.getLastUpdated()).isEqualTo(UPDATED_LAST_UPDATED);
        assertThat(testChvTeam.getTeamNo()).isEqualTo(DEFAULT_TEAM_NO);
        assertThat(testChvTeam.getTeamType()).isEqualTo(UPDATED_TEAM_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateCHVTeamWithPatch() throws Exception {
        // Initialize the database
        chvTeamRepository.saveAndFlush(cHVTeam);

        int databaseSizeBeforeUpdate = chvTeamRepository.findAll().size();

        // Update the cHVTeam using partial update
        ChvTeam partialUpdatedChvTeam = new ChvTeam();
        partialUpdatedChvTeam.setId(cHVTeam.getId());

        partialUpdatedChvTeam
            .uid(UPDATED_UID)
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .created(UPDATED_CREATED)
            .lastUpdated(UPDATED_LAST_UPDATED)
            .teamNo(UPDATED_TEAM_NO)
            .teamType(UPDATED_TEAM_TYPE);

        restChvTeamMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedChvTeam.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedChvTeam))
            )
            .andExpect(status().isOk());

        // Validate the ChvTeam in the database
        List<ChvTeam> cHVTeamList = chvTeamRepository.findAll();
        assertThat(cHVTeamList).hasSize(databaseSizeBeforeUpdate);
        ChvTeam testChvTeam = cHVTeamList.get(cHVTeamList.size() - 1);
        assertThat(testChvTeam.getUid()).isEqualTo(UPDATED_UID);
        assertThat(testChvTeam.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testChvTeam.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testChvTeam.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testChvTeam.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testChvTeam.getLastUpdated()).isEqualTo(UPDATED_LAST_UPDATED);
        assertThat(testChvTeam.getTeamNo()).isEqualTo(UPDATED_TEAM_NO);
        assertThat(testChvTeam.getTeamType()).isEqualTo(UPDATED_TEAM_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingCHVTeam() throws Exception {
        int databaseSizeBeforeUpdate = chvTeamRepository.findAll().size();
        cHVTeam.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restChvTeamMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, cHVTeam.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cHVTeam))
            )
            .andExpect(status().isBadRequest());

        // Validate the ChvTeam in the database
        List<ChvTeam> cHVTeamList = chvTeamRepository.findAll();
        assertThat(cHVTeamList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCHVTeam() throws Exception {
        int databaseSizeBeforeUpdate = chvTeamRepository.findAll().size();
        cHVTeam.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChvTeamMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cHVTeam))
            )
            .andExpect(status().isBadRequest());

        // Validate the ChvTeam in the database
        List<ChvTeam> cHVTeamList = chvTeamRepository.findAll();
        assertThat(cHVTeamList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCHVTeam() throws Exception {
        int databaseSizeBeforeUpdate = chvTeamRepository.findAll().size();
        cHVTeam.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChvTeamMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(cHVTeam)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ChvTeam in the database
        List<ChvTeam> cHVTeamList = chvTeamRepository.findAll();
        assertThat(cHVTeamList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCHVTeam() throws Exception {
        // Initialize the database
        chvTeamRepository.saveAndFlush(cHVTeam);

        int databaseSizeBeforeDelete = chvTeamRepository.findAll().size();

        // Delete the cHVTeam
        restChvTeamMockMvc
            .perform(delete(ENTITY_API_URL_ID, cHVTeam.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ChvTeam> cHVTeamList = chvTeamRepository.findAll();
        assertThat(cHVTeamList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
