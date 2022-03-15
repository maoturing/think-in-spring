package org.geekbang.environment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.Environment;

/**
 * Environment 依赖注入
 *
 * @author mao  2021/6/4 7:43
 */
public class InjectingEnvironmentDemo implements EnvironmentAware {
    private Environment environment;

    @Autowired
    private Environment environment2;

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        applicationContext.register(InjectingEnvironmentDemo.class);

        applicationContext.refresh();

        InjectingEnvironmentDemo bean = applicationContext.getBean(InjectingEnvironmentDemo.class);
        System.out.println(bean.environment);
        System.out.println(bean.environment2);
        System.out.println(bean.environment == bean.environment2);
    }
}
