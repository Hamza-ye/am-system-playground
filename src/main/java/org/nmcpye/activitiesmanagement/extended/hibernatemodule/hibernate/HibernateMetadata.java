package org.nmcpye.activitiesmanagement.extended.hibernatemodule.hibernate;

import org.hibernate.boot.SessionFactoryBuilder;
import org.hibernate.boot.spi.MetadataImplementor;
import org.hibernate.boot.spi.SessionFactoryBuilderFactory;
import org.hibernate.boot.spi.SessionFactoryBuilderImplementor;

public class HibernateMetadata implements SessionFactoryBuilderFactory {
    private static final ThreadLocal<MetadataImplementor> metadataImplementor = new ThreadLocal<>();
//    private static MetadataImplementor metadataImplementor;

    @Override
    public SessionFactoryBuilder getSessionFactoryBuilder(MetadataImplementor metadataImplementor,
                                                          SessionFactoryBuilderImplementor defaultBuilder) {
        HibernateMetadata.metadataImplementor.set(metadataImplementor);
//        HibernateMetadata.metadataImplementor= metadataImplementor;
        return defaultBuilder;
    }

    public static MetadataImplementor getMetadataImplementor() {

        return metadataImplementor.get();
//        return metadataImplementor;
    }
}
