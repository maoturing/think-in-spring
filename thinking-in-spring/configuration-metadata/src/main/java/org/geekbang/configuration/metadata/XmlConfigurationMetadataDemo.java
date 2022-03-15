package org.geekbang.configuration.metadata;

import org.geekbang.ioc.overview.lookup.domain.User;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * xml配置的元信息
 * 1.beans相关
 * 2.应用上下文相关
 *
 * @author mao  2021/5/14 20:20
 */
public class XmlConfigurationMetadataDemo {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("bean-configuration-metadata.xml");
        ConfigurableListableBeanFactory beanFactory = applicationContext.getBeanFactory();

        // 获取inner beans, 查看是否继承了outter配置的default-lazy-init属性
        BeanDefinition bd = beanFactory.getBeanDefinition("user");
        System.out.println("inner bean 是否为懒加载: " + bd.isLazyInit());
        User user = applicationContext.getBean("user", User.class);
        System.out.println(user);

        // 获取outter beans, 查看utter配置的default-lazy-init属性是否生效
        BeanDefinition bd2 = beanFactory.getBeanDefinition("user2");
        System.out.println("outter bean 是否为懒加载: " + bd2.isLazyInit());
        User user2 = applicationContext.getBean("user2", User.class);
        System.out.println(user2);


        User user4 = applicationContext.getBean("user4", User.class);
        System.out.println(user4);

    }
}
