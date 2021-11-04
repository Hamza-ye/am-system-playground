package org.nmcpye.activitiesmanagement.extended.common.adapter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.nmcpye.activitiesmanagement.domain.period.PeriodType;

import java.io.IOException;

public class JacksonPeriodTypeSerializer extends JsonSerializer<PeriodType> {

    @Override
    public void serialize(PeriodType value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        if (value != null) {
            jgen.writeString(value.getName());
        }
    }
}
