package org.geekbang.event;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @author mao  2021/5/28 3:48
 */
@EnableAsync        // 激活注解 @Async
public class EventListenerDemo {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        applicationContext.register(EventListenerDemo.class);

        // 方法一: 实现ApplicationListener, 创建监听器
        applicationContext.addApplicationListener(new ApplicationListener<ApplicationEvent>() {
            @Override
            public void onApplicationEvent(ApplicationEvent event) {
                println("ApplicationListener - 接收到spring事件: " + event);
            }
        });

        applicationContext.refresh();
        applicationContext.close();
    }

    // 方法二: 注解标记响应方法, 创建监听器
    @EventListener
    @Order(2)
    public void remind(ApplicationEvent event) {
        println("@EventListener - 接收到spring事件: " + event);
    }

    @EventListener
    @Order(3)
    public void remind(ContextClosedEvent event) {
        println("@EventListener - 接收到spring [close]事件: " + event);
    }

    @EventListener
    @Async
    public void remind(ContextRefreshedEvent event) {
        println("@EventListener - 接收到spring [refresh-async]事件: " + event);
    }

    public static void println(Object printable) {
        System.out.printf("[线程: %s]: %s\n", Thread.currentThread().getName(), printable);
    }
}
