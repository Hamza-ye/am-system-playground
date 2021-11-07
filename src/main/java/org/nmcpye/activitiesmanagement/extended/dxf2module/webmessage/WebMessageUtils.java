package org.nmcpye.activitiesmanagement.extended.dxf2module.webmessage;

import org.nmcpye.activitiesmanagement.domain.JobConfiguration;
import org.nmcpye.activitiesmanagement.extended.dxf2module.scheduling.JobConfigurationWebMessageResponse;
import org.nmcpye.activitiesmanagement.extended.dxf2module.webmessage.responses.ErrorReportsWebMessageResponse;
import org.nmcpye.activitiesmanagement.extended.dxf2module.webmessage.responses.ObjectReportWebMessageResponse;
import org.nmcpye.activitiesmanagement.extended.dxf2module.webmessage.responses.TypeReportWebMessageResponse;
import org.nmcpye.activitiesmanagement.extended.feedback.*;
import org.springframework.http.HttpStatus;

import java.util.List;

public final class WebMessageUtils
{
    public static WebMessage createWebMessage(String message, Status status, HttpStatus httpStatus )
    {
        WebMessage webMessage = new WebMessage( status, httpStatus );
        webMessage.setMessage( message );

        return webMessage;
    }

    public static WebMessage createWebMessage(String message, Status status, HttpStatus httpStatus, ErrorCode errorCode )
    {
        WebMessage webMessage = new WebMessage( status, httpStatus );
        webMessage.setErrorCode( errorCode );
        webMessage.setMessage( message );

        return webMessage;
    }

    public static WebMessage createWebMessage( String message, String devMessage, Status status, HttpStatus httpStatus )
    {
        WebMessage webMessage = new WebMessage( status, httpStatus );
        webMessage.setMessage( message );
        webMessage.setDevMessage( devMessage );

        return webMessage;
    }

    public static WebMessage ok( String message )
    {
        return createWebMessage( message, Status.OK, HttpStatus.OK );
    }

    public static WebMessage ok( String message, String devMessage )
    {
        return createWebMessage( message, devMessage, Status.OK, HttpStatus.OK );
    }

    public static WebMessage created( String message )
    {
        return createWebMessage( message, Status.OK, HttpStatus.CREATED );
    }

    public static WebMessage created( String message, String devMessage )
    {
        return createWebMessage( message, devMessage, Status.OK, HttpStatus.CREATED );
    }

    public static WebMessage notFound( String message )
    {
        return createWebMessage( message, Status.ERROR, HttpStatus.NOT_FOUND );
    }

    public static WebMessage notFound( Class<?> klass, String id )
    {
        String message = klass.getSimpleName() + " with id " + id + " could not be found.";
        return createWebMessage( message, Status.ERROR, HttpStatus.NOT_FOUND );
    }

    public static WebMessage notFound( String message, String devMessage )
    {
        return createWebMessage( message, devMessage, Status.ERROR, HttpStatus.NOT_FOUND );
    }

    public static WebMessage conflict( String message )
    {
        return createWebMessage( message, Status.ERROR, HttpStatus.CONFLICT );
    }

    public static WebMessage conflict( String message, ErrorCode errorCode )
    {
        return createWebMessage( message, Status.ERROR, HttpStatus.CONFLICT, errorCode );
    }

    public static WebMessage conflict( String message, String devMessage )
    {
        return createWebMessage( message, devMessage, Status.ERROR, HttpStatus.CONFLICT );
    }

    public static WebMessage error( String message )
    {
        return createWebMessage( message, Status.ERROR, HttpStatus.INTERNAL_SERVER_ERROR );
    }

    public static WebMessage error( String message, String devMessage )
    {
        return createWebMessage( message, devMessage, Status.ERROR, HttpStatus.INTERNAL_SERVER_ERROR );
    }

    public static WebMessage badRequest( String message )
    {
        return createWebMessage( message, Status.ERROR, HttpStatus.BAD_REQUEST );
    }

    public static WebMessage badRequest( String message, String devMessage )
    {
        return createWebMessage( message, devMessage, Status.ERROR, HttpStatus.BAD_REQUEST );
    }

    public static WebMessage forbidden( String message )
    {
        return createWebMessage( message, Status.ERROR, HttpStatus.FORBIDDEN );
    }

    public static WebMessage forbidden( String message, String devMessage )
    {
        return createWebMessage( message, devMessage, Status.ERROR, HttpStatus.FORBIDDEN );
    }

    public static WebMessage serviceUnavailable( String message )
    {
        return createWebMessage( message, Status.ERROR, HttpStatus.SERVICE_UNAVAILABLE );
    }

    public static WebMessage serviceUnavailable( String message, String devMessage )
    {
        return createWebMessage( message, devMessage, Status.ERROR, HttpStatus.SERVICE_UNAVAILABLE );
    }

    public static WebMessage unprocessableEntity( String message )
    {
        return createWebMessage( message, Status.ERROR, HttpStatus.UNPROCESSABLE_ENTITY );
    }

    public static WebMessage unprocessableEntity( String message, String devMessage )
    {
        return createWebMessage( message, devMessage, Status.ERROR, HttpStatus.UNPROCESSABLE_ENTITY );
    }

    public static WebMessage unathorized( String message )
    {
        return createWebMessage( message, Status.ERROR, HttpStatus.UNAUTHORIZED );
    }

    public static WebMessage unathorized( String message, String devMessage )
    {
        return createWebMessage( message, devMessage, Status.ERROR, HttpStatus.UNAUTHORIZED );
    }

    public static WebMessage typeReport( TypeReport typeReport )
    {
        WebMessage webMessage = new WebMessage();
        webMessage.setResponse( new TypeReportWebMessageResponse( typeReport ) );

        if ( typeReport.getErrorReports().isEmpty() )
        {
            webMessage.setStatus( Status.OK );
            webMessage.setHttpStatus( HttpStatus.OK );
        }
        else
        {
            webMessage.setMessage( "One more more errors occurred, please see full details in import report." );
            webMessage.setStatus( Status.ERROR );
            webMessage.setHttpStatus( HttpStatus.CONFLICT );
        }

        return webMessage;
    }

    public static WebMessage objectReport( ObjectReport objectReport )
    {
        WebMessage webMessage = new WebMessage();
        webMessage.setResponse( new ObjectReportWebMessageResponse( objectReport ) );

        if ( objectReport.isEmpty() )
        {
            webMessage.setStatus( Status.OK );
            webMessage.setHttpStatus( HttpStatus.OK );
        }
        else
        {
            webMessage.setMessage( "One more more errors occurred, please see full details in import report." );
            webMessage.setStatus( Status.WARNING );
            webMessage.setHttpStatus( HttpStatus.CONFLICT );
        }

        return webMessage;
    }

    public static WebMessage jobConfigurationReport( JobConfiguration jobConfiguration )
    {
        WebMessage webMessage = WebMessageUtils.ok( "Initiated " + jobConfiguration.getName() );
        webMessage.setResponse( new JobConfigurationWebMessageResponse( jobConfiguration ) );

        return webMessage;
    }

    public static WebMessage errorReports( List<ErrorReport> errorReports )
    {
        WebMessage webMessage = new WebMessage();
        webMessage.setResponse( new ErrorReportsWebMessageResponse( errorReports ) );

        if ( !errorReports.isEmpty() )
        {
            webMessage.setStatus( Status.ERROR );
            webMessage.setHttpStatus( HttpStatus.BAD_REQUEST );
        }

        return webMessage;
    }

    private WebMessageUtils()
    {
    }
}
