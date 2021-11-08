package org.nmcpye.activitiesmanagement.extended.resourcetable;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.nmcpye.activitiesmanagement.AMTest;
import org.nmcpye.activitiesmanagement.IntegrationTest;
import org.nmcpye.activitiesmanagement.domain.organisationunit.OrganisationUnit;
import org.nmcpye.activitiesmanagement.domain.period.MonthlyPeriodType;
import org.nmcpye.activitiesmanagement.domain.period.PeriodType;
import org.nmcpye.activitiesmanagement.extended.administrationModule.resourcetable.ResourceTableService;
import org.nmcpye.activitiesmanagement.extended.common.IdentifiableObjectManager;
import org.nmcpye.activitiesmanagement.service.DataSetService;
import org.springframework.beans.factory.annotation.Autowired;

@IntegrationTest
public class ResourceTableServiceTest
    extends AMTest
{
    @Autowired
    private IdentifiableObjectManager idObjectManager;

    @Autowired
    private ResourceTableService resourceTableService;

    @Autowired
    private DataSetService dataSetService;

    @Override
    public void setUpTest()
    {
        PeriodType pt = new MonthlyPeriodType();

        OrganisationUnit ouA = createOrganisationUnit( 'A' );
        OrganisationUnit ouB = createOrganisationUnit( 'B' );
        OrganisationUnit ouC = createOrganisationUnit( 'C' );

        ouB.setParent( ouA );
        ouC.setParent( ouA );
        ouA.getChildren().add( ouB );
        ouA.getChildren().add( ouC );

        idObjectManager.save( ouA );
        idObjectManager.save( ouB );
        idObjectManager.save( ouC );
    }

    @Disabled("Disabled until Creating H2 StatementBuilder!")
    @Test
    public void testGenerateAllResourceTables()
    {
        resourceTableService.generateOrganisationUnitStructures();
        resourceTableService.generateOrganisationUnitGroupSetTable();
        resourceTableService.generatePeriodTable();
        resourceTableService.generateDatePeriodTable();
    }
}
