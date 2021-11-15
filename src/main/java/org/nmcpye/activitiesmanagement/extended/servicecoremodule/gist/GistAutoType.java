package org.nmcpye.activitiesmanagement.extended.servicecoremodule.gist;

import org.nmcpye.activitiesmanagement.extended.schema.Property;
import org.nmcpye.activitiesmanagement.extended.schema.PropertyType;
import org.nmcpye.activitiesmanagement.extended.schema.annotation.Gist.Transform;

import java.util.EnumSet;

import static java.util.EnumSet.complementOf;
import static java.util.EnumSet.of;

/**
 * {@link GistAutoType}s is a gist configuration that control which fields the
 * {@code :all} / {@code *} preset will include and which {@link Transform} is
 * used by default on included collection fields.
 * <p>
 * The default {@link GistAutoType} depends on the endpoint different used:
 * <ul>
 * <li>{@code /api/{object-type}/gist} uses {@link GistAutoType#S}</li>
 * <li>{@code /api/{object-type}/{uid}/gist} uses {@link GistAutoType#L}</li>
 * <li>{@code /api/{object-type}/{uid}/{property}/gist} uses
 * {@link GistAutoType#M}</li>
 * </ul>
 * <p>
 * Users can override this default using the {@code auto} parameter.
 * <p>
 * This can be understood as the user's "master-switch" to control how short the
 * gist should be.
 * <p>
 * The reason to use different settings depending on the endpoint is to scale
 * the size of the resulting list items to match the purpose of the list/view
 * better. Main list are shortest, property lists a bit more detailed and a
 * single object view is most detailed.
 */
public enum GistAutoType {
    XL(Transform.ID_OBJECTS, complementOf(of(PropertyType.PASSWORD))),
    L(Transform.IDS, complementOf(of(PropertyType.PASSWORD))),
    M(Transform.SIZE, complementOf(of(PropertyType.PASSWORD))),
    S(Transform.NONE, complementOf(of(PropertyType.PASSWORD, PropertyType.COMPLEX))),
    XS(Transform.NONE, of(PropertyType.IDENTIFIER, PropertyType.TEXT, PropertyType.EMAIL));

    private final Transform defaultTransformation;

    private final EnumSet<PropertyType> includes;

    GistAutoType(Transform defaultTransformation, EnumSet<PropertyType> includes) {
        this.defaultTransformation = defaultTransformation;
        this.includes = includes;
    }

    public Transform getDefaultTransformation() {
        return defaultTransformation;
    }

    public boolean isIncluded(Property p) {
        return includes.contains(p.getPropertyType())
            && (!p.isCollection() || includes.contains(p.getItemPropertyType()));
    }
}
