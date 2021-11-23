package org.nmcpye.activitiesmanagement.extended.web.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.nmcpye.activitiesmanagement.domain.User;
import org.nmcpye.activitiesmanagement.extended.common.IdentifiableObject;
import org.nmcpye.activitiesmanagement.extended.common.IdentifiableObjects;
import org.nmcpye.activitiesmanagement.extended.dxf2module.webmessage.WebMessageException;
import org.nmcpye.activitiesmanagement.extended.dxf2module.webmessage.WebMessageUtils;
import org.nmcpye.activitiesmanagement.extended.hibernatemodule.hibernate.exception.UpdateAccessDeniedException;
import org.nmcpye.activitiesmanagement.extended.patch.Patch;
import org.nmcpye.activitiesmanagement.extended.patch.PatchParams;
import org.nmcpye.activitiesmanagement.extended.patch.PatchService;
import org.nmcpye.activitiesmanagement.extended.render.RenderService;
import org.nmcpye.activitiesmanagement.extended.schema.Property;
import org.nmcpye.activitiesmanagement.extended.schemamodule.validation.SchemaValidator;
import org.nmcpye.activitiesmanagement.extended.web.webdomain.WebOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.nmcpye.activitiesmanagement.extended.dxf2module.webmessage.WebMessageUtils.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_XML_VALUE;

/**
 * Created by Hamza on 05/11/2021.
 */
public abstract class AbstractCrudController<T extends IdentifiableObject>
    extends AbstractFullReadOnlyController<T> {

    @Autowired
    protected SchemaValidator schemaValidator;

    @Autowired
    protected RenderService renderService;

    @Autowired
    protected PatchService patchService;

    @PatchMapping(value = "/{uid}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void partialUpdateObject(
        @PathVariable("uid") String pvUid, @RequestParam Map<String, String> rpParameters,
        HttpServletRequest request)
        throws Exception {
        WebOptions options = new WebOptions(rpParameters);
        List<T> entities = getEntity(pvUid, options);

        if (entities.isEmpty()) {
            throw new WebMessageException(notFound(getEntityClass(), pvUid));
        }

        T persistedObject = entities.get(0);

        User user = userService.getUserWithAuthorities().orElse(null);

        if (!aclService.canUpdate(user, persistedObject)) {
            throw new UpdateAccessDeniedException("You don't have the proper permissions to update this object.");
        }

        Patch patch = diff(request);

        prePatchEntity(persistedObject);
        patchService.apply(patch, persistedObject);
        validateAndThrowErrors(() -> schemaValidator.validate(persistedObject));
        manager.update(persistedObject);
        postPatchEntity(persistedObject);
    }

    private Patch diff(HttpServletRequest request)
        throws IOException,
        WebMessageException {
        ObjectMapper mapper = isJson(request) ? jsonMapper : null;
        if (mapper == null) {
            throw new WebMessageException(badRequest("Unknown payload format."));
        }
        return patchService.diff(new PatchParams(mapper.readTree(request.getInputStream())));
    }

    @RequestMapping(value = "/{uid}/{property}", method = {RequestMethod.PUT, RequestMethod.PATCH})
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void updateObjectProperty(
        @PathVariable("uid") String pvUid, @PathVariable("property") String pvProperty, @RequestParam Map<String, String> rpParameters,
        HttpServletRequest request) throws Exception {
        WebOptions options = new WebOptions(rpParameters);

        List<T> entities = getEntity(pvUid, options);

        if (entities.isEmpty()) {
            throw new WebMessageException(WebMessageUtils.notFound(getEntityClass(), pvUid));
        }

        if (!getSchema().haveProperty(pvProperty)) {
            throw new WebMessageException(WebMessageUtils.notFound("Property " + pvProperty + " does not exist on " + getEntityName()));
        }

        Property property = getSchema().getProperty(pvProperty);
        T persistedObject = entities.get(0);

        if (!aclService.canUpdate(userService.getUserWithAuthorities().orElse(null), persistedObject)) {
            throw new UpdateAccessDeniedException("You don't have the proper permissions to update this object.");
        }

        if (!property.isWritable()) {
            throw new UpdateAccessDeniedException("This property is read-only.");
        }

        T object = deserialize(request);

        if (object == null) {
            throw new WebMessageException(WebMessageUtils.badRequest("Unknown payload format."));
        }

        Object value = property.getGetterMethod().invoke(object);

        property.getSetterMethod().invoke(persistedObject, value);

        manager.update(persistedObject);
        postPatchEntity(persistedObject);
    }
    // --------------------------------------------------------------------------
    // Hooks
    // --------------------------------------------------------------------------

    protected T deserializeJsonEntity(HttpServletRequest request)
        throws IOException {
        return renderService.fromJson(request.getInputStream(), getEntityClass());
    }

    protected void preCreateEntity(T entity)
        throws Exception {
    }

    protected void postCreateEntity(T entity) {
    }

    protected void preUpdateEntity(T entity, T newEntity)
        throws Exception {
    }

    protected void postUpdateEntity(T entity) {
    }

    protected void preDeleteEntity(T entity)
        throws Exception {
    }

    protected void postDeleteEntity(String entityUid) {
    }

    protected void prePatchEntity(T entity, T newEntity)
        throws Exception {
    }

    protected void prePatchEntity(T newEntity)
        throws Exception {
    }

    protected void postPatchEntity(T entity) {
    }

    protected void preUpdateItems(T entity, IdentifiableObjects items)
        throws Exception {
    }

    protected void postUpdateItems(T entity, IdentifiableObjects items) {
    }

    // --------------------------------------------------------------------------
    // Helpers
    // --------------------------------------------------------------------------

    /**
     * Deserializes a payload from the request, handles JSON/XML payloads
     *
     * @param request HttpServletRequest from current session
     * @return Parsed entity or null if invalid type
     */
    private T deserialize(HttpServletRequest request)
        throws IOException {
        String type = request.getContentType();
        type = !StringUtils.isEmpty(type) ? type : APPLICATION_JSON_VALUE;

        // allow type to be overridden by path extension
        if (request.getPathInfo().endsWith(".json")) {
            type = APPLICATION_JSON_VALUE;
        } else if (request.getPathInfo().endsWith(".xml")) {
            type = APPLICATION_XML_VALUE;
        }

        if (isCompatibleWith(type, MediaType.APPLICATION_JSON)) {
            return renderService.fromJson(request.getInputStream(), getEntityClass());
        }

        return null;
    }

    /**
     * Are we receiving JSON data?
     *
     * @param request HttpServletRequest from current session
     * @return true if JSON compatible
     */
    private boolean isJson(HttpServletRequest request) {
        String type = request.getContentType();
        type = !StringUtils.isEmpty(type) ? type : APPLICATION_JSON_VALUE;

        // allow type to be overridden by path extension
        if (request.getPathInfo().endsWith(".json")) {
            type = APPLICATION_JSON_VALUE;
        }

        return isCompatibleWith(type, MediaType.APPLICATION_JSON);
    }

    private boolean isCompatibleWith(String type, MediaType mediaType) {
        try {
            return !StringUtils.isEmpty(type) && MediaType.parseMediaType(type).isCompatibleWith(mediaType);
        } catch (Exception ignored) {
        }

        return false;
    }
}
