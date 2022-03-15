package org.geekbang.bean.definition;

import org.geekbang.ioc.overview.lookup.domain.User;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;

/**
 * 注册 BeanDefinition 到容器示例
 * 1. 使用配置类的方式注册
 * @author mao  2021/4/20 0:34
 */
public class BeanRegisterConfigDemo {
    public static void main(String[] args) {
        // 1. 创建容器
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        // 2. 注册配置类, 即代替xml配置
        applicationContext.register(Config.class);
        // 3. 启动应用上下文
        applicationContext.refresh();

        User genji = applicationContext.getBean("genji", User.class);
        System.out.println(genji);

        // 关闭应用上下文
        applicationContext.close();
    }

    // 配置类, 类似于xml配置文件
    public static class Config {
        @Bean(name={"genji", "yuanshi"})
        public User user() {
            User user = new User();
            user.setId(1L);
            user.setName("源氏");

            return user;
        }
    }
}
