package org.nmcpye.activitiesmanagement.extended.servicecoremodule.query;

import org.nmcpye.activitiesmanagement.extended.schemamodule.Schema;

public class Conjunction extends Junction {
    public Conjunction(Schema schema) {
        super(schema, Type.AND);
    }

    @Override
    public String toString() {
        return "AND[" + criterions + "]";
    }
}
