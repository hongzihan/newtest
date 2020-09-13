package com.st.newtest.stGame.Service;

import com.st.newtest.stGame.Entity.ChatRecord;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ken
 * @since 2020-09-12
 */
public interface ChatRecordService extends IService<ChatRecord> {
    List<String> selectAllUniqueZoneName();

    List<String> selectAllUniqueUsernameByZoneName(String zoneName);

    List<ChatRecord> selectAllNewMessageByZoneName(String zoneName);
}
