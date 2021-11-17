package org.nmcpye.activitiesmanagement.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.nmcpye.activitiesmanagement.web.rest.TestUtil.sameNumber;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.nmcpye.activitiesmanagement.IntegrationTest;
import org.nmcpye.activitiesmanagement.domain.demographicdata.DemographicData;
import org.nmcpye.activitiesmanagement.domain.demographicdata.DemographicDataSource;
import org.nmcpye.activitiesmanagement.domain.enumeration.DemographicDataLevel;
import org.nmcpye.activitiesmanagement.repository.DemographicDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link DemographicDataResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DemographicDataResourceIT {

    private static final Date DEFAULT_CREATED = Date.from(Instant.ofEpochMilli(0L));
    private static final Date UPDATED_CREATED = Date.from(Instant.now().truncatedTo(ChronoUnit.MILLIS));

    private static final Date DEFAULT_LAST_UPDATED = Date.from(Instant.ofEpochMilli(0L));
    private static final Date UPDATED_LAST_UPDATED = Date.from(Instant.now().truncatedTo(ChronoUnit.MILLIS));

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final DemographicDataLevel DEFAULT_LEVEL = DemographicDataLevel.SUBVILLAGE_LEVEL;
    private static final DemographicDataLevel UPDATED_LEVEL = DemographicDataLevel.DISTRICT_LEVEL;

    private static final Integer DEFAULT_TOTAL_POPULATION = 1;
    private static final Integer UPDATED_TOTAL_POPULATION = 2;

    private static final Integer DEFAULT_MALE_POPULATION = 1;
    private static final Integer UPDATED_MALE_POPULATION = 2;

    private static final Integer DEFAULT_FEMALE_POPULATION = 1;
    private static final Integer UPDATED_FEMALE_POPULATION = 2;

    private static final Integer DEFAULT_LESS_THAN_5_POPULATION = 1;
    private static final Integer UPDATED_LESS_THAN_5_POPULATION = 2;

    private static final Integer DEFAULT_GREATER_THAN_5_POPULATION = 1;
    private static final Integer UPDATED_GREATER_THAN_5_POPULATION = 2;

    private static final Integer DEFAULT_BW_5_AND_15_POPULATION = 1;
    private static final Integer UPDATED_BW_5_AND_15_POPULATION = 2;

    private static final Integer DEFAULT_GREATER_THAN_15_POPULATION = 1;
    private static final Integer UPDATED_GREATER_THAN_15_POPULATION = 2;

    private static final Integer DEFAULT_HOUSEHOLD = 1;
    private static final Integer UPDATED_HOUSEHOLD = 2;

    private static final Integer DEFAULT_HOUSES = 1;
    private static final Integer UPDATED_HOUSES = 2;

    private static final Integer DEFAULT_HEALTH_FACILITIES = 1;
    private static final Integer UPDATED_HEALTH_FACILITIES = 2;

    private static final BigDecimal DEFAULT_AVG_NO_OF_ROOMS = new BigDecimal(1);
    private static final BigDecimal UPDATED_AVG_NO_OF_ROOMS = new BigDecimal(2);

    private static final BigDecimal DEFAULT_AVG_ROOM_AREA = new BigDecimal(1);
    private static final BigDecimal UPDATED_AVG_ROOM_AREA = new BigDecimal(2);

    private static final BigDecimal DEFAULT_AVG_HOUSE_AREA = new BigDecimal(1);
    private static final BigDecimal UPDATED_AVG_HOUSE_AREA = new BigDecimal(2);

    private static final BigDecimal DEFAULT_INDIVIDUALS_PER_HOUSEHOLD = new BigDecimal(1);
    private static final BigDecimal UPDATED_INDIVIDUALS_PER_HOUSEHOLD = new BigDecimal(2);

    private static final BigDecimal DEFAULT_POPULATION_GROWTH_RATE = new BigDecimal(1);
    private static final BigDecimal UPDATED_POPULATION_GROWTH_RATE = new BigDecimal(2);

    private static final String DEFAULT_COMMENT = "AAAAAAAAAA";
    private static final String UPDATED_COMMENT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/demographic-data";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DemographicDataRepository demographicDataRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDemographicDataMockMvc;

    private DemographicData demographicData;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DemographicData createEntity(EntityManager em) {
        DemographicData demographicData = new DemographicData()
            .created(DEFAULT_CREATED)
            .lastUpdated(DEFAULT_LAST_UPDATED)
            .date(DEFAULT_DATE)
            .level(DEFAULT_LEVEL)
            .totalPopulation(DEFAULT_TOTAL_POPULATION)
            .malePopulation(DEFAULT_MALE_POPULATION)
            .femalePopulation(DEFAULT_FEMALE_POPULATION)
            .lessThan5Population(DEFAULT_LESS_THAN_5_POPULATION)
            .greaterThan5Population(DEFAULT_GREATER_THAN_5_POPULATION)
            .bw5And15Population(DEFAULT_BW_5_AND_15_POPULATION)
            .greaterThan15Population(DEFAULT_GREATER_THAN_15_POPULATION)
            .household(DEFAULT_HOUSEHOLD)
            .houses(DEFAULT_HOUSES)
            .healthFacilities(DEFAULT_HEALTH_FACILITIES)
            .avgNoOfRooms(DEFAULT_AVG_NO_OF_ROOMS)
            .avgRoomArea(DEFAULT_AVG_ROOM_AREA)
            .avgHouseArea(DEFAULT_AVG_HOUSE_AREA)
            .individualsPerHousehold(DEFAULT_INDIVIDUALS_PER_HOUSEHOLD)
            .populationGrowthRate(DEFAULT_POPULATION_GROWTH_RATE)
            .comment(DEFAULT_COMMENT);
        // Add required entity
        DemographicDataSource demographicDataSource;
        if (TestUtil.findAll(em, DemographicDataSource.class).isEmpty()) {
            demographicDataSource = DemographicDataSourceResourceIT.createEntity(em);
            em.persist(demographicDataSource);
            em.flush();
        } else {
            demographicDataSource = TestUtil.findAll(em, DemographicDataSource.class).get(0);
        }
        demographicData.setSource(demographicDataSource);
        return demographicData;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DemographicData createUpdatedEntity(EntityManager em) {
        DemographicData demographicData = new DemographicData()
            .created(UPDATED_CREATED)
            .lastUpdated(UPDATED_LAST_UPDATED)
            .date(UPDATED_DATE)
            .level(UPDATED_LEVEL)
            .totalPopulation(UPDATED_TOTAL_POPULATION)
            .malePopulation(UPDATED_MALE_POPULATION)
            .femalePopulation(UPDATED_FEMALE_POPULATION)
            .lessThan5Population(UPDATED_LESS_THAN_5_POPULATION)
            .greaterThan5Population(UPDATED_GREATER_THAN_5_POPULATION)
            .bw5And15Population(UPDATED_BW_5_AND_15_POPULATION)
            .greaterThan15Population(UPDATED_GREATER_THAN_15_POPULATION)
            .household(UPDATED_HOUSEHOLD)
            .houses(UPDATED_HOUSES)
            .healthFacilities(UPDATED_HEALTH_FACILITIES)
            .avgNoOfRooms(UPDATED_AVG_NO_OF_ROOMS)
            .avgRoomArea(UPDATED_AVG_ROOM_AREA)
            .avgHouseArea(UPDATED_AVG_HOUSE_AREA)
            .individualsPerHousehold(UPDATED_INDIVIDUALS_PER_HOUSEHOLD)
            .populationGrowthRate(UPDATED_POPULATION_GROWTH_RATE)
            .comment(UPDATED_COMMENT);
        // Add required entity
        DemographicDataSource demographicDataSource;
        if (TestUtil.findAll(em, DemographicDataSource.class).isEmpty()) {
            demographicDataSource = DemographicDataSourceResourceIT.createUpdatedEntity(em);
            em.persist(demographicDataSource);
            em.flush();
        } else {
            demographicDataSource = TestUtil.findAll(em, DemographicDataSource.class).get(0);
        }
        demographicData.setSource(demographicDataSource);
        return demographicData;
    }

    @BeforeEach
    public void initTest() {
        demographicData = createEntity(em);
    }

    @Test
    @Transactional
    void createDemographicData() throws Exception {
        int databaseSizeBeforeCreate = demographicDataRepository.findAll().size();
        // Create the DemographicData
        restDemographicDataMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(demographicData))
            )
            .andExpect(status().isCreated());

        // Validate the DemographicData in the database
        List<DemographicData> demographicDataList = demographicDataRepository.findAll();
        assertThat(demographicDataList).hasSize(databaseSizeBeforeCreate + 1);
        DemographicData testDemographicData = demographicDataList.get(demographicDataList.size() - 1);
        assertThat(testDemographicData.getCreated()).isEqualTo(DEFAULT_CREATED);
        assertThat(testDemographicData.getLastUpdated()).isEqualTo(DEFAULT_LAST_UPDATED);
        assertThat(testDemographicData.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testDemographicData.getLevel()).isEqualTo(DEFAULT_LEVEL);
        assertThat(testDemographicData.getTotalPopulation()).isEqualTo(DEFAULT_TOTAL_POPULATION);
        assertThat(testDemographicData.getMalePopulation()).isEqualTo(DEFAULT_MALE_POPULATION);
        assertThat(testDemographicData.getFemalePopulation()).isEqualTo(DEFAULT_FEMALE_POPULATION);
        assertThat(testDemographicData.getLessThan5Population()).isEqualTo(DEFAULT_LESS_THAN_5_POPULATION);
        assertThat(testDemographicData.getGreaterThan5Population()).isEqualTo(DEFAULT_GREATER_THAN_5_POPULATION);
        assertThat(testDemographicData.getBw5And15Population()).isEqualTo(DEFAULT_BW_5_AND_15_POPULATION);
        assertThat(testDemographicData.getGreaterThan15Population()).isEqualTo(DEFAULT_GREATER_THAN_15_POPULATION);
        assertThat(testDemographicData.getHousehold()).isEqualTo(DEFAULT_HOUSEHOLD);
        assertThat(testDemographicData.getHouses()).isEqualTo(DEFAULT_HOUSES);
        assertThat(testDemographicData.getHealthFacilities()).isEqualTo(DEFAULT_HEALTH_FACILITIES);
        assertThat(testDemographicData.getAvgNoOfRooms()).isEqualByComparingTo(DEFAULT_AVG_NO_OF_ROOMS);
        assertThat(testDemographicData.getAvgRoomArea()).isEqualByComparingTo(DEFAULT_AVG_ROOM_AREA);
        assertThat(testDemographicData.getAvgHouseArea()).isEqualByComparingTo(DEFAULT_AVG_HOUSE_AREA);
        assertThat(testDemographicData.getIndividualsPerHousehold()).isEqualByComparingTo(DEFAULT_INDIVIDUALS_PER_HOUSEHOLD);
        assertThat(testDemographicData.getPopulationGrowthRate()).isEqualByComparingTo(DEFAULT_POPULATION_GROWTH_RATE);
        assertThat(testDemographicData.getComment()).isEqualTo(DEFAULT_COMMENT);
    }

    @Test
    @Transactional
    void createDemographicDataWithExistingId() throws Exception {
        // Create the DemographicData with an existing ID
        demographicData.setId(1L);

        int databaseSizeBeforeCreate = demographicDataRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDemographicDataMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(demographicData))
            )
            .andExpect(status().isBadRequest());

        // Validate the DemographicData in the database
        List<DemographicData> demographicDataList = demographicDataRepository.findAll();
        assertThat(demographicDataList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = demographicDataRepository.findAll().size();
        // set the field null
        demographicData.setDate(null);

        // Create the DemographicData, which fails.

        restDemographicDataMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(demographicData))
            )
            .andExpect(status().isBadRequest());

        List<DemographicData> demographicDataList = demographicDataRepository.findAll();
        assertThat(demographicDataList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDemographicData() throws Exception {
        // Initialize the database
        demographicDataRepository.saveAndFlush(demographicData);

        // Get all the demographicDataList
        restDemographicDataMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(demographicData.getId().intValue())))
            .andExpect(jsonPath("$.[*].created").value(hasItem(DEFAULT_CREATED.toString())))
            .andExpect(jsonPath("$.[*].lastUpdated").value(hasItem(DEFAULT_LAST_UPDATED.toString())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].level").value(hasItem(DEFAULT_LEVEL.toString())))
            .andExpect(jsonPath("$.[*].totalPopulation").value(hasItem(DEFAULT_TOTAL_POPULATION)))
            .andExpect(jsonPath("$.[*].malePopulation").value(hasItem(DEFAULT_MALE_POPULATION)))
            .andExpect(jsonPath("$.[*].femalePopulation").value(hasItem(DEFAULT_FEMALE_POPULATION)))
            .andExpect(jsonPath("$.[*].lessThan5Population").value(hasItem(DEFAULT_LESS_THAN_5_POPULATION)))
            .andExpect(jsonPath("$.[*].greaterThan5Population").value(hasItem(DEFAULT_GREATER_THAN_5_POPULATION)))
            .andExpect(jsonPath("$.[*].bw5And15Population").value(hasItem(DEFAULT_BW_5_AND_15_POPULATION)))
            .andExpect(jsonPath("$.[*].greaterThan15Population").value(hasItem(DEFAULT_GREATER_THAN_15_POPULATION)))
            .andExpect(jsonPath("$.[*].household").value(hasItem(DEFAULT_HOUSEHOLD)))
            .andExpect(jsonPath("$.[*].houses").value(hasItem(DEFAULT_HOUSES)))
            .andExpect(jsonPath("$.[*].healthFacilities").value(hasItem(DEFAULT_HEALTH_FACILITIES)))
            .andExpect(jsonPath("$.[*].avgNoOfRooms").value(hasItem(sameNumber(DEFAULT_AVG_NO_OF_ROOMS))))
            .andExpect(jsonPath("$.[*].avgRoomArea").value(hasItem(sameNumber(DEFAULT_AVG_ROOM_AREA))))
            .andExpect(jsonPath("$.[*].avgHouseArea").value(hasItem(sameNumber(DEFAULT_AVG_HOUSE_AREA))))
            .andExpect(jsonPath("$.[*].individualsPerHousehold").value(hasItem(sameNumber(DEFAULT_INDIVIDUALS_PER_HOUSEHOLD))))
            .andExpect(jsonPath("$.[*].populationGrowthRate").value(hasItem(sameNumber(DEFAULT_POPULATION_GROWTH_RATE))))
            .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT)));
    }

    @Test
    @Transactional
    void getDemographicData() throws Exception {
        // Initialize the database
        demographicDataRepository.saveAndFlush(demographicData);

        // Get the demographicData
        restDemographicDataMockMvc
            .perform(get(ENTITY_API_URL_ID, demographicData.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(demographicData.getId().intValue()))
            .andExpect(jsonPath("$.created").value(DEFAULT_CREATED.toString()))
            .andExpect(jsonPath("$.lastUpdated").value(DEFAULT_LAST_UPDATED.toString()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.level").value(DEFAULT_LEVEL.toString()))
            .andExpect(jsonPath("$.totalPopulation").value(DEFAULT_TOTAL_POPULATION))
            .andExpect(jsonPath("$.malePopulation").value(DEFAULT_MALE_POPULATION))
            .andExpect(jsonPath("$.femalePopulation").value(DEFAULT_FEMALE_POPULATION))
            .andExpect(jsonPath("$.lessThan5Population").value(DEFAULT_LESS_THAN_5_POPULATION))
            .andExpect(jsonPath("$.greaterThan5Population").value(DEFAULT_GREATER_THAN_5_POPULATION))
            .andExpect(jsonPath("$.bw5And15Population").value(DEFAULT_BW_5_AND_15_POPULATION))
            .andExpect(jsonPath("$.greaterThan15Population").value(DEFAULT_GREATER_THAN_15_POPULATION))
            .andExpect(jsonPath("$.household").value(DEFAULT_HOUSEHOLD))
            .andExpect(jsonPath("$.houses").value(DEFAULT_HOUSES))
            .andExpect(jsonPath("$.healthFacilities").value(DEFAULT_HEALTH_FACILITIES))
            .andExpect(jsonPath("$.avgNoOfRooms").value(sameNumber(DEFAULT_AVG_NO_OF_ROOMS)))
            .andExpect(jsonPath("$.avgRoomArea").value(sameNumber(DEFAULT_AVG_ROOM_AREA)))
            .andExpect(jsonPath("$.avgHouseArea").value(sameNumber(DEFAULT_AVG_HOUSE_AREA)))
            .andExpect(jsonPath("$.individualsPerHousehold").value(sameNumber(DEFAULT_INDIVIDUALS_PER_HOUSEHOLD)))
            .andExpect(jsonPath("$.populationGrowthRate").value(sameNumber(DEFAULT_POPULATION_GROWTH_RATE)))
            .andExpect(jsonPath("$.comment").value(DEFAULT_COMMENT));
    }

    @Test
    @Transactional
    void getNonExistingDemographicData() throws Exception {
        // Get the demographicData
        restDemographicDataMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDemographicData() throws Exception {
        // Initialize the database
        demographicDataRepository.saveAndFlush(demographicData);

        int databaseSizeBeforeUpdate = demographicDataRepository.findAll().size();

        // Update the demographicData
        DemographicData updatedDemographicData = demographicDataRepository.findById(demographicData.getId()).get();
        // Disconnect from session so that the updates on updatedDemographicData are not directly saved in db
        em.detach(updatedDemographicData);
        updatedDemographicData
            .created(UPDATED_CREATED)
            .lastUpdated(UPDATED_LAST_UPDATED)
            .date(UPDATED_DATE)
            .level(UPDATED_LEVEL)
            .totalPopulation(UPDATED_TOTAL_POPULATION)
            .malePopulation(UPDATED_MALE_POPULATION)
            .femalePopulation(UPDATED_FEMALE_POPULATION)
            .lessThan5Population(UPDATED_LESS_THAN_5_POPULATION)
            .greaterThan5Population(UPDATED_GREATER_THAN_5_POPULATION)
            .bw5And15Population(UPDATED_BW_5_AND_15_POPULATION)
            .greaterThan15Population(UPDATED_GREATER_THAN_15_POPULATION)
            .household(UPDATED_HOUSEHOLD)
            .houses(UPDATED_HOUSES)
            .healthFacilities(UPDATED_HEALTH_FACILITIES)
            .avgNoOfRooms(UPDATED_AVG_NO_OF_ROOMS)
            .avgRoomArea(UPDATED_AVG_ROOM_AREA)
            .avgHouseArea(UPDATED_AVG_HOUSE_AREA)
            .individualsPerHousehold(UPDATED_INDIVIDUALS_PER_HOUSEHOLD)
            .populationGrowthRate(UPDATED_POPULATION_GROWTH_RATE)
            .comment(UPDATED_COMMENT);

        restDemographicDataMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDemographicData.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedDemographicData))
            )
            .andExpect(status().isOk());

        // Validate the DemographicData in the database
        List<DemographicData> demographicDataList = demographicDataRepository.findAll();
        assertThat(demographicDataList).hasSize(databaseSizeBeforeUpdate);
        DemographicData testDemographicData = demographicDataList.get(demographicDataList.size() - 1);
        assertThat(testDemographicData.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testDemographicData.getLastUpdated()).isEqualTo(UPDATED_LAST_UPDATED);
        assertThat(testDemographicData.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testDemographicData.getLevel()).isEqualTo(UPDATED_LEVEL);
        assertThat(testDemographicData.getTotalPopulation()).isEqualTo(UPDATED_TOTAL_POPULATION);
        assertThat(testDemographicData.getMalePopulation()).isEqualTo(UPDATED_MALE_POPULATION);
        assertThat(testDemographicData.getFemalePopulation()).isEqualTo(UPDATED_FEMALE_POPULATION);
        assertThat(testDemographicData.getLessThan5Population()).isEqualTo(UPDATED_LESS_THAN_5_POPULATION);
        assertThat(testDemographicData.getGreaterThan5Population()).isEqualTo(UPDATED_GREATER_THAN_5_POPULATION);
        assertThat(testDemographicData.getBw5And15Population()).isEqualTo(UPDATED_BW_5_AND_15_POPULATION);
        assertThat(testDemographicData.getGreaterThan15Population()).isEqualTo(UPDATED_GREATER_THAN_15_POPULATION);
        assertThat(testDemographicData.getHousehold()).isEqualTo(UPDATED_HOUSEHOLD);
        assertThat(testDemographicData.getHouses()).isEqualTo(UPDATED_HOUSES);
        assertThat(testDemographicData.getHealthFacilities()).isEqualTo(UPDATED_HEALTH_FACILITIES);
        assertThat(testDemographicData.getAvgNoOfRooms()).isEqualTo(UPDATED_AVG_NO_OF_ROOMS);
        assertThat(testDemographicData.getAvgRoomArea()).isEqualTo(UPDATED_AVG_ROOM_AREA);
        assertThat(testDemographicData.getAvgHouseArea()).isEqualTo(UPDATED_AVG_HOUSE_AREA);
        assertThat(testDemographicData.getIndividualsPerHousehold()).isEqualTo(UPDATED_INDIVIDUALS_PER_HOUSEHOLD);
        assertThat(testDemographicData.getPopulationGrowthRate()).isEqualTo(UPDATED_POPULATION_GROWTH_RATE);
        assertThat(testDemographicData.getComment()).isEqualTo(UPDATED_COMMENT);
    }

    @Test
    @Transactional
    void putNonExistingDemographicData() throws Exception {
        int databaseSizeBeforeUpdate = demographicDataRepository.findAll().size();
        demographicData.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDemographicDataMockMvc
            .perform(
                put(ENTITY_API_URL_ID, demographicData.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(demographicData))
            )
            .andExpect(status().isBadRequest());

        // Validate the DemographicData in the database
        List<DemographicData> demographicDataList = demographicDataRepository.findAll();
        assertThat(demographicDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDemographicData() throws Exception {
        int databaseSizeBeforeUpdate = demographicDataRepository.findAll().size();
        demographicData.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDemographicDataMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(demographicData))
            )
            .andExpect(status().isBadRequest());

        // Validate the DemographicData in the database
        List<DemographicData> demographicDataList = demographicDataRepository.findAll();
        assertThat(demographicDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDemographicData() throws Exception {
        int databaseSizeBeforeUpdate = demographicDataRepository.findAll().size();
        demographicData.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDemographicDataMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(demographicData))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DemographicData in the database
        List<DemographicData> demographicDataList = demographicDataRepository.findAll();
        assertThat(demographicDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDemographicDataWithPatch() throws Exception {
        // Initialize the database
        demographicDataRepository.saveAndFlush(demographicData);

        int databaseSizeBeforeUpdate = demographicDataRepository.findAll().size();

        // Update the demographicData using partial update
        DemographicData partialUpdatedDemographicData = new DemographicData();
        partialUpdatedDemographicData.setId(demographicData.getId());

        partialUpdatedDemographicData
            .created(UPDATED_CREATED)
            .level(UPDATED_LEVEL)
            .totalPopulation(UPDATED_TOTAL_POPULATION)
            .lessThan5Population(UPDATED_LESS_THAN_5_POPULATION)
            .greaterThan5Population(UPDATED_GREATER_THAN_5_POPULATION)
            .greaterThan15Population(UPDATED_GREATER_THAN_15_POPULATION)
            .household(UPDATED_HOUSEHOLD)
            .houses(UPDATED_HOUSES)
            .healthFacilities(UPDATED_HEALTH_FACILITIES)
            .individualsPerHousehold(UPDATED_INDIVIDUALS_PER_HOUSEHOLD)
            .populationGrowthRate(UPDATED_POPULATION_GROWTH_RATE)
            .comment(UPDATED_COMMENT);

        restDemographicDataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDemographicData.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDemographicData))
            )
            .andExpect(status().isOk());

        // Validate the DemographicData in the database
        List<DemographicData> demographicDataList = demographicDataRepository.findAll();
        assertThat(demographicDataList).hasSize(databaseSizeBeforeUpdate);
        DemographicData testDemographicData = demographicDataList.get(demographicDataList.size() - 1);
        assertThat(testDemographicData.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testDemographicData.getLastUpdated()).isEqualTo(DEFAULT_LAST_UPDATED);
        assertThat(testDemographicData.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testDemographicData.getLevel()).isEqualTo(UPDATED_LEVEL);
        assertThat(testDemographicData.getTotalPopulation()).isEqualTo(UPDATED_TOTAL_POPULATION);
        assertThat(testDemographicData.getMalePopulation()).isEqualTo(DEFAULT_MALE_POPULATION);
        assertThat(testDemographicData.getFemalePopulation()).isEqualTo(DEFAULT_FEMALE_POPULATION);
        assertThat(testDemographicData.getLessThan5Population()).isEqualTo(UPDATED_LESS_THAN_5_POPULATION);
        assertThat(testDemographicData.getGreaterThan5Population()).isEqualTo(UPDATED_GREATER_THAN_5_POPULATION);
        assertThat(testDemographicData.getBw5And15Population()).isEqualTo(DEFAULT_BW_5_AND_15_POPULATION);
        assertThat(testDemographicData.getGreaterThan15Population()).isEqualTo(UPDATED_GREATER_THAN_15_POPULATION);
        assertThat(testDemographicData.getHousehold()).isEqualTo(UPDATED_HOUSEHOLD);
        assertThat(testDemographicData.getHouses()).isEqualTo(UPDATED_HOUSES);
        assertThat(testDemographicData.getHealthFacilities()).isEqualTo(UPDATED_HEALTH_FACILITIES);
        assertThat(testDemographicData.getAvgNoOfRooms()).isEqualByComparingTo(DEFAULT_AVG_NO_OF_ROOMS);
        assertThat(testDemographicData.getAvgRoomArea()).isEqualByComparingTo(DEFAULT_AVG_ROOM_AREA);
        assertThat(testDemographicData.getAvgHouseArea()).isEqualByComparingTo(DEFAULT_AVG_HOUSE_AREA);
        assertThat(testDemographicData.getIndividualsPerHousehold()).isEqualByComparingTo(UPDATED_INDIVIDUALS_PER_HOUSEHOLD);
        assertThat(testDemographicData.getPopulationGrowthRate()).isEqualByComparingTo(UPDATED_POPULATION_GROWTH_RATE);
        assertThat(testDemographicData.getComment()).isEqualTo(UPDATED_COMMENT);
    }

    @Test
    @Transactional
    void fullUpdateDemographicDataWithPatch() throws Exception {
        // Initialize the database
        demographicDataRepository.saveAndFlush(demographicData);

        int databaseSizeBeforeUpdate = demographicDataRepository.findAll().size();

        // Update the demographicData using partial update
        DemographicData partialUpdatedDemographicData = new DemographicData();
        partialUpdatedDemographicData.setId(demographicData.getId());

        partialUpdatedDemographicData
            .created(UPDATED_CREATED)
            .lastUpdated(UPDATED_LAST_UPDATED)
            .date(UPDATED_DATE)
            .level(UPDATED_LEVEL)
            .totalPopulation(UPDATED_TOTAL_POPULATION)
            .malePopulation(UPDATED_MALE_POPULATION)
            .femalePopulation(UPDATED_FEMALE_POPULATION)
            .lessThan5Population(UPDATED_LESS_THAN_5_POPULATION)
            .greaterThan5Population(UPDATED_GREATER_THAN_5_POPULATION)
            .bw5And15Population(UPDATED_BW_5_AND_15_POPULATION)
            .greaterThan15Population(UPDATED_GREATER_THAN_15_POPULATION)
            .household(UPDATED_HOUSEHOLD)
            .houses(UPDATED_HOUSES)
            .healthFacilities(UPDATED_HEALTH_FACILITIES)
            .avgNoOfRooms(UPDATED_AVG_NO_OF_ROOMS)
            .avgRoomArea(UPDATED_AVG_ROOM_AREA)
            .avgHouseArea(UPDATED_AVG_HOUSE_AREA)
            .individualsPerHousehold(UPDATED_INDIVIDUALS_PER_HOUSEHOLD)
            .populationGrowthRate(UPDATED_POPULATION_GROWTH_RATE)
            .comment(UPDATED_COMMENT);

        restDemographicDataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDemographicData.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDemographicData))
            )
            .andExpect(status().isOk());

        // Validate the DemographicData in the database
        List<DemographicData> demographicDataList = demographicDataRepository.findAll();
        assertThat(demographicDataList).hasSize(databaseSizeBeforeUpdate);
        DemographicData testDemographicData = demographicDataList.get(demographicDataList.size() - 1);
        assertThat(testDemographicData.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testDemographicData.getLastUpdated()).isEqualTo(UPDATED_LAST_UPDATED);
        assertThat(testDemographicData.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testDemographicData.getLevel()).isEqualTo(UPDATED_LEVEL);
        assertThat(testDemographicData.getTotalPopulation()).isEqualTo(UPDATED_TOTAL_POPULATION);
        assertThat(testDemographicData.getMalePopulation()).isEqualTo(UPDATED_MALE_POPULATION);
        assertThat(testDemographicData.getFemalePopulation()).isEqualTo(UPDATED_FEMALE_POPULATION);
        assertThat(testDemographicData.getLessThan5Population()).isEqualTo(UPDATED_LESS_THAN_5_POPULATION);
        assertThat(testDemographicData.getGreaterThan5Population()).isEqualTo(UPDATED_GREATER_THAN_5_POPULATION);
        assertThat(testDemographicData.getBw5And15Population()).isEqualTo(UPDATED_BW_5_AND_15_POPULATION);
        assertThat(testDemographicData.getGreaterThan15Population()).isEqualTo(UPDATED_GREATER_THAN_15_POPULATION);
        assertThat(testDemographicData.getHousehold()).isEqualTo(UPDATED_HOUSEHOLD);
        assertThat(testDemographicData.getHouses()).isEqualTo(UPDATED_HOUSES);
        assertThat(testDemographicData.getHealthFacilities()).isEqualTo(UPDATED_HEALTH_FACILITIES);
        assertThat(testDemographicData.getAvgNoOfRooms()).isEqualByComparingTo(UPDATED_AVG_NO_OF_ROOMS);
        assertThat(testDemographicData.getAvgRoomArea()).isEqualByComparingTo(UPDATED_AVG_ROOM_AREA);
        assertThat(testDemographicData.getAvgHouseArea()).isEqualByComparingTo(UPDATED_AVG_HOUSE_AREA);
        assertThat(testDemographicData.getIndividualsPerHousehold()).isEqualByComparingTo(UPDATED_INDIVIDUALS_PER_HOUSEHOLD);
        assertThat(testDemographicData.getPopulationGrowthRate()).isEqualByComparingTo(UPDATED_POPULATION_GROWTH_RATE);
        assertThat(testDemographicData.getComment()).isEqualTo(UPDATED_COMMENT);
    }

    @Test
    @Transactional
    void patchNonExistingDemographicData() throws Exception {
        int databaseSizeBeforeUpdate = demographicDataRepository.findAll().size();
        demographicData.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDemographicDataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, demographicData.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(demographicData))
            )
            .andExpect(status().isBadRequest());

        // Validate the DemographicData in the database
        List<DemographicData> demographicDataList = demographicDataRepository.findAll();
        assertThat(demographicDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDemographicData() throws Exception {
        int databaseSizeBeforeUpdate = demographicDataRepository.findAll().size();
        demographicData.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDemographicDataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(demographicData))
            )
            .andExpect(status().isBadRequest());

        // Validate the DemographicData in the database
        List<DemographicData> demographicDataList = demographicDataRepository.findAll();
        assertThat(demographicDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDemographicData() throws Exception {
        int databaseSizeBeforeUpdate = demographicDataRepository.findAll().size();
        demographicData.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDemographicDataMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(demographicData))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DemographicData in the database
        List<DemographicData> demographicDataList = demographicDataRepository.findAll();
        assertThat(demographicDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDemographicData() throws Exception {
        // Initialize the database
        demographicDataRepository.saveAndFlush(demographicData);

        int databaseSizeBeforeDelete = demographicDataRepository.findAll().size();

        // Delete the demographicData
        restDemographicDataMockMvc
            .perform(delete(ENTITY_API_URL_ID, demographicData.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DemographicData> demographicDataList = demographicDataRepository.findAll();
        assertThat(demographicDataList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
