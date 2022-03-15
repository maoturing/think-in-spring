package org.geekbang.dependency.injection.setter;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 基于自动绑定byName 依赖 Setter方法注入示例
 *
 * @author mao  2021/4/21 23:45
 */
public class AutoWiringByNameDependencySetterInjectionDemo {
    public static void main(String[] args) {
        // 加载配置文件中的bean
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("autowiring-dependcy-setter-injection.xml");

        // 依赖查找并创建bean
        UserHolder userHolder = applicationContext.getBean(UserHolder.class);
        System.out.println(userHolder);
    }
}
