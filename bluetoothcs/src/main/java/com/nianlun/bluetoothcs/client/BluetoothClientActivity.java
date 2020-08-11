package com.nianlun.bluetoothcs.client;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.nianlun.bluetoothcs.R;
import com.nianlun.bluetoothcs.base.BaseBluetooth;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;

public class BluetoothClientActivity extends AppCompatActivity implements BluetoothReceiver.OnBluetoothReceiverListener, View.OnClickListener, BaseBluetooth.BTListener {

    private TextView tvTitle, tvMsg;
    private RecyclerView rvDevicesBluetooth;
    private LinearLayout llConnectedView;
    private EditText etMessage;
    private ProgressBar pbConnecting;

    private BluetoothDeviceAdapter mBluetoothDeviceAdapter;
    private BluetoothReceiver mBluetoothReceiver;
    private BluetoothAdapter mBluetoothAdapter;
    private CompositeDisposable mCompositeDisposable;
    private BluetoothClient mBluetoothClient;
    private BluetoothDevice mBluetoothDevice;
    private StringBuilder mStringBuilder;
    private boolean isConnecting = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_client);
        initView();
        initData();
        initListener();
        scanDevice();
    }

    public void scanDevice() {
        // 判断设备是否支持蓝牙
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "蓝牙异常", Toast.LENGTH_SHORT).show();
            return;
        }
        //判断蓝牙是否开启
        if (!mBluetoothAdapter.isEnabled()) {
            //开启蓝牙，监听广播进行后续操作
            mBluetoothAdapter.enable();
        } else {
            // 开始蓝牙扫描
            tvTitle.setEnabled(false);
            mBluetoothAdapter.startDiscovery();
        }
    }

    private void initListener() {
        mBluetoothDeviceAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                isConnecting = true;
                mBluetoothDeviceAdapter.setSelectPosition(position);
                pbConnecting.setVisibility(View.VISIBLE);
                rvDevicesBluetooth.setVisibility(View.GONE);
                tvTitle.setText(mBluetoothDevice.getName() + ":" + mBluetoothDevice.getAddress() + "  正在连接");
                tvTitle.setEnabled(false);

                mBluetoothDevice = (BluetoothDevice) adapter.getItem(position);
                // 取消扫描，进行连接
                mBluetoothAdapter.cancelDiscovery();
                mBluetoothClient.connect(mBluetoothDevice);
            }
        });
        tvTitle.setOnClickListener(this);
    }

    private void initData() {
        mStringBuilder = new StringBuilder();
        mBluetoothClient = new BluetoothClient(this);
        mBluetoothReceiver = new BluetoothReceiver(this, this);
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        mBluetoothDeviceAdapter = new BluetoothDeviceAdapter();
        rvDevicesBluetooth.setAdapter(mBluetoothDeviceAdapter);
    }

    private void initView() {
        tvTitle = findViewById(R.id.tv_title_client);
        tvTitle.setOnClickListener(this);
        rvDevicesBluetooth = findViewById(R.id.rv_devices_client);
        rvDevicesBluetooth.setLayoutManager(new LinearLayoutManager(this));
        llConnectedView = findViewById(R.id.ll_connected_client);
        etMessage = findViewById(R.id.et_client);
        findViewById(R.id.btn_send_client).setOnClickListener(this);
        tvMsg = findViewById(R.id.tv_message_client);
        pbConnecting = findViewById(R.id.pb_connecting);
    }

    private Handler mBluetoothHandler = new Handler(
            new Handler.Callback() {
                @Override
                public boolean handleMessage(@NonNull Message msg) {
                    switch (msg.what) {
                        case BaseBluetooth.BTListener.CONNECTED:
                            //设备连接上
                            tvTitle.setText(mBluetoothDevice.getName() + ":" + mBluetoothDevice.getAddress() + "  连接成功");
                            llConnectedView.setVisibility(View.VISIBLE);
                            pbConnecting.setVisibility(View.GONE);
                            break;
                        case BaseBluetooth.BTListener.DISCONNECTED:
                            //设备断开
                            pbConnecting.setVisibility(View.GONE);
                            tvTitle.setEnabled(false);
                            rvDevicesBluetooth.setVisibility(View.VISIBLE);
                            break;
                        case BaseBluetooth.BTListener.MSG_RECEIVED:
                            //接收到消息
                            String message = msg.obj.toString();
                            mStringBuilder.append(message + "\n");
                            tvMsg.setText(mStringBuilder.toString());
                            break;
                        case BaseBluetooth.BTListener.MSG_SEND:
                            break;
                        default:
                            break;
                    }
                    return false;
                }
            }
    );

    @Override
    public void onDeviceFound(final BluetoothDevice bluetoothDevice) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //扫描到设备
                if (!mBluetoothDeviceAdapter.getData().contains(bluetoothDevice)) {
                    mBluetoothDeviceAdapter.addData(0, bluetoothDevice);
                    rvDevicesBluetooth.smoothScrollToPosition(0);
                }
            }
        });
    }

    @Override
    public void onScanFinish() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //扫描结束
                if (!isConnecting) {
                    tvTitle.setEnabled(true);
                    tvTitle.setText("扫描结束，点击重新扫描");
                }
            }
        });
    }

    @Override
    public void onStateChanged(int state) {
        // 蓝牙打开，关闭状态改变
        if (state == BluetoothAdapter.STATE_ON) {
            tvTitle.setEnabled(false);
            if (mCompositeDisposable == null) {
                mCompositeDisposable = new CompositeDisposable();
            }
            mCompositeDisposable.add(Observable.timer(1, TimeUnit.SECONDS)
                    .subscribe(new Consumer<Long>() {
                        @Override
                        public void accept(Long aLong) throws Exception {
                            mBluetoothAdapter.startDiscovery();
                        }
                    }));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_title_client:
                mBluetoothDeviceAdapter.setNewData(new ArrayList<BluetoothDevice>());
                tvTitle.setEnabled(false);
                mBluetoothAdapter.startDiscovery();
                break;
            case R.id.btn_send_client:
                mBluetoothClient.sendMsg(etMessage.getText().toString());
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //关闭蓝牙连接
        if (mBluetoothClient != null) {
            mBluetoothClient.close();
        }
        // 注销广播
        unregisterReceiver(mBluetoothReceiver);
    }

    @Override
    public void socketNotify(int state, Object obj) {
        // 蓝牙状态消息通知
        Message msg = Message.obtain();
        msg.what = state;
        msg.obj = obj;
        mBluetoothHandler.sendMessage(msg);
    }
}
