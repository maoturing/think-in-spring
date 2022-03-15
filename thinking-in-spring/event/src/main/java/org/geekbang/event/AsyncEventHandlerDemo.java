package org.geekbang.event;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.SimpleApplicationEventMulticaster;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.GenericApplicationContext;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 异步发布事件
 * @author mao  2021/5/28 23:25
 */
public class AsyncEventHandlerDemo {

    public static void main(String[] args) {
        GenericApplicationContext applicationContext = new GenericApplicationContext();
        // 注册监听器
        applicationContext.addApplicationListener(new MySpringEventListener());
        applicationContext.refresh();

        // 获取广播器, 将广播器设置为异步模式
        ApplicationEventMulticaster multicaster = applicationContext.getBean(
                AbstractApplicationContext.APPLICATION_EVENT_MULTICASTER_BEAN_NAME,
                ApplicationEventMulticaster.class);

        // 设置为异步广播事件, 设置后发布事件的线程就不再是main线程了
        if (multicaster instanceof SimpleApplicationEventMulticaster) {
            SimpleApplicationEventMulticaster simpleMulticaster = (SimpleApplicationEventMulticaster) multicaster;

            ExecutorService executor = Executors.newSingleThreadExecutor();
            // 切换taskExecutor, 异步广播事件
            simpleMulticaster.setTaskExecutor(executor);

            // 在spring容器关闭时, 停止线程池, 否则程序无法正常退出
            simpleMulticaster.addApplicationListener((ApplicationListener<ContextClosedEvent>) event -> {
                if (!executor.isShutdown()) {
                    executor.shutdown();
                }
            });
        }

        // 发布事件
        applicationContext.publishEvent(new MySpringEvent("hello world..."));
        applicationContext.close();
    }
}
