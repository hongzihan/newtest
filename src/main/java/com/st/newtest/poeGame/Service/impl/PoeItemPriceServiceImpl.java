package com.st.newtest.poeGame.Service.impl;

import com.st.newtest.poeGame.Entity.PoeItemPrice;
import com.st.newtest.poeGame.Mapper.PoeItemPriceMapper;
import com.st.newtest.poeGame.Service.PoeItemPriceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ken
 * @since 2020-11-02
 */
@Service
public class PoeItemPriceServiceImpl extends ServiceImpl<PoeItemPriceMapper, PoeItemPrice> implements PoeItemPriceService {
    @Autowired
    private PoeItemPriceMapper poeItemPriceMapper;

    @Override
    public Boolean addNewItem(PoeItemPrice poeItemPrice) {
        Map<String, Object> map = new HashMap<>();
        map.put("item_name", poeItemPrice.getItemName());
        map.put("item_type", poeItemPrice.getItemType());
        map.put("item_desc", poeItemPrice.getItemDesc());
        List<PoeItemPrice> poeItemPriceList = poeItemPriceMapper.selectByMap(map);
        if (poeItemPriceList.size() <= 0) {
            poeItemPriceMapper.insert(poeItemPrice);
        } else {
            poeItemPriceList.get(0).setItemRecordTime(poeItemPrice.getItemRecordTime());
            poeItemPriceList.get(0).setItemCurPrice(poeItemPrice.getItemCurPrice());
            poeItemPriceList.get(0).setItemIsPopular(poeItemPrice.getItemIsPopular());
            poeItemPriceMapper.updateById(poeItemPriceList.get(0));
        }
        return true;
    }

    @Override
    public Boolean updateItem(PoeItemPrice poeItemPrice) {
        Map<String, Object> map = new HashMap<>();
        map.put("item_name", poeItemPrice.getItemName());
        map.put("item_record_time", poeItemPrice.getItemRecordTime());
        List<PoeItemPrice> poeItemPrices = poeItemPriceMapper.selectByMap(map);
        if (poeItemPrices.size() > 0) {
            PoeItemPrice pip = poeItemPrices.get(0);
            pip.setItemRecordTime(poeItemPrice.getItemRecordTime());
            pip.setItemCurPrice(poeItemPrice.getItemCurPrice());
            pip.setItemDesc(poeItemPrice.getItemDesc());
            pip.setItemName(poeItemPrice.getItemName());
            pip.setItemType(poeItemPrice.getItemType());
            poeItemPriceMapper.updateById(pip);
            return true;
        }
        return false;
    }
}
