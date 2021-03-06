package com.structit.apiclient.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.structit.apiclient.MainActivity;
import com.structit.apiclient.service.sensors.LocationSensorListener;

public class MyBroadcastReceiver extends BroadcastReceiver {
    private static final String LOG_TAG = MyBroadcastReceiver.class.getSimpleName();

    private MainActivity mActivity;
    public MyBroadcastReceiver(MainActivity activity) {
        this.mActivity = activity;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(LOG_TAG, "Intent received : " + intent.getAction());
        switch (intent.getAction()) {
            case (SensorService.ACTION_LOCATION) :
                this.mActivity.switchMusic(intent.getIntExtra("gyroZ", 0));
                break;
            case (SensorService.ACTION_LOCALISATION):
                // Compare données de localisation avec celle de la Papeterie Image Factory
                Log.i(LOG_TAG,"latitude =" + intent.getDoubleExtra("latitude",0.0) );
                Log.i(LOG_TAG,"longitude=" + intent.getDoubleExtra("longitude",0.0) );
                this.mActivity.musicPapeteries(  intent.getDoubleExtra("latitude",0.0) ,
                        intent.getDoubleExtra("longitude",0.0));
                break;
            default :  Log.w(LOG_TAG, "Unknown intent received : " + intent.getAction());
        }

        /*if(intent.getAction() == LocationSensorListener.ACTION_LOCATION) {
            Log.i(LOG_TAG, "Intent received : " + intent.getAction());
            this.mActivity.switchMusic(intent.getIntExtra("gyroZ", 0));
        } else {
            Log.w(LOG_TAG, "Unknown intent received : " + intent.getAction());
        }*/
    }
}