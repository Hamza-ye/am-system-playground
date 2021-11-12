package org.nmcpye.activitiesmanagement.extended.hibernatemodule.hibernate.jsonb.type;

/**
 * Names of custom JSONB function created in database Those functions can be
 * called in JPA Criteria query using builder.function() All functions must be
 */
public class JsonbFunctions
{
    /**
     * FUNCTION jsonb_has_user_group_ids(jsonb, text) $1: Sharing jsonb column
     * $2: Array of UserGroup uid
     *
     * @return True if the given jsonb has at least one of UserGroup's uid from
     *         the given array
     */
    public static final String HAS_USER_GROUP_IDS = "jsonb_has_user_group_ids";

    /**
     * FUNCTION jsonb_check_user_groups_access(jsonb, text, text) $1: Sharing
     * jsonb column $2: Access String to check $3: Array of UserGroup uid
     *
     * @return True if the given jsonb has at least one of UserGroup's uid from
     *         the given array and the access of UserGroup `like` given access
     */
    public static final String CHECK_USER_GROUPS_ACCESS = "jsonb_check_user_groups_access";

    /**
     * FUNCTION jsonb_has_user_id(jsonb, text ) $1: Sharing jsonb column $2:
     * User uid to check
     *
     * @return True if given jsonb has user uid
     */
    public static final String HAS_USER_ID = "jsonb_has_user_id";

    /**
     * FUNCTION jsonb_check_user_access(jsonb, text, text) $1: Sharing jsonb
     * column $2: User uid to check $3: Access string to check
     *
     * @return TRUE if given jsonb has user uid and user access like given
     *         access string
     */
    public static final String CHECK_USER_ACCESS = "jsonb_check_user_access";

    /**
     * Built-in function of PostgresQL
     */
    public static final String EXTRACT_PATH = "jsonb_extract_path";

    /**
     * Built-in function of PostgresQL
     */
    public static final String EXTRACT_PATH_TEXT = "jsonb_extract_path_text";

    /**
     * Use the regex operator '~*' to match a given string with a given regular
     * expression $1 String to search $2 Regular expression for matching
     */
    public static final String REGEXP_SEARCH = "regexp_search";
}
