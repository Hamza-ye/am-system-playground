package org.nmcpye.activitiesmanagement.extended.schemamodule.introspection;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.google.common.collect.Multimap;
import com.google.common.primitives.Primitives;
import org.apache.commons.lang3.StringUtils;
import org.nmcpye.activitiesmanagement.extended.common.EmbeddedObject;
import org.nmcpye.activitiesmanagement.extended.common.IdentifiableObject;
import org.nmcpye.activitiesmanagement.extended.common.NameableObject;
import org.nmcpye.activitiesmanagement.extended.common.annotation.Description;
import org.nmcpye.activitiesmanagement.extended.schema.Property;
import org.nmcpye.activitiesmanagement.extended.systemmodule.system.util.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ClassUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.nmcpye.activitiesmanagement.extended.systemmodule.system.util.AnnotationUtils.getAnnotation;
import static org.nmcpye.activitiesmanagement.extended.systemmodule.system.util.AnnotationUtils.isAnnotationPresent;

/**
 * A {@link PropertyIntrospector} that adds or retains those {@link Property}
 * values in the map for which there is a {@link JsonProperty} annotation
 * available of the getter method (no argument method).
 * <p>
 * It adds information to these properties from the following annotations:
 * <ul>
 * <li>{@link JsonProperty}</li>
 * <li>{@link Description}</li>
 * </ul>
 * <p>
 * {@link Property}s already contained in the provided map will be assumed to be
 * persisted {@link Property}s.
 */
public class JacksonPropertyIntrospector implements PropertyIntrospector {
    protected static final Logger log = LoggerFactory.getLogger(JacksonPropertyIntrospector.class);

    /**
     * A simple cache to remember which types do or do not have {@link Property}
     * values so the inspection is not done over and over again.
     */
    private static final Map<Class<?>, Boolean> HAS_PROPERTIES = new ConcurrentHashMap<>();

    private static boolean hasProperties(Class<?> type) {
        return HAS_PROPERTIES.computeIfAbsent(type, key -> !collectProperties(key).isEmpty());
    }

    private static void initFromDescription(Property property) {
        if (isAnnotationPresent(property.getGetterMethod(), Description.class)) {
            Description description = getAnnotation(property.getGetterMethod(),
                Description.class);
            property.setDescription(description.value());
        }
    }

    private static String initFromJsonProperty(Property property) {
        Method getter = property.getGetterMethod();
        JsonProperty jsonProperty = getAnnotation(getter, JsonProperty.class);
        String fieldName = getFieldName(getter);
        property.setName(!isEmpty(jsonProperty.value()) ? jsonProperty.value() : fieldName);
        property.setKlass(Primitives.wrap(getter.getReturnType()));
        property.setReadable(true);
        if (property.getSetterMethod() != null) {
            property.setWritable(true);
        }
        return fieldName;
    }

    private static void initFromEnumConstants(Property property) {
        if (!Enum.class.isAssignableFrom(property.getKlass())) {
            return;
        }
        Object[] enumConstants = property.getKlass().getEnumConstants();
        List<String> enumValues = new ArrayList<>();

        for (Object value : enumConstants) {
            enumValues.add(value.toString());
        }

        property.setConstants(enumValues);
    }

//    private static void initFromJacksonXmlElementWrapper( Property property )
//    {
//        if ( !property.isCollection()
//            || !isAnnotationPresent( property.getGetterMethod(), JacksonXmlElementWrapper.class ) )
//        {
//            return;
//        }
//        JacksonXmlElementWrapper jacksonXmlElementWrapper = getAnnotation( property.getGetterMethod(),
//            JacksonXmlElementWrapper.class );
//        property.setCollectionWrapping( jacksonXmlElementWrapper.useWrapping() );
//
//        // TODO what if element-wrapper have different namespace?
//        if ( !isEmpty( jacksonXmlElementWrapper.localName() ) )
//        {
//            property.setCollectionName( jacksonXmlElementWrapper.localName() );
//        }
//    }

    private static void initFromJacksonXmlProperty( Property property )
    {
        if ( !isAnnotationPresent( property.getGetterMethod(), JsonProperty.class ) )
        {
            return;
        }
        JsonProperty jacksonXmlProperty = getAnnotation( property.getGetterMethod(),
            JsonProperty.class );

        if ( isEmpty( jacksonXmlProperty.value() ) )
        {
            property.setName( property.getName() );
        }
        else
        {
            property.setName( jacksonXmlProperty.value() );
        }
    }

    private static void initFromPersistedProperty(Property property, Property persisted) {
        property.setPersisted(true);
        property.setWritable(true);
        property.setFieldName(persisted.getFieldName());
        property.setUnique(persisted.isUnique());
        property.setRequired(persisted.isRequired());
        property.setLength(persisted.getLength());
        property.setMax(persisted.getMax());
        property.setMin(persisted.getMin());
        property.setCollection(persisted.isCollection());
        property.setCascade(persisted.getCascade());
        property.setOwner(persisted.isOwner());
        property.setManyToMany(persisted.isManyToMany());
        property.setOneToMany(persisted.isOneToMany());
        property.setOneToOne(persisted.isOneToOne());
        property.setManyToOne(persisted.isManyToOne());
        property.setOwningRole(persisted.getOwningRole());
        property.setInverseRole(persisted.getInverseRole());
    }

