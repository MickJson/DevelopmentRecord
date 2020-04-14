package com.nianlun.floatingwindow;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

/**
 * @author 几圈年轮
 * @Email teamfamily17@163.com
 * @description 悬浮窗使用
 */
public class FloatingWindowActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int REQUEST_OVERLAY_CODE = 1;
    private FloatingWindow mFloatingWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_floating_window);
        initView();
    }

    /**
     * 判断是否开启悬浮窗口权限，否则，跳转开启页
     */
    public boolean requestOverlayPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, REQUEST_OVERLAY_CODE);
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    private void initView() {
        Button mBtnShowFloatingWindow = (Button) findViewById(R.id.btn_show_floating_window);
        mBtnShowFloatingWindow.setOnClickListener(this);
        Button mBtnDismissFloatingWindow = (Button) findViewById(R.id.btn_dismiss_floating_window);
        mBtnDismissFloatingWindow.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_show_floating_window) {
            if (!requestOverlayPermission()) {
                showFloatingWindow();
            } else {
                Toast.makeText(this, "请开启悬浮窗权限", Toast.LENGTH_SHORT).show();
            }
        } else if (v.getId() == R.id.btn_dismiss_floating_window) {
            if (mFloatingWindow != null) {
                mFloatingWindow.dismiss();
            }
        }
    }

    private void showFloatingWindow() {
        mFloatingWindow = new FloatingWindow();
        View view = initFloatView();
        mFloatingWindow.showFloatingWindowView(this, view);
    }

    private View initFloatView() {
        View view = View.inflate(this, R.layout.view_floating_window, null);
        // 设置视频封面
        final ImageView mThumb = (ImageView) view.findViewById(R.id.thumb_floating_view);
        Glide.with(this).load(R.drawable.thumb).into(mThumb);
        // 悬浮窗关闭
        view.findViewById(R.id.close_floating_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFloatingWindow.dismiss();
            }
        });
        // 返回前台页面
        view.findViewById(R.id.back_floating_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFloatingWindow.setTopApp(FloatingWindowActivity.this);
            }
        });
        final VideoView videoView = view.findViewById(R.id.video_view);
        //视频内容设置
        videoView.setVideoPath("https://stream7.iqilu.com/10339/article/202002/18/2fca1c77730e54c7b500573c2437003f.mp4");
        // 视频准备完毕，隐藏正在加载封面，显示视频
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mThumb.setVisibility(View.GONE);
            }
        });
        // 循环播放
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                videoView.start();
            }
        });
        // 开始播放视频
        videoView.start();
        return view;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_OVERLAY_CODE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (Settings.canDrawOverlays(FloatingWindowActivity.this)) {
                    showFloatingWindow();
                } else {
                    Toast.makeText(this, "获取悬浮窗权限失败", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
