package com.lyl.wanandroid.util.rxPreference;


import android.content.Context;
import android.content.SharedPreferences;

import com.lyl.wanandroid.constant.Const;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

/**
 * Created by lym on 2020/5/12
 * Describe :
 */
public class RxPreference {

    private SharedPreferences mPreference;
    private Context mContext;

    public RxPreference(Context context){
        mContext = context;
        mPreference =
                mContext.getSharedPreferences(Const.PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

//    /**
//     * @return 返回RxJava Observable，用于订阅SharedPreferences数据发生变化。
//     * 如果数据发生变化，订阅者将会收到变化的值。
//     * 如果SharedPreferences中没有<code>key<code/>，则返回<code>defaultValue<code/>
//     */
//    public <T> Observable<T> observe(@NonNull final String key, @NonNull final TypeToken<T> typeTokenOfT,
//                                     @NonNull final T defaultValue) {
//
//    }
    /**
     * @return 返回RxJava Observable，用于订阅SharedPreferences数据发生变化。
     */
    public Observable<String> observePreference(){
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                onChangeListener listener = new onChangeListener(emitter);
                mPreference.registerOnSharedPreferenceChangeListener(listener);

                //怎样实现解注册监听？

            }
        });
    }

    private static class onChangeListener implements SharedPreferences.OnSharedPreferenceChangeListener {
        private ObservableEmitter<String> mEmitter;
        public onChangeListener(ObservableEmitter<String> emitter){
            mEmitter = emitter;
        }

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            mEmitter.onNext(key);
        }
    }
}
