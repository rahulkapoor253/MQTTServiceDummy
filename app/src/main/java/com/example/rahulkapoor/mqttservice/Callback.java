package com.example.rahulkapoor.mqttservice;

import org.eclipse.paho.android.service.MqttAndroidClient;

/**
 * Created by rahulkapoor on 03/01/18.
 */

public interface Callback {

    void getReference(final MqttAndroidClient client_1);

}
