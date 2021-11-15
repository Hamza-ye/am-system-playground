package org.nmcpye.activitiesmanagement.extended.servicecoremodule.common;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.UnaryOperator;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;

/**
 * Meant as a wrapper around query parameters to provide convenient read access
 * to a mixed map with simple values and lists of simple values as they occur in
 * a HTTP request.
 * <p>
 * Internally it is based on accessor functions so that it can be bound to a
 * {@link Map} or a HTTP request object methods while allowing to be defined in
 * a module that is not dependent on web dependencies.
 */
public final class NamedParams {

    private final UnaryOperator<String> uni;

    private final Function<String, String[]> multi;

    public NamedParams(Map<String, String> params) {
        this(params::get);
    }

    public NamedParams(UnaryOperator<String> uni) {
        this(uni, name -> {
            String value = uni.apply(name);
            return value == null ? new String[0] : new String[]{value};
        });
    }

    public NamedParams(UnaryOperator<String> uni, Function<String, String[]> multi) {
        this.uni = uni;
        this.multi = multi;
    }

    public String getString(String name) {
        return uni.apply(name);
    }

    public String getString(String name, String defaultValue) {
        String value = getString(name);
        return value == null ? defaultValue : value;
    }

    public List<String> getStrings(String name) {
        return getStrings(name, ",");
    }

    public List<String> getStrings(String name, String splitRegex) {
        String[] value = multi.apply(name);
        if (value == null || value.length == 0) {
            return emptyList();
        }
        return value.length == 1 ? asList(value[0].split(splitRegex)) : asList(value);
    }

    public int getInt(String name, int defaultValue) {
        return parsedUni(name, defaultValue, Integer::parseInt);
    }

    public boolean getBoolean(String name, boolean defaultValue) {
        String value = uni.apply(name);
        return value == null ? defaultValue : getBoolean(name);
    }

    public boolean getBoolean(String name) {
        String value = uni.apply(name);
        return "true".equalsIgnoreCase(value) || value != null && value.isEmpty();
    }

    public <E extends Enum<E>> E getEnum(String name, E defaultValue) {
        return getEnum(name, defaultValue.getDeclaringClass(), defaultValue);
    }

    public <E extends Enum<E>> E getEnum(String name, Class<E> type, E defaultValue) {
        return parsedUni(name, defaultValue,
            constant -> Enum.valueOf(type, constant.toUpperCase().replace('-', '_')));
    }

    private <T> T parsedUni(String name, T defaultValue, Function<String, T> parser) {
        String value = getString(name);
        try {
            return value == null ? defaultValue : parser.apply(value);
        } catch (IllegalArgumentException ex) {
            throw ex;
        } catch (RuntimeException ex) {
            throw new IllegalArgumentException(ex);
        }
    }
}
