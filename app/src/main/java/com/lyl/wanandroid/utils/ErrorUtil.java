package com.lyl.wanandroid.utils;

import android.content.Context;
import android.content.Intent;

import com.lyl.wanandroid.R;

/**
 * Created by lym on 2020/10/28
 * Describe : 请求结果 统一处理
 */
public class ErrorUtil {
    /**
     * result为空 -1002
     * 修改未登录的错误码为 -1001
     * 其他错误码为 -1
     * 成功为 0
     * 建议对errorCode 判断当不为0的时候，均为错误。
     */
    public static final int CODE_SUCCESS = 0;       //成功
    public static final int CODE_NOT_LOGIN = -1001; //未登录
    public static final int CODE_RES_NULL = -1002;  //result为空
    public static final int CODE_OTHERS = -1;       //其他错误

    public static String getErrorInfo(Context context, int errCode, String errMsg){
        String result = "";
        switch (errCode){
            case CODE_SUCCESS:
                result = context.getResources().getString(R.string.request_success);
                break;
            case CODE_RES_NULL:
                result = context.getResources().getString(R.string.request_result_null);
                break;
            case CODE_NOT_LOGIN:
                //跳转登录
                result = context.getResources().getString(R.string.login_first);
                Intent intent = new Intent();
                intent.setAction(ConstUtil.GO_TO_LOGIN);
                intent.addCategory(Intent.CATEGORY_DEFAULT);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                break;
            case CODE_OTHERS:
            default:
                result = context.getResources().getString(R.string.request_other_error, errMsg);
                break;
        }
        return result;
    }
}
