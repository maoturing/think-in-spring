package org.geekbang.dependency.injection.lazy;

import org.geekbang.dependency.injection.qualifier.UserGroup;
import org.geekbang.ioc.overview.lookup.domain.User;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;

import java.util.Collection;

/**
 * ObjectProvider 延迟依赖注入
 *
 * 注意是延迟依赖注入, 不是延迟加载@Lazy
 *
 * @author mao  2021/4/21 23:45
 */
public class LazyAnnotationDependencyInjectionDemo {
    // 延迟注入
    @Autowired
    private ObjectProvider<User> objectProvider;

    public static void main(String[] args) {
        // 创建容器并启动, 加载bean, 进行依赖注入,
        // 虽然 user bean 不存在, 但是ObjectProvider是延迟注入,所以不会报错
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(LazyAnnotationDependencyInjectionDemo.class);
        System.out.println("依赖注入成功，延迟依赖未进行注入");

        // 上面的步骤进行了依赖注入,
        LazyAnnotationDependencyInjectionDemo demo = applicationContext.getBean(LazyAnnotationDependencyInjectionDemo.class);
        System.out.println("查找bean，延迟注入的bean需要注入了...");
        User user = demo.objectProvider.getObject();
        System.out.println(user);
    }

    // @Bean
    // 注释掉@Bean, 虽然容器中不存在bean, 因为是延迟注入, 所以容器启动时不会报错,
    // 只有在objectProvider.getObject() 时才会进行依赖注入, 会报错
    public User user1() {
        User user = new User();
        user.setId(1L);
        return user;
    }
}
