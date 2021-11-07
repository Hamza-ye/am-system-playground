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
        OrganisationUnit dataElementA = createOrganisationUnit('A');

        organisationUnitService.addOrganisationUnit(dataElementA);

        assertEquals(dataElementA, identifiableObjectManager.getObject(OrganisationUnit.class, IdScheme.CODE, dataElementA.getCode()));
        assertEquals(dataElementA, identifiableObjectManager.getObject(OrganisationUnit.class, IdScheme.UID, dataElementA.getUid()));
    }

    @Test
    public void testGetObject() {
        OrganisationUnit dataElementA = createOrganisationUnit('A');
        OrganisationUnit dataElementB = createOrganisationUnit('B');

        organisationUnitService.addOrganisationUnit(dataElementA);
        long dataElementIdA = dataElementA.getId();
        organisationUnitService.addOrganisationUnit(dataElementB);
        long dataElementIdB = dataElementB.getId();

        OrganisationUnitGroup dataElementGroupA = createOrganisationUnitGroup('A');
        OrganisationUnitGroup dataElementGroupB = createOrganisationUnitGroup('B');

        organisationUnitGroupService.addOrganisationUnitGroup(dataElementGroupA);
        long dataElementGroupIdA = dataElementGroupA.getId();
        organisationUnitGroupService.addOrganisationUnitGroup(dataElementGroupB);
        long dataElementGroupIdB = dataElementGroupB.getId();

        assertEquals(dataElementA, identifiableObjectManager.getObject(dataElementIdA, OrganisationUnit.class.getSimpleName()));
        assertEquals(dataElementB, identifiableObjectManager.getObject(dataElementIdB, OrganisationUnit.class.getSimpleName()));

        assertEquals(dataElementGroupA, identifiableObjectManager.getObject(dataElementGroupIdA, OrganisationUnitGroup.class.getSimpleName()));
        assertEquals(dataElementGroupB, identifiableObjectManager.getObject(dataElementGroupIdB, OrganisationUnitGroup.class.getSimpleName()));
    }

    @Test
    public void testGetWithClasses() {
        OrganisationUnit dataElementA = createOrganisationUnit('A');
        OrganisationUnit dataElementB = createOrganisationUnit('B');
        ;

        organisationUnitService.addOrganisationUnit(dataElementA);
        organisationUnitService.addOrganisationUnit(dataElementB);

        Set<Class<? extends IdentifiableObject>> classes = ImmutableSet.<Class<? extends IdentifiableObject>>builder().
            add(Person.class).add(OrganisationUnit.class).add(OrganisationUnitGroup.class).build();

        assertEquals(dataElementA, identifiableObjectManager.get(classes, dataElementA.getUid()));
        assertEquals(dataElementB, identifiableObjectManager.get(classes, dataElementB.getUid()));
    }

//    @Test
//    public void publicAccessSetIfNoUser() {
//        OrganisationUnit dataElement = createOrganisationUnit('A');
//        identifiableObjectManager.save(dataElement);
//
//        assertNotNull(dataElement.getPublicAccess());
//        assertFalse(AccessStringHelper.canRead(dataElement.getPublicAccess()));
//        assertFalse(AccessStringHelper.canWrite(dataElement.getPublicAccess()));
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
        OrganisationUnit dataElement = createOrganisationUnit('A');
        identifiableObjectManager.save(dataElement);

        assertNotNull(identifiableObjectManager.getByName(OrganisationUnit.class, "DataElementA"));
        assertNull(identifiableObjectManager.getByName(OrganisationUnit.class, "DataElementB"));
        assertEquals(dataElement, identifiableObjectManager.getByName(OrganisationUnit.class, "DataElementA"));
    }

    @Test
    public void getAllOrderedName() {
        identifiableObjectManager.save(createOrganisationUnit('D'));
        identifiableObjectManager.save(createOrganisationUnit('B'));
        identifiableObjectManager.save(createOrganisationUnit('C'));
        identifiableObjectManager.save(createOrganisationUnit('A'));

        List<OrganisationUnit> dataElements = new ArrayList<>(identifiableObjectManager.getAllSorted(OrganisationUnit.class));

        assertEquals(4, dataElements.size());
        assertEquals("DataElementA", dataElements.get(0).getName());
        assertEquals("DataElementB", dataElements.get(1).getName());
        assertEquals("DataElementC", dataElements.get(2).getName());
        assertEquals("DataElementD", dataElements.get(3).getName());
    }

    @Test
    public void userIsCurrentIfNoUserSet() {
        User user = createUserAndInjectSecurityContext(true);

        OrganisationUnit dataElement = createOrganisationUnit('A');
        identifiableObjectManager.save(dataElement);

        assertNotNull(dataElement.getUser());
        assertEquals(user, dataElement.getUser());
    }

