package org.nmcpye.activitiesmanagement.extended.node.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@NodeAnnotation
public @interface NodeRoot {
    String value() default "";

    String namespace() default "";

    boolean isWritable() default true;

    boolean isReadable() default true;
}
