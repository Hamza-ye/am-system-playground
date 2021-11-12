package org.nmcpye.activitiesmanagement.extended.servicecoremodule.query;

import org.nmcpye.activitiesmanagement.extended.servicecoremodule.query.operators.*;

import java.util.Collection;

public final class Restrictions {
    public static <T extends Comparable<? super T>> Restriction eq(String path, T value) {
        return new Restriction(path, new EqualOperator<>(value));
    }

    public static <T extends Comparable<? super T>> Restriction ne(String path, T value) {
        return new Restriction(path, new NotEqualOperator<>(value));
    }

    public static <T extends Comparable<? super T>> Restriction gt(String path, T value) {
        return new Restriction(path, new GreaterThanOperator<>(value));
    }

    public static <T extends Comparable<? super T>> Restriction lt(String path, T value) {
        return new Restriction(path, new LessThanOperator<>(value));
    }

    public static <T extends Comparable<? super T>> Restriction ge(String path, T value) {
        return new Restriction(path, new GreaterEqualOperator<>(value));
    }

    public static <T extends Comparable<? super T>> Restriction le(String path, T value) {
        return new Restriction(path, new LessEqualOperator<>(value));
    }

    public static <T extends Comparable<? super T>> Restriction between(String path, T lside, T rside) {
        return new Restriction(path, new BetweenOperator<>(lside, rside));
    }

    public static <T extends Comparable<? super T>> Restriction like(String path, T value, MatchMode matchMode) {
        return new Restriction(path, new LikeOperator<>(value, true, matchMode));
    }

    public static <T extends Comparable<? super T>> Restriction notLike(String path, T value, MatchMode matchMode) {
        return new Restriction(path, new NotLikeOperator<>(value, true, matchMode));
    }

    public static <T extends Comparable<? super T>> Restriction ilike(String path, T value, MatchMode matchMode) {
        return new Restriction(path, new LikeOperator<>(value, false, matchMode));
    }

    public static <T extends Comparable<? super T>> Restriction notIlike(String path, T value, MatchMode matchMode) {
        return new Restriction(path, new NotLikeOperator<>(value, false, matchMode));
    }

    public static <T extends Comparable<? super T>> Restriction token(String path, T value, MatchMode matchMode) {
        return new Restriction(path, new TokenOperator<>(value, false, matchMode));
    }

    public static <T extends Comparable<? super T>> Restriction notToken(String path, T value, MatchMode matchMode) {
        return new Restriction(path, new NotTokenOperator<>(value, false, matchMode));
    }

    public static <T extends Comparable<? super T>> Restriction in(String path, Collection<T> values) {
        return new Restriction(path, new InOperator<>(values));
    }

    public static <T extends Comparable<? super T>> Restriction notIn(String path, Collection<T> values) {
        return new Restriction(path, new NotInOperator<>(values));
    }

    public static Restriction isNull(String path) {
        return new Restriction(path, new NullOperator<>());
    }

    public static Restriction isNotNull(String path) {
        return new Restriction(path, new NotNullOperator<>());
    }

    public static Restriction isEmpty(String path) {
        return new Restriction(path, new EmptyOperator<>());
    }

    private Restrictions() {
    }
}
