package org.nmcpye.activitiesmanagement.extended.sqlview;

import com.google.common.base.MoreObjects;
import org.apache.commons.lang.BooleanUtils;
import org.nmcpye.activitiesmanagement.extended.common.Pager;
import org.nmcpye.activitiesmanagement.extended.common.PagerUtils;

import java.util.Set;

public class SqlViewQuery
{
    public static final SqlViewQuery EMPTY = new SqlViewQuery();

    private Set<String> criteria;

    private Set<String> var;

    private Boolean skipPaging;

    private Boolean paging;

    private int page = 1;

    private int pageSize = Pager.DEFAULT_PAGE_SIZE;

    private int total;


    public Set<String> getCriteria()
    {
        return criteria;
    }

    public void setCriteria( Set<String> criteria )
    {
        this.criteria = criteria;
    }

    public Set<String> getVar()
    {
        return var;
    }

    public void setVar( Set<String> var )
    {
        this.var = var;
    }

    public boolean isSkipPaging()
    {
        return PagerUtils.isSkipPaging( skipPaging, paging );
    }

    public void setSkipPaging( Boolean skipPaging )
    {
        this.skipPaging = skipPaging;
    }

    public boolean isPaging()
    {
        return BooleanUtils.toBoolean( paging );
    }

    public void setPaging( Boolean paging )
    {
        this.paging = paging;
    }

    public int getPage()
    {
        return page;
    }

    public void setPage( int page )
    {
        this.page = page;
    }

    public int getPageSize()
    {
        return pageSize;
    }

    public void setPageSize( int pageSize )
    {
        this.pageSize = pageSize;
    }

    public int getTotal()
    {
        return total;
    }

    public void setTotal( int total )
    {
        this.total = total;
    }

    public Pager getPager()
    {
        return PagerUtils.isSkipPaging( skipPaging, paging ) ? null : new Pager( page, total, pageSize );
    }

    @Override
    public String toString()
    {
        return MoreObjects.toStringHelper( this )
            .add( "page", page )
            .add( "pageSize", pageSize )
            .add( "total", total )
            .toString();
    }
}
