package org.geekbang.environment;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * @Profile 注解示例
 * @author mao  2021/5/30 5:50
 */
public class ProfileDemo {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        applicationContext.register(ProfileDemo.class);

        // 为Environment设置激活的profile为 odd
        ConfigurableEnvironment environment = applicationContext.getEnvironment();
        environment.setDefaultProfiles("odd");

        // 下面3行配置的作用都是一样的, 让生效的profile文件为 even
        // -Dspring.profiles.active=even        spring项目使用这个设置为vm参数
        // --spring.profiles.active=even        springboot项目使用这个设置为程序参数
//         environment.setActiveProfiles("even");

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
    // 与@Profile的作用相同, 若EvenProfileCondition#matches方法返回true才加载该bean
//    @Conditional(EvenProfileCondition.class)
    public Integer even() {
        return 2;
    }
}
