package org.geekbang.dependency.injection.lazy;

import org.geekbang.ioc.overview.lookup.domain.User;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;

/**
 * ObjectProvider 延迟依赖注入
 *
 * @author mao  2021/4/21 23:45
 */
public class RealTimeAnnotationDependencyInjectionDemo {
    @Autowired
    private User user;

    public static void main(String[] args) {
        // 创建容器并启动, 加载bean, 进行依赖注入,
        // 由于bean不存在, 会报异常
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(RealTimeAnnotationDependencyInjectionDemo.class);

    }

//    @Bean
    public User user1() {
        User user = new User();
        user.setId(1L);
        return user;
    }
}
