package com.example.rahulkapoor.mqttservice.service;

import android.content.Context;
import android.widget.Toast;

import com.example.rahulkapoor.mqttservice.AppConstant.ServerConstants;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;

/**
 * Created by rahulkapoor on 25/12/17.
 */

public class MqttConnection {

    MqttAndroidClient client_1;
    final String clientId = "phoneX";
    MqttConnectOptions connectOptions;
    private static Context mContext;
    private static MqttConnection connection = new MqttConnection();

    public static MqttConnection getInstance(final Context context) {
        mContext = context;
        return connection;
    }

    public void doConnect() {

        client_1 = new MqttAndroidClient(mContext, ServerConstants.MQTTHOST_CLOUD, clientId); // MQTTHOST should be cloud broker ipaddress


    }

    public void setOptions() {

        connectOptions = new MqttConnectOptions();
        connectOptions.setUserName(ServerConstants.USERNAME);
        connectOptions.setPassword(ServerConstants.PASSWORD.toCharArray());
        connectOptions.setCleanSession(true);
        connectOptions.setAutomaticReconnect(true);

    }

    // start mqtt connectiuon with cloud when emai sync button is pressed
    public void sync() {
        //email = email_view.getText().toString();

        try {
            // connect to cloud mqtt
            client_1.connect(connectOptions, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Toast.makeText(mContext, "connected", Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Toast.makeText(mContext, "connection failed", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }


}
