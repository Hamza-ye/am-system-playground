package org.nmcpye.activitiesmanagement.extended.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.nmcpye.activitiesmanagement.domain.User;
import org.nmcpye.activitiesmanagement.extended.translation.Translation;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

public interface IdentifiableObject
    extends PrimaryKeyObject, LinkableObject, Comparable<IdentifiableObject>, Serializable {

    String getCode();

    String getName();

    String getDisplayName();

    Date getCreated();

    Date getLastUpdated();

    User getLastUpdatedBy();

    //-----------------------------------------------------------------------------
    // Sharing
    //-----------------------------------------------------------------------------

    /**
     * Return User who created this object This field is immutable and must not
     * be updated
     */
    User getCreatedBy();

    /**
     * @deprecated This method is replaced by {@link #getCreatedBy()} Currently
     *             it is only used for web api backward compatibility
     */
    @Deprecated
    User getUser();

    void setCreatedBy( User createdBy );

    /**
     * @deprecated This method is replaced by {@link #setCreatedBy(User)} ()}
     *             Currently it is only used for web api backward compatibility
     */
    @Deprecated
    void setUser( User user );

//    Set<Translation> getTranslations();
    //-----------------------------------------------------------------------------
    // Utility methods
    //-----------------------------------------------------------------------------

    @JsonIgnore
    String getPropertyValue(IdScheme idScheme);
}
