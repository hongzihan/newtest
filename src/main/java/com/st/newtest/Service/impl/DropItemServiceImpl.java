package com.st.newtest.Service.impl;

import com.st.newtest.Entity.DropItem;
import com.st.newtest.Mapper.DropItemMapper;
import com.st.newtest.Service.DropItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("dropItemServiceImpl")
public class DropItemServiceImpl implements DropItemService {
    @Autowired(required = false)
    private DropItemMapper dropItemMapper;

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
        List<DropItem> originDropItemList = dropItemMapper.selectByItemKey(keyname);
        for (DropItem originDropItem : originDropItemList) { // 如果表中存在完全相等的数据，则更新进数据表
            if (originDropItem.getKeyname().equals(keyname) && originDropItem.getZoneid().equals(zoneid)) {
                originDropItem.setCount(originDropItem.getCount() + dropItem.getCount());
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
        newItem.setId(0);
        dropItemMapper.insert(newItem);
        return true;
    }

    @Override
    public List<DropItem> selectAllByZoneid(String zoneid) {
        return dropItemMapper.selectAllByZoneid(zoneid);
    }
}
