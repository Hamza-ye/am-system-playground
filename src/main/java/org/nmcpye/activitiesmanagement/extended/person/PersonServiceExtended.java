package org.nmcpye.activitiesmanagement.extended.person;

import java.util.Collection;
import java.util.List;
import java.util.UUID;
import javax.annotation.Nullable;
import org.nmcpye.activitiesmanagement.domain.User;
import org.nmcpye.activitiesmanagement.domain.person.Person;
import org.nmcpye.activitiesmanagement.domain.person.PersonAuthorityGroup;

public interface PersonServiceExtended {
    String ID = PersonServiceExtended.class.getName();
    String PW_NO_INTERNAL_LOGIN = "--[##no_internal_login##]--";

    // -------------------------------------------------------------------------
    // User
    // -------------------------------------------------------------------------

    /**
     * Adds a User.
     *
     * @param user the User to add.
     * @return the generated identifier.
     */
    Long addUser(Person user);

    /**
     * Updates a User.
     *
     * @param user the User to update.
     */
    void updateUser(Person user);

    /**
     * Retrieves the User with the given identifier.
     *
     * @param id the identifier of the User to retrieve.
     * @return the User.
     */
    Person getUser(Long id);

    /**
     * Retrieves the User with the given unique identifier.
     *
     * @param uid the identifier of the User to retrieve.
     * @return the User.
     */
    Person getUser(String uid);

    /**
     * Retrieves the User with the given UUID.
     *
     * @param uuid the UUID of the User to retrieve.
     * @return the User.
     */
    Person getUserByUuid(UUID uuid);

    /**
     * Retrieves the User with the given username.
     *
     * @param username the username of the User to retrieve.
     * @return the User.
     */
    Person getUserByUsername(String username);

    /**
     * Retrieves the User by attempting to look up by various identifiers
     * in the following order:
     *
     * <ul>
     * <li>UID</li>
     * <li>UUID</li>
     * <li>Username</li>
     * </ul>
     *
     * @param id the User identifier.
     * @return the User, or null if not found.
     */
    Person getUserByIdentifier(String id);

    /**
     * Retrieves a collection of User with the given unique identifiers.
     *
     * @param uids the identifiers of the collection of Users to retrieve.
     * @return the User.
     */
    List<Person> getUsers(Collection<String> uids);

    /**
     * Returns a List of all Users.
     *
     * @return a Collection of Users.
     */
    List<Person> getAllUsers();

    /**
     * Retrieves all Users with first name, surname or user name like the given
     * name.
     *
     * @param name  the name.
     * @param first the first item to return.
     * @param max   the max number of item to return.
     * @return a list of Users.
     */
    List<Person> getAllUsersBetweenByName(String name, int first, int max);

    /**
     * Deletes a User.
     *
     * @param user the User to delete.
     */
    void deleteUser(Person user);

    /**
     * Checks if the given user represents the last user with ALL authority.
     *
     * @param userCredentials the user.
     * @return true if the given user represents the last user with ALL authority.
     */
    boolean isLastSuperUser(Person userCredentials);

    /**
     * Checks if the given user role represents the last role with ALL authority.
     *
     * @param userAuthorityGroup the user role.
     * @return true if the given user role represents the last role with ALL authority.
     */
    boolean isLastSuperRole(PersonAuthorityGroup userAuthorityGroup);

    /**
     * Returns a list of users based on the given query parameters.
     * The default order of last name and first name will be applied.
     *
     * @param params the user query parameters.
     * @return a List of users.
     */
    List<Person> getUsers(PersonQueryParams params);

    /**
     * Returns a list of users based on the given query parameters.
     * If the specified list of orders are empty, default order of
     * last name and first name will be applied.
     *
     * @param params the user query parameters.
     * @param orders the already validated order strings (e.g. email:asc).
     * @return a List of users.
     */
    List<Person> getUsers(PersonQueryParams params, @Nullable List<String> orders);

    /**
     * Returns the number of users based on the given query parameters.
     *
     * @param params the user query parameters.
     * @return number of users.
     */
    int getUserCount(PersonQueryParams params);

    /**
     * Returns number of all users
     *
     * @return number of users
     */
    int getUserCount();

    List<Person> getUsersByPhoneNumber(String phoneNumber);

    /**
     * Tests whether the current user is allowed to create a user associated
     * with the given user group identifiers. Returns true if current user has
     * the F_USER_ADD authority. Returns true if the current user has the
     * F_USER_ADD_WITHIN_MANAGED_GROUP authority and can manage any of the given
     * user groups. Returns false otherwise.
     *
     * @param userGroups the user group identifiers.
     * @return true if the current user can create user, false if not.
     */
    boolean canAddOrUpdateUser(Collection<String> userGroups);

    boolean canAddOrUpdateUser(Collection<String> userGroups, User currentUser);

    // -------------------------------------------------------------------------
    // UserCredentials
    // -------------------------------------------------------------------------

    /**
     * Adds a UserCredentials.
     *
     * @param userCredentials the UserCredentials to add.
     * @return the User which the UserCredentials is associated with.
     */
    long addUserCredentials(Person userCredentials);

    /**
     * Updates a UserCredentials.
     *
     * @param userCredentials the UserCredentials to update.
     */
    void updateUserCredentials(Person userCredentials);

