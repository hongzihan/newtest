package com.st.newtest.systemManage.Entity;

public class RolePermission {
    private Long rid;

    private Long pid;

    public Long getRid() {
        return rid;
    }

    public void setRid(Long rid) {
        this.rid = rid;
    }

    public Long getPid() {
        return pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }

    @Override
    public String toString() {
        return "RolePermission{" +
                "rid=" + rid +
                ", pid=" + pid +
                '}';
    }
}
