package com.example.springbootfastdfs.utils;

import cn.hutool.core.util.StrUtil;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * @Classname: FileUtil
 * @Description: 文件工具类
 * @Date: 2022/1/12 10:05
 * @author: wwf
 */
public class FileUtil {

    // 文件转字节数组
    public static byte[] getFileBuff(File file) {
        byte[] buff = null;
        try {
            FileInputStream inputStream = new FileInputStream(file);
            buff = new byte[inputStream.available()];
            inputStream.read(buff);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return buff;
    }

    // 字节数组转文件
    public static void writeByteToFile(byte[] fileByte, String fileName) {
        File file = new File(fileName);
        try (FileOutputStream fos = new FileOutputStream(file);
             BufferedOutputStream bos = new BufferedOutputStream(fos)) {
            bos.write(fileByte);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 通过文件名称获取文件拓展名
    public static String getFileExtName(String name) {
        String ext_name = null;
        if (StrUtil.isNotEmpty(name)) {
            if (name.contains(".")) {
                ext_name = name.substring(name.indexOf(".") + 1);
            }
        }
        return ext_name;
    }


}
