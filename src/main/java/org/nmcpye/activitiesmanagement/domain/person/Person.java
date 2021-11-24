package org.nmcpye.activitiesmanagement.domain.person;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.collections4.CollectionUtils;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.nmcpye.activitiesmanagement.domain.Authority;
import org.nmcpye.activitiesmanagement.domain.User;
import org.nmcpye.activitiesmanagement.domain.enumeration.Gender;
import org.nmcpye.activitiesmanagement.domain.organisationunit.OrganisationUnit;
import org.nmcpye.activitiesmanagement.extended.common.BaseIdentifiableObject;
import org.nmcpye.activitiesmanagement.extended.common.DxfNamespaces;
import org.nmcpye.activitiesmanagement.extended.common.IdentifiableObjectUtils;
import org.nmcpye.activitiesmanagement.extended.common.MetadataObject;
import org.nmcpye.activitiesmanagement.extended.schema.annotation.PropertyTransformer;
import org.nmcpye.activitiesmanagement.extended.schema.transformer.UserPropertyTransformer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.*;
import java.util.stream.Collectors;

/**
 * A Person.
 * @author Hamza Assada
 */
@Entity
@Table(name = "person")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@JsonRootName( value = "person", namespace = DxfNamespaces.DXF_2_0 )
public class Person extends BaseIdentifiableObject implements UserDetails, MetadataObject {

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

    @Column(name = "uuid", unique = true)
    private UUID uuid;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender;

    @Column(name = "mobile")
    private String mobile;

    @Column(name = "last_login")
    private Date lastLogin;

    @Column(name = "login")
    private String login;

    @Column(name = "self_registered")
    private Boolean selfRegistered;

    @Column(name = "disabled")
    private Boolean disabled;

    @OneToOne//(mappedBy = "person")
    @JoinColumn(name = "user_info_id", unique = true)
    private User userInfo;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(
        name = "person_membership",
        joinColumns = @JoinColumn(name = "person_id"),
        inverseJoinColumns = @JoinColumn(name = "organisation_unit_id")
    )
    @JsonIgnoreProperties(
        value = {
            "malariaReports",
            "dengueReports",
            "parent",
            "hfHomeSubVillage",
            "coveredByHf",
            "createdBy",
            "user",
            "lastUpdatedBy",
            "malariaUnit",
            "assignedChv",
            "children",
            "demographicData",
            "groups",
            "people",
            "dataViewPeople",
            "dataSets",
            "coordinates",
        },
        allowSetters = true
    )
    private Set<OrganisationUnit> organisationUnits = new HashSet<>();

    /**
     * TODO Later Delete the relation from orgUnit side
     */
    @ApiModelProperty(value = "TODO Later Delete the relation from orgUnit side")
    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(
        name = "person_data_view_orgunits",
        joinColumns = @JoinColumn(name = "person_id"),
        inverseJoinColumns = @JoinColumn(name = "organisation_unit_id")
    )
    @JsonIgnoreProperties(
        value = {
            "malariaReports",
            "dengueReports",
            "parent",
            "hfHomeSubVillage",
            "coveredByHf",
            "createdBy",
            "user",
            "lastUpdatedBy",
            "malariaUnit",
            "assignedChv",
            "children",
            "demographicData",
            "groups",
            "people",
            "dataViewPeople",
            "dataSets",
            "coordinates",
        },
        allowSetters = true
    )
    private Set<OrganisationUnit> dataViewOrganisationUnits = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(
        name = "person_role_members",
        joinColumns = @JoinColumn(name = "person_id"),
        inverseJoinColumns = @JoinColumn(name = "person_role_id")
    )
    @JsonIgnoreProperties(value = {"user", "createdBy", "lastUpdatedBy", "members"}, allowSetters = true)
    private Set<PersonAuthorityGroup> personAuthorityGroups = new HashSet<>();

