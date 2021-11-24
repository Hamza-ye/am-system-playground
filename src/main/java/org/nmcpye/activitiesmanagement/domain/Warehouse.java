package org.nmcpye.activitiesmanagement.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.nmcpye.activitiesmanagement.domain.activity.Activity;

/**
 * A Warehouse.
 */
@Entity
@Table(name = "warehouse")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Warehouse implements Serializable {

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
    @Column(name = "wh_no", nullable = false)
    private Integer whNo;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "initial_balance_plan", precision = 21, scale = 2, nullable = false)
    private BigDecimal initialBalancePlan;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "initial_balance_actual", precision = 21, scale = 2, nullable = false)
    private BigDecimal initialBalanceActual;

    @OneToMany(mappedBy = "initiatedWH")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "user", "createdBy", "lastUpdatedBy", "day", "initiatedWH", "theOtherSideWH", "team" }, allowSetters = true)
    private Set<WHMovement> initiatedMovements = new HashSet<>();

    @OneToMany(mappedBy = "theOtherSideWH")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "user", "createdBy", "lastUpdatedBy", "day", "initiatedWH", "theOtherSideWH", "team" }, allowSetters = true)
    private Set<WHMovement> notInitiatedMovements = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "created_by")
    private User createdBy;

    @ManyToOne
    @JoinColumn(name = "last_updated_by")
    private User lastUpdatedBy;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "warehouses", "user", "createdBy", "lastUpdatedBy", "project" }, allowSetters = true)
    private Activity activity;

    @ManyToMany(mappedBy = "assignedToWarehouses")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
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
    private Set<Team> teams = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    @JsonProperty
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Warehouse id(Long id) {
        this.id = id;
        return this;
    }

    @JsonProperty
    public String getUid() {
        return this.uid;
    }

    public Warehouse uid(String uid) {
        this.uid = uid;
        return this;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    @JsonProperty
    public String getCode() {
        return this.code;
    }

    public Warehouse code(String code) {
        this.code = code;
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @JsonProperty
    public String getName() {
        return this.name;
    }

    public Warehouse name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty
    public Instant getCreated() {
        return this.created;
    }

    public Warehouse created(Instant created) {
        this.created = created;
        return this;
    }

    public void setCreated(Instant created) {
        this.created = created;
    }

    @JsonProperty
    public Instant getLastUpdated() {
        return this.lastUpdated;
    }

    public Warehouse lastUpdated(Instant lastUpdated) {
        this.lastUpdated = lastUpdated;
        return this;
    }

    public void setLastUpdated(Instant lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    @JsonProperty
    public Integer getWhNo() {
        return this.whNo;
    }

    public Warehouse whNo(Integer whNo) {
        this.whNo = whNo;
        return this;
    }

    public void setWhNo(Integer whNo) {
        this.whNo = whNo;
    }

    @JsonProperty
    public BigDecimal getInitialBalancePlan() {
        return this.initialBalancePlan;
    }

    public Warehouse initialBalancePlan(BigDecimal initialBalancePlan) {
        this.initialBalancePlan = initialBalancePlan;
        return this;
    }

    public void setInitialBalancePlan(BigDecimal initialBalancePlan) {
        this.initialBalancePlan = initialBalancePlan;
    }

    @JsonProperty
    public BigDecimal getInitialBalanceActual() {
        return this.initialBalanceActual;
    }

    public Warehouse initialBalanceActual(BigDecimal initialBalanceActual) {
        this.initialBalanceActual = initialBalanceActual;
        return this;
    }

    public void setInitialBalanceActual(BigDecimal initialBalanceActual) {
        this.initialBalanceActual = initialBalanceActual;
    }

    @JsonProperty
    public Set<WHMovement> getInitiatedMovements() {
        return this.initiatedMovements;
    }

    public Warehouse initiatedMovements(Set<WHMovement> wHMovements) {
        this.setInitiatedMovements(wHMovements);
        return this;
    }

    public Warehouse addInitiatedMovement(WHMovement wHMovement) {
        this.initiatedMovements.add(wHMovement);
        wHMovement.setInitiatedWH(this);
        return this;
    }

    public Warehouse removeInitiatedMovement(WHMovement wHMovement) {
        this.initiatedMovements.remove(wHMovement);
        wHMovement.setInitiatedWH(null);
        return this;
    }

    public void setInitiatedMovements(Set<WHMovement> wHMovements) {
        if (this.initiatedMovements != null) {
            this.initiatedMovements.forEach(i -> i.setInitiatedWH(null));
        }
        if (wHMovements != null) {
            wHMovements.forEach(i -> i.setInitiatedWH(this));
        }
        this.initiatedMovements = wHMovements;
    }

    @JsonProperty
    public Set<WHMovement> getNotInitiatedMovements() {
        return this.notInitiatedMovements;
    }

    public Warehouse notInitiatedMovements(Set<WHMovement> wHMovements) {
        this.setNotInitiatedMovements(wHMovements);
        return this;
    }

    public Warehouse addNotInitiatedMovement(WHMovement wHMovement) {
        this.notInitiatedMovements.add(wHMovement);
        wHMovement.setTheOtherSideWH(this);
        return this;
    }

    public Warehouse removeNotInitiatedMovement(WHMovement wHMovement) {
        this.notInitiatedMovements.remove(wHMovement);
        wHMovement.setTheOtherSideWH(null);
        return this;
    }

    public void setNotInitiatedMovements(Set<WHMovement> wHMovements) {
        if (this.notInitiatedMovements != null) {
            this.notInitiatedMovements.forEach(i -> i.setTheOtherSideWH(null));
        }
        if (wHMovements != null) {
            wHMovements.forEach(i -> i.setTheOtherSideWH(this));
        }
        this.notInitiatedMovements = wHMovements;
    }

    //    @Override
    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    //    @Override
    @JsonProperty
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
    public Warehouse user(User user) {
        this.setUser(user);
        return this;
    }

    //    @Override
    @JsonProperty
    public User getLastUpdatedBy() {
        return this.lastUpdatedBy;
    }

    public Warehouse lastUpdatedBy(User user) {
        this.setLastUpdatedBy(user);
        return this;
    }

    //    @Override
    public void setLastUpdatedBy(User user) {
        this.lastUpdatedBy = user;
    }

    @JsonProperty
    public Activity getActivity() {
        return this.activity;
    }

    public Warehouse activity(Activity activity) {
        this.setActivity(activity);
        return this;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    @JsonProperty
    public Set<Team> getTeams() {
        return this.teams;
    }

    public Warehouse teams(Set<Team> teams) {
        this.setTeams(teams);
        return this;
    }

    public Warehouse addTeam(Team team) {
        this.teams.add(team);
        team.getAssignedToWarehouses().add(this);
        return this;
    }

    public Warehouse removeTeam(Team team) {
        this.teams.remove(team);
        team.getAssignedToWarehouses().remove(this);
        return this;
    }

    public void setTeams(Set<Team> teams) {
        if (this.teams != null) {
            this.teams.forEach(i -> i.removeAssignedToWarehouse(this));
        }
        if (teams != null) {
            teams.forEach(i -> i.addAssignedToWarehouse(this));
        }
        this.teams = teams;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Warehouse)) {
            return false;
        }
        return id != null && id.equals(((Warehouse) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Warehouse{" +
            "id=" + getId() +
            ", uid='" + getUid() + "'" +
            ", code='" + getCode() + "'" +
            ", name='" + getName() + "'" +
            ", created='" + getCreated() + "'" +
            ", lastUpdated='" + getLastUpdated() + "'" +
            ", whNo=" + getWhNo() +
            ", initialBalancePlan=" + getInitialBalancePlan() +
            ", initialBalanceActual=" + getInitialBalanceActual() +
            "}";
    }
}
