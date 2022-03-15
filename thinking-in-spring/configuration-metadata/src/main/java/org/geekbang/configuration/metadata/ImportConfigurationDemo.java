package org.geekbang.configuration.metadata;

import org.geekbang.ioc.overview.lookup.domain.User;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Import;

/**
 * @Import 引入配置类, 将配置类下所有的bean注册到容器
 * @author mao  2021/5/20 1:33
 */
@Import(UserConfiguration.class)
public class ImportConfigurationDemo {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        applicationContext.register(ImportConfigurationDemo.class);

        applicationContext.refresh();
        User user = applicationContext.getBean(User.class);
        System.out.println(user);
    }
}
