package org.nmcpye.activitiesmanagement.extended.dxf2module.webmessage.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.nmcpye.activitiesmanagement.extended.dxf2module.webmessage.AbstractWebMessageResponse;
import org.nmcpye.activitiesmanagement.extended.feedback.ObjectReport;
import org.nmcpye.activitiesmanagement.extended.feedback.Stats;
import org.nmcpye.activitiesmanagement.extended.feedback.Status;
import org.nmcpye.activitiesmanagement.extended.feedback.TypeReport;
import org.springframework.util.Assert;

import java.util.List;

public class TypeReportWebMessageResponse
    extends AbstractWebMessageResponse
{
    private final TypeReport typeReport;

    public TypeReportWebMessageResponse( TypeReport typeReport )
    {
        Assert.notNull( typeReport, "ImportReport is require to be non-null." );
        this.typeReport = typeReport;
    }

    @JsonProperty
    public Status getStatus()
    {
        return typeReport.getErrorReports().isEmpty() ? Status.OK : Status.ERROR;
    }

    @JsonProperty
    public Stats getStats()
    {
        return typeReport.getStats();
    }

    @JsonProperty
    public List<ObjectReport> getObjectReports()
    {
        return typeReport.getObjectReports();
    }
}
