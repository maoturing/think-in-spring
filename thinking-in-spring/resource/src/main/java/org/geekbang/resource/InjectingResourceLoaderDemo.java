package org.geekbang.resource;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.EncodedResource;

import java.io.IOException;
import java.io.Reader;


/**
 * 注入 Resource 资源对象
 * 1.@Autowired 注入 ResourceLoader
 * 2.注入 ApplicationContext 作为 ResourceLoader
 * 3.实现 ResourceLoaderAware 回调方法
 *
 * @author mao  2021/5/21 2:31
 */
public class InjectingResourceLoaderDemo implements ResourceLoaderAware {

    public static final String LOCATION = "classpath:/hello.properties";

    // 1.@Autowired 注入 ResourceLoader
    @Autowired
    private ResourceLoader resourceLoader;

    // 2.注入 ApplicationContext 作为 ResourceLoader
    @Autowired
    private ApplicationContext applicationContext;

    public static void main(String[] args) throws IOException {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        applicationContext.register(InjectingResourceLoaderDemo.class);

        applicationContext.refresh();
        InjectingResourceLoaderDemo bean = applicationContext.getBean(InjectingResourceLoaderDemo.class);
        System.out.println(bean.resourceLoader);

        // 获取@Autowired注入的resourceLoader
        Resource resource = bean.resourceLoader.getResource(LOCATION);
        getContent(resource);

        // 通过@Autowired注入的ApplicationContext,获取resourceLoader
        Resource resource1 = bean.applicationContext.getResource(LOCATION);
        getContent(resource1);

        // 二者其实是同一个对象
        System.out.println(bean.resourceLoader == bean.applicationContext);
    }

    // 3. 实现ResourceLoaderAware回调方法, 加载resource, 打印内容
    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        Resource resource1 = resourceLoader.getResource(LOCATION);
        try {
            getContent(resource1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 打印资源的内容
    private static void getContent(Resource resource) throws IOException {
        EncodedResource encodedResource = new EncodedResource(resource);
        try (Reader reader = encodedResource.getReader()) {
            System.out.println(IOUtils.toString(reader));
        }
    }
}
