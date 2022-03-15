package org.geekbang.dependency.source;

import org.geekbang.ioc.overview.lookup.domain.User;
import org.springframework.beans.factory.config.SingletonBeanRegistry;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * 注册外部单例对象到容器
 *
 *
 * @author mao  2021/4/20 23:28
 */
public class ExternalSingletonDependencySourceDemo {
    public static void main(String[] args) {
        // 1. 创建应用上下文
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        // 2. 创建一个外部 user 对象
        User user = new User();

        // 3. 注册外部对象到容器
        SingletonBeanRegistry beanFactory = applicationContext.getBeanFactory();
        beanFactory.registerSingleton("mercy", user);
        // 4. 启动应用上下文
        applicationContext.refresh();

        // 5. 依赖查找, 会优先从容器的 singletonObjects 属性中查找 bean
        User mercy = applicationContext.getBean("mercy", User.class);
        // 6. 判断容器中的 bean 是否是那个外部对象
        System.out.println("mercy == user: " + (mercy == user));

        // 7. 关闭应用上下文
        applicationContext.close();
    }
}
