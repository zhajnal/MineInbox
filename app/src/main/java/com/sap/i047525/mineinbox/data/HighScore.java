package com.sap.i047525.mineinbox.data;

/**
 * Created by i035921 on 2015.03.09..
 */
public class HighScore {

    private String name;
    private long time;
    private String table;
    private String createdAt;
    private String updatedAt;
    private String objectId;

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getName() {

        return name;
    }

    public long getTime() {
        return time;
    }

    public String getTimeString() {
        return Long.toString(Math.round(time/60))+"s";
    }
}
