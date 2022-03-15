package org.geekbang.dependency.injection.debug;

import org.geekbang.ioc.overview.lookup.annotation.Super;
import org.geekbang.ioc.overview.lookup.domain.SuperUser;
import org.geekbang.ioc.overview.lookup.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.annotation.Resource;

/**
 * @Resource 依赖注入过程简单demo
 * 用来debug @Resource 依赖注入过程
 *
 * @author mao  2021/4/21 23:45
 */
public class AnnotationDependencyInjectionResolutionSimpleDemo2 {
    /**
     * 字段名称与bean名称相同的情况
     * factory.containsBean(name)为false
     * 将字段名作为bean名称, 字段类型作为bean类型去容器查找, 返回user
     */
    @Resource
    private User user;

    /**
     * 字段名称与bean名称不同的情况
     * element.isDefaultName为true, !factory.containsBean(name)为true
     * 根据字段类型去容器查找, 能找到两个, 返回primary类型的 superUser
     */
    @Resource
    private User user2;

    /**
     * 指定bean名称的情况
     * element.isDefaultName为false
     * 将name作为bean名称, 字段类型作为bean类型去容器查找, 返回user
     */
    @Resource(name="user")
    private User user3;

    /**
     * 指定bean类型的情况
     * !factory.containsBean(name)为false
     * 将type作为bean类型, 去容器查找, 返回superUser
     */
    @Resource(type= SuperUser.class)
    private User user4;


    public static void main(String[] args) {
        // 创建容器, 加载当前类中的bean, 加载xml配置中的两个bean
        // user, superUser, superUser有primary属性, 优先匹配, 且有 address属性
        AnnotationConfigApplicationContext applicationContext = getApplicationContext();
        // 启动 Spring 应用上下文
        applicationContext.refresh();

        AnnotationDependencyInjectionResolutionSimpleDemo2 demo = applicationContext.getBean(AnnotationDependencyInjectionResolutionSimpleDemo2.class);
        //期待输出 user
        System.out.println("User user: " + demo.user);
        System.out.println("User user: " + demo.user2);
        System.out.println("User user: " + demo.user3);
        System.out.println("User user: " + demo.user4);
    }

    private static AnnotationConfigApplicationContext getApplicationContext() {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        applicationContext.register(AnnotationDependencyInjectionResolutionSimpleDemo2.class);
        XmlBeanDefinitionReader beanDefinitionReader = new XmlBeanDefinitionReader(applicationContext);
        String xmlResourcePath = "dependcy-resolve-injection.xml";
        // 加载 XML 资源，解析并且生成 BeanDefinition
        beanDefinitionReader.loadBeanDefinitions(xmlResourcePath);
        return applicationContext;
    }
}
