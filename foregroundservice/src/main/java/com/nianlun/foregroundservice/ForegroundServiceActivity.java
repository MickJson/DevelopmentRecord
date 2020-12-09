package com.nianlun.foregroundservice;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.android.arouter.facade.annotation.Route;

@Route(path = "/foregroundservice/ForegroundServiceActivity")
public class ForegroundServiceActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foreground_service);
        initView();
    }

    private void initView() {
        Button mBtnStart = (Button) findViewById(R.id.btn_start);
        Button mBtnStop = (Button) findViewById(R.id.btn_stop);

        mBtnStart.setOnClickListener(this);
        mBtnStop.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        Intent mForegroundService;
        if (id == R.id.btn_start) {
            // 启动服务
            if (!ForegroundService.serviceIsLive) {
                // Android 8.0使用startForegroundService在前台启动新服务
                mForegroundService = new Intent(this, ForegroundService.class);
                mForegroundService.putExtra("Foreground", "This is a foreground service.");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    startForegroundService(mForegroundService);
                } else {
                    startService(mForegroundService);
                }
            } else {
                Toast.makeText(this, "前台服务正在运行中...", Toast.LENGTH_SHORT).show();
            }
        } else if (id == R.id.btn_stop) {
            // 停止服务
            mForegroundService = new Intent(this, ForegroundService.class);
            stopService(mForegroundService);
        }
    }
}
