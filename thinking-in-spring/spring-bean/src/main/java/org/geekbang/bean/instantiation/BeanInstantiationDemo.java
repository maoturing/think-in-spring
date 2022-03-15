package org.geekbang.bean.instantiation;

import org.geekbang.bean.definition.component.Student;
import org.geekbang.ioc.overview.lookup.domain.User;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * bean 实例化示例
 * 1. 通过静态工厂方法
 * 2. 通过bean工厂方法
 * 3. 通过实现FactoryBean接口
 *
 * @author mao  2021/4/20 10:04
 */
public class BeanInstantiationDemo {
    public static void main(String[] args) {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("bean-creation-context.xml");
        // xml中使用 factory-method 配置静态工厂方法
        User user = applicationContext.getBean("user-by-static-method", User.class);
        System.out.println("通过静态工厂方法创建bean: " + user);

        // xml中使用 factory-bean 配置 bean工厂方法
        User user2 = applicationContext.getBean("user-by-factory-method", User.class);
        System.out.println("通过bean工厂方法创建bean: " + user2);

        // xml中使用class配置FactoryBean接口的实现类
        User user3 = applicationContext.getBean("user-by-factory-bean", User.class);
        System.out.println("通过实现FactoryBean接口创建bean: " + user3);
        // 注解@Component配置FactoryBean接口的实现类
        AnnotationConfigApplicationContext annotationContext = new AnnotationConfigApplicationContext("org.geekbang.bean.instantiation");
        Student student = annotationContext.getBean("student", Student.class);
        System.out.println("通过@Component + 实现FactoryBean接口创建bean:" + student);

        // 使用bean名称获取的是工厂创建的bean, 如果要获取工厂本身, bean名称前加一个 &
        UserFactoryBean userFactoryBean = applicationContext.getBean("&user-by-factory-bean", UserFactoryBean.class);
        System.out.println(userFactoryBean);
    }
}
