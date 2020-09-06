package com.st.newtest.systemManage.Entity;

public class UserRole {
    private Integer uid;
    private Integer rid;

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public Integer getRid() {
        return rid;
    }

    public void setRid(Integer rid) {
        this.rid = rid;
    }

    @Override
    public String toString() {
        return "UserRole{" +
                "uid=" + uid +
                ", rid=" + rid +
                '}';
    }
}
