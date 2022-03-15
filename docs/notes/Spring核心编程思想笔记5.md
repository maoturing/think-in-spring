# 12. 国际化 i18n

# 13. 校验 Validation

# 14. 数据绑定 DataBinding

# 15. 类型转换 Type Conversion

# 16. 泛型处理 Generic Resolution



# 17. 事件 Event

## 17.1 Java 事件 - 监听器模型

事件 - 监听器模型 是观察者模式的扩展

- 被观察的对象(消息发送者) - Observable
- 观察者 - Observer
- 事件对象 - EventObject
- 事件监听器 - EventListener





## 17.2 面向接口的事件 - 监听器设计模式



## 17.3 面向注解的事件 - 监听器设计模式



## 17.4 Spring 标准事件 ApplicationEvent



## 17.5 基于接口的 Spring 事件监听器

Java 标准事件监听器 EventListener 扩展

- 实现接口 - ApplicationListener
- 设计特点 - 单一类型事件处理
- 处理方法 - onApplicationEvent(ApplicationEvent)
- 事件类型 - ApplicationEvent



ApplicationListener 接口的源码如下所示

```java
@FunctionalInterface
public interface ApplicationListener<E extends ApplicationEvent> extends EventListener {

	void onApplicationEvent(E event);
}
```



创建 ApplicationListener 监听器需要以下 3 步: 

1. 实现 ApplicationListener 接口，其中的泛型表示监听的事件类型
2. 重写 `onApplicationEvent` 方法
3. 将监听器添加到 applicationContext 中

这样在 Spring 应用上下文启动和关闭时，会发送事件，回调该方法。

```java
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
```

输出结果，接收到两个事件，应用上下文启动事件 ContextRefreshedEvent，应用上下文关闭事件 ContextClosedEvent

```
接收到spring事件: org.springframework.context.event.ContextRefreshedEvent[source=org.springframework.context.support.GenericApplicationContext@300ffa5d, started on Fri May 28 03:41:45 CST 2021]
接收到spring事件: org.springframework.context.event.ContextClosedEvent[source=org.springframework.context.support.GenericApplicationContext@300ffa5d, started on Fri May 28 03:41:45 CST 2021]
```





## 17.6 基于注解的 Spring 事件监听器

- 监听器注解 - @EventListener
- 注解目标 - 方法
- 配合 @Async + @EnableAsync 可以实现异步监听
- 配合 @Order 可以控制顺序



@EventListener 只需要标记方法，就可以起到实现 ApplicationListener 接口重写方法一样的作用，在触发事件后，会回调该方法

```java
public static void main(String[] args) {
    AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
    applicationContext.register(EventListenerDemo.class);

    // 方法一: 实现ApplicationListener, 创建监听器
    applicationContext.addApplicationListener(new ApplicationListener<ApplicationEvent>() {
        @Override
        public void onApplicationEvent(ApplicationEvent event) {
            System.out.println("ApplicationListener - 接收到spring事件: " + event);
        }
    });

    applicationContext.refresh();
    applicationContext.close();
}

// 方法二: 注解标记响应方法, 创建监听器
@EventListener
public void remind(ApplicationEvent event) {
    System.out.println("@EventListener - 接收到spring事件: " + event);
}
```

输出结果

```
@EventListener - 接收到spring事件: org.springframework.context.event.ContextRefreshedEvent[source=org.springframework.context.annotation.AnnotationConfigApplicationContext@2e817b38, started on Fri May 28 03:53:14 CST 2021]
ApplicationListener - 接收到spring事件: org.springframework.context.event.ContextRefreshedEvent[source=org.springframework.context.annotation.AnnotationConfigApplicationContext@2e817b38, started on Fri May 28 03:53:14 CST 2021]
@EventListener - 接收到spring事件: org.springframework.context.event.ContextClosedEvent[source=org.springframework.context.annotation.AnnotationConfigApplicationContext@2e817b38, started on Fri May 28 03:53:14 CST 2021]
ApplicationListener - 接收到spring事件: org.springframework.context.event.ContextClosedEvent[source=org.springframework.context.annotation.AnnotationConfigApplicationContext@2e817b38, started on Fri May 28 03:53:14 CST 2021]
```



@EventListener 还可以监听指定事件，并且配合 @Async 注解，可以实现异步监听。

```java
@EnableAsync        // 激活注解 @Async
public class EventListenerDemo {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        applicationContext.register(EventListenerDemo.class);

        // 方法一: 实现ApplicationListener, 创建监听器
        applicationContext.addApplicationListener(new ApplicationListener<ApplicationEvent>() {
            @Override
            public void onApplicationEvent(ApplicationEvent event) {
                println("ApplicationListener - 接收到spring事件: " + event);
            }
        });

        applicationContext.refresh();
        applicationContext.close();
    }

    // 方法二: 注解标记响应方法, 创建监听器
    @EventListener
    public void remind(ApplicationEvent event) {
        println("@EventListener - 接收到spring事件: " + event);
    }

    // 只监听spring关闭 close 事件
    @EventListener
    public void remind(ContextClosedEvent event) {
        println("@EventListener - 接收到spring [close]事件: " + event);
    }

    @EventListener
    @Async		// 异步
    public void remind(ContextRefreshedEvent event) {
        println("@EventListener - 接收到spring [refresh-async]事件: " + event);
    }

    public static void println(Object printable) {
        System.out.printf("[线程: %s]: %s\n", Thread.currentThread().getName(), printable);
    }
}
```

输出结果，异步的使用 Spring 创建的线程进行输出，标记的只监听 close 事件的方法，也接收到了事件。

```
[线程: SimpleAsyncTaskExecutor-1]: @EventListener - 接收到spring [refresh-async]事件: org.springframework.context.event.ContextRefreshedEvent[source=org.springframework.context.annotation.AnnotationConfigApplicationContext@2e817b38, started on Fri May 28 04:03:01 CST 2021]
[线程: main]: @EventListener - 接收到spring事件: org.springframework.context.event.ContextRefreshedEvent[source=org.springframework.context.annotation.AnnotationConfigApplicationContext@2e817b38, started on Fri May 28 04:03:01 CST 2021]
[线程: main]: ApplicationListener - 接收到spring事件: org.springframework.context.event.ContextRefreshedEvent[source=org.springframework.context.annotation.AnnotationConfigApplicationContext@2e817b38, started on Fri May 28 04:03:01 CST 2021]
[线程: main]: @EventListener - 接收到spring [close]事件: org.springframework.context.event.ContextClosedEvent[source=org.springframework.context.annotation.AnnotationConfigApplicationContext@2e817b38, started on Fri May 28 04:03:01 CST 2021]
[线程: main]: @EventListener - 接收到spring事件: org.springframework.context.event.ContextClosedEvent[source=org.springframework.context.annotation.AnnotationConfigApplicationContext@2e817b38, started on Fri May 28 04:03:01 CST 2021]
[线程: main]: ApplicationListener - 接收到spring事件: org.springframework.context.event.ContextClosedEvent[source=org.springframework.context.annotation.AnnotationConfigApplicationContext@2e817b38, started on Fri May 28 04:03:01 CST 2021]
```









