package com.st.newtest.Entity;

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
 * @since 2020-09-18
 */
@Data
public class Config implements Serializable {

    private static final long serialVersionUID=1L;

      @TableId(value = "id", type = IdType.AUTO)
      private Long id;

    private String configName;

    private String configValue;

    @Override
    public String toString() {
        return "Config{" +
              "id=" + id +
                  ", configName=" + configName +
                  ", configValue=" + configValue +
              "}";
    }
}
