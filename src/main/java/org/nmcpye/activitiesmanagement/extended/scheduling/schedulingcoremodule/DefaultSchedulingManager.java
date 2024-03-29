package org.nmcpye.activitiesmanagement.extended.scheduling.schedulingcoremodule;

import org.nmcpye.activitiesmanagement.domain.scheduling.JobConfiguration;
import org.nmcpye.activitiesmanagement.extended.scheduling.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.core.task.AsyncListenableTaskExecutor;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ScheduledFuture;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.nmcpye.activitiesmanagement.extended.scheduling.JobStatus.DISABLED;
import static org.nmcpye.activitiesmanagement.extended.util.DateUtils.getMediumDateString;

/**
 * Cron refers to the cron expression used for scheduling. Key refers to the key
 * identifying the scheduled jobs.
 *
 */

@Service("org.nmcpye.activitiesmanagement.extended.scheduling.SchedulingManager")
public class DefaultSchedulingManager implements SchedulingManager {

    private final Logger log = LoggerFactory.getLogger(DefaultSchedulingManager.class);

    private static final int DEFAULT_INITIAL_DELAY_S = 10;

    private Map<String, ScheduledFuture<?>> futures = new HashMap<>();

    private Map<String, ListenableFuture<?>> currentTasks = new HashMap<>();

    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private final JobConfigurationService jobConfigurationService;

    //    private final MessageService messageService;

    //    private final LeaderManager leaderManager;

    private final TaskScheduler jobScheduler;

    private final AsyncListenableTaskExecutor jobExecutor;

    private final ApplicationContext applicationContext;

    public DefaultSchedulingManager(
        JobConfigurationService jobConfigurationService,
        @Qualifier("taskScheduler") TaskScheduler jobScheduler,
        @Qualifier("taskScheduler") AsyncListenableTaskExecutor jobExecutor,
        ApplicationContext applicationContext
    ) {
        checkNotNull(jobConfigurationService);
        checkNotNull(jobScheduler);
        checkNotNull(jobExecutor);
        checkNotNull(applicationContext);

        this.jobConfigurationService = jobConfigurationService;
        this.jobScheduler = jobScheduler;
        this.jobExecutor = jobExecutor;
        this.applicationContext = applicationContext;
    }

    //    @PostConstruct
    //    public void init()
    //    {
    //        leaderManager.setSchedulingManager( this );
    //    }

    // -------------------------------------------------------------------------
    // Queue
    // -------------------------------------------------------------------------

    /**
     * List of currently running jobs.
     */
    private List<JobConfiguration> runningJobConfigurations = new CopyOnWriteArrayList<>();

    @Override
    public boolean isJobConfigurationRunning(JobConfiguration jobConfiguration) {
        if (jobConfiguration.isInMemoryJob()) {
            return false;
        }

        return runningJobConfigurations.stream().anyMatch(jc -> jc.getJobType().equals(jobConfiguration.getJobType()));
    }

    @Override
    public void jobConfigurationStarted(JobConfiguration jobConfiguration) {
        if (!jobConfiguration.isInMemoryJob()) {
            runningJobConfigurations.add(jobConfiguration);
            jobConfigurationService.updateJobConfiguration(jobConfiguration);
        }
    }

    @Override
    public void jobConfigurationFinished(JobConfiguration jobConfiguration) {
        runningJobConfigurations.remove(jobConfiguration);

        JobConfiguration tempJobConfiguration = jobConfigurationService.getJobConfigurationByUid(jobConfiguration.getUid());

        if (tempJobConfiguration != null) {
            if (tempJobConfiguration.getJobStatus() == DISABLED) {
                jobConfiguration.setJobStatus(DISABLED);
                jobConfiguration.setEnabled(false);
            }

            jobConfigurationService.updateJobConfiguration(jobConfiguration);
        }
    }

    // -------------------------------------------------------------------------
    // SchedulingManager implementation
    // -------------------------------------------------------------------------