## 17.7 注册 Spring ApplicationListener

- 将 ApplicationListener 作为 Spring bean 注册
- 通过 ConfigurableApplicationContext#addApplicationListener 注册
- @EventListener 会自动将监听器注册到容器



1. ApplicationListener 监听器注册到容器示例：

```java
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
```

输出结果，可以看到，两种方式注册的事件监听器 ApplicationListener 都生效了

```
ApplicationListener - 接收到spring事件: org.springframework.context.event.ContextRefreshedEvent[source=org.springframework.context.annotation.AnnotationConfigApplicationContext@2e817b38, started on Fri May 28 04:19:09 CST 2021]
MyApplicationListener - 接收到spring事件: org.springframework.context.event.ContextRefreshedEvent[source=org.springframework.context.annotation.AnnotationConfigApplicationContext@2e817b38, started on Fri May 28 04:19:09 CST 2021]
ApplicationListener - 接收到spring事件: org.springframework.context.event.ContextClosedEvent[source=org.springframework.context.annotation.AnnotationConfigApplicationContext@2e817b38, started on Fri May 28 04:19:09 CST 2021]
MyApplicationListener - 接收到spring事件: org.springframework.context.event.ContextClosedEvent[source=org.springframework.context.annotation.AnnotationConfigApplicationContext@2e817b38, started on Fri May 28 04:19:09 CST 2021]
```

2. 监听器注册源码分析，向容器注册监听器，调用的 `addApplicationListener()` 源码如下所示，会将注册的监听器添加到  applicationEventMulticaster。发布事件时，会获取所有监听器，然后通知所有关注了该事件类型的监听器。@EventListener 标记的监听器也会注册到容器，只不过监听器是由 Spring 创建的对象并注册到容器的，最终也是调用下面的方法注册。

```java
// 代码块2
AbstractApplicationContext.java

    // 应用上下文中保存了一个广播器
    private ApplicationEventMulticaster applicationEventMulticaster;
	
    // 将注册的监听器添加到applicationEventMulticaster
	@Override
	public void addApplicationListener(ApplicationListener<?> listener) {
		if (this.applicationEventMulticaster != null) {
            // 向广播器中添加监听器
			this.applicationEventMulticaster.addApplicationListener(listener);
		}
		this.applicationListeners.add(listener);
	}
```





## 17.8 Spring 事件发布器

- 通过 ApplicationEventPublisher 发布 Spring 事件
  - 依赖注入
- 通过 ApplicationEventMulticaster 发布 Spring 事件
  - 依赖注入
  - 依赖查找

ApplicationEventPublisher 源码如下所示

```java
@FunctionalInterface
public interface ApplicationEventPublisher {

	default void publishEvent(ApplicationEvent event) {
		publishEvent((Object) event);
	}

	void publishEvent(Object event);
}
```



ApplicationEventMulticaster 源码如下所示

```java
public interface ApplicationEventMulticaster {

	void addApplicationListener(ApplicationListener<?> listener);

	void addApplicationListenerBean(String listenerBeanName);

	void removeApplicationListener(ApplicationListener<?> listener);

	void removeApplicationListenerBean(String listenerBeanName);

	void removeAllListeners();

	void multicastEvent(ApplicationEvent event);

	void multicastEvent(ApplicationEvent event, @Nullable ResolvableType eventType);

}
```



1. 通过实现 ApplicationEventPublisherAware 接口，获取 ApplicationEventPublisher 并发布 Spring 事件
   1. 实现 ApplicationEventPublisherAware 接口
   2. 注册监听器
   3. 创建事件 ApplicationEvent，通过`applicationEventPublisher.publishEvent()`发布事件

```java
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
```

输出结果，可以看到监听到了我们发布的 Spring 事件 hello world

```
接收到spring事件: org.geekbang.event.ApplicationEventPublisherDemo$2[source=hello world]
接收到spring事件: org.springframework.context.PayloadApplicationEvent[source=org.springframework.context.annotation.AnnotationConfigApplicationContext@1a6c5a9e, started on Fri May 28 04:41:25 CST 2021]
接收到spring事件: org.springframework.context.event.ContextRefreshedEvent[source=org.springframework.context.annotation.AnnotationConfigApplicationContext@1a6c5a9e, started on Fri May 28 04:41:25 CST 2021]
接收到spring事件: org.springframework.context.event.ContextClosedEvent[source=org.springframework.context.annotation.AnnotationConfigApplicationContext@1a6c5a9e, started on Fri May 28 04:41:25 CST 2021]
```

2. 通过实现 @Autowired 依赖注入 ApplicationEventPublisher，然后发布事件
   1. 使用 @Autowired 依赖注入 ApplicationEventPublisher
   2. 使用 @Listener 注册监听器，这一步可替换为 `applicationContext.addApplicationListener()`注册监听器
   3. 通过`applicationEventPublisher.publishEvent()`发布 Payload 事件

```java
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
```

这种方式与上一步唯一的区别就是获取 ApplicationEventPublisher 的方式不同，上一种是通过事件 Aware 接口，这一种是通过 @Autowired 依赖注入，至于创建注册监听器的方式，发布事件的类型，上下两个代码块都可以进行替换。

输出结果 

```
接收到spring事件: hi listener...
```







## 17.9 Spring 层次性上下文事件传播

创建一个父容器，创建一个子容器，然后注册监听器，查看事件的传播机制。

```java
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
```

输出结果，第一条是 parent 容器 refresh 时触发的事件，第二条是 current 容器 refresh 时触发的事件，第三条是 current 容器触发事件后，还会进行层次性查找父容器，再在父容器中发布一次事件。

```
监听到 Spring 应用上下文[ID : parent-context] 的 ContextRefreshedEvent 事件
监听到 Spring 应用上下文[ID : current-context] 的 ContextRefreshedEvent 事件
监听到 Spring 应用上下文[ID : current-context] 的 ContextRefreshedEvent 事件
```



