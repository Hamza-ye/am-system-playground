package org.nmcpye.activitiesmanagement.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.nmcpye.activitiesmanagement.domain.enumeration.FamilyType;

/**
 * A LlinsFamilyReportHistory.
 */
@Entity
@Table(name = "llins_family_report_history")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class LlinsFamilyReportHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Size(max = 11)
    @Column(name = "uid", length = 11, nullable = false, unique = true)
    private String uid;

    @Column(name = "created")
    private Instant created;

    @Column(name = "last_updated")
    private Instant lastUpdated;

    @Column(name = "document_no")
    private Integer documentNo;

    @Min(value = 0)
    @Column(name = "male_individuals")
    private Integer maleIndividuals;

    @Min(value = 0)
    @Column(name = "female_individuals")
    private Integer femaleIndividuals;

    @Min(value = 0)
    @Column(name = "less_than_5_males")
    private Integer lessThan5Males;

    @Min(value = 0)
    @Column(name = "less_than_5_females")
    private Integer lessThan5Females;

    @Min(value = 0)
    @Column(name = "pregnant_women")
    private Integer pregnantWomen;

    @Min(value = 0)
    @Column(name = "quantity_received")
    private Integer quantityReceived;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "family_type", nullable = false)
    private FamilyType familyType;

    @ManyToOne
    @JoinColumn(name = "created_by")
    private User createdBy;

    @ManyToOne
    @JoinColumn(name = "last_updated_by")
    private User lastUpdatedBy;

    @ManyToOne(optional = false)
    @NotNull
    private WorkingDay dayReached;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(
        value = { "llinsFamilyReportHistories", "user", "createdBy", "lastUpdatedBy", "dayReached", "targetDetails", "executingTeam" },
        allowSetters = true
    )
    private LlinsFamilyReport llinsFamilyReport;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LlinsFamilyReportHistory id(Long id) {
        this.id = id;
        return this;
    }

    public String getUid() {
        return this.uid;
    }

    public LlinsFamilyReportHistory uid(String uid) {
        this.uid = uid;
        return this;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Instant getCreated() {
        return this.created;
    }

    public LlinsFamilyReportHistory created(Instant created) {
        this.created = created;
        return this;
    }

    public void setCreated(Instant created) {
        this.created = created;
    }

    public Instant getLastUpdated() {
        return this.lastUpdated;
    }

    public LlinsFamilyReportHistory lastUpdated(Instant lastUpdated) {
        this.lastUpdated = lastUpdated;
        return this;
    }

    public void setLastUpdated(Instant lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public Integer getDocumentNo() {
        return this.documentNo;
    }

    public LlinsFamilyReportHistory documentNo(Integer documentNo) {
        this.documentNo = documentNo;
        return this;
    }

    public void setDocumentNo(Integer documentNo) {
        this.documentNo = documentNo;
    }

    public Integer getMaleIndividuals() {
        return this.maleIndividuals;
    }

    public LlinsFamilyReportHistory maleIndividuals(Integer maleIndividuals) {
        this.maleIndividuals = maleIndividuals;
        return this;
    }

    public void setMaleIndividuals(Integer maleIndividuals) {
        this.maleIndividuals = maleIndividuals;
    }

    public Integer getFemaleIndividuals() {
        return this.femaleIndividuals;
    }

    public LlinsFamilyReportHistory femaleIndividuals(Integer femaleIndividuals) {
        this.femaleIndividuals = femaleIndividuals;
        return this;
    }

    public void setFemaleIndividuals(Integer femaleIndividuals) {
        this.femaleIndividuals = femaleIndividuals;
    }

    public Integer getLessThan5Males() {
        return this.lessThan5Males;
    }

    public LlinsFamilyReportHistory lessThan5Males(Integer lessThan5Males) {
        this.lessThan5Males = lessThan5Males;
        return this;
    }

    public void setLessThan5Males(Integer lessThan5Males) {
        this.lessThan5Males = lessThan5Males;
    }

    public Integer getLessThan5Females() {
        return this.lessThan5Females;
    }

    public LlinsFamilyReportHistory lessThan5Females(Integer lessThan5Females) {
        this.lessThan5Females = lessThan5Females;
        return this;
    }

    public void setLessThan5Females(Integer lessThan5Females) {
        this.lessThan5Females = lessThan5Females;
    }

    public Integer getPregnantWomen() {
        return this.pregnantWomen;
    }

    public LlinsFamilyReportHistory pregnantWomen(Integer pregnantWomen) {
        this.pregnantWomen = pregnantWomen;
        return this;
    }

    public void setPregnantWomen(Integer pregnantWomen) {
        this.pregnantWomen = pregnantWomen;
    }

    public Integer getQuantityReceived() {
        return this.quantityReceived;
    }

    public LlinsFamilyReportHistory quantityReceived(Integer quantityReceived) {
        this.quantityReceived = quantityReceived;
        return this;
    }

    public void setQuantityReceived(Integer quantityReceived) {
        this.quantityReceived = quantityReceived;
    }

    public FamilyType getFamilyType() {
        return this.familyType;
    }

    public LlinsFamilyReportHistory familyType(FamilyType familyType) {
        this.familyType = familyType;
        return this;
    }

    public void setFamilyType(FamilyType familyType) {
        this.familyType = familyType;
    }

    //    @Override
    public User getCreatedBy() {
        return createdBy;
    }

    //    @Override
    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    //    @Override
    @Deprecated
    public User getUser() {
        return createdBy;
    }

    //    @Override
    @Deprecated
    public void setUser(User user) {
        setCreatedBy(createdBy == null ? user : createdBy);
    }

    @Deprecated
    public LlinsFamilyReportHistory user(User user) {
        this.setUser(user);
        return this;
    }

    public User getLastUpdatedBy() {
        return this.lastUpdatedBy;
    }

    public LlinsFamilyReportHistory lastUpdatedBy(User user) {
        this.setLastUpdatedBy(user);
        return this;
    }

    public void setLastUpdatedBy(User user) {
        this.lastUpdatedBy = user;
    }

    public WorkingDay getDayReached() {
        return this.dayReached;
    }

    public LlinsFamilyReportHistory dayReached(WorkingDay workingDay) {
        this.setDayReached(workingDay);
        return this;
    }

    public void setDayReached(WorkingDay workingDay) {
        this.dayReached = workingDay;
    }

    public LlinsFamilyReport getLlinsFamilyReport() {
        return this.llinsFamilyReport;
    }

    public LlinsFamilyReportHistory llinsFamilyReport(LlinsFamilyReport lLINSFamilyReport) {
        this.setLlinsFamilyReport(lLINSFamilyReport);
        return this;
    }

    public void setLlinsFamilyReport(LlinsFamilyReport lLINSFamilyReport) {
        this.llinsFamilyReport = lLINSFamilyReport;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LlinsFamilyReportHistory)) {
            return false;
        }
        return id != null && id.equals(((LlinsFamilyReportHistory) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LlinsFamilyReportHistory{" +
            "id=" + getId() +
            ", uid='" + getUid() + "'" +
            ", created='" + getCreated() + "'" +
            ", lastUpdated='" + getLastUpdated() + "'" +
            ", documentNo=" + getDocumentNo() +
            ", maleIndividuals=" + getMaleIndividuals() +
            ", femaleIndividuals=" + getFemaleIndividuals() +
            ", lessThan5Males=" + getLessThan5Males() +
            ", lessThan5Females=" + getLessThan5Females() +
            ", pregnantWomen=" + getPregnantWomen() +
            ", quantityReceived=" + getQuantityReceived() +
            ", familyType='" + getFamilyType() + "'" +
            "}";
    }
}
