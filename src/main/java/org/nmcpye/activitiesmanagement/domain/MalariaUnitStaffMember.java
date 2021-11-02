package org.nmcpye.activitiesmanagement.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.nmcpye.activitiesmanagement.domain.enumeration.MalariaUnitMemberType;

/**
 * A MalariaUnitStaffMember.
 */
@Entity
@Table(name = "malaria_unit_staff_member")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class MalariaUnitStaffMember implements Serializable {

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

    @Column(name = "description")
    private String description;

    @Column(name = "created")
    private Instant created;

    @Column(name = "last_updated")
    private Instant lastUpdated;

    @NotNull
    @Column(name = "member_no", nullable = false)
    private Integer memberNo;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "member_type", nullable = false)
    private MalariaUnitMemberType memberType;

    @ManyToOne
    private User user;

    @ManyToOne
    private User lastUpdatedBy;

    @ManyToOne
    @JsonIgnoreProperties(
        value = {
            "userInfo", "user", "lastUpdatedBy", "organisationUnits", "dataViewOrganisationUnits", "personAuthorityGroups", "groups",
        },
        allowSetters = true
    )
    private Person person;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "organisationUnits", "malariaUnitStaffMembers", "user", "lastUpdatedBy" }, allowSetters = true)
    private MalariaUnit malariaUnit;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MalariaUnitStaffMember id(Long id) {
        this.id = id;
        return this;
    }

    public String getUid() {
        return this.uid;
    }

    public MalariaUnitStaffMember uid(String uid) {
        this.uid = uid;
        return this;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getCode() {
        return this.code;
    }

    public MalariaUnitStaffMember code(String code) {
        this.code = code;
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return this.description;
    }

    public MalariaUnitStaffMember description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getCreated() {
        return this.created;
    }

    public MalariaUnitStaffMember created(Instant created) {
        this.created = created;
        return this;
    }

    public void setCreated(Instant created) {
        this.created = created;
    }

    public Instant getLastUpdated() {
        return this.lastUpdated;
    }

    public MalariaUnitStaffMember lastUpdated(Instant lastUpdated) {
        this.lastUpdated = lastUpdated;
        return this;
    }

    public void setLastUpdated(Instant lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public Integer getMemberNo() {
        return this.memberNo;
    }

    public MalariaUnitStaffMember memberNo(Integer memberNo) {
        this.memberNo = memberNo;
        return this;
    }

    public void setMemberNo(Integer memberNo) {
        this.memberNo = memberNo;
    }

    public MalariaUnitMemberType getMemberType() {
        return this.memberType;
    }

    public MalariaUnitStaffMember memberType(MalariaUnitMemberType memberType) {
        this.memberType = memberType;
        return this;
    }

    public void setMemberType(MalariaUnitMemberType memberType) {
        this.memberType = memberType;
    }

    public User getUser() {
        return this.user;
    }

    public MalariaUnitStaffMember user(User user) {
        this.setUser(user);
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getLastUpdatedBy() {
        return this.lastUpdatedBy;
    }

    public MalariaUnitStaffMember lastUpdatedBy(User user) {
        this.setLastUpdatedBy(user);
        return this;
    }

    public void setLastUpdatedBy(User user) {
        this.lastUpdatedBy = user;
    }

    public Person getPerson() {
        return this.person;
    }

    public MalariaUnitStaffMember person(Person person) {
        this.setPerson(person);
        return this;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public MalariaUnit getMalariaUnit() {
        return this.malariaUnit;
    }

    public MalariaUnitStaffMember malariaUnit(MalariaUnit malariaUnit) {
        this.setMalariaUnit(malariaUnit);
        return this;
    }

    public void setMalariaUnit(MalariaUnit malariaUnit) {
        this.malariaUnit = malariaUnit;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MalariaUnitStaffMember)) {
            return false;
        }
        return id != null && id.equals(((MalariaUnitStaffMember) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MalariaUnitStaffMember{" +
            "id=" + getId() +
            ", uid='" + getUid() + "'" +
            ", code='" + getCode() + "'" +
            ", description='" + getDescription() + "'" +
            ", created='" + getCreated() + "'" +
            ", lastUpdated='" + getLastUpdated() + "'" +
            ", memberNo=" + getMemberNo() +
            ", memberType='" + getMemberType() + "'" +
            "}";
    }
}
