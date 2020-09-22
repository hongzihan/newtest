package com.st.newtest.Util;

import java.util.HashMap;
import java.util.Map;

/**
 * 神途转发工具类，所有神途的插入类请求都会被在这里定义，在需要的时候通过
 * getForwardMap方法来获取
 */
public class stUtil {
    private static Map<String, String> forwardMap = null;

    private static Map<String, Integer> webActionTypeMap = null;

    private static void loaderForwardMap() {
        forwardMap = new HashMap<>();
        forwardMap.put("插入充值数据", "forward:/openSt/insertNewChargeData");
        forwardMap.put("插入怪物数据", "forward:/openSt/insertNewMonster");
        forwardMap.put("插入聊天记录", "forward:/openSt/insertNewChatRecord");
    }

    private static void loaderWebActionTypeMap() {
        webActionTypeMap = new HashMap<>();
        webActionTypeMap.put("wat_物品操作1", 1);
        webActionTypeMap.put("wat_物品操作2", 2);
        webActionTypeMap.put("wat_系统str", 3);
        webActionTypeMap.put("wat_系统int", 4);
        webActionTypeMap.put("wat_玩家str", 5);
        webActionTypeMap.put("wat_玩家int", 6);
        webActionTypeMap.put("wat_邮件操作", 7);
        webActionTypeMap.put("wat_给予操作", 8);
        webActionTypeMap.put("wat_模拟充值", 9);
        webActionTypeMap.put("wat_怪物操作", 10);
        webActionTypeMap.put("wat_消息操作", 11);
        webActionTypeMap.put("wat_装备复制", 12);
        webActionTypeMap.put("wat_变量转移", 13);
    }

    public static Map<String, String> getForwardMap() {
        if (forwardMap == null) {
            loaderForwardMap();
        }
        return forwardMap;
    }

    public static Map<String, Integer> getWebActionTypeMap() {
        if (webActionTypeMap == null) {
            loaderWebActionTypeMap();
        }
        return webActionTypeMap;
    }
}
