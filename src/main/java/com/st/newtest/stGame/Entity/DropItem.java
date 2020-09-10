package com.st.newtest.stGame.Entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;

@Data
public class DropItem implements Serializable {
    private Integer id;

    private String keyname;

    private String itemname;

    private Integer count;

    private String zoneid;

    @TableField("dateTime")
    private String dateTime;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", keyname=").append(keyname);
        sb.append(", itemname=").append(itemname);
        sb.append(", count=").append(count);
        sb.append(", zoneid=").append(zoneid);
        sb.append(", dateTime=").append(dateTime);
        sb.append("]");
        return sb.toString();
    }
}