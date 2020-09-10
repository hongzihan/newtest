package com.st.newtest.stGame.Service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.st.newtest.stGame.Entity.DropItem;
import com.st.newtest.stGame.Mapper.DropItemMapper;
import com.st.newtest.stGame.Service.DropItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service("dropItemServiceImpl")
public class DropItemServiceImpl extends ServiceImpl<DropItemMapper, DropItem> implements DropItemService {
    @Autowired(required = false)
    private DropItemMapper dropItemMapper;

    private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public Boolean insertNewData(DropItem dropItem) {
        // 查出该数据是否已经存在在数据库中
        String keyname = dropItem.getKeyname();
        String itemname = dropItem.getItemname();
        Integer count = dropItem.getCount();
        String zoneid = dropItem.getZoneid();
        // 获取当前日期
        Date date = new Date();
        String dateTime = df.format(date.getTime());
        List<DropItem> originDropItemList = new LambdaQueryChainWrapper<DropItem>(dropItemMapper).eq(DropItem::getKeyname, keyname).list();
        for (DropItem originDropItem : originDropItemList) { // 如果表中存在完全相等的数据，则更新进数据表
            if (originDropItem.getKeyname().equals(keyname) && originDropItem.getZoneid().equals(zoneid)) {
                originDropItem.setCount(originDropItem.getCount() + dropItem.getCount());
                originDropItem.setDateTime(dateTime);
                dropItemMapper.updateById(originDropItem);
                return true;
            }
        }
        // 如果无任何相等数据，则插入一个新的数据
        dropItem.setDateTime(dateTime);
        dropItemMapper.insert(dropItem);
        return true;
    }


    @Override
    public List<String> selectAllUniqueZoneName() {
        List<DropItem> dropItems = new LambdaQueryChainWrapper<DropItem>(dropItemMapper).groupBy(DropItem::getZoneid).select(DropItem::getZoneid).list();
        List<String> list = null;
        if (dropItems != null) {
            list = new ArrayList<>();
            for (DropItem di : dropItems) {
                list.add(di.getZoneid());
            }
        }
        return list;
    }
}
