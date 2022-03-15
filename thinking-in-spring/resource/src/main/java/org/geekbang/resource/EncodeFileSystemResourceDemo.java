package org.geekbang.resource;

import org.apache.commons.io.IOUtils;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.support.EncodedResource;


import java.io.IOException;
import java.io.Reader;

/**
 * 读取当前class文件内容
 * {@link FileSystemResource} 示例
 * @author mao  2021/5/21 1:49
 */
public class EncodeFileSystemResourceDemo {
    public static void main(String[] args) throws IOException {
        String path = System.getProperty("user.dir")
                + "\\thinking-in-spring\\resource\\src\\main\\java\\org\\geekbang\\resource\\EncodeFileSystemResourceDemo.java";
        System.out.println(path);

        FileSystemResource fileSystemResource = new FileSystemResource(path);
        EncodedResource encodedResource = new EncodedResource(fileSystemResource, "UTF-8");
        try (Reader reader = encodedResource.getReader()) {
            System.out.println(IOUtils.toString(reader));
        }
    }
}
