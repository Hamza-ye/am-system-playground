package org.nmcpye.activitiesmanagement.extended.render;

import com.fasterxml.jackson.databind.JsonNode;
import org.nmcpye.activitiesmanagement.extended.common.IdentifiableObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

public interface RenderService
{
    void toJson(OutputStream output, Object value) throws IOException;

    String toJsonAsString(Object value);

    void toJsonP(OutputStream output, Object value, String callback) throws IOException;

    <T> T fromJson(InputStream input, Class<T> klass) throws IOException;

    <T> T fromJson(String input, Class<T> klass) throws IOException;

    boolean isValidJson(String json) throws IOException;

    /**
     * Gets the version from the metadata export
     *
     * @param inputStream Stream to read from
     * @param format      Payload format (only JSON is supported)
     * @return JsonNode object
     * @throws IOException
     */
    JsonNode getSystemObject(InputStream inputStream, RenderFormat format) throws IOException;

//    /**
//     * Parses metadata stream and automatically finds collections of id object based on root properties.
//     * <p>
//     * i.e. A property called "dataElements" would be tried to parsed as a collection of data elements.
//     *
//     * @param inputStream Stream to read from
//     * @param format      Payload format (only JSON is supported)
//     * @return Map of all id object types that were found
//     */
//    Map<Class<? extends IdentifiableObject>, List<IdentifiableObject>> fromMetadata(InputStream inputStream, RenderFormat format) throws IOException;

//    /**
//     * Parses the input stream for the collection of MetadataVersion objects.
//     *
//     * @param inputStream
//     * @param format
//     * @return List of MetadataVersion objects.
//     * @throws IOException
//     */
//    List<MetadataVersion> fromMetadataVersion(InputStream inputStream, RenderFormat format) throws IOException;
}
