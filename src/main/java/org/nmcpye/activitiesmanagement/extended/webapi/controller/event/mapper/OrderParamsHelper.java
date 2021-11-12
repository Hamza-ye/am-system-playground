package org.nmcpye.activitiesmanagement.extended.webapi.controller.event.mapper;

import org.nmcpye.activitiesmanagement.extended.webapi.controller.event.webrequest.OrderCriteria;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class OrderParamsHelper {
    private OrderParamsHelper() {
    }

    public static List<OrderParam> toOrderParams(List<OrderCriteria> criteria) {
        return Optional.ofNullable(criteria)
            .orElse(Collections.emptyList())
            .stream()
            .filter(Objects::nonNull)
            .map(orderCriteria -> OrderParam.builder()
                .direction(orderCriteria.getDirection())
                .field(orderCriteria.getField())
                .build())
            .collect(Collectors.toList());
    }
}
