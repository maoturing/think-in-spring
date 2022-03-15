package org.geekbang.configuration.metadata;

import org.geekbang.ioc.overview.lookup.domain.User;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Import;

/**
 * @Import 引入普通的类, 将其作为 bean 注册到容器
 * @author mao  2021/5/20 1:33
 */
@Import(User.class)
public class ImportClassDemo {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        applicationContext.register(ImportClassDemo.class);

        applicationContext.refresh();
        User user = applicationContext.getBean(User.class);
        System.out.println(user);
    }
}
