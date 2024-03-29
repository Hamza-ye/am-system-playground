package org.nmcpye.activitiesmanagement.extended.schema;

public abstract class AbstractPropertyTransformer<T> implements PropertyTransformer {

    private final Class<?> klass;

    public AbstractPropertyTransformer(Class<?> klass) {
        this.klass = klass;
    }

    @Override
    public Class<?> getKlass() {
        return klass;
    }
}
