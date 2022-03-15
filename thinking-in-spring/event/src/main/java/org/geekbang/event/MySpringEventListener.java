package org.geekbang.event;

import org.springframework.context.ApplicationListener;

/**
 * @author mao  2021/5/28 6:27
 */
public class MySpringEventListener implements ApplicationListener<MySpringEvent> {
    @Override
    public void onApplicationEvent(MySpringEvent event) {
        System.out.printf("线程[%s]监听到事件 %s", Thread.currentThread().getName(), event);
    }
}
