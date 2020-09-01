package com.st.newtest.Controller;

import com.alibaba.fastjson.JSON;
import com.st.newtest.Entity.MonsterDie;
import com.st.newtest.Entity.TbItem;
import com.st.newtest.Service.OpenStService;
import com.st.newtest.Service.TbItemService;
import com.st.newtest.Util.CommonUtil;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RequestMapping("/st")
@RestController
public class stController {

    @Autowired
    private TbItemService tbItemService;

    @Autowired
    private OpenStService openStService;

    public static String getEncoding(String str) {
        String encode = "GB2312";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) {
                String s = encode;
                return s;
            }
        } catch (Exception exception) {
        }
        encode = "ISO-8859-1";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) {
                String s1 = encode;
                return s1;
            }
        } catch (Exception exception1) {
        }
        encode = "UTF-8";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) {
                String s2 = encode;
                return s2;
            }
        } catch (Exception exception2) {
        }
        encode = "GBK";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) {
                String s3 = encode;
                return s3;
            }
        } catch (Exception exception3) {
        }
        return "";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/getHello/{name}")
    public ModelMap getHello(@PathVariable("name") String name) {
        ModelMap modelMap = new ModelMap();
        modelMap.addAttribute("code", "200");
        modelMap.addAttribute("message", "hello" + name);
        return modelMap;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/postHello")
    public String postHello(String keyname, String username, Integer count) throws UnsupportedEncodingException {
        TbItem tbItem = new TbItem();
        tbItem.setId(0);
        tbItem.setKeyname(keyname);
        tbItem.setUsername(username);
        tbItem.setCount(count);
        tbItemService.insert(tbItem);
        return "使用道具成功";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/showAll")
    public String showAll() {
        return JSON.toJSONString(tbItemService.selectAll());
    }

    @RequiresRoles("supermanager")
    @RequestMapping("/searchMonsterMsg")
    public ModelAndView searchMonsterMsg(String zonename) throws ParseException {
        List<MonsterDie> monsterDies = openStService.selectByZoneName(zonename);
        List<MonsterDie> auz = openStService.selectAllUniqueZoneName();
        ModelAndView mav = CommonUtil.getPage("monsterDieMsg");
        if (monsterDies != null) {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            for (MonsterDie monsterDie : monsterDies) {
                if (monsterDie.getRelivetime() != null) {
                    Date x = df.parse(monsterDie.getDietime());
                    monsterDie.setFutureBornTime(df.format(x.getTime() + monsterDie.getRelivetime() * 1000));
                }
            }
            mav.addObject("mobList", monsterDies);
        }
        mav.addObject("zonename", zonename);
        mav.addObject("zoneList", auz);
        return mav;
    }

}
