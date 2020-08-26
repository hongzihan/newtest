package com.st.newtest.Service.impl;

import com.st.newtest.Entity.CrudStItem;
import com.st.newtest.Mapper.CrudStItemMapper;
import com.st.newtest.Service.CrudStItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("crudStItemService")
public class CrudStItemServiceImpl implements CrudStItemService {
    @Autowired(required = false)
    private CrudStItemMapper crudStItemMapper;
    @Override
    public int deleteByPrimaryKey(Integer id) {
        return crudStItemMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(CrudStItem record) {
        return crudStItemMapper.insert(record);
    }

    @Override
    public CrudStItem selectByPrimaryKey(Integer id) {
        return crudStItemMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<CrudStItem> selectAll() {
        return crudStItemMapper.selectAll();
    }

    @Override
    public int updateByPrimaryKey(CrudStItem record) {
        return crudStItemMapper.updateByPrimaryKey(record);
    }

    @Override
    public List<CrudStItem> selectAndDelAllByZoneid(String zoneid) {
        List<CrudStItem> list = crudStItemMapper.selectByZoneid(zoneid);
        for (CrudStItem crudStItem : list) {
            crudStItemMapper.deleteByPrimaryKey(crudStItem.getId());
        }
        return list;
    }

    @Override
    public List<CrudStItem> selectByZoneid(String zoneid) {
        return crudStItemMapper.selectByZoneid(zoneid);
    }
}
