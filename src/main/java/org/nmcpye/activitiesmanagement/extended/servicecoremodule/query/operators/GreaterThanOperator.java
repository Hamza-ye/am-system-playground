package org.nmcpye.activitiesmanagement.extended.servicecoremodule.query.operators;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.nmcpye.activitiesmanagement.extended.schema.Property;
import org.nmcpye.activitiesmanagement.extended.servicecoremodule.query.QueryException;
import org.nmcpye.activitiesmanagement.extended.servicecoremodule.query.QueryUtils;
import org.nmcpye.activitiesmanagement.extended.servicecoremodule.query.Type;
import org.nmcpye.activitiesmanagement.extended.servicecoremodule.query.Typed;
import org.nmcpye.activitiesmanagement.extended.servicecoremodule.query.planner.QueryPath;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Collection;
import java.util.Date;

public class GreaterThanOperator<T extends Comparable<? super T>> extends Operator<T>
{
    public GreaterThanOperator( T arg )
    {
        super( "gt", Typed.from( String.class, Boolean.class, Number.class, Date.class ), arg );
    }

    @Override
    public Criterion getHibernateCriterion(QueryPath queryPath )
    {
        Property property = queryPath.getProperty();

        if ( property.isCollection() )
        {
            Integer value = QueryUtils.parseValue( Integer.class, args.get( 0 ) );

            if ( value == null )
            {
                throw new QueryException(
                    "Left-side is collection, and right-side is not a valid integer, so can't compare by size." );
            }

            return Restrictions.sizeGt( queryPath.getPath(), value );
        }

        return Restrictions.gt( queryPath.getPath(), args.get( 0 ) );
    }

    @Override
    public <Y> Predicate getPredicate( CriteriaBuilder builder, Root<Y> root, QueryPath queryPath )
    {
        Property property = queryPath.getProperty();

        if ( property.isCollection() )
        {
            Integer value = QueryUtils.parseValue( Integer.class, args.get( 0 ) );

            if ( value == null )
            {
                throw new QueryException(
                    "Left-side is collection, and right-side is not a valid integer, so can't compare by size." );
            }

            return builder.greaterThan( builder.size( root.get( queryPath.getPath() ) ), value );
        }

        return builder.greaterThan( root.get( queryPath.getPath() ), args.get( 0 ) );
    }

    @Override
    public boolean test( Object value )
    {
        if ( args.isEmpty() || value == null )
        {
            return false;
        }

        Type type = new Type( value );

        if ( type.isString() )
        {
            String s1 = getValue( String.class );
            String s2 = (String) value;

            return s1 != null && s2.compareTo( s1 ) > 0;
        }
        else if ( type.isInteger() )
        {
            Integer s1 = getValue( Integer.class );
            Integer s2 = (Integer) value;

            return s1 != null && s2 > s1;
        }
        else if ( type.isFloat() )
        {
            Float s1 = getValue( Float.class );
            Float s2 = (Float) value;

            return s1 != null && s2 > s1;
        }
        else if ( type.isDate() )
        {
            Date s1 = getValue( Date.class );
            Date s2 = (Date) value;

            return s1 != null && s2.after( s1 );
        }
        else if ( type.isCollection() )
        {
            Collection<?> collection = (Collection<?>) value;
            Integer size = getValue( Integer.class );

            return size != null && collection.size() > size;
        }

        return false;
    }
}