package org.geekbang.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.*;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.event.EventListener;

/**
 * 通过ApplicationEventPublisher发布事件
 * @author mao  2021/5/28 3:40
 */
public class ApplicationEventPublisherDemo2{
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        applicationContext.register(ApplicationEventPublisherDemo2.class);
        applicationContext.refresh();
        ApplicationEventPublisherDemo2 bean = applicationContext.getBean(ApplicationEventPublisherDemo2.class);

        // 获取依赖注入的 ApplicationEventPublisher
        ApplicationEventPublisher applicationEventPublisher = bean.applicationEventPublisher;
        // 发布 payload 事件
        applicationEventPublisher.publishEvent("hi listener...");
    }

    // 注册监听器
    @EventListener
    public void onApplicationEvent(PayloadApplicationEvent event){
        System.out.println("接收到spring事件: " + event.getPayload());
    }
}
