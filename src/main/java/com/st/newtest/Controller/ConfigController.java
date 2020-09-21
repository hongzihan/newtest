package com.st.newtest.Controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.st.newtest.Entity.Config;
import com.st.newtest.Service.ConfigService;
import com.st.newtest.Util.CommonUtil;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author ken
 * @since 2020-09-18
 */
@Controller
@RequestMapping("/config")
public class ConfigController {
    @Autowired
    private ConfigService configService;

    private String zoneNameConfigName = "zoneNameConfig";

    private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @ResponseBody
    @RequestMapping("/insertZoneNameConfig")
    public String insertZoneNameConfig(String zoneName) {
        List<Config> list = configService.lambdaQuery().eq(Config::getConfigName, zoneNameConfigName).list();
        if (list == null || list.size() <= 0) {
            List<Map> zList = new ArrayList<>();
            Map<String, Object> map = new HashMap<>();
            map.put("zoneName", zoneName);
            map.put("recordTime", df.format(new Date()));
            zList.add(map);
            Config config = new Config();
            config.setConfigName(zoneNameConfigName);
            config.setConfigValue(JSON.toJSONString(zList));
            configService.save(config);
        } else {
            Config config = list.get(0);
            List<Map> zList = (List<Map>) JSONArray.parse(config.getConfigValue());
            Boolean flag = true;
            for (Map zMap : zList) {
                if (zMap.get("zoneName").equals(zoneName)) {
                    zMap.put("recordTime", df.format(new Date()));
                    flag = false;
                }
            }
            if (flag) {
                Map<String, Object> map = new HashMap<>();
                map.put("zoneName", zoneName);
                map.put("recordTime", df.format(new Date()));
                zList.add(map);
            }
            configService.lambdaUpdate().eq(Config::getConfigName, zoneNameConfigName).set(Config::getConfigValue,JSON.toJSONString(zList)).update();
        }
        return "success";
    }

    @RequiresRoles(value = {"supermanager","servicer","administrator"},logical= Logical.OR)
    @ResponseBody
    @RequestMapping("/deleteZoneNameConfig")
    public String deleteZoneNameConfig(@RequestParam("zoneNameList[]") List<String> zoneNameList) {
        List<Config> list = configService.lambdaQuery().eq(Config::getConfigName, zoneNameConfigName).list();
        if (!(list == null || list.size() <= 0)) {
            Config config = list.get(0);
            List<Map> zList = (List<Map>) JSONArray.parse(config.getConfigValue());
            for (String zoneName : zoneNameList) {
                for (Map zMap : zList) {
                    if (zMap.get("zoneName").equals(zoneName)) {
                        zList.remove(zMap);
                        break;
                    }
                }
            }

            configService.lambdaUpdate().eq(Config::getConfigName, zoneNameConfigName).set(Config::getConfigValue,JSON.toJSONString(zList)).update();
        }
        return "success";
    }

    @RequiresRoles(value = {"supermanager","servicer","administrator"},logical= Logical.OR)
    @RequestMapping("/zoneManage")
    public ModelAndView zoneManage() {
        ModelAndView mav = CommonUtil.getPage("zoneManage");
        List<Map> allZoneName = configService.findAllZoneName();
        mav.addObject("zoneManageList", allZoneName);
        return mav;
    }
}

