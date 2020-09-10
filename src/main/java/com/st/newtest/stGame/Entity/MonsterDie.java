package com.st.newtest.stGame.Entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class MonsterDie implements Serializable {
    private Long id;

    private String zonename;

    private String mobname;

    private String killer;

    private String dietime;

    private String mapname;

    private Integer relivetime;

    private transient String futureBornTime;

    private transient String mobStatus;

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
        sb.append("]");
        return sb.toString();
    }
}