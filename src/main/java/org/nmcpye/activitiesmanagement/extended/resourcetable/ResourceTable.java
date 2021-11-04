package org.nmcpye.activitiesmanagement.extended.resourcetable;

import org.nmcpye.activitiesmanagement.extended.common.CodeGenerator;

import java.util.List;
import java.util.Optional;

public abstract class ResourceTable<T> {

    protected static final String TEMP_TABLE_SUFFIX = "_temp";

    protected List<T> objects;

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    protected ResourceTable() {}

    protected ResourceTable(List<T> objects) {
        this.objects = objects;
    }

    // -------------------------------------------------------------------------
    // Public methods
    // -------------------------------------------------------------------------

    /**
     * Provides the name of the resource database table.
     *
     * @return the name of the resource database table.
     */
    public String getTableName() {
        return getTableType().getTableName();
    }

    /**
     * Provides the temporary name of the resource database table.
     *
     * @return the temporary name of the resource database table.
     */
    public final String getTempTableName() {
        return getTableName() + TEMP_TABLE_SUFFIX;
    }

    public final String getDropTableStatement() {
        return "drop table if exists " + getTableName() + ";";
    }

    public final String getDropTempTableStatement() {
        //        return "drop table " + getTempTableName() + ";";
        return "drop table if exists " + getTempTableName() + ";";
    }

    public final String getRenameTempTableStatement() {
        return "alter table " + getTempTableName() + " rename to " + getTableName() + ";";
    }

    // -------------------------------------------------------------------------
    // Protected methods
    // -------------------------------------------------------------------------

    protected String getRandomSuffix() {
        return CodeGenerator.generateCode(5);
    }

    // -------------------------------------------------------------------------
    // Abstract methods
    // -------------------------------------------------------------------------

    /**
     * Returns the {@link ResourceTableType} of this resource table.
     *
     * @return the {@link ResourceTableType}.
     */
    public abstract ResourceTableType getTableType();

    /**
     * Provides a create table SQL statement for the temporary resource table.
     *
     * @return a create table statement.
     */
    public abstract String getCreateTempTableStatement();

    /**
     * Provides an insert into select from SQL statement for populating the
     * temporary resource table.
     *
     * @return an insert into select from SQL statement.
     */
    public abstract Optional<String> getPopulateTempTableStatement();

    /**
     * Provides content for the temporary resource table as a list of object arrays.
     *
     * @return content for the temporary resource table.
     */
    public abstract Optional<List<Object[]>> getPopulateTempTableContent();

    /**
     * Returns SQL create index statements for the temporary table. Note that the
     * indexes name must have a random component to avoid uniqueness conflicts.
     *
     * @return a list of SQL create index statements.
     */
    public abstract List<String> getCreateIndexStatements();
}
