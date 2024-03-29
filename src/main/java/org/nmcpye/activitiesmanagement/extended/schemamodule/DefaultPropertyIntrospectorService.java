package org.nmcpye.activitiesmanagement.extended.schemamodule;

import org.nmcpye.activitiesmanagement.extended.common.IdentifiableObject;
import org.nmcpye.activitiesmanagement.extended.schema.Property;
import org.nmcpye.activitiesmanagement.extended.schemamodule.introspection.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManagerFactory;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.Collections.unmodifiableMap;

/**
 * Default {@link PropertyIntrospectorService} implementation that uses
 * Reflection and Jackson annotations for reading in properties.
 */
@Service("org.nmcpye.activitiesmanagement.extended.schemamodule.PropertyIntrospectorService")
public class DefaultPropertyIntrospectorService implements PropertyIntrospectorService {
    private final Map<Class<?>, Map<String, Property>> classMapCache = new ConcurrentHashMap<>();

    private final PropertyIntrospector introspector;

    @Autowired
    public DefaultPropertyIntrospectorService(EntityManagerFactory entityManagerFactory) {
        this.introspector = new HibernatePropertyIntrospector(entityManagerFactory)
            .then(new JacksonPropertyIntrospector())
            .then(new PropertyPropertyIntrospector())
            .then(new GistPropertyIntrospector());
    }

//    @Autowired
//    public DefaultPropertyIntrospectorService() {
//        this( new JacksonPropertyIntrospector()
//            .then( new PropertyPropertyIntrospector() )
//            .then( new GistPropertyIntrospector() ) );
//    }

    public DefaultPropertyIntrospectorService(PropertyIntrospector introspector) {
        this.introspector = introspector;
    }

    @Override
    public Map<String, Property> getPropertiesMap(Class<?> klass) {
        return classMapCache.computeIfAbsent(klass, this::scanClass);
    }

    /**
     * Introspect a class and return a map with key=property-name, and
     * value=Property class.
     *
     * @param klass Class to scan
     * @return Map with key=property-name, and value=Property class
     */
    private Map<String, Property> scanClass(Class<?> klass) {
        if (klass.isInterface() && IdentifiableObject.class.isAssignableFrom(klass)) {
            throw new IllegalArgumentException("Use SchemaService#getConcreteClass to resolve base type: " + klass);
        }
        Map<String, Property> properties = new HashMap<>();
        introspector.introspect(klass, properties);
        return unmodifiableMap(properties);
    }

}
