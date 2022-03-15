package org.geekbang.event;

import org.springframework.context.support.GenericApplicationContext;

/**
 * @author mao  2021/5/28 6:27
 */
public class CustomizedSpringEventDemo {
    public static void main(String[] args) {
        GenericApplicationContext applicationContext = new GenericApplicationContext();
        applicationContext.addApplicationListener(new MySpringEventListener());

        applicationContext.refresh();

        applicationContext.publishEvent(new MySpringEvent("hello world..."));
    }
}
