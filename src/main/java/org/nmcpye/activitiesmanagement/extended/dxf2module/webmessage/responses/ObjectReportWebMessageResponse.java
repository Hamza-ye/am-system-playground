package org.nmcpye.activitiesmanagement.extended.dxf2module.webmessage.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.nmcpye.activitiesmanagement.extended.dxf2module.webmessage.AbstractWebMessageResponse;
import org.nmcpye.activitiesmanagement.extended.feedback.ErrorReport;
import org.nmcpye.activitiesmanagement.extended.feedback.ObjectReport;
import org.springframework.util.Assert;

import java.util.List;

public class ObjectReportWebMessageResponse
    extends AbstractWebMessageResponse
{
    private final ObjectReport objectReport;

    public ObjectReportWebMessageResponse( ObjectReport objectReport )
    {
        Assert.notNull( objectReport, "ObjectReport is required to be non-null." );
        this.objectReport = objectReport;
    }

    @JsonProperty
    public Class<?> getKlass()
    {
        return objectReport.getKlass();
    }

    @JsonProperty
    public String getUid()
    {
        return objectReport.getUid();
    }

    @JsonProperty
    public List<ErrorReport> getErrorReports()
    {
        return objectReport.getErrorReports();
    }
}
