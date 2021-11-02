package org.nmcpye.activitiesmanagement.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A DataSet.
 */
@Entity
@Table(name = "data_set")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class DataSet implements Serializable {

    private static final long serialVersionUID = 1L;

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

    @Column(name = "name")
    private String name;

    @Size(max = 50)
    @Column(name = "short_name", length = 50)
    private String shortName;

    @Column(name = "description")
    private String description;

    @Column(name = "created")
    private Instant created;

    @Column(name = "last_updated")
    private Instant lastUpdated;

    @Column(name = "expiry_days")
    private Integer expiryDays;

    @Column(name = "timely_days")
    private Integer timelyDays;

    @OneToMany(mappedBy = "dataSet")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "user", "lastUpdatedBy", "reportClass", "period", "dataSet", "organisationUnit" }, allowSetters = true)
    private Set<MalariaCasesReport> malariaCasesReports = new HashSet<>();

    @OneToMany(mappedBy = "dataSet")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "user", "lastUpdatedBy", "reportClass", "period", "dataSet", "organisationUnit" }, allowSetters = true)
    private Set<DengueCasesReport> dengueCasesReports = new HashSet<>();

    @ManyToOne
    private PeriodType periodType;

    @ManyToOne
    @JsonIgnoreProperties(value = { "user", "lastUpdatedBy", "members", "managedByGroups", "managedGroups" }, allowSetters = true)
    private PeopleGroup notificationRecipients;

    @ManyToOne
    private User user;

    @ManyToOne
    private User lastUpdatedBy;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(
        name = "rel_data_set__source",
        joinColumns = @JoinColumn(name = "data_set_id"),
        inverseJoinColumns = @JoinColumn(name = "source_id")
    )
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
    private Set<OrganisationUnit> sources = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DataSet id(Long id) {
        this.id = id;
        return this;
    }

    public String getUid() {
        return this.uid;
    }

    public DataSet uid(String uid) {
        this.uid = uid;
        return this;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getCode() {
        return this.code;
    }

    public DataSet code(String code) {
        this.code = code;
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return this.name;
    }

    public DataSet name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortName() {
        return this.shortName;
    }

    public DataSet shortName(String shortName) {
        this.shortName = shortName;
        return this;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getDescription() {
        return this.description;
    }

    public DataSet description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getCreated() {
        return this.created;
    }

    public DataSet created(Instant created) {
        this.created = created;
        return this;
    }

    public void setCreated(Instant created) {
        this.created = created;
    }

    public Instant getLastUpdated() {
        return this.lastUpdated;
    }

    public DataSet lastUpdated(Instant lastUpdated) {
        this.lastUpdated = lastUpdated;
        return this;
    }

    public void setLastUpdated(Instant lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public Integer getExpiryDays() {
        return this.expiryDays;
    }

    public DataSet expiryDays(Integer expiryDays) {
        this.expiryDays = expiryDays;
        return this;
    }

    public void setExpiryDays(Integer expiryDays) {
        this.expiryDays = expiryDays;
    }

    public Integer getTimelyDays() {
        return this.timelyDays;
    }

    public DataSet timelyDays(Integer timelyDays) {
        this.timelyDays = timelyDays;
        return this;
    }

    public void setTimelyDays(Integer timelyDays) {
        this.timelyDays = timelyDays;
    }

    public Set<MalariaCasesReport> getMalariaCasesReports() {
        return this.malariaCasesReports;
    }

    public DataSet malariaCasesReports(Set<MalariaCasesReport> malariaCasesReports) {
        this.setMalariaCasesReports(malariaCasesReports);
        return this;
    }

    public DataSet addMalariaCasesReport(MalariaCasesReport malariaCasesReport) {
        this.malariaCasesReports.add(malariaCasesReport);
        malariaCasesReport.setDataSet(this);
        return this;
    }

    public DataSet removeMalariaCasesReport(MalariaCasesReport malariaCasesReport) {
        this.malariaCasesReports.remove(malariaCasesReport);
        malariaCasesReport.setDataSet(null);
        return this;
    }

    public void setMalariaCasesReports(Set<MalariaCasesReport> malariaCasesReports) {
        if (this.malariaCasesReports != null) {
            this.malariaCasesReports.forEach(i -> i.setDataSet(null));
        }
        if (malariaCasesReports != null) {
            malariaCasesReports.forEach(i -> i.setDataSet(this));
        }
        this.malariaCasesReports = malariaCasesReports;
    }

    public Set<DengueCasesReport> getDengueCasesReports() {
        return this.dengueCasesReports;
    }

    public DataSet dengueCasesReports(Set<DengueCasesReport> dengueCasesReports) {
        this.setDengueCasesReports(dengueCasesReports);
        return this;
    }

    public DataSet addDengueCasesReport(DengueCasesReport dengueCasesReport) {
        this.dengueCasesReports.add(dengueCasesReport);
        dengueCasesReport.setDataSet(this);
        return this;
    }

    public DataSet removeDengueCasesReport(DengueCasesReport dengueCasesReport) {
        this.dengueCasesReports.remove(dengueCasesReport);
        dengueCasesReport.setDataSet(null);
        return this;
    }

    public void setDengueCasesReports(Set<DengueCasesReport> dengueCasesReports) {
        if (this.dengueCasesReports != null) {
            this.dengueCasesReports.forEach(i -> i.setDataSet(null));
        }
        if (dengueCasesReports != null) {
            dengueCasesReports.forEach(i -> i.setDataSet(this));
        }
        this.dengueCasesReports = dengueCasesReports;
    }

    public PeriodType getPeriodType() {
        return this.periodType;
    }

    public DataSet periodType(PeriodType periodType) {
        this.setPeriodType(periodType);
        return this;
    }

    public void setPeriodType(PeriodType periodType) {
        this.periodType = periodType;
    }

    public PeopleGroup getNotificationRecipients() {
        return this.notificationRecipients;
    }

    public DataSet notificationRecipients(PeopleGroup peopleGroup) {
        this.setNotificationRecipients(peopleGroup);
        return this;
    }

    public void setNotificationRecipients(PeopleGroup peopleGroup) {
        this.notificationRecipients = peopleGroup;
    }

    public User getUser() {
        return this.user;
    }

    public DataSet user(User user) {
        this.setUser(user);
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getLastUpdatedBy() {
        return this.lastUpdatedBy;
    }

    public DataSet lastUpdatedBy(User user) {
        this.setLastUpdatedBy(user);
        return this;
    }

    public void setLastUpdatedBy(User user) {
        this.lastUpdatedBy = user;
    }

    public Set<OrganisationUnit> getSources() {
        return this.sources;
    }

    public DataSet sources(Set<OrganisationUnit> organisationUnits) {
        this.setSources(organisationUnits);
        return this;
    }

    public DataSet addSource(OrganisationUnit organisationUnit) {
        this.sources.add(organisationUnit);
        organisationUnit.getDataSets().add(this);
        return this;
    }

    public DataSet removeSource(OrganisationUnit organisationUnit) {
        this.sources.remove(organisationUnit);
        organisationUnit.getDataSets().remove(this);
        return this;
    }

    public void setSources(Set<OrganisationUnit> organisationUnits) {
        this.sources = organisationUnits;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DataSet)) {
            return false;
        }
        return id != null && id.equals(((DataSet) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DataSet{" +
            "id=" + getId() +
            ", uid='" + getUid() + "'" +
            ", code='" + getCode() + "'" +
            ", name='" + getName() + "'" +
            ", shortName='" + getShortName() + "'" +
            ", description='" + getDescription() + "'" +
            ", created='" + getCreated() + "'" +
            ", lastUpdated='" + getLastUpdated() + "'" +
            ", expiryDays=" + getExpiryDays() +
            ", timelyDays=" + getTimelyDays() +
            "}";
    }
}
