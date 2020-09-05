package com.st.newtest.Util;

import java.util.HashMap;
import java.util.Map;

/**
 * 神途转发工具类，所有神途的插入类请求都会被在这里定义，在需要的时候通过
 * getForwardMap方法来获取
 */
public class stForwardUtil {
    private static Map<String, String> forwardMap = null;

    private static void loaderMap() {
        forwardMap = new HashMap<>();
        forwardMap.put("插入充值数据", "forward:/openSt/insertNewChargeData");
        forwardMap.put("插入怪物数据", "forward:/openSt/insertNewMonster");
    }

    public static Map<String, String> getForwardMap() {
        if (forwardMap == null) {
            loaderMap();
        }
        return forwardMap;
    }
}
