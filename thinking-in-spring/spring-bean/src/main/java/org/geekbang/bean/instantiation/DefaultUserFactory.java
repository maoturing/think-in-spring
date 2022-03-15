package org.geekbang.bean.instantiation;

import org.geekbang.ioc.overview.lookup.domain.User;
import org.springframework.core.annotation.Order;

import javax.annotation.PostConstruct;

/**
 * bean工厂, 通过该工厂创建bean
 *
 * @author mao  2021/4/20 10:16
 */
public class DefaultUserFactory implements UserFactory{
    @Override
    public User createUser() {
        User user = new User();
        user.setId(5L);
        user.setName("Echo");
        return user;
    }
}
