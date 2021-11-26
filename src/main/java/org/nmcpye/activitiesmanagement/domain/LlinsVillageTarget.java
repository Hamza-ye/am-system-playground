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
import org.nmcpye.activitiesmanagement.domain.organisationunit.OrganisationUnit;

/**
 * A LlinsVillageTarget.
 */
@Entity
@Table(name = "llins_village_target")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class LlinsVillageTarget implements Serializable {

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
    @Column(name = "residents_individuals", nullable = false)
    private Integer residentsIndividuals;

    @NotNull
    @Min(value = 0)
    @Column(name = "idps_individuals", nullable = false)
    private Integer idpsIndividuals;

    @NotNull
    @Min(value = 0)
    @Column(name = "residents_families", nullable = false)
    private Integer residentsFamilies;

    @NotNull
    @Min(value = 0)
    @Column(name = "idps_families", nullable = false)
    private Integer idpsFamilies;

    @Min(value = 0)
    @Column(name = "no_of_days_needed")
    private Integer noOfDaysNeeded;

    @NotNull
    @Min(value = 0)
    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @OneToMany(mappedBy = "targetDetails")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = { "llinsVillageReportHistories", "user", "createdBy", "lastUpdatedBy", "dayReached", "targetDetails", "executingTeam" },
        allowSetters = true
    )
    private Set<LlinsVillageReport> llinsVillageReports = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
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

    @ManyToOne
    @JoinColumn(name = "created_by")
    private User createdBy;

    @ManyToOne
    @JoinColumn(name = "last_updated_by")
    private User lastUpdatedBy;

    @ManyToOne(optional = false)
    @NotNull
    private WorkingDay dayPlanned;

    @ManyToOne
    @JsonIgnoreProperties(value = { "user", "createdBy", "lastUpdatedBy" }, allowSetters = true)
    private StatusOfCoverage statusOfCoverage;

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
    private Team teamAssigned;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LlinsVillageTarget id(Long id) {
        this.id = id;
        return this;
    }

    public String getUid() {
        return this.uid;
    }

    public LlinsVillageTarget uid(String uid) {
        this.uid = uid;
        return this;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Instant getCreated() {
        return this.created;
    }

    public LlinsVillageTarget created(Instant created) {
        this.created = created;
        return this;
    }

    public void setCreated(Instant created) {
        this.created = created;
    }

    public Instant getLastUpdated() {
        return this.lastUpdated;
    }

    public LlinsVillageTarget lastUpdated(Instant lastUpdated) {
        this.lastUpdated = lastUpdated;
        return this;
    }

    public void setLastUpdated(Instant lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public Integer getResidentsIndividuals() {
        return this.residentsIndividuals;
    }

    public LlinsVillageTarget residentsIndividuals(Integer residentsIndividuals) {
        this.residentsIndividuals = residentsIndividuals;
        return this;
    }

    public void setResidentsIndividuals(Integer residentsIndividuals) {
        this.residentsIndividuals = residentsIndividuals;
    }

    public Integer getIdpsIndividuals() {
        return this.idpsIndividuals;
    }

    public LlinsVillageTarget idpsIndividuals(Integer idpsIndividuals) {
        this.idpsIndividuals = idpsIndividuals;
        return this;
    }

    public void setIdpsIndividuals(Integer idpsIndividuals) {
        this.idpsIndividuals = idpsIndividuals;
    }

    public Integer getResidentsFamilies() {
        return this.residentsFamilies;
    }

    public LlinsVillageTarget residentsFamilies(Integer residentsFamilies) {
        this.residentsFamilies = residentsFamilies;
        return this;
    }

    public void setResidentsFamilies(Integer residentsFamilies) {
        this.residentsFamilies = residentsFamilies;
    }

    public Integer getIdpsFamilies() {
        return this.idpsFamilies;
    }

    public LlinsVillageTarget idpsFamilies(Integer idpsFamilies) {
        this.idpsFamilies = idpsFamilies;
        return this;
    }

    public void setIdpsFamilies(Integer idpsFamilies) {
        this.idpsFamilies = idpsFamilies;
    }

    public Integer getNoOfDaysNeeded() {
        return this.noOfDaysNeeded;
    }

    public LlinsVillageTarget noOfDaysNeeded(Integer noOfDaysNeeded) {
        this.noOfDaysNeeded = noOfDaysNeeded;
        return this;
    }

    public void setNoOfDaysNeeded(Integer noOfDaysNeeded) {
        this.noOfDaysNeeded = noOfDaysNeeded;
    }

    public Integer getQuantity() {
        return this.quantity;
    }

    public LlinsVillageTarget quantity(Integer quantity) {
        this.quantity = quantity;
        return this;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Set<LlinsVillageReport> getLlinsVillageReports() {
        return this.llinsVillageReports;
    }

    public LlinsVillageTarget llinsVillageReports(Set<LlinsVillageReport> lLINSVillageReports) {
        this.setLlinsVillageReports(lLINSVillageReports);
        return this;
    }

    public LlinsVillageTarget addLlinsVillageReport(LlinsVillageReport lLINSVillageReport) {
        this.llinsVillageReports.add(lLINSVillageReport);
        lLINSVillageReport.setTargetDetails(this);
        return this;
    }

    public LlinsVillageTarget removeLlinsVillageReport(LlinsVillageReport lLINSVillageReport) {
        this.llinsVillageReports.remove(lLINSVillageReport);
        lLINSVillageReport.setTargetDetails(null);
        return this;
    }

    public void setLlinsVillageReports(Set<LlinsVillageReport> lLINSVillageReports) {
        if (this.llinsVillageReports != null) {
            this.llinsVillageReports.forEach(i -> i.setTargetDetails(null));
        }
        if (lLINSVillageReports != null) {
            lLINSVillageReports.forEach(i -> i.setTargetDetails(this));
        }
        this.llinsVillageReports = lLINSVillageReports;
    }

    public OrganisationUnit getOrganisationUnit() {
        return this.organisationUnit;
    }

    public LlinsVillageTarget organisationUnit(OrganisationUnit organisationUnit) {
        this.setOrganisationUnit(organisationUnit);
        return this;
    }

    public void setOrganisationUnit(OrganisationUnit organisationUnit) {
        this.organisationUnit = organisationUnit;
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
    public LlinsVillageTarget user(User user) {
        this.setUser(user);
        return this;
    }

    public User getLastUpdatedBy() {
        return this.lastUpdatedBy;
    }

    public LlinsVillageTarget lastUpdatedBy(User user) {
        this.setLastUpdatedBy(user);
        return this;
    }

    public void setLastUpdatedBy(User user) {
        this.lastUpdatedBy = user;
    }

    public WorkingDay getDayPlanned() {
        return this.dayPlanned;
    }

    public LlinsVillageTarget dayPlanned(WorkingDay workingDay) {
        this.setDayPlanned(workingDay);
        return this;
    }

    public void setDayPlanned(WorkingDay workingDay) {
        this.dayPlanned = workingDay;
    }

    public StatusOfCoverage getStatusOfCoverage() {
        return this.statusOfCoverage;
    }

    public LlinsVillageTarget statusOfCoverage(StatusOfCoverage statusOfCoverage) {
        this.setStatusOfCoverage(statusOfCoverage);
        return this;
    }

    public void setStatusOfCoverage(StatusOfCoverage statusOfCoverage) {
        this.statusOfCoverage = statusOfCoverage;
    }

    public Team getTeamAssigned() {
        return this.teamAssigned;
    }

    public LlinsVillageTarget teamAssigned(Team team) {
        this.setTeamAssigned(team);
        return this;
    }

    public void setTeamAssigned(Team team) {
        this.teamAssigned = team;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LlinsVillageTarget)) {
            return false;
        }
        return id != null && id.equals(((LlinsVillageTarget) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LlinsVillageTarget{" +
            "id=" + getId() +
            ", uid='" + getUid() + "'" +
            ", created='" + getCreated() + "'" +
            ", lastUpdated='" + getLastUpdated() + "'" +
            ", residentsIndividuals=" + getResidentsIndividuals() +
            ", idpsIndividuals=" + getIdpsIndividuals() +
            ", residentsFamilies=" + getResidentsFamilies() +
            ", idpsFamilies=" + getIdpsFamilies() +
            ", noOfDaysNeeded=" + getNoOfDaysNeeded() +
            ", quantity=" + getQuantity() +
            "}";
    }
}
