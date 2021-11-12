package org.nmcpye.activitiesmanagement.extended.common.hibernate;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.nmcpye.activitiesmanagement.domain.User;
import org.nmcpye.activitiesmanagement.extended.common.AuditLogUtil;
import org.nmcpye.activitiesmanagement.extended.common.BaseIdentifiableObject;
import org.nmcpye.activitiesmanagement.extended.common.GenericDimensionalObjectStore;
import org.nmcpye.activitiesmanagement.extended.common.IdentifiableObject;
import org.nmcpye.activitiesmanagement.extended.hibernatemodule.hibernate.HibernateGenericStoreEntityManager;
import org.nmcpye.activitiesmanagement.extended.hibernatemodule.hibernate.JpaQueryParameters;
import org.nmcpye.activitiesmanagement.extended.hibernatemodule.hibernate.exception.DeleteAccessDeniedException;
import org.nmcpye.activitiesmanagement.extended.hibernatemodule.hibernate.exception.ReadAccessDeniedException;
import org.nmcpye.activitiesmanagement.extended.hibernatemodule.hibernate.exception.UpdateAccessDeniedException;
import org.nmcpye.activitiesmanagement.extended.servicecoremodule.query.JpaQueryUtils;
import org.nmcpye.activitiesmanagement.security.AuthoritiesConstants;
import org.nmcpye.activitiesmanagement.security.SecurityUtils;
import org.nmcpye.activitiesmanagement.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class HibernateIdentifiableObjectStoreEM<T extends BaseIdentifiableObject>
    extends HibernateGenericStoreEntityManager<T>
    implements GenericDimensionalObjectStore<T> {

    private final Logger log = LoggerFactory.getLogger(HibernateIdentifiableObjectStoreEM.class);

    @PersistenceContext
    EntityManager entityManager;

    protected UserService userService;

    protected boolean transientIdentifiableProperties = false;

    public HibernateIdentifiableObjectStoreEM(JdbcTemplate jdbcTemplate, Class<T> clazz, UserService userService, boolean cacheable) {
        super(jdbcTemplate, clazz, cacheable);
        //        checkNotNull(userService);
        //        checkNotNull( aclService );

        this.userService = userService;
        //        this.aclService = aclService;
        this.cacheable = cacheable;
    }

    /**
     * Indicates whether the object represented by the implementation does not
     * have persisted identifiable object properties.
     */
    private boolean isTransientIdentifiableProperties() {
        return transientIdentifiableProperties;
    }

    // -------------------------------------------------------------------------
    // IdentifiableObjectStore implementation
    // -------------------------------------------------------------------------

    @Override
    public void save(T object) {
        save(object, true);
    }

    @Override
    public void save(T object, User user) {
        save(object, user, true);
    }

    @Override
    public void save(T object, boolean clearSharing) {
        save(object, getCurrentUser(), clearSharing);
    }

    private void save(T object, User user, boolean clearSharing) {
        String username = user != null ? user.getLogin() : "system-process";

        if (IdentifiableObject.class.isAssignableFrom(object.getClass())) {
            object.setAutoFields();

            BaseIdentifiableObject identifiableObject = object;
            identifiableObject.setAutoFields();
            identifiableObject.setLastUpdatedBy(user);

            if (identifiableObject.getUser() == null) {
                identifiableObject.setUser(user);
            }
        }

        AuditLogUtil.infoWrapper(log, username, object, AuditLogUtil.ACTION_CREATE);

        entityManager.persist(object);
    }

    @Override
    public void update(T object) {
        update(object, getCurrentUser());
    }

    @Override
    public void update(T object, User user) {
        String username = user != null ? user.getLogin() : "system-process";

        if (object != null) {
            object.setAutoFields();

            object.setAutoFields();
            object.setLastUpdatedBy(user);

            if (object.getUser() == null) {
                object.setUser(user);
            }
        }

        if (!isUpdateAllowed(object, user)) {
            AuditLogUtil.infoWrapper(log, username, object, AuditLogUtil.ACTION_UPDATE_DENIED);
            throw new UpdateAccessDeniedException(String.valueOf(object));
        }

        AuditLogUtil.infoWrapper(log, username, object, AuditLogUtil.ACTION_UPDATE);

        if (object != null) {
            entityManager.merge(object);
        }
    }

    @Override
    public void delete(T object) {
        this.delete(object, getCurrentUser());
    }

    @Override
    public final void delete(T object, User user) {
        String username = user != null ? user.getLogin() : "system-process";

        if (!isDeleteAllowed(object, user)) {
            AuditLogUtil.infoWrapper(log, username, object, AuditLogUtil.ACTION_DELETE_DENIED);
            throw new DeleteAccessDeniedException(object.toString());
        }

        AuditLogUtil.infoWrapper(log, username, object, AuditLogUtil.ACTION_DELETE);

        if (object != null) {
            super.delete(object);
        }
    }

    @Override
    public final T get(Long id) {
        T object = entityManager.find(getClazz(), id);

        if (!isReadAllowed(object, getCurrentUser())) {
            AuditLogUtil.infoWrapper(log, SecurityUtils.getCurrentUserLogin().orElse(null), object, AuditLogUtil.ACTION_READ_DENIED);
            throw new ReadAccessDeniedException(object.toString());
        }

        return postProcessObject(object);
    }

    @Override
    public List<T> getAll() {
        CriteriaBuilder builder = getCriteriaBuilder();

        return getList(builder, new JpaQueryParameters<T>());
    }

    @Override
    public int getCount() {
        CriteriaBuilder builder = getCriteriaBuilder();

        JpaQueryParameters<T> param = new JpaQueryParameters<T>()
            //            .addPredicates( getSharingPredicates( builder ) )
            .count(root -> builder.countDistinct(root.get("id")));

        return getCount(builder, param).intValue();
    }

    @Override
    public final T getByUid(String uid) {
        if (isTransientIdentifiableProperties()) {
            return null;
        }

        CriteriaBuilder builder = getCriteriaBuilder();

        JpaQueryParameters<T> param = new JpaQueryParameters<T>()
            //            .addPredicates( getSharingPredicates( builder ) )
            .addPredicate(root -> builder.equal(root.get("uid"), uid));

        return getSingleResult(builder, param);
    }

    @Override
    public final T getByUidNoAcl(String uid) {
        if (isTransientIdentifiableProperties()) {
            return null;
        }

        CriteriaBuilder builder = getCriteriaBuilder();

        JpaQueryParameters<T> param = new JpaQueryParameters<T>().addPredicate(root -> builder.equal(root.get("uid"), uid));

        return getSingleResult(builder, param);
    }

    @Override
    public final void updateNoAcl(T object) {
        object.setAutoFields();
        entityManager.merge(object);
    }

    /**
     * Uses query since name property might not be unique.
     */
    @Override
    public final T getByName(String name) {
        CriteriaBuilder builder = getCriteriaBuilder();

        JpaQueryParameters<T> param = new JpaQueryParameters<T>()
            //            .addPredicates( getSharingPredicates( builder ) )
            .addPredicate(root -> builder.equal(root.get("name"), name));

        List<T> list = getList(builder, param);

        T object = list != null && !list.isEmpty() ? list.get(0) : null;

        if (!isReadAllowed(object, getCurrentUser())) {
            AuditLogUtil.infoWrapper(log, SecurityUtils.getCurrentUserLogin().orElse(null), object, AuditLogUtil.ACTION_READ_DENIED);
            throw new ReadAccessDeniedException(String.valueOf(object));
        }

        return object;
    }

    @Override
    public final T getByCode(String code) {
        if (isTransientIdentifiableProperties()) {
            return null;
        }

        CriteriaBuilder builder = getCriteriaBuilder();

        JpaQueryParameters<T> param = new JpaQueryParameters<T>()
            //            .addPredicates( getSharingPredicates( builder ) )
            .addPredicate(root -> builder.equal(root.get("code"), code));

        return getSingleResult(builder, param);
    }

    @Override
    public List<T> getAllEqName(String name) {
        CriteriaBuilder builder = getCriteriaBuilder();

        JpaQueryParameters<T> param = new JpaQueryParameters<T>()
            //            .addPredicates( getSharingPredicates( builder ) )
            .addPredicate(root -> builder.equal(root.get("name"), name))
            .addOrder(root -> builder.asc(root.get("name")));

        return getList(builder, param);
    }

    @Override
    public List<T> getAllLikeName(String name) {
        return getAllLikeName(name, true);
    }

    @Override
    public List<T> getAllLikeName(String name, boolean caseSensitive) {
        CriteriaBuilder builder = getCriteriaBuilder();

        Function<Root<T>, Predicate> likePredicate;

        if (caseSensitive) {
            likePredicate = root -> builder.like(root.get("name"), "%" + name + "%");
        } else {
            likePredicate = root -> builder.like(builder.lower(root.get("name")), "%" + name.toLowerCase() + "%");
        }

        JpaQueryParameters<T> param = new JpaQueryParameters<T>()
            //            .addPredicates( getSharingPredicates( builder ) )
            .addPredicate(likePredicate)
            .addOrder(root -> builder.asc(root.get("name")));

        return getList(builder, param);
    }

    @Override
    public List<T> getAllLikeName(String name, int first, int max) {
        return getAllLikeName(name, first, max, true);
    }

    @Override
    public List<T> getAllLikeName(String name, int first, int max, boolean caseSensitive) {
        CriteriaBuilder builder = getCriteriaBuilder();

        Function<Root<T>, Predicate> likePredicate;

        if (caseSensitive) {
            likePredicate = root -> builder.like(root.get("name"), "%" + name + "%");
        } else {
            likePredicate = root -> builder.like(builder.lower(root.get("name")), "%" + name.toLowerCase() + "%");
        }

        JpaQueryParameters<T> param = new JpaQueryParameters<T>()
            //            .addPredicates( getSharingPredicates( builder ) )
            .addPredicate(likePredicate)
            .addOrder(root -> builder.asc(root.get("name")))
            .setFirstResult(first)
            .setMaxResults(max);

        return getList(builder, param);
    }

    @Override
    public List<T> getAllLikeName(Set<String> nameWords, int first, int max) {
        CriteriaBuilder builder = getCriteriaBuilder();

        JpaQueryParameters<T> param = new JpaQueryParameters<T>()
            //            .addPredicates( getSharingPredicates( builder ) )
            .addOrder(root -> builder.asc(root.get("name")))
            .setFirstResult(first)
            .setMaxResults(max);

        if (nameWords.isEmpty()) {
            return getList(builder, param);
        }

        List<Function<Root<T>, Predicate>> conjunction = new ArrayList<>();

        for (String word : nameWords) {
            conjunction.add(root -> builder.like(builder.lower(root.get("name")), "%" + word.toLowerCase() + "%"));
        }

        param.addPredicate(
            root -> builder.and(conjunction.stream().map(p -> p.apply(root)).collect(Collectors.toList()).toArray(new Predicate[0]))
        );

        return getList(builder, param);
    }

    public List<T> getAllLikeNameAndEqualsAttribute(Set<String> nameWords, String attribute, String attributeValue, int first, int max) {
        if (StringUtils.isEmpty(attribute) || StringUtils.isEmpty(attributeValue)) {
            return new ArrayList<>();
        }

        CriteriaBuilder builder = getCriteriaBuilder();

        JpaQueryParameters<T> param = new JpaQueryParameters<T>()
            //            .addPredicates( getSharingPredicates( builder ) )
            .addOrder(root -> builder.asc(root.get("name")))
            .setFirstResult(first)
            .setMaxResults(max);

        if (nameWords.isEmpty()) {
            return getList(builder, param);
        }

        List<Function<Root<T>, Predicate>> conjunction = new ArrayList<>();

        for (String word : nameWords) {
            conjunction.add(root -> builder.like(builder.lower(root.get("name")), "%" + word.toLowerCase() + "%"));
        }

        conjunction.add(root -> builder.equal(builder.lower(root.get(attribute)), attributeValue));

        param.addPredicate(
            root -> builder.and(conjunction.stream().map(p -> p.apply(root)).collect(Collectors.toList()).toArray(new Predicate[0]))
        );

        return getList(builder, param);
    }

    @Override
    public List<T> getAllOrderedName() {
        CriteriaBuilder builder = getCriteriaBuilder();

        JpaQueryParameters<T> param = new JpaQueryParameters<T>()
            //            .addPredicates( getSharingPredicates( builder ) )
            .addOrder(root -> builder.asc(root.get("name")));

        return getList(builder, param);
    }

    @Override
    public List<T> getAllOrderedName(int first, int max) {
        CriteriaBuilder builder = getCriteriaBuilder();

        JpaQueryParameters<T> param = new JpaQueryParameters<T>()
            //            .addPredicates( getSharingPredicates( builder ) )
            .addOrder(root -> builder.asc(root.get("name")))
            .setFirstResult(first)
            .setMaxResults(max);

        return getList(builder, param);
    }

    @Override
    public List<T> getAllOrderedLastUpdated(int first, int max) {
        CriteriaBuilder builder = getCriteriaBuilder();

        JpaQueryParameters<T> param = new JpaQueryParameters<T>()
            //            .addPredicates( getSharingPredicates( builder ) )
            .addOrder(root -> builder.asc(root.get("lastUpdated")));

        return getList(builder, param);
    }

    @Override
    public int getCountLikeName(String name) {
        CriteriaBuilder builder = getCriteriaBuilder();

        JpaQueryParameters<T> param = new JpaQueryParameters<T>()
            //            .addPredicates( getSharingPredicates( builder ) )
            .addPredicate(root -> builder.like(builder.lower(root.get("name")), "%" + name.toLowerCase() + "%"))
            .count(root -> builder.countDistinct(root.get("id")));

        return getCount(builder, param).intValue();
    }

    @Override
    public int getCountGeLastUpdated(Date lastUpdated) {
        CriteriaBuilder builder = getCriteriaBuilder();

        JpaQueryParameters<T> param = new JpaQueryParameters<T>()
            //            .addPredicates( getSharingPredicates( builder ) )
            .addPredicate(root -> builder.greaterThanOrEqualTo(root.get("lastUpdated"), lastUpdated))
            .count(root -> builder.countDistinct(root.get("id")));

        return getCount(builder, param).intValue();
    }

    @Override
    public List<T> getAllGeLastUpdated(Date lastUpdated) {
        CriteriaBuilder builder = getCriteriaBuilder();

        JpaQueryParameters<T> param = new JpaQueryParameters<T>()
            //            .addPredicates( getSharingPredicates( builder ) )
            .addPredicate(root -> builder.greaterThanOrEqualTo(root.get("lastUpdated"), lastUpdated))
            .addOrder(root -> builder.desc(root.get("lastUpdated")));

        return getList(builder, param);
    }

    @Override
    public int getCountGeCreated(Date created) {
        CriteriaBuilder builder = getCriteriaBuilder();

        JpaQueryParameters<T> param = new JpaQueryParameters<T>()
            //            .addPredicates( getSharingPredicates( builder ) )
            .addPredicate(root -> builder.greaterThanOrEqualTo(root.get("created"), created))
            .count(root -> builder.countDistinct(root.get("id")));

        return getCount(builder, param).intValue();
    }

    @Override
    public List<T> getAllGeCreated(Date created) {
        CriteriaBuilder builder = getCriteriaBuilder();

        JpaQueryParameters<T> param = new JpaQueryParameters<T>()
            //            .addPredicates( getSharingPredicates( builder ) )
            .addPredicate(root -> builder.greaterThanOrEqualTo(root.get("created"), created))
            .addOrder(root -> builder.desc(root.get("created")));

        return getList(builder, param);
    }

    @Override
    public List<T> getAllLeCreated(Date created) {
        CriteriaBuilder builder = getCriteriaBuilder();

        JpaQueryParameters<T> param = new JpaQueryParameters<T>()
            //            .addPredicates( getSharingPredicates( builder ) )
            .addPredicate(root -> builder.lessThanOrEqualTo(root.get("created"), created))
            .addOrder(root -> builder.desc(root.get("created")));

        return getList(builder, param);
    }

    @Override
    public Date getLastUpdated() {
        CriteriaBuilder builder = getCriteriaBuilder();

        CriteriaQuery<Date> query = builder.createQuery(Date.class);

        Root<T> root = query.from(getClazz());

        query.select(root.get("lastUpdated"));

        query.orderBy(builder.desc(root.get("lastUpdated")));

        TypedQuery<Date> typedQuery = entityManager.createQuery(query);

        typedQuery.setMaxResults(1);

        typedQuery.setHint(JpaQueryUtils.HIBERNATE_CACHEABLE_HINT, true);

        return getSingleResult(typedQuery);
    }

    @Override
    public List<T> getByDataDimension(boolean dataDimension) {
        CriteriaBuilder builder = getCriteriaBuilder();

        JpaQueryParameters<T> jpaQueryParameters = new JpaQueryParameters<T>()
            .addPredicate(root -> builder.equal(root.get("dataDimension"), dataDimension));
        //            .addPredicates( getSharingPredicates( builder ) );

        return getList(builder, jpaQueryParameters);
    }

    @Override
    public List<T> getByDataDimensionNoAcl(boolean dataDimension) {
        CriteriaBuilder builder = getCriteriaBuilder();

        JpaQueryParameters<T> jpaQueryParameters = new JpaQueryParameters<T>()
            .addPredicate(root -> builder.equal(root.get("dataDimension"), dataDimension));

        return getList(builder, jpaQueryParameters);
    }

    @Override
    public List<T> getById(Collection<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return new ArrayList<>();
        }

        CriteriaBuilder builder = getCriteriaBuilder();

        JpaQueryParameters<T> jpaQueryParameters = new JpaQueryParameters<T>()
            //            .addPredicates( getSharingPredicates( builder ) )
            .addPredicate(root -> root.get("id").in(ids));

        return getList(builder, jpaQueryParameters);
    }

    @Override
    public List<T> getByUid(Collection<String> uids) {
        if (uids == null || uids.isEmpty()) {
            return new ArrayList<>();
        }

        //TODO Include paging to avoid exceeding max query length

        CriteriaBuilder builder = getCriteriaBuilder();

        JpaQueryParameters<T> jpaQueryParameters = new JpaQueryParameters<T>()
            //            .addPredicates( getSharingPredicates( builder ) )
            .addPredicate(root -> root.get("uid").in(uids));

        return getList(builder, jpaQueryParameters);
    }

    @Override
    public List<T> getByUid(Collection<String> uids, User user) {
        if (uids == null || uids.isEmpty()) {
            return new ArrayList<>();
        }

        CriteriaBuilder builder = getCriteriaBuilder();

        JpaQueryParameters<T> jpaQueryParameters = new JpaQueryParameters<T>()
            //            .addPredicates( getSharingPredicates( builder, user ) )
            .addPredicate(root -> root.get("uid").in(uids));

        return getList(builder, jpaQueryParameters);
    }

    @Override
    public List<T> getByUidNoAcl(Collection<String> uids) {
        if (uids == null || uids.isEmpty()) {
            return new ArrayList<>();
        }

        List<T> objects = Lists.newArrayList();

        List<List<String>> partitions = Lists.partition(Lists.newArrayList(uids), OBJECT_FETCH_SIZE);

        for (List<String> partition : partitions) {
            objects.addAll(getByUidNoAclInternal(partition));
        }

        return objects;
    }

    private List<T> getByUidNoAclInternal(Collection<String> uids) {
        CriteriaBuilder builder = getCriteriaBuilder();

        JpaQueryParameters<T> jpaQueryParameters = new JpaQueryParameters<T>().addPredicate(root -> root.get("uid").in(uids));

        return getList(builder, jpaQueryParameters);
    }

    @Override
    public List<T> getByCode(Collection<String> codes) {
        if (codes == null || codes.isEmpty()) {
            return new ArrayList<>();
        }

        CriteriaBuilder builder = getCriteriaBuilder();

        JpaQueryParameters<T> jpaQueryParameters = new JpaQueryParameters<T>()
            //            .addPredicates( getSharingPredicates( builder ) )
            .addPredicate(root -> root.get("code").in(codes));

        return getList(builder, jpaQueryParameters);
    }

    @Override
    public List<T> getByCode(Collection<String> codes, User user) {
        if (codes == null || codes.isEmpty()) {
            return new ArrayList<>();
        }

        CriteriaBuilder builder = getCriteriaBuilder();

        JpaQueryParameters<T> jpaQueryParameters = new JpaQueryParameters<T>()
            //            .addPredicates( getSharingPredicates( builder, user ) )
            .addPredicate(root -> root.get("code").in(codes));

        return getList(builder, jpaQueryParameters);
    }

    @Override
    public List<T> getByName(Collection<String> names) {
        if (names == null || names.isEmpty()) {
            return new ArrayList<>();
        }

        CriteriaBuilder builder = getCriteriaBuilder();

        JpaQueryParameters<T> jpaQueryParameters = new JpaQueryParameters<T>()
            //            .addPredicates( getSharingPredicates( builder ) )
            .addPredicate(root -> root.get("name").in(names));

        return getList(builder, jpaQueryParameters);
    }

    @Override
    public List<T> getAllNoAcl() {
        return super.getAll();
    }

    @Override
    public List<String> getUidsCreatedBefore(Date date) {
        CriteriaBuilder builder = getCriteriaBuilder();

        CriteriaQuery<String> query = builder.createQuery(String.class);

        Root<T> root = query.from(getClazz());

        query.select(root.get("uid"));
        query.where(builder.lessThan(root.get("created"), date));

        TypedQuery<String> typedQuery = entityManager.createQuery(query);
        typedQuery.setHint(JpaQueryUtils.HIBERNATE_CACHEABLE_HINT, true);

        return typedQuery.getResultList();
    }

    //----------------------------------------------------------------------------------------------------------------
    // Data sharing
    //----------------------------------------------------------------------------------------------------------------

    @Override
    public final List<T> getDataReadAll() {
        CriteriaBuilder builder = getCriteriaBuilder();

        JpaQueryParameters<T> parameters = new JpaQueryParameters<T>();
        //            .addPredicates( getDataSharingPredicates( builder ) );

        return getList(builder, parameters);
    }

    @Override
    public final List<T> getDataReadAll(User user) {
        CriteriaBuilder builder = getCriteriaBuilder();

        JpaQueryParameters<T> parameters = new JpaQueryParameters<T>();
        //            .addPredicates( getDataSharingPredicates( builder, user ) );

        return getList(builder, parameters);
    }

    @Override
    public final List<T> getDataWriteAll() {
        CriteriaBuilder builder = getCriteriaBuilder();

        JpaQueryParameters<T> parameters = new JpaQueryParameters<T>();
        //            .addPredicates( getDataSharingPredicates( builder, AclService.LIKE_WRITE_DATA ) );

        return getList(builder, parameters);
    }

    @Override
    public final List<T> getDataWriteAll(User user) {
        CriteriaBuilder builder = getCriteriaBuilder();

        JpaQueryParameters<T> parameters = new JpaQueryParameters<T>();
        //            .addPredicates( getDataSharingPredicates( builder, user, AclService.LIKE_WRITE_DATA ) );

        return getList(builder, parameters);
    }

    @Override
    public final List<T> getDataReadAll(int first, int max) {
        CriteriaBuilder builder = getCriteriaBuilder();

        JpaQueryParameters<T> parameters = new JpaQueryParameters<T>()
            //            .addPredicates( getDataSharingPredicates( builder ) )
            .setFirstResult(first)
            .setMaxResults(max);

        return getList(builder, parameters);
    }

    //----------------------------------------------------------------------------------------------------------------
    // Supportive methods
    //----------------------------------------------------------------------------------------------------------------

    // ----------------------------------------------------------------------
    // JPA Implementations
    // ----------------------------------------------------------------------

    private boolean forceAcl() {
        return false;
        //        return Dashboard.class.isAssignableFrom( clazz );
    }

    private boolean sharingEnabled(User user) {
        return false;
        //        return forceAcl() || (aclService.isShareable( clazz ) && !(user == null || user.isSuper()));
    }

    private boolean isReadAllowed(T object, User user) {
        //        if ( IdentifiableObject.class.isInstance( object ) )
        ////        {
        ////            IdentifiableObject idObject = object;
        ////
        ////            if ( sharingEnabled( user ) )
        ////            {
        ////                return aclService.canRead( user, idObject );
        ////            }
        ////        }
        ////
        ////        return true;
        if (user == null) {
            return true;
        }

        return SecurityUtils.hasCurrentUserThisAuthority(AuthoritiesConstants.ADMIN);
    }

    private boolean isUpdateAllowed(T object, User user) {
        //        if ( IdentifiableObject.class.isInstance( object ) )
        //        {
        //            IdentifiableObject idObject = object;
        //
        //            if ( aclService.isShareable( clazz ) )
        //            {
        //                return aclService.canUpdate( user, idObject );
        //            }
        //        }
        //        return true;
        if (user == null) {
            return true;
        }
        return SecurityUtils.hasCurrentUserThisAuthority(AuthoritiesConstants.ADMIN);
    }

    private boolean isDeleteAllowed(T object, User user) {
        //        if ( IdentifiableObject.class.isInstance( object ) )
        //        {
        //            IdentifiableObject idObject = object;
        //
        //            if ( aclService.isShareable( clazz ) )
        //            {
        //                return aclService.canDelete( user, idObject );
        //            }
        //        }
        //        return true;
        if (user == null) {
            return true;
        }
        return SecurityUtils.hasCurrentUserThisAuthority(AuthoritiesConstants.ADMIN);
    }

    public void flush() {
        entityManager.flush();
    }

    private User getCurrentUser() {
        User user = userService.getUserWithAuthorities().orElse(null);

        if (user != null) {
            return user;
        }

        return null;
    }
}
