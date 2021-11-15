package org.nmcpye.activitiesmanagement.extended.servicecoremodule.gist;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.nmcpye.activitiesmanagement.domain.User;
import org.nmcpye.activitiesmanagement.extended.common.IdentifiableObject;
import org.nmcpye.activitiesmanagement.extended.schemamodule.RelativePropertyContext;
import org.nmcpye.activitiesmanagement.extended.schemamodule.Schema;
import org.nmcpye.activitiesmanagement.extended.schemamodule.SchemaService;
import org.nmcpye.activitiesmanagement.extended.serviceaclmodule.security.acl.AclService;
import org.nmcpye.activitiesmanagement.extended.user.UserService;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.net.URI;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

@Service
public class DefaultGistService implements GistService {

    /**
     * Instead of an actual date value users may use string {@code now} to
     * always get current moment as time for a {@link Date} value.
     */
    private static final String NOW_PARAMETER_VALUE = "now";

    private final SchemaService schemaService;

    private final UserService userService;

    private final org.nmcpye.activitiesmanagement.service.UserService currentUserService;

    private final AclService aclService;

    private final ObjectMapper jsonMapper;

    @PersistenceContext
    EntityManager entityManager;

    public DefaultGistService(SchemaService schemaService,
                              UserService userService,
                              org.nmcpye.activitiesmanagement.service.UserService currentUserService,
                              AclService aclService, ObjectMapper jsonMapper) {
        this.schemaService = schemaService;
        this.userService = userService;
        this.currentUserService = currentUserService;
        this.aclService = aclService;
        this.jsonMapper = jsonMapper;
    }

    private Session getSession() {
        return entityManager.unwrap(Session.class);
    }

    @Override
    public GistQuery plan(GistQuery query) {
        return new GistPlanner(query, createPropertyContext(query), createGistAccessControl()).plan();
    }

    @Override
    public List<?> gist(GistQuery query) {
        GistAccessControl access = createGistAccessControl();
        RelativePropertyContext context = createPropertyContext(query);
        new GistValidator(query, context, access).validateQuery();
        GistBuilder queryBuilder = GistBuilder.createFetchBuilder(query, context, access,
            this::getUserGroupIdsByUserId);
        List<Object[]> rows = fetchWithParameters(query, queryBuilder,
            getSession().createQuery(queryBuilder.buildFetchHQL(), Object[].class));
        queryBuilder.transform(rows);
        return rows;
    }

    @Override
    public GistPager pager(GistQuery query, List<?> rows, Map<String, String[]> params) {
        int page = 1 + (query.getPageOffset() / query.getPageSize());
        Schema schema = schemaService.getDynamicSchema(query.getElementType());
        String prev = null;
        String next = null;
        Integer total = null;
        if (query.isTotal()) {
            if (rows.size() < query.getPageSize() && !rows.isEmpty()) {
                // NB. only do this when rows are returned as otherwise the page
                // simply might not exist which leads to zero rows
                total = query.getPageOffset() + rows.size();
            } else {
                GistAccessControl access = createGistAccessControl();
                RelativePropertyContext context = createPropertyContext(query);
                GistBuilder countBuilder = GistBuilder.createCountBuilder(query, context, access,
                    this::getUserGroupIdsByUserId);
                total = countWithParameters(countBuilder,
                    getSession().createQuery(countBuilder.buildCountHQL(), Long.class));
            }
        }
        if (schema.haveApiEndpoint()) {
            URI baseURL = GistPager.computeBaseURL(query, params, schemaService::getDynamicSchema);
            if (page > 1) {
                prev = UriComponentsBuilder.fromUri(baseURL).replaceQueryParam("page", page - 1).build().toString();
            }
            if (total != null && query.getPageOffset() + rows.size() < total
                || total == null && query.getPageSize() == rows.size()) {
                next = UriComponentsBuilder.fromUri(baseURL).replaceQueryParam("page", page + 1).build().toString();
            }
        }
        return new GistPager(page, query.getPageSize(), total, prev, next);
    }

    @Override
    public Map<String, ?> describe(GistQuery unplanned) {
        GistAccessControl access = createGistAccessControl();

        GistQuery planned = unplanned;
        Map<String, Object> description = new LinkedHashMap<>();
        description.put("unplanned", unplanned);
        try {
            planned = plan(unplanned);
        } catch (RuntimeException ex) {
            description.put("error.type", ex.getClass().getName());
            description.put("error.message", ex.getMessage());
            description.put("status", "planning-failed");
            return description;
        }

        RelativePropertyContext context = createPropertyContext(planned);

        // describe query
        description.put("planned.summary", planned.getFieldNames());
        description.put("planned", planned);

        // describe validation
        try {
            new GistValidator(planned, context, access).validateQuery();
        } catch (RuntimeException ex) {
            description.put("error.type", ex.getClass().getName());
            description.put("error.message", ex.getMessage());
            description.put("status", "validation-failed");
            return description;
        }

        // describe HQL queries
        if (access.canReadHQL()) {
            if (planned.isTotal()) {
                description.put("hql.count",
                    GistBuilder.createCountBuilder(planned, context, access, this::getUserGroupIdsByUserId).buildCountHQL());
            }
            GistBuilder fetchBuilder = GistBuilder.createFetchBuilder(planned, context, access, this::getUserGroupIdsByUserId);
            description.put("hql.fetch", fetchBuilder.buildFetchHQL());
            Map<String, Object> params = new LinkedHashMap<>();
            fetchBuilder.addFetchParameters(params::put, this::parseFilterArgument);
            description.put("hql.parameters", params);
        }

        description.put("status", "ok");
        return description;
    }

    private GistAccessControl createGistAccessControl() {
        return new DefaultGistAccessControl(currentUserService.getUserWithAuthorities().orElse(null), aclService, userService, this);
    }

    private RelativePropertyContext createPropertyContext(GistQuery query) {
        return new RelativePropertyContext(query.getElementType(), schemaService::getDynamicSchema);
    }

    private <T> List<T> fetchWithParameters(GistQuery gistQuery, GistBuilder builder, Query<T> query) {
        builder.addFetchParameters(query::setParameter, this::parseFilterArgument);
        query.setMaxResults(Math.max(1, gistQuery.getPageSize()));
        query.setFirstResult(gistQuery.getPageOffset());
        query.setCacheable(false);
        return query.list();
    }

    private int countWithParameters(GistBuilder builder, Query<Long> query) {
        builder.addCountParameters(query::setParameter, this::parseFilterArgument);
        query.setCacheable(false);
        return query.getSingleResult().intValue();
    }

    @SuppressWarnings("unchecked")
    private <T> T parseFilterArgument(String value, Class<T> type) {
        if (type == Date.class && NOW_PARAMETER_VALUE.equals(value)) {
            return (T) new Date();
        }
        String valueAsJson = value;
        if (!(Number.class.isAssignableFrom(type) || type == Boolean.class || type == boolean.class)) {
            valueAsJson = '"' + value + '"';
        }
        try {
            return jsonMapper.readValue(valueAsJson, type);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException(
                String.format("Type %s is not compatible with provided filter value: `%s`", type, value));
        }
    }

    private List<String> getUserGroupIdsByUserId(String userId) {
        User user = userService.getUser(userId);
        if (user.getPerson() != null) {
            return userService.getUser(userId).getPerson().getGroups().stream().map(IdentifiableObject::getUid).collect(toList());
        }
        return Lists.newArrayList();
    }
}
