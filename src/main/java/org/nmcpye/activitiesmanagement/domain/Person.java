package org.nmcpye.activitiesmanagement.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.nmcpye.activitiesmanagement.domain.enumeration.Gender;

/**
 * A Person.
 */
@Entity
@Table(name = "person")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Person implements Serializable {

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

    @Column(name = "uuid", unique = true)
    private UUID uuid;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender;

    @Column(name = "mobile")
    private String mobile;

    @Column(name = "last_login")
    private Instant lastLogin;

    @Column(name = "login")
    private String login;

    @Column(name = "self_registered")
    private Boolean selfRegistered;

    @Column(name = "disabled")
    private Boolean disabled;

    @OneToOne
    @JoinColumn(unique = true)
    private User userInfo;

    @ManyToOne
    private User user;

    @ManyToOne
    private User lastUpdatedBy;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(
        name = "rel_person__organisation_unit",
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
        name = "rel_person__data_view_organisation_unit",
        joinColumns = @JoinColumn(name = "person_id"),
        inverseJoinColumns = @JoinColumn(name = "data_view_organisation_unit_id")
    )
    @JsonIgnoreProperties(
        value = {
            "malariaReports",
            "dengueReports",
            "parent",
            "hfHomeSubVillage",
            "coveredByHf",
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
        },
        allowSetters = true
    )
    private Set<OrganisationUnit> dataViewOrganisationUnits = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(
        name = "rel_person__person_authority_groups",
        joinColumns = @JoinColumn(name = "person_id"),
        inverseJoinColumns = @JoinColumn(name = "person_authority_groups_id")
    )
    @JsonIgnoreProperties(value = { "user", "lastUpdatedBy", "members" }, allowSetters = true)
    private Set<PersonAuthorityGroup> personAuthorityGroups = new HashSet<>();

    @ManyToMany(mappedBy = "members")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "user", "lastUpdatedBy", "members", "managedByGroups", "managedGroups" }, allowSetters = true)
    private Set<PeopleGroup> groups = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Person id(Long id) {
        this.id = id;
        return this;
    }

    public String getUid() {
        return this.uid;
    }

    public Person uid(String uid) {
        this.uid = uid;
        return this;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getCode() {
        return this.code;
    }

    public Person code(String code) {
        this.code = code;
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return this.name;
    }

    public Person name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Instant getCreated() {
        return this.created;
    }

    public Person created(Instant created) {
        this.created = created;
        return this;
    }

    public void setCreated(Instant created) {
        this.created = created;
    }

    public Instant getLastUpdated() {
        return this.lastUpdated;
    }

    public Person lastUpdated(Instant lastUpdated) {
        this.lastUpdated = lastUpdated;
        return this;
    }

    public void setLastUpdated(Instant lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public Person uuid(UUID uuid) {
        this.uuid = uuid;
        return this;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

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

    public Instant getLastLogin() {
        return this.lastLogin;
    }

    public Person lastLogin(Instant lastLogin) {
        this.lastLogin = lastLogin;
        return this;
    }

    public void setLastLogin(Instant lastLogin) {
        this.lastLogin = lastLogin;
    }

    public String getLogin() {
        return this.login;
    }

    public Person login(String login) {
        this.login = login;
        return this;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public Boolean getSelfRegistered() {
        return this.selfRegistered;
    }

    public Person selfRegistered(Boolean selfRegistered) {
        this.selfRegistered = selfRegistered;
        return this;
    }

    public void setSelfRegistered(Boolean selfRegistered) {
        this.selfRegistered = selfRegistered;
    }

    public Boolean getDisabled() {
        return this.disabled;
    }

    public Person disabled(Boolean disabled) {
        this.disabled = disabled;
        return this;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

    public User getUserInfo() {
        return this.userInfo;
    }

    public Person userInfo(User user) {
        this.setUserInfo(user);
        return this;
    }

    public void setUserInfo(User user) {
        this.userInfo = user;
    }

    public User getUser() {
        return this.user;
    }

    public Person user(User user) {
        this.setUser(user);
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getLastUpdatedBy() {
        return this.lastUpdatedBy;
    }

    public Person lastUpdatedBy(User user) {
        this.setLastUpdatedBy(user);
        return this;
    }

    public void setLastUpdatedBy(User user) {
        this.lastUpdatedBy = user;
    }

    public Set<OrganisationUnit> getOrganisationUnits() {
        return this.organisationUnits;
    }

    public Person organisationUnits(Set<OrganisationUnit> organisationUnits) {
        this.setOrganisationUnits(organisationUnits);
        return this;
    }

    public Person addOrganisationUnit(OrganisationUnit organisationUnit) {
        this.organisationUnits.add(organisationUnit);
        organisationUnit.getPeople().add(this);
        return this;
    }

    public Person removeOrganisationUnit(OrganisationUnit organisationUnit) {
        this.organisationUnits.remove(organisationUnit);
        organisationUnit.getPeople().remove(this);
        return this;
    }

    public void setOrganisationUnits(Set<OrganisationUnit> organisationUnits) {
        this.organisationUnits = organisationUnits;
    }

    public Set<OrganisationUnit> getDataViewOrganisationUnits() {
        return this.dataViewOrganisationUnits;
    }

    public Person dataViewOrganisationUnits(Set<OrganisationUnit> organisationUnits) {
        this.setDataViewOrganisationUnits(organisationUnits);
        return this;
    }

    public Person addDataViewOrganisationUnit(OrganisationUnit organisationUnit) {
        this.dataViewOrganisationUnits.add(organisationUnit);
        organisationUnit.getDataViewPeople().add(this);
        return this;
    }

    public Person removeDataViewOrganisationUnit(OrganisationUnit organisationUnit) {
        this.dataViewOrganisationUnits.remove(organisationUnit);
        organisationUnit.getDataViewPeople().remove(this);
        return this;
    }

    public void setDataViewOrganisationUnits(Set<OrganisationUnit> organisationUnits) {
        this.dataViewOrganisationUnits = organisationUnits;
    }

    public Set<PersonAuthorityGroup> getPersonAuthorityGroups() {
        return this.personAuthorityGroups;
    }

    public Person personAuthorityGroups(Set<PersonAuthorityGroup> personAuthorityGroups) {
        this.setPersonAuthorityGroups(personAuthorityGroups);
        return this;
    }

    public Person addPersonAuthorityGroups(PersonAuthorityGroup personAuthorityGroup) {
        this.personAuthorityGroups.add(personAuthorityGroup);
        personAuthorityGroup.getMembers().add(this);
        return this;
    }

    public Person removePersonAuthorityGroups(PersonAuthorityGroup personAuthorityGroup) {
        this.personAuthorityGroups.remove(personAuthorityGroup);
        personAuthorityGroup.getMembers().remove(this);
        return this;
    }

    public void setPersonAuthorityGroups(Set<PersonAuthorityGroup> personAuthorityGroups) {
        this.personAuthorityGroups = personAuthorityGroups;
    }

    public Set<PeopleGroup> getGroups() {
        return this.groups;
    }

    public Person groups(Set<PeopleGroup> peopleGroups) {
        this.setGroups(peopleGroups);
        return this;
    }

    public Person addGroup(PeopleGroup peopleGroup) {
        this.groups.add(peopleGroup);
        peopleGroup.getMembers().add(this);
        return this;
    }

    public Person removeGroup(PeopleGroup peopleGroup) {
        this.groups.remove(peopleGroup);
        peopleGroup.getMembers().remove(this);
        return this;
    }

    public void setGroups(Set<PeopleGroup> peopleGroups) {
        if (this.groups != null) {
            this.groups.forEach(i -> i.removeMember(this));
        }
        if (peopleGroups != null) {
            peopleGroups.forEach(i -> i.addMember(this));
        }
        this.groups = peopleGroups;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Person)) {
            return false;
        }
        return id != null && id.equals(((Person) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Person{" +
            "id=" + getId() +
            ", uid='" + getUid() + "'" +
            ", code='" + getCode() + "'" +
            ", name='" + getName() + "'" +
            ", created='" + getCreated() + "'" +
            ", lastUpdated='" + getLastUpdated() + "'" +
            ", uuid='" + getUuid() + "'" +
            ", gender='" + getGender() + "'" +
            ", mobile='" + getMobile() + "'" +
            ", lastLogin='" + getLastLogin() + "'" +
            ", login='" + getLogin() + "'" +
            ", selfRegistered='" + getSelfRegistered() + "'" +
            ", disabled='" + getDisabled() + "'" +
            "}";
    }
}
