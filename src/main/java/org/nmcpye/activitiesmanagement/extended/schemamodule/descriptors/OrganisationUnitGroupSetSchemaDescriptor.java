package org.nmcpye.activitiesmanagement.extended.schemamodule.descriptors;

import org.nmcpye.activitiesmanagement.domain.organisationunit.OrganisationUnitGroupSet;
import org.nmcpye.activitiesmanagement.extended.schemamodule.Schema;
import org.nmcpye.activitiesmanagement.extended.schemamodule.SchemaDescriptor;

public class OrganisationUnitGroupSetSchemaDescriptor implements SchemaDescriptor {
    public static final String SINGULAR = "organisationUnitGroupSet";

    public static final String PLURAL = "organisationUnitGroupSets";

    public static final String API_ENDPOINT = "/" + PLURAL;

    @Override
    public Schema getSchema() {
        Schema schema = new Schema(OrganisationUnitGroupSet.class, SINGULAR, PLURAL);
        schema.setRelativeApiEndpoint(API_ENDPOINT);
        schema.setOrder(1130);

//        schema
//            .add( new Authority( AuthorityType.CREATE_PUBLIC, Lists.newArrayList( "F_ORGUNITGROUPSET_PUBLIC_ADD" ) ) );
//        schema.add(
//            new Authority( AuthorityType.CREATE_PRIVATE, Lists.newArrayList( "F_ORGUNITGROUPSET_PRIVATE_ADD" ) ) );
//        schema.add( new Authority( AuthorityType.DELETE, Lists.newArrayList( "F_ORGUNITGROUPSET_DELETE" ) ) );

        return schema;
    }
}
