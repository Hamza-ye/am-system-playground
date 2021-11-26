package org.nmcpye.activitiesmanagement.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.nmcpye.activitiesmanagement.domain.organisationunit.OrganisationUnit;
import org.nmcpye.activitiesmanagement.extended.common.BaseIdentifiableObject;

/**
 * A Family.
 */
@Entity
@Table(name = "family")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Family implements Serializable {

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

    @NotNull
    @Column(name = "family_no", nullable = false)
    private Integer familyNo;

    @Column(name = "address")
    private String address;

    @OneToMany(mappedBy = "family")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "user", "createdBy", "lastUpdatedBy", "family" }, allowSetters = true)
    private Set<FamilyHead> familyHeads = new HashSet<>();

    @OneToMany(mappedBy = "family")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "user", "createdBy", "lastUpdatedBy", "family" }, allowSetters = true)
    private Set<DataProvider> dataProviders = new HashSet<>();

    @OneToMany(mappedBy = "family")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "user", "createdBy", "lastUpdatedBy", "family" }, allowSetters = true)
    private Set<Fingerprint> fingerprints = new HashSet<>();

    @OneToMany(mappedBy = "family")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = { "llinsFamilyReports", "user", "createdBy", "lastUpdatedBy", "dayPlanned", "family", "teamAssigned" },
        allowSetters = true
    )
    private Set<LLINSFamilyTarget> llinsFamilyTargets = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(
        value = {
            "malariaReports",
            "dengueReports",
            "parent",
            "hfHomeSubVillage",
            "coveredByHf",
            "createdBy",
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
    private OrganisationUnit organisationUnit;

    @ManyToOne
    @JoinColumn(name = "created_by")
    private User createdBy;

    @ManyToOne
    @JoinColumn(name = "last_updated_by")
    private User lastUpdatedBy;;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    //    @Override
    @JsonProperty
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Family id(Long id) {
        this.id = id;
        return this;
    }

    //    @Override
    @JsonProperty
    public String getUid() {
        return this.uid;
    }

    public Family uid(String uid) {
        this.uid = uid;
        return this;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    //    @Override
    @JsonProperty
    public String getCode() {
        return this.code;
    }

    public Family code(String code) {
        this.code = code;
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    //    @Override
    @JsonProperty
    public String getName() {
        return this.name;
    }

    public Family name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    //    @Override
    @JsonProperty
    public Instant getCreated() {
        return this.created;
    }

    public Family created(Instant created) {
        this.created = created;
        return this;
    }

    public void setCreated(Instant created) {
        this.created = created;
    }

    //    @Override
    @JsonProperty
    public Instant getLastUpdated() {
        return this.lastUpdated;
    }

    public Family lastUpdated(Instant lastUpdated) {
        this.lastUpdated = lastUpdated;
        return this;
    }

    public void setLastUpdated(Instant lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    @JsonProperty
    public Integer getFamilyNo() {
        return this.familyNo;
    }

    public Family familyNo(Integer familyNo) {
        this.familyNo = familyNo;
        return this;
    }

    public void setFamilyNo(Integer familyNo) {
        this.familyNo = familyNo;
    }

    @JsonProperty
    public String getAddress() {
        return this.address;
    }

    public Family address(String address) {
        this.address = address;
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @JsonProperty
//    @JsonSerialize(contentAs = BaseIdentifiableObject.class)
    public Set<FamilyHead> getFamilyHeads() {
        return this.familyHeads;
    }

    public Family familyHeads(Set<FamilyHead> familyHeads) {
        this.setFamilyHeads(familyHeads);
        return this;
    }

    public Family addFamilyHead(FamilyHead familyHead) {
        this.familyHeads.add(familyHead);
        familyHead.setFamily(this);
        return this;
    }

    public Family removeFamilyHead(FamilyHead familyHead) {
        this.familyHeads.remove(familyHead);
        familyHead.setFamily(null);
        return this;
    }

    public void setFamilyHeads(Set<FamilyHead> familyHeads) {
        if (this.familyHeads != null) {
            this.familyHeads.forEach(i -> i.setFamily(null));
        }
        if (familyHeads != null) {
            familyHeads.forEach(i -> i.setFamily(this));
        }
        this.familyHeads = familyHeads;
    }

    @JsonProperty
//    @JsonSerialize(contentAs = BaseIdentifiableObject.class)
    public Set<DataProvider> getDataProviders() {
        return this.dataProviders;
    }

    public Family dataProviders(Set<DataProvider> dataProviders) {
        this.setDataProviders(dataProviders);
        return this;
    }

    public Family addDataProvider(DataProvider dataProvider) {
        this.dataProviders.add(dataProvider);
        dataProvider.setFamily(this);
        return this;
    }

    public Family removeDataProvider(DataProvider dataProvider) {
        this.dataProviders.remove(dataProvider);
        dataProvider.setFamily(null);
        return this;
    }

    public void setDataProviders(Set<DataProvider> dataProviders) {
        if (this.dataProviders != null) {
            this.dataProviders.forEach(i -> i.setFamily(null));
        }
        if (dataProviders != null) {
            dataProviders.forEach(i -> i.setFamily(this));
        }
        this.dataProviders = dataProviders;
    }

    @JsonProperty
//    @JsonSerialize(contentAs = BaseIdentifiableObject.class)
    public Set<Fingerprint> getFingerprints() {
        return this.fingerprints;
    }

    public Family fingerprints(Set<Fingerprint> fingerprints) {
        this.setFingerprints(fingerprints);
        return this;
    }

    public Family addFingerprint(Fingerprint fingerprint) {
        this.fingerprints.add(fingerprint);
        fingerprint.setFamily(this);
        return this;
    }

    public Family removeFingerprint(Fingerprint fingerprint) {
        this.fingerprints.remove(fingerprint);
        fingerprint.setFamily(null);
        return this;
    }

    public void setFingerprints(Set<Fingerprint> fingerprints) {
        if (this.fingerprints != null) {
            this.fingerprints.forEach(i -> i.setFamily(null));
        }
        if (fingerprints != null) {
            fingerprints.forEach(i -> i.setFamily(this));
        }
        this.fingerprints = fingerprints;
    }

    @JsonProperty
//    @JsonSerialize(contentAs = BaseIdentifiableObject.class)
    public Set<LLINSFamilyTarget> getLlinsFamilyTargets() {
        return this.llinsFamilyTargets;
    }

    public Family llinsFamilyTargets(Set<LLINSFamilyTarget> lLINSFamilyTargets) {
        this.setLlinsFamilyTargets(lLINSFamilyTargets);
        return this;
    }

    public Family addLlinsFamilyTarget(LLINSFamilyTarget lLINSFamilyTarget) {
        this.llinsFamilyTargets.add(lLINSFamilyTarget);
        lLINSFamilyTarget.setFamily(this);
        return this;
    }

    public Family removeLlinsFamilyTarget(LLINSFamilyTarget lLINSFamilyTarget) {
        this.llinsFamilyTargets.remove(lLINSFamilyTarget);
        lLINSFamilyTarget.setFamily(null);
        return this;
    }

    public void setLlinsFamilyTargets(Set<LLINSFamilyTarget> lLINSFamilyTargets) {
        if (this.llinsFamilyTargets != null) {
            this.llinsFamilyTargets.forEach(i -> i.setFamily(null));
        }
        if (lLINSFamilyTargets != null) {
            lLINSFamilyTargets.forEach(i -> i.setFamily(this));
        }
        this.llinsFamilyTargets = lLINSFamilyTargets;
    }

    @JsonProperty
    @JsonSerialize(contentAs = BaseIdentifiableObject.class)
    public OrganisationUnit getOrganisationUnit() {
        return this.organisationUnit;
    }

    public Family organisationUnit(OrganisationUnit organisationUnit) {
        this.setOrganisationUnit(organisationUnit);
        return this;
    }

    public void setOrganisationUnit(OrganisationUnit organisationUnit) {
        this.organisationUnit = organisationUnit;
    }

    @Deprecated
    public Family user(User user) {
        this.setUser(user);
        return this;
    }

//    @Override
    @JsonProperty
    public User getLastUpdatedBy() {
        return this.lastUpdatedBy;
    }

    public Family lastUpdatedBy(User user) {
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
        if (!(o instanceof Family)) {
            return false;
        }
        return id != null && id.equals(((Family) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Family{" +
            "id=" + getId() +
            ", uid='" + getUid() + "'" +
            ", code='" + getCode() + "'" +
            ", name='" + getName() + "'" +
            ", created='" + getCreated() + "'" +
            ", lastUpdated='" + getLastUpdated() + "'" +
            ", familyNo=" + getFamilyNo() +
            ", address='" + getAddress() + "'" +
            "}";
    }

    //    @Override
    @JsonProperty
    public User getCreatedBy() {
        return createdBy;
    }

    //    @Override
    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    //    @Override
    @Deprecated
    @JsonProperty
    public User getUser() {
        return createdBy;
    }

    //    @Override
    @Deprecated
    public void setUser(User user) {
        setCreatedBy(createdBy == null ? user : createdBy);
    }
}
