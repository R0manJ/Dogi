package com.rjstudio.dogi.bean;

import android.os.ParcelUuid;

/**
 * Created by r0man on 2018/5/6.
 */

public class Bluetooth {
    private String name;
    private String address;
    private int type;
    private ParcelUuid[] uuid;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public ParcelUuid[] getUuid() {
        return uuid;
    }

    public void setUuid(ParcelUuid[] uuid) {
        this.uuid = uuid;
    }
}
