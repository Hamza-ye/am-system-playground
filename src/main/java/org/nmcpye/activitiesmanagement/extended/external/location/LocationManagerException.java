package org.nmcpye.activitiesmanagement.extended.external.location;

public class LocationManagerException
    extends RuntimeException {
    public LocationManagerException(String message) {
        super(message);
    }

    public LocationManagerException(String message, Throwable cause) {
        super(message, cause);
    }
}
