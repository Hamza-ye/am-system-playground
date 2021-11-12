package org.nmcpye.activitiesmanagement.extended.servicecoremodule.query.operators;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.nmcpye.activitiesmanagement.extended.servicecoremodule.query.Typed;
import org.nmcpye.activitiesmanagement.extended.servicecoremodule.query.planner.QueryPath;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Date;

public class NotNullOperator<T extends Comparable<? super T>> extends Operator<T> {
    public NotNullOperator() {
        super("!null", Typed.from(String.class, Boolean.class, Number.class, Date.class, Enum.class));
    }

    @Override
    public Criterion getHibernateCriterion(QueryPath queryPath) {
        return Restrictions.isNotNull(queryPath.getPath());
    }

    @Override
    public <Y> Predicate getPredicate(CriteriaBuilder builder, Root<Y> root, QueryPath queryPath) {
        return builder.isNotNull(root.get(queryPath.getPath()));
    }

    @Override
    public boolean test(Object value) {
        return value != null;
    }
}
