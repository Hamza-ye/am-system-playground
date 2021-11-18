package org.nmcpye.activitiesmanagement;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.vividsolutions.jts.geom.Geometry;
import org.joda.time.DateTime;
import org.nmcpye.activitiesmanagement.domain.Authority;
import org.nmcpye.activitiesmanagement.domain.User;
import org.nmcpye.activitiesmanagement.domain.enumeration.OrganisationUnitType;
import org.nmcpye.activitiesmanagement.domain.organisationunit.OrganisationUnit;
import org.nmcpye.activitiesmanagement.domain.organisationunit.OrganisationUnitGroup;
import org.nmcpye.activitiesmanagement.domain.organisationunit.OrganisationUnitGroupSet;
import org.nmcpye.activitiesmanagement.domain.person.PeopleGroup;
import org.nmcpye.activitiesmanagement.domain.person.Person;
import org.nmcpye.activitiesmanagement.domain.person.PersonAuthorityGroup;
import org.nmcpye.activitiesmanagement.extended.common.CodeGenerator;
import org.nmcpye.activitiesmanagement.extended.person.PersonServiceExt;
import org.nmcpye.activitiesmanagement.extended.user.UserService;
import org.nmcpye.activitiesmanagement.security.AuthoritiesConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.Assert;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Hamza on 02/10/2021.
 */
public abstract class AMConvenienceTest {

    protected static final Logger log = LoggerFactory.getLogger(AMConvenienceTest.class);

    protected static final String BASE_UID = "abcdefghij";

    protected static final String BASE_OU_UID = "ouabcdefgh";

    protected static final String BASE_USER_UID = "userabcdef";

    protected static final String BASE_PERSON_UID = "personabcd";

    protected static final String BASE_USER_GROUP_UID = "ugabcdefgh";

    protected UserService userService;

    protected PersonServiceExt personServiceExt;

    private static Date date;

    static {
        DateTime dateTime = new DateTime(1970, 1, 1, 0, 0);
        date = dateTime.toDate();
    }

    // -------------------------------------------------------------------------
    // Convenience methods
    // -------------------------------------------------------------------------

    /**
     * Creates a date.
     *
     * @param year  the year.
     * @param month the month.
     * @param day   the day of month.
     * @return a date.
     */
    public static Date getDate(int year, int month, int day) {
        DateTime dateTime = new DateTime(year, month, day, 0, 0);
        return dateTime.toDate();
    }

    /**
     * Creates a date.
     *
     * @param s a string representation of a date
     * @return a date.
     */
    public static Date getDate(String s) {
        DateTime dateTime = new DateTime(s);
        return dateTime.toDate();
    }

    /**
     * Creates a date.
     *
     * @param day the day of the year.
     * @return a date.
     */
    public Date getDay(int day) {
        DateTime dataTime = DateTime.now();
        dataTime = dataTime.withTimeAtStartOfDay();
        dataTime = dataTime.withDayOfYear(day);

        return dataTime.toDate();
    }

    /**
     * Compares two collections for equality. This method does not check for the
     * implementation type of the collection in contrast to the native equals
     * method. This is useful for black-box testing where one will not know the
     * implementation type of the returned collection for a method.
     *
     * @param actual    the actual collection to check.
     * @param reference the reference objects to check against.
     * @return true if the collections are equal, false otherwise.
     */
    public static boolean equals(Collection<?> actual, Object... reference) {
        final Collection<Object> collection = new HashSet<>();

        Collections.addAll(collection, reference);

        if (actual == collection) {
            return true;
        }

        if (actual == null) {
            return false;
        }

        if (actual.size() != collection.size()) {
            log.warn("Actual collection has different size compared to reference collection: " + actual.size() + " / " + collection.size());
            return false;
        }

        for (Object object : actual) {
            if (!collection.contains(object)) {
                log.warn("Object in actual collection not part of reference collection: " + object);
                return false;
            }
        }

        for (Object object : collection) {
            if (!actual.contains(object)) {
                log.warn("Object in reference collection not part of actual collection: " + object);
                return false;
            }
        }

        return true;
    }

    public static String message(Object expected) {
        return "Expected was: " + ((expected != null) ? "[" + expected.toString() + "]" : "[null]");
    }

    public static String message(Object expected, Object actual) {
        return message(expected) + " Actual was: " + ((actual != null) ? "[" + actual.toString() + "]" : "[null]");
    }

    /**
     * Creates a user and injects into the security context with username
     * "username". Requires <code>identifiableObjectManager</code> and
     * <code>userService</code> to be injected into the test.
     *
     * @param allAuth whether to grant ALL authority to user.
     * @param auths   authorities to grant to user.
     * @return the user.
     */
    protected User createUserAndInjectSecurityContext(boolean allAuth, String... auths) {
        return createUserAndInjectSecurityContext(null, allAuth, auths);
    }

