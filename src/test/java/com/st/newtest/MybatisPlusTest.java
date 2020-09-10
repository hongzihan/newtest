package com.st.newtest;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.st.newtest.stGame.Entity.DropItem;
import com.st.newtest.stGame.Mapper.DropItemMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MybatisPlusTest {
    @Autowired
    private DropItemMapper dropItemMapper;

    @Test
    public void test1() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("zoneid", "赤壁之战1区");
        List<DropItem> dropItems = dropItemMapper.selectByMap(map);
        dropItems.forEach(System.out::println);
    }

    @Test
    public void test2() {
        QueryWrapper<DropItem> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("itemname", "同生共死");
        dropItemMapper.selectList(queryWrapper);
    }

    @Test
    public void test3() {
        new LambdaQueryChainWrapper<DropItem>(dropItemMapper).groupBy(DropItem::getZoneid).select(DropItem::getZoneid).list();
    }
}
