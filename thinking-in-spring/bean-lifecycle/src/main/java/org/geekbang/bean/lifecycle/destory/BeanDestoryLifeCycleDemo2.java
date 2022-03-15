package org.geekbang.bean.lifecycle.destory;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;

/**
 * Bean销毁
 *
 * @author mao  2021/5/13 3:12
 */
public class BeanDestoryLifeCycleDemo2 {

    public static void main(String[] args) {
        // 1.创建并启动 ApplicationContext 容器, 使用注解配置
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        applicationContext.register(BeanDestoryLifeCycleDemo2.class);
        // 2. 启动容器
        applicationContext.refresh();
        // 3. 关闭容器, 开始销毁bean, 这里会回调销毁方法
        System.out.println("=======Spring应用上下文开始关闭=======");
        // 这行代码需要注释, 因为对teacher对象产生了引用, 后面GC时无法回收
        // Teacher teacher = applicationContext.getBean(Teacher.class);
        applicationContext.close();

        // 4.  GC回收bean, 调用finalize方法
        System.gc();
    }

    @Bean(destroyMethod = "destroyTeacher")
    public Teacher createTeacher() {
        return new Teacher();
    }
}