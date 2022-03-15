package org.geekbang.annotation.enable;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * 使用@EnableHelloWorld导入HelloWorld模块
 *
 * @EnableHelloWorld 中导入的具体实现 HelloWorldConfiguration 配置类就生效并被注册到容器了，
 * 其中声明的 bean `helloWorld` 也注册到容器了，因此下面可以从容器中依赖查找到 bean `helloWorld`
 * <p>
 * 若未使用 @EnableHelloWorld 激活 HelloWorld 模块，自然依赖查找也就找不到 bean `helloWorld`
 *
 * @author mao  2021/5/30 4:36
 */
@EnableHelloWorld    // 激活 HelloWorld 模块
@EnableMyCaching    // 激活 MyCaching 模块
@EnableMyWeb    // 激活 MyWeb 模块
public class EnableModuleDemo {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        applicationContext.register(EnableModuleDemo.class);

        applicationContext.refresh();

        String helloWorld = applicationContext.getBean("helloWorld", String.class);
        System.out.println(helloWorld);

        String myCaching = applicationContext.getBean("myCaching", String.class);
        System.out.println(myCaching);

        String myWeb = applicationContext.getBean("myWeb", String.class);
        System.out.println(myWeb);

    }
}
