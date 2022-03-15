package org.geekbang.bean.lifecycle;

import org.geekbang.ioc.overview.lookup.domain.User;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.AnnotatedBeanDefinitionReader;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 注解配置 BeanDefinition 解析
 *
 * 搞得有点乱, 到底什么时候解析@Bean
 *
 * @author mao  2021/4/28 20:47
 */
@Configuration
public class AnnotatedBeanDefinitionParsingDemo {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory)applicationContext.getBeanFactory();
        // 用来解析注解类型配置的bean
        AnnotatedBeanDefinitionReader beanDefinitionReader = new AnnotatedBeanDefinitionReader(beanFactory);

        int countBefore = beanFactory.getBeanDefinitionCount();
        // 解析bean, 注册bean
        beanDefinitionReader.registerBean(AnnotatedBeanDefinitionParsingDemo.class);
        int countAfter = beanFactory.getBeanDefinitionCount();
        System.out.println("加载的bean数量: " + (countAfter - countBefore));
        applicationContext.refresh();
        // 检查是否注册成功
        AnnotatedBeanDefinitionParsingDemo bean = beanFactory.getBean(AnnotatedBeanDefinitionParsingDemo.class);
        beanFactory.getBean(User.class);
        System.out.println(bean);
//        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
//        applicationContext.register(AnnotatedBeanDefintionParsingDemo.class);
//        applicationContext.refresh();
//        User bean = applicationContext.getBean(User.class);

    }
    @Bean
    public User user(){
        User user = new User();
        user.setId(666L);
        user.setName("doomfist");
        return user;
    }


}
