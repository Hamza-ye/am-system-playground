package org.nmcpye.activitiesmanagement.extended.common.adapter;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.nmcpye.activitiesmanagement.domain.Authority;

import java.io.IOException;

public class AuthorityDeserializer extends JsonDeserializer<Authority> {

    @Override
    public Authority deserialize(JsonParser jp, DeserializationContext context) throws IOException {
        String authorityName = jp.readValueAs(String.class);

        return authorityName == null ? null : new Authority(authorityName);
    }
}
