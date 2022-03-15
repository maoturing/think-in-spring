package org.geekbang.dependency.injection.constructor;

import org.geekbang.dependency.injection.setter.UserHolder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 基于 xml 资源配置的 构造方法注入示例
 * @author mao  2021/4/21 23:45
 */
public class XmlDependencyConstructorInjectionDemo {
    public static void main(String[] args) {
        // 读取xml中的配置, xml 中配置了构造器依赖注入
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("dependcy-constructor-injection.xml");

        // 查找bean, 查看依赖注入结果
        UserHolder userHolder = applicationContext.getBean(UserHolder.class);
        System.out.println(userHolder);
    }
}
