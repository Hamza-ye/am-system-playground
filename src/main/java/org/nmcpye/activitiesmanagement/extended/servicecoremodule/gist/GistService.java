package org.nmcpye.activitiesmanagement.extended.servicecoremodule.gist;

import java.util.List;
import java.util.Map;

/**
 * The GIST API gives convenient access to (potentially large) collections. Good
 * performance is achieved by directly fetching simple values only. Collections
 * are projected to size, emptiness or arrays of ids. In addition the result
 * contains references to the API endpoints where the items of collections can
 * be browsed with more detail.
 */
public interface GistService {
    /**
     * Before running {@link #gist(GistQuery)} a {@link GistQuery} should be
     * planned. This gives the service a change to modify the query fields.
     * <p>
     * The result is exposed again so that the caller can use the modified
     * {@link GistQuery} in subsequent processing steps like serialising the
     * result based on the {@link GistQuery#getFields()}.
     *
     * @param query A {@link GistQuery} as build by the caller
     * @return the rectified {@link GistQuery} that should be used to query
     * results. This might be the same instance as the provided one or a
     * modified "copy".
     */
    GistQuery plan(GistQuery query);

    /**
     * Run a gist query. Each matching entry as a corresponding element in the
     * result list.
     *
     * @param query query to run and which has been {@link #plan(GistQuery)}ned
     * @return Either a list of simple values (one
     * {@link GistQuery.Field} query) or a list of
     * {@link Object[]} containing the values for
     * {@link GistQuery#getFields()}.
     */
    List<?> gist(GistQuery query);

    /**
     * Create a pager for the given {@link GistQuery}, its results rows and the
     * request params.
     *
     * @param query  a query build from params
     * @param rows   the result list of running the query
     * @param params The request params used to build the query
     * @return the pager suitable for the provided situation
     */
    GistPager pager(GistQuery query, List<?> rows, Map<String, String[]> params);

    /**
     * Describes the query execution without actually running the query.
     *
     * @param query a not yet {@link #plan(GistQuery)}ned query
     * @return a description of the query execution
     */
    Map<String, ?> describe(GistQuery query);
}
