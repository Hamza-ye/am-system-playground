package org.nmcpye.activitiesmanagement.extended.system.objectmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Hamza on 22/10/2021.
 */
public interface RowMapper<T> {
    T mapRow(ResultSet var1) throws SQLException;
}
