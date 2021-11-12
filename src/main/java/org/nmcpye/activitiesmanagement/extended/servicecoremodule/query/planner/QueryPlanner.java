package org.nmcpye.activitiesmanagement.extended.servicecoremodule.query.planner;

import org.nmcpye.activitiesmanagement.extended.schemamodule.Schema;
import org.nmcpye.activitiesmanagement.extended.servicecoremodule.query.Query;

import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;

public interface QueryPlanner {
    QueryPlan planQuery(Query query);

    QueryPlan planQuery(Query query, boolean persistedOnly);

    QueryPath getQueryPath(Schema schema, String path);

    Path<?> getQueryPath(Root<?> root, Schema schema, String path);
}
