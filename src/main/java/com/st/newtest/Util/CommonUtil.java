package com.st.newtest.Util;

import com.st.newtest.Entity.DropItem;
import com.st.newtest.Entity.User;
import org.apache.shiro.SecurityUtils;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
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
        return modelAndView;
    }

    public static Boolean userIsVaild(HttpSession session) {
        user = (User) session.getAttribute("user");
        if (user == null) {
            return false;
        }
        return true;
    }
}
