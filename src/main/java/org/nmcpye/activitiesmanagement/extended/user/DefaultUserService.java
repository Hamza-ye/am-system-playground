package org.nmcpye.activitiesmanagement.extended.user;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.nmcpye.activitiesmanagement.extended.common.CodeGenerator.isValidUid;
import static org.nmcpye.activitiesmanagement.extended.system.util.ValidationUtils.uuidIsValid;

import com.google.common.collect.Lists;
import java.util.*;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import org.nmcpye.activitiesmanagement.domain.User;
import org.nmcpye.activitiesmanagement.domain.period.PeriodType;
import org.nmcpye.activitiesmanagement.domain.person.PeopleGroup;
import org.nmcpye.activitiesmanagement.domain.person.Person;
import org.nmcpye.activitiesmanagement.domain.person.PersonAuthorityGroup;
import org.nmcpye.activitiesmanagement.extended.common.AuditLogUtil;
import org.nmcpye.activitiesmanagement.extended.common.BaseIdentifiableObject;
import org.nmcpye.activitiesmanagement.extended.common.filter.FilterUtils;
import org.nmcpye.activitiesmanagement.extended.common.filter.UserAuthorityGroupCanIssueFilter;
import org.nmcpye.activitiesmanagement.extended.feedback.ErrorCode;
import org.nmcpye.activitiesmanagement.extended.feedback.ErrorReport;
import org.nmcpye.activitiesmanagement.extended.person.*;
import org.nmcpye.activitiesmanagement.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Lazy
@Service("org.nmcpye.activitiesmanagement.extended.user.UserService")
public class DefaultUserService implements org.nmcpye.activitiesmanagement.extended.user.UserService {

    private final Logger log = LoggerFactory.getLogger(DefaultUserService.class);

    private static final int EXPIRY_THRESHOLD = 14;

    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private final UserStore userStore;

    private final PeopleGroupService peopleGroupService;

    private final PersonServiceExtended personServiceExtended;

    private final PersonAuthorityGroupStore personAuthorityGroupStore;

    private final UserService userService;

