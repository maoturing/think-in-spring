package org.geekbang.bean.initialization;

import org.springframework.beans.factory.InitializingBean;

import javax.annotation.PostConstruct;

/**
 * 设置 bean 初始化方法
 * 1. @PostConstruct 标记方法
 * 2. 实现InitializingBean#afterPropertiesSet方法
 * 3. 使用@Bean(initMethod="")指定初始化方法
 *
 * @author mao  2021/4/20 16:36
 */
public class Teacher implements InitializingBean {

    public Teacher() {
        System.out.println("Teacher 构造函数....");
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

    // 使用@Bean(initMethod="")标记该方法为初始化方法
    public void initTeacher() {
        System.out.println("initMethod设置初始化方法: Teacher 初始化中...");
    }
}
