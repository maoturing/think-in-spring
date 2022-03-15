package org.geekbang.dependency.source;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.io.ResourceLoader;

/**
 * Spring 内建的 ResolvableDependency 作为依赖来源
 * <p>
 * 这些bean都是spring框架在启动时注册的
 *
 *  不可以作为依赖查找来源, 只能作为依赖注入来源
 *
 * @author mao  2021/4/27 22:58
 */
public class ResolvableDependencySourceDemo {
    // 注入在 postProcessProperties 方法执行, 早于 Setter 注入, 也早于 @PostConstruct
    // 不应该是初始化时进行依赖注入吗, 为什么会早于 @PostConstruct
    @Autowired
    private BeanFactory beanFactory;

    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    private ApplicationContext applicationContext;

//    @PostConstruct
//    public void init() {
//        System.out.println(applicationContext==applicationEventPublisher);
//    }


    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        applicationContext.register(ResolvableDependencySourceDemo.class);
        applicationContext.refresh();

        ResolvableDependencySourceDemo bean = applicationContext.getBean(ResolvableDependencySourceDemo.class);
        System.out.println(bean.beanFactory);
        System.out.println(bean.resourceLoader);
        System.out.println(bean.applicationEventPublisher);
        System.out.println(bean.applicationContext);

        System.out.print("注入的applicationContext与当前应用上下文是同一个对象: ");
        System.out.println(bean.applicationContext == applicationContext);
        System.out.print("注入的BeanFactory与当前应用上下文中的BeanFactory是同一个对象: ");
        System.out.println((bean.beanFactory == applicationContext.getBeanFactory()));

        // 检验容器中是否有 BeanFactory 类型的bean, 肯定没有,
        // 虽然可以进行依赖注入, 但是BeanFactory不是由 Spring 容器管理的
        try {
            applicationContext.getBean(BeanFactory.class);
        } catch (NoSuchBeanDefinitionException e) {
            System.out.println("容器中没有 BeanFactory 类型的 bean, 说明 ResolvableDependency 对象无法进行依赖查找");
        }
    }
}
