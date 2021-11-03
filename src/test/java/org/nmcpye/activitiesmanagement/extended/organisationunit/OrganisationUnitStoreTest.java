package org.nmcpye.activitiesmanagement.extended.organisationunit;

import com.google.common.collect.Sets;
import org.junit.jupiter.api.Test;
import org.nmcpye.activitiesmanagement.AMTest;
import org.nmcpye.activitiesmanagement.IntegrationTest;
import org.nmcpye.activitiesmanagement.domain.organisationunit.OrganisationUnit;
import org.nmcpye.activitiesmanagement.domain.organisationunit.OrganisationUnitGroup;
import org.nmcpye.activitiesmanagement.domain.organisationunit.OrganisationUnitLevel;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@IntegrationTest
public class OrganisationUnitStoreTest extends AMTest {

    @Autowired
    private OrganisationUnitLevelStore orgUnitLevelStore;

    @Autowired
    private OrganisationUnitStore orgUnitStore;

    @Autowired
    private OrganisationUnitGroupStore orgUnitGroupStore;

    //    @Autowired
    //    private IdentifiableObjectManager idObjectManager;

    private OrganisationUnit ouA;
    private OrganisationUnit ouB;
    private OrganisationUnit ouC;
    private OrganisationUnit ouD;
    private OrganisationUnit ouE;
    private OrganisationUnit ouF;
    private OrganisationUnit ouG;

    private OrganisationUnitGroup ougA;
    private OrganisationUnitGroup ougB;

    // -------------------------------------------------------------------------
    // OrganisationUnit
    // -------------------------------------------------------------------------

    @Override
    public void setUpTest() {
        ouA = createOrganisationUnit('A'); // 1
        ouB = createOrganisationUnit('B', ouA); // 2
        ouC = createOrganisationUnit('C', ouA); // 2
        ouD = createOrganisationUnit('D', ouB); // 3
        ouE = createOrganisationUnit('E', ouB); // 3
        ouF = createOrganisationUnit('F', ouC); // 3
        ouG = createOrganisationUnit('G', ouC); // 3

        ougA = createOrganisationUnitGroup('A');
        ougB = createOrganisationUnitGroup('B');
        //        idObjectManager.save( coA );
        //        idObjectManager.save( coB );
        //
        //        dsA = createDataSet( 'A' );
        //        dsB = createDataSet( 'B' );
    }

    public void testGetOrganisationUnitsWithoutGroups() {
        orgUnitStore.save(ouA);
        orgUnitStore.save(ouB);
        orgUnitStore.save(ouC);
        orgUnitStore.save(ouD);
        orgUnitStore.save(ouE);

        ougA.addOrganisationUnit(ouA);
        ougA.addOrganisationUnit(ouB);

        orgUnitGroupStore.save(ougA);

        //        idObjectManager.save( ougA );

        List<OrganisationUnit> orgUnits = orgUnitStore.getOrganisationUnitsWithoutGroups();

        assertEquals(3, orgUnits.size());
        assertTrue(orgUnits.contains(ouC));
        assertTrue(orgUnits.contains(ouD));
        assertTrue(orgUnits.contains(ouE));
    }

    //    @Test
    //    public void testGetOrganisationUnitHierarchyMemberCount()
    //    {
    //        dsA.addOrganisationUnit( ouD );
    //        dsA.addOrganisationUnit( ouE );
    //        dsA.addOrganisationUnit( ouG );
    //        dsB.addOrganisationUnit( ouD );
    //
    //        dataSetService.addDataSet( dsA );
    //        dataSetService.addDataSet( dsB );
    //
    //        orgUnitStore.save( ouA );
    //        orgUnitStore.save( ouB );
    //        orgUnitStore.save( ouC );
    //        orgUnitStore.save( ouD );
    //        orgUnitStore.save( ouE );
    //        orgUnitStore.save( ouF );
    //        orgUnitStore.save( ouG );
    //
    //        assertEquals( new Long( 3 ), orgUnitStore.getOrganisationUnitHierarchyMemberCount( ouA, dsA, "dataSets" ) );
    //
    //        assertEquals( new Long( 2 ), orgUnitStore.getOrganisationUnitHierarchyMemberCount( ouB, dsA, "dataSets" ) );
    //
    //        assertEquals( new Long( 1 ), orgUnitStore.getOrganisationUnitHierarchyMemberCount( ouA, dsB, "dataSets" ) );
    //    }

