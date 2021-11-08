package org.nmcpye.activitiesmanagement.extended.jdbcmodule.config;

import org.nmcpye.activitiesmanagement.extended.jdbcmodule.dialect.StatementDialectFactoryBean;
import org.nmcpye.activitiesmanagement.extended.jdbcmodule.statementbuilder.StatementBuilderFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JdbcConfig
{

    @Bean( initMethod = "init" )
    public StatementDialectFactoryBean statementDialect()
    {
        return new StatementDialectFactoryBean();
    }

    @Bean( initMethod = "init" )
    public StatementBuilderFactoryBean statementBuilder()
    {
        return new StatementBuilderFactoryBean( statementDialect().getObject() );
    }
}
