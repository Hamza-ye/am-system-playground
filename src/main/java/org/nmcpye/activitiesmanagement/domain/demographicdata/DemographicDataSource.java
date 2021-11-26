package org.nmcpye.activitiesmanagement.domain.demographicdata;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.nmcpye.activitiesmanagement.domain.User;
import org.nmcpye.activitiesmanagement.extended.common.BaseIdentifiableObject;
import org.nmcpye.activitiesmanagement.extended.common.DxfNamespaces;
import org.nmcpye.activitiesmanagement.extended.common.MetadataObject;

/**
 * A DemographicDataSource.
 */
@Entity
@Table(name = "demographic_data_source")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@JsonRootName( value = "demographicDataSource", namespace = DxfNamespaces.DXF_2_0 )
public class DemographicDataSource extends BaseIdentifiableObject implements MetadataObject {

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

    ////////////////////////
    ///
    ///
    ////////////////////////

    @OneToMany(mappedBy = "source")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "organisationUnit", "user", "createdBy", "lastUpdatedBy", "source" }, allowSetters = true)
    private Set<DemographicData> demographicData = new HashSet<>();

    public DemographicDataSource id(Long id) {
        this.id = id;
        return this;
    }

    public DemographicDataSource uid(String uid) {
        this.uid = uid;
        return this;
    }

    public DemographicDataSource code(String code) {
        this.code = code;
        return this;
    }

    public DemographicDataSource name(String name) {
        this.name = name;
        return this;
    }

    public DemographicDataSource created(Date created) {
        this.created = created;
        return this;
    }

    public DemographicDataSource lastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
        return this;
    }

    @JsonProperty
    @JsonSerialize(contentAs = BaseIdentifiableObject.class)
    public Set<DemographicData> getDemographicData() {
        return this.demographicData;
    }

    public DemographicDataSource demographicData(Set<DemographicData> demographicData) {
        this.setDemographicData(demographicData);
        return this;
    }

    public DemographicDataSource addDemographicData(DemographicData demographicData) {
        this.demographicData.add(demographicData);
        demographicData.setSource(this);
        return this;
    }

    public DemographicDataSource removeDemographicData(DemographicData demographicData) {
        this.demographicData.remove(demographicData);
        demographicData.setSource(null);
        return this;
    }

    public void setDemographicData(Set<DemographicData> demographicData) {
        if (this.demographicData != null) {
            this.demographicData.forEach(i -> i.setSource(null));
        }
        if (demographicData != null) {
            demographicData.forEach(i -> i.setSource(this));
        }
        this.demographicData = demographicData;
    }

    public DemographicDataSource user(User user) {
        this.setUser(user);
        return this;
    }

    public DemographicDataSource lastUpdatedBy(User user) {
        this.setLastUpdatedBy(user);
        return this;
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
}
