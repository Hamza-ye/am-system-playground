package org.nmcpye.activitiesmanagement.extended.system;

import static com.google.common.base.Preconditions.checkNotNull;

import org.nmcpye.activitiesmanagement.extended.organisationunit.OrganisationUnitService;
import org.springframework.stereotype.Component;

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
        organisationUnitService.updatePaths();
    }
}
