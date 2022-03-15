package org.geekbang.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.*;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.GenericApplicationContext;

/**
 * 通过ApplicationEventPublisher发布事件
 * @author mao  2021/5/28 3:40
 */
public class ApplicationEventPublisherDemo implements ApplicationEventPublisherAware {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        applicationContext.register(ApplicationEventPublisherDemo.class);
        // 添加监听器
        applicationContext.addApplicationListener(new ApplicationListener<ApplicationEvent>() {
            @Override
            public void onApplicationEvent(ApplicationEvent event) {
                System.out.println("接收到spring事件: " + event);
            }
        });

        applicationContext.refresh();
        applicationContext.close();
    }

    /**
     * 重写ApplicationEventPublisherAware的方法
     * 使用 ApplicationEventPublisher 发布事件
     */
    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        // 发布事件
        applicationEventPublisher.publishEvent(new ApplicationEvent("hello world") {
        });

        // 发布事件, 重载方法, 发布的是 Payload 事件
        applicationEventPublisher.publishEvent("hello listener...");
    }

}
