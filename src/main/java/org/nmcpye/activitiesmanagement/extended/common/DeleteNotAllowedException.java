package org.nmcpye.activitiesmanagement.extended.common;

import org.nmcpye.activitiesmanagement.extended.feedback.ErrorCode;
import org.nmcpye.activitiesmanagement.extended.feedback.ErrorMessage;

public class DeleteNotAllowedException
    extends RuntimeException {
    private ErrorCode errorCode;

    public DeleteNotAllowedException(ErrorMessage errorMessage) {
        super(errorMessage.getMessage());
        this.errorCode = errorMessage.getErrorCode();
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
