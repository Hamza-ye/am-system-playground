package org.nmcpye.activitiesmanagement.extended.webapi.controller.event.webrequest;

import lombok.With;
import org.apache.commons.lang3.StringUtils;
import org.nmcpye.activitiesmanagement.extended.webapi.controller.event.mapper.OrderParam;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * This class is used as a container for order parameters and is deserialized
 * from web requests
 */
@With
public class OrderCriteria {
    private String field;

    private OrderParam.SortDirection direction;

    public OrderCriteria() {
    }

    public OrderCriteria(String field, OrderParam.SortDirection direction) {
        this.field = field;
        this.direction = direction;
    }

    public static OrderCriteria of(String field, OrderParam.SortDirection direction) {
        return new OrderCriteria(field, direction);
    }

    public String getField() {
        return field;
    }

    public OrderParam.SortDirection getDirection() {
        return direction;
    }

    public OrderParam toOrderParam() {
        return OrderParam.builder()
            .field(field)
            .direction(direction)
            .build();
    }

    public static List<OrderCriteria> fromOrderString(String source) {
        return Optional.of(source)
            .filter(StringUtils::isNotBlank)
            .map(String::trim)
            .map(OrderCriteria::toOrderCriterias)
            .orElse(Collections.emptyList());
    }

    private static List<OrderCriteria> toOrderCriterias(String s) {
        return Arrays.stream(s.split(","))
            .map(OrderCriteria::toOrderCriteria)
            .collect(Collectors.toList());
    }

    private static OrderCriteria toOrderCriteria(String s1) {
        String[] props = s1.split(":");
        if (props.length == 2) {
            return OrderCriteria.of(props[0], OrderParam.SortDirection.of(props[1]));
        }
        if (props.length == 1) {
            return OrderCriteria.of(props[0], OrderParam.SortDirection.ASC);
        }
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OrderCriteria that = (OrderCriteria) o;

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
        return "OrderCriteria{" +
            "field='" + field + '\'' +
            ", direction=" + direction +
            '}';
    }
}
