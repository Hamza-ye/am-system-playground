package org.nmcpye.activitiesmanagement.extended.servicecoremodule.query.operators;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.nmcpye.activitiesmanagement.extended.servicecoremodule.query.Type;
import org.nmcpye.activitiesmanagement.extended.servicecoremodule.query.Typed;
import org.nmcpye.activitiesmanagement.extended.servicecoremodule.query.planner.QueryPath;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Collection;
import java.util.Date;

public class BetweenOperator<T extends Comparable<? super T>> extends Operator<T> {
    public BetweenOperator(T arg0, T arg1) {
        super("between", Typed.from(String.class, Number.class, Date.class), arg0, arg1);
    }

    @Override
    public Criterion getHibernateCriterion(QueryPath queryPath) {
        return Restrictions.between(queryPath.getPath(), args.get(0), args.get(1));
    }

    @Override
    public <Y> Predicate getPredicate(CriteriaBuilder builder, Root<Y> root, QueryPath queryPath) {
        return builder.between(root.get(queryPath.getPath()), getArgs().get(0), getArgs().get(1));
    }

    @Override
    public boolean test(Object value) {
        if (args.isEmpty() || value == null) {
            return false;
        }

        Type type = new Type(value);

        if (type.isInteger()) {
            Integer s1 = getValue(Integer.class, value);
            Integer min = getValue(Integer.class, 0);
            Integer max = getValue(Integer.class, 1);

            return s1 >= min && s1 <= max;
        } else if (type.isFloat()) {
            Float s1 = getValue(Float.class, value);
            Integer min = getValue(Integer.class, 0);
            Integer max = getValue(Integer.class, 1);

            return s1 >= min && s1 <= max;
        } else if (type.isDate()) {
            Date min = getValue(Date.class, 0);
            Date max = getValue(Date.class, 1);
            Date s2 = (Date) value;

            return (s2.equals(min) || s2.after(min)) && (s2.before(max) || s2.equals(max));
        } else if (type.isCollection()) {
            Collection<?> collection = (Collection<?>) value;
            Integer min = getValue(Integer.class, 0);
            Integer max = getValue(Integer.class, 1);

            return collection.size() >= min && collection.size() <= max;
        }

        return false;
    }
}
