package org.geekbang.bean.initialization;

import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;

/**
 * Bean 延迟初始化示例
 *
 * @author mao  2021/4/20 15:56
 */
public class BeanLazyInitializationDemo {

    public static void main(String[] args) {
        // 1.创建并启动 ApplicationContext 容器, 使用注解配置
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(BeanLazyInitializationDemo.class);

        // 非延迟加载会在应用上下文启动时初始化bean
        System.out.println("========Spring应用上下文已启动=======");
        // 延迟加载会在获取bean时初始化bean, 但已经注册BeanDefinition到容器了
        applicationContext.getBean(Teacher.class);
    }
//    @Lazy
    @Bean
    public Teacher createTeacher() {
        return new Teacher();
    }
}
