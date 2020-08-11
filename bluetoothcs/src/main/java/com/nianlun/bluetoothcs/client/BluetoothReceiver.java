package com.nianlun.bluetoothcs.client;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;


/**
 * 监听蓝牙广播-各种状态
 */
public class BluetoothReceiver extends BroadcastReceiver {
    private static final String TAG = BluetoothReceiver.class.getSimpleName();
    private final OnBluetoothReceiverListener mOnBluetoothReceiverListener;

    public BluetoothReceiver(Context context,OnBluetoothReceiverListener onBluetoothReceiverListener) {
        mOnBluetoothReceiverListener = onBluetoothReceiverListener;
        context.registerReceiver(this,getBluetoothIntentFilter());
    }

    private IntentFilter getBluetoothIntentFilter() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);//蓝牙开关状态
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);//蓝牙开始搜索
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);//蓝牙搜索结束
        filter.addAction(BluetoothDevice.ACTION_FOUND);//蓝牙发现新设备(未配对的设备)
        return filter;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action == null) {
            return;
        }
        Log.i(TAG, "===" + action);
        BluetoothDevice dev = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
        if (dev != null) {
            Log.i(TAG, "BluetoothDevice: " + dev.getName() + ", " + dev.getAddress());
        }
        switch (action) {
            case BluetoothAdapter.ACTION_STATE_CHANGED:
                int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, 0);
                Log.i(TAG, "STATE: " + state);
                if (mOnBluetoothReceiverListener != null) {
                    mOnBluetoothReceiverListener.onStateChanged(state);
                }
                break;
            case BluetoothAdapter.ACTION_DISCOVERY_STARTED:
                Log.i(TAG, "ACTION_DISCOVERY_STARTED ");
                break;
            case BluetoothAdapter.ACTION_DISCOVERY_FINISHED:
                if (mOnBluetoothReceiverListener != null) {
                    mOnBluetoothReceiverListener.onScanFinish();
                }
                break;
            case BluetoothDevice.ACTION_FOUND:
                short rssi = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI, Short.MAX_VALUE);
                Log.i(TAG, "EXTRA_RSSI:" + rssi);
                if (mOnBluetoothReceiverListener != null) {
                    mOnBluetoothReceiverListener.onDeviceFound(dev);
                }
                break;
            default:
                break;
        }
    }

    public interface OnBluetoothReceiverListener {
        /**
         * 发现设备
         *
         * @param bluetoothDevice 蓝牙设备
         */
        void onDeviceFound(BluetoothDevice bluetoothDevice);

        /**
         * 扫描结束
         */
        void onScanFinish();

        /**
         * 蓝牙开启关闭状态
         *
         * @param state 状态
         */
        void onStateChanged(int state);
    }
}