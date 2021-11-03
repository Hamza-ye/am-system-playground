package org.nmcpye.activitiesmanagement.extended.schema.schemamodule;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import java.util.*;
import org.hibernate.SessionFactory;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.metamodel.spi.MetamodelImplementor;
import org.hibernate.persister.collection.CollectionPersister;
import org.hibernate.persister.entity.Joinable;
import org.hibernate.type.*;
import org.nmcpye.activitiesmanagement.extended.common.*;
import org.nmcpye.activitiesmanagement.extended.schema.Property;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

public abstract class AbstractPropertyIntrospectorService implements PropertyIntrospectorService {

    // Simple alias map for our concrete implementations of the core interfaces
    private static final ImmutableMap<Class<?>, Class<?>> BASE_ALIAS_MAP = ImmutableMap
        .<Class<?>, Class<?>>builder()
        .put(IdentifiableObject.class, BaseIdentifiableObject.class)
        .put(NameableObject.class, BaseNameableObject.class)
        //        .put( DimensionalObject.class, BaseDimensionalObject.class )
        .put(DimensionalItemObject.class, BaseDimensionalItemObject.class)
        //        .put( AnalyticalObject.class, BaseAnalyticalObject.class )
        .build();

    private final Map<Class<?>, Map<String, Property>> classMapCache = new HashMap<>();

    private final Map<String, String> roleToRole = new HashMap<>();

    @Autowired
    protected ApplicationContext context;

    @Autowired
    protected SessionFactory sessionFactory;

    @Override
    public List<Property> getProperties(Class<?> klass) {
        return Lists.newArrayList(getPropertiesMap(klass).values());
    }

    @Override
    public Map<String, Property> getPropertiesMap(Class<?> klass) {
        if (BASE_ALIAS_MAP.containsKey(klass)) {
            klass = BASE_ALIAS_MAP.get(klass);
        }

        if (!classMapCache.containsKey(klass)) {
            classMapCache.put(klass, scanClass(klass));
        }

        return classMapCache.get(klass);
    }

    @Override
    public Class<?> getConcreteClass(Class<?> klass) {
        if (BASE_ALIAS_MAP.containsKey(klass)) {
            return BASE_ALIAS_MAP.get(klass);
        }

        return klass;
    }

    private void updateJoinTables() {
        if (!roleToRole.isEmpty()) {
            return;
        }

        Map<String, List<String>> joinTableToRoles = new HashMap<>();

        SessionFactoryImplementor sessionFactoryImplementor = (SessionFactoryImplementor) sessionFactory;
        MetamodelImplementor metamodelImplementor = sessionFactoryImplementor.getMetamodel();

        for (CollectionPersister collectionPersister : metamodelImplementor.collectionPersisters().values()) {
            CollectionType collectionType = collectionPersister.getCollectionType();

            if (collectionPersister.isManyToMany() && collectionType.isAssociationType()) {
                Joinable associatedJoinable = collectionType.getAssociatedJoinable(sessionFactoryImplementor);

                if (!joinTableToRoles.containsKey(associatedJoinable.getTableName())) {
                    joinTableToRoles.put(associatedJoinable.getTableName(), new ArrayList<>());
                }

                joinTableToRoles.get(associatedJoinable.getTableName()).add(collectionPersister.getRole());
            } else if (collectionPersister.isInverse()) {
                if (collectionType instanceof SetType) {
                    SetType setType = (SetType) collectionType;
                    setType.getAssociatedJoinable(sessionFactoryImplementor);
                }
            }
        }

        Iterator<Map.Entry<String, List<String>>> entryIterator = joinTableToRoles.entrySet().iterator();

        while (entryIterator.hasNext()) {
            Map.Entry<String, List<String>> entry = entryIterator.next();

            if (entry.getValue().size() < 2) {
                entryIterator.remove();
            }
        }

        for (Map.Entry<String, List<String>> entry : joinTableToRoles.entrySet()) {
            roleToRole.put(entry.getValue().get(0), entry.getValue().get(1));
            roleToRole.put(entry.getValue().get(1), entry.getValue().get(0));
        }
    }

