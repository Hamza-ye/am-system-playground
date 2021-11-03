package org.nmcpye.activitiesmanagement.extended.resourcetable;

import static com.google.common.base.Preconditions.checkNotNull;

import org.nmcpye.activitiesmanagement.domain.organisationunit.OrganisationUnitGroupSet;
import org.nmcpye.activitiesmanagement.extended.common.IdentifiableObjectManager;
import org.nmcpye.activitiesmanagement.extended.organisationunit.OrganisationUnitService;
import org.nmcpye.activitiesmanagement.extended.period.PeriodService;
import org.nmcpye.activitiesmanagement.extended.resourcetable.table.DatePeriodResourceTable;
import org.nmcpye.activitiesmanagement.extended.resourcetable.table.OrganisationUnitGroupSetResourceTable;
import org.nmcpye.activitiesmanagement.extended.resourcetable.table.OrganisationUnitStructureResourceTable;
import org.nmcpye.activitiesmanagement.extended.resourcetable.table.PeriodResourceTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("org.nmcpye.activitiesmanagement.extended.resourcetable.ResourceTableService")
public class DefaultResourceTableService implements ResourceTableService {

    private final Logger log = LoggerFactory.getLogger(DefaultResourceTableService.class);
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private ResourceTableStore resourceTableStore;

    private IdentifiableObjectManager idObjectManager;

    private OrganisationUnitService organisationUnitService;

    private PeriodService periodService;

    public DefaultResourceTableService(
        ResourceTableStore resourceTableStore,
        IdentifiableObjectManager idObjectManager,
        OrganisationUnitService organisationUnitService,
        PeriodService periodService
    ) {
        checkNotNull(resourceTableStore);
        checkNotNull(idObjectManager);
        checkNotNull(organisationUnitService);
        checkNotNull(periodService);

        this.resourceTableStore = resourceTableStore;
        this.idObjectManager = idObjectManager;
        this.organisationUnitService = organisationUnitService;
        this.periodService = periodService;
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
                organisationUnitService,
                organisationUnitService.getNumberOfOrganisationalLevels()
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
                organisationUnitService.getNumberOfOrganisationalLevels()
            )
        );
        resourceTableStore.generateResourceTable(
            new OrganisationUnitGroupSetResourceTable(
                idObjectManager.getDataDimensionsNoAcl(OrganisationUnitGroupSet.class),
                true,
                organisationUnitService.getNumberOfOrganisationalLevels()
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
        resourceTableStore.generateResourceTable(new PeriodResourceTable(periodService.getAllPeriods()));
    }
}
