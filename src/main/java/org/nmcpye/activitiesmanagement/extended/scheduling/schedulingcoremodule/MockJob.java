package org.nmcpye.activitiesmanagement.extended.scheduling.schedulingcoremodule;

import org.nmcpye.activitiesmanagement.domain.scheduling.JobConfiguration;
import org.nmcpye.activitiesmanagement.extended.scheduling.JobType;
import org.springframework.stereotype.Component;

@Component("mockJob")
public class MockJob extends AbstractJob {

    @Override
    public JobType getJobType() {
        return JobType.MOCK;
    }

    @Override
    public void execute(JobConfiguration jobConfiguration) {
        try {
            Thread.sleep(10000);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            ex.printStackTrace();
        }
    }
}
