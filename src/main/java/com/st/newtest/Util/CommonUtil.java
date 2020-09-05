package com.st.newtest.Util;

import com.st.newtest.Entity.DropItem;
import com.st.newtest.Entity.User;
import org.apache.shiro.SecurityUtils;
import org.springframework.web.servlet.ModelAndView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CommonUtil {
    private static User user;

    public static List<DropItem> removeNullOfList(List<DropItem> list) {
        List<DropItem> list2 = new ArrayList<>();
        for (DropItem dr : list) {
            if (dr.getZoneid() != null) {
                list2.add(dr);
            }
        }
        return list2;
    }

    public static ModelAndView getPage(String redirectPage) {
        user = (User) SecurityUtils.getSubject().getPrincipals().getPrimaryPrincipal();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName(redirectPage);
        modelAndView.addObject("nickname", user.getNickname() );
        modelAndView.addObject("curPage", redirectPage);
        modelAndView.addObject("yourRoles", user.getRoleList());
        return modelAndView;
    }

    public static long calTimeInterval(String a, String b) throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date a1 = df.parse(a);
        Date b1 = df.parse(b);
        if (a1.getTime() - b1.getTime() < 0) {
            return 0;
        }
        return (a1.getTime() - b1.getTime()) / (1 * 1000);
    }
}
