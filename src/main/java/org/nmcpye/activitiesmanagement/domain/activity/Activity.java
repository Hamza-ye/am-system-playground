package org.nmcpye.activitiesmanagement.domain.activity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.nmcpye.activitiesmanagement.domain.User;
import org.nmcpye.activitiesmanagement.domain.Warehouse;
import org.nmcpye.activitiesmanagement.domain.ContentPage;
import org.nmcpye.activitiesmanagement.domain.project.Project;
import org.nmcpye.activitiesmanagement.extended.common.BaseIdentifiableObject;
import org.nmcpye.activitiesmanagement.extended.common.DxfNamespaces;
import org.nmcpye.activitiesmanagement.extended.common.MetadataObject;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * A Activity.
 */
@Entity
@Table(name = "activity")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@JsonRootName(value = "activity", namespace = DxfNamespaces.DXF_2_0)
public class Activity extends BaseIdentifiableObject implements MetadataObject {

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

    @Column(name = "name")
    private String name;

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

    @NotNull
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @NotNull
    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "active")
    private Boolean active;

    @Column(name = "displayed")
    private Boolean displayed;

    @OneToMany(mappedBy = "activity")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = {"initiatedMovements", "notInitiatedMovements", "user", "createdBy", "lastUpdatedBy", "activity", "teams"},
        allowSetters = true
    )
    private Set<Warehouse> warehouses = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = {"activities", "user", "createdBy", "lastUpdatedBy"}, allowSetters = true)
    private Project project;

    @JsonIgnoreProperties(value = { "imagesAlbum" }, allowSetters = true)
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(unique = true)
    private ContentPage contentPage;

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

    @JsonProperty
    @JsonSerialize(contentAs = BaseIdentifiableObject.class)
    public ContentPage getContentPage() {
        return contentPage;
    }

    public void setContentPage(ContentPage contentPage) {
        this.contentPage = contentPage;
    }

    @JsonProperty
    public Boolean hasContentPage() {
        return contentPage != null;
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
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
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
