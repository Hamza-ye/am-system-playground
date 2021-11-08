package org.nmcpye.activitiesmanagement.extended.schemamodule.introspection;

import org.nmcpye.activitiesmanagement.extended.schema.Property;

import java.util.Map;

/**
 * A {@link PropertyIntrospector} is a function to extract or modify
 * {@link Property} values from a {@link Class} or additional information
 * associated with that {@link Class} to build a
 * {@link org.nmcpye.activitiesmanagement.extended.schema.Schema}.
 *
 * The main sources of information are Hibernate, provided by
 * {@link HibernatePropertyIntrospector} and Jackson, provided by
 * {@link JacksonPropertyIntrospector}.
 *
 * The baseline created by the main sources is enriched by the
 * {@link PropertyPropertyIntrospector} and
 * {@link TranslatablePropertyIntrospector} which look for optional annotations.
 *
 */
@FunctionalInterface
public interface PropertyIntrospector
{

    /**
     * Adds or modifies the provided {@link Property} map for the provided
     * {@link Class}.
     *
     * @param klass The type to introspect
     * @param properties the intermediate and result state.
     *        {@link PropertyIntrospector} running before this one might already
     *        have added to the provided map. This {@link PropertyIntrospector}
     *        can add or modify the {@link Property} entries further.
     */
    void introspect(Class<?> klass, Map<String, Property> properties);

    /**
     * Allows to chain multiple {@link PropertyIntrospector}s into a sequence.
     *
     * @param next the {@link PropertyIntrospector} called after this one
     * @return A {@link PropertyIntrospector} that first calls this instance and
     *         then the next instance
     */
    default PropertyIntrospector then(PropertyIntrospector next)
    {
        return ( klass, properties ) -> {
            introspect( klass, properties );
            next.introspect( klass, properties );
        };
    }
}
