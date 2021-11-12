package org.nmcpye.activitiesmanagement.extended.servicecoremodule.query;

import org.nmcpye.activitiesmanagement.extended.schemamodule.Schema;

public class Disjunction extends Junction {
    public Disjunction(Schema schema) {
        super(schema, Type.OR);
    }

    @Override
    public String toString() {
        return "OR[" + criterions + "]";
    }
}
