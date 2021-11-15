package org.nmcpye.activitiesmanagement.extended.web.rest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.undertow.util.BadRequestException;
import org.nmcpye.activitiesmanagement.extended.common.IdentifiableObject;
import org.nmcpye.activitiesmanagement.extended.common.PrimaryKeyObject;
import org.nmcpye.activitiesmanagement.extended.schema.Property;
import org.nmcpye.activitiesmanagement.extended.schemamodule.Schema;
import org.nmcpye.activitiesmanagement.extended.schemamodule.SchemaService;
import org.nmcpye.activitiesmanagement.extended.servicecoremodule.common.NamedParams;
import org.nmcpye.activitiesmanagement.extended.servicecoremodule.gist.GistAutoType;
import org.nmcpye.activitiesmanagement.extended.servicecoremodule.gist.GistQuery;
import org.nmcpye.activitiesmanagement.extended.servicecoremodule.gist.GistQuery.Comparison;
import org.nmcpye.activitiesmanagement.extended.servicecoremodule.gist.GistQuery.Filter;
import org.nmcpye.activitiesmanagement.extended.servicecoremodule.gist.GistQuery.Owner;
import org.nmcpye.activitiesmanagement.extended.servicecoremodule.gist.GistService;
import org.nmcpye.activitiesmanagement.extended.web.CsvBuilder;
import org.nmcpye.activitiesmanagement.extended.web.JsonBuilder;
import org.nmcpye.activitiesmanagement.extended.web.rest.exception.NotFoundException;
import org.nmcpye.activitiesmanagement.extended.web.utils.ContextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Locale;

import static java.util.Arrays.asList;
import static org.springframework.http.CacheControl.noCache;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * Base controller for APIs that only want to offer read-only access though Gist
 * API.
 */
public abstract class AbstractGistReadOnlyController<T extends PrimaryKeyObject> {

    @Autowired
    protected ObjectMapper jsonMapper;

    @Autowired
    protected SchemaService schemaService;

    @Autowired
    private GistService gistService;

    // --------------------------------------------------------------------------
    // GET Gist
    // --------------------------------------------------------------------------

    @GetMapping(value = "/{uid}/gist", produces = APPLICATION_JSON_VALUE)
    public @ResponseBody
    ResponseEntity<JsonNode> getObjectGist(
        @PathVariable("uid") String uid,
        HttpServletRequest request, HttpServletResponse response)
        throws NotFoundException {
        return gistToJsonObjectResponse(uid, createGistQuery(request, getEntityClass(), GistAutoType.L)
            .withFilter(new Filter("id", Comparison.EQ, uid)));
    }

    @GetMapping(value = "/gist", produces = APPLICATION_JSON_VALUE)
    public @ResponseBody
    ResponseEntity<JsonNode> getObjectListGist(
        HttpServletRequest request, HttpServletResponse response) {
        return gistToJsonArrayResponse(request, createGistQuery(request, getEntityClass(), GistAutoType.S),
            getSchema());
    }

    @GetMapping(value = "/gist", produces = "text/csv")
    public @ResponseBody
    void getObjectListGistAsCsv(HttpServletRequest request, HttpServletResponse response)
        throws IOException {
        gistToCsvResponse(response, createGistQuery(request, getEntityClass(), GistAutoType.S));
    }

    @GetMapping(value = "/{uid}/{property}/gist", produces = APPLICATION_JSON_VALUE)
    public @ResponseBody
    ResponseEntity<JsonNode> getObjectPropertyGist(
        @PathVariable("uid") String uid,
        @PathVariable("property") String property,
        HttpServletRequest request, HttpServletResponse response)
        throws Exception {
        Property objProperty = getSchema().getProperty(property);

        if (objProperty == null) {
            throw new BadRequestException("No such property: " + property);
        }

        if (!objProperty.isCollection()) {
            return gistToJsonObjectResponse(uid, createGistQuery(request, getEntityClass(), GistAutoType.L)
                .withFilter(new Filter("id", Comparison.EQ, uid))
                .withField(property));
        }

        @SuppressWarnings("unchecked")
        GistQuery query = createGistQuery(request, (Class<IdentifiableObject>) objProperty.getItemKlass(),
            GistAutoType.M)
            .withOwner(Owner.builder()
                .id(uid)
                .type(getEntityClass())
                .collectionProperty(property).build());

        return gistToJsonArrayResponse(request, query,
            schemaService.getDynamicSchema(objProperty.getItemKlass()));
    }

