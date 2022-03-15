package org.geekbang.dependency.injection.custom;

import org.geekbang.ioc.overview.lookup.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import javax.inject.Inject;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

import static org.springframework.context.annotation.AnnotationConfigUtils.AUTOWIRED_ANNOTATION_PROCESSOR_BEAN_NAME;

/**
 * 自定义依赖注入注解 @Autowired 使用示例
 *
 * @author mao  2021/4/21 23:45
 */
public class AnnotationDependencyCustomInjectionDemo {
    @Autowired
    private User user;

    @MyAutowired
    private User myAutowiredUser;

    @InjectedUser
    private User myInjectedUser;

    /**
     * 创建一个处理@InjectedUser注解的AutowiredAnnotationBeanPostProcessor,
     * 并且替换掉容器中的bean internalAutowiredAnnotationProcessor
     * static 会在当前类bean初始化前进行注册方法返回的Bean
     *
     * @return
     */
/*    @Bean(name = AUTOWIRED_ANNOTATION_PROCESSOR_BEAN_NAME)
    public static AutowiredAnnotationBeanPostProcessor beanPostProcessor() {
        AutowiredAnnotationBeanPostProcessor postProcessor = new AutowiredAnnotationBeanPostProcessor();
        // 替换AutowiredAnnotationBeanPostProcessor处理的注解类型，使其处理@InjectedUser标记的字段
//        postProcessor.setAutowiredAnnotationType(InjectedUser.class);

        // 上面的方法会使得@Autowired无法被处理, 使用下面的方式使其处理@InjectedUser标记的字段
        Set<Class<? extends Annotation>> autowiredAnnotationTypes =
                new LinkedHashSet<>(Arrays.asList(Autowired.class, Value.class, Inject.class, InjectedUser.class));
        postProcessor.setAutowiredAnnotationTypes(autowiredAnnotationTypes);
        return postProcessor;
    }*/

    /**
     * 上面的方法是替换掉spring的内建bean
     * 必须是static类型, 要在普通bean被读取之前加载
     * @return
     */
    @Bean
    public static AutowiredAnnotationBeanPostProcessor beanPostProcessor() {
        AutowiredAnnotationBeanPostProcessor postProcessor = new AutowiredAnnotationBeanPostProcessor();
        // 替换AutowiredAnnotationBeanPostProcessor处理的注解类型，使其处理@InjectedUser标记的字段
        postProcessor.setAutowiredAnnotationType(InjectedUser.class);

        return postProcessor;
    }

    public static void main(String[] args) {
        // 创建容器, 加载当前类中的bean, 加载xml配置中的两个bean
        // user, superUser, superUser有primary属性, 优先匹配, 且有 address属性
        AnnotationConfigApplicationContext applicationContext = getApplicationContext();
        // 启动 Spring 应用上下文
        applicationContext.refresh();

        AnnotationDependencyCustomInjectionDemo demo = applicationContext.getBean(AnnotationDependencyCustomInjectionDemo.class);
        //期待输出 superUser
        System.out.println("User user: " + demo.user);
        System.out.println("User myAutowiredUser: " + demo.myAutowiredUser);
        System.out.println("User myInjectedUser: " + demo.myInjectedUser);
    }

    private static AnnotationConfigApplicationContext getApplicationContext() {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        applicationContext.register(AnnotationDependencyCustomInjectionDemo.class);
        XmlBeanDefinitionReader beanDefinitionReader = new XmlBeanDefinitionReader(applicationContext);
        String xmlResourcePath = "dependcy-resolve-injection.xml";
        // 加载 XML 资源，解析并且生成 BeanDefinition
        beanDefinitionReader.loadBeanDefinitions(xmlResourcePath);
        return applicationContext;
    }
}
