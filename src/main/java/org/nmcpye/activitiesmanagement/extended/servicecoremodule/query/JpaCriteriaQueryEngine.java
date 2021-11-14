package org.nmcpye.activitiesmanagement.extended.servicecoremodule.query;

import org.hibernate.Session;
import org.nmcpye.activitiesmanagement.extended.common.IdentifiableObject;
import org.nmcpye.activitiesmanagement.extended.hibernatemodule.cache.QueryCacheManager;
import org.nmcpye.activitiesmanagement.extended.schemamodule.Schema;
import org.nmcpye.activitiesmanagement.extended.servicecoremodule.query.planner.QueryPlan;
import org.nmcpye.activitiesmanagement.extended.servicecoremodule.query.planner.QueryPlanner;
import org.nmcpye.activitiesmanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;

@Component
public class JpaCriteriaQueryEngine<T extends IdentifiableObject>
    implements QueryEngine<T> {
    private final UserService userService;

    private final QueryPlanner queryPlanner;

    @PersistenceContext
    EntityManager entityManager;

//    private final List<InternalHibernateGenericStore<T>> hibernateGenericStores;

    private final QueryCacheManager queryCacheManager;

//    private Map<Class<?>, InternalHibernateGenericStore<T>> stores = new HashMap<>();

    @Autowired
    public JpaCriteriaQueryEngine(UserService userService, QueryPlanner queryPlanner,
                                  QueryCacheManager queryCacheManager) {
        checkNotNull(userService);
        checkNotNull(queryPlanner);

        this.userService = userService;
        this.queryPlanner = queryPlanner;
        this.queryCacheManager = queryCacheManager;
    }

    private Session getSession() {
        return entityManager.unwrap(Session.class);
    }

    @Override
    public List<T> query(Query query) {
        Schema schema = query.getSchema();

        Class<T> klass = (Class<T>) schema.getKlass();

//        InternalHibernateGenericStore<T> store = (InternalHibernateGenericStore<T>) getStore(klass);

//        if (store == null) {
//            return new ArrayList<>();
//        }

        if (query.getUser() == null) {
            query.setUser(userService.getUserWithAuthorities().orElse(null));
        }

        if (!query.isPlannedQuery()) {
            QueryPlan queryPlan = queryPlanner.planQuery(query, true);
            query = queryPlan.getPersistedQuery();
        }

        CriteriaBuilder builder = getSession().getCriteriaBuilder();

        CriteriaQuery<T> criteriaQuery = builder.createQuery(klass);
        Root<T> root = criteriaQuery.from(klass);

        if (query.isEmpty()) {
            Predicate predicate = builder.conjunction();

            //TODO append Sharing data
//            predicate.getExpressions().addAll(store
//                .getSharingPredicates(builder, query.getUser()).stream().map(t -> t.apply(root))
//                .collect(Collectors.toList()));

            criteriaQuery.where(predicate);

            TypedQuery<T> typedQuery = getSession().createQuery(criteriaQuery);

            typedQuery.setFirstResult(query.getFirstResult());
            typedQuery.setMaxResults(query.getMaxResults());

            return typedQuery.getResultList();
        }

        Predicate predicate = buildPredicates(builder, root, query);

        //TODO append Sharing data
//        predicate.getExpressions().addAll(store
//            .getSharingPredicates(builder, query.getUser()).stream().map(t -> t.apply(root))
//            .collect(Collectors.toList()));

        criteriaQuery.where(predicate);

        if (!query.getOrders().isEmpty()) {
            criteriaQuery.orderBy(query.getOrders().stream()
                .map(o -> o.isAscending() ? builder.asc(root.get(o.getProperty().getFieldName()))
                    : builder.desc(root.get(o.getProperty().getFieldName())))
                .collect(Collectors.toList()));
        }

        TypedQuery<T> typedQuery = getSession().createQuery(criteriaQuery);

        typedQuery.setFirstResult(query.getFirstResult());
        typedQuery.setMaxResults(query.getMaxResults());

        if (query.isCacheable()) {
            typedQuery.setHint("org.hibernate.cacheable", true);
            typedQuery.setHint("org.hibernate.cacheRegion", getQueryCacheRegionName(klass, typedQuery));
        }

        return typedQuery.getResultList();
    }

    @Override
    public long count(Query query) {
        Schema schema = query.getSchema();

        Class<T> klass = (Class<T>) schema.getKlass();

//        InternalHibernateGenericStore<T> store = (InternalHibernateGenericStore<T>) getStore(klass);
//
//        if (store == null) {
//            return 0;
//        }

        if (query.getUser() == null) {
            query.setUser(userService.getUserWithAuthorities().orElse(null));
        }

        if (!query.isPlannedQuery()) {
            QueryPlan queryPlan = queryPlanner.planQuery(query, true);
            query = queryPlan.getPersistedQuery();
        }

        CriteriaBuilder builder = getSession().getCriteriaBuilder();

        CriteriaQuery<Long> criteriaQuery = builder.createQuery(Long.class);
        Root<T> root = criteriaQuery.from(klass);

        criteriaQuery.select(builder.count(root));

        Predicate predicate = buildPredicates(builder, root, query);

//        predicate.getExpressions().addAll(store
//            .getSharingPredicates(builder, query.getUser()).stream().map(t -> t.apply(root))
//            .collect(Collectors.toList()));

        criteriaQuery.where(predicate);

        if (!query.getOrders().isEmpty()) {
            criteriaQuery.orderBy(query.getOrders().stream()
                .map(o -> o.isAscending() ? builder.asc(root.get(o.getProperty().getName()))
                    : builder.desc(root.get(o.getProperty().getName())))
                .collect(Collectors.toList()));
        }

        TypedQuery<Long> typedQuery = getSession().createQuery(criteriaQuery);

        return typedQuery.getSingleResult();
    }

//    private void initStoreMap() {
//        if (!stores.isEmpty()) {
//            return;
//        }
//
//        for (InternalHibernateGenericStore<T> store : hibernateGenericStores) {
//            stores.put(store.getClazz(), store);
//        }
//    }

//    private InternalHibernateGenericStore<?> getStore(Class<? extends IdentifiableObject> klass) {
//        initStoreMap();
//        return stores.get(klass);
//    }

    private <Y> Predicate buildPredicates(CriteriaBuilder builder, Root<Y> root, Query query) {
        Predicate junction = getJpaJunction(builder, query.getRootJunctionType());

        for (org.nmcpye.activitiesmanagement.extended.servicecoremodule.query.Criterion criterion : query.getCriterions()) {
            addPredicate(builder, root, junction, criterion);
        }

        query.getAliases().forEach(alias -> root.get(alias).alias(alias));

        return junction;
    }

    private Predicate getJpaJunction(CriteriaBuilder builder, Junction.Type type) {
        switch (type) {
            case AND:
                return builder.conjunction();
            case OR:
                return builder.disjunction();
        }

        return builder.conjunction();
    }

    private <Y> Predicate getPredicate(CriteriaBuilder builder, Root<Y> root, Restriction restriction) {
        if (restriction == null || restriction.getOperator() == null) {
            return null;
        }

        return restriction.getOperator().getPredicate(builder, root, restriction.getQueryPath());
    }

    private <Y> void addPredicate(CriteriaBuilder builder, Root<Y> root, Predicate predicateJunction,
                                  org.nmcpye.activitiesmanagement.extended.servicecoremodule.query.Criterion criterion) {
        if (Restriction.class.isInstance(criterion)) {
            Restriction restriction = (Restriction) criterion;
            Predicate predicate = getPredicate(builder, root, restriction);

            if (predicate != null) {
                predicateJunction.getExpressions().add(predicate);
            }
        } else if (Junction.class.isInstance(criterion)) {
            Predicate junction = null;

            if (Disjunction.class.isInstance(criterion)) {
                junction = builder.disjunction();
            } else if (Conjunction.class.isInstance(criterion)) {
                junction = builder.conjunction();
            }

            predicateJunction.getExpressions().add(junction);

            for (org.nmcpye.activitiesmanagement.extended.servicecoremodule.query.Criterion c : ((Junction) criterion).getCriterions()) {
                addJunction(builder, root, junction, c);
            }
        }
    }

    private <Y> void addJunction(CriteriaBuilder builder, Root<Y> root, Predicate junction,
                                 org.nmcpye.activitiesmanagement.extended.servicecoremodule.query.Criterion criterion) {
        if (Restriction.class.isInstance(criterion)) {
            Restriction restriction = (Restriction) criterion;
            Predicate predicate = getPredicate(builder, root, restriction);

            if (predicate != null) {
                junction.getExpressions().add(predicate);
            }
        } else if (Junction.class.isInstance(criterion)) {
            Predicate j = null;

            if (Disjunction.class.isInstance(criterion)) {
                j = builder.disjunction();
            } else if (Conjunction.class.isInstance(criterion)) {
                j = builder.conjunction();
            }

            junction.getExpressions().add(j);

            for (org.nmcpye.activitiesmanagement.extended.servicecoremodule.query.Criterion c : ((Junction) criterion).getCriterions()) {
                addJunction(builder, root, junction, c);
            }
        }
    }

    private String getQueryCacheRegionName(Class<T> klass, TypedQuery<T> typedQuery) {
        String queryString = typedQuery.unwrap(org.hibernate.query.Query.class).getQueryString();
        return queryCacheManager.generateRegionName(klass, queryString);
    }
}
