package com.example.rahulkapoor.mqttservice;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.example.rahulkapoor.mqttservice.model.SendClient;
import com.example.rahulkapoor.mqttservice.service.MyService;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.Serializable;

public class Main2Activity extends AppCompatActivity implements Serializable {


    private MyService myService;
    //private MqttAndroidClient client;
    private boolean isServiceBound = false;
    private SendClient sendClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
            sendClient = (SendClient) bundle.getSerializable("classobj");
        }


        Intent service_intent = new Intent(Main2Activity.this, MyService.class);
        bindService(service_intent, serviceConnection, Context.BIND_AUTO_CREATE);


        setSub("abc", sendClient.getClient());

        setCallbacks(sendClient.getClient());

    }


    private void setCallbacks(final MqttAndroidClient client) {


        client.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(final boolean reconnect, final String serverURI) {

            }

            @Override
            public void connectionLost(final Throwable cause) {
                Log.i("service2", "lost connection");
            }

            @Override
            public void messageArrived(final String topic, final MqttMessage message) throws Exception {
                Toast.makeText(myService, new String(message.getPayload()), Toast.LENGTH_SHORT).show();
                Log.i("service2", new String(message.getPayload()));


            }

            @Override
            public void deliveryComplete(final IMqttDeliveryToken token) {

            }
        });


    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(final ComponentName name, final IBinder service) {

            MyService.MyBinder binder = (MyService.MyBinder) service;
            myService = binder.getService();
            isServiceBound = true;


        }

        @Override
        public void onServiceDisconnected(final ComponentName name) {

            isServiceBound = false;
        }
    };

    @Override
    protected void onStop() {
        super.onStop();

    }


    private void setSub(String topic, MqttAndroidClient client) {
        try {
            client.subscribe(topic, 1);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        //Intent intent = new Intent(this, MyService.class);
        //startService(intent);
        //getApplicationContext().bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
    }


}
