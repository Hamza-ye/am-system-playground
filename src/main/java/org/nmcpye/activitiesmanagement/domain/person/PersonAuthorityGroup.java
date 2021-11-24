package org.nmcpye.activitiesmanagement.domain.person;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.collect.Sets;
import org.apache.commons.collections4.CollectionUtils;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.nmcpye.activitiesmanagement.domain.Authority;
import org.nmcpye.activitiesmanagement.domain.User;
import org.nmcpye.activitiesmanagement.extended.common.BaseIdentifiableObject;
import org.nmcpye.activitiesmanagement.extended.common.DxfNamespaces;
import org.nmcpye.activitiesmanagement.extended.common.MetadataObject;
import org.nmcpye.activitiesmanagement.extended.schema.annotation.PropertyRange;
import org.nmcpye.activitiesmanagement.security.AuthoritiesConstants;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.*;
import java.util.stream.Collectors;

/**
 * A PersonAuthorityGroup.
 * @author Hamza Assada
 */
@Entity
@Table(name = "person_authority_group")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@JsonRootName( value = "personAuthorityGroup", namespace = DxfNamespaces.DXF_2_0 )
public class PersonAuthorityGroup extends BaseIdentifiableObject implements MetadataObject {

    public static final String AUTHORITY_ALL = AuthoritiesConstants.ADMIN;

    private static final String[] CRITICAL_AUTHS = {
        AuthoritiesConstants.ADMIN,
        "F_SCHEDULING_ADMIN",
        "F_SYSTEM_SETTING",
        "F_SQLVIEW_PUBLIC_ADD",
        "F_SQLVIEW_PRIVATE_ADD",
        "F_SQLVIEW_DELETE",
        "F_USERROLE_PUBLIC_ADD",
        "F_USERROLE_PRIVATE_ADD",
        "F_USERROLE_DELETE",
    };

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
     * Last user updated this object.
     */
    @ManyToOne
    @JoinColumn(name = "last_updated_by")
    protected User lastUpdatedBy;

    /**
     * Owner of this object.
     */
    @ManyToOne
    @JoinColumn(name = "created_by")
    protected User createdBy;

    ////////////////////////
    ///
    ///
    ////////////////////////

    private String description;

    @ManyToMany(mappedBy = "personAuthorityGroups")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = {
            "userInfo", "user", "createdBy", "lastUpdatedBy", "organisationUnits", "dataViewOrganisationUnits", "personAuthorityGroups", "groups",
        },
        allowSetters = true
    )
    private Set<Person> members = new HashSet<>();

    //    fk_userroleauthorities_userroleid
//    @OneToMany(targetEntity = Authority.class, cascade = CascadeType.ALL)//(mappedBy = "personAuthorityGroup")
//    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
//    @JoinTable(
//        name = "person_authority_group_authorities",
//        joinColumns = @JoinColumn(name = "authority_group_id"),
//        inverseJoinColumns = @JoinColumn(name = "authority")
//    )
//    @JsonIgnoreProperties(value = { "personAuthorityGroup" }, allowSetters = true)
//    private Set<Authority> authorities = new HashSet<>();

    @ElementCollection
    private Set<String> authorities;
    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    public PersonAuthorityGroup() {}

    // -------------------------------------------------------------------------
    // Logic
    // -------------------------------------------------------------------------

    public void addPersonCredentials(Person person) {
        members.add(person);
        person.getPersonAuthorityGroups().add(this);
    }

    public void removePersonCredentials(Person person) {
        members.remove(person);
        person.getPersonAuthorityGroups().remove(this);
    }

    public boolean isSuper() {
        return authorities != null && authorities.contains(AUTHORITY_ALL);
    }

    public boolean hasCriticalAuthorities() {
        return authorities != null && CollectionUtils.containsAny(authorities, Sets.newHashSet(CRITICAL_AUTHS));
    }

    // -------------------------------------------------------------------------
    // Getters and setters
    // -------------------------------------------------------------------------

    @JsonProperty
    @PropertyRange(min = 2)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @JsonProperty
    public Set<String> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<String> authorities) {
        this.authorities = authorities;
    }

//    @JsonProperty
//    public Set<Authority> getAuthorities() {
//        return authorities;
//    }
//
//    public void setAuthorities(Set<Authority> authorities) {
//        this.authorities = authorities;
//    }
//
//    public Set<String> getAuthoritiesString() {
//        if (authorities != null && !authorities.isEmpty()) {
//            return authorities.stream().map(Authority::getName).collect(Collectors.toSet());
//        }
//        return null;
//    }
//
//    public void setAuthoritiesString(Set<String> authorities) {
//        if (authorities != null) {
//            this.authorities.addAll(authorities.stream().map(Authority::new).collect(Collectors.toSet()));
//        }
//    }

    @JsonProperty
    @JsonSerialize(contentAs = BaseIdentifiableObject.class)
    public Set<Person> getMembers() {
        return members;
    }

    public void setMembers(Set<Person> members) {
        this.members = members;
    }

//    @JsonProperty
//    @JsonSerialize(contentAs = BaseIdentifiableObject.class)
    public List<User> getUsers() {
        List<User> users = new ArrayList<>();

        for (Person person : members) {
            if (person.getUserInfo() != null) {
                users.add(person.getUserInfo());
            }
        }

        return users;
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
    public User getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    @Override
    public void setLastUpdatedBy(User lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    @Override
    public User getCreatedBy() {
        return createdBy;
    }

    @Override
    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }
}
