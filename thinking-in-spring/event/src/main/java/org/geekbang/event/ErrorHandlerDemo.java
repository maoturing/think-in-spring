package org.geekbang.event;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.PayloadApplicationEvent;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.SimpleApplicationEventMulticaster;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.GenericApplicationContext;

/**
 * 错误处理器
 * @author mao  2021/5/28 23:25
 */
public class ErrorHandlerDemo {

    public static void main(String[] args) {
        GenericApplicationContext applicationContext = new GenericApplicationContext();
        applicationContext.refresh();

        // 注册监听器
        applicationContext.addApplicationListener((ApplicationListener<PayloadApplicationEvent>) event -> {
            System.out.println(event.getPayload());
            throw new RuntimeException("自定义异常...");
        });
        ApplicationEventMulticaster multicaster = applicationContext.getBean(
                AbstractApplicationContext.APPLICATION_EVENT_MULTICASTER_BEAN_NAME,
                ApplicationEventMulticaster.class);
        SimpleApplicationEventMulticaster simpleMulticaster = (SimpleApplicationEventMulticaster) multicaster;

        // 为广播器设置错误处理器
        simpleMulticaster.setErrorHandler(e -> {
            System.err.println(e.getMessage());
        });
        // 发布事件
        applicationContext.publishEvent("hello world...");

        System.out.println("applicationContext 正常运行....");
        applicationContext.close();
    }
}
