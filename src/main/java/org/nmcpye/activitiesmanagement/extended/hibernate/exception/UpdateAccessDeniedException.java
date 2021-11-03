package org.nmcpye.activitiesmanagement.extended.hibernate.exception;

import org.springframework.security.access.AccessDeniedException;

public class UpdateAccessDeniedException extends AccessDeniedException {

    public UpdateAccessDeniedException(String msg) {
        super(msg);
    }
}
