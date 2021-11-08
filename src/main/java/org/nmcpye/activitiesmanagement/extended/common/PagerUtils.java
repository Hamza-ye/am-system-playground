package org.nmcpye.activitiesmanagement.extended.common;

import org.apache.commons.lang.BooleanUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public final class PagerUtils
{
    public static <T> List<T> pageCollection( Collection<T> col, Pager pager )
    {
        return pageCollection( col, pager.getOffset(), pager.getPageSize() );
    }

    public static <T> List<T> pageCollection( Collection<T> col, int offset, int limit )
    {
        List<T> objects = new ArrayList<>( col );

        if ( offset == 0 && objects.size() <= limit )
        {
            return objects;
        }

        if ( offset >= objects.size() )
        {
            offset = objects.isEmpty() ? objects.size() : objects.size() - 1;
        }

        if ( (offset + limit) > objects.size() )
        {
            limit = objects.size() - offset;
        }

        return objects.subList( offset, offset + limit );
    }

    public static boolean isSkipPaging( Boolean skipPaging, Boolean paging )
    {
        if ( skipPaging != null )
        {
            return BooleanUtils.toBoolean( skipPaging );
        }
        else if ( paging != null )
        {
            return !BooleanUtils.toBoolean( paging );
        }
     
        return false;
    }

    private PagerUtils()
    {
    }
}
