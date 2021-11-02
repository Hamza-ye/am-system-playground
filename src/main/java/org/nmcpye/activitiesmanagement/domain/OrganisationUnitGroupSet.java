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
 * A OrganisationUnitGroupSet.
 */
@Entity
@Table(name = "orgunit_groupset")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class OrganisationUnitGroupSet implements Serializable {

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

    @Column(name = "created")
    private Instant created;

    @Column(name = "last_updated")
    private Instant lastUpdated;

    @Column(name = "compulsory")
    private Boolean compulsory;

    @Column(name = "include_subhierarchy_in_analytics")
    private Boolean includeSubhierarchyInAnalytics;

    @ManyToOne
    private User user;

    @ManyToOne
    private User lastUpdatedBy;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(
        name = "rel_orgunit_groupset__organisation_unit_group",
        joinColumns = @JoinColumn(name = "orgunit_groupset_id"),
        inverseJoinColumns = @JoinColumn(name = "organisation_unit_group_id")
    )
    @JsonIgnoreProperties(value = { "user", "lastUpdatedBy", "members", "groupSets" }, allowSetters = true)
    private Set<OrganisationUnitGroup> organisationUnitGroups = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OrganisationUnitGroupSet id(Long id) {
        this.id = id;
        return this;
    }

    public String getUid() {
        return this.uid;
    }

    public OrganisationUnitGroupSet uid(String uid) {
        this.uid = uid;
        return this;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getCode() {
        return this.code;
    }

    public OrganisationUnitGroupSet code(String code) {
        this.code = code;
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return this.name;
    }

    public OrganisationUnitGroupSet name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Instant getCreated() {
        return this.created;
    }

    public OrganisationUnitGroupSet created(Instant created) {
        this.created = created;
        return this;
    }

    public void setCreated(Instant created) {
        this.created = created;
    }

    public Instant getLastUpdated() {
        return this.lastUpdated;
    }

    public OrganisationUnitGroupSet lastUpdated(Instant lastUpdated) {
        this.lastUpdated = lastUpdated;
        return this;
    }

    public void setLastUpdated(Instant lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public Boolean getCompulsory() {
        return this.compulsory;
    }

    public OrganisationUnitGroupSet compulsory(Boolean compulsory) {
        this.compulsory = compulsory;
        return this;
    }

    public void setCompulsory(Boolean compulsory) {
        this.compulsory = compulsory;
    }

    public Boolean getIncludeSubhierarchyInAnalytics() {
        return this.includeSubhierarchyInAnalytics;
    }

    public OrganisationUnitGroupSet includeSubhierarchyInAnalytics(Boolean includeSubhierarchyInAnalytics) {
        this.includeSubhierarchyInAnalytics = includeSubhierarchyInAnalytics;
        return this;
    }

    public void setIncludeSubhierarchyInAnalytics(Boolean includeSubhierarchyInAnalytics) {
        this.includeSubhierarchyInAnalytics = includeSubhierarchyInAnalytics;
    }

    public User getUser() {
        return this.user;
    }

    public OrganisationUnitGroupSet user(User user) {
        this.setUser(user);
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getLastUpdatedBy() {
        return this.lastUpdatedBy;
    }

    public OrganisationUnitGroupSet lastUpdatedBy(User user) {
        this.setLastUpdatedBy(user);
        return this;
    }

    public void setLastUpdatedBy(User user) {
        this.lastUpdatedBy = user;
    }

    public Set<OrganisationUnitGroup> getOrganisationUnitGroups() {
        return this.organisationUnitGroups;
    }

    public OrganisationUnitGroupSet organisationUnitGroups(Set<OrganisationUnitGroup> organisationUnitGroups) {
        this.setOrganisationUnitGroups(organisationUnitGroups);
        return this;
    }

    public OrganisationUnitGroupSet addOrganisationUnitGroup(OrganisationUnitGroup organisationUnitGroup) {
        this.organisationUnitGroups.add(organisationUnitGroup);
        organisationUnitGroup.getGroupSets().add(this);
        return this;
    }

    public OrganisationUnitGroupSet removeOrganisationUnitGroup(OrganisationUnitGroup organisationUnitGroup) {
        this.organisationUnitGroups.remove(organisationUnitGroup);
        organisationUnitGroup.getGroupSets().remove(this);
        return this;
    }

    public void setOrganisationUnitGroups(Set<OrganisationUnitGroup> organisationUnitGroups) {
        this.organisationUnitGroups = organisationUnitGroups;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrganisationUnitGroupSet)) {
            return false;
        }
        return id != null && id.equals(((OrganisationUnitGroupSet) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OrganisationUnitGroupSet{" +
            "id=" + getId() +
            ", uid='" + getUid() + "'" +
            ", code='" + getCode() + "'" +
            ", name='" + getName() + "'" +
            ", created='" + getCreated() + "'" +
            ", lastUpdated='" + getLastUpdated() + "'" +
            ", compulsory='" + getCompulsory() + "'" +
            ", includeSubhierarchyInAnalytics='" + getIncludeSubhierarchyInAnalytics() + "'" +
            "}";
    }
}
