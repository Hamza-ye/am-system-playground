package org.nmcpye.activitiesmanagement.extended.web.rest;

import org.nmcpye.activitiesmanagement.extended.scheduling.SchedulingManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Hamza on 03/11/2021.
 */
@Controller
@RequestMapping( value = ResourceTableController.RESOURCE_PATH )
public class ResourceTableController {

    public static final String RESOURCE_PATH = "/api/resourceTables";

    @Autowired
    private SchedulingManager schedulingManager;


}
