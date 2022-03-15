package org.geekbang.configuration.metadata;

import org.geekbang.ioc.overview.lookup.domain.User;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;

import java.util.Map;

/**
 * @ImportResource 导入配置文件中所有的bean
 * @author mao  2021/5/20 1:27
 */
@ImportResource("classpath:/dependcy-lookup-context.xml")
public class ImportResourceDemo {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        applicationContext.register(ImportResourceDemo.class);

        applicationContext.refresh();
        Map<String, User> userMap = applicationContext.getBeansOfType(User.class);
        for (Map.Entry<String, User> userEntry : userMap.entrySet()) {
            System.out.println(userEntry.getKey() + ": "+ userEntry.getValue());
        }
    }
}
