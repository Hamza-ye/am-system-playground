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
 * A MalariaUnit.
 */
@Entity
@Table(name = "malaria_unit")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class MalariaUnit implements Serializable {

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

    @OneToMany(mappedBy = "malariaUnit")
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
    private Set<OrganisationUnit> organisationUnits = new HashSet<>();

    @OneToMany(mappedBy = "malariaUnit")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "user", "lastUpdatedBy", "person", "malariaUnit" }, allowSetters = true)
    private Set<MalariaUnitStaffMember> malariaUnitStaffMembers = new HashSet<>();

    @ManyToOne
    private User user;

    @ManyToOne
    private User lastUpdatedBy;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MalariaUnit id(Long id) {
        this.id = id;
        return this;
    }

    public String getUid() {
        return this.uid;
    }

    public MalariaUnit uid(String uid) {
        this.uid = uid;
        return this;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getCode() {
        return this.code;
    }

    public MalariaUnit code(String code) {
        this.code = code;
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return this.name;
    }

    public MalariaUnit name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortName() {
        return this.shortName;
    }

    public MalariaUnit shortName(String shortName) {
        this.shortName = shortName;
        return this;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getDescription() {
        return this.description;
    }

    public MalariaUnit description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getCreated() {
        return this.created;
    }

    public MalariaUnit created(Instant created) {
        this.created = created;
        return this;
    }

    public void setCreated(Instant created) {
        this.created = created;
    }

    public Instant getLastUpdated() {
        return this.lastUpdated;
    }

    public MalariaUnit lastUpdated(Instant lastUpdated) {
        this.lastUpdated = lastUpdated;
        return this;
    }

    public void setLastUpdated(Instant lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public Set<OrganisationUnit> getOrganisationUnits() {
        return this.organisationUnits;
    }

    public MalariaUnit organisationUnits(Set<OrganisationUnit> organisationUnits) {
        this.setOrganisationUnits(organisationUnits);
        return this;
    }

    public MalariaUnit addOrganisationUnit(OrganisationUnit organisationUnit) {
        this.organisationUnits.add(organisationUnit);
        organisationUnit.setMalariaUnit(this);
        return this;
    }

    public MalariaUnit removeOrganisationUnit(OrganisationUnit organisationUnit) {
        this.organisationUnits.remove(organisationUnit);
        organisationUnit.setMalariaUnit(null);
        return this;
    }

    public void setOrganisationUnits(Set<OrganisationUnit> organisationUnits) {
        if (this.organisationUnits != null) {
            this.organisationUnits.forEach(i -> i.setMalariaUnit(null));
        }
        if (organisationUnits != null) {
            organisationUnits.forEach(i -> i.setMalariaUnit(this));
        }
        this.organisationUnits = organisationUnits;
    }

    public Set<MalariaUnitStaffMember> getMalariaUnitStaffMembers() {
        return this.malariaUnitStaffMembers;
    }

    public MalariaUnit malariaUnitStaffMembers(Set<MalariaUnitStaffMember> malariaUnitStaffMembers) {
        this.setMalariaUnitStaffMembers(malariaUnitStaffMembers);
        return this;
    }

    public MalariaUnit addMalariaUnitStaffMember(MalariaUnitStaffMember malariaUnitStaffMember) {
        this.malariaUnitStaffMembers.add(malariaUnitStaffMember);
        malariaUnitStaffMember.setMalariaUnit(this);
        return this;
    }

    public MalariaUnit removeMalariaUnitStaffMember(MalariaUnitStaffMember malariaUnitStaffMember) {
        this.malariaUnitStaffMembers.remove(malariaUnitStaffMember);
        malariaUnitStaffMember.setMalariaUnit(null);
        return this;
    }

    public void setMalariaUnitStaffMembers(Set<MalariaUnitStaffMember> malariaUnitStaffMembers) {
        if (this.malariaUnitStaffMembers != null) {
            this.malariaUnitStaffMembers.forEach(i -> i.setMalariaUnit(null));
        }
        if (malariaUnitStaffMembers != null) {
            malariaUnitStaffMembers.forEach(i -> i.setMalariaUnit(this));
        }
        this.malariaUnitStaffMembers = malariaUnitStaffMembers;
    }

    public User getUser() {
        return this.user;
    }

    public MalariaUnit user(User user) {
        this.setUser(user);
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getLastUpdatedBy() {
        return this.lastUpdatedBy;
    }

    public MalariaUnit lastUpdatedBy(User user) {
        this.setLastUpdatedBy(user);
        return this;
    }

    public void setLastUpdatedBy(User user) {
        this.lastUpdatedBy = user;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MalariaUnit)) {
            return false;
        }
        return id != null && id.equals(((MalariaUnit) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MalariaUnit{" +
            "id=" + getId() +
            ", uid='" + getUid() + "'" +
            ", code='" + getCode() + "'" +
            ", name='" + getName() + "'" +
            ", shortName='" + getShortName() + "'" +
            ", description='" + getDescription() + "'" +
            ", created='" + getCreated() + "'" +
            ", lastUpdated='" + getLastUpdated() + "'" +
            "}";
    }
}
