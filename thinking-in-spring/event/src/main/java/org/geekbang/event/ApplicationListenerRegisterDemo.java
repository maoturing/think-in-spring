package org.geekbang.event;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.stereotype.Component;

/**
 * ApplicationListener 注册
 * - 将 ApplicationListener 作为 Spring bean 注册
 * - 通过 ConfigurableApplicationContext#addApplicationListener 注册
 *
 *
 * @author mao  2021/5/28 3:40
 */
public class ApplicationListenerRegisterDemo {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        applicationContext.register(ApplicationListenerRegisterDemo.class);

        // 方法一: 通过 ConfigurableApplicationContext#addApplicationListener 注册监听器
        applicationContext.addApplicationListener(new ApplicationListener<ApplicationEvent>() {
            @Override
            public void onApplicationEvent(ApplicationEvent event) {
                System.out.println("ApplicationListener - 接收到spring事件: " + event);
            }
        });

        // 通过注册 bean 的方式注册监听器
        applicationContext.register(MyApplicationListener.class);

        applicationContext.refresh();
        applicationContext.close();
    }

    static class MyApplicationListener implements ApplicationListener<ApplicationEvent> {
        @Override
        public void onApplicationEvent(ApplicationEvent event) {
            System.out.println("MyApplicationListener - 接收到spring事件: " + event);
        }
    }
}
