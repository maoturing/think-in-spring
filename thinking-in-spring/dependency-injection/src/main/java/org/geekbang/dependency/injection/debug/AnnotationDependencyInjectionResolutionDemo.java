package org.geekbang.dependency.injection.debug;

import org.geekbang.ioc.overview.lookup.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Lazy;

import javax.inject.Inject;
import java.util.Map;
import java.util.Optional;

/**
 * 依赖注入的处理过程
 *
 * @author mao  2021/4/21 23:45
 */
public class AnnotationDependencyInjectionResolutionDemo {
    // DependencyDescriptor:
    /**
     * DependencyDescriptor:
     * 必须(required=true)
     * 实时注入(eager=true)
     * 通过类型(User.class)依赖查找
     * 字段名称("user")
     * 是否首要(primary=true)
     */
    @Autowired
    private User user;

    @Inject
    private User injectedUser;

    /**
     * 集合类型的依赖注入, key是bean名称, value是bean实例
     */
    @Autowired
    private Map<String, User> users;

    @Autowired
    private Optional<User> userOptional;    // superUser

    @Autowired
    @Lazy
    private User lazyUser;    // superUser


    public static void main(String[] args) {
        // 创建容器, 加载当前类中的bean, 加载xml配置中的两个bean
        // user, superUser, superUser有primary属性, 优先匹配, 且有 address属性
        AnnotationConfigApplicationContext applicationContext = getApplicationContext();
        // 启动 Spring 应用上下文
        applicationContext.refresh();

        AnnotationDependencyInjectionResolutionDemo demo = applicationContext.getBean(AnnotationDependencyInjectionResolutionDemo.class);
        //期待输出 superUser
        System.out.println("User user: " + demo.user);
        //期待输出 superUser
        System.out.println("User injectedUser: " + demo.injectedUser);

        // 期待输出 user, superUser
        System.out.println("Map users" + demo.users);
        // 期待输出 superUser
        System.out.println("Optional<User> userOptional: " + demo.userOptional);

        // lazyUser是一个代理对象, 虽然和user属性完全一致, 但二者并不相等
        System.out.println("User lazyUser: " + demo.lazyUser);
        System.out.println("lazyUser==user: " + (demo.lazyUser == demo.user));
    }

    private static AnnotationConfigApplicationContext getApplicationContext() {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        applicationContext.register(AnnotationDependencyInjectionResolutionDemo.class);
        XmlBeanDefinitionReader beanDefinitionReader = new XmlBeanDefinitionReader(applicationContext);
        String xmlResourcePath = "dependcy-resolve-injection.xml";
        // 加载 XML 资源，解析并且生成 BeanDefinition
        beanDefinitionReader.loadBeanDefinitions(xmlResourcePath);
        return applicationContext;
    }
}
