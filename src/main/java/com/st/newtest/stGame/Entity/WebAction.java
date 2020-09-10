package com.st.newtest.stGame.Entity;

import java.io.Serializable;

public class WebAction implements Serializable {
    private Long id;

    private String zoneid;

    private Integer actiontype;

    private String actiondata;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getZoneid() {
        return zoneid;
    }

    public void setZoneid(String zoneid) {
        this.zoneid = zoneid == null ? null : zoneid.trim();
    }

    public Integer getActiontype() {
        return actiontype;
    }

    public void setActiontype(Integer actiontype) {
        this.actiontype = actiontype;
    }

    public String getActiondata() {
        return actiondata;
    }

    public void setActiondata(String actiondata) {
        this.actiondata = actiondata == null ? null : actiondata.trim();
    }

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
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}