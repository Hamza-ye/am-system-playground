package org.nmcpye.activitiesmanagement.extended.web.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class DefaultContextService implements ContextService
{
    private static Pattern API_VERSION = Pattern.compile( "(/api/(\\d+)?/)" );

    @Override
    public String getServletPath()
    {
        return getContextPath() + getRequest().getServletPath();
    }

    @Override
    public String getContextPath()
    {
        HttpServletRequest request = getRequest();
        StringBuilder builder = new StringBuilder();
        String xForwardedProto = request.getHeader( "X-Forwarded-Proto" );
        String xForwardedPort = request.getHeader( "X-Forwarded-Port" );

        if ( xForwardedProto != null
            && (xForwardedProto.equalsIgnoreCase( "http" ) || xForwardedProto.equalsIgnoreCase( "https" )) )
        {
            builder.append( xForwardedProto );
        }
        else
        {
            builder.append( request.getScheme() );
        }

        builder.append( "://" ).append( request.getServerName() );

        int port;

        try
        {
            port = Integer.parseInt( xForwardedPort );
        }
        catch ( NumberFormatException e )
        {
            port = request.getServerPort();
        }

        if ( port != 80 && port != 443 )
        {
            builder.append( ":" ).append( port );
        }

        builder.append( request.getContextPath() );

        return builder.toString();
    }

    @Override
    public String getApiPath()
    {
        HttpServletRequest request = getRequest();
        Matcher matcher = API_VERSION.matcher( request.getRequestURI() );
        String version = "";

        if ( matcher.find() )
        {
            version = "/" + matcher.group( 2 );
        }

        return getServletPath() + version;
    }

    @Override
    public HttpServletRequest getRequest()
    {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }

    @Override
    public List<String> getParameterValues( String name )
    {
        return Optional.ofNullable( name )
            .map( this::getRequestParameterValues )
            .orElse( Collections.emptyList() );
    }

    private List<String> getRequestParameterValues( String paramName )
    {
        String[] parameterValues = getRequest().getParameterValues( paramName );

        if ( parameterValues != null )
        {
            return Arrays.stream( parameterValues )
                .distinct()
                .collect( Collectors.toList() );
        }

        return Collections.emptyList();
    }

    @Override
    public Map<String, List<String>> getParameterValuesMap()
    {
        return getRequest().getParameterMap().entrySet().stream()
            .collect( Collectors.toMap(
                Map.Entry::getKey,
                stringEntry -> Lists.newArrayList( stringEntry.getValue() ) ) );
    }

    @Override
    public List<String> getFieldsFromRequestOrAll()
    {
        return getFieldsFromRequestOrElse( ":all" );
    }

    @Override
    public List<String> getFieldsFromRequestOrElse( String fields )
    {
        return Optional.ofNullable( getParameterValues( "fields" ) )
            .filter( CollectionUtils::isNotEmpty )
            .orElse( Collections.singletonList( fields ) );
    }
}
