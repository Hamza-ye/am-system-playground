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
 * A LLINSFamilyReportHistory.
 */
@Entity
@Table(name = "llins_family_report_history")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class LLINSFamilyReportHistory implements Serializable {

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
    private User user;

    @ManyToOne
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
    private LLINSFamilyReport llinsFamilyReport;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LLINSFamilyReportHistory id(Long id) {
        this.id = id;
        return this;
    }

    public String getUid() {
        return this.uid;
    }

    public LLINSFamilyReportHistory uid(String uid) {
        this.uid = uid;
        return this;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Instant getCreated() {
        return this.created;
    }

    public LLINSFamilyReportHistory created(Instant created) {
        this.created = created;
        return this;
    }

    public void setCreated(Instant created) {
        this.created = created;
    }

    public Instant getLastUpdated() {
        return this.lastUpdated;
    }

    public LLINSFamilyReportHistory lastUpdated(Instant lastUpdated) {
        this.lastUpdated = lastUpdated;
        return this;
    }

    public void setLastUpdated(Instant lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public Integer getDocumentNo() {
        return this.documentNo;
    }

    public LLINSFamilyReportHistory documentNo(Integer documentNo) {
        this.documentNo = documentNo;
        return this;
    }

    public void setDocumentNo(Integer documentNo) {
        this.documentNo = documentNo;
    }

    public Integer getMaleIndividuals() {
        return this.maleIndividuals;
    }

    public LLINSFamilyReportHistory maleIndividuals(Integer maleIndividuals) {
        this.maleIndividuals = maleIndividuals;
        return this;
    }

    public void setMaleIndividuals(Integer maleIndividuals) {
        this.maleIndividuals = maleIndividuals;
    }

    public Integer getFemaleIndividuals() {
        return this.femaleIndividuals;
    }

    public LLINSFamilyReportHistory femaleIndividuals(Integer femaleIndividuals) {
        this.femaleIndividuals = femaleIndividuals;
        return this;
    }

    public void setFemaleIndividuals(Integer femaleIndividuals) {
        this.femaleIndividuals = femaleIndividuals;
    }

    public Integer getLessThan5Males() {
        return this.lessThan5Males;
    }

    public LLINSFamilyReportHistory lessThan5Males(Integer lessThan5Males) {
        this.lessThan5Males = lessThan5Males;
        return this;
    }

    public void setLessThan5Males(Integer lessThan5Males) {
        this.lessThan5Males = lessThan5Males;
    }

    public Integer getLessThan5Females() {
        return this.lessThan5Females;
    }

    public LLINSFamilyReportHistory lessThan5Females(Integer lessThan5Females) {
        this.lessThan5Females = lessThan5Females;
        return this;
    }

    public void setLessThan5Females(Integer lessThan5Females) {
        this.lessThan5Females = lessThan5Females;
    }

    public Integer getPregnantWomen() {
        return this.pregnantWomen;
    }

    public LLINSFamilyReportHistory pregnantWomen(Integer pregnantWomen) {
        this.pregnantWomen = pregnantWomen;
        return this;
    }

    public void setPregnantWomen(Integer pregnantWomen) {
        this.pregnantWomen = pregnantWomen;
    }

    public Integer getQuantityReceived() {
        return this.quantityReceived;
    }

    public LLINSFamilyReportHistory quantityReceived(Integer quantityReceived) {
        this.quantityReceived = quantityReceived;
        return this;
    }

    public void setQuantityReceived(Integer quantityReceived) {
        this.quantityReceived = quantityReceived;
    }

    public FamilyType getFamilyType() {
        return this.familyType;
    }

    public LLINSFamilyReportHistory familyType(FamilyType familyType) {
        this.familyType = familyType;
        return this;
    }

    public void setFamilyType(FamilyType familyType) {
        this.familyType = familyType;
    }

    public User getUser() {
        return this.user;
    }

    public LLINSFamilyReportHistory user(User user) {
        this.setUser(user);
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getLastUpdatedBy() {
        return this.lastUpdatedBy;
    }

    public LLINSFamilyReportHistory lastUpdatedBy(User user) {
        this.setLastUpdatedBy(user);
        return this;
    }

    public void setLastUpdatedBy(User user) {
        this.lastUpdatedBy = user;
    }

    public WorkingDay getDayReached() {
        return this.dayReached;
    }

    public LLINSFamilyReportHistory dayReached(WorkingDay workingDay) {
        this.setDayReached(workingDay);
        return this;
    }

    public void setDayReached(WorkingDay workingDay) {
        this.dayReached = workingDay;
    }

    public LLINSFamilyReport getLlinsFamilyReport() {
        return this.llinsFamilyReport;
    }

    public LLINSFamilyReportHistory llinsFamilyReport(LLINSFamilyReport lLINSFamilyReport) {
        this.setLlinsFamilyReport(lLINSFamilyReport);
        return this;
    }

    public void setLlinsFamilyReport(LLINSFamilyReport lLINSFamilyReport) {
        this.llinsFamilyReport = lLINSFamilyReport;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LLINSFamilyReportHistory)) {
            return false;
        }
        return id != null && id.equals(((LLINSFamilyReportHistory) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LLINSFamilyReportHistory{" +
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
