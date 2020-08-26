package com.st.newtest.Entity;

import java.io.Serializable;

public class Item implements Serializable {
    private Long id;
    private String keyname;
    private String username;
    private Integer count;

    public String getKeyname() {
        return keyname;
    }

    public void setKeyname(String keyname) {
        this.keyname = keyname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
