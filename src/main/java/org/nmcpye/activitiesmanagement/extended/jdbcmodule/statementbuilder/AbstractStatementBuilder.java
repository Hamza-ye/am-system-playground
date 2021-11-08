package org.nmcpye.activitiesmanagement.extended.jdbcmodule.statementbuilder;

import org.apache.commons.lang3.StringUtils;
import org.nmcpye.activitiesmanagement.extended.jdbcmodule.StatementBuilder;

import java.util.*;

public abstract class AbstractStatementBuilder
    implements StatementBuilder
{
    static final String AZaz = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    static final String AZaz09 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    static final String AZaz_QUOTED = QUOTE + AZaz + QUOTE;
    static final String AZaz09_QUOTED = QUOTE + AZaz09 + QUOTE;

    @Override
    public String encode( String value )
    {
        return encode( value, true );
    }

    @Override
    public String encode( String value, boolean quote )
    {
        if ( value != null )
        {
            value = value.endsWith( "\\" ) ? value.substring( 0, value.length() - 1 ) : value;
            value = value.replaceAll( QUOTE, QUOTE + QUOTE );
        }

        return quote ? (QUOTE + value + QUOTE) : value;
    }

    @Override
    public String columnQuote( String column )
    {
        String qte = getColumnQuote();
        
        column = column.replaceAll( qte, ( qte + qte ) );
        
        return qte + column + qte;
    }

    @Override
    public String limitRecord( int offset, int limit )
    {
        return " LIMIT " + limit + " OFFSET " + offset;
    }

    @Override
    public String getAutoIncrementValue()
    {
        return "null";
    }

    @Override
    public String getLongVarBinaryType()
    {
        return "VARBINARY(1000000)";
    }

    @Override
    public String concatenate( String... s )
    {
        return "CONCAT(" + StringUtils.join( s, ", " ) + ")";
    }

    @Override
    public String position( String substring, String string )
    {
        return ("POSITION(" + substring + " in " + string + ")");
    }

    @Override
    public String getUid()
    {
        return concatenate(
            getCharAt( AZaz_QUOTED , "1 + " + getRandom( AZaz.length() ) ),
            getCharAt( AZaz09_QUOTED, "1 + " + getRandom( AZaz09.length() ) ),
            getCharAt( AZaz09_QUOTED, "1 + " + getRandom( AZaz09.length() ) ),
            getCharAt( AZaz09_QUOTED, "1 + " + getRandom( AZaz09.length() ) ),
            getCharAt( AZaz09_QUOTED, "1 + " + getRandom( AZaz09.length() ) ),
            getCharAt( AZaz09_QUOTED, "1 + " + getRandom( AZaz09.length() ) ),
            getCharAt( AZaz09_QUOTED, "1 + " + getRandom( AZaz09.length() ) ),
            getCharAt( AZaz09_QUOTED, "1 + " + getRandom( AZaz09.length() ) ),
            getCharAt( AZaz09_QUOTED, "1 + " + getRandom( AZaz09.length() ) ),
            getCharAt( AZaz09_QUOTED, "1 + " + getRandom( AZaz09.length() ) ),
            getCharAt( AZaz09_QUOTED, "1 + " + getRandom( AZaz09.length() ) ) );
    }

    @Override
    public String getNumberOfColumnsInPrimaryKey( String table )
    {
        return
            "select count(cu.column_name) from information_schema.key_column_usage cu " +
                "inner join information_schema.table_constraints tc  " +
                "on cu.constraint_catalog=tc.constraint_catalog " +
                "and cu.constraint_schema=tc.constraint_schema " +
                "and cu.constraint_name=tc.constraint_name " +
                "and cu.table_schema=tc.table_schema " +
                "and cu.table_name=tc.table_name " +
                "where tc.constraint_type='PRIMARY KEY' " +
                "and cu.table_name='" + table + "';";
    }
    
    @Override
    public String getCastToDate( String column )
    {
        return "cast(" + column + " as date)";
    }
    
    @Override
    public String getDaysBetweenDates( String fromColumn, String toColumn )
    {
        return "datediff(" + toColumn + ", " + fromColumn + ")";
    }

    @Override
    public String getDropPrimaryKey( String table )
    {
        return "alter table " + table + " drop primary key;";
    }

    @Override
    public String getAddPrimaryKeyToExistingTable( String table, String column )
    {
        return "alter table " + table + " add column " + column + " integer auto_increment primary key not null;";
    }

    @Override
    public String getDropNotNullConstraint( String table, String column, String type )
    {
        return "alter table " + table + " modify column " + column + " " + type + " null;";
    }

    /**
     * Generates a derived table containing one column of literal strings.
     *
     * The generic implementation, which works in all supported database
     * types, returns a subquery in the following form:
     * <code>
     *     (select 's1' as column
     *      union select 's2'
     *      union select 's3') table
     * </code>
     *
     * @param values (non-empty) String values for the derived table
     * @param table the desired table name alias
     * @param column the desired column name
     * @return the derived literal table
     */
    @Override
    public String literalStringTable( Collection<String> values, String table, String column )
    {
        StringBuilder sb = new StringBuilder();

        String before = "(select '";
        String after = "' as " + column;

        for ( String value : values )
        {
            sb.append( before ).append( value ).append( after );

            before = " union select '";
            after = "'";
        }

        return sb.append(") ").append( table ).toString();
    }

    /**
     * Generates a derived table containing literals in two columns: integer
     * and string.
     *
     * The generic implementation, which works in all supported database
     * types, returns a subquery in the following form:
     * <code>
     *     (select i1 as intColumn, 's1' as stringColumn
     *      union select i2, 's2'
     *      union select i3, 's3') table
     * </code>
     *
     * @param longValues (non-empty) Integer values for the derived table
     * @param strValues (same size) String values for the derived table
     * @param table the desired table name alias
     * @param longColumn the desired integer column name
     * @param strColumn the desired string column name
     * @return the derived literal table
     */
    @Override
    public String literalLongStringTable( List<Long> longValues,
        List<String> strValues, String table, String longColumn, String strColumn )
    {
        StringBuilder sb = new StringBuilder();

        String before = "(select ";
        String afterInt = " as " + longColumn + ", '";
        String afterStr = "' as " + strColumn;

        for ( int i = 0; i < longValues.size(); i++ )
        {
            sb.append( before ).append( longValues.get( i ) ).append( afterInt )
                .append( strValues.get( i ) ).append( afterStr );
            before = " union select ";
            afterInt = ", '";
            afterStr = "'";
        }

        return sb.append( ") " ).append( table ).toString();
    }

    /**
     * Generates a derived table containing literals in two columns: integer
     * and integer.
     *
     * @param long1Values (non-empty) 1st integer column values for the table
     * @param long2Values (same size) 2nd integer column values for the table
     * @param table the desired table name alias
     * @param long1Column the desired 1st integer column name
     * @param long2Column the desired 2nd integer column name
     * @return the derived literal table
     *
     * The generic implementation, which works in all supported database
     * types, returns a subquery in the following form:
     * <code>
     *     (select i1_1 as int1Column, i2_1 as int2Column
     *      union select i1_2, i2_2
     *      union select i1_3, i2_3) table
     * </code>
     */
    @Override
    public String literalLongLongTable( List<Long> long1Values,
        List<Long> long2Values, String table, String long1Column, String long2Column )
    {
        StringBuilder sb = new StringBuilder();

        String before = "(select ";
        String afterInt1 = " as " + long1Column + ", ";
        String afterInt2 = " as " + long2Column;

        for ( int i = 0; i < long1Values.size(); i++ )
        {
            sb.append( before ).append( long1Values.get( i ) ).append( afterInt1 )
                .append( long2Values.get( i ) ).append( afterInt2 );
            before = " union select ";
            afterInt1 = ", ";
            afterInt2 = "";
        }

        return sb.append( ") " ).append( table ).toString();
    }

    @Override
    public boolean supportsPartialIndexes()
    {
        return false;
    }
}
