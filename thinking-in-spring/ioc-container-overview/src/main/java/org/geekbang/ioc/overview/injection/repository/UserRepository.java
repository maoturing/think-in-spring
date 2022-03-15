package org.geekbang.ioc.overview.injection.repository;

import org.geekbang.ioc.overview.lookup.domain.User;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.context.ApplicationContext;

import java.util.Collection;

/**
 * 用户信息仓库
 * @author mao  2021/4/19 10:51
 */
public class UserRepository {
    private Collection<User> users;         // 自定义 bean
    private BeanFactory beanFactory;        // 内建非 bean 对象, xml设置的按类型依赖注入, 会自动注入容器中BeanFactory对象到该属性
    private ObjectFactory<ApplicationContext> objectFactory;    //

    public ObjectFactory<ApplicationContext> getObjectFactory() {
        return objectFactory;
    }

    public void setObjectFactory(ObjectFactory<ApplicationContext> objectFactory) {
        this.objectFactory = objectFactory;
    }

    public BeanFactory getBeanFactory() {
        return beanFactory;
    }

    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public Collection<User> getUsers() {
        return users;
    }

    public void setUsers(Collection<User> users) {
        this.users = users;
    }
}
