package org.nmcpye.activitiesmanagement.extended.dxf2module.webmessage.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.nmcpye.activitiesmanagement.extended.dxf2module.webmessage.AbstractWebMessageResponse;
import org.nmcpye.activitiesmanagement.extended.feedback.ErrorReport;

import java.util.ArrayList;
import java.util.List;

public class ErrorReportsWebMessageResponse
    extends AbstractWebMessageResponse
{
    private List<ErrorReport> errorReports = new ArrayList<>();

    public ErrorReportsWebMessageResponse( List<ErrorReport> errorReports )
    {
        this.errorReports = errorReports;
    }

    @JsonProperty
    public List<ErrorReport> getErrorReports()
    {
        return errorReports;
    }

    public void setErrorReports( List<ErrorReport> errorReports )
    {
        this.errorReports = errorReports;
    }
}
