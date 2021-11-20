package org.nmcpye.activitiesmanagement.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.nmcpye.activitiesmanagement.domain.chv.CHV;
import org.nmcpye.activitiesmanagement.domain.dataset.CasesReportClass;
import org.nmcpye.activitiesmanagement.domain.enumeration.Gender;
import org.nmcpye.activitiesmanagement.domain.enumeration.MalariaTestResult;
import org.nmcpye.activitiesmanagement.domain.organisationunit.OrganisationUnit;

/**
 * A CHVMalariaCaseReport.
 */
@Entity
@Table(name = "chv_malaria_case_report")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CHVMalariaCaseReport implements Serializable {

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

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "individual_name")
    private String individualName;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender;

    @Column(name = "is_pregnant")
    private Boolean isPregnant;

    @Enumerated(EnumType.STRING)
    @Column(name = "malaria_test_result")
    private MalariaTestResult malariaTestResult;

    @Column(name = "drugs_given")
    private Integer drugsGiven;

    @Column(name = "supps_given")
    private Integer suppsGiven;

    @Column(name = "referral")
    private Boolean referral;

    @Column(name = "bar_image_url")
    private String barImageUrl;

    @Column(name = "comment")
    private String comment;

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
    private OrganisationUnit subVillage;

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
    private CHV chv;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "user", "createdBy", "lastUpdatedBy" }, allowSetters = true)
    private CasesReportClass reportClass;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CHVMalariaCaseReport id(Long id) {
        this.id = id;
        return this;
    }

    public String getUid() {
        return this.uid;
    }

    public CHVMalariaCaseReport uid(String uid) {
        this.uid = uid;
        return this;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Instant getCreated() {
        return this.created;
    }

    public CHVMalariaCaseReport created(Instant created) {
        this.created = created;
        return this;
    }

    public void setCreated(Instant created) {
        this.created = created;
    }

    public Instant getLastUpdated() {
        return this.lastUpdated;
    }

    public CHVMalariaCaseReport lastUpdated(Instant lastUpdated) {
        this.lastUpdated = lastUpdated;
        return this;
    }

    public void setLastUpdated(Instant lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public CHVMalariaCaseReport date(LocalDate date) {
        this.date = date;
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getIndividualName() {
        return this.individualName;
    }

    public CHVMalariaCaseReport individualName(String individualName) {
        this.individualName = individualName;
        return this;
    }

    public void setIndividualName(String individualName) {
        this.individualName = individualName;
    }

    public Gender getGender() {
        return this.gender;
    }

    public CHVMalariaCaseReport gender(Gender gender) {
        this.gender = gender;
        return this;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Boolean getIsPregnant() {
        return this.isPregnant;
    }

    public CHVMalariaCaseReport isPregnant(Boolean isPregnant) {
        this.isPregnant = isPregnant;
        return this;
    }

    public void setIsPregnant(Boolean isPregnant) {
        this.isPregnant = isPregnant;
    }

    public MalariaTestResult getMalariaTestResult() {
        return this.malariaTestResult;
    }

    public CHVMalariaCaseReport malariaTestResult(MalariaTestResult malariaTestResult) {
        this.malariaTestResult = malariaTestResult;
        return this;
    }

    public void setMalariaTestResult(MalariaTestResult malariaTestResult) {
        this.malariaTestResult = malariaTestResult;
    }

    public Integer getDrugsGiven() {
        return this.drugsGiven;
    }

    public CHVMalariaCaseReport drugsGiven(Integer drugsGiven) {
        this.drugsGiven = drugsGiven;
        return this;
    }

    public void setDrugsGiven(Integer drugsGiven) {
        this.drugsGiven = drugsGiven;
    }

    public Integer getSuppsGiven() {
        return this.suppsGiven;
    }

    public CHVMalariaCaseReport suppsGiven(Integer suppsGiven) {
        this.suppsGiven = suppsGiven;
        return this;
    }

    public void setSuppsGiven(Integer suppsGiven) {
        this.suppsGiven = suppsGiven;
    }

    public Boolean getReferral() {
        return this.referral;
    }

    public CHVMalariaCaseReport referral(Boolean referral) {
        this.referral = referral;
        return this;
    }

    public void setReferral(Boolean referral) {
        this.referral = referral;
    }

    public String getBarImageUrl() {
        return this.barImageUrl;
    }

    public CHVMalariaCaseReport barImageUrl(String barImageUrl) {
        this.barImageUrl = barImageUrl;
        return this;
    }

    public void setBarImageUrl(String barImageUrl) {
        this.barImageUrl = barImageUrl;
    }

    public String getComment() {
        return this.comment;
    }

    public CHVMalariaCaseReport comment(String comment) {
        this.comment = comment;
        return this;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public OrganisationUnit getSubVillage() {
        return this.subVillage;
    }

    public CHVMalariaCaseReport subVillage(OrganisationUnit organisationUnit) {
        this.setSubVillage(organisationUnit);
        return this;
    }

    public void setSubVillage(OrganisationUnit organisationUnit) {
        this.subVillage = organisationUnit;
    }

    @Deprecated
    public CHVMalariaCaseReport user(User user) {
        this.setUser(user);
        return this;
    }

    public User getLastUpdatedBy() {
        return this.lastUpdatedBy;
    }

    public CHVMalariaCaseReport lastUpdatedBy(User user) {
        this.setLastUpdatedBy(user);
        return this;
    }

    public void setLastUpdatedBy(User user) {
        this.lastUpdatedBy = user;
    }

    public CHV getChv() {
        return this.chv;
    }

    public CHVMalariaCaseReport chv(CHV cHV) {
        this.setChv(cHV);
        return this;
    }

    public void setChv(CHV cHV) {
        this.chv = cHV;
    }

    public CasesReportClass getReportClass() {
        return this.reportClass;
    }

    public CHVMalariaCaseReport reportClass(CasesReportClass casesReportClass) {
        this.setReportClass(casesReportClass);
        return this;
    }

    public void setReportClass(CasesReportClass casesReportClass) {
        this.reportClass = casesReportClass;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CHVMalariaCaseReport)) {
            return false;
        }
        return id != null && id.equals(((CHVMalariaCaseReport) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CHVMalariaCaseReport{" +
            "id=" + getId() +
            ", uid='" + getUid() + "'" +
            ", created='" + getCreated() + "'" +
            ", lastUpdated='" + getLastUpdated() + "'" +
            ", date='" + getDate() + "'" +
            ", individualName='" + getIndividualName() + "'" +
            ", gender='" + getGender() + "'" +
            ", isPregnant='" + getIsPregnant() + "'" +
            ", malariaTestResult='" + getMalariaTestResult() + "'" +
            ", drugsGiven=" + getDrugsGiven() +
            ", suppsGiven=" + getSuppsGiven() +
            ", referral='" + getReferral() + "'" +
            ", barImageUrl='" + getBarImageUrl() + "'" +
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
