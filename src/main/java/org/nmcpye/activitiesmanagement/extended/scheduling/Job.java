package org.nmcpye.activitiesmanagement.extended.scheduling;

import org.nmcpye.activitiesmanagement.domain.JobConfiguration;
import org.nmcpye.activitiesmanagement.extended.feedback.ErrorReport;

/**
 * This interface is used for jobs in the system which are scheduled or executed by the Spring
 * scheduler. The actual job will contain an execute method which performs the appropriate actions.
 * <p>
 * {@link JobInstance} is another interface connected to jobs. This interface is used for the
 * actual execution of the job. See {@link SchedulingManager} for more information about the scheduling.
 *
 */
public interface Job {
    JobType getJobType();

    void execute(JobConfiguration jobConfiguration);

    default ErrorReport validate()
    {
        return null;
    }
}
