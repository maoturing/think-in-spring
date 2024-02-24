# 19 Environment 抽象

> 什么是 Environment ?

就是对 profiles 的封装，包括生效的 profiles，默认的 profiles，profiles 的数据源。

对占位符进行处理，对





- 统一的 Spring 配置属性管理

Spring 3.1 引入了 Environment，它统一 Spring 配置属性的存储，包括占位符处理和类型转换，不仅完整地替换了  PropertyPlaceholderConfigurer，而且还支持更丰富的配置属性源 PropertySource。

- 条件化 Spring Bean 装配管理

通过 Environment Profiles 信息，帮助 Spring 容器提供条件化装配 Bean



## 19.1 Environment 接口使用场景

- 处理属性占位符
- 转换 Spring 配置属性类型，比如 String 属性转为 Integer 对象，Resource 对象
- 存储 Spring 配置属性源 PropertySource
- 用于 Profiles 状态的维护



![xxx](https://raw.githubusercontent.com/maoturing/PictureBed/master/picx/xxx.1xjuuus2q0io.png)



PropertyResolver 接口用来处理配置属性，包括获取属性值，转换配置属性类型，处理属性占位符等操作。

```java
public interface PropertyResolver {

	String getProperty(String key);

    // 获取配置属性, 若获取不到则使用默认值
    String getProperty(String key, String defaultValue);

    // 获取配置属性, 并转换为目标类型
	<T> T getProperty(String key, Class<T> targetType);

    // 处理属性占位符
	String resolvePlaceholders(String text);
}
```

Environment 接口 继承了 PropertyResolver 接口，主要负责 profiles 的管理，包括获取激活的profiles， 获取默认的profiles，加载指定的profiles 等功能

```java
public interface Environment extends PropertyResolver {
	// 获取激活的profiles
	String[] getActiveProfiles();

    // 获取默认的profiles
	String[] getDefaultProfiles();

	// 加载指定的profiles
	boolean acceptsProfiles(Profiles profiles);
}
```

ConfigurableEnvironment 接口继承了 Environment，主要负责 profiles 的管理，包括设置激活的profiles， 设置默认的profiles，获取profiles中的所有键值对等功能

```java
public interface ConfigurableEnvironment extends Environment, ConfigurablePropertyResolver {
	
    // 设置激活的 profiles
	void setActiveProfiles(String... profiles);

	void addActiveProfile(String profile);

    // 设置默认 Profiles
	void setDefaultProfiles(String... profiles);

    // 获取profiles中的所有key->value
	MutablePropertySources getPropertySources();

    // 获取系统属性
	Map<String, Object> getSystemProperties();

    // 
	Map<String, Object> getSystemEnvironment();
}
```

AbstractEnvironment 是核心类，主要有 3 个变量，分别保存生效的 profile，默认的 profiles，属性源。打印容器中的 Environment，输出的也是这 3 个属性。

```java
public abstract class AbstractEnvironment implements ConfigurableEnvironment {

    // 生效的profiles
	private final Set<String> activeProfiles = new LinkedHashSet<>();

    // 默认的profiles
	private final Set<String> defaultProfiles = new LinkedHashSet<>(getReservedDefaultProfiles());

    // 属性源,包括环境变量systemEnvironment,系统变量systemProperties,自定义配置文件
	private final MutablePropertySources propertySources = new MutablePropertySources();

    // 属性解析器
	private final ConfigurablePropertyResolver propertyResolver =
			new PropertySourcesPropertyResolver(this.propertySources);
}
```





## 19.2 Environment 处理占位符

Spring 3.1 之后

- 接口 - EmbeddedValueResolver
- 组件 - PropertySourcesPlaceholderConfigurer

Spring 3.1 之前

- 接口 - StringValueResolver

- 组件 - PropertyPlaceholderConfigurer





### 1. Spring 3.1+ 处理属性占位符

1. 创建 `user.properties` 属性文件

```properties
user.id=29
user.heroName=末日铁拳
```

2. 创建 spring bean 配置文件，为`user` bean 设置属性时使用占位符。创建`PropertySourcesPlaceholderConfigurer` bean 作为占位符处理类，并为该 bean 设置属性源为 `user.properties`，然后设置编码为`utf-8`。

   若不设置`PropertySourcesPlaceholderConfigurer` bean，则第 3 步创建`user`时无法解析占位符报错

```xml
<!--  Spring3.1后处理占位符的方式  -->
<bean class="org.springframework.context.support.PropertySourcesPlaceholderConfigurer">
    <property name="location" value="user.properties"/>
    <property name="fileEncoding" value="utf-8"/>
</bean>

<bean id="user" class="org.geekbang.ioc.overview.lookup.domain.User">
    <property name="id" value="${user.id}"/>
    <property name="name" value="${user.heroName}"/>
</bean>
```

3. 从容器中获取`user`，查看属性填充是否成功，占位符是否被处理

```java
public static void main(String[] args) {
    ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("placeholders-resolver.xml");

    User user = applicationContext.getBean("user", User.class);
    System.out.println(user);
}
```

输出结果，与配置文件中的属性一直，说明占位符被 PropertySourcesPlaceholderConfigurer 处理了。

```
User{id=29, name='末日铁拳'}
```



4. Spring 3.1 之前处理占位符，只需要将处理占位符的 bean 替换为`PropertyPlaceholderConfigurer`

```xml
<!--  Spring3.1前处理占位符的方式  -->
<bean class="org.springframework.beans.factory.config.PropertyPlaceholderConfigurer">
    <property name="location" value="user.properties"/>
    <property name="fileEncoding" value="utf-8"/>
</bean>
```



### 2. 源码分析

PropertySourcesPlaceholderConfigurer 间接实现了 BeanFactoryPostProcessor 接口的 postProcessBeanFactory 方法，调用时机会在 Spring 启动 `refresh()` 的 `invokeBeanFactoryPostProcessors(beanFactory)`步骤被调用，用来处理占位符。该方法分为以下步骤

1. 加载 systemProperties 系统属性，包括 java.vm.version, user.name 等属性
2. 加载 systemEnvironment 环境变量，包括 JAVA_HOME,CLASSPATH 等属性
3. 加载配置属性文件 `user.properties`中的所有属性

```java
PropertySourcesPlaceholderConfigurer.java	

// 继承了BeanFactoryPostProcessor#postProcessBeanFactory 
@Override
public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
    if (this.propertySources == null) {
        this.propertySources = new MutablePropertySources();
        if (this.environment != null) {
            // 1. 加载systemProperties系统属性,包括java.vm.version, user.home
            // 2. 加载systemEnvironment环境变量,包括JAVA_HOME,CLASSPATH
            this.propertySources.addLast(
                new PropertySource<Environment>(ENVIRONMENT_PROPERTIES_PROPERTY_SOURCE_NAME, this.environment) {
                    public String getProperty(String key) {
                        return this.source.getProperty(key);
                    }
                }
            );
        }
        try {
            // (重点)加载所有配置属性源中的属性,即前面的user.properties中的两个属性
            // 见代码块2, mergeProperties()会加载所有配置属性文件中的所有属性, 并进行合并
            PropertySource<?> localPropertySource =
                new PropertiesPropertySource(LOCAL_PROPERTIES_PROPERTY_SOURCE_NAME, mergeProperties());
            
            // 本地属性user.properties是否覆盖systemEnvironment,systemProperties中的属性
            // 典型代表就是user.name属性,若localOverride为true,则返回配置文件user.properties中配置的值
            // 否则返回systemProperties系统属性中的user.home,即操作系统的用户名
            if (this.localOverride) {
                this.propertySources.addFirst(localPropertySource);
            }else {
                // 默认走这里, 将user.properties中的属性添加到末尾,
                // 不会覆盖systemProperties系统属性,systemEnvironment环境变量
                this.propertySources.addLast(localPropertySource);
            }
        }catch (IOException ex) {
            throw new BeanInitializationException("Could not load properties", ex);
        }
    }

    // (重点) 处理占位符, 详细见当前类的processProperties()方法
    // 1.解析出属性key为"user.id",
    // 2.去this.propertySources中进行查询得到属性值为29
    // 3.将user的BeanDefinition中的id属性, 从占位符修改为29
    processProperties(beanFactory, new PropertySourcesPropertyResolver(this.propertySources));
    this.appliedPropertySources = this.propertySources;
}
```



```java
PropertySourcesPlaceholderConfigurer.java
// 代码块2
protected void loadProperties(Properties props) throws IOException {
    if (this.locations != null) {
        // 遍历所有属性配置文件
        for (Resource location : this.locations) {
            try {
                // 加载属性配置文件中的属性到props中
                // 文件路径为location,文件编码为fileEncoding
                // 这也是我们配置PropertySourcesPlaceholderConfigurer bean时设置的两个属性,在这里生效
                PropertiesLoaderUtils.fillProperties(
                    props, new EncodedResource(location, this.fileEncoding), this.propertiesPersister);
            }
            catch (FileNotFoundException | UnknownHostException ex) {
            }
        }
    }
}
```



## 19.3 配置 Spring Profiles



Spring 3.1 引入了条件配置 - 

API - ConfigurableEnvironment

- 修改 - addActvieProfile(String)   setActvieProfile(String)   setDefaultProfile(String) 
- 获取 - getActiveProfiles()    getDefaultProfiles()
- 匹配 - acceptsProfiles(String)

注解 - @Profile

 

详细参考 18 章



下面是 Spring 设置 profile 的示例代码，根据生效的 profile 不同，返回的 bean 也不同。设置 vm 参数`-Dspring.profiles.active=even`，运行程序

```java
public static void main(String[] args) {
    AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
    applicationContext.register(ProfileDemo.class);

    // 为Environment设置激活的profile为 odd
    ConfigurableEnvironment environment = applicationContext.getEnvironment();
    environment.setDefaultProfiles("odd");

    // 下面3行配置的作用都是一样的, 让生效的profile文件为 even
    // -Dspring.profiles.active=even        spring项目使用这个设置为vm参数
    // --spring.profiles.active=even        springboot项目使用这个设置为程序参数
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
    return 2;
}
```

输出结果 

```
2
```



源码分析 

1. `environment.setActiveProfiles("even")`的源码如下所示，会将设置激活的 profile 保存到 `activeProfiles` 属性中。

```java
AbstractEnvironment.java

private final Set<String> activeProfiles = new LinkedHashSet<>();

// 设置激活的profile
public void setActiveProfiles(String... profiles) {
    Assert.notNull(profiles, "Profile array must not be null");
    if (logger.isDebugEnabled()) {
        // debug模式下会打印激活的profile
        logger.debug("Activating profiles " + Arrays.asList(profiles));
    }
    
    // 将激活的profile保存到activeProfiles属性中
    synchronized (this.activeProfiles) {
        this.activeProfiles.clear();
        for (String profile : profiles) {
            validateProfile(profile);
            this.activeProfiles.add(profile);
        }
    }
}
```

2. 获取激活的 profile，会先去 `activeProfiles` 属性中查找，若其为空，则说明未设置激活的 profile。则去 propertySource 中查找是否设置了 vm 参数`-Dspring.profiles.active`，或者是否设置了程序参数`--spring.profiles.active`，若设置了，则将其设置为激活的 profile 并返回。

```java
public static final String ACTIVE_PROFILES_PROPERTY_NAME = "spring.profiles.active";

private final MutablePropertySources propertySources = new MutablePropertySources();

// 获取激活的profile
protected Set<String> doGetActiveProfiles() {
    synchronized (this.activeProfiles) {
        // 若未设置生效的profiles
        if (this.activeProfiles.isEmpty()) {
            // 从配置属性propertySources中获取spring.profiles.active的值
            // 如果设置了-Dspring.profiles.active=even,--spring.profiles.active=even
            // 都会读取到propertySources中
            String profiles = getProperty(ACTIVE_PROFILES_PROPERTY_NAME);
            
            // 如果设置了spring.profiles.active,则将其设置为激活的profile
            if (StringUtils.hasText(profiles)) {
                setActiveProfiles(StringUtils.commaDelimitedListToStringArray(
                    StringUtils.trimAllWhitespace(profiles)));
            }
        }
        // 返回profile
        return this.activeProfiles;
    }
}


```



## 19.4 Spring 4 重构 @Profile

- 基于 Condition 接口实现，核心类 - ProfileCondition



### 1.@Profile 原理分析

1. @Profile 的源码如下所示

```java
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
// 当ProfileCondition#match返回true,条件通过
@Conditional(ProfileCondition.class)
public @interface Profile {

	String[] value();
}
```

2. ProfileCondition 重写了 Condition 接口的 matches 方法，若 @Profile 的 value 与当前生效的 profile 相同，则返回 true，表示需要加载该 bean。

```java
// 代码块3
class ProfileCondition implements Condition {

    // 实现的Condition#matches方法
    // 若该方法返回true,则表示该bean应该被加载, 
    // 返回false,则表示该bean应该被忽略
	@Override
	public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        // 获取@Profile注解的属性值, 即为odd/even
		MultiValueMap<String, Object> attrs = metadata.getAllAnnotationAttributes(Profile.class.getName());
		if (attrs != null) {
			for (Object value : attrs.get("value")) {
                // 若当前容器的Environment指定生效的profile, 与@Profile的值相同
				if (context.getEnvironment().acceptsProfiles(Profiles.of((String[]) value))) {
					return true;
				}
			}
			return false;
		}
		return true;
	}
}
```

3. 上一小节的的示例中，`@Profile("even")` 表示生效的 profile 为 `even`时才加载该 bean

```java
@Bean(name = "number")
@Profile("even")    // 偶数
public Integer even() {
    return 2;
}
```

在加载 bean 时，会获取 bean 上的 @Conditional 注解，通过 @Profile 的源码可知，其就是 @Conditional(ProfileCondition.class)，然后调用 ProfileCondition#matches 方法，若返回 true，则加载该 bean，若返回false，则跳过该bean。

```java
// 加载方法形式创建的bean
private void loadBeanDefinitionsForBeanMethod(BeanMethod beanMethod) {
    ConfigurationClass configClass = beanMethod.getConfigurationClass();
    MethodMetadata metadata = beanMethod.getMetadata();
    String methodName = metadata.getMethodName();

    // 判断该bean是否需要跳过
    // 底层调用ProfileCondition#match, 若返回false则表示跳过, 见代码块3
    // 这里methodName为odd的bean会被跳过
    if (this.conditionEvaluator.shouldSkip(metadata, ConfigurationPhase.REGISTER_BEAN)) {
        configClass.skippedBeanMethods.add(methodName);
        return;
    }
    if (configClass.skippedBeanMethods.contains(methodName)) {
        return;
    }
    // ....
}
```





### 2. 自定义加载 bean 的规则

1. 实现 Condition 接口的 matches 方法，与 ProfileCondition 类似

```java
public class EvenProfileCondition implements Condition {
    /**
     * 如果当前激活的profile文件是even则返回true
     */
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        Environment environment = context.getEnvironment();
        return environment.acceptsProfiles("even");
    }
}
```

2. 定义 bean，当 `@Conditional(EvenProfileCondition.class)` 条件通过才加载 bean

```java
    @Bean(name = "number")
    // 与@Profile的作用相同, 
	// 若EvenProfileCondition#matches方法返回true才加载该bean
	@Conditional(EvenProfileCondition.class)
    public Integer even() {
        return 2;
    }
```

3. 创建应用上下文，设置激活的 profile，然后获取 bean

```java
public static void main(String[] args) {
    AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
    applicationContext.register(EvenProfileConditionDemo.class);

    // 为Environment设置激活的profile为 even
    ConfigurableEnvironment environment = applicationContext.getEnvironment();
    environment.setActiveProfiles("even");

    applicationContext.refresh();

    Integer number1 = applicationContext.getBean("number", Integer.class);
    System.out.println(number1);
}
```

输出结果，说明`@Conditional(EvenProfileCondition.class)`条件通过

```
2
```





## 19.5 依赖注入 Environment

- 直接依赖注入
  - 通过实现 EnvironmentAware 接口回调
  - 通过 @Autowired 注入 Environment
- 间接依赖注入
  - 通过 ApplicationContextAware 接口回调
  - 通过 @Autowired 注入 ApplicationContext



1. 直接依赖注入 Environment

```java
public class InjectingEnvironmentDemo implements EnvironmentAware {
    private Environment environment;

    @Autowired
    private Environment environment2;

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        applicationContext.register(InjectingEnvironmentDemo.class);

        applicationContext.refresh();

        InjectingEnvironmentDemo bean = applicationContext.getBean(InjectingEnvironmentDemo.class);
        System.out.println(bean.environment);
        System.out.println(bean.environment2);
        
        System.out.println(bean.environment == bean.environment2);
    }
}
```

输出结果，输出了 Environment 的 3 个重要属性，生效的 profiles，默认的 profiles，属性源，其中属性源包括环境变量 systemEnvironment，系统属性 systemProperties，以及自定义的配置文件。

```java
StandardEnvironment {activeProfiles=[], defaultProfiles=[default], propertySources=[PropertiesPropertySource {name='systemProperties'}, SystemEnvironmentPropertySource {name='systemEnvironment'}]}
StandardEnvironment {activeProfiles=[], defaultProfiles=[default], propertySources=[PropertiesPropertySource {name='systemProperties'}, SystemEnvironmentPropertySource {name='systemEnvironment'}]}
true
```



2. 间接依赖注入 Environment

```java
public class InjectingEnvironmentDemo2 implements ApplicationContextAware {
    private ApplicationContext applicationContext;

    @Autowired
    private ApplicationContext ApplicationContext2;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        applicationContext.register(InjectingEnvironmentDemo2.class);

        applicationContext.refresh();
        InjectingEnvironmentDemo2 bean = applicationContext.getBean(InjectingEnvironmentDemo2.class);
        System.out.println(bean.applicationContext.getEnvironment());
        System.out.println(bean.ApplicationContext2.getEnvironment());

        System.out.println(bean.applicationContext.getEnvironment() == bean.ApplicationContext2.getEnvironment());
    }
}
```

输出结果

```
StandardEnvironment {activeProfiles=[], defaultProfiles=[default], propertySources=[PropertiesPropertySource {name='systemProperties'}, SystemEnvironmentPropertySource {name='systemEnvironment'}]}
StandardEnvironment {activeProfiles=[], defaultProfiles=[default], propertySources=[PropertiesPropertySource {name='systemProperties'}, SystemEnvironmentPropertySource {name='systemEnvironment'}]}
true
```



3. 源码分析，在初始化 bean 时，会调用 ApplicationContextAwareProcessor 接口，回调 Aware 方法，源码如下所示，这也是为什么 EnvironmentAware 接口 和 ApplicationContextAware 接口都可以依赖注入 Environment 的原因。

```java
ApplicationContextAwareProcessor.java

private void invokeAwareInterfaces(Object bean) {
    // 回调EnvironmentAware#setEnvironment方法, 传入参数为this.applicationContext.getEnvironment()
    if (bean instanceof EnvironmentAware) {
        ((EnvironmentAware) bean).setEnvironment(this.applicationContext.getEnvironment());
    }
	// ......
    // 回调ApplicationContextAware#setApplicationContext方法, 传入参数为this.applicationContext
    if (bean instanceof ApplicationContextAware) {
        ((ApplicationContextAware) bean).setApplicationContext(this.applicationContext);
    }
}
```

Spring 容器在启动时，就向容器中注册了 ApplicationContext bean 和 Environment bean，这样使用 @Autowired 就可以依赖注入这两个 bean 了。

```java
protected void prepareBeanFactory(ConfigurableListableBeanFactory beanFactory) {

    // 注册bean ApplicationContext
    beanFactory.registerResolvableDependency(ApplicationContext.class, this);
    
    // .....

    // 注册bean environment
    if (!beanFactory.containsLocalBean(ENVIRONMENT_BEAN_NAME)) {
        beanFactory.registerSingleton(ENVIRONMENT_BEAN_NAME, getEnvironment());
    }
}
```







## 19.6 依赖查找 Environment

- 直接依赖查找 - ConfigurableApplicationContext#ENVIRONMENT_BEAN_NAME
- 简介依赖查找 - ConfigurableApplicationContext#getEnvironment

ConfigurableApplicationContext 规定了 Environment 相关的操作，与 ConfigurableEnvironment相对应，都表示与配置相关。



```java
public static void main(String[] args) {
    AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
    applicationContext.register(LookupEnvironment.class);

    applicationContext.refresh();
    // 1. 通过bean名称查找environment
    Environment environment = applicationContext.getBean(ConfigurableApplicationContext.ENVIRONMENT_BEAN_NAME, Environment.class);
    System.out.println(environment);

    // 2. 通过ConfigurableApplicationContext#getEnvironment获取environment
    ConfigurableEnvironment environment1 = applicationContext.getEnvironment();
    System.out.println(environment1);
    System.out.println(environment1 == environment);
}
```

输出结果

```
StandardEnvironment {activeProfiles=[], defaultProfiles=[default], propertySources=[PropertiesPropertySource {name='systemProperties'}, SystemEnvironmentPropertySource {name='systemEnvironment'}]}
StandardEnvironment {activeProfiles=[], defaultProfiles=[default], propertySources=[PropertiesPropertySource {name='systemProperties'}, SystemEnvironmentPropertySource {name='systemEnvironment'}]}
true
```







## 19.7 依赖注入 @Value

1. @Value 依赖注入的示例代码如下

```java
/**
 * 处理 @Value 的占位符
 * 1. 获取@Value注解的value值
 * 2. 解析value值中的占位符
 * 
 * @see AutowireCandidateResolver#getSuggestedValue(org.springframework.beans.factory.config.DependencyDescriptor)
 * @see AbstractBeanFactory#resolveEmbeddedValue(java.lang.String)
 */
public class ValueAnnotationDemo {

    @Value("${user.name}")
    private String userName;

    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        applicationContext.register(ValueAnnotationDemo.class);

        applicationContext.refresh();

        ValueAnnotationDemo bean = applicationContext.getBean(ValueAnnotationDemo.class);
        System.out.println(bean.userName);
    }
}
```

输出结果

```
mao
```



2. 源码分析，首先获取@Value注解的 value 值，这里为`"${user.name}"`，然后使用 Environment 根据配置源 PropertySource 解析占位符，填充对应的属性值

```java
QualifierAnnotationAutowireCandidateResolver.java

// 1.获取@Value注解的value值
public Object getSuggestedValue(DependencyDescriptor descriptor) {
    // 获取@Value注解的value值,这里指"${user.name}"
    Object value = findValue(descriptor.getAnnotations());
    
    // 前面处理@Value标记在字段上, 下面处理@Value标记在方法参数上的
    if (value == null) {
        MethodParameter methodParam = descriptor.getMethodParameter();
        if (methodParam != null) {
            value = findValue(methodParam.getMethodAnnotations());
        }
    }
    return value;
}

// 2.处理占位符
public String resolveEmbeddedValue(@Nullable String value) {
    if (value == null) {
        return null;
    }
    String result = value;
    for (StringValueResolver resolver : this.embeddedValueResolvers) {
        // 解析占位符, 交给Environment进行处理
        // strVal -> getEnvironment().resolvePlaceholders(strVal)
        // 见19.2章节
        result = resolver.resolveStringValue(result);
        if (result == null) {
            return null;
        }
    }
    // 返回解析后的值 mao
    return result;
}
```

QualifierAnnotationAutowireCandidateResolver 类不仅处理 @Qualifier 注解，也处理 @Value 注解。





## 19.8 Spring 类型转换在 Environment 中的运用



## 19.9 Spring 类型转换在 @Value 中的运用





## 19.10 Spring 配置属性源 PropertySource

- API - PropertySource，存储配置属性键值对
- 注解 - @PropertySource
- 关联 
  - 存储配置属性源 PropertySource  - MutablePropertySources
  - 关联方法 - ConfigurableEnvironment#getPropertySources



```java
public abstract class PropertySource<T> {

    // 属性源名称
	protected final String name;

    // 这里的source是k->v键值对,可能是Properties, Map类型
	protected final T source;
}
```

![xxx](https://cdn.jsdelivr.net/gh/maoturing/PictureBed@master/picx/xxx.5plx13kekcg.png)

- PropertiesPropertySource 存储系统属性 systemProperties
- SystemEnvironmentPropertySource 存储环境变量 systemEnvironment
- ResourcePropertySource 存储用户自定义配置文件中的属性，name 为 `class path resource[xxx.properties]`，source 为 配置文件中的键值对



MutablePropertySources 实现了 PropertySources 接口，其属性`propertySourceList`用来保存属性源的 List 集合。

```java
public class MutablePropertySources implements PropertySources {

	private final List<PropertySource<?>> propertySourceList = new CopyOnWriteArrayList<>();
}
```





1. @PropertySource 引入配置属性文件示例

```properties
stu.name=Hammond
```



```java
@Configuration  // 必须的
@PropertySource("classpath:/student.properties")
public class PropertySourceDemo {

    @Value("${stu.name}")
    private String stuName;

    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        applicationContext.register(PropertySourceDemo.class);

        applicationContext.refresh();
        PropertySourceDemo demo = applicationContext.getBean(PropertySourceDemo.class);
        System.out.println(demo.stuName);
    }
}
```

输出结果

```
Hammond
```

其中属性`stu.name=Hammond`就保存在应用上下文 applicationContext 的属性 environment中，environment 属性的类型为 StandardEnvironment，environment 下的 MutablePropertySources propertySources 属性负责存储各个属性源，从上面的源码可知，MutablePropertySources 下有一个 List<PropertySource<?>> propertySourceList 负责存储各个属性源，包括以下 3 个属性源 PropertySource：

1. name = systemProperties，source 中存储系统属性
2. name = systemEnvironment，source 中存储环境变量
3. name = class path resource[student.properties]，source 中存储配置文件 student.properties 中的键值对



2. 源码分析，Spring 框架启动时，ConfigurationClassParser#doProcessConfigurationClass 会处理 @Configuration 类，然后处理 @PropertySource 注解

```java
// 处理@Configuration 类,这也是为什么@PropertySource必须与@Configuration一起使用
protected final SourceClass doProcessConfigurationClass(ConfigurationClass configClass, SourceClass sourceClass)
    throws IOException {

    // 处理@PropertySource注解
    for (AnnotationAttributes propertySource : AnnotationConfigUtils.attributesForRepeatable(
        sourceClass.getMetadata(), PropertySources.class,
        org.springframework.context.annotation.PropertySource.class)) {
        
        if (this.environment instanceof ConfigurableEnvironment) {
            // 见下个方法, 处理@PropertySource注解
            processPropertySource(propertySource);
        }
        else {
            logger.info("Ignoring @PropertySource annotation on [" + sourceClass.getMetadata().getClassName() +
                        "]. Reason: Environment must implement ConfigurableEnvironment");
        }
    }
```

首先解析 @PropertySource 配置的各个属性，包括 name，encoding，value。然后加载配置属性文件，保存到 propertySources 中。

```java
private void processPropertySource(AnnotationAttributes propertySource) throws IOException {
    // 获取@PropertySource的name属性, 即属性源名称
    String name = propertySource.getString("name");
    if (!StringUtils.hasLength(name)) {
        name = null;
    }
    // 获取@PropertySource的encoding属性
    String encoding = propertySource.getString("encoding");
    if (!StringUtils.hasLength(encoding)) {
        encoding = null;
    }
    // 获取@PropertySource的value属性, 即配置文件的路径
    String[] locations = propertySource.getStringArray("value");
    Assert.isTrue(locations.length > 0, "At least one @PropertySource(value) location is required");
    boolean ignoreResourceNotFound = propertySource.getBoolean("ignoreResourceNotFound");

    Class<? extends PropertySourceFactory> factoryClass = propertySource.getClass("factory");
    PropertySourceFactory factory = (factoryClass == PropertySourceFactory.class ?
                                     DEFAULT_PROPERTY_SOURCE_FACTORY : BeanUtils.instantiateClass(factoryClass));

    // 遍历配置文件
    for (String location : locations) {
        try {
            // 配置文件路径可能存在占位符, 需要解析
            String resolvedLocation = this.environment.resolveRequiredPlaceholders(location);
            // 获得配置文件源
            Resource resource = this.resourceLoader.getResource(resolvedLocation);
            // 将配置文件源, 按照指定编码封装为EncodedResource, 然后使用工厂创建一个ResourcePropertySource
            // 并将配置属性源ResourcePropertySource保存到environment.propertySources中
            addPropertySource(factory.createPropertySource(name, new EncodedResource(resource, encoding)));
        }catch (IllegalArgumentException | FileNotFoundException | UnknownHostException ex) {
        }
    }
}
```









## 19.11 Spring 内建配置属性源

![xxx](https://cdn.jsdelivr.net/gh/maoturing/PictureBed@master/picx/xxx.2wu1jgp0zki0.png)



为什么默认只有 systemProperties 系统属性 和 systemEnvironment 环境变量，设置了命令行参数，Environment 中也没有 CommandLinePropertySource 配置属性源，为什么？







## 19.12 基于注解扩展 Spring 属性配置源





## 19.13 基于 API 扩展 Spring 属性配置源



## 19.14



## 19.15 面试题



