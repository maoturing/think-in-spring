package org.geekbang.environment;


import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * 自定义条件Condition, 用于判断是否加载bean
 * @author mao  2021/6/4 7:09
 */
public class EvenProfileCondition implements Condition {
    /**
     * 如果当前激活的profile文件是even则返回true
     */
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        Environment environment = context.getEnvironment();
        return environment.acceptsProfiles("even");
    }
}
