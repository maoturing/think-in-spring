package org.geekbang.bean.lifecycle;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;

/**
 * Bean 初始化示例
 * 初始化时
 *
 * 测试 3 种设置初始化方法的方式
 *
 * @author mao  2021/4/20 15:56
 */
public class BeanInitializationLifeCycleDemo2 {

    public static void main(String[] args) {
        // 1.创建并启动 ApplicationContext 容器, 使用注解配置
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(BeanInitializationLifeCycleDemo2.class);
        // 2.依赖查找, 根据类型
        Teacher teacher = applicationContext.getBean(Teacher.class);
    }

    // 指定初始化方法, 与xml方式作用一直
    @Bean(initMethod = "initTeacher")
    public Teacher createTeacher() {
        return new Teacher();
    }
}