源码分析，在发布事件时，若容器存在父容器，会在父容器中再发布一次。

```java
AbstractApplicationContext.java
// 代码块1
protected void publishEvent(Object event, @Nullable ResolvableType eventType) {
    Assert.notNull(event, "Event must not be null");

    ApplicationEvent applicationEvent;
    // 如果事件是ApplicationEvent类及其子类, 强转为ApplicationEvent
    if (event instanceof ApplicationEvent) {
        applicationEvent = (ApplicationEvent) event;
    } else {
        // 如果不是, 则创建一个Payload事件
        applicationEvent = new PayloadApplicationEvent<>(this, event);
        if (eventType == null) {
            eventType = ((PayloadApplicationEvent<?>) applicationEvent).getResolvableType();
        }
    }

    if (this.earlyApplicationEvents != null) {
        this.earlyApplicationEvents.add(applicationEvent);
    } else {
        // 发布事件
        getApplicationEventMulticaster().multicastEvent(applicationEvent, eventType);
    }

    // 如果存在父容器, 则在父容器中也要发布一次事件
    if (this.parent != null) {
        if (this.parent instanceof AbstractApplicationContext) {
            ((AbstractApplicationContext) this.parent).publishEvent(event, eventType);
        } else {
            this.parent.publishEvent(event);
        }
    }
}
```







## 17.10 Spring 内建事件

Spring 内建事件 ApplicationContextEvent 的 4 大派生事件

- ContextRefreshedEvent - Spring 应用上下文就绪事件
- ContextStartedEvent  - Spring 应用上下文启动事件
- ContextStopedEvent  - Spring 应用上下文停止事件
- ContextClosedEvent  - Spring 应用上下文关闭事件



