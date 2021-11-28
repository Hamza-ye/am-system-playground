package org.nmcpye.activitiesmanagement.extended.translation;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.google.common.base.MoreObjects;
import org.nmcpye.activitiesmanagement.extended.common.DxfNamespaces;

import java.io.Serializable;
import java.util.Objects;

@JsonRootName(value = "translations", namespace = DxfNamespaces.DXF_2_0)
public class Translation
    implements Serializable {
    private String locale;

    private String property;

    private String value;

    public Translation() {
    }

    public Translation(String locale, String property, String value) {
        this.locale = locale;
        this.property = property;
        this.value = value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(locale, property, value);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        final Translation other = (Translation) obj;

        return Objects.equals(this.locale, other.locale)
            && Objects.equals(this.property, other.property)
            && Objects.equals(this.value, other.value);
    }

    /**
     * Creates a cache key.
     *
     * @param locale   the locale string, i.e. Locale.toString().
     * @param property the translation property.
     * @return a unique cache key valid for a given translated objects, or null
     * if either locale or property is null.
     */
    public static String getCacheKey(String locale, String property) {
        return locale != null && property != null ? (locale + property) : null;
    }

    // -------------------------------------------------------------------------------
    // Accessors
    // -------------------------------------------------------------------------------

    @JsonProperty
    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    @JsonProperty
    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    @JsonProperty
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
            .add("locale", locale)
            .add("property", property)
            .add("value", value)
            .toString();
    }
}