//    @Test
//    public void userCanCreatePublic() {
//        createUserAndInjectSecurityContext(false, "F_DATAELEMENT_PUBLIC_ADD");
//
//        OrganisationUnit dataElement = createOrganisationUnit('A');
//        identifiableObjectManager.save(dataElement);
//
//        assertNotNull(dataElement.getPublicAccess());
//        assertTrue(AccessStringHelper.canRead(dataElement.getPublicAccess()));
//        assertTrue(AccessStringHelper.canWrite(dataElement.getPublicAccess()));
//    }
//
//    @Test
//    public void userCanCreatePrivate() {
//        createUserAndInjectSecurityContext(false, "F_DATAELEMENT_PRIVATE_ADD");
//
//        OrganisationUnit dataElement = createOrganisationUnit('A');
//        identifiableObjectManager.save(dataElement);
//
//        assertNotNull(dataElement.getPublicAccess());
//        assertFalse(AccessStringHelper.canRead(dataElement.getPublicAccess()));
//        assertFalse(AccessStringHelper.canWrite(dataElement.getPublicAccess()));
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
//        OrganisationUnit dataElement = createOrganisationUnit('A');
//        dataElement.setPublicAccess(AccessStringHelper.DEFAULT);
//
//        identifiableObjectManager.save(dataElement, false);
//
//        assertFalse(AccessStringHelper.canRead(dataElement.getPublicAccess()));
//        assertFalse(AccessStringHelper.canWrite(dataElement.getPublicAccess()));
//    }
//
//    @Test
//    public void publicUserModifiedPublicAccessRW() {
//        createUserAndInjectSecurityContext(false, "F_DATAELEMENT_PUBLIC_ADD");
//
//        OrganisationUnit dataElement = createOrganisationUnit('A');
//        dataElement.setPublicAccess(AccessStringHelper.READ_WRITE);
//
//        identifiableObjectManager.save(dataElement, false);
//    }
//
//    @Test(expected = CreateAccessDeniedException.class)
//    public void privateUserModifiedPublicAccessRW() {
//        createUserAndInjectSecurityContext(false, "F_DATAELEMENT_PRIVATE_ADD");
//
//        OrganisationUnit dataElement = createOrganisationUnit('A');
//        dataElement.setPublicAccess(AccessStringHelper.READ_WRITE);
//
//        identifiableObjectManager.save(dataElement, false);
//    }
//
//    @Test
//    public void privateUserModifiedPublicAccessDEFAULT() {
//        createUserAndInjectSecurityContext(false, "F_DATAELEMENT_PRIVATE_ADD");
//
//        OrganisationUnit dataElement = createOrganisationUnit('A');
//        dataElement.setPublicAccess(AccessStringHelper.DEFAULT);
//
//        identifiableObjectManager.save(dataElement, false);
//    }
//
//    @Test(expected = UpdateAccessDeniedException.class)
//    public void updateForPrivateUserDeniedAfterChangePublicAccessRW() {
//        createUserAndInjectSecurityContext(false, "F_DATAELEMENT_PRIVATE_ADD");
//
//        OrganisationUnit dataElement = createOrganisationUnit('A');
//        dataElement.setPublicAccess(AccessStringHelper.DEFAULT);
//
//        identifiableObjectManager.save(dataElement, false);
//
//        dataElement.setPublicAccess(AccessStringHelper.READ_WRITE);
//
//        identifiableObjectManager.update(dataElement);
//    }
//
//    @Test(expected = CreateAccessDeniedException.class)
//    public void userDeniedForPublicAdd() {
//        createUserAndInjectSecurityContext(false);
//
//        OrganisationUnit dataElement = createOrganisationUnit('A');
//        dataElement.setPublicAccess(AccessStringHelper.READ_WRITE);
//
//        identifiableObjectManager.save(dataElement, false);
//    }
//
//    @Test(expected = DeleteAccessDeniedException.class)
//    public void userDeniedDeleteObject() {
//        createUserAndInjectSecurityContext(false, "F_DATAELEMENT_PUBLIC_ADD", "F_USER_ADD");
//
//        User user = createUser('B');
//        identifiableObjectManager.save(user);
//
//        OrganisationUnit dataElement = createOrganisationUnit('A');
//        identifiableObjectManager.save(dataElement);
//
//        dataElement.setUser(user);
//        dataElement.setPublicAccess(AccessStringHelper.DEFAULT);
//        sessionFactory.getCurrentSession().update(dataElement);
//
//        identifiableObjectManager.delete(dataElement);
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
//        List<OrganisationUnit> dataElements = new ArrayList<>(identifiableObjectManager.getAll(OrganisationUnit.class));
//
//        for (OrganisationUnit dataElement : dataElements) {
//            dataElement.setUser(user);
//            dataElement.setPublicAccess(AccessStringHelper.DEFAULT);
//
//            sessionFactory.getCurrentSession().update(dataElement);
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
//        List<OrganisationUnit> dataElements = new ArrayList<>(identifiableObjectManager.getAll(OrganisationUnit.class));
//
//        for (OrganisationUnit dataElement : dataElements) {
//            dataElement.setUser(user);
//            dataElement.setPublicAccess(AccessStringHelper.newInstance().build());
//
//            UserGroupAccess userGroupAccess = new UserGroupAccess();
//            userGroupAccess.setAccess(AccessStringHelper.READ);
//            userGroupAccess.setUserGroup(userGroup);
//
//            sessionFactory.getCurrentSession().save(userGroupAccess);
//
//            dataElement.getUserGroupAccesses().add(userGroupAccess);
//            sessionFactory.getCurrentSession().update(dataElement);
//        }
//
//        assertEquals(4, identifiableObjectManager.getCount(OrganisationUnit.class));
//        assertEquals(4, identifiableObjectManager.getAll(OrganisationUnit.class).size());
//    }

    @Test
    public void getByUidTest() {
        OrganisationUnit dataElementA = createOrganisationUnit('A');
        OrganisationUnit dataElementB = createOrganisationUnit('B');
        ;
        OrganisationUnit dataElementC = createOrganisationUnit('C');
        OrganisationUnit dataElementD = createOrganisationUnit('D');

        identifiableObjectManager.save(dataElementA);
        identifiableObjectManager.save(dataElementB);
        identifiableObjectManager.save(dataElementC);
        identifiableObjectManager.save(dataElementD);

        List<OrganisationUnit> ab = identifiableObjectManager.getByUid(OrganisationUnit.class, Arrays.asList(dataElementA.getUid(), dataElementB.getUid()));
        List<OrganisationUnit> cd = identifiableObjectManager.getByUid(OrganisationUnit.class, Arrays.asList(dataElementC.getUid(), dataElementD.getUid()));

        assertTrue(ab.contains(dataElementA));
        assertTrue(ab.contains(dataElementB));
        assertFalse(ab.contains(dataElementC));
        assertFalse(ab.contains(dataElementD));

        assertFalse(cd.contains(dataElementA));
        assertFalse(cd.contains(dataElementB));
        assertTrue(cd.contains(dataElementC));
        assertTrue(cd.contains(dataElementD));
    }

    @Test
    public void getByUidOrderedTest() {
        OrganisationUnit dataElementA = createOrganisationUnit('A');
        OrganisationUnit dataElementB = createOrganisationUnit('B');
        ;
        OrganisationUnit dataElementC = createOrganisationUnit('C');
        OrganisationUnit dataElementD = createOrganisationUnit('D');

        identifiableObjectManager.save(dataElementA);
        identifiableObjectManager.save(dataElementB);
        identifiableObjectManager.save(dataElementC);
        identifiableObjectManager.save(dataElementD);

        List<String> uids = Arrays.asList(dataElementA.getUid(), dataElementC.getUid(), dataElementB.getUid(), dataElementD.getUid());

        List<OrganisationUnit> expected = new ArrayList<>(Arrays.asList(dataElementA, dataElementC, dataElementB, dataElementD));

        List<OrganisationUnit> actual = new ArrayList<>(identifiableObjectManager.getByUidOrdered(OrganisationUnit.class, uids));

        assertEquals(expected, actual);
    }

    @Test
    public void testGetByCode() {
        OrganisationUnit dataElementA = createOrganisationUnit('A');
        OrganisationUnit dataElementB = createOrganisationUnit('B');
        ;
        OrganisationUnit dataElementC = createOrganisationUnit('C');
        OrganisationUnit dataElementD = createOrganisationUnit('D');

        dataElementA.setCode("DE_A");
        dataElementB.setCode("DE_B");
        dataElementC.setCode("DE_C");
        dataElementD.setCode("DE_D");

        identifiableObjectManager.save(dataElementA);
        identifiableObjectManager.save(dataElementB);
        identifiableObjectManager.save(dataElementC);
        identifiableObjectManager.save(dataElementD);

        List<OrganisationUnit> ab = identifiableObjectManager.getByCode(OrganisationUnit.class, Arrays.asList(dataElementA.getCode(), dataElementB.getCode()));
        List<OrganisationUnit> cd = identifiableObjectManager.getByCode(OrganisationUnit.class, Arrays.asList(dataElementC.getCode(), dataElementD.getCode()));

        assertTrue(ab.contains(dataElementA));
        assertTrue(ab.contains(dataElementB));
        assertFalse(ab.contains(dataElementC));
        assertFalse(ab.contains(dataElementD));

        assertFalse(cd.contains(dataElementA));
        assertFalse(cd.contains(dataElementB));
        assertTrue(cd.contains(dataElementC));
        assertTrue(cd.contains(dataElementD));
    }

    @Test
    public void getByUidNoAcl() {
        OrganisationUnit dataElementA = createOrganisationUnit('A');
        OrganisationUnit dataElementB = createOrganisationUnit('B');
        ;
        OrganisationUnit dataElementC = createOrganisationUnit('C');

        dataElementA.setCode("DE_A");
        dataElementB.setCode("DE_B");
        dataElementC.setCode("DE_C");

        OrganisationUnit unit1 = createOrganisationUnit('A');

        identifiableObjectManager.save(unit1);

        identifiableObjectManager.save(dataElementA);
        identifiableObjectManager.save(dataElementB);
        identifiableObjectManager.save(dataElementC);

        List<String> uids = Lists.newArrayList(dataElementA.getUid(), dataElementB.getUid(), dataElementC.getUid());

        List<OrganisationUnit> dataElements = identifiableObjectManager.getNoAcl(OrganisationUnit.class, uids);

        assertEquals(3, dataElements.size());
        assertTrue(dataElements.contains(dataElementA));
        assertTrue(dataElements.contains(dataElementB));
        assertTrue(dataElements.contains(dataElementC));
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
        OrganisationUnit dataElementA = createOrganisationUnit('A');
        OrganisationUnit dataElementB = createOrganisationUnit('B');
        ;

        organisationUnitService.addOrganisationUnit(dataElementA);
        organisationUnitService.addOrganisationUnit(dataElementB);

        Map<String, OrganisationUnit> map = identifiableObjectManager.getIdMap(OrganisationUnit.class, IdScheme.CODE);

        assertEquals(dataElementA, map.get("DataElementCodeA"));
        assertEquals(dataElementB, map.get("DataElementCodeB"));
        assertNull(map.get("DataElementCodeX"));
    }
}
