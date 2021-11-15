package org.nmcpye.activitiesmanagement.extended.web.rest.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundException extends Exception {
    public static NotFoundException notFoundUid(String uid) {
        return new NotFoundException("Object not found for uid: " + uid);
    }

    public NotFoundException() {
        super("Object not found.");
    }

    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(String type, String uid) {
        super(type + " not found for uid: " + uid);
    }
}
