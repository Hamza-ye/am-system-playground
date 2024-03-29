package org.nmcpye.activitiesmanagement.extended.common.adapter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.nmcpye.activitiesmanagement.domain.organisationunit.OrganisationUnitGroup;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class JacksonOrganisationUnitGroupSymbolSerializer extends JsonSerializer<OrganisationUnitGroup> {

    @Override
    public void serialize(OrganisationUnitGroup value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");

        //        if ( ToXmlGenerator.class.isAssignableFrom( jgen.getClass() ) )
        //        {
        //            ToXmlGenerator xmlGenerator = (ToXmlGenerator) jgen;
        //
        //            try
        //            {
        //                XMLStreamWriter staxWriter = xmlGenerator.getStaxWriter();
        //
        //                staxWriter.writeStartElement( DxfNamespaces.DXF_2_0, "organisationUnitGroup" );
        //                staxWriter.writeAttribute( "id", value.getUid() );
        //                staxWriter.writeAttribute( "name", value.getName() );
        //                staxWriter.writeAttribute( "created", DATE_FORMAT.format( value.getCreated() ) );
        //                staxWriter.writeAttribute( "lastUpdated", DATE_FORMAT.format( value.getLastUpdated() ) );
        //
        //                if ( value.getHref() != null )
        //                {
        //                    staxWriter.writeAttribute( "href", value.getHref() );
        //                }
        //
        //                staxWriter.writeAttribute( "symbol", String.valueOf( value.getSymbol() ) );
        //                staxWriter.writeEndElement();
        //            }
        //            catch ( XMLStreamException e )
        //            {
        //                e.printStackTrace(); //TODO fix
        //            }
        //        }
        //        else
        //        {
        jgen.writeStartObject();

        jgen.writeStringField("id", value.getUid());
        jgen.writeStringField("name", value.getName());
        jgen.writeFieldName("created");
        provider.defaultSerializeDateValue(value.getCreated(), jgen);

        jgen.writeFieldName("lastUpdated");
        provider.defaultSerializeDateValue(value.getLastUpdated(), jgen);

        if (value.getHref() != null) {
            jgen.writeStringField("href", value.getHref());
        }

        jgen.writeStringField("symbol", value.getSymbol());

        jgen.writeEndObject();
        //        }
    }
}
