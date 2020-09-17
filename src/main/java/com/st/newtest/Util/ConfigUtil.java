package com.st.newtest.Util;

import com.alibaba.fastjson.JSONObject;
import org.springframework.core.io.ClassPathResource;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

public class ConfigUtil {
    private static Map<String, Object> chatConfigMap = null;
    /**
     * 获取聊天监控配置map
     * @return
     * @throws IOException
     */
    public static void loadChatConfigMap() {
        if (chatConfigMap == null) {
            try {
                BufferedReader in = IOUtil.getResourceBufferedReader("config/chatConfig.json");
                String str = null;
                StringBuffer content = new StringBuffer();
                while ((str = in.readLine()) != null) {
                    content.append(str);
                }
                chatConfigMap = JSONObject.parseObject(content.toString());
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 用于匹配文本是否含有敏感词，若含有违规敏感字则返回true
     * @param content
     * @return
     */
    public static Boolean haveKeyword(String content) {
        if (chatConfigMap == null) {
            loadChatConfigMap();
        }
        List<String> keywords = getKeywords();
        for (String keyword : keywords) {
            if (content.contains(keyword)) {
                return true;
            }
        }
        return false;
    }

    public static int getPreReadTime() {
        if (chatConfigMap == null) {
            loadChatConfigMap();
        }
        return (int) chatConfigMap.get("preReadTime");
    }

    public static int getNewMsgInterval() {
        if (chatConfigMap == null) {
            loadChatConfigMap();
        }
        return (int) chatConfigMap.get("newMsgInterval");
    }

    public static List<String> getKeywords() {
        if (chatConfigMap == null) {
            loadChatConfigMap();
        }
        return (List<String>) chatConfigMap.get("keywords");
    }
}
