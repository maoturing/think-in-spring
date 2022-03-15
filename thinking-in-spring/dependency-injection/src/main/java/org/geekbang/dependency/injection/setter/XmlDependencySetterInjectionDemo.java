package org.geekbang.dependency.injection.setter;

import org.geekbang.dependency.injection.setter.UserHolder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;

/**
 * 基于 xml 资源配置的 Setter 方法注入示例
 * @author mao  2021/4/21 23:45
 */
public class XmlDependencySetterInjectionDemo {
    public static void main(String[] args) {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(beanFactory);
        String location = "dependcy-setter-injection.xml";
        reader.loadBeanDefinitions(location);

        // 依赖查找并创建bean
        UserHolder userHolder = beanFactory.getBean(UserHolder.class);
        System.out.println(userHolder);
    }
}
