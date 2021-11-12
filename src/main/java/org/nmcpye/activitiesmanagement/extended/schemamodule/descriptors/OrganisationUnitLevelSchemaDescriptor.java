package org.nmcpye.activitiesmanagement.extended.schemamodule.descriptors;

import org.nmcpye.activitiesmanagement.domain.organisationunit.OrganisationUnitLevel;
import org.nmcpye.activitiesmanagement.extended.schemamodule.Schema;
import org.nmcpye.activitiesmanagement.extended.schemamodule.SchemaDescriptor;

public class OrganisationUnitLevelSchemaDescriptor implements SchemaDescriptor {
    public static final String SINGULAR = "organisationUnitLevel";

    public static final String PLURAL = "organisationUnitLevels";

    public static final String API_ENDPOINT = "/" + PLURAL;

    @Override
    public Schema getSchema() {
        Schema schema = new Schema(OrganisationUnitLevel.class, SINGULAR, PLURAL);
        schema.setRelativeApiEndpoint(API_ENDPOINT);
        schema.setOrder(1110);

//        schema.add( new Authority( AuthorityType.CREATE, Lists.newArrayList( "F_ORGANISATIONUNITLEVEL_UPDATE" ) ) );
//        schema.add( new Authority( AuthorityType.UPDATE, Lists.newArrayList( "F_ORGANISATIONUNITLEVEL_UPDATE" ) ) );
//        schema.add( new Authority( AuthorityType.DELETE, Lists.newArrayList( "F_ORGANISATIONUNITLEVEL_UPDATE" ) ) );

        return schema;
    }
}
