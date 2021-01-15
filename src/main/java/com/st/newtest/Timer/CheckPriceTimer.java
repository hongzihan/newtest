package com.st.newtest.Timer;

import cn.hutool.core.text.UnicodeUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.st.newtest.Util.CommonUtil;
import com.st.newtest.poeGame.Entity.PoeItemPrice;
import com.st.newtest.poeGame.Service.PoeItemPriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;

@Component
//@EnableScheduling
public class CheckPriceTimer {
    @Autowired
    private PoeItemPriceService poeItemPriceService;

    private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static int exaltedPrice = 150;

    private static String url = "https://poe.game.qq.com/api/trade/search/S13%E8%B5%9B%E5%AD%A3";

    private static String fetchUrl = "https://poe.game.qq.com/api/trade/fetch/";

    private JSONObject getSearchObjectByItemTag(String itemTag) {
        String result = "{\"query\":{\"status\":{\"option\":\"any\"},\"type\":\"清澈圣油\",\"stats\":[{\"type\":\"and\",\"filters\":[]}],\"filters\":{\"socket_filters\":{\"filters\":{\"links\":{}},\"sockets\":{}},\"map_filters\":{\"filters\":{}},\"misc_filters\":{\"filters\":{\"corrupted\":{\"option\":\"false\"}}}}},\"sort\":{\"price\":\"asc\"}}";
        JSONObject jsonObjectResult = JSONObject.parseObject(result);
        if (itemTag.equals("传奇地图")) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("min", 1);
            map.put("max", 16);
            jsonObjectResult.getJSONObject("query")
                    .getJSONObject("filters")
                    .getJSONObject("map_filters")
                    .getJSONObject("filters")
                    .put("map_tier", map);
            map.clear();
            map.put("option", "");
            map.put("discriminator", "");
            jsonObjectResult.getJSONObject("query")
                    .put("name", map);
            HashMap<String, Object> map2 = new HashMap<>();
            map2.put("option", "");
            map2.put("discriminator", "");
            jsonObjectResult.getJSONObject("query")
                    .put("type", map2);
        }

        if (itemTag.equals("预言")) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("option", "");
            map.put("discriminator", "");
            jsonObjectResult.getJSONObject("query")
                    .put("name", map);
            HashMap<String, Object> map2 = new HashMap<>();
            map2.put("option", "");
            map2.put("discriminator", "");
            jsonObjectResult.getJSONObject("query")
                    .put("type", map2);
        }

        if (itemTag.equals("技能宝石")) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("option", 0);
            jsonObjectResult.getJSONObject("query")
                    .getJSONObject("filters")
                    .getJSONObject("misc_filters")
                    .getJSONObject("filters")
                    .put("gem_alternate_quality", map);
        }

        if (itemTag.equals("传奇护甲1") || itemTag.equals("传奇武器") || itemTag.equals("传奇护甲2")) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("min", 1);
            map.put("max", 5);
            jsonObjectResult.getJSONObject("query")
                    .getJSONObject("filters")
                    .getJSONObject("socket_filters")
                    .getJSONObject("filters")
                    .put("links", map);
        }

        return JSONObject.parseObject(jsonObjectResult.toJSONString());
    }

    private String getItemJsonByItemTag(String itemTag) {
        if (itemTag.equals("技能宝石")) {
            return "[{\"type\":\"炼狱呼嚎\",\"text\":\"炼狱呼嚎\"},{\"type\":\"先祖战吼\",\"text\":\"先祖战吼\"},{\"type\":\"先祖战士长\",\"text\":\"先祖战士长\"},{\"type\":\"愤怒\",\"text\":\"愤怒\"},{\"type\":\"幻化守卫\",\"text\":\"幻化守卫\"},{\"type\":\"幻化武器\",\"text\":\"幻化武器\"},{\"type\":\"电弧\",\"text\":\"电弧\"},{\"type\":\"奥术斗篷\",\"text\":\"奥术斗篷\"},{\"type\":\"奥法烙印\",\"text\":\"奥法烙印\"},{\"type\":\"极地装甲\",\"text\":\"极地装甲\"},{\"type\":\"电光寒霜\",\"text\":\"电光寒霜\"},{\"type\":\"末日烙印\",\"text\":\"末日烙印\"},{\"type\":\"火力弩炮\",\"text\":\"火力弩炮\"},{\"type\":\"天雷之珠\",\"text\":\"天雷之珠\"},{\"type\":\"弹幕\",\"text\":\"弹幕\"},{\"type\":\"捕熊陷阱\",\"text\":\"捕熊陷阱\"},{\"type\":\"盛怒\",\"text\":\"盛怒\"},{\"type\":\"乱剑穿心\",\"text\":\"乱剑穿心\"},{\"type\":\"虚空刀雨\",\"text\":\"虚空刀雨\"},{\"type\":\"剑刃风暴\",\"text\":\"剑刃风暴\"},{\"type\":\"飞刃风暴\",\"text\":\"飞刃风暴\"},{\"type\":\"爆裂箭雨\",\"text\":\"爆裂箭雨\"},{\"type\":\"怒炎穿心\",\"text\":\"怒炎穿心\"},{\"type\":\"枯萎\",\"text\":\"枯萎\"},{\"type\":\"闪现射击\",\"text\":\"闪现射击\"},{\"type\":\"血与沙\",\"text\":\"血与沙\"},{\"type\":\"鲜血狂怒\",\"text\":\"鲜血狂怒\"},{\"type\":\"亵渎之矛\",\"text\":\"亵渎之矛\"},{\"type\":\"骸骨奉献\",\"text\":\"骸骨奉献\"},{\"type\":\"燃烧箭矢\",\"text\":\"燃烧箭矢\"},{\"type\":\"钩链攻击\",\"text\":\"钩链攻击\"},{\"type\":\"刀刃乱舞\",\"text\":\"刀刃乱舞\"},{\"type\":\"蓄力疾风闪\",\"text\":\"蓄力疾风闪\"},{\"type\":\"清晰\",\"text\":\"清晰\"},{\"type\":\"劈砍\",\"text\":\"劈砍\"},{\"type\":\"毒蛇鞭击\",\"text\":\"毒蛇鞭击\"},{\"type\":\"冰霜净化\",\"text\":\"冰霜净化\"},{\"type\":\"霜暴\",\"text\":\"霜暴\"},{\"type\":\"导电\",\"text\":\"导电\"},{\"type\":\"奉献之路\",\"text\":\"奉献之路\"},{\"type\":\"瘟疫\",\"text\":\"瘟疫\"},{\"type\":\"迷魅陷阱\",\"text\":\"迷魅陷阱\"},{\"type\":\"号召\",\"text\":\"号召\"},{\"type\":\"火葬\",\"text\":\"火葬\"},{\"type\":\"灵体转换\",\"text\":\"灵体转换\"},{\"type\":\"电殛长枪\",\"text\":\"电殛长枪\"},{\"type\":\"暗影印记\",\"text\":\"暗影印记\"},{\"type\":\"旋风斩\",\"text\":\"旋风斩\"},{\"type\":\"怨毒光环\",\"text\":\"怨毒光环\"},{\"type\":\"暗夜血契\",\"text\":\"暗夜血契\"},{\"type\":\"混沌之毒\",\"text\":\"混沌之毒\"},{\"type\":\"冲刺\",\"text\":\"冲刺\"},{\"type\":\"诱饵图腾\",\"text\":\"诱饵图腾\"},{\"type\":\"亵渎\",\"text\":\"亵渎\"},{\"type\":\"坚定\",\"text\":\"坚定\"},{\"type\":\"爆灵术\",\"text\":\"爆灵术\"},{\"type\":\"引爆地雷\",\"text\":\"引爆地雷\"},{\"type\":\"吞噬图腾\",\"text\":\"吞噬图腾\"},{\"type\":\"解放\",\"text\":\"解放\"},{\"type\":\"纪律\",\"text\":\"纪律\"},{\"type\":\"圣怨\",\"text\":\"圣怨\"},{\"type\":\"霸气之击\",\"text\":\"霸气之击\"},{\"type\":\"双重打击\",\"text\":\"双重打击\"},{\"type\":\"恐怖之旗\",\"text\":\"恐怖之旗\"},{\"type\":\"双持打击\",\"text\":\"双持打击\"},{\"type\":\"震地\",\"text\":\"震地\"},{\"type\":\"尖刺战吼\",\"text\":\"尖刺战吼\"},{\"type\":\"元素打击\",\"text\":\"元素打击\"},{\"type\":\"元素要害\",\"text\":\"元素要害\"},{\"type\":\"坚决战吼\",\"text\":\"坚决战吼\"},{\"type\":\"衰弱\",\"text\":\"衰弱\"},{\"type\":\"诱捕之箭\",\"text\":\"诱捕之箭\"},{\"type\":\"灵魂吸取\",\"text\":\"灵魂吸取\"},{\"type\":\"虚空匕首\",\"text\":\"虚空匕首\"},{\"type\":\"爆炸箭矢\",\"text\":\"爆炸箭矢\"},{\"type\":\"火球\",\"text\":\"火球\"},{\"type\":\"灼热光线\",\"text\":\"灼热光线\"},{\"type\":\"火屑地雷\",\"text\":\"火屑地雷\"},{\"type\":\"火焰净化\",\"text\":\"火焰净化\"},{\"type\":\"烈炎风暴\",\"text\":\"烈炎风暴\"},{\"type\":\"火焰陷阱\",\"text\":\"火焰陷阱\"},{\"type\":\"烈焰爆破\",\"text\":\"烈焰爆破\"},{\"type\":\"烈焰冲刺\",\"text\":\"烈焰冲刺\"},{\"type\":\"掷火陷阱\",\"text\":\"掷火陷阱\"},{\"type\":\"圣焰图腾\",\"text\":\"圣焰图腾\"},{\"type\":\"烈焰之墙\",\"text\":\"烈焰之墙\"},{\"type\":\"怒焰奔腾\",\"text\":\"怒焰奔腾\"},{\"type\":\"易燃\",\"text\":\"易燃\"},{\"type\":\"血肉与岩石\",\"text\":\"血肉与岩石\"},{\"type\":\"血肉奉献\",\"text\":\"血肉奉献\"},{\"type\":\"闪现打击\",\"text\":\"闪现打击\"},{\"type\":\"冰锥地雷\",\"text\":\"冰锥地雷\"},{\"type\":\"冰霜脉冲\",\"text\":\"冰霜脉冲\"},{\"type\":\"狂怒\",\"text\":\"狂怒\"},{\"type\":\"冻伤\",\"text\":\"冻伤\"},{\"type\":\"冰霜之刃\",\"text\":\"冰霜之刃\"},{\"type\":\"冰霜闪现\",\"text\":\"冰霜闪现\"},{\"type\":\"寒冰弹\",\"text\":\"寒冰弹\"},{\"type\":\"漩涡\",\"text\":\"漩涡\"},{\"type\":\"寒霜爆\",\"text\":\"寒霜爆\"},{\"type\":\"冰霜护盾\",\"text\":\"冰霜护盾\"},{\"type\":\"冰墙\",\"text\":\"冰墙\"},{\"type\":\"将军之吼\",\"text\":\"将军之吼\"},{\"type\":\"冰川之刺\",\"text\":\"冰川之刺\"},{\"type\":\"冰霜之锤\",\"text\":\"冰霜之锤\"},{\"type\":\"优雅\",\"text\":\"优雅\"},{\"type\":\"裂地之击\",\"text\":\"裂地之击\"},{\"type\":\"迅捷\",\"text\":\"迅捷\"},{\"type\":\"憎恨\",\"text\":\"憎恨\"},{\"type\":\"重击\",\"text\":\"重击\"},{\"type\":\"苦痛之捷\",\"text\":\"苦痛之捷\"},{\"type\":\"灰烬之捷\",\"text\":\"灰烬之捷\"},{\"type\":\"寒冰之捷\",\"text\":\"寒冰之捷\"},{\"type\":\"纯净之捷\",\"text\":\"纯净之捷\"},{\"type\":\"闪电之捷\",\"text\":\"闪电之捷\"},{\"type\":\"魔蛊爆炸\",\"text\":\"魔蛊爆炸\"},{\"type\":\"寒冰冲击\",\"text\":\"寒冰冲击\"},{\"type\":\"冰霜新星\",\"text\":\"冰霜新星\"},{\"type\":\"冰霜射击\",\"text\":\"冰霜射击\"},{\"type\":\"虹吸陷阱\",\"text\":\"虹吸陷阱\"},{\"type\":\"冰矛\",\"text\":\"冰矛\"},{\"type\":\"冰冻陷阱\",\"text\":\"冰冻陷阱\"},{\"type\":\"不朽怒嚎\",\"text\":\"不朽怒嚎\"},{\"type\":\"烧毁\",\"text\":\"烧毁\"},{\"type\":\"炼狱之击\",\"text\":\"炼狱之击\"},{\"type\":\"威吓战吼\",\"text\":\"威吓战吼\"},{\"type\":\"力量爆破\",\"text\":\"力量爆破\"},{\"type\":\"念动飞箭\",\"text\":\"念动飞箭\"},{\"type\":\"破空斩\",\"text\":\"破空斩\"},{\"type\":\"断金之刃\",\"text\":\"断金之刃\"},{\"type\":\"跃击\",\"text\":\"跃击\"},{\"type\":\"闪电箭矢\",\"text\":\"闪电箭矢\"},{\"type\":\"闪电净化\",\"text\":\"闪电净化\"},{\"type\":\"闪电打击\",\"text\":\"闪电打击\"},{\"type\":\"电能释放\",\"text\":\"电能释放\"},{\"type\":\"电塔陷阱\",\"text\":\"电塔陷阱\"},{\"type\":\"闪电陷阱\",\"text\":\"闪电陷阱\"},{\"type\":\"闪电传送\",\"text\":\"闪电传送\"},{\"type\":\"熔岩之核\",\"text\":\"熔岩之核\"},{\"type\":\"先祖卫士\",\"text\":\"先祖卫士\"},{\"type\":\"魅影射击\",\"text\":\"魅影射击\"},{\"type\":\"熔岩护盾\",\"text\":\"熔岩护盾\"},{\"type\":\"熔岩之击\",\"text\":\"熔岩之击\"},{\"type\":\"脆弱\",\"text\":\"脆弱\"},{\"type\":\"风暴漩涡\",\"text\":\"风暴漩涡\"},{\"type\":\"忏悔烙印\",\"text\":\"忏悔烙印\"},{\"type\":\"凿击\",\"text\":\"凿击\"},{\"type\":\"致疫打击\",\"text\":\"致疫打击\"},{\"type\":\"暗影迷踪\",\"text\":\"暗影迷踪\"},{\"type\":\"震波陷阱\",\"text\":\"震波陷阱\"},{\"type\":\"瘟疫使徒\",\"text\":\"瘟疫使徒\"},{\"type\":\"盗猎者印记\",\"text\":\"盗猎者印记\"},{\"type\":\"腐蚀箭矢\",\"text\":\"腐蚀箭矢\"},{\"type\":\"时空之门\",\"text\":\"时空之门\"},{\"type\":\"力量抽取\",\"text\":\"力量抽取\"},{\"type\":\"精准\",\"text\":\"精准\"},{\"type\":\"尊严\",\"text\":\"尊严\"},{\"type\":\"狙击之印\",\"text\":\"狙击之印\"},{\"type\":\"放血\",\"text\":\"放血\"},{\"type\":\"惩戒\",\"text\":\"惩戒\"},{\"type\":\"定罪波\",\"text\":\"定罪波\"},{\"type\":\"元素净化\",\"text\":\"元素净化\"},{\"type\":\"箭雨\",\"text\":\"箭雨\"},{\"type\":\"召唤灵体\",\"text\":\"召唤灵体\"},{\"type\":\"魔卫复苏\",\"text\":\"魔卫复苏\"},{\"type\":\"激励战吼\",\"text\":\"激励战吼\"},{\"type\":\"冲击波\",\"text\":\"冲击波\"},{\"type\":\"烙印召回\",\"text\":\"烙印召回\"},{\"type\":\"清算\",\"text\":\"清算\"},{\"type\":\"回春图腾\",\"text\":\"回春图腾\"},{\"type\":\"正义之火\",\"text\":\"正义之火\"},{\"type\":\"击刃\",\"text\":\"击刃\"},{\"type\":\"净化烈焰\",\"text\":\"净化烈焰\"},{\"type\":\"天灾之箭\",\"text\":\"天灾之箭\"},{\"type\":\"灼热连接\",\"text\":\"灼热连接\"},{\"type\":\"震地战吼\",\"text\":\"震地战吼\"},{\"type\":\"破碎铁刃\",\"text\":\"破碎铁刃\"},{\"type\":\"重盾冲锋\",\"text\":\"重盾冲锋\"},{\"type\":\"闪电新星\",\"text\":\"闪电新星\"},{\"type\":\"震波图腾\",\"text\":\"震波图腾\"},{\"type\":\"散射弩炮\",\"text\":\"散射弩炮\"},{\"type\":\"电光箭\",\"text\":\"电光箭\"},{\"type\":\"爆炸陷阱\",\"text\":\"爆炸陷阱\"},{\"type\":\"攻城炮台\",\"text\":\"攻城炮台\"},{\"type\":\"威能法印\",\"text\":\"威能法印\"},{\"type\":\"惩击\",\"text\":\"惩击\"},{\"type\":\"烟雾地雷\",\"text\":\"烟雾地雷\"},{\"type\":\"裂魂术\",\"text\":\"裂魂术\"},{\"type\":\"电球\",\"text\":\"电球\"},{\"type\":\"奋锐光环\",\"text\":\"奋锐光环\"},{\"type\":\"法术节魔\",\"text\":\"法术节魔\"},{\"type\":\"灵魂奉献\",\"text\":\"灵魂奉献\"},{\"type\":\"分裂箭矢\",\"text\":\"分裂箭矢\"},{\"type\":\"分裂钢刃\",\"text\":\"分裂钢刃\"},{\"type\":\"充能打击\",\"text\":\"充能打击\"},{\"type\":\"钢铁之肤\",\"text\":\"钢铁之肤\"},{\"type\":\"缚雷之纹\",\"text\":\"缚雷之纹\"},{\"type\":\"雷暴地雷\",\"text\":\"雷暴地雷\"},{\"type\":\"风暴烙印\",\"text\":\"风暴烙印\"},{\"type\":\"裂风雷球\",\"text\":\"裂风雷球\"},{\"type\":\"风暴呼唤\",\"text\":\"风暴呼唤\"},{\"type\":\"召唤腐化魔像\",\"text\":\"召唤腐化魔像\"},{\"type\":\"召唤混沌魔像\",\"text\":\"召唤混沌魔像\"},{\"type\":\"召唤烈焰魔像\",\"text\":\"召唤烈焰魔像\"},{\"type\":\"召唤寒冰魔像\",\"text\":\"召唤寒冰魔像\"},{\"type\":\"召唤闪电魔像\",\"text\":\"召唤闪电魔像\"},{\"type\":\"召唤愤怒狂灵\",\"text\":\"召唤愤怒狂灵\"},{\"type\":\"召唤圣物\",\"text\":\"召唤圣物\"},{\"type\":\"召唤巨石魔像\",\"text\":\"召唤巨石魔像\"},{\"type\":\"召唤魔侍\",\"text\":\"召唤魔侍\"},{\"type\":\"召唤飞掠者\",\"text\":\"召唤飞掠者\"},{\"type\":\"大地震击\",\"text\":\"大地震击\"},{\"type\":\"横扫\",\"text\":\"横扫\"},{\"type\":\"破釜一击\",\"text\":\"破釜一击\"},{\"type\":\"暴风之盾\",\"text\":\"暴风之盾\"},{\"type\":\"时空锁链\",\"text\":\"时空锁链\"},{\"type\":\"灵盾投掷\",\"text\":\"灵盾投掷\"},{\"type\":\"灵体投掷\",\"text\":\"灵体投掷\"},{\"type\":\"龙卷射击\",\"text\":\"龙卷射击\"},{\"type\":\"毒雨\",\"text\":\"毒雨\"},{\"type\":\"瓦尔.先祖战士长\",\"text\":\"瓦尔.先祖战士长\"},{\"type\":\"瓦尔.电弧\",\"text\":\"瓦尔.电弧\"},{\"type\":\"瓦尔.飞刃风暴\",\"text\":\"瓦尔.飞刃风暴\"},{\"type\":\"瓦尔.枯萎\",\"text\":\"瓦尔.枯萎\"},{\"type\":\"瓦尔.燃烧箭矢\",\"text\":\"瓦尔.燃烧箭矢\"},{\"type\":\"瓦尔.清晰\",\"text\":\"瓦尔.清晰\"},{\"type\":\"瓦尔.不净之冰\",\"text\":\"瓦尔.不净之冰\"},{\"type\":\"瓦尔.霜暴\",\"text\":\"瓦尔.霜暴\"},{\"type\":\"瓦尔.旋风斩\",\"text\":\"瓦尔.旋风斩\"},{\"type\":\"瓦尔.爆灵术\",\"text\":\"瓦尔.爆灵术\"},{\"type\":\"瓦尔.纪律\",\"text\":\"瓦尔.纪律\"},{\"type\":\"瓦尔.双重打击\",\"text\":\"瓦尔.双重打击\"},{\"type\":\"瓦尔.震地\",\"text\":\"瓦尔.震地\"},{\"type\":\"瓦尔.火球\",\"text\":\"瓦尔.火球\"},{\"type\":\"瓦尔.不净之火\",\"text\":\"瓦尔.不净之火\"},{\"type\":\"瓦尔.烈焰爆破\",\"text\":\"瓦尔.烈焰爆破\"},{\"type\":\"瓦尔.冰霜之锤\",\"text\":\"瓦尔.冰霜之锤\"},{\"type\":\"瓦尔.优雅\",\"text\":\"瓦尔.优雅\"},{\"type\":\"瓦尔.裂地之击\",\"text\":\"瓦尔.裂地之击\"},{\"type\":\"瓦尔.迅捷\",\"text\":\"瓦尔.迅捷\"},{\"type\":\"瓦尔.冰霜新星\",\"text\":\"瓦尔.冰霜新星\"},{\"type\":\"瓦尔.不朽怒嚎\",\"text\":\"瓦尔.不朽怒嚎\"},{\"type\":\"瓦尔.不净之雷\",\"text\":\"瓦尔.不净之雷\"},{\"type\":\"瓦尔.闪电打击\",\"text\":\"瓦尔.闪电打击\"},{\"type\":\"瓦尔.闪电陷阱\",\"text\":\"瓦尔.闪电陷阱\"},{\"type\":\"瓦尔.闪电传送\",\"text\":\"瓦尔.闪电传送\"},{\"type\":\"瓦尔.熔岩护盾\",\"text\":\"瓦尔.熔岩护盾\"},{\"type\":\"瓦尔.裂隙\",\"text\":\"瓦尔.裂隙\"},{\"type\":\"瓦尔.力量抽取\",\"text\":\"瓦尔.力量抽取\"},{\"type\":\"瓦尔.箭雨\",\"text\":\"瓦尔.箭雨\"},{\"type\":\"瓦尔.冲击波\",\"text\":\"瓦尔.冲击波\"},{\"type\":\"瓦尔.正义之火\",\"text\":\"瓦尔.正义之火\"},{\"type\":\"瓦尔.电球\",\"text\":\"瓦尔.电球\"},{\"type\":\"瓦尔.风暴呼唤\",\"text\":\"瓦尔.风暴呼唤\"},{\"type\":\"瓦尔.召唤魔侍\",\"text\":\"瓦尔.召唤魔侍\"},{\"type\":\"瓦尔.灵体投掷\",\"text\":\"瓦尔.灵体投掷\"},{\"type\":\"复仇\",\"text\":\"复仇\"},{\"type\":\"剧毒旋风\",\"text\":\"剧毒旋风\"},{\"type\":\"戒备打击\",\"text\":\"戒备打击\"},{\"type\":\"毒蛇打击\",\"text\":\"毒蛇打击\"},{\"type\":\"活力\",\"text\":\"活力\"},{\"type\":\"虚空法球\",\"text\":\"虚空法球\"},{\"type\":\"灵体火球\",\"text\":\"灵体火球\"},{\"type\":\"绝望\",\"text\":\"绝望\"},{\"type\":\"战旗\",\"text\":\"战旗\"},{\"type\":\"督军印记\",\"text\":\"督军印记\"},{\"type\":\"回旋之刃\",\"text\":\"回旋之刃\"},{\"type\":\"野性打击\",\"text\":\"野性打击\"},{\"type\":\"寒冬宝珠\",\"text\":\"寒冬宝珠\"},{\"type\":\"冬潮烙印\",\"text\":\"冬潮烙印\"},{\"type\":\"死亡凋零\",\"text\":\"死亡凋零\"},{\"type\":\"凋零步\",\"text\":\"凋零步\"},{\"type\":\"雷霆\",\"text\":\"雷霆\"},{\"type\":\"附加混沌伤害(辅)\",\"text\":\"附加混沌伤害(辅)\"},{\"type\":\"附加混沌伤害（强辅）\",\"text\":\"附加混沌伤害（强辅）\"},{\"type\":\"附加冰霜伤害(辅)\",\"text\":\"附加冰霜伤害(辅)\"},{\"type\":\"附加冰霜伤害（强辅）\",\"text\":\"附加冰霜伤害（强辅）\"},{\"type\":\"附加火焰伤害(辅)\",\"text\":\"附加火焰伤害(辅)\"},{\"type\":\"附加火焰伤害（强辅）\",\"text\":\"附加火焰伤害（强辅）\"},{\"type\":\"附加闪电伤害(辅)\",\"text\":\"附加闪电伤害(辅)\"},{\"type\":\"附加闪电伤害（强辅）\",\"text\":\"附加闪电伤害（强辅）\"},{\"type\":\"额外命中(辅)\",\"text\":\"额外命中(辅)\"},{\"type\":\"赋予(辅)\",\"text\":\"赋予(辅)\"},{\"type\":\"增幅(辅)\",\"text\":\"增幅(辅)\"},{\"type\":\"启蒙(辅)\",\"text\":\"启蒙(辅)\"},{\"type\":\"先祖召唤（强辅）\",\"text\":\"先祖召唤（强辅）\"},{\"type\":\"秘术增强(辅)\",\"text\":\"秘术增强(辅)\"},{\"type\":\"大法师（辅）\",\"text\":\"大法师（辅）\"},{\"type\":\"箭之新星（辅）\",\"text\":\"箭之新星（辅）\"},{\"type\":\"箭之新星（强辅）\",\"text\":\"箭之新星（强辅）\"},{\"type\":\"弹幕（辅）\",\"text\":\"弹幕（辅）\"},{\"type\":\"诅咒光环(辅)\",\"text\":\"诅咒光环(辅)\"},{\"type\":\"渎神（强辅）\",\"text\":\"渎神（强辅）\"},{\"type\":\"致盲(辅)\",\"text\":\"致盲(辅)\"},{\"type\":\"减少格挡几率(辅)\",\"text\":\"减少格挡几率(辅)\"},{\"type\":\"嗜血(辅)\",\"text\":\"嗜血(辅)\"},{\"type\":\"血魔法(辅)\",\"text\":\"血魔法(辅)\"},{\"type\":\"彻骨（辅）\",\"text\":\"彻骨（辅）\"},{\"type\":\"残暴(辅)\",\"text\":\"残暴(辅)\"},{\"type\":\"残暴（强辅）\",\"text\":\"残暴（强辅）\"},{\"type\":\"增加燃烧伤害（强辅）\",\"text\":\"增加燃烧伤害（强辅）\"},{\"type\":\"暴击时施放(辅)\",\"text\":\"暴击时施放(辅)\"},{\"type\":\"暴击时施放（强辅）\",\"text\":\"暴击时施放（强辅）\"},{\"type\":\"受伤时施放(辅)\",\"text\":\"受伤时施放(辅)\"},{\"type\":\"死亡时施放(辅)\",\"text\":\"死亡时施放(辅)\"},{\"type\":\"近战击败时施放(辅)\",\"text\":\"近战击败时施放(辅)\"},{\"type\":\"晕眩时施放(辅)\",\"text\":\"晕眩时施放(辅)\"},{\"type\":\"吟唱时施放(辅)\",\"text\":\"吟唱时施放(辅)\"},{\"type\":\"吟唱时施放（强辅）\",\"text\":\"吟唱时施放（强辅）\"},{\"type\":\"连锁(辅)\",\"text\":\"连锁(辅)\"},{\"type\":\"连锁（强辅）\",\"text\":\"连锁（强辅）\"},{\"type\":\"几率流血(辅)\",\"text\":\"几率流血(辅)\"},{\"type\":\"几率逃跑(辅)\",\"text\":\"几率逃跑(辅)\"},{\"type\":\"几率点燃(辅)\",\"text\":\"几率点燃(辅)\"},{\"type\":\"凋零之触（辅）\",\"text\":\"凋零之触（辅）\"},{\"type\":\"充能地雷（辅）\",\"text\":\"充能地雷（辅）\"},{\"type\":\"近战（辅）\",\"text\":\"近战（辅）\"},{\"type\":\"散弹陷阱(辅)\",\"text\":\"散弹陷阱(辅)\"},{\"type\":\"冰霜穿透(辅)\",\"text\":\"冰霜穿透(辅)\"},{\"type\":\"冰霜穿透（强辅）\",\"text\":\"冰霜穿透（强辅）\"},{\"type\":\"寒冰转烈焰(辅)\",\"text\":\"寒冰转烈焰(辅)\"},{\"type\":\"集中效应(辅)\",\"text\":\"集中效应(辅)\"},{\"type\":\"精准破坏(辅)\",\"text\":\"精准破坏(辅)\"},{\"type\":\"精准破坏（强辅）\",\"text\":\"精准破坏（强辅）\"},{\"type\":\"终结(辅)\",\"text\":\"终结(辅)\"},{\"type\":\"蛊咒（辅）\",\"text\":\"蛊咒（辅）\"},{\"type\":\"蛊咒【强辅】\",\"text\":\"蛊咒【强辅】\"},{\"type\":\"急冻(辅)\",\"text\":\"急冻(辅)\"},{\"type\":\"致命异常状态(辅)\",\"text\":\"致命异常状态(辅)\"},{\"type\":\"致命异常状态（强辅）\",\"text\":\"致命异常状态（强辅）\"},{\"type\":\"掠食（辅）\",\"text\":\"掠食（辅）\"},{\"type\":\"腐蚀(辅)\",\"text\":\"腐蚀(辅)\"},{\"type\":\"效能(辅)\",\"text\":\"效能(辅)\"},{\"type\":\"元素集中(辅)\",\"text\":\"元素集中(辅)\"},{\"type\":\"元素集中（强辅）\",\"text\":\"元素集中（强辅）\"},{\"type\":\"元素穿透（辅）\",\"text\":\"元素穿透（辅）\"},{\"type\":\"元素扩散(辅)\",\"text\":\"元素扩散(辅)\"},{\"type\":\"近战击晕获得耐力球(辅)\",\"text\":\"近战击晕获得耐力球(辅)\"},{\"type\":\"能量偷取（辅）\",\"text\":\"能量偷取（辅）\"},{\"type\":\"快速攻击(辅)\",\"text\":\"快速攻击(辅)\"},{\"type\":\"快速施法(辅)\",\"text\":\"快速施法(辅)\"},{\"type\":\"快速投射(辅)\",\"text\":\"快速投射(辅)\"},{\"type\":\"狂噬（辅）\",\"text\":\"狂噬（辅）\"},{\"type\":\"火焰穿透(辅)\",\"text\":\"火焰穿透(辅)\"},{\"type\":\"火焰穿透（强辅）\",\"text\":\"火焰穿透（强辅）\"},{\"type\":\"战争铁拳（辅）\",\"text\":\"战争铁拳（辅）\"},{\"type\":\"分裂(辅)\",\"text\":\"分裂(辅)\"},{\"type\":\"分裂（强辅）\",\"text\":\"分裂（强辅）\"},{\"type\":\"护体(辅)\",\"text\":\"护体(辅)\"},{\"type\":\"霜咬(辅)\",\"text\":\"霜咬(辅)\"},{\"type\":\"充能陷阱（辅）\",\"text\":\"充能陷阱（辅）\"},{\"type\":\"和善(辅)\",\"text\":\"和善(辅)\"},{\"type\":\"和善（强辅）\",\"text\":\"和善（强辅）\"},{\"type\":\"高阶多重投射(辅)\",\"text\":\"高阶多重投射(辅)\"},{\"type\":\"高阶多重投射（强辅）\",\"text\":\"高阶多重投射（强辅）\"},{\"type\":\"高阶齐射（辅）\",\"text\":\"高阶齐射（辅）\"},{\"type\":\"释出（辅）\",\"text\":\"释出（辅）\"},{\"type\":\"法术凝聚（辅）\",\"text\":\"法术凝聚（辅）\"},{\"type\":\"高爆地雷（辅）\",\"text\":\"高爆地雷（辅）\"},{\"type\":\"点燃扩散(辅)\",\"text\":\"点燃扩散(辅)\"},{\"type\":\"献祭(辅)\",\"text\":\"献祭(辅)\"},{\"type\":\"尖刺战杖\",\"text\":\"尖刺战杖\"},{\"type\":\"末日将至（辅）\",\"text\":\"末日将至（辅）\"},{\"type\":\"增大范围(辅)\",\"text\":\"增大范围(辅)\"},{\"type\":\"效果区域扩大（强辅）\",\"text\":\"效果区域扩大（强辅）\"},{\"type\":\"增加燃烧伤害(辅)\",\"text\":\"增加燃烧伤害(辅)\"},{\"type\":\"提高暴击伤害(辅)\",\"text\":\"提高暴击伤害(辅)\"},{\"type\":\"提高暴击几率(辅)\",\"text\":\"提高暴击几率(辅)\"},{\"type\":\"持续时间延长(辅)\",\"text\":\"持续时间延长(辅)\"},{\"type\":\"炎军（辅）\",\"text\":\"炎军（辅）\"},{\"type\":\"钢铁之握(辅)\",\"text\":\"钢铁之握(辅)\"},{\"type\":\"钢铁意志(辅)\",\"text\":\"钢铁意志(辅)\"},{\"type\":\"物品稀有度增幅(辅)\",\"text\":\"物品稀有度增幅(辅)\"},{\"type\":\"击退(辅)\",\"text\":\"击退(辅)\"},{\"type\":\"低阶多重投射(辅)\",\"text\":\"低阶多重投射(辅)\"},{\"type\":\"低阶毒化(辅)\",\"text\":\"低阶毒化(辅)\"},{\"type\":\"击中生命回复(辅)\",\"text\":\"击中生命回复(辅)\"},{\"type\":\"生命偷取(辅)\",\"text\":\"生命偷取(辅)\"},{\"type\":\"闪电穿透(辅)\",\"text\":\"闪电穿透(辅)\"},{\"type\":\"闪电穿透（强辅）\",\"text\":\"闪电穿透（强辅）\"},{\"type\":\"瘫痪(辅)\",\"text\":\"瘫痪(辅)\"},{\"type\":\"魔力偷取(辅)\",\"text\":\"魔力偷取(辅)\"},{\"type\":\"肉盾（辅）\",\"text\":\"肉盾（辅）\"},{\"type\":\"满血伤害（辅）\",\"text\":\"满血伤害（辅）\"},{\"type\":\"近战物理伤害(辅)\",\"text\":\"近战物理伤害(辅)\"},{\"type\":\"近战物理伤害（强辅）\",\"text\":\"近战物理伤害（强辅）\"},{\"type\":\"近战伤害扩散(辅)\",\"text\":\"近战伤害扩散(辅)\"},{\"type\":\"近战伤害扩散（强辅）\",\"text\":\"近战伤害扩散（强辅）\"},{\"type\":\"地雷网(辅)\",\"text\":\"地雷网(辅)\"},{\"type\":\"召唤生物伤害(辅)\",\"text\":\"召唤生物伤害(辅)\"},{\"type\":\"召唤生物伤害（强辅）\",\"text\":\"召唤生物伤害（强辅）\"},{\"type\":\"召唤生物生命(辅)\",\"text\":\"召唤生物生命(辅)\"},{\"type\":\"召唤生物速度(辅)\",\"text\":\"召唤生物速度(辅)\"},{\"type\":\"幻影射手(辅)\",\"text\":\"幻影射手(辅)\"},{\"type\":\"施法回响(辅)\",\"text\":\"施法回响(辅)\"},{\"type\":\"多重打击(辅)\",\"text\":\"多重打击(辅)\"},{\"type\":\"多重打击（强辅）\",\"text\":\"多重打击（强辅）\"},{\"type\":\"多重图腾（辅）\",\"text\":\"多重图腾（辅）\"},{\"type\":\"多重陷阱(辅)\",\"text\":\"多重陷阱(辅)\"},{\"type\":\"夜刃（辅）\",\"text\":\"夜刃（辅）\"},{\"type\":\"猛攻(辅)\",\"text\":\"猛攻(辅)\"},{\"type\":\"闪电支配(辅)\",\"text\":\"闪电支配(辅)\"},{\"type\":\"齐射(辅)\",\"text\":\"齐射(辅)\"},{\"type\":\"邪恶投掷(辅)\",\"text\":\"邪恶投掷(辅)\"},{\"type\":\"物理转闪电(辅)\",\"text\":\"物理转闪电(辅)\"},{\"type\":\"穿透(辅)\",\"text\":\"穿透(辅)\"},{\"type\":\"会心一击（辅）\",\"text\":\"会心一击（辅）\"},{\"type\":\"零点射击(辅)\",\"text\":\"零点射击(辅)\"},{\"type\":\"毒化(辅)\",\"text\":\"毒化(辅)\"},{\"type\":\"暴击获得暴击球(辅)\",\"text\":\"暴击获得暴击球(辅)\"},{\"type\":\"粉碎（辅）\",\"text\":\"粉碎（辅）\"},{\"type\":\"怒火（辅）\",\"text\":\"怒火（辅）\"},{\"type\":\"弩炮图腾（辅）\",\"text\":\"弩炮图腾（辅）\"},{\"type\":\"极速腐化(辅)\",\"text\":\"极速腐化(辅)\"},{\"type\":\"持续时间缩短(辅)\",\"text\":\"持续时间缩短(辅)\"},{\"type\":\"启迪（辅）\",\"text\":\"启迪（辅）\"},{\"type\":\"链爆地雷（辅）\",\"text\":\"链爆地雷（辅）\"},{\"type\":\"无情(辅)\",\"text\":\"无情(辅)\"},{\"type\":\"助力之风（辅）\",\"text\":\"助力之风（辅）\"},{\"type\":\"震波（辅）\",\"text\":\"震波（辅）\"},{\"type\":\"投射物减速(辅)\",\"text\":\"投射物减速(辅)\"},{\"type\":\"多重范围施法(辅)\",\"text\":\"多重范围施法(辅)\"},{\"type\":\"多重范围施法（强辅）\",\"text\":\"多重范围施法（强辅）\"},{\"type\":\"施法回响（强辅）\",\"text\":\"施法回响（强辅）\"},{\"type\":\"法术图腾(辅)\",\"text\":\"法术图腾(辅)\"},{\"type\":\"先祖召唤(辅)\",\"text\":\"先祖召唤(辅)\"},{\"type\":\"灌能吟唱(辅)\",\"text\":\"灌能吟唱(辅)\"},{\"type\":\"击晕(辅)\",\"text\":\"击晕(辅)\"},{\"type\":\"元素大军（辅）\",\"text\":\"元素大军（辅）\"},{\"type\":\"召唤幻影（辅）\",\"text\":\"召唤幻影（辅）\"},{\"type\":\"极速腐化（强辅）\",\"text\":\"极速腐化（强辅）\"},{\"type\":\"迅整（辅）\",\"text\":\"迅整（辅）\"},{\"type\":\"迅猛烙印（辅）\",\"text\":\"迅猛烙印（辅）\"},{\"type\":\"陷阱(辅)\",\"text\":\"陷阱(辅)\"},{\"type\":\"陷阱及地雷伤害(辅)\",\"text\":\"陷阱及地雷伤害(辅)\"},{\"type\":\"陷阱冷却(辅)\",\"text\":\"陷阱冷却(辅)\"},{\"type\":\"异常爆发(辅)\",\"text\":\"异常爆发(辅)\"},{\"type\":\"异常爆发（强辅）\",\"text\":\"异常爆发（强辅）\"},{\"type\":\"释出（强辅）\",\"text\":\"释出（强辅）\"},{\"type\":\"紧急号令（辅）\",\"text\":\"紧急号令（辅）\"},{\"type\":\"邪恶投掷（强辅）\",\"text\":\"邪恶投掷（强辅）\"},{\"type\":\"猛毒(辅)\",\"text\":\"猛毒(辅)\"},{\"type\":\"虚空操纵(辅)\",\"text\":\"虚空操纵(辅)\"},{\"type\":\"虚空操纵（强辅）\",\"text\":\"虚空操纵（强辅）\"},{\"type\":\"武器元素伤害(辅)\",\"text\":\"武器元素伤害(辅)\"},{\"type\":\"武器元素伤害（强辅）\",\"text\":\"武器元素伤害（强辅）\"}]";
        } else if (itemTag.equals("预言")) {
            return "[{\"name\":\"炼金师\",\"type\":\"预言\",\"text\":\"炼金师 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"混乱的尽头 I\",\"type\":\"预言\",\"text\":\"混乱的尽头 I 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"混乱的尽头 II\",\"type\":\"预言\",\"text\":\"混乱的尽头 II 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"混乱的尽头 III\",\"type\":\"预言\",\"text\":\"混乱的尽头 III 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"混乱的尽头 IV\",\"type\":\"预言\",\"text\":\"混乱的尽头 IV 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"复制人\",\"type\":\"预言\",\"text\":\"复制人 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"附身的敌人\",\"type\":\"预言\",\"text\":\"附身的敌人 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"入侵者\",\"type\":\"预言\",\"text\":\"入侵者 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"溺水者的视线\",\"type\":\"预言\",\"text\":\"溺水者的视线 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"巨大的宝物\",\"type\":\"预言\",\"text\":\"巨大的宝物 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"瓦尔侵袭\",\"type\":\"预言\",\"text\":\"瓦尔侵袭 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"饥饿的一群\",\"type\":\"预言\",\"text\":\"饥饿的一群 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"颤抖地面\",\"type\":\"预言\",\"text\":\"颤抖地面 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"诅咒之音\",\"type\":\"预言\",\"text\":\"诅咒之音 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"土壤、虫和血\",\"type\":\"预言\",\"text\":\"土壤、虫和血 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"工匠魂\",\"type\":\"预言\",\"text\":\"工匠魂 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"被遗忘的驻军\",\"type\":\"预言\",\"text\":\"被遗忘的驻军 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"丝克玛之歌\",\"type\":\"预言\",\"text\":\"丝克玛之歌 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"女王的秘宝库\",\"type\":\"预言\",\"text\":\"女王的秘宝库 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"女王的牺牲\",\"type\":\"预言\",\"text\":\"女王的牺牲 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"超越视线 I\",\"type\":\"预言\",\"text\":\"超越视线 I 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"超越视线 II\",\"type\":\"预言\",\"text\":\"超越视线 II 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"超越视线 III\",\"type\":\"预言\",\"text\":\"超越视线 III 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"超越视线 IV\",\"type\":\"预言\",\"text\":\"超越视线 IV 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"冷血狂怒\",\"type\":\"预言\",\"text\":\"冷血狂怒 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"重建连结\",\"type\":\"预言\",\"text\":\"重建连结 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"最后一个野人\",\"type\":\"预言\",\"text\":\"最后一个野人 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"冷酷的贪婪\",\"type\":\"预言\",\"text\":\"冷酷的贪婪 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"有价值的组合\",\"type\":\"预言\",\"text\":\"有价值的组合 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"占卜师的收藏\",\"type\":\"预言\",\"text\":\"占卜师的收藏 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"月影之子\",\"type\":\"预言\",\"text\":\"月影之子 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"信仰恢复\",\"type\":\"预言\",\"text\":\"信仰恢复 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"两次附魔\",\"type\":\"预言\",\"text\":\"两次附魔 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"奉献之日 I\",\"type\":\"预言\",\"text\":\"奉献之日 I 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"奉献之日 II\",\"type\":\"预言\",\"text\":\"奉献之日 II 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"奉献之日 III\",\"type\":\"预言\",\"text\":\"奉献之日 III 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"奉献之日 IV\",\"type\":\"预言\",\"text\":\"奉献之日 IV 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"死对头 I\",\"type\":\"预言\",\"text\":\"死对头 I 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"死对头 II\",\"type\":\"预言\",\"text\":\"死对头 II 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"死对头 III\",\"type\":\"预言\",\"text\":\"死对头 III 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"死对头 IV\",\"type\":\"预言\",\"text\":\"死对头 IV 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"死对头 V\",\"type\":\"预言\",\"text\":\"死对头 V 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"邪灵女巫\",\"type\":\"预言\",\"text\":\"邪灵女巫 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"烈焰之怖\",\"type\":\"预言\",\"text\":\"烈焰之怖 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"暮色之痛\",\"type\":\"预言\",\"text\":\"暮色之痛 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"蔽目之光\",\"type\":\"预言\",\"text\":\"蔽目之光 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"富有的流放者\",\"type\":\"预言\",\"text\":\"富有的流放者 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"侦查兵\",\"type\":\"预言\",\"text\":\"侦查兵 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"休耕\",\"type\":\"预言\",\"text\":\"休耕 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"受困象牙塔\",\"type\":\"预言\",\"text\":\"受困象牙塔 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"黑暗本能\",\"type\":\"预言\",\"text\":\"黑暗本能 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"蛙疫\",\"type\":\"预言\",\"text\":\"蛙疫 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"黑暗狂热\",\"type\":\"预言\",\"text\":\"黑暗狂热 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"主教的遗产\",\"type\":\"预言\",\"text\":\"主教的遗产 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"财富池\",\"type\":\"预言\",\"text\":\"财富池 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"猩红之色\",\"type\":\"预言\",\"text\":\"猩红之色 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"冰与火的视界\",\"type\":\"预言\",\"text\":\"冰与火的视界 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"卑贱之死\",\"type\":\"预言\",\"text\":\"卑贱之死 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"光之终点\",\"type\":\"预言\",\"text\":\"光之终点 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"贪婪的复仇\",\"type\":\"预言\",\"text\":\"贪婪的复仇 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"身经百战\",\"type\":\"预言\",\"text\":\"身经百战 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"麻雀变凤凰\",\"type\":\"预言\",\"text\":\"麻雀变凤凰 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"反叛之路\",\"type\":\"预言\",\"text\":\"反叛之路 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"虚空\",\"type\":\"预言\",\"text\":\"虚空 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"误会女王\",\"type\":\"预言\",\"text\":\"误会女王 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"自然的抵抗\",\"type\":\"预言\",\"text\":\"自然的抵抗 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"火焰气息\",\"type\":\"预言\",\"text\":\"火焰气息 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"国王和荆棘\",\"type\":\"预言\",\"text\":\"国王和荆棘 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"恐惧之口\",\"type\":\"预言\",\"text\":\"恐惧之口 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"仆人之心\",\"type\":\"预言\",\"text\":\"仆人之心 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"卡鲁叛军\",\"type\":\"预言\",\"text\":\"卡鲁叛军 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"终极鲜血之花\",\"type\":\"预言\",\"text\":\"终极鲜血之花 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"终结的起点\",\"type\":\"预言\",\"text\":\"终结的起点 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"沉重打击\",\"type\":\"预言\",\"text\":\"沉重打击 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"死亡之泣\",\"type\":\"预言\",\"text\":\"死亡之泣 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"冰与火\",\"type\":\"预言\",\"text\":\"冰与火 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"肢解\",\"type\":\"预言\",\"text\":\"肢解 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"火和硫磺\",\"type\":\"预言\",\"text\":\"火和硫磺 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"顶尖掠食者\",\"type\":\"预言\",\"text\":\"顶尖掠食者 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"银色丛林\",\"type\":\"预言\",\"text\":\"银色丛林 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"国王之道\",\"type\":\"预言\",\"text\":\"国王之道 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"隐藏的后援\",\"type\":\"预言\",\"text\":\"隐藏的后援 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"弓弦之乐\",\"type\":\"预言\",\"text\":\"弓弦之乐 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"重生\",\"type\":\"预言\",\"text\":\"重生 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"力量扩大\",\"type\":\"预言\",\"text\":\"力量扩大 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"内部镀金\",\"type\":\"预言\",\"text\":\"内部镀金 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"终止苦难\",\"type\":\"预言\",\"text\":\"终止苦难 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"猎人的课堂\",\"type\":\"预言\",\"text\":\"猎人的课堂 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"快乐与苦痛\",\"type\":\"预言\",\"text\":\"快乐与苦痛 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"能源流动\",\"type\":\"预言\",\"text\":\"能源流动 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"冬季悲歌\",\"type\":\"预言\",\"text\":\"冬季悲歌 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"伪神之林\",\"type\":\"预言\",\"text\":\"伪神之林 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"在记忆里划一个叉\",\"type\":\"预言\",\"text\":\"在记忆里划一个叉 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"古代末日\",\"type\":\"预言\",\"text\":\"古代末日 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"梦魇苏醒\",\"type\":\"预言\",\"text\":\"梦魇苏醒 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"狂想者之梦境\",\"type\":\"预言\",\"text\":\"狂想者之梦境 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"梦之试炼\",\"type\":\"预言\",\"text\":\"梦之试炼 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"隐藏瓦尔通道\",\"type\":\"预言\",\"text\":\"隐藏瓦尔通道 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"女巫之回音\",\"type\":\"预言\",\"text\":\"女巫之回音 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"地方的大师需要支持\",\"type\":\"预言\",\"disc\":\"alva\",\"text\":\"地方的大师需要支持 预言 (阿尔瓦)\",\"flags\":{\"prophecy\":true}},{\"name\":\"地方的大师需要支持\",\"type\":\"预言\",\"disc\":\"einhar\",\"text\":\"地方的大师需要支持 预言 (伊恩哈尔)\",\"flags\":{\"prophecy\":true}},{\"name\":\"地方的大师需要支持\",\"type\":\"预言\",\"disc\":\"niko\",\"text\":\"地方的大师需要支持 预言 (尼克)\",\"flags\":{\"prophecy\":true}},{\"name\":\"地方的大师需要支持\",\"type\":\"预言\",\"disc\":\"jun\",\"text\":\"地方的大师需要支持 预言 (6月)\",\"flags\":{\"prophecy\":true}},{\"name\":\"丰富的陷阱\",\"type\":\"预言\",\"text\":\"丰富的陷阱 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"地方的大师需要支持\",\"type\":\"预言\",\"disc\":\"zana\",\"text\":\"地方的大师需要支持 预言 (札娜)\",\"flags\":{\"prophecy\":true}},{\"name\":\"不幸之神\",\"type\":\"预言\",\"text\":\"不幸之神 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"不死蛮力\",\"type\":\"预言\",\"text\":\"不死蛮力 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"四骑士\",\"type\":\"预言\",\"text\":\"四骑士 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"瓦尔烈风\",\"type\":\"预言\",\"text\":\"瓦尔烈风 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"烈焰\",\"type\":\"预言\",\"text\":\"烈焰 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"冰霜\",\"type\":\"预言\",\"text\":\"冰霜 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"雷霆\",\"type\":\"预言\",\"text\":\"雷霆 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"冲击飓风\",\"type\":\"预言\",\"text\":\"冲击飓风 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"不死风暴\",\"type\":\"预言\",\"text\":\"不死风暴 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"致命双胞胎\",\"type\":\"预言\",\"text\":\"致命双胞胎 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"充满感激的大师\",\"type\":\"预言\",\"text\":\"充满感激的大师 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"导师\",\"type\":\"预言\",\"text\":\"导师 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"溢出的财富\",\"type\":\"预言\",\"text\":\"溢出的财富 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"神秘入侵者\",\"type\":\"预言\",\"text\":\"神秘入侵者 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"奇术史 I\",\"type\":\"预言\",\"text\":\"奇术史 I 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"奇术史 II\",\"type\":\"预言\",\"text\":\"奇术史 II 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"奇术史 III\",\"type\":\"预言\",\"text\":\"奇术史 III 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"奇术史 IV\",\"type\":\"预言\",\"text\":\"奇术史 IV 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"驱魔\",\"type\":\"预言\",\"text\":\"驱魔 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"帝王之陨\",\"type\":\"预言\",\"text\":\"帝王之陨 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"最后的哨站\",\"type\":\"预言\",\"text\":\"最后的哨站 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"无灵野兽\",\"type\":\"预言\",\"text\":\"无灵野兽 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"沉默监督者\",\"type\":\"预言\",\"text\":\"沉默监督者 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"狱卒\",\"type\":\"预言\",\"text\":\"狱卒 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"牢房钥匙\",\"type\":\"预言\",\"text\":\"牢房钥匙 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"守卫的守卫\",\"type\":\"预言\",\"text\":\"守卫的守卫 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"黑衣女士\",\"type\":\"预言\",\"text\":\"黑衣女士 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"暴力执行者\",\"type\":\"预言\",\"text\":\"暴力执行者 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"野兽之躯\",\"type\":\"预言\",\"text\":\"野兽之躯 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"地平在线的风暴\",\"type\":\"预言\",\"text\":\"地平在线的风暴 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"巢穴\",\"type\":\"预言\",\"text\":\"巢穴 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"异常光辉\",\"type\":\"预言\",\"text\":\"异常光辉 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"火、木、石\",\"type\":\"预言\",\"text\":\"火、木、石 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"死亡洗礼\",\"type\":\"预言\",\"text\":\"死亡洗礼 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"漫延的恐惧\",\"type\":\"预言\",\"text\":\"漫延的恐惧 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"抵抗潮汐\",\"type\":\"预言\",\"text\":\"抵抗潮汐 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"死亡孕育生命\",\"type\":\"预言\",\"text\":\"死亡孕育生命 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"死泣\",\"type\":\"预言\",\"text\":\"死泣 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"海岸上的风暴\",\"type\":\"预言\",\"text\":\"海岸上的风暴 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"蛮牛\",\"type\":\"预言\",\"text\":\"蛮牛 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"优美的火焰\",\"type\":\"预言\",\"text\":\"优美的火焰 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"轻声祈祷\",\"type\":\"预言\",\"text\":\"轻声祈祷 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"风与雷\",\"type\":\"预言\",\"text\":\"风与雷 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"皮开肉绽\",\"type\":\"预言\",\"text\":\"皮开肉绽 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"反叛之血\",\"type\":\"预言\",\"text\":\"反叛之血 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"污点\",\"type\":\"预言\",\"text\":\"污点 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"剑士之热情\",\"type\":\"预言\",\"text\":\"剑士之热情 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"充血\",\"type\":\"预言\",\"text\":\"充血 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"崛起血液\",\"type\":\"预言\",\"text\":\"崛起血液 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"鹰嗷\",\"type\":\"预言\",\"text\":\"鹰嗷 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"萝丝传说\",\"type\":\"预言\",\"text\":\"萝丝传说 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"石化\",\"type\":\"预言\",\"text\":\"石化 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"躯干破口\",\"type\":\"预言\",\"text\":\"躯干破口 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"移动山丘\",\"type\":\"预言\",\"text\":\"移动山丘 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"稳固立足点\",\"type\":\"预言\",\"text\":\"稳固立足点 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"罪人之石\",\"type\":\"预言\",\"text\":\"罪人之石 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"失落的不死\",\"type\":\"预言\",\"text\":\"失落的不死 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"奇妙之手\",\"type\":\"预言\",\"text\":\"奇妙之手 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"虚空之唤\",\"type\":\"预言\",\"text\":\"虚空之唤 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"空洞誓言\",\"type\":\"预言\",\"text\":\"空洞誓言 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"遗失于书页\",\"type\":\"预言\",\"text\":\"遗失于书页 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"洗净罪孽\",\"type\":\"预言\",\"text\":\"洗净罪孽 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"烈焰之心\",\"type\":\"预言\",\"text\":\"烈焰之心 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"先锋\",\"type\":\"预言\",\"text\":\"先锋 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"看守者之看守\",\"type\":\"预言\",\"text\":\"看守者之看守 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"鼠疫\",\"type\":\"预言\",\"text\":\"鼠疫 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"传承\",\"type\":\"预言\",\"text\":\"传承 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"卡兰德工艺\",\"type\":\"预言\",\"text\":\"卡兰德工艺 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"恐怖的邪喙鸟\",\"type\":\"预言\",\"text\":\"恐怖的邪喙鸟 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"命运连结\",\"type\":\"预言\",\"text\":\"命运连结 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"富豪之死\",\"type\":\"预言\",\"text\":\"富豪之死 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"神秘礼物\",\"type\":\"预言\",\"text\":\"神秘礼物 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"伊拉斯玛斯的赠礼\",\"type\":\"预言\",\"text\":\"伊拉斯玛斯的赠礼 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"死灵兄弟\",\"type\":\"预言\",\"text\":\"死灵兄弟 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"隐藏危机\",\"type\":\"预言\",\"text\":\"隐藏危机 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"准备伏击\",\"type\":\"预言\",\"text\":\"准备伏击 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"烈日惩罚\",\"type\":\"预言\",\"text\":\"烈日惩罚 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"盗贼的野望 I\",\"type\":\"预言\",\"text\":\"盗贼的野望 I 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"盗贼的野望 II\",\"type\":\"预言\",\"text\":\"盗贼的野望 II 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"盗贼的野望 III\",\"type\":\"预言\",\"text\":\"盗贼的野望 III 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"钢铁之舞\",\"type\":\"预言\",\"text\":\"钢铁之舞 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"野兽之王 I\",\"type\":\"预言\",\"text\":\"野兽之王 I 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"野兽之王 II\",\"type\":\"预言\",\"text\":\"野兽之王 II 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"野兽之王 III\",\"type\":\"预言\",\"text\":\"野兽之王 III 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"野兽之王 IV\",\"type\":\"预言\",\"text\":\"野兽之王 IV 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"野兽之王 V\",\"type\":\"预言\",\"text\":\"野兽之王 V 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"盲信\",\"type\":\"预言\",\"text\":\"盲信 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"北方的精神支柱\",\"type\":\"预言\",\"text\":\"北方的精神支柱 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"北方的伟大领袖\",\"type\":\"预言\",\"text\":\"北方的伟大领袖 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"疫病大嘴兽 I\",\"type\":\"预言\",\"text\":\"疫病大嘴兽 I 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"疫病大嘴兽 II\",\"type\":\"预言\",\"text\":\"疫病大嘴兽 II 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"疫病大嘴兽 III\",\"type\":\"预言\",\"text\":\"疫病大嘴兽 III 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"疫病大嘴兽 IV\",\"type\":\"预言\",\"text\":\"疫病大嘴兽 IV 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"疫病大嘴兽 V\",\"type\":\"预言\",\"text\":\"疫病大嘴兽 V 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"仓管\",\"type\":\"预言\",\"text\":\"仓管 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"风暴之尖\",\"type\":\"预言\",\"text\":\"风暴之尖 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"无息女皇 I\",\"type\":\"预言\",\"text\":\"无息女皇 I 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"无息女皇 II\",\"type\":\"预言\",\"text\":\"无息女皇 II 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"无息女皇 III\",\"type\":\"预言\",\"text\":\"无息女皇 III 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"无息女皇 IV\",\"type\":\"预言\",\"text\":\"无息女皇 IV 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"无息女皇 V\",\"type\":\"预言\",\"text\":\"无息女皇 V 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"好战份子 I\",\"type\":\"预言\",\"text\":\"好战份子 I 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"好战份子 II\",\"type\":\"预言\",\"text\":\"好战份子 II 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"好战份子 III\",\"type\":\"预言\",\"text\":\"好战份子 III 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"好战份子 IV\",\"type\":\"预言\",\"text\":\"好战份子 IV 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"遗失的地图\",\"type\":\"预言\",\"text\":\"遗失的地图 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"时空裂隙\",\"type\":\"预言\",\"text\":\"时空裂隙 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"单一罪魂\",\"type\":\"预言\",\"text\":\"单一罪魂 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"风之速\",\"type\":\"预言\",\"text\":\"风之速 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"抗性转变\",\"type\":\"预言\",\"text\":\"抗性转变 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"窒息卷须\",\"type\":\"预言\",\"text\":\"窒息卷须 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"生命转化\",\"type\":\"预言\",\"text\":\"生命转化 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"点金\",\"type\":\"预言\",\"text\":\"点金 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"死守这座桥\",\"type\":\"预言\",\"text\":\"死守这座桥 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"尖刺耳语 I\",\"type\":\"预言\",\"text\":\"尖刺耳语 I 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"尖刺耳语 II\",\"type\":\"预言\",\"text\":\"尖刺耳语 II 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"尖刺耳语 III\",\"type\":\"预言\",\"text\":\"尖刺耳语 III 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"尖刺耳语 IV\",\"type\":\"预言\",\"text\":\"尖刺耳语 IV 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"尖刺耳语 V\",\"type\":\"预言\",\"text\":\"尖刺耳语 V 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"不死崛起\",\"type\":\"预言\",\"text\":\"不死崛起 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"火焰化身\",\"type\":\"预言\",\"text\":\"火焰化身 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"不自然能量\",\"type\":\"预言\",\"text\":\"不自然能量 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"腐化之握\",\"type\":\"预言\",\"text\":\"腐化之握 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"坚毅\",\"type\":\"预言\",\"text\":\"坚毅 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"美丽的指引\",\"type\":\"预言\",\"text\":\"美丽的指引 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"利刃\",\"type\":\"预言\",\"text\":\"利刃 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"污染源\",\"type\":\"预言\",\"text\":\"污染源 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"被遗忘的士兵\",\"type\":\"预言\",\"text\":\"被遗忘的士兵 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"暗语者的尖哮\",\"type\":\"预言\",\"text\":\"暗语者的尖哮 预言\",\"flags\":{\"prophecy\":true}},{\"name\":\"贪婪者的讽刺\",\"type\":\"预言\",\"text\":\"贪婪者的讽刺 预言\",\"flags\":{\"prophecy\":true}}]";
        } else if (itemTag.equals("传奇地图")) {
            return "[{\"name\":\"轮回的梦魇\",\"type\":\"密草神龛\",\"disc\":\"warfortheatlas\",\"text\":\"轮回的梦魇 密草神龛\",\"flags\":{\"unique\":true}},{\"name\":\"改变的遥远回忆\",\"type\":\"围城\",\"disc\":\"warfortheatlas\",\"text\":\"改变的遥远回忆 围城\",\"flags\":{\"unique\":true}},{\"name\":\"增强的遥远回忆\",\"type\":\"失序教院\",\"disc\":\"warfortheatlas\",\"text\":\"增强的遥远回忆 失序教院\",\"flags\":{\"unique\":true}},{\"name\":\"塞尔.布雷德狼穴\",\"type\":\"地底之河\",\"disc\":\"warfortheatlas\",\"text\":\"塞尔.布雷德狼穴 地底之河\",\"flags\":{\"unique\":true}},{\"name\":\"脑层\",\"type\":\"古藏密室\",\"disc\":\"warfortheatlas\",\"text\":\"脑层 古藏密室\",\"flags\":{\"unique\":true}},{\"name\":\"亡者之财\",\"type\":\"魔影墓场\",\"disc\":\"warfortheatlas\",\"text\":\"亡者之财 魔影墓场\",\"flags\":{\"unique\":true}},{\"name\":\"多利亚尼的迷城\",\"type\":\"冥神之域\",\"disc\":\"warfortheatlas\",\"text\":\"多利亚尼的迷城 冥神之域\",\"flags\":{\"unique\":true}},{\"name\":\"元帅殿堂\",\"type\":\"月色回廊\",\"disc\":\"warfortheatlas\",\"text\":\"元帅殿堂 月色回廊\",\"flags\":{\"unique\":true}},{\"name\":\"魔诅之域\",\"type\":\"晨曦墓地\",\"disc\":\"warfortheatlas\",\"text\":\"魔诅之域 晨曦墓地\",\"flags\":{\"unique\":true}},{\"name\":\"灌注的降临之地\",\"type\":\"先驱者地图\",\"disc\":\"warfortheatlas\",\"text\":\"灌注的降临之地 先驱者地图\",\"flags\":{\"unique\":true}},{\"name\":\"混沌之渊\",\"type\":\"滨海山丘\",\"disc\":\"warfortheatlas\",\"text\":\"混沌之渊 滨海山丘\",\"flags\":{\"unique\":true}},{\"name\":\"贼窝\",\"type\":\"暮光海滩\",\"disc\":\"warfortheatlas\",\"text\":\"贼窝 暮光海滩\",\"flags\":{\"unique\":true}},{\"name\":\"欧霸的咒怨宝库\",\"type\":\"远古街区\",\"disc\":\"warfortheatlas\",\"text\":\"欧霸的咒怨宝库 远古街区\",\"flags\":{\"unique\":true}},{\"name\":\"欧霸的咒怨宝库\",\"type\":\"滨海幽穴\",\"disc\":\"warfortheatlas\",\"text\":\"欧霸的咒怨宝库 滨海幽穴\",\"flags\":{\"unique\":true}},{\"name\":\"远古之印\",\"type\":\"古石陵墓\",\"disc\":\"warfortheatlas\",\"text\":\"远古之印 古石陵墓\",\"flags\":{\"unique\":true}},{\"name\":\"阿尔伦神柱\",\"type\":\"暮色沙丘\",\"disc\":\"warfortheatlas\",\"text\":\"阿尔伦神柱 暮色沙丘\",\"flags\":{\"unique\":true}},{\"name\":\"沉沦之间\",\"type\":\"夺魂之殿\",\"disc\":\"warfortheatlas\",\"text\":\"沉沦之间 夺魂之殿\",\"flags\":{\"unique\":true}},{\"name\":\"脑层【仿品】\",\"type\":\"古藏密室\",\"disc\":\"warfortheatlas\",\"text\":\"脑层【仿品】 古藏密室\",\"flags\":{\"unique\":true}},{\"name\":\"阿尔伦神柱【仿品】\",\"type\":\"暮色沙丘\",\"disc\":\"warfortheatlas\",\"text\":\"阿尔伦神柱【仿品】 暮色沙丘\",\"flags\":{\"unique\":true}},{\"name\":\"沉沦之间【仿品】\",\"type\":\"夺魂之殿\",\"disc\":\"warfortheatlas\",\"text\":\"沉沦之间【仿品】 夺魂之殿\",\"flags\":{\"unique\":true}},{\"name\":\"重著的遥远回忆\",\"type\":\"神圣大教堂\",\"disc\":\"warfortheatlas\",\"text\":\"重著的遥远回忆 神圣大教堂\",\"flags\":{\"unique\":true}},{\"name\":\"降临之地\",\"type\":\"先驱者地图\",\"disc\":\"warfortheatlas\",\"text\":\"降临之地 先驱者地图\",\"flags\":{\"unique\":true}},{\"name\":\"懦者的试验\",\"type\":\"咒怨陵墓\",\"disc\":\"warfortheatlas\",\"text\":\"懦者的试验 咒怨陵墓\",\"flags\":{\"unique\":true}},{\"name\":\"普兰德斯庄园\",\"type\":\"古堡\",\"disc\":\"warfortheatlas\",\"text\":\"普兰德斯庄园 古堡\",\"flags\":{\"unique\":true}},{\"name\":\"幽秘博物馆\",\"type\":\"古博物馆\",\"disc\":\"warfortheatlas\",\"text\":\"幽秘博物馆 古博物馆\",\"flags\":{\"unique\":true}},{\"name\":\"暮光古庙\",\"type\":\"月影神殿\",\"disc\":\"warfortheatlas\",\"text\":\"暮光古庙 月影神殿\",\"flags\":{\"unique\":true}},{\"name\":\"维克塔广场\",\"type\":\"奇术之庭\",\"disc\":\"warfortheatlas\",\"text\":\"维克塔广场 奇术之庭\",\"flags\":{\"unique\":true}},{\"name\":\"扭曲的遥远回忆\",\"type\":\"寂夜林苑\",\"disc\":\"warfortheatlas\",\"text\":\"扭曲的遥远回忆 寂夜林苑\",\"flags\":{\"unique\":true}},{\"name\":\"阿兹里的秘宝库\",\"type\":\"瓦尔金字塔\",\"disc\":\"warfortheatlas\",\"text\":\"阿兹里的秘宝库 瓦尔金字塔\",\"flags\":{\"unique\":true}},{\"name\":\"禁闭祭坛\",\"type\":\"致命岩滩\",\"disc\":\"warfortheatlas\",\"text\":\"禁闭祭坛 致命岩滩\",\"flags\":{\"unique\":true}}]";
        } else if (itemTag.equals("珠宝")) {
            return "[{\"name\":\"真知灼见\",\"type\":\"钴蓝珠宝\",\"text\":\"真知灼见 钴蓝珠宝\",\"flags\":{\"unique\":true}},{\"name\":\"远古基石\",\"type\":\"赤红珠宝\",\"text\":\"远古基石 赤红珠宝\",\"flags\":{\"unique\":true}},{\"name\":\"巅峰状态\",\"type\":\"钴蓝珠宝\",\"text\":\"巅峰状态 钴蓝珠宝\",\"flags\":{\"unique\":true}},{\"name\":\"映像\",\"type\":\"翠绿珠宝\",\"text\":\"映像 翠绿珠宝\",\"flags\":{\"unique\":true}},{\"name\":\"暗影之影\",\"type\":\"钴蓝珠宝\",\"text\":\"暗影之影 钴蓝珠宝\",\"flags\":{\"unique\":true}},{\"name\":\"阿兹里之权\",\"type\":\"赤红珠宝\",\"text\":\"阿兹里之权 赤红珠宝\",\"flags\":{\"unique\":true}},{\"name\":\"血牺\",\"type\":\"赤红珠宝\",\"text\":\"血牺 赤红珠宝\",\"flags\":{\"unique\":true}},{\"name\":\"筋骨强化\",\"type\":\"赤红珠宝\",\"text\":\"筋骨强化 赤红珠宝\",\"flags\":{\"unique\":true}},{\"name\":\"易碎屏障\",\"type\":\"钴蓝珠宝\",\"text\":\"易碎屏障 钴蓝珠宝\",\"flags\":{\"unique\":true}},{\"name\":\"残酷的约束\",\"type\":\"永恒珠宝\",\"text\":\"残酷的约束 永恒珠宝\",\"flags\":{\"unique\":true}},{\"name\":\"蛮力冲撞\",\"type\":\"钴蓝珠宝\",\"text\":\"蛮力冲撞 钴蓝珠宝\",\"flags\":{\"unique\":true}},{\"name\":\"坚守军团\",\"type\":\"钴蓝珠宝\",\"text\":\"坚守军团 钴蓝珠宝\",\"flags\":{\"unique\":true}},{\"name\":\"灾祸异象\",\"type\":\"小型星团珠宝\",\"text\":\"灾祸异象 小型星团珠宝\",\"flags\":{\"unique\":true}},{\"name\":\"审慎计划\",\"type\":\"翠绿珠宝\",\"text\":\"审慎计划 翠绿珠宝\",\"flags\":{\"unique\":true}},{\"name\":\"廉价建设\",\"type\":\"翠绿珠宝\",\"text\":\"廉价建设 翠绿珠宝\",\"flags\":{\"unique\":true}},{\"name\":\"腐化寒息\",\"type\":\"翠绿珠宝\",\"text\":\"腐化寒息 翠绿珠宝\",\"flags\":{\"unique\":true}},{\"name\":\"理智\",\"type\":\"钴蓝珠宝\",\"text\":\"理智 钴蓝珠宝\",\"flags\":{\"unique\":true}},{\"name\":\"榴星\",\"type\":\"赤红珠宝\",\"text\":\"榴星 赤红珠宝\",\"flags\":{\"unique\":true}},{\"name\":\"冷钢\",\"type\":\"翠绿珠宝\",\"text\":\"冷钢 翠绿珠宝\",\"flags\":{\"unique\":true}},{\"name\":\"附带伤害\",\"type\":\"翠绿珠宝\",\"text\":\"附带伤害 翠绿珠宝\",\"flags\":{\"unique\":true}},{\"name\":\"战斗专注\",\"type\":\"翠绿珠宝\",\"text\":\"战斗专注 翠绿珠宝\",\"flags\":{\"unique\":true}},{\"name\":\"战斗专注\",\"type\":\"钴蓝珠宝\",\"text\":\"战斗专注 钴蓝珠宝\",\"flags\":{\"unique\":true}},{\"name\":\"战斗专注\",\"type\":\"赤红珠宝\",\"text\":\"战斗专注 赤红珠宝\",\"flags\":{\"unique\":true}},{\"name\":\"燃尽\",\"type\":\"赤红珠宝\",\"text\":\"燃尽 赤红珠宝\",\"flags\":{\"unique\":true}},{\"name\":\"征服者的迅捷\",\"type\":\"赤红珠宝\",\"text\":\"征服者的迅捷 赤红珠宝\",\"flags\":{\"unique\":true}},{\"name\":\"征服者的长生\",\"type\":\"翠绿珠宝\",\"text\":\"征服者的长生 翠绿珠宝\",\"flags\":{\"unique\":true}},{\"name\":\"征服者的力量\",\"type\":\"钴蓝珠宝\",\"text\":\"征服者的力量 钴蓝珠宝\",\"flags\":{\"unique\":true}},{\"name\":\"腐化能量\",\"type\":\"钴蓝珠宝\",\"text\":\"腐化能量 钴蓝珠宝\",\"flags\":{\"unique\":true}},{\"name\":\"死亡清算\",\"type\":\"钴蓝珠宝\",\"text\":\"死亡清算 钴蓝珠宝\",\"flags\":{\"unique\":true}},{\"name\":\"分化瓦解\",\"type\":\"翠绿珠宝\",\"text\":\"分化瓦解 翠绿珠宝\",\"flags\":{\"unique\":true}},{\"name\":\"充分训练\",\"type\":\"赤红珠宝\",\"text\":\"充分训练 赤红珠宝\",\"flags\":{\"unique\":true}},{\"name\":\"异能知识\",\"type\":\"钴蓝珠宝\",\"text\":\"异能知识 钴蓝珠宝\",\"flags\":{\"unique\":true}},{\"name\":\"优雅的狂妄\",\"type\":\"永恒珠宝\",\"text\":\"优雅的狂妄 永恒珠宝\",\"flags\":{\"unique\":true}},{\"name\":\"帝王的诡计\",\"type\":\"翠绿珠宝\",\"text\":\"帝王的诡计 翠绿珠宝\",\"flags\":{\"unique\":true}},{\"name\":\"帝王的纯熟\",\"type\":\"三相珠宝\",\"text\":\"帝王的纯熟 三相珠宝\",\"flags\":{\"unique\":true}},{\"name\":\"帝王的霸权\",\"type\":\"赤红珠宝\",\"text\":\"帝王的霸权 赤红珠宝\",\"flags\":{\"unique\":true}},{\"name\":\"帝王的智慧\",\"type\":\"钴蓝珠宝\",\"text\":\"帝王的智慧 钴蓝珠宝\",\"flags\":{\"unique\":true}},{\"name\":\"无尽之苦\",\"type\":\"钴蓝珠宝\",\"text\":\"无尽之苦 钴蓝珠宝\",\"flags\":{\"unique\":true}},{\"name\":\"能量坚甲\",\"type\":\"赤红珠宝\",\"text\":\"能量坚甲 赤红珠宝\",\"flags\":{\"unique\":true}},{\"name\":\"潜能防护\",\"type\":\"钴蓝珠宝\",\"text\":\"潜能防护 钴蓝珠宝\",\"flags\":{\"unique\":true}},{\"name\":\"丰富心灵\",\"type\":\"钴蓝珠宝\",\"text\":\"丰富心灵 钴蓝珠宝\",\"flags\":{\"unique\":true}},{\"name\":\"狂热之心\",\"type\":\"钴蓝珠宝\",\"text\":\"狂热之心 钴蓝珠宝\",\"flags\":{\"unique\":true}},{\"name\":\"为生存而战\",\"type\":\"翠绿珠宝\",\"text\":\"为生存而战 翠绿珠宝\",\"flags\":{\"unique\":true}},{\"name\":\"炎域\",\"type\":\"赤红珠宝\",\"text\":\"炎域 赤红珠宝\",\"flags\":{\"unique\":true}},{\"name\":\"初雪\",\"type\":\"钴蓝珠宝\",\"text\":\"初雪 钴蓝珠宝\",\"flags\":{\"unique\":true}},{\"name\":\"流畅行动\",\"type\":\"翠绿珠宝\",\"text\":\"流畅行动 翠绿珠宝\",\"flags\":{\"unique\":true}},{\"name\":\"要塞誓约\",\"type\":\"钴蓝珠宝\",\"text\":\"要塞誓约 钴蓝珠宝\",\"flags\":{\"unique\":true}},{\"name\":\"脆弱的繁华\",\"type\":\"赤红珠宝\",\"text\":\"脆弱的繁华 赤红珠宝\",\"flags\":{\"unique\":true}},{\"name\":\"脆弱\",\"type\":\"赤红珠宝\",\"text\":\"脆弱 赤红珠宝\",\"flags\":{\"unique\":true}},{\"name\":\"尘土归来\",\"type\":\"钴蓝珠宝\",\"text\":\"尘土归来 钴蓝珠宝\",\"flags\":{\"unique\":true}},{\"name\":\"冰冻痕迹\",\"type\":\"钴蓝珠宝\",\"text\":\"冰冻痕迹 钴蓝珠宝\",\"flags\":{\"unique\":true}},{\"name\":\"光彩夺目\",\"type\":\"永恒珠宝\",\"text\":\"光彩夺目 永恒珠宝\",\"flags\":{\"unique\":true}},{\"name\":\"聚光之石\",\"type\":\"翠绿珠宝\",\"text\":\"聚光之石 翠绿珠宝\",\"flags\":{\"unique\":true}},{\"name\":\"聚光之石\",\"type\":\"钴蓝珠宝\",\"text\":\"聚光之石 钴蓝珠宝\",\"flags\":{\"unique\":true}},{\"name\":\"聚光之石\",\"type\":\"赤红珠宝\",\"text\":\"聚光之石 赤红珠宝\",\"flags\":{\"unique\":true}},{\"name\":\"茁壮苦痛\",\"type\":\"翠绿珠宝\",\"text\":\"茁壮苦痛 翠绿珠宝\",\"flags\":{\"unique\":true}},{\"name\":\"发闸机关\",\"type\":\"翠绿珠宝\",\"text\":\"发闸机关 翠绿珠宝\",\"flags\":{\"unique\":true}},{\"name\":\"危险研究\",\"type\":\"钴蓝珠宝\",\"text\":\"危险研究 钴蓝珠宝\",\"flags\":{\"unique\":true}},{\"name\":\"灵体转换\",\"type\":\"钴蓝珠宝\",\"text\":\"灵体转换 钴蓝珠宝\",\"flags\":{\"unique\":true}},{\"name\":\"深藏的潜能\",\"type\":\"翠绿珠宝\",\"text\":\"深藏的潜能 翠绿珠宝\",\"flags\":{\"unique\":true}},{\"name\":\"鲁莽\",\"type\":\"翠绿珠宝\",\"text\":\"鲁莽 翠绿珠宝\",\"flags\":{\"unique\":true}},{\"name\":\"饥馑深渊\",\"type\":\"翠绿珠宝\",\"text\":\"饥馑深渊 翠绿珠宝\",\"flags\":{\"unique\":true}},{\"name\":\"惯性\",\"type\":\"赤红珠宝\",\"text\":\"惯性 赤红珠宝\",\"flags\":{\"unique\":true}},{\"name\":\"不可避免\",\"type\":\"钴蓝珠宝\",\"text\":\"不可避免 钴蓝珠宝\",\"flags\":{\"unique\":true}},{\"name\":\"求知的热情\",\"type\":\"赤红珠宝\",\"text\":\"求知的热情 赤红珠宝\",\"flags\":{\"unique\":true}},{\"name\":\"直觉之跃\",\"type\":\"翠绿珠宝\",\"text\":\"直觉之跃 翠绿珠宝\",\"flags\":{\"unique\":true}},{\"name\":\"伊泽洛之乱\",\"type\":\"赤红珠宝\",\"text\":\"伊泽洛之乱 赤红珠宝\",\"flags\":{\"unique\":true}},{\"name\":\"奇塔弗的指教\",\"type\":\"小型星团珠宝\",\"text\":\"奇塔弗的指教 小型星团珠宝\",\"flags\":{\"unique\":true}},{\"name\":\"致命的骄傲\",\"type\":\"永恒珠宝\",\"text\":\"致命的骄傲 永恒珠宝\",\"flags\":{\"unique\":true}},{\"name\":\"狮眼的陨落\",\"type\":\"翠绿珠宝\",\"text\":\"狮眼的陨落 翠绿珠宝\",\"flags\":{\"unique\":true}},{\"name\":\"钢铁之主\",\"type\":\"翠绿珠宝\",\"text\":\"钢铁之主 翠绿珠宝\",\"flags\":{\"unique\":true}},{\"name\":\"不善意图\",\"type\":\"钴蓝珠宝\",\"text\":\"不善意图 钴蓝珠宝\",\"flags\":{\"unique\":true}},{\"name\":\"烈炎之兆\",\"type\":\"赤红珠宝\",\"text\":\"烈炎之兆 赤红珠宝\",\"flags\":{\"unique\":true}},{\"name\":\"武艺之相\",\"type\":\"赤红珠宝\",\"text\":\"武艺之相 赤红珠宝\",\"flags\":{\"unique\":true}},{\"name\":\"妄想症\",\"type\":\"中型星团珠宝\",\"text\":\"妄想症 中型星团珠宝\",\"flags\":{\"unique\":true}},{\"name\":\"蛮力与影响\",\"type\":\"翠绿珠宝\",\"text\":\"蛮力与影响 翠绿珠宝\",\"flags\":{\"unique\":true}},{\"name\":\"卓绝之力\",\"type\":\"赤红珠宝\",\"text\":\"卓绝之力 赤红珠宝\",\"flags\":{\"unique\":true}},{\"name\":\"温柔之力\",\"type\":\"赤红珠宝\",\"text\":\"温柔之力 赤红珠宝\",\"flags\":{\"unique\":true}},{\"name\":\"好战的信仰\",\"type\":\"永恒珠宝\",\"text\":\"好战的信仰 永恒珠宝\",\"flags\":{\"unique\":true}},{\"name\":\"堕落异体\",\"type\":\"钴蓝珠宝\",\"text\":\"堕落异体 钴蓝珠宝\",\"flags\":{\"unique\":true}},{\"name\":\"低谷状态\",\"type\":\"钴蓝珠宝\",\"text\":\"低谷状态 钴蓝珠宝\",\"flags\":{\"unique\":true}},{\"name\":\"自然之喜\",\"type\":\"小型星团珠宝\",\"text\":\"自然之喜 小型星团珠宝\",\"flags\":{\"unique\":true}},{\"name\":\"风之启示\",\"type\":\"翠绿珠宝\",\"text\":\"风之启示 翠绿珠宝\",\"flags\":{\"unique\":true}},{\"name\":\"徒手空拳\",\"type\":\"小型星团珠宝\",\"text\":\"徒手空拳 小型星团珠宝\",\"flags\":{\"unique\":true}},{\"name\":\"压倒性\",\"type\":\"赤红珠宝\",\"text\":\"压倒性 赤红珠宝\",\"flags\":{\"unique\":true}},{\"name\":\"和平主义\",\"type\":\"翠绿珠宝\",\"text\":\"和平主义 翠绿珠宝\",\"flags\":{\"unique\":true}},{\"name\":\"黯黑之调\",\"type\":\"翠绿珠宝\",\"text\":\"黯黑之调 翠绿珠宝\",\"flags\":{\"unique\":true}},{\"name\":\"盗猎者的准心\",\"type\":\"翠绿珠宝\",\"text\":\"盗猎者的准心 翠绿珠宝\",\"flags\":{\"unique\":true}},{\"name\":\"无力\",\"type\":\"钴蓝珠宝\",\"text\":\"无力 钴蓝珠宝\",\"flags\":{\"unique\":true}},{\"name\":\"起源卓越\",\"type\":\"翠绿珠宝\",\"text\":\"起源卓越 翠绿珠宝\",\"flags\":{\"unique\":true}},{\"name\":\"起源和谐\",\"type\":\"钴蓝珠宝\",\"text\":\"起源和谐 钴蓝珠宝\",\"flags\":{\"unique\":true}},{\"name\":\"起源力量\",\"type\":\"赤红珠宝\",\"text\":\"起源力量 赤红珠宝\",\"flags\":{\"unique\":true}},{\"name\":\"迅击者\",\"type\":\"翠绿珠宝\",\"text\":\"迅击者 翠绿珠宝\",\"flags\":{\"unique\":true}},{\"name\":\"纯才\",\"type\":\"翠绿珠宝\",\"text\":\"纯才 翠绿珠宝\",\"flags\":{\"unique\":true}},{\"name\":\"加速誓约\",\"type\":\"翠绿珠宝\",\"text\":\"加速誓约 翠绿珠宝\",\"flags\":{\"unique\":true}},{\"name\":\"碎镞雨\",\"type\":\"赤红珠宝\",\"text\":\"碎镞雨 赤红珠宝\",\"flags\":{\"unique\":true}},{\"name\":\"急速扩张\",\"type\":\"赤红珠宝\",\"text\":\"急速扩张 赤红珠宝\",\"flags\":{\"unique\":true}},{\"name\":\"鲁莽防御\",\"type\":\"钴蓝珠宝\",\"text\":\"鲁莽防御 钴蓝珠宝\",\"flags\":{\"unique\":true}},{\"name\":\"血牺【仿品】\",\"type\":\"赤红珠宝\",\"text\":\"血牺【仿品】 赤红珠宝\",\"flags\":{\"unique\":true}},{\"name\":\"廉价建设【仿品】\",\"type\":\"翠绿珠宝\",\"text\":\"廉价建设【仿品】 翠绿珠宝\",\"flags\":{\"unique\":true}},{\"name\":\"征服者的迅捷【仿品】\",\"type\":\"赤红珠宝\",\"text\":\"征服者的迅捷【仿品】 赤红珠宝\",\"flags\":{\"unique\":true}},{\"name\":\"脆弱的繁华【仿品】\",\"type\":\"赤红珠宝\",\"text\":\"脆弱的繁华【仿品】 赤红珠宝\",\"flags\":{\"unique\":true}},{\"name\":\"鲁莽【仿品】\",\"type\":\"翠绿珠宝\",\"text\":\"鲁莽【仿品】 翠绿珠宝\",\"flags\":{\"unique\":true}},{\"name\":\"起源力量【仿品】\",\"type\":\"赤红珠宝\",\"text\":\"起源力量【仿品】 赤红珠宝\",\"flags\":{\"unique\":true}},{\"name\":\"纯才【仿品】\",\"type\":\"翠绿珠宝\",\"text\":\"纯才【仿品】 翠绿珠宝\",\"flags\":{\"unique\":true}},{\"name\":\"鲁莽防御【仿品】\",\"type\":\"钴蓝珠宝\",\"text\":\"鲁莽防御【仿品】 钴蓝珠宝\",\"flags\":{\"unique\":true}},{\"name\":\"不稳定承载【仿品】\",\"type\":\"钴蓝珠宝\",\"text\":\"不稳定承载【仿品】 钴蓝珠宝\",\"flags\":{\"unique\":true}},{\"name\":\"利刃戒指\",\"type\":\"翠绿珠宝\",\"text\":\"利刃戒指 翠绿珠宝\",\"flags\":{\"unique\":true}},{\"name\":\"滚动烈焰\",\"type\":\"钴蓝珠宝\",\"text\":\"滚动烈焰 钴蓝珠宝\",\"flags\":{\"unique\":true}},{\"name\":\"原生祭仪\",\"type\":\"翠绿珠宝\",\"text\":\"原生祭仪 翠绿珠宝\",\"flags\":{\"unique\":true}},{\"name\":\"残虐之欢愉\",\"type\":\"翠绿珠宝\",\"text\":\"残虐之欢愉 翠绿珠宝\",\"flags\":{\"unique\":true}},{\"name\":\"破碎之炼\",\"type\":\"赤红珠宝\",\"text\":\"破碎之炼 赤红珠宝\",\"flags\":{\"unique\":true}},{\"name\":\"灵魂之芯\",\"type\":\"钴蓝珠宝\",\"text\":\"灵魂之芯 钴蓝珠宝\",\"flags\":{\"unique\":true}},{\"name\":\"石塔\",\"type\":\"赤红珠宝\",\"text\":\"石塔 赤红珠宝\",\"flags\":{\"unique\":true}},{\"name\":\"先祖回音\",\"type\":\"钴蓝珠宝\",\"text\":\"先祖回音 钴蓝珠宝\",\"flags\":{\"unique\":true}},{\"name\":\"精神守护\",\"type\":\"翠绿珠宝\",\"text\":\"精神守护 翠绿珠宝\",\"flags\":{\"unique\":true}},{\"name\":\"人格分裂\",\"type\":\"赤红珠宝\",\"text\":\"人格分裂 赤红珠宝\",\"flags\":{\"unique\":true}},{\"name\":\"腐烂扩散\",\"type\":\"钴蓝珠宝\",\"text\":\"腐烂扩散 钴蓝珠宝\",\"flags\":{\"unique\":true}},{\"name\":\"静电之源\",\"type\":\"翠绿珠宝\",\"text\":\"静电之源 翠绿珠宝\",\"flags\":{\"unique\":true}},{\"name\":\"坚毅心灵\",\"type\":\"翠绿珠宝\",\"text\":\"坚毅心灵 翠绿珠宝\",\"flags\":{\"unique\":true}},{\"name\":\"突然点火\",\"type\":\"翠绿珠宝\",\"text\":\"突然点火 翠绿珠宝\",\"flags\":{\"unique\":true}},{\"name\":\"生存本能\",\"type\":\"翠绿珠宝\",\"text\":\"生存本能 翠绿珠宝\",\"flags\":{\"unique\":true}},{\"name\":\"生存秘技\",\"type\":\"钴蓝珠宝\",\"text\":\"生存秘技 钴蓝珠宝\",\"flags\":{\"unique\":true}},{\"name\":\"生存技巧\",\"type\":\"赤红珠宝\",\"text\":\"生存技巧 赤红珠宝\",\"flags\":{\"unique\":true}},{\"name\":\"淬火之躯\",\"type\":\"赤红珠宝\",\"text\":\"淬火之躯 赤红珠宝\",\"flags\":{\"unique\":true}},{\"name\":\"淬火之心\",\"type\":\"钴蓝珠宝\",\"text\":\"淬火之心 钴蓝珠宝\",\"flags\":{\"unique\":true}},{\"name\":\"淬火之魂\",\"type\":\"翠绿珠宝\",\"text\":\"淬火之魂 翠绿珠宝\",\"flags\":{\"unique\":true}},{\"name\":\"聚魂石\",\"type\":\"三相珠宝\",\"text\":\"聚魂石 三相珠宝\",\"flags\":{\"unique\":true}},{\"name\":\"碧影梦境\",\"type\":\"钴蓝珠宝\",\"text\":\"碧影梦境 钴蓝珠宝\",\"flags\":{\"unique\":true}},{\"name\":\"碧影梦魇\",\"type\":\"钴蓝珠宝\",\"text\":\"碧影梦魇 钴蓝珠宝\",\"flags\":{\"unique\":true}},{\"name\":\"前线\",\"type\":\"小型星团珠宝\",\"text\":\"前线 小型星团珠宝\",\"flags\":{\"unique\":true}},{\"name\":\"黄金法则\",\"type\":\"翠绿珠宝\",\"text\":\"黄金法则 翠绿珠宝\",\"flags\":{\"unique\":true}},{\"name\":\"青影梦境\",\"type\":\"翠绿珠宝\",\"text\":\"青影梦境 翠绿珠宝\",\"flags\":{\"unique\":true}},{\"name\":\"青影梦魇\",\"type\":\"翠绿珠宝\",\"text\":\"青影梦魇 翠绿珠宝\",\"flags\":{\"unique\":true}},{\"name\":\"审讯\",\"type\":\"小型星团珠宝\",\"text\":\"审讯 小型星团珠宝\",\"flags\":{\"unique\":true}},{\"name\":\"凛冽寒冬\",\"type\":\"钴蓝珠宝\",\"text\":\"凛冽寒冬 钴蓝珠宝\",\"flags\":{\"unique\":true}},{\"name\":\"赤影梦境\",\"type\":\"赤红珠宝\",\"text\":\"赤影梦境 赤红珠宝\",\"flags\":{\"unique\":true}},{\"name\":\"赤影梦魇\",\"type\":\"赤红珠宝\",\"text\":\"赤影梦魇 赤红珠宝\",\"flags\":{\"unique\":true}},{\"name\":\"围攻\",\"type\":\"小型星团珠宝\",\"text\":\"围攻 小型星团珠宝\",\"flags\":{\"unique\":true}},{\"name\":\"监视守夜\",\"type\":\"赤红珠宝\",\"text\":\"监视守夜 赤红珠宝\",\"flags\":{\"unique\":true}},{\"name\":\"希望之线\",\"type\":\"赤红珠宝\",\"text\":\"希望之线 赤红珠宝\",\"flags\":{\"unique\":true}},{\"name\":\"尘埃落定\",\"type\":\"钴蓝珠宝\",\"text\":\"尘埃落定 钴蓝珠宝\",\"flags\":{\"unique\":true}},{\"name\":\"升华之躯\",\"type\":\"赤红珠宝\",\"text\":\"升华之躯 赤红珠宝\",\"flags\":{\"unique\":true}},{\"name\":\"升华之心\",\"type\":\"钴蓝珠宝\",\"text\":\"升华之心 钴蓝珠宝\",\"flags\":{\"unique\":true}},{\"name\":\"升华之魂\",\"type\":\"翠绿珠宝\",\"text\":\"升华之魂 翠绿珠宝\",\"flags\":{\"unique\":true}},{\"name\":\"无尽渴望\",\"type\":\"钴蓝珠宝\",\"text\":\"无尽渴望 钴蓝珠宝\",\"flags\":{\"unique\":true}},{\"name\":\"超自然本能\",\"type\":\"翠绿珠宝\",\"text\":\"超自然本能 翠绿珠宝\",\"flags\":{\"unique\":true}},{\"name\":\"不稳定承载\",\"type\":\"钴蓝珠宝\",\"text\":\"不稳定承载 钴蓝珠宝\",\"flags\":{\"unique\":true}},{\"name\":\"瓦尔之诉\",\"type\":\"钴蓝珠宝\",\"text\":\"瓦尔之诉 钴蓝珠宝\",\"flags\":{\"unique\":true}},{\"name\":\"暴力的死者\",\"type\":\"钴蓝珠宝\",\"text\":\"暴力的死者 钴蓝珠宝\",\"flags\":{\"unique\":true}},{\"name\":\"天神之音\",\"type\":\"大型星团珠宝\",\"text\":\"天神之音 大型星团珠宝\",\"flags\":{\"unique\":true}},{\"name\":\"怒火齐发\",\"type\":\"翠绿珠宝\",\"text\":\"怒火齐发 翠绿珠宝\",\"flags\":{\"unique\":true}},{\"name\":\"督军之力\",\"type\":\"赤红珠宝\",\"text\":\"督军之力 赤红珠宝\",\"flags\":{\"unique\":true}},{\"name\":\"守望之眼\",\"type\":\"三相珠宝\",\"text\":\"守望之眼 三相珠宝\",\"flags\":{\"unique\":true}},{\"name\":\"罪恶的重量\",\"type\":\"翠绿珠宝\",\"text\":\"罪恶的重量 翠绿珠宝\",\"flags\":{\"unique\":true}},{\"name\":\"帝国重量\",\"type\":\"赤红珠宝\",\"text\":\"帝国重量 赤红珠宝\",\"flags\":{\"unique\":true}},{\"name\":\"野火\",\"type\":\"赤红珠宝\",\"text\":\"野火 赤红珠宝\",\"flags\":{\"unique\":true}},{\"name\":\"冬葬\",\"type\":\"赤红珠宝\",\"text\":\"冬葬 赤红珠宝\",\"flags\":{\"unique\":true}},{\"name\":\"冬季赏金\",\"type\":\"钴蓝珠宝\",\"text\":\"冬季赏金 钴蓝珠宝\",\"flags\":{\"unique\":true}}]";
        } else if (itemTag.equals("命运卡")) {
            return "[{\"type\":\"弃财求生\",\"text\":\"弃财求生\"},{\"type\":\"墨水点滴\",\"text\":\"墨水点滴\"},{\"type\":\"熟悉的呼唤\",\"text\":\"熟悉的呼唤\"},{\"type\":\"阿凯的预言\",\"text\":\"阿凯的预言\"},{\"type\":\"诱人的奖赏\",\"text\":\"诱人的奖赏\"},{\"type\":\"黑暗中独行\",\"text\":\"黑暗中独行\"},{\"type\":\"母亲的礼物\",\"text\":\"母亲的礼物\"},{\"type\":\"混乱代价\",\"text\":\"混乱代价\"},{\"type\":\"风中之语\",\"text\":\"风中之语\"},{\"type\":\"瓦尔的傲慢\",\"text\":\"瓦尔的傲慢\"},{\"type\":\"暗影恩惠\",\"text\":\"暗影恩惠\"},{\"type\":\"阿兹里的武器库\",\"text\":\"阿兹里的武器库\"},{\"type\":\"胆识\",\"text\":\"胆识\"},{\"type\":\"阿祖兰的奖赏\",\"text\":\"阿祖兰的奖赏\"},{\"type\":\"前程的诱惑\",\"text\":\"前程的诱惑\"},{\"type\":\"来生之美\",\"text\":\"来生之美\"},{\"type\":\"三者之诞\",\"text\":\"三者之诞\"},{\"type\":\"神佑\",\"text\":\"神佑\"},{\"type\":\"盲途\",\"text\":\"盲途\"},{\"type\":\"司法的恩赐\",\"text\":\"司法的恩赐\"},{\"type\":\"先祖赐福\",\"text\":\"先祖赐福\"},{\"type\":\"无尽之域\",\"text\":\"无尽之域\"},{\"type\":\"弓匠的梦想\",\"text\":\"弓匠的梦想\"},{\"type\":\"兄弟的秘藏\",\"text\":\"兄弟的秘藏\"},{\"type\":\"埋葬的宝藏\",\"text\":\"埋葬的宝藏\"},{\"type\":\"燃烧之血\",\"text\":\"燃烧之血\"},{\"type\":\"远古召唤\",\"text\":\"远古召唤\"},{\"type\":\"卡梅莉亚的回报\",\"text\":\"卡梅莉亚的回报\"},{\"type\":\"制图者的青睐\",\"text\":\"制图者的青睐\"},{\"type\":\"浑沌性情\",\"text\":\"浑沌性情\"},{\"type\":\"猫咪议会\",\"text\":\"猫咪议会\"},{\"type\":\"饥渴之占\",\"text\":\"饥渴之占\"},{\"type\":\"恶言诅咒\",\"text\":\"恶言诅咒\"},{\"type\":\"黑暗之梦\",\"text\":\"黑暗之梦\"},{\"type\":\"黑暗的引诱\",\"text\":\"黑暗的引诱\"},{\"type\":\"死亡\",\"text\":\"死亡\"},{\"type\":\"致命谋划\",\"text\":\"致命谋划\"},{\"type\":\"半神的赌局\",\"text\":\"半神的赌局\"},{\"type\":\"被亵渎的美德\",\"text\":\"被亵渎的美德\"},{\"type\":\"殒落的命运\",\"text\":\"殒落的命运\"},{\"type\":\"达拉夫人的宝石\",\"text\":\"达拉夫人的宝石\"},{\"type\":\"神圣的正义\",\"text\":\"神圣的正义\"},{\"type\":\"德瑞竞之狂\",\"text\":\"德瑞竞之狂\"},{\"type\":\"消逝之怒\",\"text\":\"消逝之怒\"},{\"type\":\"大地吞食者\",\"text\":\"大地吞食者\"},{\"type\":\"爱的回音\",\"text\":\"爱的回音\"},{\"type\":\"纯净帝王\",\"text\":\"纯净帝王\"},{\"type\":\"帝运\",\"text\":\"帝运\"},{\"type\":\"以血镌刻\",\"text\":\"以血镌刻\"},{\"type\":\"禁忌之力\",\"text\":\"禁忌之力\"},{\"type\":\"友谊\",\"text\":\"友谊\"},{\"type\":\"宝石匠的允诺\",\"text\":\"宝石匠的允诺\"},{\"type\":\"安赛娜丝的馈赠\",\"text\":\"安赛娜丝的馈赠\"},{\"type\":\"宝石皇后的赠礼\",\"text\":\"宝石皇后的赠礼\"},{\"type\":\"希望微光\",\"text\":\"希望微光\"},{\"type\":\"亡灵智慧\",\"text\":\"亡灵智慧\"},{\"type\":\"魂之和谐\",\"text\":\"魂之和谐\"},{\"type\":\"她的面具\",\"text\":\"她的面具\"},{\"type\":\"阴阳眼\",\"text\":\"阴阳眼\"},{\"type\":\"希望\",\"text\":\"希望\"},{\"type\":\"明镜\",\"text\":\"明镜\"},{\"type\":\"狂妄\",\"text\":\"狂妄\"},{\"type\":\"谦逊\",\"text\":\"谦逊\"},{\"type\":\"猎者之愿\",\"text\":\"猎者之愿\"},{\"type\":\"猎人的奖赏\",\"text\":\"猎人的奖赏\"},{\"type\":\"不朽决心\",\"text\":\"不朽决心\"},{\"type\":\"帝国的遗产\",\"text\":\"帝国的遗产\"},{\"type\":\"惊喜盒\",\"text\":\"惊喜盒\"},{\"type\":\"兰塔朵迷惘之爱\",\"text\":\"兰塔朵迷惘之爱\"},{\"type\":\"最后希望\",\"text\":\"最后希望\"},{\"type\":\"听天由命\",\"text\":\"听天由命\"},{\"type\":\"光与真实\",\"text\":\"光与真实\"},{\"type\":\"稍纵即逝\",\"text\":\"稍纵即逝\"},{\"type\":\"失落帝国\",\"text\":\"失落帝国\"},{\"type\":\"忠诚\",\"text\":\"忠诚\"},{\"type\":\"好运连连\",\"text\":\"好运连连\"},{\"type\":\"命运垂青\",\"text\":\"命运垂青\"},{\"type\":\"莉莎之息\",\"text\":\"莉莎之息\"},{\"type\":\"狼之信物\",\"text\":\"狼之信物\"},{\"type\":\"无情军械\",\"text\":\"无情军械\"},{\"type\":\"正气\",\"text\":\"正气\"},{\"type\":\"露指手套\",\"text\":\"露指手套\"},{\"type\":\"黑白世界\",\"text\":\"黑白世界\"},{\"type\":\"永不知足\",\"text\":\"永不知足\"},{\"type\":\"诺克之冠\",\"text\":\"诺克之冠\"},{\"type\":\"无迹可寻\",\"text\":\"无迹可寻\"},{\"type\":\"安宁之时\",\"text\":\"安宁之时\"},{\"type\":\"至臻完美\",\"text\":\"至臻完美\"},{\"type\":\"骄者必败\",\"text\":\"骄者必败\"},{\"type\":\"先祖的代价\",\"text\":\"先祖的代价\"},{\"type\":\"普罗米修斯之备\",\"text\":\"普罗米修斯之备\"},{\"type\":\"兴盛\",\"text\":\"兴盛\"},{\"type\":\"混沌之雨\",\"text\":\"混沌之雨\"},{\"type\":\"诱惑之雨\",\"text\":\"诱惑之雨\"},{\"type\":\"鼠辈\",\"text\":\"鼠辈\"},{\"type\":\"重生\",\"text\":\"重生\"},{\"type\":\"铭记\",\"text\":\"铭记\"},{\"type\":\"萨博辛的誓言\",\"text\":\"萨博辛的誓言\"},{\"type\":\"海洋学者\",\"text\":\"海洋学者\"},{\"type\":\"七年厄运\",\"text\":\"七年厄运\"},{\"type\":\"命运之晶\",\"text\":\"命运之晶\"},{\"type\":\"挥霍无度\",\"text\":\"挥霍无度\"},{\"type\":\"雷劈\",\"text\":\"雷劈\"},{\"type\":\"无罪之援\",\"text\":\"无罪之援\"},{\"type\":\"学院派\",\"text\":\"学院派\"},{\"type\":\"仰慕者\",\"text\":\"仰慕者\"},{\"type\":\"大艺术家\",\"text\":\"大艺术家\"},{\"type\":\"大法师的右手\",\"text\":\"大法师的右手\"},{\"type\":\"竞技场冠军\",\"text\":\"竞技场冠军\"},{\"type\":\"鲜血大军\",\"text\":\"鲜血大军\"},{\"type\":\"艺者\",\"text\":\"艺者\"},{\"type\":\"复仇者\",\"text\":\"复仇者\"},{\"type\":\"觉醒\",\"text\":\"觉醒\"},{\"type\":\"交易\",\"text\":\"交易\"},{\"type\":\"战火锻造\",\"text\":\"战火锻造\"},{\"type\":\"狂兽\",\"text\":\"狂兽\"},{\"type\":\"背叛\",\"text\":\"背叛\"},{\"type\":\"猛烈绽放\",\"text\":\"猛烈绽放\"},{\"type\":\"灼热之火\",\"text\":\"灼热之火\"},{\"type\":\"血肉之躯\",\"text\":\"血肉之躯\"},{\"type\":\"骸骨\",\"text\":\"骸骨\"},{\"type\":\"裂隙\",\"text\":\"裂隙\"},{\"type\":\"枯骨皇帝\",\"text\":\"枯骨皇帝\"},{\"type\":\"密藏\",\"text\":\"密藏\"},{\"type\":\"不协之音\",\"text\":\"不协之音\"},{\"type\":\"越界的呼唤\",\"text\":\"越界的呼唤\"},{\"type\":\"食腐掠鸦\",\"text\":\"食腐掠鸦\"},{\"type\":\"制图师\",\"text\":\"制图师\"},{\"type\":\"巨变\",\"text\":\"巨变\"},{\"type\":\"灾变\",\"text\":\"灾变\"},{\"type\":\"天堂执法官\",\"text\":\"天堂执法官\"},{\"type\":\"天堂之石\",\"text\":\"天堂之石\"},{\"type\":\"束缚之炼\",\"text\":\"束缚之炼\"},{\"type\":\"出老千\",\"text\":\"出老千\"},{\"type\":\"天选\",\"text\":\"天选\"},{\"type\":\"风暴来袭\",\"text\":\"风暴来袭\"},{\"type\":\"雷针\",\"text\":\"雷针\"},{\"type\":\"渴求\",\"text\":\"渴求\"},{\"type\":\"诅咒之王\",\"text\":\"诅咒之王\"},{\"type\":\"咒诅之灵\",\"text\":\"咒诅之灵\"},{\"type\":\"极致不凡\",\"text\":\"极致不凡\"},{\"type\":\"深深黑梦\",\"text\":\"深深黑梦\"},{\"type\":\"黑暗术者\",\"text\":\"黑暗术者\"},{\"type\":\"买卖\",\"text\":\"买卖\"},{\"type\":\"狡徒\",\"text\":\"狡徒\"},{\"type\":\"深渊之子\",\"text\":\"深渊之子\"},{\"type\":\"恶魔\",\"text\":\"恶魔\"},{\"type\":\"魅魔\",\"text\":\"魅魔\"},{\"type\":\"疯医\",\"text\":\"疯医\"},{\"type\":\"傀儡\",\"text\":\"傀儡\"},{\"type\":\"巨龙\",\"text\":\"巨龙\"},{\"type\":\"龙之心\",\"text\":\"龙之心\"},{\"type\":\"追梦者\",\"text\":\"追梦者\"},{\"type\":\"梦境\",\"text\":\"梦境\"},{\"type\":\"酒醉贵族\",\"text\":\"酒醉贵族\"},{\"type\":\"轻松漫步\",\"text\":\"轻松漫步\"},{\"type\":\"裂界之殇\",\"text\":\"裂界之殇\"},{\"type\":\"黑暗来犯\",\"text\":\"黑暗来犯\"},{\"type\":\"无尽黑暗\",\"text\":\"无尽黑暗\"},{\"type\":\"耐久者\",\"text\":\"耐久者\"},{\"type\":\"智慧启蒙\",\"text\":\"智慧启蒙\"},{\"type\":\"逃亡\",\"text\":\"逃亡\"},{\"type\":\"空灵\",\"text\":\"空灵\"},{\"type\":\"探险家\",\"text\":\"探险家\"},{\"type\":\"恐怖之眼\",\"text\":\"恐怖之眼\"},{\"type\":\"龙之眼\",\"text\":\"龙之眼\"},{\"type\":\"无尽深渊\",\"text\":\"无尽深渊\"},{\"type\":\"盛宴\",\"text\":\"盛宴\"},{\"type\":\"劣魔\",\"text\":\"劣魔\"},{\"type\":\"鱼贩\",\"text\":\"鱼贩\"},{\"type\":\"制箭者\",\"text\":\"制箭者\"},{\"type\":\"芙劳拉的赠礼\",\"text\":\"芙劳拉的赠礼\"},{\"type\":\"愚人\",\"text\":\"愚人\"},{\"type\":\"无迹之海\",\"text\":\"无迹之海\"},{\"type\":\"遗弃之物\",\"text\":\"遗弃之物\"},{\"type\":\"狡狐\",\"text\":\"狡狐\"},{\"type\":\"赌徒\",\"text\":\"赌徒\"},{\"type\":\"炫耀之力\",\"text\":\"炫耀之力\"},{\"type\":\"珠宝匠\",\"text\":\"珠宝匠\"},{\"type\":\"绅士之风\",\"text\":\"绅士之风\"},{\"type\":\"斗士\",\"text\":\"斗士\"},{\"type\":\"黄金纪元\",\"text\":\"黄金纪元\"},{\"type\":\"至高之愿\",\"text\":\"至高之愿\"},{\"type\":\"天人永隔\",\"text\":\"天人永隔\"},{\"type\":\"壮心不已\",\"text\":\"壮心不已\"},{\"type\":\"收割者\",\"text\":\"收割者\"},{\"type\":\"隐士\",\"text\":\"隐士\"},{\"type\":\"英勇打击\",\"text\":\"英勇打击\"},{\"type\":\"知识之巢\",\"text\":\"知识之巢\"},{\"type\":\"宝箱\",\"text\":\"宝箱\"},{\"type\":\"饥饿\",\"text\":\"饥饿\"},{\"type\":\"永恒不朽\",\"text\":\"永恒不朽\"},{\"type\":\"咒语\",\"text\":\"咒语\"},{\"type\":\"无辜者\",\"text\":\"无辜者\"},{\"type\":\"移花接木\",\"text\":\"移花接木\"},{\"type\":\"永不满足\",\"text\":\"永不满足\"},{\"type\":\"发明家\",\"text\":\"发明家\"},{\"type\":\"坚毅诗人\",\"text\":\"坚毅诗人\"},{\"type\":\"小丑\",\"text\":\"小丑\"},{\"type\":\"珠宝匠的福祉\",\"text\":\"珠宝匠的福祉\"},{\"type\":\"狗仔队\",\"text\":\"狗仔队\"},{\"type\":\"旅程\",\"text\":\"旅程\"},{\"type\":\"王者之刃\",\"text\":\"王者之刃\"},{\"type\":\"王者之心\",\"text\":\"王者之心\"},{\"type\":\"降临\",\"text\":\"降临\"},{\"type\":\"屹立不败之人\",\"text\":\"屹立不败之人\"},{\"type\":\"巫妖\",\"text\":\"巫妖\"},{\"type\":\"生命窃贼\",\"text\":\"生命窃贼\"},{\"type\":\"雄狮\",\"text\":\"雄狮\"},{\"type\":\"长线钓鱼\",\"text\":\"长线钓鱼\"},{\"type\":\"暗黑之王\",\"text\":\"暗黑之王\"},{\"type\":\"欢庆领主\",\"text\":\"欢庆领主\"},{\"type\":\"情人\",\"text\":\"情人\"},{\"type\":\"月影女祭司\",\"text\":\"月影女祭司\"},{\"type\":\"狂王\",\"text\":\"狂王\"},{\"type\":\"主宰\",\"text\":\"主宰\"},{\"type\":\"工匠大师\",\"text\":\"工匠大师\"},{\"type\":\"庄园主\",\"text\":\"庄园主\"},{\"type\":\"佣兵\",\"text\":\"佣兵\"},{\"type\":\"信使\",\"text\":\"信使\"},{\"type\":\"铁匠的赠礼\",\"text\":\"铁匠的赠礼\"},{\"type\":\"山脉\",\"text\":\"山脉\"},{\"type\":\"照料者\",\"text\":\"照料者\"},{\"type\":\"力之誓言\",\"text\":\"力之誓言\"},{\"type\":\"黑暗缭绕\",\"text\":\"黑暗缭绕\"},{\"type\":\"奉献\",\"text\":\"奉献\"},{\"type\":\"老人\",\"text\":\"老人\"},{\"type\":\"完人\",\"text\":\"完人\"},{\"type\":\"净白\",\"text\":\"净白\"},{\"type\":\"群聚之首\",\"text\":\"群聚之首\"},{\"type\":\"协约\",\"text\":\"协约\"},{\"type\":\"忏悔者\",\"text\":\"忏悔者\"},{\"type\":\"诗人\",\"text\":\"诗人\"},{\"type\":\"博学者\",\"text\":\"博学者\"},{\"type\":\"射成筛子\",\"text\":\"射成筛子\"},{\"type\":\"忠诚的代价\",\"text\":\"忠诚的代价\"},{\"type\":\"保护的代价\",\"text\":\"保护的代价\"},{\"type\":\"起源\",\"text\":\"起源\"},{\"type\":\"教授\",\"text\":\"教授\"},{\"type\":\"月影的子嗣\",\"text\":\"月影的子嗣\"},{\"type\":\"谜团\",\"text\":\"谜团\"},{\"type\":\"女王\",\"text\":\"女王\"},{\"type\":\"疯狂恐喙鸟\",\"text\":\"疯狂恐喙鸟\"},{\"type\":\"家的捷径\",\"text\":\"家的捷径\"},{\"type\":\"危机\",\"text\":\"危机\"},{\"type\":\"元素祭祀\",\"text\":\"元素祭祀\"},{\"type\":\"力量之道\",\"text\":\"力量之道\"},{\"type\":\"残酷之环\",\"text\":\"残酷之环\"},{\"type\":\"牺牲\",\"text\":\"牺牲\"},{\"type\":\"圣人之礼\",\"text\":\"圣人之礼\"},{\"type\":\"武士之眼\",\"text\":\"武士之眼\"},{\"type\":\"创痕之原\",\"text\":\"创痕之原\"},{\"type\":\"拾荒者\",\"text\":\"拾荒者\"},{\"type\":\"学者\",\"text\":\"学者\"},{\"type\":\"寻觅者\",\"text\":\"寻觅者\"},{\"type\":\"生命之树\",\"text\":\"生命之树\"},{\"type\":\"支线任务\",\"text\":\"支线任务\"},{\"type\":\"魔符\",\"text\":\"魔符\"},{\"type\":\"海妖\",\"text\":\"海妖\"},{\"type\":\"魔侍\",\"text\":\"魔侍\"},{\"type\":\"灵魂\",\"text\":\"灵魂\"},{\"type\":\"闪光与火焰\",\"text\":\"闪光与火焰\"},{\"type\":\"骄纵皇子\",\"text\":\"骄纵皇子\"},{\"type\":\"冷淡\",\"text\":\"冷淡\"},{\"type\":\"风暴使者\",\"text\":\"风暴使者\"},{\"type\":\"兵法家\",\"text\":\"兵法家\"},{\"type\":\"召唤师\",\"text\":\"召唤师\"},{\"type\":\"烈日\",\"text\":\"烈日\"},{\"type\":\"外科医师\",\"text\":\"外科医师\"},{\"type\":\"勘查员\",\"text\":\"勘查员\"},{\"type\":\"求生专家\",\"text\":\"求生专家\"},{\"type\":\"给养\",\"text\":\"给养\"},{\"type\":\"剑圣的致敬\",\"text\":\"剑圣的致敬\"},{\"type\":\"大奇术师\",\"text\":\"大奇术师\"},{\"type\":\"王座\",\"text\":\"王座\"},{\"type\":\"修补匠之桌\",\"text\":\"修补匠之桌\"},{\"type\":\"塔峰\",\"text\":\"塔峰\"},{\"type\":\"背叛者\",\"text\":\"背叛者\"},{\"type\":\"远征\",\"text\":\"远征\"},{\"type\":\"风滚草\",\"text\":\"风滚草\"},{\"type\":\"暮光之月\",\"text\":\"暮光之月\"},{\"type\":\"孪生\",\"text\":\"孪生\"},{\"type\":\" 暴虐之灵\",\"text\":\" 暴虐之灵\"},{\"type\":\"无畏者\",\"text\":\"无畏者\"},{\"type\":\"无可争议者\",\"text\":\"无可争议者\"},{\"type\":\"意外收获\",\"text\":\"意外收获\"},{\"type\":\"联姻\",\"text\":\"联姻\"},{\"type\":\"女之武神\",\"text\":\"女之武神\"},{\"type\":\"金属盒子\",\"text\":\"金属盒子\"},{\"type\":\"平壤\",\"text\":\"平壤\"},{\"type\":\"梦想家\",\"text\":\"梦想家\"},{\"type\":\"虚空\",\"text\":\"虚空\"},{\"type\":\"典狱长\",\"text\":\"典狱长\"},{\"type\":\"大军阀\",\"text\":\"大军阀\"},{\"type\":\"凝视者\",\"text\":\"凝视者\"},{\"type\":\"命运之网\",\"text\":\"命运之网\"},{\"type\":\"白衣骑士\",\"text\":\"白衣骑士\"},{\"type\":\"枯萎玫瑰\",\"text\":\"枯萎玫瑰\"},{\"type\":\"风\",\"text\":\"风\"},{\"type\":\"巫婆\",\"text\":\"巫婆\"},{\"type\":\"群狼之王\",\"text\":\"群狼之王\"},{\"type\":\"狼王的遗产\",\"text\":\"狼王的遗产\"},{\"type\":\"狼的影子\",\"text\":\"狼的影子\"},{\"type\":\"狼王之弦\",\"text\":\"狼王之弦\"},{\"type\":\"金刚狼\",\"text\":\"金刚狼\"},{\"type\":\"噬界者\",\"text\":\"噬界者\"},{\"type\":\"怨忿\",\"text\":\"怨忿\"},{\"type\":\"死灵遗物\",\"text\":\"死灵遗物\"},{\"type\":\"求知若渴\",\"text\":\"求知若渴\"},{\"type\":\"黑暗三面\",\"text\":\"黑暗三面\"},{\"type\":\"三魔音\",\"text\":\"三魔音\"},{\"type\":\"怒雷之空\",\"text\":\"怒雷之空\"},{\"type\":\"失落遗骨\",\"text\":\"失落遗骨\"},{\"type\":\"宁静\",\"text\":\"宁静\"},{\"type\":\"赏金猎手\",\"text\":\"赏金猎手\"},{\"type\":\"十三大忌\",\"text\":\"十三大忌\"},{\"type\":\"忍辱\",\"text\":\"忍辱\"},{\"type\":\"破除枷锁\",\"text\":\"破除枷锁\"},{\"type\":\"地下森林\",\"text\":\"地下森林\"},{\"type\":\"一厢情愿\",\"text\":\"一厢情愿\"},{\"type\":\"虚荣\",\"text\":\"虚荣\"},{\"type\":\"恶毒的权力\",\"text\":\"恶毒的权力\"},{\"type\":\"薇妮雅的信物\",\"text\":\"薇妮雅的信物\"},{\"type\":\"元素虚空\",\"text\":\"元素虚空\"},{\"type\":\"不稳定的力量\",\"text\":\"不稳定的力量\"},{\"type\":\"钱与权\",\"text\":\"钱与权\"}]";
        } else if (itemTag.equals("药剂")) {
            return "[{\"name\":\"阿兹里的诺言\",\"type\":\"紫晶药剂\",\"text\":\"阿兹里的诺言 紫晶药剂\",\"flags\":{\"unique\":true}},{\"name\":\"卡鲁之血\",\"type\":\"圣化生命药剂\",\"text\":\"卡鲁之血 圣化生命药剂\",\"flags\":{\"unique\":true}},{\"name\":\"瓶中信仰\",\"type\":\"硫磺药剂\",\"text\":\"瓶中信仰 硫磺药剂\",\"flags\":{\"unique\":true}},{\"name\":\"灰烬之瓮\",\"type\":\"真银药剂\",\"text\":\"灰烬之瓮 真银药剂\",\"flags\":{\"unique\":true}},{\"name\":\"克拉里多之名\",\"type\":\"宝钻药剂\",\"text\":\"克拉里多之名 宝钻药剂\",\"flags\":{\"unique\":true}},{\"name\":\"闪耀精华\",\"type\":\"红玉药剂\",\"text\":\"闪耀精华 红玉药剂\",\"flags\":{\"unique\":true}},{\"name\":\"宝视精华\",\"type\":\"大型复合药剂\",\"text\":\"宝视精华 大型复合药剂\",\"flags\":{\"unique\":true}},{\"name\":\"德瑞的妙药\",\"type\":\"良质魔力药剂\",\"text\":\"德瑞的妙药 良质魔力药剂\",\"flags\":{\"unique\":true}},{\"name\":\"逝日\",\"type\":\"红玉药剂\",\"text\":\"逝日 红玉药剂\",\"flags\":{\"unique\":true}},{\"name\":\"禁果\",\"type\":\"石英药剂\",\"text\":\"禁果 石英药剂\",\"flags\":{\"unique\":true}},{\"name\":\"奇亚拉的决心\",\"type\":\"真银药剂\",\"text\":\"奇亚拉的决心 真银药剂\",\"flags\":{\"unique\":true}},{\"name\":\"拉维安加之泉\",\"type\":\"圣化魔力药剂\",\"text\":\"拉维安加之泉 圣化魔力药剂\",\"flags\":{\"unique\":true}},{\"name\":\"狮吼精华\",\"type\":\"坚岩药剂\",\"text\":\"狮吼精华 坚岩药剂\",\"flags\":{\"unique\":true}},{\"name\":\"拉维安加之泉【仿品】\",\"type\":\"圣化魔力药剂\",\"text\":\"拉维安加之泉【仿品】 圣化魔力药剂\",\"flags\":{\"unique\":true}},{\"name\":\"鲁米的灵药【仿品】\",\"type\":\"坚岩药剂\",\"text\":\"鲁米的灵药【仿品】 坚岩药剂\",\"flags\":{\"unique\":true}},{\"name\":\"神圣哀悼【仿品】\",\"type\":\"硫磺药剂\",\"text\":\"神圣哀悼【仿品】 硫磺药剂\",\"flags\":{\"unique\":true}},{\"name\":\"伤胃酒\",\"type\":\"水银药剂\",\"text\":\"伤胃酒 水银药剂\",\"flags\":{\"unique\":true}},{\"name\":\"鲁米的灵药\",\"type\":\"坚岩药剂\",\"text\":\"鲁米的灵药 坚岩药剂\",\"flags\":{\"unique\":true}},{\"name\":\"再生的罪恶\",\"type\":\"迷雾药剂\",\"text\":\"再生的罪恶 迷雾药剂\",\"flags\":{\"unique\":true}},{\"name\":\"捕魂者\",\"type\":\"石英药剂\",\"text\":\"捕魂者 石英药剂\",\"flags\":{\"unique\":true}},{\"name\":\"裂魂者\",\"type\":\"石英药剂\",\"text\":\"裂魂者 石英药剂\",\"flags\":{\"unique\":true}},{\"name\":\"恨意\",\"type\":\"蓝玉药剂\",\"text\":\"恨意 蓝玉药剂\",\"flags\":{\"unique\":true}},{\"name\":\"高洁圣杯\",\"type\":\"硫磺药剂\",\"text\":\"高洁圣杯 硫磺药剂\",\"flags\":{\"unique\":true}},{\"name\":\"神圣哀悼\",\"type\":\"硫磺药剂\",\"text\":\"神圣哀悼 硫磺药剂\",\"flags\":{\"unique\":true}},{\"name\":\"聪明的欧克\",\"type\":\"灰岩药剂\",\"text\":\"聪明的欧克 灰岩药剂\",\"flags\":{\"unique\":true}},{\"name\":\"扭曲之罐\",\"type\":\"祝福复合药剂\",\"text\":\"扭曲之罐 祝福复合药剂\",\"flags\":{\"unique\":true}},{\"name\":\"维克塔血器\",\"type\":\"黄玉药剂\",\"text\":\"维克塔血器 黄玉药剂\",\"flags\":{\"unique\":true}},{\"name\":\"巫酿之水\",\"type\":\"迷雾药剂\",\"text\":\"巫酿之水 迷雾药剂\",\"flags\":{\"unique\":true}},{\"name\":\"泽佛伊的终息\",\"type\":\"优质魔力药剂\",\"text\":\"泽佛伊的终息 优质魔力药剂\",\"flags\":{\"unique\":true}}]";
        } else if (itemTag.equals("传奇首饰")) {
            return "[{\"name\":\"阿克莱的草原\",\"type\":\"红玉戒指\",\"text\":\"阿克莱的草原 红玉戒指\",\"flags\":{\"unique\":true}},{\"name\":\"阿克莱的高山\",\"type\":\"红玉戒指\",\"text\":\"阿克莱的高山 红玉戒指\",\"flags\":{\"unique\":true}},{\"name\":\"阿克莱的山谷\",\"type\":\"红玉戒指\",\"text\":\"阿克莱的山谷 红玉戒指\",\"flags\":{\"unique\":true}},{\"name\":\"贪欲之记\",\"type\":\"金光戒指\",\"text\":\"贪欲之记 金光戒指\",\"flags\":{\"unique\":true}},{\"name\":\"渔夫之辫\",\"type\":\"潜能之戒\",\"text\":\"渔夫之辫 潜能之戒\",\"flags\":{\"unique\":true}},{\"name\":\"隐灵之符\",\"type\":\"珊瑚护身符\",\"text\":\"隐灵之符 珊瑚护身符\",\"flags\":{\"unique\":true}},{\"name\":\"变节者\",\"type\":\"扣链腰带\",\"text\":\"变节者 扣链腰带\",\"flags\":{\"unique\":true}},{\"name\":\"星界投影\",\"type\":\"黄玉戒指\",\"text\":\"星界投影 黄玉戒指\",\"flags\":{\"unique\":true}},{\"name\":\"均衡之符\",\"type\":\"黑曜护身符\",\"text\":\"均衡之符 黑曜护身符\",\"flags\":{\"unique\":true}},{\"name\":\"阿兹里圣徽\",\"type\":\"海灵护身符\",\"text\":\"阿兹里圣徽 海灵护身符\",\"flags\":{\"unique\":true}},{\"name\":\"奥尔之兴\",\"type\":\"黑曜护身符\",\"text\":\"奥尔之兴 黑曜护身符\",\"flags\":{\"unique\":true}},{\"name\":\"奥术之符\",\"type\":\"水晶腰带\",\"text\":\"奥术之符 水晶腰带\",\"flags\":{\"unique\":true}},{\"name\":\"兄弟会徽章\",\"type\":\"青玉护身符\",\"text\":\"兄弟会徽章 青玉护身符\",\"flags\":{\"unique\":true}},{\"name\":\"凝息\",\"type\":\"扣链腰带\",\"text\":\"凝息 扣链腰带\",\"flags\":{\"unique\":true}},{\"name\":\"熊之束腰\",\"type\":\"皮革腰带\",\"text\":\"熊之束腰 皮革腰带\",\"flags\":{\"unique\":true}},{\"name\":\"狡徒束腰\",\"type\":\"重革腰带\",\"text\":\"狡徒束腰 重革腰带\",\"flags\":{\"unique\":true}},{\"name\":\"贝雷克的冰与雷之曲\",\"type\":\"双玉戒指\",\"text\":\"贝雷克的冰与雷之曲 双玉戒指\",\"flags\":{\"unique\":true}},{\"name\":\"贝雷克的冰与火之歌\",\"type\":\"双玉戒指\",\"text\":\"贝雷克的冰与火之歌 双玉戒指\",\"flags\":{\"unique\":true}},{\"name\":\"贝雷克的火与雷之乐\",\"type\":\"双玉戒指\",\"text\":\"贝雷克的火与雷之乐 双玉戒指\",\"flags\":{\"unique\":true}},{\"name\":\"比斯克的项圈\",\"type\":\"帝金护身符\",\"text\":\"比斯克的项圈 帝金护身符\",\"flags\":{\"unique\":true}},{\"name\":\"比斯克的缰绳\",\"type\":\"重革腰带\",\"text\":\"比斯克的缰绳 重革腰带\",\"flags\":{\"unique\":true}},{\"name\":\"幽暗之语\",\"type\":\"锻铁戒指\",\"text\":\"幽暗之语 锻铁戒指\",\"flags\":{\"unique\":true}},{\"name\":\"枯井\",\"type\":\"咒箍魔符\",\"text\":\"枯井 咒箍魔符\",\"flags\":{\"unique\":true}},{\"name\":\"沸血\",\"type\":\"珊瑚戒指\",\"text\":\"沸血 珊瑚戒指\",\"flags\":{\"unique\":true}},{\"name\":\"鲜血支配\",\"type\":\"大理石护身符\",\"text\":\"鲜血支配 大理石护身符\",\"flags\":{\"unique\":true}},{\"name\":\"堕落之血\",\"type\":\"琥珀护身符\",\"text\":\"堕落之血 琥珀护身符\",\"flags\":{\"unique\":true}},{\"name\":\"布琳洛特印记\",\"type\":\"潜能之戒\",\"text\":\"布琳洛特印记 潜能之戒\",\"flags\":{\"unique\":true}},{\"name\":\"意志呼唤\",\"type\":\"双玉戒指\",\"text\":\"意志呼唤 双玉戒指\",\"flags\":{\"unique\":true}},{\"name\":\"屠戮之心\",\"type\":\"黑曜护身符\",\"text\":\"屠戮之心 黑曜护身符\",\"flags\":{\"unique\":true}},{\"name\":\"脱缚之锁\",\"type\":\"扣链腰带\",\"text\":\"脱缚之锁 扣链腰带\",\"flags\":{\"unique\":true}},{\"name\":\"暴风之语\",\"type\":\"海玉护身符\",\"text\":\"暴风之语 海玉护身符\",\"flags\":{\"unique\":true}},{\"name\":\"苦痛之环\",\"type\":\"红玉戒指\",\"text\":\"苦痛之环 红玉戒指\",\"flags\":{\"unique\":true}},{\"name\":\"恐惧之环\",\"type\":\"蓝玉戒指\",\"text\":\"恐惧之环 蓝玉戒指\",\"flags\":{\"unique\":true}},{\"name\":\"罪恶之环\",\"type\":\"锻铁戒指\",\"text\":\"罪恶之环 锻铁戒指\",\"flags\":{\"unique\":true}},{\"name\":\"乡愁之环\",\"type\":\"紫晶戒指\",\"text\":\"乡愁之环 紫晶戒指\",\"flags\":{\"unique\":true}},{\"name\":\"悔恨之环\",\"type\":\"黄玉戒指\",\"text\":\"悔恨之环 黄玉戒指\",\"flags\":{\"unique\":true}},{\"name\":\"懦夫之链\",\"type\":\"扣链腰带\",\"text\":\"懦夫之链 扣链腰带\",\"flags\":{\"unique\":true}},{\"name\":\"懦夫之遗\",\"type\":\"扣链腰带\",\"text\":\"懦夫之遗 扣链腰带\",\"flags\":{\"unique\":true}},{\"name\":\"巨岩指套\",\"type\":\"皮革腰带\",\"text\":\"巨岩指套 皮革腰带\",\"flags\":{\"unique\":true}},{\"name\":\"德瑞索的战礼\",\"type\":\"黄晶护身符\",\"text\":\"德瑞索的战礼 黄晶护身符\",\"flags\":{\"unique\":true}},{\"name\":\"夜临\",\"type\":\"深渊腰带\",\"text\":\"夜临 深渊腰带\",\"flags\":{\"unique\":true}},{\"name\":\"亡者呼唤\",\"type\":\"紫晶戒指\",\"text\":\"亡者呼唤 紫晶戒指\",\"flags\":{\"unique\":true}},{\"name\":\"德瑞的魔具\",\"type\":\"海灵戒指\",\"text\":\"德瑞的魔具 海灵戒指\",\"flags\":{\"unique\":true}},{\"name\":\"多里亚尼之约\",\"type\":\"重革腰带\",\"text\":\"多里亚尼之约 重革腰带\",\"flags\":{\"unique\":true}},{\"name\":\"梦语之痕\",\"type\":\"蓝玉戒指\",\"text\":\"梦语之痕 蓝玉戒指\",\"flags\":{\"unique\":true}},{\"name\":\"戴亚迪安的晨曦\",\"type\":\"重革腰带\",\"text\":\"戴亚迪安的晨曦 重革腰带\",\"flags\":{\"unique\":true}},{\"name\":\"余烬之痕\",\"type\":\"红玉戒指\",\"text\":\"余烬之痕 红玉戒指\",\"flags\":{\"unique\":true}},{\"name\":\"菁华蠕虫\",\"type\":\"潜能之戒\",\"text\":\"菁华蠕虫 潜能之戒\",\"flags\":{\"unique\":true}},{\"name\":\"心灵抽取\",\"type\":\"玛瑙护身符\",\"text\":\"心灵抽取 玛瑙护身符\",\"flags\":{\"unique\":true}},{\"name\":\"寻宝者的心眼\",\"type\":\"黑曜护身符\",\"text\":\"寻宝者的心眼 黑曜护身符\",\"flags\":{\"unique\":true}},{\"name\":\"无罪之眼\",\"type\":\"黄晶护身符\",\"text\":\"无罪之眼 黄晶护身符\",\"flags\":{\"unique\":true}},{\"name\":\"巨狼之眼\",\"type\":\"狼王魔符\",\"text\":\"巨狼之眼 狼王魔符\",\"flags\":{\"unique\":true}},{\"name\":\"饥荒之结\",\"type\":\"素布腰带\",\"text\":\"饥荒之结 素布腰带\",\"flags\":{\"unique\":true}},{\"name\":\"命中注定\",\"type\":\"海灵戒指\",\"text\":\"命中注定 海灵戒指\",\"flags\":{\"unique\":true}},{\"name\":\"盛宴之结\",\"type\":\"素布腰带\",\"text\":\"盛宴之结 素布腰带\",\"flags\":{\"unique\":true}},{\"name\":\"愤怒之阀\",\"type\":\"青玉护身符\",\"text\":\"愤怒之阀 青玉护身符\",\"flags\":{\"unique\":true}},{\"name\":\"神赐\",\"type\":\"宝钻戒指\",\"text\":\"神赐 宝钻戒指\",\"flags\":{\"unique\":true}},{\"name\":\"昏暗之牙\",\"type\":\"碧珠护身符\",\"text\":\"昏暗之牙 碧珠护身符\",\"flags\":{\"unique\":true}},{\"name\":\"暴食\",\"type\":\"皮革腰带\",\"text\":\"暴食 皮革腰带\",\"flags\":{\"unique\":true}},{\"name\":\"猎首\",\"type\":\"皮革腰带\",\"text\":\"猎首 皮革腰带\",\"flags\":{\"unique\":true}},{\"name\":\"结魂之环\",\"type\":\"月光石戒指\",\"text\":\"结魂之环 月光石戒指\",\"flags\":{\"unique\":true}},{\"name\":\"辛格拉的凝视\",\"type\":\"黑曜护身符\",\"text\":\"辛格拉的凝视 黑曜护身符\",\"flags\":{\"unique\":true}},{\"name\":\"极北\",\"type\":\"皮革腰带\",\"text\":\"极北 皮革腰带\",\"flags\":{\"unique\":true}},{\"name\":\"西里的真相\",\"type\":\"翠玉护身符\",\"text\":\"西里的真相 翠玉护身符\",\"flags\":{\"unique\":true}},{\"name\":\"冰牙指环\",\"type\":\"锻铁戒指\",\"text\":\"冰牙指环 锻铁戒指\",\"flags\":{\"unique\":true}},{\"name\":\"永生\",\"type\":\"皮革腰带\",\"text\":\"永生 皮革腰带\",\"flags\":{\"unique\":true}},{\"name\":\"隐逝\",\"type\":\"黑曜护身符\",\"text\":\"隐逝 黑曜护身符\",\"flags\":{\"unique\":true}},{\"name\":\"冈姆的远见\",\"type\":\"珊瑚戒指\",\"text\":\"冈姆的远见 珊瑚戒指\",\"flags\":{\"unique\":true}},{\"name\":\"冈姆之路\",\"type\":\"珊瑚戒指\",\"text\":\"冈姆之路 珊瑚戒指\",\"flags\":{\"unique\":true}},{\"name\":\"卡鲁充能\",\"type\":\"翠玉护身符\",\"text\":\"卡鲁充能 翠玉护身符\",\"flags\":{\"unique\":true}},{\"name\":\"卡鲁的战徽\",\"type\":\"翠玉护身符\",\"text\":\"卡鲁的战徽 翠玉护身符\",\"flags\":{\"unique\":true}},{\"name\":\"基加萨鲁\",\"type\":\"黄玉戒指\",\"text\":\"基加萨鲁 黄玉戒指\",\"flags\":{\"unique\":true}},{\"name\":\"领袖的代价\",\"type\":\"黑曜护身符\",\"text\":\"领袖的代价 黑曜护身符\",\"flags\":{\"unique\":true}},{\"name\":\"圣体之绳\",\"type\":\"皮革腰带\",\"text\":\"圣体之绳 皮革腰带\",\"flags\":{\"unique\":true}},{\"name\":\"英灵宝环\",\"type\":\"锻铁戒指\",\"text\":\"英灵宝环 锻铁戒指\",\"flags\":{\"unique\":true}},{\"name\":\"罗里的幸运之灯\",\"type\":\"三相戒指\",\"text\":\"罗里的幸运之灯 三相戒指\",\"flags\":{\"unique\":true}},{\"name\":\"玛拉凯的巧技\",\"type\":\"潜能之戒\",\"text\":\"玛拉凯的巧技 潜能之戒\",\"flags\":{\"unique\":true}},{\"name\":\"马雷格罗的残酷\",\"type\":\"青玉护身符\",\"text\":\"马雷格罗的残酷 青玉护身符\",\"flags\":{\"unique\":true}},{\"name\":\"马雷格罗的染血之环\",\"type\":\"扣链腰带\",\"text\":\"马雷格罗的染血之环 扣链腰带\",\"flags\":{\"unique\":true}},{\"name\":\"屈服印记\",\"type\":\"潜能之戒\",\"text\":\"屈服印记 潜能之戒\",\"flags\":{\"unique\":true}},{\"name\":\"裂界之印\",\"type\":\"合金戒指\",\"text\":\"裂界之印 合金戒指\",\"flags\":{\"unique\":true}},{\"name\":\"塑界之印\",\"type\":\"蛋白石戒指\",\"text\":\"塑界之印 蛋白石戒指\",\"flags\":{\"unique\":true}},{\"name\":\"玛莉琳的护体之符\",\"type\":\"海玉护身符\",\"text\":\"玛莉琳的护体之符 海玉护身符\",\"flags\":{\"unique\":true}},{\"name\":\"梅吉诺德的力量泉源\",\"type\":\"重革腰带\",\"text\":\"梅吉诺德的力量泉源 重革腰带\",\"flags\":{\"unique\":true}},{\"name\":\"明恩的慧心\",\"type\":\"紫晶戒指\",\"text\":\"明恩的慧心 紫晶戒指\",\"flags\":{\"unique\":true}},{\"name\":\"莫考之拥\",\"type\":\"红玉戒指\",\"text\":\"莫考之拥 红玉戒指\",\"flags\":{\"unique\":true}},{\"name\":\"母亲的拥抱\",\"type\":\"重革腰带\",\"text\":\"母亲的拥抱 重革腰带\",\"flags\":{\"unique\":true}},{\"name\":\"哑风封印\",\"type\":\"潜能之戒\",\"text\":\"哑风封印 潜能之戒\",\"flags\":{\"unique\":true}},{\"name\":\"自然组织\",\"type\":\"死羽魔符\",\"text\":\"自然组织 死羽魔符\",\"flags\":{\"unique\":true}},{\"name\":\"努葛玛呼之印\",\"type\":\"红玉戒指\",\"text\":\"努葛玛呼之印 红玉戒指\",\"flags\":{\"unique\":true}},{\"name\":\"努葛玛呼之像\",\"type\":\"珊瑚护身符\",\"text\":\"努葛玛呼之像 珊瑚护身符\",\"flags\":{\"unique\":true}},{\"name\":\"夜守\",\"type\":\"黑牙魔符\",\"text\":\"夜守 黑牙魔符\",\"flags\":{\"unique\":true}},{\"name\":\"普兰德斯之印\",\"type\":\"饰布腰带\",\"text\":\"普兰德斯之印 饰布腰带\",\"flags\":{\"unique\":true}},{\"name\":\"普兰德斯之记\",\"type\":\"海灵戒指\",\"text\":\"普兰德斯之记 海灵戒指\",\"flags\":{\"unique\":true}},{\"name\":\"珀奎尔之趾\",\"type\":\"帝金护身符\",\"text\":\"珀奎尔之趾 帝金护身符\",\"flags\":{\"unique\":true}},{\"name\":\"恒毅意志\",\"type\":\"先锋腰带\",\"text\":\"恒毅意志 先锋腰带\",\"flags\":{\"unique\":true}},{\"name\":\"普拉克斯\",\"type\":\"海灵戒指\",\"text\":\"普拉克斯 海灵戒指\",\"flags\":{\"unique\":true}},{\"name\":\"先驱的纹章\",\"type\":\"双玉戒指\",\"text\":\"先驱的纹章 双玉戒指\",\"flags\":{\"unique\":true}},{\"name\":\"先驱的纹章\",\"type\":\"黄玉戒指\",\"text\":\"先驱的纹章 黄玉戒指\",\"flags\":{\"unique\":true}},{\"name\":\"先驱的纹章\",\"type\":\"蓝玉戒指\",\"text\":\"先驱的纹章 蓝玉戒指\",\"flags\":{\"unique\":true}},{\"name\":\"先驱的纹章\",\"type\":\"红玉戒指\",\"text\":\"先驱的纹章 红玉戒指\",\"flags\":{\"unique\":true}},{\"name\":\"先驱的纹章\",\"type\":\"三相戒指\",\"text\":\"先驱的纹章 三相戒指\",\"flags\":{\"unique\":true}},{\"name\":\"夏乌拉之印\",\"type\":\"黑曜护身符\",\"text\":\"夏乌拉之印 黑曜护身符\",\"flags\":{\"unique\":true}},{\"name\":\"幻彩菱织\",\"type\":\"素布腰带\",\"text\":\"幻彩菱织 素布腰带\",\"flags\":{\"unique\":true}},{\"name\":\"渎神代理\",\"type\":\"潜能之戒\",\"text\":\"渎神代理 潜能之戒\",\"flags\":{\"unique\":true}},{\"name\":\"普藤博的草原\",\"type\":\"黄玉戒指\",\"text\":\"普藤博的草原 黄玉戒指\",\"flags\":{\"unique\":true}},{\"name\":\"普藤博的高山\",\"type\":\"黄玉戒指\",\"text\":\"普藤博的高山 黄玉戒指\",\"flags\":{\"unique\":true}},{\"name\":\"普藤博的山谷\",\"type\":\"黄玉戒指\",\"text\":\"普藤博的山谷 黄玉戒指\",\"flags\":{\"unique\":true}},{\"name\":\"燃焰\",\"type\":\"蓝玉戒指\",\"text\":\"燃焰 蓝玉戒指\",\"flags\":{\"unique\":true}},{\"name\":\"火爆之拥\",\"type\":\"皮革腰带\",\"text\":\"火爆之拥 皮革腰带\",\"flags\":{\"unique\":true}},{\"name\":\"拉什卡德的耐心\",\"type\":\"翠玉护身符\",\"text\":\"拉什卡德的耐心 翠玉护身符\",\"flags\":{\"unique\":true}},{\"name\":\"红刃之环\",\"type\":\"潜能之戒\",\"text\":\"红刃之环 潜能之戒\",\"flags\":{\"unique\":true}},{\"name\":\"阿兹里圣徽【仿品】\",\"type\":\"海灵护身符\",\"text\":\"阿兹里圣徽【仿品】 海灵护身符\",\"flags\":{\"unique\":true}},{\"name\":\"凝息【仿品】\",\"type\":\"扣链腰带\",\"text\":\"凝息【仿品】 扣链腰带\",\"flags\":{\"unique\":true}},{\"name\":\"德瑞的魔具【仿品】\",\"type\":\"海灵戒指\",\"text\":\"德瑞的魔具【仿品】 海灵戒指\",\"flags\":{\"unique\":true}},{\"name\":\"余烬之痕【仿品】\",\"type\":\"红玉戒指\",\"text\":\"余烬之痕【仿品】 红玉戒指\",\"flags\":{\"unique\":true}},{\"name\":\"猎首【仿品】\",\"type\":\"皮革腰带\",\"text\":\"猎首【仿品】 皮革腰带\",\"flags\":{\"unique\":true}},{\"name\":\"西里的真相【仿品】\",\"type\":\"翠玉护身符\",\"text\":\"西里的真相【仿品】 翠玉护身符\",\"flags\":{\"unique\":true}},{\"name\":\"卡鲁的战徽【仿品】\",\"type\":\"翠玉护身符\",\"text\":\"卡鲁的战徽【仿品】 翠玉护身符\",\"flags\":{\"unique\":true}},{\"name\":\"玛拉凯的巧技【仿品】\",\"type\":\"潜能之戒\",\"text\":\"玛拉凯的巧技【仿品】 潜能之戒\",\"flags\":{\"unique\":true}},{\"name\":\"幻彩菱织【仿品】\",\"type\":\"素布腰带\",\"text\":\"幻彩菱织【仿品】 素布腰带\",\"flags\":{\"unique\":true}},{\"name\":\"突围者【仿品】\",\"type\":\"重革腰带\",\"text\":\"突围者【仿品】 重革腰带\",\"flags\":{\"unique\":true}},{\"name\":\"灵魂羁绊【仿品】\",\"type\":\"饰布腰带\",\"text\":\"灵魂羁绊【仿品】 饰布腰带\",\"flags\":{\"unique\":true}},{\"name\":\"塔萨里奥之印【仿品】\",\"type\":\"蓝玉戒指\",\"text\":\"塔萨里奥之印【仿品】 蓝玉戒指\",\"flags\":{\"unique\":true}},{\"name\":\"虚空慧眼【仿品】\",\"type\":\"潜能之戒\",\"text\":\"虚空慧眼【仿品】 潜能之戒\",\"flags\":{\"unique\":true}},{\"name\":\"冬之心【仿品】\",\"type\":\"帝金护身符\",\"text\":\"冬之心【仿品】 帝金护身符\",\"flags\":{\"unique\":true}},{\"name\":\"复仇的魅力\",\"type\":\"黄晶护身符\",\"text\":\"复仇的魅力 黄晶护身符\",\"flags\":{\"unique\":true}},{\"name\":\"瑞佛之冠\",\"type\":\"双玉戒指\",\"text\":\"瑞佛之冠 双玉戒指\",\"flags\":{\"unique\":true}},{\"name\":\"瑞佛诅咒\",\"type\":\"亡爪魔符\",\"text\":\"瑞佛诅咒 亡爪魔符\",\"flags\":{\"unique\":true}},{\"name\":\"罗米拉的潜力之环\",\"type\":\"宝钻戒指\",\"text\":\"罗米拉的潜力之环 宝钻戒指\",\"flags\":{\"unique\":true}},{\"name\":\"瑞斯拉萨的线圈\",\"type\":\"扣钉腰带\",\"text\":\"瑞斯拉萨的线圈 扣钉腰带\",\"flags\":{\"unique\":true}},{\"name\":\"祭祀之心\",\"type\":\"海灵护身符\",\"text\":\"祭祀之心 海灵护身符\",\"flags\":{\"unique\":true}},{\"name\":\"塑界者之籽\",\"type\":\"玛瑙护身符\",\"text\":\"塑界者之籽 玛瑙护身符\",\"flags\":{\"unique\":true}},{\"name\":\"薛朗的启示之环\",\"type\":\"月光石戒指\",\"text\":\"薛朗的启示之环 月光石戒指\",\"flags\":{\"unique\":true}},{\"name\":\"希比尔之叹\",\"type\":\"珊瑚戒指\",\"text\":\"希比尔之叹 珊瑚戒指\",\"flags\":{\"unique\":true}},{\"name\":\"魔灵之符\",\"type\":\"海灵护身符\",\"text\":\"魔灵之符 海灵护身符\",\"flags\":{\"unique\":true}},{\"name\":\"突围者\",\"type\":\"重革腰带\",\"text\":\"突围者 重革腰带\",\"flags\":{\"unique\":true}},{\"name\":\"蛇穴\",\"type\":\"蓝玉戒指\",\"text\":\"蛇穴 蓝玉戒指\",\"flags\":{\"unique\":true}},{\"name\":\"至日不眠\",\"type\":\"黑曜护身符\",\"text\":\"至日不眠 黑曜护身符\",\"flags\":{\"unique\":true}},{\"name\":\"灵魂羁绊\",\"type\":\"饰布腰带\",\"text\":\"灵魂羁绊 饰布腰带\",\"flags\":{\"unique\":true}},{\"name\":\"嗜魂\",\"type\":\"饰布腰带\",\"text\":\"嗜魂 饰布腰带\",\"flags\":{\"unique\":true}},{\"name\":\"瓦尔克拉斯之星\",\"type\":\"血色护身符\",\"text\":\"瓦尔克拉斯之星 血色护身符\",\"flags\":{\"unique\":true}},{\"name\":\"拉兹瓦的灵石\",\"type\":\"海玉护身符\",\"text\":\"拉兹瓦的灵石 海玉护身符\",\"flags\":{\"unique\":true}},{\"name\":\"风暴之炎\",\"type\":\"蛋白石戒指\",\"text\":\"风暴之炎 蛋白石戒指\",\"flags\":{\"unique\":true}},{\"name\":\"风暴之秘\",\"type\":\"黄玉戒指\",\"text\":\"风暴之秘 黄玉戒指\",\"flags\":{\"unique\":true}},{\"name\":\"奴役之索\",\"type\":\"重革腰带\",\"text\":\"奴役之索 重革腰带\",\"flags\":{\"unique\":true}},{\"name\":\"日炎\",\"type\":\"饰布腰带\",\"text\":\"日炎 饰布腰带\",\"flags\":{\"unique\":true}},{\"name\":\"塔萨里奥之印\",\"type\":\"蓝玉戒指\",\"text\":\"塔萨里奥之印 蓝玉戒指\",\"flags\":{\"unique\":true}},{\"name\":\"圣令\",\"type\":\"珊瑚护身符\",\"text\":\"圣令 珊瑚护身符\",\"flags\":{\"unique\":true}},{\"name\":\"纯净之泪\",\"type\":\"海玉护身符\",\"text\":\"纯净之泪 海玉护身符\",\"flags\":{\"unique\":true}},{\"name\":\"信念之砧\",\"type\":\"琥珀护身符\",\"text\":\"信念之砧 琥珀护身符\",\"flags\":{\"unique\":true}},{\"name\":\"苦行\",\"type\":\"帝金护身符\",\"text\":\"苦行 帝金护身符\",\"flags\":{\"unique\":true}},{\"name\":\"埃拉黛丝\",\"type\":\"玛瑙护身符\",\"text\":\"埃拉黛丝 玛瑙护身符\",\"flags\":{\"unique\":true}},{\"name\":\"复苏之药\",\"type\":\"饰布腰带\",\"text\":\"复苏之药 饰布腰带\",\"flags\":{\"unique\":true}},{\"name\":\"耀阳徽记\",\"type\":\"帝金护身符\",\"text\":\"耀阳徽记 帝金护身符\",\"flags\":{\"unique\":true}},{\"name\":\"短暂羁绊\",\"type\":\"海玉护身符\",\"text\":\"短暂羁绊 海玉护身符\",\"flags\":{\"unique\":true}},{\"name\":\"费伯之牙\",\"type\":\"黄晶护身符\",\"text\":\"费伯之牙 黄晶护身符\",\"flags\":{\"unique\":true}},{\"name\":\"流逝之时\",\"type\":\"饰布腰带\",\"text\":\"流逝之时 饰布腰带\",\"flags\":{\"unique\":true}},{\"name\":\"太平\",\"type\":\"翠玉护身符\",\"text\":\"太平 翠玉护身符\",\"flags\":{\"unique\":true}},{\"name\":\"绿林豪侠\",\"type\":\"金光戒指\",\"text\":\"绿林豪侠 金光戒指\",\"flags\":{\"unique\":true}},{\"name\":\"饥饿之环 \",\"type\":\"潜能之戒\",\"text\":\"饥饿之环  潜能之戒\",\"flags\":{\"unique\":true}},{\"name\":\"烈阳徽记\",\"type\":\"帝金护身符\",\"text\":\"烈阳徽记 帝金护身符\",\"flags\":{\"unique\":true}},{\"name\":\"厄运护符\",\"type\":\"黄晶护身符\",\"text\":\"厄运护符 黄晶护身符\",\"flags\":{\"unique\":true}},{\"name\":\"坚毅之环\",\"type\":\"扣钉腰带\",\"text\":\"坚毅之环 扣钉腰带\",\"flags\":{\"unique\":true}},{\"name\":\"游牧之环\",\"type\":\"扣钉腰带\",\"text\":\"游牧之环 扣钉腰带\",\"flags\":{\"unique\":true}},{\"name\":\"群魔殿\",\"type\":\"翠玉护身符\",\"text\":\"群魔殿 翠玉护身符\",\"flags\":{\"unique\":true}},{\"name\":\"草民\",\"type\":\"潜能之戒\",\"text\":\"草民 潜能之戒\",\"flags\":{\"unique\":true}},{\"name\":\"原始锁链\",\"type\":\"珊瑚护身符\",\"text\":\"原始锁链 珊瑚护身符\",\"flags\":{\"unique\":true}},{\"name\":\"呕吐\",\"type\":\"素布腰带\",\"text\":\"呕吐 素布腰带\",\"flags\":{\"unique\":true}},{\"name\":\"谋士之环\",\"type\":\"扣钉腰带\",\"text\":\"谋士之环 扣钉腰带\",\"flags\":{\"unique\":true}},{\"name\":\"元素之章\",\"type\":\"三相戒指\",\"text\":\"元素之章 三相戒指\",\"flags\":{\"unique\":true}},{\"name\":\"沧海桑田\",\"type\":\"饰布腰带\",\"text\":\"沧海桑田 饰布腰带\",\"flags\":{\"unique\":true}},{\"name\":\"守卫之铭\",\"type\":\"锻铁戒指\",\"text\":\"守卫之铭 锻铁戒指\",\"flags\":{\"unique\":true}},{\"name\":\"窃罪\",\"type\":\"三相戒指\",\"text\":\"窃罪 三相戒指\",\"flags\":{\"unique\":true}},{\"name\":\"时光之握\",\"type\":\"月光石戒指\",\"text\":\"时光之握 月光石戒指\",\"flags\":{\"unique\":true}},{\"name\":\"时空扭曲\",\"type\":\"月光石戒指\",\"text\":\"时空扭曲 月光石戒指\",\"flags\":{\"unique\":true}},{\"name\":\"不朽系命\",\"type\":\"皮革腰带\",\"text\":\"不朽系命 皮革腰带\",\"flags\":{\"unique\":true}},{\"name\":\"恩吉尔的和谐\",\"type\":\"青玉护身符\",\"text\":\"恩吉尔的和谐 青玉护身符\",\"flags\":{\"unique\":true}},{\"name\":\"乌扎萨的草原\",\"type\":\"蓝玉戒指\",\"text\":\"乌扎萨的草原 蓝玉戒指\",\"flags\":{\"unique\":true}},{\"name\":\"乌扎萨的高山\",\"type\":\"蓝玉戒指\",\"text\":\"乌扎萨的高山 蓝玉戒指\",\"flags\":{\"unique\":true}},{\"name\":\"乌扎萨的山谷\",\"type\":\"蓝玉戒指\",\"text\":\"乌扎萨的山谷 蓝玉戒指\",\"flags\":{\"unique\":true}},{\"name\":\"瓦拉库之印\",\"type\":\"黄玉戒指\",\"text\":\"瓦拉库之印 黄玉戒指\",\"flags\":{\"unique\":true}},{\"name\":\"女武神\",\"type\":\"月光石戒指\",\"text\":\"女武神 月光石戒指\",\"flags\":{\"unique\":true}},{\"name\":\"静脉穿刺\",\"type\":\"锻铁戒指\",\"text\":\"静脉穿刺 锻铁戒指\",\"flags\":{\"unique\":true}},{\"name\":\"赌神芬多\",\"type\":\"金光戒指\",\"text\":\"赌神芬多 金光戒指\",\"flags\":{\"unique\":true}},{\"name\":\"维多里奥的捷思\",\"type\":\"青玉护身符\",\"text\":\"维多里奥的捷思 青玉护身符\",\"flags\":{\"unique\":true}},{\"name\":\"活解\",\"type\":\"潜能之戒\",\"text\":\"活解 潜能之戒\",\"flags\":{\"unique\":true}},{\"name\":\"暴风之言\",\"type\":\"海玉护身符\",\"text\":\"暴风之言 海玉护身符\",\"flags\":{\"unique\":true}},{\"name\":\"虚空慧眼\",\"type\":\"潜能之戒\",\"text\":\"虚空慧眼 潜能之戒\",\"flags\":{\"unique\":true}},{\"name\":\"虚空心灵\",\"type\":\"锻铁戒指\",\"text\":\"虚空心灵 锻铁戒指\",\"flags\":{\"unique\":true}},{\"name\":\"福尔的忠诚之符\",\"type\":\"玛瑙护身符\",\"text\":\"福尔的忠诚之符 玛瑙护身符\",\"flags\":{\"unique\":true}},{\"name\":\"时空扭曲\",\"type\":\"青玉护身符\",\"text\":\"时空扭曲 青玉护身符\",\"flags\":{\"unique\":true}},{\"name\":\"战士的遗产\",\"type\":\"红玉戒指\",\"text\":\"战士的遗产 红玉戒指\",\"flags\":{\"unique\":true}},{\"name\":\"柳树之赐\",\"type\":\"翠玉护身符\",\"text\":\"柳树之赐 翠玉护身符\",\"flags\":{\"unique\":true}},{\"name\":\"冬之心\",\"type\":\"帝金护身符\",\"text\":\"冬之心 帝金护身符\",\"flags\":{\"unique\":true}},{\"name\":\"冬日之织\",\"type\":\"珊瑚戒指\",\"text\":\"冬日之织 珊瑚戒指\",\"flags\":{\"unique\":true}},{\"name\":\"龙蜕之带\",\"type\":\"皮革腰带\",\"text\":\"龙蜕之带 皮革腰带\",\"flags\":{\"unique\":true}},{\"name\":\"索伏之血\",\"type\":\"琥珀护身符\",\"text\":\"索伏之血 琥珀护身符\",\"flags\":{\"unique\":true}},{\"name\":\"索伏之心\",\"type\":\"琥珀护身符\",\"text\":\"索伏之心 琥珀护身符\",\"flags\":{\"unique\":true}},{\"name\":\"苦难羁绊\",\"type\":\"黑曜护身符\",\"text\":\"苦难羁绊 黑曜护身符\",\"flags\":{\"unique\":true}},{\"name\":\"泽佛伊之心\",\"type\":\"海灵护身符\",\"text\":\"泽佛伊之心 海灵护身符\",\"flags\":{\"unique\":true}},{\"type\":\"碧珠护身符\",\"text\":\"碧珠护身符\"},{\"type\":\"大理石护身符\",\"text\":\"大理石护身符\"},{\"type\":\"海灵护身符\",\"text\":\"海灵护身符\"},{\"type\":\"黄晶护身符\",\"text\":\"黄晶护身符\"},{\"type\":\"血色护身符\",\"text\":\"血色护身符\"},{\"type\":\"珊瑚护身符\",\"text\":\"珊瑚护身符\"},{\"type\":\"琥珀护身符\",\"text\":\"琥珀护身符\"},{\"type\":\"翠玉护身符\",\"text\":\"翠玉护身符\"},{\"type\":\"海玉护身符\",\"text\":\"海玉护身符\"},{\"type\":\"帝金护身符\",\"text\":\"帝金护身符\"},{\"type\":\"黑曜护身符\",\"text\":\"黑曜护身符\"},{\"type\":\"青玉护身符\",\"text\":\"青玉护身符\"},{\"type\":\"玛瑙护身符\",\"text\":\"玛瑙护身符\"},{\"type\":\"星盘项链\",\"text\":\"星盘项链\"},{\"type\":\"朴实项链\",\"text\":\"朴实项链\"},{\"type\":\"黑牙魔符\",\"text\":\"黑牙魔符\"},{\"type\":\"巨颚魔符\",\"text\":\"巨颚魔符\"},{\"type\":\"虫蛹魔符\",\"text\":\"虫蛹魔符\"},{\"type\":\"狂癫魔符\",\"text\":\"狂癫魔符\"},{\"type\":\"脊骨魔符\",\"text\":\"脊骨魔符\"},{\"type\":\"灰烬魔符\",\"text\":\"灰烬魔符\"},{\"type\":\"孤角魔符\",\"text\":\"孤角魔符\"},{\"type\":\"深渊魔符\",\"text\":\"深渊魔符\"},{\"type\":\"碎骨魔符\",\"text\":\"碎骨魔符\"},{\"type\":\"亡手魔符\",\"text\":\"亡手魔符\"},{\"type\":\"不朽魔符\",\"text\":\"不朽魔符\"},{\"type\":\"腐首魔符\",\"text\":\"腐首魔符\"},{\"type\":\"幻爪魔符\",\"text\":\"幻爪魔符\"},{\"type\":\"皇骨魔符\",\"text\":\"皇骨魔符\"},{\"type\":\"亡爪魔符\",\"text\":\"亡爪魔符\"},{\"type\":\"断螈魔符\",\"text\":\"断螈魔符\"},{\"type\":\"咒箍魔符\",\"text\":\"咒箍魔符\"},{\"type\":\"双子魔符\",\"text\":\"双子魔符\"},{\"type\":\"齿鲨魔符\",\"text\":\"齿鲨魔符\"},{\"type\":\"尖角魔符\",\"text\":\"尖角魔符\"},{\"type\":\"潜能魔符\",\"text\":\"潜能魔符\"},{\"type\":\"三鼠魔符\",\"text\":\"三鼠魔符\"},{\"type\":\"双猴魔符\",\"text\":\"双猴魔符\"},{\"type\":\"长牙魔符\",\"text\":\"长牙魔符\"},{\"type\":\"死羽魔符\",\"text\":\"死羽魔符\"},{\"type\":\"猴掌魔符\",\"text\":\"猴掌魔符\"},{\"type\":\"三手魔符\",\"text\":\"三手魔符\"},{\"type\":\"狼王魔符\",\"text\":\"狼王魔符\"},{\"type\":\"素布腰带\",\"text\":\"素布腰带\"},{\"type\":\"扣链腰带\",\"text\":\"扣链腰带\"},{\"type\":\"皮革腰带\",\"text\":\"皮革腰带\"},{\"type\":\"重革腰带\",\"text\":\"重革腰带\"},{\"type\":\"饰布腰带\",\"text\":\"饰布腰带\"},{\"type\":\"扣钉腰带\",\"text\":\"扣钉腰带\"},{\"type\":\"深渊腰带\",\"text\":\"深渊腰带\"},{\"type\":\"先锋腰带\",\"text\":\"先锋腰带\"},{\"type\":\"水晶腰带\",\"text\":\"水晶腰带\"},{\"type\":\"低酿腰带\",\"text\":\"低酿腰带\"},{\"type\":\"警戒腰带\",\"text\":\"警戒腰带\"},{\"type\":\"裂隙戒指\",\"text\":\"裂隙戒指\"},{\"type\":\"锻铁戒指\",\"text\":\"锻铁戒指\"},{\"type\":\"紫晶戒指\",\"text\":\"紫晶戒指\"},{\"type\":\"宝钻戒指\",\"text\":\"宝钻戒指\"},{\"type\":\"双玉戒指\",\"text\":\"双玉戒指\"},{\"type\":\"潜能之戒\",\"text\":\"潜能之戒\"},{\"type\":\"珊瑚戒指\",\"text\":\"珊瑚戒指\"},{\"type\":\"海灵戒指\",\"text\":\"海灵戒指\"},{\"type\":\"金光戒指\",\"text\":\"金光戒指\"},{\"type\":\"黄玉戒指\",\"text\":\"黄玉戒指\"},{\"type\":\"蓝玉戒指\",\"text\":\"蓝玉戒指\"},{\"type\":\"红玉戒指\",\"text\":\"红玉戒指\"},{\"type\":\"三相戒指\",\"text\":\"三相戒指\"},{\"type\":\"月光石戒指\",\"text\":\"月光石戒指\"},{\"type\":\"合金戒指\",\"text\":\"合金戒指\"},{\"type\":\"蛋白石戒指\",\"text\":\"蛋白石戒指\"},{\"type\":\"朱砂之戒\",\"text\":\"朱砂之戒\"},{\"type\":\"天蓝之戒\",\"text\":\"天蓝之戒\"},{\"type\":\"齿轮戒指\",\"text\":\"齿轮戒指\"},{\"type\":\"地线戒指\",\"text\":\"地线戒指\"},{\"type\":\"赏金猎人饰品\",\"text\":\"赏金猎人饰品\"}]";
        } else if (itemTag.equals("传奇武器")) {
            return "[{\"name\":\"艾贝拉斯之角\",\"type\":\"羊角法杖\",\"text\":\"艾贝拉斯之角 羊角法杖\",\"flags\":{\"unique\":true}},{\"name\":\"阿克顿\",\"type\":\"屠戮之斧\",\"text\":\"阿克顿 屠戮之斧\",\"flags\":{\"unique\":true}},{\"name\":\"超越壁垒\",\"type\":\"裂脏钩\",\"text\":\"超越壁垒 裂脏钩\",\"flags\":{\"unique\":true}},{\"name\":\"雷霆圣杖\",\"type\":\"帝国长杖\",\"text\":\"雷霆圣杖 帝国长杖\",\"flags\":{\"unique\":true}},{\"name\":\"东方雷霆圣杖\",\"type\":\"帝国长杖\",\"text\":\"东方雷霆圣杖 帝国长杖\",\"flags\":{\"unique\":true}},{\"name\":\"北方雷霆圣杖\",\"type\":\"帝国长杖\",\"text\":\"北方雷霆圣杖 帝国长杖\",\"flags\":{\"unique\":true}},{\"name\":\"南方雷霆圣杖\",\"type\":\"帝国长杖\",\"text\":\"南方雷霆圣杖 帝国长杖\",\"flags\":{\"unique\":true}},{\"name\":\"西方雷霆圣杖\",\"type\":\"帝国长杖\",\"text\":\"西方雷霆圣杖 帝国长杖\",\"flags\":{\"unique\":true}},{\"name\":\"汉恩的力量\",\"type\":\"夜语长剑\",\"text\":\"汉恩的力量 夜语长剑\",\"flags\":{\"unique\":true}},{\"name\":\"鬣犬之牙\",\"type\":\"远古战爪\",\"text\":\"鬣犬之牙 远古战爪\",\"flags\":{\"unique\":true}},{\"name\":\"魅惑\",\"type\":\"瓦尔战爪\",\"text\":\"魅惑 瓦尔战爪\",\"flags\":{\"unique\":true}},{\"name\":\"增幅杖\",\"type\":\"螺纹法杖\",\"text\":\"增幅杖 螺纹法杖\",\"flags\":{\"unique\":true}},{\"name\":\"艾普之怒\",\"type\":\"灵石法杖\",\"text\":\"艾普之怒 灵石法杖\",\"flags\":{\"unique\":true}},{\"name\":\"阿拉卡力之牙\",\"type\":\"兽血短匕\",\"text\":\"阿拉卡力之牙 兽血短匕\",\"flags\":{\"unique\":true}},{\"name\":\"阿伯瑞斯\",\"type\":\"暗影弓\",\"text\":\"阿伯瑞斯 暗影弓\",\"flags\":{\"unique\":true}},{\"name\":\"灰烬行者\",\"type\":\"石英法杖\",\"text\":\"灰烬行者 石英法杖\",\"flags\":{\"unique\":true}},{\"name\":\"烬杵\",\"type\":\"朽木之棒\",\"text\":\"烬杵 朽木之棒\",\"flags\":{\"unique\":true}},{\"name\":\"阿兹里的刑刃\",\"type\":\"瓦尔巨斧\",\"text\":\"阿兹里的刑刃 瓦尔巨斧\",\"flags\":{\"unique\":true}},{\"name\":\"占星\",\"type\":\"虚影短杖\",\"text\":\"占星 虚影短杖\",\"flags\":{\"unique\":true}},{\"name\":\"灵护\",\"type\":\"练武者细剑\",\"text\":\"灵护 练武者细剑\",\"flags\":{\"unique\":true}},{\"name\":\"历史公理\",\"type\":\"铜锻短杖\",\"text\":\"历史公理 铜锻短杖\",\"flags\":{\"unique\":true}},{\"name\":\"苍白烈火\",\"type\":\"灵石短杖\",\"text\":\"苍白烈火 灵石短杖\",\"flags\":{\"unique\":true}},{\"name\":\"芯木腰刀\",\"type\":\"永恒之剑\",\"text\":\"芯木腰刀 永恒之剑\",\"flags\":{\"unique\":true}},{\"name\":\"比诺的厨刀\",\"type\":\"猎者之刃\",\"text\":\"比诺的厨刀 猎者之刃\",\"flags\":{\"unique\":true}},{\"name\":\"苦梦\",\"type\":\"影语短杖\",\"text\":\"苦梦 影语短杖\",\"flags\":{\"unique\":true}},{\"name\":\"炎夏之血\",\"type\":\"锈剑\",\"text\":\"炎夏之血 锈剑\",\"flags\":{\"unique\":true}},{\"name\":\"血谑\",\"type\":\"锐利刺匕\",\"text\":\"血谑 锐利刺匕\",\"flags\":{\"unique\":true}},{\"name\":\"嗜血之爪\",\"type\":\"魔爪刃\",\"text\":\"嗜血之爪 魔爪刃\",\"flags\":{\"unique\":true}},{\"name\":\"脑乱者\",\"type\":\"戮魂重锤\",\"text\":\"脑乱者 戮魂重锤\",\"flags\":{\"unique\":true}},{\"name\":\"议会之息\",\"type\":\"禁礼短杖\",\"text\":\"议会之息 禁礼短杖\",\"flags\":{\"unique\":true}},{\"name\":\"光耀之锤\",\"type\":\"战锤\",\"text\":\"光耀之锤 战锤\",\"flags\":{\"unique\":true}},{\"name\":\"布鲁特斯的刑具\",\"type\":\"祭仪短杖\",\"text\":\"布鲁特斯的刑具 祭仪短杖\",\"flags\":{\"unique\":true}},{\"name\":\"坚定之刃\",\"type\":\"圣约之锤\",\"text\":\"坚定之刃 圣约之锤\",\"flags\":{\"unique\":true}},{\"name\":\"卡美利亚之贪婪\",\"type\":\"坚锤\",\"text\":\"卡美利亚之贪婪 坚锤\",\"flags\":{\"unique\":true}},{\"name\":\"卡美利亚之锤\",\"type\":\"坚锤\",\"text\":\"卡美利亚之锤 坚锤\",\"flags\":{\"unique\":true}},{\"name\":\"阐释之杖\",\"type\":\"艾兹麦长杖\",\"text\":\"阐释之杖 艾兹麦长杖\",\"flags\":{\"unique\":true}},{\"name\":\"冥犬残肢\",\"type\":\"血色短杖\",\"text\":\"冥犬残肢 血色短杖\",\"flags\":{\"unique\":true}},{\"name\":\"石冢\",\"type\":\"刚猛巨锤\",\"text\":\"石冢 刚猛巨锤\",\"flags\":{\"unique\":true}},{\"name\":\"乱矢之弦\",\"type\":\"暗影弓\",\"text\":\"乱矢之弦 暗影弓\",\"flags\":{\"unique\":true}},{\"name\":\"切特斯之针\",\"type\":\"贵族细剑\",\"text\":\"切特斯之针 贵族细剑\",\"flags\":{\"unique\":true}},{\"name\":\"忠诚之锤\",\"type\":\"刚猛巨锤\",\"text\":\"忠诚之锤 刚猛巨锤\",\"flags\":{\"unique\":true}},{\"name\":\"塑泥者\",\"type\":\"破岩锤\",\"text\":\"塑泥者 破岩锤\",\"flags\":{\"unique\":true}},{\"name\":\"寒铁刃\",\"type\":\"艾兹麦之匕\",\"text\":\"寒铁刃 艾兹麦之匕\",\"flags\":{\"unique\":true}},{\"name\":\"日耀之冠\",\"type\":\"水晶法杖\",\"text\":\"日耀之冠 水晶法杖\",\"flags\":{\"unique\":true}},{\"name\":\"卡斯普里怨恨\",\"type\":\"宝饰细剑\",\"text\":\"卡斯普里怨恨 宝饰细剑\",\"flags\":{\"unique\":true}},{\"name\":\"希比尔的冰爪\",\"type\":\"撕裂尖爪\",\"text\":\"希比尔的冰爪 撕裂尖爪\",\"flags\":{\"unique\":true}},{\"name\":\"德瑞索的热情\",\"type\":\"穿甲刺剑\",\"text\":\"德瑞索的热情 穿甲刺剑\",\"flags\":{\"unique\":true}},{\"name\":\"夜吟\",\"type\":\"暗影弓\",\"text\":\"夜吟 暗影弓\",\"flags\":{\"unique\":true}},{\"name\":\"死神之手\",\"type\":\"卡鲁短杖\",\"text\":\"死神之手 卡鲁短杖\",\"flags\":{\"unique\":true}},{\"name\":\"冥使之琴\",\"type\":\"死亡之弓\",\"text\":\"冥使之琴 死亡之弓\",\"flags\":{\"unique\":true}},{\"name\":\"死亡之作\",\"type\":\"死亡之弓\",\"text\":\"死亡之作 死亡之弓\",\"flags\":{\"unique\":true}},{\"name\":\"战歌\",\"type\":\"霸主巨斧\",\"text\":\"战歌 霸主巨斧\",\"flags\":{\"unique\":true}},{\"name\":\"裂世轮回\",\"type\":\"风暴长杖\",\"text\":\"裂世轮回 风暴长杖\",\"flags\":{\"unique\":true}},{\"name\":\"戒律之影\",\"type\":\"帝国短匕\",\"text\":\"戒律之影 帝国短匕\",\"flags\":{\"unique\":true}},{\"name\":\"灭世\",\"type\":\"皇家猎弓\",\"text\":\"灭世 皇家猎弓\",\"flags\":{\"unique\":true}},{\"name\":\"灭世之狱\",\"type\":\"皇家猎弓\",\"text\":\"灭世之狱 皇家猎弓\",\"flags\":{\"unique\":true}},{\"name\":\"终末之始\",\"type\":\"狮爪巨剑\",\"text\":\"终末之始 狮爪巨剑\",\"flags\":{\"unique\":true}},{\"name\":\"黑钢\",\"type\":\"瓦尔短杖\",\"text\":\"黑钢 瓦尔短杖\",\"flags\":{\"unique\":true}},{\"name\":\"多里亚尼的幻化之杖\",\"type\":\"瓦尔短杖\",\"text\":\"多里亚尼的幻化之杖 瓦尔短杖\",\"flags\":{\"unique\":true}},{\"name\":\"恐惧之镰\",\"type\":\"切割者\",\"text\":\"恐惧之镰 切割者\",\"flags\":{\"unique\":true}},{\"name\":\"恐惧利刃\",\"type\":\"锈剑\",\"text\":\"恐惧利刃 锈剑\",\"flags\":{\"unique\":true}},{\"name\":\"恐怖巨镰\",\"type\":\"切割者\",\"text\":\"恐怖巨镰 切割者\",\"flags\":{\"unique\":true}},{\"name\":\"幻梦飞羽\",\"type\":\"永恒之剑\",\"text\":\"幻梦飞羽 永恒之剑\",\"flags\":{\"unique\":true}},{\"name\":\"宵晓\",\"type\":\"风暴长杖\",\"text\":\"宵晓 风暴长杖\",\"flags\":{\"unique\":true}},{\"name\":\"龙凤吟\",\"type\":\"狱火之刃\",\"text\":\"龙凤吟 狱火之刃\",\"flags\":{\"unique\":true}},{\"name\":\"幽魂之息\",\"type\":\"铁锻长杖\",\"text\":\"幽魂之息 铁锻长杖\",\"flags\":{\"unique\":true}},{\"name\":\"叶兰德尔的拥抱\",\"type\":\"远古之祭\",\"text\":\"叶兰德尔的拥抱 远古之祭\",\"flags\":{\"unique\":true}},{\"name\":\"宇蚀\",\"type\":\"水晶法杖\",\"text\":\"宇蚀 水晶法杖\",\"flags\":{\"unique\":true}},{\"name\":\"疯狂边界\",\"type\":\"术雕巨剑\",\"text\":\"疯狂边界 术雕巨剑\",\"flags\":{\"unique\":true}},{\"name\":\"影殒\",\"type\":\"暮色之刃\",\"text\":\"影殒 暮色之刃\",\"flags\":{\"unique\":true}},{\"name\":\"精华收割器\",\"type\":\"瓦尔战爪\",\"text\":\"精华收割器 瓦尔战爪\",\"flags\":{\"unique\":true}},{\"name\":\"伊沃幻像\",\"type\":\"古代细剑\",\"text\":\"伊沃幻像 古代细剑\",\"flags\":{\"unique\":true}},{\"name\":\"瓦尔命运\",\"type\":\"宝石之剑\",\"text\":\"瓦尔命运 宝石之剑\",\"flags\":{\"unique\":true}},{\"name\":\"圣徒胫骨\",\"type\":\"史典长杖\",\"text\":\"圣徒胫骨 史典长杖\",\"flags\":{\"unique\":true}},{\"name\":\"绿藤\",\"type\":\"朽木之干\",\"text\":\"绿藤 朽木之干\",\"flags\":{\"unique\":true}},{\"name\":\"费德利塔斯之锋\",\"type\":\"锯状薄刃\",\"text\":\"费德利塔斯之锋 锯状薄刃\",\"flags\":{\"unique\":true}},{\"name\":\"血肉之嗜\",\"type\":\"梦境之锤\",\"text\":\"血肉之嗜 梦境之锤\",\"flags\":{\"unique\":true}},{\"name\":\"亘古之灵\",\"type\":\"玻璃利片\",\"text\":\"亘古之灵 玻璃利片\",\"flags\":{\"unique\":true}},{\"name\":\"霜息\",\"type\":\"华丽之锤\",\"text\":\"霜息 华丽之锤\",\"flags\":{\"unique\":true}},{\"name\":\"吉尔菲的净罪之锤\",\"type\":\"铜影巨锤\",\"text\":\"吉尔菲的净罪之锤 铜影巨锤\",\"flags\":{\"unique\":true}},{\"name\":\"吉尔菲的奉献之锤\",\"type\":\"铜影巨锤\",\"text\":\"吉尔菲的奉献之锤 铜影巨锤\",\"flags\":{\"unique\":true}},{\"name\":\"血裂\",\"type\":\"钝钉木棒\",\"text\":\"血裂 钝钉木棒\",\"flags\":{\"unique\":true}},{\"name\":\"血凿\",\"type\":\"剥皮刀\",\"text\":\"血凿 剥皮刀\",\"flags\":{\"unique\":true}},{\"name\":\"韧木曲刃\",\"type\":\"永恒之剑\",\"text\":\"韧木曲刃 永恒之剑\",\"flags\":{\"unique\":true}},{\"name\":\"思动之手\",\"type\":\"袭眼钩\",\"text\":\"思动之手 袭眼钩\",\"flags\":{\"unique\":true}},{\"name\":\"智行之手\",\"type\":\"帝国战爪\",\"text\":\"智行之手 帝国战爪\",\"flags\":{\"unique\":true}},{\"name\":\"裂心刃\",\"type\":\"皇家短匕\",\"text\":\"裂心刃 皇家短匕\",\"flags\":{\"unique\":true}},{\"name\":\"霸权时代\",\"type\":\"审判长杖\",\"text\":\"霸权时代 审判长杖\",\"flags\":{\"unique\":true}},{\"name\":\"嗜血黑兹玛娜\",\"type\":\"瓦尔巨斧\",\"text\":\"嗜血黑兹玛娜 瓦尔巨斧\",\"flags\":{\"unique\":true}},{\"name\":\"无击\",\"type\":\"残暴巨剑\",\"text\":\"无击 残暴巨剑\",\"flags\":{\"unique\":true}},{\"name\":\"破灭之希\",\"type\":\"游侠弓\",\"text\":\"破灭之希 游侠弓\",\"flags\":{\"unique\":true}},{\"name\":\"雷姆诺的挽歌\",\"type\":\"冷铁重锤\",\"text\":\"雷姆诺的挽歌 冷铁重锤\",\"flags\":{\"unique\":true}},{\"name\":\"雷姆诺的夺命凶器\",\"type\":\"冷铁重锤\",\"text\":\"雷姆诺的夺命凶器 冷铁重锤\",\"flags\":{\"unique\":true}},{\"name\":\"海昂的狂怒\",\"type\":\"军团长剑\",\"text\":\"海昂的狂怒 军团长剑\",\"flags\":{\"unique\":true}},{\"name\":\"一文字\",\"type\":\"海贼长刀\",\"text\":\"一文字 海贼长刀\",\"flags\":{\"unique\":true}},{\"name\":\"穿云\",\"type\":\"猎魂之弓\",\"text\":\"穿云 猎魂之弓\",\"flags\":{\"unique\":true}},{\"name\":\"英斯贝理之极\",\"type\":\"智者长剑\",\"text\":\"英斯贝理之极 智者长剑\",\"flags\":{\"unique\":true}},{\"name\":\"钢铁指挥\",\"type\":\"死亡之弓\",\"text\":\"钢铁指挥 死亡之弓\",\"flags\":{\"unique\":true}},{\"name\":\"伊泽洛之谜\",\"type\":\"帝国战爪\",\"text\":\"伊泽洛之谜 帝国战爪\",\"flags\":{\"unique\":true}},{\"name\":\"刽子手.杰克\",\"type\":\"瓦尔战斧\",\"text\":\"刽子手.杰克 瓦尔战斧\",\"flags\":{\"unique\":true}},{\"name\":\"乔赫黑钢\",\"type\":\"沉钢重锤\",\"text\":\"乔赫黑钢 沉钢重锤\",\"flags\":{\"unique\":true}},{\"name\":\"冈姆的霸业\",\"type\":\"卡鲁巨斧\",\"text\":\"冈姆的霸业 卡鲁巨斧\",\"flags\":{\"unique\":true}},{\"name\":\"王者之刃\",\"type\":\"霸主巨斧\",\"text\":\"王者之刃 霸主巨斧\",\"flags\":{\"unique\":true}},{\"name\":\"奇塔弗之盛宴\",\"type\":\"虚影巨斧\",\"text\":\"奇塔弗之盛宴 虚影巨斧\",\"flags\":{\"unique\":true}},{\"name\":\"刚多的虚荣\",\"type\":\"艾兹麦巨剑\",\"text\":\"刚多的虚荣 艾兹麦巨剑\",\"flags\":{\"unique\":true}},{\"name\":\"康戈的战炎\",\"type\":\"惧灵重锤\",\"text\":\"康戈的战炎 惧灵重锤\",\"flags\":{\"unique\":true}},{\"name\":\"拉奇许之刃\",\"type\":\"贵族之剑\",\"text\":\"拉奇许之刃 贵族之剑\",\"flags\":{\"unique\":true}},{\"name\":\"终息\",\"type\":\"拳钉\",\"text\":\"终息 拳钉\",\"flags\":{\"unique\":true}},{\"name\":\"拉维安加的智慧\",\"type\":\"战锤\",\"text\":\"拉维安加的智慧 战锤\",\"flags\":{\"unique\":true}},{\"name\":\"荒野之律\",\"type\":\"魔爪刃\",\"text\":\"荒野之律 魔爪刃\",\"flags\":{\"unique\":true}},{\"name\":\"生机之记\",\"type\":\"朽木法杖\",\"text\":\"生机之记 朽木法杖\",\"flags\":{\"unique\":true}},{\"name\":\"断罪\",\"type\":\"柴斧\",\"text\":\"断罪 柴斧\",\"flags\":{\"unique\":true}},{\"name\":\"狮眼的战弓\",\"type\":\"帝国之弓\",\"text\":\"狮眼的战弓 帝国之弓\",\"flags\":{\"unique\":true}},{\"name\":\"迷茫幻符\",\"type\":\"白金波刃\",\"text\":\"迷茫幻符 白金波刃\",\"flags\":{\"unique\":true}},{\"name\":\"尔奇的巨灵之锤\",\"type\":\"卡鲁重锤\",\"text\":\"尔奇的巨灵之锤 卡鲁重锤\",\"flags\":{\"unique\":true}},{\"name\":\"纯净之神的祭品\",\"type\":\"贵族长杖\",\"text\":\"纯净之神的祭品 贵族长杖\",\"flags\":{\"unique\":true}},{\"name\":\"冥约\",\"type\":\"符文法杖\",\"text\":\"冥约 符文法杖\",\"flags\":{\"unique\":true}},{\"name\":\"力量猎刃\",\"type\":\"残体利刃\",\"text\":\"力量猎刃 残体利刃\",\"flags\":{\"unique\":true}},{\"name\":\"魔藤\",\"type\":\"朽木之干\",\"text\":\"魔藤 朽木之干\",\"flags\":{\"unique\":true}},{\"name\":\"沉默之雷\",\"type\":\"坚锤\",\"text\":\"沉默之雷 坚锤\",\"flags\":{\"unique\":true}},{\"name\":\"凋灵魔爪\",\"type\":\"虚影短杖\",\"text\":\"凋灵魔爪 虚影短杖\",\"flags\":{\"unique\":true}},{\"name\":\"月岚\",\"type\":\"征战之斧\",\"text\":\"月岚 征战之斧\",\"flags\":{\"unique\":true}},{\"name\":\"泣月\",\"type\":\"魔性法杖\",\"text\":\"泣月 魔性法杖\",\"flags\":{\"unique\":true}},{\"name\":\"噬魂之牙\",\"type\":\"恐惧之爪\",\"text\":\"噬魂之牙 恐惧之爪\",\"flags\":{\"unique\":true}},{\"name\":\"内布利斯\",\"type\":\"虚影短杖\",\"text\":\"内布利斯 虚影短杖\",\"flags\":{\"unique\":true}},{\"name\":\"尼布洛克\",\"type\":\"梦魇之锤\",\"text\":\"尼布洛克 梦魇之锤\",\"flags\":{\"unique\":true}},{\"name\":\"努葛玛呼之耀\",\"type\":\"深渊巨斧\",\"text\":\"努葛玛呼之耀 深渊巨斧\",\"flags\":{\"unique\":true}},{\"name\":\"虚无之倾\",\"type\":\"游侠弓\",\"text\":\"虚无之倾 游侠弓\",\"flags\":{\"unique\":true}},{\"name\":\"努罗的竖琴\",\"type\":\"先驱者之弓\",\"text\":\"努罗的竖琴 先驱者之弓\",\"flags\":{\"unique\":true}},{\"name\":\"奈可妲之灯\",\"type\":\"水晶短杖\",\"text\":\"奈可妲之灯 水晶短杖\",\"flags\":{\"unique\":true}},{\"name\":\"抹灭\",\"type\":\"魔角法杖\",\"text\":\"抹灭 魔角法杖\",\"flags\":{\"unique\":true}},{\"name\":\"鬼弑\",\"type\":\"查兰之剑\",\"text\":\"鬼弑 查兰之剑\",\"flags\":{\"unique\":true}},{\"name\":\"东之饰\",\"type\":\"裂脏钩\",\"text\":\"东之饰 裂脏钩\",\"flags\":{\"unique\":true}},{\"name\":\"欧罗的贡品\",\"type\":\"狱炎重剑\",\"text\":\"欧罗的贡品 狱炎重剑\",\"flags\":{\"unique\":true}},{\"name\":\"巨击之锤\",\"type\":\"狼牙重锤\",\"text\":\"巨击之锤 狼牙重锤\",\"flags\":{\"unique\":true}},{\"name\":\"悖论\",\"type\":\"瓦尔细剑\",\"text\":\"悖论 瓦尔细剑\",\"flags\":{\"unique\":true}},{\"name\":\"囚神杵\",\"type\":\"长杖\",\"text\":\"囚神杵 长杖\",\"flags\":{\"unique\":true}},{\"name\":\"囚神杵\",\"type\":\"铁锻长杖\",\"text\":\"囚神杵 铁锻长杖\",\"flags\":{\"unique\":true}},{\"name\":\"皮斯卡托的慧眼\",\"type\":\"狂风法杖\",\"text\":\"皮斯卡托的慧眼 狂风法杖\",\"flags\":{\"unique\":true}},{\"name\":\"誓约\",\"type\":\"审判长杖\",\"text\":\"誓约 审判长杖\",\"flags\":{\"unique\":true}},{\"name\":\"虹耀之月\",\"type\":\"暮光长剑\",\"text\":\"虹耀之月 暮光长剑\",\"flags\":{\"unique\":true}},{\"name\":\"重击之锤\",\"type\":\"狼牙重锤\",\"text\":\"重击之锤 狼牙重锤\",\"flags\":{\"unique\":true}},{\"name\":\"威严之刃\",\"type\":\"名贵巨剑\",\"text\":\"威严之刃 名贵巨剑\",\"flags\":{\"unique\":true}},{\"name\":\"逃亡女王\",\"type\":\"名贵巨剑\",\"text\":\"逃亡女王 名贵巨剑\",\"flags\":{\"unique\":true}},{\"name\":\"骤雨之弦\",\"type\":\"短弓\",\"text\":\"骤雨之弦 短弓\",\"flags\":{\"unique\":true}},{\"name\":\"七日锋\",\"type\":\"夜语长剑\",\"text\":\"七日锋 夜语长剑\",\"flags\":{\"unique\":true}},{\"name\":\"议会之触\",\"type\":\"脊弓\",\"text\":\"议会之触 脊弓\",\"flags\":{\"unique\":true}},{\"name\":\"魂界终结\",\"type\":\"铁锻长杖\",\"text\":\"魂界终结 铁锻长杖\",\"flags\":{\"unique\":true}},{\"name\":\"创域\",\"type\":\"铁锻长杖\",\"text\":\"创域 铁锻长杖\",\"flags\":{\"unique\":true}},{\"name\":\"死神的取魂器\",\"type\":\"幽影巨斧\",\"text\":\"死神的取魂器 幽影巨斧\",\"flags\":{\"unique\":true}},{\"name\":\"瓦尔战刃\",\"type\":\"瓦尔军刃\",\"text\":\"瓦尔战刃 瓦尔军刃\",\"flags\":{\"unique\":true}},{\"name\":\"赤红短刃\",\"type\":\"锈剑\",\"text\":\"赤红短刃 锈剑\",\"flags\":{\"unique\":true}},{\"name\":\"灾祸之礁\",\"type\":\"鱼竿\",\"text\":\"灾祸之礁 鱼竿\",\"flags\":{\"unique\":true}},{\"name\":\"无尽愤怒\",\"type\":\"富贵之斧\",\"text\":\"无尽愤怒 富贵之斧\",\"flags\":{\"unique\":true}},{\"name\":\"轮回之记\",\"type\":\"粗制弓\",\"text\":\"轮回之记 粗制弓\",\"flags\":{\"unique\":true}},{\"name\":\"超越壁垒【仿品】\",\"type\":\"裂脏钩\",\"text\":\"超越壁垒【仿品】 裂脏钩\",\"flags\":{\"unique\":true}},{\"name\":\"魅惑【仿品】\",\"type\":\"瓦尔战爪\",\"text\":\"魅惑【仿品】 瓦尔战爪\",\"flags\":{\"unique\":true}},{\"name\":\"苦梦【仿品】\",\"type\":\"影语短杖\",\"text\":\"苦梦【仿品】 影语短杖\",\"flags\":{\"unique\":true}},{\"name\":\"血谑【仿品】\",\"type\":\"锐利刺匕\",\"text\":\"血谑【仿品】 锐利刺匕\",\"flags\":{\"unique\":true}},{\"name\":\"血棘【仿品】\",\"type\":\"朽木之干\",\"text\":\"血棘【仿品】 朽木之干\",\"flags\":{\"unique\":true}},{\"name\":\"寒铁刃【仿品】\",\"type\":\"艾兹麦之匕\",\"text\":\"寒铁刃【仿品】 艾兹麦之匕\",\"flags\":{\"unique\":true}},{\"name\":\"幻梦飞羽【仿品】\",\"type\":\"永恒之剑\",\"text\":\"幻梦飞羽【仿品】 永恒之剑\",\"flags\":{\"unique\":true}},{\"name\":\"宵晓【仿品】\",\"type\":\"风暴长杖\",\"text\":\"宵晓【仿品】 风暴长杖\",\"flags\":{\"unique\":true}},{\"name\":\"叶兰德尔的拥抱【仿品】\",\"type\":\"远古之祭\",\"text\":\"叶兰德尔的拥抱【仿品】 远古之祭\",\"flags\":{\"unique\":true}},{\"name\":\"绿藤【仿品】\",\"type\":\"朽木之干\",\"text\":\"绿藤【仿品】 朽木之干\",\"flags\":{\"unique\":true}},{\"name\":\"霜息【仿品】\",\"type\":\"华丽之锤\",\"text\":\"霜息【仿品】 华丽之锤\",\"flags\":{\"unique\":true}},{\"name\":\"收割者【仿品】\",\"type\":\"灵玉巨斧\",\"text\":\"收割者【仿品】 灵玉巨斧\",\"flags\":{\"unique\":true}},{\"name\":\"裂心刃【仿品】\",\"type\":\"皇家短匕\",\"text\":\"裂心刃【仿品】 皇家短匕\",\"flags\":{\"unique\":true}},{\"name\":\"穿云【仿品】\",\"type\":\"猎魂之弓\",\"text\":\"穿云【仿品】 猎魂之弓\",\"flags\":{\"unique\":true}},{\"name\":\"英斯贝理之极【仿品】\",\"type\":\"智者长剑\",\"text\":\"英斯贝理之极【仿品】 智者长剑\",\"flags\":{\"unique\":true}},{\"name\":\"钢铁指挥【仿品】\",\"type\":\"死亡之弓\",\"text\":\"钢铁指挥【仿品】 死亡之弓\",\"flags\":{\"unique\":true}},{\"name\":\"康戈的战炎【仿品】\",\"type\":\"惧灵重锤\",\"text\":\"康戈的战炎【仿品】 惧灵重锤\",\"flags\":{\"unique\":true}},{\"name\":\"终息【仿品】\",\"type\":\"拳钉\",\"text\":\"终息【仿品】 拳钉\",\"flags\":{\"unique\":true}},{\"name\":\"冥约【仿品】\",\"type\":\"符文法杖\",\"text\":\"冥约【仿品】 符文法杖\",\"flags\":{\"unique\":true}},{\"name\":\"内布利斯【仿品】\",\"type\":\"虚影短杖\",\"text\":\"内布利斯【仿品】 虚影短杖\",\"flags\":{\"unique\":true}},{\"name\":\"欧罗的贡品【仿品】\",\"type\":\"狱炎重剑\",\"text\":\"欧罗的贡品【仿品】 狱炎重剑\",\"flags\":{\"unique\":true}},{\"name\":\"悖论【仿品】\",\"type\":\"瓦尔细剑\",\"text\":\"悖论【仿品】 瓦尔细剑\",\"flags\":{\"unique\":true}},{\"name\":\"骤雨之弦【仿品】\",\"type\":\"短弓\",\"text\":\"骤雨之弦【仿品】 短弓\",\"flags\":{\"unique\":true}},{\"name\":\"开膛斧【仿品】\",\"type\":\"破城斧\",\"text\":\"开膛斧【仿品】 破城斧\",\"flags\":{\"unique\":true}},{\"name\":\"暴风之钢【仿品】\",\"type\":\"征战之剑\",\"text\":\"暴风之钢【仿品】 征战之剑\",\"flags\":{\"unique\":true}},{\"name\":\"裂颅【仿品】\",\"type\":\"刚猛巨锤\",\"text\":\"裂颅【仿品】 刚猛巨锤\",\"flags\":{\"unique\":true}},{\"name\":\"托沃卧【仿品】\",\"type\":\"狂风法杖\",\"text\":\"托沃卧【仿品】 狂风法杖\",\"flags\":{\"unique\":true}},{\"name\":\"峡湾之星【仿品】\",\"type\":\"贤者法杖\",\"text\":\"峡湾之星【仿品】 贤者法杖\",\"flags\":{\"unique\":true}},{\"name\":\"恩吉尔的叉刃【仿品】\",\"type\":\"窃者短刃\",\"text\":\"恩吉尔的叉刃【仿品】 窃者短刃\",\"flags\":{\"unique\":true}},{\"name\":\"裂风【仿品】\",\"type\":\"帝国之弓\",\"text\":\"裂风【仿品】 帝国之弓\",\"flags\":{\"unique\":true}},{\"name\":\"乱世之翼【仿品】\",\"type\":\"艾兹麦巨斧\",\"text\":\"乱世之翼【仿品】 艾兹麦巨斧\",\"flags\":{\"unique\":true}},{\"name\":\"混响\",\"type\":\"螺纹法杖\",\"text\":\"混响 螺纹法杖\",\"flags\":{\"unique\":true}},{\"name\":\"瑞佛的奋战长剑\",\"type\":\"高山重刃\",\"text\":\"瑞佛的奋战长剑 高山重刃\",\"flags\":{\"unique\":true}},{\"name\":\"瑞佛统帅\",\"type\":\"夜语长剑\",\"text\":\"瑞佛统帅 夜语长剑\",\"flags\":{\"unique\":true}},{\"name\":\"野性瑞佛\",\"type\":\"皇家之斧\",\"text\":\"野性瑞佛 皇家之斧\",\"flags\":{\"unique\":true}},{\"name\":\"裂隙\",\"type\":\"恐惧之牙\",\"text\":\"裂隙 恐惧之牙\",\"flags\":{\"unique\":true}},{\"name\":\"萝丝之触\",\"type\":\"反曲弓\",\"text\":\"萝丝之触 反曲弓\",\"flags\":{\"unique\":true}},{\"name\":\"霜刃\",\"type\":\"锈斧\",\"text\":\"霜刃 锈斧\",\"flags\":{\"unique\":true}},{\"name\":\"雀跃\",\"type\":\"剥皮刀\",\"text\":\"雀跃 剥皮刀\",\"flags\":{\"unique\":true}},{\"name\":\"食蚜\",\"type\":\"斗士长剑\",\"text\":\"食蚜 斗士长剑\",\"flags\":{\"unique\":true}},{\"name\":\"命运伤痕\",\"type\":\"拳钉\",\"text\":\"命运伤痕 拳钉\",\"flags\":{\"unique\":true}},{\"name\":\"离异梦寐\",\"type\":\"军用长刃\",\"text\":\"离异梦寐 军用长刃\",\"flags\":{\"unique\":true}},{\"name\":\"日耀之影\",\"type\":\"贤者法杖\",\"text\":\"日耀之影 贤者法杖\",\"flags\":{\"unique\":true}},{\"name\":\"低伏暗光\",\"type\":\"狂风法杖\",\"text\":\"低伏暗光 狂风法杖\",\"flags\":{\"unique\":true}},{\"name\":\"寒光剑\",\"type\":\"重剑\",\"text\":\"寒光剑 重剑\",\"flags\":{\"unique\":true}},{\"name\":\"罪恶吞噬者的叹息\",\"type\":\"暴君之统\",\"text\":\"罪恶吞噬者的叹息 暴君之统\",\"flags\":{\"unique\":true}},{\"name\":\"银枝\",\"type\":\"粗制弓\",\"text\":\"银枝 粗制弓\",\"flags\":{\"unique\":true}},{\"name\":\"幻银之弦\",\"type\":\"粗制弓\",\"text\":\"幻银之弦 粗制弓\",\"flags\":{\"unique\":true}},{\"name\":\"奇异\",\"type\":\"白金短杖\",\"text\":\"奇异 白金短杖\",\"flags\":{\"unique\":true}},{\"name\":\"勇气之魄\",\"type\":\"艾兹麦巨斧\",\"text\":\"勇气之魄 艾兹麦巨斧\",\"flags\":{\"unique\":true}},{\"name\":\"破碎传承者\",\"type\":\"蛇纹长杖\",\"text\":\"破碎传承者 蛇纹长杖\",\"flags\":{\"unique\":true}},{\"name\":\"白银之舌\",\"type\":\"先驱者之弓\",\"text\":\"白银之舌 先驱者之弓\",\"flags\":{\"unique\":true}},{\"name\":\"海妖魅曲\",\"type\":\"鱼竿\",\"text\":\"海妖魅曲 鱼竿\",\"flags\":{\"unique\":true}},{\"name\":\"开膛斧\",\"type\":\"破城斧\",\"text\":\"开膛斧 破城斧\",\"flags\":{\"unique\":true}},{\"name\":\"扭魂者\",\"type\":\"艾兹麦长杖\",\"text\":\"扭魂者 艾兹麦长杖\",\"flags\":{\"unique\":true}},{\"name\":\"先驱之脊\",\"type\":\"冷铁短杖\",\"text\":\"先驱之脊 冷铁短杖\",\"flags\":{\"unique\":true}},{\"name\":\"碎月\",\"type\":\"朽木法杖\",\"text\":\"碎月 朽木法杖\",\"flags\":{\"unique\":true}},{\"name\":\"塑星者\",\"type\":\"狱炎重剑\",\"text\":\"塑星者 狱炎重剑\",\"flags\":{\"unique\":true}},{\"name\":\"暴雨之弦\",\"type\":\"长弓\",\"text\":\"暴雨之弦 长弓\",\"flags\":{\"unique\":true}},{\"name\":\"禁锢暴风\",\"type\":\"粗纹法杖\",\"text\":\"禁锢暴风 粗纹法杖\",\"flags\":{\"unique\":true}},{\"name\":\"瓦尔传说\",\"type\":\"碧铜短剑\",\"text\":\"瓦尔传说 碧铜短剑\",\"flags\":{\"unique\":true}},{\"name\":\"砥砺深根\",\"type\":\"伏击刺刃\",\"text\":\"砥砺深根 伏击刺刃\",\"flags\":{\"unique\":true}},{\"name\":\"塔林的颤栗之语\",\"type\":\"风暴长杖\",\"text\":\"塔林的颤栗之语 风暴长杖\",\"flags\":{\"unique\":true}},{\"name\":\"行刑之刃\",\"type\":\"虎牙巨剑\",\"text\":\"行刑之刃 虎牙巨剑\",\"flags\":{\"unique\":true}},{\"name\":\"漆黑藤杖\",\"type\":\"皇家短杖\",\"text\":\"漆黑藤杖 皇家短杖\",\"flags\":{\"unique\":true}},{\"name\":\"竭血之镰\",\"type\":\"行刑巨斧\",\"text\":\"竭血之镰 行刑巨斧\",\"flags\":{\"unique\":true}},{\"name\":\"血棘\",\"type\":\"朽木之干\",\"text\":\"血棘 朽木之干\",\"flags\":{\"unique\":true}},{\"name\":\"烧灼器\",\"type\":\"柴斧\",\"text\":\"烧灼器 柴斧\",\"flags\":{\"unique\":true}},{\"name\":\"蚕食之暗\",\"type\":\"兽血短匕\",\"text\":\"蚕食之暗 兽血短匕\",\"flags\":{\"unique\":true}},{\"name\":\"赤红风暴\",\"type\":\"钢木之弓\",\"text\":\"赤红风暴 钢木之弓\",\"flags\":{\"unique\":true}},{\"name\":\"禅意苦行僧\",\"type\":\"残暴巨剑\",\"text\":\"禅意苦行僧 残暴巨剑\",\"flags\":{\"unique\":true}},{\"name\":\"杀孽戒刀\",\"type\":\"残暴巨剑\",\"text\":\"杀孽戒刀 残暴巨剑\",\"flags\":{\"unique\":true}},{\"name\":\"黯黑贤者\",\"type\":\"影语短杖\",\"text\":\"黯黑贤者 影语短杖\",\"flags\":{\"unique\":true}},{\"name\":\"圣恶之喻\",\"type\":\"帝国长杖\",\"text\":\"圣恶之喻 帝国长杖\",\"flags\":{\"unique\":true}},{\"name\":\"支点\",\"type\":\"艾兹麦长杖\",\"text\":\"支点 艾兹麦长杖\",\"flags\":{\"unique\":true}},{\"name\":\"女神的灵缚\",\"type\":\"鲸骨细刃\",\"text\":\"女神的灵缚 鲸骨细刃\",\"flags\":{\"unique\":true}},{\"name\":\"女神的怒炎\",\"type\":\"贵族之剑\",\"text\":\"女神的怒炎 贵族之剑\",\"flags\":{\"unique\":true}},{\"name\":\"女神的束缚\",\"type\":\"永恒之剑\",\"text\":\"女神的束缚 永恒之剑\",\"flags\":{\"unique\":true}},{\"name\":\"灰色尖椎\",\"type\":\"审判长杖\",\"text\":\"灰色尖椎 审判长杖\",\"flags\":{\"unique\":true}},{\"name\":\"狮鹫\",\"type\":\"碎玉斧\",\"text\":\"狮鹫 碎玉斧\",\"flags\":{\"unique\":true}},{\"name\":\"收割者\",\"type\":\"灵玉巨斧\",\"text\":\"收割者 灵玉巨斧\",\"flags\":{\"unique\":true}},{\"name\":\"隐匿之刃\",\"type\":\"伏击刺刃\",\"text\":\"隐匿之刃 伏击刺刃\",\"flags\":{\"unique\":true}},{\"name\":\"钢铁质量\",\"type\":\"斗士长剑\",\"text\":\"钢铁质量 斗士长剑\",\"flags\":{\"unique\":true}},{\"name\":\"诗人之笔\",\"type\":\"粗纹法杖\",\"text\":\"诗人之笔 粗纹法杖\",\"flags\":{\"unique\":true}},{\"name\":\"女爵\",\"type\":\"旧军刀\",\"text\":\"女爵 旧军刀\",\"flags\":{\"unique\":true}},{\"name\":\"秘法君临\",\"type\":\"军团长剑\",\"text\":\"秘法君临 军团长剑\",\"flags\":{\"unique\":true}},{\"name\":\"救世者\",\"type\":\"军团长剑\",\"text\":\"救世者 军团长剑\",\"flags\":{\"unique\":true}},{\"name\":\"灾害\",\"type\":\"恐惧之牙\",\"text\":\"灾害 恐惧之牙\",\"flags\":{\"unique\":true}},{\"name\":\"鹰啸\",\"type\":\"碎玉斧\",\"text\":\"鹰啸 碎玉斧\",\"flags\":{\"unique\":true}},{\"name\":\"炽炎之使\",\"type\":\"武术长杖\",\"text\":\"炽炎之使 武术长杖\",\"flags\":{\"unique\":true}},{\"name\":\"风暴之眼\",\"type\":\"皇家长杖\",\"text\":\"风暴之眼 皇家长杖\",\"flags\":{\"unique\":true}},{\"name\":\"风暴坚壁\",\"type\":\"皇家长杖\",\"text\":\"风暴坚壁 皇家长杖\",\"flags\":{\"unique\":true}},{\"name\":\"无上箴言\",\"type\":\"水晶短杖\",\"text\":\"无上箴言 水晶短杖\",\"flags\":{\"unique\":true}},{\"name\":\"思想奔流\",\"type\":\"军团长剑\",\"text\":\"思想奔流 军团长剑\",\"flags\":{\"unique\":true}},{\"name\":\"暴风雨\",\"type\":\"长弓\",\"text\":\"暴风雨 长弓\",\"flags\":{\"unique\":true}},{\"name\":\"暴风之钢\",\"type\":\"征战之剑\",\"text\":\"暴风之钢 征战之剑\",\"flags\":{\"unique\":true}},{\"name\":\"蜂巢涌动\",\"type\":\"刺喉刃\",\"text\":\"蜂巢涌动 刺喉刃\",\"flags\":{\"unique\":true}},{\"name\":\"冰点低语\",\"type\":\"毒牙长杖\",\"text\":\"冰点低语 毒牙长杖\",\"flags\":{\"unique\":true}},{\"name\":\"死亡屈服\",\"type\":\"帝国长杖\",\"text\":\"死亡屈服 帝国长杖\",\"flags\":{\"unique\":true}},{\"name\":\"曙雷\",\"type\":\"锈斑巨剑\",\"text\":\"曙雷 锈斑巨剑\",\"flags\":{\"unique\":true}},{\"name\":\"局势逆转者\",\"type\":\"帝国重锤\",\"text\":\"局势逆转者 帝国重锤\",\"flags\":{\"unique\":true}},{\"name\":\"痛苦之触\",\"type\":\"帝国战爪\",\"text\":\"痛苦之触 帝国战爪\",\"flags\":{\"unique\":true}},{\"name\":\"颤抖之杖\",\"type\":\"军用长杖\",\"text\":\"颤抖之杖 军用长杖\",\"flags\":{\"unique\":true}},{\"name\":\"裂颅\",\"type\":\"刚猛巨锤\",\"text\":\"裂颅 刚猛巨锤\",\"flags\":{\"unique\":true}},{\"name\":\"托沃崩\",\"type\":\"螺纹法杖\",\"text\":\"托沃崩 螺纹法杖\",\"flags\":{\"unique\":true}},{\"name\":\"托沃卧\",\"type\":\"狂风法杖\",\"text\":\"托沃卧 狂风法杖\",\"flags\":{\"unique\":true}},{\"name\":\"峡湾之星\",\"type\":\"贤者法杖\",\"text\":\"峡湾之星 贤者法杖\",\"flags\":{\"unique\":true}},{\"name\":\"恩吉尔的叉刃\",\"type\":\"窃者短刃\",\"text\":\"恩吉尔的叉刃 窃者短刃\",\"flags\":{\"unique\":true}},{\"name\":\"合流梦寐\",\"type\":\"军用长刃\",\"text\":\"合流梦寐 军用长刃\",\"flags\":{\"unique\":true}},{\"name\":\"乌尔尼多的拥抱\",\"type\":\"瓦尔巨斧\",\"text\":\"乌尔尼多的拥抱 瓦尔巨斧\",\"flags\":{\"unique\":true}},{\"name\":\"乌尔尼多之吻\",\"type\":\"双影巨斧\",\"text\":\"乌尔尼多之吻 双影巨斧\",\"flags\":{\"unique\":true}},{\"name\":\"黑鲨\",\"type\":\"瓦尔军刃\",\"text\":\"黑鲨 瓦尔军刃\",\"flags\":{\"unique\":true}},{\"name\":\"庄重之杖\",\"type\":\"朽木之干\",\"text\":\"庄重之杖 朽木之干\",\"flags\":{\"unique\":true}},{\"name\":\"潜能魔棒\",\"type\":\"箴言法杖\",\"text\":\"潜能魔棒 箴言法杖\",\"flags\":{\"unique\":true}},{\"name\":\"裂空者\",\"type\":\"狱炎重剑\",\"text\":\"裂空者 狱炎重剑\",\"flags\":{\"unique\":true}},{\"name\":\"逝空之锤\",\"type\":\"威权巨锤\",\"text\":\"逝空之锤 威权巨锤\",\"flags\":{\"unique\":true}},{\"name\":\"魔暴之痕\",\"type\":\"脊弓\",\"text\":\"魔暴之痕 脊弓\",\"flags\":{\"unique\":true}},{\"name\":\"火神锻台\",\"type\":\"魔灵短匕\",\"text\":\"火神锻台 魔灵短匕\",\"flags\":{\"unique\":true}},{\"name\":\"白净之风\",\"type\":\"帝国短匕\",\"text\":\"白净之风 帝国短匕\",\"flags\":{\"unique\":true}},{\"name\":\"阔斩\",\"type\":\"巨战斧\",\"text\":\"阔斩 巨战斧\",\"flags\":{\"unique\":true}},{\"name\":\"寡妇\",\"type\":\"窃者短匕\",\"text\":\"寡妇 窃者短匕\",\"flags\":{\"unique\":true}},{\"name\":\"野性狂爪\",\"type\":\"凿钉\",\"text\":\"野性狂爪 凿钉\",\"flags\":{\"unique\":true}},{\"name\":\"裂风\",\"type\":\"帝国之弓\",\"text\":\"裂风 帝国之弓\",\"flags\":{\"unique\":true}},{\"name\":\"乱世之翼\",\"type\":\"裂甲巨斧\",\"text\":\"乱世之翼 裂甲巨斧\",\"flags\":{\"unique\":true}},{\"name\":\"乱世之翼\",\"type\":\"艾兹麦巨斧\",\"text\":\"乱世之翼 艾兹麦巨斧\",\"flags\":{\"unique\":true}},{\"name\":\"猎巫人的审判\",\"type\":\"贵族长杖\",\"text\":\"猎巫人的审判 贵族长杖\",\"flags\":{\"unique\":true}},{\"name\":\"舍吉的手柄\",\"type\":\"强化长杖\",\"text\":\"舍吉的手柄 强化长杖\",\"flags\":{\"unique\":true}},{\"name\":\"索伏的始源\",\"type\":\"骨制弓\",\"text\":\"索伏的始源 骨制弓\",\"flags\":{\"unique\":true}},{\"name\":\"索伏的爱抚\",\"type\":\"城塞战弓\",\"text\":\"索伏的爱抚 城塞战弓\",\"flags\":{\"unique\":true}}]\n";
        } else if (itemTag.equals("传奇护甲1")) {
            return "[{\"name\":\"地动\",\"type\":\"羊皮短靴\",\"text\":\"地动 羊皮短靴\",\"flags\":{\"unique\":true}},{\"name\":\"水火不容\",\"type\":\"伏击护手\",\"text\":\"水火不容 伏击护手\",\"flags\":{\"unique\":true}},{\"name\":\"深渊之唤\",\"type\":\"艾兹麦坚盔\",\"text\":\"深渊之唤 艾兹麦坚盔\",\"flags\":{\"unique\":true}},{\"name\":\"幻芒圣盾\",\"type\":\"斗士鸢盾\",\"text\":\"幻芒圣盾 斗士鸢盾\",\"flags\":{\"unique\":true}},{\"name\":\"汉恩的蔑视\",\"type\":\"领主战冠\",\"text\":\"汉恩的蔑视 领主战冠\",\"flags\":{\"unique\":true}},{\"name\":\"汉恩的遗产\",\"type\":\"巨型塔盾\",\"text\":\"汉恩的遗产 巨型塔盾\",\"flags\":{\"unique\":true}},{\"name\":\"阿尔贝隆的征途\",\"type\":\"战士之靴\",\"text\":\"阿尔贝隆的征途 战士之靴\",\"flags\":{\"unique\":true}},{\"name\":\"尸僵\",\"type\":\"禁礼护手\",\"text\":\"尸僵 禁礼护手\",\"flags\":{\"unique\":true}},{\"name\":\"相生相克\",\"type\":\"术士手套\",\"text\":\"相生相克 术士手套\",\"flags\":{\"unique\":true}},{\"name\":\"极地之眼\",\"type\":\"罪者之帽\",\"text\":\"极地之眼 罪者之帽\",\"flags\":{\"unique\":true}},{\"name\":\"安姆布的战甲\",\"type\":\"圣战锁甲\",\"text\":\"安姆布的战甲 圣战锁甲\",\"flags\":{\"unique\":true}},{\"name\":\"艾普之梦\",\"type\":\"远古魔盾\",\"text\":\"艾普之梦 远古魔盾\",\"flags\":{\"unique\":true}},{\"name\":\"艾普的霸权\",\"type\":\"瓦尔魔盾\",\"text\":\"艾普的霸权 瓦尔魔盾\",\"flags\":{\"unique\":true}},{\"name\":\"建筑师之手\",\"type\":\"扣环护手\",\"text\":\"建筑师之手 扣环护手\",\"flags\":{\"unique\":true}},{\"name\":\"安赛娜丝的优雅之歌\",\"type\":\"铁锻之冠\",\"text\":\"安赛娜丝的优雅之歌 铁锻之冠\",\"flags\":{\"unique\":true}},{\"name\":\"安赛娜丝的安抚之语\",\"type\":\"丝绸手套\",\"text\":\"安赛娜丝的安抚之语 丝绸手套\",\"flags\":{\"unique\":true}},{\"name\":\"安赛娜丝的迅敏之冠\",\"type\":\"铁锻之冠\",\"text\":\"安赛娜丝的迅敏之冠 铁锻之冠\",\"flags\":{\"unique\":true}},{\"name\":\"拂烬\",\"type\":\"鹿皮外套\",\"text\":\"拂烬 鹿皮外套\",\"flags\":{\"unique\":true}},{\"name\":\"冰灵之吼\",\"type\":\"双锋箭袋\",\"text\":\"冰灵之吼 双锋箭袋\",\"flags\":{\"unique\":true}},{\"name\":\"千里狙敌\",\"type\":\"罪者之帽\",\"text\":\"千里狙敌 罪者之帽\",\"flags\":{\"unique\":true}},{\"name\":\"阿兹里的捷思\",\"type\":\"瓦尔护手\",\"text\":\"阿兹里的捷思 瓦尔护手\",\"flags\":{\"unique\":true}},{\"name\":\"阿兹里之镜\",\"type\":\"金阳轻盾\",\"text\":\"阿兹里之镜 金阳轻盾\",\"flags\":{\"unique\":true}},{\"name\":\"阿兹里的反击\",\"type\":\"金阳轻盾\",\"text\":\"阿兹里的反击 金阳轻盾\",\"flags\":{\"unique\":true}},{\"name\":\"阿兹里的威权\",\"type\":\"祭礼束衣\",\"text\":\"阿兹里的威权 祭礼束衣\",\"flags\":{\"unique\":true}},{\"name\":\"阿兹里的金履\",\"type\":\"迷踪短靴\",\"text\":\"阿兹里的金履 迷踪短靴\",\"flags\":{\"unique\":true}},{\"name\":\"奥库娜的意志\",\"type\":\"环带护手\",\"text\":\"奥库娜的意志 环带护手\",\"flags\":{\"unique\":true}},{\"name\":\"富贵之运\",\"type\":\"钢影护手\",\"text\":\"富贵之运 钢影护手\",\"flags\":{\"unique\":true}},{\"name\":\"疯狂的象征\",\"type\":\"异色鞋(冰闪)\",\"text\":\"疯狂的象征 异色鞋(冰闪)\",\"flags\":{\"unique\":true}},{\"name\":\"疯狂的象征\",\"type\":\"异色鞋 (火冰)\",\"text\":\"疯狂的象征 异色鞋 (火冰)\",\"flags\":{\"unique\":true}},{\"name\":\"疯狂的象征\",\"type\":\"异色鞋 (火闪)\",\"text\":\"疯狂的象征 异色鞋 (火闪)\",\"flags\":{\"unique\":true}},{\"name\":\"兽腹\",\"type\":\"连身龙鳞战甲\",\"text\":\"兽腹 连身龙鳞战甲\",\"flags\":{\"unique\":true}},{\"name\":\"苦痛之处\",\"type\":\"巨人魔盾\",\"text\":\"苦痛之处 巨人魔盾\",\"flags\":{\"unique\":true}},{\"name\":\"黑炎之芒\",\"type\":\"火焰箭袋\",\"text\":\"黑炎之芒 火焰箭袋\",\"flags\":{\"unique\":true}},{\"name\":\"黑炎之芒\",\"type\":\"火灵箭袋\",\"text\":\"黑炎之芒 火灵箭袋\",\"flags\":{\"unique\":true}},{\"name\":\"黑阳之冠\",\"type\":\"漆彩之盔\",\"text\":\"黑阳之冠 漆彩之盔\",\"flags\":{\"unique\":true}},{\"name\":\"亵渎者之握\",\"type\":\"暗影护手\",\"text\":\"亵渎者之握 暗影护手\",\"flags\":{\"unique\":true}},{\"name\":\"血脉相连\",\"type\":\"骨制战甲\",\"text\":\"血脉相连 骨制战甲\",\"flags\":{\"unique\":true}},{\"name\":\"蹒跚巨兽\",\"type\":\"星芒战铠\",\"text\":\"蹒跚巨兽 星芒战铠\",\"flags\":{\"unique\":true}},{\"name\":\"灵骸之履\",\"type\":\"丝绸便鞋\",\"text\":\"灵骸之履 丝绸便鞋\",\"flags\":{\"unique\":true}},{\"name\":\"刺棘宝甲\",\"type\":\"铁制背心\",\"text\":\"刺棘宝甲 铁制背心\",\"flags\":{\"unique\":true}},{\"name\":\"扼息者\",\"type\":\"火蝮鳞手套\",\"text\":\"扼息者 火蝮鳞手套\",\"flags\":{\"unique\":true}},{\"name\":\"布琳洛特之旗\",\"type\":\"环形魔盾\",\"text\":\"布琳洛特之旗 环形魔盾\",\"flags\":{\"unique\":true}},{\"name\":\"布琳洛特岸行者\",\"type\":\"猎人之靴\",\"text\":\"布琳洛特岸行者 猎人之靴\",\"flags\":{\"unique\":true}},{\"name\":\"荒途\",\"type\":\"扣环皮甲\",\"text\":\"荒途 扣环皮甲\",\"flags\":{\"unique\":true}},{\"name\":\"破碎信念\",\"type\":\"威能鸢盾\",\"text\":\"破碎信念 威能鸢盾\",\"flags\":{\"unique\":true}},{\"name\":\"布隆的影衣\",\"type\":\"死神之装\",\"text\":\"布隆的影衣 死神之装\",\"flags\":{\"unique\":true}},{\"name\":\"鼠疫之源\",\"type\":\"暗影者长靴\",\"text\":\"鼠疫之源 暗影者长靴\",\"flags\":{\"unique\":true}},{\"name\":\"致命之体\",\"type\":\"映彩外套\",\"text\":\"致命之体 映彩外套\",\"flags\":{\"unique\":true}},{\"name\":\"将军的复生\",\"type\":\"圣洁锁甲\",\"text\":\"将军的复生 圣洁锁甲\",\"flags\":{\"unique\":true}},{\"name\":\"恐惧之缶\",\"type\":\"战争轻盾\",\"text\":\"恐惧之缶 战争轻盾\",\"flags\":{\"unique\":true}},{\"name\":\"五芒屏障\",\"type\":\"乌木塔盾\",\"text\":\"五芒屏障 乌木塔盾\",\"flags\":{\"unique\":true}},{\"name\":\"薛鲁宾的恶作剧\",\"type\":\"胜利盔甲\",\"text\":\"薛鲁宾的恶作剧 胜利盔甲\",\"flags\":{\"unique\":true}},{\"name\":\"切特斯的威权\",\"type\":\"操灵者之冠\",\"text\":\"切特斯的威权 操灵者之冠\",\"flags\":{\"unique\":true}},{\"name\":\"卫道之袍\",\"type\":\"漆彩束衣\",\"text\":\"卫道之袍 漆彩束衣\",\"flags\":{\"unique\":true}},{\"name\":\"烈炎之袍\",\"type\":\"学者之袍\",\"text\":\"烈炎之袍 学者之袍\",\"flags\":{\"unique\":true}},{\"name\":\"塔温的披风\",\"type\":\"智者之袍\",\"text\":\"塔温的披风 智者之袍\",\"flags\":{\"unique\":true}},{\"name\":\"巨坑之令\",\"type\":\"仪式手套\",\"text\":\"巨坑之令 仪式手套\",\"flags\":{\"unique\":true}},{\"name\":\"灵枢行者\",\"type\":\"禁礼之靴\",\"text\":\"灵枢行者 禁礼之靴\",\"flags\":{\"unique\":true}},{\"name\":\"卡斯普里意志\",\"type\":\"暗影之装\",\"text\":\"卡斯普里意志 暗影之装\",\"flags\":{\"unique\":true}},{\"name\":\"嗜雷之冠\",\"type\":\"日耀之冠\",\"text\":\"嗜雷之冠 日耀之冠\",\"flags\":{\"unique\":true}},{\"name\":\"嗜寒之冠\",\"type\":\"绸缎之兜\",\"text\":\"嗜寒之冠 绸缎之兜\",\"flags\":{\"unique\":true}},{\"name\":\"嗜火之冠\",\"type\":\"艾兹麦坚盔\",\"text\":\"嗜火之冠 艾兹麦坚盔\",\"flags\":{\"unique\":true}},{\"name\":\"落石\",\"type\":\"锯齿箭袋\",\"text\":\"落石 锯齿箭袋\",\"flags\":{\"unique\":true}},{\"name\":\"岩头\",\"type\":\"锯齿箭袋\",\"text\":\"岩头 锯齿箭袋\",\"flags\":{\"unique\":true}},{\"name\":\"深海魔甲\",\"type\":\"金耀之铠\",\"text\":\"深海魔甲 金耀之铠\",\"flags\":{\"unique\":true}},{\"name\":\"深海魔角\",\"type\":\"行政者战冠\",\"text\":\"深海魔角 行政者战冠\",\"flags\":{\"unique\":true}},{\"name\":\"深海魔钳\",\"type\":\"巨人护手\",\"text\":\"深海魔钳 巨人护手\",\"flags\":{\"unique\":true}},{\"name\":\"深海魔足\",\"type\":\"巨灵胫甲\",\"text\":\"深海魔足 巨灵胫甲\",\"flags\":{\"unique\":true}},{\"name\":\"渴望之冠\",\"type\":\"全罩战盔\",\"text\":\"渴望之冠 全罩战盔\",\"flags\":{\"unique\":true}},{\"name\":\"普兰德斯之徽\",\"type\":\"松木轻盾\",\"text\":\"普兰德斯之徽 松木轻盾\",\"flags\":{\"unique\":true}},{\"name\":\"邪眼之冠\",\"type\":\"灵主之环\",\"text\":\"邪眼之冠 灵主之环\",\"flags\":{\"unique\":true}},{\"name\":\"内省之冠\",\"type\":\"箴言战冠\",\"text\":\"内省之冠 箴言战冠\",\"flags\":{\"unique\":true}},{\"name\":\"殒皇之冠\",\"type\":\"弑君之面\",\"text\":\"殒皇之冠 弑君之面\",\"flags\":{\"unique\":true}},{\"name\":\"暴君王冠\",\"type\":\"行政者战冠\",\"text\":\"暴君王冠 行政者战冠\",\"flags\":{\"unique\":true}},{\"name\":\"刺棘之冠\",\"type\":\"藤蔓之冠\",\"text\":\"刺棘之冠 藤蔓之冠\",\"flags\":{\"unique\":true}},{\"name\":\"水晶墓穴\",\"type\":\"精制环甲\",\"text\":\"水晶墓穴 精制环甲\",\"flags\":{\"unique\":true}},{\"name\":\"夜幕召唤\",\"type\":\"疫灾之面\",\"text\":\"夜幕召唤 疫灾之面\",\"flags\":{\"unique\":true}},{\"name\":\"献祭舞鞋\",\"type\":\"缚足长靴\",\"text\":\"献祭舞鞋 缚足长靴\",\"flags\":{\"unique\":true}},{\"name\":\"德瑞索的勇者之盾\",\"type\":\"古代圆盾\",\"text\":\"德瑞索的勇者之盾 古代圆盾\",\"flags\":{\"unique\":true}},{\"name\":\"德瑞索的卫衣\",\"type\":\"连身龙鳞铠\",\"text\":\"德瑞索的卫衣 连身龙鳞铠\",\"flags\":{\"unique\":true}},{\"name\":\"暗雷\",\"type\":\"龙鳞长靴\",\"text\":\"暗雷 龙鳞长靴\",\"flags\":{\"unique\":true}},{\"name\":\"死亡大门\",\"type\":\"圣战长靴\",\"text\":\"死亡大门 圣战长靴\",\"flags\":{\"unique\":true}},{\"name\":\"冥使之体\",\"type\":\"星芒战铠\",\"text\":\"冥使之体 星芒战铠\",\"flags\":{\"unique\":true}},{\"name\":\"猎踪\",\"type\":\"鹿皮短靴\",\"text\":\"猎踪 鹿皮短靴\",\"flags\":{\"unique\":true}},{\"name\":\"丧钟\",\"type\":\"金面护盔\",\"text\":\"丧钟 金面护盔\",\"flags\":{\"unique\":true}},{\"name\":\"丧吼\",\"type\":\"金面护盔\",\"text\":\"丧吼 金面护盔\",\"flags\":{\"unique\":true}},{\"name\":\"升华统御\",\"type\":\"黄金战甲\",\"text\":\"升华统御 黄金战甲\",\"flags\":{\"unique\":true}},{\"name\":\"半神的不朽\",\"type\":\"金色面具\",\"text\":\"半神的不朽 金色面具\",\"flags\":{\"unique\":true}},{\"name\":\"恶魔缝补者\",\"type\":\"缎布手套\",\"text\":\"恶魔缝补者 缎布手套\",\"flags\":{\"unique\":true}},{\"name\":\"箭丽毒蛙\",\"type\":\"哨兵之衣\",\"text\":\"箭丽毒蛙 哨兵之衣\",\"flags\":{\"unique\":true}},{\"name\":\"德沃托的信念之盔\",\"type\":\"梦魇战盔\",\"text\":\"德沃托的信念之盔 梦魇战盔\",\"flags\":{\"unique\":true}},{\"name\":\"无悔之爱\",\"type\":\"贤者之袍\",\"text\":\"无悔之爱 贤者之袍\",\"flags\":{\"unique\":true}},{\"name\":\"德瑞的恶念\",\"type\":\"丝绒手套\",\"text\":\"德瑞的恶念 丝绒手套\",\"flags\":{\"unique\":true}},{\"name\":\"德瑞的蔑视\",\"type\":\"月影之冠\",\"text\":\"德瑞的蔑视 月影之冠\",\"flags\":{\"unique\":true}},{\"name\":\"德瑞之肤\",\"type\":\"毒蛛丝之袍\",\"text\":\"德瑞之肤 毒蛛丝之袍\",\"flags\":{\"unique\":true}},{\"name\":\"德瑞的精神手套\",\"type\":\"丝绒手套\",\"text\":\"德瑞的精神手套 丝绒手套\",\"flags\":{\"unique\":true}},{\"name\":\"多利亚尼的幻想\",\"type\":\"迷踪短靴\",\"text\":\"多利亚尼的幻想 迷踪短靴\",\"flags\":{\"unique\":true}},{\"name\":\"多利亚尼的幻想\",\"type\":\"术士长靴\",\"text\":\"多利亚尼的幻想 术士长靴\",\"flags\":{\"unique\":true}},{\"name\":\"多利亚尼的幻想\",\"type\":\"巨人胫甲\",\"text\":\"多利亚尼的幻想 巨人胫甲\",\"flags\":{\"unique\":true}},{\"name\":\"多里亚尼之拳\",\"type\":\"瓦尔护手\",\"text\":\"多里亚尼之拳 瓦尔护手\",\"flags\":{\"unique\":true}},{\"name\":\"多里亚尼的试作甲\",\"type\":\"圣者链甲\",\"text\":\"多里亚尼的试作甲 圣者链甲\",\"flags\":{\"unique\":true}},{\"name\":\"穿心\",\"type\":\"穿射箭袋\",\"text\":\"穿心 穿射箭袋\",\"flags\":{\"unique\":true}},{\"name\":\"幽暗凋零\",\"type\":\"铁影长靴\",\"text\":\"幽暗凋零 铁影长靴\",\"flags\":{\"unique\":true}},{\"name\":\"迎暮\",\"type\":\"铁影长靴\",\"text\":\"迎暮 铁影长靴\",\"flags\":{\"unique\":true}},{\"name\":\"希伯的霸权\",\"type\":\"灵主之环\",\"text\":\"希伯的霸权 灵主之环\",\"flags\":{\"unique\":true}},{\"name\":\"皇帝的警戒\",\"type\":\"冷钢鸢盾\",\"text\":\"皇帝的警戒 冷钢鸢盾\",\"flags\":{\"unique\":true}},{\"name\":\"帝国之掌\",\"type\":\"巨灵护手\",\"text\":\"帝国之掌 巨灵护手\",\"flags\":{\"unique\":true}},{\"name\":\"艾许之镜\",\"type\":\"暗金魔盾\",\"text\":\"艾许之镜 暗金魔盾\",\"flags\":{\"unique\":true}},{\"name\":\"艾许之面\",\"type\":\"瓦尔魔盾\",\"text\":\"艾许之面 瓦尔魔盾\",\"flags\":{\"unique\":true}},{\"name\":\"远征尽头\",\"type\":\"哨兵之衣\",\"text\":\"远征尽头 哨兵之衣\",\"flags\":{\"unique\":true}},{\"name\":\"怨恨之眼\",\"type\":\"无情之面\",\"text\":\"怨恨之眼 无情之面\",\"flags\":{\"unique\":true}},{\"name\":\"艾兹麦之握\",\"type\":\"粗铁盔\",\"text\":\"艾兹麦之握 粗铁盔\",\"flags\":{\"unique\":true}},{\"name\":\"艾兹麦的荣光\",\"type\":\"粗铁盔\",\"text\":\"艾兹麦的荣光 粗铁盔\",\"flags\":{\"unique\":true}},{\"name\":\"毁面者\",\"type\":\"扣环护手\",\"text\":\"毁面者 扣环护手\",\"flags\":{\"unique\":true}},{\"name\":\"费尔之帽\",\"type\":\"三角帽\",\"text\":\"费尔之帽 三角帽\",\"flags\":{\"unique\":true}},{\"name\":\"大地之牙\",\"type\":\"鹰喙之面\",\"text\":\"大地之牙 鹰喙之面\",\"flags\":{\"unique\":true}},{\"name\":\"大地之痕\",\"type\":\"迷踪短靴\",\"text\":\"大地之痕 迷踪短靴\",\"flags\":{\"unique\":true}},{\"name\":\"大地之护\",\"type\":\"胜利盔甲\",\"text\":\"大地之护 胜利盔甲\",\"flags\":{\"unique\":true}},{\"name\":\"大地之握\",\"type\":\"火蝮鳞手套\",\"text\":\"大地之握 火蝮鳞手套\",\"flags\":{\"unique\":true}},{\"name\":\"暗夜弑衣\",\"type\":\"毒蛛丝之袍\",\"text\":\"暗夜弑衣 毒蛛丝之袍\",\"flags\":{\"unique\":true}},{\"name\":\"暗夜刺足\",\"type\":\"暗影之靴\",\"text\":\"暗夜刺足 暗影之靴\",\"flags\":{\"unique\":true}},{\"name\":\"暗夜毒牙\",\"type\":\"操灵者之冠\",\"text\":\"暗夜毒牙 操灵者之冠\",\"flags\":{\"unique\":true}},{\"name\":\"暗夜织礼\",\"type\":\"禁礼护手\",\"text\":\"暗夜织礼 禁礼护手\",\"flags\":{\"unique\":true}},{\"name\":\"卓识\",\"type\":\"日耀之冠\",\"text\":\"卓识 日耀之冠\",\"flags\":{\"unique\":true}},{\"name\":\"肉体与魂灵\",\"type\":\"铁影手套\",\"text\":\"肉体与魂灵 铁影手套\",\"flags\":{\"unique\":true}},{\"name\":\"塑血巨匠\",\"type\":\"操灵者背心\",\"text\":\"塑血巨匠 操灵者背心\",\"flags\":{\"unique\":true}},{\"name\":\"雷鸣洗礼\",\"type\":\"钢镜刺盾\",\"text\":\"雷鸣洗礼 钢镜刺盾\",\"flags\":{\"unique\":true}},{\"name\":\"禁断的军帽\",\"type\":\"强化巨盔\",\"text\":\"禁断的军帽 强化巨盔\",\"flags\":{\"unique\":true}},{\"name\":\"狐运强铠\",\"type\":\"狂野部族皮甲\",\"text\":\"狐运强铠 狂野部族皮甲\",\"flags\":{\"unique\":true}},{\"name\":\"狐毛铠\",\"type\":\"狂野部族皮甲\",\"text\":\"狐毛铠 狂野部族皮甲\",\"flags\":{\"unique\":true}},{\"name\":\"分形思维\",\"type\":\"瓦尔之面\",\"text\":\"分形思维 瓦尔之面\",\"flags\":{\"unique\":true}},{\"name\":\"冰狱\",\"type\":\"革兜\",\"text\":\"冰狱 革兜\",\"flags\":{\"unique\":true}},{\"name\":\"灰眼\",\"type\":\"日耀之冠\",\"text\":\"灰眼 日耀之冠\",\"flags\":{\"unique\":true}},{\"name\":\"刚勇\",\"type\":\"军团长靴\",\"text\":\"刚勇 军团长靴\",\"flags\":{\"unique\":true}},{\"name\":\"无常法袍\",\"type\":\"智者之袍\",\"text\":\"无常法袍 智者之袍\",\"flags\":{\"unique\":true}},{\"name\":\"盖卢坎的飞升\",\"type\":\"匿踪短靴\",\"text\":\"盖卢坎的飞升 匿踪短靴\",\"flags\":{\"unique\":true}},{\"name\":\"吉尔菲的荣光\",\"type\":\"强化巨盔\",\"text\":\"吉尔菲的荣光 强化巨盔\",\"flags\":{\"unique\":true}},{\"name\":\"吉尔菲的遗产\",\"type\":\"强化巨盔\",\"text\":\"吉尔菲的遗产 强化巨盔\",\"flags\":{\"unique\":true}},{\"name\":\"吉尔菲的圣殿\",\"type\":\"权贵环甲\",\"text\":\"吉尔菲的圣殿 权贵环甲\",\"flags\":{\"unique\":true}},{\"name\":\"巨灵灾星\",\"type\":\"青铜护手\",\"text\":\"巨灵灾星 青铜护手\",\"flags\":{\"unique\":true}},{\"name\":\"七彩碟\",\"type\":\"冷芒刺盾\",\"text\":\"七彩碟 冷芒刺盾\",\"flags\":{\"unique\":true}},{\"name\":\"金缕帽\",\"type\":\"皮帽\",\"text\":\"金缕帽 皮帽\",\"flags\":{\"unique\":true}},{\"name\":\"龙炎足迹\",\"type\":\"砂影短靴\",\"text\":\"龙炎足迹 砂影短靴\",\"flags\":{\"unique\":true}},{\"name\":\"葛尔贡的凝视\",\"type\":\"弑君之面\",\"text\":\"葛尔贡的凝视 弑君之面\",\"flags\":{\"unique\":true}},{\"name\":\"伟大旧神之触手\",\"type\":\"鳗皮手套\",\"text\":\"伟大旧神之触手 鳗皮手套\",\"flags\":{\"unique\":true}},{\"name\":\"邪神庇护\",\"type\":\"波纹轻盾\",\"text\":\"邪神庇护 波纹轻盾\",\"flags\":{\"unique\":true}},{\"name\":\"贪婪之拥\",\"type\":\"金耀之铠\",\"text\":\"贪婪之拥 金耀之铠\",\"flags\":{\"unique\":true}},{\"name\":\"贪欲之诱\",\"type\":\"丝绒便鞋\",\"text\":\"贪欲之诱 丝绒便鞋\",\"flags\":{\"unique\":true}},{\"name\":\"议会之握\",\"type\":\"秘术手套\",\"text\":\"议会之握 秘术手套\",\"flags\":{\"unique\":true}},{\"name\":\"欺诈獠牙\",\"type\":\"龙鳞战甲\",\"text\":\"欺诈獠牙 龙鳞战甲\",\"flags\":{\"unique\":true}},{\"name\":\"血友病\",\"type\":\"蛇鳞手套\",\"text\":\"血友病 蛇鳞手套\",\"flags\":{\"unique\":true}},{\"name\":\"强健否决者\",\"type\":\"灵能之笼\",\"text\":\"强健否决者 灵能之笼\",\"flags\":{\"unique\":true}},{\"name\":\"神主之手\",\"type\":\"圣战手套\",\"text\":\"神主之手 圣战手套\",\"flags\":{\"unique\":true}},{\"name\":\"寒焰\",\"type\":\"革兜\",\"text\":\"寒焰 革兜\",\"flags\":{\"unique\":true}},{\"name\":\"异教面纱\",\"type\":\"弑神之面\",\"text\":\"异教面纱 弑神之面\",\"flags\":{\"unique\":true}},{\"name\":\"私欲的光辉\",\"type\":\"士兵之盔\",\"text\":\"私欲的光辉 士兵之盔\",\"flags\":{\"unique\":true}},{\"name\":\"李姆本\",\"type\":\"羊皮手套\",\"text\":\"李姆本 羊皮手套\",\"flags\":{\"unique\":true}},{\"name\":\"雷姆诺的豪情\",\"type\":\"绣领之盔\",\"text\":\"雷姆诺的豪情 绣领之盔\",\"flags\":{\"unique\":true}},{\"name\":\"冰冷之眼\",\"type\":\"羊皮手套\",\"text\":\"冰冷之眼 羊皮手套\",\"flags\":{\"unique\":true}},{\"name\":\"西里的嗜血之矢\",\"type\":\"鲨齿箭袋\",\"text\":\"西里的嗜血之矢 鲨齿箭袋\",\"flags\":{\"unique\":true}},{\"name\":\"西里之逝\",\"type\":\"鲨齿箭袋\",\"text\":\"西里之逝 鲨齿箭袋\",\"flags\":{\"unique\":true}},{\"name\":\"西里的战衣\",\"type\":\"星辰皮甲\",\"text\":\"西里的战衣 星辰皮甲\",\"flags\":{\"unique\":true}},{\"name\":\"冰息\",\"type\":\"精制环甲\",\"text\":\"冰息 精制环甲\",\"flags\":{\"unique\":true}},{\"name\":\"烈炎之心\",\"type\":\"圣洁锁甲\",\"text\":\"烈炎之心 圣洁锁甲\",\"flags\":{\"unique\":true}},{\"name\":\"靛蓝之冠\",\"type\":\"灵主之环\",\"text\":\"靛蓝之冠 灵主之环\",\"flags\":{\"unique\":true}},{\"name\":\"炼狱之心\",\"type\":\"毒蛛丝之袍\",\"text\":\"炼狱之心 毒蛛丝之袍\",\"flags\":{\"unique\":true}},{\"name\":\"速度之力\",\"type\":\"狂虐者束衣\",\"text\":\"速度之力 狂虐者束衣\",\"flags\":{\"unique\":true}},{\"name\":\"耀日\",\"type\":\"威能鸢盾\",\"text\":\"耀日 威能鸢盾\",\"flags\":{\"unique\":true}},{\"name\":\"茵雅的启蒙\",\"type\":\"秘术便鞋\",\"text\":\"茵雅的启蒙 秘术便鞋\",\"flags\":{\"unique\":true}},{\"name\":\"钢铁之心\",\"type\":\"征战重铠\",\"text\":\"钢铁之心 征战重铠\",\"flags\":{\"unique\":true}},{\"name\":\"苦痛狂鲨\",\"type\":\"霸者刺盾\",\"text\":\"苦痛狂鲨 霸者刺盾\",\"flags\":{\"unique\":true}},{\"name\":\"卡莉莎的优雅之影\",\"type\":\"绣布手套\",\"text\":\"卡莉莎的优雅之影 绣布手套\",\"flags\":{\"unique\":true}},{\"name\":\"冰霜之镜\",\"type\":\"彩绘轻盾\",\"text\":\"冰霜之镜 彩绘轻盾\",\"flags\":{\"unique\":true}},{\"name\":\"冰霜之魂\",\"type\":\"彩绘轻盾\",\"text\":\"冰霜之魂 彩绘轻盾\",\"flags\":{\"unique\":true}},{\"name\":\"冈姆的壮志\",\"type\":\"荣耀战铠\",\"text\":\"冈姆的壮志 荣耀战铠\",\"flags\":{\"unique\":true}},{\"name\":\"冈姆的稳重之靴\",\"type\":\"巨人胫甲\",\"text\":\"冈姆的稳重之靴 巨人胫甲\",\"flags\":{\"unique\":true}},{\"name\":\"皇家卫甲\",\"type\":\"征战锁甲\",\"text\":\"皇家卫甲 征战锁甲\",\"flags\":{\"unique\":true}},{\"name\":\"金珏之缮\",\"type\":\"精制皮甲\",\"text\":\"金珏之缮 精制皮甲\",\"flags\":{\"unique\":true}},{\"name\":\"奇塔弗之渴望\",\"type\":\"热战之盔\",\"text\":\"奇塔弗之渴望 热战之盔\",\"flags\":{\"unique\":true}},{\"name\":\"孔明的神算\",\"type\":\"象牙魔盾\",\"text\":\"孔明的神算 象牙魔盾\",\"flags\":{\"unique\":true}},{\"name\":\"共鸣之面\",\"type\":\"节庆之面\",\"text\":\"共鸣之面 节庆之面\",\"flags\":{\"unique\":true}},{\"name\":\"临死的施舍\",\"type\":\"钢镜刺盾\",\"text\":\"临死的施舍 钢镜刺盾\",\"flags\":{\"unique\":true}},{\"name\":\"魔道之衣\",\"type\":\"华丽环甲\",\"text\":\"魔道之衣 华丽环甲\",\"flags\":{\"unique\":true}},{\"name\":\"雷语\",\"type\":\"荒野锁铠\",\"text\":\"雷语 荒野锁铠\",\"flags\":{\"unique\":true}},{\"name\":\"月影之耀\",\"type\":\"灵相魔盾\",\"text\":\"月影之耀 灵相魔盾\",\"flags\":{\"unique\":true}},{\"name\":\"光明偷猎者\",\"type\":\"强化巨盔\",\"text\":\"光明偷猎者 强化巨盔\",\"flags\":{\"unique\":true}}]";
        } else if (itemTag.equals("传奇护甲2")) {
            return "[{\"name\":\"狮眼的斗志\",\"type\":\"铜影长靴\",\"text\":\"狮眼的斗志 铜影长靴\",\"flags\":{\"unique\":true}},{\"name\":\"狮眼的荣耀之盾\",\"type\":\"坚毅塔盾\",\"text\":\"狮眼的荣耀之盾 坚毅塔盾\",\"flags\":{\"unique\":true}},{\"name\":\"狮眼的视线\",\"type\":\"征战重铠\",\"text\":\"狮眼的视线 征战重铠\",\"flags\":{\"unique\":true}},{\"name\":\"意识之缘\",\"type\":\"铁锻护手\",\"text\":\"意识之缘 铁锻护手\",\"flags\":{\"unique\":true}},{\"name\":\"满溢之甲\",\"type\":\"权贵环甲\",\"text\":\"满溢之甲 权贵环甲\",\"flags\":{\"unique\":true}},{\"name\":\"狼蛛\",\"type\":\"生皮塔盾\",\"text\":\"狼蛛 生皮塔盾\",\"flags\":{\"unique\":true}},{\"name\":\"马奇纳护手\",\"type\":\"暗影者护手\",\"text\":\"马奇纳护手 暗影者护手\",\"flags\":{\"unique\":true}},{\"name\":\"坚毅之食\",\"type\":\"坚毅塔盾\",\"text\":\"坚毅之食 坚毅塔盾\",\"flags\":{\"unique\":true}},{\"name\":\"玛拉凯之觉醒\",\"type\":\"铁锻之面\",\"text\":\"玛拉凯之觉醒 铁锻之面\",\"flags\":{\"unique\":true}},{\"name\":\"轮回\",\"type\":\"和谐魔盾\",\"text\":\"轮回 和谐魔盾\",\"flags\":{\"unique\":true}},{\"name\":\"马拉凯之记\",\"type\":\"暗影者护手\",\"text\":\"马拉凯之记 暗影者护手\",\"flags\":{\"unique\":true}},{\"name\":\"玛拉凯的祭具\",\"type\":\"铁锻之面\",\"text\":\"玛拉凯的祭具 铁锻之面\",\"flags\":{\"unique\":true}},{\"name\":\"玛拉凯的远见\",\"type\":\"领主战冠\",\"text\":\"玛拉凯的远见 领主战冠\",\"flags\":{\"unique\":true}},{\"name\":\"马雷格罗的染血透镜\",\"type\":\"复合刺盾\",\"text\":\"马雷格罗的染血透镜 复合刺盾\",\"flags\":{\"unique\":true}},{\"name\":\"马雷格罗的玫红手套\",\"type\":\"鹿皮手套\",\"text\":\"马雷格罗的玫红手套 鹿皮手套\",\"flags\":{\"unique\":true}},{\"name\":\"马洛尼的技巧\",\"type\":\"华美箭袋\",\"text\":\"马洛尼的技巧 华美箭袋\",\"flags\":{\"unique\":true}},{\"name\":\"马洛尼的暮光\",\"type\":\"钝矢箭袋\",\"text\":\"马洛尼的暮光 钝矢箭袋\",\"flags\":{\"unique\":true}},{\"name\":\"魔力风暴\",\"type\":\"石化魔盾\",\"text\":\"魔力风暴 石化魔盾\",\"flags\":{\"unique\":true}},{\"name\":\"军团的进击\",\"type\":\"军团长靴\",\"text\":\"军团的进击 军团长靴\",\"flags\":{\"unique\":true}},{\"name\":\"红契符印\",\"type\":\"祭仪之冠\",\"text\":\"红契符印 祭仪之冠\",\"flags\":{\"unique\":true}},{\"name\":\"烈士之冠\",\"type\":\"藤蔓之冠\",\"text\":\"烈士之冠 藤蔓之冠\",\"flags\":{\"unique\":true}},{\"name\":\"饮魂者面罩\",\"type\":\"圣战之盔\",\"text\":\"饮魂者面罩 圣战之盔\",\"flags\":{\"unique\":true}},{\"name\":\"缝合恶魔面罩\",\"type\":\"行政者战冠\",\"text\":\"缝合恶魔面罩 行政者战冠\",\"flags\":{\"unique\":true}},{\"name\":\"裁决面罩\",\"type\":\"行政者战冠\",\"text\":\"裁决面罩 行政者战冠\",\"flags\":{\"unique\":true}},{\"name\":\"远祖之颅\",\"type\":\"环形魔盾\",\"text\":\"远祖之颅 环形魔盾\",\"flags\":{\"unique\":true}},{\"name\":\"征服之口\",\"type\":\"冷钢之冠\",\"text\":\"征服之口 冷钢之冠\",\"flags\":{\"unique\":true}},{\"name\":\"害人之口\",\"type\":\"唤骨头盔\",\"text\":\"害人之口 唤骨头盔\",\"flags\":{\"unique\":true}},{\"name\":\"梅吉诺德的巨力腕甲\",\"type\":\"冷钢护手\",\"text\":\"梅吉诺德的巨力腕甲 冷钢护手\",\"flags\":{\"unique\":true}},{\"name\":\"记忆囚笼\",\"type\":\"领主战冠\",\"text\":\"记忆囚笼 领主战冠\",\"flags\":{\"unique\":true}},{\"name\":\"佣兵领地\",\"type\":\"迷踪手套\",\"text\":\"佣兵领地 迷踪手套\",\"flags\":{\"unique\":true}},{\"name\":\"智者之秘\",\"type\":\"鹰喙之面\",\"text\":\"智者之秘 鹰喙之面\",\"flags\":{\"unique\":true}},{\"name\":\"灵漩\",\"type\":\"战意之盔\",\"text\":\"灵漩 战意之盔\",\"flags\":{\"unique\":true}},{\"name\":\"迷雾之墙\",\"type\":\"漆彩轻盾\",\"text\":\"迷雾之墙 漆彩轻盾\",\"flags\":{\"unique\":true}},{\"name\":\"哑风尖旗\",\"type\":\"釉彩轻盾\",\"text\":\"哑风尖旗 釉彩轻盾\",\"flags\":{\"unique\":true}},{\"name\":\"哑风轻步\",\"type\":\"蛇鳞长靴\",\"text\":\"哑风轻步 蛇鳞长靴\",\"flags\":{\"unique\":true}},{\"name\":\"诺米克的风暴\",\"type\":\"扣环短靴\",\"text\":\"诺米克的风暴 扣环短靴\",\"flags\":{\"unique\":true}},{\"name\":\"虚空\",\"type\":\"军团手套\",\"text\":\"虚空 军团手套\",\"flags\":{\"unique\":true}},{\"name\":\"厌食者\",\"type\":\"狮首皮盔\",\"text\":\"厌食者 狮首皮盔\",\"flags\":{\"unique\":true}},{\"name\":\"暴君之握\",\"type\":\"军团手套\",\"text\":\"暴君之握 军团手套\",\"flags\":{\"unique\":true}},{\"name\":\"至高天堂\",\"type\":\"禁礼之靴\",\"text\":\"至高天堂 禁礼之靴\",\"flags\":{\"unique\":true}},{\"name\":\"恩德的迅影\",\"type\":\"裹趾护手\",\"text\":\"恩德的迅影 裹趾护手\",\"flags\":{\"unique\":true}},{\"name\":\"维多里奥的飞升\",\"type\":\"羊皮短靴\",\"text\":\"维多里奥的飞升 羊皮短靴\",\"flags\":{\"unique\":true}},{\"name\":\"欧斯卡姆\",\"type\":\"砂影手套\",\"text\":\"欧斯卡姆 砂影手套\",\"flags\":{\"unique\":true}},{\"name\":\"苦难探寻者\",\"type\":\"粗革手套\",\"text\":\"苦难探寻者 粗革手套\",\"flags\":{\"unique\":true}},{\"name\":\"峰回路转\",\"type\":\"艾兹麦刺盾\",\"text\":\"峰回路转 艾兹麦刺盾\",\"flags\":{\"unique\":true}},{\"name\":\"背信弃义\",\"type\":\"荣耀战铠\",\"text\":\"背信弃义 荣耀战铠\",\"flags\":{\"unique\":true}},{\"name\":\"追逐之羽\",\"type\":\"灵骨之冠\",\"text\":\"追逐之羽 灵骨之冠\",\"flags\":{\"unique\":true}},{\"name\":\"元素的庇护\",\"type\":\"威能鸢盾\",\"text\":\"元素的庇护 威能鸢盾\",\"flags\":{\"unique\":true}},{\"name\":\"森林之后\",\"type\":\"命运皮甲\",\"text\":\"森林之后 命运皮甲\",\"flags\":{\"unique\":true}},{\"name\":\"虹幕\",\"type\":\"咒者长靴\",\"text\":\"虹幕 咒者长靴\",\"flags\":{\"unique\":true}},{\"name\":\"拉克斯的渴望\",\"type\":\"仪式短靴\",\"text\":\"拉克斯的渴望 仪式短靴\",\"flags\":{\"unique\":true}},{\"name\":\"献祭之心\",\"type\":\"巨人魔盾\",\"text\":\"献祭之心 巨人魔盾\",\"flags\":{\"unique\":true}},{\"name\":\"鼠巢\",\"type\":\"熊首皮盔\",\"text\":\"鼠巢 熊首皮盔\",\"flags\":{\"unique\":true}},{\"name\":\"寒锋之卫\",\"type\":\"宽矢箭袋\",\"text\":\"寒锋之卫 宽矢箭袋\",\"flags\":{\"unique\":true}},{\"name\":\"红刃旗帜\",\"type\":\"彩绘塔盾\",\"text\":\"红刃旗帜 彩绘塔盾\",\"flags\":{\"unique\":true}},{\"name\":\"红刃蹂躏靴\",\"type\":\"远古胫甲\",\"text\":\"红刃蹂躏靴 远古胫甲\",\"flags\":{\"unique\":true}},{\"name\":\"帝国的残壁\",\"type\":\"羊皮轻盾\",\"text\":\"帝国的残壁 羊皮轻盾\",\"flags\":{\"unique\":true}},{\"name\":\"悔悟之掌\",\"type\":\"圣战手套\",\"text\":\"悔悟之掌 圣战手套\",\"flags\":{\"unique\":true}},{\"name\":\"深渊之唤【仿品】\",\"type\":\"艾兹麦坚盔\",\"text\":\"深渊之唤【仿品】 艾兹麦坚盔\",\"flags\":{\"unique\":true}},{\"name\":\"阿尔贝隆的征途【仿品】\",\"type\":\"战士之靴\",\"text\":\"阿尔贝隆的征途【仿品】 战士之靴\",\"flags\":{\"unique\":true}},{\"name\":\"相生相克【仿品】\",\"type\":\"术士手套\",\"text\":\"相生相克【仿品】 术士手套\",\"flags\":{\"unique\":true}},{\"name\":\"极地之眼【仿品】\",\"type\":\"罪者之帽\",\"text\":\"极地之眼【仿品】 罪者之帽\",\"flags\":{\"unique\":true}},{\"name\":\"安姆布的战甲【仿品】\",\"type\":\"圣战锁甲\",\"text\":\"安姆布的战甲【仿品】 圣战锁甲\",\"flags\":{\"unique\":true}},{\"name\":\"阿兹里的捷思【仿品】\",\"type\":\"瓦尔护手\",\"text\":\"阿兹里的捷思【仿品】 瓦尔护手\",\"flags\":{\"unique\":true}},{\"name\":\"灵骸之履【仿品】\",\"type\":\"丝绸便鞋\",\"text\":\"灵骸之履【仿品】 丝绸便鞋\",\"flags\":{\"unique\":true}},{\"name\":\"永恒幽影【仿品】\",\"type\":\"血色之衣\",\"text\":\"永恒幽影【仿品】 血色之衣\",\"flags\":{\"unique\":true}},{\"name\":\"大地之护【仿品】\",\"type\":\"胜利盔甲\",\"text\":\"大地之护【仿品】 胜利盔甲\",\"flags\":{\"unique\":true}},{\"name\":\"禁断的军帽【仿品】\",\"type\":\"强化巨盔\",\"text\":\"禁断的军帽【仿品】 强化巨盔\",\"flags\":{\"unique\":true}},{\"name\":\"议会之握【仿品】\",\"type\":\"秘术手套\",\"text\":\"议会之握【仿品】 秘术手套\",\"flags\":{\"unique\":true}},{\"name\":\"茵雅的启蒙【仿品】\",\"type\":\"秘术便鞋\",\"text\":\"茵雅的启蒙【仿品】 秘术便鞋\",\"flags\":{\"unique\":true}},{\"name\":\"卡莉莎的优雅之影【仿品】\",\"type\":\"绣布手套\",\"text\":\"卡莉莎的优雅之影【仿品】 绣布手套\",\"flags\":{\"unique\":true}},{\"name\":\"冈姆的壮志【仿品】\",\"type\":\"荣耀战铠\",\"text\":\"冈姆的壮志【仿品】 荣耀战铠\",\"flags\":{\"unique\":true}},{\"name\":\"共鸣之面【仿品】\",\"type\":\"节庆之面\",\"text\":\"共鸣之面【仿品】 节庆之面\",\"flags\":{\"unique\":true}},{\"name\":\"狮眼的斗志【仿品】\",\"type\":\"铜影长靴\",\"text\":\"狮眼的斗志【仿品】 铜影长靴\",\"flags\":{\"unique\":true}},{\"name\":\"满溢之甲【仿品】\",\"type\":\"权贵环甲\",\"text\":\"满溢之甲【仿品】 权贵环甲\",\"flags\":{\"unique\":true}},{\"name\":\"马洛尼的技巧【仿品】\",\"type\":\"华美箭袋\",\"text\":\"马洛尼的技巧【仿品】 华美箭袋\",\"flags\":{\"unique\":true}},{\"name\":\"迷雾之墙【仿品】\",\"type\":\"漆彩轻盾\",\"text\":\"迷雾之墙【仿品】 漆彩轻盾\",\"flags\":{\"unique\":true}},{\"name\":\"完美姿态【仿品】\",\"type\":\"星辰皮甲\",\"text\":\"完美姿态【仿品】 星辰皮甲\",\"flags\":{\"unique\":true}},{\"name\":\"赤红踪迹【仿品】\",\"type\":\"巨人胫甲\",\"text\":\"赤红踪迹【仿品】 巨人胫甲\",\"flags\":{\"unique\":true}},{\"name\":\"无尽之卫【仿品】\",\"type\":\"禁礼之甲\",\"text\":\"无尽之卫【仿品】 禁礼之甲\",\"flags\":{\"unique\":true}},{\"name\":\"泯光寿衣【仿品】\",\"type\":\"禁礼之甲\",\"text\":\"泯光寿衣【仿品】 禁礼之甲\",\"flags\":{\"unique\":true}},{\"name\":\"灵魂打击【仿品】\",\"type\":\"刺锋箭袋\",\"text\":\"灵魂打击【仿品】 刺锋箭袋\",\"flags\":{\"unique\":true}},{\"name\":\"溃败【仿品】\",\"type\":\"暗影之靴\",\"text\":\"溃败【仿品】 暗影之靴\",\"flags\":{\"unique\":true}},{\"name\":\"强袭者【仿品】\",\"type\":\"粗革短靴\",\"text\":\"强袭者【仿品】 粗革短靴\",\"flags\":{\"unique\":true}},{\"name\":\"图克哈玛堡垒【仿品】\",\"type\":\"乌木塔盾\",\"text\":\"图克哈玛堡垒【仿品】 乌木塔盾\",\"flags\":{\"unique\":true}},{\"name\":\"夜幕【仿品】\",\"type\":\"巨盔\",\"text\":\"夜幕【仿品】 巨盔\",\"flags\":{\"unique\":true}},{\"name\":\"维多里奥的贡献【仿品】\",\"type\":\"合板鸢盾\",\"text\":\"维多里奥的贡献【仿品】 合板鸢盾\",\"flags\":{\"unique\":true}},{\"name\":\"虚空行者【仿品】\",\"type\":\"暗影者长靴\",\"text\":\"虚空行者【仿品】 暗影者长靴\",\"flags\":{\"unique\":true}},{\"name\":\"福库尔的手【仿品】\",\"type\":\"狂热者手套\",\"text\":\"福库尔的手【仿品】 狂热者手套\",\"flags\":{\"unique\":true}},{\"name\":\"瑞佛之羽\",\"type\":\"双锋箭袋\",\"text\":\"瑞佛之羽 双锋箭袋\",\"flags\":{\"unique\":true}},{\"name\":\"酷寒的凝视\",\"type\":\"灵能之笼\",\"text\":\"酷寒的凝视 灵能之笼\",\"flags\":{\"unique\":true}},{\"name\":\"烈炎之翼\",\"type\":\"厚装鸢盾\",\"text\":\"烈炎之翼 厚装鸢盾\",\"flags\":{\"unique\":true}},{\"name\":\"腐朽军团\",\"type\":\"覆体环甲\",\"text\":\"腐朽军团 覆体环甲\",\"flags\":{\"unique\":true}},{\"name\":\"猎宝者的护手\",\"type\":\"羊毛手套\",\"text\":\"猎宝者的护手 羊毛手套\",\"flags\":{\"unique\":true}},{\"name\":\"勇者之礼\",\"type\":\"刺锋箭袋\",\"text\":\"勇者之礼 刺锋箭袋\",\"flags\":{\"unique\":true}},{\"name\":\"萨费尔的智慧\",\"type\":\"圣记鸢盾\",\"text\":\"萨费尔的智慧 圣记鸢盾\",\"flags\":{\"unique\":true}},{\"name\":\"苍空之翎\",\"type\":\"绸缎之兜\",\"text\":\"苍空之翎 绸缎之兜\",\"flags\":{\"unique\":true}},{\"name\":\"苍空之巢\",\"type\":\"血色之衣\",\"text\":\"苍空之巢 血色之衣\",\"flags\":{\"unique\":true}},{\"name\":\"苍空之爪\",\"type\":\"火蝮鳞长靴\",\"text\":\"苍空之爪 火蝮鳞长靴\",\"flags\":{\"unique\":true}},{\"name\":\"苍空之翼\",\"type\":\"战士手套\",\"text\":\"苍空之翼 战士手套\",\"flags\":{\"unique\":true}},{\"name\":\"禁语\",\"type\":\"灵能之笼\",\"text\":\"禁语 灵能之笼\",\"flags\":{\"unique\":true}},{\"name\":\"魔蝎的呼唤\",\"type\":\"宽矢箭袋\",\"text\":\"魔蝎的呼唤 宽矢箭袋\",\"flags\":{\"unique\":true}},{\"name\":\"山特立的回应\",\"type\":\"铜锻魔盾\",\"text\":\"山特立的回应 铜锻魔盾\",\"flags\":{\"unique\":true}},{\"name\":\"神行靴\",\"type\":\"生皮短靴\",\"text\":\"神行靴 生皮短靴\",\"flags\":{\"unique\":true}},{\"name\":\"悲运之缚\",\"type\":\"链甲手套\",\"text\":\"悲运之缚 链甲手套\",\"flags\":{\"unique\":true}},{\"name\":\"沙尘之影\",\"type\":\"环带护手\",\"text\":\"沙尘之影 环带护手\",\"flags\":{\"unique\":true}},{\"name\":\"暗影缝合\",\"type\":\"祭礼束衣\",\"text\":\"暗影缝合 祭礼束衣\",\"flags\":{\"unique\":true}},{\"name\":\"塑界之触\",\"type\":\"圣战手套\",\"text\":\"塑界之触 圣战手套\",\"flags\":{\"unique\":true}},{\"name\":\"破裂碎片\",\"type\":\"绯红圆盾\",\"text\":\"破裂碎片 绯红圆盾\",\"flags\":{\"unique\":true}},{\"name\":\"薛朗的诡计\",\"type\":\"学者长靴\",\"text\":\"薛朗的诡计 学者长靴\",\"flags\":{\"unique\":true}},{\"name\":\"薛朗的秘术长靴\",\"type\":\"学者长靴\",\"text\":\"薛朗的秘术长靴 学者长靴\",\"flags\":{\"unique\":true}},{\"name\":\"薛朗的护身长袍\",\"type\":\"秘术长衣\",\"text\":\"薛朗的护身长袍 秘术长衣\",\"flags\":{\"unique\":true}},{\"name\":\"泯光寿衣\",\"type\":\"禁礼之甲\",\"text\":\"泯光寿衣 禁礼之甲\",\"flags\":{\"unique\":true}},{\"name\":\"敏锐思维\",\"type\":\"匿踪短靴\",\"text\":\"敏锐思维 匿踪短靴\",\"flags\":{\"unique\":true}},{\"name\":\"君主之肤\",\"type\":\"简易之袍\",\"text\":\"君主之肤 简易之袍\",\"flags\":{\"unique\":true}},{\"name\":\"忠诚之肤\",\"type\":\"简易之袍\",\"text\":\"忠诚之肤 简易之袍\",\"flags\":{\"unique\":true}},{\"name\":\"小会战\",\"type\":\"双锋箭袋\",\"text\":\"小会战 双锋箭袋\",\"flags\":{\"unique\":true}},{\"name\":\"光辉之颅\",\"type\":\"精兵之盔\",\"text\":\"光辉之颅 精兵之盔\",\"flags\":{\"unique\":true}},{\"name\":\"空向\",\"type\":\"术士长靴\",\"text\":\"空向 术士长靴\",\"flags\":{\"unique\":true}},{\"name\":\"奴隶贩子之手\",\"type\":\"伏击护手\",\"text\":\"奴隶贩子之手 伏击护手\",\"flags\":{\"unique\":true}},{\"name\":\"苍蟒之鳞\",\"type\":\"铜影手套\",\"text\":\"苍蟒之鳞 铜影手套\",\"flags\":{\"unique\":true}},{\"name\":\"白银恩典\",\"type\":\"锯齿箭袋\",\"text\":\"白银恩典 锯齿箭袋\",\"flags\":{\"unique\":true}},{\"name\":\"蝮吻\",\"type\":\"暗影护手\",\"text\":\"蝮吻 暗影护手\",\"flags\":{\"unique\":true}},{\"name\":\"烈阳铠\",\"type\":\"铜锻板甲\",\"text\":\"烈阳铠 铜锻板甲\",\"flags\":{\"unique\":true}},{\"name\":\"祖灵之约\",\"type\":\"蛛丝之袍\",\"text\":\"祖灵之约 蛛丝之袍\",\"flags\":{\"unique\":true}},{\"name\":\"灵魂打击\",\"type\":\"刺锋箭袋\",\"text\":\"灵魂打击 刺锋箭袋\",\"flags\":{\"unique\":true}},{\"name\":\"南方\",\"type\":\"战士手套\",\"text\":\"南方 战士手套\",\"flags\":{\"unique\":true}},{\"name\":\"众生指引\",\"type\":\"箴言战冠\",\"text\":\"众生指引 箴言战冠\",\"flags\":{\"unique\":true}},{\"name\":\"孢囊守卫\",\"type\":\"圣者链甲\",\"text\":\"孢囊守卫 圣者链甲\",\"flags\":{\"unique\":true}},{\"name\":\"新生之徽\",\"type\":\"朽木鸢盾\",\"text\":\"新生之徽 朽木鸢盾\",\"flags\":{\"unique\":true}},{\"name\":\"斯塔空加之首\",\"type\":\"绸缎之兜\",\"text\":\"斯塔空加之首 绸缎之兜\",\"flags\":{\"unique\":true}},{\"name\":\"绝地魔履\",\"type\":\"术士长靴\",\"text\":\"绝地魔履 术士长靴\",\"flags\":{\"unique\":true}},{\"name\":\"风暴骑士\",\"type\":\"坚铁胫甲\",\"text\":\"风暴骑士 坚铁胫甲\",\"flags\":{\"unique\":true}},{\"name\":\"风暴赐福\",\"type\":\"暗影护手\",\"text\":\"风暴赐福 暗影护手\",\"flags\":{\"unique\":true}},{\"name\":\"日耀\",\"type\":\"环带长靴\",\"text\":\"日耀 环带长靴\",\"flags\":{\"unique\":true}},{\"name\":\"日怨\",\"type\":\"环带长靴\",\"text\":\"日怨 环带长靴\",\"flags\":{\"unique\":true}},{\"name\":\"浪涌缚者\",\"type\":\"龙鳞手套\",\"text\":\"浪涌缚者 龙鳞手套\",\"flags\":{\"unique\":true}},{\"name\":\"无尽之衣\",\"type\":\"简易之袍\",\"text\":\"无尽之衣 简易之袍\",\"flags\":{\"unique\":true}},{\"name\":\"海军上将\",\"type\":\"映彩外套\",\"text\":\"海军上将 映彩外套\",\"flags\":{\"unique\":true}},{\"name\":\"悬念\",\"type\":\"艾兹麦塔盾\",\"text\":\"悬念 艾兹麦塔盾\",\"flags\":{\"unique\":true}},{\"name\":\"男爵\",\"type\":\"护面盔\",\"text\":\"男爵 护面盔\",\"flags\":{\"unique\":true}},{\"name\":\"兽毛披肩\",\"type\":\"瓦尔法衣\",\"text\":\"兽毛披肩 瓦尔法衣\",\"flags\":{\"unique\":true}},{\"name\":\"血影\",\"type\":\"鲨皮短靴\",\"text\":\"血影 鲨皮短靴\",\"flags\":{\"unique\":true}},{\"name\":\"巨铜之顶\",\"type\":\"角斗重铠\",\"text\":\"巨铜之顶 角斗重铠\",\"flags\":{\"unique\":true}},{\"name\":\"海王冠冕\",\"type\":\"箴言战冠\",\"text\":\"海王冠冕 箴言战冠\",\"flags\":{\"unique\":true}},{\"name\":\"祭礼之雨\",\"type\":\"梦魇战盔\",\"text\":\"祭礼之雨 梦魇战盔\",\"flags\":{\"unique\":true}},{\"name\":\"缺角帝冠\",\"type\":\"箴言战冠\",\"text\":\"缺角帝冠 箴言战冠\",\"flags\":{\"unique\":true}},{\"name\":\"灵魂甲胄\",\"type\":\"灭世法衣\",\"text\":\"灵魂甲胄 灭世法衣\",\"flags\":{\"unique\":true}},{\"name\":\"血誓\",\"type\":\"蛛丝之袍\",\"text\":\"血誓 蛛丝之袍\",\"flags\":{\"unique\":true}},{\"name\":\"深渊绝壁\",\"type\":\"铆钉圆盾\",\"text\":\"深渊绝壁 铆钉圆盾\",\"flags\":{\"unique\":true}},{\"name\":\"吞噬者王冠\",\"type\":\"操灵者之冠\",\"text\":\"吞噬者王冠 操灵者之冠\",\"flags\":{\"unique\":true}},{\"name\":\"入殓师\",\"type\":\"禁礼护手\",\"text\":\"入殓师 禁礼护手\",\"flags\":{\"unique\":true}},{\"name\":\"永恒苹果\",\"type\":\"魂相魔盾\",\"text\":\"永恒苹果 魂相魔盾\",\"flags\":{\"unique\":true}},{\"name\":\"永恒幽影\",\"type\":\"血色之衣\",\"text\":\"永恒幽影 血色之衣\",\"flags\":{\"unique\":true}},{\"name\":\"雏鸟\",\"type\":\"漆彩之盔\",\"text\":\"雏鸟 漆彩之盔\",\"flags\":{\"unique\":true}},{\"name\":\"无形火炬\",\"type\":\"破城之盔\",\"text\":\"无形火炬 破城之盔\",\"flags\":{\"unique\":true}},{\"name\":\"无形炼狱\",\"type\":\"皇室坚盔\",\"text\":\"无形炼狱 皇室坚盔\",\"flags\":{\"unique\":true}},{\"name\":\"爆裂之射\",\"type\":\"钝矢箭袋\",\"text\":\"爆裂之射 钝矢箭袋\",\"flags\":{\"unique\":true}},{\"name\":\"惊悸剧院\",\"type\":\"柚木圆盾\",\"text\":\"惊悸剧院 柚木圆盾\",\"flags\":{\"unique\":true}},{\"name\":\"鸥喙\",\"type\":\"祸鸦之面\",\"text\":\"鸥喙 祸鸦之面\",\"flags\":{\"unique\":true}},{\"name\":\"不朽意志\",\"type\":\"威能鸢盾\",\"text\":\"不朽意志 威能鸢盾\",\"flags\":{\"unique\":true}},{\"name\":\"无尽之距\",\"type\":\"巨灵胫甲\",\"text\":\"无尽之距 巨灵胫甲\",\"flags\":{\"unique\":true}},{\"name\":\"钢铁要塞\",\"type\":\"征战重铠\",\"text\":\"钢铁要塞 征战重铠\",\"flags\":{\"unique\":true}},{\"name\":\"象牙之塔\",\"type\":\"圣者链甲\",\"text\":\"象牙之塔 圣者链甲\",\"flags\":{\"unique\":true}},{\"name\":\"橡树\",\"type\":\"朽木鸢盾\",\"text\":\"橡树 朽木鸢盾\",\"flags\":{\"unique\":true}},{\"name\":\"疾风的沉思\",\"type\":\"清视护盔\",\"text\":\"疾风的沉思 清视护盔\",\"flags\":{\"unique\":true}},{\"name\":\"完美姿态\",\"type\":\"星辰皮甲\",\"text\":\"完美姿态 星辰皮甲\",\"flags\":{\"unique\":true}},{\"name\":\"女王的饥饿\",\"type\":\"瓦尔法衣\",\"text\":\"女王的饥饿 瓦尔法衣\",\"flags\":{\"unique\":true}},{\"name\":\"疫鼠囚笼\",\"type\":\"鲨皮之衣\",\"text\":\"疫鼠囚笼 鲨皮之衣\",\"flags\":{\"unique\":true}},{\"name\":\"赤红踪迹\",\"type\":\"巨人胫甲\",\"text\":\"赤红踪迹 巨人胫甲\",\"flags\":{\"unique\":true}},{\"name\":\"无尽之卫\",\"type\":\"禁礼之甲\",\"text\":\"无尽之卫 禁礼之甲\",\"flags\":{\"unique\":true}},{\"name\":\"神性破碎\",\"type\":\"钝矢箭袋\",\"text\":\"神性破碎 钝矢箭袋\",\"flags\":{\"unique\":true}},{\"name\":\"狼烟\",\"type\":\"火灵箭袋\",\"text\":\"狼烟 火灵箭袋\",\"flags\":{\"unique\":true}},{\"name\":\"雪盲恩惠\",\"type\":\"光耀皮甲\",\"text\":\"雪盲恩惠 光耀皮甲\",\"flags\":{\"unique\":true}},{\"name\":\"溃败\",\"type\":\"暗影之靴\",\"text\":\"溃败 暗影之靴\",\"flags\":{\"unique\":true}},{\"name\":\"降伏\",\"type\":\"艾兹麦塔盾\",\"text\":\"降伏 艾兹麦塔盾\",\"flags\":{\"unique\":true}},{\"name\":\"风暴拘束\",\"type\":\"无情之面\",\"text\":\"风暴拘束 无情之面\",\"flags\":{\"unique\":true}},{\"name\":\"风雪释放\",\"type\":\"无情之面\",\"text\":\"风雪释放 无情之面\",\"flags\":{\"unique\":true}},{\"name\":\"三龙战纪\",\"type\":\"黄金之面\",\"text\":\"三龙战纪 黄金之面\",\"flags\":{\"unique\":true}},{\"name\":\"不屈之志\",\"type\":\"威能鸢盾\",\"text\":\"不屈之志 威能鸢盾\",\"flags\":{\"unique\":true}},{\"name\":\"谜容\",\"type\":\"瓦尔之面\",\"text\":\"谜容 瓦尔之面\",\"flags\":{\"unique\":true}},{\"name\":\"颤栗之饥\",\"type\":\"战争轻盾\",\"text\":\"颤栗之饥 战争轻盾\",\"flags\":{\"unique\":true}},{\"name\":\"千缎\",\"type\":\"简易之袍\",\"text\":\"千缎 简易之袍\",\"flags\":{\"unique\":true}},{\"name\":\"千齿\",\"type\":\"瓦尔轻盾\",\"text\":\"千齿 瓦尔轻盾\",\"flags\":{\"unique\":true}},{\"name\":\"强袭者\",\"type\":\"粗革短靴\",\"text\":\"强袭者 粗革短靴\",\"flags\":{\"unique\":true}},{\"name\":\"轰天雷\",\"type\":\"暗影者护手\",\"text\":\"轰天雷 暗影者护手\",\"flags\":{\"unique\":true}},{\"name\":\"雷目\",\"type\":\"日耀之冠\",\"text\":\"雷目 日耀之冠\",\"flags\":{\"unique\":true}},{\"name\":\"钟表匠的华服\",\"type\":\"狂虐者束衣\",\"text\":\"钟表匠的华服 狂虐者束衣\",\"flags\":{\"unique\":true}},{\"name\":\"提图库斯的坚盾\",\"type\":\"强化塔盾\",\"text\":\"提图库斯的坚盾 强化塔盾\",\"flags\":{\"unique\":true}},{\"name\":\"陵拳\",\"type\":\"钢影护手\",\"text\":\"陵拳 钢影护手\",\"flags\":{\"unique\":true}},{\"name\":\"火柳胫甲\",\"type\":\"古钢胫甲\",\"text\":\"火柳胫甲 古钢胫甲\",\"flags\":{\"unique\":true}},{\"name\":\"三重扣\",\"type\":\"网眼手套\",\"text\":\"三重扣 网眼手套\",\"flags\":{\"unique\":true}},{\"name\":\"巫木\",\"type\":\"松木塔盾\",\"text\":\"巫木 松木塔盾\",\"flags\":{\"unique\":true}},{\"name\":\"图克玛哈堡垒\",\"type\":\"乌木塔盾\",\"text\":\"图克玛哈堡垒 乌木塔盾\",\"flags\":{\"unique\":true}},{\"name\":\"不屈烈焰\",\"type\":\"威能鸢盾\",\"text\":\"不屈烈焰 威能鸢盾\",\"flags\":{\"unique\":true}},{\"name\":\"瓦尔的灵手\",\"type\":\"铜影手套\",\"text\":\"瓦尔的灵手 铜影手套\",\"flags\":{\"unique\":true}},{\"name\":\"夜幕\",\"type\":\"巨盔\",\"text\":\"夜幕 巨盔\",\"flags\":{\"unique\":true}},{\"name\":\"陨落之环\",\"type\":\"巨人护手\",\"text\":\"陨落之环 巨人护手\",\"flags\":{\"unique\":true}},{\"name\":\"维多里奥的贡献\",\"type\":\"合板鸢盾\",\"text\":\"维多里奥的贡献 合板鸢盾\",\"flags\":{\"unique\":true}},{\"name\":\"维多里奥之绝响\",\"type\":\"漆彩束衣\",\"text\":\"维多里奥之绝响 漆彩束衣\",\"flags\":{\"unique\":true}},{\"name\":\"毒蛇之度\",\"type\":\"连身鳞甲\",\"text\":\"毒蛇之度 连身鳞甲\",\"flags\":{\"unique\":true}},{\"name\":\"兀鸣\",\"type\":\"操灵者背心\",\"text\":\"兀鸣 操灵者背心\",\"flags\":{\"unique\":true}},{\"name\":\"刁妇的圈套\",\"type\":\"刺绣手套\",\"text\":\"刁妇的圈套 刺绣手套\",\"flags\":{\"unique\":true}},{\"name\":\"影月\",\"type\":\"祭者圆盾\",\"text\":\"影月 祭者圆盾\",\"flags\":{\"unique\":true}},{\"name\":\"虚空之力\",\"type\":\"咒者手套\",\"text\":\"虚空之力 咒者手套\",\"flags\":{\"unique\":true}},{\"name\":\"虚空制箭者\",\"type\":\"穿射箭袋\",\"text\":\"虚空制箭者 穿射箭袋\",\"flags\":{\"unique\":true}},{\"name\":\"虚空行者\",\"type\":\"暗影者长靴\",\"text\":\"虚空行者 暗影者长靴\",\"flags\":{\"unique\":true}},{\"name\":\"福库尔的手\",\"type\":\"狂热者手套\",\"text\":\"福库尔的手 狂热者手套\",\"flags\":{\"unique\":true}},{\"name\":\"福尔的战铠\",\"type\":\"圣语锁甲\",\"text\":\"福尔的战铠 圣语锁甲\",\"flags\":{\"unique\":true}},{\"name\":\"福尔的远见\",\"type\":\"领主战冠\",\"text\":\"福尔的远见 领主战冠\",\"flags\":{\"unique\":true}},{\"name\":\"覆灭之兆\",\"type\":\"网眼长靴\",\"text\":\"覆灭之兆 网眼长靴\",\"flags\":{\"unique\":true}},{\"name\":\"荆棘之墙\",\"type\":\"铁制背心\",\"text\":\"荆棘之墙 铁制背心\",\"flags\":{\"unique\":true}},{\"name\":\"苦行之履\",\"type\":\"羊毛之鞋\",\"text\":\"苦行之履 羊毛之鞋\",\"flags\":{\"unique\":true}},{\"name\":\"瓦卡图图里\",\"type\":\"环形魔盾\",\"text\":\"瓦卡图图里 环形魔盾\",\"flags\":{\"unique\":true}},{\"name\":\"暴雨之舵\",\"type\":\"朽木圆盾\",\"text\":\"暴雨之舵 朽木圆盾\",\"flags\":{\"unique\":true}},{\"name\":\"荒野之绕\",\"type\":\"扣环皮甲\",\"text\":\"荒野之绕 扣环皮甲\",\"flags\":{\"unique\":true}},{\"name\":\"意志交锋\",\"type\":\"黄金之面\",\"text\":\"意志交锋 黄金之面\",\"flags\":{\"unique\":true}},{\"name\":\"恶风足迹\",\"type\":\"强化胫甲\",\"text\":\"恶风足迹 强化胫甲\",\"flags\":{\"unique\":true}},{\"name\":\"风哮\",\"type\":\"强化胫甲\",\"text\":\"风哮 强化胫甲\",\"flags\":{\"unique\":true}},{\"name\":\"升华之风\",\"type\":\"远古护手\",\"text\":\"升华之风 远古护手\",\"flags\":{\"unique\":true}},{\"name\":\"回光之迹\",\"type\":\"丝绒便鞋\",\"text\":\"回光之迹 丝绒便鞋\",\"flags\":{\"unique\":true}},{\"name\":\"世界雕刻者\",\"type\":\"龙鳞手套\",\"text\":\"世界雕刻者 龙鳞手套\",\"flags\":{\"unique\":true}},{\"name\":\"幽冥灵王\",\"type\":\"灵骨之冠\",\"text\":\"幽冥灵王 灵骨之冠\",\"flags\":{\"unique\":true}},{\"name\":\"费西亚的花环\",\"type\":\"铁锻之冠\",\"text\":\"费西亚的花环 铁锻之冠\",\"flags\":{\"unique\":true}},{\"name\":\"龙族印记\",\"type\":\"蝮鳞手套\",\"text\":\"龙族印记 蝮鳞手套\",\"flags\":{\"unique\":true}},{\"name\":\"伊芙班的诡计\",\"type\":\"灵主之环\",\"text\":\"伊芙班的诡计 灵主之环\",\"flags\":{\"unique\":true}},{\"name\":\"伊瑞的精通\",\"type\":\"精制皮甲\",\"text\":\"伊瑞的精通 精制皮甲\",\"flags\":{\"unique\":true}},{\"name\":\"札德图斯的圣衣\",\"type\":\"贤者之袍\",\"text\":\"札德图斯的圣衣 贤者之袍\",\"flags\":{\"unique\":true}},{\"name\":\"泽尔的放大器\",\"type\":\"光辉刺盾\",\"text\":\"泽尔的放大器 光辉刺盾\",\"flags\":{\"unique\":true}}]";
        } else if (itemTag.equals("精华")) {
            return "[{\"type\":\"愤怒之呢喃精华\",\"text\":\"愤怒之呢喃精华\"},{\"type\":\"愤怒之啼泣精华\",\"text\":\"愤怒之啼泣精华\"},{\"type\":\"愤怒之哀嚎精华\",\"text\":\"愤怒之哀嚎精华\"},{\"type\":\"愤怒之咆哮精华\",\"text\":\"愤怒之咆哮精华\"},{\"type\":\"愤怒之尖啸精华\",\"text\":\"愤怒之尖啸精华\"},{\"type\":\"愤怒之破空精华\",\"text\":\"愤怒之破空精华\"},{\"type\":\"煎熬之哀嚎精华\",\"text\":\"煎熬之哀嚎精华\"},{\"type\":\"煎熬之咆哮精华\",\"text\":\"煎熬之咆哮精华\"},{\"type\":\"煎熬之尖啸精华\",\"text\":\"煎熬之尖啸精华\"},{\"type\":\"煎熬之破空精华\",\"text\":\"煎熬之破空精华\"},{\"type\":\"轻视之低语精华\",\"text\":\"轻视之低语精华\"},{\"type\":\"轻视之呢喃精华\",\"text\":\"轻视之呢喃精华\"},{\"type\":\"轻视之啼泣精华\",\"text\":\"轻视之啼泣精华\"},{\"type\":\"轻视之哀嚎精华\",\"text\":\"轻视之哀嚎精华\"},{\"type\":\"轻视之咆哮精华\",\"text\":\"轻视之咆哮精华\"},{\"type\":\"轻视之尖啸精华\",\"text\":\"轻视之尖啸精华\"},{\"type\":\"轻视之破空精华\",\"text\":\"轻视之破空精华\"},{\"type\":\"谵妄精华\",\"text\":\"谵妄精华\"},{\"type\":\"疑惑之啼泣精华\",\"text\":\"疑惑之啼泣精华\"},{\"type\":\"疑惑之哀嚎精华\",\"text\":\"疑惑之哀嚎精华\"},{\"type\":\"疑惑之咆哮精华\",\"text\":\"疑惑之咆哮精华\"},{\"type\":\"疑惑之尖啸精华\",\"text\":\"疑惑之尖啸精华\"},{\"type\":\"疑惑之破空精华\",\"text\":\"疑惑之破空精华\"},{\"type\":\"忌惮之咆哮精华\",\"text\":\"忌惮之咆哮精华\"},{\"type\":\"忌惮之尖啸精华\",\"text\":\"忌惮之尖啸精华\"},{\"type\":\"忌惮之破空精华\",\"text\":\"忌惮之破空精华\"},{\"type\":\"忌妒之咆哮精华\",\"text\":\"忌妒之咆哮精华\"},{\"type\":\"忌妒之尖啸精华\",\"text\":\"忌妒之尖啸精华\"},{\"type\":\"忌妒之破空精华\",\"text\":\"忌妒之破空精华\"},{\"type\":\"恐惧之呢喃精华\",\"text\":\"恐惧之呢喃精华\"},{\"type\":\"恐惧之啼泣精华\",\"text\":\"恐惧之啼泣精华\"},{\"type\":\"恐惧之哀嚎精华\",\"text\":\"恐惧之哀嚎精华\"},{\"type\":\"恐惧之咆哮精华\",\"text\":\"恐惧之咆哮精华\"},{\"type\":\"恐惧之尖啸精华\",\"text\":\"恐惧之尖啸精华\"},{\"type\":\"恐惧之破空精华\",\"text\":\"恐惧之破空精华\"},{\"type\":\"贪婪之低语精华\",\"text\":\"贪婪之低语精华\"},{\"type\":\"贪婪之呢喃精华\",\"text\":\"贪婪之呢喃精华\"},{\"type\":\"贪婪之啼泣精华\",\"text\":\"贪婪之啼泣精华\"},{\"type\":\"贪婪之哀嚎精华\",\"text\":\"贪婪之哀嚎精华\"},{\"type\":\"贪婪之咆哮精华\",\"text\":\"贪婪之咆哮精华\"},{\"type\":\"贪婪之尖啸精华\",\"text\":\"贪婪之尖啸精华\"},{\"type\":\"贪婪之破空精华\",\"text\":\"贪婪之破空精华\"},{\"type\":\"憎恨之低语精华\",\"text\":\"憎恨之低语精华\"},{\"type\":\"憎恨之呢喃精华\",\"text\":\"憎恨之呢喃精华\"},{\"type\":\"憎恨之啼泣精华\",\"text\":\"憎恨之啼泣精华\"},{\"type\":\"憎恨之哀嚎精华\",\"text\":\"憎恨之哀嚎精华\"},{\"type\":\"憎恨之咆哮精华\",\"text\":\"憎恨之咆哮精华\"},{\"type\":\"憎恨之尖啸精华\",\"text\":\"憎恨之尖啸精华\"},{\"type\":\"憎恨之破空精华\",\"text\":\"憎恨之破空精华\"},{\"type\":\"极恐精华\",\"text\":\"极恐精华\"},{\"type\":\"浮夸精华\",\"text\":\"浮夸精华\"},{\"type\":\"错乱精华\",\"text\":\"错乱精华\"},{\"type\":\"厌恶之哀嚎精华\",\"text\":\"厌恶之哀嚎精华\"},{\"type\":\"厌恶之咆哮精华\",\"text\":\"厌恶之咆哮精华\"},{\"type\":\"厌恶之尖啸精华\",\"text\":\"厌恶之尖啸精华\"},{\"type\":\"厌恶之破空精华\",\"text\":\"厌恶之破空精华\"},{\"type\":\"凄惨之咆哮精华\",\"text\":\"凄惨之咆哮精华\"},{\"type\":\"凄惨之尖啸精华\",\"text\":\"凄惨之尖啸精华\"},{\"type\":\"凄惨之破空精华\",\"text\":\"凄惨之破空精华\"},{\"type\":\"肆虐之啼泣精华\",\"text\":\"肆虐之啼泣精华\"},{\"type\":\"肆虐之哀嚎精华\",\"text\":\"肆虐之哀嚎精华\"},{\"type\":\"肆虐之咆哮精华\",\"text\":\"肆虐之咆哮精华\"},{\"type\":\"肆虐之尖啸精华\",\"text\":\"肆虐之尖啸精华\"},{\"type\":\"肆虐之破空精华\",\"text\":\"肆虐之破空精华\"},{\"type\":\"傲视之咆哮精华\",\"text\":\"傲视之咆哮精华\"},{\"type\":\"傲视之尖啸精华\",\"text\":\"傲视之尖啸精华\"},{\"type\":\"傲视之破空精华\",\"text\":\"傲视之破空精华\"},{\"type\":\"哀惜之呢喃精华\",\"text\":\"哀惜之呢喃精华\"},{\"type\":\"哀惜之啼泣精华\",\"text\":\"哀惜之啼泣精华\"},{\"type\":\"哀惜之哀嚎精华\",\"text\":\"哀惜之哀嚎精华\"},{\"type\":\"哀惜之咆哮精华\",\"text\":\"哀惜之咆哮精华\"},{\"type\":\"哀惜之尖啸精华\",\"text\":\"哀惜之尖啸精华\"},{\"type\":\"哀惜之破空精华\",\"text\":\"哀惜之破空精华\"},{\"type\":\"刻毒之哀嚎精华\",\"text\":\"刻毒之哀嚎精华\"},{\"type\":\"刻毒之巨吼精华\",\"text\":\"刻毒之巨吼精华\"},{\"type\":\"刻毒之尖啸精华\",\"text\":\"刻毒之尖啸精华\"},{\"type\":\"刻毒之破空精华\",\"text\":\"刻毒之破空精华\"},{\"type\":\"苦难之啼泣精华\",\"text\":\"苦难之啼泣精华\"},{\"type\":\"苦难之哀嚎精华\",\"text\":\"苦难之哀嚎精华\"},{\"type\":\"苦难之咆哮精华\",\"text\":\"苦难之咆哮精华\"},{\"type\":\"苦难之尖啸精华\",\"text\":\"苦难之尖啸精华\"},{\"type\":\"苦难之破空精华\",\"text\":\"苦难之破空精华\"},{\"type\":\"折磨之呢喃精华\",\"text\":\"折磨之呢喃精华\"},{\"type\":\"折磨之啼泣精华\",\"text\":\"折磨之啼泣精华\"},{\"type\":\"折磨之哀嚎精华\",\"text\":\"折磨之哀嚎精华\"},{\"type\":\"折磨之咆哮精华\",\"text\":\"折磨之咆哮精华\"},{\"type\":\"折磨之尖啸精华\",\"text\":\"折磨之尖啸精华\"},{\"type\":\"折磨之破空精华\",\"text\":\"折磨之破空精华\"},{\"type\":\"悲痛之低语精华\",\"text\":\"悲痛之低语精华\"},{\"type\":\"悲痛之呢喃精华\",\"text\":\"悲痛之呢喃精华\"},{\"type\":\"悲痛之啼泣精华\",\"text\":\"悲痛之啼泣精华\"},{\"type\":\"悲痛之哀嚎精华\",\"text\":\"悲痛之哀嚎精华\"},{\"type\":\"悲痛之咆哮精华\",\"text\":\"悲痛之咆哮精华\"},{\"type\":\"悲痛之尖啸精华\",\"text\":\"悲痛之尖啸精华\"},{\"type\":\"悲痛之破空精华\",\"text\":\"悲痛之破空精华\"},{\"type\":\"雷霆之啼泣精华\",\"text\":\"雷霆之啼泣精华\"},{\"type\":\"雷霆之哀嚎精华\",\"text\":\"雷霆之哀嚎精华\"},{\"type\":\"雷霆之咆哮精华\",\"text\":\"雷霆之咆哮精华\"},{\"type\":\"雷霆之尖啸精华\",\"text\":\"雷霆之尖啸精华\"},{\"type\":\"雷霆之破空精华\",\"text\":\"雷霆之破空精华\"},{\"type\":\"热情之哀嚎精华\",\"text\":\"热情之哀嚎精华\"},{\"type\":\"热情之巨吼精华\",\"text\":\"热情之巨吼精华\"},{\"type\":\"热情之尖啸精华\",\"text\":\"热情之尖啸精华\"},{\"type\":\"热情之破空精华\",\"text\":\"热情之破空精华\"}]";
        } else if (itemTag.equals("化石")) {
            return "[{\"type\":\"镂空化石\",\"text\":\"镂空化石\"},{\"type\":\"狼牙化石\",\"text\":\"狼牙化石\"},{\"type\":\"腐蚀化石\",\"text\":\"腐蚀化石\"},{\"type\":\"以太化石\",\"text\":\"以太化石\"},{\"type\":\"畸变化石\",\"text\":\"畸变化石\"},{\"type\":\"冰冽化石\",\"text\":\"冰冽化石\"},{\"type\":\"雕刻化石\",\"text\":\"雕刻化石\"},{\"type\":\"致密化石\",\"text\":\"致密化石\"},{\"type\":\"五彩化石\",\"text\":\"五彩化石\"},{\"type\":\"魔法化石\",\"text\":\"魔法化石\"},{\"type\":\"炽炎化石\",\"text\":\"炽炎化石\"},{\"type\":\"棱面化石\",\"text\":\"棱面化石\"},{\"type\":\"原始化石\",\"text\":\"原始化石\"},{\"type\":\"金属化石\",\"text\":\"金属化石\"},{\"type\":\"圣洁化石\",\"text\":\"圣洁化石\"},{\"type\":\"透光化石\",\"text\":\"透光化石\"},{\"type\":\"绑缚化石\",\"text\":\"绑缚化石\"},{\"type\":\"分裂化石\",\"text\":\"分裂化石\"},{\"type\":\"锯齿化石\",\"text\":\"锯齿化石\"},{\"type\":\"完美化石\",\"text\":\"完美化石\"},{\"type\":\"纠缠化石\",\"text\":\"纠缠化石\"},{\"type\":\"镶金化石\",\"text\":\"镶金化石\"},{\"type\":\"结壳化石\",\"text\":\"结壳化石\"},{\"type\":\"震颤化石\",\"text\":\"震颤化石\"},{\"type\":\"溅血化石\",\"text\":\"溅血化石\"}]";
        } else if (itemTag.equals("圣油")) {
            return "[{\"type\":\"清澈圣油\",\"text\":\"清澈圣油\"},{\"type\":\"乳白圣油\",\"text\":\"乳白圣油\"},{\"type\":\"白银圣油\",\"text\":\"白银圣油\"},{\"type\":\"金色圣油\",\"text\":\"金色圣油\"},{\"type\":\"墨色圣油\",\"text\":\"墨色圣油\"},{\"type\":\"琥珀圣油\",\"text\":\"琥珀圣油\"},{\"type\":\"翠绿圣油\",\"text\":\"翠绿圣油\"},{\"type\":\"水蓝圣油\",\"text\":\"水蓝圣油\"},{\"type\":\"天蓝圣油\",\"text\":\"天蓝圣油\"},{\"type\":\"靛青圣油\",\"text\":\"靛青圣油\"},{\"type\":\"紫色圣油\",\"text\":\"紫色圣油\"},{\"type\":\"绯红圣油\",\"text\":\"绯红圣油\"},{\"type\":\"漆黑圣油\",\"text\":\"漆黑圣油\"}]";
        } else if (itemTag.equals("共振器")) {
            return "[{\"type\":\"原始混乱共振器\",\"text\":\"原始混乱共振器\"},{\"type\":\"强能混乱共振器\",\"text\":\"强能混乱共振器\"},{\"type\":\"巨能混乱共振器\",\"text\":\"巨能混乱共振器\"},{\"type\":\"威能混乱共振器\",\"text\":\"威能混乱共振器\"},{\"type\":\"原始炼金共振器\",\"text\":\"原始炼金共振器\"},{\"type\":\"强能炼金共振器\",\"text\":\"强能炼金共振器\"},{\"type\":\"巨能炼金共振器\",\"text\":\"巨能炼金共振器\"},{\"type\":\"威能炼金共振器\",\"text\":\"威能炼金共振器\"}]";
        } else if (itemTag.equals("魔瓶")) {
            return "[{\"type\":\"献祭魔瓶\",\"text\":\"献祭魔瓶\"},{\"type\":\"鬼魂魔瓶\",\"text\":\"鬼魂魔瓶\"},{\"type\":\"超越魔瓶\",\"text\":\"超越魔瓶\"},{\"type\":\"命运魔瓶\",\"text\":\"命运魔瓶\"},{\"type\":\"召唤魔瓶\",\"text\":\"召唤魔瓶\"},{\"type\":\"仪祭魔瓶\",\"text\":\"仪祭魔瓶\"},{\"type\":\"结论魔瓶\",\"text\":\"结论魔瓶\"},{\"type\":\"觉醒魔瓶\",\"text\":\"觉醒魔瓶\"},{\"type\":\"统御魔瓶\",\"text\":\"统御魔瓶\"}]";
        } else if (itemTag.equals("孕育石")) {
            return "[{\"type\":\"深渊孕育石\",\"text\":\"深渊孕育石\"},{\"type\":\"占地者的孕育石\",\"text\":\"占地者的孕育石\"},{\"type\":\"众星铸甲匠的孕育石\",\"text\":\"众星铸甲匠的孕育石\"},{\"type\":\"野性的孕育石\",\"text\":\"野性的孕育石\"},{\"type\":\"朦胧孕育石\",\"text\":\"朦胧孕育石\"},{\"type\":\"上等孕育石\",\"text\":\"上等孕育石\"},{\"type\":\"豪华孕育石\",\"text\":\"豪华孕育石\"},{\"type\":\"预言者孕育石\",\"text\":\"预言者孕育石\"},{\"type\":\"低语的孕育石\",\"text\":\"低语的孕育石\"},{\"type\":\"灌注孕育石\",\"text\":\"灌注孕育石\"},{\"type\":\"化石孕育石\",\"text\":\"化石孕育石\"},{\"type\":\"破碎的孕育石\",\"text\":\"破碎的孕育石\"},{\"type\":\"奇术师的孕育石\",\"text\":\"奇术师的孕育石\"},{\"type\":\"宝石匠的孕育石\",\"text\":\"宝石匠的孕育石\"},{\"type\":\"神秘孕育石\",\"text\":\"神秘孕育石\"},{\"type\":\"预兆孕育石\",\"text\":\"预兆孕育石\"},{\"type\":\"附魔孕育石\",\"text\":\"附魔孕育石\"},{\"type\":\"可怕的孕育石\",\"text\":\"可怕的孕育石\"},{\"type\":\"制图师的孕育石\",\"text\":\"制图师的孕育石\"},{\"type\":\"颓废孕育石\",\"text\":\"颓废孕育石\"},{\"type\":\"迷踪孕育石\",\"text\":\"迷踪孕育石\"},{\"type\":\"原始孕育石\",\"text\":\"原始孕育石\"},{\"type\":\"众星珠宝匠的孕育石\",\"text\":\"众星珠宝匠的孕育石\"},{\"type\":\"失落的孕育石\",\"text\":\"失落的孕育石\"},{\"type\":\"异界行者孕育石\",\"text\":\"异界行者孕育石\"},{\"type\":\"单一的的孕育石\",\"text\":\"单一的的孕育石\"},{\"type\":\"众星铁匠的孕育石\",\"text\":\"众星铁匠的孕育石\"}]";
        } else if (itemTag.equals("梦魇宝珠")) {
            return "[{\"type\":\"深渊的雾魇宝珠\",\"text\":\"深渊的雾魇宝珠\"},{\"type\":\"铸甲师的雾魇宝珠\",\"text\":\"铸甲师的雾魇宝珠\"},{\"type\":\"枯萎的雾魇宝珠\",\"text\":\"枯萎的雾魇宝珠\"},{\"type\":\"朦胧的雾魇宝珠\",\"text\":\"朦胧的雾魇宝珠\"},{\"type\":\"良好的雾魇宝珠\",\"text\":\"良好的雾魇宝珠\"},{\"type\":\"预言师的雾魇宝珠\",\"text\":\"预言师的雾魇宝珠\"},{\"type\":\"低语的雾魇宝珠\",\"text\":\"低语的雾魇宝珠\"},{\"type\":\"石化的雾魇宝珠\",\"text\":\"石化的雾魇宝珠\"},{\"type\":\"破碎的雾魇宝珠\",\"text\":\"破碎的雾魇宝珠\"},{\"type\":\"奇术师的雾魇宝珠\",\"text\":\"奇术师的雾魇宝珠\"},{\"type\":\"不详的雾魇宝珠\",\"text\":\"不详的雾魇宝珠\"},{\"type\":\"永恒的雾魇宝珠\",\"text\":\"永恒的雾魇宝珠\"},{\"type\":\"帝国的雾魇宝珠\",\"text\":\"帝国的雾魇宝珠\"},{\"type\":\"制图师的雾魇宝珠\",\"text\":\"制图师的雾魇宝珠\"},{\"type\":\"无形的雾魇宝珠\",\"text\":\"无形的雾魇宝珠\"},{\"type\":\"颓败的雾魇宝珠\",\"text\":\"颓败的雾魇宝珠\"},{\"type\":\"预兆的雾魇宝珠\",\"text\":\"预兆的雾魇宝珠\"},{\"type\":\"迷踪的雾魇宝珠\",\"text\":\"迷踪的雾魇宝珠\"},{\"type\":\"原始的雾魇宝珠\",\"text\":\"原始的雾魇宝珠\"},{\"type\":\"珠宝匠的雾魇宝珠\",\"text\":\"珠宝匠的雾魇宝珠\"},{\"type\":\"单独的雾魇宝珠\",\"text\":\"单独的雾魇宝珠\"},{\"type\":\"铁匠的雾魇宝珠\",\"text\":\"铁匠的雾魇宝珠\"},{\"type\":\"梦魇拟像裂片\",\"text\":\"梦魇拟像裂片\"}]";
        } else if (itemTag.equals("通货")) {
            return "[{\"type\":\"圣战者的崇高石\",\"text\":\"圣战者的崇高石\"},{\"type\":\"猎人的崇高石\",\"text\":\"猎人的崇高石\"},{\"type\":\"救赎者的崇高石\",\"text\":\"救赎者的崇高石\"},{\"type\":\"督军的崇高石\",\"text\":\"督军的崇高石\"},{\"type\":\"觉醒者之石\",\"text\":\"觉醒者之石\"},{\"type\":\"简易六分仪\",\"text\":\"简易六分仪\"},{\"type\":\"觉醒六分仪\",\"text\":\"觉醒六分仪\"},{\"type\":\"原始六分仪\",\"text\":\"原始六分仪\"},{\"type\":\"费斯特的透镜\",\"text\":\"费斯特的透镜\"},{\"type\":\"崇高石\",\"text\":\"崇高石\"},{\"type\":\"崇高石碎片\",\"text\":\"崇高石碎片\"},{\"type\":\"遗忘的腐化器皿\",\"text\":\"遗忘的腐化器皿\"},{\"type\":\"卡兰德的魔镜\",\"text\":\"卡兰德的魔镜\"},{\"type\":\"卡兰德的魔镜碎片\",\"text\":\"卡兰德的魔镜碎片\"},{\"type\":\"裁剪石\",\"text\":\"裁剪石\"},{\"type\":\"回火石\",\"text\":\"回火石\"},{\"type\":\"神圣石\",\"text\":\"神圣石\"},{\"type\":\"后悔石\",\"text\":\"后悔石\"},{\"type\":\"剥离石\",\"text\":\"剥离石\"},{\"type\":\"剥离石碎片\",\"text\":\"剥离石碎片\"},{\"type\":\"远古石\",\"text\":\"远古石\"},{\"type\":\"远古石碎片\",\"text\":\"远古石碎片\"},{\"type\":\"裂界之玉\",\"text\":\"裂界之玉\"},{\"type\":\"白化的羽毛\",\"text\":\"白化的羽毛\"},{\"type\":\"银币\",\"text\":\"银币\"},{\"type\":\"时光卷轴\",\"text\":\"时光卷轴\"},{\"type\":\"解除卷轴\",\"text\":\"解除卷轴\"},{\"type\":\"破碎卷轴\",\"text\":\"破碎卷轴\"},{\"type\":\"光棱卷轴\",\"text\":\"光棱卷轴\"},{\"type\":\"焚血卷轴\",\"text\":\"焚血卷轴\"},{\"type\":\"电击卷轴\",\"text\":\"电击卷轴\"},{\"type\":\"研磨催化剂\",\"text\":\"研磨催化剂\"},{\"type\":\"内在催化剂\",\"text\":\"内在催化剂\"},{\"type\":\"灌注催化剂\",\"text\":\"灌注催化剂\"},{\"type\":\"回火催化剂\",\"text\":\"回火催化剂\"},{\"type\":\"猛烈催化剂\",\"text\":\"猛烈催化剂\"},{\"type\":\"棱光催化剂\",\"text\":\"棱光催化剂\"},{\"type\":\"丰沃催化剂\",\"text\":\"丰沃催化剂\"},{\"type\":\"主要矫正透镜\",\"text\":\"主要矫正透镜\"},{\"type\":\"次要矫正透镜\",\"text\":\"次要矫正透镜\"},{\"type\":\"未知的命运卡\",\"text\":\"未知的命运卡\"},{\"type\":\"赏金猎人印记\",\"text\":\"赏金猎人印记\"}]";
        } else if (itemTag.equals("碎片")) {
            return "[{\"name\":\"秘法先驱者碎片I\",\"type\":\"军团长剑(碎片)\",\"text\":\"秘法先驱者碎片I 军团长剑(碎片)\",\"flags\":{\"unique\":true}},{\"name\":\"残暴先驱者碎片I\",\"type\":\"帝国长杖(碎片)\",\"text\":\"残暴先驱者碎片I 帝国长杖(碎片)\",\"flags\":{\"unique\":true}},{\"name\":\"射术先驱者碎片I\",\"type\":\"钝矢箭袋(碎片)\",\"text\":\"射术先驱者碎片I 钝矢箭袋(碎片)\",\"flags\":{\"unique\":true}},{\"name\":\"不屈先驱者I\",\"type\":\"威能鸢盾(碎片)\",\"text\":\"不屈先驱者I 威能鸢盾(碎片)\",\"flags\":{\"unique\":true}},{\"name\":\"冰雷先驱者I\",\"type\":\"无情之面(碎片)\",\"text\":\"冰雷先驱者I 无情之面(碎片)\",\"flags\":{\"unique\":true}},{\"name\":\"时空先驱者碎片I\",\"type\":\"饰布腰带(碎片)\",\"text\":\"时空先驱者碎片I 饰布腰带(碎片)\",\"flags\":{\"unique\":true}},{\"name\":\"不屈先驱者IV\",\"type\":\"威能鸢盾(碎片)\",\"text\":\"不屈先驱者IV 威能鸢盾(碎片)\",\"flags\":{\"unique\":true}},{\"name\":\"秘法先驱者碎片II\",\"type\":\"军团长剑(碎片)\",\"text\":\"秘法先驱者碎片II 军团长剑(碎片)\",\"flags\":{\"unique\":true}},{\"name\":\"残暴先驱者碎片II\",\"type\":\"帝国长杖(碎片)\",\"text\":\"残暴先驱者碎片II 帝国长杖(碎片)\",\"flags\":{\"unique\":true}},{\"name\":\"射术先驱者碎片II\",\"type\":\"钝矢箭袋(碎片)\",\"text\":\"射术先驱者碎片II 钝矢箭袋(碎片)\",\"flags\":{\"unique\":true}},{\"name\":\"不屈先驱者II\",\"type\":\"威能鸢盾(碎片)\",\"text\":\"不屈先驱者II 威能鸢盾(碎片)\",\"flags\":{\"unique\":true}},{\"name\":\"冰雷先驱者II\",\"type\":\"无情之面(碎片)\",\"text\":\"冰雷先驱者II 无情之面(碎片)\",\"flags\":{\"unique\":true}},{\"name\":\"时空先驱者碎片II\",\"type\":\"饰布腰带(碎片)\",\"text\":\"时空先驱者碎片II 饰布腰带(碎片)\",\"flags\":{\"unique\":true}},{\"name\":\"秘法先驱者碎片III\",\"type\":\"军团长剑(碎片)\",\"text\":\"秘法先驱者碎片III 军团长剑(碎片)\",\"flags\":{\"unique\":true}},{\"name\":\"残暴先驱者碎片III\",\"type\":\"帝国长杖(碎片)\",\"text\":\"残暴先驱者碎片III 帝国长杖(碎片)\",\"flags\":{\"unique\":true}},{\"name\":\"射术先驱者碎片III\",\"type\":\"钝矢箭袋(碎片)\",\"text\":\"射术先驱者碎片III 钝矢箭袋(碎片)\",\"flags\":{\"unique\":true}},{\"name\":\"不屈先驱者III\",\"type\":\"威能鸢盾(碎片)\",\"text\":\"不屈先驱者III 威能鸢盾(碎片)\",\"flags\":{\"unique\":true}},{\"name\":\"冰雷先驱者III\",\"type\":\"无情之面(碎片)\",\"text\":\"冰雷先驱者III 无情之面(碎片)\",\"flags\":{\"unique\":true}},{\"type\":\"裂隙碎片(夏乌拉)\",\"text\":\"裂隙碎片(夏乌拉)\"},{\"type\":\"裂隙碎片(托沃)\",\"text\":\"裂隙碎片(托沃)\"},{\"type\":\"裂隙碎片(索伏)\",\"text\":\"裂隙碎片(索伏)\"},{\"type\":\"裂隙碎片(艾许)\",\"text\":\"裂隙碎片(艾许)\"},{\"type\":\"裂隙碎片(乌尔尼多)\",\"text\":\"裂隙碎片(乌尔尼多)\"},{\"type\":\"夏乌拉的祝福\",\"text\":\"夏乌拉的祝福\"},{\"type\":\"托沃的祝福\",\"text\":\"托沃的祝福\"},{\"type\":\"索伏的祝福\",\"text\":\"索伏的祝福\"},{\"type\":\"艾许的祝福\",\"text\":\"艾许的祝福\"},{\"type\":\"乌尔尼多的祝福\",\"text\":\"乌尔尼多的祝福\"},{\"type\":\"永恒帝国裂片\",\"text\":\"永恒帝国裂片\"},{\"type\":\"永恒卡鲁裂片\",\"text\":\"永恒卡鲁裂片\"},{\"type\":\"永恒马拉克斯裂片\",\"text\":\"永恒马拉克斯裂片\"},{\"type\":\"永恒圣堂裂片\",\"text\":\"永恒圣堂裂片\"},{\"type\":\"永恒瓦尔裂片\",\"text\":\"永恒瓦尔裂片\"}]";
        } else {
            return "";
        }
    }

    // 技能石
    // @Scheduled(cron = "0 0 0/4 * * *")
    public void gemTimer(PoeItemPriceService poeItemPriceService) throws InterruptedException {
        String item_tag = "技能宝石";
        // 暂时禁用技能石的设置
        if (1 == 1) {
            return;
        }
        JSONArray jsonArray = JSON.parseArray(getItemJsonByItemTag(item_tag));
        JSONObject jo = getSearchObjectByItemTag(item_tag);
        for (int i = 0; i < jsonArray.size(); i++) {
            for (int typeOption = 0; typeOption <= 3; typeOption++) {
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
                    // 获取结果集总数
                    int totalResult = Integer.parseInt(JSONObject.parseObject(itemsJson).get("total").toString());
                    // 通过JsonArray取出前10个物品的uuid,并用,拼接
                    int totalItem = Math.min(10, ja.size());

                    String suffix = ArrayUtil.join(ArrayUtil.resize(ja.toArray(), totalItem), ",");
                    // 根据uuid字串获取物品实际数据json串
                    String getUrl = fetchUrl + suffix;
                    Thread.sleep(200);
                    String s1 = HttpUtil
                            .get(getUrl);
                    // 将json串转换为JSONObject
                    JSONObject jo2 = JSONObject.parseObject(s1);
                    // 计算价格平均值
                    int avgPrice = 0;
                    int avgNum = totalItem;
                    if (avgNum >= 10) {
                        System.out.println("-------------- " + gemName + " -----------------------");
                        for (int index = 0; index < totalItem; index++) {
                            Double curPriceDouble = Double.parseDouble(jo2.getJSONArray("result").getJSONObject(index).getJSONObject("listing").getJSONObject("price").get("amount").toString());
                            int curPrice = curPriceDouble.intValue();
                            String curPriceType = jo2.getJSONArray("result").getJSONObject(index).getJSONObject("listing").getJSONObject("price").get("currency").toString();
                            if (curPriceType.equals("chaos")) {
                                avgPrice = avgPrice + curPrice;
                            } else if (curPriceType.equals("exalted")) {
                                avgPrice = avgPrice + curPrice * exaltedPrice;
                            } else {
                                if (avgNum > 1) {
                                    avgNum -= 1;
                                }
                            }
                        }
                        avgPrice = avgPrice / avgNum;
                        PoeItemPrice pip = new PoeItemPrice();
                        pip.setItemCurPrice(avgPrice + " 混沌石");
                        Date dNow = new Date(); // 当前时间
                        String formatTime = df.format(dNow);
                        pip.setItemRecordTime(formatTime);
                        pip.setItemType(item_tag);
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
                        poeItemPriceService.addNewItem(pip);
                    } else {
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("item_name", gemName);
                        if (typeOption == 0) {
                            map.put("item_desc", "精良的");
                        } else if (typeOption == 1) {
                            map.put("item_desc", "异常");
                        } else if (typeOption == 2) {
                            map.put("item_desc", "分歧");
                        } else {
                            map.put("item_desc", "魅影");
                        }
                        poeItemPriceService.removeByMap(map);
                    }
                } catch (Exception e) {
                    System.out.println("异常");
                    int relaxTime = CommonUtil.genRandom(100, 1000);
                    Thread.sleep(relaxTime);
                    e.printStackTrace();
                }
                Thread.sleep(500);
            }
        }
    }

    // 预言
    // @Scheduled(cron = "0 56 0/3 * * *")
    public void prophecyTimer(PoeItemPriceService poeItemPriceService) throws InterruptedException {
        String item_tag = "预言";
        JSONArray jsonArray = JSON.parseArray(getItemJsonByItemTag(item_tag));
        JSONObject jo = getSearchObjectByItemTag(item_tag);
        for (int i = 0; i < jsonArray.size(); i++) {
            try {
                // 获取预言的名字
                String prophecyName = (String) jsonArray.getJSONObject(i).get("name");
                String prophecyText = (String) jsonArray.getJSONObject(i).get("text");
                // 将预言的名字填充进入jsonObject
                jo.getJSONObject("query")
                        .getJSONObject("name")
                        .put("option", prophecyName);
                jo.getJSONObject("query")
                        .getJSONObject("type")
                        .put("option", "预言");
                if (prophecyText.equals("地方的大师需要支持 预言 (尼克)")) {
                    jo.getJSONObject("query")
                            .getJSONObject("name")
                            .put("discriminator", "niko");
                } else if (prophecyText.equals("地方的大师需要支持 预言 (伊恩哈尔)")) {
                    jo.getJSONObject("query")
                            .getJSONObject("name")
                            .put("discriminator", "einhar");
                } else if (prophecyText.equals("地方的大师需要支持 预言 (6月)")) {
                    jo.getJSONObject("query")
                            .getJSONObject("name")
                            .put("discriminator", "jun");
                } else if (prophecyText.equals("地方的大师需要支持 预言 (札娜)")) {
                    jo.getJSONObject("query")
                            .getJSONObject("name")
                            .put("discriminator", "zana");
                } else if (prophecyText.equals("地方的大师需要支持 预言 (阿尔瓦)")) {
                    jo.getJSONObject("query")
                            .getJSONObject("name")
                            .put("discriminator", "alva");
                } else {
                    jo.getJSONObject("query")
                            .getJSONObject("name")
                            .put("discriminator", "");
                }
                // 将jsonObject转化为json串
                String s = JSONObject.toJSONString(jo);
                // 通过HuTool发送Post请求获取物品数据集合
                String itemsJson = HttpRequest.post(url)
                        //.header(Header.USER_AGENT, "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.141 Safari/537.36")
                        .timeout(500)
                        .body(s)
                        .execute().body();
                System.out.println("json ========> " + itemsJson);
                // 将获取到的物品数据集合转化为jsonArray
                JSONArray ja = JSONArray.parseArray(JSONObject.parseObject(itemsJson).get("result").toString());
                // 获取结果集总数
                int totalResult = Integer.parseInt(JSONObject.parseObject(itemsJson).get("total").toString());
                // 通过JsonArray取出前10个物品的uuid,并用,拼接
                int totalItem = Math.min(10, ja.size());

                String suffix = ArrayUtil.join(ArrayUtil.resize(ja.toArray(), totalItem), ",");
                // 根据uuid字串获取物品实际数据json串
                String getUrl = fetchUrl + suffix;
                Thread.sleep(200);
                String s1 = HttpUtil
                        .get(getUrl);
                // 将json串转换为JSONObject
                JSONObject jo2 = JSONObject.parseObject(s1);
                // 计算价格平均值
                int avgPrice = 0;
                int avgNum = totalItem;
                System.out.println("avgNum ========> " + avgNum);
                if (avgNum >= 1) {
                    System.out.println("-------------- " + prophecyName + " -----------------------");
                    for (int index = 0; index < totalItem; index++) {
                        Double curPriceDouble = Double.parseDouble(jo2.getJSONArray("result").getJSONObject(index).getJSONObject("listing").getJSONObject("price").get("amount").toString());
                        int curPrice = curPriceDouble.intValue();
                        String curPriceType = jo2.getJSONArray("result").getJSONObject(index).getJSONObject("listing").getJSONObject("price").get("currency").toString();
                        if (curPriceType.equals("chaos")) {
                            avgPrice = avgPrice + curPrice;
                        } else if (curPriceType.equals("exalted")) {
                            avgPrice = avgPrice + curPrice * exaltedPrice;
                        } else {
                            if (avgNum > 1) {
                                avgNum -= 1;
                            }
                        }
                    }
                    avgPrice = avgPrice / avgNum;
                    PoeItemPrice pip = new PoeItemPrice();
                    pip.setItemCurPrice(avgPrice + " 混沌石");
                    Date dNow = new Date(); // 当前时间
                    String formatTime = df.format(dNow);
                    pip.setItemRecordTime(formatTime);
                    pip.setItemType(item_tag);
                    pip.setItemDesc(prophecyText);
                    if (prophecyName.equals("地方的大师需要支持")) {
                        if (prophecyText.equals("地方的大师需要支持 预言 (尼克)")) {
                            pip.setItemName(prophecyText);
                        } else if (prophecyText.equals("地方的大师需要支持 预言 (伊恩哈尔)")) {
                            pip.setItemName(prophecyText);
                        } else if (prophecyText.equals("地方的大师需要支持 预言 (6月)")) {
                            pip.setItemName(prophecyText);
                        } else if (prophecyText.equals("地方的大师需要支持 预言 (札娜)")) {
                            pip.setItemName(prophecyText);
                        } else if (prophecyText.equals("地方的大师需要支持 预言 (阿尔瓦)")) {
                            pip.setItemName(prophecyText);
                        } else {
                            pip.setItemName(prophecyName);
                        }
                    } else {
                        pip.setItemName(prophecyName);
                    }
                    // 是否流行
                    if (totalResult > 10) {
                        pip.setItemIsPopular("是");
                    } else {
                        pip.setItemIsPopular("否");
                    }
                    pip.setItemFilters("");
                    poeItemPriceService.addNewItem(pip);
                } else {
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("item_name", prophecyName);
                    map.put("item_desc", prophecyText);
                    poeItemPriceService.removeByMap(map);
                }
            } catch (Exception e) {
                System.out.println("异常");
                int relaxTime = CommonUtil.genRandom(100, 1000);
                Thread.sleep(relaxTime);
                e.printStackTrace();
            }
            Thread.sleep(500);
        }
    }

    // 传奇地图
    // @Scheduled(cron = "0 0 0/4 * * *")
    public void uniqueMapTimer(PoeItemPriceService poeItemPriceService) throws InterruptedException {
        String item_tag = "传奇地图";
        JSONArray jsonArray = JSON.parseArray(getItemJsonByItemTag(item_tag));
        JSONObject jo = getSearchObjectByItemTag(item_tag);
        for (int i = 0; i < jsonArray.size(); i++) {
            for (int j = 1; j <= 16; j++) {
                try {
                    // 获取预言的名字
                    String uniqueMapName = (String) jsonArray.getJSONObject(i).get("name");
                    String uniqueMapType = (String) jsonArray.getJSONObject(i).get("type");
                    // 将预言的名字填充进入jsonObject
                    jo.getJSONObject("query")
                            .getJSONObject("name")
                            .put("option", uniqueMapName);
                    jo.getJSONObject("query")
                            .getJSONObject("type")
                            .put("option", uniqueMapType);
                    jo.getJSONObject("query")
                            .getJSONObject("filters")
                            .getJSONObject("map_filters")
                            .getJSONObject("filters")
                            .getJSONObject("map_tier")
                            .put("min", j);
                    jo.getJSONObject("query")
                            .getJSONObject("filters")
                            .getJSONObject("map_filters")
                            .getJSONObject("filters")
                            .getJSONObject("map_tier")
                            .put("max", j);
                    // 将jsonObject转化为json串
                    String s = JSONObject.toJSONString(jo);
                    // 通过HuTool发送Post请求获取物品数据集合
                    String itemsJson = HttpRequest.post(url)
                            .timeout(500)
                            .body(s)
                            .execute().body();
                    System.out.println("json ========> " + itemsJson);
                    // 将获取到的物品数据集合转化为jsonArray
                    JSONArray ja = JSONArray.parseArray(JSONObject.parseObject(itemsJson).get("result").toString());
                    // 获取结果集总数
                    int totalResult = Integer.parseInt(JSONObject.parseObject(itemsJson).get("total").toString());
                    // 通过JsonArray取出前10个物品的uuid,并用,拼接
                    int totalItem = Math.min(10, ja.size());

                    String suffix = ArrayUtil.join(ArrayUtil.resize(ja.toArray(), totalItem), ",");
                    // 根据uuid字串获取物品实际数据json串
                    String getUrl = fetchUrl + suffix;
                    Thread.sleep(200);
                    String s1 = HttpUtil
                            .get(getUrl);
                    // 将json串转换为JSONObject
                    JSONObject jo2 = JSONObject.parseObject(s1);
                    // 计算价格平均值
                    int avgPrice = 0;
                    int avgNum = totalItem;
                    System.out.println("avgNum ========> " + avgNum);
                    if (avgNum >= 1) {
                        System.out.println("-------------- " + uniqueMapName + " -----------------------");
                        for (int index = 0; index < totalItem; index++) {
                            Double curPriceDouble = Double.parseDouble(jo2.getJSONArray("result").getJSONObject(index).getJSONObject("listing").getJSONObject("price").get("amount").toString());
                            int curPrice = curPriceDouble.intValue();
                            String curPriceType = jo2.getJSONArray("result").getJSONObject(index).getJSONObject("listing").getJSONObject("price").get("currency").toString();
                            if (curPriceType.equals("chaos")) {
                                avgPrice = avgPrice + curPrice;
                            } else if (curPriceType.equals("exalted")) {
                                avgPrice = avgPrice + curPrice * exaltedPrice;
                            } else {
                                if (avgNum > 1) {
                                    avgNum -= 1;
                                }
                            }
                        }
                        avgPrice = avgPrice / avgNum;
                        PoeItemPrice pip = new PoeItemPrice();
                        pip.setItemCurPrice(avgPrice + " 混沌石");
                        Date dNow = new Date(); // 当前时间
                        String formatTime = df.format(dNow);
                        pip.setItemRecordTime(formatTime);
                        pip.setItemType(item_tag);
                        pip.setItemDesc("T" + j);
                        pip.setItemName(uniqueMapName);
                        // 是否流行
                        if (totalResult > 10) {
                            pip.setItemIsPopular("是");
                        } else {
                            pip.setItemIsPopular("否");
                        }
                        pip.setItemFilters("");
                        poeItemPriceService.addNewItem(pip);
                    } else {
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("item_name", uniqueMapName);
                        map.put("item_desc", "T" + j);
                        poeItemPriceService.removeByMap(map);
                    }
                } catch (Exception e) {
                    System.out.println("异常");
                    int relaxTime = CommonUtil.genRandom(100, 1000);
                    Thread.sleep(relaxTime);
                    e.printStackTrace();
                }
                Thread.sleep(500);
            }

        }
    }

    // 珠宝
    // @Scheduled(cron = "0 0 0/4 * * *")
    public void jewelTimer(PoeItemPriceService poeItemPriceService) throws InterruptedException {
        String item_tag = "珠宝";
        JSONArray jsonArray = JSON.parseArray(getItemJsonByItemTag(item_tag));
        JSONObject jo = getSearchObjectByItemTag(item_tag);
        for (int i = 0; i < jsonArray.size(); i++) {
            try {
                // 获取预言的名字
                String jewelName = (String) jsonArray.getJSONObject(i).get("name");
                String jewelType = (String) jsonArray.getJSONObject(i).get("type");
                // 将预言的名字填充进入jsonObject
                jo.getJSONObject("query")
                        .put("name", jewelName);
                jo.getJSONObject("query")
                        .put("type", jewelType);
                // 将jsonObject转化为json串
                String s = JSONObject.toJSONString(jo);
                // 通过HuTool发送Post请求获取物品数据集合
                String itemsJson = HttpRequest.post(url)
                        .timeout(500)
                        .body(s)
                        .execute().body();
                System.out.println("json ========> " + itemsJson);
                // 将获取到的物品数据集合转化为jsonArray
                JSONArray ja = JSONArray.parseArray(JSONObject.parseObject(itemsJson).get("result").toString());
                // 获取结果集总数
                int totalResult = Integer.parseInt(JSONObject.parseObject(itemsJson).get("total").toString());
                // 通过JsonArray取出前10个物品的uuid,并用,拼接
                int totalItem = Math.min(10, ja.size());

                String suffix = ArrayUtil.join(ArrayUtil.resize(ja.toArray(), totalItem), ",");
                // 根据uuid字串获取物品实际数据json串
                String getUrl = fetchUrl + suffix;
                Thread.sleep(200);
                String s1 = HttpUtil
                        .get(getUrl);
                // 将json串转换为JSONObject
                JSONObject jo2 = JSONObject.parseObject(s1);
                // 计算价格平均值
                int avgPrice = 0;
                int avgNum = totalItem;
                System.out.println("avgNum ========> " + avgNum);
                if (avgNum >= 1) {
                    System.out.println("-------------- " + jewelName + " -----------------------");
                    for (int index = 0; index < totalItem; index++) {
                        Double curPriceDouble = Double.parseDouble(jo2.getJSONArray("result").getJSONObject(index).getJSONObject("listing").getJSONObject("price").get("amount").toString());
                        int curPrice = curPriceDouble.intValue();
                        String curPriceType = jo2.getJSONArray("result").getJSONObject(index).getJSONObject("listing").getJSONObject("price").get("currency").toString();
                        if (curPriceType.equals("chaos")) {
                            avgPrice = avgPrice + curPrice;
                        } else if (curPriceType.equals("exalted")) {
                            avgPrice = avgPrice + curPrice * exaltedPrice;
                        } else {
                            if (avgNum > 1) {
                                avgNum -= 1;
                            }
                        }
                    }
                    avgPrice = avgPrice / avgNum;
                    PoeItemPrice pip = new PoeItemPrice();
                    pip.setItemCurPrice(avgPrice + " 混沌石");
                    Date dNow = new Date(); // 当前时间
                    String formatTime = df.format(dNow);
                    pip.setItemRecordTime(formatTime);
                    pip.setItemType(item_tag);
                    pip.setItemDesc(jewelType);
                    pip.setItemName(jewelName);
                    // 是否流行
                    if (totalResult > 10) {
                        pip.setItemIsPopular("是");
                    } else {
                        pip.setItemIsPopular("否");
                    }
                    pip.setItemFilters("");
                    poeItemPriceService.addNewItem(pip);
                } else {
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("item_name", jewelName);
                    map.put("item_desc", jewelType);
                    poeItemPriceService.removeByMap(map);
                }
            } catch (Exception e) {
                System.out.println("异常");
                int relaxTime = CommonUtil.genRandom(100, 1000);
                Thread.sleep(relaxTime);
                e.printStackTrace();
            }
            Thread.sleep(500);

        }
    }

    // 命运卡
    // @Scheduled(cron = "0 0 0/4 * * *")
    public void cardTimer(PoeItemPriceService poeItemPriceService) throws InterruptedException {
        String item_tag = "命运卡";
        JSONArray jsonArray = JSON.parseArray(getItemJsonByItemTag(item_tag));
        JSONObject jo = getSearchObjectByItemTag(item_tag);
        for (int i = 0; i < jsonArray.size(); i++) {
            try {
                // 获取物品的名字
                // String cardName = (String) jsonArray.getJSONObject(i).get("name");
                String cardType = (String) jsonArray.getJSONObject(i).get("type");
                // 将物品的名字填充进入jsonObject
                jo.getJSONObject("query")
                        .put("type", cardType);
                // 将jsonObject转化为json串
                String s = JSONObject.toJSONString(jo);
                // 通过HuTool发送Post请求获取物品数据集合
                String itemsJson = HttpRequest.post(url)
                        .timeout(500)
                        .body(s)
                        .execute().body();
                System.out.println("json ========> " + itemsJson);
                // 将获取到的物品数据集合转化为jsonArray
                JSONArray ja = JSONArray.parseArray(JSONObject.parseObject(itemsJson).get("result").toString());
                // 获取结果集总数
                int totalResult = Integer.parseInt(JSONObject.parseObject(itemsJson).get("total").toString());
                // 通过JsonArray取出前10个物品的uuid,并用,拼接
                int totalItem = Math.min(10, ja.size());

                String suffix = ArrayUtil.join(ArrayUtil.resize(ja.toArray(), totalItem), ",");
                // 根据uuid字串获取物品实际数据json串
                String getUrl = fetchUrl + suffix;
                Thread.sleep(200);
                String s1 = HttpUtil
                        .get(getUrl);
                // 将json串转换为JSONObject
                JSONObject jo2 = JSONObject.parseObject(s1);
                // 计算价格平均值
                int avgPrice = 0;
                int avgNum = totalItem;
                System.out.println("avgNum ========> " + avgNum);
                if (avgNum >= 1) {
                    System.out.println("-------------- " + cardType + " -----------------------");
                    for (int index = 0; index < totalItem; index++) {
                        Double curPriceDouble = Double.parseDouble(jo2.getJSONArray("result").getJSONObject(index).getJSONObject("listing").getJSONObject("price").get("amount").toString());
                        int curPrice = curPriceDouble.intValue();
                        String curPriceType = jo2.getJSONArray("result").getJSONObject(index).getJSONObject("listing").getJSONObject("price").get("currency").toString();
                        if (curPriceType.equals("chaos")) {
                            avgPrice = avgPrice + curPrice;
                        } else if (curPriceType.equals("exalted")) {
                            avgPrice = avgPrice + curPrice * exaltedPrice;
                        } else {
                            if (avgNum > 1) {
                                avgNum -= 1;
                            }
                        }
                    }
                    avgPrice = avgPrice / avgNum;
                    PoeItemPrice pip = new PoeItemPrice();
                    pip.setItemCurPrice(avgPrice + " 混沌石");
                    Date dNow = new Date(); // 当前时间
                    String formatTime = df.format(dNow);
                    pip.setItemRecordTime(formatTime);
                    pip.setItemType(item_tag);
                    pip.setItemDesc("");
                    pip.setItemName(cardType);
                    // 是否流行
                    if (totalResult > 10) {
                        pip.setItemIsPopular("是");
                    } else {
                        pip.setItemIsPopular("否");
                    }
                    pip.setItemFilters("");
                    poeItemPriceService.addNewItem(pip);
                } else {
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("item_name", cardType);
                    map.put("item_desc", "");
                    poeItemPriceService.removeByMap(map);
                }
            } catch (Exception e) {
                System.out.println("异常");
                int relaxTime = CommonUtil.genRandom(100, 1000);
                Thread.sleep(relaxTime);
                e.printStackTrace();
            }
            Thread.sleep(500);

        }
    }

    // 药剂
    // @Scheduled(cron = "0 0 0/4 * * *")
    public void flaskTimer(PoeItemPriceService poeItemPriceService) throws InterruptedException {
        String item_tag = "药剂";
        JSONArray jsonArray = JSON.parseArray(getItemJsonByItemTag(item_tag));
        JSONObject jo = getSearchObjectByItemTag(item_tag);
        for (int i = 0; i < jsonArray.size(); i++) {
            try {
                // 获取物品的名字
                String flaskName = (String) jsonArray.getJSONObject(i).get("name");
                String flaskType = (String) jsonArray.getJSONObject(i).get("type");
                // 将物品的名字填充进入jsonObject
                jo.getJSONObject("query")
                        .put("name", flaskName);
                jo.getJSONObject("query")
                        .put("type", flaskType);
                // 将jsonObject转化为json串
                String s = JSONObject.toJSONString(jo);
                // 通过HuTool发送Post请求获取物品数据集合
                String itemsJson = HttpRequest.post(url)
                        .timeout(500)
                        .body(s)
                        .execute().body();
                System.out.println("json ========> " + itemsJson);
                // 将获取到的物品数据集合转化为jsonArray
                JSONArray ja = JSONArray.parseArray(JSONObject.parseObject(itemsJson).get("result").toString());
                // 获取结果集总数
                int totalResult = Integer.parseInt(JSONObject.parseObject(itemsJson).get("total").toString());
                System.out.println(totalResult + "++++++++++++++++++++++");
                // 通过JsonArray取出前10个物品的uuid,并用,拼接
                int totalItem = Math.min(10, ja.size());

                String suffix = ArrayUtil.join(ArrayUtil.resize(ja.toArray(), totalItem), ",");
                // 根据uuid字串获取物品实际数据json串
                String getUrl = fetchUrl + suffix;
                Thread.sleep(200);
                String s1 = HttpUtil
                        .get(getUrl);
                // 将json串转换为JSONObject
                JSONObject jo2 = JSONObject.parseObject(s1);
                // 计算价格平均值
                int avgPrice = 0;
                int avgNum = totalItem;
                System.out.println("avgNum ========> " + avgNum);
                if (avgNum >= 1) {
                    System.out.println("-------------- " + flaskName + " -----------------------");
                    for (int index = 0; index < totalItem; index++) {
                        Double curPriceDouble = Double.parseDouble(jo2.getJSONArray("result").getJSONObject(index).getJSONObject("listing").getJSONObject("price").get("amount").toString());
                        int curPrice = curPriceDouble.intValue();
                        String curPriceType = jo2.getJSONArray("result").getJSONObject(index).getJSONObject("listing").getJSONObject("price").get("currency").toString();
                        if (curPriceType.equals("chaos")) {
                            avgPrice = avgPrice + curPrice;
                        } else if (curPriceType.equals("exalted")) {
                            avgPrice = avgPrice + curPrice * exaltedPrice;
                        } else {
                            if (avgNum > 1) {
                                avgNum -= 1;
                            }
                        }
                    }
                    avgPrice = avgPrice / avgNum;
                    PoeItemPrice pip = new PoeItemPrice();
                    pip.setItemCurPrice(avgPrice + " 混沌石");
                    Date dNow = new Date(); // 当前时间
                    String formatTime = df.format(dNow);
                    pip.setItemRecordTime(formatTime);
                    pip.setItemType(item_tag);
                    pip.setItemDesc(flaskType);
                    pip.setItemName(flaskName);
                    // 是否流行
                    if (totalResult > 10) {
                        pip.setItemIsPopular("是");
                    } else {
                        pip.setItemIsPopular("否");
                    }
                    pip.setItemFilters("");
                    poeItemPriceService.addNewItem(pip);
                } else {
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("item_name", flaskName);
                    map.put("item_desc", flaskType);
                    poeItemPriceService.removeByMap(map);
                }
            } catch (Exception e) {
                System.out.println("异常");
                int relaxTime = CommonUtil.genRandom(100, 1000);
                Thread.sleep(relaxTime);
                e.printStackTrace();
            }
            Thread.sleep(500);

        }
    }

    // 传奇首饰
    // @Scheduled(cron = "0 0 0/4 * * *")
    public void uniqueAccessoriesTimer(PoeItemPriceService poeItemPriceService) throws InterruptedException {
        String item_tag = "传奇首饰";
        JSONArray jsonArray = JSON.parseArray(getItemJsonByItemTag(item_tag));
        JSONObject jo = getSearchObjectByItemTag(item_tag);
        for (int i = 0; i < jsonArray.size(); i++) {
            try {
                // 获取物品的名字
                String uniqueAccessoriesName = (String) jsonArray.getJSONObject(i).get("name");
                String uniqueAccessoriesType = (String) jsonArray.getJSONObject(i).get("type");
                // 将物品的名字填充进入jsonObject
                jo.getJSONObject("query")
                        .put("name", uniqueAccessoriesName);
                jo.getJSONObject("query")
                        .put("type", uniqueAccessoriesType);
                // 将jsonObject转化为json串
                String s = JSONObject.toJSONString(jo);
                // 通过HuTool发送Post请求获取物品数据集合
                String itemsJson = HttpRequest.post(url)
                        .timeout(500)
                        .body(s)
                        .execute().body();
                System.out.println("json ========> " + itemsJson);
                // 将获取到的物品数据集合转化为jsonArray
                JSONArray ja = JSONArray.parseArray(JSONObject.parseObject(itemsJson).get("result").toString());
                // 获取结果集总数
                int totalResult = Integer.parseInt(JSONObject.parseObject(itemsJson).get("total").toString());
                System.out.println(totalResult + "++++++++++++++++++++++");
                // 通过JsonArray取出前10个物品的uuid,并用,拼接
                int totalItem = Math.min(10, ja.size());

                String suffix = ArrayUtil.join(ArrayUtil.resize(ja.toArray(), totalItem), ",");
                // 根据uuid字串获取物品实际数据json串
                String getUrl = fetchUrl + suffix;
                Thread.sleep(200);
                String s1 = HttpUtil
                        .get(getUrl);
                // 将json串转换为JSONObject
                JSONObject jo2 = JSONObject.parseObject(s1);
                // 计算价格平均值
                int avgPrice = 0;
                int avgNum = totalItem;
                System.out.println("avgNum ========> " + avgNum);
                if (avgNum >= 1) {
                    System.out.println("-------------- " + uniqueAccessoriesName + " -----------------------");
                    for (int index = 0; index < totalItem; index++) {
                        Double curPriceDouble = Double.parseDouble(jo2.getJSONArray("result").getJSONObject(index).getJSONObject("listing").getJSONObject("price").get("amount").toString());
                        int curPrice = curPriceDouble.intValue();
                        String curPriceType = jo2.getJSONArray("result").getJSONObject(index).getJSONObject("listing").getJSONObject("price").get("currency").toString();
                        if (curPriceType.equals("chaos")) {
                            avgPrice = avgPrice + curPrice;
                        } else if (curPriceType.equals("exalted")) {
                            avgPrice = avgPrice + curPrice * exaltedPrice;
                        } else {
                            if (avgNum > 1) {
                                avgNum -= 1;
                            }
                        }
                    }
                    avgPrice = avgPrice / avgNum;
                    PoeItemPrice pip = new PoeItemPrice();
                    pip.setItemCurPrice(avgPrice + " 混沌石");
                    Date dNow = new Date(); // 当前时间
                    String formatTime = df.format(dNow);
                    pip.setItemRecordTime(formatTime);
                    pip.setItemType(item_tag);
                    pip.setItemDesc(uniqueAccessoriesType);
                    pip.setItemName(uniqueAccessoriesName);
                    // 是否流行
                    if (totalResult > 10) {
                        pip.setItemIsPopular("是");
                    } else {
                        pip.setItemIsPopular("否");
                    }
                    pip.setItemFilters("");
                    poeItemPriceService.addNewItem(pip);
                } else {
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("item_name", uniqueAccessoriesName);
                    map.put("item_desc", uniqueAccessoriesType);
                    poeItemPriceService.removeByMap(map);
                }
            } catch (Exception e) {
                System.out.println("异常");
                int relaxTime = CommonUtil.genRandom(100, 1000);
                Thread.sleep(relaxTime);
                e.printStackTrace();
            }
            Thread.sleep(500);

        }
    }

    // 传奇武器
    // @Scheduled(cron = "0 0 0/4 * * *")
    public void uniqueWeaponTimer(PoeItemPriceService poeItemPriceService) throws InterruptedException {
        String item_tag = "传奇武器";
        JSONArray jsonArray = JSON.parseArray(getItemJsonByItemTag(item_tag));
        JSONObject jo = getSearchObjectByItemTag(item_tag);
        for (int i = 0; i < jsonArray.size(); i++) {
            try {
                // 获取物品的名字
                String uniqueWeaponName = (String) jsonArray.getJSONObject(i).get("name");
                String uniqueWeaponType = (String) jsonArray.getJSONObject(i).get("type");
                // 将物品的名字填充进入jsonObject
                jo.getJSONObject("query")
                        .put("name", uniqueWeaponName);
                jo.getJSONObject("query")
                        .put("type", uniqueWeaponType);
                // 将jsonObject转化为json串
                String s = JSONObject.toJSONString(jo);
                // 通过HuTool发送Post请求获取物品数据集合
                String itemsJson = HttpRequest.post(url)
                        .timeout(500)
                        .body(s)
                        .execute().body();
                System.out.println("json ========> " + itemsJson);
                // 将获取到的物品数据集合转化为jsonArray
                JSONArray ja = JSONArray.parseArray(JSONObject.parseObject(itemsJson).get("result").toString());
                // 获取结果集总数
                int totalResult = Integer.parseInt(JSONObject.parseObject(itemsJson).get("total").toString());
                System.out.println(totalResult + "++++++++++++++++++++++");
                // 通过JsonArray取出前10个物品的uuid,并用,拼接
                int totalItem = Math.min(10, ja.size());

                String suffix = ArrayUtil.join(ArrayUtil.resize(ja.toArray(), totalItem), ",");
                // 根据uuid字串获取物品实际数据json串
                String getUrl = fetchUrl + suffix;
                Thread.sleep(200);
                String s1 = HttpUtil
                        .get(getUrl);
                // 将json串转换为JSONObject
                JSONObject jo2 = JSONObject.parseObject(s1);
                // 计算价格平均值
                int avgPrice = 0;
                int avgNum = totalItem;
                System.out.println("avgNum ========> " + avgNum);
                if (avgNum >= 1) {
                    System.out.println("-------------- " + uniqueWeaponName + " -----------------------");
                    for (int index = 0; index < totalItem; index++) {
                        Double curPriceDouble = Double.parseDouble(jo2.getJSONArray("result").getJSONObject(index).getJSONObject("listing").getJSONObject("price").get("amount").toString());
                        int curPrice = curPriceDouble.intValue();
                        String curPriceType = jo2.getJSONArray("result").getJSONObject(index).getJSONObject("listing").getJSONObject("price").get("currency").toString();
                        if (curPriceType.equals("chaos")) {
                            avgPrice = avgPrice + curPrice;
                        } else if (curPriceType.equals("exalted")) {
                            avgPrice = avgPrice + curPrice * exaltedPrice;
                        } else {
                            if (avgNum > 1) {
                                avgNum -= 1;
                            }
                        }
                    }
                    avgPrice = avgPrice / avgNum;
                    PoeItemPrice pip = new PoeItemPrice();
                    pip.setItemCurPrice(avgPrice + " 混沌石");
                    Date dNow = new Date(); // 当前时间
                    String formatTime = df.format(dNow);
                    pip.setItemRecordTime(formatTime);
                    pip.setItemType(item_tag);
                    pip.setItemDesc(uniqueWeaponType);
                    pip.setItemName(uniqueWeaponName);
                    // 是否流行
                    if (totalResult > 10) {
                        pip.setItemIsPopular("是");
                    } else {
                        pip.setItemIsPopular("否");
                    }
                    pip.setItemFilters("");
                    poeItemPriceService.addNewItem(pip);
                } else {
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("item_name", uniqueWeaponName);
                    map.put("item_desc", uniqueWeaponType);
                    poeItemPriceService.removeByMap(map);
                }
            } catch (Exception e) {
                System.out.println("异常");
                int relaxTime = CommonUtil.genRandom(100, 1000);
                Thread.sleep(relaxTime);

                e.printStackTrace();
            }
            Thread.sleep(500);

        }
    }

    // 传奇护甲1
    // @Scheduled(cron = "0 0 0/4 * * *")
    public void uniqueArmour1Timer(PoeItemPriceService poeItemPriceService) throws InterruptedException {
        String item_tag = "传奇护甲";
        JSONArray jsonArray = JSON.parseArray(getItemJsonByItemTag(item_tag + "1"));
        JSONObject jo = getSearchObjectByItemTag(item_tag);
        for (int i = 0; i < jsonArray.size(); i++) {
            try {
                // 获取物品的名字
                String itemName = (String) jsonArray.getJSONObject(i).get("name");
                String itemType = (String) jsonArray.getJSONObject(i).get("type");
                // 将物品的名字填充进入jsonObject
                jo.getJSONObject("query")
                        .put("name", itemName);
                jo.getJSONObject("query")
                        .put("type", itemType);
                // 将jsonObject转化为json串
                String s = JSONObject.toJSONString(jo);
                // 通过HuTool发送Post请求获取物品数据集合
                String itemsJson = HttpRequest.post(url)
                        .timeout(500)
                        .body(s)
                        .execute().body();
                System.out.println("json ========> " + itemsJson);
                // 将获取到的物品数据集合转化为jsonArray
                JSONArray ja = JSONArray.parseArray(JSONObject.parseObject(itemsJson).get("result").toString());
                // 获取结果集总数
                int totalResult = Integer.parseInt(JSONObject.parseObject(itemsJson).get("total").toString());
                System.out.println(totalResult + "++++++++++++++++++++++");
                // 通过JsonArray取出前10个物品的uuid,并用,拼接
                int totalItem = Math.min(10, ja.size());

                String suffix = ArrayUtil.join(ArrayUtil.resize(ja.toArray(), totalItem), ",");
                // 根据uuid字串获取物品实际数据json串
                String getUrl = fetchUrl + suffix;
                Thread.sleep(200);
                String s1 = HttpUtil
                        .get(getUrl);
                // 将json串转换为JSONObject
                JSONObject jo2 = JSONObject.parseObject(s1);
                // 计算价格平均值
                int avgPrice = 0;
                int avgNum = totalItem;
                System.out.println("avgNum ========> " + avgNum);
                if (avgNum >= 1) {
                    System.out.println("-------------- " + itemName + " -----------------------");
                    for (int index = 0; index < totalItem; index++) {
                        Double curPriceDouble = Double.parseDouble(jo2.getJSONArray("result").getJSONObject(index).getJSONObject("listing").getJSONObject("price").get("amount").toString());
                        int curPrice = curPriceDouble.intValue();
                        String curPriceType = jo2.getJSONArray("result").getJSONObject(index).getJSONObject("listing").getJSONObject("price").get("currency").toString();
                        if (curPriceType.equals("chaos")) {
                            avgPrice = avgPrice + curPrice;
                        } else if (curPriceType.equals("exalted")) {
                            avgPrice = avgPrice + curPrice * exaltedPrice;
                        } else {
                            if (avgNum > 1) {
                                avgNum -= 1;
                            }
                        }
                    }
                    avgPrice = avgPrice / avgNum;
                    PoeItemPrice pip = new PoeItemPrice();
                    pip.setItemCurPrice(avgPrice + " 混沌石");
                    Date dNow = new Date(); // 当前时间
                    String formatTime = df.format(dNow);
                    pip.setItemRecordTime(formatTime);
                    pip.setItemType(item_tag);
                    pip.setItemDesc(itemType);
                    pip.setItemName(itemName);
                    // 是否流行
                    if (totalResult > 10) {
                        pip.setItemIsPopular("是");
                    } else {
                        pip.setItemIsPopular("否");
                    }
                    pip.setItemFilters("");
                    poeItemPriceService.addNewItem(pip);
                } else {
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("item_name", itemName);
                    map.put("item_desc", itemType);
                    poeItemPriceService.removeByMap(map);
                }
            } catch (Exception e) {
                System.out.println("异常");
                int relaxTime = CommonUtil.genRandom(100, 1000);
                Thread.sleep(relaxTime);
                e.printStackTrace();
            }
            Thread.sleep(500);

        }
    }

    // 传奇护甲2
    // @Scheduled(cron = "0 0 0/4 * * *")
    public void uniqueArmour2Timer(PoeItemPriceService poeItemPriceService) throws InterruptedException {
        String item_tag = "传奇护甲";
        JSONArray jsonArray = JSON.parseArray(getItemJsonByItemTag(item_tag + "2"));
        JSONObject jo = getSearchObjectByItemTag(item_tag);
        for (int i = 0; i < jsonArray.size(); i++) {
            try {
                // 获取物品的名字
                String itemName = (String) jsonArray.getJSONObject(i).get("name");
                String itemType = (String) jsonArray.getJSONObject(i).get("type");
                // 将物品的名字填充进入jsonObject
                jo.getJSONObject("query")
                        .put("name", itemName);
                jo.getJSONObject("query")
                        .put("type", itemType);
                // 将jsonObject转化为json串
                String s = JSONObject.toJSONString(jo);
                // 通过HuTool发送Post请求获取物品数据集合
                String itemsJson = HttpRequest.post(url)
                        .timeout(500)
                        .body(s)
                        .execute().body();
                System.out.println("json ========> " + itemsJson);
                // 将获取到的物品数据集合转化为jsonArray
                JSONArray ja = JSONArray.parseArray(JSONObject.parseObject(itemsJson).get("result").toString());
                // 获取结果集总数
                int totalResult = Integer.parseInt(JSONObject.parseObject(itemsJson).get("total").toString());
                System.out.println(totalResult + "++++++++++++++++++++++");
                // 通过JsonArray取出前10个物品的uuid,并用,拼接
                int totalItem = Math.min(10, ja.size());

                String suffix = ArrayUtil.join(ArrayUtil.resize(ja.toArray(), totalItem), ",");
                // 根据uuid字串获取物品实际数据json串
                String getUrl = fetchUrl + suffix;
                Thread.sleep(200);
                String s1 = HttpUtil
                        .get(getUrl);
                // 将json串转换为JSONObject
                JSONObject jo2 = JSONObject.parseObject(s1);
                // 计算价格平均值
                int avgPrice = 0;
                int avgNum = totalItem;
                System.out.println("avgNum ========> " + avgNum);
                if (avgNum >= 1) {
                    System.out.println("-------------- " + itemName + " -----------------------");
                    for (int index = 0; index < totalItem; index++) {
                        Double curPriceDouble = Double.parseDouble(jo2.getJSONArray("result").getJSONObject(index).getJSONObject("listing").getJSONObject("price").get("amount").toString());
                        int curPrice = curPriceDouble.intValue();
                        String curPriceType = jo2.getJSONArray("result").getJSONObject(index).getJSONObject("listing").getJSONObject("price").get("currency").toString();
                        if (curPriceType.equals("chaos")) {
                            avgPrice = avgPrice + curPrice;
                        } else if (curPriceType.equals("exalted")) {
                            avgPrice = avgPrice + curPrice * exaltedPrice;
                        } else {
                            if (avgNum > 1) {
                                avgNum -= 1;
                            }
                        }
                    }
                    avgPrice = avgPrice / avgNum;
                    PoeItemPrice pip = new PoeItemPrice();
                    pip.setItemCurPrice(avgPrice + " 混沌石");
                    Date dNow = new Date(); // 当前时间
                    String formatTime = df.format(dNow);
                    pip.setItemRecordTime(formatTime);
                    pip.setItemType(item_tag);
                    pip.setItemDesc(itemType);
                    pip.setItemName(itemName);
                    // 是否流行
                    if (totalResult > 10) {
                        pip.setItemIsPopular("是");
                    } else {
                        pip.setItemIsPopular("否");
                    }
                    pip.setItemFilters("");
                    poeItemPriceService.addNewItem(pip);
                } else {
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("item_name", itemName);
                    map.put("item_desc", itemType);
                    poeItemPriceService.removeByMap(map);
                }
            } catch (Exception e) {
                System.out.println("异常");
                int relaxTime = CommonUtil.genRandom(100, 1000);
                Thread.sleep(relaxTime);
                e.printStackTrace();
            }
            Thread.sleep(500);

        }
    }

    // 精华
    // @Scheduled(cron = "0 0 0/4 * * *")
    public void essenceTimer(PoeItemPriceService poeItemPriceService) throws InterruptedException {
        String item_tag = "精华";
        JSONArray jsonArray = JSON.parseArray(getItemJsonByItemTag(item_tag));
        JSONObject jo = getSearchObjectByItemTag(item_tag);
        for (int i = 0; i < jsonArray.size(); i++) {
            try {
                // 获取物品的名字
                String essenceName = (String) jsonArray.getJSONObject(i).get("text");
                String essenceType = (String) jsonArray.getJSONObject(i).get("type");
                // 将物品的名字填充进入jsonObject
                // jo.getJSONObject("query").put("name", essenceName);
                jo.getJSONObject("query")
                        .put("type", essenceType);
                // 将jsonObject转化为json串
                String s = JSONObject.toJSONString(jo);
                // 通过HuTool发送Post请求获取物品数据集合
                String itemsJson = HttpRequest.post(url)
                        .timeout(500)
                        .body(s)
                        .execute().body();
                System.out.println("json ========> " + itemsJson);
                // 将获取到的物品数据集合转化为jsonArray
                JSONArray ja = JSONArray.parseArray(JSONObject.parseObject(itemsJson).get("result").toString());
                // 获取结果集总数
                int totalResult = Integer.parseInt(JSONObject.parseObject(itemsJson).get("total").toString());
                System.out.println(totalResult + "++++++++++++++++++++++");
                // 通过JsonArray取出前10个物品的uuid,并用,拼接
                int totalItem = Math.min(10, ja.size());

                String suffix = ArrayUtil.join(ArrayUtil.resize(ja.toArray(), totalItem), ",");
                // 根据uuid字串获取物品实际数据json串
                String getUrl = fetchUrl + suffix;
                Thread.sleep(200);
                String s1 = HttpUtil
                        .get(getUrl);
                // 将json串转换为JSONObject
                JSONObject jo2 = JSONObject.parseObject(s1);
                // 计算价格平均值
                int avgPrice = 0;
                int avgNum = totalItem;
                System.out.println("avgNum ========> " + avgNum);
                if (avgNum >= 1) {
                    System.out.println("-------------- " + essenceName + " -----------------------");
                    for (int index = 0; index < totalItem; index++) {
                        Double curPriceDouble = Double.parseDouble(jo2.getJSONArray("result").getJSONObject(index).getJSONObject("listing").getJSONObject("price").get("amount").toString());
                        int curPrice = curPriceDouble.intValue();
                        String curPriceType = jo2.getJSONArray("result").getJSONObject(index).getJSONObject("listing").getJSONObject("price").get("currency").toString();
                        if (curPriceType.equals("chaos")) {
                            avgPrice = avgPrice + curPrice;
                        } else if (curPriceType.equals("exalted")) {
                            avgPrice = avgPrice + curPrice * exaltedPrice;
                        } else {
                            if (avgNum > 1) {
                                avgNum -= 1;
                            }
                        }
                    }
                    avgPrice = avgPrice / avgNum;
                    PoeItemPrice pip = new PoeItemPrice();
                    pip.setItemCurPrice(avgPrice + " 混沌石");
                    Date dNow = new Date(); // 当前时间
                    String formatTime = df.format(dNow);
                    pip.setItemRecordTime(formatTime);
                    pip.setItemType(item_tag);
                    pip.setItemDesc(essenceType);
                    pip.setItemName(essenceName);
                    // 是否流行
                    if (totalResult > 10) {
                        pip.setItemIsPopular("是");
                    } else {
                        pip.setItemIsPopular("否");
                    }
                    pip.setItemFilters("");
                    poeItemPriceService.addNewItem(pip);
                } else {
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("item_name", essenceName);
                    map.put("item_desc", essenceType);
                    poeItemPriceService.removeByMap(map);
                }
            } catch (Exception e) {
                System.out.println("异常");
                int relaxTime = CommonUtil.genRandom(100, 1000);
                Thread.sleep(relaxTime);
                e.printStackTrace();
            }
            Thread.sleep(500);

        }
    }

    // 通货
    // @Scheduled(cron = "0 9 0/1 * * *")
    public void currencyTimer(PoeItemPriceService poeItemPriceService) throws InterruptedException {
        String item_tag = "通货";
        JSONArray jsonArray = JSON.parseArray(getItemJsonByItemTag(item_tag));
        JSONObject jo = getSearchObjectByItemTag(item_tag);
        for (int i = 0; i < jsonArray.size(); i++) {
            try {
                // 获取物品的名字
                String essenceName = (String) jsonArray.getJSONObject(i).get("text");
                String essenceType = (String) jsonArray.getJSONObject(i).get("type");
                // 将物品的名字填充进入jsonObject
                // jo.getJSONObject("query").put("name", essenceName);
                jo.getJSONObject("query")
                        .put("type", essenceType);
                // 将jsonObject转化为json串
                String s = JSONObject.toJSONString(jo);
                // 通过HuTool发送Post请求获取物品数据集合
                String itemsJson = HttpRequest.post(url)
                        .timeout(500)
                        .body(s)
                        .execute().body();
                System.out.println("json ========> " + itemsJson);
                // 将获取到的物品数据集合转化为jsonArray
                JSONArray ja = JSONArray.parseArray(JSONObject.parseObject(itemsJson).get("result").toString());
                // 获取结果集总数
                int totalResult = Integer.parseInt(JSONObject.parseObject(itemsJson).get("total").toString());
                System.out.println(totalResult + "++++++++++++++++++++++");
                // 通过JsonArray取出前10个物品的uuid,并用,拼接
                int totalItem = Math.min(10, ja.size());

                String suffix = ArrayUtil.join(ArrayUtil.resize(ja.toArray(), totalItem), ",");
                // 根据uuid字串获取物品实际数据json串
                String getUrl = fetchUrl + suffix;
                Thread.sleep(200);
                String s1 = HttpUtil
                        .get(getUrl);
                // 将json串转换为JSONObject
                JSONObject jo2 = JSONObject.parseObject(s1);
                // 计算价格平均值
                int avgPrice = 0;
                int avgNum = totalItem;
                System.out.println("avgNum ========> " + avgNum);
                if (avgNum >= 1) {
                    System.out.println("-------------- " + essenceName + " -----------------------");
                    for (int index = 0; index < totalItem; index++) {
                        Double curPriceDouble = Double.parseDouble(jo2.getJSONArray("result").getJSONObject(index).getJSONObject("listing").getJSONObject("price").get("amount").toString());
                        int curPrice = curPriceDouble.intValue();
                        String curPriceType = jo2.getJSONArray("result").getJSONObject(index).getJSONObject("listing").getJSONObject("price").get("currency").toString();
                        if (curPriceType.equals("chaos")) {
                            avgPrice = avgPrice + curPrice;
                        } else if (curPriceType.equals("exalted")) {
                            avgPrice = avgPrice + curPrice * exaltedPrice;
                        } else {
                            if (avgNum > 1) {
                                avgNum -= 1;
                            }
                        }
                    }
                    avgPrice = avgPrice / avgNum;
                    PoeItemPrice pip = new PoeItemPrice();
                    pip.setItemCurPrice(avgPrice + " 混沌石");
                    Date dNow = new Date(); // 当前时间
                    String formatTime = df.format(dNow);
                    pip.setItemRecordTime(formatTime);
                    pip.setItemType(item_tag);
                    pip.setItemDesc(essenceType);
                    pip.setItemName(essenceName);
                    // 是否流行
                    if (totalResult > 10) {
                        pip.setItemIsPopular("是");
                    } else {
                        pip.setItemIsPopular("否");
                    }
                    pip.setItemFilters("");
                    poeItemPriceService.addNewItem(pip);
                } else {
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("item_name", essenceName);
                    map.put("item_desc", essenceType);
                    poeItemPriceService.removeByMap(map);
                }
            } catch (Exception e) {
                System.out.println("异常");
                int relaxTime = CommonUtil.genRandom(100, 1000);
                Thread.sleep(relaxTime);
                e.printStackTrace();
            }
            Thread.sleep(500);

        }
    }

    // 碎片
    // @Scheduled(cron = "0 15 0/1 * * *")
    public void fragmentTimer(PoeItemPriceService poeItemPriceService) throws InterruptedException {
        String item_tag = "碎片";
        JSONArray jsonArray = JSON.parseArray(getItemJsonByItemTag(item_tag));
        JSONObject jo = getSearchObjectByItemTag(item_tag);
        for (int i = 0; i < jsonArray.size(); i++) {
            try {
                // 获取物品的名字
                String essenceName = (String) jsonArray.getJSONObject(i).get("text");
                String essenceType = (String) jsonArray.getJSONObject(i).get("type");
                // 将物品的名字填充进入jsonObject
                // jo.getJSONObject("query").put("name", essenceName);
                jo.getJSONObject("query")
                        .put("type", essenceType);
                // 将jsonObject转化为json串
                String s = JSONObject.toJSONString(jo);
                // 通过HuTool发送Post请求获取物品数据集合
                String itemsJson = HttpRequest.post(url)
                        .timeout(500)
                        .body(s)
                        .execute().body();
                System.out.println("json ========> " + itemsJson);
                // 将获取到的物品数据集合转化为jsonArray
                JSONArray ja = JSONArray.parseArray(JSONObject.parseObject(itemsJson).get("result").toString());
                // 获取结果集总数
                int totalResult = Integer.parseInt(JSONObject.parseObject(itemsJson).get("total").toString());
                System.out.println(totalResult + "++++++++++++++++++++++");
                // 通过JsonArray取出前10个物品的uuid,并用,拼接
                int totalItem = Math.min(10, ja.size());

                String suffix = ArrayUtil.join(ArrayUtil.resize(ja.toArray(), totalItem), ",");
                // 根据uuid字串获取物品实际数据json串
                String getUrl = fetchUrl + suffix;
                Thread.sleep(200);
                String s1 = HttpUtil
                        .get(getUrl);
                // 将json串转换为JSONObject
                JSONObject jo2 = JSONObject.parseObject(s1);
                // 计算价格平均值
                int avgPrice = 0;
                int avgNum = totalItem;
                System.out.println("avgNum ========> " + avgNum);
                if (avgNum >= 1) {
                    System.out.println("-------------- " + essenceName + " -----------------------");
                    for (int index = 0; index < totalItem; index++) {
                        Double curPriceDouble = Double.parseDouble(jo2.getJSONArray("result").getJSONObject(index).getJSONObject("listing").getJSONObject("price").get("amount").toString());
                        int curPrice = curPriceDouble.intValue();
                        String curPriceType = jo2.getJSONArray("result").getJSONObject(index).getJSONObject("listing").getJSONObject("price").get("currency").toString();
                        if (curPriceType.equals("chaos")) {
                            avgPrice = avgPrice + curPrice;
                        } else if (curPriceType.equals("exalted")) {
                            avgPrice = avgPrice + curPrice * exaltedPrice;
                        } else {
                            if (avgNum > 1) {
                                avgNum -= 1;
                            }
                        }
                    }
                    avgPrice = avgPrice / avgNum;
                    PoeItemPrice pip = new PoeItemPrice();
                    pip.setItemCurPrice(avgPrice + " 混沌石");
                    Date dNow = new Date(); // 当前时间
                    String formatTime = df.format(dNow);
                    pip.setItemRecordTime(formatTime);
                    pip.setItemType(item_tag);
                    pip.setItemDesc(essenceType);
                    pip.setItemName(essenceName);
                    // 是否流行
                    if (totalResult > 10) {
                        pip.setItemIsPopular("是");
                    } else {
                        pip.setItemIsPopular("否");
                    }
                    pip.setItemFilters("");
                    poeItemPriceService.addNewItem(pip);
                } else {
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("item_name", essenceName);
                    map.put("item_desc", essenceType);
                    poeItemPriceService.removeByMap(map);
                }
            } catch (Exception e) {
                System.out.println("异常");
                int relaxTime = CommonUtil.genRandom(100, 1000);
                Thread.sleep(relaxTime);
                e.printStackTrace();
            }
            Thread.sleep(500);

        }
    }

    // 化石
    // @Scheduled(cron = "0 0 0/4 * * *")
    public void fossilTimer(PoeItemPriceService poeItemPriceService) throws InterruptedException {
        String item_tag = "化石";
        JSONArray jsonArray = JSON.parseArray(getItemJsonByItemTag(item_tag));
        JSONObject jo = getSearchObjectByItemTag(item_tag);
        for (int i = 0; i < jsonArray.size(); i++) {
            try {
                // 获取物品的名字
                String itemName = (String) jsonArray.getJSONObject(i).get("text");
                String itemType = (String) jsonArray.getJSONObject(i).get("type");
                // 将物品的名字填充进入jsonObject
                // jo.getJSONObject("query").put("name", itemName);
                jo.getJSONObject("query")
                        .put("type", itemType);
                // 将jsonObject转化为json串
                String s = JSONObject.toJSONString(jo);
                // 通过HuTool发送Post请求获取物品数据集合
                String itemsJson = HttpRequest.post(url)
                        .timeout(500)
                        .body(s)
                        .execute().body();
                System.out.println("json ========> " + itemsJson);
                // 将获取到的物品数据集合转化为jsonArray
                JSONArray ja = JSONArray.parseArray(JSONObject.parseObject(itemsJson).get("result").toString());
                // 获取结果集总数
                int totalResult = Integer.parseInt(JSONObject.parseObject(itemsJson).get("total").toString());
                System.out.println(totalResult + "++++++++++++++++++++++");
                // 通过JsonArray取出前10个物品的uuid,并用,拼接
                int totalItem = Math.min(10, ja.size());

                String suffix = ArrayUtil.join(ArrayUtil.resize(ja.toArray(), totalItem), ",");
                // 根据uuid字串获取物品实际数据json串
                String getUrl = fetchUrl + suffix;
                Thread.sleep(200);
                System.out.println("getUrl => " + getUrl);
                String s1 = HttpRequest
                        .get(getUrl)
                        .header(Header.USER_AGENT, "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.141 Safari/537.36")
                        .execute()
                        .body();
                // 将json串转换为JSONObject
                s1 = UnicodeUtil.toString(s1);
                System.out.println("s1 => " + s1);
                JSONObject jo2 = JSONObject.parseObject(s1);
                // 计算价格平均值
                int avgPrice = 0;
                int avgNum = totalItem;
                System.out.println("avgNum ========> " + avgNum);
                if (avgNum >= 1) {
                    System.out.println("-------------- " + itemName + " -----------------------");
                    for (int index = 0; index < totalItem; index++) {
                        Double curPriceDouble = Double.parseDouble(jo2.getJSONArray("result").getJSONObject(index).getJSONObject("listing").getJSONObject("price").get("amount").toString());
                        int curPrice = curPriceDouble.intValue();
                        String curPriceType = jo2.getJSONArray("result").getJSONObject(index).getJSONObject("listing").getJSONObject("price").get("currency").toString();
                        if (curPriceType.equals("chaos")) {
                            avgPrice = avgPrice + curPrice;
                        } else if (curPriceType.equals("exalted")) {
                            avgPrice = avgPrice + curPrice * exaltedPrice;
                        } else {
                            if (avgNum > 1) {
                                avgNum -= 1;
                            }
                        }
                    }
                    avgPrice = avgPrice / avgNum;
                    PoeItemPrice pip = new PoeItemPrice();
                    pip.setItemCurPrice(avgPrice + " 混沌石");
                    Date dNow = new Date(); // 当前时间
                    String formatTime = df.format(dNow);
                    pip.setItemRecordTime(formatTime);
                    pip.setItemType(item_tag);
                    pip.setItemDesc(itemType);
                    pip.setItemName(itemName);
                    // 是否流行
                    if (totalResult > 10) {
                        pip.setItemIsPopular("是");
                    } else {
                        pip.setItemIsPopular("否");
                    }
                    pip.setItemFilters("");
                    poeItemPriceService.addNewItem(pip);
                } else {
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("item_name", itemName);
                    map.put("item_desc", itemType);
                    poeItemPriceService.removeByMap(map);
                }
            } catch (Exception e) {
                System.out.println("异常");
                int relaxTime = CommonUtil.genRandom(100, 1000);
                Thread.sleep(relaxTime);
                e.printStackTrace();
            }
            Thread.sleep(500);

        }
    }

    // 圣油
    // @Scheduled(cron = "0 0 0/4 * * *")
    public void oilTimer(PoeItemPriceService poeItemPriceService) throws InterruptedException {
        String item_tag = "圣油";
        JSONArray jsonArray = JSON.parseArray(getItemJsonByItemTag(item_tag));
        JSONObject jo = getSearchObjectByItemTag(item_tag);
        for (int i = 0; i < jsonArray.size(); i++) {
            try {
                // 获取物品的名字
                String itemName = (String) jsonArray.getJSONObject(i).get("text");
                String itemType = (String) jsonArray.getJSONObject(i).get("type");
                // 将物品的名字填充进入jsonObject
                // jo.getJSONObject("query").put("name", itemName);
                jo.getJSONObject("query")
                        .put("type", itemType);
                // 将jsonObject转化为json串
                String s = JSONObject.toJSONString(jo);
                // 通过HuTool发送Post请求获取物品数据集合
                String itemsJson = HttpRequest.post(url)
                        .timeout(500)
                        .body(s)
                        .execute().body();
                System.out.println("json ========> " + itemsJson);
                // 将获取到的物品数据集合转化为jsonArray
                JSONArray ja = JSONArray.parseArray(JSONObject.parseObject(itemsJson).get("result").toString());
                // 获取结果集总数
                int totalResult = Integer.parseInt(JSONObject.parseObject(itemsJson).get("total").toString());
                System.out.println(totalResult + "++++++++++++++++++++++");
                // 通过JsonArray取出前10个物品的uuid,并用,拼接
                int totalItem = Math.min(10, ja.size());

                String suffix = ArrayUtil.join(ArrayUtil.resize(ja.toArray(), totalItem), ",");
                // 根据uuid字串获取物品实际数据json串
                String getUrl = fetchUrl + suffix;
                Thread.sleep(200);
                String s1 = HttpUtil
                        .get(getUrl);
                // 将json串转换为JSONObject
                JSONObject jo2 = JSONObject.parseObject(s1);
                // 计算价格平均值
                int avgPrice = 0;
                int avgNum = totalItem;
                System.out.println("avgNum ========> " + avgNum);
                if (avgNum >= 1) {
                    System.out.println("-------------- " + itemName + " -----------------------");
                    for (int index = 0; index < totalItem; index++) {
                        Double curPriceDouble = Double.parseDouble(jo2.getJSONArray("result").getJSONObject(index).getJSONObject("listing").getJSONObject("price").get("amount").toString());
                        int curPrice = curPriceDouble.intValue();
                        String curPriceType = jo2.getJSONArray("result").getJSONObject(index).getJSONObject("listing").getJSONObject("price").get("currency").toString();
                        if (curPriceType.equals("chaos")) {
                            avgPrice = avgPrice + curPrice;
                        } else if (curPriceType.equals("exalted")) {
                            avgPrice = avgPrice + curPrice * exaltedPrice;
                        } else {
                            if (avgNum > 1) {
                                avgNum -= 1;
                            }
                        }
                    }
                    avgPrice = avgPrice / avgNum;
                    PoeItemPrice pip = new PoeItemPrice();
                    pip.setItemCurPrice(avgPrice + " 混沌石");
                    Date dNow = new Date(); // 当前时间
                    String formatTime = df.format(dNow);
                    pip.setItemRecordTime(formatTime);
                    pip.setItemType(item_tag);
                    pip.setItemDesc(itemType);
                    pip.setItemName(itemName);
                    // 是否流行
                    if (totalResult > 10) {
                        pip.setItemIsPopular("是");
                    } else {
                        pip.setItemIsPopular("否");
                    }
                    pip.setItemFilters("");
                    poeItemPriceService.addNewItem(pip);
                } else {
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("item_name", itemName);
                    map.put("item_desc", itemType);
                    poeItemPriceService.removeByMap(map);
                }
            } catch (Exception e) {
                System.out.println("异常");
                int relaxTime = CommonUtil.genRandom(100, 1000);
                Thread.sleep(relaxTime);
                e.printStackTrace();
            }
            Thread.sleep(500);

        }
    }

    // 共振器
    // @Scheduled(cron = "0 0 0/4 * * *")
    public void resonatorsTimer(PoeItemPriceService poeItemPriceService) throws InterruptedException {
        String item_tag = "共振器";
        JSONArray jsonArray = JSON.parseArray(getItemJsonByItemTag(item_tag));
        JSONObject jo = getSearchObjectByItemTag(item_tag);
        for (int i = 0; i < jsonArray.size(); i++) {
            try {
                // 获取物品的名字
                String itemName = (String) jsonArray.getJSONObject(i).get("text");
                String itemType = (String) jsonArray.getJSONObject(i).get("type");
                // 将物品的名字填充进入jsonObject
                // jo.getJSONObject("query").put("name", itemName);
                jo.getJSONObject("query")
                        .put("type", itemType);
                // 将jsonObject转化为json串
                String s = JSONObject.toJSONString(jo);
                // 通过HuTool发送Post请求获取物品数据集合
                String itemsJson = HttpRequest.post(url)
                        .timeout(500)
                        .body(s)
                        .execute().body();
                System.out.println("json ========> " + itemsJson);
                // 将获取到的物品数据集合转化为jsonArray
                JSONArray ja = JSONArray.parseArray(JSONObject.parseObject(itemsJson).get("result").toString());
                // 获取结果集总数
                int totalResult = Integer.parseInt(JSONObject.parseObject(itemsJson).get("total").toString());
                System.out.println(totalResult + "++++++++++++++++++++++");
                // 通过JsonArray取出前10个物品的uuid,并用,拼接
                int totalItem = Math.min(10, ja.size());

                String suffix = ArrayUtil.join(ArrayUtil.resize(ja.toArray(), totalItem), ",");
                // 根据uuid字串获取物品实际数据json串
                String getUrl = fetchUrl + suffix;
                Thread.sleep(200);
                String s1 = HttpUtil
                        .get(getUrl);
                // 将json串转换为JSONObject
                JSONObject jo2 = JSONObject.parseObject(s1);
                // 计算价格平均值
                int avgPrice = 0;
                int avgNum = totalItem;
                System.out.println("avgNum ========> " + avgNum);
                if (avgNum >= 1) {
                    System.out.println("-------------- " + itemName + " -----------------------");
                    for (int index = 0; index < totalItem; index++) {
                        Double curPriceDouble = Double.parseDouble(jo2.getJSONArray("result").getJSONObject(index).getJSONObject("listing").getJSONObject("price").get("amount").toString());
                        int curPrice = curPriceDouble.intValue();
                        String curPriceType = jo2.getJSONArray("result").getJSONObject(index).getJSONObject("listing").getJSONObject("price").get("currency").toString();
                        if (curPriceType.equals("chaos")) {
                            avgPrice = avgPrice + curPrice;
                        } else if (curPriceType.equals("exalted")) {
                            avgPrice = avgPrice + curPrice * exaltedPrice;
                        } else {
                            if (avgNum > 1) {
                                avgNum -= 1;
                            }
                        }
                    }
                    avgPrice = avgPrice / avgNum;
                    PoeItemPrice pip = new PoeItemPrice();
                    pip.setItemCurPrice(avgPrice + " 混沌石");
                    Date dNow = new Date(); // 当前时间
                    String formatTime = df.format(dNow);
                    pip.setItemRecordTime(formatTime);
                    pip.setItemType(item_tag);
                    pip.setItemDesc(itemType);
                    pip.setItemName(itemName);
                    // 是否流行
                    if (totalResult > 10) {
                        pip.setItemIsPopular("是");
                    } else {
                        pip.setItemIsPopular("否");
                    }
                    pip.setItemFilters("");
                    poeItemPriceService.addNewItem(pip);
                } else {
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("item_name", itemName);
                    map.put("item_desc", itemType);
                    poeItemPriceService.removeByMap(map);
                }
            } catch (Exception e) {
                System.out.println("异常");
                int relaxTime = CommonUtil.genRandom(100, 1000);
                Thread.sleep(relaxTime);
                e.printStackTrace();
            }
            Thread.sleep(500);

        }
    }

    // 魔瓶
    // @Scheduled(cron = "0 0 0/4 * * *")
    public void vialTimer(PoeItemPriceService poeItemPriceService) throws InterruptedException {
        String item_tag = "魔瓶";
        JSONArray jsonArray = JSON.parseArray(getItemJsonByItemTag(item_tag));
        JSONObject jo = getSearchObjectByItemTag(item_tag);
        for (int i = 0; i < jsonArray.size(); i++) {
            try {
                // 获取物品的名字
                String itemName = (String) jsonArray.getJSONObject(i).get("text");
                String itemType = (String) jsonArray.getJSONObject(i).get("type");
                // 将物品的名字填充进入jsonObject
                // jo.getJSONObject("query").put("name", itemName);
                jo.getJSONObject("query")
                        .put("type", itemType);
                // 将jsonObject转化为json串
                String s = JSONObject.toJSONString(jo);
                // 通过HuTool发送Post请求获取物品数据集合
                String itemsJson = HttpRequest.post(url)
                        .timeout(500)
                        .body(s)
                        .execute().body();
                System.out.println("json ========> " + itemsJson);
                // 将获取到的物品数据集合转化为jsonArray
                JSONArray ja = JSONArray.parseArray(JSONObject.parseObject(itemsJson).get("result").toString());
                // 获取结果集总数
                int totalResult = Integer.parseInt(JSONObject.parseObject(itemsJson).get("total").toString());
                System.out.println(totalResult + "++++++++++++++++++++++");
                // 通过JsonArray取出前10个物品的uuid,并用,拼接
                int totalItem = Math.min(10, ja.size());

                String suffix = ArrayUtil.join(ArrayUtil.resize(ja.toArray(), totalItem), ",");
                // 根据uuid字串获取物品实际数据json串
                String getUrl = fetchUrl + suffix;
                Thread.sleep(200);
                String s1 = HttpUtil
                        .get(getUrl);
                // 将json串转换为JSONObject
                JSONObject jo2 = JSONObject.parseObject(s1);
                // 计算价格平均值
                int avgPrice = 0;
                int avgNum = totalItem;
                System.out.println("avgNum ========> " + avgNum);
                if (avgNum >= 1) {
                    System.out.println("-------------- " + itemName + " -----------------------");
                    for (int index = 0; index < totalItem; index++) {
                        Double curPriceDouble = Double.parseDouble(jo2.getJSONArray("result").getJSONObject(index).getJSONObject("listing").getJSONObject("price").get("amount").toString());
                        int curPrice = curPriceDouble.intValue();
                        String curPriceType = jo2.getJSONArray("result").getJSONObject(index).getJSONObject("listing").getJSONObject("price").get("currency").toString();
                        if (curPriceType.equals("chaos")) {
                            avgPrice = avgPrice + curPrice;
                        } else if (curPriceType.equals("exalted")) {
                            avgPrice = avgPrice + curPrice * exaltedPrice;
                        } else {
                            if (avgNum > 1) {
                                avgNum -= 1;
                            }
                        }
                    }
                    avgPrice = avgPrice / avgNum;
                    PoeItemPrice pip = new PoeItemPrice();
                    pip.setItemCurPrice(avgPrice + " 混沌石");
                    Date dNow = new Date(); // 当前时间
                    String formatTime = df.format(dNow);
                    pip.setItemRecordTime(formatTime);
                    pip.setItemType(item_tag);
                    pip.setItemDesc(itemType);
                    pip.setItemName(itemName);
                    // 是否流行
                    if (totalResult > 10) {
                        pip.setItemIsPopular("是");
                    } else {
                        pip.setItemIsPopular("否");
                    }
                    pip.setItemFilters("");
                    poeItemPriceService.addNewItem(pip);
                } else {
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("item_name", itemName);
                    map.put("item_desc", itemType);
                    poeItemPriceService.removeByMap(map);
                }
            } catch (Exception e) {
                System.out.println("异常");
                int relaxTime = CommonUtil.genRandom(100, 1000);
                Thread.sleep(relaxTime);
                e.printStackTrace();
            }
            Thread.sleep(500);

        }
    }

    // 孕育石
    // @Scheduled(cron = "0 0 0/4 * * *")
    public void incubatorsTimer(PoeItemPriceService poeItemPriceService) throws InterruptedException {
        String item_tag = "孕育石";
        JSONArray jsonArray = JSON.parseArray(getItemJsonByItemTag(item_tag));
        JSONObject jo = getSearchObjectByItemTag(item_tag);
        for (int i = 0; i < jsonArray.size(); i++) {
            try {
                // 获取物品的名字
                String itemName = (String) jsonArray.getJSONObject(i).get("text");
                String itemType = (String) jsonArray.getJSONObject(i).get("type");
                // 将物品的名字填充进入jsonObject
                // jo.getJSONObject("query").put("name", itemName);
                jo.getJSONObject("query")
                        .put("type", itemType);
                // 将jsonObject转化为json串
                String s = JSONObject.toJSONString(jo);
                // 通过HuTool发送Post请求获取物品数据集合
                String itemsJson = HttpRequest.post(url)
                        .timeout(500)
                        .body(s)
                        .execute().body();
                System.out.println("json ========> " + itemsJson);
                // 将获取到的物品数据集合转化为jsonArray
                JSONArray ja = JSONArray.parseArray(JSONObject.parseObject(itemsJson).get("result").toString());
                // 获取结果集总数
                int totalResult = Integer.parseInt(JSONObject.parseObject(itemsJson).get("total").toString());
                System.out.println(totalResult + "++++++++++++++++++++++");
                // 通过JsonArray取出前10个物品的uuid,并用,拼接
                int totalItem = Math.min(10, ja.size());

                String suffix = ArrayUtil.join(ArrayUtil.resize(ja.toArray(), totalItem), ",");
                // 根据uuid字串获取物品实际数据json串
                String getUrl = fetchUrl + suffix;
                Thread.sleep(200);
                String s1 = HttpUtil
                        .get(getUrl);
                // 将json串转换为JSONObject
                JSONObject jo2 = JSONObject.parseObject(s1);
                // 计算价格平均值
                int avgPrice = 0;
                int avgNum = totalItem;
                System.out.println("avgNum ========> " + avgNum);
                if (avgNum >= 1) {
                    System.out.println("-------------- " + itemName + " -----------------------");
                    for (int index = 0; index < totalItem; index++) {
                        Double curPriceDouble = Double.parseDouble(jo2.getJSONArray("result").getJSONObject(index).getJSONObject("listing").getJSONObject("price").get("amount").toString());
                        int curPrice = curPriceDouble.intValue();
                        String curPriceType = jo2.getJSONArray("result").getJSONObject(index).getJSONObject("listing").getJSONObject("price").get("currency").toString();
                        if (curPriceType.equals("chaos")) {
                            avgPrice = avgPrice + curPrice;
                        } else if (curPriceType.equals("exalted")) {
                            avgPrice = avgPrice + curPrice * exaltedPrice;
                        } else {
                            if (avgNum > 1) {
                                avgNum -= 1;
                            }
                        }
                    }
                    avgPrice = avgPrice / avgNum;
                    PoeItemPrice pip = new PoeItemPrice();
                    pip.setItemCurPrice(avgPrice + " 混沌石");
                    Date dNow = new Date(); // 当前时间
                    String formatTime = df.format(dNow);
                    pip.setItemRecordTime(formatTime);
                    pip.setItemType(item_tag);
                    pip.setItemDesc(itemType);
                    pip.setItemName(itemName);
                    // 是否流行
                    if (totalResult > 10) {
                        pip.setItemIsPopular("是");
                    } else {
                        pip.setItemIsPopular("否");
                    }
                    pip.setItemFilters("");
                    poeItemPriceService.addNewItem(pip);
                } else {
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("item_name", itemName);
                    map.put("item_desc", itemType);
                    poeItemPriceService.removeByMap(map);
                }
            } catch (Exception e) {
                System.out.println("异常");
                int relaxTime = CommonUtil.genRandom(100, 1000);
                Thread.sleep(relaxTime);
                e.printStackTrace();
            }
            Thread.sleep(500);

        }
    }

    // 梦魇宝珠
    // @Scheduled(cron = "0 0 0/4 * * *")
    public void deliriumOrbTimer(PoeItemPriceService poeItemPriceService) throws InterruptedException {
        String item_tag = "梦魇宝珠";
        JSONArray jsonArray = JSON.parseArray(getItemJsonByItemTag(item_tag));
        JSONObject jo = getSearchObjectByItemTag(item_tag);
        for (int i = 0; i < jsonArray.size(); i++) {
            try {
                // 获取物品的名字
                String itemName = (String) jsonArray.getJSONObject(i).get("text");
                String itemType = (String) jsonArray.getJSONObject(i).get("type");
                // 将物品的名字填充进入jsonObject
                // jo.getJSONObject("query").put("name", itemName);
                jo.getJSONObject("query")
                        .put("type", itemType);
                // 将jsonObject转化为json串
                String s = JSONObject.toJSONString(jo);
                // 通过HuTool发送Post请求获取物品数据集合
                String itemsJson = HttpRequest.post(url)
                        .timeout(500)
                        .body(s)
                        .execute().body();
                System.out.println("json ========> " + itemsJson);
                // 将获取到的物品数据集合转化为jsonArray
                JSONArray ja = JSONArray.parseArray(JSONObject.parseObject(itemsJson).get("result").toString());
                // 获取结果集总数
                int totalResult = Integer.parseInt(JSONObject.parseObject(itemsJson).get("total").toString());
                System.out.println(totalResult + "++++++++++++++++++++++");
                // 通过JsonArray取出前10个物品的uuid,并用,拼接
                int totalItem = Math.min(10, ja.size());

                String suffix = ArrayUtil.join(ArrayUtil.resize(ja.toArray(), totalItem), ",");
                // 根据uuid字串获取物品实际数据json串
                String getUrl = fetchUrl + suffix;
                Thread.sleep(200);
                String s1 = HttpUtil
                        .get(getUrl);
                // 将json串转换为JSONObject
                JSONObject jo2 = JSONObject.parseObject(s1);
                // 计算价格平均值
                int avgPrice = 0;
                int avgNum = totalItem;
                System.out.println("avgNum ========> " + avgNum);
                if (avgNum >= 1) {
                    System.out.println("-------------- " + itemName + " -----------------------");
                    for (int index = 0; index < totalItem; index++) {
                        Double curPriceDouble = Double.parseDouble(jo2.getJSONArray("result").getJSONObject(index).getJSONObject("listing").getJSONObject("price").get("amount").toString());
                        int curPrice = curPriceDouble.intValue();
                        String curPriceType = jo2.getJSONArray("result").getJSONObject(index).getJSONObject("listing").getJSONObject("price").get("currency").toString();
                        if (curPriceType.equals("chaos")) {
                            avgPrice = avgPrice + curPrice;
                        } else if (curPriceType.equals("exalted")) {
                            avgPrice = avgPrice + curPrice * exaltedPrice;
                        } else {
                            if (avgNum > 1) {
                                avgNum -= 1;
                            }
                        }
                    }
                    avgPrice = avgPrice / avgNum;
                    PoeItemPrice pip = new PoeItemPrice();
                    pip.setItemCurPrice(avgPrice + " 混沌石");
                    Date dNow = new Date(); // 当前时间
                    String formatTime = df.format(dNow);
                    pip.setItemRecordTime(formatTime);
                    pip.setItemType(item_tag);
                    pip.setItemDesc(itemType);
                    pip.setItemName(itemName);
                    // 是否流行
                    if (totalResult > 10) {
                        pip.setItemIsPopular("是");
                    } else {
                        pip.setItemIsPopular("否");
                    }
                    pip.setItemFilters("");
                    poeItemPriceService.addNewItem(pip);
                } else {
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("item_name", itemName);
                    map.put("item_desc", itemType);
                    poeItemPriceService.removeByMap(map);
                }
            } catch (Exception e) {
                System.out.println("异常");
                int relaxTime = CommonUtil.genRandom(100, 1000);
                Thread.sleep(relaxTime);
                e.printStackTrace();
            }
            Thread.sleep(500);

        }
    }

    // @Scheduled(cron = "15 0/30 * * * *")
    public void timer() {
        String item_tag = "通货";
        //获取当前时间
        LocalDateTime localDateTime = LocalDateTime.now();
        System.out.println("当前时间为:" + localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        try {
            String json = "{\"query\":{\"status\":{\"option\":\"any\"},\"type\":\"崇高石\",\"stats\":[{\"type\":\"and\",\"filters\":[],\"disabled\":false}],\"filters\":{\"trade_filters\":{\"filters\":{\"price\":{\"min\":1,\"option\":\"chaos\"}}}}},\"sort\":{\"price\":\"asc\"}}";
            String result2 = HttpRequest.post(url)
                    .body(json)
                    .execute().body();
            System.out.println(result2);
            JSONObject jsonObject = JSONObject.parseObject(result2);
            JSONArray jsonArray = JSONArray.parseArray(jsonObject.get("result").toString());
            String suffix = "";
            for (int i = 0; i < 10; i++) {
                suffix = suffix + jsonArray.get(i) + ",";
            }
            suffix = StrUtil.sub(suffix, 0, -1);
            url = fetchUrl + suffix;
            String s = HttpUtil.get(url);

            // 取出实际价格
            JSONObject jsonObject1 = JSONObject.parseObject(s);
            int sum = 0;
            for (int i = 0; i < 10; i++) {
                //System.out.println(jsonObject1.getJSONArray("result").getJSONObject(i).getJSONObject("listing").getJSONObject("price").get("amount").toString());
                Double curPricePre = Double.parseDouble(jsonObject1.getJSONArray("result").getJSONObject(i).getJSONObject("listing").getJSONObject("price").get("amount").toString());
                sum = sum + curPricePre.intValue();
            }
            System.out.println("当前EC比例为：" + sum / 10);
            exaltedPrice = sum / 10;

            PoeItemPrice pip = new PoeItemPrice();
            pip.setChaosPrice(0);
            pip.setItemCurPrice(sum / 10 + " 混沌石");
            Date dNow = new Date(); // 当前时间
            String formatTime = df.format(dNow);
            pip.setItemRecordTime(formatTime);
            pip.setItemType(item_tag);
            pip.setItemDesc("");
            pip.setItemName("崇高石");
            pip.setItemFilters("");
            poeItemPriceService.addNewItem(pip);
        } catch (Exception e) {
            System.out.println("异常");
            e.printStackTrace();
        }
    }
}
