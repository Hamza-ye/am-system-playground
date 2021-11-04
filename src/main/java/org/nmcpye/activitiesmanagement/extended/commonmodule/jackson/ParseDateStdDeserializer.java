package org.nmcpye.activitiesmanagement.extended.commonmodule.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.nmcpye.activitiesmanagement.extended.util.DateUtils;

import java.io.IOException;
import java.util.Date;

public class ParseDateStdDeserializer extends JsonDeserializer<Date> {

    @Override
    public Date deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        Date date = null;

        try {
            date = DateUtils.parseDate(parser.getValueAsString());
        } catch (Exception ignored) {}

        if (date == null) {
            try {
                date = new Date(parser.getValueAsLong());
            } catch (Exception ignored) {}
        }

        if (date == null) {
            throw new IOException(
                String.format("Invalid date format '%s', only ISO format or UNIX Epoch timestamp is supported.", parser.getValueAsString())
            );
        }

        return date;
    }
}
