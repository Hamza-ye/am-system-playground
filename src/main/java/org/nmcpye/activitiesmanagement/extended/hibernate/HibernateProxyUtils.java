package org.nmcpye.activitiesmanagement.extended.hibernate;

import org.hibernate.Hibernate;
import org.hibernate.proxy.HibernateProxyHelper;

import java.util.Objects;

public class HibernateProxyUtils
{
    private HibernateProxyUtils()
    {
        throw new IllegalStateException( "Utility class" );
    }

    @SuppressWarnings( "rawtypes" )
    public static Class getRealClass( Object object )
    {
        Objects.requireNonNull( object );

        if ( object instanceof Class )
        {
            throw new IllegalArgumentException( "Input can't be a Class instance!" );
        }

        return HibernateProxyHelper.getClassWithoutInitializingProxy( object );
    }

    @SuppressWarnings( { "unchecked" } )
    public static <T> T unproxy( T proxy )
    {
        return (T) Hibernate.unproxy( proxy );
    }
}
