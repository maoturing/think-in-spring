package org.geekbang.dependency.injection.setter;

import org.geekbang.ioc.overview.lookup.domain.User;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;

/**
 * 基于注解 资源配置的 Setter 方法注入示例
 *
 * @author mao  2021/4/21 23:45
 */
public class AnnotationDependencySetterInjectionDemo {
    public static void main(String[] args) {
        // 创建容器
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        applicationContext.register(AnnotationDependencySetterInjectionDemo.class);
        // 将xml中的bean注册到容器
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(applicationContext);
        String location = "dependcy-setter-injection.xml";
        reader.loadBeanDefinitions(location);
        // 启动应用上下文
        applicationContext.refresh();

        UserHolder userHolder = applicationContext.getBean("userHolder1", UserHolder.class);
        System.out.println(userHolder);

        // 关闭应用上下文
        applicationContext.close();
    }

    /**
     * 加载当前bean时, 容器中已经有了一个bean user, 故会作为参数传入
     *
     * 使用setter方法设置 user, 称为setter注入
     */
    @Bean(name = "userHolder1")
    public UserHolder userHolder(User user) {
        UserHolder userHolder = new UserHolder();
        // Setter注入
        userHolder.setUser(user);
        return userHolder;
    }
}
