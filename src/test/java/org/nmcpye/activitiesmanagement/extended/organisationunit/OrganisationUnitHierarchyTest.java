package org.nmcpye.activitiesmanagement.extended.organisationunit;

import org.junit.jupiter.api.Test;
import org.nmcpye.activitiesmanagement.domain.organisationunit.OrganisationUnit;
import org.nmcpye.activitiesmanagement.domain.organisationunit.OrganisationUnitGroup;

import java.util.*;

import static org.junit.Assert.*;

public class OrganisationUnitHierarchyTest {

    @Test
    public void testGetGroupChildren() {
        OrganisationUnitGroup group = new OrganisationUnitGroup("Group");
        group.setId(1L);

        OrganisationUnit unit2 = new OrganisationUnit("Unit2");
        OrganisationUnit unit4 = new OrganisationUnit("Unit4");
        OrganisationUnit unit6 = new OrganisationUnit("Unit6");
        OrganisationUnit unit8 = new OrganisationUnit("Unit8");
        OrganisationUnit unit10 = new OrganisationUnit("Unit10");
        OrganisationUnit unit12 = new OrganisationUnit("Unit12");

        unit2.setId(2L);
        unit4.setId(4L);
        unit6.setId(6L);
        unit8.setId(8L);
        unit10.setId(10L);
        unit12.setId(12L);

        group.addOrganisationUnit(unit2);
        group.addOrganisationUnit(unit4);
        group.addOrganisationUnit(unit6);
        group.addOrganisationUnit(unit8);
        group.addOrganisationUnit(unit10);
        group.addOrganisationUnit(unit12);

        List<OrganisationUnitRelationship> relationships = new ArrayList<>();

        relationships.add(new OrganisationUnitRelationship(1, 2));
        relationships.add(new OrganisationUnitRelationship(1, 3));
        relationships.add(new OrganisationUnitRelationship(2, 4));
        relationships.add(new OrganisationUnitRelationship(2, 5));
        relationships.add(new OrganisationUnitRelationship(2, 6));
        relationships.add(new OrganisationUnitRelationship(3, 7));
        relationships.add(new OrganisationUnitRelationship(3, 8));
        relationships.add(new OrganisationUnitRelationship(3, 9));
        relationships.add(new OrganisationUnitRelationship(4, 10));
        relationships.add(new OrganisationUnitRelationship(4, 11));
        relationships.add(new OrganisationUnitRelationship(4, 12));

        OrganisationUnitHierarchy hierarchy = new OrganisationUnitHierarchy(relationships);

        assertEquals(6, hierarchy.getChildren(1, group).size());

        assertEquals(5, hierarchy.getChildren(2, group).size());
        assertTrue(hierarchy.getChildren(2, group).contains(2L));
        assertTrue(hierarchy.getChildren(2, group).contains(4L));
        assertTrue(hierarchy.getChildren(2, group).contains(6L));
        assertTrue(hierarchy.getChildren(2, group).contains(10L));
        assertTrue(hierarchy.getChildren(2, group).contains(12L));

        assertEquals(1, hierarchy.getChildren(3, group).size());
        assertTrue(hierarchy.getChildren(3, group).contains(8L));

        assertEquals(3, hierarchy.getChildren(4, group).size());
        assertTrue(hierarchy.getChildren(4, group).contains(4L));
        assertTrue(hierarchy.getChildren(4, group).contains(10L));
        assertTrue(hierarchy.getChildren(4, group).contains(12L));

        assertEquals(0, hierarchy.getChildren(11, group).size());

        assertFalse(hierarchy.getChildren(5, group).contains(10L));
        assertFalse(hierarchy.getChildren(3, group).contains(11L));
    }

