package org.nmcpye.activitiesmanagement.extended.servicecoremodule.query.operators;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.nmcpye.activitiesmanagement.extended.schema.Property;
import org.nmcpye.activitiesmanagement.extended.servicecoremodule.query.planner.QueryPath;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Collection;

public class NotInOperator<T extends Comparable<? super T>> extends InOperator<T> {
    public NotInOperator(Collection<T> arg) {
        super("!in", arg);
    }

    @Override
    public Criterion getHibernateCriterion(QueryPath queryPath) {
        return Restrictions.not(super.getHibernateCriterion(queryPath));
    }

    @Override
    public <Y> Predicate getPredicate(CriteriaBuilder builder, Root<Y> root, QueryPath queryPath) {
        Property property = queryPath.getProperty();

        if (property.isCollection()) {
            return builder.not(root.get(queryPath.getPath()).in(
                getValue(Collection.class, queryPath.getProperty().getItemKlass(), getCollectionArgs().get(0))));
        }

        return builder.not(root.get(queryPath.getPath()).in(getCollectionArgs().get(0)));
    }

    @Override
    public boolean test(Object value) {
        return !super.test(value);
    }
}
