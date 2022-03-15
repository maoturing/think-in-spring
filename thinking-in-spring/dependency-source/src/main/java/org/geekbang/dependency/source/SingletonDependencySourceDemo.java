package org.geekbang.dependency.source;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.Environment;

import java.util.Map;

/**
 * Spring 内建的单例对象作为依赖来源
 * 既可以作为依赖查找来源, 也可以作为依赖注入来源
 *
 * @author mao  2021/4/28 16:34
 */
public class SingletonDependencySourceDemo {

    @Autowired
    private Environment environment;

    // 注入spring内建单例对象,
    // 将字段名称作为 bean 名称去容器 singletonObjects 属性中查找
    @Autowired
    private Map systemProperties;

    @Autowired
    private Map systemEnvironment;

    @Autowired
    private MessageSource messageSource;

    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        applicationContext.register(SingletonDependencySourceDemo.class);
        applicationContext.refresh();

        SingletonDependencySourceDemo demo = applicationContext.getBean(SingletonDependencySourceDemo.class);
        System.out.println(demo.environment);
        System.out.println(demo.systemProperties.get("java.runtime.name"));
        System.out.println(demo.systemEnvironment.get("JAVA_HOME"));
        System.out.println(demo.messageSource);

        // 依赖查找
        Environment environment = applicationContext.getBean(Environment.class);
        System.out.println(environment==demo.environment);
    }
}
