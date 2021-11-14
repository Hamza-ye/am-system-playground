package org.nmcpye.activitiesmanagement.extended.administrationModule.sqlview;

import com.google.common.collect.Sets;
import org.apache.commons.lang.StringUtils;
import org.nmcpye.activitiesmanagement.domain.User;
import org.nmcpye.activitiesmanagement.extended.common.Grid;
import org.nmcpye.activitiesmanagement.extended.common.IllegalQueryException;
import org.nmcpye.activitiesmanagement.extended.common.util.SqlHelper;
import org.nmcpye.activitiesmanagement.extended.feedback.ErrorCode;
import org.nmcpye.activitiesmanagement.extended.feedback.ErrorMessage;
import org.nmcpye.activitiesmanagement.extended.jdbcmodule.StatementBuilder;
import org.nmcpye.activitiesmanagement.extended.servicecoremodule.query.QueryParserException;
import org.nmcpye.activitiesmanagement.extended.servicecoremodule.query.QueryUtils;
import org.nmcpye.activitiesmanagement.extended.sqlview.SqlView;
import org.nmcpye.activitiesmanagement.extended.sqlview.SqlViewService;
import org.nmcpye.activitiesmanagement.extended.sqlview.SqlViewStore;
import org.nmcpye.activitiesmanagement.extended.systemmodule.system.grid.ListGrid;
import org.nmcpye.activitiesmanagement.extended.util.ObjectUtils;
import org.nmcpye.activitiesmanagement.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.nmcpye.activitiesmanagement.extended.sqlview.SqlView.*;

