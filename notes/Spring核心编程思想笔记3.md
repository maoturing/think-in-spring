# ![image](https://cdn.jsdelivr.net/gh/maoturing/PictureBed@master/picx/image.16j89oe7q68w.png)8. Bean 作用域 Scopes

## 8.1 Spring Bean 作用域

| 类型        | 说明                                                       |
| ----------- | ---------------------------------------------------------- |
| singleton   | 默认 Spring Bean 作用域，一个 BeanFactory 有且仅有一个实例 |
| prototype   | 原型作用域，每次依赖查找和依赖注入生成新的 Bean 对象       |
| request     | 将 Spring Bean 存储在 ServletRequest 上下文中              |
| session     | 将 Spring Bean 存储在 HttpSession 中                       |
| application | 将 Spring Bean 存储在 ServletContext 中                    |



```java
public interface ConfigurableBeanFactory extends HierarchicalBeanFactory, SingletonBeanRegistry {

	String SCOPE_SINGLETON = "singleton";

	String SCOPE_PROTOTYPE = "prototype";
```

```java
@Component
@Scope("prototype")
public class Toy {
    
}
```





## 8.2 singleton Bean 作用域

SpringFramework 官方文档中有一张图，解释了单实例 Bean 的概念：

![img](https://p9-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/f1a1548eb64b49c797bc0155c43512bc~tplv-k3u1fbpfcp-zoom-1.image)

左边的几个定义的 Bean 同时引用了右边的同一个 `accountDao` ，对于这个 `accountDao` 就是单实例 Bean 。



## 1. 使用 singleton

SpringFramework 中默认所有的 Bean 都是单实例的，即：**一个 IOC 容器中只有一个**。下面咱演示一下单实例 Bean 的效果：

1. 创建Bean+配置类

咱使用注解驱动式演示，先创建 `Child` 和 `Toy` ，本案例演示中 `Toy` 不再是抽象类，直接定义为普通类即可。

```java
public class Child {
    
    private Toy toy;
    
    public void setToy(Toy toy) {
        this.toy = toy;
    }
}
```

```java
// Toy 中标注@Component注解
@Component
// @Scope("singleton")
public class Toy {
    
}
```



接下来创建配置类，同时注册两个 `Child` ，代表现在有两个小孩：

```java
@Configuration
@ComponentScan("com.linkedbear.spring.bean.b_scope.bean")
public class BeanScopeConfiguration {
    
    @Bean
    public Child child1(Toy toy) {
        Child child = new Child();
        child.setToy(toy);
        return child;
    }
    
    @Bean
    public Child child2(Toy toy) {
        Child child = new Child();
        child.setToy(toy);
        return child;
    }
    
}
```

2. 测试运行

编写启动类，驱动 IOC 容器，并获取其中的 `Child` ，打印里面的 `Toy` ：

```java
public class BeanScopeAnnoApplication {
    
    public static void main(String[] args) throws Exception {
        ApplicationContext ctx = new AnnotationConfigApplicationContext(BeanScopeConfiguration.class);
        ctx.getBeansOfType(Child.class).forEach((name, child) -> {
            System.out.println(name + " : " + child);
        });
    }
    
}
```

运行 `main` 方法，控制台中打印了两个 `Child` 持有同一个 `Toy` ：

```
child1 : Child{toy=com.linkedbear.spring.bean.b_scope.bean.Toy@971d0d8}
child2 : Child{toy=com.linkedbear.spring.bean.b_scope.bean.Toy@971d0d8}
```

说明**默认情况下，Bean 的作用域是单实例的**。





## 8.3 prototype Bean 作用域

Spring 官方的定义是：**每次对原型 Bean 提出请求时，都会创建一个新的 Bean 实例。**这里面提到的 ”提出请求“ ，包括任何依赖查找、依赖注入的动作，都算做一次 ”提出请求“ 。由此咱也可以总结一点：如果连续 `getBean()` 两次，那就应该创建两个不同的 Bean 实例；向两个不同的 Bean 中注入两次，也应该注入两个不同的 Bean 实例。SpringFramework 的官方文档中也给出了一张解释原型 Bean 的图：

![img](https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/c497ccbeac5e49eba21839c8c2f08a1a~tplv-k3u1fbpfcp-zoom-1.image)

图中的 3 个 `accountDao` 是 3 个不同的对象，由此可以体现出原型 Bean 的意思。

> 其实对于**原型**这个概念，在设计模式中也是有对应的：**原型模式**。原型模式实质上是使用对象深克隆，乍看上去跟 SpringFramework 的原型 Bean 没什么区别，但咱仔细想，每一次生成的原型 Bean 本质上都还是一样的，只是可能带一些特殊的状态等等，这个可能理解起来比较抽象，可以跟下面的 request 域结合着理解。



### 1. 使用 prototype 

下面咱也实际测试一下效果，体会原型 Bean 的使用。

1. 修改Bean

给 `Toy` 的类上标注一个额外的注解：`@Scope` ，并声明为原型类型：

```java
@Component
@Scope("prototype")
public class Toy {
    
}
```

注意，这个 **prototype** 不是随便写的常量，而是在 `ConfigurableBeanFactory` 中定义好的常量：

```java
public interface ConfigurableBeanFactory extends HierarchicalBeanFactory, SingletonBeanRegistry {

	String SCOPE_SINGLETON = "singleton";

	String SCOPE_PROTOTYPE = "prototype";
```

如果真的担心打错，建议引用该常量 ￣へ￣ 。。。

2. 测试运行

其他的代码都不需要改变，直接运行 `main` 方法，发现控制台打印的两个 Toy 确实不同：

```
child1 : Child{toy=com.linkedbear.spring.bean.b_scope.bean.Toy@18a70f16}
child2 : Child{toy=com.linkedbear.spring.bean.b_scope.bean.Toy@62e136d3}
```



### 2. 原型Bean的创建时机

仔细思考一下，单实例 Bean 的创建咱已经知道，是在 `ApplicationContext` 被初始化时就已经创建好了，那这些原型 Bean 又是什么时候被创建的呢？其实也不难想出，它都是什么时候需要，什么时候创建。咱可以给 `Toy` 加一个无参构造方法，打印构造方法被打印了：

```java
@Component
@Scope("prototype")
public class Toy {
    public Toy() {
        System.out.println("Toy constructor run ...");
    }
}
```

修改启动类，只让它扫描 bean 包，不加载配置类，这样就相当于只有一个 Toy 类被扫描进去了，Child 不会注册到 IOC 容器中。

```java
    public static void main(String[] args) throws Exception {
        ApplicationContext ctx = new AnnotationConfigApplicationContext("com.linkedbear.spring.bean.b_scope.bean");
    }
```

重新运行 `main` 方法，发现控制台什么也没打印，因为没有 `Toy` 的使用需求嘛，它当然不会被创建。







下面这 3 个可有可无

## 8.4 request Bean 作用域

## 8.5 session Bean 作用域

## 8.6 application Bean 作用域

## 8.7 自定义 Bean 作用域

## 8.8 面试题



# 9. Bean 生命周期 Lifecycle (重点)



> 什么是 Bean 的生命周期？

就是 Bean 从出生到死亡的全过程



// 补充: 这一章是重中之重, 也是难点, 补充流程图等





## 9.1 Bean 元信息配置阶段

BeanDefinition 配置的方式：

- 面向资源
  - xml 配置
  - properties 配置
- 面向注解 @Bean
- 面向 api

![image](https://cdn.jsdelivr.net/gh/maoturing/PictureBed@master/picx/image.4xs9vzojmt80.png)





配置 bean 的几种方式在前面章节已经非常熟悉了，这里只介绍 properties 配置 bean 的方式

1. 在 properties 文件中配置 bean 的元信息

```properties
# user.properties

user.(class)=org.geekbang.dependency.injection.type.Docter
user.id=11
user.name=黑百合
user.city=HANGZHOU
```

2. 使用 PropertiesBeanDefinitionReader 解析加载 bean 

```java
public static void main(String[] args) {
    DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
    PropertiesBeanDefinitionReader beanDefinitionReader = new PropertiesBeanDefinitionReader(beanFactory);
    String location = "docter.properties";
    // 创建资源
    Resource resource = new ClassPathResource(location);

    // 加载 properties 中的bean配置
    int count = beanDefinitionReader.loadBeanDefinitions(resource);

    System.out.println("已加载BeanDefinition数量: " + count);
    Docter docter = beanFactory.getBean(Docter.class);
    System.out.println(docter);
}
```

输出结果：

```
已加载BeanDefinition数量: 1
Docter{id=11, name='黑百合', city=HANGZHOU, resource=null}
```



## 9.2 Bean 元信息解析

Bean 的元信息就是 BeanDefinition，

- 面向资源 BeanDefinition 解析，核心是 BeanDefinitionReader 接口，其有 2 个实现类
  - xml 配置解析，XmlBeanDefinitionReader 解析得到 GenericBeanDefinition
  - properties 配置解析，PropertiesBeanDefinitionReader
- 面向注解 BeanDefinition 解析，核心是  AnnotatedBeanDefinitionReader 类



![image](https://cdn.jsdelivr.net/gh/maoturing/PictureBed@master/picx/image.bfhw50ix7iw.png)

![image](https://cdn.jsdelivr.net/gh/maoturing/PictureBed@master/picx/image.1cs0tt0izffk.png)

AnnotatedBeanDefinitionReader 与 BeanDefinitionReader 接口没有继承关系，因为前者不需要加载资源`loadBeanDefinitions()`



加载 xml 配置的 bean  包含两个阶段：

1. 解析 xml
2. 将 bean 配置解析为 BeanDefinition
3. 注册 BeanDefinition

```java
XmlBeanDefinitionReader.java

protected int doLoadBeanDefinitions(InputSource inputSource, Resource resource)
			throws BeanDefinitionStoreException {
    try {
        // 1.解析xml
        Document doc = doLoadDocument(inputSource, resource);
        // 2.将doc解析为BeanDefinition, 
        // 3.然后注册BeanDefinition
        // 见下方代码块
        int count = registerBeanDefinitions(doc, resource);
        
        if (logger.isDebugEnabled()) {
            logger.debug("Loaded " + count + " bean definitions from " + resource);
        }
        return count;
    }
    catch (BeanDefinitionStoreException ex) {
        // bean的配置格式有问题
    }
    catch (SAXParseException ex) {
        // xml 解析有问题
    }
    catch (IOException ex) {
		// xml 找不到
    }
}
```

解析`<bean>`标签

```java
protected void processBeanDefinition(Element ele, BeanDefinitionParserDelegate delegate) {
    // 2. 将xml中bean配置解析为BeanDefinition, 保存到BeanDefinitionHolder中
    BeanDefinitionHolder bdHolder = delegate.parseBeanDefinitionElement(ele);
    if (bdHolder != null) {
        // 解析<bean>中嵌套的自定义标签
        bdHolder = delegate.decorateBeanDefinitionIfRequired(ele, bdHolder);
        try {
            // 3. 见代码块2,注册BeanDefinition
            BeanDefinitionReaderUtils.registerBeanDefinition(bdHolder, getReaderContext().getRegistry());
        }
        catch (BeanDefinitionStoreException ex) {
            getReaderContext().error("Failed to register bean definition with name '" +
                                     bdHolder.getBeanName() + "'", ele, ex);
        }
        getReaderContext().fireComponentRegistered(new BeanComponentDefinition(bdHolder));
    }
}
```





1. 注解类型配置的 bean 示例

```java
public class AnnotatedBeanDefintionParsingDemo {
    public static void main(String[] args) {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();

        // 用来解析注解类型配置的bean
        AnnotatedBeanDefinitionReader beanDefinitionReader = new AnnotatedBeanDefinitionReader(beanFactory);

        int countBefore = beanFactory.getBeanDefinitionCount();
        // 解析bean, 注册bean, 见代码块1
        beanDefinitionReader.register(AnnotatedBeanDefintionParsingDemo.class);
        int countAfter = beanFactory.getBeanDefinitionCount();
        System.out.println("加载的bean数量: " + (countAfter - countBefore));
    }
}
```



与下面的代码作用类似，因为 AnnotationConfigApplicationContext 中有两个字段 AnnotatedBeanDefinitionReader 和 DefaultListableBeanFactory，前者负责注册 bean，后者负责容器管理。

```java
AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
applicationContext.register(AnnotationDependencyInjectionResolutionDemo.class);
```



加载注解配置的 bean  包含两个阶段：

1. 解析 bean 得到 BeanDefinition
2. 注册 BeanDefinition 到容器

源码分析，注解类型配置的 bean，会解析 bean 配置元信息得到 BeanDefinition，封装为 BeanDefinitionHolder，最后注册到容器

```java
// 代码块1
AnnotatedBeanDefinitionReader.java

private <T> void doRegisterBean(Class<T> beanClass, @Nullable String name,
                                @Nullable Class<? extends Annotation>[] qualifiers, @Nullable Supplier<T> supplier,
                                @Nullable BeanDefinitionCustomizer[] customizers) {
	// 1.解析bean配置得到BeanDefinition
    // 生成BeanDefinition, 设置beanClass
    AnnotatedGenericBeanDefinition abd = new AnnotatedGenericBeanDefinition(beanClass);
    // 处理@Conditional,若条件不满足,则跳过该bean
    if (this.conditionEvaluator.shouldSkip(abd.getMetadata())) {
        return;
    }
	// 设置回调, 这里为null
    abd.setInstanceSupplier(supplier);
    // 解析并设置scope
    ScopeMetadata scopeMetadata = this.scopeMetadataResolver.resolveScopeMetadata(abd);
    abd.setScope(scopeMetadata.getScopeName());
    
    // 若未设置bean名称, 自动生成
    String beanName = (name != null ? name : this.beanNameGenerator.generateBeanName(abd, this.registry));

    // 解析@Lazy, @Primary, @Value, @DependsOn, @Description, 保存到BeanDefinition
    AnnotationConfigUtils.processCommonDefinitionAnnotations(abd);
    
    // 使用spring api注册bean时,可以在方法参数中手动设置 Lazy,Primary等, 这里跳过
    if (qualifiers != null) {
        for (Class<? extends Annotation> qualifier : qualifiers) {
            if (Primary.class == qualifier) {
                abd.setPrimary(true);
            }
            else if (Lazy.class == qualifier) {
                abd.setLazyInit(true);
            }
            else {
                abd.addQualifier(new AutowireCandidateQualifier(qualifier));
            }
        }
    }
    // 跳过
    if (customizers != null) {
        for (BeanDefinitionCustomizer customizer : customizers) {
            customizer.customize(abd);
        }
    }
	// 封装BeanDefinition到BeanDefinitionHolder
    BeanDefinitionHolder definitionHolder = new BeanDefinitionHolder(abd, beanName);
    // 设置代理模式,若是类则Cglib,若是接口则动态代理,或者不使用代理
    definitionHolder = AnnotationConfigUtils.applyScopedProxyMode(scopeMetadata, definitionHolder, this.registry);
    
    // 2.注册BeanDefinition到容器, 见下一小节代码块2
    BeanDefinitionReaderUtils.registerBeanDefinition(definitionHolder, this.registry);
}
```





## 9.3 Bean 注册

- Bean 注册接口 BeanDefinitionRegistry#registerBeanDefinition
- Bean 注册实现 DefaultListableBeanFactory#registerBeanDefinition



```java
// 代码块2
BeanDefinitionReaderUtils.java

public static void registerBeanDefinition(
    BeanDefinitionHolder definitionHolder, BeanDefinitionRegistry registry)
    throws BeanDefinitionStoreException {

    String beanName = definitionHolder.getBeanName();
    // 见代码块3, 注册BeanDefinition到容器
    registry.registerBeanDefinition(beanName, definitionHolder.getBeanDefinition());

	// 注册bean的别名到容器, 保存到aliasMap属性中
    String[] aliases = definitionHolder.getAliases();
    if (aliases != null) {
        for (String alias : aliases) {
            registry.registerAlias(beanName, alias);
        }
    }
}
```

BeanDefinitionRegistry 接口规范了 BeanDefinition 的注册，删除，查找等操作，源码如下所示：

```java
public interface BeanDefinitionRegistry extends AliasRegistry {
	// 注册BeanDefinition
	void registerBeanDefinition(String beanName, BeanDefinition beanDefinition)
			throws BeanDefinitionStoreException;
	// 删除BeanDefinition
	void removeBeanDefinition(String beanName) throws NoSuchBeanDefinitionException;
	// 通过bean名称查找BeanDefinition
	BeanDefinition getBeanDefinition(String beanName) throws NoSuchBeanDefinitionException;
	// 通过bean名称判断容器中是否已经存在该bean
	boolean containsBeanDefinition(String beanName);
	// 获取所有BeanDefinition的名称
	String[] getBeanDefinitionNames();
	// 获取容器中BeanDefinition的数量
	int getBeanDefinitionCount();

	boolean isBeanNameInUse(String beanName);
}
```

DefaultListableBeanFactory 实现了 BeanDefinitionRegistry 接口，也实现了注册 BeanDefinition 的方法 registerBeanDefinition。

注册 BeanDefinition 到容器，都要经过该方法，无论是 xml，注解，api注册，还是 Spring 内建BeanDefinition等

```java
// 代码块3  
DefaultListableBeanFactory.java, 

// 保存BeanDefinition
private final Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>(256);
// 保存bean名称, 有序
private volatile List<String> beanDefinitionNames = new ArrayList<>(256);

// 实现了BeanDefinitionRegistry接口registerBeanDefinition方法
@Override
public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition)
    throws BeanDefinitionStoreException {

    Assert.hasText(beanName, "Bean name must not be empty");
    Assert.notNull(beanDefinition, "BeanDefinition must not be null");

    // 校验bean
    if (beanDefinition instanceof AbstractBeanDefinition) {
        try {
            ((AbstractBeanDefinition) beanDefinition).validate();
        }
        catch (BeanDefinitionValidationException ex) {
            throw new BeanDefinitionStoreException(beanDefinition.getResourceDescription(), beanName,
                                                   "Validation of bean definition failed", ex);
        }
    }

    // 查找bean名称是否已被占用
    BeanDefinition existingDefinition = this.beanDefinitionMap.get(beanName);
    if (existingDefinition != null) {
        // 默认允许同名bean覆盖, Springboot2.1中修改不允许
        if (!isAllowBeanDefinitionOverriding()) {
            // 不允许同名bean覆盖, 抛出异常
            throw new BeanDefinitionOverrideException(beanName, beanDefinition, existingDefinition);
        }else {
            // 打印相关日志
        }
		// bean名称已被占用, 会使用当前bean进行覆盖
        this.beanDefinitionMap.put(beanName, beanDefinition);
    } else {
        // bean的创建是否已经开始
        if (hasBeanCreationStarted()) {
            synchronized (this.beanDefinitionMap) {
                // 将BeanDefinition注册到容器,即保存到beanDefinitionMap
                this.beanDefinitionMap.put(beanName, beanDefinition);
                List<String> updatedDefinitions = new ArrayList<>(this.beanDefinitionNames.size() + 1);
                updatedDefinitions.addAll(this.beanDefinitionNames);
                // 添加bean的名称
                updatedDefinitions.add(beanName);
                // beanDefinitionNames是ArrayList,保证bean初始化的顺序与声明顺序相同
                this.beanDefinitionNames = updatedDefinitions;
                removeManualSingletonName(beanName);
            }
        } else {
            // 将BeanDefinition注册到容器,即保存到beanDefinitionMap
            this.beanDefinitionMap.put(beanName, beanDefinition);
            // 将bean名称保存到beanDefinitionNames
            this.beanDefinitionNames.add(beanName);
            removeManualSingletonName(beanName);
        }
        
        this.frozenBeanDefinitionNames = null;
    }

    if (existingDefinition != null || containsSingleton(beanName)) {
        resetBeanDefinition(beanName);
    }
}
```



## 9.4 BeanDefinition 合并

一个 bean 的 BeanDefinition 不仅包括自身的，还包括父 bean 的 BeanDefinition，所以需要将父 bean 的元信息合并进来，故注册子 BeanDefinition 之前要依赖查找父 BeanDefinition

- 在当前容器 BeanFactory 中查找
- 去父容器 BeanFactory 层次性查找
- 合并接口 ConfigurableBeanFactory#getMergedBeanDefinition
- 具体实现 AbstractBeanFactory#getMergedBeanDefinition(java.lang.String)，DefaultListableBeanFactory 继承了该类，调用这些方法时，本质还是他在调用。



1. 配置父子 bean

在 xml 配置 bean 时可以使用`parent`属性指定父 bean

```xml
<bean id="user" class="org.geekbang.ioc.overview.lookup.domain.User">
    <property name="id" value="1"/>
    <property name="name" value="tracccer"/>
</bean>

<bean  class="org.geekbang.ioc.overview.lookup.domain.SuperUser" parent="user" primary="true">
    <property name="address" value="杭州"/>
</bean>
```

在注解配置 bean 时，java 代码中的继承 extend 也相当于指定了父 bean



2. GenericBeanDefinition 表示 Bean 的元信息，但是不包括 Bean parent 中的元信息。上面的两个 Bean user, superUser，开始都会被解析为 GenericBeanDefinition，只不过后者设置了 parent

```java
public class GenericBeanDefinition extends AbstractBeanDefinition {

	private String parentName;

    @Override
    public void setParentName(@Nullable String parentName) {
        this.parentName = parentName;
    }
}
```



RootBeanDefinition 表示一个 Bean 完整的元信息，包括 parent bean 中的信息。

user 在经过 MergedBeanDefinition 操作后，直接将 GenericBeanDefinition 修改为 RootBeanDefinition

superUser 在经过 MergedBeanDefinition 操作后，会将其与 parent 中的元信息 BeanDefinition 合并，然后得到 RootBeanDefinition

```java
public class RootBeanDefinition extends AbstractBeanDefinition {
    public void setParentName(@Nullable String parentName) {
        if (parentName != null) {
            throw new IllegalArgumentException("Root bean cannot be changed into a child bean with parent reference");
        }
    }
}
```



3. 获取 bean 的完整 BeanDefinition，即合并后的元信息 MergedBeanDefinition

```java
// 代码块4
AbstractBeanFactory.java

public BeanDefinition getMergedBeanDefinition(String name) throws BeansException {
    String beanName = transformedBeanName(name);

    // 当前容器不存在beanName, 且存在父容器
    if (!containsBeanDefinition(beanName) && getParentBeanFactory() instanceof ConfigurableBeanFactory) {
        // 递归调用getMergedBeanDefinition, 去父容器查找
        return ((ConfigurableBeanFactory) getParentBeanFactory()).getMergedBeanDefinition(beanName);
    }
    // 从本地容器查找， 见代码块5
    return getMergedLocalBeanDefinition(beanName);
}

```

去缓存中查找 bean 的合并后元信息 mergedBeanDefinition，如果找不到，则去本地容器获取 bean 合并后的元信息。

```java
// 代码块5

protected RootBeanDefinition getMergedLocalBeanDefinition(String beanName) throws BeansException {
    // 从本地容器缓存中查找bean
    RootBeanDefinition mbd = this.mergedBeanDefinitions.get(beanName);
    if (mbd != null && !mbd.stale) {
        // 找到了目标BeanDefinition返回
        return mbd;
    }
    // 见代码块6, 
    // 根据bean名称,bean的普通元信息GenericBeanDefinition获取完整元信息
    return getMergedBeanDefinition(beanName, getBeanDefinition(beanName));
}

// 去容器查找bean的普通元信息
public BeanDefinition getBeanDefinition(String beanName) throws NoSuchBeanDefinitionException {
    BeanDefinition bd = this.beanDefinitionMap.get(beanName);
    if (bd == null) {
        throw new NoSuchBeanDefinitionException(beanName);
    }
    return bd;
}
```

根据 bean 的名称`beanName`和 bean 的普通元信息`GenericBeanDefinition`，获取合并后的完整元信息`RootBeanDefinition`，源码如下所示，

- 如果是获取 user 的完整元信息，这里的操作很简单，就是将其普通的元信息 GenericBeanDefinition 封装为 RootBeanDefinition 返回。
- 如果是获取 superUser 的完整元信息，会获取 parent bean，然后将二者的 BeanDefinition 合并为 RootBeanDefinition 并返回

```java
// 代码块6
// beanName bean名称
// bd  bean的普通元信息,包含parent属性
// containingBd  表示嵌套bean, 这里为null
// return mbd, 合并后的完整元信息
protected RootBeanDefinition getMergedBeanDefinition(
    String beanName, BeanDefinition bd, BeanDefinition containingBd)
    throws BeanDefinitionStoreException {

    synchronized (this.mergedBeanDefinitions) {
        RootBeanDefinition mbd = null;
        RootBeanDefinition previous = null;

        // 如果不是嵌套bean, 从缓存中再次检查bean是否存在
        if (containingBd == null) {
            mbd = this.mergedBeanDefinitions.get(beanName);
        }

        // 当前容器缓存中未找到bean
        if (mbd == null || mbd.stale) {
            previous = mbd;
            // 1.bean没有parent, user会进入这里
            if (bd.getParentName() == null) {
                // 如果是RootBeanDefinition,没有parent,不需要merge
                if (bd instanceof RootBeanDefinition) {
                    // 复制一份RootBeanDefinition返回
                    mbd = ((RootBeanDefinition) bd).cloneBeanDefinition();
                }
                else {
                    // 将GenericBeanDefinition封装为RootBeanDefinition返回
                    mbd = new RootBeanDefinition(bd);
                }
            } else {
                // 2.bean有parent, SuperUser会进入这里
                // 见代码块7
            }

            // 如果scope为空, 默认设置为SINGLETON
            if (!StringUtils.hasLength(mbd.getScope())) {
                mbd.setScope(SCOPE_SINGLETON);
            }
			// 跳过
            if (containingBd != null && !containingBd.isSingleton() && mbd.isSingleton()) {
                mbd.setScope(containingBd.getScope());
            }

            // 非嵌套bean为null, 会进入
            // 将user缓存到mergedBeanDefinitions,供superUser获取父bean时查找
            if (containingBd == null && isCacheBeanMetadata()) {
                this.mergedBeanDefinitions.put(beanName, mbd);
            }
        }
        if (previous != null) {
            copyRelevantMergedBeanDefinitionCaches(previous, mbd);
        }
        return mbd;
    }
}

```

这里是上一个方法的截取，主要是处理 bean 存在 parent 的情况，会去缓存 mergedBeanDefinitions 中查找 parent，然后将 parent 元信息与当前 bean 的元信息合并为 RootBeanDefinition 并返回。

```java
// 代码块7
else {
    // 2.bean有parent, SuperUser会进入这里

    BeanDefinition pbd;	// 保存parent BeanDefinition
    try {
        // 获取parent bean名称, 这里为user
        String parentBeanName = transformedBeanName(bd.getParentName());

        // bean的parent与自己同名
        if (!beanName.equals(parentBeanName)) {
            // 获取parent BeanDefinition, 这里是user
            // 调用代码块4,然后调用代码块5,会从缓存中找到user
            pbd = getMergedBeanDefinition(parentBeanName);
        } else {
            // bean的parent与自己同名,说明父bean可能在父容器中
            BeanFactory parent = getParentBeanFactory();
            if (parent instanceof ConfigurableBeanFactory) {
                // 存在父容器, 去父容器合并BeanDefinition
                pbd = ((ConfigurableBeanFactory) parent).getMergedBeanDefinition(parentBeanName);
            } else {
                // 不存在父容器,说明找不到parent bean,打印日志
                throw new NoSuchBeanDefinitionException(parentBeanName,
                                                        "Parent name '" + parentBeanName + "' is equal to bean name '" + beanName +
                                                        "': cannot be resolved without an AbstractBeanFactory parent");
            }
        }
    } catch (NoSuchBeanDefinitionException ex) {
        // 找不到parent bean
        throw new BeanDefinitionStoreException(bd.getResourceDescription(), beanName,
                                               "Could not resolve parent bean definition '" + bd.getParentName() + "'", ex);
    }
    
    // 使用parent BeanDefinition构造一个RootBeanDefinition
    mbd = new RootBeanDefinition(pbd);
    // 使用子BeanDefinition覆盖父BeanDefinition中相关属性
    // (合并)这里bd是superUser,mbd是user,将二者信息合并
    mbd.overrideFrom(bd);
}
```



下面是一个 xml 配置的嵌套 bean，Shop 类有一个属性 User，将 user 直接声明在字段中，称为嵌套 bean，嵌套的 bean 仅仅作为 setter 注入的参数，无法被容器访问。

```xml
<bean id="shop" class="com.geek.Shop">
    <property name="user">
        <bean class="org.geekbang.ioc.domain.User" />
    </property>
</bean>
```

与上面的配置几乎等价。

```xml
<bean id="user" class="org.geekbang.ioc.domain.User" />

<bean id="shop" class="com.geek.Shop">
    <property name="user" ref="user" />
</bean>
```





## 9.5 Bean Class 加载

- ClassLoader 类加载
- Java Security 安全控制
- ConfigurableBeanFactory 临时 ClassLoader   （不重要）

```java
// 代码块8
AbstractBeanFactory.java

private Class<?> doResolveBeanClass(RootBeanDefinition mbd, Class<?>... typesToMatch)
    throws ClassNotFoundException {

    ClassLoader beanClassLoader = getBeanClassLoader();
    ClassLoader dynamicLoader = beanClassLoader;
    boolean freshResolve = false;

    // 跳过
    if (!ObjectUtils.isEmpty(typesToMatch)) {}
	// 得到bean的class
    String className = mbd.getBeanClassName();
    if (className != null) {
        Object evaluated = evaluateBeanDefinitionString(className, mbd);
        // 跳过
        if (!className.equals(evaluated)) {}
        
        // 跳过
        if (freshResolve) {}
    }

    // 代码块9, 加载bean的class
    return mbd.resolveBeanClass(beanClassLoader);
}
```



```java
// 代码块9
AbstractBeanDefinition.java

public Class<?> resolveBeanClass(@Nullable ClassLoader classLoader) throws ClassNotFoundException {
    String className = getBeanClassName();
    if (className == null) {
        return null;
    }
    // 加载bean的class文件, 底层是Class.forName()
    Class<?> resolvedClass = ClassUtils.forName(className, classLoader);
    this.beanClass = resolvedClass;
    return resolvedClass;
}
```



// 这一节没看太懂







##  9.6 Bean 实例化前

- 接口 InstantiationAwareBeanPostProcessor#postProcessBeforeInstantiation





1. 自定义实例化前的回调方法，实现 InstantiationAwareBeanPostProcessor 接口，重写 postProcessBeforeInstantiation 方法

```java
static class MyInstantiationAwareBeanPostProcessor implements InstantiationAwareBeanPostProcessor {
    /**
    * 当 user 在实例化时, 手动创建一个user对象并返回
    * 返回为null表示使用spring容器实例化bean
    */
    @Override
    public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException {
        if ("user".equals(beanName)) {
            System.out.println(beanName + " bean 实例化前回调...");
            User user = new User();
            user.setName("widowmaker");

            return user;
        }
        return null;
    }
}
```

2. 注册，会将这个 BeanPostProcessor 注册保存到容器的`beanPostProcessors`属性中

```java
public static void main(String[] args) {
    // 创建容器并加载xml中的BeanDefinition
    DefaultListableBeanFactory beanFactory = getBeanFactory();

    // 1. 注册自定义的BeanPostProcessor, 保存到容器的beanPostProcessors属性
    beanFactory.addBeanPostProcessor(new MyInstantiationAwareBeanPostProcessor());
    // 2. 依赖查找, 会进行实例化
    User user = beanFactory.getBean("user", User.class);
    // 3. 期望输出widowmaker, 因为在实例化前我们将bean替换了
    System.out.println(user);
}
```

输出结果

```
user bean 实例化前回调...
User{id=null, name='widowmaker'}
```



3. 源码分析，这是在 bean 实例化前，回调所有实现了 InstantiationAwareBeanPostProcessor 接口的回调方法

```java
// 代码块10
protected Object applyBeanPostProcessorsBeforeInstantiation(Class<?> beanClass, String beanName) {
    // 从容器的beanPostProcessors属性获取所有
    for (BeanPostProcessor bp : getBeanPostProcessors()) {
        // 处理InstantiationAwareBeanPostProcessor接口实现类
        if (bp instanceof InstantiationAwareBeanPostProcessor) {
            InstantiationAwareBeanPostProcessor ibp = (InstantiationAwareBeanPostProcessor) bp;
            // 回调postProcessBeforeInstantiation方法
            Object result = ibp.postProcessBeforeInstantiation(beanClass, beanName);
            
            // 如果不为空, 会将result作为bean返回
            if (result != null) {
                return result;
            }
        }
    }
    // 返回null表示使用spring容器对bean进行实例化
    return null;
}
```

4. 创建 bean 分为 3 个步骤，
   1. 加载 bean class
   2. 回调实例化前的处理程序 Postprocessor
   3. 实例化 bean

```java
// 代码块11
protected Object createBean(String beanName, RootBeanDefinition mbd, Object[] args)
    throws BeanCreationException {


    RootBeanDefinition mbdToUse = mbd;
	// 1.加载类, 见代码块8
    Class<?> resolvedClass = resolveBeanClass(mbd, beanName);
    
    // 若mbd中没有设置bean的class, 这里进行设置
    if (resolvedClass != null && !mbd.hasBeanClass() && mbd.getBeanClassName() != null) {
        mbdToUse = new RootBeanDefinition(mbd);
        mbdToUse.setBeanClass(resolvedClass);
    }

    mbdToUse.prepareMethodOverrides();


    // 2.代码块10,处理bean实例化前的操作
    Object bean = resolveBeforeInstantiation(beanName, mbdToUse);
    if (bean != null) {
        return bean;
    }

    // 3.bean 实例化, 见代码块
    Object beanInstance = doCreateBean(beanName, mbdToUse, args);

    return beanInstance;
}
```





## 9.7 Bean 实例化

实例化方式

- 默认构造器创建，实例化策略：InstantiationStrategy
- 构造器依赖注入，
- AbstractAutowireCapableBeanFactory#createBeanInstance







```java
protected BeanWrapper createBeanInstance(String beanName, RootBeanDefinition mbd, @Nullable Object[] args) {
    // Make sure bean class is actually resolved at this point.
    Class<?> beanClass = resolveBeanClass(mbd, beanName);

    if (beanClass != null && !Modifier.isPublic(beanClass.getModifiers()) && !mbd.isNonPublicAccessAllowed()) {
        throw new BeanCreationException(mbd.getResourceDescription(), beanName,
                                        "Bean class isn't public, and non-public access not allowed: " + beanClass.getName());
    }

    // lambda方式注册的bean, 进行实例化
    Supplier<?> instanceSupplier = mbd.getInstanceSupplier();
    if (instanceSupplier != null) {
        return obtainFromSupplier(instanceSupplier, beanName);
    }

    // 跳过,若bean存在工厂方法factory-method
    if (mbd.getFactoryMethodName() != null) {
        // 通过工厂方法factory-method实例化bean
        return instantiateUsingFactoryMethod(beanName, mbd, args);
    }

    // 重复创建相同类型的bean
    boolean resolved = false;
    boolean autowireNecessary = false;
    // 跳过
    if (args == null) {}
    if (resolved) {}


    // 没有实现SmartInstantiationAwareBeanPostProcessor返回null
    Constructor<?>[] ctors = determineConstructorsFromBeanPostProcessors(beanClass, beanName);
    // 判断自动绑定的模式autowiring=constructor
    if (ctors != null || mbd.getResolvedAutowireMode() == AUTOWIRE_CONSTRUCTOR ||
        mbd.hasConstructorArgumentValues() || !ObjectUtils.isEmpty(args)) {
        // 注入构造器参数实例化
        return autowireConstructor(beanName, mbd, ctors, args);
    }

    ctors = mbd.getPreferredConstructors();
    if (ctors != null) {
        // 注入构造器参数实例化
        return autowireConstructor(beanName, mbd, ctors, null);
    }

    // (重点)使用默认构造器实例化bean
    return instantiateBean(beanName, mbd);
}

```



```java
public Object instantiate(RootBeanDefinition bd, @Nullable String beanName, BeanFactory owner) {
    // 不存在方法覆盖
    if (!bd.hasMethodOverrides()) {
        Constructor<?> constructorToUse;
        synchronized (bd.constructorArgumentLock) {
            constructorToUse = (Constructor<?>) bd.resolvedConstructorOrFactoryMethod;
            if (constructorToUse == null) {
                
                // 获取bean的class
                final Class<?> clazz = bd.getBeanClass();
                // 接口无法实例化,抛出异常
                if (clazz.isInterface()) {
                    throw new BeanInstantiationException(clazz, "Specified class is an interface");
                }
                try {
                    // 跳过,安全处理
                    if (System.getSecurityManager() != null) {}
                    else {
                        // 获取class的构造方法
                        constructorToUse = clazz.getDeclaredConstructor();
                    }
                    bd.resolvedConstructorOrFactoryMethod = constructorToUse;
                } catch (Throwable ex) {
                    throw new BeanInstantiationException(clazz, "No default constructor found", ex);
                }
            }
        }
        // 通过反射调用构造方法创建bean
        // 将构造方法Accessible修改为true,然后constructor.newInstance()创建实例
        return BeanUtils.instantiateClass(constructorToUse);
    }else {
        return instantiateWithMethodInjection(bd, beanName, owner);
    }
}
```









```java
protected <T> T doGetBean(final String name, final Class<T> requiredType,
                          final Object[] args, boolean typeCheckOnly) throws BeansException {

    final String beanName = transformedBeanName(name);
    // ....
    
    // 获取bean的完整元信息
    final RootBeanDefinition mbd = getMergedLocalBeanDefinition(beanName);
    checkMergedBeanDefinition(mbd, beanName, args);

    // 跳过, 获取并处理bean的 dependsOn
    String[] dependsOn = mbd.getDependsOn();
    if (dependsOn != null) {}

    // 如果bean是单例
    if (mbd.isSingleton()) {
        sharedInstance = getSingleton(beanName, () -> {
            // 见代码块 ,创建bean, 包含两步操作
            // 1.加载bean的class 2.实例化bean
            return createBean(beanName, mbd, args);
        });
        bean = getObjectForBeanInstance(sharedInstance, name, beanName, mbd);
    }
	// 跳过,如果bean是Prototype
    else if (mbd.isPrototype()) {}
	// ...
}
```







// debug 构造器注入, 自动绑定构造器注入

## 9.8 Bean 实例化后

- 接口 InstantiationAwareBeanPostProcessor#postProcessAfterInstantiation
- 属性赋值 populate 判断

```java
public interface InstantiationAwareBeanPostProcessor extends BeanPostProcessor {
	// 返回true表示bean的属性应该被设置, false应该被跳过
	default boolean postProcessAfterInstantiation(Object bean, String beanName) throws BeansException {
		return true;
	}
}
```



**使用案例**

实现 接口 InstantiationAwareBeanPostProcessor 的 postProcessAfterInstantiation 方法，在 bean **实例化后**对 bean 进行后置处理，这里我们将名称为 `user` 的bean 的返回值设置为 false，表示不需要为`user` 设置属性，故 xml 中配置的 bean 属性不会生效。

1. 实现接口，重写方法

```java
static class MyInstantiationAwareBeanPostProcessor implements InstantiationAwareBeanPostProcessor {
   /**
    * bean 实例化后调用
    * 返回false表示不要为bean设置属性
    * 返回true表示要为bean设置属性
    * 注释掉该方法, user bean 属性有值
    */
    @Override
    public boolean postProcessAfterInstantiation(Object bean, String beanName) throws BeansException {
        // user bean 不要为属性赋值
        if ("user".equals(beanName)) {
            return false;
        }
        return true;
    }
}
```

2. 注册自定义的 BeanPostProcessor 到容器

```java
public static void main(String[] args) {
    // 创建容器并加载xml中的BeanDefinition
    DefaultListableBeanFactory beanFactory = getBeanFactory();

    // 1. 注册自定义的BeanPostProcessor, 保存到容器的beanPostProcessors属性
    beanFactory.addBeanPostProcessor(new MyInstantiationAwareBeanPostProcessor());
    // 2. 依赖查找, 会实例化bean
    User user = beanFactory.getBean("user", User.class);
    SuperUser superUser = beanFactory.getBean(SuperUser.class);
    // 3. 期望输出bean的属性为null, 因为在实例化后我们返回了false表示不为属性赋值
    // 如果没有自定义的后置beanPostProcessors, 期待输出xml中配置的 tracccer
    System.out.println(user);
}
```

3. 输出结果，若注释掉重写的方法，会输出 User{id=1, name='tracccer'}

```java
User{id=null, name='null'}
```



## 9.9  Bean 属性赋值前

- Bean 属性值元信息 PropertyValues
- Bean 属性赋值前回调接口
  - Spring1.2-5.0 InstantiationAwareBeanPostProcessor#postProcessPropertySource
  - Spring5.1以后 InstantiationAwareBeanPostProcessor#postProcessProperties  

```java
public interface InstantiationAwareBeanPostProcessor extends BeanPostProcessor {
	default PropertyValues postProcessProperties(PropertyValues pvs, Object bean, String beanName)
			throws BeansException {

		return null;
	}
}
```

**使用案例**

实现 接口 InstantiationAwareBeanPostProcessor 的 postProcessProperties  方法，在 bean **属性赋值前**对 bean 进行后置处理，这里我们将名称为 `user` 的bean 的 name 属性值设置`Sigma`并返回，这一步替换掉了 Spring 对于 bean 属性赋值的处理。

1. 实现接口，重写方法

```java
static class MyInstantiationAwareBeanPostProcessor implements InstantiationAwareBeanPostProcessor {
    /**
         * 为bean的属性设置值
         */
    @Override
    public PropertyValues postProcessProperties(PropertyValues pvs, Object bean, String beanName) throws BeansException {
        if ("user".equals(beanName)) {
            MutablePropertyValues propertyValues = new MutablePropertyValues();
            // 作用等价于 <property name="name" value="Sigma"/>
            propertyValues.addPropertyValue("name", "Sigma");

            return propertyValues;
        }
        return null;
    }
}
```

2. 注册自定义的 BeanPostProcessor 到容器

```java
public static void main(String[] args) {
    // 创建容器并加载xml中的BeanDefinition
    DefaultListableBeanFactory beanFactory = getBeanFactory();

    // 1. 注册自定义的BeanPostProcessor, 保存到容器的beanPostProcessors属性
    beanFactory.addBeanPostProcessor(new MyInstantiationAwareBeanPostProcessor());
    // 2. 依赖查找, 会实例化bean
    User user = beanFactory.getBean("user", User.class);
    // 3. 期望输出bean的属性为Sigma, 因为在BeanPostProcessor中设置了
    System.out.println(user);
}
```

3. 输出结果，是我们定义的属性值，xml 中对 bean 的属性赋值没有生效，即替换了 Spring 对bean 的赋值，若注释掉重写的方法，会输出 User{id=1, name='tracccer'}

```
User{id=null, name='Sigma'}
```



## 9.10 Aware 接口回调

这块的注入参考 6.8 章节 接口回调方法注入





```java
AbstractAutowireCapableBeanFactory.java
    
private void invokeAwareMethods(final String beanName, final Object bean) {
    if (bean instanceof Aware) {
        // 回调BeanNameAware实现类
        if (bean instanceof BeanNameAware) {
            ((BeanNameAware) bean).setBeanName(beanName);
        }
        // 回调BeanClassLoaderAware实现类
        if (bean instanceof BeanClassLoaderAware) {
            ClassLoader bcl = getBeanClassLoader();
            if (bcl != null) {
                ((BeanClassLoaderAware) bean).setBeanClassLoader(bcl);
            }
        }
        // 回调BeanFactoryAware实现类
        if (bean instanceof BeanFactoryAware) {
            ((BeanFactoryAware) bean).setBeanFactory(AbstractAutowireCapableBeanFactory.this);
        }
    }
}
```







## 9.11 Bean 初始化前

- 回调方法 BeanPostProcessor#postProcessBeforeInitialization，方法字面意思就是初始化前的处理程序
- AOP 功能就是在这一阶段实现的，AbstractAutoProxyCreator#postProcessBeforeInstantiation

源码如下所示：

```java
public interface BeanPostProcessor {

	/**
	 * 在bean初始化之前回调
	 */
	default Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		return bean;
	}
}
```



**使用案例**

实现接口 BeanPostProcessor 的 postProcessBeforeInitialization 方法，在 bean **初始化前**对 bean 进行后置处理，这里我们将名称为 `user` 的bean 的 name 属性值设置`Torbjorn`并返回。

1. 实现接口，重写方法，由于 InstantiationAwareBeanPostProcessor 继承了 BeanPostProcessor，所以这里继承 InstantiationAwareBeanPostProcessor  也是可以的。

```java
static class MyInstantiationAwareBeanPostProcessor implements InstantiationAwareBeanPostProcessor {
    /**
    * bean初始化前回调该方法
    * 为user bean重新设置属性并返回
    */
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if ("user".equals(beanName)) {
            User user = (User) bean;
            user.setName("Torbjorn");

            return user;
        }
        return bean;
    }
}
```

2. 注册自定义的 BeanPostProcessor 到容器

```java
public static void main(String[] args) {
    // 创建容器并加载xml中的BeanDefinition
    DefaultListableBeanFactory beanFactory = getBeanFactory();

    // 1. 注册自定义的BeanPostProcessor, 保存到容器的beanPostProcessors属性
    beanFactory.addBeanPostProcessor(new MyInstantiationAwareBeanPostProcessor());
    // 2. 依赖查找, 会实例化bean
    User user = beanFactory.getBean("user", User.class);
    // 3. 期望输出bean的属性为Torbjorn, 因为在BeanPostProcessor中设置了
    // 默认输出xml中配置的tracccer
    System.out.println(user);
}
```

输出结果：是我们定义的属性值，xml 中对 bean 的属性赋值没有生效，即替换了 Spring 对bean 的赋值，若注释掉重写的方法，会输出 User{id=1, name='tracccer'}

```
User{id=1, name='Torbjorn'}
```



**源码分析**

bean 在初始化前会回调自定义的 BeanPostProcessor#postProcessBeforeInitialization 方法

```java
AbstractAutowireCapableBeanFactory.java
// 代码块 11-1，初始化bean
protected Object initializeBean(final String beanName, final Object bean, @Nullable RootBeanDefinition mbd) {
    if (System.getSecurityManager() != null) {
		// 安全部分,跳过	
    } else {
        // 回调自定义的Aware方法
        invokeAwareMethods(beanName, bean);
    }

    Object wrappedBean = bean;
    if (mbd == null || !mbd.isSynthetic()) {
        // 初始化前,见代码块11-2
        // 回调自定义的BeanPostProcessor#postProcessBeforeInitialization方法
        wrappedBean = applyBeanPostProcessorsBeforeInitialization(wrappedBean, beanName);
    }

    try {
        // 初始化, 见代码块12-2
        // 回调自定义的InitializingBean#afterPropertiesSet方法
        invokeInitMethods(beanName, wrappedBean, mbd);
    }
    catch (Throwable ex) {
        throw new BeanCreationException(
            (mbd != null ? mbd.getResourceDescription() : null),
            beanName, "Invocation of init method failed", ex);
    }
    if (mbd == null || !mbd.isSynthetic()) {
        // 初始化后, 见代码块13-1
        // 回调自定义的BeanPostProcessor#postProcessAfterInitialization方法
        wrappedBean = applyBeanPostProcessorsAfterInitialization(wrappedBean, beanName);
    }

    return wrappedBean;
}
```



```java
AbstractAutowireCapableBeanFactory.java

// 代码块 11-2
// bean初始化前回调BeanPostProcessors#postProcessBeforeInitialization 方法
public Object applyBeanPostProcessorsBeforeInitialization(Object existingBean, String beanName)
    throws BeansException {

    Object result = existingBean;
    // 获取所有BeanPostProcessor,
    for (BeanPostProcessor processor : getBeanPostProcessors()) {
        // 回调其postProcessBeforeInitialization()方法
        Object current = processor.postProcessBeforeInitialization(result, beanName);
        if (current == null) {
            // 回调返回null,则将当前bean返回
            return result;
        }
        // 将回调的返回结果作为bean返回
        result = current;
    }
    return result;
}
```



## 9.12 Bean 初始化

Bean 的初始化就是指调用 Bean 的初始化方法，会依次回调下列方法：

- @PostConstruct 标记方法
- 实现 InitialzingBean 接口的 afterPropertiesSet() 方法
- 自定义初始化方法，都是将初始化方法名保存到 BeanDefinition 元信息的属性 initMethodName 中
  - xml 配置：`<bean init-method="xxx" />`
  - Java 注解：`@Bean(initMethod="xxx")`
  - Java API：`AbstractBeanDefinition#setInitMethodName(String)`

详细使用参考 4.6 初始化 Bean 章节



### 1.@PostConstruct 的回调流程

1. 首先构建一个 bean，使用 @PostConstruct 标记初始化方法

```java
public class Teacher {
    // @PostConstruct 标记初始化方法
    @PostConstruct
    public void init() {
        System.out.println("@PostConstruct设置初始化方法: Teacher 初始化中...");
    }
}
```

2. 参考 6.17 章节，@PostConstruct 注解由 `CommonAnnotationBeanPostProcessor`进行处理，而且该类实现了 BeanPostProcessor#postProcessBeforeInitialization 方法，即和上一节中我们自定义的初始化前调用的 BeanPostProcessor 作用一致，源码如下所示

```java
public class CommonAnnotationBeanPostProcessor extends InitDestroyAnnotationBeanPostProcessor
		implements InstantiationAwareBeanPostProcessor, BeanFactoryAware, Serializable {
    // 可以处理的注解
    public CommonAnnotationBeanPostProcessor() {
		setOrder(Ordered.LOWEST_PRECEDENCE - 3);
		setInitAnnotationType(PostConstruct.class);
		setDestroyAnnotationType(PreDestroy.class);
	}
    // 该方法会在bean初始化前被回调,具体参考代码块11-2
    @Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		LifecycleMetadata metadata = findLifecycleMetadata(bean.getClass());
		try {
            // 底层是利用反射调用@PostConstruct标记的方法
			metadata.invokeInitMethods(bean, beanName);
		}
		catch (Throwable ex) {}
        
		return bean;
	}
}
```

这其实也解答了一个疑问，就是 BeanPostProcessor#postProcessBeforeInitialization 有什么具体应用案例，在 Bean 初始化前处理@PostConstruct 注解就是之一。以后我们有特殊 Bean 需要在初始化前执行一些操作，就可以重写该方法来实现，比如标记了某个自定义注解的 bean 在初始化前需要打印日志等。





### 2. 其他初始化方法的回调过程

1. 首先构建一个 bean，重写 InitialzingBean#afterPropertiesSet 方法

```java
public class Teacher implements InitializingBean {

    // 实现InitializingBean#afterPropertiesSet方法
    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("实现InitializingBean接口设置初始化方法: Teacher 初始化中...");
    }
    
    // 使用@Bean(initMethod="")标记该方法在初始化后调用
    public void initTeacher() {
        System.out.println("initMethod设置初始化方法: Teacher 初始化中...");
    }
}
```

2. 使用注解配置 bean，并设置初始化方法，然后创建容器并启动，容器会扫描到类中配置的 bean，初始化 bean 时会回调指定的初始化方法

```java
public static void main(String[] args) {
    // 1.创建注解配置的应用上下文
    AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
    applicationContext.register(BeanDestroyDemo.class);
    System.out.println("=========Spring应用上下文开始启动=========");
    
    // 2. 启动应用上下文
    applicationContext.refresh();
    System.out.println("=========Spring应用上下文已启动=========");
}

// 指定初始化方法, 与xml方式作用一直
@Bean(initMethod = "initTeacher")
public Teacher createTeacher() {
    return new Teacher();
}
```

输出结果：说明 bean 初始化时回调了两个初始化方法

```
=========Spring应用上下文开始启动=========
实现InitializingBean接口设置初始化方法: Teacher 初始化中...
initMethod设置初始化方法: Teacher 初始化中...
=========Spring应用上下文已启动=========
```



3. 参考上一节代码块 11-1，bean 在初始化时会回调初始化方法
   - 回调重写的 InitialzingBean#afterPropertiesSet 方法
   - 回调 Bean 使用`initMethod`指定的初始化方法

```java
AbstractAutowireCapableBeanFactory.java
// 代码块12-2
protected void invokeInitMethods(String beanName, final Object bean, @Nullable RootBeanDefinition mbd)
    throws Throwable {

    // 判断bean是否实现了InitializingBean接口
    boolean isInitializingBean = (bean instanceof InitializingBean);
    // 1. 回调重写的afterPropertiesSet()方法
    if (isInitializingBean && (mbd == null || !mbd.isExternallyManagedInitMethod("afterPropertiesSet"))) {
        if (System.getSecurityManager() != null) {
			// 省略安全部分...
        }else {
            // 重点:回调重写的afterPropertiesSet()方法
            ((InitializingBean) bean).afterPropertiesSet();
        }
    }

    // 2. 回调bean配置中指定的初始化方法
    // 3. 回调通过 Java Api 的方式指定初始化方法,见4.6.2
    if (mbd != null && bean.getClass() != NullBean.class) {
        // 通过BeanDefinition获得bean配置的初始化方法,包括注解和xml配置
        String initMethodName = mbd.getInitMethodName();
        // 1.初始化方法名不能为空
        // 2.若当前bean实现了InitializingBean,则配置的初始化方法名不能为afterPropertiesSet
        // 3.安全部分,跳过即可
        if (StringUtils.hasLength(initMethodName) &&
            !(isInitializingBean && "afterPropertiesSet".equals(initMethodName)) &&
            !mbd.isExternallyManagedInitMethod(initMethodName)) {
            
            // 通过反射回调指定的初始化方法
            invokeCustomInitMethod(beanName, bean, mbd);
        }
    }
}
```



通过源码分析，不难得出三种初始化方法的调用顺序是：

​	<font color="#dd0000">@PostConstruct → InitializingBean接口 → initMethod</font>，@PostConstruct 本质是 BeanPostProcessor 进行处理，在初始化前就进行了回调，后两者会在初始化时依次回调。



所谓的**初始化时**，就是 bean 在创建好之后，需要进行一些初始化操作，即会调用 bean 设置的初始化方法。



## 9.13 Bean 初始化后

- 回调方法 BeanPostProcessor#postProcessAfterInitialization，在Bean初始化后回调，与初始化前的 postProcessBeforeInitialization() 相对应

  

```java
public interface BeanPostProcessor {
    // 在bean初始化前调用
    default Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		return bean;
	}
    
    // 在bean初始化后调用
	default Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		return bean;
	}
}
```



**使用案例**

实现接口 BeanPostProcessor 的 postProcessBeforeInitialization 方法，在 bean **初始化后** 对 bean 进行后置处理，这里我们将名称为 `user` 的bean 的 name 属性值设置`Symmetra`并返回。

1. 实现接口，重写方法，由于 InstantiationAwareBeanPostProcessor 继承了 BeanPostProcessor，所以这里继承 InstantiationAwareBeanPostProcessor  都是可以的。

```java
static class MyInstantiationAwareBeanPostProcessor implements InstantiationAwareBeanPostProcessor {
    /**
         * bean初始化后回调该方法
         * 为user bean重新设置属性并返回
         */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if ("user".equals(beanName)) {
            User user = (User) bean;
            user.setName("Symmetra");

            return user;
        }
        return bean;
    }
}
```

2. 注册自定义的 BeanPostProcessor 到容器

```java
public static void main(String[] args) {
    // 创建容器并加载xml中的BeanDefinition
    DefaultListableBeanFactory beanFactory = getBeanFactory();

    // 1. 注册自定义的BeanPostProcessor, 保存到容器的beanPostProcessors属性
    beanFactory.addBeanPostProcessor(new MyInstantiationAwareBeanPostProcessor());
    // 2. 依赖查找, 会实例化bean
    User user = beanFactory.getBean("user", User.class);
    // 3. 期望输出bean的属性为Torbjorn, 因为在BeanPostProcessor中设置了
    // 默认输出xml中配置的tracccer
    System.out.println(user);
}
```

输出结果：是我们定义的属性值，xml 中对 bean 的属性赋值没有生效，即替换了 Spring 对bean 的赋值，若注释掉重写的方法，会输出 User{id=1, name='tracccer'}

```
User{id=1, name='Symmetra'}
```

**源码分析**

bean 在初始化后会回调自定义的 BeanPostProcessor#postProcessAfterInitialization 方法

```java
AbstractAutowireCapableBeanFactory.java
// 初始化bean
protected Object initializeBean(final String beanName, final Object bean, @Nullable RootBeanDefinition mbd) {

    // 回调自定义的Aware方法
    invokeAwareMethods(beanName, bean);

    Object wrappedBean = bean;
    if (mbd == null || !mbd.isSynthetic()) {
        // 初始化前,见代码块11-2
        // 回调自定义的BeanPostProcessor#postProcessBeforeInitialization方法
        wrappedBean = applyBeanPostProcessorsBeforeInitialization(wrappedBean, beanName);
    }


    // 初始化, 见代码块12-2
    // 回调自定义的InitializingBean#afterPropertiesSet方法
    invokeInitMethods(beanName, wrappedBean, mbd);

    if (mbd == null || !mbd.isSynthetic()) {
        // 初始化后, 见代码块13-1
        // 回调自定义的BeanPostProcessor#postProcessAfterInitialization方法
        wrappedBean = applyBeanPostProcessorsAfterInitialization(wrappedBean, beanName);
    }

    return wrappedBean;
}
```

这部分源码与初始化前回调 BeanPostProcessor，不能说一模一样，只能说完全一致。

```java
AbstractAutowireCapableBeanFactory.java

// bean初始化后回调BeanPostProcessor#postProcessAfterInitialization 方法
@Override
public Object applyBeanPostProcessorsAfterInitialization(Object existingBean, String beanName)
    throws BeansException {

    Object result = existingBean;
    // 获取所有的BeanPostProcessor
    for (BeanPostProcessor processor : getBeanPostProcessors()) {
        // 回调其postProcessAfterInitialization()方法
        Object current = processor.postProcessAfterInitialization(result, beanName);
        if (current == null) {
            // 回调返回null,则将当前bean返回
            return result;
        }
        // 将回调的返回结果作为bean返回
        result = current;
    }
    return result;
}
```



// 补充: 通过这几个章节,可以发现 AutowireCapableBeanFactory 也是 Spring 中非常重要的一个容器，补充一下其的主要功能。定义了以下功能：创建bean，初始化bean，销毁bean，注入bean，处理依赖，应用BeanPostProcessor











## 9.14 Bean 初始化完成

该方法可以在 Bean 在初始化完成以后，还能对 Bean 进行修改

- 回调方法 SmartInitializingSingleton#afterSingletonsInstantiated，Spring 4.1支持

```java
public interface SmartInitializingSingleton {
	// 在bean初始化完成后回调
	void afterSingletonsInstantiated();
}
```

**使用案例**

实现接口 SmartInitializingSingleton 的 afterSingletonsInstantiated 方法，在 bean **初始化完成后** 对 bean 进行后置处理，这里我们将名称为 `user` 的bean 的 name 属性值设置`Symmetra`并返回。

1. 构建一个 bean，重写 SmartInitializingSingleton#afterSingletonsInstantiated 方法

```java
public class Teacher2 implements SmartInitializingSingleton {
    private String name;

    @Override
    public void afterSingletonsInstantiated() {
        this.name = "Winston";
        System.out.println("实现SmartInitializingSingleton接口设置初始化完成时回调方法: Teacher 初始化完成...");
    }
}
```

2. 注册 bean 到容器并启动

```java
public static void main(String[] args) {
    // 1.创建并启动 ApplicationContext 容器, 使用注解配置
    AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
    applicationContext.register(BeanInitializationLifeCycleDemo4.class);
    // 容器启动, 对bean实例化初始化完成后, 回调afterSingletonsInstantiated方法
    applicationContext.refresh();

    Teacher2 teacher = applicationContext.getBean(Teacher2.class);
    System.out.println(teacher);
}

@Bean
public Teacher2 createTeacher() {
    return new Teacher2();
}
```

输出结果，自定义回调方法中修改的属性值生效

```
实现SmartInitializingSingleton接口设置初始化完成时回调方法: Teacher 初始化完成...
Teacher2{name='Winston'}
```



**源码分析**

容器在启动`refresh`时，会对所有非懒加载的 bean 进行实例化和初始化，在这两项工作完成后，会回调 SmartInitializingSingleton#afterSingletonsInstantiated 方法，源码如下所示：

```java
DefaultListableBeanFactory.java

@Override
public void preInstantiateSingletons() throws BeansException {
    List<String> beanNames = new ArrayList<>(this.beanDefinitionNames);

    for (String beanName : beanNames) {
        // 完成bean的实例化与初始化, 初始化前,初始化,初始化后的回调均在此处调用
    }

    // 遍历所有bean
    for (String beanName : beanNames) {
        Object singletonInstance = getSingleton(beanName);

        // 若当前bean实现了SmartInitializingSingleton接口
        if (singletonInstance instanceof SmartInitializingSingleton) {
            final SmartInitializingSingleton smartSingleton = (SmartInitializingSingleton) singletonInstance;
            if (System.getSecurityManager() != null) {
                // 安全处理,跳过
            } else {

                // (重点)回调SmartInitializingSingleton#afterSingletonsInstantiated 方法
                smartSingleton.afterSingletonsInstantiated();
            }
        }
    }
}
```



**应用案例**

在监听器 17.16.2 章节，@EventListener 标记监听回调方法，监听器注册到容器的时机就是 bean 初始化完成时，实现方式就是实现了 SmartInitializingSingleton#afterSingletonsInstantiated 方法。



## 9.15 Bean 销毁前

- 回调方法 DestructionAwareBeanPostProcessor#postProcessBeforeDestruction
- 所有 Bean 在销毁前都会回调该方法

```java
public interface DestructionAwareBeanPostProcessor extends BeanPostProcessor {
	// 在bean销毁前调用
    void postProcessBeforeDestruction(Object bean, String beanName) throws BeansException;
}
```



**使用案例**

实现接口 DestructionAwareBeanPostProcessor 的 postProcessBeforeDestruction 方法，在 bean **销毁前**对 bean 进行处理，这里我们打印一行日志后销毁 Bean。

1. 重写 DestructionAwareBeanPostProcessor 的 postProcessBeforeDestruction 方法

```java
// 代码块15-2
static class LifecycleDestructionPostProcessor implements DestructionAwareBeanPostProcessor {

    // 如何被调用见代码块15-1
    @Override
    public void postProcessBeforeDestruction(Object bean, String beanName) throws BeansException {
        if (bean instanceof User) {
            User user = (User) bean;
            System.out.println( user.getName() + "被放走了 ......");
        }
    }
}
```

2. 注册自定义 BeanPostProcessor 到容器，在容器关闭时会回调自定义的 BeanPostProcessor

```java
public static void main(String[] args) {
    // 1.创建并启动 ApplicationContext 容器, 使用注解配置
    AnnotationConfigApplicationContext applicationContext = getApplicationContext();
    // 2.注册自定义BeanPostProcessor,在bean销毁前调用
    ConfigurableListableBeanFactory beanFactory = applicationContext.getBeanFactory();
    beanFactory.addBeanPostProcessor(new LifecycleDestructionPostProcessor());
    // 3. 启动容器
    applicationContext.refresh();

    // 4. 关闭容器, 开始销毁bean, 此时会回调自定义BeanPostProcessor
    applicationContext.close();
}
```

输出结果

```
tracccer被放走了 ......
tracccer被放走了 ......
```



**源码分析**

1. Bean 销毁的源码如下所示，会回调销毁方法，这里只关心第一种情况

   - 回调DestructionAwareBeanPostProcessor#postProcessBeforeDestruction方法
   - 回调实现接口DisposableBean 的 destroy() 方法
   - 回调 bean 配置的销毁方法，包括 xml 和注解配置的

   bean 在销毁前会回调自定义的 DestructionAwareBeanPostProcessor#postProcessBeforeDestruction 方法，与 9.11 章节Bean 初始化前的源码非常类似

```java
DisposableBeanAdapter.java   适配器模式?
    
// 代码块15-1, 销毁方法
@Override
public void destroy() {
    if (!CollectionUtils.isEmpty(this.beanPostProcessors)) {
        // 遍历所有DestructionAwareBeanPostProcessor
        for (DestructionAwareBeanPostProcessor processor : this.beanPostProcessors) {
            // 直接调用其postProcessBeforeDestruction()方法
            processor.postProcessBeforeDestruction(this.bean, this.beanName);
        }
    }

    if (this.invokeDisposableBean) {
        try {
            if (System.getSecurityManager() != null) {
				// 安全处理,跳过
            } else {
                // 回调重写的DisposableBean#destroy方法
                ((DisposableBean) this.bean).destroy();
            }
        }
        catch (Throwable ex) {
            String msg = "Invocation of destroy method failed on bean with name '" + this.beanName + "'";
        }
    }

    if (this.destroyMethod != null) {
        // 回调bean配置的销毁前方法
        invokeCustomDestroyMethod(this.destroyMethod);
    } else if (this.destroyMethodName != null) {
        Method methodToInvoke = determineDestroyMethod(this.destroyMethodName);
        if (methodToInvoke != null) {
            invokeCustomDestroyMethod(ClassUtils.getInterfaceMethodIfPossible(methodToInvoke));
        }
    }
}
```











## 9.16 Bean 销毁

- Bean 在销毁前，会依次回调下列方法：

  - @PreDestroy 标记方法
  - 实现 DisposableBean 接口的 destroy() 方法
  - 自定义销毁方法
    - xml 配置：`<bean destroy="xxx" />`
    - java 注解：`@Bean(destroy="xxx")`
    - java api：`AbstractBeanDefinition#setInitMethodName(String)`

  详细使用参考 4.8 销毁 Bean 章节
  
  主要用来优雅下线，删除缓存等。



### 1.@PreDestroy  的回调流程

1. 首先构建一个 bean，使用 @PreDestroy 标记初始化方法

```java
public class Teacher {
    @PreDestroy
    public void preDestroy() {
        System.out.println("@PreDestroy设置销毁方法: Teacher 销毁中...");
    }
}
```

2. 参考 6.17 章节，@PreDestroy  注解由 `CommonAnnotationBeanPostProcessor`进行处理，而且该类实现了 DestructionAwareBeanPostProcessor#postProcessBeforeDestruction方法，即和上一节中我们自定义的销毁前调用的 DestructionAwareBeanPostProcessor 作用一致，源码如下所示

```java
// 代码块16-1
public class CommonAnnotationBeanPostProcessor extends InitDestroyAnnotationBeanPostProcessor
		implements InstantiationAwareBeanPostProcessor, BeanFactoryAware, Serializable {
    // 可以处理的注解, @PreDestroy
    public CommonAnnotationBeanPostProcessor() {
		setOrder(Ordered.LOWEST_PRECEDENCE - 3);
		setInitAnnotationType(PostConstruct.class);
		setDestroyAnnotationType(PreDestroy.class);
	}
    
    // 该方法会在bean销毁前被回调
	@Override
	public void postProcessBeforeDestruction(Object bean, String beanName) throws BeansException {
		LifecycleMetadata metadata = findLifecycleMetadata(bean.getClass());
		try {
            // 底层是利用反射调用@PreDestroy 标记的方法
			metadata.invokeDestroyMethods(bean, beanName);
		} catch (Throwable ex) {
		}
	}
```

与 9.12.1 章节 @PostConstruct 的处理流程相对应



### 2. 其他销毁方法的回调过程

1. 首先构建一个 bean，重写 DisposableBean#destroy 方法

```java
public class Teacher implements DisposableBean {

    @Override
    public void destroy() throws Exception {
        System.out.println("实现DisposableBean接口设置销毁方法: Teacher 销毁中...");
    }

    // 使用@Bean(destroyMethod="")标记该方法在销毁前调用
    public void destroyTeacher() {
        System.out.println("destroyMethod设置销毁方法: Teacher 销毁中...");
    }
}
```

2. 使用注解配置 bean，并设置初始化方法，然后创建容器并启动，容器会扫描到类中配置的 bean，初始化 bean 时会回调指定的初始化方法

```java
public static void main(String[] args) {
    // 1.创建并启动 ApplicationContext 容器, 使用注解配置
    AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
    applicationContext.register(BeanDestoryLifeCycleDemo2.class);
    // 2. 启动容器
    applicationContext.refresh();
    // 3. 关闭容器, 开始销毁bean, 这里会回调销毁方法
    System.out.println("=======Spring应用上下文开始关闭=======");
    applicationContext.close();
}

@Bean(destroyMethod = "destroyTeacher")
public Teacher createTeacher() {
    return new Teacher();
}
```

输出结果：说明 bean 销毁前回调了两个销毁方法

```
=======Spring应用上下文开始关闭=======
实现DisposableBean接口设置销毁方法: Teacher 销毁中...
destroyMethod设置销毁方法: Teacher 销毁中...
```



3. Bean 销毁的源码如下所示，会回调 5 种方式设置的销毁方法

   1. 回调自定义重写的DestructionAwareBeanPostProcessor#postProcessBeforeDestruction 方法

   2. 回调@PreDestroy  标记的销毁方法

   3. 回调实现接口DisposableBean 的 destroy() 方法

   4. 回调 bean 配置的销毁方法，包括 xml 和注解配置的

   5. 回调使用 api 为 bean 配置的销毁方法

      

   比较巧妙的一点是，DisposableBeanAdapter 也是实现了接口 DisposableBean 的 destroy() 方法，所以 5 种设置回调销毁方法的方式，其本质底层都是使用 DisposableBean#destroy 实现的。

```java
DisposableBeanAdapter.java   适配器模式?
    
// 代码块15-1, 销毁方法，重写了DisposableBean#destroy
@Override
public void destroy() {

    if (!CollectionUtils.isEmpty(this.beanPostProcessors)) {
        // 遍历所有DestructionAwareBeanPostProcessor
        // 包括自定义的实现和CommonAnnotationBeanPostProcessor
        for (DestructionAwareBeanPostProcessor processor : this.beanPostProcessors) {
            // 1.回调自定义的postProcessBeforeDestruction()方法
            // 2.回调CommonAnnotationBeanPostProcessor的方法
            // 见代码块16-1, 15-2
            processor.postProcessBeforeDestruction(this.bean, this.beanName);
        }
    }

    // 如果当前bean实现了DisposableBean接口
    if (this.invokeDisposableBean) {
        if (System.getSecurityManager() != null) {
            // 安全处理,跳过
        } else {
            // 3.回调重写的DisposableBean#destroy方法
            ((DisposableBean) this.bean).destroy();
        }
    }
	// 如果bean配置了destroyMethod
    if (this.destroyMethod != null) {
        // 4.回调bean配置的销毁方法,底层是通过反射实现的
        invokeCustomDestroyMethod(this.destroyMethod);
    } else if (this.destroyMethodName != null) {
        // bean未配置destroyMethod, 但通过api设置了回调方法名称
        Method methodToInvoke = determineDestroyMethod(this.destroyMethodName);
        if (methodToInvoke != null) {
            // 5.回调java api配置的销毁方法
            invokeCustomDestroyMethod(ClassUtils.getInterfaceMethodIfPossible(methodToInvoke));
        }
    }
}
```

4. 销毁 Bean 的时机：
   1. 在应用上下文关闭的时候`applicationContext.close()`，最终会调用 DefaultListableBeanFactory 的 `destroyBean()` 方法销毁 Bean
   2. 直接调用 DefaultListableBeanFactory 的 `destroyBean()` 方法销毁指定的 Bean

```java
DefaultListableBeanFactory.java

protected void destroyBean(String beanName, DisposableBean bean) {
    Set<String> dependencies;
    synchronized (this.dependentBeanMap) {
        // 将当前bean移除
        dependencies = this.dependentBeanMap.remove(beanName);
    }
    if (dependencies != null) {
        for (String dependentBeanName : dependencies) {
            destroySingleton(dependentBeanName);
        }
    }

    if (bean != null) {
        try {
            // 调用DisposableBean#destroy, 回调销毁方法
            bean.destroy();
        }
        catch (Throwable ex) {
            if (logger.isWarnEnabled()) {
                logger.warn("Destruction of bean with name '" + beanName + "' threw an exception", ex);
            }
        }
    }

    Set<String> containedBeans;
    synchronized (this.containedBeanMap) {
        // 将当前bean移除
        containedBeans = this.containedBeanMap.remove(beanName);
    }
    if (containedBeans != null) {
        for (String containedBeanName : containedBeans) {
            destroySingleton(containedBeanName);
        }
    }

    synchronized (this.dependentBeanMap) {
        for (Iterator<Map.Entry<String, Set<String>>> it = this.dependentBeanMap.entrySet().iterator(); it.hasNext();) {
            Map.Entry<String, Set<String>> entry = it.next();
            Set<String> dependenciesToClean = entry.getValue();
            dependenciesToClean.remove(beanName);
            if (dependenciesToClean.isEmpty()) {
                it.remove();
            }
        }
    }

    // 将当前bean移除
    this.dependenciesForBeanMap.remove(beanName);
}
```



destroyBean 会回调用 Bean 的销毁方法，destroySingletons 方法会清除 Spring BeanFactory 缓存的 Bean 对象

```java
DefaultListableBeanFactory.java

public void destroySingleton(String beanName) {
	// 删除单例bean对象， 见下面方法
    removeSingleton(beanName);

	// 回调bean的销毁方法
    DisposableBean disposableBean;
    synchronized (this.disposableBeans) {
        disposableBean = (DisposableBean) this.disposableBeans.remove(beanName);
    }
    destroyBean(beanName, disposableBean);
}

protected void removeSingleton(String beanName) {
    synchronized (this.singletonObjects) {
        this.singletonObjects.remove(beanName);
        this.singletonFactories.remove(beanName);
        this.earlySingletonObjects.remove(beanName);
        this.registeredSingletons.remove(beanName);
    }
}
```





> 为什么调用了beanFactory.destroyBean("userHolder", userHolder) 销毁方法，不会从容器中移除呢。

答：destroyBean 操作仅是触发 Bean 销毁生命周期，因为 Bean 有可能再次被初始化，比如显示地调用 AbstractAutowireCapableBeanFactory#initializeBean(Object, String) 方法

## 9.17 Bean 垃圾回收

参考 4.8 章节







## 9.18 Bean 的生命周期总结

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

8. DestructionAwareBeanPostProcessor 处理程序，调用其 `postProcessBeforeDestruction` 方法，会在每个 Bean 销毁前调用

9. 处理`@PreDestroy` 注解，该注解配置了 Bean 销毁前的回调方法，Spring 内部实现了CommonAnnotationBeanPostProcessor，来回调该注解指定的销毁方法

10. 处理 DisposableBean 接口，Spring 会检测bean如果实现了该接口，就会在对象销毁前调用`destory()`方法。与  InitializingBean 接口相对应

11. 处理 destory-method，`<bean destory-method="xxx">`配置了销毁方法，如果 Spring 发现 Bean 配置了该属性，就会回调他指定的方法，执行销毁逻辑

![image](https://cdn.jsdelivr.net/gh/maoturing/PictureBed@master/picx/image.16j89oe7q68w.png)



看到一个有意思的评论，Spring的bean处理过程要留这么多扩展回调接口，四处漏风。

从设计模式的角度来看，这种编程方式符合**对扩展开放，对修改关闭**的原则，这样后面需要对 Bean 实例化初始化阶段进行任何修改，都不需要修改现有代码，缺点是使得 Bean 的处理过程变得较为复杂。





> 小知识：Spring 中 Processor 通常是处理已存在的对象，而 Resolver 通常是由 A 物变为 B 物，doXX() 则是实际执行业务逻辑的方法



## 9.19 面试题

1. BeanPostProcessor 使用场景有哪些？

   答：BeanPostProcessor 提供 Spring Bean 初始化前和初始化后的生命周期回调，分别对应 postProcessBeforeInitialization 和 postProcessAfterInitialization，允许对 Bean 进行扩展修改，甚至是替换。

   加分项：其中，ApplicationContext 相关的 Aware 回调也是基于 BeanPostProcessor 实现，即 ApplicationContextAwareProcessor。（回顾AbstractApplicationContext#prepareBeanFactory）

   加分项：处理 @PostConstruct 注解的 CommonAnnotationBeanPostProcessor 类也是重写了 postProcessBeforeInitialization 方法，用来回调 @PostConstruct 标记的初始化方法。

   扩展：BeanPostProcessor  接口还有 3 个经典子接口，MergedBeanDefinitionPostProcessor 负责BeanDefinition 的合并，InstantiationAwareBeanPostProcessor 会在 Bean 实例化前后被回调，DestructionAwareBeanPostProcessor 会在 Bean 销毁前被回调

2. BeanFactoryPostProcessor 与 BeanPostProcessor 的区别

   答：BeanFactoryPostProcessor 是 Spring BeanFactory （实际为ConfigurableListableBeanFactory）的后置处理器，用于扩展 BeanFactory，或通过 BeanFactory 进行依赖查找和依赖注入。

   加分项：BeanFactoryPostProcessor 必须由 ApplicationContext 执行，BeanFactory 无法与其直接交互。而 BeanPostProcessor 则直接与 BeanFactory 关联，属于 N 对 1 的关系

   // 其实根本没学懂 BeanFactoryPostProcessor ,再回顾下前面章节吧

3. BeanFactory 是怎样处理 Bean 声明周期的？

   答：参考 9.18 章节![xxx](https://cdn.jsdelivr.net/gh/maoturing/PictureBed@master/picx/xxx.924mulbmq64.png)

   1.注册bean Definition registerBeanDefinition()
   2.bean Definition的合并阶段 getMergedLocalBeanDefinition(),比如user和superUser 最后都变为root bean Definition
   3.创建bean createBean()
   4.将bean类型从string变为class类型 resolveBeanClass()
   5.bean实例化前工作resolveBeforeInstantiation(),比如可以返回自定义的bean对象让spring不在实例化bean对象
   6.开始实例化bean doCreateBean()
   7.实例化bean createBeanInstance()
   8.bean实例化后 postProcessAfterInstantiation()返回false即bean不在对属性处理
   9.属性赋值前对属性处理postProcessProperties()
   10.属性赋值applyPropertyValues()
   11.bean初始化阶段initializeBean()
   12.初始化前aware接口回调(非ApplicationContextAware),比如beanFactoryAware
   13.初始化前回调applyBeanPostProcessorsBeforeInitialization(),比如@PostConstructor
   14.初始化invokeInitMethods(),比如实现InitializingBean接口的afterPropertiesSet()方法回调
   15.初始化后的回调applyBeanPostProcessorsAfterInitialization()
   16.bean重新的填充覆盖来更新bean preInstantiateSingletons()
   17.bean销毁前postProcessBeforeDestruction()
   18.bean销毁,比如@PreDestroy










// 这一章是**重中之重**，参考其他教程搞懂实例化部分，还有常用的生命周期回调方法的具体应用

// ! 重看Aware回调章节，实例化章节，初始化销毁部分还是简单呐^_^

// 再回顾下BeanDefinition 合并的章节, 看下MergedBeanDefinitionPostProcessor 的作用

// ConfigurableBeanFactory 的功能有注册 BeanPostProcessor，销毁bean

