package org.nmcpye.activitiesmanagement.extended.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.nmcpye.activitiesmanagement.extended.hibernatemodule.hibernate.jsonb.type.JsonSetBinaryType;
import org.nmcpye.activitiesmanagement.extended.schema.annotation.Gist;
import org.nmcpye.activitiesmanagement.extended.translation.Translation;
import org.nmcpye.activitiesmanagement.extended.user.UserSettingKey;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Base class for translatable object.
 * <p>
 * Contains necessary methods for storing and retrieving translation values.
 * <p>
 * Beside extending this object, translatable object needs to have a column
 * translations with type jblTranslations defined in hibernate mapping file.
 */
@MappedSuperclass
@TypeDef(
    name = "jblTranslations",
    typeClass = JsonSetBinaryType.class,
    parameters = {@Parameter(name = "clazz",
        value = "org.nmcpye.activitiesmanagement.extended.translation.Translation")}
)
public class TranslatableObject extends BaseLinkableObject {
    /**
     * Set of available object translation, normally filtered by locale.
     */
    @Type(type = "jblTranslations")
    @Column(name = "translations", columnDefinition = "jsonb")
    protected Set<Translation> translations = new HashSet<>();

    /**
     * Cache for object translations, where the cache key is a combination of
     * locale and translation property, and value is the translated value.
     */
    protected final transient Map<String, String> translationCache = new ConcurrentHashMap<>();

    // -------------------------------------------------------------------------
    // Getters and setters
    // -------------------------------------------------------------------------

    @Gist(included = Gist.Include.FALSE)
    @JsonProperty
    public Set<Translation> getTranslations() {
        if (translations == null) {
            translations = new HashSet<>();
        }

        return translations;
    }

    /**
     * Clears out cache when setting translations.
     */
    public void setTranslations(Set<Translation> translations) {
        this.translationCache.clear();
        this.translations = translations;
    }

    // -------------------------------------------------------------------------
    // Util methods
    // -------------------------------------------------------------------------

    /**
     * Returns a translated value for this object for the given property. The
     * current locale is read from the user context.
     *
     * @param translationKey the translation key.
     * @param defaultValue   the value to use if there are no translations.
     * @return a translated value.
     */
    protected String getTranslation(String translationKey, String defaultValue) {
        Locale locale = UserContext.getUserSetting(UserSettingKey.DB_LOCALE);

        defaultValue = defaultValue != null ? defaultValue.trim() : null;

        if (locale == null || translationKey == null || CollectionUtils.isEmpty(translations)) {
            return defaultValue;
        }

        return translationCache.computeIfAbsent(Translation.getCacheKey(locale.toString(), translationKey),
            key -> getTranslationValue(locale.toString(), translationKey));
    }

    /**
     * Get Translation value from {@code Set<Translation>} by given locale and
     * translationKey
     *
     * @return Translation value if exists, otherwise return null.
     */
    private String getTranslationValue(String locale, String translationKey) {
        for (Translation translation : translations) {
            if (locale.equals(translation.getLocale()) && translationKey.equals(translation.getProperty()) &&
                !StringUtils.isEmpty(translation.getValue())) {
                return translation.getValue();
            }
        }

        return null;
    }

    /**
     * Populates the translationsCache map unless it is already populated.
     */
    private void loadTranslationsCacheIfEmpty() {
        if (translationCache.isEmpty() && translations != null) {
            for (Translation translation : translations) {
                if (translation.getLocale() != null && translation.getProperty() != null
                    && !StringUtils.isEmpty(translation.getValue())) {
                    String key = Translation.getCacheKey(translation.getLocale(),
                        translation.getProperty());
                    translationCache.put(key, translation.getValue());
                }
            }
        }
    }
}
