package org.nmcpye.activitiesmanagement.extended.web;

import com.csvreader.CsvWriter;
import org.nmcpye.activitiesmanagement.extended.systemmodule.system.util.CsvUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UncheckedIOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.*;

import static java.util.stream.Collectors.joining;

/**
 * CSV writer specifically tailored to the Gist API needs.
 */
public final class CsvBuilder {
    public enum Preference {
        SKIP_HEADERS,
        EXPLICIT_NULLS
    }

    private final CsvWriter out;

    private final EnumSet<Preference> preferences = EnumSet.noneOf(Preference.class);

    private DateTimeFormatter dateTimeFormatter;

    public CsvBuilder(PrintWriter out) {
        this(out, CsvUtils.DELIMITER);
    }

    public CsvBuilder(PrintWriter out, char delimiter) {
        this.out = new CsvWriter(out, delimiter);
        withLocale(Locale.getDefault());
    }

    public CsvBuilder with(Preference preference) {
        preferences.add(preference);
        return this;
    }

    public CsvBuilder without(Preference preference) {
        preferences.remove(preference);
        return this;
    }

    public CsvBuilder withLocale(Locale locale) {
        this.dateTimeFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)
            .withLocale(locale == null ? Locale.getDefault() : locale);
        return this;
    }

    public CsvBuilder nullAsEmpty() {
        return without(Preference.EXPLICIT_NULLS);
    }

    public CsvBuilder nullAsNull() {
        return with(Preference.EXPLICIT_NULLS);
    }

    public CsvBuilder skipHeaders(boolean skipHeaders) {
        return skipHeaders ? with(Preference.SKIP_HEADERS) : without(Preference.SKIP_HEADERS);
    }

    public void toRows(List<String> fields, List<?> values) {
        try {
            if (!preferences.contains(Preference.SKIP_HEADERS)) {
                for (String header : fields) {
                    out.write(header);
                }
                out.endRecord();
            }
            final int columns = fields.size();
            for (Object value : values) {
                if (value instanceof Object[]) {
                    Object[] row = (Object[]) value;
                    for (int c = 0; c < columns; c++) {
                        out.write(toCsvValue(row[c]));
                    }
                } else {
                    out.write(toCsvValue(value));
                }
                out.endRecord();
            }
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }

    public boolean isNullEmpty() {
        return !preferences.contains(Preference.EXPLICIT_NULLS);
    }

    private String toCsvValue(Object value) {
        if (value == null) {
            return isNullEmpty() ? "" : "null";
        }
        if (value instanceof Object[]) {
            return toCsvValue((Object[]) value);
        }
        if (value instanceof Collection<?>) {
            return toCsvValue((Collection<?>) value);
        }
        if (value instanceof Date) {
            return dateTimeFormatter
                .format(LocalDateTime.ofInstant(Instant.ofEpochMilli(((Date) value).getTime()), ZoneOffset.UTC));
        }
        return value.toString();
    }

    private String toCsvValue(Object[] items) {
        if (items.length == 0) {
            return toCsvValue((Object) null);
        }
        if (items.length == 1) {
            return toCsvValue(items[0]);
        }
        if (items.length < 10 && items[0] instanceof Number) {
            return Arrays.stream(items).map(String::valueOf).collect(joining(" "));
        }
        return "(" + items.length + " elements)";
    }

    private String toCsvValue(Collection<?> items) {
        if (items.isEmpty()) {
            return toCsvValue((Object) null);
        }
        if (items.size() == 1) {
            return toCsvValue(items.iterator().next());
        }
        if (items.size() < 10 && items.iterator().next() instanceof Number) {
            return items.stream().map(String::valueOf).collect(joining(" "));
        }
        return "(" + items.size() + " elements)";
    }
}