    @ManyToMany(mappedBy = "members")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = {"user", "createdBy", "lastUpdatedBy", "members", "managedByGroups", "managedGroups"}, allowSetters = true)
    private Set<PeopleGroup> groups = new HashSet<>();

    /**
     * Cached all authorities {@link #getAllAuthorities()}.
     */
    @JsonIgnore
    @Transient
    private transient volatile Set<String> cachedAllAuthorities;

    /**
     * Cached state if person is super person {@link #isSuper()}.
     */
    @JsonIgnore
    @Transient
    private transient volatile Boolean cachedSuper;

    // -------------------------------------------------------------------------
    // Logic
    // -------------------------------------------------------------------------

    ///////////////////////////////////////////////////
    //
    // Person Credentials Methods
    //
    ///////////////////////////////////////////////////

    public Person() {
        this.lastLogin = null;
        this.setAutoFields(); // Needed to support person credentials uniqueness
    }

    @Override
    public void setAutoFields() {
        if (uuid == null) {
            uuid = UUID.randomUUID();
        }

        super.setAutoFields();
    }

    /**
     * Returns a concatenated String of the display names of all person authority
     * groups for this person credentials.
     */
    public String getPersonAuthorityGroupsName() {
        return IdentifiableObjectUtils.join(personAuthorityGroups);
    }

    /**
     * Returns a set of the aggregated authorities for all person authority groups
     * of this person credentials.
     */
    public Set<String> getAllAuthorities() {
        // cached all authorities can be reset to null by different thread and must be assigned before evaluation
        final Set<String> resultingAuthorities = cachedAllAuthorities;

        if (resultingAuthorities != null) {
            return resultingAuthorities;
        }

        Set<String> authorities = new HashSet<>();

        // PersonAuthorityGroup
//        for (PersonAuthorityGroup group : personAuthorityGroups) {
//            authorities.addAll(group.getAuthorities().stream().map(Authority::getName).collect(Collectors.toList()));
//        }
        for (PersonAuthorityGroup group : personAuthorityGroups) {
            authorities.addAll(group.getAuthorities());
        }

        if (userInfo != null) {
            authorities.removeAll(userInfo.getAuthorities().stream().map(Authority::getName).collect(Collectors.toSet()));

            authorities.addAll(userInfo.getAuthorities().stream().map(Authority::getName).collect(Collectors.toSet()));
        }

        authorities = Collections.unmodifiableSet(authorities);
        cachedAllAuthorities = authorities;

        return authorities;
    }

    /**
     * Indicates whether this person credentials has at least one authority through
     * its person authority groups.
     */
    public boolean hasAuthorities() {
        for (PersonAuthorityGroup group : personAuthorityGroups) {
            if (group != null && group.getAuthorities() != null && !group.getAuthorities().isEmpty()) {
                return true;
            }
        }

        if (userInfo != null && userInfo.getAuthorities() != null && !userInfo.getAuthorities().isEmpty()) {
            return true;
        }

        return false;
    }

    /**
     * Tests whether this person credentials has any of the authorities in the
     * given set.
     *
     * @param auths the authorities to compare with.
     * @return true or false.
     */
    public boolean hasAnyAuthority(Collection<String> auths) {
        Set<String> all = new HashSet<>(getAllAuthorities());
        return all.removeAll(auths);
    }

    /**
     * Tests whether the person has the given authority. Returns true in any case
     * if the person has the ALL authority.
     */
    public boolean isAuthorized(String auth) {
        if (auth == null) {
            return false;
        }

        final Set<String> auths = getAllAuthorities();

        return auths.contains(PersonAuthorityGroup.AUTHORITY_ALL) || auths.contains(auth);
    }

    /**
     * Indicates whether this person credentials is a super person, implying that the
     * ALL authority is present in at least one of the person authority groups of
     * this person credentials.
     */
    public boolean isSuper() {
        final Boolean superUser = cachedSuper;

        if (superUser != null) {
            return superUser;
        }

        final boolean resultingSuper = personAuthorityGroups.stream().anyMatch(PersonAuthorityGroup::isSuper);

        cachedSuper = resultingSuper;

        return resultingSuper;
    }

    /**
     * Indicates whether this person credentials can issue the given person authority
     * group. First the given authority group must not be null. Second this
     * person credentials must not contain the given authority group. Third
     * the authority group must be a subset of the aggregated person authorities
     * of this person credentials, or this person credentials must have the ALL
     * authority.
     *
     * @param group                          the person authority group.
     * @param canGrantOwnUserAuthorityGroups indicates whether this users can grant
     *                                       its own authority groups to others.
     */
    public boolean canIssueUserRole(PersonAuthorityGroup group, boolean canGrantOwnUserAuthorityGroups) {
        if (group == null) {
            return false;
        }

        final Set<String> authorities = getAllAuthorities();

        if (authorities.contains(PersonAuthorityGroup.AUTHORITY_ALL)) {
            return true;
        }

        if (!canGrantOwnUserAuthorityGroups && personAuthorityGroups.contains(group)) {
            return false;
        }

        return authorities.containsAll(group.getAuthorities());
    }

    /**
     * Indicates whether this person credentials can issue all of the person authority
     * groups in the given collection.
     *
     * @param groups                         the collection of person authority groups.
     * @param canGrantOwnUserAuthorityGroups indicates whether this users can grant
     *                                       its own authority groups to others.
     */
    public boolean canIssueUserRoles(Collection<PersonAuthorityGroup> groups, boolean canGrantOwnUserAuthorityGroups) {
        for (PersonAuthorityGroup group : groups) {
            if (!canIssueUserRole(group, canGrantOwnUserAuthorityGroups)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Indicates whether this person credentials can modify the given person
     * credentials. This person credentials must have the ALL authority or possess
     * all person authorities of the other person credentials to do so.
     *
     * @param other the person credentials to modify.
     */
    public boolean canModifyUser(Person other) {
        if (other == null) {
            return false;
        }

        final Set<String> authorities = getAllAuthorities();

        if (authorities.contains(PersonAuthorityGroup.AUTHORITY_ALL)) {
            return true;
        }

        return authorities.containsAll(other.getAllAuthorities());
    }

    /**
     * Return the name of this user credentials. More specifically, if this
     * credentials has a user it will return the first name and surname of that
     * user, if not it returns the login of this credentials.
     *
     * @return the name.
     */
    @Override
    public String getName() {
        return userInfo != null ? userInfo.getFirstName() : login;
    }

    /**
     * Sets the last login property to the current date.
     */
    public void updateLastLogin() {
        this.lastLogin = new Date();
    }

    /**
     * Indicates whether this person credentials has person authority groups.
     */
    public boolean hasUserAuthorityGroups() {
        return personAuthorityGroups != null && !personAuthorityGroups.isEmpty();
    }

    // -------------------------------------------------------------------------
    // hashCode and equals
    // -------------------------------------------------------------------------

    @Override
    public int hashCode() {
        return login.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null) {
            return false;
        }

        if (!(o instanceof Person)) {
            return false;
        }

        final Person other = (Person) o;

        // TODO check when a person is equal to another person
        if(login == null || other.login == null) {
            return super.equals(other);
        }

        return login.equals(other.getUsername());
    }

    // -------------------------------------------------------------------------
    // Getters and setters
    // -------------------------------------------------------------------------

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    @JsonProperty//("userRoles")
    @JsonSerialize(contentAs = BaseIdentifiableObject.class)
    public Set<PersonAuthorityGroup> getPersonAuthorityGroups() {
        return personAuthorityGroups;
    }

    public void setPersonAuthorityGroups(Set<PersonAuthorityGroup> personAuthorityGroups) {
        this.personAuthorityGroups = personAuthorityGroups;
        this.cachedSuper = null;
        this.cachedAllAuthorities = null;
    }

    @Override
    @JsonProperty
    public String getUsername() {
        return login;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    @JsonProperty
    public Date getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
    }

    @JsonProperty
    public boolean isSelfRegistered() {
        return selfRegistered;
    }

    public void setSelfRegistered(boolean selfRegistered) {
        this.selfRegistered = selfRegistered;
    }

    @JsonProperty
    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    @Override
    public String toString() {
        return (
            "{" +
                "\"login\":\"" +
                login +
                "\", " +
                "\"password\":\"" +
                getPassword() +
                "\", " +
                "\"lastLogin\":\"" +
                lastLogin +
                "\", " +
                "}"
        );
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();

        getAllAuthorities().forEach(authority -> grantedAuthorities.add(new SimpleGrantedAuthority(authority)));

        return grantedAuthorities;
    }

    @Override
    public String getPassword() {
        return userInfo != null ? userInfo.getPassword() : null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return !isDisabled();
    }

    ///////////////////////////////////////////////////
    //
    // Person Methods
    //
    ///////////////////////////////////////////////////

    public void addOrganisationUnit(OrganisationUnit unit) {
        organisationUnits.add(unit);
        unit.getPeople().add(this);
    }

    public void removeOrganisationUnit(OrganisationUnit unit) {
        organisationUnits.remove(unit);
        unit.getPeople().remove(this);
    }

    public void updateOrganisationUnits(Set<OrganisationUnit> updates) {
        for (OrganisationUnit unit : new HashSet<>(organisationUnits)) {
            if (!updates.contains(unit)) {
                removeOrganisationUnit(unit);
            }
        }

        for (OrganisationUnit unit : updates) {
            addOrganisationUnit(unit);
        }
    }

    public Person id(Long id) {
        this.id = id;
        return this;
    }

    public Person name(String name) {
        this.name = name;
        return this;
    }

    /**
     * Returns the first of the organisation units associated with the user.
     * Null is returned if the user has no organisation units. Which
     * organisation unit to return is undefined if the user has multiple
     * organisation units.
     */
    public OrganisationUnit getOrganisationUnit() {
        return CollectionUtils.isEmpty(organisationUnits) ? null : organisationUnits.iterator().next();
    }

    public boolean hasOrganisationUnit() {
        return !CollectionUtils.isEmpty(organisationUnits);
    }

    // -------------------------------------------------------------------------
    // Logic - data view organisation unit
    // -------------------------------------------------------------------------

    public boolean hasDataViewOrganisationUnit() {
        return !CollectionUtils.isEmpty(dataViewOrganisationUnits);
    }

    public OrganisationUnit getDataViewOrganisationUnit() {
        return CollectionUtils.isEmpty(dataViewOrganisationUnits) ? null : dataViewOrganisationUnits.iterator().next();
    }

    public boolean hasDataViewOrganisationUnitWithFallback() {
        return hasDataViewOrganisationUnit() || hasOrganisationUnit();
    }

    /**
     * Returns the first of the data view organisation units associated with the
     * user. If none, returns the first of the data capture organisation units.
     * If none, return nulls.
     */
    public OrganisationUnit getDataViewOrganisationUnitWithFallback() {
        return hasDataViewOrganisationUnit() ? getDataViewOrganisationUnit() : getOrganisationUnit();
    }

    /**
     * Returns the data view organisation units or organisation units if not exist.
     */
    public Set<OrganisationUnit> getDataViewOrganisationUnitsWithFallback() {
                return hasDataViewOrganisationUnit() ? dataViewOrganisationUnits : organisationUnits;
//        return hasDataViewOrganisationUnit() ? getDataViewOrganisationUnits() : getOrganisationUnits();
    }

    public String getOrganisationUnitsName() {
        return IdentifiableObjectUtils.join(organisationUnits);
    }

    public Set<PeopleGroup> getManagedGroups() {
        Set<PeopleGroup> managedGroups = new HashSet<>();

        for (PeopleGroup group : groups) {
            managedGroups.addAll(group.getManagedGroups());
        }

        return managedGroups;
    }

    public boolean hasManagedGroups() {
        for (PeopleGroup group : groups) {
            if (group != null && group.getManagedGroups() != null && !group.getManagedGroups().isEmpty()) {
                return true;
            }
        }

        return false;
    }

    /**
     * Indicates whether this user can manage the given user group.
     *
     * @param userGroup the user group to test.
     * @return true if the given user group can be managed by this user, false if not.
     */
    public boolean canManage(PeopleGroup userGroup) {
        return userGroup != null && CollectionUtils.containsAny(groups, userGroup.getManagedByGroups());
    }

    /**
     * Indicates whether this user can manage the given user.
     *
     * @param user the user to test.
     * @return true if the given user can be managed by this user, false if not.
     */
    public boolean canManage(Person user) {
        if (user == null || user.getGroups() == null) {
            return false;
        }

        for (PeopleGroup group : user.getGroups()) {
            if (canManage(group)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Indicates whether this user is managed by the given user group.
     *
     * @param userGroup the user group to test.
     * @return true if the given user group is managed by this user, false if not.
     */
    public boolean isManagedBy(PeopleGroup userGroup) {
        return userGroup != null && CollectionUtils.containsAny(groups, userGroup.getManagedGroups());
    }

    /**
     * Indicates whether this user is managed by the given user.
     *
     * @param user the user  to test.
     * @return true if the given user is managed by this user, false if not.
     */
    public boolean isManagedBy(Person user) {
        if (user == null || user.getGroups() == null) {
            return false;
        }

        for (PeopleGroup group : user.getGroups()) {
            if (isManagedBy(group)) {
                return true;
            }
        }

        return false;
    }

    public static String getSafeUsername(String username) {
        return StringUtils.isEmpty(username) ? "[Unknown]" : username;
    }

    @JsonProperty
    public Gender getGender() {
        return this.gender;
    }

    public Person gender(Gender gender) {
        this.gender = gender;
        return this;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    @JsonProperty
    public String getMobile() {
        return this.mobile;
    }

    public Person mobile(String mobile) {
        this.mobile = mobile;
        return this;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    @JsonProperty
    @JsonSerialize( as = BaseIdentifiableObject.class )
//    @JsonSerialize(contentUsing = UserPropertyTransformer.JacksonSerialize.class)
//    @JsonDeserialize(contentUsing = UserPropertyTransformer.JacksonDeserialize.class)
//    @PropertyTransformer(UserPropertyTransformer.class)
    public User getUserInfo() {
        return this.userInfo;
    }

    public Person userInfo(User user) {
        this.setUser(user);
        return this;
    }

    public void setUserInfo(User user) {
        this.userInfo = user;
    }

    @JsonProperty("peopleGroups")
    @JsonSerialize(contentAs = BaseIdentifiableObject.class)
    public Set<PeopleGroup> getGroups() {
        return groups;
    }

    public void setGroups(Set<PeopleGroup> groups) {
        this.groups = groups;
    }

    @JsonProperty
    @JsonSerialize(contentAs = BaseIdentifiableObject.class)
    public Set<OrganisationUnit> getOrganisationUnits() {
        return organisationUnits;
    }

    public Person setOrganisationUnits(Set<OrganisationUnit> organisationUnits) {
        this.organisationUnits = organisationUnits;
        return this;
    }

    @JsonProperty
    @JsonSerialize(contentAs = BaseIdentifiableObject.class)
    public Set<OrganisationUnit> getDataViewOrganisationUnits() {
        return dataViewOrganisationUnits;
    }

    public void setDataViewOrganisationUnits(Set<OrganisationUnit> dataViewOrganisationUnits) {
        this.dataViewOrganisationUnits = dataViewOrganisationUnits;
    }

    public static String username(Person person) {
        return username(person, "system-process");
    }

    public static String username(Person person, String defaultValue) {
        return person != null ? person.getUsername() : defaultValue;
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
