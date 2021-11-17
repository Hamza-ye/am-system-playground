package org.nmcpye.activitiesmanagement.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.nmcpye.activitiesmanagement.config.Constants;
import org.nmcpye.activitiesmanagement.domain.enumeration.Gender;
import org.nmcpye.activitiesmanagement.domain.organisationunit.OrganisationUnit;
import org.nmcpye.activitiesmanagement.domain.person.PeopleGroup;
import org.nmcpye.activitiesmanagement.domain.person.Person;
import org.nmcpye.activitiesmanagement.extended.common.BaseIdentifiableObject;
import org.nmcpye.activitiesmanagement.extended.common.IdentifiableObjectUtils;
import org.nmcpye.activitiesmanagement.extended.common.MetadataObject;
import org.nmcpye.activitiesmanagement.extended.schema.PropertyType;
import org.nmcpye.activitiesmanagement.extended.schema.annotation.Property;
import org.nmcpye.activitiesmanagement.extended.schema.annotation.PropertyRange;
import org.nmcpye.activitiesmanagement.extended.security.Authorities;
import org.nmcpye.activitiesmanagement.security.AuthoritiesConstants;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

/**
 * A user.
 */
@Entity
@Table(name = "app_user")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class User extends BaseIdentifiableObject implements MetadataObject {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Pattern(regexp = Constants.LOGIN_REGEX)
    @Size(min = 1, max = 50)
    @Column(length = 50, unique = true, nullable = false)
    private String login;

    @JsonIgnore
    @NotNull
    @Size(max = 60)
    @Column(name = "password_hash", length = 60, nullable = false)
    private String password;

    @Size(max = 50)
    @Column(name = "first_name", length = 50)
    private String firstName;

    @Size(max = 50)
    @Column(name = "last_name", length = 50)
    private String lastName;

    @Email
    @Size(min = 5, max = 254)
    @Column(length = 254, unique = true)
    private String email;

    @NotNull
    @Column(nullable = false)
    private boolean activated = false;

    @Size(min = 2, max = 10)
    @Column(name = "lang_key", length = 10)
    private String langKey;

    @Size(max = 256)
    @Column(name = "image_url", length = 256)
    private String imageUrl;

    @Size(max = 20)
    @Column(name = "activation_key", length = 20)
    @JsonIgnore
    private String activationKey;

    @Size(max = 20)
    @Column(name = "reset_key", length = 20)
    @JsonIgnore
    private String resetKey;

    @Column(name = "reset_date")
    private Instant resetDate = null;

    @JsonIgnore
    @ManyToMany
    @JoinTable(
        name = "app_user_authority",
        joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
        inverseJoinColumns = {@JoinColumn(name = "authority_name", referencedColumnName = "name")}
    )
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @BatchSize(size = 20)
    private Set<Authority> authorities = new HashSet<>();

    ///////////////////////////////////////////////////
    //
    // Extended Methods
    //
    ///////////////////////////////////////////////////

    /**
     * Required.
     */
    private String surname;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender;

    @Column(name = "mobile")
    private String mobile;

    @OneToOne
    @JoinColumn(unique = true)
    Person person;

    /**
     * Returns the concatenated first name and surname.
     */
    @JsonProperty
    @Override
    public String getName() {
        return firstName + " " + surname;
    }

    @JsonProperty
    @PropertyRange(min = 2)
    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    @JsonProperty
    public Gender getGender() {
        return this.gender;
    }

    public User gender(Gender gender) {
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

    public User mobile(String mobile) {
        this.mobile = mobile;
        return this;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    @JsonProperty
    @JsonSerialize(contentAs = BaseIdentifiableObject.class)
    @Property(required = Property.Value.TRUE, value = PropertyType.REFERENCE)
    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }

    @JsonProperty
    public String getLogin() {
        return login;
    }

    // Lowercase the login before saving it in database
    public void setLogin(String login) {
        this.login = StringUtils.lowerCase(login, Locale.ENGLISH);
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @JsonProperty
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @JsonProperty
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @JsonProperty
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @JsonProperty
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @JsonProperty
    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    public String getActivationKey() {
        return activationKey;
    }

    public void setActivationKey(String activationKey) {
        this.activationKey = activationKey;
    }

    public String getResetKey() {
        return resetKey;
    }

    public void setResetKey(String resetKey) {
        this.resetKey = resetKey;
    }

    public Instant getResetDate() {
        return resetDate;
    }

    public void setResetDate(Instant resetDate) {
        this.resetDate = resetDate;
    }

    @JsonProperty
    public String getLangKey() {
        return langKey;
    }

    public void setLangKey(String langKey) {
        this.langKey = langKey;
    }

    @JsonProperty
    public Set<Authority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<Authority> authorities) {
        this.authorities = authorities;
    }

    //////////////////////////////
    //
    // Person Data
    //
    //////////////////////////////

    public Set<OrganisationUnit> getOrganisationUnits() {
        return hasOrganisationUnit() ? null : person.getOrganisationUnits();
    }

    public Set<OrganisationUnit> getDataViewOrganisationUnits() {
        return hasDataViewOrganisationUnit() ? null : person.getDataViewOrganisationUnits();
    }
    /**
     * Returns the first of the organisation units associated with the user.
     * Null is returned if the user has no organisation units. Which
     * organisation unit to return is undefined if the user has multiple
     * organisation units.
     */
    public OrganisationUnit getOrganisationUnit() {
        return !hasOrganisationUnit() ? null : person.getOrganisationUnits().iterator().next();
    }

    public boolean hasOrganisationUnit() {
        return !CollectionUtils.isEmpty(person != null ? person.getOrganisationUnits() : null);
    }

    // -------------------------------------------------------------------------
    // Logic - data view organisation unit
    // -------------------------------------------------------------------------

    public boolean hasDataViewOrganisationUnit() {
        return !CollectionUtils.isEmpty(person != null ? person.getDataViewOrganisationUnits() : null);
    }

    public OrganisationUnit getDataViewOrganisationUnit() {
        return !hasDataViewOrganisationUnit() ? null
            : person.getDataViewOrganisationUnits().iterator().next();
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
     * Returns the data view organisation units or organisation units if not
     * exist.
     */
    public Set<OrganisationUnit> getDataViewOrganisationUnitsWithFallback() {
        return hasDataViewOrganisationUnit() ? person.getDataViewOrganisationUnits() : person.getOrganisationUnits();
    }

    public String getOrganisationUnitsName() {
        return IdentifiableObjectUtils.join(person != null ? person.getOrganisationUnits() : null);
    }

    public Set<PeopleGroup> getManagedGroups() {
        Set<PeopleGroup> managedGroups = new HashSet<>();

        if (person != null) {
            for (PeopleGroup group : person.getGroups()) {
                managedGroups.addAll(group.getManagedGroups());
            }
        }
        return managedGroups;
    }

    public boolean hasManagedGroups() {
        if (person != null) {
            for (PeopleGroup group : person.getGroups()) {
                if (group != null && group.getManagedGroups() != null && !group.getManagedGroups().isEmpty()) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Indicates whether this user can manage the given user group.
     *
     * @param userGroup the user group to test.
     * @return true if the given user group can be managed by this user, false
     * if not.
     */
    public boolean canManage(PeopleGroup userGroup) {
        return userGroup != null && person != null
            && CollectionUtils.containsAny(person.getGroups(), userGroup.getManagedByGroups());
    }

    /**
     * Indicates whether this user can manage the given user.
     *
     * @param user the user to test.
     * @return true if the given user can be managed by this user, false if not.
     */
    public boolean canManage(User user) {
        if (user == null || user.getPerson() == null || user.getPerson().getGroups() == null) {
            return false;
        }

        for (PeopleGroup group : user.getPerson().getGroups()) {
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
     * @return true if the given user group is managed by this user, false if
     * not.
     */
    public boolean isManagedBy(PeopleGroup userGroup) {
        return userGroup != null && person != null && CollectionUtils.containsAny(person.getGroups(), userGroup.getManagedGroups());
    }

    /**
     * Indicates whether this user is managed by the given user.
     *
     * @param user the user to test.
     * @return true if the given user is managed by this user, false if not.
     */
    public boolean isManagedBy(User user) {
        if (user == null || user.getPerson() == null || user.getPerson().getGroups() == null) {
            return false;
        }

        for (PeopleGroup group : user.getPerson().getGroups()) {
            if (isManagedBy(group)) {
                return true;
            }
        }

        return false;
    }

    public Boolean isSuper() {
        return authorities.stream()
            .anyMatch(authority -> authority.getName().equals(AuthoritiesConstants.ADMIN))
            || (person != null && person.isSuper());
    }

    /**
     * Tests whether the user has the given authority. Returns true in any case
     * if the user has the ALL authority.
     *
     * @param auth the authority.
     */
    public boolean isAuthorized(String auth) {
        return person != null && person.isAuthorized(auth);
    }

    /**
     * Tests whether the user has the given authority. Returns true in any case
     * if the user has the ALL authority.
     *
     * @param auth the {@link Authorities}.
     */
    public boolean isAuthorized(Authorities auth) {
        return isAuthorized(auth.getAuthority());
    }
}
