package com.st.newtest.stGame.Entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author ken
 * @since 2020-09-23
 */
@Data
public class StRole implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(value = "id", type = IdType.AUTO)
      private Long id;

    private String zoneId;

    private String zoneName;

    private String account;

    private String roleName;

    private String roleId;

    private String roleLevel;

    private String job;

    private String gender;

    private String chargeYb;

    private String curYb;

    private String curGold;

    private Date lastRecordDate;

    @Override
    public String toString() {
        return "StRole{" +
              "id=" + id +
                  ", zoneId=" + zoneId +
                  ", zoneName=" + zoneName +
                  ", account=" + account +
                  ", roleName=" + roleName +
                  ", roleId=" + roleId +
                  ", roleLevel=" + roleLevel +
                  ", job=" + job +
                  ", gender=" + gender +
                  ", chargeYb=" + chargeYb +
                  ", curYb=" + curYb +
                  ", curGold=" + curGold +
                  ", lastRecordDate=" + lastRecordDate +
              "}";
    }
}
