package org.nmcpye.activitiesmanagement.extended.servicecoremodule.gist;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.nmcpye.activitiesmanagement.extended.common.PrimaryKeyObject;
import org.nmcpye.activitiesmanagement.extended.schema.annotation.Gist.Transform;
import org.nmcpye.activitiesmanagement.extended.servicecoremodule.common.NamedParams;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.function.BiFunction;

import static java.lang.StrictMath.abs;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

/**
 * Description of the gist query that should be run.
 * <p>
 * There are two essential types of queries:
 * <ul>
 * <li>owner property list query ({@link #owner} is non-null)</li>
 * <li>direct list query ({@link #owner} is null)</li>
 * </ul>
 */
public final class GistQuery {
    /**
     * Fields allow {@code property[sub,sub]} syntax where a comma occurs as
     * part of the property name. These commas need to be ignored when splitting
     * a {@code fields} parameter list.
     */
    private static final String FIELD_SPLIT = ",(?![^\\[\\]]*\\]|[^\\(\\)]*\\))";
    private final Owner owner;
    private final Class<? extends PrimaryKeyObject> elementType;
    @JsonProperty
    private final int pageOffset;
    @JsonProperty
    private final int pageSize;
    /**
     * Include total match count in pager? Default false.
     */
    @JsonProperty
    private final boolean total;
    private final String contextRoot;
    private final Locale translationLocale;
    /**
     * Not the elements contained in the collection but those not contained
     * (yet). Default false.
     */
    @JsonProperty
    private final boolean inverse;
    /**
     * Apply translations to translatable properties? Default true.
     */
    @JsonProperty
    private final boolean translate;
    /**
     * Use absolute URLs when referring to other APIs in pager and
     * {@code apiEndpoints}? Default false.
     */
    @JsonProperty
    private final boolean absoluteUrls;
    /**
     * Return plain result list (without pager wrapper). Default false.
     */
    @JsonProperty
    private final boolean headless;
    /**
     * Use OR instead of AND between filters so that any match for one of the
     * filters is a match. Default false.
     */
    @JsonProperty
    private final boolean anyFilter;
    /**
     * Mode where no actual query is performed - instead the output and query is
     * described similar to "SQL describe".
     */
    private final boolean describe;
    /**
     * Weather or not to include the API endpoints references
     */
    @JsonProperty
    private final boolean references;
    /**
     * The extend to which fields are included by default
     */
    @JsonProperty(value = "auto")
    private final GistAutoType autoType;
    /**
     * Names of those properties that should be included in the response.
     */
    @JsonProperty
    private final List<Field> fields;
    /**
     * List of filter property expressions. An expression has the format
     * {@code property:operator:value} or {@code property:operator}.
     */
    @JsonProperty
    private final List<Filter> filters;
    @JsonProperty
    private final List<Order> orders;

    private GistQuery(Owner owner, Class<? extends PrimaryKeyObject> elementType,
                      int pageOffset, int pageSize, boolean total,
                      String contextRoot, Locale translationLocale, boolean inverse,
                      boolean translate, boolean absoluteUrls, boolean headless, boolean anyFilter,
                      boolean describe, boolean references, GistAutoType autoType, List<Field> fields, List<Filter> filters, List<Order> orders) {
        this.owner = owner;
        this.elementType = elementType;
        this.pageOffset = pageOffset;
        this.pageSize = pageSize;
        this.total = total;
        this.contextRoot = contextRoot;
        this.translationLocale = translationLocale;
        this.inverse = inverse;
        this.translate = translate;
        this.absoluteUrls = absoluteUrls;
        this.headless = headless;
        this.anyFilter = anyFilter;
        this.describe = describe;
        this.references = references;
        this.autoType = autoType;
        this.fields = fields;
        this.filters = filters;
        this.orders = orders;
    }

