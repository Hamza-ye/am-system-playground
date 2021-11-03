package org.nmcpye.activitiesmanagement.extended.common.adapter;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;
import org.nmcpye.activitiesmanagement.domain.period.PeriodType;

public class JacksonPeriodTypeDeserializer extends JsonDeserializer<PeriodType> {

    @Override
    public PeriodType deserialize(JsonParser jp, DeserializationContext context) throws IOException {
        String periodTypeString = jp.readValueAs(String.class);

        return periodTypeString == null ? null : PeriodType.getPeriodTypeByName(periodTypeString);
    }
}
