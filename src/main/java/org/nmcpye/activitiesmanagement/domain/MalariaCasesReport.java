package org.nmcpye.activitiesmanagement.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.nmcpye.activitiesmanagement.domain.organisationunit.OrganisationUnit;
import org.nmcpye.activitiesmanagement.domain.period.Period;

/**
 * A MalariaCasesReport.
 */
@Entity
@Table(
    name = "malaria_cases_report",
    uniqueConstraints = {
        @UniqueConstraint(columnNames =
            {"data_set_id", "organisation_unit_id", "period_id", "report_class_id"})}
)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class MalariaCasesReport implements Serializable {

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

    @Column(name = "rdt_tested")
    private Integer rdtTested;

    @Column(name = "rdt_positive")
    private Integer rdtPositive;

    @Column(name = "rdt_pf")
    private Integer rdtPf;

    @Column(name = "rdt_pv")
    private Integer rdtPv;

    @Column(name = "rdt_pother")
    private Integer rdtPother;

    @Column(name = "micro_tested")
    private Integer microTested;

    @Column(name = "micro_positive")
    private Integer microPositive;

    @Column(name = "micro_pf")
    private Integer microPf;

    @Column(name = "micro_pv")
    private Integer microPv;

    @Column(name = "micro_mix")
    private Integer microMix;

    @Column(name = "micro_pother")
    private Integer microPother;

    @Column(name = "probable_cases")
    private Integer probableCases;

    @Column(name = "inpatient_cases")
    private Integer inpatientCases;

    @Column(name = "death_cases")
    private Integer deathCases;

    @Column(name = "treated")
    private Integer treated;

    @Column(name = "suspected_cases")
    private Integer suspectedCases;

    @Column(name = "total_frequents")
    private Integer totalFrequents;

    @Column(name = "comment")
    private String comment;

    @ManyToOne
    private User user;

    @ManyToOne
    private User lastUpdatedBy;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "user", "lastUpdatedBy" }, allowSetters = true)
    private CasesReportClass reportClass;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "periodType" }, allowSetters = true)
    private Period period;

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "malariaCasesReports", "dengueCasesReports", "periodType", "notificationRecipients", "user", "lastUpdatedBy", "sources" },
        allowSetters = true
    )
    private DataSet dataSet;

    @ManyToOne
    @JsonIgnoreProperties(
        value = {
            "malariaReports",
            "dengueReports",
            "parent",
            "hfHomeSubVillage",
            "coveredByHf",
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

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MalariaCasesReport id(Long id) {
        this.id = id;
        return this;
    }

    public String getUid() {
        return this.uid;
    }

    public MalariaCasesReport uid(String uid) {
        this.uid = uid;
        return this;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Instant getCreated() {
        return this.created;
    }

    public MalariaCasesReport created(Instant created) {
        this.created = created;
        return this;
    }

    public void setCreated(Instant created) {
        this.created = created;
    }

    public Instant getLastUpdated() {
        return this.lastUpdated;
    }

    public MalariaCasesReport lastUpdated(Instant lastUpdated) {
        this.lastUpdated = lastUpdated;
        return this;
    }

    public void setLastUpdated(Instant lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public Integer getRdtTested() {
        return this.rdtTested;
    }

    public MalariaCasesReport rdtTested(Integer rdtTested) {
        this.rdtTested = rdtTested;
        return this;
    }

    public void setRdtTested(Integer rdtTested) {
        this.rdtTested = rdtTested;
    }

    public Integer getRdtPositive() {
        return this.rdtPositive;
    }

    public MalariaCasesReport rdtPositive(Integer rdtPositive) {
        this.rdtPositive = rdtPositive;
        return this;
    }

    public void setRdtPositive(Integer rdtPositive) {
        this.rdtPositive = rdtPositive;
    }

    public Integer getRdtPf() {
        return this.rdtPf;
    }

    public MalariaCasesReport rdtPf(Integer rdtPf) {
        this.rdtPf = rdtPf;
        return this;
    }

    public void setRdtPf(Integer rdtPf) {
        this.rdtPf = rdtPf;
    }

    public Integer getRdtPv() {
        return this.rdtPv;
    }

    public MalariaCasesReport rdtPv(Integer rdtPv) {
        this.rdtPv = rdtPv;
        return this;
    }

    public void setRdtPv(Integer rdtPv) {
        this.rdtPv = rdtPv;
    }

    public Integer getRdtPother() {
        return this.rdtPother;
    }

    public MalariaCasesReport rdtPother(Integer rdtPother) {
        this.rdtPother = rdtPother;
        return this;
    }

    public void setRdtPother(Integer rdtPother) {
        this.rdtPother = rdtPother;
    }

    public Integer getMicroTested() {
        return this.microTested;
    }

    public MalariaCasesReport microTested(Integer microTested) {
        this.microTested = microTested;
        return this;
    }

    public void setMicroTested(Integer microTested) {
        this.microTested = microTested;
    }

    public Integer getMicroPositive() {
        return this.microPositive;
    }

    public MalariaCasesReport microPositive(Integer microPositive) {
        this.microPositive = microPositive;
        return this;
    }

    public void setMicroPositive(Integer microPositive) {
        this.microPositive = microPositive;
    }

    public Integer getMicroPf() {
        return this.microPf;
    }

    public MalariaCasesReport microPf(Integer microPf) {
        this.microPf = microPf;
        return this;
    }

    public void setMicroPf(Integer microPf) {
        this.microPf = microPf;
    }

    public Integer getMicroPv() {
        return this.microPv;
    }

    public MalariaCasesReport microPv(Integer microPv) {
        this.microPv = microPv;
        return this;
    }

    public void setMicroPv(Integer microPv) {
        this.microPv = microPv;
    }

    public Integer getMicroMix() {
        return this.microMix;
    }

    public MalariaCasesReport microMix(Integer microMix) {
        this.microMix = microMix;
        return this;
    }

    public void setMicroMix(Integer microMix) {
        this.microMix = microMix;
    }

    public Integer getMicroPother() {
        return this.microPother;
    }

    public MalariaCasesReport microPother(Integer microPother) {
        this.microPother = microPother;
        return this;
    }

    public void setMicroPother(Integer microPother) {
        this.microPother = microPother;
    }

    public Integer getProbableCases() {
        return this.probableCases;
    }

    public MalariaCasesReport probableCases(Integer probableCases) {
        this.probableCases = probableCases;
        return this;
    }

    public void setProbableCases(Integer probableCases) {
        this.probableCases = probableCases;
    }

    public Integer getInpatientCases() {
        return this.inpatientCases;
    }

    public MalariaCasesReport inpatientCases(Integer inpatientCases) {
        this.inpatientCases = inpatientCases;
        return this;
    }

    public void setInpatientCases(Integer inpatientCases) {
        this.inpatientCases = inpatientCases;
    }

    public Integer getDeathCases() {
        return this.deathCases;
    }

    public MalariaCasesReport deathCases(Integer deathCases) {
        this.deathCases = deathCases;
        return this;
    }

    public void setDeathCases(Integer deathCases) {
        this.deathCases = deathCases;
    }

    public Integer getTreated() {
        return this.treated;
    }

    public MalariaCasesReport treated(Integer treated) {
        this.treated = treated;
        return this;
    }

    public void setTreated(Integer treated) {
        this.treated = treated;
    }

    public Integer getSuspectedCases() {
        return this.suspectedCases;
    }

    public MalariaCasesReport suspectedCases(Integer suspectedCases) {
        this.suspectedCases = suspectedCases;
        return this;
    }

    public void setSuspectedCases(Integer suspectedCases) {
        this.suspectedCases = suspectedCases;
    }

    public Integer getTotalFrequents() {
        return this.totalFrequents;
    }

    public MalariaCasesReport totalFrequents(Integer totalFrequents) {
        this.totalFrequents = totalFrequents;
        return this;
    }

    public void setTotalFrequents(Integer totalFrequents) {
        this.totalFrequents = totalFrequents;
    }

    public String getComment() {
        return this.comment;
    }

    public MalariaCasesReport comment(String comment) {
        this.comment = comment;
        return this;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public User getUser() {
        return this.user;
    }

    public MalariaCasesReport user(User user) {
        this.setUser(user);
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getLastUpdatedBy() {
        return this.lastUpdatedBy;
    }

    public MalariaCasesReport lastUpdatedBy(User user) {
        this.setLastUpdatedBy(user);
        return this;
    }

    public void setLastUpdatedBy(User user) {
        this.lastUpdatedBy = user;
    }

    public CasesReportClass getReportClass() {
        return this.reportClass;
    }

    public MalariaCasesReport reportClass(CasesReportClass casesReportClass) {
        this.setReportClass(casesReportClass);
        return this;
    }

    public void setReportClass(CasesReportClass casesReportClass) {
        this.reportClass = casesReportClass;
    }

    public Period getPeriod() {
        return this.period;
    }

    public MalariaCasesReport period(Period period) {
        this.setPeriod(period);
        return this;
    }

    public void setPeriod(Period period) {
        this.period = period;
    }

    public DataSet getDataSet() {
        return this.dataSet;
    }

    public MalariaCasesReport dataSet(DataSet dataSet) {
        this.setDataSet(dataSet);
        return this;
    }

    public void setDataSet(DataSet dataSet) {
        this.dataSet = dataSet;
    }

    public OrganisationUnit getOrganisationUnit() {
        return this.organisationUnit;
    }

    public MalariaCasesReport organisationUnit(OrganisationUnit organisationUnit) {
        this.setOrganisationUnit(organisationUnit);
        return this;
    }

    public void setOrganisationUnit(OrganisationUnit organisationUnit) {
        this.organisationUnit = organisationUnit;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MalariaCasesReport)) {
            return false;
        }
        return id != null && id.equals(((MalariaCasesReport) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MalariaCasesReport{" +
            "id=" + getId() +
            ", uid='" + getUid() + "'" +
            ", created='" + getCreated() + "'" +
            ", lastUpdated='" + getLastUpdated() + "'" +
            ", rdtTested=" + getRdtTested() +
            ", rdtPositive=" + getRdtPositive() +
            ", rdtPf=" + getRdtPf() +
            ", rdtPv=" + getRdtPv() +
            ", rdtPother=" + getRdtPother() +
            ", microTested=" + getMicroTested() +
            ", microPositive=" + getMicroPositive() +
            ", microPf=" + getMicroPf() +
            ", microPv=" + getMicroPv() +
            ", microMix=" + getMicroMix() +
            ", microPother=" + getMicroPother() +
            ", probableCases=" + getProbableCases() +
            ", inpatientCases=" + getInpatientCases() +
            ", deathCases=" + getDeathCases() +
            ", treated=" + getTreated() +
            ", suspectedCases=" + getSuspectedCases() +
            ", totalFrequents=" + getTotalFrequents() +
            ", comment='" + getComment() + "'" +
            "}";
    }
}
