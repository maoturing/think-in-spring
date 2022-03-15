package org.geekbang.configuration.metadata;

import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertySourceFactory;

import java.io.IOException;
import java.util.Properties;

/**
 * @author mao  2021/5/20 5:15
 */
public class YamlPropertySourceFactory implements PropertySourceFactory {
    /**
     *
     * @param name yml属性源名称, 在@PropertySource中设置
     * @param resource yml属性源, 在@PropertySource中设置
     * @return
     * @throws IOException
     */
    @Override
    public PropertySource<?> createPropertySource(String name, EncodedResource resource) throws IOException {
        YamlPropertiesFactoryBean factoryBean = new YamlPropertiesFactoryBean();
        factoryBean.setResources(resource.getResource());
        Properties yamlProperties = factoryBean.getObject();

        return new PropertiesPropertySource(name, yamlProperties);
    }
}
