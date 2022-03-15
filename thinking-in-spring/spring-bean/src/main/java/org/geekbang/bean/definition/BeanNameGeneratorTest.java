package org.geekbang.bean.definition;

import org.geekbang.ioc.overview.lookup.domain.User;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 测试 bean 名称自动生成
 * @author mao  2021/4/19 23:31
 */
public class BeanNameGeneratorTest {
    public static void main(String[] args) {
        testDefaultgenerate();

        testAnnotationGenerate();
    }

    /**
     * 测试@Component标记的bean的名称
     */
    private static void testAnnotationGenerate() {
        // 创建并启动 applicationContext, 扫描指定包下 @Component
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext("org.geekbang.bean.definition");
        String[] beanDefinitionNames = applicationContext.getBeanDefinitionNames();
        System.out.println("自动生成的 bean 名称: "+beanDefinitionNames[5]);
    }

    /**
     * 测试 bean 自动生成的名称, xml 文件中未对 bean 设置名称
     */
    private static void testDefaultgenerate() {
        // 1. 在 xml 文件中配置 bean
        // 2. 启动spring 应用上下文
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("bean-name-generator.xml");
        String[] beanDefinitionNames = applicationContext.getBeanDefinitionNames();
        // 打印自动生成的 bean 名称
        System.out.println("自动生成的 bean 名称: "+beanDefinitionNames[0]);
    }

    @Bean  // 默认bean名称为方法名
    public User getUser() {
        return new User();
    }

}
