package org.geekbang.environment;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

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
        System.out.println(demo.stuName);
    }
}
