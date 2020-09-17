package com.st.newtest.stGame.Entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author ken
 * @since 2020-09-12
 */
@Data
public class ChatRecord implements Serializable {

    private static final long serialVersionUID=1L;

  @TableId(value = "id", type = IdType.AUTO)
  private Long id;

    private String zoneName;

    private Date dateTime;

    private String content;

    private String username;

    private String channelName;
}
