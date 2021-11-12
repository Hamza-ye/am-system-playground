package org.nmcpye.activitiesmanagement.extended.servicecoremodule.query.operators;

import org.hibernate.criterion.Criterion;
import org.nmcpye.activitiesmanagement.extended.servicecoremodule.query.*;
import org.nmcpye.activitiesmanagement.extended.servicecoremodule.query.planner.QueryPath;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public abstract class Operator<T extends Comparable<? super T>> {
    protected final String name;

    protected final List<T> args = new ArrayList<>();

    protected final List<Collection<T>> collectionArgs = new ArrayList<>();

    protected final Typed typed;

    protected Class<T> klass;

    protected Type argumentType;

    public Operator(String name, Typed typed) {
        this.name = name;
        this.typed = typed;
    }

    public Operator(String name, Typed typed, Collection<T> collectionArg) {
        this(name, typed);
        this.argumentType = new Type(collectionArg);
        this.collectionArgs.add(collectionArg);
    }

    @SafeVarargs
    public Operator(String name, Typed typed, Collection<T>... collectionArgs) {
        this(name, typed);
        this.argumentType = new Type(collectionArgs[0]);
        Collections.addAll(this.collectionArgs, collectionArgs);
    }

    public Operator(String name, Typed typed, T arg) {
        this(name, typed);
        this.argumentType = new Type(arg);
        this.args.add(arg);
        validate();
    }

    @SafeVarargs
    public Operator(String name, Typed typed, T... args) {
        this(name, typed);
        this.argumentType = new Type(args[0]);
        Collections.addAll(this.args, args);
    }

    private void validate() {
        for (Object arg : args) {
            if (!isValid(arg.getClass())) {
                throw new QueryParserException("Value `" + args.get(0) + "` of type `"
                    + arg.getClass().getSimpleName() + "` is not supported by this operator.");
            }
        }
    }

    public List<T> getArgs() {
        return args;
    }

    public List<Collection<T>> getCollectionArgs() {
        return collectionArgs;
    }

    protected <S> S getValue(Class<S> klass, Class<?> secondaryClass, int idx) {
        if (Collection.class.isAssignableFrom(klass)) {
            return QueryUtils.parseValue(klass, secondaryClass, getCollectionArgs().get(idx));
        }

        return QueryUtils.parseValue(klass, secondaryClass, args.get(idx));
    }

    protected <S> S getValue(Class<S> klass, int idx) {
        if (Collection.class.isAssignableFrom(klass)) {
            return QueryUtils.parseValue(klass, null, getCollectionArgs().get(idx));
        }

        return QueryUtils.parseValue(klass, null, args.get(idx));
    }

    protected <S> S getValue(Class<S> klass) {
        if (Collection.class.isAssignableFrom(klass)) {
            return QueryUtils.parseValue(klass, null, getCollectionArgs().get(0));
        }

        return getValue(klass, 0);
    }

    protected <S> T getValue(Class<S> klass, Class<?> secondaryClass, Object value) {
        return QueryUtils.parseValue(klass, secondaryClass, value);
    }

    protected <S> S getValue(Class<S> klass, Object value) {
        return QueryUtils.parseValue(klass, value);
    }

    public boolean isValid(Class<?> klass) {
        return typed.isValid(klass);
    }

    public abstract Criterion getHibernateCriterion(QueryPath queryPath);

    public abstract <Y> Predicate getPredicate(CriteriaBuilder builder, Root<Y> root, QueryPath queryPath);

    public abstract boolean test(Object value);

    org.hibernate.criterion.MatchMode getMatchMode(MatchMode matchMode) {
        switch (matchMode) {
            case EXACT:
                return org.hibernate.criterion.MatchMode.EXACT;
            case START:
                return org.hibernate.criterion.MatchMode.START;
            case END:
                return org.hibernate.criterion.MatchMode.END;
            case ANYWHERE:
                return org.hibernate.criterion.MatchMode.ANYWHERE;
            default:
                return null;
        }
    }

    protected JpaQueryUtils.StringSearchMode getJpaMatchMode(MatchMode matchMode) {
        switch (matchMode) {
            case EXACT:
                return JpaQueryUtils.StringSearchMode.EQUALS;
            case START:
                return JpaQueryUtils.StringSearchMode.STARTING_LIKE;
            case END:
                return JpaQueryUtils.StringSearchMode.ENDING_LIKE;
            case ANYWHERE:
                return JpaQueryUtils.StringSearchMode.ANYWHERE;
            default:
                return null;
        }
    }

    /**
     * Get JPA String search mode for NOT LIKE match mode.
     *
     * @param matchMode {@link MatchMode}
     * @return {@link JpaQueryUtils.StringSearchMode} used for generating JPA
     * Api Query
     */
    protected JpaQueryUtils.StringSearchMode getNotLikeJpaMatchMode(MatchMode matchMode) {
        switch (matchMode) {
            case EXACT:
                return JpaQueryUtils.StringSearchMode.NOT_EQUALS;
            case START:
                return JpaQueryUtils.StringSearchMode.NOT_STARTING_LIKE;
            case END:
                return JpaQueryUtils.StringSearchMode.NOT_ENDING_LIKE;
            case ANYWHERE:
                return JpaQueryUtils.StringSearchMode.NOT_ANYWHERE;
            default:
                return null;
        }
    }

    @Override
    public String toString() {
        return "[" + name + ", args: " + args + "]";
    }
}