    @Test
    public void testGetOrganisationUnits() {
        orgUnitStore.save(ouA);
        orgUnitStore.save(ouB);
        orgUnitStore.save(ouC);
        orgUnitStore.save(ouD);
        orgUnitStore.save(ouE);
        orgUnitStore.save(ouF);
        orgUnitStore.save(ouG);

        ougA.getMembers().addAll(Sets.newHashSet(ouD, ouF));
        ougB.getMembers().addAll(Sets.newHashSet(ouE, ouG));
        orgUnitGroupStore.save(ougA);
        orgUnitGroupStore.save(ougB);

        orgUnitStore.forceUpdatePaths();

        // Query

        OrganisationUnitQueryParams params = new OrganisationUnitQueryParams();
        params.setQuery("UnitC");

        List<OrganisationUnit> ous = orgUnitStore.getOrganisationUnits(params);

        assertEquals(1, ous.size());
        assertTrue(ous.contains(ouC));

        // Query

        params = new OrganisationUnitQueryParams();
        params.setQuery("OrganisationUnitCodeA");

        ous = orgUnitStore.getOrganisationUnits(params);

        assertTrue(ous.contains(ouA));
        assertEquals(1, ous.size());

        // Parents

        params = new OrganisationUnitQueryParams();
        params.setParents(Sets.newHashSet(ouC, ouF));

        ous = orgUnitStore.getOrganisationUnits(params);

        assertEquals(3, ous.size());
        assertTrue(ous.containsAll(Sets.newHashSet(ouC, ouF, ouG)));

        // Groups

        params = new OrganisationUnitQueryParams();
        params.setGroups(Sets.newHashSet(ougA));

        ous = orgUnitStore.getOrganisationUnits(params);

        assertEquals(2, ous.size());
        assertTrue(ous.containsAll(Sets.newHashSet(ouD, ouF)));

        // Groups

        params = new OrganisationUnitQueryParams();
        params.setGroups(Sets.newHashSet(ougA));

        ous = orgUnitStore.getOrganisationUnits(params);

        assertEquals(2, ous.size());
        assertTrue(ous.containsAll(Sets.newHashSet(ouD, ouF)));

        params = new OrganisationUnitQueryParams();
        params.setGroups(Sets.newHashSet(ougA, ougB));

        ous = orgUnitStore.getOrganisationUnits(params);

        assertEquals(4, ous.size());
        assertTrue(ous.containsAll(Sets.newHashSet(ouD, ouF, ouE, ouG)));

        // Levels

        params = new OrganisationUnitQueryParams();
        params.setLevels(Sets.newHashSet(2));

        ous = orgUnitStore.getOrganisationUnits(params);

        assertEquals(2, ous.size());
        assertTrue(ous.containsAll(Sets.newHashSet(ouB, ouC)));

        // Levels

        params = new OrganisationUnitQueryParams();
        params.setLevels(Sets.newHashSet(2, 3));

        ous = orgUnitStore.getOrganisationUnits(params);

        assertEquals(6, ous.size());
        assertTrue(ous.containsAll(Sets.newHashSet(ouB, ouC, ouD, ouE, ouF, ouG)));

        // Levels and groups

        params = new OrganisationUnitQueryParams();
        params.setLevels(Sets.newHashSet(3));
        params.setGroups(Sets.newHashSet(ougA));

        ous = orgUnitStore.getOrganisationUnits(params);

        assertEquals(2, ous.size());
        assertTrue(ous.containsAll(Sets.newHashSet(ouD, ouF)));

        // Parents and groups

        params = new OrganisationUnitQueryParams();
        params.setParents(Sets.newHashSet(ouC));
        params.setGroups(Sets.newHashSet(ougB));

        ous = orgUnitStore.getOrganisationUnits(params);

        assertEquals(1, ous.size());
        assertTrue(ous.containsAll(Sets.newHashSet(ouG)));

        // Parents and max levels

        params = new OrganisationUnitQueryParams();
        params.setParents(Sets.newHashSet(ouA));
        params.setMaxLevels(2);

        ous = orgUnitStore.getOrganisationUnits(params);

        assertEquals(3, ous.size());
        assertTrue(ous.containsAll(Sets.newHashSet(ouA, ouB, ouC)));

        // Parents and max levels

        params = new OrganisationUnitQueryParams();
        params.setParents(Sets.newHashSet(ouA));
        params.setMaxLevels(3);

        ous = orgUnitStore.getOrganisationUnits(params);

        assertEquals(7, ous.size());
        assertTrue(ous.containsAll(Sets.newHashSet(ouA, ouB, ouC, ouD, ouE, ouF, ouG)));

        // Parents and max levels

        params = new OrganisationUnitQueryParams();
        params.setParents(Sets.newHashSet(ouA));
        params.setMaxLevels(1);

        ous = orgUnitStore.getOrganisationUnits(params);

        assertEquals(1, ous.size());
        assertTrue(ous.containsAll(Sets.newHashSet(ouA)));

        // Parents and max levels

        params = new OrganisationUnitQueryParams();
        params.setParents(Sets.newHashSet(ouB));
        params.setMaxLevels(3);

        ous = orgUnitStore.getOrganisationUnits(params);

        assertEquals(3, ous.size());
        assertTrue(ous.containsAll(Sets.newHashSet(ouB, ouD, ouE)));
    }

