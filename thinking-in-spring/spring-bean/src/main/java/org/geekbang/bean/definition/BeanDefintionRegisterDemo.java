package org.geekbang.bean.definition;

import org.geekbang.ioc.overview.lookup.domain.User;
import org.springframework.beans.factory.support.*;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.util.StringUtils;

import java.util.Map;

/**
 * 通过java api 注册 bean 到容器, 有两种方式:
 * 1. 手动设置 bean 名称
 * 2. 自动生成 bean 名称
 * @author mao  2021/4/20 1:28
 */
public class BeanDefintionRegisterDemo {
    public static void main(String[] args) {
        // 1. 创建容器
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        // 2. 注册配置类, 即代替xml配置
        applicationContext.register(BeanDefintionRegisterDemo.class);
        // 3. 启动应用上下文
        applicationContext.refresh();

        // 4.1 注册bean到容器, 手动设置bean名称
        // AnnotationConfigApplicationContext是BeanDefinitionRegistry接口的实现类
        registerUserBeanDefinition(applicationContext, "hanzo");
        User hanzo = applicationContext.getBean("hanzo", User.class);
        System.out.println("手动设置bean名称, hanzo: " + hanzo);

        // 4.2 注册bean到容器, 自动生成bean名称
        registerUserBeanDefinition(applicationContext, "");
        Map<String, User> user = applicationContext.getBeansOfType(User.class);
        // 自动生成的bean名称为 "org...User#0"
        System.out.println("自动生成bean名称, user: " + user);
    }

    private static void registerUserBeanDefinition(BeanDefinitionRegistry registry, String beanName) {
        // 1. 通过 BeanDefinitionBuilder 构建 bean
        AbstractBeanDefinition beanDefinition = BeanDefinitionBuilder.genericBeanDefinition(User.class)
                .addPropertyValue("id", 1)
                .addPropertyValue("name", "半藏")
                .setScope("singleton").getBeanDefinition();

        // 2. 注册bean到容器
        if (StringUtils.isEmpty(beanName)) {
            // 自动生成bean名称, 注册bean
            BeanDefinitionReaderUtils.registerWithGeneratedName(beanDefinition, registry);
        } else {
            // 手动设置bean名称, 注册bean
            registry.registerBeanDefinition(beanName, beanDefinition);
        }
    }
}
