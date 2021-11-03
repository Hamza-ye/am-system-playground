package org.nmcpye.activitiesmanagement.extended.domain.organisationunit;

import com.vividsolutions.jts.geom.Geometry;
import org.geotools.geojson.geom.GeometryJSON;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.nmcpye.activitiesmanagement.domain.organisationunit.CoordinatesTuple;
import org.nmcpye.activitiesmanagement.domain.organisationunit.OrganisationUnit;
import org.nmcpye.activitiesmanagement.extended.common.coordinate.CoordinateUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.nmcpye.activitiesmanagement.domain.organisationunit.FeatureType.MULTI_POLYGON;

public class OrganisationUnitTest {

    private List<CoordinatesTuple> multiPolygonCoordinatesList = new ArrayList<>();
    private List<CoordinatesTuple> pointCoordinatesList = new ArrayList<>();

    private String multiPolygonCoordinates =
        "[[[[11.11,22.22],[33.33,44.44],[55.55,66.66],[11.11,22.22]]]," +
        "[[[77.77,88.88],[99.99,11.11],[22.22,33.33],[77.77,88.88]]]," +
        "[[[44.44,55.55],[66.66,77.77],[88.88,99.99],[44.44,55.55]]]]";

    private CoordinatesTuple tupleA;
    private CoordinatesTuple tupleB;
    private CoordinatesTuple tupleC;
    private CoordinatesTuple tupleD;

    private OrganisationUnit unitA;
    private OrganisationUnit unitB;
    private OrganisationUnit unitC;
    private OrganisationUnit unitD;

    private GeometryJSON geometryJSON = new GeometryJSON();

    @BeforeEach
    public void before() {
        tupleA = new CoordinatesTuple();
        tupleA.addCoordinates("11.11,22.22");
        tupleA.addCoordinates("33.33,44.44");
        tupleA.addCoordinates("55.55,66.66");

        tupleB = new CoordinatesTuple();
        tupleB.addCoordinates("77.77,88.88");
        tupleB.addCoordinates("99.99,11.11");
        tupleB.addCoordinates("22.22,33.33");

        tupleC = new CoordinatesTuple();
        tupleC.addCoordinates("44.44,55.55");
        tupleC.addCoordinates("66.66,77.77");
        tupleC.addCoordinates("88.88,99.99");

        tupleD = new CoordinatesTuple();
        tupleD.addCoordinates("11.11,22.22");

        multiPolygonCoordinatesList.add(tupleA);
        multiPolygonCoordinatesList.add(tupleB);
        multiPolygonCoordinatesList.add(tupleC);
        pointCoordinatesList.add(tupleD);

        unitA = new OrganisationUnit("OrgUnitA");
        unitB = new OrganisationUnit("OrgUnitB");
        unitC = new OrganisationUnit("OrgUnitC");
        unitD = new OrganisationUnit("OrgUnitD");

        unitA.setUid("uidA");
        unitB.setUid("uidB");
        unitC.setUid("uidC");
        unitD.setUid("uidD");
    }

    @Test
    public void testGetAncestors() {
        unitD.setParent(unitC);
        unitC.setParent(unitB);
        unitB.setParent(unitA);

        List<OrganisationUnit> expected = new ArrayList<>(Arrays.asList(unitA, unitB, unitC));

        assertEquals(expected, unitD.getAncestors());
    }

    @Test
    public void testGetAncestorNames() {
        unitD.setParent(unitC);
        unitC.setParent(unitB);
        unitB.setParent(unitA);

        List<String> expected = new ArrayList<>(Arrays.asList(unitA.getDisplayName(), unitB.getDisplayName(), unitC.getDisplayName()));

        assertEquals(expected, unitD.getAncestorNames(null, false));

        expected =
            new ArrayList<>(Arrays.asList(unitA.getDisplayName(), unitB.getDisplayName(), unitC.getDisplayName(), unitD.getDisplayName()));

        assertEquals(expected, unitD.getAncestorNames(null, true));
    }

    @Test
    public void testGetAncestorsWithRoots() {
        unitD.setParent(unitC);
        unitC.setParent(unitB);
        unitB.setParent(unitA);

        List<OrganisationUnit> roots = new ArrayList<>(Arrays.asList(unitB));

        List<OrganisationUnit> expected = new ArrayList<>(Arrays.asList(unitB, unitC));

        assertEquals(expected, unitD.getAncestors(roots));
    }

    @Test
    public void testGetPath() {
        unitD.setParent(unitC);
        unitC.setParent(unitB);
        unitB.setParent(unitA);

        String expected = "/uidA/uidB/uidC/uidD";

        assertEquals(expected, unitD.getPath());
    }

    @Test
    public void testGetParentNameGraph() {
        unitD.setParent(unitC);
        unitC.setParent(unitB);
        unitB.setParent(unitA);

        List<OrganisationUnit> roots = new ArrayList<>(Arrays.asList(unitB));

        String expected = "/OrgUnitB/OrgUnitC";

        assertEquals(expected, unitD.getParentNameGraph(roots, false));

        expected = "/OrgUnitA/OrgUnitB/OrgUnitC";

        assertEquals(expected, unitD.getParentNameGraph(null, false));
    }

    @Test
    public void testGetCoordinatesAsCollection() throws IOException {
        OrganisationUnit unit = new OrganisationUnit();
        Geometry geometry = geometryJSON.read("{\"type\":\"MultiPolygon\", \"coordinates\":" + multiPolygonCoordinates + "}");
        unit.setGeometry(geometry);

        assertEquals(3, CoordinateUtils.getCoordinatesAsList(unit.getCoordinates(), MULTI_POLYGON).size());
    }
}
