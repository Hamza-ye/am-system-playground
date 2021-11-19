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
import org.nmcpye.activitiesmanagement.extended.organisationunit.OrganisationUnitServiceExt;
import org.nmcpye.activitiesmanagement.extended.user.UserServiceExt;
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
    private UserServiceExt userServiceExt;

    @Autowired
    private PersonServiceExt personServiceExt;

    @Autowired
    private PeopleGroupServiceExt peopleGroupServiceExt;

    @Autowired
    private OrganisationUnitServiceExt organisationUnitServiceExt;

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
        super.userServiceExt = userServiceExt;

        unitA = createOrganisationUnit('A');
        unitB = createOrganisationUnit('B');
        unitC = createOrganisationUnit('C', unitA);
        unitD = createOrganisationUnit('D', unitB);
        unitE = createOrganisationUnit('E');

        organisationUnitServiceExt.addOrganisationUnit(unitA);
        organisationUnitServiceExt.addOrganisationUnit(unitB);
        organisationUnitServiceExt.addOrganisationUnit(unitC);
        organisationUnitServiceExt.addOrganisationUnit(unitD);
        organisationUnitServiceExt.addOrganisationUnit(unitE);

        organisationUnitServiceExt.forceUpdatePaths();

        roleA = createUserAuthorityGroup('A');
        roleB = createUserAuthorityGroup('B');
        roleC = createUserAuthorityGroup('C');

