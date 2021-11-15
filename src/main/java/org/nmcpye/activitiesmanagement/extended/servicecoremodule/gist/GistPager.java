package org.nmcpye.activitiesmanagement.extended.servicecoremodule.gist;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.nmcpye.activitiesmanagement.extended.schemamodule.Schema;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Map;
import java.util.function.Function;

/**
 * Pager POJO for paging gist lists.
 */
public final class GistPager {
    @JsonProperty
    private final int page;

    @JsonProperty
    private final int pageSize;

    @JsonProperty
    private final Integer total;

    @JsonProperty
    private final String prevPage;

    @JsonProperty
    private final String nextPage;

    public GistPager(int page, int pageSize, Integer total, String prevPage, String nextPage) {
        this.page = page;
        this.pageSize = pageSize;
        this.total = total;
        this.prevPage = prevPage;
        this.nextPage = nextPage;
    }

    public static URI computeBaseURL(GistQuery query, Map<String, String[]> params,
                                     Function<Class<?>, Schema> schemaByType) {
        UriBuilder url = UriComponentsBuilder.fromUriString(query.getEndpointRoot());
        GistQuery.Owner owner = query.getOwner();
        if (owner != null) {
            Schema o = schemaByType.apply(owner.getType());
            url.pathSegment(o.getRelativeApiEndpoint().substring(1), owner.getId(),
                o.getProperty(owner.getCollectionProperty()).key(), "gist");
        } else {
            Schema s = schemaByType.apply(query.getElementType());
            url.pathSegment(s.getRelativeApiEndpoint().substring(1), "gist");
        }
        params.forEach(url::queryParam);
        return url.build();
    }

    @JsonProperty
    public Integer getPageCount() {
        return total == null ? null : (int) Math.ceil(total / (double) pageSize);
    }

    public int getPage() {
        return page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public Integer getTotal() {
        return total;
    }

    public String getPrevPage() {
        return prevPage;
    }

    public String getNextPage() {
        return nextPage;
    }

    public String toString() {
        return "[Page: " + page + " size: " + pageSize + "]";
    }
}
