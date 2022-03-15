package org.geekbang.bean.juejin;

import org.geekbang.bean.juejin.bean.Person;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author mao  2021/5/7 22:18
 */
@Configuration
@ComponentScan("org.geekbang.bean.juejin.bean")
public class LifecycleSourceConfiguration {

    @Bean(initMethod = "initMethod", destroyMethod = "destroyMethod")
    public Person person() {
        Person person = new Person();
        person.setName("lisi");
        return person;
    }
}