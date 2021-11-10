package org.nmcpye.activitiesmanagement.extended.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.nmcpye.activitiesmanagement.extended.schema.PropertyType;
import org.nmcpye.activitiesmanagement.extended.schema.annotation.Property;

public class BaseLinkableObject
    implements LinkableObject
{
    /**
     * As part of the serializing process, this field can be set to indicate a
     * link to this identifiable object (will be used on the web layer for
     * navigating the REST API)
     */
    private transient String href;

    @Override
    @JsonProperty
    @Property( PropertyType.URL )
    public String getHref()
    {
        return href;
    }

    @Override
    public void setHref( String href )
    {
        this.href = href;
    }
}
