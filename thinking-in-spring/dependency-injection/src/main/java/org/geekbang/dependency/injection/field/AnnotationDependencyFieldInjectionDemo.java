package org.geekbang.dependency.injection.field;

import org.geekbang.dependency.injection.setter.UserHolder;
import org.geekbang.ioc.overview.lookup.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;

import javax.annotation.Resource;
import javax.inject.Inject;

/**
 * 字段注入
 * 1.@Autowired
 * 2.@Resource
 * 3.@Inject
 *
 * @author mao  2021/4/21 23:45
 */
public class AnnotationDependencyFieldInjectionDemo {
    // 字段注入
    @Autowired
    private UserHolder userHolder2;

//    @Resource(name = "xxx", type = UserHolder.class)
    @Resource   // 默认会先按照属性名查找bean,再按照类型去查找bean
    private UserHolder userHolder3;

    @Inject
    private UserHolder userHolder4;

    public static void main(String[] args) {
        // 创建容器, 加载xml中的bean
        AnnotationConfigApplicationContext applicationContext = getApplicationContext();
        // 启动应用上下文
        applicationContext.refresh();

        // 从容器获取当前主类, 该主类已经被注册到了容器
        AnnotationDependencyFieldInjectionDemo demo = applicationContext.getBean(AnnotationDependencyFieldInjectionDemo.class);
        System.out.println("userHolder2: " + demo.userHolder2);
        System.out.println("userHolder3: " + demo.userHolder3);
        System.out.println("userHolder3: " + demo.userHolder4);
        System.out.println("userHolder2 == userHolder3: " + (demo.userHolder2 == demo.userHolder3));
        System.out.println("userHolder2 == userHolder4: " + (demo.userHolder2 == demo.userHolder4));

        // 关闭应用上下文
        applicationContext.close();
    }


    private static AnnotationConfigApplicationContext getApplicationContext() {
        // 创建容器
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        // 注册主类到容器
        applicationContext.register(AnnotationDependencyFieldInjectionDemo.class);
        // 将xml中的两个bean注册到容器
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(applicationContext);
        String location = "dependcy-setter-injection.xml";
        reader.loadBeanDefinitions(location);
        return applicationContext;
    }
}
