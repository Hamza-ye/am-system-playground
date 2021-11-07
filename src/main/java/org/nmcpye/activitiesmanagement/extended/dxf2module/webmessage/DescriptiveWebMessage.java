package org.nmcpye.activitiesmanagement.extended.dxf2module.webmessage;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DescriptiveWebMessage
    extends WebMessage
{    
    private String description;

    public DescriptiveWebMessage()
    {
        super();
    }
    
    @JsonProperty
    public String getDescription()
    {
        return description;
    }

    public void setDescription( String description )
    {
        this.description = description;
    }
}
