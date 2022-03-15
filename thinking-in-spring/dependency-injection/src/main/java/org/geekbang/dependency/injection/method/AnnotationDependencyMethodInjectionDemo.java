package org.geekbang.dependency.injection.method;

import org.geekbang.dependency.injection.setter.UserHolder;
import org.geekbang.ioc.overview.lookup.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;

import javax.annotation.Resource;

/**
 * 方法注入
 *
 * @author mao  2021/4/21 23:45
 */
public class AnnotationDependencyMethodInjectionDemo {

    private UserHolder userHolder2;

    private UserHolder userHolder3;

    // 方法注入
    @Autowired
    public void initUserHolder2(UserHolder userHolder) {
        this.userHolder2 = userHolder;
    }

    // 方法注入
    @Resource
    public void initUserHolder3(UserHolder userHolder) {
        this.userHolder3 = userHolder;
    }

    // 方法注入
    @Bean
    public UserHolder myHolder(UserHolder userHolder) {
        return userHolder;
    }

    public static void main(String[] args) {
        // 创建容器, 加载xml中的bean
        AnnotationConfigApplicationContext applicationContext = getApplicationContext();
        // 启动应用上下文
        applicationContext.refresh();

        // 从容器获取当前主类, 该主类已经被注册到了容器
        AnnotationDependencyMethodInjectionDemo demo = applicationContext.getBean(AnnotationDependencyMethodInjectionDemo.class);
        System.out.println("userHolder2: " + demo.userHolder2);
        System.out.println("userHolder3: " + demo.userHolder3);
        UserHolder myHolder = applicationContext.getBean("myHolder", UserHolder.class);
        System.out.println("myHolder: " + myHolder);

        System.out.println("userHolder2 == userHolder3: " + (demo.userHolder2 == demo.userHolder3));

        // 关闭应用上下文
        applicationContext.close();
    }


    private static AnnotationConfigApplicationContext getApplicationContext() {
        // 创建容器
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        // 注册主类到容器
        applicationContext.register(AnnotationDependencyMethodInjectionDemo.class);
        // 将xml中的两个bean注册到容器
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(applicationContext);
        String location = "dependcy-setter-injection.xml";
        reader.loadBeanDefinitions(location);
        return applicationContext;
    }
}
