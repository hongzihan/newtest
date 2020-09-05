package com.st.newtest.Controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.st.newtest.Entity.Charge;
import com.st.newtest.Entity.MonsterDie;
import com.st.newtest.Service.OpenStService;
import com.st.newtest.Util.stForwardUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RequestMapping("/openSt")
@Controller
public class openForStController {

    @Autowired
    private OpenStService openStService;

    @ResponseBody
    @RequestMapping("/insertNewMonster")
    public String insertNewMonster(MonsterDie monsterDie) {
        openStService.insertNewMonster(monsterDie);
        return "success";
    }

    @ResponseBody
    @RequestMapping("/insertNewChargeData")
    public String insertNewChargeData(String jsonMsg) {
        Map<String, Object> parse = null;
        try {
            // 将json信息转为JSONObject
            parse = JSONObject.parseObject(jsonMsg);
            // 获取json内的actionType属性值
            Map<String, Object>  actionData = (Map<String, Object> ) parse.get("actionData");
            // 转化存储的jsonData值
            Charge charge = new Charge((String) actionData.get("username"), (String) actionData.get("accountName"), (String) actionData.get("zoneName"), (Integer) actionData.get("chargeNum"));
            openStService.insertNewChargeData(charge);
        } catch (Exception e) {
            e.printStackTrace();
            return "failed";
        }
        //openStService.insertNewChargeData(charge);
        return "success";
    }

    @RequestMapping("/main")
    public String main(String jsonMsg) {
        Map<String, Object> parse = null;
        String forwarUrl = "error404";
        try {
            // 将json信息转为JSONObject
            parse = JSONObject.parseObject(jsonMsg);
            // 获取json内的actionType属性值
            String actionType = (String) parse.get("actionType");
            // 获取转发map
            Map<String, String> forwardMap = stForwardUtil.getForwardMap();
            // 通过转发map获取到转发的请求url
            if (forwardMap.get(actionType) != null) {
                forwarUrl = forwardMap.get(actionType);
            }
        } catch (Exception e) {
            System.out.println("异常json，不予理会");
            e.printStackTrace();
        }
        return forwarUrl;
    }
}
