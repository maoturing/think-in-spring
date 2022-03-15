package org.geekbang.bean.juejin.config;

import org.geekbang.bean.juejin.bean.Person;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 * @author mao  2021/5/7 22:15
 */
public class LifecycleNameReadPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof Person) {
            Person person = (Person) bean;
            System.out.println("LifecycleNameReadPostProcessor ------> " + person.getName());
        }
        return bean;
    }
}