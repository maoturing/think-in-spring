package org.geekbang.bean.lifecycle;

import org.geekbang.ioc.overview.lookup.domain.SuperUser;
import org.geekbang.ioc.overview.lookup.domain.User;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

/**
 * Bean 实例化生命周期
 * 测试实例化前
 *
 * @author mao  2021/4/29 20:00
 */
public class BeanInstantiationLifeCycleDemo {

    public static void main(String[] args) {
        // 创建容器并加载xml中的BeanDefinition
        DefaultListableBeanFactory beanFactory = getBeanFactory();

        // 1. 注册自定义的BeanPostProcessor, 保存到容器的beanPostProcessors属性
        beanFactory.addBeanPostProcessor(new MyInstantiationAwareBeanPostProcessor());
        // 2. 依赖查找, 会实例化bean
        User user = beanFactory.getBean("user", User.class);
        SuperUser superUser = beanFactory.getBean(SuperUser.class);

        // 3. 期望输出widowmaker, 因为在实例化前我们将bean替换了
        // 如果没有自定义的前置beanPostProcessors, 期待输出 tracccer
        System.out.println(user);
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

    static class MyInstantiationAwareBeanPostProcessor implements InstantiationAwareBeanPostProcessor {
        /**
         * 当 user 在实例化时, 手动创建一个user对象并返回
         * 返回为null表示使用spring容器实例化bean
         */
        @Override
        public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException {
            if ("user".equals(beanName)) {
                System.out.println(beanName + " bean 实例化前回调...");
                User user = new User();
                user.setName("widowmaker");

                return user;
            }
            return null;
        }
    }
}


