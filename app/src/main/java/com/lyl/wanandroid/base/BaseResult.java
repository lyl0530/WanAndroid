package com.lyl.wanandroid.base;

/**
 * Created by lym on 2020/3/29
 * Describe :
 */
public class BaseResult {
    private int errorCode;
    private String errorMsg;

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
