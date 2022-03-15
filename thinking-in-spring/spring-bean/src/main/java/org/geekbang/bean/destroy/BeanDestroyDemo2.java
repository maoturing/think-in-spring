package org.geekbang.bean.destroy;

import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Bean 初始化示例
 * 测试使用 java api 指定销毁前回调方法
 *
 * @author mao  2021/4/20 15:56
 */
public class BeanDestroyDemo2 {

    public static void main(String[] args) {
        // 1.创建并启动 ApplicationContext 容器
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(BeanDestroyDemo2.class);
        // 2. 创建 BeanDefinition 元信息, 并设置 DestroyMethod
        AbstractBeanDefinition beanDefinition = BeanDefinitionBuilder
                .genericBeanDefinition(Teacher.class)
                .setDestroyMethodName("destroyTeacher")
                .getBeanDefinition();
        // 3. 注册 bean 到容器, 设置 bean 名称为Winston
        applicationContext.registerBeanDefinition("Winston", beanDefinition);
        applicationContext.getBean("Winston");

        // 4. 关闭容器，会回调DestroyMethod
        applicationContext.close();
    }
}