    public GistQuery(GistQueryBuilder gistQueryBuilder) {
        this.owner = gistQueryBuilder.owner;
        this.elementType = gistQueryBuilder.elementType;
        this.pageOffset = gistQueryBuilder.pageOffset;
        this.pageSize = gistQueryBuilder.pageSize;
        this.total = gistQueryBuilder.total;
        this.contextRoot = gistQueryBuilder.contextRoot;
        this.translationLocale = gistQueryBuilder.translationLocale;
        this.inverse = gistQueryBuilder.inverse;
        this.translate = gistQueryBuilder.translate;
        this.absoluteUrls = gistQueryBuilder.absoluteUrls;
        this.headless = gistQueryBuilder.headless;
        this.anyFilter = gistQueryBuilder.anyFilter;
        this.describe = gistQueryBuilder.describe;
        this.references = gistQueryBuilder.references;
        this.autoType = gistQueryBuilder.autoType;
        this.fields = gistQueryBuilder.fields;
        this.filters = gistQueryBuilder.filters;
        this.orders = gistQueryBuilder.orders;
    }

    public GistQueryBuilder toBuilder() {
        return new GistQueryBuilder(owner, elementType, pageOffset, pageSize,
            total, contextRoot, translationLocale, inverse, translate,
            absoluteUrls, headless, anyFilter, describe, references,
            autoType, fields, filters, orders);
    }

    public static GistQueryBuilder builder() {
        return new GistQueryBuilder();
    }

    public Owner getOwner() {
        return owner;
    }

    public Class<? extends PrimaryKeyObject> getElementType() {
        return elementType;
    }

    public int getPageOffset() {
        return pageOffset;
    }

    public int getPageSize() {
        return pageSize;
    }

    public boolean isTotal() {
        return total;
    }

    public String getContextRoot() {
        return contextRoot;
    }

    public Locale getTranslationLocale() {
        return translationLocale;
    }

    public boolean isInverse() {
        return inverse;
    }

    public boolean isTranslate() {
        return translate;
    }

    public boolean isAbsoluteUrls() {
        return absoluteUrls;
    }

    public boolean isHeadless() {
        return headless;
    }

    public boolean isAnyFilter() {
        return anyFilter;
    }

    public boolean isDescribe() {
        return describe;
    }

    public boolean isReferences() {
        return references;
    }

    public GistAutoType getAutoType() {
        return autoType;
    }

    public List<Field> getFields() {
        return fields;
    }

