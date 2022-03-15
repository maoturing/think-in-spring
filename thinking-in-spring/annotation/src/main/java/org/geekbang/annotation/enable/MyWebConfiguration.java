package org.geekbang.annotation.enable;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author mao  2021/5/30 4:35
 */
@Configuration
public class MyWebConfiguration {
    @Bean
    public String myWeb() {
        return "MyWeb";
    }
}
