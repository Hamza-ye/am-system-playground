package org.nmcpye.activitiesmanagement.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.nmcpye.activitiesmanagement.domain.enumeration.MovementType;

/**
 * A WhMovement.
 */
@Entity
@Table(name = "wh_movement")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class WhMovement implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "movement_type", nullable = false)
    private MovementType movementType;

    @NotNull
    @Min(value = 0)
    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "reconciliation_source")
    private String reconciliationSource;

    @Column(name = "reconciliation_destination")
    private String reconciliationDestination;

    @Column(name = "confirmed_by_other_side")
    private Boolean confirmedByOtherSide;

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
    private WorkingDay day;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(
        value = { "initiatedMovements", "notInitiatedMovements", "user", "createdBy", "lastUpdatedBy", "activity", "teams" },
        allowSetters = true
    )
//    @JoinColumn(name = "initiated_wh_id")
    private Warehouse initiatedWh;

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "initiatedMovements", "notInitiatedMovements", "user", "createdBy", "lastUpdatedBy", "activity", "teams" },
        allowSetters = true
    )
//    @JoinColumn(name = "the_other_side_wh_id")
    private Warehouse theOtherSideWh;

    @ManyToOne
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
    private Team team;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    //    @Override
    @JsonProperty
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public WhMovement id(Long id) {
        this.id = id;
        return this;
    }

    @JsonProperty
    public MovementType getMovementType() {
        return this.movementType;
    }

    public WhMovement movementType(MovementType movementType) {
        this.movementType = movementType;
        return this;
    }

    public void setMovementType(MovementType movementType) {
        this.movementType = movementType;
    }

    @JsonProperty
    public Integer getQuantity() {
        return this.quantity;
    }

    public WhMovement quantity(Integer quantity) {
        this.quantity = quantity;
        return this;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @JsonProperty
    public String getReconciliationSource() {
        return this.reconciliationSource;
    }

    public WhMovement reconciliationSource(String reconciliationSource) {
        this.reconciliationSource = reconciliationSource;
        return this;
    }

    public void setReconciliationSource(String reconciliationSource) {
        this.reconciliationSource = reconciliationSource;
    }

    @JsonProperty
    public String getReconciliationDestination() {
        return this.reconciliationDestination;
    }

    public WhMovement reconciliationDestination(String reconciliationDestination) {
        this.reconciliationDestination = reconciliationDestination;
        return this;
    }

    public void setReconciliationDestination(String reconciliationDestination) {
        this.reconciliationDestination = reconciliationDestination;
    }

    @JsonProperty
    public Boolean getConfirmedByOtherSide() {
        return this.confirmedByOtherSide;
    }

    public WhMovement confirmedByOtherSide(Boolean confirmedByOtherSide) {
        this.confirmedByOtherSide = confirmedByOtherSide;
        return this;
    }

    public void setConfirmedByOtherSide(Boolean confirmedByOtherSide) {
        this.confirmedByOtherSide = confirmedByOtherSide;
    }

    @JsonProperty
    public String getComment() {
        return this.comment;
    }

    public WhMovement comment(String comment) {
        this.comment = comment;
        return this;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    //    @Override
    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    //    @Override
    public User getCreatedBy() {
        return createdBy;
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
    public WhMovement user(User user) {
        this.setUser(user);
        return this;
    }

    @JsonProperty
    public User getLastUpdatedBy() {
        return this.lastUpdatedBy;
    }

    public WhMovement lastUpdatedBy(User user) {
        this.setLastUpdatedBy(user);
        return this;
    }

    public void setLastUpdatedBy(User user) {
        this.lastUpdatedBy = user;
    }

    @JsonProperty
    public WorkingDay getDay() {
        return this.day;
    }

    public WhMovement day(WorkingDay workingDay) {
        this.setDay(workingDay);
        return this;
    }

    public void setDay(WorkingDay workingDay) {
        this.day = workingDay;
    }

    @JsonProperty
    public Warehouse getInitiatedWh() {
        return this.initiatedWh;
    }

    public WhMovement initiatedWh(Warehouse warehouse) {
        this.setInitiatedWh(warehouse);
        return this;
    }

    public void setInitiatedWh(Warehouse warehouse) {
        this.initiatedWh = warehouse;
    }

    @JsonProperty
    public Warehouse getTheOtherSideWh() {
        return this.theOtherSideWh;
    }

    public WhMovement theOtherSideWh(Warehouse warehouse) {
        this.setTheOtherSideWh(warehouse);
        return this;
    }

    public void setTheOtherSideWh(Warehouse warehouse) {
        this.theOtherSideWh = warehouse;
    }

    @JsonProperty
    public Team getTeam() {
        return this.team;
    }

    public WhMovement team(Team team) {
        this.setTeam(team);
        return this;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WhMovement)) {
            return false;
        }
        return id != null && id.equals(((WhMovement) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WhMovement{" +
            "id=" + getId() +
            ", movementType='" + getMovementType() + "'" +
            ", quantity=" + getQuantity() +
            ", reconciliationSource='" + getReconciliationSource() + "'" +
            ", reconciliationDestination='" + getReconciliationDestination() + "'" +
            ", confirmedByOtherSide='" + getConfirmedByOtherSide() + "'" +
            ", comment='" + getComment() + "'" +
            "}";
    }
}
