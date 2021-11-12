package org.nmcpye.activitiesmanagement.extended.web.rest;

import com.google.common.base.Enums;
import com.google.common.base.Joiner;
import com.google.common.base.Optional;
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
import org.nmcpye.activitiesmanagement.extended.serviceaclmodule.security.acl.AclService;
import org.nmcpye.activitiesmanagement.extended.servicecoremodule.query.*;
import org.nmcpye.activitiesmanagement.extended.web.service.ContextService;
import org.nmcpye.activitiesmanagement.extended.web.service.LinkService;
import org.nmcpye.activitiesmanagement.extended.web.utils.ContextUtils;
import org.nmcpye.activitiesmanagement.extended.web.webdomain.WebMetadata;
import org.nmcpye.activitiesmanagement.extended.web.webdomain.WebOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
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
//    protected static final WebOptions NO_WEB_OPTIONS = new WebOptions( new HashMap<>() );

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

    // --------------------------------------------------------------------------
    // Hooks
    // --------------------------------------------------------------------------

//    /**
//     * Override to process entities after it has been retrieved from storage and
//     * before it is returned to the view. Entities is null-safe.
//     */
//    protected void postProcessResponseEntities(List<T> entityList, WebOptions options, Map<String, String> parameters) {
//    }
//
//    /**
//     * Override to process a single entity after it has been retrieved from
//     * storage and before it is returned to the view. Entity is null-safe.
//     */
//    protected void postProcessResponseEntity(T entity, WebOptions options, Map<String, String> parameters)
//        throws Exception {
//    }
//
//    // --------------------------------------------------------------------------
//    // GET Full
//    // --------------------------------------------------------------------------
//
//    @GetMapping
//    public @ResponseBody
//    RootNode getObjectList(
//        @RequestParam Map<String, String> rpParameters, OrderParams orderParams,
//        HttpServletResponse response, User currentUser)
//        throws QueryParserException {
//        List<String> fields = Lists.newArrayList(contextService.getParameterValues("fields"));
//        List<String> filters = Lists.newArrayList(contextService.getParameterValues("filter"));
//        List<Order> orders = orderParams.getOrders(getSchema());
//
////        if ( fields.isEmpty() )
////        {
////            fields.addAll( Preset.defaultPreset().getFields() );
////        }
//
//        WebOptions options = new WebOptions(rpParameters);
//        WebMetadata metadata = new WebMetadata();
//
//        if (!aclService.canRead(currentUser, getEntityClass())) {
//            throw new ReadAccessDeniedException(
//                "You don't have the proper permissions to read objects of this type.");
//        }
//
//        List<T> entities = getEntityList(metadata, options, filters, orders);
//
//        Pager pager = metadata.getPager();
//
//        if (options.hasPaging() && pager == null) {
//            long totalCount;
//
//            if (options.getOptions().containsKey("query")) {
//                totalCount = entities.size();
//
//                long skip = (long) (options.getPage() - 1) * options.getPageSize();
//                entities = entities.stream()
//                    .skip(skip)
//                    .limit(options.getPageSize())
//                    .collect(toList());
//            } else {
//                totalCount = countTotal(options, filters, orders);
//            }
//
//            pager = new Pager(options.getPage(), totalCount, options.getPageSize());
//        }
//
//        postProcessResponseEntities(entities, options, rpParameters);
//
//        handleLinksAndAccess(entities, fields, false);
//
//        handleAttributeValues(entities, fields);
//
//        linkService.generatePagerLinks(pager, getEntityClass());
//
//        RootNode rootNode = NodeUtils.createMetadata();
//        rootNode.getConfig().setInclusionStrategy(getInclusionStrategy(rpParameters.get("inclusionStrategy")));
//
//        if (pager != null) {
//            rootNode.addChild(NodeUtils.createPager(pager));
//        }
//
//        rootNode.addChild(fieldFilterService.toCollectionNode(getEntityClass(),
//            new FieldFilterParams(entities, fields, Defaults.valueOf(options.get("defaults", DEFAULTS)))));
//
//        cachePrivate(response);
//
//        return rootNode;
//    }
//
//    @GetMapping("/{uid}")
//    public @ResponseBody
//    RootNode getObject(
//        @PathVariable("uid") String pvUid,
//        @RequestParam Map<String, String> rpParameters,
//        HttpServletRequest request, HttpServletResponse response)
//        throws Exception {
//        User user = currentUserService.getCurrentUser();
//
//        if (!aclService.canRead(user, getEntityClass())) {
//            throw new ReadAccessDeniedException(
//                "You don't have the proper permissions to read objects of this type.");
//        }
//
//        List<String> fields = Lists.newArrayList(contextService.getParameterValues("fields"));
//        List<String> filters = Lists.newArrayList(contextService.getParameterValues("filter"));
//
//        if (fields.isEmpty()) {
//            fields.add(":all");
//        }
//
//        cachePrivate(response);
//
//        return getObjectInternal(pvUid, rpParameters, filters, fields, user);
//    }
//
//    @GetMapping("/{uid}/{property}")
//    public @ResponseBody
//    RootNode getObjectProperty(
//        @PathVariable("uid") String pvUid, @PathVariable("property") String pvProperty,
//        @RequestParam Map<String, String> rpParameters,
//        TranslateParams translateParams,
//        HttpServletResponse response)
//        throws Exception {
//        User user = currentUserService.getCurrentUser();
//
//        if (!"translations".equals(pvProperty)) {
//            setUserContext(user, translateParams);
//        } else {
//            setUserContext(null, new TranslateParams(false));
//        }
//
//        try {
//            if (!aclService.canRead(user, getEntityClass())) {
//                throw new ReadAccessDeniedException(
//                    "You don't have the proper permissions to read objects of this type.");
//            }
//
//            List<String> fields = Lists.newArrayList(contextService.getParameterValues("fields"));
//
//            if (fields.isEmpty()) {
//                fields.add(":all");
//            }
//
//            String fieldFilter = "[" + Joiner.on(',').join(fields) + "]";
//
//            cachePrivate(response);
//
//            return getObjectInternal(pvUid, rpParameters, Lists.newArrayList(),
//                Lists.newArrayList(pvProperty + fieldFilter), user);
//        } finally {
//            UserContext.reset();
//        }
//    }
//
//    @GetMapping("/{uid}/{property}/{itemId}")
//    public @ResponseBody
//    RootNode getCollectionItem(
//        @PathVariable("uid") String pvUid,
//        @PathVariable("property") String pvProperty,
//        @PathVariable("itemId") String pvItemId,
//        @RequestParam Map<String, String> parameters,
//        TranslateParams translateParams,
//        HttpServletResponse response)
//        throws Exception {
//        User user = currentUserService.getCurrentUser();
//        setUserContext(user, translateParams);
//
//        try {
//            if (!aclService.canRead(user, getEntityClass())) {
//                throw new ReadAccessDeniedException(
//                    "You don't have the proper permissions to read objects of this type.");
//            }
//
//            RootNode rootNode = getObjectInternal(pvUid, parameters, Lists.newArrayList(),
//                Lists.newArrayList(pvProperty + "[:all]"), user);
//
//            // TODO optimize this using field filter (collection filtering)
//            if (!rootNode.getChildren().isEmpty() && rootNode.getChildren().get(0).isCollection()) {
//                rootNode.getChildren().get(0).getChildren().stream().filter(Node::isComplex).forEach(node -> {
//                    node.getChildren().stream()
//                        .filter(child -> child.isSimple() && child.getName().equals("id")
//                            && !((SimpleNode) child).getValue().equals(pvItemId))
//                        .forEach(child -> rootNode.getChildren().get(0).removeChild(node));
//                });
//            }
//
//            if (rootNode.getChildren().isEmpty() || rootNode.getChildren().get(0).getChildren().isEmpty()) {
//                throw new WebMessageException(
//                    notFound(pvProperty + " with ID " + pvItemId + " could not be found."));
//            }
//
//            cachePrivate(response);
//
//            return rootNode;
//        } finally {
//            UserContext.reset();
//        }
//    }
//
//    @SuppressWarnings("unchecked")
//    private RootNode getObjectInternal(String uid, Map<String, String> parameters,
//                                       List<String> filters, List<String> fields, User user)
//        throws Exception {
//        WebOptions options = new WebOptions(parameters);
//        List<T> entities = getEntity(uid, options);
//
//        if (entities.isEmpty()) {
//            throw new WebMessageException(notFound(getEntityClass(), uid));
//        }
//
//        Query query = queryService.getQueryFromUrl(getEntityClass(), filters, new ArrayList<>(),
//            getPaginationData(options), options.getRootJunction());
//        query.setUser(user);
//        query.setObjects(entities);
////        query.setDefaults( Defaults.valueOf( options.get( "defaults", DEFAULTS ) ) );
//
//        entities = (List<T>) queryService.query(query);
//
//        handleLinksAndAccess(entities, fields, true);
//
//        handleAttributeValues(entities, fields);
//
//        for (T entity : entities) {
//            postProcessResponseEntity(entity, options, parameters);
//        }
//
//        CollectionNode collectionNode = fieldFilterService.toCollectionNode(getEntityClass(),
//            new FieldFilterParams(entities, fields, Defaults.valueOf(options.get("defaults", DEFAULTS)))
//                .setUser(user));
//
//        if (options.isTrue("useWrapper") || entities.size() > 1) {
//            RootNode rootNode = NodeUtils.createMetadata(collectionNode);
//            rootNode.getConfig().setInclusionStrategy(getInclusionStrategy(parameters.get("inclusionStrategy")));
//
//            return rootNode;
//        } else {
//            List<Node> children = collectionNode.getChildren();
//            RootNode rootNode;
//
//            if (!children.isEmpty()) {
//                rootNode = NodeUtils.createRootNode(children.get(0));
//            } else {
//                rootNode = NodeUtils.createRootNode(new ComplexNode(getSchema().getSingular()));
//            }
//
//            rootNode.getConfig().setInclusionStrategy(getInclusionStrategy(parameters.get("inclusionStrategy")));
//
//            return rootNode;
//        }
//    }
//
//    @SuppressWarnings("unchecked")
//    protected List<T> getEntityList(WebMetadata metadata, WebOptions options, List<String> filters,
//                                    List<Order> orders)
//        throws QueryParserException {
//        List<T> entityList;
//        Query query = queryService.getQueryFromUrl(getEntityClass(), filters, orders, getPaginationData(options),
//            options.getRootJunction());
//        query.setDefaultOrder();
//        query.setDefaults(Defaults.valueOf(options.get("defaults", DEFAULTS)));
//
//        if (options.getOptions().containsKey("query")) {
//            entityList = Lists.newArrayList(manager.filter(getEntityClass(), options.getOptions().get("query")));
//        } else {
//            entityList = (List<T>) queryService.query(query);
//        }
//
//        return entityList;
//    }
//
//    private long countTotal(WebOptions options, List<String> filters, List<Order> orders) {
//        Query query = queryService.getQueryFromUrl(getEntityClass(), filters, orders, new Pagination(),
//            options.getRootJunction());
//
//        return queryService.count(query);
//    }
//
//    private void cachePrivate(HttpServletResponse response) {
//        response.setHeader(ContextUtils.HEADER_CACHE_CONTROL,
//            noCache().cachePrivate().getHeaderValue());
//    }
//
//    private boolean hasHref(List<String> fields) {
//        return fieldsContains("href", fields);
//    }
//
//    private void handleLinksAndAccess(List<T> entityList, List<String> fields, boolean deep) {
//        boolean generateLinks = hasHref(fields);
//
//        if (generateLinks) {
//            linkService.generateLinks(entityList, deep);
//        }
//    }
//
//    private boolean fieldsContains(String match, List<String> fields) {
//        for (String field : fields) {
//            // for now assume href/access if * or preset is requested
//            if (field.contains(match) || field.equals("*") || field.startsWith(":")) {
//                return true;
//            }
//        }
//
//        return false;
//    }
//
//    // --------------------------------------------------------------------------
//    // Reflection helpers
//    // --------------------------------------------------------------------------
//
//    private String entityName;
//
//    private String entitySimpleName;
//
//    protected final String getEntityName() {
//        if (entityName == null) {
//            entityName = getEntityClass().getName();
//        }
//
//        return entityName;
//    }
//
//    protected final String getEntitySimpleName() {
//        if (entitySimpleName == null) {
//            entitySimpleName = getEntityClass().getSimpleName();
//        }
//
//        return entitySimpleName;
//    }
//
//    protected final List<T> getEntity(String uid) {
//        return getEntity(uid, NO_WEB_OPTIONS);
//    }
//
//    protected List<T> getEntity(String uid, WebOptions options) {
//        ArrayList<T> list = new ArrayList<>();
//        getEntity(uid, getEntityClass()).ifPresent(list::add);
//        return list; // TODO consider ACL
//    }
//
//    protected final <E extends IdentifiableObject> java.util.Optional<E> getEntity(String uid, Class<E> entityType) {
//        return java.util.Optional.ofNullable(manager.getNoAcl(entityType, uid));
//    }
//
//    protected final Schema getSchema(Class<?> klass) {
//        return schemaService.getDynamicSchema(klass);
//    }
//
//    // --------------------------------------------------------------------------
//    // Helpers
//    // --------------------------------------------------------------------------
//
//    protected final void setUserContext(TranslateParams translateParams) {
//        setUserContext(currentUserService.getCurrentUser(), translateParams);
//    }
//
//    protected final void setUserContext(User user, TranslateParams translateParams) {
//        Locale dbLocale = getLocaleWithDefault(translateParams);
//        UserContext.setUser(user);
//        UserContext.setUserSetting(UserSettingKey.DB_LOCALE, dbLocale);
//    }
//
//    private Locale getLocaleWithDefault(TranslateParams translateParams) {
//        return translateParams.isTranslate()
//            ? translateParams.getLocaleWithDefault(
//            (Locale) userSettingService.getUserSetting(UserSettingKey.DB_LOCALE))
//            : null;
//    }
//
//    protected final Pagination getPaginationData(WebOptions options) {
//        return PaginationUtils.getPaginationData(options);
//    }
//
//    private InclusionStrategy.Include getInclusionStrategy(String inclusionStrategy) {
//        if (inclusionStrategy != null) {
//            Optional<Include> optional = Enums.getIfPresent(InclusionStrategy.Include.class,
//                inclusionStrategy);
//
//            if (optional.isPresent()) {
//                return optional.get();
//            }
//        }
//
//        return InclusionStrategy.Include.NON_NULL;
//    }
}
