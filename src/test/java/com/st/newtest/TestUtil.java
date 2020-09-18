package com.st.newtest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.st.newtest.Entity.Config;
import com.st.newtest.Util.ConfigUtil;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestUtil {
    @Test
    public void test1() throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String a = "2020-8-31 19:12:00";
        String b = "2020-8-30 19:12:00";

        Date a1 = df.parse(a);
        Date b1 = df.parse(b);

        System.out.println(df.format(a1));

        System.out.println(a1);
        System.out.println(a1.getTime());
        System.out.println(b1.getTime());
        System.out.println(a1.getTime() - b1.getTime());
        System.out.println((a1.getTime() - b1.getTime()) / (1 * 1000) + "秒");
        System.out.println(((a1.getTime() - b1.getTime()) / (60 * 1000)) % 60 + "秒");
    }

    @Test
    public void test2() {
        Map<String, Object> map = new HashMap<>();
        map.put("actionType", "插入新的充值数据");
        Map<String, Object> map2 = new HashMap<>();
        map2.put("username", "久玩神途");
        map2.put("accountName", "jwst");
        map2.put("zoneName", "久玩神途1区");
        map2.put("chargeNum", 1000);
        map.put("actionData", map2);
        System.out.println(JSON.toJSONString(map));
//        System.out.println(map.get("123"));
//        String jsonmap = JSON.toJSONString(map);
//        Map<String, Object> jsonx = JSONObject.parseObject("da");
//        System.out.println(jsonx);
//        System.out.println(jsonx.get("actionData"));
    }

    @Test
    public void test3() throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(new ClassPathResource("config/chatConfig.json").getInputStream(), "UTF-8"));
        String str = null;
        StringBuffer content = new StringBuffer();
        while ((str = in.readLine()) != null) {
            content.append(str);
        }
        Map<String, Object> parse = JSONObject.parseObject(content.toString());
        System.out.println(content.toString());
        System.out.println(parse.get("keywords"));
        List<String> list = (List<String>) parse.get("keywords");
        for (String li : list) {
            System.out.println(li);
        }
        in.close();
    }

    @Test
    public void test4() {
        System.out.println("你好啊，杨大吊".contains("，杨大吊"));
    }

    @Test
    public void test5() {

    }
}
