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
import java.time.*;
import java.time.format.DateTimeFormatter;
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
        //获取当前时区的日期
        LocalDate localDate = LocalDate.now();
        System.out.println("localDate: " + localDate);
        //时间
        LocalTime localTime = LocalTime.now();
        System.out.println("localTime: " + localTime);
        //根据上面两个对象，获取日期时间
        LocalDateTime localDateTime = LocalDateTime.of(localDate,localTime);
        System.out.println("localDateTime: " + localDateTime);
        //使用静态方法生成此对象
        LocalDateTime localDateTime2 = LocalDateTime.now();
        System.out.println("localDateTime2: " + localDateTime2);
        //格式化时间
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm:ss");
        System.out.println("格式化之后的时间: " + localDateTime2.format(formatter));

        //转化为时间戳(秒)
        long epochSecond = localDateTime2.toEpochSecond(ZoneOffset.of("+8"));
        //转化为毫秒
        long epochMilli = localDateTime2.atZone(ZoneOffset.systemDefault()).toInstant().toEpochMilli();
        System.out.println("时间戳为:(秒) " + epochSecond + "; (毫秒): " + epochMilli);

        //时间戳(毫秒)转化成LocalDateTime
        Instant instant = Instant.ofEpochMilli(epochMilli);
        LocalDateTime localDateTime3 = LocalDateTime.ofInstant(instant, ZoneOffset.systemDefault());
        System.out.println("时间戳(毫秒)转化成LocalDateTime: " + localDateTime3.format(formatter));
        //时间戳(秒)转化成LocalDateTime
        Instant instant2 = Instant.ofEpochSecond(epochSecond);
        LocalDateTime localDateTime4 = LocalDateTime.ofInstant(instant2, ZoneOffset.systemDefault());
        System.out.println("时间戳(秒)转化成LocalDateTime: " + localDateTime4.format(formatter));

    }

    @Test
    public void test6() {
        //LocalDateTime ldt = "2020-09-23 17:04:57.590000";
    }

}
