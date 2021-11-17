package org.nmcpye.activitiesmanagement.extended.activity;

import org.nmcpye.activitiesmanagement.domain.activity.Activity;
import org.nmcpye.activitiesmanagement.domain.project.Project;

import java.time.LocalDate;

public class ActivityQueryParams {

    private String query;

    private Activity activity;

    private LocalDate startDate;

    private LocalDate endDate;

    private Boolean active;

    private Boolean isDisplayed;

    private Project project;
    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    public ActivityQueryParams() {
    }

    public ActivityQueryParams(Activity activity) {
        this.activity = activity;
    }

    // -------------------------------------------------------------------------
    // toString
    // -------------------------------------------------------------------------

    @Override
    public String toString() {
        return "ActivityQueryParams{" +
            "query='" + query + '\'' +
            ", activity=" + activity +
            ", startDate=" + startDate +
            ", endDate=" + endDate +
            ", active=" + active +
            ", isDisplayed=" + isDisplayed +
            ", project=" + project +
            '}';
    }

    // -------------------------------------------------------------------------
    // Builder
    // -------------------------------------------------------------------------

    public boolean hasActivity() {
        return activity != null;
    }

    public boolean hasProject() {
        return project != null;
    }

    // -------------------------------------------------------------------------
    // Getters and setters
    // -------------------------------------------------------------------------

    public ActivityQueryParams setQuery(String query) {
        this.query = query;
        return this;
    }

    public ActivityQueryParams setActivity(Activity activity) {
        this.activity = activity;
        return this;
    }

    public ActivityQueryParams setStartDate(LocalDate startDate) {
        this.startDate = startDate;
        return this;
    }

    public ActivityQueryParams setEndDate(LocalDate endDate) {
        this.endDate = endDate;
        return this;
    }

    public ActivityQueryParams setActive(Boolean active) {
        this.active = active;
        return this;
    }

    public ActivityQueryParams setDisplayed(Boolean displayed) {
        isDisplayed = displayed;
        return this;
    }

    public ActivityQueryParams setProject(Project project) {
        this.project = project;
        return this;
    }

    public String getQuery() {
        return query;
    }

    public Activity getActivity() {
        return activity;
    }

    public Project getProject() {
        return project;
    }

    public boolean isDisplayed() {
        return isDisplayed;
    }

    public boolean isActive() {
        return active;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }
}
