package com.st.newtest.Util;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.st.newtest.Entity.Config;
import com.st.newtest.Mapper.ConfigMapper;
import com.st.newtest.Service.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

@Component
public class ConfigUtil {
    private static Map<String, Object> chatConfigMap = null;

    @Autowired
    private static ConfigService configService;

    @Autowired
    private ConfigService configService2;

    private static String chatConfigName = "chatConfig";

    @PostConstruct
    public void init() {
        configService = configService2;
        loadChatConfigMap();
    }
    /**
     * 获取聊天监控配置map
     * @return
     * @throws IOException
     */
    public static void loadChatConfigMap() {
        if (chatConfigMap == null) {
            List<Config> configList = configService.lambdaQuery().eq(Config::getConfigName, chatConfigName).list();
            if (configList == null || configList.size() <= 0) {
                try {
                    BufferedReader in = IOUtil.getResourceBufferedReader("config/chatConfig.json");
                    String str = null;
                    StringBuffer content = new StringBuffer();
                    while ((str = in.readLine()) != null) {
                        content.append(str);
                    }
                    chatConfigMap = JSONObject.parseObject(content.toString());
                    Config config = new Config();
                    config.setConfigName(chatConfigName);
                    config.setConfigValue(content.toString());
                    configService.save(config);
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                chatConfigMap = JSONObject.parseObject(configList.get(0).getConfigValue());
            }

        }
    }

    /**
     * 用于匹配文本是否含有敏感词，若含有违规敏感字则返回true
     * @param content
     * @return
     */
    public static Boolean haveKeyword(String content) {
        loadChatConfigMap();
        List<String> keywords = getChatKeywords();
        for (String keyword : keywords) {
            if (content.contains(keyword)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取预读取的聊天时间
     * @return
     */
    public static int getChatPreReadTime() {
        loadChatConfigMap();
        return (int) chatConfigMap.get("preReadTime");
    }

    /**
     * 设置预读取的聊天时间
     * 例如 72000 就代表预读取2小时
     * @param preReadTime
     * @return
     */
    public static Boolean setChatPreReadTime(int preReadTime) {
        loadChatConfigMap();
        List<Config> configList = configService.lambdaQuery().eq(Config::getConfigName, chatConfigName).list();
        Config config = configList.get(0);
        Map<String, Object> map = JSONObject.parseObject(config.getConfigValue());
        map.put("preReadTime", preReadTime);
        chatConfigMap = map;
        configService.lambdaUpdate().eq(Config::getConfigName, chatConfigName).set(Config::getConfigValue, JSONObject.toJSONString(map)).update();
        return true;
    }

    /**
     * 获取聊天的间隔时间
     * @return
     */
    public static int getChatNewMsgInterval() {
        loadChatConfigMap();
        return (int) chatConfigMap.get("newMsgInterval");
    }

    /**
     * 设置获取聊天的间隔时间
     * @param newMsgInterval
     * @return
     */
    public static Boolean setChatNewMsgInterval(int newMsgInterval) {
        loadChatConfigMap();
        List<Config> configList = configService.lambdaQuery().eq(Config::getConfigName, chatConfigName).list();
        Config config = configList.get(0);
        Map<String, Object> map = JSONObject.parseObject(config.getConfigValue());
        map.put("newMsgInterval", newMsgInterval);
        chatConfigMap = map;
        configService.lambdaUpdate().eq(Config::getConfigName, chatConfigName).set(Config::getConfigValue, JSONObject.toJSONString(map)).update();
        return true;
    }

    /**
     * 获取敏感词列表
     * @return
     */
    public static List<String> getChatKeywords() {
        loadChatConfigMap();
        return (List<String>) chatConfigMap.get("keywords");
    }

    /**
     * 增加敏感词
     * @param keyword
     * @return
     */
    public static Boolean addChatKeywords(String keyword) {
        loadChatConfigMap();
        List<Config> configList = configService.lambdaQuery().eq(Config::getConfigName, chatConfigName).list();
        Config config = configList.get(0);
        Map<String, Object> map = JSONObject.parseObject(config.getConfigValue());
        List<String> keywordList = (List<String>) map.get("keywords");
        keywordList.add(keyword);
        map.put("keywords", keywordList);
        chatConfigMap = map;
        configService.lambdaUpdate().eq(Config::getConfigName, chatConfigName).set(Config::getConfigValue, JSONObject.toJSONString(map)).update();
        return true;
    }

    /**
     * 删除敏感词
     * @param keyword
     * @return
     */
    public static Boolean delChatKeywords(String keyword) {
        loadChatConfigMap();
        List<Config> configList = configService.lambdaQuery().eq(Config::getConfigName, chatConfigName).list();
        Config config = configList.get(0);
        Map<String, Object> map = JSONObject.parseObject(config.getConfigValue());
        List<String> keywordList = (List<String>) map.get("keywords");
        keywordList.remove(keyword);
        map.put("keywords", keywordList);
        chatConfigMap = map;
        configService.lambdaUpdate().eq(Config::getConfigName, chatConfigName).set(Config::getConfigValue, JSONObject.toJSONString(map)).update();
        return true;
    }
}
