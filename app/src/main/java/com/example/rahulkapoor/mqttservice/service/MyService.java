package com.example.rahulkapoor.mqttservice.service;

import android.app.AlertDialog;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.example.rahulkapoor.mqttservice.AppConstant.ServerConstants;
import com.example.rahulkapoor.mqttservice.Callback;
import com.example.rahulkapoor.mqttservice.model.SendClient;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.io.Serializable;


/**
 * Created by rahulkapoor on 25/12/17.
 */

public class MyService extends Service implements Serializable {

    public MqttAndroidClient client_1;
    MqttConnectOptions connectOptions;
    final String clientId = "phoneX";
    private IBinder binder = new MyBinder();
    private Callback mCallback;
    private Context mContext;
    private BroadcastReceiver broadcastReceiver;
    private LocalBroadcastManager localBroadcastManager;

//    public MyService() {
//
//    }

//    public MyService(final Context context, final Callback callback) {
//        this.mContext = context;
//        this.mCallback = callback;
//    }


    @Nullable
    @Override
    public IBinder onBind(final Intent intent) {
        Toast.makeText(this, "activity binded with service", Toast.LENGTH_SHORT).show();
        return binder;
    }

    @Override
    public boolean onUnbind(final Intent intent) {
        return super.onUnbind(intent);

    }

    @Override
    public void onRebind(final Intent intent) {
        super.onRebind(intent);
    }


    @Override
    public int onStartCommand(final Intent intent, final int flags, final int startId) {


//        MqttConnection.getInstance(getApplicationContext()).doConnect();
//        MqttConnection.getInstance(getApplicationContext()).setOptions();
//        MqttConnection.getInstance(getApplicationContext()).sync();
        client_1 = new MqttAndroidClient(getApplicationContext(), ServerConstants.MQTTHOST_LOCAL, clientId);
        connectOptions = new MqttConnectOptions();
        connectOptions.setUserName(ServerConstants.USERNAME);
        connectOptions.setPassword(ServerConstants.PASSWORD.toCharArray());
        connectOptions.setCleanSession(true);
        connectOptions.setAutomaticReconnect(true);


        mqttConnect();

//        mCallback.getReference(client_1);
//        Log.i("callback", mCallback.toString());

//        SendClient sendClient = new SendClient();
//        sendClient.setClient(client_1);

//        SendClient sendClient = ((SendClient) getApplicationContext());
//        sendClient.setClient(client_1);
//        Log.i("client", client_1.getClientId());


        broadcastClient();


        return Service.START_STICKY;

    }

    private void broadcastClient() {
        //broadcastReceiver = new MyBroadcastReceiver();
        //broadcast client object on subject = mqttclient;
        Intent i = new Intent("broadcast");
        SendClient sendClient = new SendClient();
        sendClient.setClient(client_1);
        i.putExtra("data", sendClient);
//        i.addCategory(Intent.CATEGORY_DEFAULT);
        //i.setAction("com.example.Pass");
        //make use of local broadcast manager to limit the broadcast to this app only;
        LocalBroadcastManager.getInstance(this).sendBroadcast(i);
    }


    private void mqttConnect() {

        try {
            client_1.connect(connectOptions, null, new IMqttActionListener() {
                @Override
                public void onSuccess(final IMqttToken asyncActionToken) {
                    Toast.makeText(MyService.this, "Success", Toast.LENGTH_LONG).show();
                    Log.i("service", "success");
                }

                @Override
                public void onFailure(final IMqttToken asyncActionToken, final Throwable exception) {
                    Toast.makeText(MyService.this, "Failure", Toast.LENGTH_LONG).show();
                    Log.i("service", "failure");
                    //callAlertDialog();
                }


            });
        } catch (MqttException e) {
            e.printStackTrace();
        }

    }


    public void getCallback(final Callback callback) {

        this.mCallback = callback;

        mCallback.getReference(client_1);


    }


    private void callAlertDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setCancelable(false);
        builder.setMessage("Try Connecting again...");
        builder.setPositiveButton("Connect", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, final int which) {

                //try to reconnect till it gets success;
                mqttConnect();
            }
        });


    }

    /**
     * it will return an instance of MyServie;
     */
    public class MyBinder extends Binder {
        public MyService getService() {
            return MyService.this;
        }
    }


}
