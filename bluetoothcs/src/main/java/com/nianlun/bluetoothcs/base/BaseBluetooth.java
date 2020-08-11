package com.nianlun.bluetoothcs.base;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.lang.reflect.Method;
import java.util.UUID;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class BaseBluetooth {
    public static final Executor EXECUTOR = Executors.newSingleThreadExecutor();
    protected static final UUID SPP_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"); //自定义
    private static final String FILE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/bluetooth/";
    private static final int FLAG_MSG = 0;  //消息标记
    private static final int FLAG_FILE = 1; //文件标记

    private BluetoothSocket mSocket;
    private DataOutputStream mOut;
    private BTListener mBTListener;
    private boolean isRead;
    private boolean isSending;

    public BaseBluetooth(BTListener BTListener) {
        mBTListener = BTListener;
    }

    /**
     * 循环读取对方数据(若没有数据，则阻塞等待)
     */
    public void loopRead(BluetoothSocket socket) {
        mSocket = socket;
        try {
            if (!mSocket.isConnected())
                mSocket.connect();
            notifyUI(BTListener.CONNECTED, mSocket.getRemoteDevice());
            mOut = new DataOutputStream(mSocket.getOutputStream());
            DataInputStream in = new DataInputStream(mSocket.getInputStream());
            isRead = true;
            while (isRead) { //循环读取
                switch (in.readInt()) {
                    case FLAG_MSG: //读取短消息
                        String msg = in.readUTF();
                        notifyUI(BTListener.MSG_RECEIVED, msg);
                        break;
                    case FLAG_FILE: //读取文件
                        File file = new File(FILE_PATH);
                        if (!file.exists()) {
                            file.mkdirs();
                        }
                        String fileName = in.readUTF(); //文件名
                        long fileLen = in.readLong(); //文件长度
                        notifyUI(BTListener.MSG_RECEIVED, "正在接收文件(" + fileName + ")····················");
                        // 读取文件内容
                        long len = 0;
                        int r;
                        byte[] b = new byte[4 * 1024];
                        FileOutputStream out = new FileOutputStream(FILE_PATH + fileName);
                        while ((r = in.read(b)) != -1) {
                            out.write(b, 0, r);
                            len += r;
                            if (len >= fileLen)
                                break;
                        }
                        notifyUI(BTListener.MSG_RECEIVED, "文件接收完成(存放在:" + FILE_PATH + ")");
                        break;
                }
            }
        } catch (Throwable e) {
            close();
        }
    }

    /**
     * 发送短消息
     */
    public void sendMsg(String msg) {
        if (isSending || TextUtils.isEmpty(msg))
            return;
        isSending = true;
        try {
            mOut.writeInt(FLAG_MSG); //消息标记
            mOut.writeUTF(msg);
        } catch (Throwable e) {
            close();
        }
        notifyUI(BTListener.MSG_SEND, "发送短消息：" + msg);
        isSending = false;
    }

    /**
     * 发送文件
     */
    public void sendFile(final String filePath) {
        if (isSending || TextUtils.isEmpty(filePath))
            return;
        isSending = true;
        EXECUTOR.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    notifyUI(BTListener.MSG_SEND, "正在发送文件(" + filePath + ")····················");
                    FileInputStream in = new FileInputStream(filePath);
                    File file = new File(filePath);
                    mOut.writeInt(FLAG_FILE); //文件标记
                    mOut.writeUTF(file.getName()); //文件名
                    mOut.writeLong(file.length()); //文件长度
                    int r;
                    byte[] b = new byte[4 * 1024];
                    while ((r = in.read(b)) != -1) {
                        mOut.write(b, 0, r);
                    }
                    notifyUI(BTListener.MSG_SEND, "文件发送完成.");
                } catch (Throwable e) {
                    close();
                }
                isSending = false;
            }
        });
    }

    /**
     * 关闭Socket连接
     */
    public void close() {
        try {
            isRead = false;
            if (mSocket != null) {
                mSocket.close();
                notifyUI(BTListener.DISCONNECTED, null);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    /**
     * 当前设备与指定设备是否连接
     */
    public boolean isConnected(BluetoothDevice dev) {
        boolean connected = (mSocket != null && mSocket.isConnected());
        if (dev == null)
            return connected;
        return connected && mSocket.getRemoteDevice().equals(dev);
    }

    private void notifyUI(final int state, final Object obj) {
        mBTListener.socketNotify(state, obj);
    }

    public interface BTListener {
        int DISCONNECTED = 0;
        int CONNECTED = 1;
        int MSG_SEND = 2;
        int MSG_RECEIVED = 3;

        void socketNotify(int state, Object obj);
    }

    public void setDiscoverableTimeout() {
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        try {
            Method setDiscoverableTimeout = BluetoothAdapter.class.getMethod("setDiscoverableTimeout", int.class);
            setDiscoverableTimeout.setAccessible(true);
            Method setScanMode = BluetoothAdapter.class.getMethod("setScanMode", int.class, int.class);
            setScanMode.setAccessible(true);
            setDiscoverableTimeout.invoke(adapter, 0);
            setScanMode.invoke(adapter, BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE, 0);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Bluetooth", "setDiscoverableTimeout failure:" + e.getMessage());
        }
    }

    public void closeDiscoverableTimeout() {
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        try {
            Method setDiscoverableTimeout = BluetoothAdapter.class.getMethod("setDiscoverableTimeout", int.class);
            setDiscoverableTimeout.setAccessible(true);
            Method setScanMode = BluetoothAdapter.class.getMethod("setScanMode", int.class, int.class);
            setScanMode.setAccessible(true);
            setDiscoverableTimeout.invoke(adapter, 1);
            setScanMode.invoke(adapter, BluetoothAdapter.SCAN_MODE_CONNECTABLE, 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}