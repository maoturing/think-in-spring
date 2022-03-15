package org.geekbang.bean.lifecycle;

import org.geekbang.ioc.overview.lookup.domain.User;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

/**
 * Bean 实例化生命周期
 * 初始化后
 *
 * @author mao  2021/4/29 20:00
 */
public class BeanInitializationLifeCycleDemo3 {

    public static void main(String[] args) {
        // 创建容器并加载xml中的BeanDefinition
        DefaultListableBeanFactory beanFactory = getBeanFactory();
        // 1. 注册自定义的BeanPostProcessor, 保存到容器的beanPostProcessors属性
        beanFactory.addBeanPostProcessor(new MyInstantiationAwareBeanPostProcessor());
        // 2. 依赖查找, 会实例化bean
        User user = beanFactory.getBean("user", User.class);
        // 3. 期望输出bean的属性为Symmetra, 因为在BeanPostProcessor中设置了
        // 默认输出xml中配置的tracccer
        System.out.println(user);
    }

    static class MyInstantiationAwareBeanPostProcessor implements InstantiationAwareBeanPostProcessor {
        /**
         * bean初始化后回调该方法
         * 为user bean重新设置属性并返回
         */
        @Override
        public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
            if ("user".equals(beanName)) {
                User user = (User) bean;
                user.setName("Symmetra");

                return user;
            }
            return bean;
        }
    }


    private static DefaultListableBeanFactory getBeanFactory() {
        // 1.创建 BeanFactory 容器
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(beanFactory);
        // bean 配置文件路径
        String location = "dependcy-lookup-context.xml";
        Resource resource = new ClassPathResource(location);
        // 2.加载xml配置文件中的BeanDefinition
        int count = reader.loadBeanDefinitions(resource);
        return beanFactory;
    }

}


