package org.nmcpye.activitiesmanagement.extended.servicecoremodule.gist;

import org.nmcpye.activitiesmanagement.extended.common.PrimaryKeyObject;
import org.nmcpye.activitiesmanagement.extended.hibernatemodule.hibernate.exception.ReadAccessDeniedException;
import org.nmcpye.activitiesmanagement.extended.schema.Property;
import org.nmcpye.activitiesmanagement.extended.schema.annotation.Gist.Transform;
import org.nmcpye.activitiesmanagement.extended.schemamodule.RelativePropertyContext;
import org.nmcpye.activitiesmanagement.extended.schemamodule.Schema;

import java.util.List;

import static java.util.Arrays.stream;
import static org.nmcpye.activitiesmanagement.extended.servicecoremodule.gist.GistLogic.getBaseType;
import static org.nmcpye.activitiesmanagement.extended.servicecoremodule.gist.GistLogic.isNonNestedPath;

/**
 * Validates a {@link GistQuery} for consistency and access restrictions.
 */
final class GistValidator {
    private final GistQuery query;

    private final RelativePropertyContext context;

    private final GistAccessControl access;

    public GistValidator(GistQuery query, RelativePropertyContext context, GistAccessControl access) {
        this.query = query;
        this.context = context;
        this.access = access;
    }

    public void validateQuery() {
        validateOwnerCollection();
        validateOwnerAccess();
        query.getFilters().forEach(filter -> validateFilter(filter, context));
        query.getOrders().forEach(order -> validateOrder(context.resolveMandatory(order.getPropertyPath())));
        query.getFields().forEach(field -> validateField(field, context));
    }

    /**
     * Can the current user view the owner object of the collection listed?
     */
    private void validateOwnerAccess() {
        GistQuery.Owner owner = query.getOwner();
        if (owner == null || owner.getCollectionProperty() == null) {
            return;
        }
        if (!access.canReadObject(owner.getType(), owner.getId())) {
            throw new ReadAccessDeniedException(
                String.format("User not allowed to view %s %s", owner.getType().getSimpleName(), owner.getId()));
        }
    }

    private void validateOwnerCollection() {
        GistQuery.Owner owner = query.getOwner();
        if (owner == null || owner.getCollectionProperty() == null) {
            return;
        }
        Property collection = context.switchedTo(owner.getType()).resolveMandatory(owner.getCollectionProperty());
        if (!collection.isCollection() || !collection.isPersisted()) {
            throw createIllegalProperty(collection, "Property `%s` is not a persisted collection member.");
        }
    }

    private void validateField(GistQuery.Field f, RelativePropertyContext context) {
        String path = f.getPropertyPath();
        if (GistQuery.Field.REFS_PATH.equals(path) || f.isAttribute()) {
            return;
        }
        Property field = context.resolveMandatory(path);
        if (!isNonNestedPath(path)) {
            List<Property> pathElements = context.resolvePath(path);
            Property head = pathElements.get(0);
            if (head.isCollection() && head.isPersisted()) {
                throw createIllegalProperty(field,
                    "Property `%s` computes to many values and therefore cannot be used as a field.");
            }
        }
        Transform transformation = f.getTransformation();
        String transArgs = f.getTransformationArgument();
        if (transformation == Transform.PLUCK && transArgs != null) {
            Property plucked = context.switchedTo(getBaseType(field)).resolveMandatory(transArgs);
            if (!plucked.isPersisted()) {
                throw createIllegalProperty(plucked,
                    "Property `%s` cannot be plucked as it is not a persistent field.");
            }
        }
        if (transformation == Transform.FROM) {
            validateFromTransformation(context, field, transArgs);
        }
        if (!field.isReadable()) {
            throw createNoReadAccess(f, null);
        }
        validateFieldAccess(f, context);
    }

    private void validateFromTransformation(RelativePropertyContext context, Property field, String transArgs) {
        if (stream(query.getElementType().getConstructors()).noneMatch(c -> c.getParameterCount() == 0)) {
            throw createIllegalProperty(field,
                "Property `%s` cannot use from transformation as bean has no default constructor.");
        }
        if (field.isPersisted()) {
            throw createIllegalProperty(field,
                "Property `%s` is persistent an cannot be computed using transformation from.");
        }
        if (transArgs == null || transArgs.isEmpty()) {
            throw createIllegalProperty(field,
                "Property `%s` requires one or more source fields when used with transformation from.");
        }
        for (String fromPropertyName : transArgs.split(",")) {
            Property fromField = context.resolve(fromPropertyName);
            if (fromField == null) {
                throw createIllegalProperty(fromPropertyName,
                    "Property `%s` used in from transformation does not exist.");
            }
            if (!fromField.isPersisted()) {
                throw createIllegalProperty(fromField,
                    "Property `%s` must be persistent to be used as source for from transformation.");
            }
        }
    }