//        Authority authA = new Authority();
//        authA.setName("AuthA");
//        Authority authB = new Authority();
//        authB.setName("AuthB");
//        Authority authC = new Authority();
//        authC.setName("AuthC");
//        Authority authD = new Authority();
//        authD.setName("AuthD");
//
//        roleA.getAuthorities().add(authA);
//        roleA.getAuthorities().add(authB);
//        roleA.getAuthorities().add(authC);
//        roleA.getAuthorities().add(authD);
//
//        roleB.getAuthorities().add(authA);
//        roleB.getAuthorities().add(authB);
//
//        roleC.getAuthorities().add(authC);

        roleA.getAuthorities().add("AuthA");
        roleA.getAuthorities().add("AuthB");
        roleA.getAuthorities().add("AuthC");
        roleA.getAuthorities().add("AuthD");

        roleB.getAuthorities().add("AuthA");
        roleB.getAuthorities().add("AuthB");

        roleC.getAuthorities().add("AuthC");


        personServiceExt.addUserAuthorityGroup(roleA);
        personServiceExt.addUserAuthorityGroup(roleB);
        personServiceExt.addUserAuthorityGroup(roleC);
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

        Long idA = personServiceExt.addUser(personA);
        Long idB = personServiceExt.addUser(personB);

        assertEquals(personA, personServiceExt.getUser(idA));
        assertEquals(personB, personServiceExt.getUser(idB));

        assertEquals(units, personServiceExt.getUser(idA).getOrganisationUnits());
        assertEquals(units, personServiceExt.getUser(idB).getOrganisationUnits());
    }

    @Test
    public void testUpdateUser() {
        User userA = createUser('A');
        User userB = createUser('B');

        Person personA = createUserCredentials('A', userA);
        Person personB = createUserCredentials('B', userB);

        Long idA = personServiceExt.addUser(personA);
        Long idB = personServiceExt.addUser(personB);

        assertEquals(personA, personServiceExt.getUser(idA));
        assertEquals(personB, personServiceExt.getUser(idB));

        personA.setLogin("UpdatedLoginA");

        personServiceExt.updateUser(personA);

        assertEquals(personServiceExt.getUser(idA).getLogin(), "UpdatedLoginA");
    }

    @Test
    public void testDeleteUser() {
        User userA = createUser('A');
        User userB = createUser('B');

        Person personA = createUserCredentials('A', userA);
        Person personB = createUserCredentials('B', userB);

        Long idA = personServiceExt.addUser(personA);
        Long idB = personServiceExt.addUser(personB);

        assertEquals(personA, personServiceExt.getUser(idA));
        assertEquals(personB, personServiceExt.getUser(idB));

        personServiceExt.deleteUser(personA);

        assertNull(personServiceExt.getUser(idA));
        assertNotNull(personServiceExt.getUser(idB));
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

        userServiceExt.addUser(userA);
        userServiceExt.addUser(userB);
        userServiceExt.addUser(userC);
        userServiceExt.addUser(userD);

        personServiceExt.addUser(personA);
        personServiceExt.addUser(personB);
        personServiceExt.addUser(personC);
        personServiceExt.addUser(personD);

        PersonQueryParams params = new PersonQueryParams().addOrganisationUnit(unitA).setPerson(personA);

        List<Person> users = personServiceExt.getUsers(params);

        assertEquals(1, users.size());
        assertTrue(users.contains(personA));

        params = new PersonQueryParams().addOrganisationUnit(unitA).setIncludeOrgUnitChildren(true).setPerson(personA);

        users = personServiceExt.getUsers(params);

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

        userServiceExt.addUser(userA);
        userServiceExt.addUser(userB);
        userServiceExt.addUser(userC);
        userServiceExt.addUser(userD);

        personServiceExt.addUser(personA);
        personServiceExt.addUser(personB);
        personServiceExt.addUser(personC);
        personServiceExt.addUser(personD);

        PeopleGroup ugA = createUserGroup('A', Sets.newHashSet(personA, personB));
        PeopleGroup ugB = createUserGroup('B', Sets.newHashSet(personB, personC));
        PeopleGroup ugC = createUserGroup('C', Sets.newHashSet(personD));

        peopleGroupServiceExt.addUserGroup(ugA);
        peopleGroupServiceExt.addUserGroup(ugB);
        peopleGroupServiceExt.addUserGroup(ugC);

        List<Person> users = personServiceExt.getUsers(new PersonQueryParams().setPeopleGroups(Sets.newHashSet(ugA)));

        assertEquals(2, users.size());
        assertTrue(users.contains(personA));
        assertTrue(users.contains(personB));

        users = personServiceExt.getUsers(new PersonQueryParams().setPeopleGroups(Sets.newHashSet(ugA, ugB)));

        assertEquals(3, users.size());
        assertTrue(users.contains(personA));
        assertTrue(users.contains(personB));
        assertTrue(users.contains(personC));

        users = personServiceExt.getUsers(new PersonQueryParams().setPeopleGroups(Sets.newHashSet(ugA, ugC)));

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

        userServiceExt.addUser(currentUser);
        userServiceExt.addUser(userA);
        userServiceExt.addUser(userB);
        userServiceExt.addUser(userC);
        userServiceExt.addUser(userD);
        userServiceExt.addUser(userE);

        personServiceExt.addUser(currentPerson);
        personServiceExt.addUser(credentialsA);
        personServiceExt.addUser(credentialsB);
        personServiceExt.addUser(credentialsC);
        personServiceExt.addUser(credentialsD);
        personServiceExt.addUser(credentialsE);

        PersonQueryParams params = new PersonQueryParams().setPerson(currentPerson).setUserOrgUnits(true);

        List<Person> users = personServiceExt.getUsers(params);

        assertEquals(3, users.size());
        assertTrue(users.contains(currentPerson));
        assertTrue(users.contains(credentialsA));
        assertTrue(users.contains(credentialsB));

        params = new PersonQueryParams().setPerson(currentPerson).setUserOrgUnits(true).setIncludeOrgUnitChildren(true);

        users = personServiceExt.getUsers(params);

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

        userServiceExt.addUser(userA);
        userServiceExt.addUser(userB);
        userServiceExt.addUser(userC);
        userServiceExt.addUser(userD);

        personServiceExt.addUser(personA);
        personServiceExt.addUser(personB);
        personServiceExt.addUser(personC);
        personServiceExt.addUser(personD);

        PeopleGroup userGroup1 = createUserGroup('A', Sets.newHashSet(personA, personB));
        PeopleGroup userGroup2 = createUserGroup('B', Sets.newHashSet(personC, personD));

        personA.getGroups().add(userGroup1);
        personB.getGroups().add(userGroup1);
        personC.getGroups().add(userGroup2);
        personD.getGroups().add(userGroup2);

        userGroup1.setManagedGroups(Sets.newHashSet(userGroup2));
        userGroup2.setManagedByGroups(Sets.newHashSet(userGroup1));

        long group1 = peopleGroupServiceExt.addUserGroup(userGroup1);
        long group2 = peopleGroupServiceExt.addUserGroup(userGroup2);

        assertEquals(1, peopleGroupServiceExt.getUserGroup(group1).getManagedGroups().size());
        assertTrue(peopleGroupServiceExt.getUserGroup(group1).getManagedGroups().contains(userGroup2));
        assertEquals(1, peopleGroupServiceExt.getUserGroup(group2).getManagedByGroups().size());
        assertTrue(peopleGroupServiceExt.getUserGroup(group2).getManagedByGroups().contains(userGroup1));

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

        userServiceExt.addUser(userA);
        userServiceExt.addUser(userB);
        userServiceExt.addUser(userC);

        personServiceExt.addUser(credentialsA);
        personServiceExt.addUser(credentialsB);
        personServiceExt.addUser(credentialsC);

        List<Person> users = personServiceExt.getUsersByPhoneNumber("23452134");

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

        userServiceExt.addUser(userA);
        userServiceExt.addUser(userB);

        personServiceExt.addUser(credentialsA);
        personServiceExt.addUser(credentialsB);

        assertEquals(userA, userServiceExt.getUserByUuid(credentialsA.getUuid()));
        assertEquals(userB, userServiceExt.getUserByUuid(credentialsB.getUuid()));
    }

    @Test
    public void testGetByIdentifier() {
        User userA = createUser('A');
        User userB = createUser('B');
        User userC = createUser('C');

        Person credentialsA = createUserCredentials('A', userA);
        Person credentialsB = createUserCredentials('B', userB);
        Person credentialsC = createUserCredentials('C', userC);

        userServiceExt.addUser(userA);
        userServiceExt.addUser(userB);
        userServiceExt.addUser(userC);

        personServiceExt.addUser(credentialsA);
        personServiceExt.addUser(credentialsB);
        personServiceExt.addUser(credentialsC);

        // Match

        assertEquals(credentialsA, personServiceExt.getUserByIdentifier(credentialsA.getUid()));
        assertEquals(credentialsA, personServiceExt.getUserByIdentifier(credentialsA.getUuid().toString()));
        assertEquals(credentialsA, personServiceExt.getUserByIdentifier(credentialsA.getUsername()));

        assertEquals(credentialsB, personServiceExt.getUserByIdentifier(credentialsB.getUid()));
        assertEquals(credentialsB, personServiceExt.getUserByIdentifier(credentialsB.getUuid().toString()));
        assertEquals(credentialsB, personServiceExt.getUserByIdentifier(credentialsB.getUsername()));

        // No match

        assertNull(personServiceExt.getUserByIdentifier("hYg6TgAfN71"));
        assertNull(personServiceExt.getUserByIdentifier("cac39761-3ef9-4774-8b1e-b96cedbc57a9"));
        assertNull(personServiceExt.getUserByIdentifier("johndoe"));
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

        userServiceExt.addUser(userA);
        userServiceExt.addUser(userB);
        userServiceExt.addUser(userC);

        personServiceExt.addUser(credentialsA);
        personServiceExt.addUser(credentialsB);
        personServiceExt.addUser(credentialsC);

        List<Person> users = personServiceExt.getUsers(
            new PersonQueryParams().addOrganisationUnit(unitA),
            Collections.singletonList("email:idesc")
        );
        assertEquals(3, users.size());
        assertEquals(credentialsA, users.get(0));
        assertEquals(credentialsB, users.get(1));
        assertEquals(credentialsC, users.get(2));

        users = personServiceExt.getUsers(new PersonQueryParams().addOrganisationUnit(unitA), null);
        assertEquals(3, users.size());
        assertEquals(credentialsA, users.get(2));
        assertEquals(credentialsB, users.get(0));
        assertEquals(credentialsC, users.get(1));

        users =
            personServiceExt.getUsers(new PersonQueryParams().addOrganisationUnit(unitA), Collections.singletonList("firstName:asc"));
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

        userServiceExt.addUser(userA);
        userServiceExt.addUser(userB);
        userServiceExt.addUser(userC);
        userServiceExt.addUser(userD);
        userServiceExt.addUser(userE);
        userServiceExt.addUser(userF);

        personServiceExt.addUser(credentialsA);
        personServiceExt.addUser(credentialsB);
        personServiceExt.addUser(credentialsC);
        personServiceExt.addUser(credentialsD);
        personServiceExt.addUser(credentialsE);
        personServiceExt.addUser(credentialsF);

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

        peopleGroupServiceExt.addUserGroup(userGroup1);
        peopleGroupServiceExt.addUserGroup(userGroup2);

        PersonQueryParams params = new PersonQueryParams();
        params.setCanManage(true);
        params.setAuthSubset(true);
        params.setPerson(credentialsA);

        List<Person> users = personServiceExt.getUsers(params);

        //        assertEquals(2, users.size());
        assertEquals(4, users.size());
        assertTrue(users.contains(credentialsD));
        assertTrue(users.contains(credentialsF));

        //        assertEquals(2, personServiceExt.getUserCount(params));
        assertEquals(4, personServiceExt.getUserCount(params));

        //        params.setPerson(credentialsB);
        params.setPerson(credentialsD);

        users = personServiceExt.getUsers(params);

        assertEquals(0, users.size());

        assertEquals(0, personServiceExt.getUserCount(params));

        params.setPerson(credentialsC);

        users = personServiceExt.getUsers(params);

        assertEquals(0, users.size());

        assertEquals(0, personServiceExt.getUserCount(params));
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

        userServiceExt.addUser(userA);
        userServiceExt.addUser(userB);
        userServiceExt.addUser(userC);
        userServiceExt.addUser(userD);
        userServiceExt.addUser(userE);
        userServiceExt.addUser(userF);

        personServiceExt.addUser(credentialsA);
        personServiceExt.addUser(credentialsB);
        personServiceExt.addUser(credentialsC);
        personServiceExt.addUser(credentialsD);
        personServiceExt.addUser(credentialsE);
        personServiceExt.addUser(credentialsF);

        PersonQueryParams params = new PersonQueryParams();
        params.setQuery("oginA");

        List<Person> users = personServiceExt.getUsers(params);

        assertEquals(1, users.size());
        assertTrue(users.contains(credentialsA));

        assertEquals(1, personServiceExt.getUserCount(params));
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

        userServiceExt.addUser(userA);
        userServiceExt.addUser(userB);
        userServiceExt.addUser(userC);
        userServiceExt.addUser(userD);

        personServiceExt.addUser(credentialsA);
        personServiceExt.addUser(credentialsB);
        personServiceExt.addUser(credentialsC);
        personServiceExt.addUser(credentialsD);

        PersonQueryParams params = new PersonQueryParams();
        params.setSelfRegistered(true);

        List<Person> users = personServiceExt.getUsers(params);

        assertEquals(2, users.size());
        assertTrue(users.contains(credentialsA));
        assertTrue(users.contains(credentialsC));

        assertEquals(2, personServiceExt.getUserCount(params));
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

        userServiceExt.addUser(userA);
        userServiceExt.addUser(userB);
        userServiceExt.addUser(userC);
        userServiceExt.addUser(userD);

        personServiceExt.addUser(credentialsA);
        personServiceExt.addUser(credentialsB);
        personServiceExt.addUser(credentialsC);
        personServiceExt.addUser(credentialsD);

        PersonQueryParams params = new PersonQueryParams();
        params.getOrganisationUnits().add(unitA);

        List<Person> users = personServiceExt.getUsers(params);

        assertEquals(2, users.size());
        assertTrue(users.contains(credentialsA));
        assertTrue(users.contains(credentialsC));

        assertEquals(2, personServiceExt.getUserCount(params));
    }
}
