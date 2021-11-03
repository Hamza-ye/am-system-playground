package org.nmcpye.activitiesmanagement.extended.person;

import com.google.common.collect.Sets;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.nmcpye.activitiesmanagement.AMTest;
import org.nmcpye.activitiesmanagement.IntegrationTest;
import org.nmcpye.activitiesmanagement.domain.Authority;
import org.nmcpye.activitiesmanagement.domain.User;
import org.nmcpye.activitiesmanagement.domain.organisationunit.OrganisationUnit;
import org.nmcpye.activitiesmanagement.domain.person.PeopleGroup;
import org.nmcpye.activitiesmanagement.domain.person.Person;
import org.nmcpye.activitiesmanagement.domain.person.PersonAuthorityGroup;
import org.nmcpye.activitiesmanagement.extended.organisationunit.OrganisationUnitService;
import org.nmcpye.activitiesmanagement.extended.user.UserService;
import org.nmcpye.activitiesmanagement.repository.AuthorityRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@IntegrationTest
public class PersonServiceTest extends AMTest {

    @Autowired
    private UserService userService;

    @Autowired
    private PersonServiceExtended personServiceExtended;

    @Autowired
    private PeopleGroupService peopleGroupService;

    @Autowired
    private OrganisationUnitService organisationUnitService;

    @Autowired
    AuthorityRepository authorityRepository;

    private OrganisationUnit unitA;
    private OrganisationUnit unitB;
    private OrganisationUnit unitC;
    private OrganisationUnit unitD;
    private OrganisationUnit unitE;

    private PersonAuthorityGroup roleA;
    private PersonAuthorityGroup roleB;
    private PersonAuthorityGroup roleC;

    @Override
    public void setUpTest() throws Exception {
        unitA = createOrganisationUnit('A');
        unitB = createOrganisationUnit('B');
        unitC = createOrganisationUnit('C', unitA);
        unitD = createOrganisationUnit('D', unitB);
        unitE = createOrganisationUnit('E');

        organisationUnitService.addOrganisationUnit(unitA);
        organisationUnitService.addOrganisationUnit(unitB);
        organisationUnitService.addOrganisationUnit(unitC);
        organisationUnitService.addOrganisationUnit(unitD);
        organisationUnitService.addOrganisationUnit(unitE);

        organisationUnitService.forceUpdatePaths();

        roleA = createUserAuthorityGroup('A');
        roleB = createUserAuthorityGroup('B');
        roleC = createUserAuthorityGroup('C');

        Authority authA = new Authority();
        authA.setName("AuthA");
        Authority authB = new Authority();
        authB.setName("AuthB");
        Authority authC = new Authority();
        authC.setName("AuthC");
        Authority authD = new Authority();
        authD.setName("AuthD");

        roleA.getAuthorities().add(authA);
        roleA.getAuthorities().add(authB);
        roleA.getAuthorities().add(authC);
        roleA.getAuthorities().add(authD);

        roleB.getAuthorities().add(authA);
        roleB.getAuthorities().add(authB);

        roleC.getAuthorities().add(authC);

        //        authorityRepository.save(authA);
        //        authorityRepository.save(authB);
        //        authorityRepository.save(authC);
        //        authorityRepository.save(authD);

        personServiceExtended.addUserAuthorityGroup(roleA);
        personServiceExtended.addUserAuthorityGroup(roleB);
        personServiceExtended.addUserAuthorityGroup(roleC);
    }

    @Test
    public void testAddGetUser() {
        Set<OrganisationUnit> units = new HashSet<>();

        units.add(unitA);
        units.add(unitB);

        User userA = createUser('A');
        User userB = createUser('B');

        Person personA = createUserCredentials('A', userA);
        Person personB = createUserCredentials('B', userB);

        personA.setOrganisationUnits(units);
        personB.setOrganisationUnits(units);

        Long idA = personServiceExtended.addUser(personA);
        Long idB = personServiceExtended.addUser(personB);

        assertEquals(personA, personServiceExtended.getUser(idA));
        assertEquals(personB, personServiceExtended.getUser(idB));

        assertEquals(units, personServiceExtended.getUser(idA).getOrganisationUnits());
        assertEquals(units, personServiceExtended.getUser(idB).getOrganisationUnits());
    }

