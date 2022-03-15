package org.geekbang.bean.lifecycle;

import org.geekbang.ioc.overview.lookup.domain.SuperUser;
import org.geekbang.ioc.overview.lookup.domain.User;
import org.springframework.beans.factory.support.AbstractBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

/**
 * BeanDefinition 合并示例
 *
 * debug 下面这个方法, 查找BeanDefinition如何合并的
 * @see AbstractBeanFactory#getMergedLocalBeanDefinition(java.lang.String)
 *
 * @author mao  2021/4/29 17:07
 */
public class MergedBeanDefinitionDemo {
    public static void main(String[] args) {
        // 1.创建 BeanFactory 容器
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(beanFactory);
        // bean 配置文件路径
        String location = "dependcy-lookup-context.xml";
        Resource resource = new ClassPathResource(location);
        // 2.加载xml配置文件
        int count = reader.loadBeanDefinitions(resource);


        // 在获取bean时会先获取BeanDefinition, 然后利用BeanDefinition创建bean
        // user没有parent, 会将自身元信息GenericBeanDefinition封装为RootBeanDefinition返回
        User user = beanFactory.getBean("user", User.class);
        // superUser有parent, 会将parent的元信息, 也就是user, 与自身合并, 封装为RootBeanDefinition
        // 这也是为什么我们能在 superUser 中看到user中配置的属性的原因
        SuperUser superUser = beanFactory.getBean(SuperUser.class);

        System.out.println(user);
        System.out.println(superUser);
    }
}
