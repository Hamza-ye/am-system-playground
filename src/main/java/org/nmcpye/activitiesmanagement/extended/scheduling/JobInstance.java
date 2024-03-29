package org.nmcpye.activitiesmanagement.extended.scheduling;

import org.nmcpye.activitiesmanagement.domain.scheduling.JobConfiguration;

/**
 * This interface is an abstraction for the actual execution of jobs based on a job configuration.
 *
 */
public interface JobInstance {
    /**
     * This method will try to execute the actual job. It will verify a set of
     * parameters, such as no other jobs of the same JobType is running. If the
     * JobConfiguration is disabled it will not run.
     *
     * @param jobConfiguration  the configuration of the job.
     */
    void execute(JobConfiguration jobConfiguration);
}
