package org.nmcpye.activitiesmanagement.extended.resourcetable.table;

import static org.nmcpye.activitiesmanagement.extended.system.util.SqlUtils.quote;

import java.util.ArrayList;
import java.util.List;
import org.nmcpye.activitiesmanagement.extended.common.BaseNameableObject;

public class UniqueNameVerifier {

    protected List<String> columnNames = new ArrayList<>();

    /**
     * Returns the short name in quotes for the given {@see BaseDimensionalObject}, ensuring
     * that the short name is unique across the list of BaseDimensionalObject this
     * class operates on
     *
     * @param baseDimensionalObject a {@see BaseDimensionalObject}
     * @return a unique, quoted short name
     */
    protected String ensureUniqueShortName(BaseNameableObject baseDimensionalObject) {
        String columnName = quote(
            baseDimensionalObject.getShortName() + (columnNames.contains(baseDimensionalObject.getShortName()) ? columnNames.size() : "")
        );

        this.columnNames.add(baseDimensionalObject.getShortName());

        return columnName;
    }

    /**
     * Returns the name in quotes, ensuring
     * that the name is unique across the list of objects this class operates on
     *
     * @param name a String
     * @return a unique, quoted name
     */
    protected String ensureUniqueName(String name) {
        String columnName = quote(name + (columnNames.contains(name) ? columnNames.size() : ""));

        this.columnNames.add(name);

        return columnName;
    }
}
