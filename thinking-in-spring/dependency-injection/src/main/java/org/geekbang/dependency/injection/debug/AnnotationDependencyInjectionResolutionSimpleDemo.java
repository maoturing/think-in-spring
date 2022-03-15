package org.geekbang.dependency.injection.debug;

import org.geekbang.ioc.overview.lookup.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.annotation.Resource;

/**
 * @Autowired 依赖注入过程简单demo
 * 用来debug @Autowired 依赖注入过程
 *
 * @author mao  2021/4/21 23:45
 */
public class AnnotationDependencyInjectionResolutionSimpleDemo {
    /**
     * DependencyDescriptor:
     * 必须(required=true)
     * 实时注入(eager=true)
     * 通过类型(User.class)依赖查找
     * 字段名称("user")
     * 是否首要(primary=true)
     *
     * InjectedElement:
     * member:
     * isField: true
     *
     */
    @Autowired
    private User user;

    public static void main(String[] args) {
        // 创建容器, 加载当前类中的bean, 加载xml配置中的两个bean
        // user, superUser, superUser有primary属性, 优先匹配, 且有 address属性
        AnnotationConfigApplicationContext applicationContext = getApplicationContext();
        // 启动 Spring 应用上下文
        applicationContext.refresh();

        AnnotationDependencyInjectionResolutionSimpleDemo demo = applicationContext.getBean(AnnotationDependencyInjectionResolutionSimpleDemo.class);
        //期待输出 superUser
        System.out.println("User user: " + demo.user);
    }

    private static AnnotationConfigApplicationContext getApplicationContext() {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        applicationContext.register(AnnotationDependencyInjectionResolutionSimpleDemo.class);
        XmlBeanDefinitionReader beanDefinitionReader = new XmlBeanDefinitionReader(applicationContext);
        String xmlResourcePath = "dependcy-resolve-injection.xml";
        // 加载 XML 资源，解析并且生成 BeanDefinition
        beanDefinitionReader.loadBeanDefinitions(xmlResourcePath);
        return applicationContext;
    }
}
