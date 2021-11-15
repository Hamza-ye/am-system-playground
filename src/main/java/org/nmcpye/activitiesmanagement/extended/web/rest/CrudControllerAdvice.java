package org.nmcpye.activitiesmanagement.extended.web.rest;

import com.fasterxml.jackson.core.JsonParseException;
import io.undertow.util.BadRequestException;
import org.hibernate.exception.ConstraintViolationException;
import org.nmcpye.activitiesmanagement.extended.common.DeleteNotAllowedException;
import org.nmcpye.activitiesmanagement.extended.common.IdentifiableProperty;
import org.nmcpye.activitiesmanagement.extended.common.IllegalQueryException;
import org.nmcpye.activitiesmanagement.extended.common.QueryRuntimeException;
import org.nmcpye.activitiesmanagement.extended.common.exception.InvalidIdentifierReferenceException;
import org.nmcpye.activitiesmanagement.extended.dxf2module.metadata.MetadataExportException;
import org.nmcpye.activitiesmanagement.extended.dxf2module.metadata.MetadataImportException;
import org.nmcpye.activitiesmanagement.extended.dxf2module.webmessage.WebMessage;
import org.nmcpye.activitiesmanagement.extended.dxf2module.webmessage.WebMessageException;
import org.nmcpye.activitiesmanagement.extended.feedback.Status;
import org.nmcpye.activitiesmanagement.extended.schemamodule.SchemaPathException;
import org.nmcpye.activitiesmanagement.extended.servicecommonsmodule.jsonpatch.JsonPatchException;
import org.nmcpye.activitiesmanagement.extended.servicecoremodule.query.QueryException;
import org.nmcpye.activitiesmanagement.extended.servicecoremodule.query.QueryParserException;
import org.nmcpye.activitiesmanagement.extended.servicenodemodule.fieldfilter.FieldFilterException;
import org.nmcpye.activitiesmanagement.extended.util.DateUtils;
import org.nmcpye.activitiesmanagement.extended.web.rest.exception.NotAuthenticatedException;
import org.nmcpye.activitiesmanagement.extended.web.rest.exception.NotFoundException;
import org.nmcpye.activitiesmanagement.extended.web.rest.exception.OperationNotAllowedException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.HttpStatusCodeException;

import javax.servlet.ServletException;
import java.beans.PropertyEditorSupport;
import java.util.Date;
import java.util.function.Function;

import static org.nmcpye.activitiesmanagement.extended.dxf2module.webmessage.WebMessageUtils.*;

@ControllerAdvice
public class CrudControllerAdvice {
    // Add sensitive exceptions into this array
    private static final Class<?>[] SENSITIVE_EXCEPTIONS = {BadSqlGrammarException.class,
        org.hibernate.QueryException.class, DataAccessResourceFailureException.class};

    private static final String GENERIC_ERROR_MESSAGE = "An unexpected error has occured. Please contact your system administrator";

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Date.class, new FromTextPropertyEditor(DateUtils::parseDate));
        binder.registerCustomEditor(IdentifiableProperty.class, new FromTextPropertyEditor(String::toUpperCase));
    }

    @ExceptionHandler(IllegalQueryException.class)
    @ResponseBody
    public WebMessage illegalQueryExceptionHandler(IllegalQueryException ex) {
        return conflict(ex.getMessage(), ex.getErrorCode());
    }

    @ExceptionHandler(QueryRuntimeException.class)
    @ResponseBody
    public WebMessage queryRuntimeExceptionHandler(QueryRuntimeException ex) {
        return conflict(ex.getMessage(), ex.getErrorCode());
    }

    @ExceptionHandler(DeleteNotAllowedException.class)
    @ResponseBody
    public WebMessage deleteNotAllowedExceptionHandler(DeleteNotAllowedException ex) {
        return conflict(ex.getMessage(), ex.getErrorCode());
    }

    @ExceptionHandler(InvalidIdentifierReferenceException.class)
    @ResponseBody
    public WebMessage invalidIdentifierReferenceExceptionHandler(InvalidIdentifierReferenceException ex) {
        return conflict(ex.getMessage());
    }