    /**
     * Creates a user and injects into the security context with username
     * "username". Requires <code>identifiableObjectManager</code> and
     * <code>userService</code> to be injected into the test.
     *
     * @param organisationUnits the organisation units of the user.
     * @param allAuth           whether to grant the ALL authority to user.
     * @param auths             authorities to grant to user.
     * @return the user.
     */
    protected User createUserAndInjectSecurityContext(Set<OrganisationUnit> organisationUnits, boolean allAuth, String... auths) {
        return createUserAndInjectSecurityContext(organisationUnits, null, allAuth, auths);
    }

    /**
     * Creates a user and injects into the security context with username
     * "username". Requires <code>identifiableObjectManager</code> and
     * <code>userService</code> to be injected into the test.
     *
     * @param organisationUnits         the organisation units of the user.
     * @param dataViewOrganisationUnits the data view organisation units of the user.
     * @param allAuth                   whether to grant the ALL authority to the user.
     * @param auths                     authorities to grant to the user.
     * @return the user.
     */
    protected User createUserAndInjectSecurityContext(
        Set<OrganisationUnit> organisationUnits,
        Set<OrganisationUnit> dataViewOrganisationUnits,
        boolean allAuth,
        String... auths
    ) {
        Assert.notNull(userService, "UserService must be injected in test");

        Set<String> authorities = new HashSet<>();

        if (allAuth) {
            authorities.add(PersonAuthorityGroup.AUTHORITY_ALL);
        }

        if (auths != null) {
            authorities.addAll(Lists.newArrayList(auths));
        }

        PersonAuthorityGroup userAuthorityGroup = new PersonAuthorityGroup();
        userAuthorityGroup.setName("Superuser");

        userAuthorityGroup.setAuthoritiesString(Sets.newHashSet(auths));

        userService.addUserAuthorityGroup(userAuthorityGroup);

        User user = createUser('A');

        if (organisationUnits != null) {
            user.getPerson().setOrganisationUnits(organisationUnits);
        }

        if (dataViewOrganisationUnits != null) {
            user.getPerson().setDataViewOrganisationUnits(dataViewOrganisationUnits);
        }

        user.getPerson().getPersonAuthorityGroups().add(userAuthorityGroup);
        userService.addUser(user);
        user.getPerson().setUserInfo(user);
        userService.addUserCredentials(user.getPerson());

        Set<GrantedAuthority> grantedAuths = authorities.stream().map(a -> new SimpleGrantedAuthority(a)).collect(Collectors.toSet());

        grantedAuths.addAll(
            user.getAuthorities().stream().map(Authority::getName).map(SimpleGrantedAuthority::new).collect(Collectors.toSet())
        );

        UserDetails userDetails = new org.springframework.security.core.userdetails.User(user.getLogin(), user.getPassword(), grantedAuths);

        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, "", grantedAuths);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return user;
    }

    protected void saveAndInjectUserSecurityContext(User user) {
        userService.addUser(user);
        userService.addUserCredentials(user.getPerson());

        List<GrantedAuthority> grantedAuthorities = user
            .getPerson()
            .getAllAuthorities()
            .stream()
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toList());

        grantedAuthorities.addAll(
            user.getAuthorities().stream().map(Authority::getName).map(SimpleGrantedAuthority::new).collect(Collectors.toSet())
        );

        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
            user.getLogin(),
            user.getPassword(),
            grantedAuthorities
        );

        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, "", grantedAuthorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    //////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////

    public static User createUser(char uniqueCharacter) {
        return createUser(uniqueCharacter, Lists.newArrayList());
    }

    public static User createUser(char uniqueCharacter, List<String> auths) {
        Person credentials = new Person();
        User user = new User();
        user.setUid(BASE_USER_UID + uniqueCharacter);

        credentials.setUserInfo(user);
        credentials.setUser(user);
        user.setPerson(credentials);
        user.setEmail(uniqueCharacter + "nmcpye@nmcpye.org");
        user.setLogin("username" + uniqueCharacter);

        credentials.setLogin("username" + uniqueCharacter);
        user.setPassword("password" + uniqueCharacter);

        if (auths != null && !auths.isEmpty()) {
            PersonAuthorityGroup role = new PersonAuthorityGroup();
            role.setAuthoritiesString(Sets.newHashSet(auths));
            credentials.getPersonAuthorityGroups().add(role);
        }

        user.setFirstName("FirstName" + uniqueCharacter);
        user.setSurname("Surname" + uniqueCharacter);
        //        user.setEmail( "Email" + uniqueCharacter );
        user.setMobile("PhoneNumber" + uniqueCharacter);
        user.setCode("UserCode" + uniqueCharacter);
        user.setAutoFields();

        return user;
    }

    protected User createUser(String username, String... authorities) {
        Assert.notNull(userService, "UserService must be injected in test");

        String password = "district";

        PersonAuthorityGroup userAuthorityGroup = new PersonAuthorityGroup();
        userAuthorityGroup.setCode(username);
        userAuthorityGroup.setName(username);
        userAuthorityGroup.setDescription(username);
        userAuthorityGroup.setAuthoritiesString(Sets.newHashSet(authorities));

        personServiceExt.addUserAuthorityGroup(userAuthorityGroup);

        User user = new User();
        user.setCode(username);
        user.setFirstName(username);
        user.setSurname(username);

        Set<String> auths = Sets.newHashSet(authorities);
        Set<Authority> authoritySet = auths.stream().map(Authority::new).collect(Collectors.toSet());

        user.setAuthorities(authoritySet);
        userService.addUser(user);

        Person userCredentials = new Person();
        userCredentials.setCode(username);
        userCredentials.setUser(user);
        userCredentials.setUserInfo(user);
        userCredentials.setLogin(username);
        userCredentials.getPersonAuthorityGroups().add(userAuthorityGroup);

        userService.addUserCredentials(userCredentials);

        user.setPerson(userCredentials);
        userService.updateUser(user);

        return user;
    }

    protected User createAdminUser(String... authorities) {
        Assert.notNull(userService, "UserService must be injected in test");

        String username = "admin";
        String password = "district";

        PersonAuthorityGroup userAuthorityGroup = new PersonAuthorityGroup();
        userAuthorityGroup.setUid("yrB6vc5Ip3r");
        userAuthorityGroup.setCode("Superuser");
        userAuthorityGroup.setName("Superuser");
        userAuthorityGroup.setDescription("Superuser");

        userAuthorityGroup.setAuthoritiesString(Sets.newHashSet(authorities));

        userService.addUserAuthorityGroup(userAuthorityGroup);

        User user = new User();
        user.setUid("M5zQapPyTZI");
        user.setCode(username);
        user.setFirstName(username);
        user.setSurname(username);

        Set<String> auths = Sets.newHashSet(authorities);
        Set<Authority> authoritySet = auths.stream().map(Authority::new).collect(Collectors.toSet());

        user.setAuthorities(authoritySet);
        user.setPassword(password);

        userService.addUser(user);

        Person userCredentials = new Person();
        userCredentials.setUid("KvMx6c1eoYo");
        userCredentials.setUuid(UUID.fromString("6507f586-f154-4ec1-a25e-d7aa51de5216"));
        userCredentials.setCode(username);
        userCredentials.setUser(user);
        userCredentials.setUserInfo(user);
        userCredentials.setLogin(username);
        userCredentials.getPersonAuthorityGroups().add(userAuthorityGroup);

        //        userService.encodeAndSetPassword( userCredentials, password );
        userService.addUserCredentials(userCredentials);

        user.setPerson(userCredentials);
        userService.updateUser(user);

        return user;
    }

    protected User createAndInjectAdminUser() {
        return createAndInjectAdminUser(AuthoritiesConstants.ADMIN);
    }

    protected User createAndInjectAdminUser(String... authorities) {
        User user = createAdminUser(authorities);

        List<GrantedAuthority> grantedAuthorities = user
            .getAuthorities()
            .stream()
            .map(Authority::getName)
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toList());

        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
            user.getLogin(),
            user.getPassword(),
            grantedAuthorities
        );

        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, "", grantedAuthorities);

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);

        return user;
    }

    protected void injectSecurityContext(User user) {
        List<GrantedAuthority> grantedAuthorities = user
            .getAuthorities()
            .stream()
            .map(Authority::getName)
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toList());

        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
            user.getLogin(),
            user.getPassword(),
            grantedAuthorities
        );

        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, "", grantedAuthorities);

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }

    protected void clearSecurityContext() {
        if (SecurityContextHolder.getContext() != null) {
            SecurityContextHolder.getContext().setAuthentication(null);
        }

        SecurityContextHolder.clearContext();
    }

    public static Person createUserCredentials(char uniqueCharacter, User user) {
        Person credentials = new Person();
        credentials.setName("Name" + uniqueCharacter);
        credentials.setUid(BASE_PERSON_UID + uniqueCharacter);
        credentials.setLogin("login" + uniqueCharacter);
        // TODO Delete Mobile and make available only in User
        credentials.setMobile("Mobile" + uniqueCharacter);
        credentials.setCode("PersonCode" + uniqueCharacter);
        credentials.setAutoFields();

        credentials.setUserInfo(user);
        if (user != null) {
            user.setPerson(credentials);
        }

        return credentials;
    }

    public static PeopleGroup createUserGroup(char uniqueCharacter, Set<Person> users) {
        PeopleGroup userGroup = new PeopleGroup();
        userGroup.setAutoFields();

        userGroup.setUid(BASE_USER_GROUP_UID + uniqueCharacter);
        userGroup.setCode("UserGroupCode" + uniqueCharacter);
        userGroup.setName("UserGroup" + uniqueCharacter);
        userGroup.setMembers(users);

        return userGroup;
    }

    public static PersonAuthorityGroup createUserAuthorityGroup(char uniqueCharacter) {
        return createUserAuthorityGroup(uniqueCharacter, new String[] {});
    }

    public static PersonAuthorityGroup createUserAuthorityGroup(char uniqueCharacter, String... auths) {
        PersonAuthorityGroup role = new PersonAuthorityGroup();
        role.setAutoFields();

        role.setUid(BASE_UID + uniqueCharacter);
        role.setName("UserAuthorityGroup" + uniqueCharacter);

        role.setAuthoritiesString(Sets.newHashSet(auths));

        return role;
    }

    //////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////

    /**
     * @param uniqueCharacter A unique character to identify the object.
     */
    public static OrganisationUnit createOrganisationUnit(char uniqueCharacter) {
        OrganisationUnit unit = new OrganisationUnit();
        unit.setAutoFields();

        unit.setUid(BASE_OU_UID + uniqueCharacter);
        unit.setOrganisationUnitType(OrganisationUnitType.DISTRICT);
        unit.setName("OrganisationUnit" + uniqueCharacter);
        unit.setShortName("OrganisationUnitShort" + uniqueCharacter);
        unit.setCode("OrganisationUnitCode" + uniqueCharacter);
        unit.setOpeningDate(date);
        unit.setComment("Comment" + uniqueCharacter);

        return unit;
    }

    public static OrganisationUnit createOrganisationUnit(char uniqueCharacter, Geometry geometry) {
        OrganisationUnit unit = createOrganisationUnit(uniqueCharacter);

        unit.setGeometry(geometry);

        return unit;
    }

    /**
     * @param uniqueCharacter A unique character to identify the object.
     * @param parent          The parent.
     */
    public static OrganisationUnit createOrganisationUnit(char uniqueCharacter, OrganisationUnit parent) {
        OrganisationUnit unit = createOrganisationUnit(uniqueCharacter);

        unit.setParent(parent);
        parent.getChildren().add(unit);

        return unit;
    }

    /**
     * @param name The name, short name and code of the organisation unit.
     */
    public static OrganisationUnit createOrganisationUnit(String name) {
        OrganisationUnit unit = new OrganisationUnit();
        unit.setAutoFields();

        unit.setUid(CodeGenerator.generateUid());
        unit.setName(name);
        unit.setShortName(name);
        unit.setCode(name);
        unit.setOpeningDate(date);
        unit.setComment("Comment " + name);

        return unit;
    }

    /**
     * @param name   The name, short name and code of the organisation unit.
     * @param parent The parent.
     */
    public static OrganisationUnit createOrganisationUnit(String name, OrganisationUnit parent) {
        OrganisationUnit unit = createOrganisationUnit(name);

        unit.setParent(parent);
        parent.getChildren().add(unit);

        return unit;
    }

    /**
     * @param uniqueCharacter A unique character to identify the object.
     */
    public static OrganisationUnitGroup createOrganisationUnitGroup(char uniqueCharacter) {
        OrganisationUnitGroup group = new OrganisationUnitGroup();
        group.setAutoFields();

        group.setUid(BASE_UID + uniqueCharacter);
        group.setName("OrganisationUnitGroup" + uniqueCharacter);
        group.setShortName("OrganisationUnitGroupShort" + uniqueCharacter);
        group.setCode("OrganisationUnitGroupCode" + uniqueCharacter);

        return group;
    }

    /**
     * @param uniqueCharacter A unique character to identify the object.
     */
    public static OrganisationUnitGroupSet createOrganisationUnitGroupSet(char uniqueCharacter) {
        OrganisationUnitGroupSet groupSet = new OrganisationUnitGroupSet();
        groupSet.setAutoFields();

        groupSet.setName("OrganisationUnitGroupSet" + uniqueCharacter);
        groupSet.setDescription("Description" + uniqueCharacter);
        groupSet.setCompulsory(true);

        return groupSet;
    }
}
