package org.nmcpye.activitiesmanagement.extended.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.MoreObjects;
import org.nmcpye.activitiesmanagement.extended.analytics.AggregationType;
import org.nmcpye.activitiesmanagement.extended.analytics.QueryKey;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class BaseDimensionalObject extends BaseNameableObject implements DimensionalObject {

    /**
     * The type of this dimension.
     */
    private DimensionType dimensionType;

    //    /**
    //     * The data dimension type of this dimension. Can be null. Only applicable for
    //     * {@link DimensionType#CATEGORY}.
    //     */
    //    protected DataDimensionType dataDimensionType;

    /**
     * Indicates whether this object should be handled as a data dimension.
     */
    protected boolean dataDimension = true;

    /**
     * The name of this dimension. For the dynamic dimensions this will be equal
     * to dimension identifier. For the period dimension, this will reflect the
     * period type. For the org unit dimension, this will reflect the level.
     */
    private String dimensionName;

    /**
     * The dimensional items for this dimension.
     */
    private List<DimensionalItemObject> items = new ArrayList<>();

    /**
     * Indicates whether all available items in this dimension are included.
     */
    private boolean allItems;

    //    /**
    //     * The legend set for this dimension.
    //     */
    //    protected LegendSet legendSet;
    //
    //    /**
    //     * The program stage for this dimension.
    //     */
    //    private ProgramStage programStage;

    /**
     * The aggregation type for this dimension.
     */
    protected AggregationType aggregationType;

    /**
     * Filter. Applicable for events. Contains operator and filter on this format:
     * <operator>:<filter>;<operator>:<filter>
     * Operator and filter pairs can be repeated any number of times.
     */
    private String filter;

    /**
     * A {@link DimensionalKeywords} defines a pre-defined group of items. For instance,
     * all the OU withing a district
     */
    private DimensionalKeywords dimensionalKeywords;

    /**
     * Indicates whether this dimension is fixed, meaning that the name of the
     * dimension will be returned as is for all dimension items in the response.
     */
    private boolean fixed;

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    // TODO displayName collides with translation solution, rename

    public BaseDimensionalObject() {}

    public BaseDimensionalObject(String dimension) {
        this.uid = dimension;
    }

    public BaseDimensionalObject(String dimension, DimensionType dimensionType, List<? extends DimensionalItemObject> items) {
        this.uid = dimension;
        this.dimensionType = dimensionType;
        this.items = new ArrayList<>(items);
    }

    public BaseDimensionalObject(
        String dimension,
        DimensionType dimensionType,
        String displayName,
        List<? extends DimensionalItemObject> items
    ) {
        this(dimension, dimensionType, items);
        this.displayName = displayName;
    }

    public BaseDimensionalObject(
        String dimension,
        DimensionType dimensionType,
        String dimensionName,
        String displayName,
        List<? extends DimensionalItemObject> items
    ) {
        this(dimension, dimensionType, items);
        this.dimensionName = dimensionName;
        this.displayName = displayName;
    }

    public BaseDimensionalObject(
        String dimension,
        DimensionType dimensionType,
        String dimensionName,
        String displayName,
        DimensionalKeywords dimensionalKeywords,
        List<? extends DimensionalItemObject> items
    ) {
        this(dimension, dimensionType, items);
        this.dimensionName = dimensionName;
        this.displayName = displayName;
        this.dimensionalKeywords = dimensionalKeywords;
    }

    public BaseDimensionalObject(
        String dimension,
        DimensionType dimensionType,
        String dimensionName,
        String displayName,
        List<? extends DimensionalItemObject> items,
        boolean allItems
    ) {
        this(dimension, dimensionType, dimensionName, displayName, items);
        this.allItems = allItems;
    }

    public BaseDimensionalObject(String dimension, DimensionType dimensionType, String dimensionName, String displayName, String filter) {
        this.uid = dimension;
        this.dimensionType = dimensionType;
        this.dimensionName = dimensionName;
        this.displayName = displayName;
        //        this.legendSet = legendSet;
        //        this.programStage = programStage;
        this.filter = filter;
    }

    // TODO aggregationType in constructors

    // -------------------------------------------------------------------------
    // Logic
    // -------------------------------------------------------------------------

    public DimensionalObject instance() {
        BaseDimensionalObject object = new BaseDimensionalObject(
            this.uid,
            this.dimensionType,
            this.dimensionName,
            this.displayName,
            this.items,
            this.allItems
        );

        //        object.legendSet = this.legendSet;
        object.aggregationType = this.aggregationType;
        object.filter = this.filter;
        object.dataDimension = this.dataDimension;
        object.fixed = this.fixed;
        object.dimensionalKeywords = this.dimensionalKeywords;
        return object;
    }

    @Override
    public boolean hasItems() {
        return !getItems().isEmpty();
    }

    //    @Override
    //    public boolean hasLegendSet()
    //    {
    //        return getLegendSet() != null;
    //    }
    //
    //    @Override
    //    public boolean hasProgramStage()
    //    {
    //        return getProgramStage() != null;
    //    }

    @Override
    public String getDimensionName() {
        return dimensionName != null ? dimensionName : uid;
    }

    //    @Override
    //    public AnalyticsType getAnalyticsType()
    //    {
    //        return
    //            DimensionType.PROGRAM_ATTRIBUTE.equals( dimensionType ) ||
    //                DimensionType.PROGRAM_DATA_ELEMENT.equals( dimensionType ) ?
    //                AnalyticsType.EVENT : AnalyticsType.AGGREGATE;
    //    }

    /**
     * Returns the items in the filter as a list. Order of items are preserved.
     * Requires that the filter has the IN operator and that at least one item
     * is specified in the filter, returns null if not.
     */
    public List<String> getFilterItemsAsList() {
        final String inOp = QueryOperator.IN.getValue().toLowerCase();
        final int opLen = inOp.length() + 1;

        if (filter == null || !filter.toLowerCase().startsWith(inOp) || filter.length() < opLen) {
            return null;
        }

        String filterItems = filter.substring(opLen, filter.length());

        return new ArrayList<>(Arrays.asList(filterItems.split(DimensionalObject.OPTION_SEP)));
    }

    @Override
    public String getKey() {
        QueryKey key = new QueryKey();

        key.add("dimension", getDimension());
        getItems().forEach(e -> key.add("item", e.getDimensionItem()));

        return key
            .add("allItems", allItems)
            //            .addIgnoreNull( "legendSet", legendSet )
            .addIgnoreNull("aggregationType", aggregationType)
            .addIgnoreNull("filter", filter)
            .asPlainKey();
    }

    //--------------------------------------------------------------------------
    // Getters and setters
    //--------------------------------------------------------------------------

    @Override
    @JsonProperty
    public String getDimension() {
        return uid;
    }

    public void setDimension(String dimension) {
        this.uid = dimension;
    }

    @Override
    @JsonProperty
    public DimensionType getDimensionType() {
        return dimensionType;
    }

    public void setDimensionType(DimensionType dimensionType) {
        this.dimensionType = dimensionType;
    }

    //    @Override
    //    @JsonProperty
    //    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    //    public DataDimensionType getDataDimensionType()
    //    {
    //        return dataDimensionType;
    //    }

    //    public void setDataDimensionType( DataDimensionType dataDimensionType )
    //    {
    //        this.dataDimensionType = dataDimensionType;
    //    }

    public void setDimensionName(String dimensionName) {
        this.dimensionName = dimensionName;
    }

    @Override
    @JsonProperty
    public boolean isDataDimension() {
        return dataDimension;
    }

    public void setDataDimension(boolean dataDimension) {
        this.dataDimension = dataDimension;
    }

    @Override
    @JsonProperty
    @JsonDeserialize(contentAs = BaseDimensionalItemObject.class)
    public List<DimensionalItemObject> getItems() {
        return items;
    }

    public void setItems(List<DimensionalItemObject> items) {
        this.items = items;
    }

    @Override
    @JsonProperty
    public boolean isAllItems() {
        return allItems;
    }

    public void setAllItems(boolean allItems) {
        this.allItems = allItems;
    }

    //    @Override
    //    @JsonProperty
    //    @JsonSerialize( as = BaseIdentifiableObject.class )
    //    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    //    public LegendSet getLegendSet()
    //    {
    //        return legendSet;
    //    }

    //    public void setLegendSet( LegendSet legendSet )
    //    {
    //        this.legendSet = legendSet;
    //    }

    //    @Override
    //    @JsonProperty
    //    @JsonSerialize( as = BaseIdentifiableObject.class )
    //    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    //    public ProgramStage getProgramStage()
    //    {
    //        return programStage;
    //    }
    //
    //    public void setProgramStage( ProgramStage programStage )
    //    {
    //        this.programStage = programStage;
    //    }

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
    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    @Override
    @JsonIgnore
    public boolean isFixed() {
        return fixed;
    }

    public void setFixed(boolean fixed) {
        this.fixed = fixed;
    }

    @Override
    @JsonProperty
    public DimensionalKeywords getDimensionalKeywords() {
        return this.dimensionalKeywords;
    }

    public void setDimensionalKeywords(DimensionalKeywords dimensionalKeywords) {
        this.dimensionalKeywords = dimensionalKeywords;
    }

    @Override
    public String toString() {
        List<String> itemStr = items
            .stream()
            .map(
                item ->
                    MoreObjects.toStringHelper(DimensionalItemObject.class).add("uid", item.getUid()).add("name", item.getName()).toString()
            )
            .collect(Collectors.toList());

        return MoreObjects
            .toStringHelper(this)
            .add("Dimension", uid)
            .add("type", dimensionType)
            .add("display name", displayName)
            .add("items", itemStr)
            .toString();
    }
}
