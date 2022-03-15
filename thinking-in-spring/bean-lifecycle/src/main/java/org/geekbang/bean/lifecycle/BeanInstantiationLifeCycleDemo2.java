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
 * 测试实例化后
 *
 * @author mao  2021/4/29 20:00
 */
public class BeanInstantiationLifeCycleDemo2 {

    public static void main(String[] args) {
        // 创建容器并加载xml中的BeanDefinition
        DefaultListableBeanFactory beanFactory = getBeanFactory();

        // 1. 注册自定义的BeanPostProcessor, 保存到容器的beanPostProcessors属性
        beanFactory.addBeanPostProcessor(new MyInstantiationAwareBeanPostProcessor());
        // 2. 依赖查找, 会实例化bean
        User user = beanFactory.getBean("user", User.class);
        SuperUser superUser = beanFactory.getBean(SuperUser.class);
        // 3. 期望输出bean的属性为null, 因为在实例化后我们返回了false表示不为属性赋值
        // 如果没有自定义的后置beanPostProcessors, 期待输出xml中配置的 tracccer
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
         * bean 实例化后调用
         * 返回false表示不要为bean设置属性
         * 返回true表示要为bean设置属性
         * 注释掉该方法, user bean 属性有值
         */
        @Override
        public boolean postProcessAfterInstantiation(Object bean, String beanName) throws BeansException {
            // user bean 不要为属性赋值
            if ("user".equals(beanName)) {
                return false;
            }
            return true;
        }
    }
}


