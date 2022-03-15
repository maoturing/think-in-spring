package org.geekbang.bean.destroy;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;

/**
 * Bean 初始化示例
 * 测试 3 种设置销毁前回调方法的方式
 *
 * @author mao  2021/4/20 15:56
 */
public class BeanDestroyDemo {

    public static void main(String[] args) {
        // 1.创建注解配置的应用上下文
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        applicationContext.register(BeanDestroyDemo.class);

        System.out.println("=========Spring应用上下文开始启动=========");
        // 2. 启动应用上下文
        applicationContext.refresh();
        System.out.println("=========Spring应用上下文已启动=========");

        // 3.依赖查找, 根据类型
        applicationContext.getBean(Teacher.class);
        // 4. 关闭应用上下文
        System.out.println("=========Spring应用上下文开始关闭=========");
        applicationContext.close();
        System.out.println("=========Spring应用上下文已关闭=========");

        // 强制进行 GC
        System.gc();
    }

    // 指定初始化方法, 与xml方式作用一直
    @Bean(destroyMethod = "destroyTeacher", initMethod = "initTeacher")
    public Teacher createTeacher() {
        return new Teacher();
    }
}
