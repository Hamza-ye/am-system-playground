package org.nmcpye.activitiesmanagement.extended.common.coordinate;

import com.vividsolutions.jts.geom.Geometry;
import org.nmcpye.activitiesmanagement.domain.organisationunit.FeatureType;

public interface CoordinateObject {
    FeatureType getFeatureType();

    String getCoordinates();

    boolean hasCoordinates();

    boolean hasDescendantsWithCoordinates();

    default String extractCoordinates(Geometry geometry) {
        return CoordinateUtils.getCoordinatesFromGeometry(geometry);
    }
}
