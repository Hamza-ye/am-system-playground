package org.nmcpye.activitiesmanagement.domain.activity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.nmcpye.activitiesmanagement.domain.project.Project;
import org.nmcpye.activitiesmanagement.domain.User;
import org.nmcpye.activitiesmanagement.domain.Warehouse;
import org.nmcpye.activitiesmanagement.extended.common.BaseIdentifiableObject;
import org.nmcpye.activitiesmanagement.extended.common.MetadataObject;

/**
 * A Activity.
 */
@Entity
@Table(name = "activity")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Activity extends BaseIdentifiableObject implements MetadataObject {

//    @Id
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
//    @SequenceGenerator(name = "sequenceGenerator")
//    private Long id;
//
//    @NotNull
//    @Size(max = 11)
//    @Column(name = "uid", length = 11, nullable = false, unique = true)
//    private String uid;
//
//    @Column(name = "code", unique = true)
//    private String code;
//
//    @Column(name = "name")
//    private String name;
//
//    @Column(name = "created")
//    private Instant created;
//
//    @Column(name = "last_updated")
//    private Instant lastUpdated;

    @NotNull
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @NotNull
    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Min(value = 0)
    @Column(name = "no_of_days")
    private Integer noOfDays;

    @Column(name = "active")
    private Boolean active;

    @Column(name = "is_displayed")
    private Boolean displayed;

    @OneToMany(mappedBy = "activity")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = { "initiatedMovements", "notInitiatedMovements", "user", "createdBy", "lastUpdatedBy", "activity", "teams" },
        allowSetters = true
    )
    private Set<Warehouse> warehouses = new HashSet<>();

//    @ManyToOne
//    private User user;
//
//    @ManyToOne
//    private User lastUpdatedBy;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "activities", "user", "createdBy", "lastUpdatedBy" }, allowSetters = true)
    private Project project;

    public Activity id(Long id) {
        this.id = id;
        return this;
    }

    public Activity uid(String uid) {
        this.uid = uid;
        return this;
    }

    public Activity code(String code) {
        this.code = code;
        return this;
    }

    public Activity name(String name) {
        this.name = name;
        return this;
    }

    public Activity created(Date created) {
        this.created = created;
        return this;
    }

    public Activity lastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
        return this;
    }

    @JsonProperty
    public LocalDate getStartDate() {
        return this.startDate;
    }

    public Activity startDate(LocalDate startDate) {
        this.startDate = startDate;
        return this;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    @JsonProperty
    public LocalDate getEndDate() {
        return this.endDate;
    }

    public Activity endDate(LocalDate endDate) {
        this.endDate = endDate;
        return this;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    @JsonProperty
    public Integer getNoOfDays() {
        return this.noOfDays;
    }

    public Activity noOfDays(Integer noOfDays) {
        this.noOfDays = noOfDays;
        return this;
    }

    public void setNoOfDays(Integer noOfDays) {
        this.noOfDays = noOfDays;
    }

    @JsonProperty
    public Boolean getActive() {
        return this.active;
    }

    public Activity active(Boolean active) {
        this.active = active;
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    @JsonProperty
    public Boolean getDisplayed() {
        return this.displayed;
    }

    public Activity displayed(Boolean isDisplayed) {
        this.displayed = isDisplayed;
        return this;
    }

    public void setDisplayed(Boolean displayed) {
        this.displayed = displayed;
    }

    public Set<Warehouse> getWarehouses() {
        return this.warehouses;
    }

    public Activity warehouses(Set<Warehouse> warehouses) {
        this.setWarehouses(warehouses);
        return this;
    }

    public Activity addWarehouse(Warehouse warehouse) {
        this.warehouses.add(warehouse);
        warehouse.setActivity(this);
        return this;
    }

    public Activity removeWarehouse(Warehouse warehouse) {
        this.warehouses.remove(warehouse);
        warehouse.setActivity(null);
        return this;
    }

    public void setWarehouses(Set<Warehouse> warehouses) {
        if (this.warehouses != null) {
            this.warehouses.forEach(i -> i.setActivity(null));
        }
        if (warehouses != null) {
            warehouses.forEach(i -> i.setActivity(this));
        }
        this.warehouses = warehouses;
    }

    public Activity user(User user) {
        this.setUser(user);
        return this;
    }

    public Activity lastUpdatedBy(User user) {
        this.setLastUpdatedBy(user);
        return this;
    }

    @JsonProperty
    @JsonSerialize(contentAs = BaseIdentifiableObject.class)
    public Project getProject() {
        return this.project;
    }

    public Activity project(Project project) {
        this.setProject(project);
        return this;
    }

    public void setProject(Project project) {
        this.project = project;
    }
}
