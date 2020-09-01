package com.st.newtest.Entity;

import java.io.Serializable;

public class MonsterDie implements Serializable {
    private Integer id;

    private String zonename;

    private String mobname;

    private String killer;

    private String dietime;

    private String mapname;

    private Integer relivetime;

    private String futureBornTime;

    private String mobStatus;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getZonename() {
        return zonename;
    }

    public void setZonename(String zonename) {
        this.zonename = zonename == null ? null : zonename.trim();
    }

    public String getMobname() {
        return mobname;
    }

    public void setMobname(String mobname) {
        this.mobname = mobname == null ? null : mobname.trim();
    }

    public String getKiller() {
        return killer;
    }

    public void setKiller(String killer) {
        this.killer = killer == null ? null : killer.trim();
    }

    public String getDietime() {
        return dietime;
    }

    public void setDietime(String dietime) {
        this.dietime = dietime == null ? null : dietime.trim();
    }

    public String getMapname() {
        return mapname;
    }

    public void setMapname(String mapname) {
        this.mapname = mapname;
    }

    public Integer getRelivetime() {
        return relivetime;
    }

    public void setRelivetime(Integer relivetime) {
        this.relivetime = relivetime;
    }

    public String getFutureBornTime() {
        return futureBornTime;
    }

    public void setFutureBornTime(String futureBornTime) {
        this.futureBornTime = futureBornTime;
    }

    public String getMobStatus() {
        return mobStatus;
    }

    public void setMobStatus(String mobStatus) {
        this.mobStatus = mobStatus;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", zonename=").append(zonename);
        sb.append(", mobname=").append(mobname);
        sb.append(", killer=").append(killer);
        sb.append(", dietime=").append(dietime);
        sb.append(", mapname=").append(mapname);
        sb.append(", relivetime=").append(relivetime);
        sb.append(", futureBornTime=").append(futureBornTime);
        sb.append(", mobStatus=").append(mobStatus);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}