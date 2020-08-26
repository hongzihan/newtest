package com.st.newtest.Controller;

import com.alibaba.fastjson.JSON;
import com.st.newtest.Entity.CountItem;
import com.st.newtest.Service.CountItemService;
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

@RequestMapping("/countItem")
@RestController
public class ItemStatisticsController {
    @Autowired
    private CountItemService countItemService;

    @RequestMapping(method = RequestMethod.POST, value="/testOneData")
    public CountItem findOne(String keyname) {
        return countItemService.selectByItemKey(keyname);
    }

    @RequestMapping(method = RequestMethod.POST, value="/insertNewData")
    public String insertNewData(String keyname, Integer count) {
        CountItem countItem = new CountItem();
        countItem.setCount(count);
        countItem.setKeyname(keyname);
        countItem.setId(0);
        countItemService.insertNewData(countItem);
        return "成功";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/showAllItemStatistics")
    public String showAll() {
        return JSON.toJSONString(countItemService.selectAll());
    }

    @GetMapping("/file")
    public Result download(HttpServletResponse response) {
        List<Map<String, Object>> dataList = null;

        List<CountItem> itemList = countItemService.selectAll();// 查询到要导出的信息

        if (itemList.size() == 0) {
            // ResultUtil.failure("无数据导出");
        }
        String sTitle = "id,物品名称,物品使用数量";
        String fName = "log_";
        String mapKey = "id,username,operation,method,createDate";
        dataList = new ArrayList<>();
        Map<String, Object> map = null;
        for (CountItem countItem : itemList) {
            map = new HashMap<>();

            map.put("id", countItem.getId());
            map.put("username", countItem.getKeyname());
            map.put("operation", countItem.getCount());

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
