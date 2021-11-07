package org.nmcpye.activitiesmanagement.extended.web.service;

import org.nmcpye.activitiesmanagement.extended.dxf2module.webmessage.WebMessage;
import org.nmcpye.activitiesmanagement.extended.render.RenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * WebMessage service methods.
 *
 */
@Service
public class WebMessageService
{
    private final RenderService renderService;

    @Autowired
    public WebMessageService(RenderService renderService) {
        this.renderService = renderService;
    }

    public void sendJson( WebMessage message, HttpServletResponse response )
    {
        response.setContentType( MediaType.APPLICATION_JSON_VALUE );
        response.setStatus( message.getHttpStatusCode() );

        try
        {
            renderService.toJson( response.getOutputStream(), message );
        }
        catch ( IOException ignored )
        {
        }
    }

    public void send( WebMessage webMessage, HttpServletResponse response, HttpServletRequest request )
    {
        String type = request.getHeader( "Accept" );
        type = !StringUtils.isEmpty( type ) ? type : request.getContentType();
        type = !StringUtils.isEmpty( type ) ? type : MediaType.APPLICATION_JSON_VALUE;
        HttpStatus httpStatus = HttpStatus.valueOf( webMessage.getHttpStatusCode() );

        if ( httpStatus.is4xxClientError() || httpStatus.is5xxServerError() )
        {
            response.setHeader( "Cache-Control", CacheControl.noCache().cachePrivate().getHeaderValue() );
        }

        // allow type to be overridden by path extension
//        if ( request.getPathInfo().endsWith( ".json" ) )
//        {
            type = MediaType.APPLICATION_JSON_VALUE;
//        }
//        else if ( request.getPathInfo().endsWith( ".xml" ) )
//        {
//            type = MediaType.APPLICATION_XML_VALUE;
//        }

        if ( isCompatibleWith( type, MediaType.APPLICATION_JSON ) )
        {
            sendJson( webMessage, response );
        }
        else
        {
            sendJson( webMessage, response ); // default to json
        }
    }

    private boolean isCompatibleWith( String type, MediaType mediaType )
    {
        try
        {
            return !StringUtils.isEmpty( type ) && MediaType.parseMediaType( type ).isCompatibleWith( mediaType );
        }
        catch ( Exception ignored )
        {
        }

        return false;
    }
}
