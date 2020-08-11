package com.nianlun.bluetoothcs.client;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import com.nianlun.bluetoothcs.base.BaseBluetooth;

/**
 * 客户端，与服务端建立长连接
 */
public class BluetoothClient extends BaseBluetooth {

    public BluetoothClient(BTListener BTListener) {
        super(BTListener);
    }

    /**
     * 与远端设备建立长连接
     *
     * @param bluetoothDevice 远端设备
     */
    public void connect(BluetoothDevice bluetoothDevice) {
        close();
        try {
//             final BluetoothSocket socket = bluetoothDevice.createRfcommSocketToServiceRecord(SPP_UUID); //加密传输，Android强制执行配对，弹窗显示配对码
            final BluetoothSocket socket = bluetoothDevice.createInsecureRfcommSocketToServiceRecord(SPP_UUID); //明文传输(不安全)，无需配对
            // 开启子线程（必须在新线程中进行连接操作）
            EXECUTOR.execute(new Runnable() {
                @Override
                public void run() {
                    //连接，并进行循环读取
                    loopRead(socket);
                }
            });
        } catch (Throwable e) {
            close();
        }
    }
}