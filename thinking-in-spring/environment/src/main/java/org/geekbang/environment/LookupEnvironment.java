package org.geekbang.environment;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;

import java.util.ArrayList;

/**
 * Environment 依赖查找
 *
 * @author mao  2021/6/4 7:43
 */
public class LookupEnvironment{

    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        applicationContext.register(LookupEnvironment.class);

        applicationContext.refresh();
        // 1. 通过bean名称查找environment
        Environment environment = applicationContext.getBean(ConfigurableApplicationContext.ENVIRONMENT_BEAN_NAME, Environment.class);
        System.out.println(environment);

        // 2. 通过ConfigurableApplicationContext#getEnvironment获取environment
        ConfigurableEnvironment environment1 = applicationContext.getEnvironment();
        System.out.println(environment1);
        System.out.println(environment1 == environment);
    }
}
