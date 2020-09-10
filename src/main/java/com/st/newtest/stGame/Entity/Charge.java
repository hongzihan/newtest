package com.st.newtest.stGame.Entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;

@Data
public class Charge implements Serializable {
    private Integer id;

    private String username;

    @TableField("accountName")
    private String accountName;

    @TableField("zoneName")
    private String zoneName;

    @TableField("chargeNum")
    private Integer chargeNum;

    @TableField("dateTime")
    private String dateTime;

    @TableField("chargeCount")
    private Integer chargeCount;

    public Charge() {

    }

    public Charge(String username, String accountName, String zoneName, Integer chargeNum) {
        this.username = username;
        this.accountName = accountName;
        this.zoneName = zoneName;
        this.chargeNum = chargeNum;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", username=").append(username);
        sb.append(", accountName=").append(accountName);
        sb.append(", zoneName=").append(zoneName);
        sb.append(", chargeNum=").append(chargeNum);
        sb.append(", dateTime=").append(dateTime);
        sb.append(", chargeCount=").append(chargeCount);
        sb.append("]");
        return sb.toString();
    }
}