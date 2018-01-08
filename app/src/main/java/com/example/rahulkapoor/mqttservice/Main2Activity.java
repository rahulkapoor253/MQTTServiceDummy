package com.example.rahulkapoor.mqttservice;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
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
    private MqttAndroidClient client;
    //private Broadcast2 myBroadcast2;
    private BroadcastReceiver mMessageReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);


        //setCallbacks(sendClient.getClient());

    }

    @Override
    protected void onResume() {
        super.onResume();
        // IntentFilter intentFilter = new IntentFilter("com.example.pass");
        // IntentFilter intentFilter = new IntentFilter("com.example.Broadcast");
        //myBroadcast2 = new Broadcast2();

         final IntentFilter intentFilter = new IntentFilter("broadcast2");

//binding the activity to the service class;
        Intent service_intent = new Intent(Main2Activity.this, MyService.class);
        bindService(service_intent, serviceConnection, Context.BIND_AUTO_CREATE);

        mMessageReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(final Context context, final Intent intent) {


                sendClient = (SendClient) intent.getSerializableExtra("data2");

                setSub("abc", client);

                client.setCallback(new MqttCallbackExtended() {
                    @Override
                    public void connectComplete(final boolean reconnect, final String serverURI) {
                        Toast.makeText(context, "connectComplete", Toast.LENGTH_LONG).show();

                        //setSub("abcd", client);
                    }

                    @Override
                    public void connectionLost(final Throwable cause) {

                        Log.i("service1", "connection lost");
                    }

                    @Override
                    public void messageArrived(final String topic, final MqttMessage message) throws Exception {
                        Log.i("message", new String(message.getPayload()));

                    }

                    @Override
                    public void deliveryComplete(final IMqttDeliveryToken token) {

                    }
                });

                LocalBroadcastManager.getInstance(Main2Activity.this).registerReceiver(mMessageReceiver, intentFilter);


            }

        };


//        SendClient sendClient = ((SendClient) getApplicationContext());
//        sendClient.getClient().setCallback(new MqttCallbackExtended() {
//            @Override
//            public void connectComplete(final boolean reconnect, final String serverURI) {
//                Log.i("hie", "");
//            }
//
//            @Override
//            public void connectionLost(final Throwable cause) {
//
//            }
//
//            @Override
//            public void messageArrived(final String topic, final MqttMessage message) throws Exception {
//
//            }
//
//            @Override
//            public void deliveryComplete(final IMqttDeliveryToken token) {
//
//            }
//        });

//        myBroadcast = new MyBroadcast();
//        LocalBroadcastManager.getInstance(this).registerReceiver(myBroadcast, intentFilter);

        //this.registerReceiver(mMessageReceiver, intentFilter);


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
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        //this.unregisterReceiver(mMessageReceiver);
    }

    @Override
    protected void onStop() {
        super.onStop();

        //unregisterReceiver(mMessageReceiver);

    }


    private void setSub(String topic, MqttAndroidClient client) {
        try {
            client.subscribe(topic, 1);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }


}
