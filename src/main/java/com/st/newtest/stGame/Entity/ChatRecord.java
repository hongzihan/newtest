package com.st.newtest.stGame.Entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

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

    private String dateTime;

    private String content;

    private String username;

    private String channelName;

    private Integer isNew;
}
