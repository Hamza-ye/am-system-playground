package org.nmcpye.activitiesmanagement.extended.webapi.controller.event.mapper;

import java.util.Arrays;

/**
 * Order parameter container to use within services.
 */
public class OrderParam {
    private final String field;

    private final SortDirection direction;

    public OrderParam(OrderParamBuilder orderParamBuilder) {
        this.field = orderParamBuilder.field;
        this.direction = orderParamBuilder.direction;
    }

    public static OrderParamBuilder builder() {
        return new OrderParamBuilder();
    }

    public String getField() {
        return field;
    }

    public SortDirection getDirection() {
        return direction;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OrderParam that = (OrderParam) o;

        if (field != null ? !field.equals(that.field) : that.field != null) return false;
        return direction == that.direction;
    }

    @Override
    public int hashCode() {
        int result = field != null ? field.hashCode() : 0;
        result = 31 * result + (direction != null ? direction.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "OrderParam{" +
            "field='" + field + '\'' +
            ", direction=" + direction +
            '}';
    }

    public enum SortDirection {
        ASC("asc", false),
        DESC("desc", false),
        IASC("iasc", true),
        IDESC("idesc", true);

        private static final SortDirection DEFAULT_SORTING_DIRECTION = ASC;

        private final String value;

        private final boolean ignoreCase;

        SortDirection(String value, boolean ignoreCase) {
            this.value = value;
            this.ignoreCase = ignoreCase;
        }

        public static SortDirection of(String value) {
            return of(value, DEFAULT_SORTING_DIRECTION);
        }

        public static SortDirection of(String value, SortDirection defaultSortingDirection) {
            return Arrays.stream(values())
                .filter(sortDirection -> sortDirection.getValue().equalsIgnoreCase(value))
                .findFirst()
                .orElse(defaultSortingDirection);
        }

        public boolean isAscending() {
            return this.equals(ASC) || this.equals(IASC);
        }

        public static SortDirection getDefaultSortingDirection() {
            return DEFAULT_SORTING_DIRECTION;
        }

        public String getValue() {
            return value;
        }

        public boolean isIgnoreCase() {
            return ignoreCase;
        }
    }

    public static class OrderParamBuilder {

        private String field;

        private SortDirection direction;

        public OrderParamBuilder field(String field) {
            this.field = field;
            return this;
        }

        public OrderParamBuilder direction(SortDirection direction) {
            this.direction = direction;
            return this;
        }

        public OrderParam build() {
            OrderParam user = new OrderParam(this);
            validateUserObject(user);
            return user;
        }

        private void validateUserObject(OrderParam user) {
            //Do some basic validations to check
            //if OrderParam object does not break any assumption of system
        }
    }
}
