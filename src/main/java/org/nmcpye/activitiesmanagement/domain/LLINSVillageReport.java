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
 * A LLINSVillageReport.
 */
@Entity
@Table(name = "llins_village_report")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class LLINSVillageReport implements Serializable {

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

    @NotNull
    @Min(value = 0)
    @Column(name = "houses", nullable = false)
    private Integer houses;

    @NotNull
    @Min(value = 0)
    @Column(name = "resident_household", nullable = false)
    private Integer residentHousehold;

    @NotNull
    @Min(value = 0)
    @Column(name = "idps_household", nullable = false)
    private Integer idpsHousehold;

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

    @NotNull
    @Min(value = 0)
    @Column(name = "quantity_received", nullable = false)
    private Integer quantityReceived;

    @Column(name = "comment")
    private String comment;

    @OneToMany(mappedBy = "llinsVillageReport")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "user", "lastUpdatedBy", "dayReached", "llinsVillageReport" }, allowSetters = true)
    private Set<LLINSVillageReportHistory> llinsVillageReportHistories = new HashSet<>();

    @ManyToOne
    private User user;

    @ManyToOne
    private User lastUpdatedBy;

    @ManyToOne(optional = false)
    @NotNull
    private WorkingDay dayReached;

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "llinsVillageReports", "organisationUnit", "user", "lastUpdatedBy", "dayPlanned", "statusOfCoverage", "teamAssigned" },
        allowSetters = true
    )
    private LLINSVillageTarget targetDetails;

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

    public LLINSVillageReport id(Long id) {
        this.id = id;
        return this;
    }

    public String getUid() {
        return this.uid;
    }

    public LLINSVillageReport uid(String uid) {
        this.uid = uid;
        return this;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Instant getCreated() {
        return this.created;
    }

    public LLINSVillageReport created(Instant created) {
        this.created = created;
        return this;
    }

    public void setCreated(Instant created) {
        this.created = created;
    }

    public Instant getLastUpdated() {
        return this.lastUpdated;
    }

    public LLINSVillageReport lastUpdated(Instant lastUpdated) {
        this.lastUpdated = lastUpdated;
        return this;
    }

    public void setLastUpdated(Instant lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public Integer getHouses() {
        return this.houses;
    }

    public LLINSVillageReport houses(Integer houses) {
        this.houses = houses;
        return this;
    }

    public void setHouses(Integer houses) {
        this.houses = houses;
    }

    public Integer getResidentHousehold() {
        return this.residentHousehold;
    }

    public LLINSVillageReport residentHousehold(Integer residentHousehold) {
        this.residentHousehold = residentHousehold;
        return this;
    }

    public void setResidentHousehold(Integer residentHousehold) {
        this.residentHousehold = residentHousehold;
    }

    public Integer getIdpsHousehold() {
        return this.idpsHousehold;
    }

    public LLINSVillageReport idpsHousehold(Integer idpsHousehold) {
        this.idpsHousehold = idpsHousehold;
        return this;
    }

    public void setIdpsHousehold(Integer idpsHousehold) {
        this.idpsHousehold = idpsHousehold;
    }

    public Integer getMaleIndividuals() {
        return this.maleIndividuals;
    }

    public LLINSVillageReport maleIndividuals(Integer maleIndividuals) {
        this.maleIndividuals = maleIndividuals;
        return this;
    }

    public void setMaleIndividuals(Integer maleIndividuals) {
        this.maleIndividuals = maleIndividuals;
    }

    public Integer getFemaleIndividuals() {
        return this.femaleIndividuals;
    }

    public LLINSVillageReport femaleIndividuals(Integer femaleIndividuals) {
        this.femaleIndividuals = femaleIndividuals;
        return this;
    }

    public void setFemaleIndividuals(Integer femaleIndividuals) {
        this.femaleIndividuals = femaleIndividuals;
    }

    public Integer getLessThan5Males() {
        return this.lessThan5Males;
    }

    public LLINSVillageReport lessThan5Males(Integer lessThan5Males) {
        this.lessThan5Males = lessThan5Males;
        return this;
    }

    public void setLessThan5Males(Integer lessThan5Males) {
        this.lessThan5Males = lessThan5Males;
    }

    public Integer getLessThan5Females() {
        return this.lessThan5Females;
    }

    public LLINSVillageReport lessThan5Females(Integer lessThan5Females) {
        this.lessThan5Females = lessThan5Females;
        return this;
    }

    public void setLessThan5Females(Integer lessThan5Females) {
        this.lessThan5Females = lessThan5Females;
    }

    public Integer getPregnantWomen() {
        return this.pregnantWomen;
    }

    public LLINSVillageReport pregnantWomen(Integer pregnantWomen) {
        this.pregnantWomen = pregnantWomen;
        return this;
    }

    public void setPregnantWomen(Integer pregnantWomen) {
        this.pregnantWomen = pregnantWomen;
    }

    public Integer getQuantityReceived() {
        return this.quantityReceived;
    }

    public LLINSVillageReport quantityReceived(Integer quantityReceived) {
        this.quantityReceived = quantityReceived;
        return this;
    }

    public void setQuantityReceived(Integer quantityReceived) {
        this.quantityReceived = quantityReceived;
    }

    public String getComment() {
        return this.comment;
    }

    public LLINSVillageReport comment(String comment) {
        this.comment = comment;
        return this;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Set<LLINSVillageReportHistory> getLlinsVillageReportHistories() {
        return this.llinsVillageReportHistories;
    }

    public LLINSVillageReport llinsVillageReportHistories(Set<LLINSVillageReportHistory> lLINSVillageReportHistories) {
        this.setLlinsVillageReportHistories(lLINSVillageReportHistories);
        return this;
    }

    public LLINSVillageReport addLlinsVillageReportHistory(LLINSVillageReportHistory lLINSVillageReportHistory) {
        this.llinsVillageReportHistories.add(lLINSVillageReportHistory);
        lLINSVillageReportHistory.setLlinsVillageReport(this);
        return this;
    }

    public LLINSVillageReport removeLlinsVillageReportHistory(LLINSVillageReportHistory lLINSVillageReportHistory) {
        this.llinsVillageReportHistories.remove(lLINSVillageReportHistory);
        lLINSVillageReportHistory.setLlinsVillageReport(null);
        return this;
    }

    public void setLlinsVillageReportHistories(Set<LLINSVillageReportHistory> lLINSVillageReportHistories) {
        if (this.llinsVillageReportHistories != null) {
            this.llinsVillageReportHistories.forEach(i -> i.setLlinsVillageReport(null));
        }
        if (lLINSVillageReportHistories != null) {
            lLINSVillageReportHistories.forEach(i -> i.setLlinsVillageReport(this));
        }
        this.llinsVillageReportHistories = lLINSVillageReportHistories;
    }

    public User getUser() {
        return this.user;
    }

    public LLINSVillageReport user(User user) {
        this.setUser(user);
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getLastUpdatedBy() {
        return this.lastUpdatedBy;
    }

    public LLINSVillageReport lastUpdatedBy(User user) {
        this.setLastUpdatedBy(user);
        return this;
    }

    public void setLastUpdatedBy(User user) {
        this.lastUpdatedBy = user;
    }

    public WorkingDay getDayReached() {
        return this.dayReached;
    }

    public LLINSVillageReport dayReached(WorkingDay workingDay) {
        this.setDayReached(workingDay);
        return this;
    }

    public void setDayReached(WorkingDay workingDay) {
        this.dayReached = workingDay;
    }

    public LLINSVillageTarget getTargetDetails() {
        return this.targetDetails;
    }

    public LLINSVillageReport targetDetails(LLINSVillageTarget lLINSVillageTarget) {
        this.setTargetDetails(lLINSVillageTarget);
        return this;
    }

    public void setTargetDetails(LLINSVillageTarget lLINSVillageTarget) {
        this.targetDetails = lLINSVillageTarget;
    }

    public Team getExecutingTeam() {
        return this.executingTeam;
    }

    public LLINSVillageReport executingTeam(Team team) {
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
        if (!(o instanceof LLINSVillageReport)) {
            return false;
        }
        return id != null && id.equals(((LLINSVillageReport) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LLINSVillageReport{" +
            "id=" + getId() +
            ", uid='" + getUid() + "'" +
            ", created='" + getCreated() + "'" +
            ", lastUpdated='" + getLastUpdated() + "'" +
            ", houses=" + getHouses() +
            ", residentHousehold=" + getResidentHousehold() +
            ", idpsHousehold=" + getIdpsHousehold() +
            ", maleIndividuals=" + getMaleIndividuals() +
            ", femaleIndividuals=" + getFemaleIndividuals() +
            ", lessThan5Males=" + getLessThan5Males() +
            ", lessThan5Females=" + getLessThan5Females() +
            ", pregnantWomen=" + getPregnantWomen() +
            ", quantityReceived=" + getQuantityReceived() +
            ", comment='" + getComment() + "'" +
            "}";
    }
}
