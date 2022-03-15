### Spring 框架中 Bean 的创建过程是怎样的?

简单来说，Spring 框架中 Bean 经过 4 个节点：实例化 -> 属性赋值 -> 初始化 -> 销毁

1. 实例化：new xxx()，读取到 bean 后使用 BeanDefinition 保存bean的信息，实例化有两个时机：

   ​	1. 当客户端向容器申请一个 bean 时 

   ​	2. 当容器初始化一个 bean 时发现还需要依赖另一个 bean。

2. 设置对象属性，即依赖注入，Spring 通过 BeanDefinition 找到对象依赖的其他对象，并将这些对象复制到当前对象的相应属性

3. 处理 Aware 接口，Spring 会检测对象如果实现了 xxxAware 接口，会调用相应的方法。BeanNameAware，BeanFactoryWawre

4. BeanPostProcessor 前置处理，调用 BeanPostProcessor 的 `postProcessBeforeInitialization()`方法，会在每个 bean 实例化之前都调用

5. 处理 InitializingBean 接口，Spring 会检测bean如果实现了该接口，就会调用 `afterPropertiesSet()`方法，定制初始化逻辑，在 bean 创建前调用

6. 处理 init-method，`<bean init-method="xxx">`如果 Spring 发现 Bean 配置了该属性，就会调用他的配置方法，执行初始化逻辑，与`@PostConstruct`类似

7. BeanPostProcessor 后置处理，调用 BeanPostProcessor 的 `postProcessAfterInitialization()`方法，会在每个 bean 实例化之后都调用

   > **到了这一步，bean 就可以正常被使用了**

8. 处理 DisposableBean 接口，Spring 会检测bean如果实现了该接口，就会在对象销毁前调用`destory()`方法。与  InitializingBean 接口相对应

9. 处理 destory-method，`<bean destory-method="xxx">`如果 Spring 发现 Bean 配置了该属性，就会调用他的配置方法，执行初始化逻辑，与`@PostDestory`类似

