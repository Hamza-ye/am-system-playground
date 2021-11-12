package org.nmcpye.activitiesmanagement.extended.servicecoremodule.query.operators;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.nmcpye.activitiesmanagement.extended.schema.Property;
import org.nmcpye.activitiesmanagement.extended.servicecoremodule.query.QueryException;
import org.nmcpye.activitiesmanagement.extended.servicecoremodule.query.QueryUtils;
import org.nmcpye.activitiesmanagement.extended.servicecoremodule.query.planner.QueryPath;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class NotEqualOperator<T extends Comparable<? super T>> extends EqualOperator<T> {
    public NotEqualOperator(T arg) {
        super("ne", arg);
    }

    @Override
    public Criterion getHibernateCriterion(QueryPath queryPath) {
        return Restrictions.not(super.getHibernateCriterion(queryPath));
    }

    @Override
    public <Y> Predicate getPredicate(CriteriaBuilder builder, Root<Y> root, QueryPath queryPath) {
        Property property = queryPath.getProperty();

        if (property.isCollection()) {
            Integer value = QueryUtils.parseValue(Integer.class, args.get(0));

            if (value == null) {
                throw new QueryException(
                    "Left-side is collection, and right-side is not a valid integer, so can't compare by size.");
            }

            return builder.notEqual(builder.size(root.get(queryPath.getPath())), value);
        }
        return builder.notEqual(root.get(queryPath.getPath()), args.get(0));
    }

    @Override
    public boolean test(Object value) {
        return !super.test(value);
    }
}
