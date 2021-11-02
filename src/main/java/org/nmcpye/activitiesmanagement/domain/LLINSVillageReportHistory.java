package org.nmcpye.activitiesmanagement.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A LLINSVillageReportHistory.
 */
@Entity
@Table(name = "llins_village_report_history")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class LLINSVillageReportHistory implements Serializable {

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
        value = { "llinsVillageReportHistories", "user", "lastUpdatedBy", "dayReached", "targetDetails", "executingTeam" },
        allowSetters = true
    )
    private LLINSVillageReport llinsVillageReport;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LLINSVillageReportHistory id(Long id) {
        this.id = id;
        return this;
    }

    public String getUid() {
        return this.uid;
    }

    public LLINSVillageReportHistory uid(String uid) {
        this.uid = uid;
        return this;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Instant getCreated() {
        return this.created;
    }

    public LLINSVillageReportHistory created(Instant created) {
        this.created = created;
        return this;
    }

    public void setCreated(Instant created) {
        this.created = created;
    }

    public Instant getLastUpdated() {
        return this.lastUpdated;
    }

    public LLINSVillageReportHistory lastUpdated(Instant lastUpdated) {
        this.lastUpdated = lastUpdated;
        return this;
    }

    public void setLastUpdated(Instant lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public Integer getHouses() {
        return this.houses;
    }

    public LLINSVillageReportHistory houses(Integer houses) {
        this.houses = houses;
        return this;
    }

    public void setHouses(Integer houses) {
        this.houses = houses;
    }

    public Integer getResidentHousehold() {
        return this.residentHousehold;
    }

    public LLINSVillageReportHistory residentHousehold(Integer residentHousehold) {
        this.residentHousehold = residentHousehold;
        return this;
    }

    public void setResidentHousehold(Integer residentHousehold) {
        this.residentHousehold = residentHousehold;
    }

    public Integer getIdpsHousehold() {
        return this.idpsHousehold;
    }

    public LLINSVillageReportHistory idpsHousehold(Integer idpsHousehold) {
        this.idpsHousehold = idpsHousehold;
        return this;
    }

    public void setIdpsHousehold(Integer idpsHousehold) {
        this.idpsHousehold = idpsHousehold;
    }

    public Integer getMaleIndividuals() {
        return this.maleIndividuals;
    }

    public LLINSVillageReportHistory maleIndividuals(Integer maleIndividuals) {
        this.maleIndividuals = maleIndividuals;
        return this;
    }

    public void setMaleIndividuals(Integer maleIndividuals) {
        this.maleIndividuals = maleIndividuals;
    }

    public Integer getFemaleIndividuals() {
        return this.femaleIndividuals;
    }

    public LLINSVillageReportHistory femaleIndividuals(Integer femaleIndividuals) {
        this.femaleIndividuals = femaleIndividuals;
        return this;
    }

    public void setFemaleIndividuals(Integer femaleIndividuals) {
        this.femaleIndividuals = femaleIndividuals;
    }

    public Integer getLessThan5Males() {
        return this.lessThan5Males;
    }

    public LLINSVillageReportHistory lessThan5Males(Integer lessThan5Males) {
        this.lessThan5Males = lessThan5Males;
        return this;
    }

    public void setLessThan5Males(Integer lessThan5Males) {
        this.lessThan5Males = lessThan5Males;
    }

    public Integer getLessThan5Females() {
        return this.lessThan5Females;
    }

    public LLINSVillageReportHistory lessThan5Females(Integer lessThan5Females) {
        this.lessThan5Females = lessThan5Females;
        return this;
    }

    public void setLessThan5Females(Integer lessThan5Females) {
        this.lessThan5Females = lessThan5Females;
    }

    public Integer getPregnantWomen() {
        return this.pregnantWomen;
    }

    public LLINSVillageReportHistory pregnantWomen(Integer pregnantWomen) {
        this.pregnantWomen = pregnantWomen;
        return this;
    }

    public void setPregnantWomen(Integer pregnantWomen) {
        this.pregnantWomen = pregnantWomen;
    }

    public Integer getQuantityReceived() {
        return this.quantityReceived;
    }

    public LLINSVillageReportHistory quantityReceived(Integer quantityReceived) {
        this.quantityReceived = quantityReceived;
        return this;
    }

    public void setQuantityReceived(Integer quantityReceived) {
        this.quantityReceived = quantityReceived;
    }

    public User getUser() {
        return this.user;
    }

    public LLINSVillageReportHistory user(User user) {
        this.setUser(user);
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getLastUpdatedBy() {
        return this.lastUpdatedBy;
    }

    public LLINSVillageReportHistory lastUpdatedBy(User user) {
        this.setLastUpdatedBy(user);
        return this;
    }

    public void setLastUpdatedBy(User user) {
        this.lastUpdatedBy = user;
    }

    public WorkingDay getDayReached() {
        return this.dayReached;
    }

    public LLINSVillageReportHistory dayReached(WorkingDay workingDay) {
        this.setDayReached(workingDay);
        return this;
    }

    public void setDayReached(WorkingDay workingDay) {
        this.dayReached = workingDay;
    }

    public LLINSVillageReport getLlinsVillageReport() {
        return this.llinsVillageReport;
    }

    public LLINSVillageReportHistory llinsVillageReport(LLINSVillageReport lLINSVillageReport) {
        this.setLlinsVillageReport(lLINSVillageReport);
        return this;
    }

    public void setLlinsVillageReport(LLINSVillageReport lLINSVillageReport) {
        this.llinsVillageReport = lLINSVillageReport;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LLINSVillageReportHistory)) {
            return false;
        }
        return id != null && id.equals(((LLINSVillageReportHistory) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LLINSVillageReportHistory{" +
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
            "}";
    }
}
