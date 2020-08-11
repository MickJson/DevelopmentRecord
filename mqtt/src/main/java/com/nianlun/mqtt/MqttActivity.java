package com.nianlun.mqtt;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.concurrent.Executors;

/**
 * @author 几圈年轮
 * @Email teamfamily17@163.com
 * @description MQTT协议实现Android中的指令收发--MqttClient
 */
public class MqttActivity extends AppCompatActivity implements View.OnClickListener {

    /**
     * 开源MQTT服务地址
     */
    private String broker = "tcp://broker.hivemq.com:1883";
    /**
     * 主题订阅
     */
    private String topic = "MyTopic";
    /**
     * 客户端ID
     */
    private String clientId = "client";
    /**
     * 用户名
     */
    private String userName = "admin";
    /**
     * 密码
     */
    private String passWord = "password";

    /**
     * MQTT客户端
     */
    private MqttClient sampleClient;

    private EditText mEtMessage;
    private Button mBtnPublish;
    private TextView mTextViewReceived;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mqtt);
        initView();
        Executors.newCachedThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                initClient();
            }
        });
    }

    private void initView() {
        mEtMessage = (EditText) findViewById(R.id.et_message);
        mBtnPublish = (Button) findViewById(R.id.btn_publish);
        mTextViewReceived = (TextView) findViewById(R.id.tv_received);
        mBtnPublish.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_publish) {
            publishMsg();
        }
    }

    public void initClient() {
        try {
            MemoryPersistence persistence = new MemoryPersistence();
            // 设置唯一客户端ID
            clientId = clientId + System.currentTimeMillis();
            //设置订阅方订阅的Topic集合，遵循MQTT的订阅规则，可以是多级Topic集合
            final String topicFilter = topic;
            //服务质量，对应topicFilter
            final int qos = 0;
            //创建客户端
            sampleClient = new MqttClient(broker, clientId, persistence);
            //配置回调函数
            sampleClient.setCallback(new MqttCallbackExtended() {
                @Override
                public void connectComplete(boolean reconnect, String serverUri) {
                    setTextInfo("connectComplete: " + serverUri);
                    try {
                        //连接成功，需要上传客户端所有的订阅关系
                        sampleClient.subscribe(topicFilter, qos);
                        setTextInfo("subscribe: " + serverUri);
                    } catch (MqttException e) {
                        setTextInfo("subscribeException: " + e.getMessage());
                    }
                }

                @Override
                public void connectionLost(Throwable cause) {
                    setTextInfo("connectionLostException: " + cause.getMessage());
                }

                @Override
                public void messageArrived(String topic, MqttMessage message) {
                    setTextInfo("messageArrived:" + new String(message.getPayload()));
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
                    setTextInfo("deliveryComplete");
                }
            });
            //创建连接选择
            MqttConnectOptions connOpts = createConnectOptions(userName, passWord);
            setTextInfo("Connecting to broker: " + broker);
            //创建服务连接
            sampleClient.connect(connOpts);
        } catch (MqttException me) {
            setTextInfo("initException: " + me.getMessage());
        }
    }

    public void publishMsg() {
        String content = mEtMessage.getText().toString().trim();
        if (TextUtils.isEmpty(content)) {
            content = "Hello MQTT ";
        }
        //此处消息体需要传入byte数组
        MqttMessage message = new MqttMessage(content.getBytes());
        //设置质量级别
        message.setQos(0);
        try {
            if (sampleClient != null && sampleClient.isConnected()) {
                /*
                 * 消息发送到某个主题Topic，所有订阅这个Topic的设备都能收到这个消息。
                 * 遵循MQTT的发布订阅规范，Topic也可以是多级Topic。此处设置了发送到一级Topic。
                 */
                sampleClient.publish(topic, message);
                setTextInfo("publishMsg: " + message);
            }
        } catch (MqttException e) {
            setTextInfo(" publishException: " + e.getMessage());
        }
    }

    /**
     * 创建连接选项设置
     *
     * @param userName 用户名
     * @param passWord 密码
     */
    private MqttConnectOptions createConnectOptions(String userName, String passWord) {
        MqttConnectOptions connOpts = new MqttConnectOptions();
        connOpts.setCleanSession(true);
        connOpts.setUserName(userName);
        connOpts.setPassword(passWord.toCharArray());
        connOpts.setAutomaticReconnect(true);
        // 设置连接超时时间, 单位为秒,默认30
        connOpts.setConnectionTimeout(30);
        // 设置会话心跳时间,单位为秒,默认20
        connOpts.setKeepAliveInterval(20);
        return connOpts;
    }

    /**
     * 断开连接
     */
    public void disconnect() {
        try {
            sampleClient.disconnect();
        } catch (MqttException e) {
            setTextInfo("disconnectException: " + e.getMessage());
        }
    }

    private void setTextInfo(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mTextViewReceived.append("\n" + message);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disconnect();
    }
}
