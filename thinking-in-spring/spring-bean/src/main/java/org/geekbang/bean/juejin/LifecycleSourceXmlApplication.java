package org.geekbang.bean.juejin;

import org.geekbang.bean.juejin.bean.Cat;
import org.geekbang.bean.juejin.bean.Person;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 探究 xml 配置 bean 的生命周期源码
 *
 *
 * @author mao  2021/5/7 22:20
 */
public class LifecycleSourceXmlApplication {

    public static void main(String[] args) throws Exception {
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext();
        ctx.setConfigLocation("bean-lifecycle-juejin.xml");

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