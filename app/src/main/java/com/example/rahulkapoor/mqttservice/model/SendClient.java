package com.example.rahulkapoor.mqttservice.model;

import org.eclipse.paho.android.service.MqttAndroidClient;

import java.io.Serializable;

/**
 * Created by rahulkapoor on 04/01/18.
 */

public class SendClient implements Serializable {


    private MqttAndroidClient mClient;

    public void setClient(final MqttAndroidClient client) {
        this.mClient = client;
    }

    public MqttAndroidClient getClient() {
        return mClient;
    }

}
