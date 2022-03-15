package org.geekbang.bean.lifecycle;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;

/**
 * Bean 初始化示例
 * 初始化完成,会回调SmartInitializingSingleton#afterSingletonsInstantiated方法
 *
 *
 * @author mao  2021/4/20 15:56
 */
public class BeanInitializationLifeCycleDemo4 {

    public static void main(String[] args) {
        // 1.创建并启动 ApplicationContext 容器, 使用注解配置
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        applicationContext.register(BeanInitializationLifeCycleDemo4.class);
        // 容器启动, 对bean实例化初始化完成后, 回调afterSingletonsInstantiated方法
        applicationContext.refresh();

        Teacher2 teacher = applicationContext.getBean(Teacher2.class);
        System.out.println(teacher);
    }

    @Bean
    public Teacher2 createTeacher() {
        return new Teacher2();
    }
}
