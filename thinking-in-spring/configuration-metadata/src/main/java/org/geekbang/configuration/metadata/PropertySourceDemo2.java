package org.geekbang.configuration.metadata;

import org.geekbang.ioc.overview.lookup.domain.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;

import java.util.HashMap;
import java.util.Map;

/**
 * @PropertySource 引入属性文件，@Value 使用属性文件中的属性，进行注入
 * 使用spring api 手动创建属性源
 * 判断各个属性源中属性的优先级
 *
 * @author mao  2021/5/20 2:59
 */
@Configuration
@PropertySource("classpath:/student.properties")
public class PropertySourceDemo2 {

    @Bean
    public User user(@Value("${user.name}") String name) {
        User user = new User();
        user.setName(name);
        return user;
    }

    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        applicationContext.register(PropertySourceDemo2.class);

        // 创建属性源
        Map<String, Object> map = new HashMap<>();
        map.put("user.name", "firstName");
        MapPropertySource mapPropertySource = new MapPropertySource("first-api-property-source", map);
        // 将该属性源添加到Spring应用上下文环境
        applicationContext.getEnvironment().getPropertySources().addFirst(mapPropertySource);

        applicationContext.refresh();
        User user = applicationContext.getBean(User.class);
        System.out.println(user);

        MutablePropertySources propertySources = applicationContext.getEnvironment().getPropertySources();
        System.out.println("============打印 Spring PropertySource 所有属性源==============");
        for (org.springframework.core.env.PropertySource<?> propertySource : propertySources) {
            System.out.println(propertySource);
        }
    }
}