    private static GistQuery createGistQuery(HttpServletRequest request,
                                             Class<? extends PrimaryKeyObject> elementType, GistAutoType autoDefault) {
        NamedParams params = new NamedParams(request::getParameter, request::getParameterValues);
        Locale translationLocale = !params.getString("locale", "").isEmpty()
            ? Locale.forLanguageTag(params.getString("locale"))
            : Locale.ENGLISH;
        return GistQuery.builder()
            .elementType(elementType)
            .autoType(params.getEnum("auto", autoDefault))
            .contextRoot(ContextUtils.getRootPath(request))
            .translationLocale(translationLocale)
            .build()
            .with(params);
    }

    private ResponseEntity<JsonNode> gistToJsonObjectResponse(String uid, GistQuery query)
        throws NotFoundException {
        if (query.isDescribe()) {
            return gistDescribeToJsonObjectResponse(query);
        }
        query = gistService.plan(query);
        List<?> elements = gistService.gist(query);
        JsonNode body = new JsonBuilder(jsonMapper).skipNullOrEmpty().toArray(query.getFieldNames(), elements);
        if (body.isEmpty()) {
            throw NotFoundException.notFoundUid(uid);
        }
        return ResponseEntity.ok().cacheControl(noCache().cachePrivate()).body(body.get(0));
    }

    private ResponseEntity<JsonNode> gistToJsonArrayResponse(HttpServletRequest request,
                                                             GistQuery query, Schema schema) {
        if (query.isDescribe()) {
            return gistDescribeToJsonObjectResponse(query);
        }
        query = gistService.plan(query);
        List<?> elements = gistService.gist(query);
        JsonBuilder responseBuilder = new JsonBuilder(jsonMapper);
        JsonNode body = responseBuilder.skipNullOrEmpty().toArray(query.getFieldNames(), elements);
        if (!query.isHeadless()) {
            body = responseBuilder.toObject(asList("pager", schema.getPlural()),
                gistService.pager(query, elements, request.getParameterMap()), body);
        }
        return ResponseEntity.ok().cacheControl(noCache().cachePrivate()).body(body);
    }

    private ResponseEntity<JsonNode> gistDescribeToJsonObjectResponse(GistQuery query) {
        return ResponseEntity.ok().cacheControl(noCache().cachePrivate()).body(
            new JsonBuilder(jsonMapper).skipNullMembers().toObject(gistService.describe(query)));
    }

    private void gistToCsvResponse(HttpServletResponse response, GistQuery query)
        throws IOException {
        query = gistService.plan(query).toBuilder().references(false).build();
        response.addHeader(HttpHeaders.CONTENT_TYPE, "text/csv");
        new CsvBuilder(response.getWriter())
            .withLocale(query.getTranslationLocale())
            .skipHeaders(query.isHeadless())
            .toRows(query.getFieldNames(), gistService.gist(query));
    }

    // --------------------------------------------------------------------------
    // Reflection helpers
    // --------------------------------------------------------------------------

    private Class<T> entityClass;

//    @SuppressWarnings("unchecked")
//    protected final Class<T> getEntityClass() {
//        if (entityClass == null) {
//            Type[] actualTypeArguments = ((ParameterizedType) getClass().getGenericSuperclass())
//                .getActualTypeArguments();
//            entityClass = (Class<T>) actualTypeArguments[0];
//        }
//
//        return entityClass;
//    }

    @SuppressWarnings("unchecked")
    protected Class<T> getEntityClass() {
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