    @Test
    public void testGetChildren() {
        Set<Long> parentIds = new HashSet<>();

        List<OrganisationUnitRelationship> relations = new ArrayList<>();

        int parentMax = 1000; // Increase to stress-test
        int childMax = 4;
        int childId = 0;

        for (long parentId = 0; parentId < parentMax; parentId++) {
            parentIds.add(parentId);

            for (int j = 0; j < childMax; j++) {
                relations.add(new OrganisationUnitRelationship(parentId, ++childId));
            }
        }

        OrganisationUnitHierarchy hierarchy = new OrganisationUnitHierarchy(relations);

        Set<Long> children = hierarchy.getChildren(parentIds);

        assertNotNull(children);
        assertEquals((parentMax * childMax) + 1, children.size());
    }

    @Test
    public void testGetChildrenA() {
        List<OrganisationUnitRelationship> relationships = new ArrayList<>();

        relationships.add(new OrganisationUnitRelationship(1, 2));
        relationships.add(new OrganisationUnitRelationship(1, 3));
        relationships.add(new OrganisationUnitRelationship(2, 4));
        relationships.add(new OrganisationUnitRelationship(2, 5));
        relationships.add(new OrganisationUnitRelationship(2, 6));
        relationships.add(new OrganisationUnitRelationship(3, 7));
        relationships.add(new OrganisationUnitRelationship(3, 8));
        relationships.add(new OrganisationUnitRelationship(3, 9));
        relationships.add(new OrganisationUnitRelationship(4, 10));
        relationships.add(new OrganisationUnitRelationship(4, 11));
        relationships.add(new OrganisationUnitRelationship(4, 12));

        OrganisationUnitHierarchy hierarchy = new OrganisationUnitHierarchy(relationships);

        testHierarchy(hierarchy);
    }

    @Test
    public void testGetChildrenB() {
        Map<Long, Set<Long>> relationships = new HashMap<>();

        relationships.put(1l, getSet(2L, 3L));
        relationships.put(2L, getSet(4L, 5L, 6L));
        relationships.put(3L, getSet(7L, 8L, 9L));
        relationships.put(4L, getSet(10L, 11L, 12L));

        OrganisationUnitHierarchy hierarchy = new OrganisationUnitHierarchy(relationships);

        testHierarchy(hierarchy);
    }

    private void testHierarchy(OrganisationUnitHierarchy hierarchy) {
        assertEquals(12, hierarchy.getChildren(1).size());

        assertEquals(7, hierarchy.getChildren(2).size());
        assertTrue(hierarchy.getChildren(2).contains(2L));
        assertTrue(hierarchy.getChildren(2).contains(4L));
        assertTrue(hierarchy.getChildren(2).contains(5L));
        assertTrue(hierarchy.getChildren(2).contains(6L));
        assertTrue(hierarchy.getChildren(2).contains(10L));
        assertTrue(hierarchy.getChildren(2).contains(11L));
        assertTrue(hierarchy.getChildren(2).contains(12L));

        assertEquals(4, hierarchy.getChildren(3).size());
        assertTrue(hierarchy.getChildren(3).contains(3L));
        assertTrue(hierarchy.getChildren(3).contains(7L));
        assertTrue(hierarchy.getChildren(3).contains(8L));
        assertTrue(hierarchy.getChildren(3).contains(9L));

        assertEquals(4, hierarchy.getChildren(4).size());
        assertTrue(hierarchy.getChildren(4).contains(4L));
        assertTrue(hierarchy.getChildren(4).contains(10L));
        assertTrue(hierarchy.getChildren(4).contains(11L));
        assertTrue(hierarchy.getChildren(4).contains(12L));

        assertEquals(1, hierarchy.getChildren(11).size());
        assertTrue(hierarchy.getChildren(11).contains(11L));

        assertFalse(hierarchy.getChildren(2).contains(3L));
        assertFalse(hierarchy.getChildren(2).contains(8L));
    }

    private Set<Long> getSet(Long... ints) {
        Set<Long> set = new HashSet<>();

        Collections.addAll(set, ints);

        return set;
    }
}
