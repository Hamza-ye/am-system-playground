package org.nmcpye.activitiesmanagement.extended.web.rest;

import com.google.common.collect.Lists;
import org.nmcpye.activitiesmanagement.extended.dxf2module.webmessage.WebMessage;
import org.nmcpye.activitiesmanagement.extended.feedback.ErrorReport;
import org.nmcpye.activitiesmanagement.extended.render.RenderService;
import org.nmcpye.activitiesmanagement.extended.schema.Property;
import org.nmcpye.activitiesmanagement.extended.schemamodule.Schema;
import org.nmcpye.activitiesmanagement.extended.schemamodule.SchemaService;
import org.nmcpye.activitiesmanagement.extended.schemamodule.Schemas;
import org.nmcpye.activitiesmanagement.extended.schemamodule.validation.SchemaValidator;
import org.nmcpye.activitiesmanagement.extended.web.constants.ApiEndPoint;
import org.nmcpye.activitiesmanagement.extended.web.service.ContextService;
import org.nmcpye.activitiesmanagement.extended.web.service.LinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static org.nmcpye.activitiesmanagement.extended.dxf2module.webmessage.WebMessageUtils.errorReports;

@Controller
@RequestMapping(ApiEndPoint.API_END_POINT + "/schemas")
public class SchemaController {

    private final SchemaService schemaService;

    private final SchemaValidator schemaValidator;

    private final RenderService renderService;

    private final LinkService linkService;

    private final ContextService contextService;

    @Autowired
    public SchemaController(SchemaService schemaService, SchemaValidator schemaValidator,
                            RenderService renderService, LinkService linkService,
                            ContextService contextService) {

        this.schemaService = schemaService;
        this.schemaValidator = schemaValidator;
        this.renderService = renderService;
        this.linkService = linkService;
        this.contextService = contextService;
    }

    @GetMapping
    public @ResponseBody
    Schemas getSchemas() {
        List<String> fields = Lists.newArrayList(contextService.getParameterValues("fields"));

        if (fields.isEmpty()) {
            fields.add("*");
        }

        Schemas schemas = new Schemas(schemaService.getSortedSchemas());
        linkService.generateSchemaLinks(schemas.getSchemas());

        return schemas;
    }

    @GetMapping("/{type}")
    public @ResponseBody
    Schema getSchema(@PathVariable String type) {
        List<String> fields = Lists.newArrayList(contextService.getParameterValues("fields"));

        if (fields.isEmpty()) {
            fields.add("*");
        }

        Schema schema = getSchemaFromType(type);

        if (schema != null) {
            linkService.generateSchemaLinks(schema);

            return schema;
        }

        throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "Type " + type + " does not exist.");
    }

    @RequestMapping(value = "/{type}", method = {RequestMethod.POST, RequestMethod.PUT}, consumes = {
        MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ResponseBody
    public WebMessage validateSchema(@PathVariable String type, HttpServletRequest request,
                                     HttpServletResponse response)
        throws IOException {
        Schema schema = getSchemaFromType(type);

        if (schema == null) {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "Type " + type + " does not exist.");
        }

        Object object = renderService.fromJson(request.getInputStream(), schema.getKlass());
        List<ErrorReport> validationViolations = schemaValidator.validate(object);

        return errorReports(validationViolations);
    }

    @GetMapping("/{type}/{property}")
    public @ResponseBody
    Property getSchemaProperty(@PathVariable String type, @PathVariable String property) {
        Schema schema = getSchemaFromType(type);

        if (schema == null) {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "Type " + type + " does not exist.");
        }

        if (schema.haveProperty(property)) {
            return schema.getProperty(property);
        }

        throw new HttpClientErrorException(HttpStatus.NOT_FOUND,
            "Property " + property + " does not exist on type " + type + ".");
    }

    private Schema getSchemaFromType(String type) {
        Schema schema = schemaService.getSchemaBySingularName(type);

        if (schema == null) {
            try {
                schema = schemaService.getSchema(Class.forName(type));
            } catch (ClassNotFoundException ignored) {
            }
        }

        return schema;
    }
}
