package org.nmcpye.activitiesmanagement.extended.administrationModule.resourcetable;

import org.nmcpye.activitiesmanagement.domain.organisationunit.OrganisationUnitGroupSet;
import org.nmcpye.activitiesmanagement.extended.administrationModule.resourcetable.table.OrganisationUnitStructureResourceTable;
import org.nmcpye.activitiesmanagement.extended.common.IdentifiableObjectManager;
import org.nmcpye.activitiesmanagement.extended.organisationunit.OrganisationUnitServiceExt;
import org.nmcpye.activitiesmanagement.extended.period.PeriodServiceExt;
import org.nmcpye.activitiesmanagement.extended.administrationModule.resourcetable.table.DatePeriodResourceTable;
import org.nmcpye.activitiesmanagement.extended.administrationModule.resourcetable.table.OrganisationUnitGroupSetResourceTable;
import org.nmcpye.activitiesmanagement.extended.administrationModule.resourcetable.table.PeriodResourceTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.google.common.base.Preconditions.checkNotNull;

@Service("ResourceTableService")
public class DefaultResourceTableService implements ResourceTableService {

    private final Logger log = LoggerFactory.getLogger(DefaultResourceTableService.class);
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private ResourceTableStore resourceTableStore;

    private IdentifiableObjectManager idObjectManager;

    private OrganisationUnitServiceExt organisationUnitServiceExt;

    private PeriodServiceExt periodServiceExt;

    public DefaultResourceTableService(
        ResourceTableStore resourceTableStore,
        IdentifiableObjectManager idObjectManager,
        OrganisationUnitServiceExt organisationUnitServiceExt,
        PeriodServiceExt periodServiceExt
    ) {
        checkNotNull(resourceTableStore);
        checkNotNull(idObjectManager);
        checkNotNull(organisationUnitServiceExt);
        checkNotNull(periodServiceExt);

        this.resourceTableStore = resourceTableStore;
        this.idObjectManager = idObjectManager;
        this.organisationUnitServiceExt = organisationUnitServiceExt;
        this.periodServiceExt = periodServiceExt;
    }

    // -------------------------------------------------------------------------
    // ResourceTableService implementation
    // -------------------------------------------------------------------------

    @Override
    @Transactional
    public void generateOrganisationUnitStructures() {
        resourceTableStore.generateResourceTable(
            new OrganisationUnitStructureResourceTable(
                null,
                organisationUnitServiceExt,
                organisationUnitServiceExt.getNumberOfOrganisationalLevels()
            )
        );
    }

    @Override
    @Transactional
    public void generateOrganisationUnitGroupSetTable() {
        resourceTableStore.generateResourceTable(
            new OrganisationUnitGroupSetResourceTable(
                idObjectManager.getDataDimensionsNoAcl(OrganisationUnitGroupSet.class),
                true,
                organisationUnitServiceExt.getNumberOfOrganisationalLevels()
            )
        );
    }

    @Override
    public void generateDatePeriodTable() {
        resourceTableStore.generateResourceTable(new DatePeriodResourceTable(null));
    }

    @Override
    @Transactional
    public void generatePeriodTable() {
        resourceTableStore.generateResourceTable(new PeriodResourceTable(periodServiceExt.getAllPeriods()));
    }
}
