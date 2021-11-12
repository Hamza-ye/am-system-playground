package org.nmcpye.activitiesmanagement.extended.schemamodule;

import org.nmcpye.activitiesmanagement.extended.schema.Property;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

import static java.util.Collections.singletonList;

/**
 * A utility to resolve the {@link Property} for a given path.
 * <p>
 * A path can be
 *
 * <pre>
 *   property
 *   parentProperty.childProperty
 *   root.child.child
 * </pre>
 */
public final class RelativePropertyContext {

    private final Schema homeSchema;

    private final Function<Class<?>, Schema> schemaLookup;

    private final Map<String, Property> cache = new ConcurrentHashMap<>();

    private final Map<Class<?>, RelativePropertyContext> byHomeType;

    public RelativePropertyContext(Class<?> baseType, Function<Class<?>, Schema> schemaLookup) {
        this(baseType, schemaLookup, new ConcurrentHashMap<>());
    }

    private RelativePropertyContext(Class<?> homeType, Function<Class<?>, Schema> schemaLookup,
                                    Map<Class<?>, RelativePropertyContext> byHomeType) {
        this.homeSchema = schemaLookup.apply(homeType);
        if (this.homeSchema == null) {
            throw new IllegalArgumentException("No schema for type: " + homeType.getSimpleName());
        }
        this.schemaLookup = schemaLookup;
        this.byHomeType = byHomeType;
        if (byHomeType.isEmpty()) {
            byHomeType.put(homeType, this);
        }
    }

    public Schema getHome() {
        return homeSchema;
    }

    public RelativePropertyContext switchedTo(Class<?> homeType) {
        return homeSchema.getKlass() == homeType
            ? this
            : byHomeType.computeIfAbsent(homeType,
            home -> new RelativePropertyContext(home, schemaLookup, byHomeType));
    }

    public RelativePropertyContext switchedTo(String path) {
        if (path.indexOf('.') < 0) {
            return this;
        }
        List<Property> segments = resolvePath(path);
        return switchedTo(segments.get(segments.size() - 2).getKlass());
    }

    @Nonnull
    public Property resolveMandatory(String path) {
        Property property = resolve(path);
        if (property == null) {
            throw createNoSuchPath(path);
        }
        return property;
    }

    @Nullable
    public Property resolve(String path) {
        if (path.indexOf('.') < 0) {
            // just for performance do simple and common case first
            // also these are not cached
            return homeSchema.getProperty(path);
        }
        return cache.computeIfAbsent(path, key -> resolvePath(key, null));
    }

    @Nonnull
    public List<Property> resolvePath(String path) {
        if (path.indexOf('.') < 0) {
            // just for performance do simple and common case first
            return singletonList(resolveMandatory(path));
        }
        ArrayList<Property> pathElements = new ArrayList<>();
        Property tail = resolvePath(path, pathElements);
        if (tail == null) {
            throw createNoSuchPath(path);
        }
        return pathElements;
    }

    private Property resolvePath(String path, List<Property> pathElements) {
        String[] segments = path.split("\\.");
        Schema curSchema = homeSchema;
        Property curProp = null;
        for (int i = 0; i < segments.length - 1; i++) {
            curProp = curSchema.getProperty(segments[i]);
            if (curProp == null) {
                return null; // does not exist
            }
            if (pathElements != null) {
                pathElements.add(curProp);
            }
            curSchema = schemaLookup.apply(curProp.isCollection() ? curProp.getItemKlass() : curProp.getKlass());
            if (curSchema == null) {
                return null; // does not exist
            }
        }
        Property tail = curSchema.getProperty(segments[segments.length - 1]);
        if (pathElements != null) {
            pathElements.add(tail);
        }
        return tail;
    }

    private SchemaPathException createNoSuchPath(String path) {
        return new SchemaPathException(
            String.format("Property `%s` does not exist in %s", path, homeSchema.getSingular()));
    }
}
