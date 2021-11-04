package org.nmcpye.activitiesmanagement.extended.config.commonmoduleconfig;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.nmcpye.activitiesmanagement.extended.commonmodule.jackson.EmptyStringToNullStdDeserializer;
import org.nmcpye.activitiesmanagement.extended.commonmodule.jackson.ParseDateStdDeserializer;
import org.nmcpye.activitiesmanagement.extended.commonmodule.jackson.WriteDateStdSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.util.Date;

/**
 * Created by Hamza on 31/10/2021.
 */
@Configuration
public class JacksonObjectMapperConfig {

    @Bean
    public MappingJackson2HttpMessageConverter jacksonMessageConverter(){
        MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();

        ObjectMapper mapper = configureMapper(new ObjectMapper(), false );

        messageConverter.setObjectMapper(mapper);
        return messageConverter;
    }

    /**
     * Shared configuration for all Jackson mappers
     *
     * @param objectMapper an {@see ObjectMapper}
     * @param autoDetectGetters if true, enable `autoDetectGetters`
     * @return a configured {@see ObjectMapper}
     */
    private static ObjectMapper configureMapper(ObjectMapper objectMapper, boolean autoDetectGetters )
    {

        SimpleModule module = new SimpleModule();
        module.addDeserializer( String.class, new EmptyStringToNullStdDeserializer() );
        module.addDeserializer( Date.class, new ParseDateStdDeserializer() );
        module.addSerializer( Date.class, new WriteDateStdSerializer() );

        objectMapper.registerModules( module, new JavaTimeModule(), new Jdk8Module(), new Hibernate5Module() );

        objectMapper.setSerializationInclusion( JsonInclude.Include.NON_NULL );
        objectMapper.disable( SerializationFeature.WRITE_DATES_AS_TIMESTAMPS );
        objectMapper.disable( SerializationFeature.FAIL_ON_EMPTY_BEANS );
        objectMapper.enable( SerializationFeature.WRAP_EXCEPTIONS );

        objectMapper.disable( DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES );
        objectMapper.disable( DeserializationFeature.FAIL_ON_MISSING_EXTERNAL_TYPE_ID_PROPERTY );
        objectMapper.enable( DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES );
        objectMapper.enable( DeserializationFeature.WRAP_EXCEPTIONS );

        objectMapper.disable( MapperFeature.AUTO_DETECT_FIELDS );
        objectMapper.disable( MapperFeature.AUTO_DETECT_CREATORS );
        if ( !autoDetectGetters )
        {
            objectMapper.disable( MapperFeature.AUTO_DETECT_GETTERS );
        }

        // TODO Check when updating or saving entity from request
        objectMapper.disable( MapperFeature.AUTO_DETECT_SETTERS );

        objectMapper.disable( MapperFeature.AUTO_DETECT_IS_GETTERS );

        return objectMapper;
    }
}