    @Test
    public void testUpdateUser() {
        User userA = createUser('A');
        User userB = createUser('B');

        Person personA = createUserCredentials('A', userA);
        Person personB = createUserCredentials('B', userB);

        Long idA = personServiceExtended.addUser(personA);
        Long idB = personServiceExtended.addUser(personB);

        assertEquals(personA, personServiceExtended.getUser(idA));
        assertEquals(personB, personServiceExtended.getUser(idB));

        personA.setLogin("UpdatedLoginA");

        personServiceExtended.updateUser(personA);

        assertEquals(personServiceExtended.getUser(idA).getLogin(), "UpdatedLoginA");
    }

    @Test
    public void testDeleteUser() {
        User userA = createUser('A');
        User userB = createUser('B');

        Person personA = createUserCredentials('A', userA);
        Person personB = createUserCredentials('B', userB);

        Long idA = personServiceExtended.addUser(personA);
        Long idB = personServiceExtended.addUser(personB);

        assertEquals(personA, personServiceExtended.getUser(idA));
        assertEquals(personB, personServiceExtended.getUser(idB));

        personServiceExtended.deleteUser(personA);

        assertNull(personServiceExtended.getUser(idA));
        assertNotNull(personServiceExtended.getUser(idB));
    }

    @Test
    public void testUserByOrgUnits() {
        User userA = createUser('A');
        User userB = createUser('B');
        User userC = createUser('C');
        User userD = createUser('D');

        Person personA = createUserCredentials('A', userA);
        Person personB = createUserCredentials('B', userB);
        Person personC = createUserCredentials('C', userC);
        Person personD = createUserCredentials('D', userD);

        personA.getOrganisationUnits().add(unitA);
        personB.getOrganisationUnits().add(unitB);
        personC.getOrganisationUnits().add(unitC);
        personD.getOrganisationUnits().add(unitD);

        userService.addUser(userA);
        userService.addUser(userB);
        userService.addUser(userC);
        userService.addUser(userD);

        personServiceExtended.addUser(personA);
        personServiceExtended.addUser(personB);
        personServiceExtended.addUser(personC);
        personServiceExtended.addUser(personD);

        PersonQueryParams params = new PersonQueryParams().addOrganisationUnit(unitA).setPerson(personA);

        List<Person> users = personServiceExtended.getUsers(params);

        assertEquals(1, users.size());
        assertTrue(users.contains(personA));

        params = new PersonQueryParams().addOrganisationUnit(unitA).setIncludeOrgUnitChildren(true).setPerson(personA);

        users = personServiceExtended.getUsers(params);

        assertEquals(2, users.size());
        assertTrue(users.contains(personA));
        assertTrue(users.contains(personC));
    }

    @Test
    public void testUserByUserGroups() {
        User userA = createUser('A');
        User userB = createUser('B');
        User userC = createUser('C');
        User userD = createUser('D');

        Person personA = createUserCredentials('A', userA);
        Person personB = createUserCredentials('B', userB);
        Person personC = createUserCredentials('C', userC);
        Person personD = createUserCredentials('D', userD);

        userService.addUser(userA);
        userService.addUser(userB);
        userService.addUser(userC);
        userService.addUser(userD);

        personServiceExtended.addUser(personA);
        personServiceExtended.addUser(personB);
        personServiceExtended.addUser(personC);
        personServiceExtended.addUser(personD);

        PeopleGroup ugA = createUserGroup('A', Sets.newHashSet(personA, personB));
        PeopleGroup ugB = createUserGroup('B', Sets.newHashSet(personB, personC));
        PeopleGroup ugC = createUserGroup('C', Sets.newHashSet(personD));

        peopleGroupService.addUserGroup(ugA);
        peopleGroupService.addUserGroup(ugB);
        peopleGroupService.addUserGroup(ugC);

        List<Person> users = personServiceExtended.getUsers(new PersonQueryParams().setPeopleGroups(Sets.newHashSet(ugA)));

        assertEquals(2, users.size());
        assertTrue(users.contains(personA));
        assertTrue(users.contains(personB));

        users = personServiceExtended.getUsers(new PersonQueryParams().setPeopleGroups(Sets.newHashSet(ugA, ugB)));

        assertEquals(3, users.size());
        assertTrue(users.contains(personA));
        assertTrue(users.contains(personB));
        assertTrue(users.contains(personC));

        users = personServiceExtended.getUsers(new PersonQueryParams().setPeopleGroups(Sets.newHashSet(ugA, ugC)));

        assertEquals(3, users.size());
        assertTrue(users.contains(personA));
        assertTrue(users.contains(personB));
        assertTrue(users.contains(personD));
    }

