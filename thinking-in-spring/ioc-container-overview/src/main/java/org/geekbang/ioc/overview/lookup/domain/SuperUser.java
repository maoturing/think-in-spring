package org.geekbang.ioc.overview.lookup.domain;

import org.geekbang.ioc.overview.lookup.annotation.Super;

/**
 * @author mao  2021/4/19 1:20
 */
@Super
public class SuperUser extends User {
    private String address;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + getId() +
                ", name='" + getName() +
                ", address='" + address + '\'' +
                '}';
    }
}
