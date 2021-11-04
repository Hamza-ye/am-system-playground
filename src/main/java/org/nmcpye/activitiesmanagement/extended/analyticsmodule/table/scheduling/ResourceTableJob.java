package org.nmcpye.activitiesmanagement.extended.analyticsmodule.table.scheduling;

import org.nmcpye.activitiesmanagement.domain.JobConfiguration;
import org.nmcpye.activitiesmanagement.extended.analyticsmodule.AnalyticsTableGenerator;
import org.nmcpye.activitiesmanagement.extended.scheduling.JobType;
import org.nmcpye.activitiesmanagement.extended.scheduling.schedulingcoremodule.AbstractJob;
import org.springframework.stereotype.Component;

import static com.google.common.base.Preconditions.checkNotNull;

@Component( "resourceTableJob" )
public class ResourceTableJob
    extends AbstractJob
{
    private final AnalyticsTableGenerator analyticsTableGenerator;

    public ResourceTableJob(AnalyticsTableGenerator analyticsTableGenerator )
    {
        checkNotNull( analyticsTableGenerator );

        this.analyticsTableGenerator = analyticsTableGenerator;
    }

    // -------------------------------------------------------------------------
    // Implementation
    // -------------------------------------------------------------------------

    @Override
    public JobType getJobType()
    {
        return JobType.RESOURCE_TABLE;
    }

    @Override
    public void execute( JobConfiguration jobConfiguration )
    {
        analyticsTableGenerator.generateResourceTables( jobConfiguration );
    }
}
