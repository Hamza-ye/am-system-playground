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

public class EmptyOperator<T extends Comparable<? super T>> extends Operator<T> {
    public EmptyOperator() {
        super("empty", Typed.from(Collection.class));
    }

    @Override
    public Criterion getHibernateCriterion(QueryPath queryPath) {
        return Restrictions.sizeEq(queryPath.getPath(), 0);
    }

    @Override
    public <Y> Predicate getPredicate(CriteriaBuilder builder, Root<Y> root, QueryPath queryPath) {
        return builder.equal(builder.size(root.get(queryPath.getPath())), 0);
    }

    @Override
    public boolean test(Object value) {
        if (value == null) {
            return false;
        }

        Type type = new Type(value);

        if (type.isCollection()) {
            Collection<?> collection = (Collection<?>) value;
            return collection.isEmpty();
        }

        return false;
    }
}
