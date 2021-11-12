package org.nmcpye.activitiesmanagement.extended.webapi.controller.event.webrequest;

import java.util.Optional;

/**
 * Paging parameters
 */
public interface PagingCriteria {
    Integer DEFAULT_PAGE = 1;

    Integer DEFAULT_PAGE_SIZE = 50;

    /**
     * Page number to return.
     */
    Integer getPage();

    /**
     * Page size.
     */
    Integer getPageSize();

    /**
     * Indicates whether to include the total number of pages in the paging
     * response.
     */
    boolean isTotalPages();

    /**
     * Indicates whether paging should be skipped.
     */
    Boolean getSkipPaging();

    default Integer getFirstResult() {
        Integer page = Optional.ofNullable(getPage())
            .filter(p -> p > 0)
            .orElse(DEFAULT_PAGE);

        Integer pageSize = Optional.ofNullable(getPageSize())
            .filter(ps -> ps > 0)
            .orElse(DEFAULT_PAGE_SIZE);

        return (page - 1) * pageSize;
    }

}
