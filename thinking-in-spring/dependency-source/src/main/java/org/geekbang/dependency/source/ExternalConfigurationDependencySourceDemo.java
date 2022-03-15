package org.geekbang.dependency.source;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * 外部化配置作为依赖来源, 使用@Value引入
 *
 * 不可以作为依赖查找来源, 只能作为依赖注入来源
 *
 * @author mao  2021/4/28 14:35
 */
@Configuration  // 这个是必须的
@PropertySource("default.properties")
public class ExternalConfigurationDependencySourceDemo {

    // 设置默认值为-1
    @Value("${user.id:-1}")
    private Long id;

    // 这里会注入系统属性，即当前操作系统用户的名称
    @Value("${user.name}")
    private String name;

    @Value("${hero.name}")
    private String heroName;

    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        applicationContext.register(ExternalConfigurationDependencySourceDemo.class);

        // 启动上下文
        applicationContext.refresh();
        ExternalConfigurationDependencySourceDemo bean = applicationContext.getBean(ExternalConfigurationDependencySourceDemo.class);
        System.out.println("user.id: " + bean.id);
        System.out.println("user.name: " + bean.name);
        System.out.println("hero.name: " + bean.heroName);
    }
}
