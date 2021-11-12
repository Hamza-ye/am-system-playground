package org.nmcpye.activitiesmanagement.extended.schemamodule.introspection;

import com.google.common.primitives.Primitives;
import org.nmcpye.activitiesmanagement.extended.common.IdentifiableObject;
import org.nmcpye.activitiesmanagement.extended.schema.Property;
import org.nmcpye.activitiesmanagement.extended.schema.PropertyType;
import org.nmcpye.activitiesmanagement.extended.schema.annotation.Property.Access;
import org.nmcpye.activitiesmanagement.extended.schema.annotation.Property.Value;
import org.nmcpye.activitiesmanagement.extended.schema.annotation.PropertyRange;
import org.nmcpye.activitiesmanagement.extended.schema.annotation.PropertyTransformer;
import org.springframework.util.Assert;

import java.lang.reflect.Method;
import java.util.*;

import static org.nmcpye.activitiesmanagement.extended.schema.PropertyType.*;
import static org.nmcpye.activitiesmanagement.extended.systemmodule.system.util.AnnotationUtils.getAnnotation;
import static org.nmcpye.activitiesmanagement.extended.systemmodule.system.util.AnnotationUtils.isAnnotationPresent;

/**
 * A {@link PropertyIntrospector} adds information to existing {@link Property}
 * values if they are annotated with one of the following annotations:
 *
 * <ul>
 * <li>{@link org.nmcpye.activitiesmanagement.extended.schema.annotation.Property}</li>
 * <li>{@link PropertyRange}</li>
 * <li>{@link PropertyTransformer}</li>
 * </ul>
 * <p>
 * It will also initialise the {@link Property#getMin()} and
 * {@link Property#getMax()} based on the overall information available for the
 * {@link Property}.
 */
public class PropertyPropertyIntrospector implements PropertyIntrospector {
    private static final Set<PropertyType> PROPS_IGNORE_MIN_MAX = EnumSet.of(REFERENCE, BOOLEAN, DATE, CONSTANT);

    private static void updatePropertyTypes(Property property) {
        Assert.notNull(property, "Property cannot be null");
        Assert.notNull(property.getKlass(), "Property class cannot be null");

        property.setPropertyType(getPropertyType(property.getKlass()));

        if (property.isCollection()) {
            property.setItemPropertyType(getPropertyType(property.getItemKlass()));
        }

        if (property.isWritable()) {
            initFromPropertyAnnotation(property);
            initFromPropertyTransformerAnnotation(property);
            initFromPropertyRangeAnnotation(property);
            ensureMinMaxDefaults(property);
        } else {
            property.setMin(null);
            property.setMax(null);
        }
    }

    private static void ensureMinMaxDefaults(Property property) {
        if (property.getMin() == null) {
            property.setMin(0d);
        }

        if (property.getMax() == null) {
            property.setMax(property.is(PropertyType.INTEGER, PropertyType.TEXT)
                ? (double) Integer.MAX_VALUE
                : Double.MAX_VALUE);
        }

        if (!property.isCollection() && PROPS_IGNORE_MIN_MAX.contains(property.getPropertyType())) {
            property.setMin(null);
            property.setMax(null);
        }
    }

    private static void initFromPropertyRangeAnnotation(Property property) {
        if (isAnnotationPresent(property.getGetterMethod(), PropertyRange.class)) {
            PropertyRange range = getAnnotation(property.getGetterMethod(), PropertyRange.class);

            double max = range.max();
            double min = range.min();

            if (max > Integer.MAX_VALUE
                && property.is(PropertyType.INTEGER, PropertyType.TEXT, PropertyType.COLLECTION)) {
                max = Integer.MAX_VALUE;
            }

            if (property.is(PropertyType.COLLECTION) && min < 0) {
                min = 0d;
            }

            // Max will be applied from PropertyRange annotation only if it
            // is more restrictive than hibernate max (or its a collection)
            if (property.getMax() == null || max < property.getMax() || property.is(PropertyType.COLLECTION)) {
                property.setMax(max);
            }

            // Min is not set by hibernate (always 0) hence the min from
            // PropertyRange will always be applied.
            property.setMin(min);
        }
    }

    private static void initFromPropertyTransformerAnnotation(Property property) {
        Method getter = property.getGetterMethod();
        if (isAnnotationPresent(getter, PropertyTransformer.class)) {
            property.setPropertyTransformer(getAnnotation(getter, PropertyTransformer.class).value());
        }
    }

    private static void initFromPropertyAnnotation(Property property) {
        Method getter = property.getGetterMethod();
        if (isAnnotationPresent(getter, org.nmcpye.activitiesmanagement.extended.schema.annotation.Property.class)) {
            org.nmcpye.activitiesmanagement.extended.schema.annotation.Property pAnnotation = getAnnotation(getter,
                org.nmcpye.activitiesmanagement.extended.schema.annotation.Property.class);
            property.setPropertyType(pAnnotation.value());

            if (pAnnotation.required() != Value.DEFAULT) {
                property.setRequired(pAnnotation.required() == Value.TRUE);
            }

            if (pAnnotation.persisted() != Value.DEFAULT) {
                property.setPersisted(pAnnotation.persisted() == Value.TRUE);
            }

            if (pAnnotation.owner() != Value.DEFAULT) {
                property.setOwner(pAnnotation.owner() == Value.TRUE);
            }

            if (Access.READ_ONLY == pAnnotation.access()) {
                property.setWritable(false);
            }

            if (Access.WRITE_ONLY == pAnnotation.access()) {
                property.setReadable(false);
            }
            if (!pAnnotation.persistedAs().isEmpty()) {
                property.setFieldName(pAnnotation.persistedAs());
            }
        }
    }

    private static PropertyType getPropertyType(Class<?> klass) {
        if (isAssignableFrom(klass, String.class)
            || isAssignableFrom(klass, Character.class)
            || isAssignableFrom(klass, Byte.class)) {
            return PropertyType.TEXT;
        }
        if (isAssignableFrom(klass, Integer.class)) {
            return PropertyType.INTEGER;
        }
        if (isAssignableFrom(klass, Boolean.class)) {
            return PropertyType.BOOLEAN;
        }
        if (isAssignableFrom(klass, Float.class)
            || isAssignableFrom(klass, Double.class)) {
            return PropertyType.NUMBER;
        }
        if (isAssignableFrom(klass, Date.class)
            || isAssignableFrom(klass, java.sql.Date.class)) {
            return PropertyType.DATE;
        }
        if (isAssignableFrom(klass, Enum.class)) {
            return PropertyType.CONSTANT;
        }
        if (isAssignableFrom(klass, IdentifiableObject.class)) {
            return PropertyType.REFERENCE;
        }
        if (isAssignableFrom(klass, Collection.class)) {
            return PropertyType.COLLECTION;
        }

        // if klass is primitive (but unknown), fall back to text, if its not
        // then assume reference
        return Primitives.isWrapperType(klass) ? PropertyType.TEXT : PropertyType.COMPLEX;
    }

    private static boolean isAssignableFrom(Class<?> propertyKlass, Class<?> klass) {
        return klass.isAssignableFrom(propertyKlass);
    }

    @Override
    public void introspect(Class<?> klass, Map<String, Property> properties) {
        for (Property property : properties.values()) {
            if (property.getKlass() != null) {
                updatePropertyTypes(property);
            }
        }
    }

}
