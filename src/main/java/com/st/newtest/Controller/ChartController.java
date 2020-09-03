package com.st.newtest.Controller;

import com.st.newtest.Entity.DropItem;
import com.st.newtest.Service.DropItemService;
import com.st.newtest.Service.OpenStService;
import com.st.newtest.Util.CommonUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class ChartController {

    @Autowired
    private OpenStService openStService;

    @Autowired
    private DropItemService dropItemService;

    @RequiresPermissions("charts")
    @RequestMapping("/charts")
    public ModelAndView charts(DropItem dropItem) {
        ModelAndView mav = CommonUtil.getPage("charts");
        List<String> strings = dropItemService.selectAllUniqueZoneName();
        if (strings != null) {
            mav.addObject("zoneNameList", strings);
        }
        if (dropItem.getZoneid() != null) {
            mav.addObject("curZoneName", dropItem.getZoneid());
            List<DropItem> dropItems = dropItemService.selectAllByZoneid(dropItem.getZoneid());
            if (dropItems != null) {
                mav.addObject("dropItemList", dropItems);
            }
        }
        return mav;
    }
}
