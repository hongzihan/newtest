package com.st.newtest.Util;

import org.springframework.core.io.ClassPathResource;

import java.io.*;

public class IOUtil {
    /**
     * 获取资源文件夹下的文件读取流
     * @param path
     * @return
     */
    public static BufferedReader getResourceBufferedReader(String path) {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(new ClassPathResource(path).getInputStream(), "UTF-8"));
            return in;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取资源文件夹下的文件输出流
     * @param path
     * @return
     */
    public static BufferedWriter getResourceBufferedWriter(String path) {
        try {
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new ClassPathResource(path).getFile()),"UTF-8"));
            return out;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
