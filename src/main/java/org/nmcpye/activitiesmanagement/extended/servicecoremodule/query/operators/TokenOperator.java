package org.nmcpye.activitiesmanagement.extended.servicecoremodule.query.operators;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.nmcpye.activitiesmanagement.extended.hibernatemodule.hibernate.jsonb.type.JsonbFunctions;
import org.nmcpye.activitiesmanagement.extended.servicecoremodule.query.Typed;
import org.nmcpye.activitiesmanagement.extended.servicecoremodule.query.planner.QueryPath;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class TokenOperator<T extends Comparable<? super T>> extends Operator<T> {
    private final boolean caseSensitive;

    private final org.hibernate.criterion.MatchMode matchMode;

    public TokenOperator(T arg, boolean caseSensitive, MatchMode matchMode) {
        super("token", Typed.from(String.class), arg);
        this.caseSensitive = caseSensitive;
        this.matchMode = getMatchMode(matchMode);
    }

    @Override
    public Criterion getHibernateCriterion(QueryPath queryPath) {
        String value = caseSensitive ? getValue(String.class) : getValue(String.class).toLowerCase();

        return Restrictions
            .sqlRestriction("c_." + queryPath.getPath() + " ~* '" + TokenUtils.createRegex(value) + "'");
    }

    @Override
    public <Y> Predicate getPredicate(CriteriaBuilder builder, Root<Y> root, QueryPath queryPath) {
        String value = caseSensitive ? getValue(String.class) : getValue(String.class).toLowerCase();

        return builder
            .equal(builder.function(JsonbFunctions.REGEXP_SEARCH, Boolean.class, root.get(queryPath.getPath()),
                builder.literal(TokenUtils.createRegex(value).toString())), true);
    }

    @Override
    public boolean test(Object value) {
        return TokenUtils.test(value, getValue(String.class), caseSensitive, matchMode);
    }
}
