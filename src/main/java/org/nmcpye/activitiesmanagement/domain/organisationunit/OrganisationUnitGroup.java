package org.nmcpye.activitiesmanagement.domain.organisationunit;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.vividsolutions.jts.geom.Geometry;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.nmcpye.activitiesmanagement.domain.User;
import org.nmcpye.activitiesmanagement.extended.common.*;
import org.nmcpye.activitiesmanagement.extended.common.coordinate.CoordinateObject;
import org.nmcpye.activitiesmanagement.extended.common.coordinate.CoordinateUtils;
import org.nmcpye.activitiesmanagement.extended.schema.annotation.Gist;
import org.nmcpye.activitiesmanagement.extended.schema.annotation.Gist.Include;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * An OrganisationUnitGroup.
 * @author Hamza Assada
 */
@Entity
@Table(name = "orgunit_group")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@JsonRootName( value = "organisationUnitGroup", namespace = DxfNamespaces.DXF_2_0 )
public class OrganisationUnitGroup extends BaseDimensionalItemObject implements MetadataObject, CoordinateObject {

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

    @Column(name = "symbol")
    private String symbol;

    @Column(name = "color")
    private String color;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(
        name = "orgunit_group_members",
        joinColumns = @JoinColumn(name = "orgunit_group_id"),
        inverseJoinColumns = @JoinColumn(name = "organisation_unit_id")
    )
    @JsonIgnoreProperties(
        value = {
            "malariaReports", "dengueReports", "parent", "hfHomeSubVillage", "coveredByHf", "user",
            "createdBy", "lastUpdatedBy", "malariaUnit", "assignedChv", "children", "demographicData",
            "groups", "people", "dataViewPeople", "dataSets",
        },
        allowSetters = true
    )
    private Set<OrganisationUnit> members = new HashSet<>();

    @ManyToMany(mappedBy = "organisationUnitGroups")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "user", "createdBy", "lastUpdatedBy", "organisationUnitGroups" }, allowSetters = true)
    private Set<OrganisationUnitGroupSet> groupSets = new HashSet<>();

    @Column(columnDefinition = "geometry(Geometry,4326)", nullable = true)
    private Geometry geometry;

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    public OrganisationUnitGroup() {}

    public OrganisationUnitGroup(String name) {
        this.name = name;
    }

    // -------------------------------------------------------------------------
    // Logic
    // -------------------------------------------------------------------------

    public boolean addOrganisationUnit(OrganisationUnit organisationUnit) {
        members.add(organisationUnit);
        return organisationUnit.getGroups().add(this);
    }

    public boolean removeOrganisationUnit(OrganisationUnit organisationUnit) {
        members.remove(organisationUnit);
        return organisationUnit.getGroups().remove(this);
    }

    public void removeAllOrganisationUnits() {
        for (OrganisationUnit organisationUnit : members) {
            organisationUnit.getGroups().remove(this);
        }

        members.clear();
    }

    public void updateOrganisationUnits(Set<OrganisationUnit> updates) {
        for (OrganisationUnit unit : new HashSet<>(members)) {
            if (!updates.contains(unit)) {
                removeOrganisationUnit(unit);
            }
        }

        for (OrganisationUnit unit : updates) {
            addOrganisationUnit(unit);
        }
    }

    public boolean hasSymbol() {
        return symbol != null && !symbol.trim().isEmpty();
    }

    // -------------------------------------------------------------------------
    // DimensionalItemObject
    // -------------------------------------------------------------------------
    @Override
    public DimensionItemType getDimensionItemType() {
        return DimensionItemType.ORGANISATION_UNIT_GROUP;
    }

    // -------------------------------------------------------------------------
    // Getters and setters
    // -------------------------------------------------------------------------


    @JsonProperty
    @Override
    public String getShortName() {
        return shortName;
    }

    @Override
    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    @JsonProperty
    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    @JsonProperty
    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @JsonProperty("organisationUnits")
    @JsonSerialize(contentAs = BaseIdentifiableObject.class)
    public Set<OrganisationUnit> getMembers() {
        return members;
    }

    public void setMembers(Set<OrganisationUnit> members) {
        this.members = members;
    }

    @JsonProperty
    @JsonSerialize(contentAs = BaseIdentifiableObject.class)
    public Set<OrganisationUnitGroupSet> getGroupSets() {
        return groupSets;
    }

    public void setGroupSets(Set<OrganisationUnitGroupSet> groupSets) {
        this.groupSets = groupSets;
    }

    @Gist( included = Include.FALSE )
    @JsonProperty
    public Geometry getGeometry() {
        return geometry;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }

    public boolean hasDescendantsWithCoordinates() {
        return CoordinateUtils.hasDescendantsWithCoordinates(members);
    }

    @JsonProperty
    public FeatureType getFeatureType() {
        return geometry != null ? FeatureType.getTypeFromName(this.geometry.getGeometryType()) : null;
    }

    @Override
    public String getCoordinates() {
        return extractCoordinates(this.geometry);
    }

    @Override
    public boolean hasCoordinates() {
        return this.geometry != null;
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
