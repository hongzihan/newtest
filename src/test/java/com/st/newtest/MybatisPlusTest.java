package com.st.newtest;

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
}
