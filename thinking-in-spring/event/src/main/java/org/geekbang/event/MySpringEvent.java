package org.geekbang.event;

import org.springframework.context.ApplicationEvent;

/**
 * @author mao  2021/5/28 6:26
 */
public class MySpringEvent extends ApplicationEvent {

    public MySpringEvent(Object source) {
        super(source);
    }
}
