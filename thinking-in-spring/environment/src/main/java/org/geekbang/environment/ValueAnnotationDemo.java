package org.geekbang.environment;

import org.geekbang.ioc.overview.lookup.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.support.AbstractBeanFactory;
import org.springframework.beans.factory.support.AutowireCandidateResolver;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;

/**
 * 处理 @Value 的占位符
 * 1. 获取@Value注解的value值
 * 2. 解析value值中的占位符
 *
 * @see AutowireCandidateResolver#getSuggestedValue(org.springframework.beans.factory.config.DependencyDescriptor)
 * @see AbstractBeanFactory#resolveEmbeddedValue(java.lang.String)
 *
 * @author mao  2021/6/4 7:43
 */
public class ValueAnnotationDemo {

    @Value("${user.name}")
    private String userName;

    @Autowired
    @Qualifier(value = "echo")
    private User user;

    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        applicationContext.register(ValueAnnotationDemo.class);

        applicationContext.refresh();

        ValueAnnotationDemo bean = applicationContext.getBean(ValueAnnotationDemo.class);
        System.out.println(bean.userName);
        System.out.println(bean.user);
    }

    @Bean("echo")
    public User createUser(){
        User user = new User();
        user.setName("echo");
        return user;
    }
}
