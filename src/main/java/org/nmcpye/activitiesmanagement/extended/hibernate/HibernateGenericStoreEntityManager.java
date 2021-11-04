package org.nmcpye.activitiesmanagement.extended.hibernate;

import org.hibernate.annotations.QueryHints;
import org.nmcpye.activitiesmanagement.extended.common.AuditLogUtil;
import org.nmcpye.activitiesmanagement.extended.common.GenericStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.persistence.*;
import javax.persistence.criteria.*;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class HibernateGenericStoreEntityManager<T> implements GenericStore<T> {

    private final Logger log = LoggerFactory.getLogger(HibernateGenericStoreEntityManager.class);
    public static final String FUNCTION_JSONB_EXTRACT_PATH = "jsonb_extract_path";
    public static final String FUNCTION_JSONB_EXTRACT_PATH_TEXT = "jsonb_extract_path_text";
    protected static final int OBJECT_FETCH_SIZE = 2000;

    @PersistenceContext
    EntityManager entityManager;

    protected JdbcTemplate jdbcTemplate;

    protected Class<T> clazz;

    protected boolean cacheable;

    public HibernateGenericStoreEntityManager(JdbcTemplate jdbcTemplate, Class<T> clazz, boolean cacheable) {
        //        checkNotNull( jdbcTemplate );
        //        checkNotNull( clazz );

        this.jdbcTemplate = jdbcTemplate;
        this.clazz = clazz;
        this.cacheable = cacheable;
    }

    /**
     * Could be overridden programmatically.
     */
    @Override
    public Class<T> getClazz() {
        return clazz;
    }

    /**
     * Could be overridden programmatically.
     */
    protected boolean isCacheable() {
        return cacheable;
    }

    /**
     * Could be injected through container.
     */
    public void setCacheable(boolean cacheable) {
        this.cacheable = cacheable;
    }

    // -------------------------------------------------------------------------
    // Convenience methods
    // -------------------------------------------------------------------------

    //    /**
    //     * Returns the current session.
    //     *
    //     * @return the current session.
    //     */
    //    protected final Session getSession() {
    //        return entityManager.unwrap(Session.class);
    //    }

    /**
     * Creates a Query for given HQL query string. Return type is casted
     * to generic type T of the Store class.
     *
     * @param hql the HQL query.
     * @return a Query instance with return type is the object type T of the store class
     */
    @SuppressWarnings("unchecked")
    protected final TypedQuery<T> getQuery(String hql) {
        // 1 extended
        return entityManager.createQuery(hql, getClazz()).setHint(QueryHints.CACHEABLE, cacheable);
        //        return getSession().createQuery(hql).setCacheable(cacheable).setHint(QueryHints.CACHEABLE, cacheable);
    }

    /**
     * Creates a Query for given HQL query string. Must specify the return
     * type of the Query variable.
     *
     * @param hql the HQL query.
     * @return a Query instance with return type specified in the Query<Y>
     */
    @SuppressWarnings("unchecked")
    protected final <V> Query getTypedQuery(String hql) {
        // 1 extended
        return entityManager.createQuery(hql).setHint(QueryHints.CACHEABLE, cacheable);
        //        return getSession().createQuery(hql).setCacheable(cacheable).setHint(QueryHints.CACHEABLE, cacheable);
    }

    //    /**
    //     * Creates a Criteria for the implementation Class type.
    //     * <p>
    //     * Please note that sharing is not considered.
    //     *
    //     * @return a Criteria instance.
    //     */
    //    @Deprecated
    //    public final Criteria getCriteria() {
    //        DetachedCriteria criteria = DetachedCriteria.forClass(getClazz());
    //
    //        preProcessDetachedCriteria(criteria);
    //
    //        return getExecutableCriteria(criteria);
    //    }

    //    /**
    //     * Override to add additional restrictions to criteria before
    //     * it is invoked.
    //     */
    //    protected void preProcessDetachedCriteria(DetachedCriteria detachedCriteria) {}
    //
    //    public final Criteria getExecutableCriteria(DetachedCriteria detachedCriteria) {
    //        return detachedCriteria.getExecutableCriteria(getSession()).setCacheable(cacheable);
    //    }

    public CriteriaBuilder getCriteriaBuilder() {
        // 1 extended
        return entityManager.getCriteriaBuilder();
        //        return getSession().getCriteriaBuilder();
    }

    //------------------------------------------------------------------------------------------
    // JPA Methods
    //------------------------------------------------------------------------------------------

    /**
     * Get executable Typed Query from Criteria Query.
     * Apply cache if needed.
     *
     * @return executable TypedQuery
     */
    private TypedQuery<T> getExecutableTypedQuery(CriteriaQuery<T> criteriaQuery) {
        // 1 extended
        return entityManager.createQuery(criteriaQuery).setHint(QueryHints.CACHEABLE, cacheable);
        //        return getSession().createQuery(criteriaQuery).setCacheable(cacheable).setHint(QueryHints.CACHEABLE, cacheable);
    }

    /**
     * Method for adding additional Predicates into where clause
     *
     */
    protected void preProcessPredicates(CriteriaBuilder builder, List<Function<Root<T>, Predicate>> predicates) {}

    /**
     * Get single result from executable typedQuery
     *
     * @param typedQuery TypedQuery
     * @return single object
     */
    protected <V> V getSingleResult(TypedQuery<V> typedQuery) {
        List<V> list = typedQuery.getResultList();

        if (list != null && list.size() > 1) {
            throw new NonUniqueResultException("More than one entity found for query");
        }

        return list != null && !list.isEmpty() ? list.get(0) : null;
    }

    /**
     * Get List objects returned by executable TypedQuery
     *
     * @return list result
     */
    protected final List<T> getList(TypedQuery<T> typedQuery) {
        return typedQuery.getResultList();
    }

    /**
     * Get List objects return by querying given JpaQueryParameters
     *
     * @param parameters JpaQueryParameters
     * @return list objects
     */
    protected final List<T> getList(CriteriaBuilder builder, JpaQueryParameters<T> parameters) {
        return getTypedQuery(builder, parameters).getResultList();
    }

    /**
     * Get executable TypedQuery from JpaQueryParameter.
     *
     * @return executable TypedQuery
     */
    protected final TypedQuery<T> getTypedQuery(CriteriaBuilder builder, JpaQueryParameters<T> parameters) {
        List<Function<Root<T>, Predicate>> predicateProviders = parameters.getPredicates();
        List<Function<Root<T>, Order>> orderProviders = parameters.getOrders();
        preProcessPredicates(builder, predicateProviders);

        CriteriaQuery<T> query = builder.createQuery(getClazz());
        Root<T> root = query.from(getClazz());
        query.select(root);

        if (!predicateProviders.isEmpty()) {
            List<Predicate> predicates = predicateProviders.stream().map(t -> t.apply(root)).collect(Collectors.toList());
            query.where(predicates.toArray(new Predicate[0]));
        }

        if (!orderProviders.isEmpty()) {
            List<Order> orders = orderProviders.stream().map(o -> o.apply(root)).collect(Collectors.toList());
            query.orderBy(orders);
        }

        TypedQuery<T> typedQuery = getExecutableTypedQuery(query);

        if (parameters.hasFirstResult()) {
            typedQuery.setFirstResult(parameters.getFirstResult());
        }

        if (parameters.hasMaxResult()) {
            typedQuery.setMaxResults(parameters.getMaxResults());
        }

        return typedQuery.setHint(QueryHints.CACHEABLE, parameters.isCacheable(cacheable));
    }

    /**
     * Count number of objects based on given parameters
     *
     * @param parameters JpaQueryParameters
     * @return number of objects
     */
    protected final Long getCount(CriteriaBuilder builder, JpaQueryParameters<T> parameters) {
        CriteriaQuery<Long> query = builder.createQuery(Long.class);

        Root<T> root = query.from(getClazz());

        List<Function<Root<T>, Predicate>> predicateProviders = parameters.getPredicates();

        List<Function<Root<T>, Expression<Long>>> countExpressions = parameters.getCountExpressions();

        if (!countExpressions.isEmpty()) {
            if (countExpressions.size() > 1) {
                query.multiselect(countExpressions.stream().map(c -> c.apply(root)).collect(Collectors.toList()));
            } else {
                query.select(countExpressions.get(0).apply(root));
            }
        } else {
            query.select(parameters.isUseDistinct() ? builder.countDistinct(root) : builder.count(root));
        }

        if (!predicateProviders.isEmpty()) {
            List<Predicate> predicates = predicateProviders.stream().map(t -> t.apply(root)).collect(Collectors.toList());
            query.where(predicates.toArray(new Predicate[0]));
        }

        // 1 extended
        return entityManager.createQuery(query).setHint(QueryHints.CACHEABLE, parameters.isCacheable(cacheable)).getSingleResult();
        //        return getSession().createQuery(query).setHint(QueryHints.CACHEABLE, parameters.isCacheable(cacheable)).getSingleResult();
    }

    /**
     * Retrieves an object based on the given Jpa Predicates.
     *
     * @return an object of the implementation Class type.
     */
    protected T getSingleResult(CriteriaBuilder builder, JpaQueryParameters<T> parameters) {
        return getSingleResult(getTypedQuery(builder, parameters));
    }

    //------------------------------------------------------------------------------------------
    // End JPA Methods
    //------------------------------------------------------------------------------------------

    /**
     * Creates a SqlQuery.
     *
     * @param sql the SQL query String.
     * @return a NativeQuery<T> instance.
     */
    @SuppressWarnings("unchecked")
    protected final Query getSqlQuery(String sql) {
        // 1 extended
        return entityManager.createNativeQuery(sql).setHint(QueryHints.CACHEABLE, cacheable);
        //        return getSession().createNativeQuery(sql).setCacheable(cacheable).setHint(QueryHints.CACHEABLE, cacheable);
    }

    /**
     * Creates a untyped SqlQuery.
     *
     * @param sql the SQL query String.
     * @return a NativeQuery<T> instance.
     */
    protected final Query getUntypedSqlQuery(String sql) {
        // 1 extended
        return entityManager.createNativeQuery(sql).setHint(QueryHints.CACHEABLE, cacheable);
        //        return getSession().createNativeQuery(sql).setCacheable(cacheable).setHint(QueryHints.CACHEABLE, cacheable);
    }

    // -------------------------------------------------------------------------
    // GenericIdentifiableObjectStore implementation
    // -------------------------------------------------------------------------

    @Override
    public void save(T object) {
        AuditLogUtil.infoWrapper(log, object, AuditLogUtil.ACTION_CREATE);

        // 1 extended
        entityManager.persist(object);
        //        getSession().save(object);
    }

    @Override
    public void update(T object) {
        // 1 extended
        entityManager.merge(object);
        //        getSession().update(object);
    }

    @Override
    public void delete(T object) {
        // 1 extended
        entityManager.remove(object);
        //        getSession().delete(object);
    }

    @Override
    public T get(Long id) {
        // 1 extended
        T object = entityManager.find(getClazz(), id);

        //        T object = getSession().get(getClazz(), id);

        return postProcessObject(object);
    }

    /**
     * Override for further processing of a retrieved object.
     *
     * @param object the object.
     * @return the processed object.
     */
    protected T postProcessObject(T object) {
        return object;
    }

    @Override
    public List<T> getAll() {
        return getList(getCriteriaBuilder(), new JpaQueryParameters<>());
    }

    @Override
    public int getCount() {
        CriteriaBuilder builder = getCriteriaBuilder();

        return getCount(builder, newJpaParameters().count(root -> builder.countDistinct(root.get("id")))).intValue();
    }

    /**
     * Create new instance of JpaQueryParameters
     *
     * @return JpaQueryParameters<T>
     */
    protected JpaQueryParameters<T> newJpaParameters() {
        return new JpaQueryParameters<>();
    }
}