    @Override
    public void scheduleJob(JobConfiguration jobConfiguration) {
        if (ifJobInSystemStop(jobConfiguration.getUid())) {
            //            JobInstance jobInstance = new DefaultJobInstance( this, messageService, leaderManager );
            JobInstance jobInstance = new DefaultJobInstance(this);

            if (jobConfiguration.getUid() != null && !futures.containsKey(jobConfiguration.getUid())) {
                log.info(String.format("Scheduling job: %s", jobConfiguration));

                ScheduledFuture<?> future = null;

                if (jobConfiguration.getJobType().isCronSchedulingType()) {
                    future =
                        jobScheduler.schedule(
                            () -> jobInstance.execute(jobConfiguration),
                            new CronTrigger(jobConfiguration.getCronExpression())
                        );
                } else if (jobConfiguration.getJobType().isFixedDelaySchedulingType()) {
                    future =
                        jobScheduler.scheduleWithFixedDelay(
                            () -> jobInstance.execute(jobConfiguration),
                            Instant.now().plusSeconds(DEFAULT_INITIAL_DELAY_S),
                            Duration.of(jobConfiguration.getDelay(), ChronoUnit.SECONDS)
                        );
                }

                futures.put(jobConfiguration.getUid(), future);

                log.info(String.format("Scheduled job: %s", jobConfiguration));
            }
        }
    }

    @Override
    public void scheduleJobWithStartTime(JobConfiguration jobConfiguration, Date startTime) {
        if (ifJobInSystemStop(jobConfiguration.getUid())) {
            //            JobInstance jobInstance = new DefaultJobInstance( this, messageService, leaderManager );
            JobInstance jobInstance = new DefaultJobInstance(this);

            if (jobConfiguration.getUid() != null && !futures.containsKey(jobConfiguration.getUid())) {
                ScheduledFuture<?> future = jobScheduler.schedule(() -> jobInstance.execute(jobConfiguration), startTime);

                futures.put(jobConfiguration.getUid(), future);

                log.info(String.format("Scheduled job: %s with start time: %s", jobConfiguration, getMediumDateString(startTime)));
            }
        }
    }

    @Override
    public void stopJob(JobConfiguration jobConfiguration) {
        if (isJobInSystem(jobConfiguration.getUid())) {
            jobConfiguration.setLastExecutedStatus(JobStatus.STOPPED);
            jobConfigurationService.updateJobConfiguration(jobConfiguration);

            internalStopJob(jobConfiguration.getUid());
        }
    }

    @Override
    public boolean executeJob(JobConfiguration jobConfiguration) {
        if (jobConfiguration != null && !isJobConfigurationRunning(jobConfiguration)) {
            internalExecuteJobConfiguration(jobConfiguration);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void executeJob(Runnable job) {
        jobExecutor.execute(job);
    }

    @Override
    public Map<String, ScheduledFuture<?>> getAllFutureJobs() {
        return futures;
    }

    @Override
    public Job getJob(JobType jobType) {
        return (Job) applicationContext.getBean(jobType.getKey());
    }

    @Override
    public <T> ListenableFuture<T> executeJob(Callable<T> callable) {
        return jobExecutor.submitListenable(callable);
    }

    // -------------------------------------------------------------------------
    // Supportive methods
    // -------------------------------------------------------------------------

    private void internalExecuteJobConfiguration(JobConfiguration jobConfiguration) {
        //        JobInstance jobInstance = new DefaultJobInstance( this, messageService, leaderManager );
        JobInstance jobInstance = new DefaultJobInstance(this);

        ListenableFuture<?> future = jobExecutor.submitListenable(() -> jobInstance.execute(jobConfiguration));

        currentTasks.put(jobConfiguration.getUid(), future);

        log.info(String.format("Scheduler initiated execution of job: %s", jobConfiguration));
    }

    private boolean internalStopJob(String uid) {
        if (uid != null) {
            ScheduledFuture<?> future = futures.get(uid);

            if (future == null) {
                log.info(String.format("Tried to stop job but job was not found with key: '%s'", uid));

                return true;
            } else {
                boolean result = future.cancel(true);

                futures.remove(uid);

                log.info(String.format("Stopped job with key: '%s' with successful result: '%b'", uid, result));

                return result;
            }
        }

        return false;
    }

    private boolean ifJobInSystemStop(String jobKey) {
        return !isJobInSystem(jobKey) || internalStopJob(jobKey);
    }

    private boolean isJobInSystem(String jobKey) {
        return futures.get(jobKey) != null || currentTasks.get(jobKey) != null;
    }
}
