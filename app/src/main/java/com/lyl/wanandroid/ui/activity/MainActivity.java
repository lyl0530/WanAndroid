package com.lyl.wanandroid.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.lyl.wanandroid.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void Next(View view) {
        //throw new RuntimeException("test");//add bugly
        startActivity(new Intent(this, LoginActivity.class));
    }
}
