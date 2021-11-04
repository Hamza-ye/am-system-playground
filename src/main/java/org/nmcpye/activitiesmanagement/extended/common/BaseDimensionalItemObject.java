package org.nmcpye.activitiesmanagement.extended.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.nmcpye.activitiesmanagement.extended.analytics.AggregationType;

//@MappedSuperclass
public class BaseDimensionalItemObject extends BaseNameableObject implements DimensionalItemObject {

    /**
     * The dimension type.
     */
    //    @Column(name = "dimension_item_type")
    private DimensionItemType dimensionItemType;

    /**
     * The aggregation type for this dimension.
     */
    //    @Column(name = "aggregation_type")
    protected AggregationType aggregationType;

    /**
     * A value representing a period offset that can be applied to Dimensional Item
     * Object within a Indicator formula
     */
    protected transient int periodOffset = 0;

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    public BaseDimensionalItemObject() {}

    public BaseDimensionalItemObject(String dimensionItem) {
        this.uid = dimensionItem;
        this.code = dimensionItem;
        this.name = dimensionItem;
    }

    // -------------------------------------------------------------------------
    // DimensionalItemObject
    // -------------------------------------------------------------------------

    @Override
    public boolean hasAggregationType() {
        return getAggregationType() != null;
    }

    @Override
    @JsonProperty
    public String getDimensionItem() {
        return getUid();
    }

    @Override
    public String getDimensionItem(IdScheme idScheme) {
        return getPropertyValue(idScheme);
    }

    @Override
    public TotalAggregationType getTotalAggregationType() {
        return TotalAggregationType.SUM;
    }

    // -------------------------------------------------------------------------
    // Get and set methods
    // -------------------------------------------------------------------------

    @Override
    @JsonProperty
    public DimensionItemType getDimensionItemType() {
        return dimensionItemType;
    }

    public void setDimensionItemType(DimensionItemType dimensionItemType) {
        this.dimensionItemType = dimensionItemType;
    }

    @Override
    @JsonProperty
    public AggregationType getAggregationType() {
        return aggregationType;
    }

    public void setAggregationType(AggregationType aggregationType) {
        this.aggregationType = aggregationType;
    }

    @Override
    @JsonProperty
    public int getPeriodOffset() {
        return periodOffset;
    }

    public void setPeriodOffset(int periodOffset) {
        this.periodOffset = periodOffset;
    }

    @Override
    public boolean equals(Object o) {
        if (!super.equals(o)) {
            return false;
        }

        final BaseDimensionalItemObject that = (BaseDimensionalItemObject) o;

        return periodOffset == that.periodOffset;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + periodOffset;
        return result;
    }
}
