package com.st.newtest.Service.impl;

import com.st.newtest.Entity.CountItem;
import com.st.newtest.Mapper.CountItemMapper;
import com.st.newtest.Service.CountItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("countItemServiceImpl")
public class CountItemServiceImpl implements CountItemService {

    @Autowired
    private CountItemMapper countItemMapper;

    @Override
    public int deleteByPrimaryKey(Integer id) {
        return countItemMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(CountItem record) {
        return countItemMapper.insert(record);
    }

    @Override
    public CountItem selectByPrimaryKey(Integer id) {
        return countItemMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<CountItem> selectAll() {
        return countItemMapper.selectAll();
    }

    @Override
    public int updateByPrimaryKey(CountItem record) {
        return countItemMapper.updateByPrimaryKey(record);
    }

    @Override
    public CountItem selectByItemKey(String keyname) {
        return countItemMapper.selectByItemKey(keyname);
    }

    @Override
    public Boolean insertNewData(CountItem countItem) {
        // 查出该数据是否已经存在在数据库中
        String keyname = countItem.getKeyname();
        Integer count = countItem.getCount();
        CountItem originCountItem = countItemMapper.selectByItemKey(keyname);
        if (originCountItem == null) {
            CountItem newItem = new CountItem();
            newItem.setCount(count);
            newItem.setKeyname(keyname);
            newItem.setId(0);
            countItemMapper.insert(newItem);
        } else {
            originCountItem.setCount(originCountItem.getCount() + countItem.getCount());
            countItemMapper.updateByPrimaryKey(originCountItem);
        }
        return true;
    }
}
