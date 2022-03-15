package org.geekbang.dependency.lookup;

import org.geekbang.ioc.overview.lookup.domain.User;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;

/**
 * 类型安全 以来查找示例
 *
 * @author mao  2021/4/21 14:49
 */
public class DependencyLookupSafeDemo {

    public static void main(String[] args) {
        // 1.创建 ApplicationContext 容器, 使用注解配置
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        // 2.将当前类作为配置类
        applicationContext.register(DependencyLookupSafeDemo.class);
        // 3.启动应用上下文
        applicationContext.refresh();

        // 4.依赖查找
        displayBeanFactoryGetBean(applicationContext);
        displayObjectFactoryGetObject(applicationContext);

        // 5.停止应用上下文
        applicationContext.close();
    }

    private static void displayObjectFactoryGetObject(AnnotationConfigApplicationContext applicationContext) {
        // 因为是延迟查找, 这一步并不会报错
        ObjectProvider<User> beanProvider = applicationContext.getBeanProvider(User.class);
        try {
            // 容器中不存在bean
            System.out.println("====ObjectFactoryGetObject====");
            beanProvider.getObject();
        } catch (BeansException e) {
            e.printStackTrace();
        }
    }

    private static void displayBeanFactoryGetBean(BeanFactory beanFactory) {
        try {
            // 容器中不存在bean
            System.out.println("====BeanFactoryGetBean====");
            beanFactory.getBean(User.class);
        } catch (BeansException e) {
            e.printStackTrace();
        }
    }
}
