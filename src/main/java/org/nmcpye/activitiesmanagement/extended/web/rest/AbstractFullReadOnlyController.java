package org.nmcpye.activitiesmanagement.extended.web.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import org.nmcpye.activitiesmanagement.domain.User;
import org.nmcpye.activitiesmanagement.extended.common.IdentifiableObject;
import org.nmcpye.activitiesmanagement.extended.common.IdentifiableObjectManager;
import org.nmcpye.activitiesmanagement.extended.common.Pager;
import org.nmcpye.activitiesmanagement.extended.common.UserContext;
import org.nmcpye.activitiesmanagement.extended.dxf2module.common.OrderParams;
import org.nmcpye.activitiesmanagement.extended.dxf2module.webmessage.WebMessageException;
import org.nmcpye.activitiesmanagement.extended.hibernatemodule.hibernate.exception.ReadAccessDeniedException;
import org.nmcpye.activitiesmanagement.extended.schemamodule.Schema;
import org.nmcpye.activitiesmanagement.extended.schemamodule.SchemaService;
import org.nmcpye.activitiesmanagement.extended.serviceaclmodule.security.acl.AclService;
import org.nmcpye.activitiesmanagement.extended.servicecoremodule.query.*;
import org.nmcpye.activitiesmanagement.extended.web.service.ContextService;
import org.nmcpye.activitiesmanagement.extended.web.service.LinkService;
import org.nmcpye.activitiesmanagement.extended.web.utils.ContextUtils;
import org.nmcpye.activitiesmanagement.extended.web.utils.PaginationUtils;
import org.nmcpye.activitiesmanagement.extended.web.webdomain.WebMetadata;
import org.nmcpye.activitiesmanagement.extended.web.webdomain.WebOptions;
import org.nmcpye.activitiesmanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;
import static org.nmcpye.activitiesmanagement.extended.dxf2module.webmessage.WebMessageUtils.notFound;
import static org.springframework.http.CacheControl.noCache;

/**
 * Base controller for APIs that only want to offer read only access
 * <p>
 * Created by Hamza on 12/11/2021.
 */
public abstract class AbstractFullReadOnlyController<T extends IdentifiableObject> {
    protected static final WebOptions NO_WEB_OPTIONS = new WebOptions(new HashMap<>());

    @Autowired
    protected IdentifiableObjectManager manager;

    @Autowired
    protected ContextService contextService;

    @Autowired
    protected QueryService queryService;

    @Autowired
    protected LinkService linkService;

    @Autowired
    protected AclService aclService;

    @Autowired
    protected ObjectMapper jsonMapper;

    @Autowired
    protected SchemaService schemaService;

    @Autowired
    protected UserService userService;

    // --------------------------------------------------------------------------
    // Hooks
    // --------------------------------------------------------------------------

    /**
     * Override to process entities after it has been retrieved from storage and
     * before it is returned to the view. Entities is null-safe.
     */
    protected void postProcessResponseEntities(List<T> entityList, WebOptions options, Map<String, String> parameters) {
    }

    /**
     * Override to process a single entity after it has been retrieved from
     * storage and before it is returned to the view. Entity is null-safe.
     */
    protected void postProcessResponseEntity(T entity, WebOptions options, Map<String, String> parameters)
        throws Exception {
    }

    // --------------------------------------------------------------------------
    // GET Full
    // --------------------------------------------------------------------------

    @GetMapping
    public @ResponseBody
    List<T> getObjectList(
        @RequestParam Map<String, String> rpParameters, OrderParams orderParams,
        HttpServletResponse response, User currentUser)
        throws QueryParserException {
        List<String> fields = Lists.newArrayList(contextService.getParameterValues("fields"));
        List<String> filters = Lists.newArrayList(contextService.getParameterValues("filter"));
        List<Order> orders = orderParams.getOrders(getSchema());

//        if ( fields.isEmpty() )
//        {
//            fields.addAll( Preset.defaultPreset().getFields() );
//        }

        WebOptions options = new WebOptions(rpParameters);
        WebMetadata metadata = new WebMetadata();

        if (!aclService.canRead(currentUser, getEntityClass())) {
            throw new ReadAccessDeniedException(
                "You don't have the proper permissions to read objects of this type.");
        }

        List<T> entities = getEntityList(metadata, options, filters, orders);

        Pager pager = metadata.getPager();
        if (options.hasPaging() && pager == null) {
            long totalCount;

            if (options.getOptions().containsKey("query")) {
                totalCount = entities.size();

                long skip = (long) (options.getPage() - 1) * options.getPageSize();
                entities = entities.stream()
                    .skip(skip)
                    .limit(options.getPageSize())
                    .collect(toList());
            } else {
                totalCount = countTotal(options, filters, orders);
            }

            pager = new Pager(options.getPage(), totalCount, options.getPageSize());
        }

        postProcessResponseEntities(entities, options, rpParameters);

        handleLinksAndAccess(entities, fields, false);

        linkService.generatePagerLinks(pager, getEntityClass());

        cachePrivate(response);

        return entities;
    }

    @GetMapping("/{uid}")
    public @ResponseBody
    T getObject(
        @PathVariable("uid") String pvUid,
        @RequestParam Map<String, String> rpParameters,
        HttpServletRequest request, HttpServletResponse response)
        throws Exception {
        User user = userService.getUserWithAuthorities().orElse(null);

        if (!aclService.canRead(user, getEntityClass())) {
            throw new ReadAccessDeniedException(
                "You don't have the proper permissions to read objects of this type.");
        }

        List<String> fields = Lists.newArrayList(contextService.getParameterValues("fields"));
        List<String> filters = Lists.newArrayList(contextService.getParameterValues("filter"));

        if (fields.isEmpty()) {
            fields.add(":all");
        }

        cachePrivate(response);

        return getObjectInternal(pvUid, rpParameters, filters, fields, user).get(0);
    }

