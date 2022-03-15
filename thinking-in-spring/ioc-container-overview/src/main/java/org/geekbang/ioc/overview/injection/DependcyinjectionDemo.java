package org.geekbang.ioc.overview.injection;

import javafx.application.Application;
import org.geekbang.ioc.overview.injection.repository.UserRepository;
import org.geekbang.ioc.overview.lookup.annotation.Super;
import org.geekbang.ioc.overview.lookup.domain.User;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractRefreshableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.env.Environment;

import java.util.Map;

/**
 * 依赖注入示例
 *
 * @author mao  2021/4/19 0:17
 */
public class DependcyinjectionDemo {
    public static void main(String[] args) {
        // 1. 在 xml 文件中配置 bean
        // 2. 启动spring 应用上下文
        BeanFactory beanFactory = new ClassPathXmlApplicationContext("dependcy-injection-context.xml");

        // 依赖查找获取自定义bean
        UserRepository userRepository = beanFactory.getBean("userRepository", UserRepository.class);
        // 这里检测是否所有user对象是否自动注入到了users属性中
        System.out.println("自动注入: " + userRepository.getUsers());

        // 获取beanFactory, 依赖注入内建依赖
        System.out.println("beanFactory: " + userRepository.getBeanFactory());        // 依赖注入
        whoIsIOCContainer(userRepository, beanFactory);

        // 依赖查找, 会报错
//        System.out.println(beanFactory.getBean(BeanFactory.class));

        // 获取objectFactory
        ObjectFactory<ApplicationContext> objectFactory = userRepository.getObjectFactory();
        System.out.println("ApplicationContext: " + objectFactory.getObject());
        System.out.println(objectFactory.getObject() == beanFactory);

        // 获取容器内建bean Environment
        Environment environment = beanFactory.getBean(Environment.class);
        System.out.println("容器 Environment 类型的内建 bean: " + environment);
    }

    /**
     * 这个表达式为什么为 false?
     * <p>
     * userRepository.getBeanFactory() 返回的是对象 DefaultListableBeanFactory@1719
     * beanFactory 其实是 ClassPathXmlApplicationContext, 他的属性 beanFactory 保存了对象 DefaultListableBeanFactory@1719
     * 即 ClassPathXmlApplicationContext 是使用组合的方式来扩展 DefaultListableBeanFactory 的,
     * <p>
     * 前者返回 DefaultListableBeanFactory 类型的对象, 后者返回的是 ClassPathXmlApplicationContext 对象, 故返回false
     *
     * @param userRepository
     * @param beanFactory
     */
    private static void whoIsIOCContainer(UserRepository userRepository, BeanFactory beanFactory) {
        // 这个表达式为什么为 false?
        System.out.println(beanFactory == userRepository.getBeanFactory());     // false

        AbstractRefreshableApplicationContext applicationContext = (AbstractRefreshableApplicationContext) beanFactory;
        // 打印 ClassPathXmlApplicationContext 的 beanFactory 属性
        System.out.println("beanFactory: " + applicationContext.getBeanFactory());

        // 判断ClassPathXmlApplicationContext 的 beanFactory 属性是否与依赖注入的 beanFactory 相等
        System.out.println(applicationContext.getBeanFactory() == userRepository.getBeanFactory());     // true
        System.out.println("======================");
    }
}
