package org.nmcpye.activitiesmanagement.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.nmcpye.activitiesmanagement.domain.chv.Chv;
import org.nmcpye.activitiesmanagement.domain.period.Period;

/**
 * A ChvMalariaReportVersion1.
 */
@Entity
@Table(name = "chv_malaria_report_version_1")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ChvMalariaReportVersion1 implements Serializable {

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

    @Column(name = "tested")
    private Integer tested;

    @Column(name = "positive")
    private Integer positive;

    @Column(name = "drugs_given")
    private Integer drugsGiven;

    @Column(name = "supps_given")
    private Integer suppsGiven;

    @Column(name = "rdt_balance")
    private Integer rdtBalance;

    @Column(name = "rdt_received")
    private Integer rdtReceived;

    @Column(name = "rdt_used")
    private Integer rdtUsed;

    @Column(name = "rdt_damaged_lost")
    private Integer rdtDamagedLost;

    @Column(name = "drugs_balance")
    private Integer drugsBalance;

    @Column(name = "drugs_received")
    private Integer drugsReceived;

    @Column(name = "drugs_used")
    private Integer drugsUsed;

    @Column(name = "drugs_damaged_lost")
    private Integer drugsDamagedLost;

    @Column(name = "supps_balance")
    private Integer suppsBalance;

    @Column(name = "supps_received")
    private Integer suppsReceived;

    @Column(name = "supps_used")
    private Integer suppsUsed;

    @Column(name = "supps_damaged_lost")
    private Integer suppsDamagedLost;

    @Column(name = "comment")
    private String comment;

    @ManyToOne
    @JoinColumn(name = "created_by")
    private User createdBy;

    @ManyToOne
    @JoinColumn(name = "last_updated_by")
    private User lastUpdatedBy;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(
        value = {
            "person", "coveredSubVillages", "district", "homeSubvillage", "managedByHf", "user", "createdBy", "lastUpdatedBy", "supervisionTeams",
        },
        allowSetters = true
    )
    private Chv chv;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "periodType" }, allowSetters = true)
    private Period period;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ChvMalariaReportVersion1 id(Long id) {
        this.id = id;
        return this;
    }

    public String getUid() {
        return this.uid;
    }

    public ChvMalariaReportVersion1 uid(String uid) {
        this.uid = uid;
        return this;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Instant getCreated() {
        return this.created;
    }

    public ChvMalariaReportVersion1 created(Instant created) {
        this.created = created;
        return this;
    }

    public void setCreated(Instant created) {
        this.created = created;
    }

    public Instant getLastUpdated() {
        return this.lastUpdated;
    }

    public ChvMalariaReportVersion1 lastUpdated(Instant lastUpdated) {
        this.lastUpdated = lastUpdated;
        return this;
    }

    public void setLastUpdated(Instant lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public Integer getTested() {
        return this.tested;
    }

    public ChvMalariaReportVersion1 tested(Integer tested) {
        this.tested = tested;
        return this;
    }

    public void setTested(Integer tested) {
        this.tested = tested;
    }

    public Integer getPositive() {
        return this.positive;
    }

    public ChvMalariaReportVersion1 positive(Integer positive) {
        this.positive = positive;
        return this;
    }

    public void setPositive(Integer positive) {
        this.positive = positive;
    }

    public Integer getDrugsGiven() {
        return this.drugsGiven;
    }

    public ChvMalariaReportVersion1 drugsGiven(Integer drugsGiven) {
        this.drugsGiven = drugsGiven;
        return this;
    }

    public void setDrugsGiven(Integer drugsGiven) {
        this.drugsGiven = drugsGiven;
    }

    public Integer getSuppsGiven() {
        return this.suppsGiven;
    }

    public ChvMalariaReportVersion1 suppsGiven(Integer suppsGiven) {
        this.suppsGiven = suppsGiven;
        return this;
    }

    public void setSuppsGiven(Integer suppsGiven) {
        this.suppsGiven = suppsGiven;
    }

    public Integer getRdtBalance() {
        return this.rdtBalance;
    }

    public ChvMalariaReportVersion1 rdtBalance(Integer rdtBalance) {
        this.rdtBalance = rdtBalance;
        return this;
    }

    public void setRdtBalance(Integer rdtBalance) {
        this.rdtBalance = rdtBalance;
    }

    public Integer getRdtReceived() {
        return this.rdtReceived;
    }

    public ChvMalariaReportVersion1 rdtReceived(Integer rdtReceived) {
        this.rdtReceived = rdtReceived;
        return this;
    }

    public void setRdtReceived(Integer rdtReceived) {
        this.rdtReceived = rdtReceived;
    }

    public Integer getRdtUsed() {
        return this.rdtUsed;
    }

    public ChvMalariaReportVersion1 rdtUsed(Integer rdtUsed) {
        this.rdtUsed = rdtUsed;
        return this;
    }

    public void setRdtUsed(Integer rdtUsed) {
        this.rdtUsed = rdtUsed;
    }

    public Integer getRdtDamagedLost() {
        return this.rdtDamagedLost;
    }

    public ChvMalariaReportVersion1 rdtDamagedLost(Integer rdtDamagedLost) {
        this.rdtDamagedLost = rdtDamagedLost;
        return this;
    }

    public void setRdtDamagedLost(Integer rdtDamagedLost) {
        this.rdtDamagedLost = rdtDamagedLost;
    }

    public Integer getDrugsBalance() {
        return this.drugsBalance;
    }

    public ChvMalariaReportVersion1 drugsBalance(Integer drugsBalance) {
        this.drugsBalance = drugsBalance;
        return this;
    }

    public void setDrugsBalance(Integer drugsBalance) {
        this.drugsBalance = drugsBalance;
    }

    public Integer getDrugsReceived() {
        return this.drugsReceived;
    }

    public ChvMalariaReportVersion1 drugsReceived(Integer drugsReceived) {
        this.drugsReceived = drugsReceived;
        return this;
    }

    public void setDrugsReceived(Integer drugsReceived) {
        this.drugsReceived = drugsReceived;
    }

    public Integer getDrugsUsed() {
        return this.drugsUsed;
    }

    public ChvMalariaReportVersion1 drugsUsed(Integer drugsUsed) {
        this.drugsUsed = drugsUsed;
        return this;
    }

    public void setDrugsUsed(Integer drugsUsed) {
        this.drugsUsed = drugsUsed;
    }

    public Integer getDrugsDamagedLost() {
        return this.drugsDamagedLost;
    }

    public ChvMalariaReportVersion1 drugsDamagedLost(Integer drugsDamagedLost) {
        this.drugsDamagedLost = drugsDamagedLost;
        return this;
    }

    public void setDrugsDamagedLost(Integer drugsDamagedLost) {
        this.drugsDamagedLost = drugsDamagedLost;
    }

    public Integer getSuppsBalance() {
        return this.suppsBalance;
    }

    public ChvMalariaReportVersion1 suppsBalance(Integer suppsBalance) {
        this.suppsBalance = suppsBalance;
        return this;
    }

    public void setSuppsBalance(Integer suppsBalance) {
        this.suppsBalance = suppsBalance;
    }

    public Integer getSuppsReceived() {
        return this.suppsReceived;
    }

    public ChvMalariaReportVersion1 suppsReceived(Integer suppsReceived) {
        this.suppsReceived = suppsReceived;
        return this;
    }

    public void setSuppsReceived(Integer suppsReceived) {
        this.suppsReceived = suppsReceived;
    }

    public Integer getSuppsUsed() {
        return this.suppsUsed;
    }

    public ChvMalariaReportVersion1 suppsUsed(Integer suppsUsed) {
        this.suppsUsed = suppsUsed;
        return this;
    }

    public void setSuppsUsed(Integer suppsUsed) {
        this.suppsUsed = suppsUsed;
    }

    public Integer getSuppsDamagedLost() {
        return this.suppsDamagedLost;
    }

    public ChvMalariaReportVersion1 suppsDamagedLost(Integer suppsDamagedLost) {
        this.suppsDamagedLost = suppsDamagedLost;
        return this;
    }

    public void setSuppsDamagedLost(Integer suppsDamagedLost) {
        this.suppsDamagedLost = suppsDamagedLost;
    }

    public String getComment() {
        return this.comment;
    }

    public ChvMalariaReportVersion1 comment(String comment) {
        this.comment = comment;
        return this;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Deprecated
    public ChvMalariaReportVersion1 user(User user) {
        this.setUser(user);
        return this;
    }

    public User getLastUpdatedBy() {
        return this.lastUpdatedBy;
    }

    public ChvMalariaReportVersion1 lastUpdatedBy(User user) {
        this.setLastUpdatedBy(user);
        return this;
    }

    public void setLastUpdatedBy(User user) {
        this.lastUpdatedBy = user;
    }

    public Chv getChv() {
        return this.chv;
    }

    public ChvMalariaReportVersion1 chv(Chv cHV) {
        this.setChv(cHV);
        return this;
    }

    public void setChv(Chv cHV) {
        this.chv = cHV;
    }

    public Period getPeriod() {
        return this.period;
    }

    public ChvMalariaReportVersion1 period(Period period) {
        this.setPeriod(period);
        return this;
    }

    public void setPeriod(Period period) {
        this.period = period;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ChvMalariaReportVersion1)) {
            return false;
        }
        return id != null && id.equals(((ChvMalariaReportVersion1) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ChvMalariaReportVersion1{" +
            "id=" + getId() +
            ", uid='" + getUid() + "'" +
            ", created='" + getCreated() + "'" +
            ", lastUpdated='" + getLastUpdated() + "'" +
            ", tested=" + getTested() +
            ", positive=" + getPositive() +
            ", drugsGiven=" + getDrugsGiven() +
            ", suppsGiven=" + getSuppsGiven() +
            ", rdtBalance=" + getRdtBalance() +
            ", rdtReceived=" + getRdtReceived() +
            ", rdtUsed=" + getRdtUsed() +
            ", rdtDamagedLost=" + getRdtDamagedLost() +
            ", drugsBalance=" + getDrugsBalance() +
            ", drugsReceived=" + getDrugsReceived() +
            ", drugsUsed=" + getDrugsUsed() +
            ", drugsDamagedLost=" + getDrugsDamagedLost() +
            ", suppsBalance=" + getSuppsBalance() +
            ", suppsReceived=" + getSuppsReceived() +
            ", suppsUsed=" + getSuppsUsed() +
            ", suppsDamagedLost=" + getSuppsDamagedLost() +
            ", comment='" + getComment() + "'" +
            "}";
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
}