    // -------------------------------------------------------------------------
    // OrganisationUnitLevel
    // -------------------------------------------------------------------------

    @Test
    public void testAddGetOrganisationUnitLevel() {
        OrganisationUnitLevel levelA = new OrganisationUnitLevel(1, "National");
        OrganisationUnitLevel levelB = new OrganisationUnitLevel(2, "District");

        orgUnitLevelStore.save(levelA);
        long idA = levelA.getId();
        orgUnitLevelStore.save(levelB);
        long idB = levelB.getId();

        assertEquals(levelA, orgUnitLevelStore.get(idA));
        assertEquals(levelB, orgUnitLevelStore.get(idB));
    }

    @Test
    public void testGetOrganisationUnitLevels() {
        OrganisationUnitLevel levelA = new OrganisationUnitLevel(1, "National");
        OrganisationUnitLevel levelB = new OrganisationUnitLevel(2, "District");

        orgUnitLevelStore.save(levelA);
        orgUnitLevelStore.save(levelB);

        List<OrganisationUnitLevel> actual = orgUnitLevelStore.getAll();

        assertNotNull(actual);
        assertEquals(2, actual.size());
        assertTrue(actual.contains(levelA));
        assertTrue(actual.contains(levelB));
    }

    @Test
    public void testRemoveOrganisationUnitLevel() {
        OrganisationUnitLevel levelA = new OrganisationUnitLevel(1, "National");
        OrganisationUnitLevel levelB = new OrganisationUnitLevel(2, "District");

        orgUnitLevelStore.save(levelA);
        long idA = levelA.getId();
        orgUnitLevelStore.save(levelB);
        long idB = levelB.getId();

        assertNotNull(orgUnitLevelStore.get(idA));
        assertNotNull(orgUnitLevelStore.get(idB));

        orgUnitLevelStore.delete(levelA);

        assertNull(orgUnitLevelStore.get(idA));
        assertNotNull(orgUnitLevelStore.get(idB));

        orgUnitLevelStore.delete(levelB);

        assertNull(orgUnitLevelStore.get(idA));
        assertNull(orgUnitLevelStore.get(idB));
    }

    @Test
    public void testGetMaxLevel() {
        OrganisationUnit ouA = createOrganisationUnit('A'); // 1
        OrganisationUnit ouB = createOrganisationUnit('B', ouA); // 2
        OrganisationUnit ouC = createOrganisationUnit('C', ouA); // 2
        OrganisationUnit ouD = createOrganisationUnit('D', ouB); // 3
        OrganisationUnit ouE = createOrganisationUnit('E', ouB); // 3
        OrganisationUnit ouF = createOrganisationUnit('F', ouC); // 3
        OrganisationUnit ouG = createOrganisationUnit('G', ouC); // 3

        assertEquals(0, orgUnitStore.getMaxLevel());

        orgUnitStore.save(ouA);

        orgUnitStore.forceUpdatePaths();

        assertEquals(1, orgUnitStore.getMaxLevel());

        orgUnitStore.save(ouB);
        orgUnitStore.save(ouC);

        orgUnitStore.forceUpdatePaths();

        assertEquals(2, orgUnitStore.getMaxLevel());

        orgUnitStore.save(ouD);
        orgUnitStore.save(ouE);
        orgUnitStore.save(ouF);
        orgUnitStore.save(ouG);

        orgUnitStore.forceUpdatePaths();

        assertEquals(3, orgUnitStore.getMaxLevel());
    }
}
