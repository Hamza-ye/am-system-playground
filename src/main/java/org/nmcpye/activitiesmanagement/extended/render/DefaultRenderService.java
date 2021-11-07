package org.nmcpye.activitiesmanagement.extended.render;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.util.JSONPObject;
import lombok.extern.slf4j.Slf4j;
import org.nmcpye.activitiesmanagement.extended.common.IdentifiableObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Default implementation that uses Jackson to serialize/deserialize
 */

@Service("org.nmcpye.activitiesmanagement.extended.render.RenderService")
public class DefaultRenderService
    implements RenderService {

    private final Logger log = LoggerFactory.getLogger(DefaultRenderService.class);

    private final ObjectMapper jsonMapper;

//    private final ObjectMapper xmlMapper;

//    private SchemaService schemaService;

    //    public DefaultRenderService(
//        @Qualifier( "jsonMapper" ) ObjectMapper jsonMapper,
//        @Qualifier( "xmlMapper" ) ObjectMapper xmlMapper,
//        SchemaService schemaService )
//    {
//        checkNotNull( jsonMapper );
//        checkNotNull( xmlMapper );
//        checkNotNull( schemaService );
//
//        this.jsonMapper = jsonMapper;
//        this.xmlMapper = xmlMapper;
//        this.schemaService = schemaService;
//    }
    public DefaultRenderService(
        @Qualifier("jsonMapper") ObjectMapper jsonMapper) {
        checkNotNull(jsonMapper);

        this.jsonMapper = jsonMapper;
    }
    //--------------------------------------------------------------------------
    // RenderService
    //--------------------------------------------------------------------------

    @Override
    public void toJson(OutputStream output, Object value)
        throws IOException {
        jsonMapper.writeValue(output, value);
    }

    @Override
    public String toJsonAsString(Object value) {
        try {
            return jsonMapper.writeValueAsString(value);
        } catch (JsonProcessingException ignored) {
            ignored.printStackTrace();
        }

        return null;
    }

    @Override
    public void toJsonP(OutputStream output, Object value, String callback)
        throws IOException {
        if (StringUtils.isEmpty(callback)) {
            callback = "callback";
        }

        jsonMapper.writeValue(output, new JSONPObject(callback, value));
    }

    @Override
    public <T> T fromJson(InputStream input, Class<T> klass)
        throws IOException {
        return jsonMapper.readValue(input, klass);
    }

    @Override
    public <T> T fromJson(String input, Class<T> klass)
        throws IOException {
        return jsonMapper.readValue(input, klass);
    }

    @Override
    public boolean isValidJson(String json)
        throws IOException {
        try {
            jsonMapper.readValue(json, Object.class);
        } catch (JsonParseException | JsonMappingException e) {
            return false;
        }

        return true;
    }

    @Override
    public JsonNode getSystemObject(InputStream inputStream, RenderFormat format) throws IOException {
        ObjectMapper mapper;

        if (RenderFormat.JSON == format) {
            mapper = jsonMapper;
        } else if (RenderFormat.XML == format) {
            throw new IllegalArgumentException("XML format is not supported.");
        } else {
            return null;
        }

        JsonNode rootNode = mapper.readTree(inputStream);

        return rootNode.get("system");
    }

//    @Override
//    @SuppressWarnings("unchecked")
//    public Map<Class<? extends IdentifiableObject>, List<IdentifiableObject>> fromMetadata(InputStream inputStream, RenderFormat format) throws IOException {
//        Map<Class<? extends IdentifiableObject>, List<IdentifiableObject>> map = new HashMap<>();
//
//        ObjectMapper mapper;
//
//        if (RenderFormat.JSON == format) {
//            mapper = jsonMapper;
//        } else if (RenderFormat.XML == format) {
//            throw new IllegalArgumentException("XML format is not supported.");
//        } else {
//            return map;
//        }
//
//        JsonNode rootNode = mapper.readTree(inputStream);
//        Iterator<String> fieldNames = rootNode.fieldNames();
//
//        while (fieldNames.hasNext()) {
//            String fieldName = fieldNames.next();
//            JsonNode node = rootNode.get(fieldName);
//            Schema schema = schemaService.getSchemaByPluralName(fieldName);
//
//            if (schema == null || !schema.isIdentifiableObject()) {
//                log.info("Skipping unknown property '" + fieldName + "'.");
//                continue;
//            }
//
//            if (!schema.isMetadata()) {
//                log.debug("Skipping non-metadata property `" + fieldName + "`.");
//                continue;
//            }
//
//            List<IdentifiableObject> collection = new ArrayList<>();
//
//            for (JsonNode item : node) {
//                IdentifiableObject value = mapper.treeToValue(item, (Class<? extends IdentifiableObject>) schema.getKlass());
//                if (value != null) collection.add(value);
//            }
//
//            map.put((Class<? extends IdentifiableObject>) schema.getKlass(), collection);
//        }
//
//        return map;
//    }
}
