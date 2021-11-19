package org.nmcpye.activitiesmanagement.domain.project;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.nmcpye.activitiesmanagement.domain.User;
import org.nmcpye.activitiesmanagement.domain.activity.Activity;
import org.nmcpye.activitiesmanagement.extended.common.BaseIdentifiableObject;
import org.nmcpye.activitiesmanagement.extended.common.DxfNamespaces;
import org.nmcpye.activitiesmanagement.extended.common.MetadataObject;

/**
 * A Project.
 */

@Entity
@Table(name = "project")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@JsonRootName( value = "project", namespace = DxfNamespaces.DXF_2_0 )
public class Project extends BaseIdentifiableObject implements MetadataObject {

    @Column(name = "displayed")
    private Boolean displayed;

    @OneToMany(mappedBy = "project")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "warehouses", "user", "createdBy", "lastUpdatedBy", "project" }, allowSetters = true)
    private Set<Activity> activities = new HashSet<>();

    public Project id(Long id) {
        this.id = id;
        return this;
    }

    public Project uid(String uid) {
        this.uid = uid;
        return this;
    }

    public Project code(String code) {
        this.code = code;
        return this;
    }

    public Project name(String name) {
        this.name = name;
        return this;
    }

    public Project created(Date created) {
        this.created = created;
        return this;
    }

    public Project lastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
        return this;
    }

    @JsonProperty
    public Boolean getDisplayed() {
        return displayed;
    }

    public Project displayed(Boolean isDisplayed) {
        this.displayed = isDisplayed;
        return this;
    }

    public void setDisplayed(Boolean displayed) {
        this.displayed = displayed;
    }

    @JsonProperty
    @JsonSerialize(contentAs = BaseIdentifiableObject.class)
    public Set<Activity> getActivities() {
        return this.activities;
    }

    public Project activities(Set<Activity> activities) {
        this.setActivities(activities);
        return this;
    }

    public Project addActivity(Activity activity) {
        this.activities.add(activity);
        activity.setProject(this);
        return this;
    }

    public Project removeActivity(Activity activity) {
        this.activities.remove(activity);
        activity.setProject(null);
        return this;
    }

    public void setActivities(Set<Activity> activities) {
        if (this.activities != null) {
            this.activities.forEach(i -> i.setProject(null));
        }
        if (activities != null) {
            activities.forEach(i -> i.setProject(this));
        }
        this.activities = activities;
    }

    public Project user(User user) {
        this.setUser(user);
        return this;
    }

    public Project lastUpdatedBy(User user) {
        this.setLastUpdatedBy(user);
        return this;
    }
}
