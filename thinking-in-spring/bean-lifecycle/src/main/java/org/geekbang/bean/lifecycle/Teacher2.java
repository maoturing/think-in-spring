package org.geekbang.bean.lifecycle;

import org.springframework.beans.factory.SmartInitializingSingleton;

/**
 * @author mao  2021/5/13 2:10
 */
public class Teacher2 implements SmartInitializingSingleton {
    private String name;

    @Override
    public void afterSingletonsInstantiated() {
        this.name = "Winston";
        System.out.println("实现SmartInitializingSingleton接口设置初始化完成时回调方法: Teacher 初始化完成...");
    }

    @Override
    public String toString() {
        return "Teacher2{" +
                "name='" + name + '\'' +
                '}';
    }
}
