package com.nianlun.bluetoothcs.server;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;
import android.widget.Toast;

import com.nianlun.bluetoothcs.R;
import com.nianlun.bluetoothcs.base.BaseBluetooth;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;

public class BluetoothServerActivity extends AppCompatActivity implements BaseBluetooth.BTListener {

    private BluetoothServer mBluetoothServer;
    private CompositeDisposable mCompositeDisposable;
    private TextView tv_received;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blue_server);

        tv_received = findViewById(R.id.tv_msg_server);
        mCompositeDisposable = new CompositeDisposable();

        initBluetoothConnect();
    }

    private void initBluetoothConnect() {
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        if (adapter == null) {
            Toast.makeText(this, "本机没有找到蓝牙硬件或驱动", Toast.LENGTH_SHORT).show();
        } else {
            if (!adapter.isEnabled()) {
                adapter.enable();
            }
        }
        mBluetoothServer = new BluetoothServer(this);
        mCompositeDisposable.add(Observable.timer(1, TimeUnit.SECONDS)
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        mBluetoothServer.setDiscoverableTimeout();
                    }
                }));

    }

    private Handler mBluetoothHandler = new Handler(
            new Handler.Callback() {
                @Override
                public boolean handleMessage(@NonNull Message message) {
                    switch (message.what) {
                        case BaseBluetooth.BTListener.CONNECTED:
                            tv_received.setText("蓝牙连接成功");
                            break;
                        case BaseBluetooth.BTListener.DISCONNECTED:
                            tv_received.setText("蓝牙连接断开");
                            mBluetoothServer.listen();
                            break;
                        case BaseBluetooth.BTListener.MSG_RECEIVED:
                            String msg = message.obj.toString();
                            tv_received.setText("蓝牙消息：" + msg);
                            mBluetoothServer.sendMsg("服务端反馈：" + msg);
                            break;
                        default:
                    }

                    return false;
                }
            }
    );

    @Override
    public void socketNotify(int state, Object obj) {
        Message message = Message.obtain();
        message.what = state;
        message.obj = obj;
        mBluetoothHandler.sendMessage(message);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBluetoothServer != null) {
            mBluetoothServer.close();
        }
    }
}