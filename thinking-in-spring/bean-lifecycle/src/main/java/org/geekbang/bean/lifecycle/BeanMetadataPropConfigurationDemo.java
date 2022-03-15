package org.geekbang.bean.lifecycle;

import jdk.internal.org.objectweb.asm.tree.MultiANewArrayInsnNode;
import org.geekbang.dependency.injection.type.Docter;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.PropertiesBeanDefinitionReader;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

/**
 * bean 元信息配置示例
 *
 * @author mao  2021/4/28 20:18
 */
public class BeanMetadataPropConfigurationDemo {
    public static void main(String[] args) {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        PropertiesBeanDefinitionReader beanDefinitionReader = new PropertiesBeanDefinitionReader(beanFactory);
        String location = "docter.properties";
        // 创建资源
        Resource resource = new ClassPathResource(location);

        // 加载 properties 中的bean配置
        int count = beanDefinitionReader.loadBeanDefinitions(resource);

        System.out.println("已加载BeanDefinition数量: " + count);
        Docter docter = beanFactory.getBean(Docter.class);
        System.out.println(docter);
    }
}
