package org.geekbang.bean.instantiation;

import org.geekbang.ioc.overview.lookup.domain.User;

/**
 * User 工厂类
 *
 * @author mao  2021/4/20 10:15
 */
public interface UserFactory {
    default User createUser() {
        return new User();
    }
}
