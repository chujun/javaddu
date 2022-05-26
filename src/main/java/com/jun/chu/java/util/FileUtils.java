package com.jun.chu.java.util;

import lombok.experimental.UtilityClass;

import java.io.File;
import java.io.IOException;

/**
 * @author chujun
 * @date 2022/5/26
 */
@UtilityClass
public class FileUtils {
    public void writeByteArrayToFile(byte[] bytes, File file) {
        try {
            org.apache.commons.io.FileUtils.writeByteArrayToFile(file, bytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
