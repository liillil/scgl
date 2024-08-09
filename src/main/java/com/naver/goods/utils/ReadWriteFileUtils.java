package com.naver.goods.utils;

import java.io.*;
public class ReadWriteFileUtils {

    // 写入字节数组到文件
    public static void writeByte(byte[] data, File file) {
        try (FileOutputStream fos = new FileOutputStream(file);
             BufferedOutputStream bos = new BufferedOutputStream(fos)) {
            bos.write(data, 0, data.length);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 从文件读取字节数组
    public static byte[] readByte(File file) {
        byte[] data = null;
        try (FileInputStream fis = new FileInputStream(file);
             BufferedInputStream bis = new BufferedInputStream(fis)) {
            data = new byte[(int) file.length()];
            bis.read(data, 0, data.length);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }
}