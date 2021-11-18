package org.nmcpye.activitiesmanagement.extended.servicecoremodule.startup;

import org.nmcpye.activitiesmanagement.extended.organisationunit.OrganisationUnitServiceExt;
import org.nmcpye.activitiesmanagement.extended.systemmodule.system.startup.TransactionContextStartupRoutine;
import org.springframework.stereotype.Component;

import static com.google.common.base.Preconditions.checkNotNull;

@Component
public class ModelUpgrader extends TransactionContextStartupRoutine {

    private final OrganisationUnitServiceExt organisationUnitServiceExt;

    public ModelUpgrader(OrganisationUnitServiceExt organisationUnitServiceExt) {
        checkNotNull(organisationUnitServiceExt);
        this.organisationUnitServiceExt = organisationUnitServiceExt;
        this.setRunlevel(7);
        this.setSkipInTests(true);
    }

    // -------------------------------------------------------------------------
    // Execute
    // -------------------------------------------------------------------------

    @Override
    public void executeInTransaction() {
//        organisationUnitService.updatePaths();
        organisationUnitServiceExt.forceUpdatePaths();
    }
}
