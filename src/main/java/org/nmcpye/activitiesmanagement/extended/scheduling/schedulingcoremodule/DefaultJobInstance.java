package org.nmcpye.activitiesmanagement.extended.scheduling.schedulingcoremodule;

import com.google.common.base.Preconditions;
import java.util.Date;
import org.nmcpye.activitiesmanagement.domain.JobConfiguration;
import org.nmcpye.activitiesmanagement.extended.common.DebugUtils;
import org.nmcpye.activitiesmanagement.extended.scheduling.JobInstance;
import org.nmcpye.activitiesmanagement.extended.scheduling.JobStatus;
import org.nmcpye.activitiesmanagement.extended.scheduling.SchedulingManager;
import org.nmcpye.activitiesmanagement.extended.system.util.Clock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component("org.nmcpye.activitiesmanagement.extended.scheduling.JobInstance")
public class DefaultJobInstance implements JobInstance {

    private final Logger log = LoggerFactory.getLogger(DefaultJobInstance.class);

    private static final String NOT_LEADER_SKIP_LOG = "Not a leader, skipping job with jobType:%s and name:%s";

    private SchedulingManager schedulingManager;

    //    private MessageService messageService;
    //
    //    private LeaderManager leaderManager;

    @SuppressWarnings("unused")
    private DefaultJobInstance() {}

    public DefaultJobInstance(SchedulingManager schedulingManager) {
        this.schedulingManager = schedulingManager;
        //        this.messageService = messageService;
        //        this.leaderManager = leaderManager;

        Preconditions.checkNotNull(schedulingManager);
        //        Preconditions.checkNotNull( messageService );
        //        Preconditions.checkNotNull( leaderManager );
    }

    @Override
    public void execute(JobConfiguration jobConfiguration) {
        if (!jobConfiguration.isEnabled()) {
            return;
        }

        //        if ( jobConfiguration.isLeaderOnlyJob() && !leaderManager.isLeader() )
        //        {
        //            log.debug( String.format( NOT_LEADER_SKIP_LOG, jobConfiguration.getJobType(), jobConfiguration.getName() ) );
        //            return;
        //        }

        final Clock clock = new Clock().startClock();

        try {
            if (jobConfiguration.isInMemoryJob()) {
                executeJob(jobConfiguration, clock);
            } else if (!schedulingManager.isJobConfigurationRunning(jobConfiguration)) {
                jobConfiguration.setJobStatus(JobStatus.RUNNING);
                schedulingManager.jobConfigurationStarted(jobConfiguration);
                jobConfiguration.setNextExecutionTime(null);

                executeJob(jobConfiguration, clock);

                jobConfiguration.setLastExecutedStatus(JobStatus.COMPLETED);
            } else {
                String message = String.format(
                    "Job failed: '%s', job type already running: '%s'",
                    jobConfiguration.getName(),
                    jobConfiguration.getJobType()
                );
                log.error(message);
                //                messageService.sendSystemErrorNotification( message, new RuntimeException( message ) );

                jobConfiguration.setLastExecutedStatus(JobStatus.FAILED);
            }
        } catch (Exception ex) {
            String message = String.format("Job failed: '%s'", jobConfiguration.getName());
            //            messageService.sendSystemErrorNotification( message, ex );
            log.error(message, ex);
            log.error(DebugUtils.getStackTrace(ex));

            jobConfiguration.setLastExecutedStatus(JobStatus.FAILED);
        } finally {
            setFinishingStatus(clock, jobConfiguration);
        }
    }

    /**
     * Set status properties of job after finish. If the job was executed manually and the job is disabled we want
     * to set the status back to DISABLED.
     *
     * @param clock Clock for keeping track of time usage.
     * @param jobConfiguration the job configuration.
     */
    private void setFinishingStatus(Clock clock, JobConfiguration jobConfiguration) {
        if (jobConfiguration.isInMemoryJob()) {
            return;
        }

        if (!jobConfiguration.isEnabled()) {
            jobConfiguration.setJobStatus(JobStatus.DISABLED);
        } else {
            jobConfiguration.setJobStatus(JobStatus.SCHEDULED);
        }

        jobConfiguration.setNextExecutionTime(null);
        jobConfiguration.setLastExecuted(new Date());
        jobConfiguration.setLastRuntimeExecution(clock.time());

        schedulingManager.jobConfigurationFinished(jobConfiguration);
    }

    /**
     * Method which calls the execute method in the job. The job will run in this thread and finish,
     * either with success or with an exception.
     *
     * @param jobConfiguration the configuration to execute.
     * @param clock refers to start time.
     */
    private void executeJob(JobConfiguration jobConfiguration, Clock clock) {
        log.debug(String.format("Job started: '%s'", jobConfiguration.getName()));

        schedulingManager.getJob(jobConfiguration.getJobType()).execute(jobConfiguration);

        log.debug(String.format("Job executed successfully: '%s'. Time used: '%s'", jobConfiguration.getName(), clock.time()));
    }
}
