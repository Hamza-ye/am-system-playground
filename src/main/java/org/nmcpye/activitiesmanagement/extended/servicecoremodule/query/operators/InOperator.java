package org.nmcpye.activitiesmanagement.extended.servicecoremodule.query.operators;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.nmcpye.activitiesmanagement.extended.schema.Property;
import org.nmcpye.activitiesmanagement.extended.servicecoremodule.query.Type;
import org.nmcpye.activitiesmanagement.extended.servicecoremodule.query.Typed;
import org.nmcpye.activitiesmanagement.extended.servicecoremodule.query.planner.QueryPath;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Collection;
import java.util.Date;

public class InOperator<T extends Comparable<? super T>> extends Operator<T> {
    public InOperator(Collection<T> arg) {
        super("in", Typed.from(Collection.class), arg);
    }

    public InOperator(String name, Collection<T> arg) {
        super(name, Typed.from(Collection.class), arg);
    }

    @Override
    public Criterion getHibernateCriterion(QueryPath queryPath) {
        Property property = queryPath.getProperty();

        if (property.isCollection()) {
            return Restrictions.in(queryPath.getPath(),
                getValue(Collection.class, queryPath.getProperty().getItemKlass(), args.get(0)));
        }

        return Restrictions.in(queryPath.getPath(),
            getValue(Collection.class, queryPath.getProperty().getKlass(), args.get(0)));
    }

    @Override
    public <Y> Predicate getPredicate(CriteriaBuilder builder, Root<Y> root, QueryPath queryPath) {
        Property property = queryPath.getProperty();

        if (property.isCollection()) {
            return root.get(queryPath.getPath()).in(
                getValue(Collection.class, queryPath.getProperty().getItemKlass(), getCollectionArgs().get(0)));
        }

        return root.get(queryPath.getPath()).in(getCollectionArgs().get(0));
    }

    @Override
    public boolean test(Object value) {
        Collection<?> items = getValue(Collection.class);

        if (items == null || value == null) {
            return false;
        }

        if (Collection.class.isInstance(value)) {
            Collection<?> valueItems = (Collection<?>) value;

            for (Object item : items) {
                if (compareCollection(item, valueItems)) {
                    return true;
                }
            }
        } else {
            if (compareCollection(value, items)) {
                return true;
            }
        }

        return false;
    }

    private boolean compareCollection(Object value, Collection<?> items) {
        Type type = new Type(value);

        for (Object item : items) {
            if (compare(type, item, value)) {
                return true;
            }
        }

        return false;
    }

    private boolean compare(Type type, Object lside, Object rside) {
        if (type.isString()) {
            String s1 = getValue(String.class, lside);
            String s2 = (String) rside;

            return s1 != null && s2.equals(s1);
        } else if (type.isBoolean()) {
            Boolean s1 = getValue(Boolean.class, lside);
            Boolean s2 = (Boolean) rside;

            return s1 != null && s2.equals(s1);
        } else if (type.isInteger()) {
            Integer s1 = getValue(Integer.class, lside);
            Integer s2 = (Integer) rside;

            return s1 != null && s2.equals(s1);
        } else if (type.isFloat()) {
            Float s1 = getValue(Float.class, lside);
            Float s2 = (Float) rside;

            return s1 != null && s2.equals(s1);
        } else if (type.isDate()) {
            Date s1 = getValue(Date.class, lside);
            Date s2 = (Date) rside;

            return s1 != null && s2.equals(s1);
        } else if (type.isEnum()) {
            String s1 = String.valueOf(lside);
            String s2 = String.valueOf(rside);

            return s1 != null && s2.equals(s1);
        }

        return false;
    }
}
