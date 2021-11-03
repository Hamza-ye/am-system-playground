package org.nmcpye.activitiesmanagement.extended.scheduling.schedulingcoremodule;

import org.nmcpye.activitiesmanagement.extended.feedback.ErrorReport;
import org.nmcpye.activitiesmanagement.extended.scheduling.Job;

/**
 * All jobs related to the system extends AbstractJob and can override the validate method.
 *
 */
public abstract class AbstractJob implements Job {

    @Override
    public ErrorReport validate() {
        return null;
    }
}
