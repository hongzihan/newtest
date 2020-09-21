package com.st.newtest.stGame.Controller;

import com.alibaba.fastjson.JSON;
import com.st.newtest.Entity.Config;
import com.st.newtest.Service.ConfigService;
import com.st.newtest.Util.ConfigUtil;
import com.st.newtest.stGame.Entity.Charge;
import com.st.newtest.stGame.Entity.ChatRecord;
import com.st.newtest.stGame.Entity.MonsterDie;
import com.st.newtest.stGame.Service.ChatRecordService;
import com.st.newtest.stGame.Service.DropItemService;
import com.st.newtest.stGame.Service.MonsterDieService;
import com.st.newtest.Util.CommonUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RequestMapping("/st")
@RestController
public class stController {

    @Autowired
    private DropItemService dropItemService;

    @Autowired
    private ChatRecordService chatRecordService;

    @Autowired
    private MonsterDieService monsterDieService;

    @Autowired
    private ConfigService configService;

    private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @RequestMapping(method = RequestMethod.GET, value = "/getHello/{name}")
    public ModelMap getHello(@PathVariable("name") String name) {
        ModelMap modelMap = new ModelMap();
        modelMap.addAttribute("code", "200");
        modelMap.addAttribute("message", "hello" + name);
        return modelMap;
    }

    @RequiresPermissions("searchMonsterMsg")
    @RequestMapping("/searchMonsterMsg")
    public ModelAndView searchMonsterMsg(String zonename) throws ParseException {
        List<MonsterDie> monsterDies = monsterDieService.lambdaQuery().eq(MonsterDie::getZonename, zonename).list();
        List<MonsterDie> auz = monsterDieService.lambdaQuery().groupBy(MonsterDie::getZonename).select(MonsterDie::getZonename).list();
        ModelAndView mav = CommonUtil.getPage("monsterDieMsg");
        if (monsterDies != null) {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date dNow = new Date( ); // 当前时间
            for (MonsterDie monsterDie : monsterDies) {
                if (monsterDie.getRelivetime() != null) {
                    Date x = df.parse(monsterDie.getDietime());
                    long l = x.getTime() + monsterDie.getRelivetime() * 1000; // 未来出生时间
                    monsterDie.setFutureBornTime(df.format(l));
                    if (dNow.getTime() - l >= 0) {
                        monsterDie.setMobStatus("存活");
                    } else {
                        monsterDie.setMobStatus("死亡");
                    }
                } else {
                    monsterDie.setMobStatus("未知");
                }
            }
            mav.addObject("mobList", monsterDies);
        }
        mav.addObject("zonename", zonename);
        mav.addObject("zoneList", auz);
        return mav;
    }

    @RequiresPermissions("clearRelive")
    @ResponseBody
    @RequestMapping("/clearRelive")
    public String clearRelive(MonsterDie monsterDie) {
        if (!monsterDieService.clearTargetReliveTime(monsterDie)) {
            return "failed";
        }
        return "success";
    }

    @RequiresPermissions("form-common")
    @RequestMapping("/form-common")
    public ModelAndView newForm1() {
        ModelAndView page = CommonUtil.getPage("form-common");
        List<Map> strings = configService.findAllZoneName();
        if (strings != null) {
            page.addObject("zoneNameList", strings);
        }
        return page;
    }

    @RequiresPermissions("chat_record")
    @RequestMapping("/chat_record")
    public ModelAndView chat_record(ChatRecord chatRecord) {
        ModelAndView mav = CommonUtil.getPage("chatRecord");

        // 查所有的区名
        List<String> stringList = chatRecordService.selectAllUniqueZoneName();
        if (stringList != null) {
            mav.addObject("zoneNameList", stringList);
        }
        mav.addObject("timeInterval", ConfigUtil.getChatNewMsgInterval());
        mav.addObject("preReadTime", ConfigUtil.getChatPreReadTime());
        mav.addObject("keywords", JSON.toJSONString(ConfigUtil.getChatKeywords()));
        if (chatRecord.getZoneName() != null) {
            // 查所有的用户名
            stringList = chatRecordService.selectAllUniqueUsernameByZoneName(chatRecord.getZoneName());
            if (stringList != null) {
                mav.addObject("userList", stringList);
            }
            mav.addObject("curZoneName", chatRecord.getZoneName());
            Integer preReadTime = ConfigUtil.getChatPreReadTime();
            if (chatRecord.getZoneName().equals("敏感词专区")) {
                preReadTime = ConfigUtil.getChatPreReadTime() * 24;
            }
            List<ChatRecord> chats = chatRecordService.selectAllNewMessageByZoneName(chatRecord.getZoneName(), preReadTime, 666);
            if (chats != null) {
                mav.addObject("recordList", chats);
            }
        }
        return mav;
    }

    @RequiresPermissions("chat_record")
    @RequestMapping("/getUniqueMessages")
    public String getUniqueMessages(ChatRecord chatRecord) {
        List<ChatRecord> list = chatRecordService.lambdaQuery().eq(ChatRecord::getZoneName, chatRecord.getZoneName()).eq(ChatRecord::getUsername, chatRecord.getUsername()).list();
        String result = "failed";
        if (list != null) {
            result = JSON.toJSONStringWithDateFormat(list, "yyyy-MM-dd HH:mm:ss");
        }
        return result;
    }

    @RequiresPermissions("chat_record")
    @RequestMapping("/getNewMessages")
    public String getNewMessages(ChatRecord chatRecord) {
        List<ChatRecord> crList = chatRecordService.selectAllNewMessageByZoneName(chatRecord.getZoneName(), ConfigUtil.getChatNewMsgInterval(), 300);
        String result = "failed";
        if (crList != null) {
            result = JSON.toJSONStringWithDateFormat(crList, "yyyy-MM-dd HH:mm:ss");
        }
        return result;
    }

    @RequiresPermissions("chat_record")
    @RequestMapping("/chatConfigSetting")
    public String chatConfigSetting(String param, Integer actionType) {
        switch (actionType) {
            case 1:
                ConfigUtil.setChatPreReadTime(Integer.parseInt(param));
                break;
            case 2:
                ConfigUtil.setChatNewMsgInterval(Integer.parseInt(param));
                break;
            case 3:
                ConfigUtil.addChatKeywords(param);
                break;
            case 4:
                ConfigUtil.delChatKeywords(param);
                break;
            default:
                return "failed";
        }
        return "success";
    }
}
