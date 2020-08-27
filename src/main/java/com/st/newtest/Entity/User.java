package com.st.newtest.Entity;

import java.io.Serializable;
import java.util.List;

public class User implements Serializable {
    private Integer id;

    private String username;

    private String password;

    private String email;

    private Integer corepwd;

    private String nickname;

    private List<Role> roleList;

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public Integer getCorepwd() {
        return corepwd;
    }

    public void setCorepwd(Integer corepwd) {
        this.corepwd = corepwd;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public List<Role> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<Role> roleList) {
        this.roleList = roleList;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", corepwd=" + corepwd +
                ", nickname='" + nickname + '\'' +
                ", roleList=" + roleList +
                '}';
    }
}