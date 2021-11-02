package org.nmcpye.activitiesmanagement.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.nmcpye.activitiesmanagement.IntegrationTest;
import org.nmcpye.activitiesmanagement.domain.OrganisationUnit;
import org.nmcpye.activitiesmanagement.domain.enumeration.OrganisationUnitType;
import org.nmcpye.activitiesmanagement.repository.OrganisationUnitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link OrganisationUnitResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OrganisationUnitResourceIT {

    private static final String DEFAULT_UID = "AAAAAAAAAA";
    private static final String UPDATED_UID = "BBBBBBBBBB";

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_SHORT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_SHORT_NAME = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_LAST_UPDATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_UPDATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_PATH = "AAAAAAAAAA";
    private static final String UPDATED_PATH = "BBBBBBBBBB";

    private static final Integer DEFAULT_HIERARCHY_LEVEL = 1;
    private static final Integer UPDATED_HIERARCHY_LEVEL = 2;

    private static final LocalDate DEFAULT_OPENING_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_OPENING_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_COMMENT = "AAAAAAAAAA";
    private static final String UPDATED_COMMENT = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_CLOSED_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CLOSED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_URL = "AAAAAAAAAA";
    private static final String UPDATED_URL = "BBBBBBBBBB";

    private static final String DEFAULT_CONTACT_PERSON = "AAAAAAAAAA";
    private static final String UPDATED_CONTACT_PERSON = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBB";

    private static final OrganisationUnitType DEFAULT_ORGANISATION_UNIT_TYPE = OrganisationUnitType.COUNTRY;
    private static final OrganisationUnitType UPDATED_ORGANISATION_UNIT_TYPE = OrganisationUnitType.GOV;

    private static final String ENTITY_API_URL = "/api/organisation-units";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private OrganisationUnitRepository organisationUnitRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOrganisationUnitMockMvc;

    private OrganisationUnit organisationUnit;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrganisationUnit createEntity(EntityManager em) {
        OrganisationUnit organisationUnit = new OrganisationUnit()
            .uid(DEFAULT_UID)
            .code(DEFAULT_CODE)
            .name(DEFAULT_NAME)
            .shortName(DEFAULT_SHORT_NAME)
            .created(DEFAULT_CREATED)
            .lastUpdated(DEFAULT_LAST_UPDATED)
            .path(DEFAULT_PATH)
            .hierarchyLevel(DEFAULT_HIERARCHY_LEVEL)
            .openingDate(DEFAULT_OPENING_DATE)
            .comment(DEFAULT_COMMENT)
            .closedDate(DEFAULT_CLOSED_DATE)
            .url(DEFAULT_URL)
            .contactPerson(DEFAULT_CONTACT_PERSON)
            .address(DEFAULT_ADDRESS)
            .email(DEFAULT_EMAIL)
            .phoneNumber(DEFAULT_PHONE_NUMBER)
            .organisationUnitType(DEFAULT_ORGANISATION_UNIT_TYPE);
        return organisationUnit;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrganisationUnit createUpdatedEntity(EntityManager em) {
        OrganisationUnit organisationUnit = new OrganisationUnit()
            .uid(UPDATED_UID)
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .shortName(UPDATED_SHORT_NAME)
            .created(UPDATED_CREATED)
            .lastUpdated(UPDATED_LAST_UPDATED)
            .path(UPDATED_PATH)
            .hierarchyLevel(UPDATED_HIERARCHY_LEVEL)
            .openingDate(UPDATED_OPENING_DATE)
            .comment(UPDATED_COMMENT)
            .closedDate(UPDATED_CLOSED_DATE)
            .url(UPDATED_URL)
            .contactPerson(UPDATED_CONTACT_PERSON)
            .address(UPDATED_ADDRESS)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .organisationUnitType(UPDATED_ORGANISATION_UNIT_TYPE);
        return organisationUnit;
    }

    @BeforeEach
    public void initTest() {
        organisationUnit = createEntity(em);
    }

    @Test
    @Transactional
    void createOrganisationUnit() throws Exception {
        int databaseSizeBeforeCreate = organisationUnitRepository.findAll().size();
        // Create the OrganisationUnit
        restOrganisationUnitMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(organisationUnit))
            )
            .andExpect(status().isCreated());

        // Validate the OrganisationUnit in the database
        List<OrganisationUnit> organisationUnitList = organisationUnitRepository.findAll();
        assertThat(organisationUnitList).hasSize(databaseSizeBeforeCreate + 1);
        OrganisationUnit testOrganisationUnit = organisationUnitList.get(organisationUnitList.size() - 1);
        assertThat(testOrganisationUnit.getUid()).isEqualTo(DEFAULT_UID);
        assertThat(testOrganisationUnit.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testOrganisationUnit.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testOrganisationUnit.getShortName()).isEqualTo(DEFAULT_SHORT_NAME);
        assertThat(testOrganisationUnit.getCreated()).isEqualTo(DEFAULT_CREATED);
        assertThat(testOrganisationUnit.getLastUpdated()).isEqualTo(DEFAULT_LAST_UPDATED);
        assertThat(testOrganisationUnit.getPath()).isEqualTo(DEFAULT_PATH);
        assertThat(testOrganisationUnit.getHierarchyLevel()).isEqualTo(DEFAULT_HIERARCHY_LEVEL);
        assertThat(testOrganisationUnit.getOpeningDate()).isEqualTo(DEFAULT_OPENING_DATE);
        assertThat(testOrganisationUnit.getComment()).isEqualTo(DEFAULT_COMMENT);
        assertThat(testOrganisationUnit.getClosedDate()).isEqualTo(DEFAULT_CLOSED_DATE);
        assertThat(testOrganisationUnit.getUrl()).isEqualTo(DEFAULT_URL);
        assertThat(testOrganisationUnit.getContactPerson()).isEqualTo(DEFAULT_CONTACT_PERSON);
        assertThat(testOrganisationUnit.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testOrganisationUnit.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testOrganisationUnit.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
        assertThat(testOrganisationUnit.getOrganisationUnitType()).isEqualTo(DEFAULT_ORGANISATION_UNIT_TYPE);
    }

    @Test
    @Transactional
    void createOrganisationUnitWithExistingId() throws Exception {
        // Create the OrganisationUnit with an existing ID
        organisationUnit.setId(1L);

        int databaseSizeBeforeCreate = organisationUnitRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrganisationUnitMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(organisationUnit))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrganisationUnit in the database
        List<OrganisationUnit> organisationUnitList = organisationUnitRepository.findAll();
        assertThat(organisationUnitList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkUidIsRequired() throws Exception {
        int databaseSizeBeforeTest = organisationUnitRepository.findAll().size();
        // set the field null
        organisationUnit.setUid(null);

        // Create the OrganisationUnit, which fails.

        restOrganisationUnitMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(organisationUnit))
            )
            .andExpect(status().isBadRequest());

        List<OrganisationUnit> organisationUnitList = organisationUnitRepository.findAll();
        assertThat(organisationUnitList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkOrganisationUnitTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = organisationUnitRepository.findAll().size();
        // set the field null
        organisationUnit.setOrganisationUnitType(null);

        // Create the OrganisationUnit, which fails.

        restOrganisationUnitMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(organisationUnit))
            )
            .andExpect(status().isBadRequest());

        List<OrganisationUnit> organisationUnitList = organisationUnitRepository.findAll();
        assertThat(organisationUnitList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllOrganisationUnits() throws Exception {
        // Initialize the database
        organisationUnitRepository.saveAndFlush(organisationUnit);

        // Get all the organisationUnitList
        restOrganisationUnitMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(organisationUnit.getId().intValue())))
            .andExpect(jsonPath("$.[*].uid").value(hasItem(DEFAULT_UID)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].shortName").value(hasItem(DEFAULT_SHORT_NAME)))
            .andExpect(jsonPath("$.[*].created").value(hasItem(DEFAULT_CREATED.toString())))
            .andExpect(jsonPath("$.[*].lastUpdated").value(hasItem(DEFAULT_LAST_UPDATED.toString())))
            .andExpect(jsonPath("$.[*].path").value(hasItem(DEFAULT_PATH)))
            .andExpect(jsonPath("$.[*].hierarchyLevel").value(hasItem(DEFAULT_HIERARCHY_LEVEL)))
            .andExpect(jsonPath("$.[*].openingDate").value(hasItem(DEFAULT_OPENING_DATE.toString())))
            .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT)))
            .andExpect(jsonPath("$.[*].closedDate").value(hasItem(DEFAULT_CLOSED_DATE.toString())))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL)))
            .andExpect(jsonPath("$.[*].contactPerson").value(hasItem(DEFAULT_CONTACT_PERSON)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].organisationUnitType").value(hasItem(DEFAULT_ORGANISATION_UNIT_TYPE.toString())));
    }

    @Test
    @Transactional
    void getOrganisationUnit() throws Exception {
        // Initialize the database
        organisationUnitRepository.saveAndFlush(organisationUnit);

        // Get the organisationUnit
        restOrganisationUnitMockMvc
            .perform(get(ENTITY_API_URL_ID, organisationUnit.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(organisationUnit.getId().intValue()))
            .andExpect(jsonPath("$.uid").value(DEFAULT_UID))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.shortName").value(DEFAULT_SHORT_NAME))
            .andExpect(jsonPath("$.created").value(DEFAULT_CREATED.toString()))
            .andExpect(jsonPath("$.lastUpdated").value(DEFAULT_LAST_UPDATED.toString()))
            .andExpect(jsonPath("$.path").value(DEFAULT_PATH))
            .andExpect(jsonPath("$.hierarchyLevel").value(DEFAULT_HIERARCHY_LEVEL))
            .andExpect(jsonPath("$.openingDate").value(DEFAULT_OPENING_DATE.toString()))
            .andExpect(jsonPath("$.comment").value(DEFAULT_COMMENT))
            .andExpect(jsonPath("$.closedDate").value(DEFAULT_CLOSED_DATE.toString()))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL))
            .andExpect(jsonPath("$.contactPerson").value(DEFAULT_CONTACT_PERSON))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER))
            .andExpect(jsonPath("$.organisationUnitType").value(DEFAULT_ORGANISATION_UNIT_TYPE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingOrganisationUnit() throws Exception {
        // Get the organisationUnit
        restOrganisationUnitMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewOrganisationUnit() throws Exception {
        // Initialize the database
        organisationUnitRepository.saveAndFlush(organisationUnit);

        int databaseSizeBeforeUpdate = organisationUnitRepository.findAll().size();

        // Update the organisationUnit
        OrganisationUnit updatedOrganisationUnit = organisationUnitRepository.findById(organisationUnit.getId()).get();
        // Disconnect from session so that the updates on updatedOrganisationUnit are not directly saved in db
        em.detach(updatedOrganisationUnit);
        updatedOrganisationUnit
            .uid(UPDATED_UID)
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .shortName(UPDATED_SHORT_NAME)
            .created(UPDATED_CREATED)
            .lastUpdated(UPDATED_LAST_UPDATED)
            .path(UPDATED_PATH)
            .hierarchyLevel(UPDATED_HIERARCHY_LEVEL)
            .openingDate(UPDATED_OPENING_DATE)
            .comment(UPDATED_COMMENT)
            .closedDate(UPDATED_CLOSED_DATE)
            .url(UPDATED_URL)
            .contactPerson(UPDATED_CONTACT_PERSON)
            .address(UPDATED_ADDRESS)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .organisationUnitType(UPDATED_ORGANISATION_UNIT_TYPE);

        restOrganisationUnitMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedOrganisationUnit.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedOrganisationUnit))
            )
            .andExpect(status().isOk());

        // Validate the OrganisationUnit in the database
        List<OrganisationUnit> organisationUnitList = organisationUnitRepository.findAll();
        assertThat(organisationUnitList).hasSize(databaseSizeBeforeUpdate);
        OrganisationUnit testOrganisationUnit = organisationUnitList.get(organisationUnitList.size() - 1);
        assertThat(testOrganisationUnit.getUid()).isEqualTo(UPDATED_UID);
        assertThat(testOrganisationUnit.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testOrganisationUnit.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testOrganisationUnit.getShortName()).isEqualTo(UPDATED_SHORT_NAME);
        assertThat(testOrganisationUnit.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testOrganisationUnit.getLastUpdated()).isEqualTo(UPDATED_LAST_UPDATED);
        assertThat(testOrganisationUnit.getPath()).isEqualTo(UPDATED_PATH);
        assertThat(testOrganisationUnit.getHierarchyLevel()).isEqualTo(UPDATED_HIERARCHY_LEVEL);
        assertThat(testOrganisationUnit.getOpeningDate()).isEqualTo(UPDATED_OPENING_DATE);
        assertThat(testOrganisationUnit.getComment()).isEqualTo(UPDATED_COMMENT);
        assertThat(testOrganisationUnit.getClosedDate()).isEqualTo(UPDATED_CLOSED_DATE);
        assertThat(testOrganisationUnit.getUrl()).isEqualTo(UPDATED_URL);
        assertThat(testOrganisationUnit.getContactPerson()).isEqualTo(UPDATED_CONTACT_PERSON);
        assertThat(testOrganisationUnit.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testOrganisationUnit.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testOrganisationUnit.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testOrganisationUnit.getOrganisationUnitType()).isEqualTo(UPDATED_ORGANISATION_UNIT_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingOrganisationUnit() throws Exception {
        int databaseSizeBeforeUpdate = organisationUnitRepository.findAll().size();
        organisationUnit.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrganisationUnitMockMvc
            .perform(
                put(ENTITY_API_URL_ID, organisationUnit.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(organisationUnit))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrganisationUnit in the database
        List<OrganisationUnit> organisationUnitList = organisationUnitRepository.findAll();
        assertThat(organisationUnitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOrganisationUnit() throws Exception {
        int databaseSizeBeforeUpdate = organisationUnitRepository.findAll().size();
        organisationUnit.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrganisationUnitMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(organisationUnit))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrganisationUnit in the database
        List<OrganisationUnit> organisationUnitList = organisationUnitRepository.findAll();
        assertThat(organisationUnitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOrganisationUnit() throws Exception {
        int databaseSizeBeforeUpdate = organisationUnitRepository.findAll().size();
        organisationUnit.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrganisationUnitMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(organisationUnit))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrganisationUnit in the database
        List<OrganisationUnit> organisationUnitList = organisationUnitRepository.findAll();
        assertThat(organisationUnitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOrganisationUnitWithPatch() throws Exception {
        // Initialize the database
        organisationUnitRepository.saveAndFlush(organisationUnit);

        int databaseSizeBeforeUpdate = organisationUnitRepository.findAll().size();

        // Update the organisationUnit using partial update
        OrganisationUnit partialUpdatedOrganisationUnit = new OrganisationUnit();
        partialUpdatedOrganisationUnit.setId(organisationUnit.getId());

        partialUpdatedOrganisationUnit.uid(UPDATED_UID).name(UPDATED_NAME).lastUpdated(UPDATED_LAST_UPDATED).comment(UPDATED_COMMENT);

        restOrganisationUnitMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrganisationUnit.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrganisationUnit))
            )
            .andExpect(status().isOk());

        // Validate the OrganisationUnit in the database
        List<OrganisationUnit> organisationUnitList = organisationUnitRepository.findAll();
        assertThat(organisationUnitList).hasSize(databaseSizeBeforeUpdate);
        OrganisationUnit testOrganisationUnit = organisationUnitList.get(organisationUnitList.size() - 1);
        assertThat(testOrganisationUnit.getUid()).isEqualTo(UPDATED_UID);
        assertThat(testOrganisationUnit.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testOrganisationUnit.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testOrganisationUnit.getShortName()).isEqualTo(DEFAULT_SHORT_NAME);
        assertThat(testOrganisationUnit.getCreated()).isEqualTo(DEFAULT_CREATED);
        assertThat(testOrganisationUnit.getLastUpdated()).isEqualTo(UPDATED_LAST_UPDATED);
        assertThat(testOrganisationUnit.getPath()).isEqualTo(DEFAULT_PATH);
        assertThat(testOrganisationUnit.getHierarchyLevel()).isEqualTo(DEFAULT_HIERARCHY_LEVEL);
        assertThat(testOrganisationUnit.getOpeningDate()).isEqualTo(DEFAULT_OPENING_DATE);
        assertThat(testOrganisationUnit.getComment()).isEqualTo(UPDATED_COMMENT);
        assertThat(testOrganisationUnit.getClosedDate()).isEqualTo(DEFAULT_CLOSED_DATE);
        assertThat(testOrganisationUnit.getUrl()).isEqualTo(DEFAULT_URL);
        assertThat(testOrganisationUnit.getContactPerson()).isEqualTo(DEFAULT_CONTACT_PERSON);
        assertThat(testOrganisationUnit.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testOrganisationUnit.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testOrganisationUnit.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
        assertThat(testOrganisationUnit.getOrganisationUnitType()).isEqualTo(DEFAULT_ORGANISATION_UNIT_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateOrganisationUnitWithPatch() throws Exception {
        // Initialize the database
        organisationUnitRepository.saveAndFlush(organisationUnit);

        int databaseSizeBeforeUpdate = organisationUnitRepository.findAll().size();

        // Update the organisationUnit using partial update
        OrganisationUnit partialUpdatedOrganisationUnit = new OrganisationUnit();
        partialUpdatedOrganisationUnit.setId(organisationUnit.getId());

        partialUpdatedOrganisationUnit
            .uid(UPDATED_UID)
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .shortName(UPDATED_SHORT_NAME)
            .created(UPDATED_CREATED)
            .lastUpdated(UPDATED_LAST_UPDATED)
            .path(UPDATED_PATH)
            .hierarchyLevel(UPDATED_HIERARCHY_LEVEL)
            .openingDate(UPDATED_OPENING_DATE)
            .comment(UPDATED_COMMENT)
            .closedDate(UPDATED_CLOSED_DATE)
            .url(UPDATED_URL)
            .contactPerson(UPDATED_CONTACT_PERSON)
            .address(UPDATED_ADDRESS)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .organisationUnitType(UPDATED_ORGANISATION_UNIT_TYPE);

        restOrganisationUnitMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrganisationUnit.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrganisationUnit))
            )
            .andExpect(status().isOk());

        // Validate the OrganisationUnit in the database
        List<OrganisationUnit> organisationUnitList = organisationUnitRepository.findAll();
        assertThat(organisationUnitList).hasSize(databaseSizeBeforeUpdate);
        OrganisationUnit testOrganisationUnit = organisationUnitList.get(organisationUnitList.size() - 1);
        assertThat(testOrganisationUnit.getUid()).isEqualTo(UPDATED_UID);
        assertThat(testOrganisationUnit.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testOrganisationUnit.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testOrganisationUnit.getShortName()).isEqualTo(UPDATED_SHORT_NAME);
        assertThat(testOrganisationUnit.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testOrganisationUnit.getLastUpdated()).isEqualTo(UPDATED_LAST_UPDATED);
        assertThat(testOrganisationUnit.getPath()).isEqualTo(UPDATED_PATH);
        assertThat(testOrganisationUnit.getHierarchyLevel()).isEqualTo(UPDATED_HIERARCHY_LEVEL);
        assertThat(testOrganisationUnit.getOpeningDate()).isEqualTo(UPDATED_OPENING_DATE);
        assertThat(testOrganisationUnit.getComment()).isEqualTo(UPDATED_COMMENT);
        assertThat(testOrganisationUnit.getClosedDate()).isEqualTo(UPDATED_CLOSED_DATE);
        assertThat(testOrganisationUnit.getUrl()).isEqualTo(UPDATED_URL);
        assertThat(testOrganisationUnit.getContactPerson()).isEqualTo(UPDATED_CONTACT_PERSON);
        assertThat(testOrganisationUnit.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testOrganisationUnit.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testOrganisationUnit.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testOrganisationUnit.getOrganisationUnitType()).isEqualTo(UPDATED_ORGANISATION_UNIT_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingOrganisationUnit() throws Exception {
        int databaseSizeBeforeUpdate = organisationUnitRepository.findAll().size();
        organisationUnit.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrganisationUnitMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, organisationUnit.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(organisationUnit))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrganisationUnit in the database
        List<OrganisationUnit> organisationUnitList = organisationUnitRepository.findAll();
        assertThat(organisationUnitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOrganisationUnit() throws Exception {
        int databaseSizeBeforeUpdate = organisationUnitRepository.findAll().size();
        organisationUnit.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrganisationUnitMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(organisationUnit))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrganisationUnit in the database
        List<OrganisationUnit> organisationUnitList = organisationUnitRepository.findAll();
        assertThat(organisationUnitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOrganisationUnit() throws Exception {
        int databaseSizeBeforeUpdate = organisationUnitRepository.findAll().size();
        organisationUnit.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrganisationUnitMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(organisationUnit))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrganisationUnit in the database
        List<OrganisationUnit> organisationUnitList = organisationUnitRepository.findAll();
        assertThat(organisationUnitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOrganisationUnit() throws Exception {
        // Initialize the database
        organisationUnitRepository.saveAndFlush(organisationUnit);

        int databaseSizeBeforeDelete = organisationUnitRepository.findAll().size();

        // Delete the organisationUnit
        restOrganisationUnitMockMvc
            .perform(delete(ENTITY_API_URL_ID, organisationUnit.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<OrganisationUnit> organisationUnitList = organisationUnitRepository.findAll();
        assertThat(organisationUnitList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