    @Test
    public void testGetUserOrgUnits() {
        User currentUser = createUser('Z');
        Person currentPerson = createUserCredentials('Z', currentUser);

        User userA = createUser('A');
        User userB = createUser('B');
        User userC = createUser('C');
        User userD = createUser('D');
        User userE = createUser('E');

        currentPerson.getOrganisationUnits().add(unitA);
        currentPerson.getOrganisationUnits().add(unitB);

        Person credentialsA = createUserCredentials('A', userA);
        Person credentialsB = createUserCredentials('B', userB);
        Person credentialsC = createUserCredentials('C', userC);
        Person credentialsD = createUserCredentials('D', userD);
        Person credentialsE = createUserCredentials('E', userE);

        credentialsA.addOrganisationUnit(unitA);
        credentialsB.addOrganisationUnit(unitB);
        credentialsC.addOrganisationUnit(unitC);
        credentialsD.addOrganisationUnit(unitD);
        credentialsE.addOrganisationUnit(unitE);

        userService.addUser(currentUser);
        userService.addUser(userA);
        userService.addUser(userB);
        userService.addUser(userC);
        userService.addUser(userD);
        userService.addUser(userE);

        personServiceExtended.addUser(currentPerson);
        personServiceExtended.addUser(credentialsA);
        personServiceExtended.addUser(credentialsB);
        personServiceExtended.addUser(credentialsC);
        personServiceExtended.addUser(credentialsD);
        personServiceExtended.addUser(credentialsE);

        PersonQueryParams params = new PersonQueryParams().setPerson(currentPerson).setUserOrgUnits(true);

        List<Person> users = personServiceExtended.getUsers(params);

        assertEquals(3, users.size());
        assertTrue(users.contains(currentPerson));
        assertTrue(users.contains(credentialsA));
        assertTrue(users.contains(credentialsB));

        params = new PersonQueryParams().setPerson(currentPerson).setUserOrgUnits(true).setIncludeOrgUnitChildren(true);

        users = personServiceExtended.getUsers(params);

        assertEquals(5, users.size());
        assertTrue(users.contains(currentPerson));
        assertTrue(users.contains(credentialsA));
        assertTrue(users.contains(credentialsB));
        assertTrue(users.contains(credentialsC));
        assertTrue(users.contains(credentialsD));
    }

    @Test
    public void testManagedGroups() {
        // TODO find way to override in parameters

        User userA = createUser('A');
        User userB = createUser('B');
        User userC = createUser('C');
        User userD = createUser('D');

        Person personA = createUserCredentials('A', userA);
        Person personB = createUserCredentials('B', userB);
        Person personC = createUserCredentials('C', userC);
        Person personD = createUserCredentials('D', userD);

        userService.addUser(userA);
        userService.addUser(userB);
        userService.addUser(userC);
        userService.addUser(userD);

        personServiceExtended.addUser(personA);
        personServiceExtended.addUser(personB);
        personServiceExtended.addUser(personC);
        personServiceExtended.addUser(personD);

        PeopleGroup userGroup1 = createUserGroup('A', Sets.newHashSet(personA, personB));
        PeopleGroup userGroup2 = createUserGroup('B', Sets.newHashSet(personC, personD));

        personA.getGroups().add(userGroup1);
        personB.getGroups().add(userGroup1);
        personC.getGroups().add(userGroup2);
        personD.getGroups().add(userGroup2);

        userGroup1.setManagedGroups(Sets.newHashSet(userGroup2));
        userGroup2.setManagedByGroups(Sets.newHashSet(userGroup1));

        long group1 = peopleGroupService.addUserGroup(userGroup1);
        long group2 = peopleGroupService.addUserGroup(userGroup2);

        assertEquals(1, peopleGroupService.getUserGroup(group1).getManagedGroups().size());
        assertTrue(peopleGroupService.getUserGroup(group1).getManagedGroups().contains(userGroup2));
        assertEquals(1, peopleGroupService.getUserGroup(group2).getManagedByGroups().size());
        assertTrue(peopleGroupService.getUserGroup(group2).getManagedByGroups().contains(userGroup1));

        assertTrue(personA.canManage(userGroup2));
        assertTrue(personB.canManage(userGroup2));
        assertFalse(personC.canManage(userGroup1));
        assertFalse(personD.canManage(userGroup1));

        assertTrue(personA.canManage(personC));
        assertTrue(personA.canManage(personD));
        assertTrue(personB.canManage(personC));
        assertTrue(personA.canManage(personD));
        assertFalse(personC.canManage(personA));
        assertFalse(personC.canManage(personB));

        assertTrue(personC.isManagedBy(userGroup1));
        assertTrue(personD.isManagedBy(userGroup1));
        assertFalse(personA.isManagedBy(userGroup2));
        assertFalse(personB.isManagedBy(userGroup2));

        assertTrue(personC.isManagedBy(personA));
        assertTrue(personC.isManagedBy(personB));
        assertTrue(personD.isManagedBy(personA));
        assertTrue(personD.isManagedBy(personB));
        assertFalse(personA.isManagedBy(personC));
        assertFalse(personA.isManagedBy(personD));
    }

