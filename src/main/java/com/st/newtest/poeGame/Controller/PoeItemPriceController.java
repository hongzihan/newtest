package com.st.newtest.poeGame.Controller;


import com.alibaba.fastjson.JSON;
import com.st.newtest.Timer.CheckPriceTimer;
import com.st.newtest.Util.CommonUtil;
import com.st.newtest.poeGame.Entity.PoeItemPrice;
import com.st.newtest.poeGame.Service.PoeItemPriceService;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author ken
 * @since 2020-11-02
 */
@Controller
@RequestMapping("/poeItemPrice")
public class PoeItemPriceController {
    @Autowired
    private PoeItemPriceService poeItemPriceService;

    @RequestMapping("/updatePricePage")
    public ModelAndView pricePage(String itemType) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("form-updatePrice");
        return modelAndView;
    }

    @RequestMapping("/index")
    public ModelAndView mainPage(String itemType) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("poe-price-index");
        List<PoeItemPrice> exList = poeItemPriceService.lambdaQuery().eq(PoeItemPrice::getItemName, "崇高石").list();
        String itemCurPrice = exList.get(0).getItemCurPrice();
        String[] s = itemCurPrice.split(" ");
        int exPrice = Integer.parseInt(s[0]);
        List<PoeItemPrice> list = null;
        if (!(itemType != null && !itemType.equals(""))) {
            itemType = "通货";
        }
        list = poeItemPriceService.lambdaQuery().eq(PoeItemPrice::getItemType, itemType).list();
        for (PoeItemPrice poeItemPrice : list) {
            String curItemPriceStr = poeItemPrice.getItemCurPrice();
            String[] ss = curItemPriceStr.split(" ");
            int curPrice = 0;
            if (ss[1].equals("崇高石")) {
                curPrice = Integer.parseInt(ss[0]) * exPrice;
            } else {
                curPrice = Integer.parseInt(ss[0]);
            }
            poeItemPrice.setChaosPrice(curPrice);
        }
        list.sort(new Comparator<PoeItemPrice>() {
            @Override
            public int compare(PoeItemPrice o1, PoeItemPrice o2) {
                return o2.getChaosPrice().compareTo(o1.getChaosPrice());
            }
        });
        modelAndView.addObject("itemList", list);
        return modelAndView;
    }

    @RequestMapping("/search")
    @ResponseBody
    public String search(String itemName) {
        List<PoeItemPrice> list = poeItemPriceService.lambdaQuery().eq(PoeItemPrice::getItemName, itemName).list();
        return JSON.toJSONString(list);
    }

    @RequestMapping("/BlurrySearch")
    @ResponseBody
    public String BlurrySearch(String itemName) {
        List<PoeItemPrice> list = poeItemPriceService.lambdaQuery().like(PoeItemPrice::getItemName, itemName).list();
        return JSON.toJSONString(list);
    }

    @RequiresRoles("supermanager")
    @RequestMapping("/addNewItem")
    @ResponseBody
    public String addNewItem(PoeItemPrice poeItemPrice, String itemValueUnit) {
        poeItemPrice.setItemCurPrice(poeItemPrice.getItemCurPrice() + " " + itemValueUnit);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date dNow = new Date( ); // 当前时间
        String formatTime = df.format(dNow);
        poeItemPrice.setItemRecordTime(formatTime);
        boolean save = poeItemPriceService.addNewItem(poeItemPrice);
        if (save) {
            return "{code:200, msg:'成功'}";
        } else {
            return "{code:500, msg:'失败'}";
        }
    }

    @RequiresRoles("supermanager")
    @RequestMapping("/updateItemPriceByMainType")
    @ResponseBody
    public String updateItemPriceByMainType(String itemMainType) throws InterruptedException {
        System.out.println(itemMainType);
        CheckPriceTimer checkPriceTimer = new CheckPriceTimer();
        if (itemMainType.equals("技能宝石")) {
            checkPriceTimer.gemTimer(poeItemPriceService);
        } else if (itemMainType.equals("预言")) {
            checkPriceTimer.prophecyTimer(poeItemPriceService);
        } else if (itemMainType.equals("传奇地图")) {
            checkPriceTimer.uniqueMapTimer(poeItemPriceService);
        } else if (itemMainType.equals("珠宝")) {
            checkPriceTimer.jewelTimer(poeItemPriceService);
        } else if (itemMainType.equals("命运卡")) {
            checkPriceTimer.cardTimer(poeItemPriceService);
        } else if (itemMainType.equals("药剂")) {
            checkPriceTimer.flaskTimer(poeItemPriceService);
        } else if (itemMainType.equals("传奇首饰")) {
            checkPriceTimer.uniqueAccessoriesTimer(poeItemPriceService);
        } else if (itemMainType.equals("传奇武器")) {
            checkPriceTimer.uniqueWeaponTimer(poeItemPriceService);
        } else if (itemMainType.equals("传奇护甲1")) {
            checkPriceTimer.uniqueArmour1Timer(poeItemPriceService);
        } else if (itemMainType.equals("传奇护甲2")) {
            checkPriceTimer.uniqueArmour2Timer(poeItemPriceService);
        } else if (itemMainType.equals("精华")) {
            checkPriceTimer.essenceTimer(poeItemPriceService);
        } else if (itemMainType.equals("化石")) {
            checkPriceTimer.fossilTimer(poeItemPriceService);
        } else if (itemMainType.equals("圣油")) {
            checkPriceTimer.oilTimer(poeItemPriceService);
        } else if (itemMainType.equals("共振器")) {
            checkPriceTimer.resonatorsTimer(poeItemPriceService);
        } else if (itemMainType.equals("魔瓶")) {
            checkPriceTimer.vialTimer(poeItemPriceService);
        } else if (itemMainType.equals("孕育石")) {
            checkPriceTimer.incubatorsTimer(poeItemPriceService);
        } else if (itemMainType.equals("梦魇宝珠")) {
            checkPriceTimer.deliriumOrbTimer(poeItemPriceService);
        } else if (itemMainType.equals("通货")) {
            checkPriceTimer.currencyTimer(poeItemPriceService);
        } else if (itemMainType.equals("碎片")) {
            checkPriceTimer.fragmentTimer(poeItemPriceService);
        } else {
            return "{code:500, msg:'失败'}";
        }
        return "{code:200, msg:'成功'}";
    }

    @RequiresRoles("supermanager")
    @RequestMapping("/editItem")
    @ResponseBody
    public String editItem(PoeItemPrice poeItemPrice, String itemValueUnit) {
        poeItemPrice.setItemCurPrice(poeItemPrice.getItemCurPrice() + " " + itemValueUnit);
        boolean update = poeItemPriceService.updateItem(poeItemPrice);
        if (update) {
            return "{code:200, msg:'成功'}";
        } else {
            return "{code:500, msg:'失败'}";
        }
    }

    @RequiresRoles("supermanager")
    @RequestMapping("/delItem")
    @ResponseBody
    public String delItem(PoeItemPrice poeItemPrice) {
        boolean remove = poeItemPriceService.lambdaUpdate().eq(PoeItemPrice::getItemName, poeItemPrice.getItemName()).eq(PoeItemPrice::getItemRecordTime, poeItemPrice.getItemRecordTime())
                .remove();
        if (remove) {
            return "{code:200, msg:'成功'}";
        } else {
            return "{code:500, msg:'失败'}";
        }
    }
}

