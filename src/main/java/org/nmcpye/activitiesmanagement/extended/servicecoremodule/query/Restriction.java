package org.nmcpye.activitiesmanagement.extended.servicecoremodule.query;

import org.nmcpye.activitiesmanagement.extended.servicecoremodule.query.operators.Operator;
import org.nmcpye.activitiesmanagement.extended.servicecoremodule.query.planner.QueryPath;

import javax.persistence.criteria.Predicate;

public class Restriction implements Criterion {
    /**
     * Path to property you want to restrict only, one first-level properties
     * are currently supported.
     */
    private String path;

    /**
     * Operator for restriction.
     */
    private Operator operator;

    /**
     * Query Path.
     */
    private QueryPath queryPath;

    private Predicate predicate;

    public Restriction(String path, Predicate predicate) {
        this.path = path;
        this.predicate = predicate;
    }

    public Restriction(String path, Operator operator) {
        this.path = path;
        this.operator = operator;
    }

    public String getPath() {
        return path;
    }

    public Operator getOperator() {
        return operator;
    }

    public QueryPath getQueryPath() {
        return queryPath;
    }

    public Restriction setQueryPath(QueryPath queryPath) {
        this.queryPath = queryPath;
        return this;
    }

    public Predicate getPredicate() {
        return predicate;
    }

    public void setPredicate(Predicate predicate) {
        this.predicate = predicate;
    }

    public boolean haveQueryPath() {
        return queryPath != null;
    }

    @Override
    public String toString() {
        return "[" + path + ", op: " + operator + "]";
    }
}