![image-20210528052038597](https://raw.githubusercontent.com/maoturing/PictureBed/master/picx/image-20210528052038597.53tolh2yq5k0.png)





AbstractApplicationContext 类实现了 ApplicationEventPublisher 接口，重写了 `publishEvent()` 方法来发布事件，在应用上下文 `refresh()` 完毕会发布 ContextRefreshedEvent 事件，源码如下所示：

```java
	AbstractApplicationContext.java

	@Override
	public void publishEvent(ApplicationEvent event) {
		publishEvent(event, null);
	}	

	// 该方法会在refresh() 最后一步被调用
	protected void finishRefresh() {
		clearResourceCaches();
		initLifecycleProcessor();
		getLifecycleProcessor().onRefresh();

        // 发布ContextRefreshedEvent事件
		publishEvent(new ContextRefreshedEvent(this));

		LiveBeansView.registerApplicationContext(this);
	}
	
	@Override
	public void start() {
		getLifecycleProcessor().start();
        // 发布ContextStartedEvent事件
		publishEvent(new ContextStartedEvent(this));
	}

	@Override
	public void stop() {
		getLifecycleProcessor().stop();
        // 发布ContextStoppedEvent事件
		publishEvent(new ContextStoppedEvent(this));
	}

	protected void doClose() {
		if (this.active.get() && this.closed.compareAndSet(false, true)) {
			try {
				// 发布ContextClosedEvent事件
				publishEvent(new ContextClosedEvent(this));
			}
			catch (Throwable ex) {
				logger.warn("Exception thrown from ApplicationListener handling ContextClosedEvent", ex);
			}
			// ......
		}
	}
```





## 17.11 Payload 事件

Payload 事件是为了简化 Spring 事件发送，更关注事件内容，比如发送一段字符串，发送一个对象，都可以使用 Payload 事件，如名称所示，Payload 是负责运载发送的事件的。

- 发送方法 - ApplicationEventPublisher#publishEvent(Object object)



```java
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

    // 实现ApplicationEventPublisherAware的方法, 发布事件
    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        // 发布 Payload 事件
        applicationEventPublisher.publishEvent("hello ...");
    }
}
```

输出结果

```
接收到spring事件: hello ...
```



## 17.12 自定义 Spring 事件

1. 自定义 Spring 事件

```java
public class MySpringEvent extends ApplicationEvent {

    public MySpringEvent(Object source) {
        super(source);
    }
}
```

2. 自定义监听器

```java
public class MySpringEventListener implements ApplicationListener<MySpringEvent> {
    @Override
    public void onApplicationEvent(MySpringEvent event) {
        System.out.println("监听到事件 " + event);
    }
}
```

3. 发布自定义事件

```java
public static void main(String[] args) {
    GenericApplicationContext applicationContext = new GenericApplicationContext();
    applicationContext.addApplicationListener(new MySpringEventListener());

    applicationContext.refresh();

    // 发布事件
    applicationContext.publishEvent(new MySpringEvent("hello world..."));
}
```

输出结果

```
监听到事件 org.geekbang.event.MySpringEvent[source=hello world...]
```





## 17.13 依赖注入 ApplicationEventPublisher

依赖注入 ApplicationEventPublisher 有两种方式

- 通过 @Autowired 依赖注入 ApplicationEventPublisher
- 通过实现 ApplicationEventPublisherAware 接口，回调获取 ApplicationEventPublisher



通过 @Autowired 依赖注入 ApplicationEventPublisher 的代码示例

```java
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
```



2. 通过实现 ApplicationEventPublisherAware 接口，回调获取 ApplicationEventPublisher

```java
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
```

实现 Aware 接口获取 ApplicationEventPublisher 的原理在生命周期章节已经讲过，再回顾一下，

```java
// 重写的BeanPostProcessor接口的方法,该方法会在所有bean初始化前回调
@Override
public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
    // 只对着6种接口的实现类bean进行处理
    if (!(bean instanceof EnvironmentAware || bean instanceof EmbeddedValueResolverAware ||
          bean instanceof ResourceLoaderAware || bean instanceof ApplicationEventPublisherAware ||
          bean instanceof MessageSourceAware || bean instanceof ApplicationContextAware)){
        return bean;
    }

    if (acc != null) {
		// 跳过
    }
    else {
        // 调用bean重写的Aware方法
        invokeAwareInterfaces(bean);
    }

    return bean;
}

// 调用Aware接口的实现类重写的方法
private void invokeAwareInterfaces(Object bean) {
    if (bean instanceof EnvironmentAware) {
        ((EnvironmentAware) bean).setEnvironment(this.applicationContext.getEnvironment());
    }
    if (bean instanceof EmbeddedValueResolverAware) {
        ((EmbeddedValueResolverAware) bean).setEmbeddedValueResolver(this.embeddedValueResolver);
    }
    if (bean instanceof ResourceLoaderAware) {
        ((ResourceLoaderAware) bean).setResourceLoader(this.applicationContext);
    }
    // 若bean实现了ApplicationEventPublisherAware接口
    if (bean instanceof ApplicationEventPublisherAware) {
        // 回调重写的setApplicationEventPublisher()方法
        ((ApplicationEventPublisherAware) bean).setApplicationEventPublisher(this.applicationContext);
    }
    if (bean instanceof MessageSourceAware) {
        ((MessageSourceAware) bean).setMessageSource(this.applicationContext);
    }
    if (bean instanceof ApplicationContextAware) {
        ((ApplicationContextAware) bean).setApplicationContext(this.applicationContext);
    }
}
```







## 17.14 依赖查找 ApplicationEventMulticaster

依赖查找 ApplicationEventMulticaster 的条件

- Bean 名称 - applicationEventMulticaster
- Bean 类型 - ApplicationEventMulticaster



Spring 框架在启动`refresh()`时会调用下面的方法，将广播器进行初始化。

```java
AbstractApplicationContext.java

public static final String APPLICATION_EVENT_MULTICASTER_BEAN_NAME = "applicationEventMulticaster";

protected void initApplicationEventMulticaster() {
    ConfigurableListableBeanFactory beanFactory = getBeanFactory();
    // 当前容器存在bean applicationEventMulticaster,则依赖查找该bean
    if (beanFactory.containsLocalBean(APPLICATION_EVENT_MULTICASTER_BEAN_NAME)) {
        this.applicationEventMulticaster =
            beanFactory.getBean(APPLICATION_EVENT_MULTICASTER_BEAN_NAME, ApplicationEventMulticaster.class);

    }else {
        // 当前容器不存在bean applicationEventMulticaster,创建一个Multicaster注册到容器
        this.applicationEventMulticaster = new SimpleApplicationEventMulticaster(beanFactory);
        beanFactory.registerSingleton(APPLICATION_EVENT_MULTICASTER_BEAN_NAME, this.applicationEventMulticaster);
    }
}
```



源码分析，前面章节都会向容器注册监听器，调用的 `addApplicationListener()` 源码如下所示，会将注册的监听器添加到  applicationEventMulticaster。发布事件时，会获取所有监听器，然后通知所有关注了该事件类型的监听器。

```java
// 代码块2
AbstractApplicationContext.java

    // 应用上下文中保存了一个广播器
    private ApplicationEventMulticaster applicationEventMulticaster;
	
    // 将注册的监听器添加到applicationEventMulticaster
	@Override
	public void addApplicationListener(ApplicationListener<?> listener) {
		if (this.applicationEventMulticaster != null) {
            // 向广播器中添加监听器
			this.applicationEventMulticaster.addApplicationListener(listener);
		}
		this.applicationListeners.add(listener);
	}
```





## 17.15 ApplicationEventPublisher 事件广播

- 接口 - ApplicationEventMulticaster
- 抽象类 - AbstractApplicationEventMulticaster
- 实现类 - SimpleApplicationEventMulticaster



SpringBoot 和 SpringCloud 都使用了 SimpleApplicationEventMulticaster 来广播事件，





## 17.16 同步和异步事件广播



### 1. 基于接口发布异步事件

Spring 的同步和异步事件都是基于 SimpleApplicationEventMulticaster

- 模式切换 - setTaskExecutor(java.util.concurrent.Executor) 方法
  - 默认模式：同比
  - 异步模式：
- 设计缺陷：非基于接口的契约编程



1. 先创建一个监听器，用于监听广播器发布的事件，并作出响应

```java
public class MySpringEventListener implements ApplicationListener<MySpringEvent> {
    @Override
    public void onApplicationEvent(MySpringEvent event) {
        System.out.printf("线程[%s]监听到事件 %s", Thread.currentThread().getName(), event);
    }
}
```



1. 使用 Spring 广播器 ApplicationEventMulticaster 发布异步事件，需要提供一个线程池来执行异步操作，下面的代码演示了如何为 Spring 广播器设置线程池，用于异步发布事件。

```java
public static void main(String[] args) {
    GenericApplicationContext applicationContext = new GenericApplicationContext();
    // 注册监听器
    applicationContext.addApplicationListener(new MySpringEventListener());
    applicationContext.refresh();

    // 获取广播器, 将广播器设置为异步模式
    ApplicationEventMulticaster multicaster = applicationContext.getBean(
        AbstractApplicationContext.APPLICATION_EVENT_MULTICASTER_BEAN_NAME,
        ApplicationEventMulticaster.class);

    // 设置为异步广播事件, 设置后发布事件的线程就不再是main线程了
    if (multicaster instanceof SimpleApplicationEventMulticaster) {
        SimpleApplicationEventMulticaster simpleMulticaster = (SimpleApplicationEventMulticaster) multicaster;

        ExecutorService executor = Executors.newSingleThreadExecutor();
        // (重点)为广播器设置用于执行异步任务的线程池taskExecutor
        simpleMulticaster.setTaskExecutor(executor);

        // 在spring容器关闭时, 停止线程池, 否则程序无法正常退出
        simpleMulticaster.addApplicationListener((ApplicationListener<ContextClosedEvent>) event -> {
            if (!executor.isShutdown()) {
                executor.shutdown();
            }
        });
    }

    // 发布事件
    applicationContext.publishEvent(new MySpringEvent("hello world..."));
    applicationContext.close();
}
```

输出结果，第 1 行表示监听到了事件，而且是异步线程监听到事件并作出了响应，第 2 行表示程序正常退出。如果代码中没有手动关闭线程池，那么第 2 行不会显示，程序也不会停止。

```
线程[pool-1-thread-1]监听到事件 org.geekbang.event.MySpringEvent[source=hello world...]
Process finished with exit code 0
```

2. 源码分析，示例代码中使用应用上下文 applicationContext 发布了事件，实际上是获取应用上下文中的 applicationEventMulticaster，然后广播事件，源码如下所示

```java
protected void publishEvent(Object event, @Nullable ResolvableType eventType) {
    Assert.notNull(event, "Event must not be null");

	// 判断事件类型,若不是ApplicationEvent类型, 则构建为Payload类型
    ApplicationEvent applicationEvent;
    if (event instanceof ApplicationEvent) {
        applicationEvent = (ApplicationEvent) event;
    }else {
        applicationEvent = new PayloadApplicationEvent<>(this, event);
        if (eventType == null) {
            eventType = ((PayloadApplicationEvent<?>) applicationEvent).getResolvableType();
        }
    }

    if (this.earlyApplicationEvents != null) {
        this.earlyApplicationEvents.add(applicationEvent);
    }
    else {
        // (重点) 获取Spring广播器,广播事件
        // 见下方代码块
        getApplicationEventMulticaster().multicastEvent(applicationEvent, eventType);
    }

	// 省略向父容器中发布事件
}
```

3. 源码分析，applicationEventMulticaster 广播事件分为两种模式：
   1. 同步模式，若未对广播器设置`setTaskExecutor()`执行异步任务的线程池 executor，则认为是同步模式，发布事件会直接回调监听器
   2. 异步模式，示例代码中对广播器设置了`setTaskExecutor()`执行异步任务的线程池 executor，因此发布事件，会使用线程池来回调监听器，达到异步发布的效果
   3. 回调监听器，都是回调的实现了 ApplicationListener 接口的 `onApplicationEvent()` 方法

```java
SimpleApplicationEventMulticaster.java
    
// 用于执行异步任务的线程池
private Executor taskExecutor;

@Override
public void multicastEvent(final ApplicationEvent event, @Nullable ResolvableType eventType) {
    // 解析事件event的类型
    ResolvableType type = (eventType != null ? eventType : resolveDefaultEventType(event));
    // 获取用于异步监听的线程池,实例代码中手动设置了, setTaskExecutor()
    Executor executor = getTaskExecutor();
    // 遍历监听器, 根据事件类型获取关注该事件类型的监听器
    for (ApplicationListener<?> listener : getApplicationListeners(event, type)) {
        
        // 异步事件,设置了executor
        if (executor != null) {
            // 将任务提交给线程池executor执行, 
            // 好处是回调监听器方法如果阻塞了,也不影响下面的代码执行
            // 回调监听器监听方法,见doInvokeListener()
            executor.execute(() -> invokeListener(listener, event));
        } else {
            // 同步事件,未设置executor,
            // 回调监听器监听方法,见doInvokeListener() 
            invokeListener(listener, event);
        }
    }
}

// 回调监听器的onApplicationEvent()方法
private void doInvokeListener(ApplicationListener listener, ApplicationEvent event) {
    try {
        // 回调监听器重写的onApplicationEvent()方法
        
        listener.onApplicationEvent(event);
    }
    catch (ClassCastException ex) {
		//.....
    }
}
```



### 2. 基于注解发布异步事件

- 基于注解 @EventListener
- 默认为同步，使用 @Async + @EnableAsync 切换为异步

- 好处：不需要实现接口和注册监听器



1. 基于注解发布异步事件示例代码如下，

```java
@EnableAsync
public class AnnotatedAsyncEventHandlerDemo {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        applicationContext.register(AnnotatedAsyncEventHandlerDemo.class);
        applicationContext.refresh();

        // 发布事件
        applicationContext.publishEvent(new MySpringEvent("hello world..."));
        applicationContext.close();
    }

    @EventListener
    @Async
    public void onApplicationEvent(MySpringEvent event) {
        System.out.printf("线程[%s]监听到事件 %s", Thread.currentThread().getName(), event);
    }

    @Bean
    public Executor taskExecutor() {
        return Executors.newSingleThreadExecutor();
    }
}
```

输出结果，如果示例代码中没有手动创建 taskExecutor bean，那么就会打印日志说明，如果创建了  taskExecutor bean，则只输出第 3 行内容。

```
五月 29, 2021 12:54:59 上午 org.springframework.aop.interceptor.AsyncExecutionAspectSupport getDefaultExecutor
信息: No task executor bean found for async processing: no bean of type TaskExecutor and no bean named 'taskExecutor' either

线程[pool-1-thread-1]监听到事件 org.geekbang.event.MySpringEvent[source=hello world...]
```



2. 源码分析，发布异步事件前，会先获取线程池，如果未手动设置线程池，则会创建一个默认线程池 SimpleAsyncTaskExecutor。

```java
AsyncExecutionInterceptor.java
    
protected Executor getDefaultExecutor(@Nullable BeanFactory beanFactory) {
    // 获取用于异步执行的线程池, 见下个代码块
    Executor defaultExecutor = super.getDefaultExecutor(beanFactory);
    // 若未获得线程池,则创建一个线程池
    return (defaultExecutor != null ? defaultExecutor : new SimpleAsyncTaskExecutor());
}
```

3. 源码分析，获取用于执行异步任务的线程池，会从 Spring 容器中查找

```java
AsyncExecutionAspectSupport.java

protected Executor getDefaultExecutor(@Nullable BeanFactory beanFactory) {
    if (beanFactory != null) {
        try {
            // 根据类型依赖查找
            return beanFactory.getBean(TaskExecutor.class);
        }
        catch (NoUniqueBeanDefinitionException ex) {
            try {
                // 存在多个TaskExecutor,则根据名称"taskExecutor"再查找
                return beanFactory.getBean(DEFAULT_TASK_EXECUTOR_BEAN_NAME, Executor.class);
            }catch (NoSuchBeanDefinitionException ex2) {
                if (logger.isInfoEnabled()) {
                    logger.info("More than one TaskExecutor bean found within the context, and none is named " +
                                "'taskExecutor'. Mark one of them as primary or name it 'taskExecutor' (possibly " +
                                "as an alias) in order to use it for async processing: " + ex.getBeanNamesFound());
                }
            }
        }
        catch (NoSuchBeanDefinitionException ex) {
            try {
                // 容器中不存在TaskExecutor,则查找名称为"taskExecutor"类型为Executor的bean
                return beanFactory.getBean(DEFAULT_TASK_EXECUTOR_BEAN_NAME, Executor.class);
            }catch (NoSuchBeanDefinitionException ex2) {
                // 打印日志,示例代码中打印的信息
                logger.info("No task executor bean found for async processing: " +
                            "no bean of type TaskExecutor and no bean named 'taskExecutor' either");
            }
        }
    }
    return null;
}
```

4. @EventListener 如何注册到容器？

Spring 框架在启动时，肯定会将 @EventListener 标记的方法所在类，创建为 bean 对象并注册到容器，然后进行初始化，在**初始化完成时**，会回调 SmartInitializingSingleton#afterSingletonsInstantiated 方法，然后调用下面的方法 `processBean()`，创建一个监听器 ApplicationListenerMethodAdapter，并注册到 Spring 应用上下文。

EventListenerMethodProcessor 实现了 SmartInitializingSingleton 接口，重写了 afterSingletonsInstantiated  方法，在方法中调用了下面的方法 `processBean()`，在初始化完成时会回调该方法，具体细节见 Bean 生命周期章节 9.14 Bean 初始化完成

```java
EventListenerMethodProcessor.java
    
// 这里肯定是处理的示例代码类, 类型targetType为AnnotatedAsyncEventHandlerDemo.class
private void processBean(final String beanName, final Class<?> targetType) {
    
    // 目标类中存在@EventListener注解
    if (!this.nonAnnotatedClasses.contains(targetType) &&
        AnnotationUtils.isCandidateClass(targetType, EventListener.class) &&
        !isSpringContainerClass(targetType)) {

        Map<Method, EventListener> annotatedMethods = null;
        try {
            // 获取所有被@EventListener标记的方法
            annotatedMethods = MethodIntrospector.selectMethods(targetType,
                                                                (MethodIntrospector.MetadataLookup<EventListener>) method ->
                                                                AnnotatedElementUtils.findMergedAnnotation(method, EventListener.class));
        } catch (Throwable ex) {}

        if (CollectionUtils.isEmpty(annotatedMethods)) {
            this.nonAnnotatedClasses.add(targetType);
        }else {
            ConfigurableApplicationContext context = this.applicationContext;
            List<EventListenerFactory> factories = this.eventListenerFactories;
            // 遍历被@EventListener标记的方法
            for (Method method : annotatedMethods.keySet()) {
                // 这里只有一个DefaultEventListenerFactory
                for (EventListenerFactory factory : factories) {
                    if (factory.supportsMethod(method)) {
                        Method methodToUse = AopUtils.selectInvocableMethod(method, context.getType(beanName));
                        
                        // 创建一个ApplicationListenerMethodAdapter,封装了bean名称,回调方法
                        ApplicationListener<?> applicationListener =
                            factory.createApplicationListener(beanName, targetType, methodToUse);
                        if (applicationListener instanceof ApplicationListenerMethodAdapter) {
                            ((ApplicationListenerMethodAdapter) applicationListener).init(context, this.evaluator);
                        }
                        // (重点)将创建的监听器注册到容器,后面发布事件时,会获取监听器并通知
                        context.addApplicationListener(applicationListener);
                        break;
                    }
                }
            }
        }
    }
}
```





## 17.17 事件异常处理

Spring 错误处理接口 - ErrorHandler

- 使用场景
  - Spring 事件 - ApplicationEventMulticaster
  - Spring 本地调度 - ConcurrentTaskScheduler，ThreadPoolTaskScheduler

1. 错误处理器的作用是，当发生异常时，程序不会终止。示例代码为广播器设置了错误处理器，当监听器响应时发生了异常，ErrorHandler 则会进行处理，Spring 应用上下文正常运行，不会因为发生了异常而终止了程序。

```java
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
```

输出结果，发布事件后监听到了，打印事件信息，发生了异常，程序正常运行，ErrorHandler 处理异常打印异常信息

```
hello world...
applicationContext 正常运行....
自定义异常...
```



2. 源码分析，广播器发布事件后，回调监听器的事件处理方法，若处理方法发生了异常，会 catch 住并交给 ErrorHandler 进行处理，即回调自定义的 ErrorHandler 的异常处理方法。

```java
SimpleApplicationEventMulticaster.java

// 错误处理器
private ErrorHandler errorHandler;

protected void invokeListener(ApplicationListener<?> listener, ApplicationEvent event) {
    // 获取ErrorHandler
    ErrorHandler errorHandler = getErrorHandler();
    
    // 当设置了ErrorHandler,若回调监听器方法时出现了异常,则catch并交给ErrorHandler处理
    if (errorHandler != null) {
        try {
            doInvokeListener(listener, event);
        }catch (Throwable err) {
            // 回调ErrorHandler错误处理方法
            errorHandler.handleError(err);
        }
    }else {
        // 若未设置ErrorHandler,若回调监听器方法时出现了异常,则终止程序
        doInvokeListener(listener, event);
    }
}
```





## 17.18 事件 - 监听器实现原理





## 17.19 SpringBoot SpringCloud 中的事件

- SpringBoot 中的事件

| 事件类型                            | 触发时机                                 |
| ----------------------------------- | ---------------------------------------- |
| ApplicationStartingEvent            | 当 SpringBoot 应用启动时                 |
| ApplicationStartedEvent             | 当 SpringBoot 应用已启动完成             |
| ApplicationEnvironmentPreparedEvent | 当 SpringBoot Environment 实例已准备完成 |
| ApplicationPreparedEvent            | 当 SpringBoot 应用准备好时               |
| ApplicationReadyEvent               | 当 SpringBoot 应用完全可用               |
| ApplicationFailedEvent              | 当 SpringBoot 应用启动失败               |



- SpringCloud 中的事件

| 事件类型                   | 触发时机                              |
| -------------------------- | ------------------------------------- |
| EnvironmentChangeEvent     | 当 Environment 实例配置属性发生变化时 |
| HearbeatEvent              | 当 DiscoveryClient 客户端发送心跳时   |
| InstancePreRegisteredEvent | 当服务实例注册前                      |
| InstanceRegisteredEvent    | 当服务实例注册后                      |
| RefreshEvent               | 当 RefreshEndpoint 被调用时           |
| RefreshScopeRefreshedEvent | 当 Refresh Scope Bean 刷新后          |



SpringBoot 和 SpringCloud 中的事件都是集成了 ApplicationEvent，与前面我们自定义事件的方式一致，发布事件也是通过 `ApplicationEventPublisher.publishEvent()` 发布事件。





## 17.20 面试题

1. Spring 事件的核心接口和组件

   答：1. Spring 事件 - ApplicationEvent

   1. Spring 事件监听器 - ApplicationListener
   2. Spring 事件发布器 - ApplicationEventPublisher
   3. Spring 事件广播器 - ApplicationEventMulticaster

   

2. Spring 同步事件和异步事件处理的使用场景

   答：Spring 同步事件，Spring 中经常使用，如容器启动事件 ContextRefreshedEvent

   Spring 异步事件，主要是 @EventListener 和 @Async 配合，实现异步处理，不阻塞主线程，比如耗时较长的数据计算任务等。

   

3. @EventListener 的工作原理

   答：参考 17.16.2.4 章节，

   1. 注册监听器：Spring 框架在 bean 初始化完成时，会回调方法，创建监听器并注册到容器
   2. 回调 @EventListener 标记的方法：当事件发布后，会通知所有关注该事件类型的监听器，然后回调监听器方法





# 18 注解 Annotations

## 18.1 Spring 注解驱动编程发展历程

- Spring 1.x 

- Spring 2.x   @Transactional  @ManagedResource  @Component

- Spring 3.x   @ComponentScan  @Configuration  @Bean  @Lazy  @Primary  @ImportResource  

- Spring 4.x    @Profile  @Conditional  

- Spring 5.x    @NonNull  @Nullable  @Indexed

  

[Spring中@NotNull注解@Valid注解简介及使用](https://blog.csdn.net/weixin_44906271/article/details/105844930)

[Spring5--@Indexed注解](https://blog.csdn.net/Crazypokerk_/article/details/98211897)



## 18.2 核心注解场景分类

- Spring 模式注解

| Spring 模式注解 | 场景               | 起始版本 |
| --------------- | ------------------ | -------- |
| @Repository     | 数据仓储模式注解   | 2.0      |
| @Component      | 通用组件模式注解   | 2.5      |
| @Service        | 服务模式注解       | 2.5      |
| @Controller     | Web 控制器模式注解 | 2.5      |
| @Configuration  | 配置类模式注解     | 3.0      |

- Spring 装配注解

| Spring 装配注解 | 场景                               | 起始版本 |
| --------------- | ---------------------------------- | -------- |
| @ImportResource | 替换 xml 标签 `<import>`           | 2.5      |
| @Import         | 导入 Configuration 类              | 2.5      |
| @ComponentScan  | 扫描指定包下被 @Conponent 标记的类 | 3.1      |

- Spring 依赖注入注解

| Spring 装配注解 | 场景                                | 起始版本 |
| --------------- | ----------------------------------- | -------- |
| @Autowired      | Bean 依赖注入，支持多种依赖查找方法 | 2.5      |
| @Import         | 指定 Bean 名称 @Autowired 依赖查找  | 2.5      |





## 18.3 注解编程模型

Spring 注解编程模型

- 元注解 Meta Annotations

- 模式注解 Stereotype Annotations

- 组合注解 Composed Annotations

- 注解属性别名 Attribute Aliases

- 注解属性覆盖 Attribute Overrides





## 18.4 元注解 Meta Annotations



元注解是指可以标记在注解上的注解



@Component 注解源码如下所示，其上标记的注解都可以称为元注解

```java
// 表示该注解可以标记的类型, 这里表示可以标记类,接口,注解
@Target(ElementType.TYPE)
// 表示注解保留的时间,这里表示保留到运行时
@Retention(RetentionPolicy.RUNTIME)
// 表示生产文档, 没有什么用
@Documented
@Indexed
public @interface Component {

	String value() default "";
}
```



@Target 注解源码如下所示，其上标记的注解 @Target 表示 @Target 可以标记在注解上，故 @Target 就是元注解

```java
@Documented
@Retention(RetentionPolicy.RUNTIME)
// 表示@Target可以标记在注解上 
@Target(ElementType.ANNOTATION_TYPE)
public @interface Target {

    ElementType[] value();
}
```







## 18.5 模式注解 Stereotype Annotations

下面都是 @Component 注解的派生注解

- @Component
- Controller
- Service
- Repository
- Configuration
- SpringBootConfiguration



 @Component 派生原理

- 核心组件 - ClassPathBeanDefinitionScanner，ClasspathScanningCandidateComponentProvider
- 资源处理 - ResourcePatternResolver
- 资源类的元信息  - MetadataReaderFactory
- 类元信息 - ClassMetadata
  - ASM 实现 - ClassMetadataReadingVistor
  - 反射实现 - StandardAnnotationMetadata
- 注解元信息 - AnnotationMetadata
  - ASM 实现 - AnnotationMetadataReadingVistor
  - 反射实现 - StandardAnnotationMetadata



// 补充@Component 类的扫描，解析，分析上面各个类的作用和核心方法

// 重点，参考掘金小册Spring源码 33 节 https://juejin.cn/book/6857911863016390663/section/6867689993351987207











## 18.6 组合注解 Composed Annotations

> 组合注解 Composed Annotations 是通过元标注的方法，组合多个注解。



最常见的组合注解就是 @SpringBootApplication，合并了 @SpringBootConfiguration ， @EnableAutoConfiguration 和 @ComponentScan 注解，具有 3 个注解的作用。

```java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@SpringBootConfiguration
@EnableAutoConfiguration
@ComponentScan(excludeFilters = { @Filter(type = FilterType.CUSTOM, classes = TypeExcludeFilter.class),
		@Filter(type = FilterType.CUSTOM, classes = AutoConfigurationExcludeFilter.class) })
public @interface SpringBootApplication {
    
}
```





## 18.7 注解属性别名 Attribute Aliases



1. 显式别名：如果注解中两个属性使用 @AliasFor 互为别名，那么两个属性的作用是一样的。下面例子中 @ComponentScan 的两个属性 `value` 与 `basePackages` 互为别名，说明在使用 @ComponentScan 注解时，使用这两个属性都可以指定扫描包

```java
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Repeatable(ComponentScans.class)
public @interface ComponentScan {

	@AliasFor("basePackages")
	String[] value() default {};

	@AliasFor("value")
	String[] basePackages() default {};
}
```

下面两行代码的作用完全一致

```java
@ComponentScan(value="org.geekbang.annotation")
@ComponentScan(basePackages="org.geekbang.annotation")
```



2. 隐形别名：对继承或组合的元注解属性设置别名

   下面 @SpringBootApplication 组合了 @ComponentScan 注解，使用 @AliasFor 为 @ComponentScan 的 basePackages 属性设置别名为 scanBasePackages；

   同样的  @SpringBootApplication 还组合了 @EnableAutoConfiguration注解，使用 @AliasFor 为 @EnableAutoConfiguration 的 exclude 属性设置别名为当前注解的属性 exclude；

   这样为 @SpringBootApplication 属性 scanBasePackages 设置的值，相当于为注解 @ComponentScan 的 basePackages 属性设置了值。

```java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@SpringBootConfiguration
@EnableAutoConfiguration
@ComponentScan(excludeFilters = { @Filter(type = FilterType.CUSTOM, classes = TypeExcludeFilter.class),
		@Filter(type = FilterType.CUSTOM, classes = AutoConfigurationExcludeFilter.class) })
public @interface SpringBootApplication {

	@AliasFor(annotation = EnableAutoConfiguration.class)
	Class<?>[] exclude() default {};

	@AliasFor(annotation = EnableAutoConfiguration.class)
	String[] excludeName() default {};

	@AliasFor(annotation = ComponentScan.class, attribute = "basePackages")
	String[] scanBasePackages() default {};
}
```





3. 传递性别名：





## 18.8 注解属性覆盖 Attribute Overrides





## 18.9 @Enable 模块驱动

@Enable 模块驱动是以 @Enable 为前缀的注解驱动编程模型。所谓模块时指具备相同领域的功能组件集合，组合所形成的一个独立单元。比如 Web MVC 模块，AspectJ 代理模块，Caching 缓存模块，Async 异步处理模块。

- @EnableWebMvc
- @EnableTransactionManagement
- @EnableCaching
- @EnableMBeanExport
- @EnableAsync



@Enable 模块驱动编程模式

1. 创建驱动注解 - @EnableXXX

2. 使用 @Import 导入具体实现

3. 具体实现有 3 种方式

   - Configuration

   - ImportSelector
   - ImportBeanDefinitionRegister 





### 1. @Import + @Configuration



1. 创建驱动注解 @EnableHelloWorld，使用 @Import 导入具体实现 HelloWorldConfiguration

```java
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
// 导入具体实现
@Import(HelloWorldConfiguration.class)
public @interface EnableHelloWorld {
}
```

2. 创建具体实现配置类 HelloWorldConfiguration，配置类中创建了 bean helloWorld。

```java
@Configuration
public class HelloWorldConfiguration {
    @Bean
    public String helloWorld() {
        return "hello world...";
    }
}
```

3. 使用 @EnableHelloWorld 激活 HelloWorld 模块，这样  @EnableHelloWorld 中导入的具体实现 HelloWorldConfiguration 配置类就生效并被注册到容器了，其中声明的 bean `helloWorld` 也注册到容器了，因此下面可以从容器中依赖查找到 bean `helloWorld` 

   若未使用 @EnableHelloWorld 激活 HelloWorld 模块，自然依赖查找也就找不到 bean `helloWorld` 

```java
// 激活 HelloWorld 模块
@EnableHelloWorld
public class EnableModuleDemo {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        applicationContext.register(EnableModuleDemo.class);

        applicationContext.refresh();
		
        String helloWorld = applicationContext.getBean("helloWorld", String.class);
        System.out.println(helloWorld);
    }
}
```

输出结果

```
hello world...
```



### 2. @Import + ImportSelector 接口

1. 创建驱动注解 @EnableMyCaching，使用 @Import 导入具体实现 MyCachingImportSelector

```java
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
// 导入具体实现
@Import(MyCachingImportSelector.class)
public @interface EnableMyCaching {
}
```

2. 创建具体实现 MyCachingImportSelector，加载 MyCachingConfiguration 配置类。

```java
public class MyCachingImportSelector implements ImportSelector {
    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        // 导入指定的配置类
        return new String[]{"org.geekbang.annotation.enable.MyCachingConfiguration"};
    }
}
```

```java
@Configuration
public class MyCachingConfiguration {
    @Bean
    public String myCaching() {
        return "MyCaching";
    }
}
```

3. 使用 @EnableMyCaching 激活 MyCaching 模块，这样  @EnableMyCaching 中导入的具体实现 MyCachingConfiguration 配置类就生效并被注册到容器了，其中声明的 bean `myCaching`也注册到容器了，因此下面可以从容器中依赖查找到 bean `myCaching` 

```java
@EnableMyCaching    // 激活 MyCaching 模块
public class EnableModuleDemo {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        applicationContext.register(EnableModuleDemo.class);

        applicationContext.refresh();

        String myCaching = applicationContext.getBean("myCaching", String.class);
        System.out.println(myCaching);
    }
}
```



### 3.@Import + ImportBeanDefinitionRegistrar 接口



// 补充: 复习 SpringBoot 源码 starter 部分, 这才是重点



## 18.10 条件注解

- 基于配置的条件注解 - @Profile
- 基于编程的条件注解 - @Conditional



@Conditional 实现原理

- 上下文对象 ConditionContext
- 条件判断 - ConditionEvaluator
- 配置阶段 - ConfigurationPhase
- 判断入口 - ConfigurationClassPostProcessor，ConfigurationClassParser



### 1. @Profile 条件注解

@Profile 使用示例，创建两个名词都为`number`的 bean，但是设置不同的 profile。与上下文的 Environment profile 匹配的 bean，才会被注册到容器

```java
public static void main(String[] args) {
    AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
    applicationContext.register(ProfileDemo.class);

    // 为Environment设置激活的profile为 odd
    ConfigurableEnvironment environment = applicationContext.getEnvironment();
    environment.setActiveProfiles("odd");
    // environment.setActiveProfiles("even");

    applicationContext.refresh();

    Integer number1 = applicationContext.getBean("number", Integer.class);
    System.out.println(number1);
}

@Bean(name = "number")
@Profile("odd")     // 奇数
public Integer odd() {
    return 1;
}

@Bean(name = "number")
@Profile("even")    // 偶数
public Integer even() {
    return 1;
}
```

输出结果，可以看出，第 1 次 Environment 激活的 profile 是 odd，依赖查找获得的 `number=1`；第 2 次 Environment 激活的 profile 是 even，依赖查找获得的 `number=2`；

```
1
2
```



### 2. Conditional 条件注解

// 补充，参考尚硅谷Spring注解驱动，Springboot源码

// 补充 @Conditional 生效的源码分析















## 18.11 SpringBoot 和 SpringCloud 中的注解

- SpringBoot 中的注解

| 注解                     | 场景                     |
| ------------------------ | ------------------------ |
| @SpringBootConfiguration | Spring Boot 配置类       |
| @SpringBootApplication   | Spring Boot 应用引导注解 |
| @EnableAutoConfiguration | Spring Boot 激活自动装配 |

- SpringCloud 中的注解

| 注解                    | 场景                      |
| ----------------------- | ------------------------- |
| @SpringCloudApplication | Spring Cloud 应用引导注解 |
| @EnableDiscoveryClient  | Spring Cloud 激活服务发现 |
| @EnableCircuitBreaker   | Spring Boot 激活熔断      |



## 18.12 面试题

1. SpringBoot 模式注解由那些？

   答：@Component 等派生注解

2. @EventListener 的工作原理

   答：核心类 - EventListenerMethodProcessor

   // 补充一下

   



