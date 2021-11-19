package org.nmcpye.activitiesmanagement.domain.sqlview;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.google.common.collect.ImmutableSet;
import org.apache.commons.lang3.StringUtils;
import org.nmcpye.activitiesmanagement.extended.common.BaseIdentifiableObject;
import org.nmcpye.activitiesmanagement.extended.common.DxfNamespaces;
import org.nmcpye.activitiesmanagement.extended.common.MetadataObject;
import org.nmcpye.activitiesmanagement.extended.schema.annotation.PropertyRange;
import org.nmcpye.activitiesmanagement.extended.sqlview.SqlViewType;

import javax.persistence.*;
import java.util.*;
import java.util.regex.Pattern;

@Entity
@Table(name = "sql_view")
@JsonRootName( value = "sqlView", namespace = DxfNamespaces.DXF_2_0 )
public class SqlView
    extends BaseIdentifiableObject
    implements MetadataObject {
    public static final String PREFIX_VIEWNAME = "_view";

    public static final Set<String> PROTECTED_TABLES = ImmutableSet.<String>builder().add(
        "app_user", "person").build();

    public static final Set<String> ILLEGAL_KEYWORDS = ImmutableSet.<String>builder().add(
        "delete", "alter", "update", "create", "drop", "commit", "createdb",
        "createuser", "insert", "rename", "restore", "write").build();

    public static final String CURRENT_USER_ID_VARIABLE = "_current_user_id";
    public static final String CURRENT_USERNAME_VARIABLE = "_current_username";

    public static final Set<String> STANDARD_VARIABLES = ImmutableSet.of(
        CURRENT_USER_ID_VARIABLE, CURRENT_USERNAME_VARIABLE);

    private static final String CRITERIA_SEP = ":";
    private static final String REGEX_SEP = "|";

    private static final String QUERY_VALUE_REGEX = "^[\\w\\s\\-]*$";

    // -------------------------------------------------------------------------
    // Properties
    // -------------------------------------------------------------------------

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "sql_query", nullable = false)
    private String sqlQuery;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private SqlViewType type;

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    public SqlView() {
    }

    public SqlView(String name, String sqlQuery, SqlViewType type) {
        this.name = name;
        this.sqlQuery = sqlQuery;
        this.type = type;
    }

    // -------------------------------------------------------------------------
    // Logic
    // -------------------------------------------------------------------------

    public String getViewName() {
        final Pattern p = Pattern.compile("\\W");

        String input = name;

        String[] items = p.split(input.trim().replaceAll("_", ""));

        input = "";

        for (String s : items) {
            input += s.isEmpty() ? "" : ("_" + s);
        }

        return PREFIX_VIEWNAME + input.toLowerCase();
    }

    public static Map<String, String> getCriteria(Set<String> params) {
        Map<String, String> map = new HashMap<>();

        if (params != null) {
            for (String param : params) {
                if (param != null && param.split(CRITERIA_SEP).length == 2) {
                    String[] criteria = param.split(CRITERIA_SEP);
                    String filter = criteria[0];
                    String value = criteria[1];

                    map.put(filter, value);
                }
            }
        }

        return map;
    }

    public static Set<String> getInvalidQueryParams(Set<String> params) {
        Set<String> invalid = new HashSet<>();

        for (String param : params) {
            if (!isValidQueryParam(param)) {
                invalid.add(param);
            }
        }

        return invalid;
    }

    /**
     * Indicates whether the given query parameter is valid.
     */
    public static boolean isValidQueryParam(String param) {
        return StringUtils.isAlphanumeric(param);
    }

    public static Set<String> getInvalidQueryValues(Collection<String> values) {
        Set<String> invalid = new HashSet<>();

        for (String value : values) {
            if (!isValidQueryValue(value)) {
                invalid.add(value);
            }
        }

        return invalid;
    }

    /**
     * Indicates whether the given query value is valid.
     */
    public static boolean isValidQueryValue(String value) {
        return value != null && value.matches(QUERY_VALUE_REGEX);
    }

    public static String getProtectedTablesRegex() {
        StringBuffer regex = new StringBuffer("^(.*\\W)?(");

        for (String table : PROTECTED_TABLES) {
            regex.append(table).append(REGEX_SEP);
        }

        regex.delete(regex.length() - 1, regex.length());

        return regex.append(")(\\W.*)?$").toString();
    }

    public static String getIllegalKeywordsRegex() {
        StringBuffer regex = new StringBuffer("^(.*\\W)?(");

        for (String word : ILLEGAL_KEYWORDS) {
            regex.append(word).append(REGEX_SEP);
        }

        regex.delete(regex.length() - 1, regex.length());

        return regex.append(")(\\W.*)?$").toString();
    }

    /**
     * Indicates whether this SQL view is a query.
     */
    public boolean isQuery() {
        return SqlViewType.QUERY.equals(type);
    }

    /**
     * Indicates whether this SQl view is a view / materialized view.
     */
    public boolean isView() {
        return SqlViewType.QUERY.equals(type) || isMaterializedView();
    }

    /**
     * Indicates whether this SQL view is a materalized view.
     */
    public boolean isMaterializedView() {
        return SqlViewType.MATERIALIZED_VIEW.equals(type);
    }

    // -------------------------------------------------------------------------
    // Getters and setters
    // -------------------------------------------------------------------------

    @JsonProperty
    @PropertyRange(min = 2)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @JsonProperty
    public String getSqlQuery() {
        return sqlQuery;
    }

    public void setSqlQuery(String sqlQuery) {
        this.sqlQuery = sqlQuery;
    }

    @JsonProperty
    public SqlViewType getType() {
        return type;
    }

    public void setType(SqlViewType type) {
        this.type = type;
    }
}
