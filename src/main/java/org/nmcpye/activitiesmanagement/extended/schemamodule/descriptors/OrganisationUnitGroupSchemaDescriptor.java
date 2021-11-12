package org.nmcpye.activitiesmanagement.extended.schemamodule.descriptors;

import org.nmcpye.activitiesmanagement.domain.organisationunit.OrganisationUnitGroup;
import org.nmcpye.activitiesmanagement.extended.schemamodule.Schema;
import org.nmcpye.activitiesmanagement.extended.schemamodule.SchemaDescriptor;

public class OrganisationUnitGroupSchemaDescriptor implements SchemaDescriptor {
    public static final String SINGULAR = "organisationUnitGroup";

    public static final String PLURAL = "organisationUnitGroups";

    public static final String API_ENDPOINT = "/" + PLURAL;

    @Override
    public Schema getSchema() {
        Schema schema = new Schema(OrganisationUnitGroup.class, SINGULAR, PLURAL);
        schema.setRelativeApiEndpoint(API_ENDPOINT);
        schema.setOrder(1120);

//        schema.add( new Authority( AuthorityType.CREATE_PUBLIC, Lists.newArrayList( "F_ORGUNITGROUP_PUBLIC_ADD" ) ) );
//        schema.add( new Authority( AuthorityType.CREATE_PRIVATE, Lists.newArrayList( "F_ORGUNITGROUP_PRIVATE_ADD" ) ) );
//        schema.add( new Authority( AuthorityType.DELETE, Lists.newArrayList( "F_ORGUNITGROUP_DELETE" ) ) );

        return schema;
    }
}
