package org.nmcpye.activitiesmanagement.extended.schemamodule;

import java.util.NoSuchElementException;

/**
 * Exception thrown then a {@link Schema} {@link org.nmcpye.activitiesmanagement.extended.schema.Property} is looked up by a
 * path and no such property does exist.
 */
public final class SchemaPathException extends NoSuchElementException {
    public SchemaPathException(String message) {
        super(message);
    }
}
