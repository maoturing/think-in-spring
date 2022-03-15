package org.geekbang.bean.lifecycle;

import org.geekbang.ioc.overview.lookup.domain.User;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.util.Map;

/**
 * xml 配置bean元信息示例
 *
 * @author mao  2021/4/19 18:15
 */
public class BeanMetadataXmlConfigurationDemo {
    public static void main(String[] args) {
        // 1.创建 BeanFactory 容器
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(beanFactory);
        // bean 配置文件路径
        String location = "dependcy-lookup-context.xml";
        Resource resource = new ClassPathResource(location);
        // 2.加载xml配置文件
        int count = reader.loadBeanDefinitions(resource);

        // 返回 3, 与xml中定义 bean 的数量一致
        System.out.println("容器中bean的数量: " + count);

        // 3.依赖查找, 根据名称
        User user = beanFactory.getBean("user", User.class);
        System.out.println(user);
    }
}
