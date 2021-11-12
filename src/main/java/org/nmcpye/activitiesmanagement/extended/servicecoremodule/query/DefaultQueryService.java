package org.nmcpye.activitiesmanagement.extended.servicecoremodule.query;

import org.nmcpye.activitiesmanagement.extended.common.IdentifiableObject;
import org.nmcpye.activitiesmanagement.extended.servicecoremodule.query.planner.QueryPlan;
import org.nmcpye.activitiesmanagement.extended.servicecoremodule.query.planner.QueryPlanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Default implementation of QueryService which works with IdObjects.
 */
@Component("org.nmcpye.activitiesmanagement.extended.servicecoremodule.query.QueryService")
public class DefaultQueryService
    implements QueryService {
    private final Logger log = LoggerFactory.getLogger(DefaultQueryService.class);
    private final QueryParser queryParser;

    private final QueryPlanner queryPlanner;

    private final JpaCriteriaQueryEngine<? extends IdentifiableObject> criteriaQueryEngine;

    private final InMemoryQueryEngine<? extends IdentifiableObject> inMemoryQueryEngine;

    private final Junction.Type DEFAULT_JUNCTION_TYPE = Junction.Type.AND;

    public DefaultQueryService(QueryParser queryParser, QueryPlanner queryPlanner,
                               JpaCriteriaQueryEngine<? extends IdentifiableObject> criteriaQueryEngine,
                               InMemoryQueryEngine<? extends IdentifiableObject> inMemoryQueryEngine) {
        checkNotNull(queryParser);
        checkNotNull(queryPlanner);
        checkNotNull(criteriaQueryEngine);
        checkNotNull(inMemoryQueryEngine);

        this.queryParser = queryParser;
        this.queryPlanner = queryPlanner;
        this.criteriaQueryEngine = criteriaQueryEngine;
        this.inMemoryQueryEngine = inMemoryQueryEngine;
    }

    @Override
    public List<? extends IdentifiableObject> query(Query query) {
        return queryObjects(query);
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public List<? extends IdentifiableObject> query(Query query, ResultTransformer transformer) {
        List<? extends IdentifiableObject> objects = queryObjects(query);

        if (transformer != null) {
            return transformer.transform(objects);
        }

        return objects;
    }

    @Override
    public long count(Query query) {
        Query cloned = Query.from(query);

        cloned.clearOrders();
        cloned.setFirstResult(0);
        cloned.setMaxResults(Integer.MAX_VALUE);

        return countObjects(cloned);
    }

    @Override
    public Query getQueryFromUrl(Class<?> klass, List<String> filters, List<Order> orders, Pagination pagination)
        throws QueryParserException {
        return getQueryFromUrl(klass, filters, orders, pagination, DEFAULT_JUNCTION_TYPE);
    }

    @Override
    public Query getQueryFromUrl(Class<?> klass, List<String> filters, List<Order> orders, Pagination pagination,
                                 Junction.Type rootJunction)
        throws QueryParserException {
        Query query = queryParser.parse(klass, filters, rootJunction);
        query.addOrders(orders);

        if (pagination.hasPagination()) {
            query.setFirstResult(pagination.getFirstResult());
            query.setMaxResults(pagination.getSize());
        }

        return query;
    }

    @Override
    public Query getQueryFromUrl(Class<?> klass, List<String> filters, List<Order> orders)
        throws QueryParserException {
        return getQueryFromUrl(klass, filters, orders, new Pagination(), DEFAULT_JUNCTION_TYPE);
    }

    // ---------------------------------------------------------------------------------------------
    // Helper methods
    // ---------------------------------------------------------------------------------------------

    private long countObjects(Query query) {
        List<? extends IdentifiableObject> objects;
        QueryPlan queryPlan = queryPlanner.planQuery(query);
        Query pQuery = queryPlan.getPersistedQuery();
        Query npQuery = queryPlan.getNonPersistedQuery();
        if (!npQuery.isEmpty()) {
            npQuery.setObjects(criteriaQueryEngine.query(pQuery));
            objects = inMemoryQueryEngine.query(npQuery);
            return objects.size();
        } else {
            return criteriaQueryEngine.count(pQuery);
        }
    }

    private List<? extends IdentifiableObject> queryObjects(Query query) {
        List<? extends IdentifiableObject> objects = query.getObjects();

        if (objects != null) {
            objects = inMemoryQueryEngine.query(query.setObjects(objects));
//            clearDefaults( query.getSchema().getKlass(), objects, query.getDefaults() );

            return objects;
        }

        QueryPlan queryPlan = queryPlanner.planQuery(query);

        Query pQuery = queryPlan.getPersistedQuery();
        Query npQuery = queryPlan.getNonPersistedQuery();

        objects = criteriaQueryEngine.query(pQuery);

        if (!npQuery.isEmpty()) {
            if (log.isDebugEnabled()) {
                log.debug("Doing in-memory for " + npQuery.getCriterions().size() + " criterions and "
                    + npQuery.getOrders().size() + " orders.");
            }

            npQuery.setObjects(objects);

            objects = inMemoryQueryEngine.query(npQuery);
        }

//        clearDefaults( query.getSchema().getKlass(), objects, query.getDefaults() );

        return objects;
    }

//    private void clearDefaults( Class<?> klass, List<? extends IdentifiableObject> objects, Defaults defaults )
//    {
//        if ( Defaults.INCLUDE == defaults || !Preheat.isDefaultClass( klass ) )
//        {
//            return;
//        }
//
//        objects.removeIf( object -> "default".equals( object.getName() ) );
//    }
}
