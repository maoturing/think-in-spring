package org.geekbang.dependency.injection.aware;


import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Aware 接口回调注入
 *
 * @author mao  2021/4/21 23:45
 */
public class AwareInterfaceDependencyInjectionDemo {

    public static void main(String[] args) {
        // 创建容器, 扫描当前包所有bean
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext("org.geekbang.dependency.injection.aware");

        // 获取bean
        UserService userService = applicationContext.getBean(UserService.class);

        // 判断aware接口回调注入的bean, 与容器中的bean是否同一个
        System.out.println(userService.getBeanFactory() == applicationContext.getBeanFactory());
        System.out.println(userService.getApplicationContext() == applicationContext);
    }
}
