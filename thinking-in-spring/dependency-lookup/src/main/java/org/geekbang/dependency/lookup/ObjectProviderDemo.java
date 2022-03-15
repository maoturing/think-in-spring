package org.geekbang.dependency.lookup;

import org.geekbang.ioc.overview.lookup.domain.User;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;

/**
 * 通过 ObjectProvider 进行延迟依赖查找
 *
 * @author mao  2021/4/21 0:10
 */
public class ObjectProviderDemo {
    public static void main(String[] args) {
        // 1.创建 ApplicationContext 容器, 使用注解配置
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        // 2.将当前类作为配置类
        applicationContext.register(ObjectProviderDemo.class);
        // 3.启动应用上下文
        applicationContext.refresh();

        // 4.依赖查找
        lookupByObjectProvider(applicationContext);
        lookupByIfAvaiable(applicationContext);

        // 5.停止应用上下文
        applicationContext.close();
    }

    /**
     * ObjectProvider 接口的依赖查找示例
     *
     * @param applicationContext
     */
    private static void lookupByIfAvaiable(AnnotationConfigApplicationContext applicationContext) {
        // 查找User类型的bean, 肯定找不到
        ObjectProvider<User> objectProvider = applicationContext.getBeanProvider(User.class);

        // 1.若找不到bean, 返回null
        User user = objectProvider.getIfAvailable();
        System.out.println("bean不存在返回null: " + user);

        // 2.若找不到bean, 返回默认bean
        User user2 = objectProvider.getIfAvailable(() -> new User());
        System.out.println("bean不存在返回默认bean: " + user2);

        // 3. 若找不到bean, 抛出异常
        System.out.println(objectProvider.getObject());
    }

    @Bean
    public String helloWorld() {
        return "HelloWorld";
    }

    private static void lookupByObjectProvider(AnnotationConfigApplicationContext applicationContext) {
        ObjectProvider<String> objectProvider = applicationContext.getBeanProvider(String.class);
        String hello = objectProvider.getObject();

        System.out.println("getObject: " + hello);
    }
}
