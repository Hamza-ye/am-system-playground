package org.nmcpye.activitiesmanagement.extended.web.rest;

import org.nmcpye.activitiesmanagement.extended.dxf2module.webmessage.WebMessage;
import org.nmcpye.activitiesmanagement.extended.dxf2module.webmessage.WebMessageResponse;
import org.nmcpye.activitiesmanagement.extended.web.service.ContextService;
import org.nmcpye.activitiesmanagement.extended.web.utils.ContextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * When returning {@link WebMessage} or even subclasses of {@link WebMessage}s
 * the message's {@link WebMessage#getHttpStatusCode()} is used to set the HTTP
 * response status code.
 * <p>
 * In case the response is a 4xx or 5xx caching is turned off.
 */
@ControllerAdvice
public class WebMessageControllerAdvice implements ResponseBodyAdvice<WebMessageResponse> {
    @Autowired
    private ContextService contextService;

    @Override
    public boolean supports(MethodParameter returnType,
                            Class<? extends HttpMessageConverter<?>> selectedConverterType) {
        return WebMessage.class.isAssignableFrom(returnType.getParameterType());
    }

    @Override
    public WebMessageResponse beforeBodyWrite(WebMessageResponse body, MethodParameter returnType,
                                              MediaType selectedContentType,
                                              Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request,
                                              ServerHttpResponse response) {
        WebMessage message = (WebMessage) body;
        String location = message.getLocation();
        if (location != null) {
            response.getHeaders().addIfAbsent(ContextUtils.HEADER_LOCATION,
                contextService.getApiPath() + location);
        }
        if (isPlainResponse(request, message)) {
            return ((WebMessage) body).getResponse();
        }
        HttpStatus httpStatus = HttpStatus.resolve(message.getHttpStatusCode());
        if (httpStatus != null) {
            response.setStatusCode(httpStatus);
            if (httpStatus.is4xxClientError() || httpStatus.is5xxServerError()) {
                response.getHeaders().addIfAbsent("Cache-Control",
                    CacheControl.noCache().cachePrivate().getHeaderValue());
            }
        }
        return body;
    }

    private boolean isPlainResponse(ServerHttpRequest request, WebMessage message) {
        return true;
//        DhisApiVersion plainBefore = message.getPlainResponseBefore();
//        return plainBefore == DhisApiVersion.ALL
//            || plainBefore != null && DhisApiVersion.getVersionFromPath( request.getURI().getPath() ).lt( plainBefore );
    }
}
