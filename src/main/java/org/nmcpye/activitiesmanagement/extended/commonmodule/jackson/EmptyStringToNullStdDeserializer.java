package org.nmcpye.activitiesmanagement.extended.commonmodule.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.springframework.util.StringUtils;

import java.io.IOException;

public class EmptyStringToNullStdDeserializer extends JsonDeserializer<String> {

    @Override
    public String deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        String result = parser.getValueAsString();

        if (StringUtils.isEmpty(result)) {
            return null;
        }

        return result;
    }
}
