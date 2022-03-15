package org.geekbang.annotation;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

/**
 * @ComponentScan 扫描示例
 * @author mao  2021/5/29 14:46
 */

// 指定classpath
@ComponentScan(value="org.geekbang.annotation")
public class ComponentScanDemo {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        applicationContext.register(ComponentScanDemo.class);

        applicationContext.refresh();
    }
}
