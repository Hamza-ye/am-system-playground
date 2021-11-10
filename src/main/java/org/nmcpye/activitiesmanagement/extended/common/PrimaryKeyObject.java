package org.nmcpye.activitiesmanagement.extended.common;

import java.io.Serializable;

/**
 * Common interface for objects that have a unique ID used in RESTful APIs but
 * that might not have use for a name and other fundamentals that come with
 * {@link IdentifiableObject}s.
 *
 */
public interface PrimaryKeyObject extends Serializable
{
    /**
     * @return internal unique ID of the object as used in the database
     */
    Long getId();

    /**
     * @return external unique ID of the object as used in the RESTful API
     */
    String getUid();
}
