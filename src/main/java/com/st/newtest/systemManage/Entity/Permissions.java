package com.st.newtest.systemManage.Entity;

public class Permissions {
    private Long id;
    private String modelname;
    private String permission;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getModelname() {
        return modelname;
    }

    public void setModelname(String modelname) {
        this.modelname = modelname;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    @Override
    public String toString() {
        return "Permissions{" +
                "id=" + id +
                ", modelname='" + modelname + '\'' +
                ", permission='" + permission + '\'' +
                '}';
    }
}
