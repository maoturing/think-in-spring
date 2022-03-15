package org.geekbang.bean.instantiation;

import org.geekbang.ioc.overview.lookup.domain.User;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.FactoryBean;

/**
 * 通过实现 FactoryBean 接口实例化bean
 *
 * @author mao  2021/4/20 11:14
 */
public class UserFactoryBean implements FactoryBean {

    @Override
    public Object getObject() throws Exception {
        User user = new User();
        user.setId(6L);
        user.setName("Doomfist");
        return user;
    }

    @Override
    public Class<?> getObjectType() {
        return User.class;
    }
}
