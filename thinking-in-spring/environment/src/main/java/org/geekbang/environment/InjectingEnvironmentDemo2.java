package org.geekbang.environment;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.Environment;

/**
 * Environment 依赖注入
 *
 * @author mao  2021/6/4 7:43
 */
public class InjectingEnvironmentDemo2 implements ApplicationContextAware {
    private ApplicationContext applicationContext;

    @Autowired
    private ApplicationContext ApplicationContext2;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        applicationContext.register(InjectingEnvironmentDemo2.class);

        applicationContext.refresh();
        InjectingEnvironmentDemo2 bean = applicationContext.getBean(InjectingEnvironmentDemo2.class);
        System.out.println(bean.applicationContext.getEnvironment());
        System.out.println(bean.ApplicationContext2.getEnvironment());

        System.out.println(bean.applicationContext.getEnvironment() == bean.ApplicationContext2.getEnvironment());
    }
}
