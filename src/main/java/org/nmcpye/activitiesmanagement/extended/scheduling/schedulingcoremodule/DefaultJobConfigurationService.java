package org.nmcpye.activitiesmanagement.extended.scheduling.schedulingcoremodule;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Maps;
import com.google.common.primitives.Primitives;
import org.apache.commons.beanutils.PropertyUtils;
import org.nmcpye.activitiesmanagement.domain.JobConfiguration;
import org.nmcpye.activitiesmanagement.extended.common.EmbeddedObject;
import org.nmcpye.activitiesmanagement.extended.common.IdentifiableObject;
import org.nmcpye.activitiesmanagement.extended.common.IdentifiableObjectStore;
import org.nmcpye.activitiesmanagement.extended.common.NameableObject;
import org.nmcpye.activitiesmanagement.extended.common.util.TextUtils;
import org.nmcpye.activitiesmanagement.extended.scheduling.JobConfigurationService;
import org.nmcpye.activitiesmanagement.extended.scheduling.JobStatus;
import org.nmcpye.activitiesmanagement.extended.scheduling.JobType;
import org.nmcpye.activitiesmanagement.extended.scheduling.JobTypeInfo;
import org.nmcpye.activitiesmanagement.extended.schema.Property;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkNotNull;

@Service("jobConfigurationService")
public class DefaultJobConfigurationService implements JobConfigurationService {

    private final Logger log = LoggerFactory.getLogger(DefaultJobConfigurationService.class);

    private final IdentifiableObjectStore<JobConfiguration> jobConfigurationStore;

    public DefaultJobConfigurationService(
        @Qualifier(
            "org.nmcpye.activitiesmanagement.extended.scheduling.JobConfigurationStore"
        ) IdentifiableObjectStore<JobConfiguration> jobConfigurationStore
    ) {
        checkNotNull(jobConfigurationStore);

        this.jobConfigurationStore = jobConfigurationStore;
    }

    @Override
    @Transactional
    public long addJobConfiguration(JobConfiguration jobConfiguration) {
        if (!jobConfiguration.isInMemoryJob()) {
            jobConfigurationStore.save(jobConfiguration);
        }

        return jobConfiguration.getId();
    }

    @Override
    @Transactional
    public void addJobConfigurations(List<JobConfiguration> jobConfigurations) {
        jobConfigurations.forEach(jobConfiguration -> jobConfigurationStore.save(jobConfiguration));
    }

    @Override
    @Transactional
    public long updateJobConfiguration(JobConfiguration jobConfiguration) {
        if (!jobConfiguration.isInMemoryJob()) {
            jobConfigurationStore.update(jobConfiguration);
        }

        return jobConfiguration.getId();
    }

    @Override
    @Transactional
    public void deleteJobConfiguration(JobConfiguration jobConfiguration) {
        if (!jobConfiguration.isInMemoryJob()) {
            jobConfigurationStore.delete(jobConfigurationStore.getByUid(jobConfiguration.getUid()));
        }
    }

    @Override
    @Transactional(readOnly = true)
    public JobConfiguration getJobConfigurationByUid(String uid) {
        return jobConfigurationStore.getByUid(uid);
    }

    @Override
    @Transactional(readOnly = true)
    public JobConfiguration getJobConfiguration(long jobId) {
        return jobConfigurationStore.get(jobId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<JobConfiguration> getAllJobConfigurations() {
        return jobConfigurationStore.getAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Map<String, Property>> getJobParametersSchema() {
        Map<String, Map<String, Property>> propertyMap = Maps.newHashMap();

        for (JobType jobType : JobType.values()) {
            if (!jobType.isConfigurable()) {
                continue;
            }

            Map<String, Property> jobParameters = Maps.uniqueIndex(getJobParameters(jobType), p -> p.getName());

            propertyMap.put(jobType.name(), jobParameters);
        }

        return propertyMap;
    }

    @Override
    public List<JobTypeInfo> getJobTypeInfo() {
        List<JobTypeInfo> jobTypes = new ArrayList<>();

        for (JobType jobType : JobType.values()) {
            if (!jobType.isConfigurable()) {
                continue;
            }

            String name = TextUtils.getPrettyEnumName(jobType);

            List<Property> jobParameters = getJobParameters(jobType);

            JobTypeInfo info = new JobTypeInfo(name, jobType, jobParameters);

            jobTypes.add(info);
        }

        return jobTypes;
    }

    @Override
    public void refreshScheduling(JobConfiguration jobConfiguration) {
        if (jobConfiguration.isEnabled()) {
            jobConfiguration.setJobStatus(JobStatus.SCHEDULED);
        } else {
            jobConfiguration.setJobStatus(JobStatus.DISABLED);
        }

        jobConfigurationStore.update(jobConfiguration);
    }

    // -------------------------------------------------------------------------
    // Supportive methods
    // -------------------------------------------------------------------------

    /**
     * Returns a list of job parameters for the given job type.
     *
     * @param jobType the {@link JobType}.
     * @return a list of {@link Property}.
     */
    private List<Property> getJobParameters( JobType jobType )
    {
        List<Property> jobParameters = new ArrayList<>();

        Class<?> clazz = jobType.getJobParameters();

        if ( clazz == null )
        {
            return jobParameters;
        }

        final Set<String> propertyNames = Stream.of( PropertyUtils.getPropertyDescriptors( clazz ) )
            .filter( pd -> pd.getReadMethod() != null && pd.getWriteMethod() != null
                && pd.getReadMethod().getAnnotation( JsonProperty.class ) != null )
            .map( PropertyDescriptor::getName )
            .collect( Collectors.toSet() );

        for ( Field field : Stream.of( clazz.getDeclaredFields() ).filter( f -> propertyNames.contains( f.getName() ) )
            .collect( Collectors.toList() ) )
        {
            Property property = new Property( Primitives.wrap( field.getType() ), null, null );
            property.setName( field.getName() );
            property.setFieldName( TextUtils.getPrettyPropertyName( field.getName() ) );

            try
            {
                field.setAccessible( true );
                property.setDefaultValue( field.get( jobType.getJobParameters().newInstance() ) );
            }
            catch ( IllegalAccessException | InstantiationException e )
            {
                log.error(
                    "Fetching default value for JobParameters properties failed for property: " + field.getName(), e );
            }

            String relativeApiElements = jobType.getRelativeApiElements() != null
                ? jobType.getRelativeApiElements().get( field.getName() )
                : "";

            if ( relativeApiElements != null && !relativeApiElements.equals( "" ) )
            {
                property.setRelativeApiEndpoint( relativeApiElements );
            }

            if ( Collection.class.isAssignableFrom( field.getType() ) )
            {
                property = setPropertyIfCollection( property, field, clazz );
            }

            jobParameters.add( property );
        }

        return jobParameters;
    }

    private static Property setPropertyIfCollection( Property property, Field field, Class<?> klass )
    {
        property.setCollection( true );
        property.setCollectionName( field.getName() );

        Type type = field.getGenericType();

        if ( type instanceof ParameterizedType)
        {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            Class<?> itemKlass = (Class<?>) parameterizedType.getActualTypeArguments()[0];
            property.setItemKlass( itemKlass );

            property.setIdentifiableObject( IdentifiableObject.class.isAssignableFrom( itemKlass ) );
            property.setNameableObject( NameableObject.class.isAssignableFrom( itemKlass ) );
            property.setEmbeddedObject( EmbeddedObject.class.isAssignableFrom( klass ) );
//            property.setAnalyticalObject( AnalyticalObject.class.isAssignableFrom( klass ) );
            property.setAnalyticalObject( false );
        }

        return property;
    }
}
