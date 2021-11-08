package org.nmcpye.activitiesmanagement.extended.schema.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.FIELD, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Property {
    org.nmcpye.activitiesmanagement.extended.schema.PropertyType value() default org.nmcpye.activitiesmanagement.extended.schema.PropertyType.TEXT;

    Value required() default Value.DEFAULT;

    Value persisted() default Value.DEFAULT;

    Value owner() default Value.DEFAULT;

    Access access() default Access.READ_WRITE;

    /**
     * This is essentially a manual override to specify the
     * {@link org.nmcpye.activitiesmanagement.extended.schema.Property#getFieldName()} of the annotated
     * member.
     *
     * @return Name of the field this property is persisted as in case this is a
     *         non persistent property which has a corresponding persistent
     *         member.
     */
    String persistedAs() default "";

    enum Value
    {
        TRUE,
        FALSE,
        DEFAULT
    }

    enum Access
    {
        READ_ONLY,
        WRITE_ONLY,
        READ_WRITE;

        public boolean isReadable()
        {
            return READ_ONLY == this || READ_WRITE == this;
        }

        public boolean isWritable()
        {
            return WRITE_ONLY == this || READ_WRITE == this;
        }
    }
}
