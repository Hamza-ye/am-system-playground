package org.nmcpye.activitiesmanagement.extended.administrationModule.resourcetable;

import java.util.List;

public interface ResourceTableStore {
    String ID = ResourceTableStore.class.getName();

    String TABLE_NAME_CATEGORY_OPTION_COMBO_NAME = "_categoryoptioncomboname";
    String TABLE_NAME_DATA_ELEMENT_STRUCTURE = "_dataelementstructure";
    String TABLE_NAME_PERIOD_STRUCTURE = "_periodstructure";
    String TABLE_NAME_DATE_PERIOD_STRUCTURE = "_dateperiodstructure";
    String TABLE_NAME_DATA_ELEMENT_CATEGORY_OPTION_COMBO = "_dataelementcategoryoptioncombo";
    String TABLE_NAME_DATA_APPROVAL_MIN_LEVEL = "_dataapprovalminlevel";

    /**
     * Generates the given resource table.
     *
     * @param resourceTable the resource table.
     */
    void generateResourceTable(ResourceTable<?> resourceTable);

    /**
     * Performs a batch update.
     *
     * @param columns the number of columns in the table to update.
     * @param tableName the name of the table to update.
     * @param batchArgs the arguments to use for the update statement.
     */
    void batchUpdate(int columns, String tableName, List<Object[]> batchArgs);
}
