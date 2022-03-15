package org.geekbang.configuration.metadata;

import org.geekbang.ioc.overview.lookup.domain.User;
import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;

/**
 * Bean 配置元信息
 *
 * @author mao  2021/5/14 8:31
 */
public class BeanConfigurationMetadataDemo {
    public static void main(String[] args) {
        // 创建BeanDefinition, 为bean设置属性
        AbstractBeanDefinition beanDefinition = BeanDefinitionBuilder
                .genericBeanDefinition(User.class)
                .addPropertyValue("name", "tracccer")
                .getBeanDefinition();

        // 附加属性, 注意不是bean的属性, 不影响bean的实例化初始化
        beanDefinition.setAttribute("aaa", "小毛");
        // 设置当前Bean的来源为当前类, 可以是Class,也可以是Resource
        beanDefinition.setSource(BeanConfigurationMetadataDemo.class);

        // 创建容器并注册bean
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        beanFactory.registerBeanDefinition("user", beanDefinition);

        // beanDefinition的Attribute属性的应用, 在bean初始化后, 对bean做一些修改
        // 应用之后输出bean的name为小毛, 之前为tracccer
        beanFactory.addBeanPostProcessor(new BeanPostProcessor() {
            @Override
            public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
                if ("user".equals(beanName)) {
                    BeanDefinition userBd = beanFactory.getBeanDefinition(beanName);
                    PropertyValue property = userBd.getPropertyValues().getPropertyValue("name");
                    System.out.println("bean 修改之前的属性: name -> " + property.getValue());

                    // 修改的bean名称必须为user,来源必须是这个类
                    if (userBd.getSource().equals(BeanConfigurationMetadataDemo.class)) {
                        String attr = (String) userBd.getAttribute("aaa");
                        User user = (User) bean;
                        user.setName(attr);
                        return user;
                    }
                    return null;
                }
                return null;
            }
        });

        // 依赖查找
        User user = beanFactory.getBean(User.class);
        System.out.println(user);
    }

}
