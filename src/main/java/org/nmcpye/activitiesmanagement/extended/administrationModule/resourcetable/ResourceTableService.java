package org.nmcpye.activitiesmanagement.extended.administrationModule.resourcetable;

public interface ResourceTableService {
    String ID = ResourceTableService.class.getName();

    /**
     * Generates a resource table containing the hierarchy graph for each
     * OrganisationUnit.
     */
    void generateOrganisationUnitStructures();

    //    /**
    //     * Generates a resource table containing data sets and organisation units
    //     * with their associated attribute option combinations.
    //     */
    //    void generateDataSetOrganisationUnitCategoryTable();

    //    /**
    //     * Generates a resource table containing id and a derived name for
    //     * all CategoryOptionCombos.
    //     */
    //    void generateCategoryOptionComboNames();
    //
    //    /**
    //     * Generates a resource table for all data elements.
    //     */
    //    void generateDataElementGroupSetTable();
    //
    //    /**
    //     * Generates a resource table for all indicators.
    //     */
    //    void generateIndicatorGroupSetTable();

    /**
     * Generates a resource table for all organisation units
     */
    void generateOrganisationUnitGroupSetTable();

    //    /**
    //     * Generates a resource table for all category option combos.
    //     *
    //     * Depends on the category option combo names table.
    //     */
    //    void generateCategoryTable();

    //    /**
    //     * Generates a resource table for all data elements.
    //     */
    //    void generateDataElementTable();

    /**
     * Generates a resource table for dates and associated periods.
     */
    void generateDatePeriodTable();

    /**
     * Generates a resource table for all periods.
     */
    void generatePeriodTable();
    //    /**
    //     * Generates a resource table for all data elements and relevant category
    //     * option combinations.
    //     */
    //    void generateCategoryOptionComboTable();
    //
    //    /**
    //     * Generates a resource table for remapping any skipped approval levels.
    //     */
    //    void generateDataApprovalRemapLevelTable();

    //    /**
    //     * Generates a resource table for data approval aggregated to minimum level.
    //     */
    //    void generateDataApprovalMinLevelTable();

    //    /**
    //     * Create all SQL views.
    //     */
    //    void createAllSqlViews();
    //
    //    /**
    //     * Drop all SQL views.
    //     */
    //    void dropAllSqlViews();
}
