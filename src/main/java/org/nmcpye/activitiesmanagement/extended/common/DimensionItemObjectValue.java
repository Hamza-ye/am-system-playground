package org.nmcpye.activitiesmanagement.extended.common;

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
