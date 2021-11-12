package org.nmcpye.activitiesmanagement.extended.servicecoremodule.query.operators;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.nmcpye.activitiesmanagement.extended.servicecoremodule.query.JpaQueryUtils;
import org.nmcpye.activitiesmanagement.extended.servicecoremodule.query.Type;
import org.nmcpye.activitiesmanagement.extended.servicecoremodule.query.Typed;
import org.nmcpye.activitiesmanagement.extended.servicecoremodule.query.planner.QueryPath;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class NotLikeOperator<T extends Comparable<? super T>> extends Operator<T> {
    private final boolean caseSensitive;

    private final JpaQueryUtils.StringSearchMode jpaMatchMode;

    private final org.hibernate.criterion.MatchMode matchMode;

    public NotLikeOperator(T arg, boolean caseSensitive, org.nmcpye.activitiesmanagement.extended.servicecoremodule.query.operators.MatchMode matchMode) {
        super("!like", Typed.from(String.class), arg);
        this.caseSensitive = caseSensitive;
        this.matchMode = getMatchMode(matchMode);
        this.jpaMatchMode = getNotLikeJpaMatchMode(matchMode);
    }

    @Override
    public Criterion getHibernateCriterion(QueryPath queryPath) {
        if (caseSensitive) {
            return Restrictions.like(queryPath.getPath(), String.valueOf(args.get(0)).replace("%", "\\%"),
                matchMode);
        } else {
            return Restrictions.ilike(queryPath.getPath(), String.valueOf(args.get(0)).replace("%", "\\%"),
                matchMode);
        }
    }

    @Override
    public <Y> Predicate getPredicate(CriteriaBuilder builder, Root<Y> root, QueryPath queryPath) {
        if (caseSensitive) {
            return JpaQueryUtils.stringPredicateCaseSensitive(builder, root.get(queryPath.getPath()),
                String.valueOf(args.get(0)).replace("%", ""),
                jpaMatchMode);
        } else {
            return JpaQueryUtils.stringPredicateIgnoreCase(builder, root.get(queryPath.getPath()),
                String.valueOf(args.get(0)).replace("%", ""),
                jpaMatchMode);
        }
    }

    @Override
    public boolean test(Object value) {
        if (args.isEmpty() || value == null) {
            return false;
        }

        Type type = new Type(value);

        if (type.isString()) {
            String s1 = caseSensitive ? getValue(String.class) : getValue(String.class).toLowerCase();
            String s2 = caseSensitive ? (String) value : ((String) value).toLowerCase();

            switch (jpaMatchMode) {
                case NOT_EQUALS:
                    return !s2.equals(s1);
                case NOT_STARTING_LIKE:
                    return !s2.startsWith(s1);
                case NOT_ENDING_LIKE:
                    return !s2.endsWith(s1);
                case NOT_ANYWHERE:
                    return !s2.contains(s1);
                default:
                    return false;
            }
        }

        return false;
    }
}
