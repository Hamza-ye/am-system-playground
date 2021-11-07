package org.nmcpye.activitiesmanagement.extended.dxf2module.scheduling;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.nmcpye.activitiesmanagement.domain.JobConfiguration;
import org.nmcpye.activitiesmanagement.extended.dxf2module.webmessage.WebMessageResponse;
import org.nmcpye.activitiesmanagement.extended.scheduling.JobParameters;
import org.nmcpye.activitiesmanagement.extended.scheduling.JobStatus;
import org.nmcpye.activitiesmanagement.extended.scheduling.JobType;

import java.util.Date;

public class JobConfigurationWebMessageResponse
    implements WebMessageResponse
{
    private String name;

    private String id;

    private Date created;

    private JobType jobType;

    private JobStatus jobStatus;

    private JobParameters jobParameters;

    private String relativeNotifierEndpoint;

    public JobConfigurationWebMessageResponse(JobConfiguration jobConfiguration )
    {
        this.name = jobConfiguration.getDisplayName();
        this.id = jobConfiguration.getUid();
        this.created = jobConfiguration.getCreated();
        this.jobType = jobConfiguration.getJobType();
        this.jobStatus = jobConfiguration.getJobStatus();
        this.jobParameters = jobConfiguration.getJobParameters();
        this.relativeNotifierEndpoint = "/api/system/tasks/" + this.jobType + "/" + this.id;
    }

    @JsonProperty
    public String getName()
    {
        return name;
    }

    @JsonProperty
    public String getId()
    {
        return id;
    }

    @JsonProperty
    public Date getCreated()
    {
        return created;
    }

    @JsonProperty
    public JobType getJobType()
    {
        return jobType;
    }

    @JsonProperty
    public JobStatus getJobStatus()
    {
        return jobStatus;
    }

    @JsonProperty
    public JobParameters getJobParameters()
    {
        return jobParameters;
    }

    @JsonProperty
    public String getRelativeNotifierEndpoint()
    {
        return relativeNotifierEndpoint;
    }
}
