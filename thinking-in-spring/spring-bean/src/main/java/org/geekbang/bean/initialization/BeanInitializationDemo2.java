package org.geekbang.bean.initialization;

import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;

/**
 * Bean 初始化示例
 * 测试使用 java api 指定初始化方法
 *
 * @author mao  2021/4/20 15:56
 */
public class BeanInitializationDemo2 {

    public static void main(String[] args) {
        // 1.创建并启动 ApplicationContext 容器
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(BeanInitializationDemo2.class);
        // 2. 创建 BeanDefinition 元信息, 并设置 initMethod
        AbstractBeanDefinition beanDefinition = BeanDefinitionBuilder
                .genericBeanDefinition(Teacher.class)
                .setInitMethodName("initTeacher")
                .getBeanDefinition();
        // 3. 注册 bean 到容器, 设置 bean 名称为Winston
        applicationContext.registerBeanDefinition("Winston", beanDefinition);

        applicationContext.getBean("Winston");
    }
}
