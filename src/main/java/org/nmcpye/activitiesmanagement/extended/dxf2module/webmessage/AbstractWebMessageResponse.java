package org.nmcpye.activitiesmanagement.extended.dxf2module.webmessage;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AbstractWebMessageResponse implements WebMessageResponse
{
    /**
     * Optional type property. Since we are using the somewhat generic name 'response' for the data
     * part of the message, this can be used to signal what kind of response this is.
     * <p/>
     * Some examples might be 'ImportCount', 'ImportSummary', etc.
     */
    private String responseType;

    public AbstractWebMessageResponse()
    {
        this.responseType = getClass().getSimpleName().replaceFirst( "WebMessageResponse", "" );
    }

    public AbstractWebMessageResponse( String responseType )
    {
        this.responseType = responseType;
    }

    @JsonProperty
    public String getResponseType()
    {
        return responseType;
    }

    public void setResponseType( String responseType )
    {
        this.responseType = responseType;
    }
}
