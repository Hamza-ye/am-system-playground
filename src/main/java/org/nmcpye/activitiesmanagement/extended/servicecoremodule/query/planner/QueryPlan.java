package org.nmcpye.activitiesmanagement.extended.servicecoremodule.query.planner;

import org.nmcpye.activitiesmanagement.domain.User;
import org.nmcpye.activitiesmanagement.extended.schemamodule.Schema;
import org.nmcpye.activitiesmanagement.extended.servicecoremodule.query.Query;

public class QueryPlan {
    private final Query persistedQuery;

    private final Query nonPersistedQuery;

    private QueryPlan(Query persistedQuery, Query nonPersistedQuery) {
        this.persistedQuery = persistedQuery;
        this.nonPersistedQuery = nonPersistedQuery;
    }

    public Query getPersistedQuery() {
        return persistedQuery;
    }

    public Query getNonPersistedQuery() {
        return nonPersistedQuery;
    }

    public Schema getSchema() {
        if (persistedQuery != null) {
            return persistedQuery.getSchema();
        } else if (nonPersistedQuery != null) {
            return nonPersistedQuery.getSchema();
        }

        return null;
    }

    public User getUser() {
        if (persistedQuery != null) {
            return persistedQuery.getUser();
        } else if (nonPersistedQuery != null) {
            return nonPersistedQuery.getUser();
        }

        return null;
    }

    public void setUser(User user) {
        if (persistedQuery != null) {
            persistedQuery.setUser(user);
        }

        if (nonPersistedQuery != null) {
            nonPersistedQuery.setUser(user);
        }
    }

    public static final class QueryPlanBuilder {
        private Query persistedQuery;

        private Query nonPersistedQuery;

        private QueryPlanBuilder() {
        }

        public static QueryPlanBuilder newBuilder() {
            return new QueryPlanBuilder();
        }

        public QueryPlanBuilder persistedQuery(Query persistedQuery) {
            this.persistedQuery = persistedQuery;
            return this;
        }

        public QueryPlanBuilder nonPersistedQuery(Query nonPersistedQuery) {
            this.nonPersistedQuery = nonPersistedQuery;
            return this;
        }

        public QueryPlan build() {
            return new QueryPlan(persistedQuery, nonPersistedQuery);
        }
    }
}
