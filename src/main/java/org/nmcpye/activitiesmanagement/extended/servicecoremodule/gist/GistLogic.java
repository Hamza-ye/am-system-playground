package org.nmcpye.activitiesmanagement.extended.servicecoremodule.gist;

import org.nmcpye.activitiesmanagement.extended.common.CodeGenerator;
import org.nmcpye.activitiesmanagement.extended.schema.Property;
import org.nmcpye.activitiesmanagement.extended.schema.PropertyType;
import org.nmcpye.activitiesmanagement.extended.schema.annotation.Gist.Include;
import org.nmcpye.activitiesmanagement.extended.schema.annotation.Gist.Transform;

/**
 * Contains the "business logic" aspects of building and running a
 * {@link GistQuery}.
 */
final class GistLogic {

    private GistLogic() {
        throw new UnsupportedOperationException("utility");
    }

    static boolean isIncludedField(Property p, GistAutoType all) {
        Include included = p.getGistPreferences().getIncluded();
        if (included == Include.TRUE) {
            return true;
        }
        if (included == Include.FALSE) {
            return false;
        }
        // AUTO:
        return all.isIncluded(p) && isAutoIncludedField(p);
    }

    private static boolean isAutoIncludedField(Property p) {
        return p.isPersisted()
            && p.isReadable()
            && p.getFieldName() != null;
    }

    static boolean isPersistentCollectionField(Property p) {
        return p.isPersisted() && p.isCollection() && (p.isOneToMany() || p.isManyToMany());
    }

    static boolean isPersistentReferenceField(Property p) {
        return p.isPersisted() && (p.isOneToOne() || p.isManyToOne()
            || p.getPropertyType() == PropertyType.REFERENCE && p.isIdentifiableObject());
    }

    static Class<?> getBaseType(Property p) {
        return p.isCollection() ? p.getItemKlass() : p.getKlass();
    }

    static boolean isNonNestedPath(String path) {
        return path.indexOf('.') < 0;
    }

    static boolean isAttributePath(String path) {
        return path.length() == 11
            && CodeGenerator.isValidUid(path);
    }

    static String parentPath(String path) {
        return isNonNestedPath(path) ? "" : path.substring(0, path.lastIndexOf('.'));
    }

    static String pathOnSameParent(String path, String property) {
        return isNonNestedPath(path) ? property : parentPath(path) + '.' + property;
    }

    static boolean isAccessProperty(Property p) {
        return false;
//        return "access".equals(p.key()) && p.getKlass() == Access.class;
    }

    static boolean isHrefProperty(Property p) {
        return "href".equals(p.key()) && p.getKlass() == String.class;
    }

    static boolean isCollectionSizeFilter(GistQuery.Filter filter, Property property) {
        return filter.getOperator().isSizeCompare() ||
            (filter.getOperator().isNumericCompare() && property.isCollection());
    }

    static boolean isStringLengthFilter(GistQuery.Filter filter, Property property) {
        return filter.getOperator().isSizeCompare() && property.isSimple() && property.getKlass() == String.class;
    }

    static Transform effectiveTransform(Property property, Transform fallback, Transform target) {
        if (target == Transform.AUTO) {
            target = fallback;
        }
        if (target == Transform.AUTO) {
            target = property.getGistPreferences().getTransformation();
        }
        if ((target == Transform.IDS || target == Transform.ID_OBJECTS) && property.isEmbeddedObject()
            && isPersistentCollectionField(property)) {
            return Transform.SIZE;
        }
        return target == Transform.AUTO ? Transform.NONE : target;
    }
}
