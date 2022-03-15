package org.geekbang.ioc.overview.lookup.domain;

/**
 * @author mao  2021/4/19 0:20
 */
public class User {
    private Long id;
    private String name;

    // 静态工厂方法, 配置到xml factory-method, 通过该方法实例化 bean
    public static User createUser() {
        User user = new User();
        user.setId(3L);
        user.setName("Zarya");
        return user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
