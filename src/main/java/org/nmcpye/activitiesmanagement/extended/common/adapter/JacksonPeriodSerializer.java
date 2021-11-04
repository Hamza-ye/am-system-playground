package org.nmcpye.activitiesmanagement.extended.common.adapter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.nmcpye.activitiesmanagement.domain.period.Period;

import java.io.IOException;

public class JacksonPeriodSerializer extends JsonSerializer<Period> {

    @Override
    public void serialize(Period value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        if (value != null && value.getIsoDate() != null) {
            jgen.writeStartObject();
            jgen.writeStringField("id", value.getIsoDate());

            if (value.getName() != null) {
                jgen.writeStringField("name", value.getName());
            }

            jgen.writeEndObject();
        }
    }
}
