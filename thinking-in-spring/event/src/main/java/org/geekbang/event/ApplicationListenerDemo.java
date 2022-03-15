package org.geekbang.event;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.GenericApplicationContext;

/**
 * ApplicationListener 示例，在 Spring 应用上下文启动和关闭时，会发送事件
 * @author mao  2021/5/28 3:40
 */
public class ApplicationListenerDemo {
    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = new GenericApplicationContext();
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
}
