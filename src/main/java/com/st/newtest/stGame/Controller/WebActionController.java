package com.st.newtest.stGame.Controller;

import com.alibaba.fastjson.JSON;
import com.st.newtest.Util.stUtil;
import com.st.newtest.stGame.Entity.WebAction;
import com.st.newtest.stGame.Service.WebActionService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import sun.security.x509.OtherName;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;

@RequestMapping("/webAction")
@Controller
public class WebActionController {
    @Autowired
    private WebActionService webActionService;

    @RequiresPermissions("saveActionData_item")
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/saveActionData_item")
    public String saveActionData_item(String zoneid, String playername, String keyname, Integer count, Integer type, HttpSession session) {
        if (zoneid == "" || playername == "" || keyname == "" || count == 0 || (type != 1 && type != 2)) {
            return "{code:404, msg:'失败'}";
        }
        HashMap<String, Object> objMap = new HashMap<>();
        objMap.put("playername", playername);
        objMap.put("keyname", keyname);
        objMap.put("count", count);
        type = stUtil.getWebActionTypeMap().get("wat_物品操作" + type);
        if (type == null) {
            return "{code:404, msg:'失败'}";
        }
        WebAction webAction = new WebAction();
        webAction.setZoneid(zoneid);
        webAction.setActiontype(type);
        webAction.setActiondata(JSON.toJSONString(objMap));
        webActionService.save(webAction);
        return "{code:200, msg:'成功'}";
    }

    @RequiresPermissions("saveActionData_var")
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/saveActionData_var")
    public String saveActionData_var(String zoneid, String targetobj, String vartype, String varname, String varvalue, Integer areatype, String playername, HttpSession session) {
        if (zoneid == "" || targetobj == "" || vartype == "" || varname == "" || varvalue == "") {
            return "{code:404, msg:'失败'}";
        }
        Integer type = stUtil.getWebActionTypeMap().get("wat_" + targetobj + vartype);
        if (type == null) {
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
        webActionService.save(webAction);
        return "{code:200, msg:'成功'}";
    }

    @RequiresPermissions("saveActionData_mail")
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value="/saveActionData_mail")
    public String saveActionData_mail(String zoneid, String playername, String mailtitle, String mailtext, Integer gold, Integer yuanbao, Integer integral, String templates, HttpSession session) {
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
        Integer type = stUtil.getWebActionTypeMap().get("wat_邮件操作");
        if (type == null) {
            return "{code:404, msg:'失败'}";
        }
        webAction.setActiontype(type);
        webAction.setZoneid(zoneid);
        webActionService.save(webAction);
        return "{code:200, msg:'成功'}";
    }

    @RequiresPermissions("saveActionData_give")
    @ResponseBody
    @RequestMapping("saveActionData_give")
    public String saveActionData_give(WebAction webAction, String username, Integer num, String targetObj) {
        if (username == null || webAction.getZoneid() == null) {
            return "{code:404, msg:'失败'}";
        }
        // actionType
        Integer type = stUtil.getWebActionTypeMap().get("wat_给予操作");
        if (type == null) {
            return "{code:404, msg:'失败'}";
        }
        webAction.setActiontype(type);
        // actionData
        HashMap<String, Object> hmap = new HashMap<>();
        hmap.put("username", username);
        hmap.put("num", num);
        hmap.put("type", targetObj);
        webAction.setActiondata(JSON.toJSONString(hmap));
        webActionService.save(webAction);
        return "{code:200, msg:'成功'}";
    }

    @RequiresPermissions("saveActionData_charge")
    @ResponseBody
    @RequestMapping("saveActionData_charge")
    public String saveActionData_charge(WebAction webAction, String username, Integer num, Integer reChargePercent) {
        if (username == null || webAction.getZoneid() == null) {
            return "{code:404, msg:'失败'}";
        }
        // actionType
        Integer type = stUtil.getWebActionTypeMap().get("wat_模拟充值");
        if (type == null) {
            return "{code:404, msg:'失败'}";
        }
        webAction.setActiontype(type);
        // actionData
        HashMap<String, Object> hmap = new HashMap<>();
        hmap.put("username", username);
        hmap.put("num", num);
        if (reChargePercent == null) {
            hmap.put("chargePercent", 0);
        } else {
            hmap.put("chargePercent", reChargePercent);
        }
        webAction.setActiondata(JSON.toJSONString(hmap));
        webActionService.save(webAction);
        return "{code:200, msg:'成功'}";
    }

