package com.st.newtest.stGame.Service.impl;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.st.newtest.stGame.Entity.ChatRecord;
import com.st.newtest.stGame.Mapper.ChatRecordMapper;
import com.st.newtest.stGame.Service.ChatRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

    @Autowired(required = false)
    private ChatRecordMapper chatRecordMapper;

    private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

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

    @Override
    public List<String> selectAllUniqueUsernameByZoneName(String zoneName) {
        List<ChatRecord> list = new LambdaQueryChainWrapper<ChatRecord>(chatRecordMapper).groupBy(ChatRecord::getUsername).eq(ChatRecord::getZoneName, zoneName).select(ChatRecord::getUsername).list();
        List<String> listS = null;
        if (list != null) {
            listS = new ArrayList<>();
            for (ChatRecord chatRecord : list) {
                listS.add(chatRecord.getUsername());
            }
        }
        return listS;
    }

    @Override
    public List<ChatRecord> selectAllNewMessageByZoneName(String zoneName, int seconds, int pageLimit) {
        Date now = new Date();
        // 这里需要减去时间转换
        long before = now.getTime() - 1000 * seconds - 13 * 60 * 60 * 1000;
        long after = now.getTime() - 13 * 60 * 60 * 1000;
        List<ChatRecord> list = new LambdaQueryChainWrapper<ChatRecord>(chatRecordMapper).eq(ChatRecord::getZoneName, zoneName).between(ChatRecord::getDateTime,df.format(before),df.format(after)).orderByAsc(ChatRecord::getId).last("limit " + pageLimit + "").list();
        return list;
    }


}
