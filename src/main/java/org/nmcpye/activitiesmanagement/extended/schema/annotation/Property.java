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

    enum Value {
        TRUE,
        FALSE,
        DEFAULT,
    }

    enum Access {
        READ_ONLY,
        WRITE_ONLY,
        READ_WRITE;

        public boolean isReadable() {
            return READ_ONLY == this || READ_WRITE == this;
        }

        public boolean isWritable() {
            return WRITE_ONLY == this || READ_WRITE == this;
        }
    }
}
