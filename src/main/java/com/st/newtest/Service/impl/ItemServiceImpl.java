package com.st.newtest.Service.impl;

import com.st.newtest.Entity.Item;
import com.st.newtest.Mapper.ItemMapper;
import com.st.newtest.Service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("itemServiceImpl")
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ItemMapper itemMapper;

    @Override
    public List<Item> findAll() {
        return itemMapper.findAll();
    }
}
