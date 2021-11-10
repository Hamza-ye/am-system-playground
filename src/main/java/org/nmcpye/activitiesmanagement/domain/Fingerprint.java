package org.nmcpye.activitiesmanagement.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Fingerprint.
 */
@Entity
@Table(name = "fingerprint")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Fingerprint implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Size(max = 11)
    @Column(name = "uid", length = 11, nullable = false, unique = true)
    private String uid;

    @Column(name = "description")
    private String description;

    @Column(name = "created")
    private Instant created;

    @Column(name = "last_updated")
    private Instant lastUpdated;

    @Column(name = "fingerprint_url")
    private String fingerprintUrl;

    @Column(name = "fingerprint_owner")
    private String fingerprintOwner;

    @ManyToOne
    private User user;

    @ManyToOne
    private User lastUpdatedBy;

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "familyHeads", "dataProviders", "fingerprints", "llinsFamilyTargets", "organisationUnit", "user", "createdBy", "lastUpdatedBy" },
        allowSetters = true
    )
    private Family family;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Fingerprint id(Long id) {
        this.id = id;
        return this;
    }

    public String getUid() {
        return this.uid;
    }

    public Fingerprint uid(String uid) {
        this.uid = uid;
        return this;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getDescription() {
        return this.description;
    }

    public Fingerprint description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getCreated() {
        return this.created;
    }

    public Fingerprint created(Instant created) {
        this.created = created;
        return this;
    }

    public void setCreated(Instant created) {
        this.created = created;
    }

    public Instant getLastUpdated() {
        return this.lastUpdated;
    }

    public Fingerprint lastUpdated(Instant lastUpdated) {
        this.lastUpdated = lastUpdated;
        return this;
    }

    public void setLastUpdated(Instant lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getFingerprintUrl() {
        return this.fingerprintUrl;
    }

    public Fingerprint fingerprintUrl(String fingerprintUrl) {
        this.fingerprintUrl = fingerprintUrl;
        return this;
    }

    public void setFingerprintUrl(String fingerprintUrl) {
        this.fingerprintUrl = fingerprintUrl;
    }

    public String getFingerprintOwner() {
        return this.fingerprintOwner;
    }

    public Fingerprint fingerprintOwner(String fingerprintOwner) {
        this.fingerprintOwner = fingerprintOwner;
        return this;
    }

    public void setFingerprintOwner(String fingerprintOwner) {
        this.fingerprintOwner = fingerprintOwner;
    }

    public User getUser() {
        return this.user;
    }

    public Fingerprint user(User user) {
        this.setUser(user);
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getLastUpdatedBy() {
        return this.lastUpdatedBy;
    }

    public Fingerprint lastUpdatedBy(User user) {
        this.setLastUpdatedBy(user);
        return this;
    }

    public void setLastUpdatedBy(User user) {
        this.lastUpdatedBy = user;
    }

    public Family getFamily() {
        return this.family;
    }

    public Fingerprint family(Family family) {
        this.setFamily(family);
        return this;
    }

    public void setFamily(Family family) {
        this.family = family;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Fingerprint)) {
            return false;
        }
        return id != null && id.equals(((Fingerprint) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Fingerprint{" +
            "id=" + getId() +
            ", uid='" + getUid() + "'" +
            ", description='" + getDescription() + "'" +
            ", created='" + getCreated() + "'" +
            ", lastUpdated='" + getLastUpdated() + "'" +
            ", fingerprintUrl='" + getFingerprintUrl() + "'" +
            ", fingerprintOwner='" + getFingerprintOwner() + "'" +
            "}";
    }
}
