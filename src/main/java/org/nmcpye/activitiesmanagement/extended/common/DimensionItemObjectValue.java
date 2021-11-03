package org.nmcpye.activitiesmanagement.extended.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class DimensionItemObjectValue {

    private final DimensionalItemObject dimensionalItemObject;

    private final Double value;

    public DimensionItemObjectValue(DimensionalItemObject dimensionalItemObject, Double value) {
        this.dimensionalItemObject = dimensionalItemObject;
        this.value = value;
    }

    public DimensionalItemObject getDimensionalItemObject() {
        return dimensionalItemObject;
    }

    public Double getValue() {
        return value;
    }
}
