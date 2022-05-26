package com.jun.chu.java.util;

import lombok.experimental.UtilityClass;
import org.apache.commons.io.FileUtils;
import sun.misc.ProxyGenerator;

import java.io.File;
import java.io.IOException;

/**
 * @author chujun
 * @date 2022/5/26
 */
@UtilityClass
public class ProxyClassUtils {
    /**
     * 可以查看代理类生成的内容
     */
    public static void generateProxyClass(String path, String proxyClassName, Class<?>[] interfaces) {
        byte[] bytes = ProxyGenerator.generateProxyClass(proxyClassName, interfaces);
        try {
            FileUtils.writeByteArrayToFile(new File(path + "/" + proxyClassName + ".class"), bytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
