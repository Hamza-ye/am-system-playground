package org.nmcpye.activitiesmanagement.extended.sqlview.hibernate;

import com.google.common.collect.ImmutableMap;
import org.nmcpye.activitiesmanagement.extended.common.Grid;
import org.nmcpye.activitiesmanagement.extended.common.hibernate.HibernateIdentifiableObjectStore;
import org.nmcpye.activitiesmanagement.extended.jdbcmodule.StatementBuilder;
import org.nmcpye.activitiesmanagement.extended.sqlview.SqlView;
import org.nmcpye.activitiesmanagement.extended.sqlview.SqlViewStore;
import org.nmcpye.activitiesmanagement.extended.sqlview.SqlViewType;
import org.nmcpye.activitiesmanagement.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.UncategorizedSQLException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

@Repository( "org.nmcpye.activitiesmanagement.extended.sqlview.SqlViewStore" )
public class HibernateSqlViewStore
    extends HibernateIdentifiableObjectStore<SqlView>
    implements SqlViewStore
{
    protected static final Logger log = LoggerFactory.getLogger(HibernateSqlViewStore.class);

    private static final Map<SqlViewType, String> TYPE_CREATE_PREFIX_MAP =
        ImmutableMap.of( SqlViewType.VIEW, "CREATE VIEW ", SqlViewType.MATERIALIZED_VIEW, "CREATE MATERIALIZED VIEW " );

    private static final Map<SqlViewType, String> TYPE_DROP_PREFIX_MAP =
        ImmutableMap.of( SqlViewType.VIEW, "DROP VIEW ", SqlViewType.MATERIALIZED_VIEW, "DROP MATERIALIZED VIEW " );

    private final StatementBuilder statementBuilder;

    private final JdbcTemplate readOnlyJdbcTemplate;

    public HibernateSqlViewStore( JdbcTemplate jdbcTemplate, UserService userService,
                                 StatementBuilder statementBuilder, JdbcTemplate readOnlyJdbcTemplate )
    {
        super( jdbcTemplate, SqlView.class, userService, false );

        checkNotNull( statementBuilder );
        checkNotNull( readOnlyJdbcTemplate );

        this.statementBuilder = statementBuilder;
        this.readOnlyJdbcTemplate = readOnlyJdbcTemplate;
    }

    // -------------------------------------------------------------------------
    // Implementing methods
    // -------------------------------------------------------------------------

    @Override
    public boolean viewTableExists( String viewTableName )
    {
        try
        {
            jdbcTemplate.queryForRowSet( "select * from " + statementBuilder.columnQuote( viewTableName ) + " limit 1" );

            return true;
        }
        catch ( BadSqlGrammarException ex )
        {
            return false; // View does not exist
        }
    }

    @Override
    public String createViewTable( SqlView sqlView )
    {
        dropViewTable( sqlView );

        final String sql = TYPE_CREATE_PREFIX_MAP.get( sqlView.getType() ) + statementBuilder.columnQuote( sqlView.getViewName() ) + " AS " + sqlView.getSqlQuery();

        log.debug( "Create view SQL: " + sql );

        try
        {
            jdbcTemplate.execute( sql );

            return null;
        }
        catch ( BadSqlGrammarException ex )
        {
            return ex.getCause().getMessage();
        }
    }

    @Override
    public void populateSqlViewGrid(Grid grid, String sql )
    {
        SqlRowSet rs = readOnlyJdbcTemplate.queryForRowSet( sql );

        int maxLimit = -1; //(Integer) systemSettingManager.getSystemSetting( SettingKey.SQL_VIEW_MAX_LIMIT );

        log.debug( "Get view SQL: " + sql + ", max limit: " + maxLimit );

        grid.addHeaders( rs );
        grid.addRows( rs, maxLimit );
    }

    @Override
    public String testSqlGrammar( String sql )
    {
        String viewName = SqlView.PREFIX_VIEWNAME + System.currentTimeMillis();

        sql = "CREATE VIEW " + viewName + " AS " + sql;

        log.debug( "Test view SQL: " + sql );

        try
        {
            jdbcTemplate.execute( sql );

            jdbcTemplate.execute( "DROP VIEW IF EXISTS " + viewName );
        }
        catch ( BadSqlGrammarException | UncategorizedSQLException ex )
        {
            return ex.getCause().getMessage();
        }

        return null;
    }

    @Override
    public void dropViewTable( SqlView sqlView )
    {
        String viewName = sqlView.getViewName();

        try
        {
            final String sql = TYPE_DROP_PREFIX_MAP.get( sqlView.getType() ) + " IF EXISTS " + statementBuilder.columnQuote( viewName );

            log.debug( "Drop view SQL: " + sql );

            jdbcTemplate.update( sql );
        }
        catch ( Exception ex )
        {
            log.warn( "Could not drop view: " + viewName, ex );
        }
    }

    @Override
    public boolean refreshMaterializedView( SqlView sqlView )
    {
        final String sql = "REFRESH MATERIALIZED VIEW " + sqlView.getViewName();

        log.debug( "Refresh materialized view: " + sql );

        try
        {
            jdbcTemplate.update( sql );

            return true;
        }
        catch ( Exception ex )
        {
            log.warn( "Could not refresh materialized view: " + sqlView.getViewName(), ex );

            return false;
        }
    }
}
