package org.geekbang.bean.lifecycle.destory;

import org.geekbang.ioc.overview.lookup.domain.User;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.DestructionAwareBeanPostProcessor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

/**
 * Bean销毁前
 *
 * @author mao  2021/5/13 3:12
 */
public class BeanDestoryLifeCycleDemo {

    public static void main(String[] args) {
        // 1.创建并启动 ApplicationContext 容器, 使用注解配置
        AnnotationConfigApplicationContext applicationContext = getApplicationContext();
        // 2.注册自定义BeanPostProcessor,在bean销毁前调用
        ConfigurableListableBeanFactory beanFactory = applicationContext.getBeanFactory();
        beanFactory.addBeanPostProcessor(new LifecycleDestructionPostProcessor());
        // 3. 启动容器
        applicationContext.refresh();

        // 4. 关闭容器, 开始销毁bean, 此时会回调自定义BeanPostProcessor
        applicationContext.close();
    }


    static class LifecycleDestructionPostProcessor implements DestructionAwareBeanPostProcessor {

        @Override
        public void postProcessBeforeDestruction(Object bean, String beanName) throws BeansException {
            if (bean instanceof User) {
                User user = (User) bean;
                System.out.println( user.getName() + "被放走了 ......");
            }
        }
    }

    private static AnnotationConfigApplicationContext getApplicationContext() {
        // 1.创建并启动 ApplicationContext 容器, 使用注解配置
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        applicationContext.register(BeanDestoryLifeCycleDemo.class);
        DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) applicationContext.getBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(beanFactory);
        // bean 配置文件路径
        String location = "dependcy-lookup-context.xml";
        Resource resource = new ClassPathResource(location);
        // 2.加载xml配置文件中的BeanDefinition
        int count = reader.loadBeanDefinitions(resource);
        return applicationContext;
    }
}