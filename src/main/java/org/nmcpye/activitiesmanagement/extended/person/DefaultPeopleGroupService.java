package org.nmcpye.activitiesmanagement.extended.person;

import org.nmcpye.activitiesmanagement.domain.User;
import org.nmcpye.activitiesmanagement.domain.person.PeopleGroup;
import org.nmcpye.activitiesmanagement.domain.person.Person;
import org.nmcpye.activitiesmanagement.extended.common.IdentifiableObjectStore;
import org.nmcpye.activitiesmanagement.security.SecurityUtils;
import org.nmcpye.activitiesmanagement.service.UserService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

@Service("org.nmcpye.activitiesmanagement.extended.person.PeopleGroupService")
public class DefaultPeopleGroupService implements PeopleGroupService {

    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private final IdentifiableObjectStore<PeopleGroup> userGroupStore;

    private final UserService userService;

    public DefaultPeopleGroupService(
        @Qualifier("org.nmcpye.activitiesmanagement.extended.person.PeopleGroupStore") IdentifiableObjectStore<PeopleGroup> userGroupStore,
        UserService userService
    ) {
        checkNotNull(userGroupStore);
        checkNotNull(userService);

        this.userGroupStore = userGroupStore;
        this.userService = userService;
    }

    // -------------------------------------------------------------------------
    // UserGroup
    // -------------------------------------------------------------------------

    @Override
    @Transactional
    public long addUserGroup(PeopleGroup userGroup) {
        userGroupStore.saveObject(userGroup);
        return userGroup.getId();
    }

    @Override
    @Transactional
    public void deleteUserGroup(PeopleGroup userGroup) {
        userGroupStore.delete(userGroup);
    }

    @Override
    @Transactional
    public void updateUserGroup(PeopleGroup userGroup) {
        userGroupStore.update(userGroup);
        // Clear query cache due to sharing and user group membership

        //        cacheManager.clearQueryCache();
    }

    @Override
    @Transactional(readOnly = true)
    public List<PeopleGroup> getAllUserGroups() {
        return userGroupStore.getAll();
    }

    @Override
    @Transactional(readOnly = true)
    public PeopleGroup getUserGroup(long userGroupId) {
        return userGroupStore.get(userGroupId);
    }

    @Override
    @Transactional(readOnly = true)
    public PeopleGroup getUserGroup(String uid) {
        return userGroupStore.getByUid(uid);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean canAddOrRemoveMember(String uid) {
        return canAddOrRemoveMember(uid, userService.getUserWithAuthorities().orElse(null));
    }

    @Override
    @Transactional(readOnly = true)
    public boolean canAddOrRemoveMember(String uid, User currentUser) {
        PeopleGroup userGroup = getUserGroup(uid);

        if (userGroup == null || currentUser == null || userService.getUserWithAuthorities().orElse(null) == null) {
            return false;
        }

        // TODO Later
        //        boolean canUpdate = aclService.canUpdate( currentUser, userGroup );
        boolean isAdmin = SecurityUtils.hasCurrentUserThisAuthority("ROLE_ADMIN");

        boolean canAddMember = currentUser.getPerson().isAuthorized(PeopleGroup.AUTH_ADD_MEMBERS_TO_READ_ONLY_USER_GROUPS);

        //        return canUpdate || canAddMember;
        return isAdmin || canAddMember;
    }

    @Override
    @Transactional
    public void addUserToGroups(Person user, Collection<String> uids) {
        addUserToGroups(user, uids, userService.getUserWithAuthorities().orElse(null));
    }

    @Override
    @Transactional
    public void addUserToGroups(Person user, Collection<String> uids, User currentUser) {
        for (String uid : uids) {
            if (canAddOrRemoveMember(uid, currentUser)) {
                PeopleGroup userGroup = getUserGroup(uid);
                userGroup.addPerson(user);
                userGroupStore.updateNoAcl(userGroup);
            }
        }
    }

    @Override
    @Transactional
    public void removeUserFromGroups(Person user, Collection<String> uids) {
        for (String uid : uids) {
            if (canAddOrRemoveMember(uid)) {
                PeopleGroup userGroup = getUserGroup(uid);
                userGroup.removePerson(user);
                userGroupStore.updateNoAcl(userGroup);
            }
        }
    }

    @Override
    @Transactional
    public void updateUserGroups(Person user, Collection<String> uids) {
        updateUserGroups(user, uids, userService.getUserWithAuthorities().orElse(null));
    }

    @Override
    @Transactional
    public void updateUserGroups(Person user, Collection<String> uids, User currentUser) {
        Collection<PeopleGroup> updates = getUserGroupsByUid(uids);

        for (PeopleGroup userGroup : new HashSet<>(user.getGroups())) {
            if (!updates.contains(userGroup) && canAddOrRemoveMember(userGroup.getUid(), currentUser)) {
                userGroup.removePerson(user);
            }
        }

        for (PeopleGroup userGroup : updates) {
            if (canAddOrRemoveMember(userGroup.getUid(), currentUser)) {
                userGroup.addPerson(user);
                userGroupStore.updateNoAcl(userGroup);
            }
        }
    }

    private Collection<PeopleGroup> getUserGroupsByUid(Collection<String> uids) {
        return userGroupStore.getByUid(uids);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PeopleGroup> getUserGroupByName(String name) {
        return userGroupStore.getAllEqName(name);
    }

    @Override
    @Transactional(readOnly = true)
    public int getUserGroupCount() {
        return userGroupStore.getCount();
    }

    @Override
    @Transactional(readOnly = true)
    public int getUserGroupCountByName(String name) {
        return userGroupStore.getCountLikeName(name);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PeopleGroup> getUserGroupsBetween(int first, int max) {
        return userGroupStore.getAllOrderedName(first, max);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PeopleGroup> getUserGroupsBetweenByName(String name, int first, int max) {
        return userGroupStore.getAllLikeName(name, first, max, false);
    }
}
