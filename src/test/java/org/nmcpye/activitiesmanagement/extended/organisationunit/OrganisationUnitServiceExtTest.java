package org.nmcpye.activitiesmanagement.extended.organisationunit;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.nmcpye.activitiesmanagement.AMTest;
import org.nmcpye.activitiesmanagement.IntegrationTest;
import org.nmcpye.activitiesmanagement.domain.enumeration.OrganisationUnitType;
import org.nmcpye.activitiesmanagement.domain.organisationunit.OrganisationUnit;
import org.nmcpye.activitiesmanagement.domain.organisationunit.OrganisationUnitGroup;
import org.nmcpye.activitiesmanagement.domain.organisationunit.OrganisationUnitGroupSet;
import org.nmcpye.activitiesmanagement.domain.organisationunit.OrganisationUnitLevel;
import org.nmcpye.activitiesmanagement.domain.person.Person;
import org.nmcpye.activitiesmanagement.extended.common.DeleteNotAllowedException;
import org.nmcpye.activitiesmanagement.extended.common.IdentifiableObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@IntegrationTest
public class OrganisationUnitServiceExtTest
    extends AMTest {

    @Autowired
    private OrganisationUnitServiceExt organisationUnitServiceExt;

//    @Autowired
//    private ProgramService programService;
//
//    @Autowired
//    private UserService userService;
//
//    @Autowired
//    private DataSetService dataSetService;

//    @Autowired
//    private FileResourceService fileResourceService;

    @Autowired
    private OrganisationUnitGroupServiceExt organisationUnitGroupServiceExt;

    // -------------------------------------------------------------------------
    // OrganisationUnit
    // -------------------------------------------------------------------------

    @Test
    public void testBasicOrganisationUnitCoarseGrained() {
        OrganisationUnit organisationUnit1 = createOrganisationUnit('A');

        long id1 = organisationUnitServiceExt.addOrganisationUnit(organisationUnit1);

        assertNotNull(organisationUnitServiceExt.getOrganisationUnit(id1));

        assertNull(organisationUnitServiceExt.getOrganisationUnit(-1L));

        OrganisationUnit organisationUnit2 = createOrganisationUnit('B', organisationUnit1);

        long id2 = organisationUnitServiceExt.addOrganisationUnit(organisationUnit2);

        assertTrue(organisationUnitServiceExt.getOrganisationUnit(id2).getParent().getId() == id1);

        organisationUnitServiceExt.deleteOrganisationUnit(organisationUnitServiceExt.getOrganisationUnit(id2));

        assertNotNull(organisationUnitServiceExt.getOrganisationUnit(id1));
        assertNull(organisationUnitServiceExt.getOrganisationUnit(id2));
    }

    @Test
    public void testAddAndDelOrganisationUnitInOrganisationGroup() {
        OrganisationUnit organisationUnit1 = createOrganisationUnit('A');
        OrganisationUnit organisationUnit2 = createOrganisationUnit('B');

        OrganisationUnitGroup organisationUnitGroup = createOrganisationUnitGroup('A');

        long id1 = organisationUnitServiceExt.addOrganisationUnit(organisationUnit1);
        long id2 = organisationUnitServiceExt.addOrganisationUnit(organisationUnit2);

        long groupId1 = organisationUnitGroupServiceExt.addOrganisationUnitGroup(organisationUnitGroup);

        organisationUnit1.addOrganisationUnitGroup(organisationUnitGroup);
        organisationUnit2.addOrganisationUnitGroup(organisationUnitGroup);

        organisationUnitServiceExt.updateOrganisationUnit(organisationUnit1);
        organisationUnitServiceExt.updateOrganisationUnit(organisationUnit2);

        organisationUnitGroup.addOrganisationUnit(organisationUnit1);
        organisationUnitGroup.addOrganisationUnit(organisationUnit2);

        organisationUnitGroupServiceExt.updateOrganisationUnitGroup(organisationUnitGroup);

        assertNotNull(organisationUnitServiceExt.getOrganisationUnit(id1));
        assertNotNull(organisationUnitServiceExt.getOrganisationUnit(id2));

        assertEquals("OrganisationUnitGroupA",
            organisationUnitGroupServiceExt.getOrganisationUnitGroup(groupId1).getName());

        organisationUnitServiceExt.deleteOrganisationUnit(organisationUnitServiceExt.getOrganisationUnit(id1));

        assertNull(organisationUnitServiceExt.getOrganisationUnit(id1));
        assertNotNull(organisationUnitServiceExt.getOrganisationUnit(id2));
        assertFalse(organisationUnitGroupServiceExt.getOrganisationUnitGroup(groupId1).getMembers()
            .contains(organisationUnit1));
        assertTrue(organisationUnitGroupServiceExt.getOrganisationUnitGroup(groupId1).getMembers()
            .contains(organisationUnit2));
    }

    @Test
    public void testAddAndDelOrganisationUnitWithParent() {
        OrganisationUnit organisationUnit1 = createOrganisationUnit('A');
        OrganisationUnit organisationUnit2 = createOrganisationUnit('B');

        long id1 = organisationUnitServiceExt.addOrganisationUnit(organisationUnit1);
        long id2 = organisationUnitServiceExt.addOrganisationUnit(organisationUnit2);

        organisationUnit1.setChildren(Sets.newHashSet(organisationUnit2));
        organisationUnit2.setParent(organisationUnit1);

        organisationUnitServiceExt.updateOrganisationUnit(organisationUnit1);

        assertNotNull(organisationUnitServiceExt.getOrganisationUnit(id1));
        assertNotNull(organisationUnitServiceExt.getOrganisationUnit(id2));

        organisationUnitServiceExt.deleteOrganisationUnit(organisationUnitServiceExt.getOrganisationUnit(id2));

        assertNull(organisationUnitServiceExt.getOrganisationUnit(id2));
        assertNotNull(organisationUnitServiceExt.getOrganisationUnit(id1));

        OrganisationUnit ou = organisationUnitServiceExt.getOrganisationUnit(id1);
        assertTrue(organisationUnitServiceExt.getOrganisationUnit(id1).getChildren().isEmpty());
    }

    @Test
    public void testAddAndDelOrganisationUnitWithChildren() {
        OrganisationUnit organisationUnit1 = createOrganisationUnit('A');
        OrganisationUnit organisationUnit2 = createOrganisationUnit('B');

        long id1 = organisationUnitServiceExt.addOrganisationUnit(organisationUnit1);
        long id2 = organisationUnitServiceExt.addOrganisationUnit(organisationUnit2);

        organisationUnit1.setChildren(Sets.newHashSet(organisationUnit2));
        organisationUnit2.setParent(organisationUnit1);

        organisationUnitServiceExt.updateOrganisationUnit(organisationUnit1);

        organisationUnitServiceExt.updatePaths();

        assertNotNull(organisationUnitServiceExt.getOrganisationUnit(id1));
        assertNotNull(organisationUnitServiceExt.getOrganisationUnit(id2));

        Assertions.assertThrows(DeleteNotAllowedException.class, () -> {
            organisationUnitServiceExt.deleteOrganisationUnit(organisationUnitServiceExt.getOrganisationUnit(id1));
        });
    }

    // TODO Enable when converting DataSet into identifiable Object
//    @Test
//    public void testAddAndDelOrganisationUnitWithPrgUserAndDataSet()
//    {
//        OrganisationUnit organisationUnit1 = createOrganisationUnit( 'A' );
//        Program program = createProgram( 'A' );
//        Person user = createUserCredentials( 'A', null );
//        DataSet dataSet = createDataSet( 'A' );
//
//        long id1 = organisationUnitService.addOrganisationUnit( organisationUnit1 );
//        long programId = programService.addProgram( program );
//        long userId = userService.addUser( user );
//        long dataSetId = dataSetService.addDataSet( dataSet );
//
//        program.getOrganisationUnits().add( organisationUnit1 );
//        organisationUnit1.getPrograms().add( program );
//
//        user.addOrganisationUnit( organisationUnit1 );
//        organisationUnit1.addUser( user );
//
//        dataSet.addOrganisationUnit( organisationUnit1 );
//        organisationUnit1.addDataSet( dataSet );
//
//        organisationUnitService.updateOrganisationUnit( organisationUnit1 );
//        programService.updateProgram( program );
//        userService.updateUser( user );
//        dataSetService.updateDataSet( dataSet );
//
//        assertNotNull( organisationUnitService.getOrganisationUnit( id1 ) );
//
//        organisationUnitService.deleteOrganisationUnit( organisationUnitService.getOrganisationUnit( id1 ) );
//
//        assertNull( organisationUnitService.getOrganisationUnit( id1 ) );
//        assertTrue( programService.getProgram( programId ).getOrganisationUnits().isEmpty() );
//        assertTrue( userService.getUser( userId ).getOrganisationUnits().isEmpty() );
//        assertTrue( dataSetService.getDataSet( dataSetId ).getSources().isEmpty() );
//    }

    @Test
    public void testUpdateOrganisationUnit() {
        String updatedName = "updatedName";
        String updatedShortName = "updatedShortName";

        OrganisationUnit organisationUnit = createOrganisationUnit('A');

        long id = organisationUnitServiceExt.addOrganisationUnit(organisationUnit);

        organisationUnit.setName(updatedName);
        organisationUnit.setShortName(updatedShortName);

        organisationUnitServiceExt.updateOrganisationUnit(organisationUnit);

        OrganisationUnit updatedOrganisationUnit = organisationUnitServiceExt.getOrganisationUnit(id);

        assertEquals(updatedOrganisationUnit.getName(), updatedName);
        assertEquals(updatedOrganisationUnit.getShortName(), updatedShortName);
    }

    @Test
    public void testGetOrganisationUnitWithChildren()
        throws Exception {
        OrganisationUnit unit1 = createOrganisationUnit('A');
        OrganisationUnit unit2 = createOrganisationUnit('B', unit1);
        OrganisationUnit unit3 = createOrganisationUnit('C', unit2);
        OrganisationUnit unit4 = createOrganisationUnit('D');

        long id1 = organisationUnitServiceExt.addOrganisationUnit(unit1);
        unit1.getChildren().add(unit2);
        organisationUnitServiceExt.addOrganisationUnit(unit2);
        organisationUnitServiceExt.addOrganisationUnit(unit3);
        organisationUnitServiceExt.addOrganisationUnit(unit4);

        organisationUnitServiceExt.updatePaths();

        List<OrganisationUnit> actual = new ArrayList<>(
            organisationUnitServiceExt.getOrganisationUnitWithChildren(id1));

        assertEquals(3, actual.size());
        assertTrue(actual.contains(unit1));
        assertTrue(actual.contains(unit2));
    }

    @Test
    public void testGetOrganisationUnitWithChildrenB() {
        OrganisationUnit unitA = createOrganisationUnit('A');
        OrganisationUnit unitB = createOrganisationUnit('B', unitA);
        OrganisationUnit unitC = createOrganisationUnit('C', unitB);
        OrganisationUnit unitD = createOrganisationUnit('D', unitB);
        OrganisationUnit unitE = createOrganisationUnit('E', unitC);

        long idA = organisationUnitServiceExt.addOrganisationUnit(unitA);
        long idB = organisationUnitServiceExt.addOrganisationUnit(unitB);
        long idC = organisationUnitServiceExt.addOrganisationUnit(unitC);
        long idD = organisationUnitServiceExt.addOrganisationUnit(unitD);
        organisationUnitServiceExt.addOrganisationUnit(unitE);

        organisationUnitServiceExt.updatePaths();

        List<OrganisationUnit> actualA = new ArrayList<>(
            organisationUnitServiceExt.getOrganisationUnitWithChildren(idA));
        List<OrganisationUnit> actualB = new ArrayList<>(
            organisationUnitServiceExt.getOrganisationUnitWithChildren(idB));
        List<OrganisationUnit> actualC = new ArrayList<>(
            organisationUnitServiceExt.getOrganisationUnitWithChildren(idC));
        List<OrganisationUnit> actualD = new ArrayList<>(
            organisationUnitServiceExt.getOrganisationUnitWithChildren(idD));

        assertEquals(5, actualA.size());
        assertEquals(4, actualB.size());
        assertEquals(2, actualC.size());
        assertEquals(1, actualD.size());

        assertEquals(Sets.newHashSet(unitB, unitC, unitD, unitE), Sets.newHashSet(actualB));
        assertEquals(Sets.newHashSet(unitC, unitE), Sets.newHashSet(actualC));
    }

    @Test
    public void testGetOrganisationUnitLevel() {
        OrganisationUnit unitA = createOrganisationUnit('A');
        OrganisationUnit unitB = createOrganisationUnit('B', unitA);
        OrganisationUnit unitC = createOrganisationUnit('C', unitB);
        OrganisationUnit unitD = createOrganisationUnit('D', unitB);
        OrganisationUnit unitE = createOrganisationUnit('E', unitC);

        organisationUnitServiceExt.addOrganisationUnit(unitA);
        organisationUnitServiceExt.addOrganisationUnit(unitB);
        organisationUnitServiceExt.addOrganisationUnit(unitC);
        organisationUnitServiceExt.addOrganisationUnit(unitD);
        organisationUnitServiceExt.addOrganisationUnit(unitE);

        organisationUnitServiceExt.updatePaths();

        assertEquals(1, unitA.getLevel());
        assertEquals(2, unitB.getLevel());
        assertEquals(3, unitC.getLevel());
        assertEquals(3, unitD.getLevel());
        assertEquals(4, unitE.getLevel());
    }

    @Test
    public void testGetOrganisationUnitWithChildrenMaxLevel() {
        OrganisationUnit unit1 = createOrganisationUnit('A');
        OrganisationUnit unit2 = createOrganisationUnit('B', unit1);
        OrganisationUnit unit3 = createOrganisationUnit('C', unit1);
        OrganisationUnit unit4 = createOrganisationUnit('D', unit2);
        OrganisationUnit unit5 = createOrganisationUnit('E', unit2);
        OrganisationUnit unit6 = createOrganisationUnit('F', unit3);
        OrganisationUnit unit7 = createOrganisationUnit('G', unit3);

        unit1.getChildren().add(unit2);
        unit1.getChildren().add(unit3);
        unit2.getChildren().add(unit4);
        unit2.getChildren().add(unit5);
        unit3.getChildren().add(unit6);
        unit3.getChildren().add(unit7);

        long id1 = organisationUnitServiceExt.addOrganisationUnit(unit1);
        long id2 = organisationUnitServiceExt.addOrganisationUnit(unit2);
        organisationUnitServiceExt.addOrganisationUnit(unit3);
        long id4 = organisationUnitServiceExt.addOrganisationUnit(unit4);
        organisationUnitServiceExt.addOrganisationUnit(unit5);
        organisationUnitServiceExt.addOrganisationUnit(unit6);
        organisationUnitServiceExt.addOrganisationUnit(unit7);

        organisationUnitServiceExt.updatePaths();

        List<OrganisationUnit> actual = new ArrayList<>(
            organisationUnitServiceExt.getOrganisationUnitWithChildren(id1, 0));
        assertEquals(0, actual.size());

        actual = new ArrayList<>(organisationUnitServiceExt.getOrganisationUnitWithChildren(id1, 1));
        assertEquals(1, actual.size());
        assertTrue(actual.contains(unit1));

        actual = new ArrayList<>(organisationUnitServiceExt.getOrganisationUnitWithChildren(id1, 2));
        assertEquals(3, actual.size());
        assertTrue(actual.contains(unit1));
        assertTrue(actual.contains(unit2));
        assertTrue(actual.contains(unit3));

        actual = new ArrayList<>(organisationUnitServiceExt.getOrganisationUnitWithChildren(id1, 3));
        assertEquals(7, actual.size());

        actual = new ArrayList<>(organisationUnitServiceExt.getOrganisationUnitWithChildren(id1, 8));
        assertEquals(7, actual.size());

        actual = new ArrayList<>(organisationUnitServiceExt.getOrganisationUnitWithChildren(id2, 1));
        assertEquals(1, actual.size());

        actual = new ArrayList<>(organisationUnitServiceExt.getOrganisationUnitWithChildren(id2, 2));
        assertEquals(3, actual.size());

        actual = new ArrayList<>(organisationUnitServiceExt.getOrganisationUnitWithChildren(id4, 1));
        assertEquals(1, actual.size());

        actual = new ArrayList<>(organisationUnitServiceExt.getOrganisationUnitWithChildren(id4, 8));
        assertEquals(1, actual.size());
    }

    @Test
    public void testGetOrganisationUnitsByFields() {
        String oU1Name = "OU1name";
        String oU2Name = "OU2name";
        String oU3Name = "OU3name";
        String oU1ShortName = "OU1ShortName";
        String oU2ShortName = "OU2ShortName";
        String oU3ShortName = "OU3ShortName";
        String oU1Code = "OU1Code";
        String oU2Code = "OU2Code";
        String oU3Code = "OU3Code";

        OrganisationUnit organisationUnit1 = new OrganisationUnit(oU1Name, null, oU1ShortName, oU1Code, new Date(),
            null, null, OrganisationUnitType.DISTRICT);
        OrganisationUnit organisationUnit2 = new OrganisationUnit(oU2Name, null, oU2ShortName, oU2Code, new Date(),
            null, null, OrganisationUnitType.DISTRICT);
        OrganisationUnit organisationUnit3 = new OrganisationUnit(oU3Name, null, oU3ShortName, oU3Code, new Date(),
            null, null, OrganisationUnitType.DISTRICT);

        organisationUnitServiceExt.addOrganisationUnit(organisationUnit1);
        organisationUnitServiceExt.addOrganisationUnit(organisationUnit2);
        organisationUnitServiceExt.addOrganisationUnit(organisationUnit3);

        OrganisationUnit unit1 = organisationUnitServiceExt.getOrganisationUnitByName(oU1Name).get(0);
        assertEquals(unit1.getName(), oU1Name);

        List<OrganisationUnit> units = organisationUnitServiceExt.getOrganisationUnitByName("foo");
        assertTrue(units.isEmpty());

        unit1 = organisationUnitServiceExt.getOrganisationUnitByCode(oU1Code);
        assertEquals(unit1.getName(), oU1Name);

        OrganisationUnit unit4 = organisationUnitServiceExt.getOrganisationUnitByCode("foo");
        assertNull(unit4);
    }

    @Test
    public void testGetAllOrganisationUnitsAndGetRootOrganisationUnit() {
        // creating a tree with two roots ( id1 and id4 )

        OrganisationUnit unit1 = createOrganisationUnit('A');
        OrganisationUnit unit2 = createOrganisationUnit('B', unit1);
        OrganisationUnit unit3 = createOrganisationUnit('C', unit1);
        OrganisationUnit unit4 = createOrganisationUnit('D');
        OrganisationUnit unit5 = createOrganisationUnit('E', unit4);

        organisationUnitServiceExt.addOrganisationUnit(unit1);
        organisationUnitServiceExt.addOrganisationUnit(unit2);
        organisationUnitServiceExt.addOrganisationUnit(unit3);
        organisationUnitServiceExt.addOrganisationUnit(unit4);
        organisationUnitServiceExt.addOrganisationUnit(unit5);

        List<OrganisationUnit> units = organisationUnitServiceExt.getAllOrganisationUnits();

        assertNotNull(units);
        assertEquals(5, units.size());
        assertTrue(units.contains(unit1));
        assertTrue(units.contains(unit2));
        assertTrue(units.contains(unit3));
        assertTrue(units.contains(unit4));
        assertTrue(units.contains(unit5));

        units = organisationUnitServiceExt.getRootOrganisationUnits();

        assertNotNull(units);
        assertEquals(2, units.size());
        assertTrue(units.contains(unit1));
        assertTrue(units.contains(unit4));
    }

    @Test
    public void testGetOrganisationUnitsAtLevel() {
        OrganisationUnitLevel levelA = new OrganisationUnitLevel(1, "National");
        OrganisationUnitLevel levelB = new OrganisationUnitLevel(2, "Province");
        OrganisationUnitLevel levelC = new OrganisationUnitLevel(3, "County");
        OrganisationUnitLevel levelD = new OrganisationUnitLevel(4, "District");

        organisationUnitServiceExt.addOrganisationUnitLevel(levelA);
        organisationUnitServiceExt.addOrganisationUnitLevel(levelB);
        organisationUnitServiceExt.addOrganisationUnitLevel(levelC);
        organisationUnitServiceExt.addOrganisationUnitLevel(levelD);

        OrganisationUnit unit1 = createOrganisationUnit('1');
        organisationUnitServiceExt.addOrganisationUnit(unit1);

        OrganisationUnit unit2 = createOrganisationUnit('2', unit1);
        unit1.getChildren().add(unit2);
        organisationUnitServiceExt.addOrganisationUnit(unit2);

        OrganisationUnit unit3 = createOrganisationUnit('3', unit2);
        unit2.getChildren().add(unit3);
        organisationUnitServiceExt.addOrganisationUnit(unit3);

        OrganisationUnit unit4 = createOrganisationUnit('4', unit2);
        unit2.getChildren().add(unit4);
        organisationUnitServiceExt.addOrganisationUnit(unit4);

        OrganisationUnit unit5 = createOrganisationUnit('5', unit2);
        unit2.getChildren().add(unit5);
        organisationUnitServiceExt.addOrganisationUnit(unit5);

        OrganisationUnit unit6 = createOrganisationUnit('6', unit3);
        unit3.getChildren().add(unit6);
        organisationUnitServiceExt.addOrganisationUnit(unit6);

        OrganisationUnit unit7 = createOrganisationUnit('7');
        organisationUnitServiceExt.addOrganisationUnit(unit7);

        organisationUnitServiceExt.updatePaths();

        // unit1
        // unit1 . unit2
        // unit1 . unit2 . unit3
        // unit1 . unit2 . unit4
        // unit1 . unit2 . unit5
        // unit1 . unit2 . unit3 . unit6
        // unit7

        assertEquals(2, organisationUnitServiceExt.getOrganisationUnitsAtLevel(1).size());
        assertEquals(3, organisationUnitServiceExt.getOrganisationUnitsAtLevel(3).size());
        assertEquals(4, organisationUnitServiceExt.getNumberOfOrganisationalLevels());
        assertTrue(unit4.getLevel() == 3);
        assertTrue(unit1.getLevel() == 1);
        assertTrue(unit6.getLevel() == 4);

        assertEquals(3, organisationUnitServiceExt
            .getOrganisationUnitsAtLevels(Lists.newArrayList(3), Lists.newArrayList(unit1)).size());
        assertEquals(3, organisationUnitServiceExt
            .getOrganisationUnitsAtLevels(Lists.newArrayList(3), Lists.newArrayList(unit2)).size());
        assertEquals(0, organisationUnitServiceExt
            .getOrganisationUnitsAtLevels(Lists.newArrayList(3), Lists.newArrayList(unit7)).size());
        assertEquals(4, organisationUnitServiceExt
            .getOrganisationUnitsAtLevels(Lists.newArrayList(3, 4), Lists.newArrayList(unit1)).size());
        assertEquals(4, organisationUnitServiceExt
            .getOrganisationUnitsAtLevels(Lists.newArrayList(3, 4), Lists.newArrayList(unit1, unit7)).size());

        assertEquals(3, organisationUnitServiceExt
            .getOrganisationUnitsAtOrgUnitLevels(Lists.newArrayList(levelC), Lists.newArrayList(unit1)).size());
        assertEquals(3, organisationUnitServiceExt
            .getOrganisationUnitsAtOrgUnitLevels(Lists.newArrayList(levelC), Lists.newArrayList(unit2)).size());
        assertEquals(0, organisationUnitServiceExt
            .getOrganisationUnitsAtOrgUnitLevels(Lists.newArrayList(levelC), Lists.newArrayList(unit7)).size());
        assertEquals(4, organisationUnitServiceExt
            .getOrganisationUnitsAtOrgUnitLevels(Lists.newArrayList(levelC, levelD), Lists.newArrayList(unit1))
            .size());
        assertEquals(4,
            organisationUnitServiceExt.getOrganisationUnitsAtOrgUnitLevels(Lists.newArrayList(levelC, levelD),
                Lists.newArrayList(unit1, unit7)).size());
    }

    @Test
    public void testGetNumberOfOrganisationalLevels() {
        assertEquals(0, organisationUnitServiceExt.getNumberOfOrganisationalLevels());

        OrganisationUnit unit1 = createOrganisationUnit('1');
        organisationUnitServiceExt.addOrganisationUnit(unit1);

        OrganisationUnit unit2 = createOrganisationUnit('2', unit1);
        unit1.getChildren().add(unit2);
        organisationUnitServiceExt.addOrganisationUnit(unit2);

        organisationUnitServiceExt.updatePaths();

        assertEquals(2, organisationUnitServiceExt.getNumberOfOrganisationalLevels());

        OrganisationUnit unit3 = createOrganisationUnit('3', unit2);
        unit2.getChildren().add(unit3);
        organisationUnitServiceExt.addOrganisationUnit(unit3);

        organisationUnitServiceExt.updatePaths();

        OrganisationUnit unit4 = createOrganisationUnit('4', unit2);
        unit2.getChildren().add(unit4);
        organisationUnitServiceExt.addOrganisationUnit(unit4);

        organisationUnitServiceExt.updatePaths();

        assertEquals(3, organisationUnitServiceExt.getNumberOfOrganisationalLevels());
    }

    @Test
    public void testIsDescendantSet() {
        OrganisationUnit unit1 = createOrganisationUnit('1');
        organisationUnitServiceExt.addOrganisationUnit(unit1);

        OrganisationUnit unit2 = createOrganisationUnit('2', unit1);
        unit1.getChildren().add(unit2);
        organisationUnitServiceExt.addOrganisationUnit(unit2);

        OrganisationUnit unit3 = createOrganisationUnit('3', unit2);
        unit2.getChildren().add(unit3);
        organisationUnitServiceExt.addOrganisationUnit(unit3);

        OrganisationUnit unit4 = createOrganisationUnit('4');
        organisationUnitServiceExt.addOrganisationUnit(unit4);

        assertTrue(unit1.isDescendant(Sets.newHashSet(unit1)));
        assertTrue(unit2.isDescendant(Sets.newHashSet(unit1)));
        assertTrue(unit3.isDescendant(Sets.newHashSet(unit1)));
        assertTrue(unit2.isDescendant(Sets.newHashSet(unit1, unit3)));

        assertFalse(unit2.isDescendant(Sets.newHashSet(unit3)));
        assertFalse(unit4.isDescendant(Sets.newHashSet(unit1)));
    }

    @Test
    public void testIsDescendantObject() {
        OrganisationUnit unit1 = createOrganisationUnit('1');
        organisationUnitServiceExt.addOrganisationUnit(unit1);

        OrganisationUnit unit2 = createOrganisationUnit('2', unit1);
        unit1.getChildren().add(unit2);
        organisationUnitServiceExt.addOrganisationUnit(unit2);

        OrganisationUnit unit3 = createOrganisationUnit('3', unit2);
        unit2.getChildren().add(unit3);
        organisationUnitServiceExt.addOrganisationUnit(unit3);

        OrganisationUnit unit4 = createOrganisationUnit('4');
        organisationUnitServiceExt.addOrganisationUnit(unit4);

        assertTrue(unit1.isDescendant(unit1));
        assertTrue(unit2.isDescendant(unit1));
        assertTrue(unit3.isDescendant(unit1));

        assertFalse(unit2.isDescendant(unit3));
        assertFalse(unit4.isDescendant(unit1));
    }

    @Test
    public void testGetOrganisationUnitAtLevelAndBranch()
        throws Exception {
        OrganisationUnit unitA = createOrganisationUnit('A');
        OrganisationUnit unitB = createOrganisationUnit('B', unitA);
        OrganisationUnit unitC = createOrganisationUnit('C', unitA);
        OrganisationUnit unitD = createOrganisationUnit('D', unitB);
        OrganisationUnit unitE = createOrganisationUnit('E', unitB);
        OrganisationUnit unitF = createOrganisationUnit('F', unitC);
        OrganisationUnit unitG = createOrganisationUnit('G', unitC);
        OrganisationUnit unitH = createOrganisationUnit('H', unitD);
        OrganisationUnit unitI = createOrganisationUnit('I', unitD);
        OrganisationUnit unitJ = createOrganisationUnit('J', unitE);
        OrganisationUnit unitK = createOrganisationUnit('K', unitE);
        OrganisationUnit unitL = createOrganisationUnit('L', unitF);
        OrganisationUnit unitM = createOrganisationUnit('M', unitF);
        OrganisationUnit unitN = createOrganisationUnit('N', unitG);
        OrganisationUnit unitO = createOrganisationUnit('O', unitG);

        organisationUnitServiceExt.addOrganisationUnit(unitA);
        organisationUnitServiceExt.addOrganisationUnit(unitB);
        organisationUnitServiceExt.addOrganisationUnit(unitC);
        organisationUnitServiceExt.addOrganisationUnit(unitD);
        organisationUnitServiceExt.addOrganisationUnit(unitE);
        organisationUnitServiceExt.addOrganisationUnit(unitF);
        organisationUnitServiceExt.addOrganisationUnit(unitG);
        organisationUnitServiceExt.addOrganisationUnit(unitH);
        organisationUnitServiceExt.addOrganisationUnit(unitI);
        organisationUnitServiceExt.addOrganisationUnit(unitJ);
        organisationUnitServiceExt.addOrganisationUnit(unitK);
        organisationUnitServiceExt.addOrganisationUnit(unitL);
        organisationUnitServiceExt.addOrganisationUnit(unitM);
        organisationUnitServiceExt.addOrganisationUnit(unitN);
        organisationUnitServiceExt.addOrganisationUnit(unitO);

        organisationUnitServiceExt.updatePaths();

        assertEquals(Sets.newHashSet(unitB),
            Sets.newHashSet(organisationUnitServiceExt.getOrganisationUnitsAtLevel(2, unitB)));
        assertEquals(Sets.newHashSet(unitD, unitE),
            Sets.newHashSet(organisationUnitServiceExt.getOrganisationUnitsAtLevel(3, unitB)));
        assertEquals(Sets.newHashSet(unitH, unitI, unitJ, unitK),
            Sets.newHashSet(organisationUnitServiceExt.getOrganisationUnitsAtLevel(4, unitB)));

        assertEquals(2, unitB.getLevel());
        assertEquals(3, unitD.getLevel());
        assertEquals(3, unitE.getLevel());
        assertEquals(4, unitH.getLevel());
        assertEquals(4, unitI.getLevel());
        assertEquals(4, unitJ.getLevel());
        assertEquals(4, unitK.getLevel());
    }

    @Test
    public void testGetOrganisationUnitAtLevelAndBranches() {
        OrganisationUnit unitA = createOrganisationUnit('A');
        OrganisationUnit unitB = createOrganisationUnit('B', unitA);
        OrganisationUnit unitC = createOrganisationUnit('C', unitA);
        OrganisationUnit unitD = createOrganisationUnit('D', unitB);
        OrganisationUnit unitE = createOrganisationUnit('E', unitB);
        OrganisationUnit unitF = createOrganisationUnit('F', unitC);
        OrganisationUnit unitG = createOrganisationUnit('G', unitC);
        OrganisationUnit unitH = createOrganisationUnit('H', unitD);
        OrganisationUnit unitI = createOrganisationUnit('I', unitD);
        OrganisationUnit unitJ = createOrganisationUnit('J', unitE);
        OrganisationUnit unitK = createOrganisationUnit('K', unitE);
        OrganisationUnit unitL = createOrganisationUnit('L', unitF);
        OrganisationUnit unitM = createOrganisationUnit('M', unitF);
        OrganisationUnit unitN = createOrganisationUnit('N', unitG);
        OrganisationUnit unitO = createOrganisationUnit('O', unitG);

        unitA.getChildren().add(unitB);
        unitA.getChildren().add(unitC);
        unitB.getChildren().add(unitD);
        unitB.getChildren().add(unitE);
        unitC.getChildren().add(unitF);
        unitC.getChildren().add(unitG);
        unitD.getChildren().add(unitH);
        unitD.getChildren().add(unitI);
        unitE.getChildren().add(unitJ);
        unitE.getChildren().add(unitK);
        unitF.getChildren().add(unitL);
        unitF.getChildren().add(unitM);
        unitG.getChildren().add(unitN);
        unitG.getChildren().add(unitO);

        organisationUnitServiceExt.addOrganisationUnit(unitA);
        organisationUnitServiceExt.addOrganisationUnit(unitB);
        organisationUnitServiceExt.addOrganisationUnit(unitC);
        organisationUnitServiceExt.addOrganisationUnit(unitD);
        organisationUnitServiceExt.addOrganisationUnit(unitE);
        organisationUnitServiceExt.addOrganisationUnit(unitF);
        organisationUnitServiceExt.addOrganisationUnit(unitG);
        organisationUnitServiceExt.addOrganisationUnit(unitH);
        organisationUnitServiceExt.addOrganisationUnit(unitI);
        organisationUnitServiceExt.addOrganisationUnit(unitJ);
        organisationUnitServiceExt.addOrganisationUnit(unitK);
        organisationUnitServiceExt.addOrganisationUnit(unitL);
        organisationUnitServiceExt.addOrganisationUnit(unitM);
        organisationUnitServiceExt.addOrganisationUnit(unitN);
        organisationUnitServiceExt.addOrganisationUnit(unitO);

        organisationUnitServiceExt.updatePaths();

        List<OrganisationUnit> unitsA = new ArrayList<>(Arrays.asList(unitB, unitC));
        List<OrganisationUnit> unitsB = new ArrayList<>(Arrays.asList(unitD, unitE));

        assertEquals(Sets.newHashSet(unitD, unitE, unitF, unitG),
            Sets.newHashSet(organisationUnitServiceExt.getOrganisationUnitsAtLevels(Sets.newHashSet(3), unitsA)));
        assertEquals(Sets.newHashSet(unitH, unitI, unitJ, unitK, unitL, unitM, unitN, unitO),
            Sets.newHashSet(organisationUnitServiceExt.getOrganisationUnitsAtLevels(Sets.newHashSet(4), unitsA)));
        assertEquals(Sets.newHashSet(unitH, unitI, unitJ, unitK),
            Sets.newHashSet(organisationUnitServiceExt.getOrganisationUnitsAtLevels(Sets.newHashSet(4), unitsB)));

        assertEquals(2, unitB.getLevel());
        assertEquals(3, unitD.getLevel());
        assertEquals(3, unitE.getLevel());
        assertEquals(4, unitH.getLevel());
        assertEquals(4, unitI.getLevel());
        assertEquals(4, unitJ.getLevel());
        assertEquals(4, unitK.getLevel());
    }

    // -------------------------------------------------------------------------
    // OrganisationUnitGroup
    // -------------------------------------------------------------------------

    @Test
    public void testAddAndDelOrganisationUnitGroup() {
        OrganisationUnitGroup organisationUnitGroup1 = new OrganisationUnitGroup("OUGname");

        long id1 = organisationUnitGroupServiceExt.addOrganisationUnitGroup(organisationUnitGroup1);

        // assert getOrganisationUnitGroup
        assertNotNull(organisationUnitGroupServiceExt.getOrganisationUnitGroup(id1));

        assertEquals(organisationUnitGroupServiceExt.getOrganisationUnitGroup(id1).getName(), "OUGname");

        organisationUnitGroupServiceExt
            .deleteOrganisationUnitGroup(organisationUnitGroupServiceExt.getOrganisationUnitGroup(id1));

        // assert delOrganisationUnitGroup
        assertNull(organisationUnitGroupServiceExt.getOrganisationUnitGroup(id1));
    }

    @Test
    public void testAddAndDelOrganisationUnitGroupInOrganisationGroupSet() {
        OrganisationUnitGroup organisationUnitGroup1 = new OrganisationUnitGroup("OUGname");
        OrganisationUnitGroup organisationUnitGroup2 = new OrganisationUnitGroup("OUGname2");

        OrganisationUnitGroupSet organisationUnitGroupSet1 = new OrganisationUnitGroupSet();
        organisationUnitGroupSet1.setName("ougs1");
        organisationUnitGroupSet1.setShortName("ougs1");
        organisationUnitGroupSet1.setCompulsory(true);

        long id1 = organisationUnitGroupServiceExt.addOrganisationUnitGroup(organisationUnitGroup1);
        long id2 = organisationUnitGroupServiceExt.addOrganisationUnitGroup(organisationUnitGroup2);

        long setId1 = organisationUnitGroupServiceExt.addOrganisationUnitGroupSet(organisationUnitGroupSet1);

        organisationUnitGroup1.setGroupSets(Sets.newHashSet(organisationUnitGroupSet1));
        organisationUnitGroup2.setGroupSets(Sets.newHashSet(organisationUnitGroupSet1));

        organisationUnitGroupServiceExt.updateOrganisationUnitGroup(organisationUnitGroup1);
        organisationUnitGroupServiceExt.updateOrganisationUnitGroup(organisationUnitGroup2);

        organisationUnitGroupSet1.getOrganisationUnitGroups().add(organisationUnitGroup1);
        organisationUnitGroupSet1.getOrganisationUnitGroups().add(organisationUnitGroup2);

        organisationUnitGroupServiceExt.updateOrganisationUnitGroupSet(organisationUnitGroupSet1);

        organisationUnitServiceExt.updatePaths();

        // assert getOrganisationUnitGroup
        assertNotNull(organisationUnitGroupServiceExt.getOrganisationUnitGroup(id1));
        assertNotNull(organisationUnitGroupServiceExt.getOrganisationUnitGroup(id2));

        assertEquals("OUGname", organisationUnitGroupServiceExt.getOrganisationUnitGroup(id1).getName());
        assertEquals("OUGname2", organisationUnitGroupServiceExt.getOrganisationUnitGroup(id2).getName());

        organisationUnitGroupServiceExt
            .deleteOrganisationUnitGroup(organisationUnitGroupServiceExt.getOrganisationUnitGroup(id1));

        organisationUnitServiceExt.updatePaths();

        // assert delOrganisationUnitGroup
        assertNull(organisationUnitGroupServiceExt.getOrganisationUnitGroup(id1));
        assertNotNull(organisationUnitGroupServiceExt.getOrganisationUnitGroup(id2));
        assertFalse(organisationUnitGroupServiceExt.getOrganisationUnitGroupSet(setId1).getOrganisationUnitGroups()
            .contains(organisationUnitGroup1));
        assertTrue(organisationUnitGroupServiceExt.getOrganisationUnitGroupSet(setId1).getOrganisationUnitGroups()
            .contains(organisationUnitGroup2));
    }

    @Test
    public void testUpdateOrganisationUnitGroup() {
        OrganisationUnitGroup organisationUnitGroup = new OrganisationUnitGroup("OUGname");

        OrganisationUnit organisationUnit1 = createOrganisationUnit('A');
        OrganisationUnit organisationUnit2 = createOrganisationUnit('B');

        organisationUnitGroup.getMembers().add(organisationUnit1);
        organisationUnitGroup.getMembers().add(organisationUnit2);

        organisationUnitServiceExt.addOrganisationUnit(organisationUnit1);
        organisationUnitServiceExt.addOrganisationUnit(organisationUnit2);

        long ougid = organisationUnitGroupServiceExt.addOrganisationUnitGroup(organisationUnitGroup);

        assertTrue(organisationUnitGroupServiceExt.getOrganisationUnitGroup(ougid).getMembers().size() == 2);

        organisationUnitGroup.getMembers().remove(organisationUnit1);

        organisationUnitGroupServiceExt.updateOrganisationUnitGroup(organisationUnitGroup);

        assertTrue(organisationUnitGroupServiceExt.getOrganisationUnitGroup(ougid).getMembers().size() == 1);
    }

    @Test
    public void testGetAllOrganisationUnitGroups() {
        OrganisationUnitGroup group1 = new OrganisationUnitGroup("organisationUnitGroupName1");
        OrganisationUnitGroup group2 = new OrganisationUnitGroup("organisationUnitGroupName2");
        OrganisationUnitGroup group3 = new OrganisationUnitGroup("organisationUnitGroupName3");
        OrganisationUnitGroup group4 = new OrganisationUnitGroup("organisationUnitGroupName4");

        List<OrganisationUnitGroup> groups = new ArrayList<>();
        groups.add(group1);
        groups.add(group2);
        groups.add(group3);
        groups.add(group4);

        ArrayList<Long> groupIds = new ArrayList<>();

        for (OrganisationUnitGroup group : groups) {
            groupIds.add(organisationUnitGroupServiceExt.addOrganisationUnitGroup(group));
        }

        List<OrganisationUnitGroup> fetchedGroups = organisationUnitGroupServiceExt.getAllOrganisationUnitGroups();

        ArrayList<Long> fetchedGroupIds = new ArrayList<>();

        for (OrganisationUnitGroup group : fetchedGroups) {
            fetchedGroupIds.add(group.getId());
        }

        assertTrue(fetchedGroups.size() == 4);
        assertTrue(fetchedGroups.containsAll(groups));

        assertTrue(fetchedGroupIds.size() == 4);
        assertTrue(fetchedGroupIds.containsAll(groupIds));
    }

    // -------------------------------------------------------------------------
    // OrganisationUnitGroupSets
    // -------------------------------------------------------------------------

    @Test
    public void testOrganisationUnitGroupSetsBasic() {
        OrganisationUnitGroup organisationUnitGroup1 = new OrganisationUnitGroup();
        organisationUnitGroup1.setName("oug1");
        OrganisationUnitGroup organisationUnitGroup2 = new OrganisationUnitGroup();
        organisationUnitGroup2.setName("oug2");
        OrganisationUnitGroup organisationUnitGroup3 = new OrganisationUnitGroup();
        organisationUnitGroup3.setName("oug3");
        OrganisationUnitGroup organisationUnitGroup4 = new OrganisationUnitGroup();
        organisationUnitGroup4.setName("oug4");

        organisationUnitGroupServiceExt.addOrganisationUnitGroup(organisationUnitGroup1);
        organisationUnitGroupServiceExt.addOrganisationUnitGroup(organisationUnitGroup2);
        organisationUnitGroupServiceExt.addOrganisationUnitGroup(organisationUnitGroup3);
        organisationUnitGroupServiceExt.addOrganisationUnitGroup(organisationUnitGroup4);

        OrganisationUnitGroupSet organisationUnitGroupSet1 = new OrganisationUnitGroupSet();
        organisationUnitGroupSet1.setName("ougs1");
        organisationUnitGroupSet1.setShortName("ougs1");
        organisationUnitGroupSet1.setCompulsory(true);
        organisationUnitGroupSet1.getOrganisationUnitGroups().add(organisationUnitGroup1);
        organisationUnitGroupSet1.getOrganisationUnitGroups().add(organisationUnitGroup2);
        organisationUnitGroupSet1.getOrganisationUnitGroups().add(organisationUnitGroup3);

        long id1 = organisationUnitGroupServiceExt.addOrganisationUnitGroupSet(organisationUnitGroupSet1);

        // assert add
        assertNotNull(organisationUnitGroupServiceExt.getOrganisationUnitGroupSet(id1));

        assertEquals(organisationUnitGroupServiceExt.getOrganisationUnitGroupSet(id1).getName(), "ougs1");

        assertTrue(
            organisationUnitGroupServiceExt.getOrganisationUnitGroupSet(id1).getOrganisationUnitGroups().size() == 3);

        organisationUnitGroupSet1.getOrganisationUnitGroups().remove(organisationUnitGroup3);

        organisationUnitGroupServiceExt.updateOrganisationUnitGroupSet(organisationUnitGroupSet1);

        // assert update
        assertTrue(
            organisationUnitGroupServiceExt.getOrganisationUnitGroupSet(id1).getOrganisationUnitGroups().size() == 2);

        OrganisationUnitGroupSet organisationUnitGroupSet2 = new OrganisationUnitGroupSet();
        organisationUnitGroupSet2.setName("ougs2");
        organisationUnitGroupSet2.setShortName("ougs2");
        organisationUnitGroupSet2.setCompulsory(true);
        organisationUnitGroupSet2.getOrganisationUnitGroups().add(organisationUnitGroup4);

        long id2 = organisationUnitGroupServiceExt.addOrganisationUnitGroupSet(organisationUnitGroupSet2);

        // assert getAllOrderedName
        assertTrue(organisationUnitGroupServiceExt.getAllOrganisationUnitGroupSets().size() == 2);

        organisationUnitGroupServiceExt.deleteOrganisationUnitGroupSet(organisationUnitGroupSet1);
        organisationUnitGroupServiceExt.deleteOrganisationUnitGroupSet(organisationUnitGroupSet2);

        assertNull(organisationUnitGroupServiceExt.getOrganisationUnitGroupSet(id1));
        assertNull(organisationUnitGroupServiceExt.getOrganisationUnitGroupSet(id2));
    }

    // -------------------------------------------------------------------------
    // OrganisationUnitLevel
    // -------------------------------------------------------------------------

    @Test
    public void testAddGetOrganisationUnitLevel() {
        OrganisationUnitLevel levelA = new OrganisationUnitLevel(1, "National");
        OrganisationUnitLevel levelB = new OrganisationUnitLevel(2, "District");

        long idA = organisationUnitServiceExt.addOrganisationUnitLevel(levelA);
        long idB = organisationUnitServiceExt.addOrganisationUnitLevel(levelB);

        assertEquals(levelA, organisationUnitServiceExt.getOrganisationUnitLevel(idA));
        assertEquals(levelB, organisationUnitServiceExt.getOrganisationUnitLevel(idB));
    }

    @Test
    public void testGetOrganisationUnitLevels() {
        OrganisationUnitLevel level1 = new OrganisationUnitLevel(1, "National");
        OrganisationUnitLevel level2 = new OrganisationUnitLevel(2, "District");
        OrganisationUnitLevel level4 = new OrganisationUnitLevel(4, "PHU");

        organisationUnitServiceExt.addOrganisationUnitLevel(level1);
        organisationUnitServiceExt.addOrganisationUnitLevel(level2);
        organisationUnitServiceExt.addOrganisationUnitLevel(level4);

        OrganisationUnit unitA = createOrganisationUnit('A');
        OrganisationUnit unitB = createOrganisationUnit('B', unitA);
        OrganisationUnit unitC = createOrganisationUnit('C', unitB);
        OrganisationUnit unitD = createOrganisationUnit('D', unitC);

        unitA.getChildren().add(unitB);
        unitB.getChildren().add(unitC);
        unitC.getChildren().add(unitD);

        organisationUnitServiceExt.addOrganisationUnit(unitA);
        organisationUnitServiceExt.addOrganisationUnit(unitB);
        organisationUnitServiceExt.addOrganisationUnit(unitC);
        organisationUnitServiceExt.addOrganisationUnit(unitD);

        Iterator<OrganisationUnitLevel> actual = organisationUnitServiceExt.getOrganisationUnitLevels().iterator();

        assertNotNull(actual);
        assertEquals(level1, actual.next());
        assertEquals(level2, actual.next());

        level4 = actual.next();

        assertEquals(4, level4.getLevel());
        assertEquals("PHU", level4.getName());
    }

    @Test
    public void testRemoveOrganisationUnitLevel() {
        OrganisationUnitLevel levelA = new OrganisationUnitLevel(1, "National");
        OrganisationUnitLevel levelB = new OrganisationUnitLevel(2, "District");

        long idA = organisationUnitServiceExt.addOrganisationUnitLevel(levelA);
        long idB = organisationUnitServiceExt.addOrganisationUnitLevel(levelB);

        assertNotNull(organisationUnitServiceExt.getOrganisationUnitLevel(idA));
        assertNotNull(organisationUnitServiceExt.getOrganisationUnitLevel(idB));

        organisationUnitServiceExt.deleteOrganisationUnitLevel(levelA);

        assertNull(organisationUnitServiceExt.getOrganisationUnitLevel(idA));
        assertNotNull(organisationUnitServiceExt.getOrganisationUnitLevel(idB));

        organisationUnitServiceExt.deleteOrganisationUnitLevel(levelB);

        assertNull(organisationUnitServiceExt.getOrganisationUnitLevel(idA));
        assertNull(organisationUnitServiceExt.getOrganisationUnitLevel(idB));
    }

    @Test
    public void testGetLevelByNumericOrUid() {
        OrganisationUnitLevel levelA = new OrganisationUnitLevel(1, "National");
        OrganisationUnitLevel levelB = new OrganisationUnitLevel(2, "District");

        organisationUnitServiceExt.addOrganisationUnitLevel(levelA);
        organisationUnitServiceExt.addOrganisationUnitLevel(levelB);

        assertEquals(new Integer(1), organisationUnitServiceExt.getOrganisationUnitLevelByLevelOrUid("1"));
        assertEquals(new Integer(1),
            organisationUnitServiceExt.getOrganisationUnitLevelByLevelOrUid(levelA.getUid()));
        assertEquals(new Integer(2), organisationUnitServiceExt.getOrganisationUnitLevelByLevelOrUid("2"));
        assertEquals(new Integer(2),
            organisationUnitServiceExt.getOrganisationUnitLevelByLevelOrUid(levelB.getUid()));
    }

    @Test
    public void testIsInUserHierarchy() {
        OrganisationUnit ouA = createOrganisationUnit('A');
        OrganisationUnit ouB = createOrganisationUnit('B', ouA);
        OrganisationUnit ouC = createOrganisationUnit('C', ouA);
        OrganisationUnit ouD = createOrganisationUnit('D', ouB);
        OrganisationUnit ouE = createOrganisationUnit('E', ouB);
        OrganisationUnit ouF = createOrganisationUnit('F', ouC);
        OrganisationUnit ouG = createOrganisationUnit('G', ouC);

        ouA.getChildren().add(ouB);
        ouA.getChildren().add(ouC);
        ouB.getChildren().add(ouD);
        ouB.getChildren().add(ouE);
        ouC.getChildren().add(ouF);
        ouC.getChildren().add(ouG);

        organisationUnitServiceExt.addOrganisationUnit(ouA);
        organisationUnitServiceExt.addOrganisationUnit(ouB);
        organisationUnitServiceExt.addOrganisationUnit(ouC);
        organisationUnitServiceExt.addOrganisationUnit(ouD);
        organisationUnitServiceExt.addOrganisationUnit(ouE);
        organisationUnitServiceExt.addOrganisationUnit(ouF);
        organisationUnitServiceExt.addOrganisationUnit(ouG);

        Person user = createUserCredentials('A', null);
        Set<OrganisationUnit> organisationUnits = Sets.newHashSet(ouB);
        user.setOrganisationUnits(organisationUnits);

        assertTrue(organisationUnitServiceExt.isInUserHierarchy(ouB.getUid(), organisationUnits));
        assertTrue(organisationUnitServiceExt.isInUserHierarchy(ouD.getUid(), organisationUnits));
        assertTrue(organisationUnitServiceExt.isInUserHierarchy(ouE.getUid(), organisationUnits));

        assertFalse(organisationUnitServiceExt.isInUserHierarchy(ouA.getUid(), organisationUnits));
        assertFalse(organisationUnitServiceExt.isInUserHierarchy(ouC.getUid(), organisationUnits));
        assertFalse(organisationUnitServiceExt.isInUserHierarchy(ouF.getUid(), organisationUnits));
        assertFalse(organisationUnitServiceExt.isInUserHierarchy(ouG.getUid(), organisationUnits));
    }

    @Test
    public void testGetAncestorUids() {
        OrganisationUnit ouA = createOrganisationUnit('A');
        OrganisationUnit ouB = createOrganisationUnit('B', ouA);
        OrganisationUnit ouC = createOrganisationUnit('C', ouB);
        OrganisationUnit ouD = createOrganisationUnit('D', ouC);

        ouA.getChildren().add(ouB);
        ouA.getChildren().add(ouC);
        ouB.getChildren().add(ouD);

        organisationUnitServiceExt.addOrganisationUnit(ouA);
        organisationUnitServiceExt.addOrganisationUnit(ouB);
        organisationUnitServiceExt.addOrganisationUnit(ouC);
        organisationUnitServiceExt.addOrganisationUnit(ouD);

        organisationUnitServiceExt.updatePaths();

        List<String> expected = IdentifiableObjectUtils.getUids(Arrays.asList(ouA, ouB, ouC));

        assertEquals(expected, ouD.getAncestorUids(null));
        assertEquals(expected, ouD.getAncestorUids(Sets.newHashSet(ouA.getUid())));

        expected = IdentifiableObjectUtils.getUids(Arrays.asList(ouB, ouC));

        assertEquals(expected, ouD.getAncestorUids(Sets.newHashSet(ouB.getUid())));
    }

    @Test
    public void testGetParentGraph() {
        OrganisationUnit ouA = createOrganisationUnit('A');
        OrganisationUnit ouB = createOrganisationUnit('B', ouA);
        OrganisationUnit ouC = createOrganisationUnit('C', ouB);
        OrganisationUnit ouD = createOrganisationUnit('D', ouC);

        ouA.getChildren().add(ouB);
        ouA.getChildren().add(ouC);
        ouB.getChildren().add(ouD);

        organisationUnitServiceExt.addOrganisationUnit(ouA);
        organisationUnitServiceExt.addOrganisationUnit(ouB);
        organisationUnitServiceExt.addOrganisationUnit(ouC);
        organisationUnitServiceExt.addOrganisationUnit(ouD);

        organisationUnitServiceExt.updatePaths();

        String expected = ouA.getUid() + "/" + ouB.getUid() + "/" + ouC.getUid();

        assertEquals(expected, ouD.getParentGraph(null));
        assertEquals(expected, ouD.getParentGraph(Sets.newHashSet(ouA)));

        expected = ouB.getUid() + "/" + ouC.getUid();

        assertEquals(expected, ouD.getParentGraph(Sets.newHashSet(ouB)));
    }

    // TODO Enable when Adding FileResource
//    @Test
//    public void testSaveImage()
//    {
//        byte[] content = "<<png data>>".getBytes();
//        FileResource fileResource = new FileResource( "testOrgUnitImage.png", MimeTypeUtils.IMAGE_PNG.getType(),
//            content.length,
//            "md5", FileResourceDomain.ORG_UNIT );
//        fileResource.setAssigned( false );
//        fileResource.setCreated( new Date() );
//        fileResource.setAutoFields();
//        fileResourceService.saveFileResource( fileResource, content );
//
//        OrganisationUnit orgUnit = createOrganisationUnit( 'A' );
//        orgUnit.setImage( fileResource );
//        organisationUnitService.addOrganisationUnit( orgUnit );
//
//        OrganisationUnit savedOU = organisationUnitService.getOrganisationUnit( orgUnit.getUid() );
//
//        assertEquals( fileResource.getUid(), savedOU.getImage().getUid() );
//
//    }
}
