package org.nmcpye.activitiesmanagement.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.nmcpye.activitiesmanagement.domain.chv.Chv;
import org.nmcpye.activitiesmanagement.domain.enumeration.ChvTeamType;
import org.nmcpye.activitiesmanagement.domain.person.Person;

/**
 * A ChvTeam.
 */
@Entity
@Table(name = "chv_team")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ChvTeam implements Serializable {

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

    @Column(name = "description")
    private String description;

    @Column(name = "created")
    private Instant created;

    @Column(name = "last_updated")
    private Instant lastUpdated;

    @NotNull
    @Pattern(regexp = "^[0-9]{1,12}")
    @Column(name = "team_no", nullable = false, unique = true)
    private String teamNo;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "team_type", nullable = false)
    private ChvTeamType teamType;

    @ManyToOne
    @JoinColumn(name = "created_by")
    private User createdBy;

    @ManyToOne
    @JoinColumn(name = "last_updated_by")
    private User lastUpdatedBy;

    @ManyToOne
    @JsonIgnoreProperties(
        value = {
            "userInfo", "user", "createdBy", "lastUpdatedBy", "organisationUnits", "dataViewOrganisationUnits", "personAuthorityGroups", "groups",
        },
        allowSetters = true
    )
    private Person person;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(
        name = "chv_team_responsible_for_chv",
        joinColumns = @JoinColumn(name = "chv_team_id"),
        inverseJoinColumns = @JoinColumn(name = "chv_id")
    )
    @JsonIgnoreProperties(
        value = {
            "person", "coveredSubVillages", "district", "homeSubvillage", "managedByHf", "user", "createdBy", "lastUpdatedBy", "supervisionTeams",
        },
        allowSetters = true
    )
    private Set<Chv> responsibleForChvs = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ChvTeam id(Long id) {
        this.id = id;
        return this;
    }

    public String getUid() {
        return this.uid;
    }

    public ChvTeam uid(String uid) {
        this.uid = uid;
        return this;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getCode() {
        return this.code;
    }

    public ChvTeam code(String code) {
        this.code = code;
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return this.name;
    }

    public ChvTeam name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public ChvTeam description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getCreated() {
        return this.created;
    }

    public ChvTeam created(Instant created) {
        this.created = created;
        return this;
    }

    public void setCreated(Instant created) {
        this.created = created;
    }

    public Instant getLastUpdated() {
        return this.lastUpdated;
    }

    public ChvTeam lastUpdated(Instant lastUpdated) {
        this.lastUpdated = lastUpdated;
        return this;
    }

    public void setLastUpdated(Instant lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getTeamNo() {
        return this.teamNo;
    }

    public ChvTeam teamNo(String teamNo) {
        this.teamNo = teamNo;
        return this;
    }

    public void setTeamNo(String teamNo) {
        this.teamNo = teamNo;
    }

    public ChvTeamType getTeamType() {
        return this.teamType;
    }

    public ChvTeam teamType(ChvTeamType teamType) {
        this.teamType = teamType;
        return this;
    }

    public void setTeamType(ChvTeamType teamType) {
        this.teamType = teamType;
    }

    @Deprecated
    public ChvTeam user(User user) {
        this.setUser(user);
        return this;
    }

    public User getLastUpdatedBy() {
        return this.lastUpdatedBy;
    }

    public ChvTeam lastUpdatedBy(User user) {
        this.setLastUpdatedBy(user);
        return this;
    }

    public void setLastUpdatedBy(User user) {
        this.lastUpdatedBy = user;
    }

    public Person getPerson() {
        return this.person;
    }

    public ChvTeam person(Person person) {
        this.setPerson(person);
        return this;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Set<Chv> getResponsibleForChvs() {
        return this.responsibleForChvs;
    }

    public ChvTeam responsibleForChvs(Set<Chv> cHVS) {
        this.setResponsibleForChvs(cHVS);
        return this;
    }

    public ChvTeam addResponsibleForChv(Chv cHV) {
        this.responsibleForChvs.add(cHV);
        cHV.getSupervisionTeams().add(this);
        return this;
    }

    public ChvTeam removeResponsibleForChv(Chv cHV) {
        this.responsibleForChvs.remove(cHV);
        cHV.getSupervisionTeams().remove(this);
        return this;
    }

    public void setResponsibleForChvs(Set<Chv> cHVS) {
        this.responsibleForChvs = cHVS;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ChvTeam)) {
            return false;
        }
        return id != null && id.equals(((ChvTeam) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ChvTeam{" +
            "id=" + getId() +
            ", uid='" + getUid() + "'" +
            ", code='" + getCode() + "'" +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", created='" + getCreated() + "'" +
            ", lastUpdated='" + getLastUpdated() + "'" +
            ", teamNo='" + getTeamNo() + "'" +
            ", teamType='" + getTeamType() + "'" +
            "}";
    }

    //    @Override
    public User getCreatedBy() {
        return createdBy;
    }

    //    @Override
    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    //    @Override
    @Deprecated
    public User getUser() {
        return createdBy;
    }

    //    @Override
    @Deprecated
    public void setUser(User user) {
        setCreatedBy(createdBy == null ? user : createdBy);
    }
}
