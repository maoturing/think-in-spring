package org.geekbang.bean.juejin.config;

import org.geekbang.bean.juejin.bean.Cat;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.DestructionAwareBeanPostProcessor;

/**
 * @author mao  2021/5/7 22:16
 */
public class LifecycleDestructionPostProcessor implements DestructionAwareBeanPostProcessor {

    @Override
    public void postProcessBeforeDestruction(Object bean, String beanName) throws BeansException {
        if (bean instanceof Cat) {
            Cat cat = (Cat) bean;
            System.out.println(cat.getName() + "被放走了 ......");
        }
    }
}