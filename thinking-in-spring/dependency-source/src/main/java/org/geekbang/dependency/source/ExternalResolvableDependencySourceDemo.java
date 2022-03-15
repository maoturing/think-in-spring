package org.geekbang.dependency.source;

import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.annotation.PostConstruct;

/**
 * 手动注册外部游离对象到容器
 *
 * 不可以作为依赖查找来源, 只能作为依赖注入来源
 *
 * @author mao  2021/4/28 14:06
 */
public class ExternalResolvableDependencySourceDemo {
    @Autowired
    private String value;

    @PostConstruct
    public void init() {
        System.out.println("依赖注入成功, value: " + value);
    }

    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        applicationContext.register(ExternalResolvableDependencySourceDemo.class);

        ConfigurableListableBeanFactory beanFactory = applicationContext.getBeanFactory();
        // 手动注册游离对象
        beanFactory.registerResolvableDependency(String.class, "hello world");

        // 启动上下文
        applicationContext.refresh();

        try {
            applicationContext.getBean(String.class);
        } catch (NoSuchBeanDefinitionException e) {
            System.out.println("容器中没有找到 String 类型的 bean, 说明 ResolvableDependency 对象无法进行依赖查找");
        }
    }
}
