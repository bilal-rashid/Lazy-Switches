package com.menthoven.arduinoandroid.Recievers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.menthoven.arduinoandroid.BluetoothActivity;
import com.menthoven.arduinoandroid.Constants;

/**
 * Created by Bilal Rashid on 5/21/2017.
 */

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context k1, Intent k2) {
        // TODO Auto-generated method stub
        Log.d("Alarm","Recieved");

//        Toast.makeText(k1, "Alarm received!", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(k1, BluetoothActivity.class);

        intent.putExtra(Constants.EXTRA_DEVICE, k2.getExtras().getParcelable(Constants.EXTRA_DEVICE));
        intent.putExtra(Constants.STATE_DEVICE,k2.getStringExtra(Constants.STATE_DEVICE));

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        k1.startActivity(intent);

    }

}