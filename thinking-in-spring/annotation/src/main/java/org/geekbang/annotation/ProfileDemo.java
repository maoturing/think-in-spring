package org.geekbang.annotation;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * @author mao  2021/5/30 5:50
 */
public class ProfileDemo {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        applicationContext.register(ProfileDemo.class);

        // 为Environment设置激活的profile为 odd
        ConfigurableEnvironment environment = applicationContext.getEnvironment();
        environment.setActiveProfiles("odd");
//        environment.setActiveProfiles("even");

        applicationContext.refresh();

        Integer number1 = applicationContext.getBean("number", Integer.class);
        System.out.println(number1);
    }

    @Bean(name = "number")
    @Profile("odd")     // 奇数
    public Integer odd() {
        return 1;
    }

    @Bean(name = "number")
    @Profile("even")    // 偶数
    public Integer even() {
        return 2;
    }
}
