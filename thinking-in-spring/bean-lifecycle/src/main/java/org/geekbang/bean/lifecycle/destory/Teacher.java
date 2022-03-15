package org.geekbang.bean.lifecycle.destory;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * 设置 bean 销毁前方法
 * 1. @PreDestroy 标记方法
 * 2. 实现DisposableBean#destroy方法
 * 3. 使用@Bean(destroyMethod="")指定初始化方法
 *
 * @author mao  2021/4/20 16:36
 */
public class Teacher implements DisposableBean {
    public Teacher() {
        System.out.println("Teacher 构造函数....");
    }

    // 会在垃圾回收前被调用
    @Override
    protected void finalize() throws Throwable {
        System.out.println("Teacher 析构函数, 开始GC....");
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
