package org.nmcpye.activitiesmanagement.extended.schemamodule.introspection;

import org.hibernate.MappingException;
import org.hibernate.boot.spi.MetadataImplementor;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.mapping.Column;
import org.hibernate.mapping.OneToMany;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.metamodel.spi.MetamodelImplementor;
import org.hibernate.persister.collection.CollectionPersister;
import org.hibernate.persister.entity.Joinable;
import org.hibernate.type.*;
import org.nmcpye.activitiesmanagement.extended.hibernatemodule.hibernate.HibernateMetadata;
import org.nmcpye.activitiesmanagement.extended.schema.Property;

import javax.persistence.EntityManagerFactory;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * A {@link PropertyIntrospector} that extract information from Hibernate
 * mappings.
 * <p>
 * It assumes that no {@link PropertyIntrospector} has already provided any
 * baseline.
 */
public class HibernatePropertyIntrospector implements PropertyIntrospector {


    private final EntityManagerFactory entityManagerFactory;


    private final Map<String, String> roleToRole = new ConcurrentHashMap<>();

    private final AtomicBoolean roleToRoleComputing = new AtomicBoolean(false);

    public HibernatePropertyIntrospector(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    private void updateJoinTables(Class<?> klass) {
        if (!roleToRoleComputing.compareAndSet(false, true)) {
            // when we already started the below computation we do not want to
            // do it again
            return;
        }

        Map<String, List<String>> joinTableToRoles = new HashMap<>();

//        SessionFactoryImplementor sessionFactoryImplementor = (SessionFactoryImplementor) entityManagerFactory.unwrap(SessionFactory.class);
        SessionFactoryImplementor sessionFactoryImplementor = entityManagerFactory.unwrap(SessionFactoryImplementor.class);
        MetamodelImplementor metamodelImplementor = sessionFactoryImplementor.getMetamodel();

        for (CollectionPersister collectionPersister : metamodelImplementor.collectionPersisters().values()) {
            CollectionType collectionType = collectionPersister.getCollectionType();

            if (collectionPersister.isManyToMany() && collectionType.isAssociationType()) {
                Joinable associatedJoinable = collectionType.getAssociatedJoinable(sessionFactoryImplementor);

                if (!joinTableToRoles.containsKey(associatedJoinable.getTableName())) {
                    joinTableToRoles.put(associatedJoinable.getTableName(), new ArrayList<>());
                }

                joinTableToRoles.get(associatedJoinable.getTableName()).add(collectionPersister.getRole());
            } else if (collectionPersister.isInverse() && collectionType instanceof SetType) {
                SetType setType = (SetType) collectionType;
                setType.getAssociatedJoinable(sessionFactoryImplementor);
            }
        }

        joinTableToRoles.entrySet().removeIf(entry -> entry.getValue().size() < 2);

        for (Map.Entry<String, List<String>> entry : joinTableToRoles.entrySet()) {
            roleToRole.put(entry.getValue().get(0), entry.getValue().get(1));
            roleToRole.put(entry.getValue().get(1), entry.getValue().get(0));
        }
    }

    @Override
    public void introspect(Class<?> klass, Map<String, Property> properties) {
        updateJoinTables(klass);
        MetamodelImplementor metamodelImplementor = getMetamodelImplementor();

        try {
            metamodelImplementor.entityPersister(klass);
        } catch (MappingException ex) {
            // Class is not persisted with Hibernate
            return;
        }

        MetadataImplementor metadataImplementor = HibernateMetadata.getMetadataImplementor();

        if (metadataImplementor == null) {
            return;
        }

        PersistentClass persistentClass = metadataImplementor.getEntityBinding(klass.getName());

        Iterator<?> propertyIterator = persistentClass.getPropertyClosureIterator();

        while (propertyIterator.hasNext()) {
            Property property = createProperty(klass, (org.hibernate.mapping.Property) propertyIterator.next(),
                metamodelImplementor);
            properties.put(property.getName(), property);
        }
    }

    private MetamodelImplementor getMetamodelImplementor() {
//        return ((SessionFactoryImplementor) entityManagerFactory.unwrap(SessionFactory.class)).getMetamodel();
        return entityManagerFactory.unwrap(SessionFactoryImplementor.class).getMetamodel();
    }

    private Property createProperty(Class<?> klass, org.hibernate.mapping.Property hibernateProperty,
                                    MetamodelImplementor metamodelImplementor) {
        Property property = new Property(klass);
        property.setRequired(false);
        property.setPersisted(true);
        property.setWritable(true);
        property.setOwner(true);

        Type type = hibernateProperty.getType();

        property.setName(hibernateProperty.getName());
        property.setFieldName(hibernateProperty.getName());
        property.setCascade(hibernateProperty.getCascade());
        property.setCollection(type.isCollectionType());

        property.setSetterMethod(hibernateProperty.getSetter(klass).getMethod());
        property.setGetterMethod(hibernateProperty.getGetter(klass).getMethod());

        if (property.isCollection()) {
            initCollectionProperty(metamodelImplementor, property, (CollectionType) type);
        }

        if (type instanceof SingleColumnType || type instanceof CustomType || type instanceof ManyToOneType) {
            Column column = (Column) hibernateProperty.getColumnIterator().next();

            property.setUnique(column.isUnique());
            property.setRequired(!column.isNullable());
            property.setMin(0d);
            property.setMax((double) column.getLength());
            property.setLength(column.getLength());

            if (type instanceof TextType) {
                property.setMin(0d);
                property.setMax((double) Integer.MAX_VALUE);
                property.setLength(Integer.MAX_VALUE);
            } else if (type instanceof IntegerType) {
                property.setMin((double) Integer.MIN_VALUE);
                property.setMax((double) Integer.MAX_VALUE);
                property.setLength(Integer.MAX_VALUE);
            } else if (type instanceof LongType) {
                property.setMin((double) Long.MIN_VALUE);
                property.setMax((double) Long.MAX_VALUE);
                property.setLength(Integer.MAX_VALUE);
            } else if (type instanceof DoubleType) {
                property.setMin(-Double.MAX_VALUE);
                property.setMax(Double.MAX_VALUE);
                property.setLength(Integer.MAX_VALUE);
            }
        }

        if (type instanceof ManyToOneType) {
            property.setManyToOne(true);
            property.setRequired(property.isRequired() && !property.isCollection());

            if (property.isOwner()) {
                property.setOwningRole(klass.getName() + "." + property.getName());
            } else {
                property.setInverseRole(klass.getName() + "." + property.getName());
            }
        } else if (type instanceof OneToOneType) {
            property.setOneToOne(true);
        } else if (type instanceof OneToMany) {
            property.setOneToMany(true);
        }
        return property;
    }

    private void initCollectionProperty(MetamodelImplementor metamodelImplementor, Property property,
                                        CollectionType type) {
        CollectionPersister persister = metamodelImplementor.collectionPersister(type.getRole());

        property.setOwner(!persister.isInverse());
        property.setManyToMany(persister.isManyToMany());
        property.setOneToMany(persister.isOneToMany());

        property.setMin(0d);
        property.setMax(Double.MAX_VALUE);

        if (property.isOwner()) {
            property.setOwningRole(type.getRole());
            property.setInverseRole(roleToRole.get(type.getRole()));
        } else {
            property.setOwningRole(roleToRole.get(type.getRole()));
            property.setInverseRole(type.getRole());
        }
    }
}