    // TODO Maybe Removed later with mobile removed from person
    @Test
    public void testGetByPhoneNumber() {
        User userA = createUser('A');
        User userB = createUser('B');
        User userC = createUser('C');

        userA.setMobile("73647271");
        userB.setMobile("23452134");
        userC.setMobile("14543232");

        Person credentialsA = createUserCredentials('A', userA);
        Person credentialsB = createUserCredentials('B', userB);
        Person credentialsC = createUserCredentials('C', userC);

        credentialsA.setMobile("73647271");
        credentialsB.setMobile("23452134");
        credentialsC.setMobile("14543232");

        userService.addUser(userA);
        userService.addUser(userB);
        userService.addUser(userC);

        personServiceExtended.addUser(credentialsA);
        personServiceExtended.addUser(credentialsB);
        personServiceExtended.addUser(credentialsC);

        List<Person> users = personServiceExtended.getUsersByPhoneNumber("23452134");

        assertEquals(1, users.size());
        assertEquals(credentialsB, users.get(0));
    }

    // TODO Add Uuid to User too
    @Test
    public void testGetByUuid() {
        User userA = createUser('A');
        User userB = createUser('B');

        Person credentialsA = createUserCredentials('A', userA);
        Person credentialsB = createUserCredentials('B', userB);

        userService.addUser(userA);
        userService.addUser(userB);

        personServiceExtended.addUser(credentialsA);
        personServiceExtended.addUser(credentialsB);

        assertEquals(userA, userService.getUserByUuid(credentialsA.getUuid()));
        assertEquals(userB, userService.getUserByUuid(credentialsB.getUuid()));
    }

    @Test
    public void testGetByIdentifier() {
        User userA = createUser('A');
        User userB = createUser('B');
        User userC = createUser('C');

        Person credentialsA = createUserCredentials('A', userA);
        Person credentialsB = createUserCredentials('B', userB);
        Person credentialsC = createUserCredentials('C', userC);

        userService.addUser(userA);
        userService.addUser(userB);
        userService.addUser(userC);

        personServiceExtended.addUser(credentialsA);
        personServiceExtended.addUser(credentialsB);
        personServiceExtended.addUser(credentialsC);

        // Match

        assertEquals(credentialsA, personServiceExtended.getUserByIdentifier(credentialsA.getUid()));
        assertEquals(credentialsA, personServiceExtended.getUserByIdentifier(credentialsA.getUuid().toString()));
        assertEquals(credentialsA, personServiceExtended.getUserByIdentifier(credentialsA.getUsername()));

        assertEquals(credentialsB, personServiceExtended.getUserByIdentifier(credentialsB.getUid()));
        assertEquals(credentialsB, personServiceExtended.getUserByIdentifier(credentialsB.getUuid().toString()));
        assertEquals(credentialsB, personServiceExtended.getUserByIdentifier(credentialsB.getUsername()));

        // No match

        assertNull(personServiceExtended.getUserByIdentifier("hYg6TgAfN71"));
        assertNull(personServiceExtended.getUserByIdentifier("cac39761-3ef9-4774-8b1e-b96cedbc57a9"));
        assertNull(personServiceExtended.getUserByIdentifier("johndoe"));
    }

