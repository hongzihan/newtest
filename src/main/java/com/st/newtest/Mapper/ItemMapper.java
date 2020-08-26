package com.st.newtest.Mapper;

import com.st.newtest.Entity.Item;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ItemMapper {
    List<Item> findAll();
}
