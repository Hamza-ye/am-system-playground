package org.nmcpye.activitiesmanagement.extended.web.rest.exception;

public class NotAuthenticatedException extends Exception {
    public NotAuthenticatedException() {
        super("User object is null, user is not authenticated.");
    }
}
