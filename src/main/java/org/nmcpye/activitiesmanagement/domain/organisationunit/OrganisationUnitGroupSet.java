package org.nmcpye.activitiesmanagement.domain.organisationunit;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.nmcpye.activitiesmanagement.domain.User;
import org.nmcpye.activitiesmanagement.extended.common.*;
import org.nmcpye.activitiesmanagement.extended.common.adapter.JacksonOrganisationUnitGroupSymbolSerializer;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.*;

/**
 * An OrganisationUnitGroupSet.
 * @author Hamza Assada
 */
@Entity
@Table(name = "orgunit_groupset")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@JsonRootName( value = "organisationUnitGroupSet", namespace = DxfNamespaces.DXF_2_0 )
public class OrganisationUnitGroupSet extends BaseDimensionalObject implements MetadataObject {

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

    @Column(name = "name")
    private String name;

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

    @Size(max = 50)
    @Column(name = "short_name", length = 50)
    private String shortName;

    @Column(name = "description")
    protected String description;

    /**
     * Indicates whether this object should be handled as a data dimension.
     */
    @Column(name = "data_dimension")
    protected boolean dataDimension = true;

    /**
     * The name of this dimension. For the dynamic dimensions this will be equal
     * to dimension identifier. For the period dimension, this will reflect the
     * period type. For the org unit dimension, this will reflect the level.
     */
    @Column(name = "dimension_name")
    private String dimensionName;

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
    @JsonIgnoreProperties(value = { "user", "createdBy", "lastUpdatedBy", "members", "groupSets" }, allowSetters = true)
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

    @Override
    @JsonProperty
    public boolean isDataDimension() {
        return dataDimension;
    }

    @Override
    public void setDataDimension(boolean dataDimension) {
        this.dataDimension = dataDimension;
    }

    @Override
    public String getDimensionName() {
        return dimensionName != null ? dimensionName : uid;
    }

    @Override
    public void setDimensionName(String dimensionName) {
        this.dimensionName = dimensionName;
    }

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

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

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

    @Override
    public void setLastUpdatedBy(User lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    @Override
    public User getUser() {
        return createdBy;
    }

    @Override
    public void setUser(User user) {
        setCreatedBy( createdBy == null ? user : createdBy );
    }
}
