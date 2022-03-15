package org.geekbang.environment;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * 使用自定义条件EvenProfileCondition, 判断是否加载bean
 * @author mao  2021/5/30 5:50
 */
public class EvenProfileConditionDemo {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        applicationContext.register(EvenProfileConditionDemo.class);

        ConfigurableEnvironment environment = applicationContext.getEnvironment();
        environment.setActiveProfiles("even");

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
//    @Profile("even")    // 偶数
    // 与@Profile的作用相同, 若EvenProfileCondition#matches方法返回true才加载该bean
    @Conditional(EvenProfileCondition.class)
    public Integer even() {
        return 2;
    }
}
