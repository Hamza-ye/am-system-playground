package org.nmcpye.activitiesmanagement.domain.demographicdata;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.*;

import com.fasterxml.jackson.annotation.JsonRootName;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.nmcpye.activitiesmanagement.domain.User;
import org.nmcpye.activitiesmanagement.domain.enumeration.DemographicDataLevel;
import org.nmcpye.activitiesmanagement.domain.organisationunit.OrganisationUnit;
import org.nmcpye.activitiesmanagement.extended.common.BaseIdentifiableObject;
import org.nmcpye.activitiesmanagement.extended.common.DxfNamespaces;
import org.nmcpye.activitiesmanagement.extended.common.MetadataObject;

/**
 * A DemographicData.
 */
@Entity
@Table(name = "demographic_data")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@JsonRootName( value = "demographicData", namespace = DxfNamespaces.DXF_2_0 )
public class DemographicData extends BaseIdentifiableObject implements MetadataObject {

    ////////////////////////
    ///
    /// Common Columns
    ///
    ////////////////////////

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Size(max = 11)
    @Column(name = "uid", length = 11, nullable = false, unique = true)
    private String uid;

    @Column(name = "code", unique = true)
    private String code;

//    @Column(name = "name")
//    private String name;

    @Column(name = "created")
    private Date created;

    @Column(name = "last_updated")
    private Date lastUpdated;

    /**
     * Owner of this object.
     */
    @ManyToOne
    @JoinColumn(name = "created_by")
    protected User createdBy;

    /**
     * Last user updated this object.
     */
    @ManyToOne
    @JoinColumn(name = "last_updated_by")
    protected User lastUpdatedBy;

    ////////////////////////
    ///
    ///
    ////////////////////////

    @NotNull
    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Enumerated(EnumType.STRING)
    @Column(name = "level")
    private DemographicDataLevel level;

    @Column(name = "total_population")
    private Integer totalPopulation;

    @Column(name = "male_population")
    private Integer malePopulation;

    @Column(name = "female_population")
    private Integer femalePopulation;

    @Column(name = "less_than_5_population")
    private Integer lessThan5Population;

    @Column(name = "greater_than_5_population")
    private Integer greaterThan5Population;

    @Column(name = "bw_5_and_15_population")
    private Integer bw5And15Population;

    @Column(name = "greater_than_15_population")
    private Integer greaterThan15Population;

    @Column(name = "household")
    private Integer household;

    @Column(name = "houses")
    private Integer houses;

    @Column(name = "health_facilities")
    private Integer healthFacilities;

    @Column(name = "avg_no_of_rooms", precision = 21, scale = 2)
    private BigDecimal avgNoOfRooms;

    @Column(name = "avg_room_area", precision = 21, scale = 2)
    private BigDecimal avgRoomArea;

    @Column(name = "avg_house_area", precision = 21, scale = 2)
    private BigDecimal avgHouseArea;

    @Column(name = "individuals_per_household", precision = 21, scale = 2)
    private BigDecimal individualsPerHousehold;

    @Column(name = "population_growth_rate", precision = 21, scale = 2)
    private BigDecimal populationGrowthRate;

    @Column(name = "comment")
    private String comment;