    private static void initCollectionProperty(Property property) {
        Class<?> returnType = property.getGetterMethod().getReturnType();
        if (!Collection.class.isAssignableFrom(returnType)) {
            property.setCollection(false);
            return;
        }
        property.setCollection(true);
        property.setCollectionName(property.getName());
        property.setOrdered(List.class.isAssignableFrom(returnType));

        Type type = property.getGetterMethod().getGenericReturnType();

        if (type instanceof ParameterizedType) {
            Class<?> klass = (Class<?>) getInnerType((ParameterizedType) type);
            property.setItemKlass(Primitives.wrap(klass));

            if (!hasProperties(klass)) {
                property.setSimple(true);
            }

            property.setIdentifiableObject(IdentifiableObject.class.isAssignableFrom(klass));
            property.setNameableObject(NameableObject.class.isAssignableFrom(klass));
            property.setEmbeddedObject(EmbeddedObject.class.isAssignableFrom(klass));
//            property.setAnalyticalObject( AnalyticalObject.class.isAssignableFrom( klass ) );
            property.setAnalyticalObject(false);
        }
    }

    private static String getFieldName(Method method) {
        String name;

        String[] getters = new String[]{"is", "has", "get"};

        name = method.getName();

        for (String getter : getters) {
            if (name.startsWith(getter)) {
                name = name.substring(getter.length());
            }
        }

        return StringUtils.uncapitalize(name);
    }

    private static Property createSelfProperty( Class<?> clazz )
    {
        Property self = new Property();

        JsonRootName jsonRootName = getAnnotation( clazz,
            JsonRootName.class );

        if ( !isEmpty( jsonRootName.value() ) )
        {
            self.setName( jsonRootName.value() );
        }

        if ( !isEmpty( jsonRootName.namespace() ) )
        {
            self.setNamespace( jsonRootName.namespace() );
        }
        return self;
    }

    private static List<Property> collectProperties(Class<?> klass) {
        boolean isPrimitiveOrWrapped = ClassUtils.isPrimitiveOrWrapper(klass);

        if (isPrimitiveOrWrapped) {
            return Collections.emptyList();
        }

        Multimap<String, Method> multimap = ReflectionUtils.getMethodsMultimap(klass);
        List<String> fieldNames = ReflectionUtils.getAllFields(klass).stream().map(Field::getName)
            .collect(Collectors.toList());
        List<Property> properties = new ArrayList<>();

        Map<String, Method> methodMap = multimap.keySet().stream().filter(key -> {
            List<Method> methods = multimap.get(key).stream()
                .filter(method -> isAnnotationPresent(method, JsonProperty.class)
                    && method.getParameterTypes().length == 0)
                .collect(Collectors.toList());

            if (methods.size() > 1) {
                log.error("More than one web-api exposed method with name '" + key + "' found on class '"
                    + klass.getName() + "' please fix as this is known to cause issues with Schema / Query services.");

                log.debug("Methods found: " + methods);
            }

            return methods.size() == 1;
        }).collect(Collectors.toMap(Function.identity(), key -> {
            List<Method> collect = multimap.get(key).stream()
                .filter(method -> isAnnotationPresent(method, JsonProperty.class)
                    && method.getParameterTypes().length == 0)
                .collect(Collectors.toList());

            return collect.get(0);
        }));

        methodMap.keySet().forEach(key -> {
            String fieldName = getFieldName(methodMap.get(key));
            String setterName = "set" + StringUtils.capitalize(fieldName);

            Property property = new Property(klass, methodMap.get(key), null);

            if (fieldNames.contains(fieldName)) {
                property.setFieldName(fieldName);
            }

            Iterator<Method> methodIterator = multimap.get(setterName).iterator();

            if (methodIterator.hasNext()) {
                property.setSetterMethod(methodIterator.next());
            }

            properties.add(property);
        });

        return properties;
    }

    private static Type getInnerType(ParameterizedType parameterizedType) {
        ParameterizedType innerType = parameterizedType;

        while (innerType.getActualTypeArguments()[0] instanceof ParameterizedType) {
            innerType = (ParameterizedType) parameterizedType.getActualTypeArguments()[0];
        }

        return innerType.getActualTypeArguments()[0];
    }

    @Override
    public void introspect(Class<?> clazz, Map<String, Property> properties) {
        Map<String, Property> persistedProperties = new HashMap<>(properties);
        properties.clear();
        Set<String> classFieldNames = ReflectionUtils.getAllFieldNames(clazz);

        // TODO this is quite nasty, should find a better way of exposing
        // properties at class-level
        if ( isAnnotationPresent( clazz, JsonRootName.class ) )
        {
            properties.put( "__self__", createSelfProperty( clazz ) );
        }

        for (Property property : collectProperties(clazz)) {
            String fieldName = initFromJsonProperty(property);

            if (classFieldNames.contains(fieldName)) {
                property.setFieldName(fieldName);
            }

            if (persistedProperties.containsKey(fieldName)) {
                initFromPersistedProperty(property, persistedProperties.get(fieldName));
            }

            initFromDescription(property);
            initFromJacksonXmlProperty( property );

            initCollectionProperty(property);
            if (!property.isCollection() && !hasProperties(property.getGetterMethod().getReturnType())) {
                property.setSimple(true);
            }

//            initFromJacksonXmlElementWrapper( property );
            initFromEnumConstants(property);

            properties.put(property.key(), property);
        }
    }
}
