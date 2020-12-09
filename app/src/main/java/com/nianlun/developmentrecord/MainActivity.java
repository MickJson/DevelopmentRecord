package com.nianlun.developmentrecord;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.android.arouter.launcher.ARouter;

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
                ARouter.getInstance().build("/advancedtextview/AdvancedTextViewActivity").navigation();
                break;
            case R.id.btn_foreground_service:
                ARouter.getInstance().build("/foregroundservice/ForegroundServiceActivity").navigation();
                break;
            case R.id.btn_bottom_alignment:
                ARouter.getInstance().build("/advancedtextview/BottomAlignmentActivity").navigation();
                break;
            case R.id.btn_mqtt:
                ARouter.getInstance().build("/mqtt/MqttActivity").navigation();
                break;
            case R.id.btn_floating_window:
                ARouter.getInstance().build("/floatingwindow/FloatingWindowActivity").navigation();
                break;
            case R.id.btn_green_dao:
                ARouter.getInstance().build("/greendaodb/GreenDaoDBActivity").navigation();
                break;
            case R.id.btn_object_box:
                ARouter.getInstance().build("/objectboxdb/ObjectBoxDBActivity").navigation();
                break;
                default:
                break;
        }
    }
}
