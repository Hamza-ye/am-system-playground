package org.nmcpye.activitiesmanagement.extended.web.utils;

import org.nmcpye.activitiesmanagement.extended.servicecoremodule.query.Pagination;
import org.nmcpye.activitiesmanagement.extended.web.webdomain.WebOptions;

public class PaginationUtils {
    public final static Pagination NO_PAGINATION = new Pagination();

    /**
     * Calculates the paging first result based on pagination data from
     * {@see WebOptions} if the WebOptions have pagination information
     * <p>
     * The first result is simply calculated by multiplying page -1 * page size
     *
     * @param options a {@see WebOptions} object
     * @return a {@see PaginationData} object either empty or containing
     * pagination data
     */
    public static Pagination getPaginationData(WebOptions options) {
        if (options.hasPaging()) {
            // ignore if page < 0
            int page = Math.max(options.getPage(), 1);
            return new Pagination((page - 1) * options.getPageSize(), options.getPageSize());
        }

        return NO_PAGINATION;
    }
}
