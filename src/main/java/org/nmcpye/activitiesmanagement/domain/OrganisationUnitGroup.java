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
 * A OrganisationUnitGroup.
 */
@Entity
@Table(name = "orgunit_group")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class OrganisationUnitGroup implements Serializable {

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

    @Column(name = "symbol")
    private String symbol;

    @Column(name = "color")
    private String color;

    @ManyToOne
    private User user;

    @ManyToOne
    private User lastUpdatedBy;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(
        name = "rel_orgunit_group__member",
        joinColumns = @JoinColumn(name = "orgunit_group_id"),
        inverseJoinColumns = @JoinColumn(name = "member_id")
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
    private Set<OrganisationUnit> members = new HashSet<>();

    @ManyToMany(mappedBy = "organisationUnitGroups")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "user", "lastUpdatedBy", "organisationUnitGroups" }, allowSetters = true)
    private Set<OrganisationUnitGroupSet> groupSets = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OrganisationUnitGroup id(Long id) {
        this.id = id;
        return this;
    }

    public String getUid() {
        return this.uid;
    }

    public OrganisationUnitGroup uid(String uid) {
        this.uid = uid;
        return this;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getCode() {
        return this.code;
    }

    public OrganisationUnitGroup code(String code) {
        this.code = code;
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return this.name;
    }

    public OrganisationUnitGroup name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortName() {
        return this.shortName;
    }

    public OrganisationUnitGroup shortName(String shortName) {
        this.shortName = shortName;
        return this;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public Instant getCreated() {
        return this.created;
    }

    public OrganisationUnitGroup created(Instant created) {
        this.created = created;
        return this;
    }

    public void setCreated(Instant created) {
        this.created = created;
    }

    public Instant getLastUpdated() {
        return this.lastUpdated;
    }

    public OrganisationUnitGroup lastUpdated(Instant lastUpdated) {
        this.lastUpdated = lastUpdated;
        return this;
    }

    public void setLastUpdated(Instant lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getSymbol() {
        return this.symbol;
    }

    public OrganisationUnitGroup symbol(String symbol) {
        this.symbol = symbol;
        return this;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getColor() {
        return this.color;
    }

    public OrganisationUnitGroup color(String color) {
        this.color = color;
        return this;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public User getUser() {
        return this.user;
    }

    public OrganisationUnitGroup user(User user) {
        this.setUser(user);
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getLastUpdatedBy() {
        return this.lastUpdatedBy;
    }

    public OrganisationUnitGroup lastUpdatedBy(User user) {
        this.setLastUpdatedBy(user);
        return this;
    }

    public void setLastUpdatedBy(User user) {
        this.lastUpdatedBy = user;
    }

    public Set<OrganisationUnit> getMembers() {
        return this.members;
    }

    public OrganisationUnitGroup members(Set<OrganisationUnit> organisationUnits) {
        this.setMembers(organisationUnits);
        return this;
    }

    public OrganisationUnitGroup addMember(OrganisationUnit organisationUnit) {
        this.members.add(organisationUnit);
        organisationUnit.getGroups().add(this);
        return this;
    }

    public OrganisationUnitGroup removeMember(OrganisationUnit organisationUnit) {
        this.members.remove(organisationUnit);
        organisationUnit.getGroups().remove(this);
        return this;
    }

    public void setMembers(Set<OrganisationUnit> organisationUnits) {
        this.members = organisationUnits;
    }

    public Set<OrganisationUnitGroupSet> getGroupSets() {
        return this.groupSets;
    }

    public OrganisationUnitGroup groupSets(Set<OrganisationUnitGroupSet> organisationUnitGroupSets) {
        this.setGroupSets(organisationUnitGroupSets);
        return this;
    }

    public OrganisationUnitGroup addGroupSet(OrganisationUnitGroupSet organisationUnitGroupSet) {
        this.groupSets.add(organisationUnitGroupSet);
        organisationUnitGroupSet.getOrganisationUnitGroups().add(this);
        return this;
    }

    public OrganisationUnitGroup removeGroupSet(OrganisationUnitGroupSet organisationUnitGroupSet) {
        this.groupSets.remove(organisationUnitGroupSet);
        organisationUnitGroupSet.getOrganisationUnitGroups().remove(this);
        return this;
    }

    public void setGroupSets(Set<OrganisationUnitGroupSet> organisationUnitGroupSets) {
        if (this.groupSets != null) {
            this.groupSets.forEach(i -> i.removeOrganisationUnitGroup(this));
        }
        if (organisationUnitGroupSets != null) {
            organisationUnitGroupSets.forEach(i -> i.addOrganisationUnitGroup(this));
        }
        this.groupSets = organisationUnitGroupSets;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrganisationUnitGroup)) {
            return false;
        }
        return id != null && id.equals(((OrganisationUnitGroup) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OrganisationUnitGroup{" +
            "id=" + getId() +
            ", uid='" + getUid() + "'" +
            ", code='" + getCode() + "'" +
            ", name='" + getName() + "'" +
            ", shortName='" + getShortName() + "'" +
            ", created='" + getCreated() + "'" +
            ", lastUpdated='" + getLastUpdated() + "'" +
            ", symbol='" + getSymbol() + "'" +
            ", color='" + getColor() + "'" +
            "}";
    }
}
