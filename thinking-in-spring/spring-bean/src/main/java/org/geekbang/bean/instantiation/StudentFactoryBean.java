package org.geekbang.bean.instantiation;

import org.geekbang.bean.definition.component.Student;
import org.geekbang.ioc.overview.lookup.domain.User;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.stereotype.Component;

/**
 * 通过实现 FactoryBean 接口实例化bean
 * 使用@Component将其注册到容器
 *
 * @author mao  2021/4/20 11:14
 */
@Component("student")
public class StudentFactoryBean implements FactoryBean {

    public Object getObject() throws Exception {
        Student student = new Student();
        student.setId(2);
        student.setName("小明");
        return student;
    }

    public Class<?> getObjectType() {
        return Student.class;
    }
}
