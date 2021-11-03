package org.nmcpye.activitiesmanagement.extended.user;

import com.google.common.base.MoreObjects;
import java.util.*;
import org.nmcpye.activitiesmanagement.domain.User;
import org.nmcpye.activitiesmanagement.domain.organisationunit.OrganisationUnit;
import org.nmcpye.activitiesmanagement.domain.person.PeopleGroup;

public class UserQueryParams {

    private String query;

    private String mobile;

    private User user;

    private boolean canManage;

    private boolean authSubset;

    private boolean disjointRoles;

    private Date lastLogin;

    private boolean selfRegistered;

    private List<OrganisationUnit> organisationUnits = new ArrayList<>();

    private Set<PeopleGroup> userGroups = new HashSet<>();

    private Integer first;

    private Integer max;

    private boolean userOrgUnits;

    private boolean includeOrgUnitChildren;

    private boolean prefetchUserGroups;

    private Boolean disabled;

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    public UserQueryParams() {}

    public UserQueryParams(User user) {
        this.user = user;
    }

    // -------------------------------------------------------------------------
    // toString
    // -------------------------------------------------------------------------

    @Override
    public String toString() {
        return MoreObjects
            .toStringHelper(this)
            .add("query", query)
            .add("phone number", mobile)
            .add("user", user != null ? user.getLogin() : null)
            .add("can manage", canManage)
            .add("auth subset", authSubset)
            .add("disjoint roles", disjointRoles)
            .add("last login", lastLogin)
            .add("self registered", selfRegistered)
            .add("first", first)
            .add("max", max)
            .add("includeOrgUnitChildren", includeOrgUnitChildren)
            .add("prefetchUserGroups", prefetchUserGroups)
            .add("disabled", disabled)
            .toString();
    }

    // -------------------------------------------------------------------------
    // Builder
    // -------------------------------------------------------------------------

    public UserQueryParams addOrganisationUnit(OrganisationUnit unit) {
        this.organisationUnits.add(unit);
        return this;
    }

    public boolean hasOrganisationUnits() {
        return !organisationUnits.isEmpty();
    }

    public boolean hasUserGroups() {
        return !userGroups.isEmpty();
    }

    public boolean hasUser() {
        return user != null;
    }

    // -------------------------------------------------------------------------
    // Getters and setters
    // -------------------------------------------------------------------------

    public UserQueryParams setQuery(String query) {
        this.query = query;
        return this;
    }

    public UserQueryParams setMobile(String phoneNumber) {
        this.mobile = phoneNumber;
        return this;
    }

    public UserQueryParams setUser(User user) {
        this.user = user;
        return this;
    }

    public UserQueryParams setCanManage(boolean canManage) {
        this.canManage = canManage;
        return this;
    }

    public UserQueryParams setAuthSubset(boolean authSubset) {
        this.authSubset = authSubset;
        return this;
    }

    public UserQueryParams setDisjointRoles(boolean disjointRoles) {
        this.disjointRoles = disjointRoles;
        return this;
    }

    public UserQueryParams setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
        return this;
    }

    public UserQueryParams setSelfRegistered(boolean selfRegistered) {
        this.selfRegistered = selfRegistered;
        return this;
    }

    public UserQueryParams setOrganisationUnits(List<OrganisationUnit> organisationUnits) {
        this.organisationUnits = organisationUnits;
        return this;
    }

    public UserQueryParams setUserGroups(Set<PeopleGroup> userGroups) {
        this.userGroups = userGroups;
        return this;
    }

    public UserQueryParams setFirst(Integer first) {
        this.first = first;
        return this;
    }

    public UserQueryParams setMax(Integer max) {
        this.max = max;
        return this;
    }

    public UserQueryParams setUserOrgUnits(boolean userOrgUnits) {
        this.userOrgUnits = userOrgUnits;
        return this;
    }

    public UserQueryParams setIncludeOrgUnitChildren(boolean includeOrgUnitChildren) {
        this.includeOrgUnitChildren = includeOrgUnitChildren;
        return this;
    }

    public UserQueryParams setPrefetchUserGroups(boolean prefetchUserGroups) {
        this.prefetchUserGroups = prefetchUserGroups;
        return this;
    }

    public UserQueryParams setDisabled(Boolean disabled) {
        this.disabled = disabled;
        return this;
    }

    public String getQuery() {
        return query;
    }

    public String getMobile() {
        return mobile;
    }

    public User getUser() {
        return user;
    }

    public boolean isCanManage() {
        return canManage;
    }

    public boolean isAuthSubset() {
        return authSubset;
    }

    public boolean isDisjointRoles() {
        return disjointRoles;
    }

    public Date getLastLogin() {
        return lastLogin;
    }

    public boolean isSelfRegistered() {
        return selfRegistered;
    }

    public List<OrganisationUnit> getOrganisationUnits() {
        return organisationUnits;
    }

    public Set<PeopleGroup> getUserGroups() {
        return userGroups;
    }

    public Integer getFirst() {
        return first;
    }

    public Integer getMax() {
        return max;
    }

    public boolean isUserOrgUnits() {
        return userOrgUnits;
    }

    public boolean isIncludeOrgUnitChildren() {
        return includeOrgUnitChildren;
    }

    public boolean isPrefetchUserGroups() {
        return prefetchUserGroups;
    }

    public Boolean getDisabled() {
        return disabled;
    }
}