    @RequiresPermissions("saveActionData_monster")
    @ResponseBody
    @RequestMapping("saveActionData_monster")
    public String saveActionData_monster(WebAction webAction, String mobKey, String mapKey, Integer num, Integer coordinateX, Integer coordinateY, Integer range, Integer cType) {
        if (mobKey == null || webAction.getZoneid() == null || mapKey == null) {
            return "{code:404, msg:'失败'}";
        }
        // actionType
        Integer type = stUtil.getWebActionTypeMap().get("wat_怪物操作");
        if (type == null) {
            return "{code:404, msg:'失败'}";
        }
        webAction.setActiontype(type);
        // actionData
        HashMap<String, Object> hmap = new HashMap<>();
        hmap.put("mobKey", mobKey);
        hmap.put("mapKey", mapKey);
        hmap.put("cType", cType);
        if (cType == 1 || cType == 3 || cType == 4) {
            if (coordinateX == null || coordinateY == null) {
                hmap.put("coordinateX", 0);
                hmap.put("coordinateY", 0);
            } else {
                hmap.put("coordinateX", coordinateX);
                hmap.put("coordinateY", coordinateY);
            }
            if (range == null) {
                hmap.put("range", 1);
            } else {
                hmap.put("range", range);
            }
        }
        if (num == null) {
            hmap.put("num", 1);
        } else {
            hmap.put("num", num);
        }

        webAction.setActiondata(JSON.toJSONString(hmap));
        webActionService.save(webAction);
        return "{code:200, msg:'成功'}";
    }

    @RequiresPermissions("saveActionData_message")
    @ResponseBody
    @RequestMapping("saveActionData_message")
    public String saveActionData_message(WebAction webAction, String content, String username, Integer msgType, Integer foreground, Integer background) {
        if (content == null || msgType == null || webAction.getZoneid() == null) {
            return "{code:404, msg:'失败'}";
        }
        // actionType
        Integer type = stUtil.getWebActionTypeMap().get("wat_消息操作");
        if (type == null) {
            return "{code:404, msg:'失败'}";
        }
        webAction.setActiontype(type);
        // actionData
        HashMap<String, Object> hmap = new HashMap<>();
        if (username != null) {
            hmap.put("username", username);
        } else {
            hmap.put("username", "");
        }
        hmap.put("msgType", msgType);
        hmap.put("content", content);
        hmap.put("foreground", foreground);
        hmap.put("background", background);
        webAction.setActiondata(JSON.toJSONString(hmap));
        webActionService.save(webAction);
        return "{code:200, msg:'成功'}";
    }

    @RequiresPermissions("saveActionData_equip")
    @ResponseBody
    @RequestMapping("saveActionData_equip")
    public String saveActionData_equip(WebAction webAction, String originUsername, String targetUsername) {
        if (originUsername == null || targetUsername == null || webAction.getZoneid() == null) {
            return "{code:404, msg:'失败'}";
        }
        // actionType
        Integer type = stUtil.getWebActionTypeMap().get("wat_装备复制");
        if (type == null) {
            return "{code:404, msg:'失败'}";
        }
        webAction.setActiontype(type);
        // actionData
        HashMap<String, Object> hmap = new HashMap<>();
        hmap.put("originUsername", originUsername);
        hmap.put("targetUsername", targetUsername);
        webAction.setActiondata(JSON.toJSONString(hmap));
        webActionService.save(webAction);
        return "{code:200, msg:'成功'}";
    }

    @RequiresPermissions("saveActionData_var_transfer")
    @ResponseBody
    @RequestMapping("saveActionData_var_transfer")
    public String saveActionData_var_transfer(WebAction webAction, String usernameBefore, String accountBefore, String idBefore, String usernameAfter, String accountAfter, String idAfter, String accountSerialize) {
        if (accountBefore == null || idBefore == null || accountAfter == null || idAfter == null) {
            return "{code:404, msg:'失败'}";
        }
        // actionType
        Integer type = stUtil.getWebActionTypeMap().get("wat_变量转移");
        if (type == null) {
            return "{code:404, msg:'失败'}";
        }
        webAction.setActiontype(type);
        // actionData
        HashMap<String, Object> hmap = new HashMap<>();
        hmap.put("usernameBefore", usernameBefore == null ? "" : usernameBefore);
        hmap.put("accountBefore", accountBefore);
        hmap.put("idBefore", idBefore);
        hmap.put("usernameAfter", usernameAfter == null ? "" : usernameAfter);
        hmap.put("accountAfter", accountAfter);
        hmap.put("idAfter", idAfter);
        if (accountSerialize == null) {
            hmap.put("accountSerialize", "");
        } else {
            hmap.put("accountSerialize", accountSerialize);
        }

        webAction.setActiondata(JSON.toJSONString(hmap));
        webActionService.save(webAction);
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
