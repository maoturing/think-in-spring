package org.geekbang.bean.definition;

import org.geekbang.ioc.overview.lookup.domain.User;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.GenericBeanDefinition;

/**
 * BeanDefinition 构建示例
 * 1. 通过BeanDefinitionBuilder构建
 * 2. 通过AbstractBeanDefinition派生类构建
 *
 * @author mao  2021/4/19 22:34
 */
public class BeanDefinitionCreationDemo {

    public static void main(String[] args) {
        // 1. 通过 BeanDefinitionBuilder 构建
        AbstractBeanDefinition beanDefinition = BeanDefinitionBuilder.genericBeanDefinition(User.class)
                .addPropertyValue("id", 1)
                .addPropertyValue("name", "小马")
                .setScope("singleton").getBeanDefinition();

        System.out.println("BeanDefinitionBuilder 构造: " + beanDefinition);

        // 2. 通过 AbstractBeanDefinition派生类构建bean
        GenericBeanDefinition genericBeanDefinition = new GenericBeanDefinition();
        // 设置 bean 类型
        genericBeanDefinition.setBeanClass(User.class);
        // 设置属性
        MutablePropertyValues propertyValues = new MutablePropertyValues();
        propertyValues.addPropertyValue("id", 1);
        propertyValues.addPropertyValue("name", "安娜");
        genericBeanDefinition.setPropertyValues(propertyValues);

        System.out.println("genericBeanDefinition 构造: " + genericBeanDefinition);
    }
}
