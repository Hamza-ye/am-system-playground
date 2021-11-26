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
 * A LlinsFamilyReport.
 */
@Entity
@Table(name = "llins_family_report")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class LlinsFamilyReport implements Serializable {

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
    @JsonIgnoreProperties(value = { "user", "createdBy", "lastUpdatedBy", "dayReached", "llinsFamilyReport" }, allowSetters = true)
    private Set<LlinsFamilyReportHistory> llinsFamilyReportHistories = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "created_by")
    private User createdBy;

    @ManyToOne
    @JoinColumn(name = "last_updated_by")
    private User lastUpdatedBy;

    @ManyToOne(optional = false)
    @NotNull
    private WorkingDay dayReached;

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "llinsFamilyReports", "user", "createdBy", "lastUpdatedBy", "dayPlanned", "family", "teamAssigned" },
        allowSetters = true
    )
    private LlinsFamilyTarget targetDetails;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(
        value = {
            "whMovements",
            "llinsVillageTargets",
            "llinsVillageReports",
            "llinsFamilyTargets",
            "llinsFamilyReports",
            "createdBy",
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

    public LlinsFamilyReport id(Long id) {
        this.id = id;
        return this;
    }

    public String getUid() {
        return this.uid;
    }

    public LlinsFamilyReport uid(String uid) {
        this.uid = uid;
        return this;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Instant getCreated() {
        return this.created;
    }

    public LlinsFamilyReport created(Instant created) {
        this.created = created;
        return this;
    }

    public void setCreated(Instant created) {
        this.created = created;
    }

    public Instant getLastUpdated() {
        return this.lastUpdated;
    }

    public LlinsFamilyReport lastUpdated(Instant lastUpdated) {
        this.lastUpdated = lastUpdated;
        return this;
    }

    public void setLastUpdated(Instant lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public Integer getCheckNo() {
        return this.checkNo;
    }

    public LlinsFamilyReport checkNo(Integer checkNo) {
        this.checkNo = checkNo;
        return this;
    }

    public void setCheckNo(Integer checkNo) {
        this.checkNo = checkNo;
    }

    public Integer getMaleIndividuals() {
        return this.maleIndividuals;
    }

    public LlinsFamilyReport maleIndividuals(Integer maleIndividuals) {
        this.maleIndividuals = maleIndividuals;
        return this;
    }

    public void setMaleIndividuals(Integer maleIndividuals) {
        this.maleIndividuals = maleIndividuals;
    }

    public Integer getFemaleIndividuals() {
        return this.femaleIndividuals;
    }

    public LlinsFamilyReport femaleIndividuals(Integer femaleIndividuals) {
        this.femaleIndividuals = femaleIndividuals;
        return this;
    }

    public void setFemaleIndividuals(Integer femaleIndividuals) {
        this.femaleIndividuals = femaleIndividuals;
    }

    public Integer getLessThan5Males() {
        return this.lessThan5Males;
    }

    public LlinsFamilyReport lessThan5Males(Integer lessThan5Males) {
        this.lessThan5Males = lessThan5Males;
        return this;
    }

    public void setLessThan5Males(Integer lessThan5Males) {
        this.lessThan5Males = lessThan5Males;
    }

    public Integer getLessThan5Females() {
        return this.lessThan5Females;
    }

    public LlinsFamilyReport lessThan5Females(Integer lessThan5Females) {
        this.lessThan5Females = lessThan5Females;
        return this;
    }

    public void setLessThan5Females(Integer lessThan5Females) {
        this.lessThan5Females = lessThan5Females;
    }

    public Integer getPregnantWomen() {
        return this.pregnantWomen;
    }

    public LlinsFamilyReport pregnantWomen(Integer pregnantWomen) {
        this.pregnantWomen = pregnantWomen;
        return this;
    }

    public void setPregnantWomen(Integer pregnantWomen) {
        this.pregnantWomen = pregnantWomen;
    }

    public Integer getQuantityReceived() {
        return this.quantityReceived;
    }

    public LlinsFamilyReport quantityReceived(Integer quantityReceived) {
        this.quantityReceived = quantityReceived;
        return this;
    }

    public void setQuantityReceived(Integer quantityReceived) {
        this.quantityReceived = quantityReceived;
    }

    public FamilyType getFamilyType() {
        return this.familyType;
    }

    public LlinsFamilyReport familyType(FamilyType familyType) {
        this.familyType = familyType;
        return this;
    }

    public void setFamilyType(FamilyType familyType) {
        this.familyType = familyType;
    }

    public String getComment() {
        return this.comment;
    }

    public LlinsFamilyReport comment(String comment) {
        this.comment = comment;
        return this;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Set<LlinsFamilyReportHistory> getLlinsFamilyReportHistories() {
        return this.llinsFamilyReportHistories;
    }

    public LlinsFamilyReport llinsFamilyReportHistories(Set<LlinsFamilyReportHistory> lLINSFamilyReportHistories) {
        this.setLlinsFamilyReportHistories(lLINSFamilyReportHistories);
        return this;
    }

    public LlinsFamilyReport addLlinsFamilyReportHistory(LlinsFamilyReportHistory lLINSFamilyReportHistory) {
        this.llinsFamilyReportHistories.add(lLINSFamilyReportHistory);
        lLINSFamilyReportHistory.setLlinsFamilyReport(this);
        return this;
    }

    public LlinsFamilyReport removeLlinsFamilyReportHistory(LlinsFamilyReportHistory lLINSFamilyReportHistory) {
        this.llinsFamilyReportHistories.remove(lLINSFamilyReportHistory);
        lLINSFamilyReportHistory.setLlinsFamilyReport(null);
        return this;
    }

    public void setLlinsFamilyReportHistories(Set<LlinsFamilyReportHistory> lLINSFamilyReportHistories) {
        if (this.llinsFamilyReportHistories != null) {
            this.llinsFamilyReportHistories.forEach(i -> i.setLlinsFamilyReport(null));
        }
        if (lLINSFamilyReportHistories != null) {
            lLINSFamilyReportHistories.forEach(i -> i.setLlinsFamilyReport(this));
        }
        this.llinsFamilyReportHistories = lLINSFamilyReportHistories;
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
    public LlinsFamilyReport user(User user) {
        this.setUser(user);
        return this;
    }

    public User getLastUpdatedBy() {
        return this.lastUpdatedBy;
    }

    public LlinsFamilyReport lastUpdatedBy(User user) {
        this.setLastUpdatedBy(user);
        return this;
    }

    public void setLastUpdatedBy(User user) {
        this.lastUpdatedBy = user;
    }

    public WorkingDay getDayReached() {
        return this.dayReached;
    }

    public LlinsFamilyReport dayReached(WorkingDay workingDay) {
        this.setDayReached(workingDay);
        return this;
    }

    public void setDayReached(WorkingDay workingDay) {
        this.dayReached = workingDay;
    }

    public LlinsFamilyTarget getTargetDetails() {
        return this.targetDetails;
    }

    public LlinsFamilyReport targetDetails(LlinsFamilyTarget lLINSFamilyTarget) {
        this.setTargetDetails(lLINSFamilyTarget);
        return this;
    }

    public void setTargetDetails(LlinsFamilyTarget lLINSFamilyTarget) {
        this.targetDetails = lLINSFamilyTarget;
    }

    public Team getExecutingTeam() {
        return this.executingTeam;
    }

    public LlinsFamilyReport executingTeam(Team team) {
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
        if (!(o instanceof LlinsFamilyReport)) {
            return false;
        }
        return id != null && id.equals(((LlinsFamilyReport) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LlinsFamilyReport{" +
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
