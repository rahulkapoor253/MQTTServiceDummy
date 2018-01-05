package com.example.rahulkapoor.mqttservice;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rahulkapoor.mqttservice.model.SendClient;
import com.example.rahulkapoor.mqttservice.service.MyService;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.Serializable;

public class MainActivity extends Activity implements Serializable {

    private TextView tvDisplay;
    private Button btnSync, btnNext;
    private MyService myService;
    private SendClient sendClient;
    private boolean isServiceBound = false;
    private MqttAndroidClient client;
    MyBroadcast myBroadcast;
    //private BroadcastReceiver mMessageReceiver;

    // private ServiceConnection serviceConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //mMessageReceiver = new MyBroadcastReceiver();

        tvDisplay = (TextView) findViewById(R.id.tv_display);
        btnSync = (Button) findViewById(R.id.btn_sync);
        btnNext = (Button) findViewById(R.id.btn_next);

        //connectService();
        //myService = new MyService(this, this);
        Intent service_intent = new Intent(MainActivity.this, MyService.class);
        bindService(service_intent, serviceConnection, Context.BIND_AUTO_CREATE);
        startService(service_intent);


//        myService.getCallback(this);
//        LocalBroadcastManager.getInstance(this).registerReceiver(
//                mMessageReceiver, new IntentFilter("mqttClient"));

        //Toast.makeText(MainActivity.this, MACAddress.getInstance(getApplicationContext()).getMACUpdated(getApplicationContext()), Toast.LENGTH_LONG).show();


        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Intent i = new Intent(MainActivity.this, Main2Activity.class);
                //client.setCallback(null);
                //i.putExtra("data", (Serializable) sendClient);
                startActivity(i);
            }
        });


    }


    private void setSub(String topic, MqttAndroidClient client) {
        try {
            client.subscribe(topic, 1);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }


//    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(final Context context, final Intent intent) {
//
//
//            sendClient = (SendClient) intent.getSerializableExtra("data");
//            client = sendClient.getClient();
//
//            sendClient.getClient().setCallback(new MqttCallbackExtended() {
//                @Override
//                public void connectComplete(final boolean reconnect, final String serverURI) {
//                    Toast.makeText(context, "connectComplete", Toast.LENGTH_LONG).show();
//
//                    setSub("abcd", sendClient.getClient());
//                }
//
//                @Override
//                public void connectionLost(final Throwable cause) {
//
//                    Log.i("service1", "connection lost");
//                }
//
//                @Override
//                public void messageArrived(final String topic, final MqttMessage message) throws Exception {
//                    Log.i("message", new String(message.getPayload()));
//
//                }
//
//                @Override
//                public void deliveryComplete(final IMqttDeliveryToken token) {
//
//                }
//            });
//
//
//        }
//    };

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    protected void onPause() {
        super.onPause();
        //this.unregisterReceiver(mMessageReceiver);
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
    protected void onResume() {
        super.onResume();


        IntentFilter intentFilter = new IntentFilter("com.example.broadcast");
        myBroadcast = new MyBroadcast();
        LocalBroadcastManager.getInstance(this).registerReceiver(myBroadcast, intentFilter);

        //this.registerReceiver(mMessageReceiver, intentFilter);
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, final Intent intent) {

            sendClient = (SendClient) intent.getSerializableExtra("data");
            client = (MqttAndroidClient) sendClient.getClient();

            client.setCallback(new MqttCallbackExtended() {
                @Override
                public void connectComplete(final boolean reconnect, final String serverURI) {
                    Toast.makeText(context, "connectComplete", Toast.LENGTH_LONG).show();

                    setSub("abcd", client);
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


        }
    };

    @Override
    protected void onStop() {
        super.onStop();
        if (isServiceBound) {
            unbindService(serviceConnection);
            isServiceBound = false;
        }

        LocalBroadcastManager.getInstance(this).unregisterReceiver(myBroadcast);
        //this.unregisterReceiver(mMessageReceiver);

    }
}

