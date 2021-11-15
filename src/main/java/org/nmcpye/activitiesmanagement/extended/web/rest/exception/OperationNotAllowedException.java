package org.nmcpye.activitiesmanagement.extended.web.rest.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by sultanm. This exception could be used in all operation forbidden
 * cases
 */
@ResponseStatus(HttpStatus.FORBIDDEN)
public class OperationNotAllowedException
    extends Exception {

    public OperationNotAllowedException(String message) {
        super(message);
    }

    public OperationNotAllowedException(Throwable cause) {
        super(cause);
    }

    public OperationNotAllowedException(String message, Throwable cause) {
        super(message, cause);
    }
}
