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
 * A LLINSVillageTarget.
 */
@Entity
@Table(name = "llins_village_target")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class LLINSVillageTarget implements Serializable {

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
        value = { "llinsVillageReportHistories", "user", "lastUpdatedBy", "dayReached", "targetDetails", "executingTeam" },
        allowSetters = true
    )
    private Set<LLINSVillageReport> llinsVillageReports = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
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

    @ManyToOne
    private User user;

    @ManyToOne
    private User lastUpdatedBy;

    @ManyToOne(optional = false)
    @NotNull
    private WorkingDay dayPlanned;

    @ManyToOne
    @JsonIgnoreProperties(value = { "user", "lastUpdatedBy" }, allowSetters = true)
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

    public LLINSVillageTarget id(Long id) {
        this.id = id;
        return this;
    }

    public String getUid() {
        return this.uid;
    }

    public LLINSVillageTarget uid(String uid) {
        this.uid = uid;
        return this;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Instant getCreated() {
        return this.created;
    }

    public LLINSVillageTarget created(Instant created) {
        this.created = created;
        return this;
    }

    public void setCreated(Instant created) {
        this.created = created;
    }

    public Instant getLastUpdated() {
        return this.lastUpdated;
    }

    public LLINSVillageTarget lastUpdated(Instant lastUpdated) {
        this.lastUpdated = lastUpdated;
        return this;
    }

    public void setLastUpdated(Instant lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public Integer getResidentsIndividuals() {
        return this.residentsIndividuals;
    }

    public LLINSVillageTarget residentsIndividuals(Integer residentsIndividuals) {
        this.residentsIndividuals = residentsIndividuals;
        return this;
    }

    public void setResidentsIndividuals(Integer residentsIndividuals) {
        this.residentsIndividuals = residentsIndividuals;
    }

    public Integer getIdpsIndividuals() {
        return this.idpsIndividuals;
    }

    public LLINSVillageTarget idpsIndividuals(Integer idpsIndividuals) {
        this.idpsIndividuals = idpsIndividuals;
        return this;
    }

    public void setIdpsIndividuals(Integer idpsIndividuals) {
        this.idpsIndividuals = idpsIndividuals;
    }

    public Integer getResidentsFamilies() {
        return this.residentsFamilies;
    }

    public LLINSVillageTarget residentsFamilies(Integer residentsFamilies) {
        this.residentsFamilies = residentsFamilies;
        return this;
    }

    public void setResidentsFamilies(Integer residentsFamilies) {
        this.residentsFamilies = residentsFamilies;
    }

    public Integer getIdpsFamilies() {
        return this.idpsFamilies;
    }

    public LLINSVillageTarget idpsFamilies(Integer idpsFamilies) {
        this.idpsFamilies = idpsFamilies;
        return this;
    }

    public void setIdpsFamilies(Integer idpsFamilies) {
        this.idpsFamilies = idpsFamilies;
    }

    public Integer getNoOfDaysNeeded() {
        return this.noOfDaysNeeded;
    }

    public LLINSVillageTarget noOfDaysNeeded(Integer noOfDaysNeeded) {
        this.noOfDaysNeeded = noOfDaysNeeded;
        return this;
    }

    public void setNoOfDaysNeeded(Integer noOfDaysNeeded) {
        this.noOfDaysNeeded = noOfDaysNeeded;
    }

    public Integer getQuantity() {
        return this.quantity;
    }

    public LLINSVillageTarget quantity(Integer quantity) {
        this.quantity = quantity;
        return this;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Set<LLINSVillageReport> getLlinsVillageReports() {
        return this.llinsVillageReports;
    }

    public LLINSVillageTarget llinsVillageReports(Set<LLINSVillageReport> lLINSVillageReports) {
        this.setLlinsVillageReports(lLINSVillageReports);
        return this;
    }

    public LLINSVillageTarget addLlinsVillageReport(LLINSVillageReport lLINSVillageReport) {
        this.llinsVillageReports.add(lLINSVillageReport);
        lLINSVillageReport.setTargetDetails(this);
        return this;
    }

    public LLINSVillageTarget removeLlinsVillageReport(LLINSVillageReport lLINSVillageReport) {
        this.llinsVillageReports.remove(lLINSVillageReport);
        lLINSVillageReport.setTargetDetails(null);
        return this;
    }

    public void setLlinsVillageReports(Set<LLINSVillageReport> lLINSVillageReports) {
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

    public LLINSVillageTarget organisationUnit(OrganisationUnit organisationUnit) {
        this.setOrganisationUnit(organisationUnit);
        return this;
    }

    public void setOrganisationUnit(OrganisationUnit organisationUnit) {
        this.organisationUnit = organisationUnit;
    }

    public User getUser() {
        return this.user;
    }

    public LLINSVillageTarget user(User user) {
        this.setUser(user);
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getLastUpdatedBy() {
        return this.lastUpdatedBy;
    }

    public LLINSVillageTarget lastUpdatedBy(User user) {
        this.setLastUpdatedBy(user);
        return this;
    }

    public void setLastUpdatedBy(User user) {
        this.lastUpdatedBy = user;
    }

    public WorkingDay getDayPlanned() {
        return this.dayPlanned;
    }

    public LLINSVillageTarget dayPlanned(WorkingDay workingDay) {
        this.setDayPlanned(workingDay);
        return this;
    }

    public void setDayPlanned(WorkingDay workingDay) {
        this.dayPlanned = workingDay;
    }

    public StatusOfCoverage getStatusOfCoverage() {
        return this.statusOfCoverage;
    }

    public LLINSVillageTarget statusOfCoverage(StatusOfCoverage statusOfCoverage) {
        this.setStatusOfCoverage(statusOfCoverage);
        return this;
    }

    public void setStatusOfCoverage(StatusOfCoverage statusOfCoverage) {
        this.statusOfCoverage = statusOfCoverage;
    }

    public Team getTeamAssigned() {
        return this.teamAssigned;
    }

    public LLINSVillageTarget teamAssigned(Team team) {
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
        if (!(o instanceof LLINSVillageTarget)) {
            return false;
        }
        return id != null && id.equals(((LLINSVillageTarget) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LLINSVillageTarget{" +
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
