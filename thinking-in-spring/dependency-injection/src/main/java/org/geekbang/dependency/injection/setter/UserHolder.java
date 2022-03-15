package org.geekbang.dependency.injection.setter;

import org.geekbang.ioc.overview.lookup.domain.User;

/**
 * @author mao  2021/4/21 23:50
 */
public class UserHolder {
    public UserHolder() {
    }

    public UserHolder(User user) {
        this.user = user;
    }

    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "UserHolder{" +
                "user=" + user +
                '}';
    }
}
