package org.nmcpye.activitiesmanagement.domain.organisationunit;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.nmcpye.activitiesmanagement.extended.common.*;
import org.nmcpye.activitiesmanagement.extended.common.adapter.JacksonOrganisationUnitGroupSymbolSerializer;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.*;

/**
 * An OrganisationUnitGroupSet.
 * @author Hamza Assada
 */
@Entity
@Table(name = "orgunit_groupset")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class OrganisationUnitGroupSet extends BaseDimensionalObject implements MetadataObject {

    @Size(max = 50)
    @Column(name = "short_name", length = 50)
    private String shortName;

    @Column(name = "description")
    protected String description;

    @Column(name = "compulsory")
    private Boolean compulsory;

    @Column(name = "include_subhierarchy_in_analytics")
    private Boolean includeSubhierarchyInAnalytics;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(
        name = "orgunit_groupset_members",
        joinColumns = @JoinColumn(name = "orgunit_groupset_id"),
        inverseJoinColumns = @JoinColumn(name = "orgunit_group_id")
    )
    @JsonIgnoreProperties(value = { "user", "lastUpdatedBy", "members", "groupSets" }, allowSetters = true)
    private Set<OrganisationUnitGroup> organisationUnitGroups = new HashSet<>();

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    public OrganisationUnitGroupSet() {}

    public OrganisationUnitGroupSet(String name, String description, boolean compulsory) {
        this.name = name;
        this.description = description;
        this.compulsory = compulsory;
        this.includeSubhierarchyInAnalytics = false;
    }

    // -------------------------------------------------------------------------
    // Logic
    // -------------------------------------------------------------------------

    public void addOrganisationUnitGroup(OrganisationUnitGroup organisationUnitGroup) {
        organisationUnitGroups.add(organisationUnitGroup);
        organisationUnitGroup.getGroupSets().add(this);
    }

    public void removeOrganisationUnitGroup(OrganisationUnitGroup organisationUnitGroup) {
        organisationUnitGroups.remove(organisationUnitGroup);
        organisationUnitGroup.getGroupSets().remove(this);
    }

    public void removeAllOrganisationUnitGroups() {
        for (OrganisationUnitGroup group : organisationUnitGroups) {
            group.getGroupSets().remove(this);
        }

        organisationUnitGroups.clear();
    }

    public Collection<OrganisationUnit> getOrganisationUnits() {
        List<OrganisationUnit> units = new ArrayList<>();

        for (OrganisationUnitGroup group : organisationUnitGroups) {
            units.addAll(group.getMembers());
        }

        return units;
    }

    public boolean isMemberOfOrganisationUnitGroups(OrganisationUnit organisationUnit) {
        for (OrganisationUnitGroup group : organisationUnitGroups) {
            if (group.getMembers().contains(organisationUnit)) {
                return true;
            }
        }

        return false;
    }

    public boolean hasOrganisationUnitGroups() {
        return organisationUnitGroups != null && organisationUnitGroups.size() > 0;
    }

    public OrganisationUnitGroup getGroup(OrganisationUnit unit) {
        for (OrganisationUnitGroup group : organisationUnitGroups) {
            if (group.getMembers().contains(unit)) {
                return group;
            }
        }

        return null;
    }

    public List<OrganisationUnitGroup> getSortedGroups() {
        List<OrganisationUnitGroup> sortedGroups = new ArrayList<>(organisationUnitGroups);

        Collections.sort(sortedGroups);

        return sortedGroups;
    }

    @Override
    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    @JsonProperty
    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @JsonProperty
    @Override
    public String getShortName() {
        if (getName() == null || getName().length() <= 50) {
            return getName();
        } else {
            return getName().substring(0, 49);
        }
    }

    // -------------------------------------------------------------------------
    // Dimensional object
    // -------------------------------------------------------------------------

    @Override
    @JsonProperty
    @JsonSerialize(contentAs = BaseDimensionalItemObject.class)
    public List<DimensionalItemObject> getItems() {
        return new ArrayList<>(organisationUnitGroups);
    }

    @Override
    public DimensionType getDimensionType() {
        return DimensionType.ORGANISATION_UNIT_GROUP_SET;
    }

    // -------------------------------------------------------------------------
    // Getters and setters
    // -------------------------------------------------------------------------

    @JsonProperty
    public boolean isCompulsory() {
        return compulsory;
    }

    public void setCompulsory(boolean compulsory) {
        this.compulsory = compulsory;
    }

    @JsonProperty
    public boolean isIncludeSubhierarchyInAnalytics() {
        return includeSubhierarchyInAnalytics;
    }

    public void setIncludeSubhierarchyInAnalytics(boolean includeSubhierarchyInAnalytics) {
        this.includeSubhierarchyInAnalytics = includeSubhierarchyInAnalytics;
    }

    @JsonProperty("organisationUnitGroups")
    // @JsonSerialize(contentAs = BaseIdentifiableObject.class)
    @JsonSerialize(contentUsing = JacksonOrganisationUnitGroupSymbolSerializer.class)
    public Set<OrganisationUnitGroup> getOrganisationUnitGroups() {
        return organisationUnitGroups;
    }

    public void setOrganisationUnitGroups(Set<OrganisationUnitGroup> organisationUnitGroups) {
        this.organisationUnitGroups = organisationUnitGroups;
    }
}
