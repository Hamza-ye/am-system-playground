package org.nmcpye.activitiesmanagement.extended.patch;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import org.nmcpye.activitiesmanagement.extended.common.EmbeddedObject;
import org.nmcpye.activitiesmanagement.extended.common.IdentifiableObject;

public class Mutation {
    /**
     * Full dot separated path of this mutation (e.g a.b.c).
     */
    private final String path;

    /**
     * New value for given path.
     */
    private final Object value;

    /**
     * Is the mutation an ADD or DEL, this mainly applies to collections.
     */
    private Operation operation = Operation.ADDITION;

    public enum Operation {
        ADDITION,
        DELETION
    }

    public Mutation(String path) {
        this.path = path;
        this.value = null;
    }

    public Mutation(String path, Object value) {
        this.path = path;
        this.value = value;
    }

    public Mutation(String path, Object value, Operation operation) {
        this.path = path;
        this.value = value;
        this.operation = operation;
    }

    @JsonProperty
    public String getPath() {
        return path;
    }

    public Object getValue() {
        return value;
    }

    @JsonProperty("value")
    public Object getLogValue() {
        if (IdentifiableObject.class.isInstance(value) && !EmbeddedObject.class.isInstance(value)) {
            return ((IdentifiableObject) value).getUid();
        }

        return value;
    }

    @JsonProperty
    public Operation getOperation() {
        return operation;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
            .add("path", path)
            .add("operation", operation)
            .add("value", getLogValue())
            .toString();
    }
}
