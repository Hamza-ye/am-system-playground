package org.nmcpye.activitiesmanagement.extended.person;

import com.google.common.base.MoreObjects;
import org.nmcpye.activitiesmanagement.domain.User;
import org.nmcpye.activitiesmanagement.domain.organisationunit.OrganisationUnit;
import org.nmcpye.activitiesmanagement.domain.person.PeopleGroup;
import org.nmcpye.activitiesmanagement.domain.person.Person;

import java.util.*;

public class PersonQueryParams {

    private String query;

    private String mobile;

    private Person person;

    private User user;

    private boolean canManage;

    private boolean authSubset;

    private boolean disjointRoles;

    private Date lastLogin;

    private boolean selfRegistered;

    private List<OrganisationUnit> organisationUnits = new ArrayList<>();

    private Set<PeopleGroup> peopleGroups = new HashSet<>();

    private Integer first;

    private Integer max;

    private boolean userOrgUnits;

    private boolean includeOrgUnitChildren;

    private boolean prefetchUserGroups;

    private Boolean disabled;

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    public PersonQueryParams() {}

    public PersonQueryParams(User user) {
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
            .add("person", person != null ? person.getName() : null)
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

    public PersonQueryParams addOrganisationUnit(OrganisationUnit unit) {
        this.organisationUnits.add(unit);
        return this;
    }

    public boolean hasOrganisationUnits() {
        return !organisationUnits.isEmpty();
    }

    public boolean hasUserGroups() {
        return !peopleGroups.isEmpty();
    }

    public boolean hasPerson() {
        return person != null;
    }

    public boolean hasUser() {
        return user != null;
    }

    // -------------------------------------------------------------------------
    // Getters and setters
    // -------------------------------------------------------------------------

    public PersonQueryParams setQuery(String query) {
        this.query = query;
        return this;
    }

    public PersonQueryParams setMobile(String mobile) {
        this.mobile = mobile;
        return this;
    }

    public PersonQueryParams setPerson(Person person) {
        this.person = person;
        return this;
    }

    public PersonQueryParams setUser(User user) {
        this.user = user;
        return this;
    }

    public PersonQueryParams setCanManage(boolean canManage) {
        this.canManage = canManage;
        return this;
    }

    public PersonQueryParams setAuthSubset(boolean authSubset) {
        this.authSubset = authSubset;
        return this;
    }

    public PersonQueryParams setDisjointRoles(boolean disjointRoles) {
        this.disjointRoles = disjointRoles;
        return this;
    }

    public PersonQueryParams setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
        return this;
    }

    public PersonQueryParams setSelfRegistered(boolean selfRegistered) {
        this.selfRegistered = selfRegistered;
        return this;
    }

    public PersonQueryParams setOrganisationUnits(List<OrganisationUnit> organisationUnits) {
        this.organisationUnits = organisationUnits;
        return this;
    }

    public PersonQueryParams setPeopleGroups(Set<PeopleGroup> peopleGroups) {
        this.peopleGroups = peopleGroups;
        return this;
    }

    public PersonQueryParams setFirst(Integer first) {
        this.first = first;
        return this;
    }

    public PersonQueryParams setMax(Integer max) {
        this.max = max;
        return this;
    }

    public PersonQueryParams setUserOrgUnits(boolean userOrgUnits) {
        this.userOrgUnits = userOrgUnits;
        return this;
    }

    public PersonQueryParams setIncludeOrgUnitChildren(boolean includeOrgUnitChildren) {
        this.includeOrgUnitChildren = includeOrgUnitChildren;
        return this;
    }

    public PersonQueryParams setPrefetchPersonGroups(boolean prefetchUserGroups) {
        this.prefetchUserGroups = prefetchUserGroups;
        return this;
    }

    public PersonQueryParams setDisabled(Boolean disabled) {
        this.disabled = disabled;
        return this;
    }

    public String getQuery() {
        return query;
    }

    public String getMobile() {
        return mobile;
    }

    public Person getPerson() {
        return person;
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

    public Set<PeopleGroup> getPeopleGroups() {
        return peopleGroups;
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
