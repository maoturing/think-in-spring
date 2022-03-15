package org.geekbang.configuration.metadata;

import org.geekbang.ioc.overview.lookup.domain.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author mao  2021/5/20 1:39
 */
@Configuration
public class UserConfiguration {

    @Bean("user")
    public User createUser() {
        User user = new User();
        user.setId(9L);
        user.setName("Sombra");
        return user;
    }
}
