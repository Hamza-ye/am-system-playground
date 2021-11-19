package org.nmcpye.activitiesmanagement.extended.person;

import com.google.common.collect.Lists;
import org.nmcpye.activitiesmanagement.domain.User;
import org.nmcpye.activitiesmanagement.domain.person.PeopleGroup;
import org.nmcpye.activitiesmanagement.domain.person.Person;
import org.nmcpye.activitiesmanagement.domain.person.PersonAuthorityGroup;
import org.nmcpye.activitiesmanagement.extended.common.AuditLogUtil;
import org.nmcpye.activitiesmanagement.extended.common.filter.FilterUtils;
import org.nmcpye.activitiesmanagement.extended.common.filter.UserAuthorityGroupCanIssueFilter;
import org.nmcpye.activitiesmanagement.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Nullable;
import java.util.*;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.nmcpye.activitiesmanagement.extended.common.CodeGenerator.isValidUid;
import static org.nmcpye.activitiesmanagement.extended.systemmodule.system.util.ValidationUtils.uuidIsValid;

@Lazy
@Service
public class DefaultPersonService implements PersonServiceExt {

    private final Logger log = LoggerFactory.getLogger(DefaultPersonService.class);

    private static final int EXPIRY_THRESHOLD = 14;

    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private final PersonStore personStore;

    private final PeopleGroupServiceExt peopleGroupServiceExt;

    private final PersonAuthorityGroupStore userAuthorityGroupStore;

    private final UserService userService;

    public DefaultPersonService(
        PersonStore personStore,
        PeopleGroupServiceExt peopleGroupServiceExt,
        PersonAuthorityGroupStore userAuthorityGroupStore,
        UserService userService
    ) {
        checkNotNull(personStore);
        checkNotNull(peopleGroupServiceExt);
        checkNotNull(userAuthorityGroupStore);

        this.personStore = personStore;
        this.peopleGroupServiceExt = peopleGroupServiceExt;
        this.userAuthorityGroupStore = userAuthorityGroupStore;
        this.userService = userService;
    }

    // -------------------------------------------------------------------------
    // UserService implementation
    // -------------------------------------------------------------------------

    // -------------------------------------------------------------------------
    // User
    // -------------------------------------------------------------------------

    @Override
    @Transactional
    public Long addUser(Person person) {
        User user = userService.getUserWithAuthorities().orElse(null);
        String userLogin = null;
        if (user != null) {
            userLogin = user.getLogin();
        }
        AuditLogUtil.infoWrapper(log, userLogin, person, AuditLogUtil.ACTION_CREATE);

        personStore.saveObject(person);

        return person.getId();
    }

    @Override
    @Transactional
    public void updateUser(Person person) {
        personStore.update(person);

        User user = userService.getUserWithAuthorities().orElse(null);
        String userLogin = null;
        if (user != null) {
            userLogin = user.getLogin();
        }

        AuditLogUtil.infoWrapper(log, userLogin, person, AuditLogUtil.ACTION_UPDATE);
    }

