package org.nmcpye.activitiesmanagement.extended.schemamodule.descriptors;

import org.nmcpye.activitiesmanagement.domain.person.PeopleGroup;
import org.nmcpye.activitiesmanagement.extended.schemamodule.Schema;
import org.nmcpye.activitiesmanagement.extended.schemamodule.SchemaDescriptor;

public class PeopleGroupSchemaDescriptor implements SchemaDescriptor {
    public static final String SINGULAR = "people-group";

    public static final String PLURAL = "people-groups";

    public static final String API_ENDPOINT = "/" + PLURAL;

    @Override
    public Schema getSchema() {
        Schema schema = new Schema(PeopleGroup.class, SINGULAR, PLURAL);
        schema.setRelativeApiEndpoint(API_ENDPOINT);
        schema.setOrder(1100);

//        schema.add( new Authority( AuthorityType.CREATE, Lists.newArrayList( "F_ORGANISATIONUNIT_ADD" ) ) );
//        schema.add( new Authority( AuthorityType.DELETE, Lists.newArrayList( "F_ORGANISATIONUNIT_DELETE" ) ) );

        return schema;
    }
}
