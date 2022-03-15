package org.geekbang.event;

import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;

/**
 * 层次性Spring事件传播示例
 *
 * @author mao  2021/5/28 4:44
 */
public class HierarchicalSpringEventPropagateDemo {
    public static void main(String[] args) {
        // 1. 创建 patent Spring 应用上下文
        AnnotationConfigApplicationContext parentContext = new AnnotationConfigApplicationContext();
        parentContext.setId("parent-context");
        // 注册监听器
        parentContext.register(MyListener.class);

        // 2. 创建 current Spring 应用上下文
        AnnotationConfigApplicationContext currentContext = new AnnotationConfigApplicationContext();
        currentContext.setId("current-context");

        // 3. 让 current 继承 parent
        currentContext.setParent(parentContext);
        // 注册监听器
        currentContext.register(MyListener.class);

        // 4. 启动两个应用上下文
        parentContext.refresh();
        currentContext.refresh();

    }

    static class MyListener implements ApplicationListener<ContextRefreshedEvent> {

        @Override
        public void onApplicationEvent(ContextRefreshedEvent event) {
            System.out.printf("监听到 Spring 应用上下文[ID : %s] 的 ContextRefreshedEvent 事件\n",
                    event.getApplicationContext().getId());
        }
    }
}
