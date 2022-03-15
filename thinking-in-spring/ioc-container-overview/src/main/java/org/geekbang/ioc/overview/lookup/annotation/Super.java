package org.geekbang.ioc.overview.lookup.annotation;


import java.lang.annotation.*;

/**
 * 超级注解, 表示标记的类是超级用户
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Super {

    String value() default "";
}
