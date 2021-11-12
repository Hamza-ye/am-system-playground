package org.nmcpye.activitiesmanagement.extended.schemamodule.introspection;

import org.nmcpye.activitiesmanagement.extended.schema.GistPreferences;
import org.nmcpye.activitiesmanagement.extended.schema.Property;
import org.nmcpye.activitiesmanagement.extended.schema.annotation.Gist;

import java.lang.reflect.Method;
import java.util.Map;

import static org.nmcpye.activitiesmanagement.extended.systemmodule.system.util.AnnotationUtils.getAnnotation;

/**
 * A {@link PropertyIntrospector} that adds information to existing
 * {@link Property} values if they are annotated with
 * {@link org.nmcpye.activitiesmanagement.extended.schema.annotation.Gist}.
 */
public class GistPropertyIntrospector implements PropertyIntrospector {
    @Override
    public void introspect(Class<?> klass, Map<String, Property> properties) {
        for (Property property : properties.values()) {
            if (property.getKlass() != null) {
                initFromGistAnnotation(property);
            }
        }
    }

    private void initFromGistAnnotation(Property property) {
        Method getter = property.getGetterMethod();
        Gist gist = getAnnotation(getter, Gist.class);
        if (gist != null) {
            property.setGistPreferences(new GistPreferences(
                gist.included(),
                gist.transformation()));
        }
    }
}
