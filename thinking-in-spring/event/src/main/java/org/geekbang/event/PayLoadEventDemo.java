package org.geekbang.event;

import org.springframework.context.*;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * 发布payLoad事件
 * @author mao  2021/5/28 3:40
 */
public class PayLoadEventDemo implements ApplicationEventPublisherAware {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        applicationContext.register(PayLoadEventDemo.class);
        // 添加监听器
        applicationContext.addApplicationListener(
                (ApplicationListener<PayloadApplicationEvent>) event
                        -> System.out.println("接收到spring事件: " + event.getPayload())
        );

        applicationContext.refresh();
        applicationContext.close();
    }

    // 实现ApplicationEventPublisherAware的方法, 借助ApplicationEventPublisher发布事件
    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        // 发布 Payload 事件
        applicationEventPublisher.publishEvent("hello ...");
    }
}
