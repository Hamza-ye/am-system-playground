package org.nmcpye.activitiesmanagement.extended.person.criteria;

import org.nmcpye.activitiesmanagement.domain.enumeration.Gender;
import org.nmcpye.activitiesmanagement.domain.person.Person;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

import java.io.Serializable;
import java.util.Objects;

/**
 * Criteria class for the {@link Person} entity. This class is used
 * in {@link org.nmcpye.activitiesmanagement.web.rest.PersonResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /people?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class PersonCriteria implements Serializable, Criteria {

    /**
     * Class for filtering Gender
     */
    public static class GenderFilter extends Filter<Gender> {

        public GenderFilter() {}

        public GenderFilter(GenderFilter filter) {
            super(filter);
        }

        @Override
        public GenderFilter copy() {
            return new GenderFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter firstName;

    private StringFilter surname;

    private GenderFilter gender;

    private StringFilter mobile;

    private LongFilter userId;

    private LongFilter personCredentialId;

    private LongFilter groupId;

    private LongFilter organisationUnitId;

    private LongFilter dataViewOrganisationUnitId;

    private LongFilter teamId;

    private LongFilter malariaUnitStaffMemberId;

    private LongFilter chvTeamId;

    private LongFilter chvId;

    public PersonCriteria() {}

    public PersonCriteria(PersonCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.gender = other.gender == null ? null : other.gender.copy();
        this.mobile = other.mobile == null ? null : other.mobile.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
        this.teamId = other.teamId == null ? null : other.teamId.copy();

        this.personCredentialId = other.personCredentialId == null ? null : other.personCredentialId.copy();
        this.groupId = other.groupId == null ? null : other.groupId.copy();
        this.organisationUnitId = other.organisationUnitId == null ? null : other.organisationUnitId.copy();
        this.dataViewOrganisationUnitId = other.dataViewOrganisationUnitId == null ? null : other.dataViewOrganisationUnitId.copy();

        this.malariaUnitStaffMemberId = other.malariaUnitStaffMemberId == null ? null : other.malariaUnitStaffMemberId.copy();
        this.chvTeamId = other.chvTeamId == null ? null : other.chvTeamId.copy();
        this.chvId = other.chvId == null ? null : other.chvId.copy();
    }

    @Override
    public PersonCriteria copy() {
        return new PersonCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getName() {
        return name;
    }

    public StringFilter name() {
        if (name == null) {
            name = new StringFilter();
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public GenderFilter getGender() {
        return gender;
    }

    public GenderFilter gender() {
        if (gender == null) {
            gender = new GenderFilter();
        }
        return gender;
    }

    public void setGender(GenderFilter gender) {
        this.gender = gender;
    }

    public StringFilter getMobile() {
        return mobile;
    }

    public StringFilter mobile() {
        if (mobile == null) {
            mobile = new StringFilter();
        }
        return mobile;
    }

    public void setMobile(StringFilter mobile) {
        this.mobile = mobile;
    }

    public LongFilter getUserId() {
        return userId;
    }

    public LongFilter userId() {
        if (userId == null) {
            userId = new LongFilter();
        }
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
    }

    public LongFilter getTeamId() {
        return teamId;
    }

    public LongFilter teamId() {
        if (teamId == null) {
            teamId = new LongFilter();
        }
        return teamId;
    }

    public void setTeamId(LongFilter teamId) {
        this.teamId = teamId;
    }

    public StringFilter getFirstName() {
        return firstName;
    }

    public StringFilter firstName() {
        if (firstName == null) {
            firstName = new StringFilter();
        }
        return firstName;
    }

    public void setFirstName(StringFilter firstName) {
        this.firstName = firstName;
    }

    public StringFilter getSurname() {
        return surname;
    }

    public StringFilter surname() {
        if (surname == null) {
            surname = new StringFilter();
        }
        return surname;
    }

    public void setSurname(StringFilter surname) {
        this.surname = surname;
    }

    public LongFilter getPersonCredentialId() {
        return personCredentialId;
    }

    public LongFilter personCredentialId() {
        if (personCredentialId == null) {
            personCredentialId = new LongFilter();
        }
        return personCredentialId;
    }

    public void setPersonCredentialId(LongFilter personCredentialId) {
        this.personCredentialId = personCredentialId;
    }

    public LongFilter getGroupId() {
        return groupId;
    }

    public LongFilter groupId() {
        if (groupId == null) {
            groupId = new LongFilter();
        }
        return groupId;
    }

    public void setGroupId(LongFilter groupId) {
        this.groupId = groupId;
    }

    public LongFilter getOrganisationUnitId() {
        return organisationUnitId;
    }

    public LongFilter organisationUnitId() {
        if (organisationUnitId == null) {
            organisationUnitId = new LongFilter();
        }
        return organisationUnitId;
    }

    public void setOrganisationUnitId(LongFilter organisationUnitId) {
        this.organisationUnitId = organisationUnitId;
    }

    public LongFilter getDataViewOrganisationUnitId() {
        return dataViewOrganisationUnitId;
    }

    public LongFilter dataViewOrganisationUnitId() {
        if (dataViewOrganisationUnitId == null) {
            dataViewOrganisationUnitId = new LongFilter();
        }
        return dataViewOrganisationUnitId;
    }

    public void setDataViewOrganisationUnitId(LongFilter dataViewOrganisationUnitId) {
        this.dataViewOrganisationUnitId = dataViewOrganisationUnitId;
    }

    public LongFilter getMalariaUnitStaffMemberId() {
        return malariaUnitStaffMemberId;
    }

    public LongFilter malariaUnitStaffMemberId() {
        if (malariaUnitStaffMemberId == null) {
            malariaUnitStaffMemberId = new LongFilter();
        }
        return malariaUnitStaffMemberId;
    }

    public void setMalariaUnitStaffMemberId(LongFilter malariaUnitStaffMemberId) {
        this.malariaUnitStaffMemberId = malariaUnitStaffMemberId;
    }

    public LongFilter getChvTeamId() {
        return chvTeamId;
    }

    public LongFilter chvTeamId() {
        if (chvTeamId == null) {
            chvTeamId = new LongFilter();
        }
        return chvTeamId;
    }

    public void setChvTeamId(LongFilter chvTeamId) {
        this.chvTeamId = chvTeamId;
    }

    public LongFilter getChvId() {
        return chvId;
    }

    public LongFilter chvId() {
        if (chvId == null) {
            chvId = new LongFilter();
        }
        return chvId;
    }

    public void setChvId(LongFilter chvId) {
        this.chvId = chvId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final PersonCriteria that = (PersonCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(gender, that.gender) &&
            Objects.equals(mobile, that.mobile) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(teamId, that.teamId) &&
            Objects.equals(firstName, that.firstName) &&
            Objects.equals(surname, that.surname) &&
            Objects.equals(groupId, that.groupId) &&
            Objects.equals(organisationUnitId, that.organisationUnitId) &&
            Objects.equals(dataViewOrganisationUnitId, that.dataViewOrganisationUnitId) &&
            Objects.equals(malariaUnitStaffMemberId, that.malariaUnitStaffMemberId) &&
            Objects.equals(chvTeamId, that.chvTeamId) &&
            Objects.equals(chvId, that.chvId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            name,
            gender,
            mobile,
            userId,
            teamId,
            firstName,
            surname,
            groupId,
            organisationUnitId,
            dataViewOrganisationUnitId,
            malariaUnitStaffMemberId,
            chvTeamId,
            chvId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PersonCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (firstName != null ? "teamId=" + teamId + ", " : "") +
            (surname != null ? "teamId=" + teamId + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (gender != null ? "gender=" + gender + ", " : "") +
            (mobile != null ? "mobile=" + mobile + ", " : "") +
            (userId != null ? "userId=" + userId + ", " : "") +
            (teamId != null ? "teamId=" + teamId + ", " : "") +
            (groupId != null ? "teamId=" + teamId + ", " : "") +
            (organisationUnitId != null ? "teamId=" + teamId + ", " : "") +
            (dataViewOrganisationUnitId != null ? "teamId=" + teamId + ", " : "") +
            (malariaUnitStaffMemberId != null ? "malariaUnitStaffMemberId=" + malariaUnitStaffMemberId + ", " : "") +
            (chvTeamId != null ? "chvTeamId=" + chvTeamId + ", " : "") +
            (chvId != null ? "chvId=" + chvId + ", " : "") +
            "}";
    }
}
