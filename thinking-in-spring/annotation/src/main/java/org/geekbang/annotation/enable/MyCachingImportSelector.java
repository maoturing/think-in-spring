package org.geekbang.annotation.enable;

import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @author mao  2021/5/30 4:46
 */
public class MyCachingImportSelector implements ImportSelector {
    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        // 导入指定的配置类
        return new String[]{"org.geekbang.annotation.enable.MyCachingConfiguration"};
    }
}
