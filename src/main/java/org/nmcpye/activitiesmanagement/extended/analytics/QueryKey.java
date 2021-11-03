package org.nmcpye.activitiesmanagement.extended.analytics;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;

public class QueryKey {

    private static final char VALUE_SEP = ':';
    private static final char COMPONENT_SEP = '-';

    List<String> keyComponents = new ArrayList<>();

    public QueryKey() {}

    /**
     * Adds a component to this key. Null values are included.
     *
     * @param property the key property.
     * @param value the key component value.
     */
    public QueryKey add(String property, Object value) {
        String keyComponent = property + VALUE_SEP + String.valueOf(value);
        this.keyComponents.add(keyComponent);
        return this;
    }

    /**
     * Adds a component to this key. Null values are included.
     *
     * @param value the key component value.
     */
    public QueryKey add(Object value) {
        this.keyComponents.add(String.valueOf(value));
        return this;
    }

    /**
     * Adds a component to this key. Null values are omitted.
     *
     * @param property the key property.
     * @param value the key component value.
     */
    public QueryKey addIgnoreNull(String property, Object value) {
        if (value != null) {
            this.add(property, value);
        }

        return this;
    }

    /**
     * Adds a component value to this key if the given object is not null, supplied
     * by the given value supplier.
     *
     * @param property the key property.
     * @param object the object to check for null.
     * @param valueSupplier the supplier of the key component value.
     */
    public QueryKey addIgnoreNull(String property, Object object, Supplier<String> valueSupplier) {
        if (object != null) {
            this.addIgnoreNull(property, valueSupplier.get());
        }

        return this;
    }

    /**
     * Returns a plain text key based on the components of this key. Use
     * {@link QueryKey#build()} to obtain a shorter and more usable key.
     */
    public String asPlainKey() {
        return StringUtils.join(keyComponents, COMPONENT_SEP);
    }

    /**
     * Returns a 40-character unique key. The key is a SHA-1 hash of
     * the components of this key.
     */
    public String build() {
        return DigestUtils.sha1Hex(asPlainKey());
    }

    /**
     * Equal to {@link QueryKey#build()}.
     */
    @Override
    public String toString() {
        return build();
    }
}
