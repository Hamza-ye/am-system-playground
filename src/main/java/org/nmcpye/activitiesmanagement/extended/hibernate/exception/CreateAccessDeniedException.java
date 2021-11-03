package org.nmcpye.activitiesmanagement.extended.hibernate.exception;

import org.springframework.security.access.AccessDeniedException;

public class CreateAccessDeniedException extends AccessDeniedException {

    public CreateAccessDeniedException(String msg) {
        super(msg);
    }
}
