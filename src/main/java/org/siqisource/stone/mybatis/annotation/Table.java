package org.siqisource.stone.mybatis.annotation;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Target({ ElementType.TYPE })
@Retention(RUNTIME)
public @interface Table {

	String schema() default "";

	String value() default "";

	KeyGenerator keyGenerator() default KeyGenerator.sequence;

	String keySequence() default "";

}
