package org.nmcpye.activitiesmanagement.extended.common;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.junit.jupiter.api.Test;
import org.nmcpye.activitiesmanagement.AMTest;
import org.nmcpye.activitiesmanagement.IntegrationTest;
import org.nmcpye.activitiesmanagement.domain.User;
import org.nmcpye.activitiesmanagement.domain.organisationunit.OrganisationUnit;
import org.nmcpye.activitiesmanagement.domain.organisationunit.OrganisationUnitGroup;
import org.nmcpye.activitiesmanagement.domain.person.Person;
import org.nmcpye.activitiesmanagement.extended.organisationunit.OrganisationUnitGroupService;
import org.nmcpye.activitiesmanagement.extended.organisationunit.OrganisationUnitService;
import org.nmcpye.activitiesmanagement.extended.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@IntegrationTest
public class IdentifiableObjectManagerTest
    extends AMTest {

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    private OrganisationUnitService organisationUnitService;

    @Autowired
    private OrganisationUnitGroupService organisationUnitGroupService;

    @Autowired
    private IdentifiableObjectManager identifiableObjectManager;

    @Autowired
    private UserService _userService;

    @Override
    protected void setUpTest() throws Exception {
        this.userService = _userService;
    }

    @Test
    public void testGetObjectWithIdScheme() {
        OrganisationUnit unitA = createOrganisationUnit('A');

        organisationUnitService.addOrganisationUnit(unitA);

        assertEquals(unitA, identifiableObjectManager.getObject(OrganisationUnit.class, IdScheme.CODE, unitA.getCode()));
        assertEquals(unitA, identifiableObjectManager.getObject(OrganisationUnit.class, IdScheme.UID, unitA.getUid()));
    }

    @Test
    public void testGetObject() {
        OrganisationUnit unitA = createOrganisationUnit('A');
        OrganisationUnit unitB = createOrganisationUnit('B');

        organisationUnitService.addOrganisationUnit(unitA);
        long unitIdA = unitA.getId();
        organisationUnitService.addOrganisationUnit(unitB);
        long unitIdB = unitB.getId();

        OrganisationUnitGroup unitGroupA = createOrganisationUnitGroup('A');
        OrganisationUnitGroup unitGroupB = createOrganisationUnitGroup('B');

        organisationUnitGroupService.addOrganisationUnitGroup(unitGroupA);
        long unitGroupIdA = unitGroupA.getId();
        organisationUnitGroupService.addOrganisationUnitGroup(unitGroupB);
        long unitGroupIdB = unitGroupB.getId();

        assertEquals(unitA, identifiableObjectManager.getObject(unitIdA, OrganisationUnit.class.getSimpleName()));
        assertEquals(unitB, identifiableObjectManager.getObject(unitIdB, OrganisationUnit.class.getSimpleName()));

        assertEquals(unitGroupA, identifiableObjectManager.getObject(unitGroupIdA, OrganisationUnitGroup.class.getSimpleName()));
        assertEquals(unitGroupB, identifiableObjectManager.getObject(unitGroupIdB, OrganisationUnitGroup.class.getSimpleName()));
    }

    @Test
    public void testGetWithClasses() {
        OrganisationUnit unitA = createOrganisationUnit('A');
        OrganisationUnit unitB = createOrganisationUnit('B');
        ;

        organisationUnitService.addOrganisationUnit(unitA);
        organisationUnitService.addOrganisationUnit(unitB);

        Set<Class<? extends IdentifiableObject>> classes = ImmutableSet.<Class<? extends IdentifiableObject>>builder().
            add(Person.class).add(OrganisationUnit.class).add(OrganisationUnitGroup.class).build();

        assertEquals(unitA, identifiableObjectManager.get(classes, unitA.getUid()));
        assertEquals(unitB, identifiableObjectManager.get(classes, unitB.getUid()));
    }

//    @Test
//    public void publicAccessSetIfNoUser() {
//        OrganisationUnit unit = createOrganisationUnit('A');
//        identifiableObjectManager.save(unit);
//
//        assertNotNull(unit.getPublicAccess());
//        assertFalse(AccessStringHelper.canRead(unit.getPublicAccess()));
//        assertFalse(AccessStringHelper.canWrite(unit.getPublicAccess()));
//    }

    @Test
    public void getCount() {
        identifiableObjectManager.save(createOrganisationUnit('A'));
        identifiableObjectManager.save(createOrganisationUnit('B'));
        identifiableObjectManager.save(createOrganisationUnit('C'));
        identifiableObjectManager.save(createOrganisationUnit('D'));

        assertEquals(4, identifiableObjectManager.getCount(OrganisationUnit.class));
    }

    @Test
    public void getEqualToName() {
        OrganisationUnit unit = createOrganisationUnit('A');
        identifiableObjectManager.save(unit);

        assertNotNull(identifiableObjectManager.getByName(OrganisationUnit.class, "OrganisationUnitA"));
        assertNull(identifiableObjectManager.getByName(OrganisationUnit.class, "OrganisationUnitB"));
        assertEquals(unit, identifiableObjectManager.getByName(OrganisationUnit.class, "OrganisationUnitA"));
    }

    @Test
    public void getAllOrderedName() {
        identifiableObjectManager.save(createOrganisationUnit('D'));
        identifiableObjectManager.save(createOrganisationUnit('B'));
        identifiableObjectManager.save(createOrganisationUnit('C'));
        identifiableObjectManager.save(createOrganisationUnit('A'));

        List<OrganisationUnit> units = new ArrayList<>(identifiableObjectManager.getAllSorted(OrganisationUnit.class));

        assertEquals(4, units.size());
        assertEquals("OrganisationUnitA", units.get(0).getName());
        assertEquals("OrganisationUnitB", units.get(1).getName());
        assertEquals("OrganisationUnitC", units.get(2).getName());
        assertEquals("OrganisationUnitD", units.get(3).getName());
    }

    @Test
    public void userIsCurrentIfNoUserSet() {
        User user = createUserAndInjectSecurityContext(true);

        OrganisationUnit unit = createOrganisationUnit('A');
        identifiableObjectManager.save(unit);

        assertNotNull(unit.getUser());
        assertEquals(user, unit.getUser());
    }

//    @Test
//    public void userCanCreatePublic() {
//        createUserAndInjectSecurityContext(false, "F_DATAELEMENT_PUBLIC_ADD");
//
//        OrganisationUnit unit = createOrganisationUnit('A');
//        identifiableObjectManager.save(unit);
//
//        assertNotNull(unit.getPublicAccess());
//        assertTrue(AccessStringHelper.canRead(unit.getPublicAccess()));
//        assertTrue(AccessStringHelper.canWrite(unit.getPublicAccess()));
//    }
//
//    @Test
//    public void userCanCreatePrivate() {
//        createUserAndInjectSecurityContext(false, "F_DATAELEMENT_PRIVATE_ADD");
//
//        OrganisationUnit unit = createOrganisationUnit('A');
//        identifiableObjectManager.save(unit);
//
//        assertNotNull(unit.getPublicAccess());
//        assertFalse(AccessStringHelper.canRead(unit.getPublicAccess()));
//        assertFalse(AccessStringHelper.canWrite(unit.getPublicAccess()));
//    }

//    @Test(expected = CreateAccessDeniedException.class)
//    public void userDeniedCreateObject() {
//        createUserAndInjectSecurityContext(false);
//        identifiableObjectManager.save(createOrganisationUnit('A'));
//    }
//
//    @Test
//    public void publicUserModifiedPublicAccessDEFAULT() {
//        createUserAndInjectSecurityContext(false, "F_DATAELEMENT_PUBLIC_ADD");
//
//        OrganisationUnit unit = createOrganisationUnit('A');
//        unit.setPublicAccess(AccessStringHelper.DEFAULT);
//
//        identifiableObjectManager.save(unit, false);
//
//        assertFalse(AccessStringHelper.canRead(unit.getPublicAccess()));
//        assertFalse(AccessStringHelper.canWrite(unit.getPublicAccess()));
//    }
//
//    @Test
//    public void publicUserModifiedPublicAccessRW() {
//        createUserAndInjectSecurityContext(false, "F_DATAELEMENT_PUBLIC_ADD");
//
//        OrganisationUnit unit = createOrganisationUnit('A');
//        unit.setPublicAccess(AccessStringHelper.READ_WRITE);
//
//        identifiableObjectManager.save(unit, false);
//    }
//
//    @Test(expected = CreateAccessDeniedException.class)
//    public void privateUserModifiedPublicAccessRW() {
//        createUserAndInjectSecurityContext(false, "F_DATAELEMENT_PRIVATE_ADD");
//
//        OrganisationUnit unit = createOrganisationUnit('A');
//        unit.setPublicAccess(AccessStringHelper.READ_WRITE);
//
//        identifiableObjectManager.save(unit, false);
//    }
//
//    @Test
//    public void privateUserModifiedPublicAccessDEFAULT() {
//        createUserAndInjectSecurityContext(false, "F_DATAELEMENT_PRIVATE_ADD");
//
//        OrganisationUnit unit = createOrganisationUnit('A');
//        unit.setPublicAccess(AccessStringHelper.DEFAULT);
//
//        identifiableObjectManager.save(unit, false);
//    }
//
//    @Test(expected = UpdateAccessDeniedException.class)
//    public void updateForPrivateUserDeniedAfterChangePublicAccessRW() {
//        createUserAndInjectSecurityContext(false, "F_DATAELEMENT_PRIVATE_ADD");
//
//        OrganisationUnit unit = createOrganisationUnit('A');
//        unit.setPublicAccess(AccessStringHelper.DEFAULT);
//
//        identifiableObjectManager.save(unit, false);
//
//        unit.setPublicAccess(AccessStringHelper.READ_WRITE);
//
//        identifiableObjectManager.update(unit);
//    }
//
//    @Test(expected = CreateAccessDeniedException.class)
//    public void userDeniedForPublicAdd() {
//        createUserAndInjectSecurityContext(false);
//
//        OrganisationUnit unit = createOrganisationUnit('A');
//        unit.setPublicAccess(AccessStringHelper.READ_WRITE);
//
//        identifiableObjectManager.save(unit, false);
//    }
//
//    @Test(expected = DeleteAccessDeniedException.class)
//    public void userDeniedDeleteObject() {
//        createUserAndInjectSecurityContext(false, "F_DATAELEMENT_PUBLIC_ADD", "F_USER_ADD");
//
//        User user = createUser('B');
//        identifiableObjectManager.save(user);
//
//        OrganisationUnit unit = createOrganisationUnit('A');
//        identifiableObjectManager.save(unit);
//
//        unit.setUser(user);
//        unit.setPublicAccess(AccessStringHelper.DEFAULT);
//        sessionFactory.getCurrentSession().update(unit);
//
//        identifiableObjectManager.delete(unit);
//    }

    @Test
    public void objectsWithNoUser() {
        identifiableObjectManager.save(createOrganisationUnit('A'));
        identifiableObjectManager.save(createOrganisationUnit('B'));
        identifiableObjectManager.save(createOrganisationUnit('C'));
        identifiableObjectManager.save(createOrganisationUnit('D'));

        assertEquals(4, identifiableObjectManager.getCount(OrganisationUnit.class));
        assertEquals(4, identifiableObjectManager.getAll(OrganisationUnit.class).size());
    }

//    @Test
//    public void readPrivateObjects() {
//        createUserAndInjectSecurityContext(false, "F_DATAELEMENT_PUBLIC_ADD", "F_USER_ADD");
//
//        User user = createUser('B');
//        identifiableObjectManager.save(user);
//
//        identifiableObjectManager.save(createOrganisationUnit('A'));
//        identifiableObjectManager.save(createOrganisationUnit('B'));
//        identifiableObjectManager.save(createOrganisationUnit('C'));
//        identifiableObjectManager.save(createOrganisationUnit('D'));
//
//        assertEquals(4, identifiableObjectManager.getAll(OrganisationUnit.class).size());
//
//        List<OrganisationUnit> units = new ArrayList<>(identifiableObjectManager.getAll(OrganisationUnit.class));
//
//        for (OrganisationUnit unit : units) {
//            unit.setUser(user);
//            unit.setPublicAccess(AccessStringHelper.DEFAULT);
//
//            sessionFactory.getCurrentSession().update(unit);
//        }
//
//        assertEquals(0, identifiableObjectManager.getCount(OrganisationUnit.class));
//        assertEquals(0, identifiableObjectManager.getAll(OrganisationUnit.class).size());
//    }

//    @Test
//    public void readUserGroupSharedObjects() {
//        User loginUser = createUserAndInjectSecurityContext(false, "F_DATAELEMENT_PUBLIC_ADD", "F_USER_ADD", "F_USERGROUP_PUBLIC_ADD");
//
//        User user = createUser('B');
//        identifiableObjectManager.save(user);
//
//        PeopleGroup userGroup = createUserGroup('A', Sets.newHashSet(loginUser));
//        identifiableObjectManager.save(userGroup);
//
//        identifiableObjectManager.save(createOrganisationUnit('A'));
//        identifiableObjectManager.save(createOrganisationUnit('B'));
//        identifiableObjectManager.save(createOrganisationUnit('C'));
//        identifiableObjectManager.save(createOrganisationUnit('D'));
//
//        assertEquals(4, identifiableObjectManager.getCount(OrganisationUnit.class));
//        assertEquals(4, identifiableObjectManager.getAll(OrganisationUnit.class).size());
//
//        List<OrganisationUnit> units = new ArrayList<>(identifiableObjectManager.getAll(OrganisationUnit.class));
//
//        for (OrganisationUnit unit : units) {
//            unit.setUser(user);
//            unit.setPublicAccess(AccessStringHelper.newInstance().build());
//
//            UserGroupAccess userGroupAccess = new UserGroupAccess();
//            userGroupAccess.setAccess(AccessStringHelper.READ);
//            userGroupAccess.setUserGroup(userGroup);
//
//            sessionFactory.getCurrentSession().save(userGroupAccess);
//
//            unit.getUserGroupAccesses().add(userGroupAccess);
//            sessionFactory.getCurrentSession().update(unit);
//        }
//
//        assertEquals(4, identifiableObjectManager.getCount(OrganisationUnit.class));
//        assertEquals(4, identifiableObjectManager.getAll(OrganisationUnit.class).size());
//    }

    @Test
    public void getByUidTest() {
        OrganisationUnit unitA = createOrganisationUnit('A');
        OrganisationUnit unitB = createOrganisationUnit('B');
        ;
        OrganisationUnit unitC = createOrganisationUnit('C');
        OrganisationUnit unitD = createOrganisationUnit('D');

        identifiableObjectManager.save(unitA);
        identifiableObjectManager.save(unitB);
        identifiableObjectManager.save(unitC);
        identifiableObjectManager.save(unitD);

        List<OrganisationUnit> ab = identifiableObjectManager.getByUid(OrganisationUnit.class, Arrays.asList(unitA.getUid(), unitB.getUid()));
        List<OrganisationUnit> cd = identifiableObjectManager.getByUid(OrganisationUnit.class, Arrays.asList(unitC.getUid(), unitD.getUid()));

        assertTrue(ab.contains(unitA));
        assertTrue(ab.contains(unitB));
        assertFalse(ab.contains(unitC));
        assertFalse(ab.contains(unitD));

        assertFalse(cd.contains(unitA));
        assertFalse(cd.contains(unitB));
        assertTrue(cd.contains(unitC));
        assertTrue(cd.contains(unitD));
    }

    @Test
    public void getByUidOrderedTest() {
        OrganisationUnit unitA = createOrganisationUnit('A');
        OrganisationUnit unitB = createOrganisationUnit('B');
        ;
        OrganisationUnit unitC = createOrganisationUnit('C');
        OrganisationUnit unitD = createOrganisationUnit('D');

        identifiableObjectManager.save(unitA);
        identifiableObjectManager.save(unitB);
        identifiableObjectManager.save(unitC);
        identifiableObjectManager.save(unitD);

        List<String> uids = Arrays.asList(unitA.getUid(), unitC.getUid(), unitB.getUid(), unitD.getUid());

        List<OrganisationUnit> expected = new ArrayList<>(Arrays.asList(unitA, unitC, unitB, unitD));

        List<OrganisationUnit> actual = new ArrayList<>(identifiableObjectManager.getByUidOrdered(OrganisationUnit.class, uids));

        assertEquals(expected, actual);
    }

    @Test
    public void testGetByCode() {
        OrganisationUnit unitA = createOrganisationUnit('A');
        OrganisationUnit unitB = createOrganisationUnit('B');
        ;
        OrganisationUnit unitC = createOrganisationUnit('C');
        OrganisationUnit unitD = createOrganisationUnit('D');

        unitA.setCode("DE_A");
        unitB.setCode("DE_B");
        unitC.setCode("DE_C");
        unitD.setCode("DE_D");

        identifiableObjectManager.save(unitA);
        identifiableObjectManager.save(unitB);
        identifiableObjectManager.save(unitC);
        identifiableObjectManager.save(unitD);

        List<OrganisationUnit> ab = identifiableObjectManager.getByCode(OrganisationUnit.class, Arrays.asList(unitA.getCode(), unitB.getCode()));
        List<OrganisationUnit> cd = identifiableObjectManager.getByCode(OrganisationUnit.class, Arrays.asList(unitC.getCode(), unitD.getCode()));

        assertTrue(ab.contains(unitA));
        assertTrue(ab.contains(unitB));
        assertFalse(ab.contains(unitC));
        assertFalse(ab.contains(unitD));

        assertFalse(cd.contains(unitA));
        assertFalse(cd.contains(unitB));
        assertTrue(cd.contains(unitC));
        assertTrue(cd.contains(unitD));
    }

    @Test
    public void getByUidNoAcl() {
        OrganisationUnit unitA = createOrganisationUnit('A');
        OrganisationUnit unitB = createOrganisationUnit('B');
        OrganisationUnit unitC = createOrganisationUnit('C');

        unitA.setCode("OU_A");
        unitB.setCode("OU_B");
        unitC.setCode("OU_C");

        Person person = createUserCredentials('A', null);

        identifiableObjectManager.save(person);

        identifiableObjectManager.save(unitA);
        identifiableObjectManager.save(unitB);
        identifiableObjectManager.save(unitC);

        List<String> uids = Lists.newArrayList(unitA.getUid(), unitB.getUid(), unitC.getUid());

        List<OrganisationUnit> units = identifiableObjectManager.getNoAcl(OrganisationUnit.class, uids);

        assertEquals(3, units.size());
        assertTrue(units.contains(unitA));
        assertTrue(units.contains(unitB));
        assertTrue(units.contains(unitC));
    }

    @Test
    public void testGetObjects() {
        OrganisationUnit unit1 = createOrganisationUnit('A');
        OrganisationUnit unit2 = createOrganisationUnit('B');
        OrganisationUnit unit3 = createOrganisationUnit('C');

        identifiableObjectManager.save(unit1);
        identifiableObjectManager.save(unit2);
        identifiableObjectManager.save(unit3);

        Set<String> codes = Sets.newHashSet(unit2.getCode(), unit3.getCode());

        List<OrganisationUnit> units = identifiableObjectManager.getObjects(OrganisationUnit.class, IdentifiableProperty.CODE, codes);

        assertEquals(2, units.size());
        assertTrue(units.contains(unit2));
        assertTrue(units.contains(unit3));
    }

    @Test
    public void testGetIdMapIdScheme() {
        OrganisationUnit unitA = createOrganisationUnit('A');
        OrganisationUnit unitB = createOrganisationUnit('B');
        ;

        organisationUnitService.addOrganisationUnit(unitA);
        organisationUnitService.addOrganisationUnit(unitB);

        Map<String, OrganisationUnit> map = identifiableObjectManager.getIdMap(OrganisationUnit.class, IdScheme.CODE);

        assertEquals(unitA, map.get("OrganisationUnitCodeA"));
        assertEquals(unitB, map.get("OrganisationUnitCodeB"));
        assertNull(map.get("OrganisationUnitCodeX"));
    }
}
