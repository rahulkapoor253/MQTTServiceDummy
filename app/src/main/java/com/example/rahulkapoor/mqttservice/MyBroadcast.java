package com.example.rahulkapoor.mqttservice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.rahulkapoor.mqttservice.model.SendClient;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * Created by rahulkapoor on 05/01/18.
 */

public class MyBroadcast extends BroadcastReceiver {

    private SendClient sendClient;

    @Override
    public void onReceive(final Context context, final Intent intent) {


        sendClient = (SendClient) intent.getSerializableExtra("data");

        sendClient.getClient().setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(final boolean reconnect, final String serverURI) {

            }

            @Override
            public void connectionLost(final Throwable cause) {

            }

            @Override
            public void messageArrived(final String topic, final MqttMessage message) throws Exception {

            }

            @Override
            public void deliveryComplete(final IMqttDeliveryToken token) {

            }
        });

    }
}