    /**
     * Retrieves the UserCredentials associated with the User with the given
     * name.
     *
     * @param username the name of the User.
     * @return the UserCredentials.
     */
    Person getUserCredentialsByUsername(String username);

    Person getUserCredentialsWithEagerFetchAuthorities(String username);

    /**
     * Retrieves all UserCredentials.
     *
     * @return a List of UserCredentials.
     */
    List<Person> getAllUserCredentials();

    //    /**
    //     * Encodes and sets the password of the User.
    //     * Due to business logic required on password updates the password for a user
    //     * should only be changed using this method or {@link #encodeAndSetPassword(Person, String) encodeAndSetPassword}
    //     * and not directly on the User or UserCredentials object.
    //     * <p>
    //     * Note that the changes made to the User object are not persisted.
    //     *
    //     * @param user        the User.
    //     * @param rawPassword the raw password.
    //     */
    //    void encodeAndSetPassword(User user, String rawPassword);

    //    /**
    //     * Encodes and sets the password of the UserCredentials.
    //     * Due to business logic required on password updates the password for a user
    //     * should only be changed using this method or {@link #encodeAndSetPassword(User, String) encodeAndSetPassword}
    //     * and not directly on the User or UserCredentials object.
    //     * <p>
    //     * Note that the changes made to the UserCredentials object are not persisted.
    //     *
    //     * @param userCredentials the UserCredentials.
    //     * @param rawPassword     the raw password.
    //     */
    //    void encodeAndSetPassword(Person userCredentials, String rawPassword);

    /**
     * Updates the last login date of UserCredentials with the given username
     * with the current date.
     *
     * @param username the username of the UserCredentials.
     */
    void setLastLogin(String username);
    //
    //    int getActiveUsersCount(int days);
    //
    //    int getActiveUsersCount(Date since);

    //    boolean credentialsNonExpired(Person credentials);

    // -------------------------------------------------------------------------
    // UserAuthorityGroup
    // -------------------------------------------------------------------------

    /**
     * Adds a UserAuthorityGroup.
     *
     * @param userAuthorityGroup the UserAuthorityGroup.
     * @return the generated identifier.
     */
    long addUserAuthorityGroup(PersonAuthorityGroup userAuthorityGroup);

    /**
     * Updates a UserAuthorityGroup.
     *
     * @param userAuthorityGroup the UserAuthorityGroup.
     */
    void updateUserAuthorityGroup(PersonAuthorityGroup userAuthorityGroup);

    /**
     * Retrieves the UserAuthorityGroup with the given identifier.
     *
     * @param id the identifier of the UserAuthorityGroup to retrieve.
     * @return the UserAuthorityGroup.
     */
    PersonAuthorityGroup getUserAuthorityGroup(long id);

    /**
     * Retrieves the UserAuthorityGroup with the given identifier.
     *
     * @param uid the identifier of the UserAuthorityGroup to retrieve.
     * @return the UserAuthorityGroup.
     */
    PersonAuthorityGroup getUserAuthorityGroup(String uid);

    /**
     * Retrieves the UserAuthorityGroup with the given name.
     *
     * @param name the name of the UserAuthorityGroup to retrieve.
     * @return the UserAuthorityGroup.
     */
    PersonAuthorityGroup getUserAuthorityGroupByName(String name);

    /**
     * Deletes a UserAuthorityGroup.
     *
     * @param userAuthorityGroup the UserAuthorityGroup to delete.
     */
    void deleteUserAuthorityGroup(PersonAuthorityGroup userAuthorityGroup);

    /**
     * Retrieves all UserAuthorityGroups.
     *
     * @return a List of UserAuthorityGroups.
     */
    List<PersonAuthorityGroup> getAllUserAuthorityGroups();

    /**
     * Retrieves UserAuthorityGroups with the given UIDs.
     *
     * @param uids the UIDs.
     * @return a List of UserAuthorityGroups.
     */
    List<PersonAuthorityGroup> getUserRolesByUid(Collection<String> uids);

    /**
     * Retrieves all UserAuthorityGroups.
     *
     * @return a List of UserAuthorityGroups.
     */
    List<PersonAuthorityGroup> getUserRolesBetween(int first, int max);

    /**
     * Retrieves all UserAuthorityGroups.
     *
     * @return a List of UserAuthorityGroups.
     */
    List<PersonAuthorityGroup> getUserRolesBetweenByName(String name, int first, int max);

    /**
     * Filters the given collection of user roles based on whether the current user
     * is allowed to issue it.
     *
     * @param userRoles the collection of user roles.
     */
    void canIssueFilter(Collection<PersonAuthorityGroup> userRoles);
    //    List<ErrorReport> validateUser(User user, User currentUser);

    //    /**
    //     * Returns list of active users whose credentials are expiring with in few days.
    //     *
    //     * @return list of active users whose credentials are expiring with in few days.
    //     */
    //    List<User> getExpiringUsers();
    //
    //    void set2FA(User user, Boolean twoFA);

    //    /**
    //     * Expire a user's active sessions retrieved from the Spring security's
    //     * org.springframework.security.core.session.SessionRegistry
    //     *
    //     * @param credentials the user credentials
    //     */
    //    void expireActiveSessions(Person credentials);
}
