package org.geekbang.configuration.metadata;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.CommandLinePropertySource;
import org.springframework.core.env.SimpleCommandLinePropertySource;

/**
 * @PropertySource 引入属性文件，@Value 使用属性文件中的属性，进行注入
 * @author mao  2021/5/20 2:59
 */
@Configuration  // 必须的
@PropertySource("classpath:/student.properties")
public class PropertySourceDemo {

    @Value("${stu.name}")
    private String stuName;

    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        applicationContext.register(PropertySourceDemo.class);

        applicationContext.refresh();
        PropertySourceDemo demo = applicationContext.getBean(PropertySourceDemo.class);
        String[] command = { "--o1=v1", "--o2", "a", "b", "c" };
        CommandLinePropertySource propertySource = new SimpleCommandLinePropertySource("command", command);
        applicationContext.getEnvironment().getPropertySources().addLast(propertySource);
        System.out.println(demo.stuName);
    }
}
