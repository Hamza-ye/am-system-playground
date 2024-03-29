package org.nmcpye.activitiesmanagement.extended.systemmodule.system.notification;

import org.nmcpye.activitiesmanagement.domain.scheduling.JobConfiguration;
import org.nmcpye.activitiesmanagement.extended.scheduling.JobType;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public interface Notifier
{
    Notifier notify(JobConfiguration id, String message);

    Notifier notify(JobConfiguration id, NotificationLevel level, String message);

    Notifier notify(JobConfiguration id, NotificationLevel level, String message, boolean completed);

    Notifier update(JobConfiguration id, String message);

    Notifier update(JobConfiguration id, String message, boolean completed);

    Notifier update(JobConfiguration id, NotificationLevel level, String message);

    Notifier update(JobConfiguration id, NotificationLevel level, String message, boolean completed);

    Map<JobType, LinkedHashMap<String, LinkedList<Notification>>> getNotifications();

    List<Notification> getLastNotificationsByJobType(JobType jobType, String lastId);

    List<Notification> getNotificationsByJobId(JobType jobType, String jobId);

    Map<String, LinkedList<Notification>> getNotificationsByJobType(JobType jobType);

    Notifier clear(JobConfiguration id);

    Notifier addJobSummary(JobConfiguration id, Object taskSummary, Class<?> jobSummaryType);

    Notifier addJobSummary(JobConfiguration id, NotificationLevel level, Object jobSummary, Class<?> jobSummaryType);

    Object getJobSummariesForJobType(JobType jobType);

    Object getJobSummary(JobType jobType);

    Object getJobSummaryByJobId(JobType jobType, String jobId);
}
