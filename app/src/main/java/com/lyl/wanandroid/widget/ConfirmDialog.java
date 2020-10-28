package com.lyl.wanandroid.widget;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.lyl.wanandroid.R;
import com.lyl.wanandroid.listener.DialogListener;

/**
 * Created by lym on 2020/10/28
 * Describe :DialogFragment的使用
 * https://blog.csdn.net/lyl0530/article/details/109210102
 */
public class ConfirmDialog extends DialogFragment {

    private String title;
    private static final String TITLE_KEY = "title";
    public static ConfirmDialog getInstance(String title){
        ConfirmDialog dialog = new ConfirmDialog();
        Bundle bundle = new Bundle();
        bundle.putString(TITLE_KEY, title);
        dialog.setArguments(bundle);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_hint_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Dialog dialog = getDialog();
        //去掉默认对话框的标题
        dialog.requestWindowFeature(STYLE_NO_TITLE);
        Window window = dialog.getWindow();
        if (null != window){
            //去除系统自带的margin
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            //设置dialog在界面中的属性
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        }
        dialog.setCanceledOnTouchOutside(false);
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialogInterface, int keyCode, KeyEvent keyEvent) {
                return keyCode == KeyEvent.KEYCODE_BACK;
            }
        });

        Bundle bundle = getArguments();
        if (null != bundle){
            title = bundle.getString(TITLE_KEY);
        }

        TextView tv = view.findViewById(R.id.content);
        tv.setText(title);
        Button btnCancel = view.findViewById(R.id.cancel);
        Button btnSure = view.findViewById(R.id.sure);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                if (null != mListener){
                    mListener.leftClick();
                }
            }
        });

        btnSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                if (null != mListener){
                    mListener.rightClick();
                }
            }
        });
    }

    private DialogListener mListener;
    public void setOnDialogClickListener(DialogListener l){
        mListener = l;
    }
}
