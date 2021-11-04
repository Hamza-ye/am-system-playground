package org.nmcpye.activitiesmanagement.extended.hibernate.dialect;

import org.hibernate.dialect.PostgreSQL10Dialect;
import org.hibernate.type.descriptor.sql.BinaryTypeDescriptor;
import org.hibernate.type.descriptor.sql.SqlTypeDescriptor;

import javax.inject.Inject;
import java.sql.Types;

/**
 * <p>AMPostgresDialect class.</p>
 */
public class AMPostgresDialect extends PostgreSQL10Dialect {

    /**
     * <p>Constructor for AMPostgresDialect.</p>
     */
    @Inject
    public AMPostgresDialect() {
        super();
        registerColumnType(Types.JAVA_OBJECT, "jsonb");
        //        registerColumnType( Types.JAVA_OBJECT, "json" );
        registerColumnType(Types.BLOB, "bytea");
        //        registerHibernateType( Types.OTHER, "pg-uuid" );
        //        registerFunction( "jsonb_extract_path", new StandardSQLFunction( "jsonb_extract_path", StandardBasicTypes.STRING ) );
        //        registerFunction( "jsonb_extract_path_text", new StandardSQLFunction( "jsonb_extract_path_text", StandardBasicTypes.STRING ) );
    }

    /** {@inheritDoc} */
    @Override
    public SqlTypeDescriptor remapSqlTypeDescriptor(SqlTypeDescriptor sqlTypeDescriptor) {
        if (sqlTypeDescriptor.getSqlType() == java.sql.Types.BLOB) {
            return BinaryTypeDescriptor.INSTANCE;
        }
        return super.remapSqlTypeDescriptor(sqlTypeDescriptor);
    }
}
