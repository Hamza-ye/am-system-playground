package org.nmcpye.activitiesmanagement.extended.systemmodule.system.notification;

import org.nmcpye.activitiesmanagement.domain.JobConfiguration;
import org.nmcpye.activitiesmanagement.extended.scheduling.JobType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.*;

public class InMemoryNotifier implements Notifier
{
    private final Logger log = LoggerFactory.getLogger(InMemoryNotifier.class);

    private NotificationMap notificationMap = new NotificationMap();

    // -------------------------------------------------------------------------
    // Notifier implementation
    // -------------------------------------------------------------------------

    @Override
    public Notifier notify(JobConfiguration id, String message )
    {
        return notify( id, NotificationLevel.INFO, message, false );
    }

    @Override
    public Notifier notify( JobConfiguration id, NotificationLevel level, String message )
    {
        return notify( id, level, message, false );
    }

    @Override
    public Notifier notify( JobConfiguration id, NotificationLevel level, String message, boolean completed )
    {
        if ( id != null && !(level != null && level.isOff()) )
        {
            Notification notification = new Notification( level, id.getJobType(), new Date(), message, completed );

            if ( id.isInMemoryJob() && !StringUtils.isEmpty( id.getUid() ) )
            {
                notification.setUid( id.getUid() );
            }

            notificationMap.add( id, notification );

            log.info( notification.toString() );
        }

        return this;
    }

    @Override
    public Notifier update( JobConfiguration id, String message )
    {
        return update( id, NotificationLevel.INFO, message, false );
    }

    @Override
    public Notifier update( JobConfiguration id, String message, boolean completed )
    {
        return update( id, NotificationLevel.INFO, message, completed );
    }

    @Override
    public Notifier update( JobConfiguration id, NotificationLevel level, String message )
    {
        return update( id, level, message, false );
    }

    @Override
    public Notifier update( JobConfiguration id, NotificationLevel level, String message, boolean completed )
    {
        if ( id != null && !(level != null && level.isOff()) )
        {
            notify( id, level, message, completed );
        }

        return this;
    }

    @Override
    public List<Notification> getLastNotificationsByJobType(JobType jobType, String lastId )
    {
        List<Notification> list = new ArrayList<>();

        for ( Notification notification : notificationMap.getLastNotificationsByJobType( jobType ) )
        {
            if ( lastId != null && lastId.equals( notification.getUid() ) )
            {
                if ( list.isEmpty() )
                {
                    list.add( notification );
                }
                break;
            }
            list.add( notification );
        }

        return list;
    }

    @Override
    public Map<JobType, LinkedHashMap<String, LinkedList<Notification>>> getNotifications()
    {
        return notificationMap.getNotifications();
    }

    @Override
    public List<Notification> getNotificationsByJobId( JobType jobType, String jobId )
    {
        return notificationMap.getNotificationsByJobId( jobType, jobId );
    }

    @Override
    public Map<String, LinkedList<Notification>> getNotificationsByJobType( JobType jobType )
    {
        return notificationMap.getNotificationsWithType( jobType );
    }

    @Override
    public Notifier clear( JobConfiguration id )
    {
        if ( id != null )
        {
            notificationMap.clear( id );
        }

        return this;
    }

    @Override
    public Notifier addJobSummary( JobConfiguration id, Object jobSummary, Class<?> jobSummaryType )
    {
        return addJobSummary( id, NotificationLevel.INFO, jobSummary, jobSummaryType );
    }

    @Override
    public Notifier addJobSummary( JobConfiguration id, NotificationLevel level, Object jobSummary,
        Class<?> jobSummaryType )
    {
        if ( id != null && !(level != null && level.isOff()) )
        {
            notificationMap.addSummary( id, jobSummary );
        }

        return this;
    }

    @Override
    public Object getJobSummariesForJobType( JobType jobType )
    {
        return notificationMap.getJobSummariesForJobType( jobType );
    }

    @Override
    public Object getJobSummary( JobType jobType )
    {
        return notificationMap.getSummary( jobType );
    }

    @Override
    public Object getJobSummaryByJobId( JobType jobType, String jobId )
    {
        return notificationMap.getSummary( jobType, jobId );
    }
}
