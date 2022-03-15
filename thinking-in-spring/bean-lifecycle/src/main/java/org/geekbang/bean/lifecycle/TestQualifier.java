package org.geekbang.bean.lifecycle;

import org.geekbang.ioc.overview.lookup.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * 测试@Qualifier注解的处理时机
 * @author mao  2021/5/1 11:34
 */
public class TestQualifier {
    @Autowired
    @Qualifier("user1")
    private User user2;
    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = getApplicationContext();
        applicationContext.refresh();
        TestQualifier bean = applicationContext.getBean(TestQualifier.class);
        System.out.println(bean.user2);

    }
    private static AnnotationConfigApplicationContext getApplicationContext() {
        // 创建容器
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        // 注册主类到容器
        applicationContext.register(TestQualifier.class);
        // 将xml中的两个bean注册到容器
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(applicationContext);
        String location = "test.xml";
        reader.loadBeanDefinitions(location);
        return applicationContext;
    }
}