@Transactional
@Service( "org.nmcpye.activitiesmanagement.extended.sqlview.SqlViewService" )
public class DefaultSqlViewService
    implements SqlViewService
{
    private final Logger log = LoggerFactory.getLogger(DefaultSqlViewService.class);
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private final SqlViewStore sqlViewStore;

    private final StatementBuilder statementBuilder;

    private final UserService userService;

    public DefaultSqlViewService(SqlViewStore sqlViewStore, StatementBuilder statementBuilder, UserService userService)
    {
        checkNotNull( sqlViewStore );
        checkNotNull( statementBuilder );

        this.sqlViewStore = sqlViewStore;
        this.statementBuilder = statementBuilder;
        this.userService = userService;

    }

    // -------------------------------------------------------------------------
    // CRUD methods
    // -------------------------------------------------------------------------

    @Override
    public long saveSqlView( SqlView sqlView )
    {
        sqlViewStore.saveObject( sqlView );

        return sqlView.getId();
    }

    @Override
    public void updateSqlView( SqlView sqlView )
    {
        sqlViewStore.update( sqlView );
    }

    @Override
    public void deleteSqlView( SqlView sqlView )
    {
        if ( !sqlView.isQuery() )
        {
            dropViewTable( sqlView );
        }

        sqlViewStore.delete( sqlView );
    }

    @Override
    public List<SqlView> getAllSqlViews()
    {
        return sqlViewStore.getAll();
    }

    @Override
    public List<SqlView> getAllSqlViewsNoAcl()
    {
        return sqlViewStore.getAllNoAcl();
    }

    @Override
    public SqlView getSqlView( long viewId )
    {
        return sqlViewStore.get( viewId );
    }

    @Override
    public SqlView getSqlViewByUid( String uid )
    {
        return sqlViewStore.getByUid( uid );
    }

    @Override
    public SqlView getSqlView( String viewName )
    {
        return sqlViewStore.getByName( viewName );
    }

    @Override
    public int getSqlViewCount()
    {
        return sqlViewStore.getCount();
    }

    // -------------------------------------------------------------------------
    // Service methods
    // -------------------------------------------------------------------------

    @Override
    public boolean viewTableExists( String viewTableName )
    {
        return sqlViewStore.viewTableExists( viewTableName );
    }

    @Override
    public String createViewTable( SqlView sqlView )
    {
        validateSqlView( sqlView, null, null );

        return sqlViewStore.createViewTable( sqlView );
    }

    @Override
    public Grid getSqlViewGrid( SqlView sqlView, Map<String, String> criteria, Map<String, String> variables, List<String> filters, List<String> fields  )
    {
        validateSqlView( sqlView, criteria, variables );

        Grid grid = new ListGrid();
        grid.setTitle( sqlView.getName() );
        grid.setSubtitle( sqlView.getDescription() );

        log.info( String.format( "Retriving data for SQL view: '%s'", sqlView.getUid() ) );

        String sql = sqlView.isQuery() ?
            getSqlForQuery( sqlView, criteria, variables, filters, fields ) :
            getSqlForView( sqlView, criteria, filters, fields );

        sqlViewStore.populateSqlViewGrid( grid, sql );

        return grid;
    }

    private String parseFilters(List<String> filters, SqlHelper sqlHelper ) throws QueryParserException
    {
        String query = StringUtils.EMPTY;

        for ( String filter : filters )
        {
            String[] split = filter.split( ":" );

            if ( split.length == 3 )
            {
                int index = split[0].length() + ":".length() + split[1].length() + ":".length();
                query +=  getFilterQuery(sqlHelper, split[0], split[1], filter.substring( index ) ) ;
            }
            else
            {
                throw new QueryParserException( "Invalid filter => " + filter );
            }
        }

        return query;
    }

    private String getFilterQuery( SqlHelper sqlHelper, String columnName, String operator, String value ) {

        String query = StringUtils.EMPTY;

        query += sqlHelper.whereAnd() + " " + columnName + " " + QueryUtils.parseFilterOperator( operator, value );

        return query;
    }

    private String getSqlForQuery(SqlView sqlView, Map<String, String> criteria, Map<String, String> variables, List<String> filters, List<String> fields )
    {
        boolean hasCriteria = criteria != null && !criteria.isEmpty();

        boolean hasFilter = filters != null && !filters.isEmpty();

        String sql = substituteQueryVariables( sqlView, variables );

        if ( hasCriteria || hasFilter )
        {
            sql = SqlViewUtils.removeQuerySeparator( sql );

            String outerSql = "select " + QueryUtils.parseSelectFields( fields ) + " from " + "(" + sql + ") as qry ";

            SqlHelper sqlHelper = new SqlHelper();

            if ( hasCriteria )
            {
                outerSql += getCriteriaSqlClause( criteria, sqlHelper );
            }

            if ( hasFilter )
            {
                outerSql += parseFilters( filters, sqlHelper );
            }

            sql = outerSql;
        }

        return sql;
    }

    private String substituteQueryVariables( SqlView sqlView, Map<String, String> variables )
    {
        String sql = SqlViewUtils.substituteSqlVariables( sqlView.getSqlQuery(), variables );

        User currentUser = userService.getUserWithAuthorities().orElse(null);

        if ( currentUser != null )
        {
            sql = SqlViewUtils.substituteSqlVariable( sql, CURRENT_USER_ID_VARIABLE, Long.toString( currentUser.getId() ) );
            sql = SqlViewUtils.substituteSqlVariable( sql, CURRENT_USERNAME_VARIABLE, currentUser.getLogin() );
        }

        return sql;
    }

    private String getSqlForView( SqlView sqlView, Map<String, String> criteria, List<String> filters, List<String> fields )
    {
        String sql = "select " + QueryUtils.parseSelectFields( fields ) + " from " + statementBuilder.columnQuote( sqlView.getViewName() ) + " ";

        boolean hasCriteria = criteria != null && !criteria.isEmpty();

        boolean hasFilter = filters != null && !filters.isEmpty();

        if ( hasCriteria || hasFilter )
        {
            SqlHelper sqlHelper = new SqlHelper();

            if ( hasCriteria )
            {
                sql += getCriteriaSqlClause( criteria, sqlHelper );
            }

            if ( hasFilter )
            {
                sql += parseFilters( filters, sqlHelper );
            }
        }

        return sql;
    }

    private String getCriteriaSqlClause( Map<String, String> criteria,  SqlHelper sqlHelper )
    {
        String sql = StringUtils.EMPTY;

        if ( criteria != null && !criteria.isEmpty() )
        {
            sqlHelper = ObjectUtils.firstNonNull( sqlHelper, new SqlHelper() );

            for ( String filter : criteria.keySet() )
            {
                sql += sqlHelper.whereAnd() + " " + statementBuilder.columnQuote( filter ) + "='" + criteria.get( filter ) + "' ";
            }
        }

        return sql;
    }

    @Override
    public void validateSqlView( SqlView sqlView, Map<String, String> criteria, Map<String, String> variables )
        throws IllegalQueryException
    {
        ErrorMessage error = null;

        if ( sqlView == null || sqlView.getSqlQuery() == null )
        {
            throw new IllegalQueryException( ErrorCode.E4300 );
        }

        final Set<String> sqlVars = SqlViewUtils.getVariables( sqlView.getSqlQuery() );
        final String sql = sqlView.getSqlQuery().replaceAll("\\r|\\n"," ").toLowerCase();
        final Set<String> allowedVariables = variables == null ? STANDARD_VARIABLES : Sets.union( variables.keySet(), STANDARD_VARIABLES );

        if ( !SELECT_PATTERN.matcher( sql ).matches() )
        {
            error = new ErrorMessage( ErrorCode.E4301 );
        }

        if ( sql.contains( ";" ) && !sql.trim().endsWith( ";" ) )
        {
            error = new ErrorMessage( ErrorCode.E4302 );
        }

        if ( variables != null && variables.keySet().contains( null ) )
        {
            error = new ErrorMessage( ErrorCode.E4303 );
        }

        if ( variables != null && variables.values().contains( null ) )
        {
            error = new ErrorMessage( ErrorCode.E4304 );
        }

        if ( variables != null && !SqlView.getInvalidQueryParams( variables.keySet() ).isEmpty() )
        {
            error = new ErrorMessage( ErrorCode.E4305, SqlView.getInvalidQueryParams( variables.keySet() ) );
        }

        if ( variables != null && !SqlView.getInvalidQueryValues( variables.values() ).isEmpty() )
        {
            error = new ErrorMessage( ErrorCode.E4306, SqlView.getInvalidQueryValues( variables.values() ) );
        }

        if ( sqlView.isQuery() && !sqlVars.isEmpty() && ( !allowedVariables.containsAll( sqlVars ) ) )
        {
            error = new ErrorMessage( ErrorCode.E4307, sqlVars );
        }

        if ( criteria != null && !SqlView.getInvalidQueryParams( criteria.keySet() ).isEmpty() )
        {
            error = new ErrorMessage( ErrorCode.E4308, SqlView.getInvalidQueryParams( criteria.keySet() ) );
        }

        if ( criteria != null && !SqlView.getInvalidQueryValues( criteria.values() ).isEmpty() )
        {
            error = new ErrorMessage( ErrorCode.E4309, SqlView.getInvalidQueryValues( criteria.values() ) );
        }

        if ( sql.matches( SqlView.getIllegalKeywordsRegex() ) )
        {
            error = new ErrorMessage( ErrorCode.E4311 );
        }

        if ( error != null )
        {
            log.warn( String.format( "Validation failed for SQL view '%s' with code: '%s' and message: '%s'",
                sqlView.getUid(), error.getErrorCode(), error.getMessage() ) );

            throw new IllegalQueryException( error );
        }
    }

    @Override
    public String testSqlGrammar( String sql )
    {
        return sqlViewStore.testSqlGrammar( sql );
    }

    @Override
    public void dropViewTable( SqlView sqlView )
    {
        sqlViewStore.dropViewTable( sqlView );
    }

    @Override
    public boolean refreshMaterializedView( SqlView sqlView )
    {
        if ( sqlView == null || !sqlView.isMaterializedView() )
        {
            return false;
        }

        return sqlViewStore.refreshMaterializedView( sqlView );
    }
}
