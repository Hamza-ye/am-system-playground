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
import org.nmcpye.activitiesmanagement.domain.RelativePeriods;
import org.nmcpye.activitiesmanagement.repository.RelativePeriodsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link RelativePeriodsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RelativePeriodsResourceIT {

    private static final Boolean DEFAULT_THIS_DAY = false;
    private static final Boolean UPDATED_THIS_DAY = true;

    private static final Boolean DEFAULT_YESTERDAY = false;
    private static final Boolean UPDATED_YESTERDAY = true;

    private static final Boolean DEFAULT_LAST_3_DAYS = false;
    private static final Boolean UPDATED_LAST_3_DAYS = true;

    private static final Boolean DEFAULT_LAST_7_DAYS = false;
    private static final Boolean UPDATED_LAST_7_DAYS = true;

    private static final Boolean DEFAULT_LAST_14_DAYS = false;
    private static final Boolean UPDATED_LAST_14_DAYS = true;

    private static final Boolean DEFAULT_THIS_MONTH = false;
    private static final Boolean UPDATED_THIS_MONTH = true;

    private static final Boolean DEFAULT_LAST_MONTH = false;
    private static final Boolean UPDATED_LAST_MONTH = true;

    private static final Boolean DEFAULT_THIS_BIMONTH = false;
    private static final Boolean UPDATED_THIS_BIMONTH = true;

    private static final Boolean DEFAULT_LAST_BIMONTH = false;
    private static final Boolean UPDATED_LAST_BIMONTH = true;

    private static final Boolean DEFAULT_THIS_QUARTER = false;
    private static final Boolean UPDATED_THIS_QUARTER = true;

    private static final Boolean DEFAULT_LAST_QUARTER = false;
    private static final Boolean UPDATED_LAST_QUARTER = true;

    private static final Boolean DEFAULT_THIS_SIX_MONTH = false;
    private static final Boolean UPDATED_THIS_SIX_MONTH = true;

    private static final Boolean DEFAULT_LAST_SIX_MONTH = false;
    private static final Boolean UPDATED_LAST_SIX_MONTH = true;

    private static final Boolean DEFAULT_WEEKS_THIS_YEAR = false;
    private static final Boolean UPDATED_WEEKS_THIS_YEAR = true;

    private static final Boolean DEFAULT_MONTHS_THIS_YEAR = false;
    private static final Boolean UPDATED_MONTHS_THIS_YEAR = true;

    private static final Boolean DEFAULT_BI_MONTHS_THIS_YEAR = false;
    private static final Boolean UPDATED_BI_MONTHS_THIS_YEAR = true;

    private static final Boolean DEFAULT_QUARTERS_THIS_YEAR = false;
    private static final Boolean UPDATED_QUARTERS_THIS_YEAR = true;

    private static final Boolean DEFAULT_THIS_YEAR = false;
    private static final Boolean UPDATED_THIS_YEAR = true;

    private static final Boolean DEFAULT_MONTHS_LAST_YEAR = false;
    private static final Boolean UPDATED_MONTHS_LAST_YEAR = true;

    private static final Boolean DEFAULT_QUARTERS_LAST_YEAR = false;
    private static final Boolean UPDATED_QUARTERS_LAST_YEAR = true;

    private static final Boolean DEFAULT_LAST_YEAR = false;
    private static final Boolean UPDATED_LAST_YEAR = true;

    private static final Boolean DEFAULT_LAST_5_YEARS = false;
    private static final Boolean UPDATED_LAST_5_YEARS = true;

    private static final Boolean DEFAULT_LAST_12_MONTHS = false;
    private static final Boolean UPDATED_LAST_12_MONTHS = true;

    private static final Boolean DEFAULT_LAST_6_MONTHS = false;
    private static final Boolean UPDATED_LAST_6_MONTHS = true;

    private static final Boolean DEFAULT_LAST_3_MONTHS = false;
    private static final Boolean UPDATED_LAST_3_MONTHS = true;

    private static final Boolean DEFAULT_LAST_6_BI_MONTHS = false;
    private static final Boolean UPDATED_LAST_6_BI_MONTHS = true;

    private static final Boolean DEFAULT_LAST_4_QUARTERS = false;
    private static final Boolean UPDATED_LAST_4_QUARTERS = true;

    private static final Boolean DEFAULT_LAST_2_SIX_MONTHS = false;
    private static final Boolean UPDATED_LAST_2_SIX_MONTHS = true;

    private static final Boolean DEFAULT_THIS_FINANCIAL_YEAR = false;
    private static final Boolean UPDATED_THIS_FINANCIAL_YEAR = true;

    private static final Boolean DEFAULT_LAST_FINANCIAL_YEAR = false;
    private static final Boolean UPDATED_LAST_FINANCIAL_YEAR = true;

    private static final Boolean DEFAULT_LAST_5_FINANCIAL_YEARS = false;
    private static final Boolean UPDATED_LAST_5_FINANCIAL_YEARS = true;

    private static final Boolean DEFAULT_THIS_WEEK = false;
    private static final Boolean UPDATED_THIS_WEEK = true;

    private static final Boolean DEFAULT_LAST_WEEK = false;
    private static final Boolean UPDATED_LAST_WEEK = true;

    private static final Boolean DEFAULT_THIS_BI_WEEK = false;
    private static final Boolean UPDATED_THIS_BI_WEEK = true;

    private static final Boolean DEFAULT_LAST_BI_WEEK = false;
    private static final Boolean UPDATED_LAST_BI_WEEK = true;

    private static final Boolean DEFAULT_LAST_4_WEEKS = false;
    private static final Boolean UPDATED_LAST_4_WEEKS = true;

    private static final Boolean DEFAULT_LAST_4_BI_WEEKS = false;
    private static final Boolean UPDATED_LAST_4_BI_WEEKS = true;

    private static final Boolean DEFAULT_LAST_12_WEEKS = false;
    private static final Boolean UPDATED_LAST_12_WEEKS = true;

    private static final Boolean DEFAULT_LAST_52_WEEKS = false;
    private static final Boolean UPDATED_LAST_52_WEEKS = true;

    private static final String ENTITY_API_URL = "/api/relative-periods";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RelativePeriodsRepository relativePeriodsRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRelativePeriodsMockMvc;

    private RelativePeriods relativePeriods;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RelativePeriods createEntity(EntityManager em) {
        RelativePeriods relativePeriods = new RelativePeriods()
            .thisDay(DEFAULT_THIS_DAY)
            .yesterday(DEFAULT_YESTERDAY)
            .last3Days(DEFAULT_LAST_3_DAYS)
            .last7Days(DEFAULT_LAST_7_DAYS)
            .last14Days(DEFAULT_LAST_14_DAYS)
            .thisMonth(DEFAULT_THIS_MONTH)
            .lastMonth(DEFAULT_LAST_MONTH)
            .thisBimonth(DEFAULT_THIS_BIMONTH)
            .lastBimonth(DEFAULT_LAST_BIMONTH)
            .thisQuarter(DEFAULT_THIS_QUARTER)
            .lastQuarter(DEFAULT_LAST_QUARTER)
            .thisSixMonth(DEFAULT_THIS_SIX_MONTH)
            .lastSixMonth(DEFAULT_LAST_SIX_MONTH)
            .weeksThisYear(DEFAULT_WEEKS_THIS_YEAR)
            .monthsThisYear(DEFAULT_MONTHS_THIS_YEAR)
            .biMonthsThisYear(DEFAULT_BI_MONTHS_THIS_YEAR)
            .quartersThisYear(DEFAULT_QUARTERS_THIS_YEAR)
            .thisYear(DEFAULT_THIS_YEAR)
            .monthsLastYear(DEFAULT_MONTHS_LAST_YEAR)
            .quartersLastYear(DEFAULT_QUARTERS_LAST_YEAR)
            .lastYear(DEFAULT_LAST_YEAR)
            .last5Years(DEFAULT_LAST_5_YEARS)
            .last12Months(DEFAULT_LAST_12_MONTHS)
            .last6Months(DEFAULT_LAST_6_MONTHS)
            .last3Months(DEFAULT_LAST_3_MONTHS)
            .last6BiMonths(DEFAULT_LAST_6_BI_MONTHS)
            .last4Quarters(DEFAULT_LAST_4_QUARTERS)
            .last2SixMonths(DEFAULT_LAST_2_SIX_MONTHS)
            .thisFinancialYear(DEFAULT_THIS_FINANCIAL_YEAR)
            .lastFinancialYear(DEFAULT_LAST_FINANCIAL_YEAR)
            .last5FinancialYears(DEFAULT_LAST_5_FINANCIAL_YEARS)
            .thisWeek(DEFAULT_THIS_WEEK)
            .lastWeek(DEFAULT_LAST_WEEK)
            .thisBiWeek(DEFAULT_THIS_BI_WEEK)
            .lastBiWeek(DEFAULT_LAST_BI_WEEK)
            .last4Weeks(DEFAULT_LAST_4_WEEKS)
            .last4BiWeeks(DEFAULT_LAST_4_BI_WEEKS)
            .last12Weeks(DEFAULT_LAST_12_WEEKS)
            .last52Weeks(DEFAULT_LAST_52_WEEKS);
        return relativePeriods;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RelativePeriods createUpdatedEntity(EntityManager em) {
        RelativePeriods relativePeriods = new RelativePeriods()
            .thisDay(UPDATED_THIS_DAY)
            .yesterday(UPDATED_YESTERDAY)
            .last3Days(UPDATED_LAST_3_DAYS)
            .last7Days(UPDATED_LAST_7_DAYS)
            .last14Days(UPDATED_LAST_14_DAYS)
            .thisMonth(UPDATED_THIS_MONTH)
            .lastMonth(UPDATED_LAST_MONTH)
            .thisBimonth(UPDATED_THIS_BIMONTH)
            .lastBimonth(UPDATED_LAST_BIMONTH)
            .thisQuarter(UPDATED_THIS_QUARTER)
            .lastQuarter(UPDATED_LAST_QUARTER)
            .thisSixMonth(UPDATED_THIS_SIX_MONTH)
            .lastSixMonth(UPDATED_LAST_SIX_MONTH)
            .weeksThisYear(UPDATED_WEEKS_THIS_YEAR)
            .monthsThisYear(UPDATED_MONTHS_THIS_YEAR)
            .biMonthsThisYear(UPDATED_BI_MONTHS_THIS_YEAR)
            .quartersThisYear(UPDATED_QUARTERS_THIS_YEAR)
            .thisYear(UPDATED_THIS_YEAR)
            .monthsLastYear(UPDATED_MONTHS_LAST_YEAR)
            .quartersLastYear(UPDATED_QUARTERS_LAST_YEAR)
            .lastYear(UPDATED_LAST_YEAR)
            .last5Years(UPDATED_LAST_5_YEARS)
            .last12Months(UPDATED_LAST_12_MONTHS)
            .last6Months(UPDATED_LAST_6_MONTHS)
            .last3Months(UPDATED_LAST_3_MONTHS)
            .last6BiMonths(UPDATED_LAST_6_BI_MONTHS)
            .last4Quarters(UPDATED_LAST_4_QUARTERS)
            .last2SixMonths(UPDATED_LAST_2_SIX_MONTHS)
            .thisFinancialYear(UPDATED_THIS_FINANCIAL_YEAR)
            .lastFinancialYear(UPDATED_LAST_FINANCIAL_YEAR)
            .last5FinancialYears(UPDATED_LAST_5_FINANCIAL_YEARS)
            .thisWeek(UPDATED_THIS_WEEK)
            .lastWeek(UPDATED_LAST_WEEK)
            .thisBiWeek(UPDATED_THIS_BI_WEEK)
            .lastBiWeek(UPDATED_LAST_BI_WEEK)
            .last4Weeks(UPDATED_LAST_4_WEEKS)
            .last4BiWeeks(UPDATED_LAST_4_BI_WEEKS)
            .last12Weeks(UPDATED_LAST_12_WEEKS)
            .last52Weeks(UPDATED_LAST_52_WEEKS);
        return relativePeriods;
    }

    @BeforeEach
    public void initTest() {
        relativePeriods = createEntity(em);
    }

    @Test
    @Transactional
    void createRelativePeriods() throws Exception {
        int databaseSizeBeforeCreate = relativePeriodsRepository.findAll().size();
        // Create the RelativePeriods
        restRelativePeriodsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(relativePeriods))
            )
            .andExpect(status().isCreated());

        // Validate the RelativePeriods in the database
        List<RelativePeriods> relativePeriodsList = relativePeriodsRepository.findAll();
        assertThat(relativePeriodsList).hasSize(databaseSizeBeforeCreate + 1);
        RelativePeriods testRelativePeriods = relativePeriodsList.get(relativePeriodsList.size() - 1);
        assertThat(testRelativePeriods.getThisDay()).isEqualTo(DEFAULT_THIS_DAY);
        assertThat(testRelativePeriods.getYesterday()).isEqualTo(DEFAULT_YESTERDAY);
        assertThat(testRelativePeriods.getLast3Days()).isEqualTo(DEFAULT_LAST_3_DAYS);
        assertThat(testRelativePeriods.getLast7Days()).isEqualTo(DEFAULT_LAST_7_DAYS);
        assertThat(testRelativePeriods.getLast14Days()).isEqualTo(DEFAULT_LAST_14_DAYS);
        assertThat(testRelativePeriods.getThisMonth()).isEqualTo(DEFAULT_THIS_MONTH);
        assertThat(testRelativePeriods.getLastMonth()).isEqualTo(DEFAULT_LAST_MONTH);
        assertThat(testRelativePeriods.getThisBimonth()).isEqualTo(DEFAULT_THIS_BIMONTH);
        assertThat(testRelativePeriods.getLastBimonth()).isEqualTo(DEFAULT_LAST_BIMONTH);
        assertThat(testRelativePeriods.getThisQuarter()).isEqualTo(DEFAULT_THIS_QUARTER);
        assertThat(testRelativePeriods.getLastQuarter()).isEqualTo(DEFAULT_LAST_QUARTER);
        assertThat(testRelativePeriods.getThisSixMonth()).isEqualTo(DEFAULT_THIS_SIX_MONTH);
        assertThat(testRelativePeriods.getLastSixMonth()).isEqualTo(DEFAULT_LAST_SIX_MONTH);
        assertThat(testRelativePeriods.getWeeksThisYear()).isEqualTo(DEFAULT_WEEKS_THIS_YEAR);
        assertThat(testRelativePeriods.getMonthsThisYear()).isEqualTo(DEFAULT_MONTHS_THIS_YEAR);
        assertThat(testRelativePeriods.getBiMonthsThisYear()).isEqualTo(DEFAULT_BI_MONTHS_THIS_YEAR);
        assertThat(testRelativePeriods.getQuartersThisYear()).isEqualTo(DEFAULT_QUARTERS_THIS_YEAR);
        assertThat(testRelativePeriods.getThisYear()).isEqualTo(DEFAULT_THIS_YEAR);
        assertThat(testRelativePeriods.getMonthsLastYear()).isEqualTo(DEFAULT_MONTHS_LAST_YEAR);
        assertThat(testRelativePeriods.getQuartersLastYear()).isEqualTo(DEFAULT_QUARTERS_LAST_YEAR);
        assertThat(testRelativePeriods.getLastYear()).isEqualTo(DEFAULT_LAST_YEAR);
        assertThat(testRelativePeriods.getLast5Years()).isEqualTo(DEFAULT_LAST_5_YEARS);
        assertThat(testRelativePeriods.getLast12Months()).isEqualTo(DEFAULT_LAST_12_MONTHS);
        assertThat(testRelativePeriods.getLast6Months()).isEqualTo(DEFAULT_LAST_6_MONTHS);
        assertThat(testRelativePeriods.getLast3Months()).isEqualTo(DEFAULT_LAST_3_MONTHS);
        assertThat(testRelativePeriods.getLast6BiMonths()).isEqualTo(DEFAULT_LAST_6_BI_MONTHS);
        assertThat(testRelativePeriods.getLast4Quarters()).isEqualTo(DEFAULT_LAST_4_QUARTERS);
        assertThat(testRelativePeriods.getLast2SixMonths()).isEqualTo(DEFAULT_LAST_2_SIX_MONTHS);
        assertThat(testRelativePeriods.getThisFinancialYear()).isEqualTo(DEFAULT_THIS_FINANCIAL_YEAR);
        assertThat(testRelativePeriods.getLastFinancialYear()).isEqualTo(DEFAULT_LAST_FINANCIAL_YEAR);
        assertThat(testRelativePeriods.getLast5FinancialYears()).isEqualTo(DEFAULT_LAST_5_FINANCIAL_YEARS);
        assertThat(testRelativePeriods.getThisWeek()).isEqualTo(DEFAULT_THIS_WEEK);
        assertThat(testRelativePeriods.getLastWeek()).isEqualTo(DEFAULT_LAST_WEEK);
        assertThat(testRelativePeriods.getThisBiWeek()).isEqualTo(DEFAULT_THIS_BI_WEEK);
        assertThat(testRelativePeriods.getLastBiWeek()).isEqualTo(DEFAULT_LAST_BI_WEEK);
        assertThat(testRelativePeriods.getLast4Weeks()).isEqualTo(DEFAULT_LAST_4_WEEKS);
        assertThat(testRelativePeriods.getLast4BiWeeks()).isEqualTo(DEFAULT_LAST_4_BI_WEEKS);
        assertThat(testRelativePeriods.getLast12Weeks()).isEqualTo(DEFAULT_LAST_12_WEEKS);
        assertThat(testRelativePeriods.getLast52Weeks()).isEqualTo(DEFAULT_LAST_52_WEEKS);
    }

    @Test
    @Transactional
    void createRelativePeriodsWithExistingId() throws Exception {
        // Create the RelativePeriods with an existing ID
        relativePeriods.setId(1L);

        int databaseSizeBeforeCreate = relativePeriodsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRelativePeriodsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(relativePeriods))
            )
            .andExpect(status().isBadRequest());

        // Validate the RelativePeriods in the database
        List<RelativePeriods> relativePeriodsList = relativePeriodsRepository.findAll();
        assertThat(relativePeriodsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllRelativePeriods() throws Exception {
        // Initialize the database
        relativePeriodsRepository.saveAndFlush(relativePeriods);

        // Get all the relativePeriodsList
        restRelativePeriodsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(relativePeriods.getId().intValue())))
            .andExpect(jsonPath("$.[*].thisDay").value(hasItem(DEFAULT_THIS_DAY.booleanValue())))
            .andExpect(jsonPath("$.[*].yesterday").value(hasItem(DEFAULT_YESTERDAY.booleanValue())))
            .andExpect(jsonPath("$.[*].last3Days").value(hasItem(DEFAULT_LAST_3_DAYS.booleanValue())))
            .andExpect(jsonPath("$.[*].last7Days").value(hasItem(DEFAULT_LAST_7_DAYS.booleanValue())))
            .andExpect(jsonPath("$.[*].last14Days").value(hasItem(DEFAULT_LAST_14_DAYS.booleanValue())))
            .andExpect(jsonPath("$.[*].thisMonth").value(hasItem(DEFAULT_THIS_MONTH.booleanValue())))
            .andExpect(jsonPath("$.[*].lastMonth").value(hasItem(DEFAULT_LAST_MONTH.booleanValue())))
            .andExpect(jsonPath("$.[*].thisBimonth").value(hasItem(DEFAULT_THIS_BIMONTH.booleanValue())))
            .andExpect(jsonPath("$.[*].lastBimonth").value(hasItem(DEFAULT_LAST_BIMONTH.booleanValue())))
            .andExpect(jsonPath("$.[*].thisQuarter").value(hasItem(DEFAULT_THIS_QUARTER.booleanValue())))
            .andExpect(jsonPath("$.[*].lastQuarter").value(hasItem(DEFAULT_LAST_QUARTER.booleanValue())))
            .andExpect(jsonPath("$.[*].thisSixMonth").value(hasItem(DEFAULT_THIS_SIX_MONTH.booleanValue())))
            .andExpect(jsonPath("$.[*].lastSixMonth").value(hasItem(DEFAULT_LAST_SIX_MONTH.booleanValue())))
            .andExpect(jsonPath("$.[*].weeksThisYear").value(hasItem(DEFAULT_WEEKS_THIS_YEAR.booleanValue())))
            .andExpect(jsonPath("$.[*].monthsThisYear").value(hasItem(DEFAULT_MONTHS_THIS_YEAR.booleanValue())))
            .andExpect(jsonPath("$.[*].biMonthsThisYear").value(hasItem(DEFAULT_BI_MONTHS_THIS_YEAR.booleanValue())))
            .andExpect(jsonPath("$.[*].quartersThisYear").value(hasItem(DEFAULT_QUARTERS_THIS_YEAR.booleanValue())))
            .andExpect(jsonPath("$.[*].thisYear").value(hasItem(DEFAULT_THIS_YEAR.booleanValue())))
            .andExpect(jsonPath("$.[*].monthsLastYear").value(hasItem(DEFAULT_MONTHS_LAST_YEAR.booleanValue())))
            .andExpect(jsonPath("$.[*].quartersLastYear").value(hasItem(DEFAULT_QUARTERS_LAST_YEAR.booleanValue())))
            .andExpect(jsonPath("$.[*].lastYear").value(hasItem(DEFAULT_LAST_YEAR.booleanValue())))
            .andExpect(jsonPath("$.[*].last5Years").value(hasItem(DEFAULT_LAST_5_YEARS.booleanValue())))
            .andExpect(jsonPath("$.[*].last12Months").value(hasItem(DEFAULT_LAST_12_MONTHS.booleanValue())))
            .andExpect(jsonPath("$.[*].last6Months").value(hasItem(DEFAULT_LAST_6_MONTHS.booleanValue())))
            .andExpect(jsonPath("$.[*].last3Months").value(hasItem(DEFAULT_LAST_3_MONTHS.booleanValue())))
            .andExpect(jsonPath("$.[*].last6BiMonths").value(hasItem(DEFAULT_LAST_6_BI_MONTHS.booleanValue())))
            .andExpect(jsonPath("$.[*].last4Quarters").value(hasItem(DEFAULT_LAST_4_QUARTERS.booleanValue())))
            .andExpect(jsonPath("$.[*].last2SixMonths").value(hasItem(DEFAULT_LAST_2_SIX_MONTHS.booleanValue())))
            .andExpect(jsonPath("$.[*].thisFinancialYear").value(hasItem(DEFAULT_THIS_FINANCIAL_YEAR.booleanValue())))
            .andExpect(jsonPath("$.[*].lastFinancialYear").value(hasItem(DEFAULT_LAST_FINANCIAL_YEAR.booleanValue())))
            .andExpect(jsonPath("$.[*].last5FinancialYears").value(hasItem(DEFAULT_LAST_5_FINANCIAL_YEARS.booleanValue())))
            .andExpect(jsonPath("$.[*].thisWeek").value(hasItem(DEFAULT_THIS_WEEK.booleanValue())))
            .andExpect(jsonPath("$.[*].lastWeek").value(hasItem(DEFAULT_LAST_WEEK.booleanValue())))
            .andExpect(jsonPath("$.[*].thisBiWeek").value(hasItem(DEFAULT_THIS_BI_WEEK.booleanValue())))
            .andExpect(jsonPath("$.[*].lastBiWeek").value(hasItem(DEFAULT_LAST_BI_WEEK.booleanValue())))
            .andExpect(jsonPath("$.[*].last4Weeks").value(hasItem(DEFAULT_LAST_4_WEEKS.booleanValue())))
            .andExpect(jsonPath("$.[*].last4BiWeeks").value(hasItem(DEFAULT_LAST_4_BI_WEEKS.booleanValue())))
            .andExpect(jsonPath("$.[*].last12Weeks").value(hasItem(DEFAULT_LAST_12_WEEKS.booleanValue())))
            .andExpect(jsonPath("$.[*].last52Weeks").value(hasItem(DEFAULT_LAST_52_WEEKS.booleanValue())));
    }

    @Test
    @Transactional
    void getRelativePeriods() throws Exception {
        // Initialize the database
        relativePeriodsRepository.saveAndFlush(relativePeriods);

        // Get the relativePeriods
        restRelativePeriodsMockMvc
            .perform(get(ENTITY_API_URL_ID, relativePeriods.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(relativePeriods.getId().intValue()))
            .andExpect(jsonPath("$.thisDay").value(DEFAULT_THIS_DAY.booleanValue()))
            .andExpect(jsonPath("$.yesterday").value(DEFAULT_YESTERDAY.booleanValue()))
            .andExpect(jsonPath("$.last3Days").value(DEFAULT_LAST_3_DAYS.booleanValue()))
            .andExpect(jsonPath("$.last7Days").value(DEFAULT_LAST_7_DAYS.booleanValue()))
            .andExpect(jsonPath("$.last14Days").value(DEFAULT_LAST_14_DAYS.booleanValue()))
            .andExpect(jsonPath("$.thisMonth").value(DEFAULT_THIS_MONTH.booleanValue()))
            .andExpect(jsonPath("$.lastMonth").value(DEFAULT_LAST_MONTH.booleanValue()))
            .andExpect(jsonPath("$.thisBimonth").value(DEFAULT_THIS_BIMONTH.booleanValue()))
            .andExpect(jsonPath("$.lastBimonth").value(DEFAULT_LAST_BIMONTH.booleanValue()))
            .andExpect(jsonPath("$.thisQuarter").value(DEFAULT_THIS_QUARTER.booleanValue()))
            .andExpect(jsonPath("$.lastQuarter").value(DEFAULT_LAST_QUARTER.booleanValue()))
            .andExpect(jsonPath("$.thisSixMonth").value(DEFAULT_THIS_SIX_MONTH.booleanValue()))
            .andExpect(jsonPath("$.lastSixMonth").value(DEFAULT_LAST_SIX_MONTH.booleanValue()))
            .andExpect(jsonPath("$.weeksThisYear").value(DEFAULT_WEEKS_THIS_YEAR.booleanValue()))
            .andExpect(jsonPath("$.monthsThisYear").value(DEFAULT_MONTHS_THIS_YEAR.booleanValue()))
            .andExpect(jsonPath("$.biMonthsThisYear").value(DEFAULT_BI_MONTHS_THIS_YEAR.booleanValue()))
            .andExpect(jsonPath("$.quartersThisYear").value(DEFAULT_QUARTERS_THIS_YEAR.booleanValue()))
            .andExpect(jsonPath("$.thisYear").value(DEFAULT_THIS_YEAR.booleanValue()))
            .andExpect(jsonPath("$.monthsLastYear").value(DEFAULT_MONTHS_LAST_YEAR.booleanValue()))
            .andExpect(jsonPath("$.quartersLastYear").value(DEFAULT_QUARTERS_LAST_YEAR.booleanValue()))
            .andExpect(jsonPath("$.lastYear").value(DEFAULT_LAST_YEAR.booleanValue()))
            .andExpect(jsonPath("$.last5Years").value(DEFAULT_LAST_5_YEARS.booleanValue()))
            .andExpect(jsonPath("$.last12Months").value(DEFAULT_LAST_12_MONTHS.booleanValue()))
            .andExpect(jsonPath("$.last6Months").value(DEFAULT_LAST_6_MONTHS.booleanValue()))
            .andExpect(jsonPath("$.last3Months").value(DEFAULT_LAST_3_MONTHS.booleanValue()))
            .andExpect(jsonPath("$.last6BiMonths").value(DEFAULT_LAST_6_BI_MONTHS.booleanValue()))
            .andExpect(jsonPath("$.last4Quarters").value(DEFAULT_LAST_4_QUARTERS.booleanValue()))
            .andExpect(jsonPath("$.last2SixMonths").value(DEFAULT_LAST_2_SIX_MONTHS.booleanValue()))
            .andExpect(jsonPath("$.thisFinancialYear").value(DEFAULT_THIS_FINANCIAL_YEAR.booleanValue()))
            .andExpect(jsonPath("$.lastFinancialYear").value(DEFAULT_LAST_FINANCIAL_YEAR.booleanValue()))
            .andExpect(jsonPath("$.last5FinancialYears").value(DEFAULT_LAST_5_FINANCIAL_YEARS.booleanValue()))
            .andExpect(jsonPath("$.thisWeek").value(DEFAULT_THIS_WEEK.booleanValue()))
            .andExpect(jsonPath("$.lastWeek").value(DEFAULT_LAST_WEEK.booleanValue()))
            .andExpect(jsonPath("$.thisBiWeek").value(DEFAULT_THIS_BI_WEEK.booleanValue()))
            .andExpect(jsonPath("$.lastBiWeek").value(DEFAULT_LAST_BI_WEEK.booleanValue()))
            .andExpect(jsonPath("$.last4Weeks").value(DEFAULT_LAST_4_WEEKS.booleanValue()))
            .andExpect(jsonPath("$.last4BiWeeks").value(DEFAULT_LAST_4_BI_WEEKS.booleanValue()))
            .andExpect(jsonPath("$.last12Weeks").value(DEFAULT_LAST_12_WEEKS.booleanValue()))
            .andExpect(jsonPath("$.last52Weeks").value(DEFAULT_LAST_52_WEEKS.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingRelativePeriods() throws Exception {
        // Get the relativePeriods
        restRelativePeriodsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewRelativePeriods() throws Exception {
        // Initialize the database
        relativePeriodsRepository.saveAndFlush(relativePeriods);

        int databaseSizeBeforeUpdate = relativePeriodsRepository.findAll().size();

        // Update the relativePeriods
        RelativePeriods updatedRelativePeriods = relativePeriodsRepository.findById(relativePeriods.getId()).get();
        // Disconnect from session so that the updates on updatedRelativePeriods are not directly saved in db
        em.detach(updatedRelativePeriods);
        updatedRelativePeriods
            .thisDay(UPDATED_THIS_DAY)
            .yesterday(UPDATED_YESTERDAY)
            .last3Days(UPDATED_LAST_3_DAYS)
            .last7Days(UPDATED_LAST_7_DAYS)
            .last14Days(UPDATED_LAST_14_DAYS)
            .thisMonth(UPDATED_THIS_MONTH)
            .lastMonth(UPDATED_LAST_MONTH)
            .thisBimonth(UPDATED_THIS_BIMONTH)
            .lastBimonth(UPDATED_LAST_BIMONTH)
            .thisQuarter(UPDATED_THIS_QUARTER)
            .lastQuarter(UPDATED_LAST_QUARTER)
            .thisSixMonth(UPDATED_THIS_SIX_MONTH)
            .lastSixMonth(UPDATED_LAST_SIX_MONTH)
            .weeksThisYear(UPDATED_WEEKS_THIS_YEAR)
            .monthsThisYear(UPDATED_MONTHS_THIS_YEAR)
            .biMonthsThisYear(UPDATED_BI_MONTHS_THIS_YEAR)
            .quartersThisYear(UPDATED_QUARTERS_THIS_YEAR)
            .thisYear(UPDATED_THIS_YEAR)
            .monthsLastYear(UPDATED_MONTHS_LAST_YEAR)
            .quartersLastYear(UPDATED_QUARTERS_LAST_YEAR)
            .lastYear(UPDATED_LAST_YEAR)
            .last5Years(UPDATED_LAST_5_YEARS)
            .last12Months(UPDATED_LAST_12_MONTHS)
            .last6Months(UPDATED_LAST_6_MONTHS)
            .last3Months(UPDATED_LAST_3_MONTHS)
            .last6BiMonths(UPDATED_LAST_6_BI_MONTHS)
            .last4Quarters(UPDATED_LAST_4_QUARTERS)
            .last2SixMonths(UPDATED_LAST_2_SIX_MONTHS)
            .thisFinancialYear(UPDATED_THIS_FINANCIAL_YEAR)
            .lastFinancialYear(UPDATED_LAST_FINANCIAL_YEAR)
            .last5FinancialYears(UPDATED_LAST_5_FINANCIAL_YEARS)
            .thisWeek(UPDATED_THIS_WEEK)
            .lastWeek(UPDATED_LAST_WEEK)
            .thisBiWeek(UPDATED_THIS_BI_WEEK)
            .lastBiWeek(UPDATED_LAST_BI_WEEK)
            .last4Weeks(UPDATED_LAST_4_WEEKS)
            .last4BiWeeks(UPDATED_LAST_4_BI_WEEKS)
            .last12Weeks(UPDATED_LAST_12_WEEKS)
            .last52Weeks(UPDATED_LAST_52_WEEKS);

        restRelativePeriodsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedRelativePeriods.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedRelativePeriods))
            )
            .andExpect(status().isOk());

        // Validate the RelativePeriods in the database
        List<RelativePeriods> relativePeriodsList = relativePeriodsRepository.findAll();
        assertThat(relativePeriodsList).hasSize(databaseSizeBeforeUpdate);
        RelativePeriods testRelativePeriods = relativePeriodsList.get(relativePeriodsList.size() - 1);
        assertThat(testRelativePeriods.getThisDay()).isEqualTo(UPDATED_THIS_DAY);
        assertThat(testRelativePeriods.getYesterday()).isEqualTo(UPDATED_YESTERDAY);
        assertThat(testRelativePeriods.getLast3Days()).isEqualTo(UPDATED_LAST_3_DAYS);
        assertThat(testRelativePeriods.getLast7Days()).isEqualTo(UPDATED_LAST_7_DAYS);
        assertThat(testRelativePeriods.getLast14Days()).isEqualTo(UPDATED_LAST_14_DAYS);
        assertThat(testRelativePeriods.getThisMonth()).isEqualTo(UPDATED_THIS_MONTH);
        assertThat(testRelativePeriods.getLastMonth()).isEqualTo(UPDATED_LAST_MONTH);
        assertThat(testRelativePeriods.getThisBimonth()).isEqualTo(UPDATED_THIS_BIMONTH);
        assertThat(testRelativePeriods.getLastBimonth()).isEqualTo(UPDATED_LAST_BIMONTH);
        assertThat(testRelativePeriods.getThisQuarter()).isEqualTo(UPDATED_THIS_QUARTER);
        assertThat(testRelativePeriods.getLastQuarter()).isEqualTo(UPDATED_LAST_QUARTER);
        assertThat(testRelativePeriods.getThisSixMonth()).isEqualTo(UPDATED_THIS_SIX_MONTH);
        assertThat(testRelativePeriods.getLastSixMonth()).isEqualTo(UPDATED_LAST_SIX_MONTH);
        assertThat(testRelativePeriods.getWeeksThisYear()).isEqualTo(UPDATED_WEEKS_THIS_YEAR);
        assertThat(testRelativePeriods.getMonthsThisYear()).isEqualTo(UPDATED_MONTHS_THIS_YEAR);
        assertThat(testRelativePeriods.getBiMonthsThisYear()).isEqualTo(UPDATED_BI_MONTHS_THIS_YEAR);
        assertThat(testRelativePeriods.getQuartersThisYear()).isEqualTo(UPDATED_QUARTERS_THIS_YEAR);
        assertThat(testRelativePeriods.getThisYear()).isEqualTo(UPDATED_THIS_YEAR);
        assertThat(testRelativePeriods.getMonthsLastYear()).isEqualTo(UPDATED_MONTHS_LAST_YEAR);
        assertThat(testRelativePeriods.getQuartersLastYear()).isEqualTo(UPDATED_QUARTERS_LAST_YEAR);
        assertThat(testRelativePeriods.getLastYear()).isEqualTo(UPDATED_LAST_YEAR);
        assertThat(testRelativePeriods.getLast5Years()).isEqualTo(UPDATED_LAST_5_YEARS);
        assertThat(testRelativePeriods.getLast12Months()).isEqualTo(UPDATED_LAST_12_MONTHS);
        assertThat(testRelativePeriods.getLast6Months()).isEqualTo(UPDATED_LAST_6_MONTHS);
        assertThat(testRelativePeriods.getLast3Months()).isEqualTo(UPDATED_LAST_3_MONTHS);
        assertThat(testRelativePeriods.getLast6BiMonths()).isEqualTo(UPDATED_LAST_6_BI_MONTHS);
        assertThat(testRelativePeriods.getLast4Quarters()).isEqualTo(UPDATED_LAST_4_QUARTERS);
        assertThat(testRelativePeriods.getLast2SixMonths()).isEqualTo(UPDATED_LAST_2_SIX_MONTHS);
        assertThat(testRelativePeriods.getThisFinancialYear()).isEqualTo(UPDATED_THIS_FINANCIAL_YEAR);
        assertThat(testRelativePeriods.getLastFinancialYear()).isEqualTo(UPDATED_LAST_FINANCIAL_YEAR);
        assertThat(testRelativePeriods.getLast5FinancialYears()).isEqualTo(UPDATED_LAST_5_FINANCIAL_YEARS);
        assertThat(testRelativePeriods.getThisWeek()).isEqualTo(UPDATED_THIS_WEEK);
        assertThat(testRelativePeriods.getLastWeek()).isEqualTo(UPDATED_LAST_WEEK);
        assertThat(testRelativePeriods.getThisBiWeek()).isEqualTo(UPDATED_THIS_BI_WEEK);
        assertThat(testRelativePeriods.getLastBiWeek()).isEqualTo(UPDATED_LAST_BI_WEEK);
        assertThat(testRelativePeriods.getLast4Weeks()).isEqualTo(UPDATED_LAST_4_WEEKS);
        assertThat(testRelativePeriods.getLast4BiWeeks()).isEqualTo(UPDATED_LAST_4_BI_WEEKS);
        assertThat(testRelativePeriods.getLast12Weeks()).isEqualTo(UPDATED_LAST_12_WEEKS);
        assertThat(testRelativePeriods.getLast52Weeks()).isEqualTo(UPDATED_LAST_52_WEEKS);
    }

    @Test
    @Transactional
    void putNonExistingRelativePeriods() throws Exception {
        int databaseSizeBeforeUpdate = relativePeriodsRepository.findAll().size();
        relativePeriods.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRelativePeriodsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, relativePeriods.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(relativePeriods))
            )
            .andExpect(status().isBadRequest());

        // Validate the RelativePeriods in the database
        List<RelativePeriods> relativePeriodsList = relativePeriodsRepository.findAll();
        assertThat(relativePeriodsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRelativePeriods() throws Exception {
        int databaseSizeBeforeUpdate = relativePeriodsRepository.findAll().size();
        relativePeriods.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRelativePeriodsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(relativePeriods))
            )
            .andExpect(status().isBadRequest());

        // Validate the RelativePeriods in the database
        List<RelativePeriods> relativePeriodsList = relativePeriodsRepository.findAll();
        assertThat(relativePeriodsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRelativePeriods() throws Exception {
        int databaseSizeBeforeUpdate = relativePeriodsRepository.findAll().size();
        relativePeriods.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRelativePeriodsMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(relativePeriods))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the RelativePeriods in the database
        List<RelativePeriods> relativePeriodsList = relativePeriodsRepository.findAll();
        assertThat(relativePeriodsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRelativePeriodsWithPatch() throws Exception {
        // Initialize the database
        relativePeriodsRepository.saveAndFlush(relativePeriods);

        int databaseSizeBeforeUpdate = relativePeriodsRepository.findAll().size();

        // Update the relativePeriods using partial update
        RelativePeriods partialUpdatedRelativePeriods = new RelativePeriods();
        partialUpdatedRelativePeriods.setId(relativePeriods.getId());

        partialUpdatedRelativePeriods
            .thisDay(UPDATED_THIS_DAY)
            .last3Days(UPDATED_LAST_3_DAYS)
            .last7Days(UPDATED_LAST_7_DAYS)
            .thisQuarter(UPDATED_THIS_QUARTER)
            .lastQuarter(UPDATED_LAST_QUARTER)
            .weeksThisYear(UPDATED_WEEKS_THIS_YEAR)
            .quartersLastYear(UPDATED_QUARTERS_LAST_YEAR)
            .lastYear(UPDATED_LAST_YEAR)
            .last5Years(UPDATED_LAST_5_YEARS)
            .last12Months(UPDATED_LAST_12_MONTHS)
            .last6Months(UPDATED_LAST_6_MONTHS)
            .last3Months(UPDATED_LAST_3_MONTHS)
            .last4Quarters(UPDATED_LAST_4_QUARTERS)
            .last5FinancialYears(UPDATED_LAST_5_FINANCIAL_YEARS)
            .lastWeek(UPDATED_LAST_WEEK)
            .thisBiWeek(UPDATED_THIS_BI_WEEK)
            .lastBiWeek(UPDATED_LAST_BI_WEEK)
            .last4Weeks(UPDATED_LAST_4_WEEKS)
            .last4BiWeeks(UPDATED_LAST_4_BI_WEEKS);

        restRelativePeriodsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRelativePeriods.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRelativePeriods))
            )
            .andExpect(status().isOk());

        // Validate the RelativePeriods in the database
        List<RelativePeriods> relativePeriodsList = relativePeriodsRepository.findAll();
        assertThat(relativePeriodsList).hasSize(databaseSizeBeforeUpdate);
        RelativePeriods testRelativePeriods = relativePeriodsList.get(relativePeriodsList.size() - 1);
        assertThat(testRelativePeriods.getThisDay()).isEqualTo(UPDATED_THIS_DAY);
        assertThat(testRelativePeriods.getYesterday()).isEqualTo(DEFAULT_YESTERDAY);
        assertThat(testRelativePeriods.getLast3Days()).isEqualTo(UPDATED_LAST_3_DAYS);
        assertThat(testRelativePeriods.getLast7Days()).isEqualTo(UPDATED_LAST_7_DAYS);
        assertThat(testRelativePeriods.getLast14Days()).isEqualTo(DEFAULT_LAST_14_DAYS);
        assertThat(testRelativePeriods.getThisMonth()).isEqualTo(DEFAULT_THIS_MONTH);
        assertThat(testRelativePeriods.getLastMonth()).isEqualTo(DEFAULT_LAST_MONTH);
        assertThat(testRelativePeriods.getThisBimonth()).isEqualTo(DEFAULT_THIS_BIMONTH);
        assertThat(testRelativePeriods.getLastBimonth()).isEqualTo(DEFAULT_LAST_BIMONTH);
        assertThat(testRelativePeriods.getThisQuarter()).isEqualTo(UPDATED_THIS_QUARTER);
        assertThat(testRelativePeriods.getLastQuarter()).isEqualTo(UPDATED_LAST_QUARTER);
        assertThat(testRelativePeriods.getThisSixMonth()).isEqualTo(DEFAULT_THIS_SIX_MONTH);
        assertThat(testRelativePeriods.getLastSixMonth()).isEqualTo(DEFAULT_LAST_SIX_MONTH);
        assertThat(testRelativePeriods.getWeeksThisYear()).isEqualTo(UPDATED_WEEKS_THIS_YEAR);
        assertThat(testRelativePeriods.getMonthsThisYear()).isEqualTo(DEFAULT_MONTHS_THIS_YEAR);
        assertThat(testRelativePeriods.getBiMonthsThisYear()).isEqualTo(DEFAULT_BI_MONTHS_THIS_YEAR);
        assertThat(testRelativePeriods.getQuartersThisYear()).isEqualTo(DEFAULT_QUARTERS_THIS_YEAR);
        assertThat(testRelativePeriods.getThisYear()).isEqualTo(DEFAULT_THIS_YEAR);
        assertThat(testRelativePeriods.getMonthsLastYear()).isEqualTo(DEFAULT_MONTHS_LAST_YEAR);
        assertThat(testRelativePeriods.getQuartersLastYear()).isEqualTo(UPDATED_QUARTERS_LAST_YEAR);
        assertThat(testRelativePeriods.getLastYear()).isEqualTo(UPDATED_LAST_YEAR);
        assertThat(testRelativePeriods.getLast5Years()).isEqualTo(UPDATED_LAST_5_YEARS);
        assertThat(testRelativePeriods.getLast12Months()).isEqualTo(UPDATED_LAST_12_MONTHS);
        assertThat(testRelativePeriods.getLast6Months()).isEqualTo(UPDATED_LAST_6_MONTHS);
        assertThat(testRelativePeriods.getLast3Months()).isEqualTo(UPDATED_LAST_3_MONTHS);
        assertThat(testRelativePeriods.getLast6BiMonths()).isEqualTo(DEFAULT_LAST_6_BI_MONTHS);
        assertThat(testRelativePeriods.getLast4Quarters()).isEqualTo(UPDATED_LAST_4_QUARTERS);
        assertThat(testRelativePeriods.getLast2SixMonths()).isEqualTo(DEFAULT_LAST_2_SIX_MONTHS);
        assertThat(testRelativePeriods.getThisFinancialYear()).isEqualTo(DEFAULT_THIS_FINANCIAL_YEAR);
        assertThat(testRelativePeriods.getLastFinancialYear()).isEqualTo(DEFAULT_LAST_FINANCIAL_YEAR);
        assertThat(testRelativePeriods.getLast5FinancialYears()).isEqualTo(UPDATED_LAST_5_FINANCIAL_YEARS);
        assertThat(testRelativePeriods.getThisWeek()).isEqualTo(DEFAULT_THIS_WEEK);
        assertThat(testRelativePeriods.getLastWeek()).isEqualTo(UPDATED_LAST_WEEK);
        assertThat(testRelativePeriods.getThisBiWeek()).isEqualTo(UPDATED_THIS_BI_WEEK);
        assertThat(testRelativePeriods.getLastBiWeek()).isEqualTo(UPDATED_LAST_BI_WEEK);
        assertThat(testRelativePeriods.getLast4Weeks()).isEqualTo(UPDATED_LAST_4_WEEKS);
        assertThat(testRelativePeriods.getLast4BiWeeks()).isEqualTo(UPDATED_LAST_4_BI_WEEKS);
        assertThat(testRelativePeriods.getLast12Weeks()).isEqualTo(DEFAULT_LAST_12_WEEKS);
        assertThat(testRelativePeriods.getLast52Weeks()).isEqualTo(DEFAULT_LAST_52_WEEKS);
    }

    @Test
    @Transactional
    void fullUpdateRelativePeriodsWithPatch() throws Exception {
        // Initialize the database
        relativePeriodsRepository.saveAndFlush(relativePeriods);

        int databaseSizeBeforeUpdate = relativePeriodsRepository.findAll().size();

        // Update the relativePeriods using partial update
        RelativePeriods partialUpdatedRelativePeriods = new RelativePeriods();
        partialUpdatedRelativePeriods.setId(relativePeriods.getId());

        partialUpdatedRelativePeriods
            .thisDay(UPDATED_THIS_DAY)
            .yesterday(UPDATED_YESTERDAY)
            .last3Days(UPDATED_LAST_3_DAYS)
            .last7Days(UPDATED_LAST_7_DAYS)
            .last14Days(UPDATED_LAST_14_DAYS)
            .thisMonth(UPDATED_THIS_MONTH)
            .lastMonth(UPDATED_LAST_MONTH)
            .thisBimonth(UPDATED_THIS_BIMONTH)
            .lastBimonth(UPDATED_LAST_BIMONTH)
            .thisQuarter(UPDATED_THIS_QUARTER)
            .lastQuarter(UPDATED_LAST_QUARTER)
            .thisSixMonth(UPDATED_THIS_SIX_MONTH)
            .lastSixMonth(UPDATED_LAST_SIX_MONTH)
            .weeksThisYear(UPDATED_WEEKS_THIS_YEAR)
            .monthsThisYear(UPDATED_MONTHS_THIS_YEAR)
            .biMonthsThisYear(UPDATED_BI_MONTHS_THIS_YEAR)
            .quartersThisYear(UPDATED_QUARTERS_THIS_YEAR)
            .thisYear(UPDATED_THIS_YEAR)
            .monthsLastYear(UPDATED_MONTHS_LAST_YEAR)
            .quartersLastYear(UPDATED_QUARTERS_LAST_YEAR)
            .lastYear(UPDATED_LAST_YEAR)
            .last5Years(UPDATED_LAST_5_YEARS)
            .last12Months(UPDATED_LAST_12_MONTHS)
            .last6Months(UPDATED_LAST_6_MONTHS)
            .last3Months(UPDATED_LAST_3_MONTHS)
            .last6BiMonths(UPDATED_LAST_6_BI_MONTHS)
            .last4Quarters(UPDATED_LAST_4_QUARTERS)
            .last2SixMonths(UPDATED_LAST_2_SIX_MONTHS)
            .thisFinancialYear(UPDATED_THIS_FINANCIAL_YEAR)
            .lastFinancialYear(UPDATED_LAST_FINANCIAL_YEAR)
            .last5FinancialYears(UPDATED_LAST_5_FINANCIAL_YEARS)
            .thisWeek(UPDATED_THIS_WEEK)
            .lastWeek(UPDATED_LAST_WEEK)
            .thisBiWeek(UPDATED_THIS_BI_WEEK)
            .lastBiWeek(UPDATED_LAST_BI_WEEK)
            .last4Weeks(UPDATED_LAST_4_WEEKS)
            .last4BiWeeks(UPDATED_LAST_4_BI_WEEKS)
            .last12Weeks(UPDATED_LAST_12_WEEKS)
            .last52Weeks(UPDATED_LAST_52_WEEKS);

        restRelativePeriodsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRelativePeriods.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRelativePeriods))
            )
            .andExpect(status().isOk());

        // Validate the RelativePeriods in the database
        List<RelativePeriods> relativePeriodsList = relativePeriodsRepository.findAll();
        assertThat(relativePeriodsList).hasSize(databaseSizeBeforeUpdate);
        RelativePeriods testRelativePeriods = relativePeriodsList.get(relativePeriodsList.size() - 1);
        assertThat(testRelativePeriods.getThisDay()).isEqualTo(UPDATED_THIS_DAY);
        assertThat(testRelativePeriods.getYesterday()).isEqualTo(UPDATED_YESTERDAY);
        assertThat(testRelativePeriods.getLast3Days()).isEqualTo(UPDATED_LAST_3_DAYS);
        assertThat(testRelativePeriods.getLast7Days()).isEqualTo(UPDATED_LAST_7_DAYS);
        assertThat(testRelativePeriods.getLast14Days()).isEqualTo(UPDATED_LAST_14_DAYS);
        assertThat(testRelativePeriods.getThisMonth()).isEqualTo(UPDATED_THIS_MONTH);
        assertThat(testRelativePeriods.getLastMonth()).isEqualTo(UPDATED_LAST_MONTH);
        assertThat(testRelativePeriods.getThisBimonth()).isEqualTo(UPDATED_THIS_BIMONTH);
        assertThat(testRelativePeriods.getLastBimonth()).isEqualTo(UPDATED_LAST_BIMONTH);
        assertThat(testRelativePeriods.getThisQuarter()).isEqualTo(UPDATED_THIS_QUARTER);
        assertThat(testRelativePeriods.getLastQuarter()).isEqualTo(UPDATED_LAST_QUARTER);
        assertThat(testRelativePeriods.getThisSixMonth()).isEqualTo(UPDATED_THIS_SIX_MONTH);
        assertThat(testRelativePeriods.getLastSixMonth()).isEqualTo(UPDATED_LAST_SIX_MONTH);
        assertThat(testRelativePeriods.getWeeksThisYear()).isEqualTo(UPDATED_WEEKS_THIS_YEAR);
        assertThat(testRelativePeriods.getMonthsThisYear()).isEqualTo(UPDATED_MONTHS_THIS_YEAR);
        assertThat(testRelativePeriods.getBiMonthsThisYear()).isEqualTo(UPDATED_BI_MONTHS_THIS_YEAR);
        assertThat(testRelativePeriods.getQuartersThisYear()).isEqualTo(UPDATED_QUARTERS_THIS_YEAR);
        assertThat(testRelativePeriods.getThisYear()).isEqualTo(UPDATED_THIS_YEAR);
        assertThat(testRelativePeriods.getMonthsLastYear()).isEqualTo(UPDATED_MONTHS_LAST_YEAR);
        assertThat(testRelativePeriods.getQuartersLastYear()).isEqualTo(UPDATED_QUARTERS_LAST_YEAR);
        assertThat(testRelativePeriods.getLastYear()).isEqualTo(UPDATED_LAST_YEAR);
        assertThat(testRelativePeriods.getLast5Years()).isEqualTo(UPDATED_LAST_5_YEARS);
        assertThat(testRelativePeriods.getLast12Months()).isEqualTo(UPDATED_LAST_12_MONTHS);
        assertThat(testRelativePeriods.getLast6Months()).isEqualTo(UPDATED_LAST_6_MONTHS);
        assertThat(testRelativePeriods.getLast3Months()).isEqualTo(UPDATED_LAST_3_MONTHS);
        assertThat(testRelativePeriods.getLast6BiMonths()).isEqualTo(UPDATED_LAST_6_BI_MONTHS);
        assertThat(testRelativePeriods.getLast4Quarters()).isEqualTo(UPDATED_LAST_4_QUARTERS);
        assertThat(testRelativePeriods.getLast2SixMonths()).isEqualTo(UPDATED_LAST_2_SIX_MONTHS);
        assertThat(testRelativePeriods.getThisFinancialYear()).isEqualTo(UPDATED_THIS_FINANCIAL_YEAR);
        assertThat(testRelativePeriods.getLastFinancialYear()).isEqualTo(UPDATED_LAST_FINANCIAL_YEAR);
        assertThat(testRelativePeriods.getLast5FinancialYears()).isEqualTo(UPDATED_LAST_5_FINANCIAL_YEARS);
        assertThat(testRelativePeriods.getThisWeek()).isEqualTo(UPDATED_THIS_WEEK);
        assertThat(testRelativePeriods.getLastWeek()).isEqualTo(UPDATED_LAST_WEEK);
        assertThat(testRelativePeriods.getThisBiWeek()).isEqualTo(UPDATED_THIS_BI_WEEK);
        assertThat(testRelativePeriods.getLastBiWeek()).isEqualTo(UPDATED_LAST_BI_WEEK);
        assertThat(testRelativePeriods.getLast4Weeks()).isEqualTo(UPDATED_LAST_4_WEEKS);
        assertThat(testRelativePeriods.getLast4BiWeeks()).isEqualTo(UPDATED_LAST_4_BI_WEEKS);
        assertThat(testRelativePeriods.getLast12Weeks()).isEqualTo(UPDATED_LAST_12_WEEKS);
        assertThat(testRelativePeriods.getLast52Weeks()).isEqualTo(UPDATED_LAST_52_WEEKS);
    }

    @Test
    @Transactional
    void patchNonExistingRelativePeriods() throws Exception {
        int databaseSizeBeforeUpdate = relativePeriodsRepository.findAll().size();
        relativePeriods.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRelativePeriodsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, relativePeriods.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(relativePeriods))
            )
            .andExpect(status().isBadRequest());

        // Validate the RelativePeriods in the database
        List<RelativePeriods> relativePeriodsList = relativePeriodsRepository.findAll();
        assertThat(relativePeriodsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRelativePeriods() throws Exception {
        int databaseSizeBeforeUpdate = relativePeriodsRepository.findAll().size();
        relativePeriods.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRelativePeriodsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(relativePeriods))
            )
            .andExpect(status().isBadRequest());

        // Validate the RelativePeriods in the database
        List<RelativePeriods> relativePeriodsList = relativePeriodsRepository.findAll();
        assertThat(relativePeriodsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRelativePeriods() throws Exception {
        int databaseSizeBeforeUpdate = relativePeriodsRepository.findAll().size();
        relativePeriods.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRelativePeriodsMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(relativePeriods))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the RelativePeriods in the database
        List<RelativePeriods> relativePeriodsList = relativePeriodsRepository.findAll();
        assertThat(relativePeriodsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRelativePeriods() throws Exception {
        // Initialize the database
        relativePeriodsRepository.saveAndFlush(relativePeriods);

        int databaseSizeBeforeDelete = relativePeriodsRepository.findAll().size();

        // Delete the relativePeriods
        restRelativePeriodsMockMvc
            .perform(delete(ENTITY_API_URL_ID, relativePeriods.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<RelativePeriods> relativePeriodsList = relativePeriodsRepository.findAll();
        assertThat(relativePeriodsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
