package com.lyl.wanandroid.widget;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lyl.wanandroid.R;

/**
 * Created by lym on 2020/5/15
 * Describe :
 */
public class DialogHelper {
    private Context mContext;
    private LayoutInflater mInflater;

    public DialogHelper(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(mContext);

    }

    public Dialog bindHintDialog(String titleStr, String contentStr, View.OnClickListener sureListener,
                                 View.OnClickListener cancelListener) {
        LinearLayout.LayoutParams lp
                = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        LinearLayout layout = new LinearLayout(mContext);
        layout.setLayoutParams(lp);
        View view = mInflater.inflate(R.layout.dialog_hint_layout, layout, false);
        TextView tvTitle = view.findViewById(R.id.title);
        TextView tvContent = view.findViewById(R.id.content);
        Button btnCancel = view.findViewById(R.id.cancel);
        Button btnSure = view.findViewById(R.id.sure);

        tvTitle.setVisibility(TextUtils.isEmpty(titleStr) ? View.GONE : View.VISIBLE);
        tvTitle.setText(titleStr);
        tvContent.setText(contentStr);
        btnCancel.setOnClickListener(cancelListener);
        btnSure.setOnClickListener(sureListener);

        Dialog dialog = new Dialog(mContext, R.style.dialog);
        dialog.setContentView(view);
        return dialog;
    }

}
