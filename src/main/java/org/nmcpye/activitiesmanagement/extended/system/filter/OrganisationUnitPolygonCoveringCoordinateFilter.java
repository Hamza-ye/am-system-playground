package org.nmcpye.activitiesmanagement.extended.system.filter;

import com.vividsolutions.jts.geom.Geometry;
import org.nmcpye.activitiesmanagement.domain.organisationunit.FeatureType;
import org.nmcpye.activitiesmanagement.domain.organisationunit.OrganisationUnit;
import org.nmcpye.activitiesmanagement.extended.common.filter.Filter;
import org.nmcpye.activitiesmanagement.extended.system.util.GeoUtils;

public class OrganisationUnitPolygonCoveringCoordinateFilter implements Filter<OrganisationUnit> {

    private double longitude;
    private double latitude;

    public OrganisationUnitPolygonCoveringCoordinateFilter(double longitude, double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    @Override
    public boolean retain(OrganisationUnit unit) {
        Geometry geometry = unit.getGeometry();
        return (
            geometry != null &&
            FeatureType.getTypeFromName(geometry.getGeometryType()) == FeatureType.POLYGON &&
            GeoUtils.checkPointWithMultiPolygon(longitude, latitude, unit.getGeometry())
        );
    }
}
