package com.st.newtest.stGame.Entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class WebAction implements Serializable {
    private Long id;

    private String zoneid;

    private Integer actiontype;

    private String actiondata;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", zoneid=").append(zoneid);
        sb.append(", actiontype=").append(actiontype);
        sb.append(", actiondata=").append(actiondata);
        sb.append("]");
        return sb.toString();
    }
}