package org.nmcpye.activitiesmanagement.extended.sqlview;

import org.nmcpye.activitiesmanagement.extended.common.Grid;
import org.nmcpye.activitiesmanagement.extended.common.IdentifiableObjectStore;

public interface SqlViewStore
    extends IdentifiableObjectStore<SqlView>
{
    String ID = SqlViewStore.class.getName();

    boolean viewTableExists(String viewTableName);

    String createViewTable(SqlView sqlView);

    void dropViewTable(SqlView sqlView);

    void populateSqlViewGrid(Grid grid, String sql);

    /**
     * Tests the given SQL for validity.
     *
     * @param sql the SQL string.
     * @return a non-null description if invalid, and null if valid.
     */
    String testSqlGrammar(String sql);

    boolean refreshMaterializedView(SqlView sqlView);
}
