package com.st.newtest.Service.impl;

import com.st.newtest.Entity.TbItem;
import com.st.newtest.Mapper.TbItemMapper;
import com.st.newtest.Service.TbItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("tbItemServiceImpl")
public class TbItemServiceImpl implements TbItemService {

    @Autowired
    private TbItemMapper tbItemMapper;

    @Override
    public Integer deleteByPrimaryKey(Integer id) {
        return tbItemMapper.deleteByPrimaryKey(id);
    }

    @Override
    public Integer insert(TbItem record) {
        return tbItemMapper.insert(record);
    }

    @Override
    public TbItem selectByPrimaryKey(Integer id) {
        return tbItemMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<TbItem> selectAll() {
        return tbItemMapper.selectAll();
    }

    @Override
    public Integer updateByPrimaryKey(TbItem record) {
        return tbItemMapper.updateByPrimaryKey(record);
    }
}
