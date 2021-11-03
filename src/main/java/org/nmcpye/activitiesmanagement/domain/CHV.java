package org.nmcpye.activitiesmanagement.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.nmcpye.activitiesmanagement.domain.organisationunit.OrganisationUnit;
import org.nmcpye.activitiesmanagement.domain.person.Person;

/**
 * A CHV.
 */
@Entity
@Table(name = "chv")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CHV implements Serializable {

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

    @Column(name = "description")
    private String description;

    @Column(name = "created")
    private Instant created;

    @Column(name = "last_updated")
    private Instant lastUpdated;

    @Column(name = "mobile")
    private String mobile;

    @JsonIgnoreProperties(
        value = {
            "userInfo", "user", "lastUpdatedBy", "organisationUnits", "dataViewOrganisationUnits", "personAuthorityGroups", "groups",
        },
        allowSetters = true
    )
    @OneToOne
    @JoinColumn(unique = true)
    private Person person;

    /**
     * When OrgUnit is subvillage, Which CHV covers it
     */
    @ApiModelProperty(value = "When OrgUnit is subvillage, Which CHV covers it")
    @OneToMany(mappedBy = "assignedChv")
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
    private Set<OrganisationUnit> coveredSubVillages = new HashSet<>();

    /**
     * The district CHV belongs to it When OrgUnit is district
     */
    @ApiModelProperty(value = "The district CHV belongs to it When OrgUnit is district")
    @ManyToOne(optional = false)
    @NotNull
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
    private OrganisationUnit district;

    /**
     * When OrgUnit is SubVillage, What CHV it is home to
     */
    @ApiModelProperty(value = "When OrgUnit is SubVillage, What CHV it is home to")
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
    private OrganisationUnit homeSubvillage;

    /**
     * When OrgUnit is HealthFacility, What CHV it manage
     */
    @ApiModelProperty(value = "When OrgUnit is HealthFacility, What CHV it manage")
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
    private OrganisationUnit managedByHf;

    @ManyToOne
    private User user;

    @ManyToOne
    private User lastUpdatedBy;

    @ManyToMany(mappedBy = "responsibleForChvs")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "user", "lastUpdatedBy", "person", "responsibleForChvs" }, allowSetters = true)
    private Set<CHVTeam> supervisionTeams = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CHV id(Long id) {
        this.id = id;
        return this;
    }

    public String getUid() {
        return this.uid;
    }

    public CHV uid(String uid) {
        this.uid = uid;
        return this;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getCode() {
        return this.code;
    }

    public CHV code(String code) {
        this.code = code;
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return this.description;
    }

    public CHV description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getCreated() {
        return this.created;
    }

    public CHV created(Instant created) {
        this.created = created;
        return this;
    }

    public void setCreated(Instant created) {
        this.created = created;
    }

    public Instant getLastUpdated() {
        return this.lastUpdated;
    }

    public CHV lastUpdated(Instant lastUpdated) {
        this.lastUpdated = lastUpdated;
        return this;
    }

    public void setLastUpdated(Instant lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getMobile() {
        return this.mobile;
    }

    public CHV mobile(String mobile) {
        this.mobile = mobile;
        return this;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Person getPerson() {
        return this.person;
    }

    public CHV person(Person person) {
        this.setPerson(person);
        return this;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Set<OrganisationUnit> getCoveredSubVillages() {
        return this.coveredSubVillages;
    }

    public CHV coveredSubVillages(Set<OrganisationUnit> organisationUnits) {
        this.setCoveredSubVillages(organisationUnits);
        return this;
    }

    public CHV addCoveredSubVillage(OrganisationUnit organisationUnit) {
        this.coveredSubVillages.add(organisationUnit);
        organisationUnit.setAssignedChv(this);
        return this;
    }

    public CHV removeCoveredSubVillage(OrganisationUnit organisationUnit) {
        this.coveredSubVillages.remove(organisationUnit);
        organisationUnit.setAssignedChv(null);
        return this;
    }

    public void setCoveredSubVillages(Set<OrganisationUnit> organisationUnits) {
        if (this.coveredSubVillages != null) {
            this.coveredSubVillages.forEach(i -> i.setAssignedChv(null));
        }
        if (organisationUnits != null) {
            organisationUnits.forEach(i -> i.setAssignedChv(this));
        }
        this.coveredSubVillages = organisationUnits;
    }

    public OrganisationUnit getDistrict() {
        return this.district;
    }

    public CHV district(OrganisationUnit organisationUnit) {
        this.setDistrict(organisationUnit);
        return this;
    }

    public void setDistrict(OrganisationUnit organisationUnit) {
        this.district = organisationUnit;
    }

    public OrganisationUnit getHomeSubvillage() {
        return this.homeSubvillage;
    }

    public CHV homeSubvillage(OrganisationUnit organisationUnit) {
        this.setHomeSubvillage(organisationUnit);
        return this;
    }

    public void setHomeSubvillage(OrganisationUnit organisationUnit) {
        this.homeSubvillage = organisationUnit;
    }

    public OrganisationUnit getManagedByHf() {
        return this.managedByHf;
    }

    public CHV managedByHf(OrganisationUnit organisationUnit) {
        this.setManagedByHf(organisationUnit);
        return this;
    }

    public void setManagedByHf(OrganisationUnit organisationUnit) {
        this.managedByHf = organisationUnit;
    }

    public User getUser() {
        return this.user;
    }

    public CHV user(User user) {
        this.setUser(user);
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getLastUpdatedBy() {
        return this.lastUpdatedBy;
    }

    public CHV lastUpdatedBy(User user) {
        this.setLastUpdatedBy(user);
        return this;
    }

    public void setLastUpdatedBy(User user) {
        this.lastUpdatedBy = user;
    }

    public Set<CHVTeam> getSupervisionTeams() {
        return this.supervisionTeams;
    }

    public CHV supervisionTeams(Set<CHVTeam> cHVTeams) {
        this.setSupervisionTeams(cHVTeams);
        return this;
    }

    public CHV addSupervisionTeam(CHVTeam cHVTeam) {
        this.supervisionTeams.add(cHVTeam);
        cHVTeam.getResponsibleForChvs().add(this);
        return this;
    }

    public CHV removeSupervisionTeam(CHVTeam cHVTeam) {
        this.supervisionTeams.remove(cHVTeam);
        cHVTeam.getResponsibleForChvs().remove(this);
        return this;
    }

    public void setSupervisionTeams(Set<CHVTeam> cHVTeams) {
        if (this.supervisionTeams != null) {
            this.supervisionTeams.forEach(i -> i.removeResponsibleForChv(this));
        }
        if (cHVTeams != null) {
            cHVTeams.forEach(i -> i.addResponsibleForChv(this));
        }
        this.supervisionTeams = cHVTeams;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CHV)) {
            return false;
        }
        return id != null && id.equals(((CHV) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CHV{" +
            "id=" + getId() +
            ", uid='" + getUid() + "'" +
            ", code='" + getCode() + "'" +
            ", description='" + getDescription() + "'" +
            ", created='" + getCreated() + "'" +
            ", lastUpdated='" + getLastUpdated() + "'" +
            ", mobile='" + getMobile() + "'" +
            "}";
    }
}
