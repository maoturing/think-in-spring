package org.geekbang.dependency.injection.type;

import org.geekbang.dependency.injection.setter.UserHolder;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

/**
 * 基础类型的注入与类型转换
 *
 * @author mao  2021/4/21 23:45
 */
public class XmlDependencyTypeInjectionDemo {
    public static void main(String[] args) throws IOException {
        BeanFactory beanFactory = new ClassPathXmlApplicationContext("dependcy-type-injection.xml");

        Docter docter = beanFactory.getBean("docter", Docter.class);
        System.out.println("docter: " + docter);

        boolean flag = (docter.getCity() instanceof CityEnum);
        System.out.println("city属性值类型为CityEnum： " + flag);


        Resource resource = docter.getResource();
        System.out.println("URL: " + resource.getURL().toString());

        System.out.println("========集合类型的配置=========");
        CityEnum[] workCitys = docter.getWorkCitys();
        System.out.println("workCitys数组:" + workCitys[0] + ", " + workCitys[1]);

        List<CityEnum> lifeCitys = docter.getLifeCitys();
        System.out.println("lifeCitys集合: " + lifeCitys);
    }
}
