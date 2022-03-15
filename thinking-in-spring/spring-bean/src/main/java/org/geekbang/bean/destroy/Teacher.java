package org.geekbang.bean.destroy;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * 设置 bean 初始化方法
 * 1. @PostConstruct 标记方法
 * 2. 实现InitializingBean#afterPropertiesSet方法
 * 3. 使用@Bean(initMethod="")指定初始化方法
 *
 * @author mao  2021/4/20 16:36
 */
public class Teacher implements InitializingBean, DisposableBean {
    public Teacher() {
        System.out.println("Teacher 构造函数....");
    }

    // 会在垃圾回收前被调用
    @Override
    protected void finalize() throws Throwable {
        System.out.println("Teacher 析构函数, 开始GC....");
    }

    // @PostConstruct 标记初始化方法
    @PostConstruct
    public void init() {
        System.out.println("@PostConstruct设置初始化方法: Teacher 初始化中...");
    }

    // 实现InitializingBean#afterPropertiesSet方法
    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("实现InitializingBean接口设置初始化方法: Teacher 初始化中...");
    }

    // 使用@Bean(initMethod="")标记该方法在初始化后调用
    public void initTeacher() {
        System.out.println("initMethod设置初始化方法: Teacher 初始化中...");
    }

    @PreDestroy
    public void preDestroy() {
        System.out.println("@PreDestroy设置销毁方法: Teacher 销毁中...");
    }

    @Override
    public void destroy() throws Exception {
        System.out.println("实现DisposableBean接口设置销毁方法: Teacher 销毁中...");
    }

    // 使用@Bean(destroyMethod="")标记该方法在销毁前调用
    public void destroyTeacher() {
        System.out.println("destroyMethod设置销毁方法: Teacher 销毁中...");
    }


}
