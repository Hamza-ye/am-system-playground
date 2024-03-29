package org.nmcpye.activitiesmanagement.extended.scheduling.parameters;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.nmcpye.activitiesmanagement.extended.feedback.ErrorReport;
import org.nmcpye.activitiesmanagement.extended.scheduling.JobParameters;

import java.util.Optional;

public class MockJobParameters implements JobParameters {

    private static final long serialVersionUID = 3600315605964091689L;

    @JsonProperty
    private String message;

    public MockJobParameters() {}

    public MockJobParameters(String message) {
        this.message = message;
    }

    @JsonProperty
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public Optional<ErrorReport> validate() {
        return Optional.empty();
    }
}
