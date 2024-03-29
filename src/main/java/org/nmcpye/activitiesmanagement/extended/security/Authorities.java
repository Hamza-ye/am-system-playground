package org.nmcpye.activitiesmanagement.extended.security;

public enum Authorities
{
    F_TRACKED_ENTITY_INSTANCE_SEARCH_IN_ALL_ORGUNITS( "F_TRACKED_ENTITY_INSTANCE_SEARCH_IN_ALL_ORGUNITS" ),
    F_TEI_CASCADE_DELETE( "F_TEI_CASCADE_DELETE" ),
    F_ENROLLMENT_CASCADE_DELETE( "F_ENROLLMENT_CASCADE_DELETE" ),
    F_EDIT_EXPIRED( "F_EDIT_EXPIRED" ),
    F_IGNORE_TRACKER_REQUIRED_VALUE_VALIDATION( "F_IGNORE_TRACKER_REQUIRED_VALUE_VALIDATION" ),
    F_SKIP_DATA_IMPORT_AUDIT( "F_SKIP_DATA_IMPORT_AUDIT" ),
    F_METADATA_EXPORT( "F_METADATA_EXPORT" ),
    F_USER_VIEW( "F_USER_VIEW" );

    private String authority;

    Authorities(String authority )
    {
        this.authority = authority;
    }

    public String getAuthority()
    {
        return authority;
    }
}
