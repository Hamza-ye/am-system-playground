package org.nmcpye.activitiesmanagement.extended.schemamodule.descriptors;

import org.nmcpye.activitiesmanagement.domain.sqlview.SqlView;
import org.nmcpye.activitiesmanagement.extended.schemamodule.Schema;
import org.nmcpye.activitiesmanagement.extended.schemamodule.SchemaDescriptor;

public class SqlViewSchemaDescriptor implements SchemaDescriptor {
    public static final String SINGULAR = "sqlView";

    public static final String PLURAL = "sqlViews";

    public static final String API_ENDPOINT = "/" + PLURAL;

    @Override
    public Schema getSchema() {
        Schema schema = new Schema(SqlView.class, SINGULAR, PLURAL);
        schema.setRelativeApiEndpoint(API_ENDPOINT);
        schema.setOrder(1010);
        schema.setDataShareable(true);
        schema.setDataWriteShareable(false);

//        schema.add( new Authority( AuthorityType.CREATE_PUBLIC, Lists.newArrayList( "F_SQLVIEW_PUBLIC_ADD" ) ) );
//        schema.add( new Authority( AuthorityType.CREATE_PRIVATE, Lists.newArrayList( "F_SQLVIEW_PRIVATE_ADD" ) ) );
//        schema.add( new Authority( AuthorityType.DELETE, Lists.newArrayList( "F_SQLVIEW_DELETE" ) ) );
//        schema.add( new Authority( AuthorityType.EXTERNALIZE, Lists.newArrayList( "F_SQLVIEW_EXTERNAL" ) ) );

        return schema;
    }
}
