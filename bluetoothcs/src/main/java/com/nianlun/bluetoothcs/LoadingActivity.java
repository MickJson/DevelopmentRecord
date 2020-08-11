package com.nianlun.bluetoothcs;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import com.nianlun.bluetoothcs.client.BluetoothClientActivity;
import com.nianlun.bluetoothcs.server.BluetoothServerActivity;
import com.qw.soul.permission.SoulPermission;
import com.qw.soul.permission.bean.Permission;
import com.qw.soul.permission.bean.Permissions;
import com.qw.soul.permission.callbcak.CheckRequestPermissionsListener;

public class LoadingActivity extends AppCompatActivity implements View.OnClickListener {

    private RadioButton rbClient;
    private RadioButton rbServer;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        requestPermission();
    }

    private void requestPermission() {
        SoulPermission.getInstance().checkAndRequestPermissions(Permissions.build(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE), new CheckRequestPermissionsListener() {
            @Override
            public void onAllPermissionOk(Permission[] allPermissions) {
                assignViews();
                initData();
            }

            @Override
            public void onPermissionDenied(Permission[] refusedPermissions) {
                Toast.makeText(LoadingActivity.this, "请给与权限", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initData() {
        sharedPreferences = getSharedPreferences("BLUETOOTH", Context.MODE_PRIVATE);
        int mode = sharedPreferences.getInt("Mode", 0);
        if (mode == 0) {
            rbClient.setChecked(true);
        } else {
            rbServer.setChecked(true);
        }
    }

    private void assignViews() {
        rbClient = (RadioButton) findViewById(R.id.rb_client);
        rbServer = (RadioButton) findViewById(R.id.rb_server);
        Button btnGo = (Button) findViewById(R.id.btn_go);
        btnGo.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_go) {
            Class<?> activityClass = rbClient.isChecked() ? BluetoothClientActivity.class : BluetoothServerActivity.class;
            sharedPreferences.edit().putInt("Mode", rbClient.isChecked() ? 0 : 1).apply();
            Intent intent = new Intent(LoadingActivity.this, activityClass);
            startActivity(intent);
        }
    }
}