//    @ExceptionHandler( { DataApprovalException.class, AdxException.class, IllegalStateException.class } )
//    @ResponseBody
//    public WebMessage dataApprovalExceptionHandler( Exception ex )
//    {
//        return conflict( ex.getMessage() );
//    }

    @ExceptionHandler({JsonParseException.class, MetadataImportException.class, MetadataExportException.class})
    @ResponseBody
    public WebMessage jsonParseExceptionHandler(Exception ex) {
        return conflict(ex.getMessage());
    }

    @ExceptionHandler({QueryParserException.class, QueryException.class})
    @ResponseBody
    public WebMessage queryExceptionHandler(Exception ex) {
        return conflict(ex.getMessage());
    }

    @ExceptionHandler(FieldFilterException.class)
    @ResponseBody
    public WebMessage fieldFilterExceptionHandler(FieldFilterException ex) {
        return conflict(ex.getMessage());
    }

    @ExceptionHandler(NotAuthenticatedException.class)
    @ResponseBody
    public WebMessage notAuthenticatedExceptionHandler(NotAuthenticatedException ex) {
        return unauthorized(ex.getMessage());
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseBody
    public WebMessage notFoundExceptionHandler(NotFoundException ex) {
        return notFound(ex.getMessage());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    public WebMessage constraintViolationExceptionHandler(ConstraintViolationException ex) {
        return error(getExceptionMessage(ex));
    }

//    @ExceptionHandler( MaintenanceModeException.class )
//    @ResponseBody
//    public WebMessage maintenanceModeExceptionHandler( MaintenanceModeException ex )
//    {
//        return serviceUnavailable( ex.getMessage() );
//    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseBody
    public WebMessage accessDeniedExceptionHandler(AccessDeniedException ex) {
        return forbidden(ex.getMessage());
    }

    @ExceptionHandler(WebMessageException.class)
    @ResponseBody
    public WebMessage webMessageExceptionHandler(WebMessageException ex) {
        return ex.getWebMessage();
    }

    @ExceptionHandler(HttpStatusCodeException.class)
    @ResponseBody
    public WebMessage httpStatusCodeExceptionHandler(HttpStatusCodeException ex) {
        return createWebMessage(ex.getMessage(), Status.ERROR, ex.getStatusCode());
    }

    @ExceptionHandler(HttpClientErrorException.class)
    @ResponseBody
    public WebMessage httpClientErrorExceptionHandler(HttpClientErrorException ex) {
        return createWebMessage(ex.getMessage(), Status.ERROR, ex.getStatusCode());
    }

    @ExceptionHandler(HttpServerErrorException.class)
    @ResponseBody
    public WebMessage httpServerErrorExceptionHandler(HttpServerErrorException ex) {
        return createWebMessage(ex.getMessage(), Status.ERROR, ex.getStatusCode());
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseBody
    public WebMessage httpRequestMethodNotSupportedExceptionHandler(HttpRequestMethodNotSupportedException ex) {
        return createWebMessage(ex.getMessage(), Status.ERROR, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
    @ResponseBody
    public WebMessage httpMediaTypeNotAcceptableExceptionHandler(HttpMediaTypeNotAcceptableException ex) {
        return createWebMessage(ex.getMessage(), Status.ERROR, HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    @ResponseBody
    public WebMessage httpMediaTypeNotSupportedExceptionHandler(HttpMediaTypeNotSupportedException ex) {
        return createWebMessage(ex.getMessage(), Status.ERROR, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }

    @ExceptionHandler(ServletException.class)
    public void servletExceptionHandler(ServletException ex)
        throws ServletException {
        throw ex;
    }

    @ExceptionHandler({BadRequestException.class, IllegalArgumentException.class, SchemaPathException.class,
        JsonPatchException.class})
    @ResponseBody
    public WebMessage handleBadRequest(Exception exception) {
        return badRequest(exception.getMessage());
    }

//    @ExceptionHandler( { ConflictException.class } )
//    @ResponseBody
//    public WebMessage handleConflictRequest( Exception exception )
//    {
//        return conflict( exception.getMessage() );
//    }
//
//    @ExceptionHandler( MetadataVersionException.class )
//    @ResponseBody
//    public WebMessage handleMetaDataVersionException( MetadataVersionException metadataVersionException )
//    {
//        return error( metadataVersionException.getMessage() );
//    }

//    @ExceptionHandler( MetadataSyncException.class )
//    @ResponseBody
//    public WebMessage handleMetaDataSyncException( MetadataSyncException metadataSyncException )
//    {
//        return error( metadataSyncException.getMessage() );
//    }

//    @ExceptionHandler( MetadataImportConflictException.class )
//    @ResponseBody
//    public WebMessage handleMetadataImportConflictException( MetadataImportConflictException conflictException )
//    {
//        if ( conflictException.getMetadataSyncSummary() == null )
//        {
//            return conflict( conflictException.getMessage() );
//        }
//        return conflict( null ).setResponse( conflictException.getMetadataSyncSummary() );
//    }

    @ExceptionHandler(OperationNotAllowedException.class)
    @ResponseBody
    public WebMessage handleOperationNotAllowedException(OperationNotAllowedException ex) {
        return forbidden(ex.getMessage());
    }

//    @ExceptionHandler( OAuth2AuthenticationException.class )
//    @ResponseBody
//    public WebMessage handleOAuth2AuthenticationException( OAuth2AuthenticationException ex )
//    {
//        OAuth2Error error = ex.getError();
//        if ( error instanceof BearerTokenError )
//        {
//            BearerTokenError bearerTokenError = (BearerTokenError) error;
//            HttpStatus status = ((BearerTokenError) error).getHttpStatus();
//
//            return createWebMessage( bearerTokenError.getErrorCode(),
//                bearerTokenError.getDescription(), Status.ERROR, status );
//        }
//        return unauthorized( ex.getMessage() );
//    }

//    @ExceptionHandler( ApiTokenAuthenticationException.class )
//    @ResponseBody
//    public WebMessage handleApiTokenAuthenticationException( ApiTokenAuthenticationException ex )
//    {
//        ApiTokenError apiTokenError = ex.getError();
//        if ( apiTokenError != null )
//        {
//            return createWebMessage( apiTokenError.getDescription(), Status.ERROR,
//                apiTokenError.getHttpStatus() );
//        }
//        return unauthorized( ex.getMessage() );
//    }

//    @ExceptionHandler({PotentialDuplicateConflictException.class})
//    @ResponseBody
//    public WebMessage handlePotentialDuplicateConflictRequest(Exception exception) {
//        return conflict(exception.getMessage());
//    }

//    @ExceptionHandler({PotentialDuplicateForbiddenException.class})
//    @ResponseBody
//    public WebMessage handlePotentialDuplicateForbiddenRequest(Exception exception) {
//        return forbidden(exception.getMessage());
//    }

    /**
     * Catches default exception and send back to user, but re-throws internally
     * so it still ends up in server logs.
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public WebMessage defaultExceptionHandler(Exception ex) {
        // We print the stacktrace so it shows up in the logs, so we can more
        // easily understand 500-issues.
        ex.printStackTrace();
        return error(getExceptionMessage(ex));
    }

    private String getExceptionMessage(Exception ex) {
        boolean isMessageSensitive = false;

        String message = ex.getMessage();

        if (isSensitiveException(ex)) {
            isMessageSensitive = true;
        }

        if (ex.getCause() != null) {
            message = ex.getCause().getMessage();

            if (isSensitiveException(ex.getCause())) {
                isMessageSensitive = true;
            }
        }

        if (isMessageSensitive) {
            message = GENERIC_ERROR_MESSAGE;
        }
        return message;
    }

    private boolean isSensitiveException(Throwable e) {
        for (Class<?> exClass : SENSITIVE_EXCEPTIONS) {
            if (exClass.isAssignableFrom(e.getClass())) {
                return true;
            }
        }

        return false;
    }

    /**
     * Simple adapter to {@link PropertyEditorSupport} that allows to use lambda
     * {@link Function}s to convert value from its text representation.
     */
    private static final class FromTextPropertyEditor extends PropertyEditorSupport {

        private final Function<String, Object> fromText;

        private FromTextPropertyEditor(Function<String, Object> fromText) {
            this.fromText = fromText;
        }

        @Override
        public void setAsText(String text)
            throws IllegalArgumentException {
            setValue(fromText.apply(text));
        }
    }
}
