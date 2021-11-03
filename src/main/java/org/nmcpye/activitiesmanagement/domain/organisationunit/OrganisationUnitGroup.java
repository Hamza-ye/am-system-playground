package org.nmcpye.activitiesmanagement.domain.organisationunit;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.vividsolutions.jts.geom.Geometry;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.nmcpye.activitiesmanagement.extended.common.BaseDimensionalItemObject;
import org.nmcpye.activitiesmanagement.extended.common.BaseIdentifiableObject;
import org.nmcpye.activitiesmanagement.extended.common.DimensionItemType;
import org.nmcpye.activitiesmanagement.extended.common.MetadataObject;
import org.nmcpye.activitiesmanagement.extended.common.coordinate.CoordinateObject;
import org.nmcpye.activitiesmanagement.extended.common.coordinate.CoordinateUtils;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

/**
 * An OrganisationUnitGroup.
 * @author Hamza Assada
 */
@Entity
@Table(name = "orgunit_group")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class OrganisationUnitGroup extends BaseDimensionalItemObject implements MetadataObject, CoordinateObject {

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
}
