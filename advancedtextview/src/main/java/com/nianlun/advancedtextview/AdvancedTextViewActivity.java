package com.nianlun.advancedtextview;

import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

/**
 * @author 几圈年轮
 * @Email teamfamily17@163.com
 * @description 富文本SpannableStringBuilder使用
 */
public class AdvancedTextViewActivity extends AppCompatActivity {

    private TextView mTvAdvanced01;
    private TextView mTvAdvanced02;
    private TextView mTvAdvanced03;
    private TextView mTvAdvanced04;
    private TextView mTvAdvanced05;
    private TextView mTvAdvanced06;
    private TextView mTvAdvanced07;
    private TextView mTvAdvanced08;
    private TextView mTvAdvanced09;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advanced_text_view);
        initView();
        initTextStyle();
    }

    private void initTextStyle() {
        SpannableStringBuilder spanString01 = new SpannableStringBuilder("风急天高猿啸哀");
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.RED);
        spanString01.setSpan(foregroundColorSpan, 4, 7, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        mTvAdvanced01.setText(spanString01);

        SpannableStringBuilder spanString02 = new SpannableStringBuilder("渚清沙白鸟飞回");
        BackgroundColorSpan backgroundColorSpan = new BackgroundColorSpan(Color.YELLOW);
        spanString02.setSpan(backgroundColorSpan, 4, 7, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        mTvAdvanced02.setText(spanString02);

        SpannableStringBuilder spanString03 = new SpannableStringBuilder("无边落木萧萧下");
        AbsoluteSizeSpan absoluteSizeSpan = new AbsoluteSizeSpan(30);
        spanString03.setSpan(absoluteSizeSpan, 4, 7, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mTvAdvanced03.setText(spanString03);

        SpannableStringBuilder spanString04 = new SpannableStringBuilder("不尽长江滚滚来");
        StyleSpan styleSpan = new StyleSpan(Typeface.BOLD_ITALIC);
        spanString04.setSpan(styleSpan, 4, 7, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        mTvAdvanced04.setText(spanString04);

        SpannableStringBuilder spanString05 = new SpannableStringBuilder("万里悲秋常作客");
        StrikethroughSpan strikethroughSpan = new StrikethroughSpan();
        spanString05.setSpan(strikethroughSpan, 4, 7, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        mTvAdvanced05.setText(spanString05);

        SpannableStringBuilder spanString06 = new SpannableStringBuilder("百年多病独登台");
        UnderlineSpan underlineSpan = new UnderlineSpan();
        spanString06.setSpan(underlineSpan, 4, 7, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        mTvAdvanced06.setText(spanString06);

        SpannableStringBuilder spanString07 = new SpannableStringBuilder("艰难苦恨繁霜鬓");
        ImageSpan span = new ImageSpan(this, R.drawable.ic_text);
        spanString07.setSpan(span, 4, 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        mTvAdvanced07.setText(spanString07);

        SpannableStringBuilder spanString08 = new SpannableStringBuilder("潦倒新停浊酒杯");
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                Toast.makeText(AdvancedTextViewActivity.this, "杜甫", Toast.LENGTH_SHORT).show();
            }
        };
        spanString08.setSpan(clickableSpan, 4, 7, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        mTvAdvanced08.setText(spanString08);
        mTvAdvanced08.setMovementMethod(LinkMovementMethod.getInstance());

        setMultipleUse();
    }

    private void setMultipleUse() {
        SpannableStringBuilder spanString09 = new SpannableStringBuilder();
        spanString09.append("杜甫 ,字子美，自号少陵野老，后人称为'诗圣'。");
        //图片
        ImageSpan imageSpan = new ImageSpan(this, R.drawable.ic_author);
        spanString09.setSpan(imageSpan, 2, 4, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        //点击事件
        ClickableSpan cbSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                Toast.makeText(AdvancedTextViewActivity.this, "杜甫", Toast.LENGTH_SHORT).show();
            }
        };
        spanString09.setSpan(cbSpan, 2, 4, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        //文字颜色
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.WHITE);
        spanString09.setSpan(colorSpan, 10, 14, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        //文字背景颜色
        BackgroundColorSpan bgColorSpan = new BackgroundColorSpan(Color.BLUE);
        spanString09.setSpan(bgColorSpan, 10, 14, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        mTvAdvanced09.setText(spanString09);
        mTvAdvanced09.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void initView() {
        mTvAdvanced01 = (TextView) findViewById(R.id.tv_advanced_01);
        mTvAdvanced02 = (TextView) findViewById(R.id.tv_advanced_02);
        mTvAdvanced03 = (TextView) findViewById(R.id.tv_advanced_03);
        mTvAdvanced04 = (TextView) findViewById(R.id.tv_advanced_04);
        mTvAdvanced05 = (TextView) findViewById(R.id.tv_advanced_05);
        mTvAdvanced06 = (TextView) findViewById(R.id.tv_advanced_06);
        mTvAdvanced07 = (TextView) findViewById(R.id.tv_advanced_07);
        mTvAdvanced08 = (TextView) findViewById(R.id.tv_advanced_08);
        mTvAdvanced09 = (TextView) findViewById(R.id.tv_advanced_09);
    }
}
