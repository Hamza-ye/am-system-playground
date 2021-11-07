package org.nmcpye.activitiesmanagement.extended.web.rest;

import org.nmcpye.activitiesmanagement.domain.JobConfiguration;
import org.nmcpye.activitiesmanagement.extended.scheduling.JobType;
import org.nmcpye.activitiesmanagement.extended.scheduling.SchedulingManager;
import org.nmcpye.activitiesmanagement.extended.web.service.WebMessageService;
import org.nmcpye.activitiesmanagement.security.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.nmcpye.activitiesmanagement.extended.dxf2module.webmessage.WebMessageUtils.jobConfigurationReport;

/**
 * Created by Hamza on 03/11/2021.
 */
@Controller
@RequestMapping( value = ResourceTableController.RESOURCE_PATH )
public class ResourceTableController {

    public static final String RESOURCE_PATH = "/api/resourceTables";

    private final SchedulingManager schedulingManager;

    private final WebMessageService webMessageService;

    @Autowired
    public ResourceTableController(SchedulingManager schedulingManager, WebMessageService webMessageService) {
        this.schedulingManager = schedulingManager;
        this.webMessageService = webMessageService;
    }

    @RequestMapping( value = "/analytics", method = { RequestMethod.PUT, RequestMethod.POST } )
//    @PreAuthorize( "hasRole('ALL') or hasRole('F_PERFORM_MAINTENANCE')" )
    public void analytics(
        @RequestParam( required = false ) boolean skipResourceTables,
        @RequestParam( required = false ) boolean skipAggregate,
        @RequestParam( required = false ) boolean skipEvents,
        @RequestParam( required = false ) boolean skipEnrollment,
        @RequestParam( required = false ) Integer lastYears,
        HttpServletResponse response, HttpServletRequest request ) {

    }

    @RequestMapping( method = { RequestMethod.PUT, RequestMethod.POST } )
//    @PreAuthorize( "hasRole('ALL') or hasRole('F_PERFORM_MAINTENANCE')" )
    public void resourceTables( HttpServletResponse response, HttpServletRequest request ) {
        JobConfiguration resourceTableJob = new JobConfiguration( "inMemoryResourceTableJob",
            JobType.RESOURCE_TABLE, SecurityUtils.getCurrentUserLogin().orElse(null), true );

        schedulingManager.executeJob( resourceTableJob );

        webMessageService.send( jobConfigurationReport( resourceTableJob ), response, request );
    }
}
