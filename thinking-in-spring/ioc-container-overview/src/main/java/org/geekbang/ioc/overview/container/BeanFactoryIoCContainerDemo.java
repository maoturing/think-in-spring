package org.geekbang.ioc.overview.container;

import org.geekbang.ioc.overview.lookup.domain.User;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;

import java.util.Map;

/**
 * BeanFactory 作为 IoC 容器示例
 *
 * @author mao  2021/4/19 18:15
 */
public class BeanFactoryIoCContainerDemo {
    public static void main(String[] args) {
        // 1.创建 BeanFactory 容器
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(beanFactory);
        // bean 配置文件路径
        String location = "dependcy-lookup-context.xml";
        // 2.加载xml配置文件
        int count = reader.loadBeanDefinitions(location);

        // 返回 3, 与xml中定义 bean 的数量一致
        System.out.println("容器中bean的数量: " + count);

        // 3.依赖查找, 根据名称
        User user = beanFactory.getBean("user", User.class);
        System.out.println(user);
        // 4.依赖查找所有 User 对象, 根据类型
        lookupAllByType(beanFactory);
    }

    /**
     * 查找类型为 User 的所有对象
     * @param beanFactory
     */
    private static void lookupAllByType(BeanFactory beanFactory) {
        if (beanFactory instanceof ListableBeanFactory) {
            ListableBeanFactory listableBeanFactory = (ListableBeanFactory) beanFactory;
            Map<String, User> users = listableBeanFactory.getBeansOfType(User.class);   // 根据类型查找
            System.out.println("查找到的所有 User 对象: " + users);
        }
    }
}
