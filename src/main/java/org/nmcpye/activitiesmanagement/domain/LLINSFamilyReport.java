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
import org.nmcpye.activitiesmanagement.domain.enumeration.FamilyType;

/**
 * A LLINSFamilyReport.
 */
@Entity
@Table(name = "llins_family_report")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class LLINSFamilyReport implements Serializable {

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

    @Column(name = "check_no")
    private Integer checkNo;

    @NotNull
    @Min(value = 0)
    @Column(name = "male_individuals", nullable = false)
    private Integer maleIndividuals;

    @NotNull
    @Min(value = 0)
    @Column(name = "female_individuals", nullable = false)
    private Integer femaleIndividuals;

    @NotNull
    @Min(value = 0)
    @Column(name = "less_than_5_males", nullable = false)
    private Integer lessThan5Males;

    @NotNull
    @Min(value = 0)
    @Column(name = "less_than_5_females", nullable = false)
    private Integer lessThan5Females;

    @NotNull
    @Min(value = 0)
    @Column(name = "pregnant_women", nullable = false)
    private Integer pregnantWomen;

    @NotNull
    @Min(value = 0)
    @Column(name = "quantity_received", nullable = false)
    private Integer quantityReceived;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "family_type", nullable = false)
    private FamilyType familyType;

    @Column(name = "comment")
    private String comment;

    @OneToMany(mappedBy = "llinsFamilyReport")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "user", "lastUpdatedBy", "dayReached", "llinsFamilyReport" }, allowSetters = true)
    private Set<LLINSFamilyReportHistory> llinsFamilyReportHistories = new HashSet<>();

    @ManyToOne
    private User user;

    @ManyToOne
    private User lastUpdatedBy;

    @ManyToOne(optional = false)
    @NotNull
    private WorkingDay dayReached;

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "llinsFamilyReports", "user", "lastUpdatedBy", "dayPlanned", "family", "teamAssigned" },
        allowSetters = true
    )
    private LLINSFamilyTarget targetDetails;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(
        value = {
            "whMovements",
            "llinsVillageTargets",
            "llinsVillageReports",
            "llinsFamilyTargets",
            "llinsFamilyReports",
            "user",
            "lastUpdatedBy",
            "person",
            "assignedToWarehouses",
        },
        allowSetters = true
    )
    private Team executingTeam;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LLINSFamilyReport id(Long id) {
        this.id = id;
        return this;
    }

    public String getUid() {
        return this.uid;
    }

    public LLINSFamilyReport uid(String uid) {
        this.uid = uid;
        return this;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Instant getCreated() {
        return this.created;
    }

    public LLINSFamilyReport created(Instant created) {
        this.created = created;
        return this;
    }

    public void setCreated(Instant created) {
        this.created = created;
    }

    public Instant getLastUpdated() {
        return this.lastUpdated;
    }

    public LLINSFamilyReport lastUpdated(Instant lastUpdated) {
        this.lastUpdated = lastUpdated;
        return this;
    }

    public void setLastUpdated(Instant lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public Integer getCheckNo() {
        return this.checkNo;
    }

    public LLINSFamilyReport checkNo(Integer checkNo) {
        this.checkNo = checkNo;
        return this;
    }

    public void setCheckNo(Integer checkNo) {
        this.checkNo = checkNo;
    }

    public Integer getMaleIndividuals() {
        return this.maleIndividuals;
    }

    public LLINSFamilyReport maleIndividuals(Integer maleIndividuals) {
        this.maleIndividuals = maleIndividuals;
        return this;
    }

    public void setMaleIndividuals(Integer maleIndividuals) {
        this.maleIndividuals = maleIndividuals;
    }

    public Integer getFemaleIndividuals() {
        return this.femaleIndividuals;
    }

    public LLINSFamilyReport femaleIndividuals(Integer femaleIndividuals) {
        this.femaleIndividuals = femaleIndividuals;
        return this;
    }

    public void setFemaleIndividuals(Integer femaleIndividuals) {
        this.femaleIndividuals = femaleIndividuals;
    }

    public Integer getLessThan5Males() {
        return this.lessThan5Males;
    }

    public LLINSFamilyReport lessThan5Males(Integer lessThan5Males) {
        this.lessThan5Males = lessThan5Males;
        return this;
    }

    public void setLessThan5Males(Integer lessThan5Males) {
        this.lessThan5Males = lessThan5Males;
    }

    public Integer getLessThan5Females() {
        return this.lessThan5Females;
    }

    public LLINSFamilyReport lessThan5Females(Integer lessThan5Females) {
        this.lessThan5Females = lessThan5Females;
        return this;
    }

    public void setLessThan5Females(Integer lessThan5Females) {
        this.lessThan5Females = lessThan5Females;
    }

    public Integer getPregnantWomen() {
        return this.pregnantWomen;
    }

    public LLINSFamilyReport pregnantWomen(Integer pregnantWomen) {
        this.pregnantWomen = pregnantWomen;
        return this;
    }

    public void setPregnantWomen(Integer pregnantWomen) {
        this.pregnantWomen = pregnantWomen;
    }

    public Integer getQuantityReceived() {
        return this.quantityReceived;
    }

    public LLINSFamilyReport quantityReceived(Integer quantityReceived) {
        this.quantityReceived = quantityReceived;
        return this;
    }

    public void setQuantityReceived(Integer quantityReceived) {
        this.quantityReceived = quantityReceived;
    }

    public FamilyType getFamilyType() {
        return this.familyType;
    }

    public LLINSFamilyReport familyType(FamilyType familyType) {
        this.familyType = familyType;
        return this;
    }

    public void setFamilyType(FamilyType familyType) {
        this.familyType = familyType;
    }

    public String getComment() {
        return this.comment;
    }

    public LLINSFamilyReport comment(String comment) {
        this.comment = comment;
        return this;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Set<LLINSFamilyReportHistory> getLlinsFamilyReportHistories() {
        return this.llinsFamilyReportHistories;
    }

    public LLINSFamilyReport llinsFamilyReportHistories(Set<LLINSFamilyReportHistory> lLINSFamilyReportHistories) {
        this.setLlinsFamilyReportHistories(lLINSFamilyReportHistories);
        return this;
    }

    public LLINSFamilyReport addLlinsFamilyReportHistory(LLINSFamilyReportHistory lLINSFamilyReportHistory) {
        this.llinsFamilyReportHistories.add(lLINSFamilyReportHistory);
        lLINSFamilyReportHistory.setLlinsFamilyReport(this);
        return this;
    }

    public LLINSFamilyReport removeLlinsFamilyReportHistory(LLINSFamilyReportHistory lLINSFamilyReportHistory) {
        this.llinsFamilyReportHistories.remove(lLINSFamilyReportHistory);
        lLINSFamilyReportHistory.setLlinsFamilyReport(null);
        return this;
    }

    public void setLlinsFamilyReportHistories(Set<LLINSFamilyReportHistory> lLINSFamilyReportHistories) {
        if (this.llinsFamilyReportHistories != null) {
            this.llinsFamilyReportHistories.forEach(i -> i.setLlinsFamilyReport(null));
        }
        if (lLINSFamilyReportHistories != null) {
            lLINSFamilyReportHistories.forEach(i -> i.setLlinsFamilyReport(this));
        }
        this.llinsFamilyReportHistories = lLINSFamilyReportHistories;
    }

    public User getUser() {
        return this.user;
    }

    public LLINSFamilyReport user(User user) {
        this.setUser(user);
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getLastUpdatedBy() {
        return this.lastUpdatedBy;
    }

    public LLINSFamilyReport lastUpdatedBy(User user) {
        this.setLastUpdatedBy(user);
        return this;
    }

    public void setLastUpdatedBy(User user) {
        this.lastUpdatedBy = user;
    }

    public WorkingDay getDayReached() {
        return this.dayReached;
    }

    public LLINSFamilyReport dayReached(WorkingDay workingDay) {
        this.setDayReached(workingDay);
        return this;
    }

    public void setDayReached(WorkingDay workingDay) {
        this.dayReached = workingDay;
    }

    public LLINSFamilyTarget getTargetDetails() {
        return this.targetDetails;
    }

    public LLINSFamilyReport targetDetails(LLINSFamilyTarget lLINSFamilyTarget) {
        this.setTargetDetails(lLINSFamilyTarget);
        return this;
    }

    public void setTargetDetails(LLINSFamilyTarget lLINSFamilyTarget) {
        this.targetDetails = lLINSFamilyTarget;
    }

    public Team getExecutingTeam() {
        return this.executingTeam;
    }

    public LLINSFamilyReport executingTeam(Team team) {
        this.setExecutingTeam(team);
        return this;
    }

    public void setExecutingTeam(Team team) {
        this.executingTeam = team;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LLINSFamilyReport)) {
            return false;
        }
        return id != null && id.equals(((LLINSFamilyReport) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LLINSFamilyReport{" +
            "id=" + getId() +
            ", uid='" + getUid() + "'" +
            ", created='" + getCreated() + "'" +
            ", lastUpdated='" + getLastUpdated() + "'" +
            ", checkNo=" + getCheckNo() +
            ", maleIndividuals=" + getMaleIndividuals() +
            ", femaleIndividuals=" + getFemaleIndividuals() +
            ", lessThan5Males=" + getLessThan5Males() +
            ", lessThan5Females=" + getLessThan5Females() +
            ", pregnantWomen=" + getPregnantWomen() +
            ", quantityReceived=" + getQuantityReceived() +
            ", familyType='" + getFamilyType() + "'" +
            ", comment='" + getComment() + "'" +
            "}";
    }
}
