package org.nmcpye.activitiesmanagement.extended.web.rest;

import org.nmcpye.activitiesmanagement.extended.render.RenderService;
import org.nmcpye.activitiesmanagement.extended.scheduling.JobType;
import org.nmcpye.activitiesmanagement.extended.systemmodule.notification.Notification;
import org.nmcpye.activitiesmanagement.extended.systemmodule.notification.Notifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

import static org.nmcpye.activitiesmanagement.extended.web.utils.ContextUtils.CONTENT_TYPE_JSON;
import static org.nmcpye.activitiesmanagement.extended.web.utils.ContextUtils.setNoStore;

/**
 * Created by Hamza on 05/11/2021.
 */
@Controller
@RequestMapping( value = SystemController.RESOURCE_PATH )
public class SystemController {
    public static final String RESOURCE_PATH = "/api/system";

    private final Notifier notifier;

    private final RenderService renderService;

    @Autowired
    public SystemController(Notifier notifier, RenderService renderService) {
        this.notifier = notifier;
        this.renderService = renderService;
    }

    // -------------------------------------------------------------------------
    // Tasks
    // -------------------------------------------------------------------------

    @RequestMapping( value = "/tasks", method = RequestMethod.GET, produces = { "*/*", "application/json" } )
    public void getTasksJson( HttpServletResponse response )
        throws IOException
    {
        setNoStore( response );
        response.setContentType( CONTENT_TYPE_JSON );

        renderService.toJson( response.getOutputStream(), notifier.getNotifications() );
    }

    @RequestMapping( value = "/tasks/{jobType}", method = RequestMethod.GET, produces = { "*/*", "application/json" } )
    public void getTaskJson(@PathVariable( "jobType" ) String jobType, @RequestParam( required = false ) String lastId, HttpServletResponse response )
        throws IOException
    {
        List<Notification> notifications = new ArrayList<>();

        if ( jobType != null )
        {
            notifications = notifier.getLastNotificationsByJobType( JobType.valueOf( jobType.toUpperCase() ), lastId );
        }

        setNoStore( response );
        response.setContentType( CONTENT_TYPE_JSON );

        renderService.toJson( response.getOutputStream(), notifications );
    }

    @RequestMapping( value = "/tasksExtended/{jobType}", method = RequestMethod.GET, produces = { "*/*", "application/json" } )
    public void getTasksExtendedJson( @PathVariable( "jobType" ) String jobType, HttpServletResponse response ) throws IOException
    {
        Map<String, LinkedList<Notification>> notifications = new HashMap<>();

        if ( jobType != null )
        {
            notifications = notifier.getNotificationsByJobType( JobType.valueOf( jobType.toUpperCase() ) );
        }

        setNoStore( response );
        response.setContentType( CONTENT_TYPE_JSON );

        renderService.toJson( response.getOutputStream(), notifications );
    }

    @RequestMapping( value = "/tasks/{jobType}/{jobId}", method = RequestMethod.GET, produces = { "*/*", "application/json" } )
    public void getTaskJsonByUid( @PathVariable( "jobType" ) String jobType, @PathVariable( "jobId" ) String jobId,
                                  HttpServletResponse response )
        throws IOException
    {
        List<Notification> notifications = new ArrayList<>();

        if ( jobType != null )
        {
            notifications = notifier.getNotificationsByJobId( JobType.valueOf( jobType.toUpperCase() ), jobId );
        }

        setNoStore( response );
        response.setContentType( CONTENT_TYPE_JSON );

        renderService.toJson( response.getOutputStream(), notifications );
    }
}
