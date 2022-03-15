package org.geekbang.environment;

import org.geekbang.ioc.overview.lookup.domain.User;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author mao  2021/6/4 3:17
 */
public class PropertyPlaceholderConfigurerDemo {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("placeholders-resolver.xml");

        User user = applicationContext.getBean("user", User.class);
        System.out.println(user);
    }
}
