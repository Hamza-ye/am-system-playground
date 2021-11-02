package org.nmcpye.activitiesmanagement.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A PeopleGroup.
 */
@Entity
@Table(name = "people_group")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class PeopleGroup implements Serializable {

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

    @Column(name = "uuid")
    private UUID uuid;

    @ManyToOne
    private User user;

    @ManyToOne
    private User lastUpdatedBy;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(
        name = "rel_people_group__member",
        joinColumns = @JoinColumn(name = "people_group_id"),
        inverseJoinColumns = @JoinColumn(name = "member_id")
    )
    @JsonIgnoreProperties(
        value = {
            "userInfo", "user", "lastUpdatedBy", "organisationUnits", "dataViewOrganisationUnits", "personAuthorityGroups", "groups",
        },
        allowSetters = true
    )
    private Set<Person> members = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(
        name = "rel_people_group__managed_by_group",
        joinColumns = @JoinColumn(name = "people_group_id"),
        inverseJoinColumns = @JoinColumn(name = "managed_by_group_id")
    )
    @JsonIgnoreProperties(value = { "user", "lastUpdatedBy", "members", "managedByGroups", "managedGroups" }, allowSetters = true)
    private Set<PeopleGroup> managedByGroups = new HashSet<>();

    @ManyToMany(mappedBy = "managedByGroups")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "user", "lastUpdatedBy", "members", "managedByGroups", "managedGroups" }, allowSetters = true)
    private Set<PeopleGroup> managedGroups = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PeopleGroup id(Long id) {
        this.id = id;
        return this;
    }

    public String getUid() {
        return this.uid;
    }

    public PeopleGroup uid(String uid) {
        this.uid = uid;
        return this;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getCode() {
        return this.code;
    }

    public PeopleGroup code(String code) {
        this.code = code;
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return this.name;
    }

    public PeopleGroup name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Instant getCreated() {
        return this.created;
    }

    public PeopleGroup created(Instant created) {
        this.created = created;
        return this;
    }

    public void setCreated(Instant created) {
        this.created = created;
    }

    public Instant getLastUpdated() {
        return this.lastUpdated;
    }

    public PeopleGroup lastUpdated(Instant lastUpdated) {
        this.lastUpdated = lastUpdated;
        return this;
    }

    public void setLastUpdated(Instant lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public PeopleGroup uuid(UUID uuid) {
        this.uuid = uuid;
        return this;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public User getUser() {
        return this.user;
    }

    public PeopleGroup user(User user) {
        this.setUser(user);
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getLastUpdatedBy() {
        return this.lastUpdatedBy;
    }

    public PeopleGroup lastUpdatedBy(User user) {
        this.setLastUpdatedBy(user);
        return this;
    }

    public void setLastUpdatedBy(User user) {
        this.lastUpdatedBy = user;
    }

    public Set<Person> getMembers() {
        return this.members;
    }

    public PeopleGroup members(Set<Person> people) {
        this.setMembers(people);
        return this;
    }

    public PeopleGroup addMember(Person person) {
        this.members.add(person);
        person.getGroups().add(this);
        return this;
    }

    public PeopleGroup removeMember(Person person) {
        this.members.remove(person);
        person.getGroups().remove(this);
        return this;
    }

    public void setMembers(Set<Person> people) {
        this.members = people;
    }

    public Set<PeopleGroup> getManagedByGroups() {
        return this.managedByGroups;
    }

    public PeopleGroup managedByGroups(Set<PeopleGroup> peopleGroups) {
        this.setManagedByGroups(peopleGroups);
        return this;
    }

    public PeopleGroup addManagedByGroup(PeopleGroup peopleGroup) {
        this.managedByGroups.add(peopleGroup);
        peopleGroup.getManagedGroups().add(this);
        return this;
    }

    public PeopleGroup removeManagedByGroup(PeopleGroup peopleGroup) {
        this.managedByGroups.remove(peopleGroup);
        peopleGroup.getManagedGroups().remove(this);
        return this;
    }

    public void setManagedByGroups(Set<PeopleGroup> peopleGroups) {
        this.managedByGroups = peopleGroups;
    }

    public Set<PeopleGroup> getManagedGroups() {
        return this.managedGroups;
    }

    public PeopleGroup managedGroups(Set<PeopleGroup> peopleGroups) {
        this.setManagedGroups(peopleGroups);
        return this;
    }

    public PeopleGroup addManagedGroup(PeopleGroup peopleGroup) {
        this.managedGroups.add(peopleGroup);
        peopleGroup.getManagedByGroups().add(this);
        return this;
    }

    public PeopleGroup removeManagedGroup(PeopleGroup peopleGroup) {
        this.managedGroups.remove(peopleGroup);
        peopleGroup.getManagedByGroups().remove(this);
        return this;
    }

    public void setManagedGroups(Set<PeopleGroup> peopleGroups) {
        if (this.managedGroups != null) {
            this.managedGroups.forEach(i -> i.removeManagedByGroup(this));
        }
        if (peopleGroups != null) {
            peopleGroups.forEach(i -> i.addManagedByGroup(this));
        }
        this.managedGroups = peopleGroups;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PeopleGroup)) {
            return false;
        }
        return id != null && id.equals(((PeopleGroup) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PeopleGroup{" +
            "id=" + getId() +
            ", uid='" + getUid() + "'" +
            ", code='" + getCode() + "'" +
            ", name='" + getName() + "'" +
            ", created='" + getCreated() + "'" +
            ", lastUpdated='" + getLastUpdated() + "'" +
            ", uuid='" + getUuid() + "'" +
            "}";
    }
}
