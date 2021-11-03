package org.nmcpye.activitiesmanagement.extended.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.util.Date;
import org.nmcpye.activitiesmanagement.domain.User;

public interface IdentifiableObject extends Comparable<IdentifiableObject>, Serializable {
    Long getId();

    String getUid();

    String getCode();

    String getName();

    String getDisplayName();

    Date getCreated();

    Date getLastUpdated();

    User getLastUpdatedBy();

    //-----------------------------------------------------------------------------
    // Sharing
    //-----------------------------------------------------------------------------

    User getUser();

    //-----------------------------------------------------------------------------
    // Utility methods
    //-----------------------------------------------------------------------------

    @JsonIgnore
    String getPropertyValue(IdScheme idScheme);
}
