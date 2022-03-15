package org.geekbang.dependency.injection.setter;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;

/**
 * 基于api 资源配置的 Setter 方法注入示例
 *
 * @author mao  2021/4/21 23:45
 */
public class ApiDependencySetterInjectionDemo {
    public static void main(String[] args) {
        // 创建容器, 读取xml中的bean: user
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(beanFactory);
        String location = "dependcy-setter-injection.xml";
        reader.loadBeanDefinitions(location);

        // 1.创建 userHolder BeanDefinition
        BeanDefinition userHolderBeanDefinition = createUserHolderBeanDefinition();
        // 2.注册 userHolder1 到容器
        beanFactory.registerBeanDefinition("userHolder1", userHolderBeanDefinition);

        // 3.查找bean, 查看是否注入成功
        UserHolder userHolder = beanFactory.getBean("userHolder1", UserHolder.class);
        System.out.println(userHolder);
    }

    public static BeanDefinition createUserHolderBeanDefinition() {
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(UserHolder.class);

        // setter 注入引用, 前面是字段名, 后面是bean名称
        builder.addPropertyReference("user", "user1");
        return builder.getBeanDefinition();
    }
}