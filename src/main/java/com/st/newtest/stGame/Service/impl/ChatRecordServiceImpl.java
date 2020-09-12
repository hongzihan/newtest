package com.st.newtest.stGame.Service.impl;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.st.newtest.stGame.Entity.ChatRecord;
import com.st.newtest.stGame.Mapper.ChatRecordMapper;
import com.st.newtest.stGame.Service.ChatRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ken
 * @since 2020-09-12
 */
@Service
public class ChatRecordServiceImpl extends ServiceImpl<ChatRecordMapper, ChatRecord> implements ChatRecordService {

    @Autowired
    private ChatRecordMapper chatRecordMapper;

    @Override
    public List<String> selectAllUniqueZoneName() {
        List<ChatRecord> list = new LambdaQueryChainWrapper<ChatRecord>(chatRecordMapper).groupBy(ChatRecord::getZoneName).select(ChatRecord::getZoneName).list();
        List<String> listS = null;
        if (list != null) {
            listS = new ArrayList<>();
            for (ChatRecord chatRecord : list) {
                listS.add(chatRecord.getZoneName());
            }
        }
        return listS;
    }
}
