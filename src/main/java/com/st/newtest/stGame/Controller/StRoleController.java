package com.st.newtest.stGame.Controller;


import com.st.newtest.Util.CommonUtil;
import com.st.newtest.stGame.Entity.StRole;
import com.st.newtest.stGame.Service.StRoleService;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author ken
 * @since 2020-09-23
 */
@Controller
@RequestMapping("/st/role")
public class StRoleController {
    @Autowired
    private StRoleService stRoleService;

    @RequiresRoles(value = {"supermanager","servicer","administrator"},logical= Logical.OR)
    @RequestMapping("/main")
    public ModelAndView main(String zoneName) {
        List<StRole> roleList = stRoleService.lambdaQuery().eq(StRole::getZoneName, zoneName).list();
        List<StRole> roleZoneNameList = stRoleService.lambdaQuery().groupBy(StRole::getZoneName).select(StRole::getZoneName).list();
        ModelAndView mav = CommonUtil.getPage("roleManage");
        if (roleList != null) {
            mav.addObject("roleList", roleList);
        }
        if (roleZoneNameList != null) {
            mav.addObject("roleZoneNameList", roleZoneNameList);
        }
        mav.addObject("zoneName",zoneName);
        return mav;
    }

    @RequiresRoles(value = {"supermanager", "servicer", "administrator"}, logical = Logical.OR)
    @RequestMapping("/search")
    public String search() {
        return "";
    }
}

