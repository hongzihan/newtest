package com.st.newtest;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.st.newtest.Entity.Config;
import com.st.newtest.Timer.CheckPriceTimer;
import com.st.newtest.Util.ConfigUtil;
import com.st.newtest.poeGame.Entity.PoeItemPrice;
import com.st.newtest.poeGame.Service.PoeItemPriceService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

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
        System.out.println(100/10);
        System.out.println(100/3);
        System.out.println(100/1000);
        try {
            int x = 99/0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("继续");
    }

    public int Recursion1(int num) {
        if (num == 0) {
            return 0;
        } else {
            return num + Recursion1(num - 1);
        }
    }

    @Test
    public void test7() {
        System.out.println(Recursion1(100));
        System.out.println(CheckPriceTimer.exaltedPrice);
    }

    @Test
    public void test8() throws InterruptedException {
        JSONArray jsonArray = JSON.parseArray("[{\"type\":\"炼狱呼嚎\",\"text\":\"炼狱呼嚎\"},{\"type\":\"先祖战吼\",\"text\":\"先祖战吼\"},{\"type\":\"先祖战士长\",\"text\":\"先祖战士长\"},{\"type\":\"愤怒\",\"text\":\"愤怒\"},{\"type\":\"幻化守卫\",\"text\":\"幻化守卫\"},{\"type\":\"幻化武器\",\"text\":\"幻化武器\"},{\"type\":\"电弧\",\"text\":\"电弧\"},{\"type\":\"奥术斗篷\",\"text\":\"奥术斗篷\"},{\"type\":\"奥法烙印\",\"text\":\"奥法烙印\"},{\"type\":\"极地装甲\",\"text\":\"极地装甲\"},{\"type\":\"电光寒霜\",\"text\":\"电光寒霜\"},{\"type\":\"末日烙印\",\"text\":\"末日烙印\"},{\"type\":\"火力弩炮\",\"text\":\"火力弩炮\"},{\"type\":\"天雷之珠\",\"text\":\"天雷之珠\"},{\"type\":\"弹幕\",\"text\":\"弹幕\"},{\"type\":\"捕熊陷阱\",\"text\":\"捕熊陷阱\"},{\"type\":\"盛怒\",\"text\":\"盛怒\"},{\"type\":\"乱剑穿心\",\"text\":\"乱剑穿心\"},{\"type\":\"虚空刀雨\",\"text\":\"虚空刀雨\"},{\"type\":\"剑刃风暴\",\"text\":\"剑刃风暴\"},{\"type\":\"飞刃风暴\",\"text\":\"飞刃风暴\"},{\"type\":\"爆裂箭雨\",\"text\":\"爆裂箭雨\"},{\"type\":\"怒炎穿心\",\"text\":\"怒炎穿心\"},{\"type\":\"枯萎\",\"text\":\"枯萎\"},{\"type\":\"闪现射击\",\"text\":\"闪现射击\"},{\"type\":\"血与沙\",\"text\":\"血与沙\"},{\"type\":\"鲜血狂怒\",\"text\":\"鲜血狂怒\"},{\"type\":\"亵渎之矛\",\"text\":\"亵渎之矛\"},{\"type\":\"骸骨奉献\",\"text\":\"骸骨奉献\"},{\"type\":\"燃烧箭矢\",\"text\":\"燃烧箭矢\"},{\"type\":\"钩链攻击\",\"text\":\"钩链攻击\"},{\"type\":\"刀刃乱舞\",\"text\":\"刀刃乱舞\"},{\"type\":\"蓄力疾风闪\",\"text\":\"蓄力疾风闪\"},{\"type\":\"清晰\",\"text\":\"清晰\"},{\"type\":\"劈砍\",\"text\":\"劈砍\"},{\"type\":\"毒蛇鞭击\",\"text\":\"毒蛇鞭击\"},{\"type\":\"冰霜净化\",\"text\":\"冰霜净化\"},{\"type\":\"霜暴\",\"text\":\"霜暴\"},{\"type\":\"导电\",\"text\":\"导电\"},{\"type\":\"奉献之路\",\"text\":\"奉献之路\"},{\"type\":\"瘟疫\",\"text\":\"瘟疫\"},{\"type\":\"迷魅陷阱\",\"text\":\"迷魅陷阱\"},{\"type\":\"号召\",\"text\":\"号召\"},{\"type\":\"火葬\",\"text\":\"火葬\"},{\"type\":\"灵体转换\",\"text\":\"灵体转换\"},{\"type\":\"电殛长枪\",\"text\":\"电殛长枪\"},{\"type\":\"暗影印记\",\"text\":\"暗影印记\"},{\"type\":\"旋风斩\",\"text\":\"旋风斩\"},{\"type\":\"怨毒光环\",\"text\":\"怨毒光环\"},{\"type\":\"暗夜血契\",\"text\":\"暗夜血契\"},{\"type\":\"混沌之毒\",\"text\":\"混沌之毒\"},{\"type\":\"冲刺\",\"text\":\"冲刺\"},{\"type\":\"诱饵图腾\",\"text\":\"诱饵图腾\"},{\"type\":\"亵渎\",\"text\":\"亵渎\"},{\"type\":\"坚定\",\"text\":\"坚定\"},{\"type\":\"爆灵术\",\"text\":\"爆灵术\"},{\"type\":\"引爆地雷\",\"text\":\"引爆地雷\"},{\"type\":\"吞噬图腾\",\"text\":\"吞噬图腾\"},{\"type\":\"解放\",\"text\":\"解放\"},{\"type\":\"纪律\",\"text\":\"纪律\"},{\"type\":\"圣怨\",\"text\":\"圣怨\"},{\"type\":\"霸气之击\",\"text\":\"霸气之击\"},{\"type\":\"双重打击\",\"text\":\"双重打击\"},{\"type\":\"恐怖之旗\",\"text\":\"恐怖之旗\"},{\"type\":\"双持打击\",\"text\":\"双持打击\"},{\"type\":\"震地\",\"text\":\"震地\"},{\"type\":\"尖刺战吼\",\"text\":\"尖刺战吼\"},{\"type\":\"元素打击\",\"text\":\"元素打击\"},{\"type\":\"元素要害\",\"text\":\"元素要害\"},{\"type\":\"坚决战吼\",\"text\":\"坚决战吼\"},{\"type\":\"衰弱\",\"text\":\"衰弱\"},{\"type\":\"诱捕之箭\",\"text\":\"诱捕之箭\"},{\"type\":\"灵魂吸取\",\"text\":\"灵魂吸取\"},{\"type\":\"虚空匕首\",\"text\":\"虚空匕首\"},{\"type\":\"爆炸箭矢\",\"text\":\"爆炸箭矢\"},{\"type\":\"火球\",\"text\":\"火球\"},{\"type\":\"灼热光线\",\"text\":\"灼热光线\"},{\"type\":\"火屑地雷\",\"text\":\"火屑地雷\"},{\"type\":\"火焰净化\",\"text\":\"火焰净化\"},{\"type\":\"烈炎风暴\",\"text\":\"烈炎风暴\"},{\"type\":\"火焰陷阱\",\"text\":\"火焰陷阱\"},{\"type\":\"烈焰爆破\",\"text\":\"烈焰爆破\"},{\"type\":\"烈焰冲刺\",\"text\":\"烈焰冲刺\"},{\"type\":\"掷火陷阱\",\"text\":\"掷火陷阱\"},{\"type\":\"圣焰图腾\",\"text\":\"圣焰图腾\"},{\"type\":\"烈焰之墙\",\"text\":\"烈焰之墙\"},{\"type\":\"怒焰奔腾\",\"text\":\"怒焰奔腾\"},{\"type\":\"易燃\",\"text\":\"易燃\"},{\"type\":\"血肉与岩石\",\"text\":\"血肉与岩石\"},{\"type\":\"血肉奉献\",\"text\":\"血肉奉献\"},{\"type\":\"闪现打击\",\"text\":\"闪现打击\"},{\"type\":\"冰锥地雷\",\"text\":\"冰锥地雷\"},{\"type\":\"冰霜脉冲\",\"text\":\"冰霜脉冲\"},{\"type\":\"狂怒\",\"text\":\"狂怒\"},{\"type\":\"冻伤\",\"text\":\"冻伤\"},{\"type\":\"冰霜之刃\",\"text\":\"冰霜之刃\"},{\"type\":\"冰霜闪现\",\"text\":\"冰霜闪现\"},{\"type\":\"寒冰弹\",\"text\":\"寒冰弹\"},{\"type\":\"漩涡\",\"text\":\"漩涡\"},{\"type\":\"寒霜爆\",\"text\":\"寒霜爆\"},{\"type\":\"冰霜护盾\",\"text\":\"冰霜护盾\"},{\"type\":\"冰墙\",\"text\":\"冰墙\"},{\"type\":\"将军之吼\",\"text\":\"将军之吼\"},{\"type\":\"冰川之刺\",\"text\":\"冰川之刺\"},{\"type\":\"冰霜之锤\",\"text\":\"冰霜之锤\"},{\"type\":\"优雅\",\"text\":\"优雅\"},{\"type\":\"裂地之击\",\"text\":\"裂地之击\"},{\"type\":\"迅捷\",\"text\":\"迅捷\"},{\"type\":\"憎恨\",\"text\":\"憎恨\"},{\"type\":\"重击\",\"text\":\"重击\"},{\"type\":\"苦痛之捷\",\"text\":\"苦痛之捷\"},{\"type\":\"灰烬之捷\",\"text\":\"灰烬之捷\"},{\"type\":\"寒冰之捷\",\"text\":\"寒冰之捷\"},{\"type\":\"纯净之捷\",\"text\":\"纯净之捷\"},{\"type\":\"闪电之捷\",\"text\":\"闪电之捷\"},{\"type\":\"魔蛊爆炸\",\"text\":\"魔蛊爆炸\"},{\"type\":\"寒冰冲击\",\"text\":\"寒冰冲击\"},{\"type\":\"冰霜新星\",\"text\":\"冰霜新星\"},{\"type\":\"冰霜射击\",\"text\":\"冰霜射击\"},{\"type\":\"虹吸陷阱\",\"text\":\"虹吸陷阱\"},{\"type\":\"冰矛\",\"text\":\"冰矛\"},{\"type\":\"冰冻陷阱\",\"text\":\"冰冻陷阱\"},{\"type\":\"不朽怒嚎\",\"text\":\"不朽怒嚎\"},{\"type\":\"烧毁\",\"text\":\"烧毁\"},{\"type\":\"炼狱之击\",\"text\":\"炼狱之击\"},{\"type\":\"威吓战吼\",\"text\":\"威吓战吼\"},{\"type\":\"力量爆破\",\"text\":\"力量爆破\"},{\"type\":\"念动飞箭\",\"text\":\"念动飞箭\"},{\"type\":\"破空斩\",\"text\":\"破空斩\"},{\"type\":\"断金之刃\",\"text\":\"断金之刃\"},{\"type\":\"跃击\",\"text\":\"跃击\"},{\"type\":\"闪电箭矢\",\"text\":\"闪电箭矢\"},{\"type\":\"闪电净化\",\"text\":\"闪电净化\"},{\"type\":\"闪电打击\",\"text\":\"闪电打击\"},{\"type\":\"电能释放\",\"text\":\"电能释放\"},{\"type\":\"电塔陷阱\",\"text\":\"电塔陷阱\"},{\"type\":\"闪电陷阱\",\"text\":\"闪电陷阱\"},{\"type\":\"闪电传送\",\"text\":\"闪电传送\"},{\"type\":\"熔岩之核\",\"text\":\"熔岩之核\"},{\"type\":\"先祖卫士\",\"text\":\"先祖卫士\"},{\"type\":\"魅影射击\",\"text\":\"魅影射击\"},{\"type\":\"熔岩护盾\",\"text\":\"熔岩护盾\"},{\"type\":\"熔岩之击\",\"text\":\"熔岩之击\"},{\"type\":\"脆弱\",\"text\":\"脆弱\"},{\"type\":\"风暴漩涡\",\"text\":\"风暴漩涡\"},{\"type\":\"忏悔烙印\",\"text\":\"忏悔烙印\"},{\"type\":\"凿击\",\"text\":\"凿击\"},{\"type\":\"致疫打击\",\"text\":\"致疫打击\"},{\"type\":\"暗影迷踪\",\"text\":\"暗影迷踪\"},{\"type\":\"震波陷阱\",\"text\":\"震波陷阱\"},{\"type\":\"瘟疫使徒\",\"text\":\"瘟疫使徒\"},{\"type\":\"盗猎者印记\",\"text\":\"盗猎者印记\"},{\"type\":\"腐蚀箭矢\",\"text\":\"腐蚀箭矢\"},{\"type\":\"时空之门\",\"text\":\"时空之门\"},{\"type\":\"力量抽取\",\"text\":\"力量抽取\"},{\"type\":\"精准\",\"text\":\"精准\"},{\"type\":\"尊严\",\"text\":\"尊严\"},{\"type\":\"狙击之印\",\"text\":\"狙击之印\"},{\"type\":\"放血\",\"text\":\"放血\"},{\"type\":\"惩戒\",\"text\":\"惩戒\"},{\"type\":\"定罪波\",\"text\":\"定罪波\"},{\"type\":\"元素净化\",\"text\":\"元素净化\"},{\"type\":\"箭雨\",\"text\":\"箭雨\"},{\"type\":\"召唤灵体\",\"text\":\"召唤灵体\"},{\"type\":\"魔卫复苏\",\"text\":\"魔卫复苏\"},{\"type\":\"激励战吼\",\"text\":\"激励战吼\"},{\"type\":\"冲击波\",\"text\":\"冲击波\"},{\"type\":\"烙印召回\",\"text\":\"烙印召回\"},{\"type\":\"清算\",\"text\":\"清算\"},{\"type\":\"回春图腾\",\"text\":\"回春图腾\"},{\"type\":\"正义之火\",\"text\":\"正义之火\"},{\"type\":\"击刃\",\"text\":\"击刃\"},{\"type\":\"净化烈焰\",\"text\":\"净化烈焰\"},{\"type\":\"天灾之箭\",\"text\":\"天灾之箭\"},{\"type\":\"灼热连接\",\"text\":\"灼热连接\"},{\"type\":\"震地战吼\",\"text\":\"震地战吼\"},{\"type\":\"破碎铁刃\",\"text\":\"破碎铁刃\"},{\"type\":\"重盾冲锋\",\"text\":\"重盾冲锋\"},{\"type\":\"闪电新星\",\"text\":\"闪电新星\"},{\"type\":\"震波图腾\",\"text\":\"震波图腾\"},{\"type\":\"散射弩炮\",\"text\":\"散射弩炮\"},{\"type\":\"电光箭\",\"text\":\"电光箭\"},{\"type\":\"爆炸陷阱\",\"text\":\"爆炸陷阱\"},{\"type\":\"攻城炮台\",\"text\":\"攻城炮台\"},{\"type\":\"威能法印\",\"text\":\"威能法印\"},{\"type\":\"惩击\",\"text\":\"惩击\"},{\"type\":\"烟雾地雷\",\"text\":\"烟雾地雷\"},{\"type\":\"裂魂术\",\"text\":\"裂魂术\"},{\"type\":\"电球\",\"text\":\"电球\"},{\"type\":\"奋锐光环\",\"text\":\"奋锐光环\"},{\"type\":\"法术节魔\",\"text\":\"法术节魔\"},{\"type\":\"灵魂奉献\",\"text\":\"灵魂奉献\"},{\"type\":\"分裂箭矢\",\"text\":\"分裂箭矢\"},{\"type\":\"分裂钢刃\",\"text\":\"分裂钢刃\"},{\"type\":\"充能打击\",\"text\":\"充能打击\"},{\"type\":\"钢铁之肤\",\"text\":\"钢铁之肤\"},{\"type\":\"缚雷之纹\",\"text\":\"缚雷之纹\"},{\"type\":\"雷暴地雷\",\"text\":\"雷暴地雷\"},{\"type\":\"风暴烙印\",\"text\":\"风暴烙印\"},{\"type\":\"裂风雷球\",\"text\":\"裂风雷球\"},{\"type\":\"风暴呼唤\",\"text\":\"风暴呼唤\"},{\"type\":\"召唤腐化魔像\",\"text\":\"召唤腐化魔像\"},{\"type\":\"召唤混沌魔像\",\"text\":\"召唤混沌魔像\"},{\"type\":\"召唤烈焰魔像\",\"text\":\"召唤烈焰魔像\"},{\"type\":\"召唤寒冰魔像\",\"text\":\"召唤寒冰魔像\"},{\"type\":\"召唤闪电魔像\",\"text\":\"召唤闪电魔像\"},{\"type\":\"召唤愤怒狂灵\",\"text\":\"召唤愤怒狂灵\"},{\"type\":\"召唤圣物\",\"text\":\"召唤圣物\"},{\"type\":\"召唤巨石魔像\",\"text\":\"召唤巨石魔像\"},{\"type\":\"召唤魔侍\",\"text\":\"召唤魔侍\"},{\"type\":\"召唤飞掠者\",\"text\":\"召唤飞掠者\"},{\"type\":\"大地震击\",\"text\":\"大地震击\"},{\"type\":\"横扫\",\"text\":\"横扫\"},{\"type\":\"破釜一击\",\"text\":\"破釜一击\"},{\"type\":\"暴风之盾\",\"text\":\"暴风之盾\"},{\"type\":\"时空锁链\",\"text\":\"时空锁链\"},{\"type\":\"灵盾投掷\",\"text\":\"灵盾投掷\"},{\"type\":\"灵体投掷\",\"text\":\"灵体投掷\"},{\"type\":\"龙卷射击\",\"text\":\"龙卷射击\"},{\"type\":\"毒雨\",\"text\":\"毒雨\"},{\"type\":\"瓦尔.先祖战士长\",\"text\":\"瓦尔.先祖战士长\"},{\"type\":\"瓦尔.电弧\",\"text\":\"瓦尔.电弧\"},{\"type\":\"瓦尔.飞刃风暴\",\"text\":\"瓦尔.飞刃风暴\"},{\"type\":\"瓦尔.枯萎\",\"text\":\"瓦尔.枯萎\"},{\"type\":\"瓦尔.燃烧箭矢\",\"text\":\"瓦尔.燃烧箭矢\"},{\"type\":\"瓦尔.清晰\",\"text\":\"瓦尔.清晰\"},{\"type\":\"瓦尔.不净之冰\",\"text\":\"瓦尔.不净之冰\"},{\"type\":\"瓦尔.霜暴\",\"text\":\"瓦尔.霜暴\"},{\"type\":\"瓦尔.旋风斩\",\"text\":\"瓦尔.旋风斩\"},{\"type\":\"瓦尔.爆灵术\",\"text\":\"瓦尔.爆灵术\"},{\"type\":\"瓦尔.纪律\",\"text\":\"瓦尔.纪律\"},{\"type\":\"瓦尔.双重打击\",\"text\":\"瓦尔.双重打击\"},{\"type\":\"瓦尔.震地\",\"text\":\"瓦尔.震地\"},{\"type\":\"瓦尔.火球\",\"text\":\"瓦尔.火球\"},{\"type\":\"瓦尔.不净之火\",\"text\":\"瓦尔.不净之火\"},{\"type\":\"瓦尔.烈焰爆破\",\"text\":\"瓦尔.烈焰爆破\"},{\"type\":\"瓦尔.冰霜之锤\",\"text\":\"瓦尔.冰霜之锤\"},{\"type\":\"瓦尔.优雅\",\"text\":\"瓦尔.优雅\"},{\"type\":\"瓦尔.裂地之击\",\"text\":\"瓦尔.裂地之击\"},{\"type\":\"瓦尔.迅捷\",\"text\":\"瓦尔.迅捷\"},{\"type\":\"瓦尔.冰霜新星\",\"text\":\"瓦尔.冰霜新星\"},{\"type\":\"瓦尔.不朽怒嚎\",\"text\":\"瓦尔.不朽怒嚎\"},{\"type\":\"瓦尔.不净之雷\",\"text\":\"瓦尔.不净之雷\"},{\"type\":\"瓦尔.闪电打击\",\"text\":\"瓦尔.闪电打击\"},{\"type\":\"瓦尔.闪电陷阱\",\"text\":\"瓦尔.闪电陷阱\"},{\"type\":\"瓦尔.闪电传送\",\"text\":\"瓦尔.闪电传送\"},{\"type\":\"瓦尔.熔岩护盾\",\"text\":\"瓦尔.熔岩护盾\"},{\"type\":\"瓦尔.裂隙\",\"text\":\"瓦尔.裂隙\"},{\"type\":\"瓦尔.力量抽取\",\"text\":\"瓦尔.力量抽取\"},{\"type\":\"瓦尔.箭雨\",\"text\":\"瓦尔.箭雨\"},{\"type\":\"瓦尔.冲击波\",\"text\":\"瓦尔.冲击波\"},{\"type\":\"瓦尔.正义之火\",\"text\":\"瓦尔.正义之火\"},{\"type\":\"瓦尔.电球\",\"text\":\"瓦尔.电球\"},{\"type\":\"瓦尔.风暴呼唤\",\"text\":\"瓦尔.风暴呼唤\"},{\"type\":\"瓦尔.召唤魔侍\",\"text\":\"瓦尔.召唤魔侍\"},{\"type\":\"瓦尔.灵体投掷\",\"text\":\"瓦尔.灵体投掷\"},{\"type\":\"复仇\",\"text\":\"复仇\"},{\"type\":\"剧毒旋风\",\"text\":\"剧毒旋风\"},{\"type\":\"戒备打击\",\"text\":\"戒备打击\"},{\"type\":\"毒蛇打击\",\"text\":\"毒蛇打击\"},{\"type\":\"活力\",\"text\":\"活力\"},{\"type\":\"虚空法球\",\"text\":\"虚空法球\"},{\"type\":\"灵体火球\",\"text\":\"灵体火球\"},{\"type\":\"绝望\",\"text\":\"绝望\"},{\"type\":\"战旗\",\"text\":\"战旗\"},{\"type\":\"督军印记\",\"text\":\"督军印记\"},{\"type\":\"回旋之刃\",\"text\":\"回旋之刃\"},{\"type\":\"野性打击\",\"text\":\"野性打击\"},{\"type\":\"寒冬宝珠\",\"text\":\"寒冬宝珠\"},{\"type\":\"冬潮烙印\",\"text\":\"冬潮烙印\"},{\"type\":\"死亡凋零\",\"text\":\"死亡凋零\"},{\"type\":\"凋零步\",\"text\":\"凋零步\"},{\"type\":\"雷霆\",\"text\":\"雷霆\"},{\"type\":\"附加混沌伤害(辅)\",\"text\":\"附加混沌伤害(辅)\"},{\"type\":\"附加混沌伤害（强辅）\",\"text\":\"附加混沌伤害（强辅）\"},{\"type\":\"附加冰霜伤害(辅)\",\"text\":\"附加冰霜伤害(辅)\"},{\"type\":\"附加冰霜伤害（强辅）\",\"text\":\"附加冰霜伤害（强辅）\"},{\"type\":\"附加火焰伤害(辅)\",\"text\":\"附加火焰伤害(辅)\"},{\"type\":\"附加火焰伤害（强辅）\",\"text\":\"附加火焰伤害（强辅）\"},{\"type\":\"附加闪电伤害(辅)\",\"text\":\"附加闪电伤害(辅)\"},{\"type\":\"附加闪电伤害（强辅）\",\"text\":\"附加闪电伤害（强辅）\"},{\"type\":\"额外命中(辅)\",\"text\":\"额外命中(辅)\"},{\"type\":\"赋予(辅)\",\"text\":\"赋予(辅)\"},{\"type\":\"增幅(辅)\",\"text\":\"增幅(辅)\"},{\"type\":\"启蒙(辅)\",\"text\":\"启蒙(辅)\"},{\"type\":\"先祖召唤（强辅）\",\"text\":\"先祖召唤（强辅）\"},{\"type\":\"秘术增强(辅)\",\"text\":\"秘术增强(辅)\"},{\"type\":\"大法师（辅）\",\"text\":\"大法师（辅）\"},{\"type\":\"箭之新星（辅）\",\"text\":\"箭之新星（辅）\"},{\"type\":\"箭之新星（强辅）\",\"text\":\"箭之新星（强辅）\"},{\"type\":\"弹幕（辅）\",\"text\":\"弹幕（辅）\"},{\"type\":\"诅咒光环(辅)\",\"text\":\"诅咒光环(辅)\"},{\"type\":\"渎神（强辅）\",\"text\":\"渎神（强辅）\"},{\"type\":\"致盲(辅)\",\"text\":\"致盲(辅)\"},{\"type\":\"减少格挡几率(辅)\",\"text\":\"减少格挡几率(辅)\"},{\"type\":\"嗜血(辅)\",\"text\":\"嗜血(辅)\"},{\"type\":\"血魔法(辅)\",\"text\":\"血魔法(辅)\"},{\"type\":\"彻骨（辅）\",\"text\":\"彻骨（辅）\"},{\"type\":\"残暴(辅)\",\"text\":\"残暴(辅)\"},{\"type\":\"残暴（强辅）\",\"text\":\"残暴（强辅）\"},{\"type\":\"增加燃烧伤害（强辅）\",\"text\":\"增加燃烧伤害（强辅）\"},{\"type\":\"暴击时施放(辅)\",\"text\":\"暴击时施放(辅)\"},{\"type\":\"暴击时施放（强辅）\",\"text\":\"暴击时施放（强辅）\"},{\"type\":\"受伤时施放(辅)\",\"text\":\"受伤时施放(辅)\"},{\"type\":\"死亡时施放(辅)\",\"text\":\"死亡时施放(辅)\"},{\"type\":\"近战击败时施放(辅)\",\"text\":\"近战击败时施放(辅)\"},{\"type\":\"晕眩时施放(辅)\",\"text\":\"晕眩时施放(辅)\"},{\"type\":\"吟唱时施放(辅)\",\"text\":\"吟唱时施放(辅)\"},{\"type\":\"吟唱时施放（强辅）\",\"text\":\"吟唱时施放（强辅）\"},{\"type\":\"连锁(辅)\",\"text\":\"连锁(辅)\"},{\"type\":\"连锁（强辅）\",\"text\":\"连锁（强辅）\"},{\"type\":\"几率流血(辅)\",\"text\":\"几率流血(辅)\"},{\"type\":\"几率逃跑(辅)\",\"text\":\"几率逃跑(辅)\"},{\"type\":\"几率点燃(辅)\",\"text\":\"几率点燃(辅)\"},{\"type\":\"凋零之触（辅）\",\"text\":\"凋零之触（辅）\"},{\"type\":\"充能地雷（辅）\",\"text\":\"充能地雷（辅）\"},{\"type\":\"近战（辅）\",\"text\":\"近战（辅）\"},{\"type\":\"散弹陷阱(辅)\",\"text\":\"散弹陷阱(辅)\"},{\"type\":\"冰霜穿透(辅)\",\"text\":\"冰霜穿透(辅)\"},{\"type\":\"冰霜穿透（强辅）\",\"text\":\"冰霜穿透（强辅）\"},{\"type\":\"寒冰转烈焰(辅)\",\"text\":\"寒冰转烈焰(辅)\"},{\"type\":\"集中效应(辅)\",\"text\":\"集中效应(辅)\"},{\"type\":\"精准破坏(辅)\",\"text\":\"精准破坏(辅)\"},{\"type\":\"精准破坏（强辅）\",\"text\":\"精准破坏（强辅）\"},{\"type\":\"终结(辅)\",\"text\":\"终结(辅)\"},{\"type\":\"蛊咒（辅）\",\"text\":\"蛊咒（辅）\"},{\"type\":\"蛊咒【强辅】\",\"text\":\"蛊咒【强辅】\"},{\"type\":\"急冻(辅)\",\"text\":\"急冻(辅)\"},{\"type\":\"致命异常状态(辅)\",\"text\":\"致命异常状态(辅)\"},{\"type\":\"致命异常状态（强辅）\",\"text\":\"致命异常状态（强辅）\"},{\"type\":\"掠食（辅）\",\"text\":\"掠食（辅）\"},{\"type\":\"腐蚀(辅)\",\"text\":\"腐蚀(辅)\"},{\"type\":\"效能(辅)\",\"text\":\"效能(辅)\"},{\"type\":\"元素集中(辅)\",\"text\":\"元素集中(辅)\"},{\"type\":\"元素集中（强辅）\",\"text\":\"元素集中（强辅）\"},{\"type\":\"元素穿透（辅）\",\"text\":\"元素穿透（辅）\"},{\"type\":\"元素扩散(辅)\",\"text\":\"元素扩散(辅)\"},{\"type\":\"近战击晕获得耐力球(辅)\",\"text\":\"近战击晕获得耐力球(辅)\"},{\"type\":\"能量偷取（辅）\",\"text\":\"能量偷取（辅）\"},{\"type\":\"快速攻击(辅)\",\"text\":\"快速攻击(辅)\"},{\"type\":\"快速施法(辅)\",\"text\":\"快速施法(辅)\"},{\"type\":\"快速投射(辅)\",\"text\":\"快速投射(辅)\"},{\"type\":\"狂噬（辅）\",\"text\":\"狂噬（辅）\"},{\"type\":\"火焰穿透(辅)\",\"text\":\"火焰穿透(辅)\"},{\"type\":\"火焰穿透（强辅）\",\"text\":\"火焰穿透（强辅）\"},{\"type\":\"战争铁拳（辅）\",\"text\":\"战争铁拳（辅）\"},{\"type\":\"分裂(辅)\",\"text\":\"分裂(辅)\"},{\"type\":\"分裂（强辅）\",\"text\":\"分裂（强辅）\"},{\"type\":\"护体(辅)\",\"text\":\"护体(辅)\"},{\"type\":\"霜咬(辅)\",\"text\":\"霜咬(辅)\"},{\"type\":\"充能陷阱（辅）\",\"text\":\"充能陷阱（辅）\"},{\"type\":\"和善(辅)\",\"text\":\"和善(辅)\"},{\"type\":\"和善（强辅）\",\"text\":\"和善（强辅）\"},{\"type\":\"高阶多重投射(辅)\",\"text\":\"高阶多重投射(辅)\"},{\"type\":\"高阶多重投射（强辅）\",\"text\":\"高阶多重投射（强辅）\"},{\"type\":\"高阶齐射（辅）\",\"text\":\"高阶齐射（辅）\"},{\"type\":\"释出（辅）\",\"text\":\"释出（辅）\"},{\"type\":\"法术凝聚（辅）\",\"text\":\"法术凝聚（辅）\"},{\"type\":\"高爆地雷（辅）\",\"text\":\"高爆地雷（辅）\"},{\"type\":\"点燃扩散(辅)\",\"text\":\"点燃扩散(辅)\"},{\"type\":\"献祭(辅)\",\"text\":\"献祭(辅)\"},{\"type\":\"尖刺战杖\",\"text\":\"尖刺战杖\"},{\"type\":\"末日将至（辅）\",\"text\":\"末日将至（辅）\"},{\"type\":\"增大范围(辅)\",\"text\":\"增大范围(辅)\"},{\"type\":\"效果区域扩大（强辅）\",\"text\":\"效果区域扩大（强辅）\"},{\"type\":\"增加燃烧伤害(辅)\",\"text\":\"增加燃烧伤害(辅)\"},{\"type\":\"提高暴击伤害(辅)\",\"text\":\"提高暴击伤害(辅)\"},{\"type\":\"提高暴击几率(辅)\",\"text\":\"提高暴击几率(辅)\"},{\"type\":\"持续时间延长(辅)\",\"text\":\"持续时间延长(辅)\"},{\"type\":\"炎军（辅）\",\"text\":\"炎军（辅）\"},{\"type\":\"钢铁之握(辅)\",\"text\":\"钢铁之握(辅)\"},{\"type\":\"钢铁意志(辅)\",\"text\":\"钢铁意志(辅)\"},{\"type\":\"物品稀有度增幅(辅)\",\"text\":\"物品稀有度增幅(辅)\"},{\"type\":\"击退(辅)\",\"text\":\"击退(辅)\"},{\"type\":\"低阶多重投射(辅)\",\"text\":\"低阶多重投射(辅)\"},{\"type\":\"低阶毒化(辅)\",\"text\":\"低阶毒化(辅)\"},{\"type\":\"击中生命回复(辅)\",\"text\":\"击中生命回复(辅)\"},{\"type\":\"生命偷取(辅)\",\"text\":\"生命偷取(辅)\"},{\"type\":\"闪电穿透(辅)\",\"text\":\"闪电穿透(辅)\"},{\"type\":\"闪电穿透（强辅）\",\"text\":\"闪电穿透（强辅）\"},{\"type\":\"瘫痪(辅)\",\"text\":\"瘫痪(辅)\"},{\"type\":\"魔力偷取(辅)\",\"text\":\"魔力偷取(辅)\"},{\"type\":\"肉盾（辅）\",\"text\":\"肉盾（辅）\"},{\"type\":\"满血伤害（辅）\",\"text\":\"满血伤害（辅）\"},{\"type\":\"近战物理伤害(辅)\",\"text\":\"近战物理伤害(辅)\"},{\"type\":\"近战物理伤害（强辅）\",\"text\":\"近战物理伤害（强辅）\"},{\"type\":\"近战伤害扩散(辅)\",\"text\":\"近战伤害扩散(辅)\"},{\"type\":\"近战伤害扩散（强辅）\",\"text\":\"近战伤害扩散（强辅）\"},{\"type\":\"地雷网(辅)\",\"text\":\"地雷网(辅)\"},{\"type\":\"召唤生物伤害(辅)\",\"text\":\"召唤生物伤害(辅)\"},{\"type\":\"召唤生物伤害（强辅）\",\"text\":\"召唤生物伤害（强辅）\"},{\"type\":\"召唤生物生命(辅)\",\"text\":\"召唤生物生命(辅)\"},{\"type\":\"召唤生物速度(辅)\",\"text\":\"召唤生物速度(辅)\"},{\"type\":\"幻影射手(辅)\",\"text\":\"幻影射手(辅)\"},{\"type\":\"施法回响(辅)\",\"text\":\"施法回响(辅)\"},{\"type\":\"多重打击(辅)\",\"text\":\"多重打击(辅)\"},{\"type\":\"多重打击（强辅）\",\"text\":\"多重打击（强辅）\"},{\"type\":\"多重图腾（辅）\",\"text\":\"多重图腾（辅）\"},{\"type\":\"多重陷阱(辅)\",\"text\":\"多重陷阱(辅)\"},{\"type\":\"夜刃（辅）\",\"text\":\"夜刃（辅）\"},{\"type\":\"猛攻(辅)\",\"text\":\"猛攻(辅)\"},{\"type\":\"闪电支配(辅)\",\"text\":\"闪电支配(辅)\"},{\"type\":\"齐射(辅)\",\"text\":\"齐射(辅)\"},{\"type\":\"邪恶投掷(辅)\",\"text\":\"邪恶投掷(辅)\"},{\"type\":\"物理转闪电(辅)\",\"text\":\"物理转闪电(辅)\"},{\"type\":\"穿透(辅)\",\"text\":\"穿透(辅)\"},{\"type\":\"会心一击（辅）\",\"text\":\"会心一击（辅）\"},{\"type\":\"零点射击(辅)\",\"text\":\"零点射击(辅)\"},{\"type\":\"毒化(辅)\",\"text\":\"毒化(辅)\"},{\"type\":\"暴击获得暴击球(辅)\",\"text\":\"暴击获得暴击球(辅)\"},{\"type\":\"粉碎（辅）\",\"text\":\"粉碎（辅）\"},{\"type\":\"怒火（辅）\",\"text\":\"怒火（辅）\"},{\"type\":\"弩炮图腾（辅）\",\"text\":\"弩炮图腾（辅）\"},{\"type\":\"极速腐化(辅)\",\"text\":\"极速腐化(辅)\"},{\"type\":\"持续时间缩短(辅)\",\"text\":\"持续时间缩短(辅)\"},{\"type\":\"启迪（辅）\",\"text\":\"启迪（辅）\"},{\"type\":\"链爆地雷（辅）\",\"text\":\"链爆地雷（辅）\"},{\"type\":\"无情(辅)\",\"text\":\"无情(辅)\"},{\"type\":\"助力之风（辅）\",\"text\":\"助力之风（辅）\"},{\"type\":\"震波（辅）\",\"text\":\"震波（辅）\"},{\"type\":\"投射物减速(辅)\",\"text\":\"投射物减速(辅)\"},{\"type\":\"多重范围施法(辅)\",\"text\":\"多重范围施法(辅)\"},{\"type\":\"多重范围施法（强辅）\",\"text\":\"多重范围施法（强辅）\"},{\"type\":\"施法回响（强辅）\",\"text\":\"施法回响（强辅）\"},{\"type\":\"法术图腾(辅)\",\"text\":\"法术图腾(辅)\"},{\"type\":\"先祖召唤(辅)\",\"text\":\"先祖召唤(辅)\"},{\"type\":\"灌能吟唱(辅)\",\"text\":\"灌能吟唱(辅)\"},{\"type\":\"击晕(辅)\",\"text\":\"击晕(辅)\"},{\"type\":\"元素大军（辅）\",\"text\":\"元素大军（辅）\"},{\"type\":\"召唤幻影（辅）\",\"text\":\"召唤幻影（辅）\"},{\"type\":\"极速腐化（强辅）\",\"text\":\"极速腐化（强辅）\"},{\"type\":\"迅整（辅）\",\"text\":\"迅整（辅）\"},{\"type\":\"迅猛烙印（辅）\",\"text\":\"迅猛烙印（辅）\"},{\"type\":\"陷阱(辅)\",\"text\":\"陷阱(辅)\"},{\"type\":\"陷阱及地雷伤害(辅)\",\"text\":\"陷阱及地雷伤害(辅)\"},{\"type\":\"陷阱冷却(辅)\",\"text\":\"陷阱冷却(辅)\"},{\"type\":\"异常爆发(辅)\",\"text\":\"异常爆发(辅)\"},{\"type\":\"异常爆发（强辅）\",\"text\":\"异常爆发（强辅）\"},{\"type\":\"释出（强辅）\",\"text\":\"释出（强辅）\"},{\"type\":\"紧急号令（辅）\",\"text\":\"紧急号令（辅）\"},{\"type\":\"邪恶投掷（强辅）\",\"text\":\"邪恶投掷（强辅）\"},{\"type\":\"猛毒(辅)\",\"text\":\"猛毒(辅)\"},{\"type\":\"虚空操纵(辅)\",\"text\":\"虚空操纵(辅)\"},{\"type\":\"虚空操纵（强辅）\",\"text\":\"虚空操纵（强辅）\"},{\"type\":\"武器元素伤害(辅)\",\"text\":\"武器元素伤害(辅)\"},{\"type\":\"武器元素伤害（强辅）\",\"text\":\"武器元素伤害（强辅）\"}]");
        String jsonBase = "{\"query\":{\"status\":{\"option\":\"any\"},\"type\":\"元素净化\",\"stats\":[{\"type\":\"and\",\"filters\":[],\"disabled\":false}],\"filters\":{\"misc_filters\":{\"filters\":{\"quality\":{\"min\":0,\"max\":null},\"corrupted\":{\"option\":\"false\"},\"gem_level\":{\"min\":0,\"max\":null},\"gem_alternate_quality\":{\"option\":\"0\"}},\"disabled\":false}}},\"sort\":{\"price\":\"asc\"}}";
        JSONObject jo = JSONObject.parseObject(jsonBase);
        String url = "https://poe.game.qq.com/api/trade/search/S13%E8%B5%9B%E5%AD%A3";
        for (int i=0; i<jsonArray.size(); i++) {
            try {
                String gemName = (String) jsonArray.getJSONObject(i).get("type");
                jo.getJSONObject("query").put("type", gemName);
                jo.getJSONObject("query")
                        .getJSONObject("filters")
                        .getJSONObject("misc_filters")
                        .getJSONObject("filters")
                        .getJSONObject("gem_alternate_quality")
                        .put("option", 2);
                String s = JSONObject.toJSONString(jo);
                String result2 = HttpRequest.post(url)
                        .timeout(500)
                        .body(s)
                        .execute().body();
                JSONArray ja = JSONArray.parseArray(JSONObject.parseObject(result2).get("result").toString());
                String getUrl = "https://poe.game.qq.com/api/trade/fetch/" + ja.get(0);
                String s1 = HttpUtil
                        .get(getUrl);
                JSONObject jo2 = JSONObject.parseObject(s1);
                System.out.println("------------------" + gemName + "---------------------");
                System.out.println(jo2.getJSONArray("result").getJSONObject(0).getJSONObject("listing").getJSONObject("price").get("amount"));
                System.out.println(jo2.getJSONArray("result").getJSONObject(0).getJSONObject("listing").getJSONObject("price").get("currency"));
                int curPrice = Integer.parseInt(jo2.getJSONArray("result").getJSONObject(0).getJSONObject("listing").getJSONObject("price").get("amount").toString());
                System.out.println(curPrice);
            } catch (Exception e) {
                System.out.println("异常");
            }
            Thread.sleep(500);
        }
    }

    @Test
    public void test9() throws InterruptedException {
        JSONArray jsonArray = JSON.parseArray("[{\"type\":\"元素净化\",\"text\":\"元素净化\"},{\"type\":\"冰霜净化\",\"text\":\"冰霜净化\"}]");
        String jsonBase = "{\"query\":{\"status\":{\"option\":\"any\"},\"type\":\"元素净化\",\"stats\":[{\"type\":\"and\",\"filters\":[],\"disabled\":false}],\"filters\":{\"misc_filters\":{\"filters\":{\"quality\":{\"min\":0,\"max\":null},\"corrupted\":{\"option\":\"false\"},\"gem_level\":{\"min\":0,\"max\":null},\"gem_alternate_quality\":{\"option\":\"0\"}},\"disabled\":false}}},\"sort\":{\"price\":\"asc\"}}";
        JSONObject jo = JSONObject.parseObject(jsonBase);
        String url = "https://poe.game.qq.com/api/trade/search/S13%E8%B5%9B%E5%AD%A3";
        for (int i=0; i<jsonArray.size(); i++) {
            for (int typeOption=0; typeOption<=3; typeOption++) {
                try {
                    // 获取技能石的名字
                    String gemName = (String) jsonArray.getJSONObject(i).get("type");
                    // 将技能石的名字填充进入jsonObject
                    jo.getJSONObject("query").put("type", gemName);
                    // 填充技能石类型进入jsonObject
                    jo.getJSONObject("query")
                            .getJSONObject("filters")
                            .getJSONObject("misc_filters")
                            .getJSONObject("filters")
                            .getJSONObject("gem_alternate_quality")
                            .put("option", typeOption);
                    // 将jsonObject转化为json串
                    String s = JSONObject.toJSONString(jo);
                    // 通过HuTool发送Post请求获取物品数据集合
                    String itemsJson = HttpRequest.post(url)
                            .timeout(500)
                            .body(s)
                            .execute().body();
                    // 将获取到的物品数据集合转化为jsonArray
                    JSONArray ja = JSONArray.parseArray(JSONObject.parseObject(itemsJson).get("result").toString());
                    // 通过JsonArray取出前10个物品的uuid,并用,拼接
                    String suffix = ArrayUtil.join(ja.toArray(), ",");
                    // 根据uuid字串获取物品实际数据json串
                    System.out.println("suffix = " + suffix);
                    String getUrl = "https://poe.game.qq.com/api/trade/fetch/" + suffix;
                    String s1 = HttpUtil
                            .get(getUrl);
                    System.out.println(s1);
                    // 将json串转换为JSONObject
                    JSONObject jo2 = JSONObject.parseObject(s1);
                    // 计算价格平均值
                    int avgPrice = 0;
                    int avgNum = 10;
                    System.out.println("-------------- " + gemName + " -----------------------");
                    int exaltedPrice = 200;
                    for (int index=0; index<1; index++) {
                        int curPrice = Integer.parseInt(jo2.getJSONArray("result").getJSONObject(index).getJSONObject("listing").getJSONObject("price").get("amount").toString());
                        String curPriceType = jo2.getJSONArray("result").getJSONObject(0).getJSONObject("listing").getJSONObject("price").get("currency").toString();
                        if (curPriceType.equals("chaos")) {
                            avgPrice = avgPrice + curPrice;
                            System.out.println("chaos");
                        } else if(curPriceType.equals("exalted")) {
                            avgPrice = avgPrice + curPrice * exaltedPrice;
                            System.out.println("exalted");
                        } else {
                            avgNum -= 1;
                        }
                    }
                    System.out.println(avgPrice);
                    avgPrice = avgPrice / avgNum;
                    PoeItemPrice pip = new PoeItemPrice();
                    pip.setItemCurPrice(avgPrice + " 混沌石");
                    Date dNow = new Date( ); // 当前时间
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String formatTime = df.format(dNow);
                    pip.setItemRecordTime(formatTime);
                    pip.setItemType("技能宝石");
                    if (typeOption == 0) {
                        pip.setItemDesc("精良的");
                    } else if (typeOption == 1) {
                        pip.setItemDesc("异常");
                    } else if (typeOption == 2) {
                        pip.setItemDesc("分歧");
                    } else {
                        pip.setItemDesc("魅影");
                    }
                    pip.setItemName(gemName);
                    pip.setItemFilters("");
                    System.out.println(pip);
                    // poeItemPriceService.addNewItem(pip);
                } catch (Exception e) {
                    System.out.println("异常");
                    e.printStackTrace();
                }
                Thread.sleep(500);
            }
        }
    }

    @Test
    public void test10() {
        int[] a = {1,2,3,4,5,6,7,8};
        String join = ArrayUtil.join(a, ",");
        System.out.println(join);
        String join1 = ArrayUtil.join(ArrayUtil.resize(a, 3), "-");
        System.out.println(join1);
    }
}
