package org.nmcpye.activitiesmanagement.extended.servicecoremodule.startup;

import org.nmcpye.activitiesmanagement.extended.organisationunit.OrganisationUnitService;
import org.nmcpye.activitiesmanagement.extended.systemmodule.system.startup.TransactionContextStartupRoutine;
import org.springframework.stereotype.Component;

import static com.google.common.base.Preconditions.checkNotNull;

@Component
public class ModelUpgrader extends TransactionContextStartupRoutine {

    private final OrganisationUnitService organisationUnitService;

    public ModelUpgrader(OrganisationUnitService organisationUnitService) {
        checkNotNull(organisationUnitService);
        this.organisationUnitService = organisationUnitService;
        this.setRunlevel(7);
        this.setSkipInTests(true);
    }

    // -------------------------------------------------------------------------
    // Execute
    // -------------------------------------------------------------------------

    @Override
    public void executeInTransaction() {
//        organisationUnitService.updatePaths();
        organisationUnitService.forceUpdatePaths();
    }
}
