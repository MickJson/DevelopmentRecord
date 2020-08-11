package com.nianlun.bluetoothcs.server;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;

import com.nianlun.bluetoothcs.base.BaseBluetooth;


/**
 * 服务端监听和连接线程，只连接一个设备
 */
public class BluetoothServer extends BaseBluetooth {
    private BluetoothServerSocket mSSocket;

    public BluetoothServer(BTListener BTListener) {
        super(BTListener);
        listen();
    }

    /**
     * 监听客户端发起的连接
     */
    public void listen() {
        try {
            BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
//            mSSocket = adapter.listenUsingRfcommWithServiceRecord("BT", SPP_UUID); //加密传输，Android强制执行配对，弹窗显示配对码
            mSSocket = adapter.listenUsingInsecureRfcommWithServiceRecord("BT", SPP_UUID); //明文传输(不安全)，无需配对
            // 开启子线程
            EXECUTOR.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        BluetoothSocket socket = mSSocket.accept(); // 监听连接
                        mSSocket.close(); // 关闭监听，只连接一个设备
                        loopRead(socket); // 循环读取
                    } catch (Throwable e) {
                        close();
                    }
                }
            });
        } catch (Throwable e) {
            close();
        }
    }

    @Override
    public void close() {
        super.close();
        try {
            mSSocket.close();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}