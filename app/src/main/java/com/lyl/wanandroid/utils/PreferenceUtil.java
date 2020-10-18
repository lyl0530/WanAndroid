package com.lyl.wanandroid.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.lyl.wanandroid.app.BaseApplication;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by lym on 2020/5/12
 * Describe :
 */
public class PreferenceUtil {


    private SharedPreferences mPreference;
    private volatile static PreferenceUtil instance;

    private PreferenceUtil() {
        mPreference =
                BaseApplication.getContext().getSharedPreferences(ConstUtil.PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

    public static PreferenceUtil instance() {
        if (null == instance) {
            synchronized (PreferenceUtil.class) {
                if (null == instance) {
                    instance = new PreferenceUtil();
                }
            }
        }
        return instance;
    }

    private static final String ACCOUNT = "account";

    public void setAccount(String account) {
        mPreference.edit().putString(ACCOUNT, account).apply();
    }

    public String getAccount() {
        return mPreference.getString(ACCOUNT, "");
    }

    private static final String PWD = "pwd";

    public void setPwd(String pwd) {
        mPreference.edit().putString(PWD, pwd).apply();
    }

    public String getPwd() {
        return mPreference.getString(PWD, "");
    }

    private static final String REMEMBER_PWD_CHECKED = "remember_pwd_checked";

    public void setCheck(boolean isCheck) {
        mPreference.edit().putBoolean(REMEMBER_PWD_CHECKED, isCheck).apply();
    }

    public boolean getCheck() {
        return mPreference.getBoolean(REMEMBER_PWD_CHECKED, false);
    }

    private static final String USER_ID = "user_id";

    public void setUserId(int userId) {
        mPreference.edit().putInt(USER_ID, userId).apply();
    }

    public int getUserId() {
        return mPreference.getInt(USER_ID, 0);
    }

    private static final String DOMAIN_NAME = "wanandroid.com";
    public void setDomainName(String domainName) {
        mPreference.edit().putString(DOMAIN_NAME, domainName).apply();
    }
    public String getDomainName() {
        return mPreference.getString(DOMAIN_NAME, "");
    }

    private static final String COOKIES = "COOKIES";
    public void setCookieSet(HashSet<String> cookieSet) {
        mPreference.edit().putStringSet(COOKIES, cookieSet).apply();
    }
    public Set<String> getCookieSet() {
        return mPreference.getStringSet(COOKIES, new HashSet<>());
    }

}
