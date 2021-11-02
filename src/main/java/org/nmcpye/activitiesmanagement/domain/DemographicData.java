package org.nmcpye.activitiesmanagement.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.nmcpye.activitiesmanagement.domain.enumeration.DemographicDataLevel;

/**
 * A DemographicData.
 */
@Entity
@Table(name = "demographic_data")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class DemographicData implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "created")
    private Instant created;

    @Column(name = "last_updated")
    private Instant lastUpdated;

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

    @ManyToOne
    private User user;

    @ManyToOne
    private User lastUpdatedBy;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "demographicData", "user", "lastUpdatedBy" }, allowSetters = true)
    private DemographicDataSource source;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DemographicData id(Long id) {
        this.id = id;
        return this;
    }

    public Instant getCreated() {
        return this.created;
    }

    public DemographicData created(Instant created) {
        this.created = created;
        return this;
    }

    public void setCreated(Instant created) {
        this.created = created;
    }

    public Instant getLastUpdated() {
        return this.lastUpdated;
    }

    public DemographicData lastUpdated(Instant lastUpdated) {
        this.lastUpdated = lastUpdated;
        return this;
    }

    public void setLastUpdated(Instant lastUpdated) {
        this.lastUpdated = lastUpdated;
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

    public User getUser() {
        return this.user;
    }

    public DemographicData user(User user) {
        this.setUser(user);
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getLastUpdatedBy() {
        return this.lastUpdatedBy;
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

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DemographicData)) {
            return false;
        }
        return id != null && id.equals(((DemographicData) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DemographicData{" +
            "id=" + getId() +
            ", created='" + getCreated() + "'" +
            ", lastUpdated='" + getLastUpdated() + "'" +
            ", date='" + getDate() + "'" +
            ", level='" + getLevel() + "'" +
            ", totalPopulation=" + getTotalPopulation() +
            ", malePopulation=" + getMalePopulation() +
            ", femalePopulation=" + getFemalePopulation() +
            ", lessThan5Population=" + getLessThan5Population() +
            ", greaterThan5Population=" + getGreaterThan5Population() +
            ", bw5And15Population=" + getBw5And15Population() +
            ", greaterThan15Population=" + getGreaterThan15Population() +
            ", household=" + getHousehold() +
            ", houses=" + getHouses() +
            ", healthFacilities=" + getHealthFacilities() +
            ", avgNoOfRooms=" + getAvgNoOfRooms() +
            ", avgRoomArea=" + getAvgRoomArea() +
            ", avgHouseArea=" + getAvgHouseArea() +
            ", individualsPerHousehold=" + getIndividualsPerHousehold() +
            ", populationGrowthRate=" + getPopulationGrowthRate() +
            ", comment='" + getComment() + "'" +
            "}";
    }
}
