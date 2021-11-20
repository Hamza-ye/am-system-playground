package org.nmcpye.activitiesmanagement.domain.person;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.nmcpye.activitiesmanagement.domain.User;
import org.nmcpye.activitiesmanagement.extended.common.BaseIdentifiableObject;
import org.nmcpye.activitiesmanagement.extended.common.DxfNamespaces;
import org.nmcpye.activitiesmanagement.extended.common.MetadataObject;
import org.nmcpye.activitiesmanagement.extended.schema.annotation.PropertyTransformer;
import org.nmcpye.activitiesmanagement.extended.schema.transformer.PersonPropertyTransformer;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * A PeopleGroup.
 * @author Hamza Assada
 */
@Entity
@Table(name = "people_group")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@JsonRootName( value = "peopleGroup", namespace = DxfNamespaces.DXF_2_0 )
public class PeopleGroup extends BaseIdentifiableObject implements MetadataObject {

    public static final String AUTH_USER_ADD = "F_USER_ADD";
    public static final String AUTH_USER_DELETE = "F_USER_DELETE";
    public static final String AUTH_USER_VIEW = "F_USER_VIEW";
    public static final String AUTH_USER_ADD_IN_GROUP = "F_USER_ADD_WITHIN_MANAGED_GROUP";
    public static final String AUTH_ADD_MEMBERS_TO_READ_ONLY_USER_GROUPS = "F_USER_GROUPS_READ_ONLY_ADD_MEMBERS";

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

    /**
     * Global unique identifier for PeopleGroup (to be used for sharing etc)
     */
    private UUID uuid;

    /**
     * Set of related people
     */

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(
        name = "people_group_members",
        joinColumns = @JoinColumn(name = "people_group_id"),
        inverseJoinColumns = @JoinColumn(name = "person_id")
    )
    @JsonIgnoreProperties(
        value = {
            "userInfo", "user", "createdBy", "lastUpdatedBy", "organisationUnits", "dataViewOrganisationUnits", "personAuthorityGroups", "groups",
        },
        allowSetters = true
    )
    private Set<Person> members = new HashSet<>();

    /**
     * People groups (if any) whose members can manage the members of this
     * People group.
     */
    //    fk_usergroupmanaging_managedbygroupid
    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(
        name = "people_group_managed",
        joinColumns = @JoinColumn(name = "managed_group_id"),
        inverseJoinColumns = @JoinColumn(name = "managed_by_group_id")
    )
    @JsonIgnoreProperties(value = { "user", "createdBy", "lastUpdatedBy", "members", "managedByGroups", "managedGroups" }, allowSetters = true)
    private Set<PeopleGroup> managedByGroups = new HashSet<>();

    /**
     * People groups (if any) that members of this people group can manage
     * the members within.
     */
    @ManyToMany(mappedBy = "managedByGroups")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "user", "createdBy", "lastUpdatedBy", "members", "managedByGroups", "managedGroups" }, allowSetters = true)
    private Set<PeopleGroup> managedGroups = new HashSet<>();

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    public PeopleGroup() {
        this.setAutoFields();
    }

    public PeopleGroup(String name) {
        this();
        this.name = name;
    }

    public PeopleGroup(String name, Set<Person> members) {
        this(name);
        this.members = members;
    }

    @Override
    public void setAutoFields() {
        if (uuid == null) {
            uuid = UUID.randomUUID();
        }

        super.setAutoFields();
    }

    // -------------------------------------------------------------------------
    // Logic
    // -------------------------------------------------------------------------

    public void addPerson(Person person) {
        members.add(person);
        person.getGroups().add(this);
    }

    public void removePerson(Person person) {
        members.remove(person);
        person.getGroups().remove(this);
    }

    public void updatePeople(Set<Person> updates) {
        new HashSet<>(members).stream().filter(person -> !updates.contains(person)).forEach(this::removePerson);

        updates.forEach(this::addPerson);
    }

    public void addManagedGroup(PeopleGroup group) {
        managedGroups.add(group);
        group.getManagedByGroups().add(this);
    }

    public void removeManagedGroup(PeopleGroup group) {
        managedGroups.remove(group);
        group.getManagedByGroups().remove(this);
    }

    public void updateManagedGroups(Set<PeopleGroup> updates) {
        new HashSet<>(managedGroups).stream().filter(group -> !updates.contains(group)).forEach(this::removeManagedGroup);

        updates.forEach(this::addManagedGroup);
    }

    // -------------------------------------------------------------------------
    // Getters and setters
    // -------------------------------------------------------------------------

//    @Override
//    @JsonIgnore
//    public User getUser() {
//        return user;
//    }
//
//    @Override
//    @JsonIgnore
//    public void setUser(User user) {
//        this.user = user;
//    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    @JsonProperty("people")
    @JsonSerialize(contentUsing = PersonPropertyTransformer.JacksonSerialize.class)
    @JsonDeserialize(contentUsing = PersonPropertyTransformer.JacksonDeserialize.class)
    @PropertyTransformer(PersonPropertyTransformer.class)
    public Set<Person> getMembers() {
        return members;
    }

    public void setMembers(Set<Person> members) {
        this.members = members;
    }

    @JsonProperty("managedGroups")
    @JsonSerialize(contentAs = BaseIdentifiableObject.class)
    public Set<PeopleGroup> getManagedGroups() {
        return managedGroups;
    }

    public void setManagedGroups(Set<PeopleGroup> managedGroups) {
        this.managedGroups = managedGroups;
    }

    @JsonProperty("managedByGroups")
    @JsonSerialize(contentAs = BaseIdentifiableObject.class)
    public Set<PeopleGroup> getManagedByGroups() {
        return managedByGroups;
    }

    public void setManagedByGroups(Set<PeopleGroup> managedByGroups) {
        this.managedByGroups = managedByGroups;
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
