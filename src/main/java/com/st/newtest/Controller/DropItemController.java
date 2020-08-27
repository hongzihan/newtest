package com.st.newtest.Controller;

import com.alibaba.fastjson.JSON;
import com.st.newtest.Entity.DropItem;
import com.st.newtest.Service.DropItemService;
import com.st.newtest.Util.CommonUtil;
import com.st.newtest.Util.ExportUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.Result;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/dropItem")
@RestController
public class DropItemController {
    @Autowired
    private DropItemService dropItemService;

    private int countT = 0;

    @RequestMapping(method = RequestMethod.POST, value="/insertNewData")
    public String insertNewData(String keyname, String itemname, Integer count, String zoneid) {
        countT += 1;
        DropItem dropItem = new DropItem();
        dropItem.setCount(count);
        dropItem.setKeyname(keyname);
        dropItem.setItemname(itemname);
        dropItem.setZoneid(zoneid);
        dropItem.setId(0);
        dropItemService.insertNewData(dropItem);
        return "成功接收参数 keyname = " + keyname + "itemname = " + itemname + "count = " + count + "zoneid = " + zoneid + "countT = " + countT;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/showAll")
    public String showAll() {
        return JSON.toJSONString(CommonUtil.removeNullOfList(dropItemService.selectAll()));
    }

    @RequestMapping(method = RequestMethod.POST, value = "/showAllByZoneid")
    public String showAllByZoneid(String zoneid) {
        return JSON.toJSONString(CommonUtil.removeNullOfList(dropItemService.selectAllByZoneid(zoneid)));
    }

    @GetMapping("/file")
    public Result download(HttpServletResponse response) {
        List<Map<String, Object>> dataList = null;

        List<DropItem> itemList = CommonUtil.removeNullOfList(dropItemService.selectAll());// 查询到要导出的信息

        if (itemList.size() == 0) {
            // ResultUtil.failure("无数据导出");
        }
        String sTitle = "id,物品名称,物品索引名,物品数量,区id";
        String fName = "掉落统计_";
        String mapKey = "id,itemname,keyname,count,zoneid";
        dataList = new ArrayList<>();
        Map<String, Object> map = null;
        for (DropItem dropItem : itemList) {
            map = new HashMap<>();
            map.put("id", dropItem.getId());
            map.put("itemname", dropItem.getItemname());
            map.put("keyname", dropItem.getKeyname());
            map.put("count", dropItem.getCount());
            map.put("zoneid", dropItem.getZoneid());
            dataList.add(map);
        }
        try (final OutputStream os = response.getOutputStream()) {
            ExportUtil.responseSetProperties(fName, response);
            ExportUtil.doExport(dataList, sTitle, mapKey, os);
            return null;
        } catch (Exception e) {
            // log.error("生成csv文件失败", e);
        }
        return null;
    }
}
