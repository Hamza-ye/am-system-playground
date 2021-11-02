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
import org.nmcpye.activitiesmanagement.domain.enumeration.TeamType;

/**
 * A Team.
 */
@Entity
@Table(name = "team")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Team implements Serializable {

    private static final long serialVersionUID = 1L;

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

    @Column(name = "name")
    private String name;

    @Column(name = "created")
    private Instant created;

    @Column(name = "last_updated")
    private Instant lastUpdated;

    @NotNull
    @Min(value = 1)
    @Column(name = "team_no", nullable = false)
    private Integer teamNo;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "team_type", nullable = false)
    private TeamType teamType;

    @OneToMany(mappedBy = "team")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "user", "lastUpdatedBy", "day", "initiatedWH", "theOtherSideWH", "team" }, allowSetters = true)
    private Set<WHMovement> whMovements = new HashSet<>();

    @OneToMany(mappedBy = "teamAssigned")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = { "llinsVillageReports", "organisationUnit", "user", "lastUpdatedBy", "dayPlanned", "statusOfCoverage", "teamAssigned" },
        allowSetters = true
    )
    private Set<LLINSVillageTarget> llinsVillageTargets = new HashSet<>();

    @OneToMany(mappedBy = "executingTeam")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = { "llinsVillageReportHistories", "user", "lastUpdatedBy", "dayReached", "targetDetails", "executingTeam" },
        allowSetters = true
    )
    private Set<LLINSVillageReport> llinsVillageReports = new HashSet<>();

    @OneToMany(mappedBy = "teamAssigned")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = { "llinsFamilyReports", "user", "lastUpdatedBy", "dayPlanned", "family", "teamAssigned" },
        allowSetters = true
    )
    private Set<LLINSFamilyTarget> llinsFamilyTargets = new HashSet<>();

    @OneToMany(mappedBy = "executingTeam")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = { "llinsFamilyReportHistories", "user", "lastUpdatedBy", "dayReached", "targetDetails", "executingTeam" },
        allowSetters = true
    )
    private Set<LLINSFamilyReport> llinsFamilyReports = new HashSet<>();

    @ManyToOne
    private User user;

    @ManyToOne
    private User lastUpdatedBy;

    @ManyToOne
    @JsonIgnoreProperties(
        value = {
            "userInfo", "user", "lastUpdatedBy", "organisationUnits", "dataViewOrganisationUnits", "personAuthorityGroups", "groups",
        },
        allowSetters = true
    )
    private Person person;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(
        name = "rel_team__assigned_to_warehouse",
        joinColumns = @JoinColumn(name = "team_id"),
        inverseJoinColumns = @JoinColumn(name = "assigned_to_warehouse_id")
    )
    @JsonIgnoreProperties(
        value = { "initiatedMovements", "notInitiatedMovements", "user", "lastUpdatedBy", "activity", "teams" },
        allowSetters = true
    )
    private Set<Warehouse> assignedToWarehouses = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Team id(Long id) {
        this.id = id;
        return this;
    }

    public String getUid() {
        return this.uid;
    }

    public Team uid(String uid) {
        this.uid = uid;
        return this;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getCode() {
        return this.code;
    }

    public Team code(String code) {
        this.code = code;
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return this.name;
    }

    public Team name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Instant getCreated() {
        return this.created;
    }

    public Team created(Instant created) {
        this.created = created;
        return this;
    }

    public void setCreated(Instant created) {
        this.created = created;
    }

    public Instant getLastUpdated() {
        return this.lastUpdated;
    }

    public Team lastUpdated(Instant lastUpdated) {
        this.lastUpdated = lastUpdated;
        return this;
    }

    public void setLastUpdated(Instant lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public Integer getTeamNo() {
        return this.teamNo;
    }

    public Team teamNo(Integer teamNo) {
        this.teamNo = teamNo;
        return this;
    }

    public void setTeamNo(Integer teamNo) {
        this.teamNo = teamNo;
    }

    public TeamType getTeamType() {
        return this.teamType;
    }

    public Team teamType(TeamType teamType) {
        this.teamType = teamType;
        return this;
    }

    public void setTeamType(TeamType teamType) {
        this.teamType = teamType;
    }

    public Set<WHMovement> getWhMovements() {
        return this.whMovements;
    }

    public Team whMovements(Set<WHMovement> wHMovements) {
        this.setWhMovements(wHMovements);
        return this;
    }

    public Team addWhMovement(WHMovement wHMovement) {
        this.whMovements.add(wHMovement);
        wHMovement.setTeam(this);
        return this;
    }

    public Team removeWhMovement(WHMovement wHMovement) {
        this.whMovements.remove(wHMovement);
        wHMovement.setTeam(null);
        return this;
    }

    public void setWhMovements(Set<WHMovement> wHMovements) {
        if (this.whMovements != null) {
            this.whMovements.forEach(i -> i.setTeam(null));
        }
        if (wHMovements != null) {
            wHMovements.forEach(i -> i.setTeam(this));
        }
        this.whMovements = wHMovements;
    }

    public Set<LLINSVillageTarget> getLlinsVillageTargets() {
        return this.llinsVillageTargets;
    }

    public Team llinsVillageTargets(Set<LLINSVillageTarget> lLINSVillageTargets) {
        this.setLlinsVillageTargets(lLINSVillageTargets);
        return this;
    }

    public Team addLlinsVillageTarget(LLINSVillageTarget lLINSVillageTarget) {
        this.llinsVillageTargets.add(lLINSVillageTarget);
        lLINSVillageTarget.setTeamAssigned(this);
        return this;
    }

    public Team removeLlinsVillageTarget(LLINSVillageTarget lLINSVillageTarget) {
        this.llinsVillageTargets.remove(lLINSVillageTarget);
        lLINSVillageTarget.setTeamAssigned(null);
        return this;
    }

    public void setLlinsVillageTargets(Set<LLINSVillageTarget> lLINSVillageTargets) {
        if (this.llinsVillageTargets != null) {
            this.llinsVillageTargets.forEach(i -> i.setTeamAssigned(null));
        }
        if (lLINSVillageTargets != null) {
            lLINSVillageTargets.forEach(i -> i.setTeamAssigned(this));
        }
        this.llinsVillageTargets = lLINSVillageTargets;
    }

    public Set<LLINSVillageReport> getLlinsVillageReports() {
        return this.llinsVillageReports;
    }

    public Team llinsVillageReports(Set<LLINSVillageReport> lLINSVillageReports) {
        this.setLlinsVillageReports(lLINSVillageReports);
        return this;
    }

    public Team addLlinsVillageReport(LLINSVillageReport lLINSVillageReport) {
        this.llinsVillageReports.add(lLINSVillageReport);
        lLINSVillageReport.setExecutingTeam(this);
        return this;
    }

    public Team removeLlinsVillageReport(LLINSVillageReport lLINSVillageReport) {
        this.llinsVillageReports.remove(lLINSVillageReport);
        lLINSVillageReport.setExecutingTeam(null);
        return this;
    }

    public void setLlinsVillageReports(Set<LLINSVillageReport> lLINSVillageReports) {
        if (this.llinsVillageReports != null) {
            this.llinsVillageReports.forEach(i -> i.setExecutingTeam(null));
        }
        if (lLINSVillageReports != null) {
            lLINSVillageReports.forEach(i -> i.setExecutingTeam(this));
        }
        this.llinsVillageReports = lLINSVillageReports;
    }

    public Set<LLINSFamilyTarget> getLlinsFamilyTargets() {
        return this.llinsFamilyTargets;
    }

    public Team llinsFamilyTargets(Set<LLINSFamilyTarget> lLINSFamilyTargets) {
        this.setLlinsFamilyTargets(lLINSFamilyTargets);
        return this;
    }

    public Team addLlinsFamilyTarget(LLINSFamilyTarget lLINSFamilyTarget) {
        this.llinsFamilyTargets.add(lLINSFamilyTarget);
        lLINSFamilyTarget.setTeamAssigned(this);
        return this;
    }

    public Team removeLlinsFamilyTarget(LLINSFamilyTarget lLINSFamilyTarget) {
        this.llinsFamilyTargets.remove(lLINSFamilyTarget);
        lLINSFamilyTarget.setTeamAssigned(null);
        return this;
    }

    public void setLlinsFamilyTargets(Set<LLINSFamilyTarget> lLINSFamilyTargets) {
        if (this.llinsFamilyTargets != null) {
            this.llinsFamilyTargets.forEach(i -> i.setTeamAssigned(null));
        }
        if (lLINSFamilyTargets != null) {
            lLINSFamilyTargets.forEach(i -> i.setTeamAssigned(this));
        }
        this.llinsFamilyTargets = lLINSFamilyTargets;
    }

    public Set<LLINSFamilyReport> getLlinsFamilyReports() {
        return this.llinsFamilyReports;
    }

    public Team llinsFamilyReports(Set<LLINSFamilyReport> lLINSFamilyReports) {
        this.setLlinsFamilyReports(lLINSFamilyReports);
        return this;
    }

    public Team addLlinsFamilyReport(LLINSFamilyReport lLINSFamilyReport) {
        this.llinsFamilyReports.add(lLINSFamilyReport);
        lLINSFamilyReport.setExecutingTeam(this);
        return this;
    }

    public Team removeLlinsFamilyReport(LLINSFamilyReport lLINSFamilyReport) {
        this.llinsFamilyReports.remove(lLINSFamilyReport);
        lLINSFamilyReport.setExecutingTeam(null);
        return this;
    }

    public void setLlinsFamilyReports(Set<LLINSFamilyReport> lLINSFamilyReports) {
        if (this.llinsFamilyReports != null) {
            this.llinsFamilyReports.forEach(i -> i.setExecutingTeam(null));
        }
        if (lLINSFamilyReports != null) {
            lLINSFamilyReports.forEach(i -> i.setExecutingTeam(this));
        }
        this.llinsFamilyReports = lLINSFamilyReports;
    }

    public User getUser() {
        return this.user;
    }

    public Team user(User user) {
        this.setUser(user);
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getLastUpdatedBy() {
        return this.lastUpdatedBy;
    }

    public Team lastUpdatedBy(User user) {
        this.setLastUpdatedBy(user);
        return this;
    }

    public void setLastUpdatedBy(User user) {
        this.lastUpdatedBy = user;
    }

    public Person getPerson() {
        return this.person;
    }

    public Team person(Person person) {
        this.setPerson(person);
        return this;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Set<Warehouse> getAssignedToWarehouses() {
        return this.assignedToWarehouses;
    }

    public Team assignedToWarehouses(Set<Warehouse> warehouses) {
        this.setAssignedToWarehouses(warehouses);
        return this;
    }

    public Team addAssignedToWarehouse(Warehouse warehouse) {
        this.assignedToWarehouses.add(warehouse);
        warehouse.getTeams().add(this);
        return this;
    }

    public Team removeAssignedToWarehouse(Warehouse warehouse) {
        this.assignedToWarehouses.remove(warehouse);
        warehouse.getTeams().remove(this);
        return this;
    }

    public void setAssignedToWarehouses(Set<Warehouse> warehouses) {
        this.assignedToWarehouses = warehouses;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Team)) {
            return false;
        }
        return id != null && id.equals(((Team) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Team{" +
            "id=" + getId() +
            ", uid='" + getUid() + "'" +
            ", code='" + getCode() + "'" +
            ", name='" + getName() + "'" +
            ", created='" + getCreated() + "'" +
            ", lastUpdated='" + getLastUpdated() + "'" +
            ", teamNo=" + getTeamNo() +
            ", teamType='" + getTeamType() + "'" +
            "}";
    }
}
