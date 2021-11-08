package org.nmcpye.activitiesmanagement.extended.systemmodule.system.objectmapper;

import org.nmcpye.activitiesmanagement.extended.organisationunit.OrganisationUnitRelationship;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Lars Helge Overland
 */
public class OrganisationUnitRelationshipRowMapper
    implements RowMapper<OrganisationUnitRelationship>, org.springframework.jdbc.core.RowMapper<OrganisationUnitRelationship> {

    @Override
    public OrganisationUnitRelationship mapRow(ResultSet resultSet) throws SQLException {
        return new OrganisationUnitRelationship(resultSet.getLong("parentid"), resultSet.getLong("organisationunitid"));
    }

    @Override
    public OrganisationUnitRelationship mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        return mapRow(resultSet);
    }
}
