package org.geekbang.configuration.metadata;

import org.geekbang.ioc.overview.lookup.domain.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;

/**
 * YAML 外部化配置示例
 *
 * @author mao  2021/5/20 2:59
 */

@PropertySource(name = "yamlPropertySource",
        value = "classpath:/student.yml",
        factory = YamlPropertySourceFactory.class)
public class AnnotatedYamlPropertySourceDemo {

    @Bean   // @Value注入的是yaml中的属性
    public User user(@Value("${student.name}") String name) {
        User user = new User();
        user.setName(name);
        return user;
    }

    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        applicationContext.register(AnnotatedYamlPropertySourceDemo.class);

        applicationContext.refresh();
        User user = applicationContext.getBean(User.class);
        System.out.println(user);
    }
}
