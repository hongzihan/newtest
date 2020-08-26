package com.st.newtest.Util;



import com.st.newtest.Entity.DropItem;

import java.util.ArrayList;
import java.util.List;

public class CommonUtil {
    public static List<DropItem> removeNullOfList(List<DropItem> list) {
        List<DropItem> list2 = new ArrayList<>();
        for (DropItem dr : list) {
            if (dr.getZoneid() != null) {
                list2.add(dr);
            }
        }
        return list2;
    }
}
