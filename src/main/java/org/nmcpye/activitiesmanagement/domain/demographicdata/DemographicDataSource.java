package org.nmcpye.activitiesmanagement.domain.demographicdata;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonRootName;
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
//
//    private static final long serialVersionUID = 1L;
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
//    @SequenceGenerator(name = "sequenceGenerator")
//    private Long id;
//
//    @NotNull
//    @Size(max = 11)
//    @Column(name = "uid", length = 11, nullable = false, unique = true)
//    private String uid;
//
//    @Column(name = "code", unique = true)
//    private String code;
//
//    @Column(name = "name")
//    private String name;
//
//    @Column(name = "created")
//    private Instant created;
//
//    @Column(name = "last_updated")
//    private Instant lastUpdated;

    @OneToMany(mappedBy = "source")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "organisationUnit", "user", "createdBy", "lastUpdatedBy", "source" }, allowSetters = true)
    private Set<DemographicData> demographicData = new HashSet<>();

//    @ManyToOne
//    private User user;
//
//    @ManyToOne
//    private User lastUpdatedBy;

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
}
