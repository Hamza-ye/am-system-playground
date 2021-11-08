package org.nmcpye.activitiesmanagement.extended.jdbcmodule.dialect;

import org.springframework.beans.factory.FactoryBean;

import java.util.HashMap;
import java.util.Map;

public class StatementDialectFactoryBean
    implements FactoryBean<StatementDialect>
{
    private static final String KEY_DIALECT = "hibernate.dialect";
    
    private static Map<String, StatementDialect> dialectMap;
    
    static
    {
        dialectMap = new HashMap<>();
        dialectMap.put( "org.hibernate.dialect.MySQLDialect", StatementDialect.MYSQL );
        dialectMap.put( "org.hibernate.dialect.PostgreSQLDialect", StatementDialect.POSTGRESQL );
        dialectMap.put( "org.nmcpye.activitiesmanagement.extended.hibernate.dialect.AMPostgresDialect", StatementDialect.POSTGRESQL );
        dialectMap.put( "org.hibernate.dialect.HSQLDialect", StatementDialect.HSQL );
        dialectMap.put( "org.hibernate.dialect.H2Dialect", StatementDialect.H2 );
        dialectMap.put( "org.nmcpye.activitiesmanagement.extended.hibernate.dialect.AMH2Dialect",StatementDialect.H2 );
    }

    private StatementDialect statementDialect;
    
    // -------------------------------------------------------------------------
    // Initialisation
    // -------------------------------------------------------------------------
    
    public void init()
    {
        String dialect = "org.hibernate.dialect.PostgreSQLDialect";

        statementDialect = dialectMap.get( dialect );
        
        if ( statementDialect == null )
        {
            throw new RuntimeException( "Unsupported dialect: " + dialect );
        }
    }

    // -------------------------------------------------------------------------
    // FactoryBean implementation
    // -------------------------------------------------------------------------
        
    @Override
    public StatementDialect getObject() {
        return statementDialect;
    }

    @Override
    public Class<StatementDialect> getObjectType()
    {
        return StatementDialect.class;
    }

    @Override
    public boolean isSingleton()
    {
        return true;
    }
}
