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
 * A DemographicDataSource.
 */
@Entity
@Table(name = "demographic_data_source")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class DemographicDataSource implements Serializable {

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

    @OneToMany(mappedBy = "source")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "organisationUnit", "user", "lastUpdatedBy", "source" }, allowSetters = true)
    private Set<DemographicData> demographicData = new HashSet<>();

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

    public DemographicDataSource id(Long id) {
        this.id = id;
        return this;
    }

    public String getUid() {
        return this.uid;
    }

    public DemographicDataSource uid(String uid) {
        this.uid = uid;
        return this;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getCode() {
        return this.code;
    }

    public DemographicDataSource code(String code) {
        this.code = code;
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return this.name;
    }

    public DemographicDataSource name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Instant getCreated() {
        return this.created;
    }

    public DemographicDataSource created(Instant created) {
        this.created = created;
        return this;
    }

    public void setCreated(Instant created) {
        this.created = created;
    }

    public Instant getLastUpdated() {
        return this.lastUpdated;
    }

    public DemographicDataSource lastUpdated(Instant lastUpdated) {
        this.lastUpdated = lastUpdated;
        return this;
    }

    public void setLastUpdated(Instant lastUpdated) {
        this.lastUpdated = lastUpdated;
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

    public User getUser() {
        return this.user;
    }

    public DemographicDataSource user(User user) {
        this.setUser(user);
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getLastUpdatedBy() {
        return this.lastUpdatedBy;
    }

    public DemographicDataSource lastUpdatedBy(User user) {
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
        if (!(o instanceof DemographicDataSource)) {
            return false;
        }
        return id != null && id.equals(((DemographicDataSource) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DemographicDataSource{" +
            "id=" + getId() +
            ", uid='" + getUid() + "'" +
            ", code='" + getCode() + "'" +
            ", name='" + getName() + "'" +
            ", created='" + getCreated() + "'" +
            ", lastUpdated='" + getLastUpdated() + "'" +
            "}";
    }
}
