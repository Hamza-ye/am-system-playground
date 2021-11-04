package org.nmcpye.activitiesmanagement.extended.commonmodule.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.nmcpye.activitiesmanagement.extended.util.DateUtils;

import java.io.IOException;
import java.util.Date;

public class WriteDateStdSerializer extends JsonSerializer<Date> {

    @Override
    public void serialize(Date date, JsonGenerator generator, SerializerProvider provider) throws IOException {
        generator.writeString(DateUtils.getIso8601NoTz(date));
    }
}