![img](https://img2018.cnblogs.com/blog/1027015/201907/1027015-20190722165455745-357028185.png)



### Spring 框架中的 bean 是线程安全的吗？如果线程不安全，要如何处理？

Spring 容器中的 bean 不是线程安全的。

bean 的作用于：

1. singleton，单例bean，默认线程不安全，但是对于开发中大部分的 Bean，其实都是无状态的，不需要保证线程安全。无状态表示这个实例没有属性对象，不能保存数据，即是不变的类，如 Controller，service，在平常的 MVC 开发中，是不会有线程安全问题的。另外还可以采用 ThreadLocal 来解决线程安全问题。
2. prototype，为每个bean 请求都创建新的实例，所以不存在线程安全问题

# Spring 如何处理循环依赖问题？

1. 构造器注入

```java
@Component
public class A {
    private B b;

    // 依赖B, 构造器注入
    public A(B b) {
        this.b = b;
    }
}

@Component
public class B {
    private A a;

    public B(A a) {
        this.a = a;
    }
}

@SpringBootApplication
public class Test {
    public static void main(String[] args) throws InterruptedException {
        SpringApplication.run(com.mooc.sb2.Sb2Application.class, args);
    }
}
```

执行结果：

```cmd
Description:

The dependencies of some of the beans in the application context form a cycle:

┌─────┐
|  a defined in file [D:\MyProjects\github\SpringBoot-Source\target\classes\com\mooc\sb2\circular\A.class]
↑     ↓
|  b defined in file [D:\MyProjects\github\SpringBoot-Source\target\classes\com\mooc\sb2\circular\B.class]
└─────┘
```



2. 通过 setter 注入

```java
public class A {
    private B b;

    public B getB() {
        return b;
    }

    public void setB(B b) {
        this.b = b;
    }

    public A() {
        System.out.println("a success");
    }
}

public class B {
    private A a;

    public A getA() {
        return a;
    }

    public void setA(A a) {
        this.a = a;
    }
    public B() {
        System.out.println("b success");
    }
}
```

```xml
<bean name = "a" class="com.mooc.sb2.circular.A" scope="prototype">
    <property name="b" ref="b"/>
</bean>
<bean name = "b" class="com.mooc.sb2.circular.B" scope="prototype">
    <property name="a" ref="a"/>
</bean>
```

```java
public static void main(String[] args) throws InterruptedException {
    ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:ioc/demo.xml");
    A a = context.getBean("a", A.class);
    System.out.println(a.getB());
}
```

执行结果：

```cmd
Caused by: org.springframework.beans.factory.BeanCurrentlyInCreationException: Error creating bean with name 'a': Requested bean is currently in creation: Is there an unresolvable circular reference?
```



通过以上两种注入方式，可以知道

1. 构造器注入不支持循环依赖会报错
2. setter 注入时，bean 为 prototype 类型时不支持循环依赖会报错，为 singleton 类型时支持循环依赖不报错
3. 只有单例 singleton 的 bean 会通过三级缓存提前暴露来解决循环依赖问题，因为我们讨论如何解决循环依赖建立在单例 bean 这种情况下





https://docs.spring.io/spring-framework/docs/current/reference/html/core.html#beans-dependency-resolution



如果使用构造器注入，则可能出现无法解决的循环依赖问题，Spring 会抛出 `BeanCurrentlyInCreationException`。使用 set 注入替代构造器注入可以避免该问题。

## 三级缓存

Spring 通过**三级缓存**来解决循环依赖问题，只有单例 singleton 的 bean 会通过三级缓存提前暴露来解决循环依赖问题，而非单例的 bean，每次从容器获取都是一个新的对象，都会重新创建，所以非单例的 bean 是没有缓存的，不会将其放到三级缓存中。

 

**实例化与初始化**

1. 实例化是内存中申请一块内存空间，类似于租好了房子，但是自己的家具东西还没有搬家进去
2. 初始化是给上一步的内存空间赋值，并完成属性的各种赋值

下面的代码中 bean 的定义就是实例化部分，属性定义就是初始化部分

```xml
<bean name = "a" class="com.mooc.sb2.circular.A" scope="singleton">
    <property name="b" ref="b"/>
</bean>
```



Spring 三级缓存有 3 个 HashMap 和 4 个重要方法：

```java
DefaultSingletonBeanRegistry.java

// 1级缓存, 也叫单例池, 存放已经初始化好了的bean对象
private final Map<String, Object> singletonObjects = new ConcurrentHashMap<>(256);

// 2级缓存, 存放的是实例化了，但未初始化的bean, 即未设置属性
private final Map<String, Object> earlySingletonObjects = new HashMap<>(16);

// 3级缓存, 存放可以生产bean的工厂FactoryBean, 
// 假如A类实现了FactoryBean,那么依赖注入的不是A类,而是A类产生的bean
private final Map<String, ObjectFactory<?>> singletonFactories = new HashMap<>(16);
```

- `getSingleton()` 

- `doCreateBean()`

- `populateBean()`

- `addSingleton()`

  

```xml
<bean name = "a" class="com.mooc.sb2.circular.A" scope="prototype">
    <property name="b" ref="b"/>
</bean>

<bean name = "b" class="com.mooc.sb2.circular.B" scope="prototype">
    <property name="a" ref="a"/>
</bean>
```



使用三级缓存创建 bean 的过程：

1. A 创建过程中需要 B，于是 A 将自己放到三级缓存中，暂停A的创建，去实例化 B

2. B 实例化的时候发现需要 A，于是去查找一级缓存，没有，再查二级缓存，没有，再查三级缓存，找到了 A。然后把三级缓存里的 A 放到二级缓存中，并从三级缓存中删除 A

3. B 初始化设置属性完毕，将自己放到一级缓存里，此时 B 的属性 A 依然是创建中的状态，所以接着创建 A，此时 B 已经创建结束，直接从一级缓存中取到 B，然后完成 A 的初始化，并将 A 自己放到一级缓存里面。

   

```java
protected Object getSingleton(String beanName, boolean allowEarlyReference) {
    Object singletonObject = this.singletonObjects.get(beanName);
    if (singletonObject == null && isSingletonCurrentlyInCreation(beanName)) {
        synchronized (this.singletonObjects) {
            singletonObject = this.earlySingletonObjects.get(beanName);
            if (singletonObject == null && allowEarlyReference) {
                ObjectFactory<?> singletonFactory = this.singletonFactories.get(beanName);
                if (singletonFactory != null) {
                    singletonObject = singletonFactory.getObject();
                    this.earlySingletonObjects.put(beanName, singletonObject);
                    this.singletonFactories.remove(beanName);
                }
            }
        }
    }
    return singletonObject;
}
```

