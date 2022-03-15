package org.geekbang.bean.juejin;

import org.geekbang.bean.juejin.bean.Cat;
import org.geekbang.bean.juejin.bean.Person;
import org.geekbang.bean.juejin.config.LifecycleDestructionPostProcessor;
import org.geekbang.bean.juejin.config.LifecycleNameReadPostProcessor;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author mao  2021/5/7 22:20
 */
public class LifecycleSourceAnnotationApplication {

    public static void main(String[] args) throws Exception {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
        ctx.register(LifecycleSourceConfiguration.class);
        ctx.register(LifecycleNameReadPostProcessor.class);
        ctx.register(LifecycleDestructionPostProcessor.class);

        System.out.println("================准备刷新IOC容器==================");

        ctx.refresh();

        System.out.println("================IOC容器刷新完毕==================");

        ctx.start();

        System.out.println("================IOC容器启动完成==================");

        Person person = ctx.getBean(Person.class);
        System.out.println(person);
        Cat cat = ctx.getBean(Cat.class);
        System.out.println(cat);

        System.out.println("================准备停止IOC容器==================");

        ctx.stop();

        System.out.println("================IOC容器停止成功==================");

        ctx.close();
    }
}