    public List<Filter> getFilters() {
        return filters;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public List<String> getFieldNames() {
        return fields.stream().map(Field::getName).collect(toList());
    }

    public Transform getDefaultTransformation() {
        return getAutoType() == null ? Transform.AUTO : getAutoType().getDefaultTransformation();
    }

    public String getEndpointRoot() {
        return isAbsoluteUrls() ? getContextRoot() : "";
    }

    public boolean hasFilterGroups() {
        if (filters.size() <= 1) {
            return false;
        }
        int group0 = filters.get(0).group;
        return filters.stream().anyMatch(f -> f.group != group0);
    }

    public GistQuery with(NamedParams params) {
        int page = abs(params.getInt("page", 1));
        int size = Math.min(1000, abs(params.getInt("pageSize", 50)));
        return toBuilder().pageSize(size).pageOffset(Math.max(0, page - 1) * size)
            .translate(params.getBoolean("translate", true))
            .inverse(params.getBoolean("inverse", false))
            .total(params.getBoolean("total", false))
            .absoluteUrls(params.getBoolean("absoluteUrls", false))
            .headless(params.getBoolean("headless", false))
            .describe(params.getBoolean("describe", false))
            .references(params.getBoolean("references", true))
            .anyFilter(params.getString("rootJunction", "AND").equalsIgnoreCase("OR"))
            .fields(params.getStrings("fields", FIELD_SPLIT).stream()
                .map(Field::parse).collect(toList()))
            .filters(params.getStrings("filter", FIELD_SPLIT).stream().map(Filter::parse).collect(toList()))
            .orders(params.getStrings("order").stream().map(Order::parse).collect(toList()))
            .build();
    }

    public GistQuery withOwner(Owner owner) {
        return toBuilder().owner(owner).build();
    }

    public GistQuery withFilter(Filter filter) {
        return withAddedItem(filter, getFilters(), GistQueryBuilder::filters);
    }

    public GistQuery withOrder(Order order) {
        return withAddedItem(order, getOrders(), GistQueryBuilder::orders);
    }

    public GistQuery withField(String path) {
        return withAddedItem(new Field(path, getDefaultTransformation()), getFields(), GistQueryBuilder::fields);
    }

    public GistQuery withFields(List<Field> fields) {
        return toBuilder().fields(fields).build();
    }

    public GistQuery withFilters(List<Filter> filters) {
        return toBuilder().filters(filters).build();
    }

    private <E> GistQuery withAddedItem(E e, List<E> collection,
                                        BiFunction<GistQueryBuilder, List<E>, GistQueryBuilder> setter) {
        List<E> plus1 = new ArrayList<>(collection);
        plus1.add(e);
        return setter.apply(toBuilder(), plus1).build();
    }

    public enum Direction {
        ASC,
        DESC
    }

    public enum Comparison {
        // identity/numeric comparison
        NULL("null"),
        NOT_NULL("!null"),
        EQ("eq"),
        NE("!eq", "ne", "neq"),

        // numeric comparison
        LT("lt"),
        LE("le", "lte"),
        GT("gt"),
        GE("ge", "gte"),

        // collection operations
        IN("in"),
        NOT_IN("!in"),
        EMPTY("empty"),
        NOT_EMPTY("!empty"),

        // string comparison
        LIKE("like"),
        NOT_LIKE("!like"),
        STARTS_LIKE("$like"),
        NOT_STARTS_LIKE("!$like"),
        ENDS_LIKE("like$"),
        NOT_ENDS_LIKE("!like$"),
        ILIKE("ilike"),
        NOT_ILIKE("!ilike"),
        STARTS_WITH("$ilike", "startswith"),
        NOT_STARTS_WITH("!$ilike", "!startswith"),
        ENDS_WITH("ilike$", "endswith"),
        NOT_ENDS_WITH("!ilike$", "!endswith"),

        // access checks
        CAN_READ("canread"),
        CAN_WRITE("canwrite"),
        CAN_DATA_READ("candataread"),
        CAN_DATA_WRITE("candatawrite"),
        CAN_ACCESS("canaccess");

        private final String[] symbols;

        Comparison(String... symbols) {
            this.symbols = symbols;
        }

        public static Comparison parse(String symbol) {
            String s = symbol.toLowerCase();
            for (Comparison op : values()) {
                if (asList(op.symbols).contains(s)) {
                    return op;
                }
            }
            throw new IllegalArgumentException("Not an comparison operator symbol: " + symbol);
        }

        public boolean isUnary() {
            return this == NULL || this == NOT_NULL || this == EMPTY || this == NOT_EMPTY;
        }

        public boolean isMultiValue() {
            return this == IN || this == NOT_IN || this == CAN_ACCESS;
        }

        public boolean isIdentityCompare() {
            return this == NULL || this == NOT_NULL || this == EQ || this == NE;
        }

        public boolean isOrderCompare() {
            return this == EQ || this == NE || isNumericCompare();
        }

        public boolean isNumericCompare() {
            return this == LT || this == LE || this == GE || this == GT;
        }

        public boolean isCollectionCompare() {
            return isContainsCompare() || isSizeCompare();
        }

        public boolean isSizeCompare() {
            return this == EMPTY || this == NOT_EMPTY;
        }

        public boolean isStringCompare() {
            return ordinal() >= LIKE.ordinal() && ordinal() < CAN_READ.ordinal();
        }

        public boolean isContainsCompare() {
            return this == IN || this == NOT_IN;
        }

        public boolean isAccessCompare() {
            return ordinal() >= CAN_READ.ordinal();
        }
    }

    /**
     * Query properties about the owner of the collection property.
     */
    public static final class Owner {

        /**
         * The object type that has the collection
         */
        private final Class<? extends PrimaryKeyObject> type;

        /**
         * Id of the collection owner object.
         */
        private final String id;

        /**
         * Name of the collection property in the {@link #type}.
         */
        private final String collectionProperty;

        public Owner(Class<? extends PrimaryKeyObject> type, String id, String collectionProperty) {
            this.type = type;
            this.id = id;
            this.collectionProperty = collectionProperty;
        }

        public static OwnerBuilder builder() {
            return new OwnerBuilder();
        }

        public Class<? extends PrimaryKeyObject> getType() {
            return type;
        }

        public String getId() {
            return id;
        }

        public String getCollectionProperty() {
            return collectionProperty;
        }

        @Override
        public String toString() {
            return type.getSimpleName() + "[" + id + "]." + collectionProperty;
        }

        public static class OwnerBuilder {
            private Class<? extends PrimaryKeyObject> type;
            private String id;
            private String collectionProperty;

            public OwnerBuilder type(Class<? extends PrimaryKeyObject> type) {
                this.type = type;
                return this;
            }

            public OwnerBuilder id(String id) {
                this.id = id;
                return this;
            }

            public OwnerBuilder collectionProperty(String collectionProperty) {
                this.collectionProperty = collectionProperty;
                return this;
            }

            public Owner build() {
                return new Owner(type, id, collectionProperty);
            }
        }
    }

    public static class GistQueryBuilder {

        private Owner owner;

        private Class<? extends PrimaryKeyObject> elementType;

        private int pageOffset;

        private int pageSize;

        private boolean total;

        private String contextRoot;

        private Locale translationLocale;

        private boolean inverse;

        private boolean translate;

        private boolean absoluteUrls;

        private boolean headless;

        private boolean anyFilter;

        private boolean describe;

        private boolean references;

        private GistAutoType autoType;

        private List<Field> fields = emptyList();

        private List<Filter> filters = emptyList();

        private List<Order> orders = emptyList();

        public GistQueryBuilder() {
        }

        public GistQueryBuilder(Owner owner, Class<? extends PrimaryKeyObject> elementType,
                                int pageOffset, int pageSize, boolean total, String contextRoot,
                                Locale translationLocale, boolean inverse, boolean translate,
                                boolean absoluteUrls, boolean headless, boolean anyFilter,
                                boolean describe, boolean references, GistAutoType autoType,
                                List<Field> fields, List<Filter> filters, List<Order> orders) {
            this.owner = owner;
            this.elementType = elementType;
            this.pageOffset = pageOffset;
            this.pageSize = pageSize;
            this.total = total;
            this.contextRoot = contextRoot;
            this.translationLocale = translationLocale;
            this.inverse = inverse;
            this.translate = translate;
            this.absoluteUrls = absoluteUrls;
            this.headless = headless;
            this.anyFilter = anyFilter;
            this.describe = describe;
            this.references = references;
            this.autoType = autoType;
            this.fields = fields;
            this.filters = filters;
            this.orders = orders;
        }

        public GistQueryBuilder owner(Owner owner) {
            this.owner = owner;
            return this;
        }

        public GistQueryBuilder elementType(Class<? extends PrimaryKeyObject> elementType) {
            this.elementType = elementType;
            return this;
        }

        public GistQueryBuilder pageOffset(int pageOffset) {
            this.pageOffset = pageOffset;
            return this;
        }

        public GistQueryBuilder pageSize(int pageSize) {
            this.pageSize = pageSize;
            return this;
        }

        public GistQueryBuilder total(boolean total) {
            this.total = total;
            return this;
        }

        public GistQueryBuilder contextRoot(String contextRoot) {
            this.contextRoot = contextRoot;
            return this;
        }

        public GistQueryBuilder translationLocale(Locale translationLocale) {
            this.translationLocale = translationLocale;
            return this;
        }

        public GistQueryBuilder inverse(boolean inverse) {
            this.inverse = inverse;
            return this;
        }

        public GistQueryBuilder translate(boolean translate) {
            this.translate = translate;
            return this;
        }

        public GistQueryBuilder absoluteUrls(boolean absoluteUrls) {
            this.absoluteUrls = absoluteUrls;
            return this;
        }

        public GistQueryBuilder headless(boolean headless) {
            this.headless = headless;
            return this;
        }

        public GistQueryBuilder anyFilter(boolean anyFilter) {
            this.anyFilter = anyFilter;
            return this;
        }

        public GistQueryBuilder describe(boolean describe) {
            this.describe = describe;
            return this;
        }

        public GistQueryBuilder references(boolean references) {
            this.references = references;
            return this;
        }

        public GistQueryBuilder autoType(GistAutoType autoType) {
            this.autoType = autoType;
            return this;
        }

        public GistQueryBuilder fields(List<Field> fields) {
            this.fields = fields;
            return this;
        }

        public GistQueryBuilder filters(List<Filter> filters) {
            this.filters = filters;
            return this;
        }

        public GistQueryBuilder orders(List<Order> orders) {
            this.orders = orders;
            return this;
        }

        //Return the finally consrcuted User object
        public GistQuery build() {
            GistQuery gistQuery = new GistQuery(this);
            validateUserObject(gistQuery);
            return gistQuery;
        }

        private void validateUserObject(GistQuery gistQuery) {
            //Do some basic validations to check
            //if user object does not break any assumption of system
        }
    }

    public static final class Field {
        public static final String REFS_PATH = "__refs__";

        public static final String ALL_PATH = "*";

        public static final Field ALL = new Field(ALL_PATH, Transform.NONE);

        @JsonProperty
        private final String propertyPath;

        @JsonProperty
        private final Transform transformation;

        @JsonProperty
        private final String alias;

        @JsonProperty
        private final String transformationArgument;

        @JsonProperty
        private final boolean translate;

        @JsonProperty
        private final boolean attribute;

        public FieldBuilder toBuilder() {
            return new FieldBuilder(propertyPath, transformation, alias,
                transformationArgument, translate, attribute);
        }

        public static FieldBuilder builder() {
            return new FieldBuilder();
        }

        public Field(String propertyPath, Transform transformation) {
            this(propertyPath, transformation, "", null, false, false);
        }

        public Field(String propertyPath, Transform transformation,
                     String alias, String transformationArgument,
                     boolean translate, boolean attribute) {
            this.propertyPath = propertyPath;
            this.transformation = transformation;
            this.alias = alias;
            this.transformationArgument = transformationArgument;
            this.translate = translate;
            this.attribute = attribute;
        }

        public static String getAllPath() {
            return ALL_PATH;
        }

        public static Field getALL() {
            return ALL;
        }

        public static Field parse(String field) {
            String[] parts = field.split("(?:::|~|@)(?![^\\[\\]]*\\])");
            if (parts.length == 1) {
                return new Field(field, Transform.AUTO);
            }
            Transform transform = Transform.AUTO;
            String alias = "";
            String arg = null;
            for (int i = 1; i < parts.length; i++) {
                String part = parts[i];
                if (part.startsWith("rename")) {
                    alias = parseArgument(part);
                } else {
                    transform = Transform.parse(part);
                    if (part.indexOf('(') >= 0) {
                        arg = parseArgument(part);
                    }
                }
            }
            return new Field(parts[0], transform, alias, arg, false, false);
        }

        private static String parseArgument(String part) {
            return part.substring(part.indexOf('(') + 1, part.lastIndexOf(')'));
        }

        @JsonProperty
        public String getName() {
            return alias.isEmpty() ? propertyPath : alias;
        }

        public Field withTransformation(Transform transform) {
            return toBuilder().transformation(transform).build();
        }

        public Field withPropertyPath(String path) {
            return toBuilder().propertyPath(path).build();
        }

        public Field withAlias(String alias) {
            return toBuilder().alias(alias).build();
        }

        public Field withTranslate() {
            return toBuilder().translate(true).build();
        }

        public Field asAttribute() {
            return toBuilder().attribute(true).build();
        }

        public String getPropertyPath() {
            return propertyPath;
        }

        public Transform getTransformation() {
            return transformation;
        }

        public String getAlias() {
            return alias;
        }

        public String getTransformationArgument() {
            return transformationArgument;
        }

        public boolean isTranslate() {
            return translate;
        }

        public boolean isAttribute() {
            return attribute;
        }

        @Override
        public String toString() {
            return transformation == Transform.NONE
                ? propertyPath
                : propertyPath + "::" + transformation.name().toLowerCase().replace('_', '-');
        }

        public static class FieldBuilder {

            private String propertyPath;
            private Transform transformation;
            private String alias = "";
            private String transformationArgument = null;
            private boolean translate = false;
            private boolean attribute = false;

            public FieldBuilder() {
            }

            public FieldBuilder(String propertyPath, Transform transformation, String alias,
                                String transformationArgument, boolean translate, boolean attribute) {

                this.propertyPath = propertyPath;
                this.transformation = transformation;
                this.alias = alias;
                this.transformationArgument = transformationArgument;
                this.translate = translate;
                this.attribute = attribute;
            }

            public FieldBuilder propertyPath(String propertyPath) {
                this.propertyPath = propertyPath;
                return this;
            }

            public FieldBuilder transformation(Transform transformation) {
                this.transformation = transformation;
                return this;
            }

            public FieldBuilder alias(String alias) {
                this.alias = alias;
                return this;
            }

            public FieldBuilder transformationArgument(String transformationArgument) {
                this.transformationArgument = transformationArgument;
                return this;
            }

            public FieldBuilder translate(boolean translate) {
                this.translate = translate;
                return this;
            }

            public FieldBuilder attribute(boolean attribute) {
                this.attribute = attribute;
                return this;
            }

            public Field build() {
                return new Field(propertyPath, transformation, alias, transformationArgument, translate, attribute);
            }
        }
    }

    public static final class Order {
        @JsonProperty
        private final String propertyPath;

        @JsonProperty
        private final Direction direction;

        public Order(String propertyPath, Direction direction) {
            this.propertyPath = propertyPath;
            this.direction = direction;
        }

        public static Order parse(String order) {
            String[] parts = order.split("(?:::|:|~|@)");
            if (parts.length == 1) {
                return new Order(order, Direction.ASC);
            }
            if (parts.length == 2) {
                return new Order(parts[0], Direction.valueOf(parts[1].toUpperCase()));
            }
            throw new IllegalArgumentException("Not a valid order expression: " + order);
        }

        private OrderBuilder builder() {
            return new OrderBuilder();
        }

        public String getPropertyPath() {
            return propertyPath;
        }

        public Direction getDirection() {
            return direction;
        }

        @Override
        public String toString() {
            return propertyPath + " " + direction.name();
        }

        public static final class OrderBuilder {
            private String propertyPath;
            private Direction direction = Direction.ASC;

            public OrderBuilder propertyPath(String propertyPath) {
                this.propertyPath = propertyPath;
                return this;
            }

            public OrderBuilder direction(Direction direction) {
                this.direction = direction;
                return this;
            }

            public Order build() {
                return new Order(this.propertyPath, this.direction);
            }
        }
    }

    public static final class Filter {
        @JsonProperty
        private final int group;

        @JsonProperty
        private final String propertyPath;

        @JsonProperty
        private final Comparison operator;

        @JsonProperty
        private final String[] value;

        @JsonProperty
        private final boolean attribute;

        public Filter(String propertyPath, Comparison operator, String... value) {
            this(0, propertyPath, operator, value, false);
        }

        public Filter(int group, String propertyPath, Comparison operator, String[] value, boolean attribute) {
            this.group = group;
            this.propertyPath = propertyPath;
            this.operator = operator;
            this.value = value;
            this.attribute = attribute;
        }

        public static Filter parse(String filter) {
            String[] parts = filter.split("(?:::|:|~|@)");
            int group = 0;
            int nameIndex = 0;
            int opIndex = 1;
            int valueIndex = 2;
            if (parts[0].matches("[0-9]")) {
                nameIndex++;
                opIndex++;
                valueIndex++;
                group = Integer.parseInt(parts[0]);
            }
            if (parts.length == valueIndex) {
                return new Filter(parts[nameIndex], Comparison.parse(parts[opIndex])).inGroup(group);
            }
            if (parts.length == valueIndex + 1) {
                String value = parts[valueIndex];
                if (value.startsWith("[") && value.endsWith("]")) {
                    return new Filter(parts[nameIndex], Comparison.parse(parts[opIndex]),
                        value.substring(1, value.length() - 1).split(",")).inGroup(group);
                }
                return new Filter(parts[nameIndex], Comparison.parse(parts[opIndex]), value).inGroup(group);
            }
            throw new IllegalArgumentException("Not a valid filter expression: " + filter);
        }

        public Filter withPropertyPath(String path) {
            return new Filter(path, operator, value);
        }

        public Filter withValue(String... value) {
            return new Filter(propertyPath, operator, value);
        }

        public Filter asAttribute() {
            return new Filter(group, propertyPath, operator, value, true);
        }

        public Filter inGroup(int group) {
            return group == this.group ? this : new Filter(group, propertyPath, operator, value, attribute);
        }

        public int getGroup() {
            return group;
        }

        public String getPropertyPath() {
            return propertyPath;
        }

        public Comparison getOperator() {
            return operator;
        }

        public String[] getValue() {
            return value;
        }

        public boolean isAttribute() {
            return attribute;
        }

        @Override
        public String toString() {
            return propertyPath + ":" + operator.symbols[0] + ":" + Arrays.toString(value);
        }

    }
}