    /**
     * Can the current user view the field? Usually this asks if the user can
     * view the owning object type but there are fields that are generally
     * visible.
     */
    private void validateFieldAccess(GistQuery.Field f, RelativePropertyContext context) {
        String path = f.getPropertyPath();
        Property field = context.resolveMandatory(path);
        if (!access.canRead(query.getElementType(), path)) {
            throw createNoReadAccess(f, query.getElementType());
        }
        if (!isNonNestedPath(path)) {
            Schema fieldOwner = context.switchedTo(path).getHome();
            @SuppressWarnings("unchecked")
            Class<? extends PrimaryKeyObject> ownerType = (Class<? extends PrimaryKeyObject>) fieldOwner.getKlass();
            if (fieldOwner.isIdentifiableObject() && !access.canRead(ownerType, field.getName())) {
                throw createNoReadAccess(f, ownerType);
            }
        }
    }

    private void validateFilter(GistQuery.Filter f, RelativePropertyContext context) {
        if (f.isAttribute()) {
            return;
        }
        Property filter = context.resolveMandatory(f.getPropertyPath());
        if (!filter.isPersisted()) {
            throw createIllegalProperty(filter, "Property `%s` cannot be used as filter property.");
        }

        validateFilterArgument(f);
        validateFilterAccess(f);
    }

    private void validateFilterAccess(GistQuery.Filter f) {
        GistQuery.Comparison operator = f.getOperator();
        if (operator.isAccessCompare()) {
            String[] ids = f.getValue();
            if (operator != GistQuery.Comparison.CAN_ACCESS && ids.length != 1) {
                throw createIllegalFilter(f, "Filter `%s` requires a single user ID as argument.");
            }
            if (operator == GistQuery.Comparison.CAN_ACCESS && ids.length != 2) {
                throw createIllegalFilter(f, "Filter `%s` requires a user ID and an access pattern argument.");
            }
            if (operator == GistQuery.Comparison.CAN_ACCESS) {
                // OBS! we include this user input directly in the query so we
                // have
                // to make sure it is not malicious
                String pattern = f.getValue()[1];
                if (!pattern.matches("[_%rw]{2,8}")) {
                    throw createIllegalFilter(f,
                        "Filter `%s` pattern argument must be 2 to 8 letters allowing letters 'r', 'w', '_' and '%%'.");
                }
            }
            if (!access.canFilterByAccessOfUser(ids[0])) {
                throw new ReadAccessDeniedException(String.format(
                    "Filtering by user access in filter `%s` requires permissions to manage the user %s.",
                    f, ids[0]));
            }
        }
    }

    private void validateFilterArgument(GistQuery.Filter f) {
        if (f.getOperator().isUnary()) {
            if (f.getValue().length > 0) {
                throw createIllegalFilter(f, "Filter `%s` uses an unary operator and does not need an argument.");
            }
        } else if (f.getValue().length == 0) {
            throw createIllegalFilter(f, "Filter `%s` uses a binary operator that does need an argument.");
        }
        if (!f.getOperator().isMultiValue() && f.getValue().length > 1) {
            throw createIllegalFilter(f, "Filter `%s` can only be used with a single argument.");
        }
    }

    private void validateOrder(Property order) {
        if (!order.isPersisted() || !order.isSimple()) {
            throw createIllegalProperty(order, "Property `%s` cannot be used as order property.");
        }
    }

    private IllegalArgumentException createIllegalProperty(Property property, String message) {
        return createIllegalProperty(property.getName(), message);
    }

    private IllegalArgumentException createIllegalProperty(String propertyName, String message) {
        return new IllegalArgumentException(String.format(message, propertyName));
    }

    private IllegalArgumentException createIllegalFilter(GistQuery.Filter filter, String message) {
        return new IllegalArgumentException(String.format(message, filter.toString()));
    }

    private ReadAccessDeniedException createNoReadAccess(GistQuery.Field field, Class<? extends PrimaryKeyObject> ownerType) {
        if (ownerType == null) {
            return new ReadAccessDeniedException(
                String.format("Property `%s` is not readable.", field.getPropertyPath()));
        }
        return new ReadAccessDeniedException(
            String.format("Field `%s` is not readable as user is not allowed to view objects of type %s.",
                field.getPropertyPath(), ownerType.getSimpleName()));
    }
}
