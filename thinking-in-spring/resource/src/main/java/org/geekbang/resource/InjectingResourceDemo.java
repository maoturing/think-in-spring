package org.geekbang.resource;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.EncodedResource;

import java.io.IOException;
import java.io.Reader;


/**
 * 注入 Resource 资源对象
 * @author mao  2021/5/21 2:31
 */
public class InjectingResourceDemo {

    @Value("classpath:/hello.properties")
    private Resource helloResource;

    public static void main(String[] args) throws IOException {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        applicationContext.register(InjectingResourceDemo.class);

        applicationContext.refresh();
        InjectingResourceDemo bean = applicationContext.getBean(InjectingResourceDemo.class);
        System.out.println(bean.helloResource);

        EncodedResource encodedResource = new EncodedResource(bean.helloResource);
        try (Reader reader = encodedResource.getReader()) {
            System.out.println(IOUtils.toString(reader));
        }
    }
}
