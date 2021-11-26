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
import org.nmcpye.activitiesmanagement.domain.enumeration.StatusOfFamilyTarget;

/**
 * A LlinsFamilyTarget.
 */
@Entity
@Table(name = "llins_family_target")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class LlinsFamilyTarget implements Serializable {

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
    @Column(name = "residents_individuals_planned", nullable = false)
    private Integer residentsIndividualsPlanned;

    @NotNull
    @Min(value = 0)
    @Column(name = "idps_individuals_planned", nullable = false)
    private Integer idpsIndividualsPlanned;

    @NotNull
    @Min(value = 0)
    @Column(name = "quantity_planned", nullable = false)
    private Integer quantityPlanned;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "family_type", nullable = false)
    private FamilyType familyType;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status_of_family_target", nullable = false)
    private StatusOfFamilyTarget statusOfFamilyTarget;

    @OneToMany(mappedBy = "targetDetails")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = { "llinsFamilyReportHistories", "user", "createdBy", "lastUpdatedBy", "dayReached", "targetDetails", "executingTeam" },
        allowSetters = true
    )
    private Set<LlinsFamilyReport> llinsFamilyReports = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "created_by")
    private User createdBy;

    @ManyToOne
    @JoinColumn(name = "last_updated_by")
    private User lastUpdatedBy;

    @ManyToOne(optional = false)
    @NotNull
    private WorkingDay dayPlanned;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(
        value = { "familyHeads", "dataProviders", "fingerprints", "llinsFamilyTargets", "organisationUnit", "user", "createdBy", "lastUpdatedBy" },
        allowSetters = true
    )
    private Family family;

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

    public LlinsFamilyTarget id(Long id) {
        this.id = id;
        return this;
    }

    public String getUid() {
        return this.uid;
    }

    public LlinsFamilyTarget uid(String uid) {
        this.uid = uid;
        return this;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Instant getCreated() {
        return this.created;
    }

    public LlinsFamilyTarget created(Instant created) {
        this.created = created;
        return this;
    }

    public void setCreated(Instant created) {
        this.created = created;
    }

    public Instant getLastUpdated() {
        return this.lastUpdated;
    }

    public LlinsFamilyTarget lastUpdated(Instant lastUpdated) {
        this.lastUpdated = lastUpdated;
        return this;
    }

    public void setLastUpdated(Instant lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public Integer getResidentsIndividualsPlanned() {
        return this.residentsIndividualsPlanned;
    }

    public LlinsFamilyTarget residentsIndividualsPlanned(Integer residentsIndividualsPlanned) {
        this.residentsIndividualsPlanned = residentsIndividualsPlanned;
        return this;
    }

    public void setResidentsIndividualsPlanned(Integer residentsIndividualsPlanned) {
        this.residentsIndividualsPlanned = residentsIndividualsPlanned;
    }

    public Integer getIdpsIndividualsPlanned() {
        return this.idpsIndividualsPlanned;
    }

    public LlinsFamilyTarget idpsIndividualsPlanned(Integer idpsIndividualsPlanned) {
        this.idpsIndividualsPlanned = idpsIndividualsPlanned;
        return this;
    }

    public void setIdpsIndividualsPlanned(Integer idpsIndividualsPlanned) {
        this.idpsIndividualsPlanned = idpsIndividualsPlanned;
    }

    public Integer getQuantityPlanned() {
        return this.quantityPlanned;
    }

    public LlinsFamilyTarget quantityPlanned(Integer quantityPlanned) {
        this.quantityPlanned = quantityPlanned;
        return this;
    }

    public void setQuantityPlanned(Integer quantityPlanned) {
        this.quantityPlanned = quantityPlanned;
    }

    public FamilyType getFamilyType() {
        return this.familyType;
    }

    public LlinsFamilyTarget familyType(FamilyType familyType) {
        this.familyType = familyType;
        return this;
    }

    public void setFamilyType(FamilyType familyType) {
        this.familyType = familyType;
    }

    public StatusOfFamilyTarget getStatusOfFamilyTarget() {
        return this.statusOfFamilyTarget;
    }

    public LlinsFamilyTarget statusOfFamilyTarget(StatusOfFamilyTarget statusOfFamilyTarget) {
        this.statusOfFamilyTarget = statusOfFamilyTarget;
        return this;
    }

    public void setStatusOfFamilyTarget(StatusOfFamilyTarget statusOfFamilyTarget) {
        this.statusOfFamilyTarget = statusOfFamilyTarget;
    }

    public Set<LlinsFamilyReport> getLlinsFamilyReports() {
        return this.llinsFamilyReports;
    }

    public LlinsFamilyTarget llinsFamilyReports(Set<LlinsFamilyReport> lLINSFamilyReports) {
        this.setLlinsFamilyReports(lLINSFamilyReports);
        return this;
    }

    public LlinsFamilyTarget addLlinsFamilyReport(LlinsFamilyReport lLINSFamilyReport) {
        this.llinsFamilyReports.add(lLINSFamilyReport);
        lLINSFamilyReport.setTargetDetails(this);
        return this;
    }

    public LlinsFamilyTarget removeLlinsFamilyReport(LlinsFamilyReport lLINSFamilyReport) {
        this.llinsFamilyReports.remove(lLINSFamilyReport);
        lLINSFamilyReport.setTargetDetails(null);
        return this;
    }

    public void setLlinsFamilyReports(Set<LlinsFamilyReport> lLINSFamilyReports) {
        if (this.llinsFamilyReports != null) {
            this.llinsFamilyReports.forEach(i -> i.setTargetDetails(null));
        }
        if (lLINSFamilyReports != null) {
            lLINSFamilyReports.forEach(i -> i.setTargetDetails(this));
        }
        this.llinsFamilyReports = lLINSFamilyReports;
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
    public LlinsFamilyTarget user(User user) {
        this.setUser(user);
        return this;
    }

    public User getLastUpdatedBy() {
        return this.lastUpdatedBy;
    }

    public LlinsFamilyTarget lastUpdatedBy(User user) {
        this.setLastUpdatedBy(user);
        return this;
    }

    public void setLastUpdatedBy(User user) {
        this.lastUpdatedBy = user;
    }

    public WorkingDay getDayPlanned() {
        return this.dayPlanned;
    }

    public LlinsFamilyTarget dayPlanned(WorkingDay workingDay) {
        this.setDayPlanned(workingDay);
        return this;
    }

    public void setDayPlanned(WorkingDay workingDay) {
        this.dayPlanned = workingDay;
    }

    public Family getFamily() {
        return this.family;
    }

    public LlinsFamilyTarget family(Family family) {
        this.setFamily(family);
        return this;
    }

    public void setFamily(Family family) {
        this.family = family;
    }

    public Team getTeamAssigned() {
        return this.teamAssigned;
    }

    public LlinsFamilyTarget teamAssigned(Team team) {
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
        if (!(o instanceof LlinsFamilyTarget)) {
            return false;
        }
        return id != null && id.equals(((LlinsFamilyTarget) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LlinsFamilyTarget{" +
            "id=" + getId() +
            ", uid='" + getUid() + "'" +
            ", created='" + getCreated() + "'" +
            ", lastUpdated='" + getLastUpdated() + "'" +
            ", residentsIndividualsPlanned=" + getResidentsIndividualsPlanned() +
            ", idpsIndividualsPlanned=" + getIdpsIndividualsPlanned() +
            ", quantityPlanned=" + getQuantityPlanned() +
            ", familyType='" + getFamilyType() + "'" +
            ", statusOfFamilyTarget='" + getStatusOfFamilyTarget() + "'" +
            "}";
    }
}
