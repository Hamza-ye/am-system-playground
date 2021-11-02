package org.nmcpye.activitiesmanagement.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.nmcpye.activitiesmanagement.domain.enumeration.OrganisationUnitType;

/**
 * A OrganisationUnit.
 */
@Entity
@Table(name = "organisation_unit")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class OrganisationUnit implements Serializable {

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

    @Column(name = "created")
    private Instant created;

    @Column(name = "last_updated")
    private Instant lastUpdated;

    @Column(name = "path")
    private String path;

    @Column(name = "hierarchy_level")
    private Integer hierarchyLevel;

    @Column(name = "opening_date")
    private LocalDate openingDate;

    @Column(name = "comment")
    private String comment;

    @Column(name = "closed_date")
    private LocalDate closedDate;

    @Column(name = "url")
    private String url;

    @Column(name = "contact_person")
    private String contactPerson;

    @Column(name = "address")
    private String address;

    @Column(name = "email")
    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "organisation_unit_type", nullable = false)
    private OrganisationUnitType organisationUnitType;

    @OneToMany(mappedBy = "organisationUnit")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "user", "lastUpdatedBy", "reportClass", "period", "dataSet", "organisationUnit" }, allowSetters = true)
    private Set<MalariaCasesReport> malariaReports = new HashSet<>();

    @OneToMany(mappedBy = "organisationUnit")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "user", "lastUpdatedBy", "reportClass", "period", "dataSet", "organisationUnit" }, allowSetters = true)
    private Set<DengueCasesReport> dengueReports = new HashSet<>();

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
    private OrganisationUnit parent;

    /**
     * When OrgUnit is HealthFacility, what its home subvillage
     */
    @ApiModelProperty(value = "When OrgUnit is HealthFacility, what its home subvillage")
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
    private OrganisationUnit hfHomeSubVillage;

    /**
     * When OrgUnit is HealthFacility, what villages it covers
     */
    @ApiModelProperty(value = "When OrgUnit is HealthFacility, what villages it covers")
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
    private OrganisationUnit coveredByHf;

    @ManyToOne
    private User user;

    @ManyToOne
    private User lastUpdatedBy;

    @ManyToOne
    @JsonIgnoreProperties(value = { "organisationUnits", "malariaUnitStaffMembers", "user", "lastUpdatedBy" }, allowSetters = true)
    private MalariaUnit malariaUnit;

    @ManyToOne
    @JsonIgnoreProperties(
        value = {
            "person", "coveredSubVillages", "district", "homeSubvillage", "managedByHf", "user", "lastUpdatedBy", "supervisionTeams",
        },
        allowSetters = true
    )
    private CHV assignedChv;

    @OneToMany(mappedBy = "parent")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
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
    private Set<OrganisationUnit> children = new HashSet<>();

    @OneToMany(mappedBy = "organisationUnit")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "organisationUnit", "user", "lastUpdatedBy", "source" }, allowSetters = true)
    private Set<DemographicData> demographicData = new HashSet<>();

    @ManyToMany(mappedBy = "members")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "user", "lastUpdatedBy", "members", "groupSets" }, allowSetters = true)
    private Set<OrganisationUnitGroup> groups = new HashSet<>();

    @ManyToMany(mappedBy = "organisationUnits")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = {
            "userInfo", "user", "lastUpdatedBy", "organisationUnits", "dataViewOrganisationUnits", "personAuthorityGroups", "groups",
        },
        allowSetters = true
    )
    private Set<Person> people = new HashSet<>();

    @ManyToMany(mappedBy = "dataViewOrganisationUnits")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = {
            "userInfo", "user", "lastUpdatedBy", "organisationUnits", "dataViewOrganisationUnits", "personAuthorityGroups", "groups",
        },
        allowSetters = true
    )
    private Set<Person> dataViewPeople = new HashSet<>();

    @ManyToMany(mappedBy = "sources")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = { "malariaCasesReports", "dengueCasesReports", "periodType", "notificationRecipients", "user", "lastUpdatedBy", "sources" },
        allowSetters = true
    )
    private Set<DataSet> dataSets = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OrganisationUnit id(Long id) {
        this.id = id;
        return this;
    }

    public String getUid() {
        return this.uid;
    }

    public OrganisationUnit uid(String uid) {
        this.uid = uid;
        return this;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getCode() {
        return this.code;
    }

    public OrganisationUnit code(String code) {
        this.code = code;
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return this.name;
    }

    public OrganisationUnit name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortName() {
        return this.shortName;
    }

    public OrganisationUnit shortName(String shortName) {
        this.shortName = shortName;
        return this;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public Instant getCreated() {
        return this.created;
    }

    public OrganisationUnit created(Instant created) {
        this.created = created;
        return this;
    }

    public void setCreated(Instant created) {
        this.created = created;
    }

    public Instant getLastUpdated() {
        return this.lastUpdated;
    }

    public OrganisationUnit lastUpdated(Instant lastUpdated) {
        this.lastUpdated = lastUpdated;
        return this;
    }

    public void setLastUpdated(Instant lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getPath() {
        return this.path;
    }

    public OrganisationUnit path(String path) {
        this.path = path;
        return this;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Integer getHierarchyLevel() {
        return this.hierarchyLevel;
    }

    public OrganisationUnit hierarchyLevel(Integer hierarchyLevel) {
        this.hierarchyLevel = hierarchyLevel;
        return this;
    }

    public void setHierarchyLevel(Integer hierarchyLevel) {
        this.hierarchyLevel = hierarchyLevel;
    }

    public LocalDate getOpeningDate() {
        return this.openingDate;
    }

    public OrganisationUnit openingDate(LocalDate openingDate) {
        this.openingDate = openingDate;
        return this;
    }

    public void setOpeningDate(LocalDate openingDate) {
        this.openingDate = openingDate;
    }

    public String getComment() {
        return this.comment;
    }

    public OrganisationUnit comment(String comment) {
        this.comment = comment;
        return this;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public LocalDate getClosedDate() {
        return this.closedDate;
    }

    public OrganisationUnit closedDate(LocalDate closedDate) {
        this.closedDate = closedDate;
        return this;
    }

    public void setClosedDate(LocalDate closedDate) {
        this.closedDate = closedDate;
    }

    public String getUrl() {
        return this.url;
    }

    public OrganisationUnit url(String url) {
        this.url = url;
        return this;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getContactPerson() {
        return this.contactPerson;
    }

    public OrganisationUnit contactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
        return this;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getAddress() {
        return this.address;
    }

    public OrganisationUnit address(String address) {
        this.address = address;
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return this.email;
    }

    public OrganisationUnit email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public OrganisationUnit phoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public OrganisationUnitType getOrganisationUnitType() {
        return this.organisationUnitType;
    }

    public OrganisationUnit organisationUnitType(OrganisationUnitType organisationUnitType) {
        this.organisationUnitType = organisationUnitType;
        return this;
    }

    public void setOrganisationUnitType(OrganisationUnitType organisationUnitType) {
        this.organisationUnitType = organisationUnitType;
    }

    public Set<MalariaCasesReport> getMalariaReports() {
        return this.malariaReports;
    }

    public OrganisationUnit malariaReports(Set<MalariaCasesReport> malariaCasesReports) {
        this.setMalariaReports(malariaCasesReports);
        return this;
    }

    public OrganisationUnit addMalariaReport(MalariaCasesReport malariaCasesReport) {
        this.malariaReports.add(malariaCasesReport);
        malariaCasesReport.setOrganisationUnit(this);
        return this;
    }

    public OrganisationUnit removeMalariaReport(MalariaCasesReport malariaCasesReport) {
        this.malariaReports.remove(malariaCasesReport);
        malariaCasesReport.setOrganisationUnit(null);
        return this;
    }

    public void setMalariaReports(Set<MalariaCasesReport> malariaCasesReports) {
        if (this.malariaReports != null) {
            this.malariaReports.forEach(i -> i.setOrganisationUnit(null));
        }
        if (malariaCasesReports != null) {
            malariaCasesReports.forEach(i -> i.setOrganisationUnit(this));
        }
        this.malariaReports = malariaCasesReports;
    }

    public Set<DengueCasesReport> getDengueReports() {
        return this.dengueReports;
    }

    public OrganisationUnit dengueReports(Set<DengueCasesReport> dengueCasesReports) {
        this.setDengueReports(dengueCasesReports);
        return this;
    }

    public OrganisationUnit addDengueReport(DengueCasesReport dengueCasesReport) {
        this.dengueReports.add(dengueCasesReport);
        dengueCasesReport.setOrganisationUnit(this);
        return this;
    }

    public OrganisationUnit removeDengueReport(DengueCasesReport dengueCasesReport) {
        this.dengueReports.remove(dengueCasesReport);
        dengueCasesReport.setOrganisationUnit(null);
        return this;
    }

    public void setDengueReports(Set<DengueCasesReport> dengueCasesReports) {
        if (this.dengueReports != null) {
            this.dengueReports.forEach(i -> i.setOrganisationUnit(null));
        }
        if (dengueCasesReports != null) {
            dengueCasesReports.forEach(i -> i.setOrganisationUnit(this));
        }
        this.dengueReports = dengueCasesReports;
    }

    public OrganisationUnit getParent() {
        return this.parent;
    }

    public OrganisationUnit parent(OrganisationUnit organisationUnit) {
        this.setParent(organisationUnit);
        return this;
    }

    public void setParent(OrganisationUnit organisationUnit) {
        this.parent = organisationUnit;
    }

    public OrganisationUnit getHfHomeSubVillage() {
        return this.hfHomeSubVillage;
    }

    public OrganisationUnit hfHomeSubVillage(OrganisationUnit organisationUnit) {
        this.setHfHomeSubVillage(organisationUnit);
        return this;
    }

    public void setHfHomeSubVillage(OrganisationUnit organisationUnit) {
        this.hfHomeSubVillage = organisationUnit;
    }

    public OrganisationUnit getCoveredByHf() {
        return this.coveredByHf;
    }

    public OrganisationUnit coveredByHf(OrganisationUnit organisationUnit) {
        this.setCoveredByHf(organisationUnit);
        return this;
    }

    public void setCoveredByHf(OrganisationUnit organisationUnit) {
        this.coveredByHf = organisationUnit;
    }

    public User getUser() {
        return this.user;
    }

    public OrganisationUnit user(User user) {
        this.setUser(user);
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getLastUpdatedBy() {
        return this.lastUpdatedBy;
    }

    public OrganisationUnit lastUpdatedBy(User user) {
        this.setLastUpdatedBy(user);
        return this;
    }

    public void setLastUpdatedBy(User user) {
        this.lastUpdatedBy = user;
    }

    public MalariaUnit getMalariaUnit() {
        return this.malariaUnit;
    }

    public OrganisationUnit malariaUnit(MalariaUnit malariaUnit) {
        this.setMalariaUnit(malariaUnit);
        return this;
    }

    public void setMalariaUnit(MalariaUnit malariaUnit) {
        this.malariaUnit = malariaUnit;
    }

    public CHV getAssignedChv() {
        return this.assignedChv;
    }

    public OrganisationUnit assignedChv(CHV cHV) {
        this.setAssignedChv(cHV);
        return this;
    }

    public void setAssignedChv(CHV cHV) {
        this.assignedChv = cHV;
    }

    public Set<OrganisationUnit> getChildren() {
        return this.children;
    }

    public OrganisationUnit children(Set<OrganisationUnit> organisationUnits) {
        this.setChildren(organisationUnits);
        return this;
    }

    public OrganisationUnit addChildren(OrganisationUnit organisationUnit) {
        this.children.add(organisationUnit);
        organisationUnit.setParent(this);
        return this;
    }

    public OrganisationUnit removeChildren(OrganisationUnit organisationUnit) {
        this.children.remove(organisationUnit);
        organisationUnit.setParent(null);
        return this;
    }

    public void setChildren(Set<OrganisationUnit> organisationUnits) {
        if (this.children != null) {
            this.children.forEach(i -> i.setParent(null));
        }
        if (organisationUnits != null) {
            organisationUnits.forEach(i -> i.setParent(this));
        }
        this.children = organisationUnits;
    }

    public Set<DemographicData> getDemographicData() {
        return this.demographicData;
    }

    public OrganisationUnit demographicData(Set<DemographicData> demographicData) {
        this.setDemographicData(demographicData);
        return this;
    }

    public OrganisationUnit addDemographicData(DemographicData demographicData) {
        this.demographicData.add(demographicData);
        demographicData.setOrganisationUnit(this);
        return this;
    }

    public OrganisationUnit removeDemographicData(DemographicData demographicData) {
        this.demographicData.remove(demographicData);
        demographicData.setOrganisationUnit(null);
        return this;
    }

    public void setDemographicData(Set<DemographicData> demographicData) {
        if (this.demographicData != null) {
            this.demographicData.forEach(i -> i.setOrganisationUnit(null));
        }
        if (demographicData != null) {
            demographicData.forEach(i -> i.setOrganisationUnit(this));
        }
        this.demographicData = demographicData;
    }

    public Set<OrganisationUnitGroup> getGroups() {
        return this.groups;
    }

    public OrganisationUnit groups(Set<OrganisationUnitGroup> organisationUnitGroups) {
        this.setGroups(organisationUnitGroups);
        return this;
    }

    public OrganisationUnit addGroup(OrganisationUnitGroup organisationUnitGroup) {
        this.groups.add(organisationUnitGroup);
        organisationUnitGroup.getMembers().add(this);
        return this;
    }

    public OrganisationUnit removeGroup(OrganisationUnitGroup organisationUnitGroup) {
        this.groups.remove(organisationUnitGroup);
        organisationUnitGroup.getMembers().remove(this);
        return this;
    }

    public void setGroups(Set<OrganisationUnitGroup> organisationUnitGroups) {
        if (this.groups != null) {
            this.groups.forEach(i -> i.removeMember(this));
        }
        if (organisationUnitGroups != null) {
            organisationUnitGroups.forEach(i -> i.addMember(this));
        }
        this.groups = organisationUnitGroups;
    }

    public Set<Person> getPeople() {
        return this.people;
    }

    public OrganisationUnit people(Set<Person> people) {
        this.setPeople(people);
        return this;
    }

    public OrganisationUnit addPerson(Person person) {
        this.people.add(person);
        person.getOrganisationUnits().add(this);
        return this;
    }

    public OrganisationUnit removePerson(Person person) {
        this.people.remove(person);
        person.getOrganisationUnits().remove(this);
        return this;
    }

    public void setPeople(Set<Person> people) {
        if (this.people != null) {
            this.people.forEach(i -> i.removeOrganisationUnit(this));
        }
        if (people != null) {
            people.forEach(i -> i.addOrganisationUnit(this));
        }
        this.people = people;
    }

    public Set<Person> getDataViewPeople() {
        return this.dataViewPeople;
    }

    public OrganisationUnit dataViewPeople(Set<Person> people) {
        this.setDataViewPeople(people);
        return this;
    }

    public OrganisationUnit addDataViewPerson(Person person) {
        this.dataViewPeople.add(person);
        person.getDataViewOrganisationUnits().add(this);
        return this;
    }

    public OrganisationUnit removeDataViewPerson(Person person) {
        this.dataViewPeople.remove(person);
        person.getDataViewOrganisationUnits().remove(this);
        return this;
    }

    public void setDataViewPeople(Set<Person> people) {
        if (this.dataViewPeople != null) {
            this.dataViewPeople.forEach(i -> i.removeDataViewOrganisationUnit(this));
        }
        if (people != null) {
            people.forEach(i -> i.addDataViewOrganisationUnit(this));
        }
        this.dataViewPeople = people;
    }

    public Set<DataSet> getDataSets() {
        return this.dataSets;
    }

    public OrganisationUnit dataSets(Set<DataSet> dataSets) {
        this.setDataSets(dataSets);
        return this;
    }

    public OrganisationUnit addDataSet(DataSet dataSet) {
        this.dataSets.add(dataSet);
        dataSet.getSources().add(this);
        return this;
    }

    public OrganisationUnit removeDataSet(DataSet dataSet) {
        this.dataSets.remove(dataSet);
        dataSet.getSources().remove(this);
        return this;
    }

    public void setDataSets(Set<DataSet> dataSets) {
        if (this.dataSets != null) {
            this.dataSets.forEach(i -> i.removeSource(this));
        }
        if (dataSets != null) {
            dataSets.forEach(i -> i.addSource(this));
        }
        this.dataSets = dataSets;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrganisationUnit)) {
            return false;
        }
        return id != null && id.equals(((OrganisationUnit) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OrganisationUnit{" +
            "id=" + getId() +
            ", uid='" + getUid() + "'" +
            ", code='" + getCode() + "'" +
            ", name='" + getName() + "'" +
            ", shortName='" + getShortName() + "'" +
            ", created='" + getCreated() + "'" +
            ", lastUpdated='" + getLastUpdated() + "'" +
            ", path='" + getPath() + "'" +
            ", hierarchyLevel=" + getHierarchyLevel() +
            ", openingDate='" + getOpeningDate() + "'" +
            ", comment='" + getComment() + "'" +
            ", closedDate='" + getClosedDate() + "'" +
            ", url='" + getUrl() + "'" +
            ", contactPerson='" + getContactPerson() + "'" +
            ", address='" + getAddress() + "'" +
            ", email='" + getEmail() + "'" +
            ", phoneNumber='" + getPhoneNumber() + "'" +
            ", organisationUnitType='" + getOrganisationUnitType() + "'" +
            "}";
    }
}
