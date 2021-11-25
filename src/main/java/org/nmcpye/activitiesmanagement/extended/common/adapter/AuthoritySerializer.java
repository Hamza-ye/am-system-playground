package org.nmcpye.activitiesmanagement.extended.common.adapter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.nmcpye.activitiesmanagement.domain.Authority;

import java.io.IOException;

public class AuthoritySerializer extends JsonSerializer<Authority> {

    @Override
    public void serialize(Authority value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        if (value != null) {
            jgen.writeString(value.getName());
        }
    }
}
