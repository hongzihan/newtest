package com.st.newtest.stGame.Service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.st.newtest.stGame.Entity.DropItem;

import java.util.List;

public interface DropItemService extends IService<DropItem> {

    Boolean insertNewData(DropItem dropItem);

    List<String> selectAllUniqueZoneName();
}
