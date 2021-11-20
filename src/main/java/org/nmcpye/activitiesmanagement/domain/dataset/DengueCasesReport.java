package org.nmcpye.activitiesmanagement.domain.dataset;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.*;

import com.fasterxml.jackson.annotation.JsonRootName;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.nmcpye.activitiesmanagement.domain.User;
import org.nmcpye.activitiesmanagement.domain.organisationunit.OrganisationUnit;
import org.nmcpye.activitiesmanagement.domain.period.Period;
import org.nmcpye.activitiesmanagement.extended.common.BaseIdentifiableObject;
import org.nmcpye.activitiesmanagement.extended.common.DxfNamespaces;
import org.nmcpye.activitiesmanagement.extended.common.MetadataObject;

/**
 * A DengueCasesReport.
 */
@Entity
@Table(
    name = "dengue_cases_report",
    uniqueConstraints = {
        @UniqueConstraint(columnNames =
            {"data_set_id", "organisation_unit_id", "period_id", "report_class_id"})
    }
)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@JsonRootName( value = "dengueCasesReport", namespace = DxfNamespaces.DXF_2_0 )
public class DengueCasesReport extends BaseIdentifiableObject implements MetadataObject {

    ////////////////////////
    ///
    /// Common Columns
    ///
    ////////////////////////

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

    @Column(name = "created")
    private Date created;

    @Column(name = "last_updated")
    private Date lastUpdated;

    /**
     * Owner of this object.
     */
    @ManyToOne
    @JoinColumn(name = "created_by")
    protected User createdBy;

    /**
     * Last user updated this object.
     */
    @ManyToOne
    @JoinColumn(name = "last_updated_by")
    protected User lastUpdatedBy;

    ////////////////////////
    ///
    ///
    ////////////////////////

    @Column(name = "rdt_tested")
    private Integer rdtTested;

    @Column(name = "rdt_positive")
    private Integer rdtPositive;

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

    @Column(name = "comment")
    private String comment;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "user", "createdBy", "lastUpdatedBy" }, allowSetters = true)
    private CasesReportClass reportClass;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "periodType" }, allowSetters = true)
    private Period period;

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "malariaCasesReports", "dengueCasesReports", "periodType", "notificationRecipients", "user", "createdBy", "lastUpdatedBy", "sources" },
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

    public DengueCasesReport id(Long id) {
        this.id = id;
        return this;
    }

    public DengueCasesReport uid(String uid) {
        this.uid = uid;
        return this;
    }

    public DengueCasesReport created(Date created) {
        this.created = created;
        return this;
    }

    public DengueCasesReport lastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
        return this;
    }

    public Integer getRdtTested() {
        return this.rdtTested;
    }

    public DengueCasesReport rdtTested(Integer rdtTested) {
        this.rdtTested = rdtTested;
        return this;
    }

    public void setRdtTested(Integer rdtTested) {
        this.rdtTested = rdtTested;
    }

    public Integer getRdtPositive() {
        return this.rdtPositive;
    }

    public DengueCasesReport rdtPositive(Integer rdtPositive) {
        this.rdtPositive = rdtPositive;
        return this;
    }

    public void setRdtPositive(Integer rdtPositive) {
        this.rdtPositive = rdtPositive;
    }

    public Integer getProbableCases() {
        return this.probableCases;
    }

    public DengueCasesReport probableCases(Integer probableCases) {
        this.probableCases = probableCases;
        return this;
    }

    public void setProbableCases(Integer probableCases) {
        this.probableCases = probableCases;
    }

    public Integer getInpatientCases() {
        return this.inpatientCases;
    }

    public DengueCasesReport inpatientCases(Integer inpatientCases) {
        this.inpatientCases = inpatientCases;
        return this;
    }

    public void setInpatientCases(Integer inpatientCases) {
        this.inpatientCases = inpatientCases;
    }

    public Integer getDeathCases() {
        return this.deathCases;
    }

    public DengueCasesReport deathCases(Integer deathCases) {
        this.deathCases = deathCases;
        return this;
    }

    public void setDeathCases(Integer deathCases) {
        this.deathCases = deathCases;
    }

    public Integer getTreated() {
        return this.treated;
    }

    public DengueCasesReport treated(Integer treated) {
        this.treated = treated;
        return this;
    }

    public void setTreated(Integer treated) {
        this.treated = treated;
    }

    public Integer getSuspectedCases() {
        return this.suspectedCases;
    }

    public DengueCasesReport suspectedCases(Integer suspectedCases) {
        this.suspectedCases = suspectedCases;
        return this;
    }

    public void setSuspectedCases(Integer suspectedCases) {
        this.suspectedCases = suspectedCases;
    }

    public String getComment() {
        return this.comment;
    }

    public DengueCasesReport comment(String comment) {
        this.comment = comment;
        return this;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public DengueCasesReport user(User user) {
        this.setUser(user);
        return this;
    }

    public DengueCasesReport lastUpdatedBy(User user) {
        this.setLastUpdatedBy(user);
        return this;
    }

    public CasesReportClass getReportClass() {
        return this.reportClass;
    }

    public DengueCasesReport reportClass(CasesReportClass casesReportClass) {
        this.setReportClass(casesReportClass);
        return this;
    }

    public void setReportClass(CasesReportClass casesReportClass) {
        this.reportClass = casesReportClass;
    }

    public Period getPeriod() {
        return this.period;
    }

    public DengueCasesReport period(Period period) {
        this.setPeriod(period);
        return this;
    }

    public void setPeriod(Period period) {
        this.period = period;
    }

    public DataSet getDataSet() {
        return this.dataSet;
    }

    public DengueCasesReport dataSet(DataSet dataSet) {
        this.setDataSet(dataSet);
        return this;
    }

    public void setDataSet(DataSet dataSet) {
        this.dataSet = dataSet;
    }

    public OrganisationUnit getOrganisationUnit() {
        return this.organisationUnit;
    }

    public DengueCasesReport organisationUnit(OrganisationUnit organisationUnit) {
        this.setOrganisationUnit(organisationUnit);
        return this;
    }

    public void setOrganisationUnit(OrganisationUnit organisationUnit) {
        this.organisationUnit = organisationUnit;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return getClass().getSimpleName();
    }

    @Override
    public String getUid() {
        return uid;
    }

    @Override
    public void setUid(String uid) {
        this.uid = uid;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public Date getCreated() {
        return created;
    }

    @Override
    public void setCreated(Date created) {
        this.created = created;
    }

    @Override
    public Date getLastUpdated() {
        return lastUpdated;
    }

    @Override
    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    @Override
    public User getCreatedBy() {
        return createdBy;
    }

    @Override
    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    @Override
    public User getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    @Override
    public void setLastUpdatedBy(User lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }
}
