package com.st.newtest.stGame.Service.impl;

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
public class DropItemServiceImpl implements DropItemService {
    @Autowired(required = false)
    private DropItemMapper dropItemMapper;

    private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public int deleteByPrimaryKey(Integer id) {
        return dropItemMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(DropItem record) {
        return dropItemMapper.insert(record);
    }

    @Override
    public DropItem selectByPrimaryKey(Integer id) {
        return dropItemMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<DropItem> selectAll() {
        return dropItemMapper.selectAll();
    }

    @Override
    public int updateByPrimaryKey(DropItem record) {
        return dropItemMapper.updateByPrimaryKey(record);
    }

    @Override
    public List<DropItem> selectByItemKey(String keyname) {
        return dropItemMapper.selectByItemKey(keyname);
    }

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
        List<DropItem> originDropItemList = dropItemMapper.selectByItemKey(keyname);
        for (DropItem originDropItem : originDropItemList) { // 如果表中存在完全相等的数据，则更新进数据表
            if (originDropItem.getKeyname().equals(keyname) && originDropItem.getZoneid().equals(zoneid)) {
                originDropItem.setCount(originDropItem.getCount() + dropItem.getCount());

                originDropItem.setDateTime(dateTime);
                dropItemMapper.updateByPrimaryKey(originDropItem);
                return true;
            }
        }
        // 如果无任何相等数据，则插入一个新的数据
        DropItem newItem = new DropItem();
        newItem.setCount(count);
        newItem.setKeyname(keyname);
        newItem.setItemname(itemname);
        newItem.setZoneid(zoneid);
        newItem.setDateTime(dateTime);
        newItem.setId(0);
        dropItemMapper.insert(newItem);
        return true;
    }

    @Override
    public List<DropItem> selectAllByZoneid(String zoneid) {
        return dropItemMapper.selectAllByZoneid(zoneid);
    }

    @Override
    public List<String> selectAllUniqueZoneName() {
        List<DropItem> dropItems = dropItemMapper.selectAllUniqueZoneName();
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
