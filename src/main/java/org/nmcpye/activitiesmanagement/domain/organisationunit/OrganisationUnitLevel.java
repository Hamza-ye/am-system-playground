package org.nmcpye.activitiesmanagement.domain.organisationunit;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.nmcpye.activitiesmanagement.extended.common.BaseIdentifiableObject;
import org.nmcpye.activitiesmanagement.extended.common.DxfNamespaces;
import org.nmcpye.activitiesmanagement.extended.common.MetadataObject;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * An OrganisationUnitLevel.
 * @author Hamza Assada
 */
@Entity
@Table(name = "orgunit_level")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@JsonRootName( value = "organisationUnitLevel", namespace = DxfNamespaces.DXF_2_0 )
public class OrganisationUnitLevel extends BaseIdentifiableObject implements MetadataObject {

    @Column(name = "level")
    private Integer level;

    @Column(name = "offline_levels")
    private Integer offlineLevels;

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    public OrganisationUnitLevel() {}

    public OrganisationUnitLevel(int level, String name) {
        this.level = level;
        this.name = name;
    }

    public OrganisationUnitLevel(int level, String name, Integer offlineLevels) {
        this.level = level;
        this.name = name;
        this.offlineLevels = offlineLevels;
    }

    @Override
    public String toString() {
        return "[Name: " + name + ", level: " + level + "]";
    }

    // -------------------------------------------------------------------------
    // Getters and setters
    // -------------------------------------------------------------------------

    @JsonProperty
    //    @PropertyRange( min = 1, max = 999 )
    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    @JsonProperty
    public Integer getOfflineLevels() {
        return offlineLevels;
    }

    public void setOfflineLevels(Integer offlineLevels) {
        this.offlineLevels = offlineLevels;
    }
}
