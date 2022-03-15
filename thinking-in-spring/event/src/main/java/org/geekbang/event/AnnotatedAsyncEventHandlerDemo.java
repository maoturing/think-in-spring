package org.geekbang.event;

import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.context.event.SimpleApplicationEventMulticaster;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 基于注解异步发布事件
 *
 * @author mao  2021/5/28 23:25
 */
@EnableAsync
public class AnnotatedAsyncEventHandlerDemo {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        applicationContext.register(AnnotatedAsyncEventHandlerDemo.class);
        applicationContext.refresh();

        // 发布事件
        applicationContext.publishEvent(new MySpringEvent("hello world..."));
        applicationContext.close();
    }

    @EventListener
    @Async
    public void onApplicationEvent(MySpringEvent event) {
        System.out.printf("线程[%s]监听到事件 %s", Thread.currentThread().getName(), event);
    }

    @Bean
    public Executor taskExecutor() {
        // 创建单线程的线程池, 并传入线程工厂, 设置线程名称
        return Executors.newSingleThreadExecutor(new CustomizableThreadFactory("my-spring-event-thread-pool-"));
    }
}
