package org.geekbang.resource;

import org.apache.commons.io.IOUtils;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.FileSystemResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.EncodedResource;

import java.io.IOException;
import java.io.Reader;

/**
 * 读取当前class文件内容
 * {@link FileSystemResourceLoader} 示例
 *
 * @author mao  2021/5/21 1:49
 */
public class EncodeFileSystemResourceLoaderDemo {
    public static void main(String[] args) throws IOException {
        String path = System.getProperty("user.dir")
                + "\\thinking-in-spring\\resource\\src\\main\\java\\org\\geekbang\\resource\\EncodeFileSystemResourceDemo.java";
        System.out.println(path);

        FileSystemResourceLoader resourceLoader = new FileSystemResourceLoader();
        // 本质是创建了一个 ClassPathContextResource
        Resource resource = resourceLoader.getResource(path);

        EncodedResource encodedResource = new EncodedResource(resource, "UTF-8");
        try (Reader reader = encodedResource.getReader()) {
            System.out.println(IOUtils.toString(reader));
        }
    }
}
