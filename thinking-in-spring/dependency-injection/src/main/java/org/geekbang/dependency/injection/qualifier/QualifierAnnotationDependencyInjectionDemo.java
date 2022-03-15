package org.geekbang.dependency.injection.qualifier;

import org.geekbang.dependency.injection.setter.UserHolder;
import org.geekbang.ioc.overview.lookup.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;

import java.util.Collection;

/**
 * @Qualifier 限定注入
 *
 * @author mao  2021/4/21 23:45
 */
public class QualifierAnnotationDependencyInjectionDemo {

    @Autowired
    @Qualifier("user1")
    // 使用Qualifier限制bean的名称, 否则会报UnsatisfiedDependencyException+NoUniqueBeanDefinitionException
    private User user;

    @Autowired
    @Qualifier
    // 注入标记了@Qualifier的bean
    private Collection<User> admins;

    @Autowired
    @UserGroup
    // 注入标记了@UserGroup的bean
    private User manager;

    public static void main(String[] args) {
        // 创建容器
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(QualifierAnnotationDependencyInjectionDemo.class);
        // 获取当前主类在容器中的bean
        QualifierAnnotationDependencyInjectionDemo demo = applicationContext.getBean(QualifierAnnotationDependencyInjectionDemo.class);

        // 期望输出 1
        System.out.println(demo.user);
        // 期望输出 3, 4
        System.out.println(demo.admins);
        // 期望输出 4
        System.out.println(demo.manager);
    }

    @Bean
    public User user1() {
        return createUser(1L);
    }

    @Bean
    public User user2() {
        return createUser(2L);
    }

    @Bean
    @Qualifier
    public User user3() {
        return createUser(3L);
    }

    @Bean
    @UserGroup
    public User user4() {
        return createUser(4L);
    }

    private User createUser(Long id) {
        User user = new User();
        user.setId(id);
        return user;
    }
}
