package org.geekbang.ioc.overview.container;

import org.geekbang.ioc.overview.lookup.domain.User;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;

import java.util.Map;

/**
 * 注解 + ApplicationContext 作为 IOC 容器示例
 *
 * @author mao  2021/4/19 18:15
 */
public class AnnotationApplicationContextAsIoCContainerDemo {
    public static void main(String[] args) {
        // 1.创建 ApplicationContext 容器, 使用注解配置
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        // 2.注册主类到容器
        applicationContext.register(AnnotationApplicationContextAsIoCContainerDemo.class);
        // 3.启动应用上下文
        // 为什么 ClassPathXmlApplicationContext 不用启动? 因为在构造方法中已经调用 refresh启动了
        applicationContext.refresh();

        // 4.依赖查找, 根据名称
        User user = applicationContext.getBean("user", User.class);
        System.out.println(user);

        // 4.依赖查找所有 User 对象, 根据类型
        lookupAllByType(applicationContext);

        // 5.停止应用上下文
        applicationContext.close();
    }

    /**
     * 通过 java 注解的方式, 注册一个 bean 到 ioc 容器
     * @return
     */
    @Bean
    public User user() {
        User user = new User();
        user.setId(1L);
        user.setName("小毛");

        return user;
    }

    /**
     * 查找类型为 User 的所有对象
     * @param beanFactory
     */
    private static void lookupAllByType(BeanFactory beanFactory) {
        if (beanFactory instanceof ListableBeanFactory) {
            ListableBeanFactory listableBeanFactory = (ListableBeanFactory) beanFactory;
            Map<String, User> users = listableBeanFactory.getBeansOfType(User.class);   // 根据类型查找
            System.out.println("查找到的所有 User 对象: " + users);
        }
    }
}
