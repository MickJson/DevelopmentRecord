package com.nianlun.mqtt;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.DisconnectedBufferOptions;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * @author 几圈年轮
 * @Email teamfamily17@163.com
 * @description MQTT协议实现Android中的指令收发--MqttAndroidClient
 */
public class MqttAndroidActivity extends AppCompatActivity {

    private MqttAndroidClient mqttAndroidClient;

    private String serverUri = "tcp://broker.hivemq.com:1883";

    private String clientId = "client";
    private String subscriptionTopic = "MyTopic";

    private TextView mTv;
    private EditText mEt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mqtt);

        mTv = findViewById(R.id.tv_received);

        mEt = findViewById(R.id.et_message);

        findViewById(R.id.btn_publish).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                publishMessage();
            }
        });

        initClient();

    }

    private void initClient() {
        // 设置唯一客户端ID
        clientId = clientId + System.currentTimeMillis();

        mqttAndroidClient = new MqttAndroidClient(getApplicationContext(), serverUri, clientId);
        mqttAndroidClient.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean reconnect, String serverUri) {
                if (reconnect) {
                    addToHistory("Reconnected to : " + serverUri);
                    subscribeToTopic();
                } else {
                    addToHistory("Connected to: " + serverUri);
                }
            }

            @Override
            public void connectionLost(Throwable cause) {
                addToHistory("The Connection was lost.");
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                addToHistory("messageArrived: " + new String(message.getPayload()));
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {
                addToHistory("deliveryComplete.");
            }
        });

        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setAutomaticReconnect(true);
        mqttConnectOptions.setCleanSession(false);

        try {
            addToHistory("Connecting to " + serverUri);
            mqttAndroidClient.connect(mqttConnectOptions, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    DisconnectedBufferOptions disconnectedBufferOptions = new DisconnectedBufferOptions();
                    disconnectedBufferOptions.setBufferEnabled(true);
                    disconnectedBufferOptions.setBufferSize(100);
                    disconnectedBufferOptions.setPersistBuffer(false);
                    disconnectedBufferOptions.setDeleteOldestMessages(false);
                    mqttAndroidClient.setBufferOpts(disconnectedBufferOptions);
                    subscribeToTopic();
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    addToHistory("Failed to connect to: " + serverUri);
                }
            });

        } catch (MqttException ex) {
            addToHistory("Exception while connecting");
        }
    }

    /**
     * 订阅主题
     */
    public void subscribeToTopic() {
        try {
            mqttAndroidClient.subscribe(subscriptionTopic, 0, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    addToHistory("Subscribed!");
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    addToHistory("Failed to subscribe");
                }
            });

            mqttAndroidClient.subscribe(subscriptionTopic, 0, new IMqttMessageListener() {
                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    addToHistory("Message: " + topic + " : " + new String(message.getPayload()));
                }
            });

        } catch (MqttException ex) {
            addToHistory("Exception while subscribing");
        }
    }

    public void publishMessage() {
        try {
            MqttMessage message = new MqttMessage();
            message.setQos(0);
            String content = mEt.getText().toString().trim();
            if (TextUtils.isEmpty(content)) {
                content = "Hello MQTT";
            }
            message.setPayload(content.getBytes());
            if (mqttAndroidClient != null && mqttAndroidClient.isConnected()) {
                mqttAndroidClient.publish(subscriptionTopic, message);
                addToHistory("Message Published");
            }
        } catch (MqttException e) {
            addToHistory("Error Publishing: " + e.getMessage());
        }
    }


    private void addToHistory(final String mainText) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mTv.append("\n" + mainText);
            }
        });
    }

}