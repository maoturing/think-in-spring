package org.geekbang.bean.definition;

import org.geekbang.ioc.overview.lookup.domain.User;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author mao  2021/4/20 0:24
 */
public class BeanAliasDemo {

    public static void main(String[] args) {
        // 1. 在 xml 文件中配置 bean
        // 2. 启动spring 应用上下文
        BeanFactory beanFactory = new ClassPathXmlApplicationContext("bean-definition-context.xml");
        User user = beanFactory.getBean("user", User.class);
        User maoUser = beanFactory.getBean("mao-user", User.class);

        System.out.println("mao-user: " + maoUser);
        System.out.println("user == maoUser: " + (user == maoUser));
    }
}