    /**
     * Introspect a class and return a map with key=property-name, and value=Property class.
     *
     * @param klass Class to scan
     * @return Map with key=property-name, and value=Property class
     */
    protected abstract Map<String, Property> scanClass(Class<?> klass);
    //    protected Map<String, Property> getPropertiesFromHibernate( Class<?> klass )
    //    {
    //        updateJoinTables();
    //        SessionFactoryImplementor sessionFactoryImplementor = (SessionFactoryImplementor) sessionFactory;
    //        MetamodelImplementor metamodelImplementor = sessionFactoryImplementor.getMetamodel();
    //
    //        try
    //        {
    //            metamodelImplementor.entityPersister( klass );
    //        }
    //        catch ( MappingException ex )
    //        {
    //            // Class is not persisted with Hibernate
    //            return new HashMap<>();
    //        }
    //
    //        Map<String, Property> properties = new HashMap<>();
    //
    //        MetadataImplementor metadataImplementor = HibernateMetadata.getMetadataImplementor();
    //
    //        if ( metadataImplementor == null )
    //        {
    //            return new HashMap<>();
    //        }
    //
    //        PersistentClass persistentClass = metadataImplementor.getEntityBinding( klass.getName() );
    //
    //        Iterator<?> propertyIterator = persistentClass.getPropertyClosureIterator();
    //
    //        while ( propertyIterator.hasNext() )
    //        {
    //            Property property = new Property( klass );
    //            property.setRequired( false );
    //            property.setPersisted( true );
    //            property.setOwner( true );
    //
    //            org.hibernate.mapping.Property hibernateProperty = (org.hibernate.mapping.Property) propertyIterator.next();
    //            Type type = hibernateProperty.getType();
    //
    //            property.setName( hibernateProperty.getName() );
    //            property.setCascade( hibernateProperty.getCascade() );
    //            property.setCollection( type.isCollectionType() );
    //
    //            property.setSetterMethod( hibernateProperty.getSetter( klass ).getMethod() );
    //            property.setGetterMethod( hibernateProperty.getGetter( klass ).getMethod() );
    //
    //            if ( property.isCollection() )
    //            {
    //                CollectionType collectionType = (CollectionType) type;
    //                CollectionPersister persister = metamodelImplementor.collectionPersister( collectionType.getRole() );
    //
    //                property.setOwner( !persister.isInverse() );
    //                property.setManyToMany( persister.isManyToMany() );
    //
    //                property.setMin( 0d );
    //                property.setMax( Double.MAX_VALUE );
    //
    //                if ( property.isOwner() )
    //                {
    //                    property.setOwningRole( collectionType.getRole() );
    //                    property.setInverseRole( roleToRole.get( collectionType.getRole() ) );
    //                }
    //                else
    //                {
    //                    property.setOwningRole( roleToRole.get( collectionType.getRole() ) );
    //                    property.setInverseRole( collectionType.getRole() );
    //                }
    //            }
    //
    //            if ( type instanceof SingleColumnType || type instanceof CustomType
    //                || type instanceof ManyToOneType)
    //            {
    //                Column column = (Column) hibernateProperty.getColumnIterator().next();
    //
    //                property.setUnique( column.isUnique() );
    //                property.setRequired( !column.isNullable() );
    //                property.setMin( 0d );
    //                property.setMax( (double) column.getLength() );
    //                property.setLength( column.getLength() );
    //
    //                if ( type instanceof TextType)
    //                {
    //                    property.setMin( 0d );
    //                    property.setMax( (double) Integer.MAX_VALUE );
    //                    property.setLength( Integer.MAX_VALUE );
    //                }
    //                else if ( type instanceof IntegerType)
    //                {
    //                    property.setMin( (double) Integer.MIN_VALUE );
    //                    property.setMax( (double) Integer.MAX_VALUE );
    //                    property.setLength( Integer.MAX_VALUE );
    //                }
    //                else if ( type instanceof LongType)
    //                {
    //                    property.setMin( (double) Long.MIN_VALUE );
    //                    property.setMax( (double) Long.MAX_VALUE );
    //                    property.setLength( Integer.MAX_VALUE );
    //                }
    //                else if ( type instanceof DoubleType)
    //                {
    //                    property.setMin( -Double.MAX_VALUE );
    //                    property.setMax( Double.MAX_VALUE );
    //                    property.setLength( Integer.MAX_VALUE );
    //                }
    //            }
    //
    //            if ( type instanceof ManyToOneType)
    //            {
    //                property.setManyToOne( true );
    //                property.setRequired( property.isRequired() && !property.isCollection() );
    //
    //                if ( property.isOwner() )
    //                {
    //                    property.setOwningRole( klass.getName() + "." + property.getName() );
    //                }
    //                else
    //                {
    //                    property.setInverseRole( klass.getName() + "." + property.getName() );
    //                }
    //            }
    //            else if ( type instanceof OneToOneType)
    //            {
    //                property.setOneToOne( true );
    //            }
    //
    //            properties.put( property.getName(), property );
    //        }
    //
    //        return properties;
    //    }
}
