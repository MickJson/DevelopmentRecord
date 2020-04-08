package com.nianlun.developmentrecord;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.nianlun.advancedtextview.AdvancedTextViewActivity;
import com.nianlun.advancedtextview.BottomAlignmentActivity;
import com.nianlun.foregroundservice.ForegroundServiceActivity;
import com.nianlun.mqtt.MqttActivity;
import com.nianlun.mqtt.MqttAndroidActivity;

/**
 * @author 几圈年轮
 * @Email teamfamily17@163.com
 * @description Android开发内容整理记录
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mBtnBottomAlignment;
    private Button mBtnAdvancedTextView;
    private Button mBtnForegroundService;
    private Button mBtnMqtt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        mBtnAdvancedTextView = (Button) findViewById(R.id.btn_advanced_text_view);
        mBtnAdvancedTextView.setOnClickListener(this);
        mBtnBottomAlignment = (Button) findViewById(R.id.btn_bottom_alignment);
        mBtnBottomAlignment.setOnClickListener(this);
        mBtnAdvancedTextView = (Button) findViewById(R.id.btn_advanced_text_view);
        mBtnAdvancedTextView.setOnClickListener(this);
        mBtnForegroundService = (Button) findViewById(R.id.btn_foreground_service);
        mBtnForegroundService.setOnClickListener(this);
        mBtnMqtt = (Button) findViewById(R.id.btn_mqtt);
        mBtnMqtt.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_advanced_text_view:
                startActivity(new Intent(this, AdvancedTextViewActivity.class));
                break;
            case R.id.btn_foreground_service:
                startActivity(new Intent(this, ForegroundServiceActivity.class));
                break;
            case R.id.btn_bottom_alignment:
                startActivity(new Intent(this, BottomAlignmentActivity.class));
                break;
            default:
                break;
            case R.id.btn_mqtt:
                startActivity(new Intent(this, MqttActivity.class));
                break;
        }
    }
}
