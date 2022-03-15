package org.geekbang.dependency.injection.constructor;

import org.geekbang.dependency.injection.setter.UserHolder;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 基于自动绑定 依赖构造方法注入示例
 *
 * @author mao  2021/4/21 23:45
 */
public class AutoWiringByNameDependencyConstructorInjectionDemo {
    public static void main(String[] args) {
        // 加载配置文件中的bean
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("autowiring-dependcy-constructor-injection.xml");

        // 依赖查找并创建bean
        UserHolder userHolder = applicationContext.getBean(UserHolder.class);
        System.out.println(userHolder);
    }
}
