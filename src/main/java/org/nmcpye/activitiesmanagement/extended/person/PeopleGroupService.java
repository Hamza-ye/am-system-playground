package org.nmcpye.activitiesmanagement.extended.person;

import org.nmcpye.activitiesmanagement.domain.User;
import org.nmcpye.activitiesmanagement.domain.person.PeopleGroup;
import org.nmcpye.activitiesmanagement.domain.person.Person;

import java.util.Collection;
import java.util.List;

public interface PeopleGroupService {
    String ID = PeopleGroupService.class.getName();

    long addUserGroup(PeopleGroup peopleGroup);

    void updateUserGroup(PeopleGroup peopleGroup);

    void deleteUserGroup(PeopleGroup peopleGroup);

    PeopleGroup getUserGroup(long userGroupId);

    PeopleGroup getUserGroup(String uid);

    /**
     * Indicates whether the current person can add or remove members for the person
     * group with the given UID. To to so the current person must have write access
     * to the group or have read access as well as the F_USER_GROUPS_READ_ONLY_ADD_MEMBERS
     * authority.
     *
     * @param uid the person group UID.
     * @return true if the current person can add or remove members of the person group.
     */
    boolean canAddOrRemoveMember(String uid);

    boolean canAddOrRemoveMember(String uid, User currentUser);

    void addUserToGroups(Person user, Collection<String> uids);

    void addUserToGroups(Person user, Collection<String> uids, User currentUser);

    void removeUserFromGroups(Person user, Collection<String> uids);

    void updateUserGroups(Person user, Collection<String> uids);

    void updateUserGroups(Person user, Collection<String> uids, User currentUser);

    List<PeopleGroup> getAllUserGroups();

    List<PeopleGroup> getUserGroupByName(String name);

    List<PeopleGroup> getUserGroupsBetween(int first, int max);

    List<PeopleGroup> getUserGroupsBetweenByName(String name, int first, int max);

    int getUserGroupCount();

    int getUserGroupCountByName(String name);
}