    public DefaultUserService(
        UserStore userStore,
        PeopleGroupService peopleGroupService,
        PersonServiceExtended personServiceExtended,
        PersonAuthorityGroupStore personAuthorityGroupStore,
        UserService userService
    ) {
        checkNotNull(userStore);
        checkNotNull(peopleGroupService);
        checkNotNull(personServiceExtended);
        checkNotNull(personAuthorityGroupStore);

        this.userStore = userStore;
        this.peopleGroupService = peopleGroupService;
        this.personServiceExtended = personServiceExtended;
        this.personAuthorityGroupStore = personAuthorityGroupStore;
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
    public long addUser(User user) {
        User user1 = userService.getUserWithAuthorities().orElse(null);
        String userString = null;
        if (user1 != null) {
            userString = user1.getLogin();
        }
        AuditLogUtil.infoWrapper(log, userString, user, AuditLogUtil.ACTION_CREATE);

        userStore.save(user);

        return user.getId();
    }

    @Override
    @Transactional
    public void updateUser(User user) {
        userStore.update(user);

        User user1 = userService.getUserWithAuthorities().orElse(null);
        String userString = null;
        if (user1 != null) {
            userString = user1.getLogin();
        }

        AuditLogUtil.infoWrapper(log, userString, user, AuditLogUtil.ACTION_UPDATE);
    }

    @Override
    @Transactional
    public void deleteUser(User user) {
        User user1 = userService.getUserWithAuthorities().orElse(null);
        String userString = null;
        if (user1 != null) {
            userString = user1.getLogin();
        }

        AuditLogUtil.infoWrapper(log, userString, user, AuditLogUtil.ACTION_DELETE);

        userStore.delete(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userStore.getAll();
    }

    @Override
    @Transactional(readOnly = true)
    public User getUser(Long userId) {
        return userStore.get(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public User getUser(String uid) {
        return userStore.getByUid(uid);
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserByUuid(UUID uuid) {
        Person userCredentials = personServiceExtended.getUserByUuid(uuid);

        return userCredentials != null ? userCredentials.getUserInfo() : null;
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserByUsername(String username) {
        Person userCredentials = personServiceExtended.getUserCredentialsByUsername(username);

        return userCredentials != null ? userCredentials.getUserInfo() : null;
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserByIdentifier(String id) {
        User user = null;

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
    public List<User> getUsers(Collection<String> uids) {
        return userStore.getByUid(uids);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getAllUsersBetweenByName(String name, int first, int max) {
        UserQueryParams params = new UserQueryParams();
        params.setQuery(name);
        params.setFirst(first);
        params.setMax(max);

        return userStore.getUsers(params);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getUsers(UserQueryParams params) {
        return getUsers(params, null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getUsers(UserQueryParams params, @Nullable List<String> orders) {
        handleUserQueryParams(params);

        if (!validateUserQueryParams(params)) {
            return Lists.newArrayList();
        }

        return userStore.getUsers(params, orders);
    }

    @Override
    @Transactional(readOnly = true)
    public int getUserCount(UserQueryParams params) {
        handleUserQueryParams(params);

        if (!validateUserQueryParams(params)) {
            return 0;
        }

        return userStore.getUserCount(params);
    }

    @Override
    @Transactional(readOnly = true)
    public int getUserCount() {
        return userStore.getUserCount();
    }

    private void handleUserQueryParams(UserQueryParams params) {
        boolean canGrantOwnRoles = true; //(Boolean) systemSettingManager.getSystemSetting( SettingKey.CAN_GRANT_OWN_USER_AUTHORITY_GROUPS );
        params.setDisjointRoles(!canGrantOwnRoles);

        if (!params.hasUser()) {
            params.setUser(userService.getUserWithAuthorities().orElse(null));
        }
        //        if ( params.hasUser() && params.getUser().isSuper() )
        //        {
        //            params.setCanManage( false );
        //            params.setAuthSubset( false );
        //            params.setDisjointRoles( false );
        //        }

        //        if ( params.getInactiveMonths() != null )
        //        {
        //            Calendar cal = PeriodType.createCalendarInstance();
        //            cal.add( Calendar.MONTH, (params.getInactiveMonths() * -1) );
        //            params.setInactiveSince( cal.getTime() );
        //        }

        //        if ( params.isUserOrgUnits() && params.hasUser() )
        //        {
        //            params.setOrganisationUnits( Lists.newArrayList( params.getUser().getOrganisationUnits() ) );
        //        }
    }

    private boolean validateUserQueryParams(UserQueryParams params) {
        //        if ( params.isCanManage() && (params.getUser() == null || !params.getUser().hasManagedGroups()) )
        //        {
        //            log.warn( "Cannot get managed users as user does not have any managed groups" );
        //            return false;
        //        }
        //
        //        if ( params.isAuthSubset() && (params.getUser() == null || !params.getUser().getUserCredentials().hasAuthorities()) )
        //        {
        //            log.warn( "Cannot get users with authority subset as user does not have any authorities" );
        //            return false;
        //        }
        //
        //        if ( params.isDisjointRoles() && (params.getUser() == null || !params.getUser().getUserCredentials().hasUserAuthorityGroups()) )
        //        {
        //            log.warn( "Cannot get users with disjoint roles as user does not have any user roles" );
        //            return false;
        //        }

        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getUsersByPhoneNumber(String phoneNumber) {
        UserQueryParams params = new UserQueryParams();
        params.setMobile(phoneNumber);
        return getUsers(params);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isLastSuperUser(Person userCredentials) {
        if (!userCredentials.isSuper()) {
            return false; // Cannot be last if not super user
        }

        Collection<Person> users = personServiceExtended.getAllUsers();

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
        Collection<PersonAuthorityGroup> groups = personAuthorityGroupStore.getAll();

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
            PeopleGroup userGroup = peopleGroupService.getUserGroup(uid);

            if (currentUser.getPerson().canManage(userGroup)) {
                canManageAnyGroup = true;
                break;
            }
        }

        return canManageAnyGroup;
    }

    // -------------------------------------------------------------------------
    // UserAuthorityGroup
    // -------------------------------------------------------------------------

    @Override
    @Transactional
    public Long addUserAuthorityGroup(PersonAuthorityGroup userAuthorityGroup) {
        personAuthorityGroupStore.save(userAuthorityGroup);
        return userAuthorityGroup.getId();
    }

    @Override
    @Transactional
    public void updateUserAuthorityGroup(PersonAuthorityGroup userAuthorityGroup) {
        personAuthorityGroupStore.update(userAuthorityGroup);
    }

    @Override
    @Transactional
    public void deleteUserAuthorityGroup(PersonAuthorityGroup userAuthorityGroup) {
        personAuthorityGroupStore.delete(userAuthorityGroup);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PersonAuthorityGroup> getAllUserAuthorityGroups() {
        return personAuthorityGroupStore.getAll();
    }

    @Override
    @Transactional(readOnly = true)
    public PersonAuthorityGroup getUserAuthorityGroup(Long id) {
        return personAuthorityGroupStore.get(id);
    }

    @Override
    @Transactional(readOnly = true)
    public PersonAuthorityGroup getUserAuthorityGroup(String uid) {
        return personAuthorityGroupStore.getByUid(uid);
    }

    @Override
    @Transactional(readOnly = true)
    public PersonAuthorityGroup getUserAuthorityGroupByName(String name) {
        return personAuthorityGroupStore.getByName(name);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PersonAuthorityGroup> getUserRolesByUid(Collection<String> uids) {
        return personAuthorityGroupStore.getByUid(uids);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PersonAuthorityGroup> getUserRolesBetween(int first, int max) {
        return personAuthorityGroupStore.getAllOrderedName(first, max);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PersonAuthorityGroup> getUserRolesBetweenByName(String name, int first, int max) {
        return personAuthorityGroupStore.getAllLikeName(name, first, max);
    }

    @Override
    @Transactional(readOnly = true)
    public void canIssueFilter(Collection<PersonAuthorityGroup> userRoles) {
        User user = userService.getUserWithAuthorities().orElse(null);

        boolean canGrantOwnUserAuthorityGroups = true; //(Boolean) systemSettingManager.getSystemSetting( SettingKey.CAN_GRANT_OWN_USER_AUTHORITY_GROUPS );

        FilterUtils.filter(userRoles, new UserAuthorityGroupCanIssueFilter(user, canGrantOwnUserAuthorityGroups));
    }

    // -------------------------------------------------------------------------
    // UserCredentials
    // -------------------------------------------------------------------------

    @Override
    @Transactional
    public Long addUserCredentials(Person userCredentials) {
        personServiceExtended.addUser(userCredentials);
        return userCredentials.getId();
    }

    @Override
    @Transactional
    public void updateUserCredentials(Person userCredentials) {
        personServiceExtended.updateUser(userCredentials);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Person> getAllUserCredentials() {
        return personServiceExtended.getAllUsers();
    }

    @Override
    @Transactional(readOnly = true)
    public Person getUserCredentialsByUsername(String username) {
        return personServiceExtended.getUserCredentialsByUsername(username);
    }

    @Override
    @Transactional(readOnly = true)
    public Person getUserCredentialsWithEagerFetchAuthorities(String username) {
        Person userCredentials = personServiceExtended.getUserCredentialsByUsername(username);

        if (userCredentials != null) {
            userCredentials.getAllAuthorities();
        }

        return userCredentials;
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

    @Override
    @Transactional(readOnly = true)
    public int getActiveUsersCount(int days) {
        Calendar cal = PeriodType.createCalendarInstance();
        cal.add(Calendar.DAY_OF_YEAR, (days * -1));

        return getActiveUsersCount(cal.getTime());
    }

    @Override
    @Transactional(readOnly = true)
    public int getActiveUsersCount(Date since) {
        UserQueryParams params = new UserQueryParams();
        params.setLastLogin(since);

        return getUserCount(params);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ErrorReport> validateUser(User user, User currentUser) {
        List<ErrorReport> errors = new ArrayList<>();

        if (currentUser == null || currentUser.getPerson() == null || user == null || user.getPerson() == null) {
            return errors;
        }

        // Validate user role

        boolean canGrantOwnUserAuthorityGroups = true; //(Boolean) systemSettingManager.getSystemSetting( SettingKey.CAN_GRANT_OWN_USER_AUTHORITY_GROUPS );

        List<PersonAuthorityGroup> roles = personAuthorityGroupStore.getByUid(
            user.getPerson().getPersonAuthorityGroups().stream().map(BaseIdentifiableObject::getUid).collect(Collectors.toList())
        );

        roles.forEach(
            ur -> {
                if (!currentUser.getPerson().canIssueUserRole(ur, canGrantOwnUserAuthorityGroups)) {
                    errors.add(new ErrorReport(PersonAuthorityGroup.class, ErrorCode.E3003, currentUser.getLogin(), ur.getName()));
                }
            }
        );

        // Validate user group

        boolean canAdd = currentUser.getPerson().isAuthorized(PeopleGroup.AUTH_USER_ADD);

        if (canAdd) {
            return errors;
        }

        boolean canAddInGroup = currentUser.getPerson().isAuthorized(PeopleGroup.AUTH_USER_ADD_IN_GROUP);

        if (!canAddInGroup) {
            errors.add(new ErrorReport(PeopleGroup.class, ErrorCode.E3004, currentUser));
            return errors;
        }

        user
            .getPerson()
            .getGroups()
            .forEach(
                ug -> {
                    if (!(currentUser.getPerson().canManage(ug) || peopleGroupService.canAddOrRemoveMember(ug.getUid()))) {
                        errors.add(new ErrorReport(PeopleGroup.class, ErrorCode.E3005, currentUser, ug));
                    }
                }
            );

        return errors;
    }
}
