package org.nmcpye.activitiesmanagement.extended.servicecoremodule.query;

import org.nmcpye.activitiesmanagement.extended.schemamodule.Schema;

public abstract class Junction extends Criteria implements Criterion {
    public enum Type {
        AND,
        OR
    }

    protected Type type;

    public Junction(Schema schema, Type type) {
        super(schema);
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    @Override
    public String toString() {
        return "[ " + type + ", " + criterions + "]";
    }
}
