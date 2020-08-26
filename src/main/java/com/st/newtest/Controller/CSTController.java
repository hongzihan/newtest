package com.st.newtest.Controller;

import com.alibaba.fastjson.JSON;
import com.st.newtest.Entity.CrudStItem;
import com.st.newtest.Service.CrudStItemService;
import com.st.newtest.aop.NoRepeatSubmit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@RequestMapping("/crudOfSt")
@Controller
public class CSTController {
    @Autowired
    private CrudStItemService crudStItemService;

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value="/getActionData")
    public String getActionData(String zoneid) {
        List<CrudStItem> list = crudStItemService.selectAndDelAllByZoneid(zoneid);
        return JSON.toJSONString(list);
    }

    @NoRepeatSubmit
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value="/saveActionData")
    public String saveActionData(String zoneid, String playername, String keyname, Integer count, Integer type) {
        if (zoneid == "" || playername == "" || keyname == "" || count == 0 || (type != 1 && type != 2)) {
            return "{code:404, msg:'失败'}";
        }
        CrudStItem crudStItem = new CrudStItem();
        crudStItem.setZoneid(zoneid);
        crudStItem.setPlayername(playername);
        crudStItem.setKeyname(keyname);
        crudStItem.setCount(count);
        crudStItem.setType(type);
        crudStItemService.insert(crudStItem);
        return "{code:200, msg:'成功'}";
    }
}
