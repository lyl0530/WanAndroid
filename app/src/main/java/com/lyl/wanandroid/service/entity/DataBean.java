package com.lyl.wanandroid.service.entity;

import org.jetbrains.annotations.NotNull;

/**
 * Created by lym on 2020/10/21
 * Describe :
 */
public class DataBean {
    int type;
    Object res;

    public DataBean(int type, Object res) {
        this.type = type;
        this.res = res;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Object getContent() {
        return res;
    }

    public void setContent(Object res) {
        this.res = res;
    }

    @NotNull
    @Override
    public String toString() {
        return "HomeBean{" +
                "type=" + type +
                ", res=" + res +
                '}';
    }
}
