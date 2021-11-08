package org.nmcpye.activitiesmanagement.extended.jdbcmodule.dialect;

/**
 * Created by Hamza on 07/11/2021.
 */
public enum StatementDialect {
    MYSQL,
    POSTGRESQL,
    H2,
    HSQL,
    DERBY,
    MSSQL;

    private StatementDialect() {
    }
}

