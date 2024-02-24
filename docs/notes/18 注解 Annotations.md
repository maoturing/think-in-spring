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

   



