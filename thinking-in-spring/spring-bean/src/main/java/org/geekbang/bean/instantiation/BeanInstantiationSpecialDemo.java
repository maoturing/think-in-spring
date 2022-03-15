package org.geekbang.bean.instantiation;

import org.geekbang.bean.definition.component.Student;
import org.geekbang.ioc.overview.lookup.domain.User;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Iterator;
import java.util.ServiceLoader;

/**
 * bean 实例化特殊方式示例
 * 1. 通过 ServiceLoaderFactoryBean 实例化 bean，
 *
 * @author mao  2021/4/20 10:04
 */
public class BeanInstantiationSpecialDemo {
    public static void main(String[] args) {
        // 测试 serviceLoader
        // serviceLoaderDemo();

        // 1. 通过 ServiceLoaderFactoryBean 实例化 bean
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("bean-instantiation-special-context.xml");
        // xml中配置了 bean ServiceLoaderFactoryBean, 获取 ServiceLoader 实例
        ServiceLoader<UserFactory> serviceLoader = applicationContext.getBean("userFacotoryServiceLoader", ServiceLoader.class);

        // 遍历 ServiceLoader 实例
        for (UserFactory userFactory : serviceLoader) {
            System.out.println("ServiceLoaderFactoryBean加载实例化的类：" + userFactory);
            System.out.println("使用bean工厂UserFactory创建user：" + userFactory.createUser());
        }

        // 2. 通过 AutowireCapableBeanFactory 实例化 bean
        AutowireCapableBeanFactory autowireCapableBeanFactory = applicationContext.getAutowireCapableBeanFactory();
        User user = autowireCapableBeanFactory.createBean(User.class);
        System.out.println(user);

        UserFactory userFactory = autowireCapableBeanFactory.createBean(DefaultUserFactory.class);
        System.out.println(userFactory.createUser());
    }

    /**
     * 使用 serviceLoader 加载 META-INF/service 下配置的类
     * 注意这是 java 的内容
     */
    public static void serviceLoaderDemo() {
        // 使用ServiceLoader加载 META-INF/services/ 下配置的 UserFactory 实现类
        ServiceLoader<UserFactory> serviceLoader = ServiceLoader.load(UserFactory.class);

        // 遍历加载到的 UserFactory 实现类
        for (UserFactory userFactory : serviceLoader) {
            System.out.println("ServiceLoader加载的类：" + userFactory.createUser());
        }
    }
}
