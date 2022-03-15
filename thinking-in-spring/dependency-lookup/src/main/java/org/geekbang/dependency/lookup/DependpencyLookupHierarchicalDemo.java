package org.geekbang.dependency.lookup;

import org.geekbang.ioc.overview.lookup.domain.SuperUser;
import org.geekbang.ioc.overview.lookup.domain.User;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.HierarchicalBeanFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;

import java.util.Map;


/**
 * 层次性查找示例
 *
 * @author mao  2021/4/21 10:39
 */
public class DependpencyLookupHierarchicalDemo {

    public static void main(String[] args) {
        // 1.创建应用上下文, 使用注解配置, 并启动
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        applicationContext.register(DependpencyLookupHierarchicalDemo.class);

        // 2.获取 HierarchicalBeanFactory 的实现类 ConfigurableListableBeanFactory
        // HierarchicalBeanFactory -> ConfigurableBeanFactory -> ConfigurableListableBeanFactory
        ConfigurableListableBeanFactory beanFactory = applicationContext.getBeanFactory();
        System.out.println("当前BeanFactory的父容器ParentBeanFactory: " + beanFactory.getParentBeanFactory());

        // 3.设置 parent BeanFactory
        HierarchicalBeanFactory parentBeanFactory = createParentBeanFactory();
        beanFactory.setParentBeanFactory(parentBeanFactory);
        System.out.println("当前BeanFactory的父容器ParentBeanFactory: " + beanFactory.getParentBeanFactory());

        // 4.判断bean在本地容器还是在父容器
        displayLocalBean(beanFactory, "user", "本地容器LocalBeanFactory");
        displayLocalBean(parentBeanFactory, "user", "父容器ParentBeanFactory");

        // 注解bean在容器启动时才会加载
        applicationContext.refresh();
        displayLocalBean(beanFactory, "hello", "本地容器LocalBeanFactory");

        // 5.层次性查找bean是否在当前容器及其祖先容器
        System.out.println("====层次性查找bean是否在当前容器及其祖先容器=====");
        displayBean(beanFactory, "user");

        // 6.使用BeanFactoryUtils查找当前容器及其祖先容器是否存在bean
        Map<String, User> users = BeanFactoryUtils.beansOfTypeIncludingAncestors(beanFactory, User.class);
        System.out.println("user类型bean列表: " + users);

        // 查找User类型的一个bean, 若存在多个抛出异常
//        BeanFactoryUtils.beanOfTypeIncludingAncestors(beanFactory, User.class);
    }

    @Bean
    public String hello() {
        return "HelloWorld";
    }

    private static void displayBean(HierarchicalBeanFactory beanFactory, String beanName) {
        System.out.println("层次性查找当前容器是否包含bean=" + beanName + ": " + containsBean(beanFactory, beanName));
    }

    /**
     * 递归查询容器及其父容器是否存在bean
     *
     * @param beanFactory
     * @param beanName
     * @return
     */
    private static boolean containsBean(HierarchicalBeanFactory beanFactory, String beanName) {
        // 获取父容器
        BeanFactory parentBeanFactory = beanFactory.getParentBeanFactory();
        // 父容器是HierarchicalBeanFactory类型, 正面其还有父容器
        if (parentBeanFactory instanceof HierarchicalBeanFactory) {
            HierarchicalBeanFactory hierarchicalBeanFactory = (HierarchicalBeanFactory) parentBeanFactory;
            // 递归调用, 判断父容器中是否包含bean
            if (containsBean(hierarchicalBeanFactory, beanName)) {
                // 父容器中包含bean, 返回true
                return true;
            }
        }
        // 返回当前容器是否包含bean
        return beanFactory.containsLocalBean(beanName);
    }

    private static void displayLocalBean(HierarchicalBeanFactory beanFactory, String beanName, String BeanFactoryMode) {
        if (beanFactory.containsLocalBean(beanName)) {
            Object bean = beanFactory.getBean(beanName);
            System.out.println(BeanFactoryMode + "存在bean: " + beanName + ", " + bean);
        } else {
            System.out.println(BeanFactoryMode + "不存在bean: " + beanName);
        }
    }

    /**
     * 创建一个容器 BeanFactory, 这里使用的是 ioc 项目中的 xml 文件创建 BeanFactory,
     * 该xml中定义了user等bean
     * 当前项目引入了 ioc 项目的依赖, 所以可以直接使用其中的 xml 文件和类
     *
     * @return
     */
    private static HierarchicalBeanFactory createParentBeanFactory() {
        // 创建BeanFactory容器
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(beanFactory);
        // bean 配置文件路径
        String location = "dependcy-lookup-context.xml";
        // 加载xml配置文件中的bean
        reader.loadBeanDefinitions(location);

        return beanFactory;
    }
}
