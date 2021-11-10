package org.nmcpye.activitiesmanagement.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Activity.
 */
@Entity
@Table(name = "activity")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Activity implements Serializable {

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
    private Boolean isDisplayed;

    @OneToMany(mappedBy = "activity")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = { "initiatedMovements", "notInitiatedMovements", "user", "createdBy", "lastUpdatedBy", "activity", "teams" },
        allowSetters = true
    )
    private Set<Warehouse> warehouses = new HashSet<>();

    @ManyToOne
    private User user;

    @ManyToOne
    private User lastUpdatedBy;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "activities", "user", "createdBy", "lastUpdatedBy" }, allowSetters = true)
    private Project project;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Activity id(Long id) {
        this.id = id;
        return this;
    }

    public String getUid() {
        return this.uid;
    }

    public Activity uid(String uid) {
        this.uid = uid;
        return this;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getCode() {
        return this.code;
    }

    public Activity code(String code) {
        this.code = code;
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return this.name;
    }

    public Activity name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Instant getCreated() {
        return this.created;
    }

    public Activity created(Instant created) {
        this.created = created;
        return this;
    }

    public void setCreated(Instant created) {
        this.created = created;
    }

    public Instant getLastUpdated() {
        return this.lastUpdated;
    }

    public Activity lastUpdated(Instant lastUpdated) {
        this.lastUpdated = lastUpdated;
        return this;
    }

    public void setLastUpdated(Instant lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

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

    public Boolean getIsDisplayed() {
        return this.isDisplayed;
    }

    public Activity isDisplayed(Boolean isDisplayed) {
        this.isDisplayed = isDisplayed;
        return this;
    }

    public void setIsDisplayed(Boolean isDisplayed) {
        this.isDisplayed = isDisplayed;
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

    public User getUser() {
        return this.user;
    }

    public Activity user(User user) {
        this.setUser(user);
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getLastUpdatedBy() {
        return this.lastUpdatedBy;
    }

    public Activity lastUpdatedBy(User user) {
        this.setLastUpdatedBy(user);
        return this;
    }

    public void setLastUpdatedBy(User user) {
        this.lastUpdatedBy = user;
    }

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

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Activity)) {
            return false;
        }
        return id != null && id.equals(((Activity) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Activity{" +
            "id=" + getId() +
            ", uid='" + getUid() + "'" +
            ", code='" + getCode() + "'" +
            ", name='" + getName() + "'" +
            ", created='" + getCreated() + "'" +
            ", lastUpdated='" + getLastUpdated() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", noOfDays=" + getNoOfDays() +
            ", active='" + getActive() + "'" +
            ", isDisplayed='" + getIsDisplayed() + "'" +
            "}";
    }
}