    @Disabled("Ignore until implementing Queries with orders")
    @Test
    public void testGetOrdered() {
        User userA = createUser('A');
        User userB = createUser('B');
        User userC = createUser('C');

        userA.setSurname("Yong");
        userA.setFirstName("Anne");
        userA.setEmail("lost@space.com");
        userB.setSurname("Arden");
        userB.setFirstName("Jenny");
        userB.setEmail("Inside@other.com");
        userC.setSurname("Smith");
        userC.setFirstName("Igor");
        userC.setEmail("home@other.com");

        Person credentialsA = createUserCredentials('A', userA);
        Person credentialsB = createUserCredentials('B', userB);
        Person credentialsC = createUserCredentials('C', userC);

        credentialsA.getOrganisationUnits().add(unitA);
        credentialsB.getOrganisationUnits().add(unitA);
        credentialsC.getOrganisationUnits().add(unitA);

        userService.addUser(userA);
        userService.addUser(userB);
        userService.addUser(userC);

        personServiceExtended.addUser(credentialsA);
        personServiceExtended.addUser(credentialsB);
        personServiceExtended.addUser(credentialsC);

        List<Person> users = personServiceExtended.getUsers(
            new PersonQueryParams().addOrganisationUnit(unitA),
            Collections.singletonList("email:idesc")
        );
        assertEquals(3, users.size());
        assertEquals(credentialsA, users.get(0));
        assertEquals(credentialsB, users.get(1));
        assertEquals(credentialsC, users.get(2));

        users = personServiceExtended.getUsers(new PersonQueryParams().addOrganisationUnit(unitA), null);
        assertEquals(3, users.size());
        assertEquals(credentialsA, users.get(2));
        assertEquals(credentialsB, users.get(0));
        assertEquals(credentialsC, users.get(1));

        users =
            personServiceExtended.getUsers(new PersonQueryParams().addOrganisationUnit(unitA), Collections.singletonList("firstName:asc"));
        assertEquals(3, users.size());
        assertEquals(credentialsA, users.get(0));
        assertEquals(credentialsB, users.get(2));
        assertEquals(credentialsC, users.get(1));
    }

    @Test
    public void testGetManagedGroupsLessAuthoritiesDisjointRoles() {
        User userA = createUser('A');
        User userB = createUser('B');
        User userC = createUser('C');
        User userD = createUser('D');
        User userE = createUser('E');
        User userF = createUser('F');

        Person credentialsA = createUserCredentials('A', userA);
        Person credentialsB = createUserCredentials('B', userB);
        Person credentialsC = createUserCredentials('C', userC);
        Person credentialsD = createUserCredentials('D', userD);
        Person credentialsE = createUserCredentials('E', userE);
        Person credentialsF = createUserCredentials('F', userF);

        credentialsA.getPersonAuthorityGroups().add(roleA);
        credentialsB.getPersonAuthorityGroups().add(roleB);
        credentialsB.getPersonAuthorityGroups().add(roleC);
        credentialsC.getPersonAuthorityGroups().add(roleA);
        credentialsC.getPersonAuthorityGroups().add(roleB);
        credentialsD.getPersonAuthorityGroups().add(roleC);
        credentialsE.getPersonAuthorityGroups().add(roleA);
        credentialsE.getPersonAuthorityGroups().add(roleB);
        credentialsF.getPersonAuthorityGroups().add(roleC);

        userService.addUser(userA);
        userService.addUser(userB);
        userService.addUser(userC);
        userService.addUser(userD);
        userService.addUser(userE);
        userService.addUser(userF);

        personServiceExtended.addUser(credentialsA);
        personServiceExtended.addUser(credentialsB);
        personServiceExtended.addUser(credentialsC);
        personServiceExtended.addUser(credentialsD);
        personServiceExtended.addUser(credentialsE);
        personServiceExtended.addUser(credentialsF);

        PeopleGroup userGroup1 = createUserGroup('A', Sets.newHashSet(credentialsA, credentialsB));
        PeopleGroup userGroup2 = createUserGroup('B', Sets.newHashSet(credentialsC, credentialsD, credentialsE, credentialsF));

        credentialsA.getGroups().add(userGroup1);
        credentialsB.getGroups().add(userGroup1);
        credentialsC.getGroups().add(userGroup2);
        credentialsD.getGroups().add(userGroup2);
        credentialsE.getGroups().add(userGroup2);
        credentialsF.getGroups().add(userGroup2);

        userGroup1.setManagedGroups(Sets.newHashSet(userGroup2));
        userGroup2.setManagedByGroups(Sets.newHashSet(userGroup1));

        peopleGroupService.addUserGroup(userGroup1);
        peopleGroupService.addUserGroup(userGroup2);

        PersonQueryParams params = new PersonQueryParams();
        params.setCanManage(true);
        params.setAuthSubset(true);
        params.setPerson(credentialsA);

        List<Person> users = personServiceExtended.getUsers(params);

        //        assertEquals(2, users.size());
        assertEquals(4, users.size());
        assertTrue(users.contains(credentialsD));
        assertTrue(users.contains(credentialsF));

        //        assertEquals(2, personServiceExtended.getUserCount(params));
        assertEquals(4, personServiceExtended.getUserCount(params));

        //        params.setPerson(credentialsB);
        params.setPerson(credentialsD);

        users = personServiceExtended.getUsers(params);

        assertEquals(0, users.size());

        assertEquals(0, personServiceExtended.getUserCount(params));

        params.setPerson(credentialsC);

        users = personServiceExtended.getUsers(params);

        assertEquals(0, users.size());

        assertEquals(0, personServiceExtended.getUserCount(params));
    }

