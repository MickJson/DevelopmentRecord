package com.nianlun.advancedtextview;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.nianlun.advancedtextview.bean.TextItem;

public class BottomAlignmentActivity extends AppCompatActivity {

    private TextView mTvNumberUnit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_alignment);
        initView();
        setBottomAlignment(new TextItem(37.0f, "℃"));
    }

    private void initView() {
        mTvNumberUnit = (TextView) findViewById(R.id.tv_number_unit);
    }

    private void setBottomAlignment(TextItem item) {
        SpannableStringBuilder spanString = new SpannableStringBuilder(item.getValue() + "  " + item.getUnit());
        //绝对尺寸
        AbsoluteSizeSpan absoluteSizeSpan = new AbsoluteSizeSpan(60);
        spanString.setSpan(absoluteSizeSpan, String.valueOf(item.getValue()).length(), spanString.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        // 字体颜色
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.GRAY);
        spanString.setSpan(colorSpan, String.valueOf(item.getValue()).length(), spanString.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        // 字体加粗
        StyleSpan styleSpan = new StyleSpan(Typeface.BOLD);
        spanString.setSpan(styleSpan, 0, String.valueOf(item.getValue()).length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        mTvNumberUnit.setText(spanString);
    }
}
