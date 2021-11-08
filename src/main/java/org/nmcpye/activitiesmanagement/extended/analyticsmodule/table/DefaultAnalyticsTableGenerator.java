package org.nmcpye.activitiesmanagement.extended.analyticsmodule.table;

import org.nmcpye.activitiesmanagement.domain.JobConfiguration;
import org.nmcpye.activitiesmanagement.extended.analyticsmodule.AnalyticsTableGenerator;
import org.nmcpye.activitiesmanagement.extended.administrationModule.resourcetable.ResourceTableService;
import org.nmcpye.activitiesmanagement.extended.systemmodule.system.util.Clock;
import org.nmcpye.activitiesmanagement.extended.systemmodule.system.notification.Notifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Date;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.nmcpye.activitiesmanagement.extended.systemmodule.system.notification.NotificationLevel.ERROR;
import static org.nmcpye.activitiesmanagement.extended.systemmodule.system.notification.NotificationLevel.INFO;

@Service("org.nmcpye.activitiesmanagement.extended.analyticsmodule.AnalyticsTableGenerator")
public class DefaultAnalyticsTableGenerator
    implements AnalyticsTableGenerator {

    private final Logger log = LoggerFactory.getLogger(DefaultAnalyticsTableGenerator.class);

    private ResourceTableService resourceTableService;

    private Notifier notifier;

    public DefaultAnalyticsTableGenerator(ResourceTableService resourceTableService, Notifier notifier) {
        checkNotNull(resourceTableService);
        checkNotNull(notifier);

        this.resourceTableService = resourceTableService;
        this.notifier = notifier;
    }

    // -------------------------------------------------------------------------
    // Implementation
    // -------------------------------------------------------------------------

    @Override
    public void dropTables() {
//        for ( AnalyticsTableService service : analyticsTableServices )
//        {
//            service.dropTables();
//        }
    }

    @Override
    public void generateResourceTables(JobConfiguration jobId) {
        final Clock clock = new Clock().startClock();

        notifier.notify(jobId, "Generating resource tables");

        try {
            generateResourceTables();

            notifier.notify(jobId, INFO, "Resource tables generated: " + clock.time(), true);
        } catch (RuntimeException ex) {
            notifier.notify(jobId, ERROR, "Process failed: " + ex.getMessage(), true);

//            messageService.sendSystemErrorNotification( "Resource table process failed", ex );

            throw ex;
        }
    }

    // -------------------------------------------------------------------------
    // Supportive methods
    // -------------------------------------------------------------------------

    private void generateResourceTables() {
        final Date startTime = new Date();

//        resourceTableService.dropAllSqlViews();
        resourceTableService.generateOrganisationUnitStructures();
//        resourceTableService.generateDataSetOrganisationUnitCategoryTable();
//        resourceTableService.generateCategoryOptionComboNames();
//        resourceTableService.generateDataElementGroupSetTable();
//        resourceTableService.generateIndicatorGroupSetTable();
        resourceTableService.generateOrganisationUnitGroupSetTable();
//        resourceTableService.generateCategoryTable();
//        resourceTableService.generateDataElementTable();
        resourceTableService.generatePeriodTable();
        resourceTableService.generateDatePeriodTable();
//        resourceTableService.generateCategoryOptionComboTable();
//        resourceTableService.createAllSqlViews();

//        systemSettingManager.saveSystemSetting( SettingKey.LAST_SUCCESSFUL_RESOURCE_TABLES_UPDATE, startTime );
    }
}
