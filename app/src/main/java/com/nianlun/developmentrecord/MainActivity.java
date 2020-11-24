package com.nianlun.developmentrecord;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.nianlun.advancedtextview.AdvancedTextViewActivity;
import com.nianlun.advancedtextview.BottomAlignmentActivity;
import com.nianlun.floatingwindow.FloatingWindowActivity;
import com.nianlun.foregroundservice.ForegroundServiceActivity;
import com.nianlun.greendaodb.GreenDaoDBActivity;
import com.nianlun.mqtt.MqttActivity;
import com.nianlun.objectboxdb.ObjectBoxDBActivity;

/**
 * @author 几圈年轮
 * @Email teamfamily17@163.com
 * @description Android开发内容整理记录
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        findViewById(R.id.btn_advanced_text_view).setOnClickListener(this);
        findViewById(R.id.btn_bottom_alignment).setOnClickListener(this);
        findViewById(R.id.btn_advanced_text_view).setOnClickListener(this);
        findViewById(R.id.btn_foreground_service).setOnClickListener(this);
        findViewById(R.id.btn_mqtt).setOnClickListener(this);
        findViewById(R.id.btn_floating_window).setOnClickListener(this);
        findViewById(R.id.btn_green_dao).setOnClickListener(this);
        findViewById(R.id.btn_object_box).setOnClickListener(this);
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
            case R.id.btn_mqtt:
                startActivity(new Intent(this, MqttActivity.class));
                break;
            case R.id.btn_floating_window:
                startActivity(new Intent(this, FloatingWindowActivity.class));
                break;
            case R.id.btn_green_dao:
                startActivity(new Intent(this, GreenDaoDBActivity.class));
                break;
            case R.id.btn_object_box:
                startActivity(new Intent(this, ObjectBoxDBActivity.class));
                break;
                default:
                break;
        }
    }
}
