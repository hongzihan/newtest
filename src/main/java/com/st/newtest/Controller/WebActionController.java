package com.st.newtest.Controller;

import com.alibaba.fastjson.JSON;
import com.st.newtest.Entity.CrudStItem;
import com.st.newtest.Entity.WebAction;
import com.st.newtest.Service.WebActionService;
import com.st.newtest.Util.CommonUtil;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

@RequestMapping("/webAction")
@Controller
public class WebActionController {
    @Autowired
    private WebActionService webActionService;

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/saveActionData_item")
    public String saveActionData_item(String zoneid, String playername, String keyname, Integer count, Integer type, HttpSession session) {
        if (!CommonUtil.userIsVaild(session)) { // 用户合法性验证
            return "{code:404, msg:'失败'}";
        }
        if (zoneid == "" || playername == "" || keyname == "" || count == 0 || (type != 1 && type != 2)) {
            return "{code:404, msg:'失败'}";
        }
        HashMap<String, Object> objMap = new HashMap<>();
        objMap.put("playername", playername);
        objMap.put("keyname", keyname);
        objMap.put("count", count);

        WebAction webAction = new WebAction();
        webAction.setZoneid(zoneid);
        webAction.setActiontype(type);
        webAction.setActiondata(JSON.toJSONString(objMap));
        webActionService.insert(webAction);
        return "{code:200, msg:'成功'}";
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/saveActionData_var")
    public String saveActionData_var(String zoneid, String targetobj, String vartype, String varname, String varvalue, Integer areatype, String playername, HttpSession session) {
        if (!CommonUtil.userIsVaild(session)) { // 用户合法性验证
            return "{code:404, msg:'失败'}";
        }
        if (zoneid == "" || targetobj == "" || vartype == "" || varname == "" || varvalue == "") {
            return "{code:404, msg:'失败'}";
        }
        Integer type = 0;
        if (targetobj.equals("系统") && vartype.equals("str")) {
            type = 3;
        } else if(targetobj.equals("系统") && vartype.equals("int")) {
            type = 4;
        } else if(targetobj.equals("玩家") && vartype.equals("str")) {
            type = 5;
        } else if(targetobj.equals("玩家") && vartype.equals("int")) {
            type = 6;
        } else {
            return "{code:404, msg:'失败'}";
        }
        if ((type == 5 || type == 6) && playername == "") {
            return "{code:404, msg:'失败'}";
        }
        if ((type == 3 || type == 4) && (areatype < 0 || areatype > 6)) {
            return "{code:404, msg:'失败'}";
        }
        // 准备整合jsondata数据
        HashMap<String, Object> sohmap = new HashMap<>();
        sohmap.put("varname", varname);
        sohmap.put("varvalue", varvalue);
        sohmap.put("areatype", areatype);
        sohmap.put("playername", playername);

        WebAction webAction = new WebAction();
        webAction.setZoneid(zoneid);
        webAction.setActiontype(type);
        webAction.setActiondata(JSON.toJSONString(sohmap));
        webActionService.insert(webAction);
        return "{code:200, msg:'成功'}";
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value="/saveActionData_mail")
    public String saveActionData_mail(String zoneid, String playername, String mailtitle, String mailtext, Integer gold, Integer yuanbao, Integer integral, String templates, HttpSession session) {
        if (!CommonUtil.userIsVaild(session)) { // 用户合法性验证
            return "{code:404, msg:'失败'}";
        }
        if (zoneid == "" || playername == "" || mailtitle == "" || templates == "") {
            return "{code:404, msg:'失败'}";
        }
        HashMap<String, Object> hmap = new HashMap<>();
        hmap.put("playername", playername);
        hmap.put("title", mailtitle);
        hmap.put("text", mailtext);
        hmap.put("gold", gold);
        hmap.put("yuanbao", yuanbao);
        hmap.put("integral", integral);
        hmap.put("templates", templates);
        WebAction webAction = new WebAction();
        webAction.setActiondata(JSON.toJSONString(hmap));
        webAction.setActiontype(7);
        webAction.setZoneid(zoneid);
        webActionService.insert(webAction);
        return "{code:200, msg:'成功'}";
    }

    /**
     * 用于神途客户端来获取数据库数据使用，无需进行用户验证
     * @param zoneid
     * @return
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value="/getActionData")
    public String getActionData(String zoneid) {
        List<WebAction> list = webActionService.selectAndDelAllByZoneid(zoneid);
        return JSON.toJSONString(list);
    }
}
