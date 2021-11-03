package org.nmcpye.activitiesmanagement.extended.common;

/**
 * This class is deprecated in favor of new async auditing solution, do not use.
 */
public enum AuditType {
    CREATE("create"),
    UPDATE("update"),
    DELETE("delete"),
    READ("read"),
    SEARCH("search");

    private final String value;

    AuditType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
