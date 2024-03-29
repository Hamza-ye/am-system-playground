package org.nmcpye.activitiesmanagement.extended.dxf2module.webmessage;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.google.common.base.MoreObjects;
import org.nmcpye.activitiesmanagement.extended.feedback.ErrorCode;
import org.nmcpye.activitiesmanagement.extended.feedback.Status;
import org.springframework.http.HttpStatus;

import javax.annotation.Nonnull;

@JsonPropertyOrder({
    "httpStatus", "httpStatusCode", "status", "code", "message", "devMessage", "response"
})
public class WebMessage implements WebMessageResponse {
    /**
     * Message status, currently two statuses are available: OK, ERROR. Default
     * value is OK.
     *
     * @see Status
     */
    private Status status = Status.OK;

    /**
     * Internal code for this message. Should be used to help with third party
     * clients which should not have to resort to string parsing of message to
     * know what is happening.
     */
    private Integer code;

    /**
     * HTTP status.
     */
    private HttpStatus httpStatus = HttpStatus.OK;

    /**
     * The {@link ErrorCode} which describes a potential error. Only relevant
     * for {@link Status#ERROR}.
     */
    private ErrorCode errorCode;

    /**
     * Non-technical message, should be simple and could possibly be used to
     * display message to an end-user.
     */
    private String message;

    /**
     * Technical message that should explain as much details as possible, mainly
     * to be used for debugging.
     */
    private String devMessage;

    /**
     * When a simple text feedback is not enough, you can use this interface to
     * implement your own message responses.
     *
     * @see WebMessageResponse
     */
    private WebMessageResponse response;

    private String location;

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Only for deserialisation
     */
    public WebMessage() {
    }

    public WebMessage(Status status, HttpStatus httpStatus) {
        this.status = status;
        this.httpStatus = httpStatus;
    }

    // -------------------------------------------------------------------------
    // Logic
    // -------------------------------------------------------------------------

    public boolean isOk() {
        return Status.OK == status;
    }

    public boolean isWarning() {
        return Status.WARNING == status;
    }

    public boolean isError() {
        return Status.ERROR == status;
    }

    // -------------------------------------------------------------------------
    // Get and set methods
    // -------------------------------------------------------------------------

    @JsonProperty
    public Status getStatus() {
        return status;
    }

    public WebMessage setStatus(Status status) {
        this.status = status;
        return this;
    }

    @JsonProperty
    public Integer getCode() {
        return code;
    }

    public WebMessage setCode(Integer code) {
        this.code = code;
        return this;
    }

    @JsonProperty
    public String getHttpStatus() {
        return httpStatus.getReasonPhrase();
    }

    public WebMessage setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
        return this;
    }

    @JsonProperty
    @Nonnull
    public Integer getHttpStatusCode() {
        return httpStatus.value();
    }

    @JsonProperty
    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public WebMessage setErrorCode(ErrorCode errorCode) {
        this.errorCode = errorCode;
        return this;
    }

    @JsonProperty
    public String getMessage() {
        return message;
    }

    public WebMessage setMessage(String message) {
        this.message = message;
        return this;
    }

    @JsonProperty
    public String getDevMessage() {
        return devMessage;
    }

    public WebMessage setDevMessage(String devMessage) {
        this.devMessage = devMessage;
        return this;
    }

    @JsonProperty
    public WebMessageResponse getResponse() {
        return response;
    }

    public WebMessage setResponse(WebMessageResponse response) {
        this.response = response;
        return this;
    }

    public String getLocation() {
        return location;
    }

    public WebMessage setLocation(String location) {
        this.location = location;
        return this;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
            .add("status", status)
            .add("code", code)
            .add("httpStatus", httpStatus)
            .add("message", message)
            .add("devMessage", devMessage)
            .add("response", response)
            .toString();
    }
}
