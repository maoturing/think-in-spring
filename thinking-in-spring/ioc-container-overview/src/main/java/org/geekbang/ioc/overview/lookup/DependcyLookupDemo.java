package org.geekbang.ioc.overview.lookup;

import org.geekbang.ioc.overview.lookup.annotation.Super;
import org.geekbang.ioc.overview.lookup.domain.User;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Map;

/**
 * 依赖查找示例
 * 1. 通过bean的名称查找: 实时查找, 延迟查找
 * 2. 通过bean的类型查找: 一个bean对象, 所有该类型的bean对象
 * 3. 通过java注解查找:
 * 从代码可以看出, 依赖查找需要根据bean的id主动获取资源, 实现繁琐, 而且依赖 IOC 容器的 API,
 * 但是可读性好, 能清楚的看到 bean 是怎么获得的
 *
 * @author mao  2021/4/19 0:17
 */
public class DependcyLookupDemo {
    public static void main(String[] args) {
        // 1. 在 xml 文件中配置 bean
        // 2. 启动spring 应用上下文
        BeanFactory beanFactory = new ClassPathXmlApplicationContext("dependcy-lookup-context.xml");

        // 根据名称实时查找
        lookupInRealTime(beanFactory);
        // 根据名称延迟查找
        lookupInLazy(beanFactory);
//        // 根据类型查找单个对象
//        lookupByType(beanFactory);
        // 根据类型查找所有该类型的对象
        lookupAllByType(beanFactory);
        lookupByAnnotation(beanFactory);
    }

    private static void lookupByAnnotation(BeanFactory beanFactory) {
        if (beanFactory instanceof ListableBeanFactory) {
            ListableBeanFactory listableBeanFactory = (ListableBeanFactory) beanFactory;
            // 根据注解查找
            Map<String, User> users = (Map) listableBeanFactory.getBeansWithAnnotation(Super.class);
            System.out.println("查找标注@Super User 对象: " + users);
        }
    }

    private static void lookupAllByType(BeanFactory beanFactory) {
        if (beanFactory instanceof ListableBeanFactory) {
            ListableBeanFactory listableBeanFactory = (ListableBeanFactory) beanFactory;
            Map<String, User> users = listableBeanFactory.getBeansOfType(User.class);   // 根据类型查找
            System.out.println("查找到的所有 User 对象: " + users);
        }
    }

    private static void lookupByType(BeanFactory beanFactory) {
        // 3. 使用ioc容器api查找bean, 根据类型 User.class 查找
        User user = beanFactory.getBean(User.class);
        System.out.println("类型查找: " + user);
    }

    private static void lookupInRealTime(BeanFactory beanFactory) {
        // 3. 使用ioc容器api查找bean, 根据名称"user"查找
        User user = (User) beanFactory.getBean("user");
        System.out.println("实时查找: " + user);
    }

    private static void lookupInLazy(BeanFactory beanFactory) {
        // 3. 使用ioc容器api查找bean 根据名称"user"查找
        ObjectFactory<User> objectFactory = (ObjectFactory<User>) beanFactory.getBean("objectFactory");
        User user = objectFactory.getObject();
        System.out.println("延迟查找: " + user);
    }
}