    @SuppressWarnings("unchecked")
    private List<T> getObjectInternal(String uid, Map<String, String> parameters,
                                      List<String> filters, List<String> fields, User user)
        throws Exception {
        WebOptions options = new WebOptions(parameters);
        List<T> entities = getEntity(uid, options);

        if (entities.isEmpty()) {
            throw new WebMessageException(notFound(getEntityClass(), uid));
        }

        Query query = queryService.getQueryFromUrl(getEntityClass(), filters, new ArrayList<>(),
            getPaginationData(options), options.getRootJunction());
        query.setUser(user);
        query.setObjects(entities);

        entities = (List<T>) queryService.query(query);

        handleLinksAndAccess(entities, fields, true);

        for (T entity : entities) {
            postProcessResponseEntity(entity, options, parameters);
        }

        return entities;
    }

    @SuppressWarnings("unchecked")
    protected List<T> getEntityList(WebMetadata metadata, WebOptions options, List<String> filters,
                                    List<Order> orders)
        throws QueryParserException {
        List<T> entityList;
        Query query = queryService.getQueryFromUrl(getEntityClass(), filters, orders, getPaginationData(options),
            options.getRootJunction());
        query.setDefaultOrder();

        if (options.getOptions().containsKey("query")) {
            entityList = Lists.newArrayList(manager.filter(getEntityClass(), options.getOptions().get("query")));
        } else {
            entityList = (List<T>) queryService.query(query);
        }

        return entityList;
    }

    private long countTotal(WebOptions options, List<String> filters, List<Order> orders) {
        Query query = queryService.getQueryFromUrl(getEntityClass(), filters, orders, new Pagination(),
            options.getRootJunction());

        return queryService.count(query);
    }

    private void cachePrivate(HttpServletResponse response) {
        response.setHeader(ContextUtils.HEADER_CACHE_CONTROL,
            noCache().cachePrivate().getHeaderValue());
    }

    private boolean hasHref(List<String> fields) {
        return fieldsContains("href", fields);
    }

    private void handleLinksAndAccess(List<T> entityList, List<String> fields, boolean deep) {
        boolean generateLinks = hasHref(fields);

        if (generateLinks) {
            linkService.generateLinks(entityList, deep);
        }
    }

    private boolean fieldsContains(String match, List<String> fields) {
        for (String field : fields) {
            // for now assume href/access if * or preset is requested
            if (field.contains(match) || field.equals("*") || field.startsWith(":")) {
                return true;
            }
        }

        return false;
    }

    // --------------------------------------------------------------------------
    // Reflection helpers
    // --------------------------------------------------------------------------

    private String entityName;

    private String entitySimpleName;

    protected final String getEntityName() {
        if (entityName == null) {
            entityName = getEntityClass().getName();
        }

        return entityName;
    }

    protected final String getEntitySimpleName() {
        if (entitySimpleName == null) {
            entitySimpleName = getEntityClass().getSimpleName();
        }

        return entitySimpleName;
    }

    protected final List<T> getEntity(String uid) {
        return getEntity(uid, NO_WEB_OPTIONS);
    }

    protected List<T> getEntity(String uid, WebOptions options) {
        ArrayList<T> list = new ArrayList<>();
        getEntity(uid, getEntityClass()).ifPresent(list::add);
        return list; // TODO consider ACL
    }

    protected final <E extends IdentifiableObject> java.util.Optional<E> getEntity(String uid, Class<E> entityType) {
        return java.util.Optional.ofNullable(manager.getNoAcl(entityType, uid));
    }

    protected final Schema getSchema(Class<?> klass) {
        return schemaService.getDynamicSchema(klass);
    }

    // --------------------------------------------------------------------------
    // Helpers
    // --------------------------------------------------------------------------

    protected final void setUserContext() {
        setUserContext(userService.getUserWithAuthorities().orElse(null));
    }

    protected final void setUserContext(User user) {
//        Locale dbLocale = getLocaleWithDefault(translateParams);
        UserContext.setUser(user);
//        UserContext.setUserSetting(UserSettingKey.DB_LOCALE, dbLocale);
    }

//    private Locale getLocaleWithDefault(TranslateParams translateParams) {
//        return translateParams.isTranslate()
//            ? translateParams.getLocaleWithDefault(
//            (Locale) userSettingService.getUserSetting(UserSettingKey.DB_LOCALE))
//            : null;
//    }

    protected final Pagination getPaginationData(WebOptions options) {
        return PaginationUtils.getPaginationData(options);
    }

    private Class<T> entityClass;

    @SuppressWarnings("unchecked")
    protected final Class<T> getEntityClass() {
        if (entityClass == null) {
            Type[] actualTypeArguments = ((ParameterizedType) getClass().getGenericSuperclass())
                .getActualTypeArguments();
            entityClass = (Class<T>) actualTypeArguments[0];
        }

        return entityClass;
    }

    private Schema schema;

    protected final Schema getSchema() {
        if (schema == null) {
            schema = schemaService.getDynamicSchema(getEntityClass());
        }
        return schema;
    }
}