    @ManyToOne
    @JsonIgnoreProperties(
        value = {
            "malariaReports",
            "dengueReports",
            "parent",
            "hfHomeSubVillage",
            "coveredByHf",
            "createdBy",
            "user",
            "lastUpdatedBy",
            "malariaUnit",
            "assignedChv",
            "children",
            "demographicData",
            "groups",
            "people",
            "dataViewPeople",
            "dataSets",
        },
        allowSetters = true
    )
    private OrganisationUnit organisationUnit;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "demographicData", "user", "createdBy", "lastUpdatedBy" }, allowSetters = true)
    private DemographicDataSource source;

    public DemographicData id(Long id) {
        this.id = id;
        return this;
    }

    public DemographicData created(Date created) {
        this.created = created;
        return this;
    }

    public DemographicData lastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
        return this;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public DemographicData date(LocalDate date) {
        this.date = date;
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public DemographicDataLevel getLevel() {
        return this.level;
    }

    public DemographicData level(DemographicDataLevel level) {
        this.level = level;
        return this;
    }

    public void setLevel(DemographicDataLevel level) {
        this.level = level;
    }

    public Integer getTotalPopulation() {
        return this.totalPopulation;
    }

    public DemographicData totalPopulation(Integer totalPopulation) {
        this.totalPopulation = totalPopulation;
        return this;
    }

    public void setTotalPopulation(Integer totalPopulation) {
        this.totalPopulation = totalPopulation;
    }

    public Integer getMalePopulation() {
        return this.malePopulation;
    }

    public DemographicData malePopulation(Integer malePopulation) {
        this.malePopulation = malePopulation;
        return this;
    }

    public void setMalePopulation(Integer malePopulation) {
        this.malePopulation = malePopulation;
    }

    public Integer getFemalePopulation() {
        return this.femalePopulation;
    }

    public DemographicData femalePopulation(Integer femalePopulation) {
        this.femalePopulation = femalePopulation;
        return this;
    }

    public void setFemalePopulation(Integer femalePopulation) {
        this.femalePopulation = femalePopulation;
    }

    public Integer getLessThan5Population() {
        return this.lessThan5Population;
    }

    public DemographicData lessThan5Population(Integer lessThan5Population) {
        this.lessThan5Population = lessThan5Population;
        return this;
    }

    public void setLessThan5Population(Integer lessThan5Population) {
        this.lessThan5Population = lessThan5Population;
    }

    public Integer getGreaterThan5Population() {
        return this.greaterThan5Population;
    }

    public DemographicData greaterThan5Population(Integer greaterThan5Population) {
        this.greaterThan5Population = greaterThan5Population;
        return this;
    }

    public void setGreaterThan5Population(Integer greaterThan5Population) {
        this.greaterThan5Population = greaterThan5Population;
    }

    public Integer getBw5And15Population() {
        return this.bw5And15Population;
    }

    public DemographicData bw5And15Population(Integer bw5And15Population) {
        this.bw5And15Population = bw5And15Population;
        return this;
    }

    public void setBw5And15Population(Integer bw5And15Population) {
        this.bw5And15Population = bw5And15Population;
    }

    public Integer getGreaterThan15Population() {
        return this.greaterThan15Population;
    }

    public DemographicData greaterThan15Population(Integer greaterThan15Population) {
        this.greaterThan15Population = greaterThan15Population;
        return this;
    }

    public void setGreaterThan15Population(Integer greaterThan15Population) {
        this.greaterThan15Population = greaterThan15Population;
    }

    public Integer getHousehold() {
        return this.household;
    }

    public DemographicData household(Integer household) {
        this.household = household;
        return this;
    }

    public void setHousehold(Integer household) {
        this.household = household;
    }

    public Integer getHouses() {
        return this.houses;
    }

    public DemographicData houses(Integer houses) {
        this.houses = houses;
        return this;
    }

    public void setHouses(Integer houses) {
        this.houses = houses;
    }

    public Integer getHealthFacilities() {
        return this.healthFacilities;
    }

    public DemographicData healthFacilities(Integer healthFacilities) {
        this.healthFacilities = healthFacilities;
        return this;
    }

    public void setHealthFacilities(Integer healthFacilities) {
        this.healthFacilities = healthFacilities;
    }

    public BigDecimal getAvgNoOfRooms() {
        return this.avgNoOfRooms;
    }

    public DemographicData avgNoOfRooms(BigDecimal avgNoOfRooms) {
        this.avgNoOfRooms = avgNoOfRooms;
        return this;
    }

    public void setAvgNoOfRooms(BigDecimal avgNoOfRooms) {
        this.avgNoOfRooms = avgNoOfRooms;
    }

    public BigDecimal getAvgRoomArea() {
        return this.avgRoomArea;
    }

    public DemographicData avgRoomArea(BigDecimal avgRoomArea) {
        this.avgRoomArea = avgRoomArea;
        return this;
    }

    public void setAvgRoomArea(BigDecimal avgRoomArea) {
        this.avgRoomArea = avgRoomArea;
    }

    public BigDecimal getAvgHouseArea() {
        return this.avgHouseArea;
    }

    public DemographicData avgHouseArea(BigDecimal avgHouseArea) {
        this.avgHouseArea = avgHouseArea;
        return this;
    }

    public void setAvgHouseArea(BigDecimal avgHouseArea) {
        this.avgHouseArea = avgHouseArea;
    }

    public BigDecimal getIndividualsPerHousehold() {
        return this.individualsPerHousehold;
    }

    public DemographicData individualsPerHousehold(BigDecimal individualsPerHousehold) {
        this.individualsPerHousehold = individualsPerHousehold;
        return this;
    }

    public void setIndividualsPerHousehold(BigDecimal individualsPerHousehold) {
        this.individualsPerHousehold = individualsPerHousehold;
    }

    public BigDecimal getPopulationGrowthRate() {
        return this.populationGrowthRate;
    }

    public DemographicData populationGrowthRate(BigDecimal populationGrowthRate) {
        this.populationGrowthRate = populationGrowthRate;
        return this;
    }

    public void setPopulationGrowthRate(BigDecimal populationGrowthRate) {
        this.populationGrowthRate = populationGrowthRate;
    }

    public String getComment() {
        return this.comment;
    }

    public DemographicData comment(String comment) {
        this.comment = comment;
        return this;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public OrganisationUnit getOrganisationUnit() {
        return this.organisationUnit;
    }

    public DemographicData organisationUnit(OrganisationUnit organisationUnit) {
        this.setOrganisationUnit(organisationUnit);
        return this;
    }

    public void setOrganisationUnit(OrganisationUnit organisationUnit) {
        this.organisationUnit = organisationUnit;
    }

    public DemographicData user(User user) {
        this.setUser(user);
        return this;
    }

    public DemographicData lastUpdatedBy(User user) {
        this.setLastUpdatedBy(user);
        return this;
    }

    public void setLastUpdatedBy(User user) {
        this.lastUpdatedBy = user;
    }

    public DemographicDataSource getSource() {
        return this.source;
    }

    public DemographicData source(DemographicDataSource demographicDataSource) {
        this.setSource(demographicDataSource);
        return this;
    }

    public void setSource(DemographicDataSource demographicDataSource) {
        this.source = demographicDataSource;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getUid() {
        return uid;
    }

    @Override
    public void setUid(String uid) {
        this.uid = uid;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public void setCode(String code) {
        this.code = code;
    }

//    @Override
//    public String getName() {
//        return name;
//    }

//    @Override
//    public void setName(String name) {
//        this.name = name;
//    }

    @Override
    public Date getCreated() {
        return created;
    }

    @Override
    public void setCreated(Date created) {
        this.created = created;
    }

    @Override
    public Date getLastUpdated() {
        return lastUpdated;
    }

    @Override
    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    @Override
    public User getCreatedBy() {
        return createdBy;
    }

    @Override
    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    @Override
    public User getLastUpdatedBy() {
        return lastUpdatedBy;
    }
}
