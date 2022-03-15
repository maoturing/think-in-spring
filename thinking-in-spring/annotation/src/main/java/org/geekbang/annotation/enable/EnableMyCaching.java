package org.geekbang.annotation.enable;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 激活 HelloWorld 模块
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
// 导入具体实现
@Import(MyCachingImportSelector.class)
public @interface EnableMyCaching {
}
