package org.nmcpye.activitiesmanagement.extended.hibernate.dialect;

import org.hibernate.dialect.H2Dialect;

import java.sql.Types;

/**
 * <p>AMH2Dialect class.</p>
 */
public class AMH2Dialect extends H2Dialect {

    /**
     * <p>Constructor for AMH2Dialect.</p>
     */
    public AMH2Dialect() {
        super();
        registerColumnType(Types.FLOAT, "real");
        registerColumnType(Types.JAVA_OBJECT, "text");
        registerColumnType(Types.JAVA_OBJECT, "jsonb");
        registerColumnType(Types.OTHER, "uuid");
    }

    @Override
    public String getDropSequenceString(String sequenceName) {
        // Adding the "if exists" clause to avoid warnings
        return "drop sequence if exists " + sequenceName;
    }

    @Override
    public boolean dropConstraints() {
        // No need to drop constraints before dropping tables, leads to error
        // messages
        return false;
    }
}