    @Override
    @Transactional
    public void deleteUser(Person person) {
        User user = userService.getUserWithAuthorities().orElse(null);
        String userLogin = null;
        if (user != null) {
            userLogin = user.getLogin();
        }

        AuditLogUtil.infoWrapper(log, userLogin, person, AuditLogUtil.ACTION_DELETE);

        personStore.delete(person);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Person> getAllUsers() {
        return personStore.getAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Person getUser(Long userId) {
        return personStore.get(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public Person getUser(String uid) {
        return personStore.getByUid(uid);
    }

    @Override
    @Transactional(readOnly = true)
    public Person getUserByUuid(UUID uuid) {
        Person userCredentials = personStore.getPersonByUuid(uuid);

        //        return userCredentials != null ? userCredentials.getUserInfo() : null;
        return userCredentials;
    }

    @Override
    @Transactional(readOnly = true)
    public Person getUserByUsername(String username) {
        Person userCredentials = personStore.getPersonByLogin(username);

        //        return userCredentials != null ? userCredentials.getUserInfo() : null;
        return userCredentials;
    }

    @Override
    @Transactional(readOnly = true)
    public Person getUserByIdentifier(String id) {
        Person user = null;

        if (isValidUid(id) && (user = getUser(id)) != null) {
            return user;
        }

        if (uuidIsValid(id) && (user = getUserByUuid(UUID.fromString(id))) != null) {
            return user;
        }

        if ((user = getUserByUsername(id)) != null) {
            return user;
        }

        return user;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Person> getUsers(Collection<String> uids) {
        return personStore.getByUid(uids);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Person> getAllUsersBetweenByName(String name, int first, int max) {
        PersonQueryParams params = new PersonQueryParams();
        params.setQuery(name);
        params.setFirst(first);
        params.setMax(max);

        return personStore.getPeople(params);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Person> getUsers(PersonQueryParams params) {
        return getUsers(params, null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Person> getUsers(PersonQueryParams params, @Nullable List<String> orders) {
        handlePersonQueryParams(params);

        if (!validatePersonQueryParams(params)) {
            return Lists.newArrayList();
        }

        return personStore.getPeople(params, orders);
    }

    @Override
    @Transactional(readOnly = true)
    public int getUserCount(PersonQueryParams params) {
        handlePersonQueryParams(params);

        if (!validatePersonQueryParams(params)) {
            return 0;
        }

        return personStore.getPersonCount(params);
    }

    @Override
    @Transactional(readOnly = true)
    public int getUserCount() {
        return personStore.getPersonCount();
    }

    private void handlePersonQueryParams(PersonQueryParams params) {
        boolean canGrantOwnRoles = true; //(Boolean) systemSettingManager.getSystemSetting( SettingKey.CAN_GRANT_OWN_USER_AUTHORITY_GROUPS );
        params.setDisjointRoles(!canGrantOwnRoles);

        User currentUser = userService.getUserWithAuthorities().orElse(null);

        if (!params.hasPerson() && currentUser != null && currentUser.getPerson() != null) {
            params.setPerson(currentUser.getPerson());
        }

        if (params.hasPerson() && params.getPerson().isSuper()) {
            params.setCanManage(false);
            params.setAuthSubset(false);
            params.setDisjointRoles(false);
        }

        if (params.isUserOrgUnits() && params.hasPerson()) {
            params.setOrganisationUnits(Lists.newArrayList(params.getPerson().getOrganisationUnits()));
        }
    }

    private boolean validatePersonQueryParams(PersonQueryParams params) {
        if (params.isCanManage() && (params.getPerson() == null || !params.getPerson().hasManagedGroups())) {
            log.warn("Cannot get managed users as user does not have any managed groups");
            return false;
        }

        if (params.isAuthSubset() && (params.getPerson() == null || !params.getPerson().hasAuthorities())) {
            log.warn("Cannot get users with authority subset as user does not have any authorities");
            return false;
        }

        if (params.isDisjointRoles() && (params.getPerson() == null || !params.getPerson().hasUserAuthorityGroups())) {
            log.warn("Cannot get users with disjoint roles as user does not have any user roles");
            return false;
        }

        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Person> getUsersByPhoneNumber(String phoneNumber) {
        PersonQueryParams params = new PersonQueryParams();
        params.setMobile(phoneNumber);
        return getUsers(params);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isLastSuperUser(Person userCredentials) {
        if (!userCredentials.isSuper()) {
            return false; // Cannot be last if not super user
        }

        Collection<Person> users = personStore.getAll();

        for (Person user : users) {
            if (user.isSuper() && !user.equals(userCredentials)) {
                return false;
            }
        }

        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isLastSuperRole(PersonAuthorityGroup userAuthorityGroup) {
        Collection<PersonAuthorityGroup> groups = userAuthorityGroupStore.getAll();

        for (PersonAuthorityGroup group : groups) {
            if (group.isSuper() && group.getId() != userAuthorityGroup.getId()) {
                return false;
            }
        }

        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean canAddOrUpdateUser(Collection<String> userGroups) {
        return canAddOrUpdateUser(userGroups, userService.getUserWithAuthorities().orElse(null));
    }

    @Override
    @Transactional(readOnly = true)
    public boolean canAddOrUpdateUser(Collection<String> userGroups, User currentUser) {
        if (currentUser == null) {
            return false;
        }

        // Ext
        if (currentUser.getPerson() == null) {
            return false;
        }

        boolean canAdd = currentUser.getPerson().isAuthorized(PeopleGroup.AUTH_USER_ADD);

        if (canAdd) {
            return true;
        }

        boolean canAddInGroup = currentUser.getPerson().isAuthorized(PeopleGroup.AUTH_USER_ADD_IN_GROUP);

        if (!canAddInGroup) {
            return false;
        }

        boolean canManageAnyGroup = false;

        for (String uid : userGroups) {
            PeopleGroup userGroup = peopleGroupServiceExt.getUserGroup(uid);

            if (currentUser.getPerson().canManage(userGroup)) {
                canManageAnyGroup = true;
                break;
            }
        }

        return canManageAnyGroup;
    }

    // -------------------------------------------------------------------------
    // PersonAuthorityGroup
    // -------------------------------------------------------------------------

    @Override
    @Transactional
    public long addUserAuthorityGroup(PersonAuthorityGroup userAuthorityGroup) {
        userAuthorityGroupStore.saveObject(userAuthorityGroup);
        return userAuthorityGroup.getId();
    }

    @Override
    @Transactional
    public void updateUserAuthorityGroup(PersonAuthorityGroup userAuthorityGroup) {
        userAuthorityGroupStore.update(userAuthorityGroup);
    }

    @Override
    @Transactional
    public void deleteUserAuthorityGroup(PersonAuthorityGroup userAuthorityGroup) {
        userAuthorityGroupStore.delete(userAuthorityGroup);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PersonAuthorityGroup> getAllUserAuthorityGroups() {
        return userAuthorityGroupStore.getAll();
    }

    @Override
    @Transactional(readOnly = true)
    public PersonAuthorityGroup getUserAuthorityGroup(long id) {
        return userAuthorityGroupStore.get(id);
    }

    @Override
    @Transactional(readOnly = true)
    public PersonAuthorityGroup getUserAuthorityGroup(String uid) {
        return userAuthorityGroupStore.getByUid(uid);
    }

    @Override
    @Transactional(readOnly = true)
    public PersonAuthorityGroup getUserAuthorityGroupByName(String name) {
        return userAuthorityGroupStore.getByName(name);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PersonAuthorityGroup> getUserRolesByUid(Collection<String> uids) {
        return userAuthorityGroupStore.getByUid(uids);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PersonAuthorityGroup> getUserRolesBetween(int first, int max) {
        return userAuthorityGroupStore.getAllOrderedName(first, max);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PersonAuthorityGroup> getUserRolesBetweenByName(String name, int first, int max) {
        return userAuthorityGroupStore.getAllLikeName(name, first, max);
    }

    @Override
    @Transactional(readOnly = true)
    public void canIssueFilter(Collection<PersonAuthorityGroup> userRoles) {
        User user = userService.getUserWithAuthorities().orElse(null);

        //        boolean canGrantOwnUserAuthorityGroups = (Boolean) systemSettingManager.getSystemSetting( SettingKey.CAN_GRANT_OWN_USER_AUTHORITY_GROUPS );
        boolean canGrantOwnUserAuthorityGroups = false;

        FilterUtils.filter(userRoles, new UserAuthorityGroupCanIssueFilter(user, canGrantOwnUserAuthorityGroups));
    }

    // -------------------------------------------------------------------------
    // Person
    // -------------------------------------------------------------------------

    @Override
    @Transactional
    public long addUserCredentials(Person userCredentials) {
        personStore.saveObject(userCredentials);
        return userCredentials.getId();
    }

    @Override
    @Transactional
    public void updateUserCredentials(Person userCredentials) {
        personStore.update(userCredentials);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Person> getAllUserCredentials() {
        return personStore.getAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Person getUserCredentialsByUsername(String username) {
        Optional<User> user = userService.getUserWithAuthoritiesByLogin(username);
        if (user.isPresent()) {
            return user.get().getPerson();
        }
        //        return userCredentialsStore.getUserCredentialsByUsername( username );
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public Person getUserCredentialsWithEagerFetchAuthorities(String username) {
        Optional<User> user = userService.getUserWithAuthoritiesByLogin(username);
        if (user.isPresent()) {
            if (user.get().getPerson() != null) {
                user.get().getPerson().getAllAuthorities();
                return user.get().getPerson();
            }
        }

        //        Person userCredentials = userCredentialsStore.getUserCredentialsByUsername( username );
        //
        //        if ( userCredentials != null )
        //        {
        //            userCredentials.getAllAuthorities();
        //        }

        return null;
    }

    @Override
    @Transactional
    public void setLastLogin(String username) {
        Person credentials = getUserCredentialsByUsername(username);

        if (credentials != null) {
            credentials.setLastLogin(new Date());
            updateUserCredentials(credentials);
        }
    }
}
