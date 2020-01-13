package com.nianlun.developmentrecord;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.nianlun.advancedtextview.AdvancedTextViewActivity;

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
        Button mBtnAdvancedTextView = (Button) findViewById(R.id.btn_advanced_text_view);
        mBtnAdvancedTextView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_advanced_text_view:
                startActivity(new Intent(this, AdvancedTextViewActivity.class));
                break;
            default:
                break;
        }
    }
}
