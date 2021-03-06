package com.st.newtest.stGame.Controller;

import com.st.newtest.stGame.Entity.DropItem;
import com.st.newtest.stGame.Service.DropItemService;
import com.st.newtest.Util.CommonUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RequestMapping("chart")
@Controller
public class ChartController {

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
            List<DropItem> dropItems = dropItemService.lambdaQuery().eq(DropItem::getZoneid, dropItem.getZoneid()).list();
            if (dropItems != null) {
                mav.addObject("dropItemList", dropItems);
            }
        }
        return mav;
    }
}
