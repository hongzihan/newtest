package com.st.newtest.stGame.Entity;

import java.io.Serializable;

public class Charge implements Serializable {
    private Integer id;

    private String username;

    private String accountName;

    private String zoneName;

    private Integer chargeNum;

    private String dateTime;

    private Integer chargeCount;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName == null ? null : accountName.trim();
    }

    public String getZoneName() {
        return zoneName;
    }

    public void setZoneName(String zoneName) {
        this.zoneName = zoneName == null ? null : zoneName.trim();
    }

    public Integer getChargeNum() {
        return chargeNum;
    }

    public void setChargeNum(Integer chargeNum) {
        this.chargeNum = chargeNum;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime == null ? null : dateTime.trim();
    }

    public Integer getChargeCount() {
        return chargeCount;
    }

    public void setChargeCount(Integer chargeCount) {
        this.chargeCount = chargeCount;
    }

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
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}