    @Test
    public void testGetManagedGroupsSearch() {
        User userA = createUser('A');
        User userB = createUser('B');
        User userC = createUser('C');
        User userD = createUser('D');
        User userE = createUser('E');
        User userF = createUser('F');

        Person credentialsA = createUserCredentials('A', userA);
        Person credentialsB = createUserCredentials('B', userB);
        Person credentialsC = createUserCredentials('C', userC);
        Person credentialsD = createUserCredentials('D', userD);
        Person credentialsE = createUserCredentials('E', userE);
        Person credentialsF = createUserCredentials('F', userF);

        userService.addUser(userA);
        userService.addUser(userB);
        userService.addUser(userC);
        userService.addUser(userD);
        userService.addUser(userE);
        userService.addUser(userF);

        personServiceExtended.addUser(credentialsA);
        personServiceExtended.addUser(credentialsB);
        personServiceExtended.addUser(credentialsC);
        personServiceExtended.addUser(credentialsD);
        personServiceExtended.addUser(credentialsE);
        personServiceExtended.addUser(credentialsF);

        PersonQueryParams params = new PersonQueryParams();
        params.setQuery("oginA");

        List<Person> users = personServiceExtended.getUsers(params);

        assertEquals(1, users.size());
        assertTrue(users.contains(credentialsA));

        assertEquals(1, personServiceExtended.getUserCount(params));
    }

    @Test
    public void testGetManagedGroupsSelfRegistered() {
        User userA = createUser('A');
        User userB = createUser('B');
        User userC = createUser('C');
        User userD = createUser('D');

        Person credentialsA = createUserCredentials('A', userA);
        Person credentialsB = createUserCredentials('B', userB);
        Person credentialsC = createUserCredentials('C', userC);
        Person credentialsD = createUserCredentials('D', userD);

        credentialsA.setSelfRegistered(true);
        credentialsC.setSelfRegistered(true);

        userService.addUser(userA);
        userService.addUser(userB);
        userService.addUser(userC);
        userService.addUser(userD);

        personServiceExtended.addUser(credentialsA);
        personServiceExtended.addUser(credentialsB);
        personServiceExtended.addUser(credentialsC);
        personServiceExtended.addUser(credentialsD);

        PersonQueryParams params = new PersonQueryParams();
        params.setSelfRegistered(true);

        List<Person> users = personServiceExtended.getUsers(params);

        assertEquals(2, users.size());
        assertTrue(users.contains(credentialsA));
        assertTrue(users.contains(credentialsC));

        assertEquals(2, personServiceExtended.getUserCount(params));
    }

    @Test
    public void testGetManagedGroupsOrganisationUnit() {
        User userA = createUser('A');
        User userB = createUser('B');
        User userC = createUser('C');
        User userD = createUser('D');

        Person credentialsA = createUserCredentials('A', userA);
        Person credentialsB = createUserCredentials('B', userB);
        Person credentialsC = createUserCredentials('C', userC);
        Person credentialsD = createUserCredentials('D', userD);

        credentialsA.getOrganisationUnits().add(unitA);
        credentialsA.getOrganisationUnits().add(unitB);
        credentialsB.getOrganisationUnits().add(unitB);
        credentialsC.getOrganisationUnits().add(unitA);
        credentialsD.getOrganisationUnits().add(unitB);

        userService.addUser(userA);
        userService.addUser(userB);
        userService.addUser(userC);
        userService.addUser(userD);

        personServiceExtended.addUser(credentialsA);
        personServiceExtended.addUser(credentialsB);
        personServiceExtended.addUser(credentialsC);
        personServiceExtended.addUser(credentialsD);

        PersonQueryParams params = new PersonQueryParams();
        params.getOrganisationUnits().add(unitA);

        List<Person> users = personServiceExtended.getUsers(params);

        assertEquals(2, users.size());
        assertTrue(users.contains(credentialsA));
        assertTrue(users.contains(credentialsC));

        assertEquals(2, personServiceExtended.getUserCount(params));
    }
}
